package com.hughie.linkgame.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hughie.linkgame.R;
import com.hughie.linkgame.common.HughieGameApplication;
import com.hughie.linkgame.common.HughieSPManager;
import com.hughie.linkgame.ui.HughieGameMainActivity;
import com.hughie.linkgame.widget.HughieGameSurfaceView;
import com.hughie.linkgame.widget.HughieGameSurfaceView.OnGameSurfaceItemClickListener;

/**
 * 游戏控制器工具类
 * @ClassName: HughieGameController
 * @author hughiezhang
 * @since 2015-10-19 09:50
 */
public class HughieGameController {
	public HughieGameApplication mGameApplication;						 
	public HughieGameMainActivity mGameMainActivity;					//游戏的主activity
	
	private HughieGameSurfaceView mGameSurfaceView;					//自定义的游戏视图类，用来呈现游戏界面
	
	private int[][] mGameSurfaceMap;												// 用来保存程序布局的数组，之前在HughieGameSurfaceView中的
																									// map只是对这个数组的一个引用，控制器中将为之分配空间，生成初始化布局。
	
	private List<Point> mGameIconConnectedPathList = new ArrayList<Point>();		// 保存连线的起始点和折点
	
	/*
	 * 刷新界面包含两部分：1.实现连线消除动画，对HughieGameSurfaceView进行延时刷新；2.更新游戏剩余时间进行页面刷新
	 * 2中需要用到一个Timer，而Timer会引起另起一个线程，而刷新界面需要在UI线程中执行，所以程序需要用到Handler方法来刷新
	 * 1,2我们通过对一个定义好的Handler发送不同的消息进行不同的刷新操作
	 */
	private GameSurfaceRefreshHandler mRefreshHandler = new GameSurfaceRefreshHandler();
	
	// 对于游戏的计时器和帮助次数的更新，我们通过一个TimerTask来实现
	private static final long mTotalGameTime = 120;						// 游戏进度总时间
	private long mRemainGameTime;												// 游戏剩余时间
	private long mStartGameTime;													// 游戏的开始时间
	private long mFreezeGameTime;												// 游戏冻结时间
	private Timer mGameSurfaceTimer;											// 游戏时间计时器
	
	public int mGameLevel = 1;														// 游戏关卡数
	public int mGameMaxLevel = 1;													// 游戏完成的最大关卡数
	private int mGameRefreshNum = 1;											// 可用的刷新次数
	private int mGameBombNum = 3;												// 可用的炸弹次数
	private int mGameHintNum = 3;													// 可用的提示次数
	private int mGameFreezeNum = 3;												// 可用的冻结次数
	private int mGameScores = 0;													//	游戏分数
	
	private boolean mAddGameTime = false;									// 判断时间是否需要增加
	private boolean mGameResume = false;									// 判断是否暂停
	
	private int mGameAddTimeCount = 0;										// 游戏进度添加时间
	
	
	
	
	//game alert toast
	private Toast game_toast = null;
	//记录toast显示状态
	private final int STATUS_TOAST_NO_FRESH = 0;
	private final int STATUS_TOAST_NO_BOOM = 1;
	private final int STATUS_TOAST_NO_HINT = 2;
	private final int STATUS_TOAST_NO_FREEZE = 3;
	private final int STATUS_TOAST_FREEZE_IN_TIME = 4;
	
	/**
	 * @title startGame
	 * @description 开始游戏
	 * @param activity：调用的activity
	 * @param state: 游戏开始的状态
	 * @return
	 */
	public void startGame(HughieGameMainActivity activity, int state){
		// 生成游戏布局的函数
		generateGameSurfaceMap();
		
		this.mGameLevel = activity.mGameLevel;
		this.mGameMaxLevel = activity.mGameMaxLevel;
		if(state == 0) {
			this.mGameRefreshNum = activity.mGameRefreshNum;
			this.mGameBombNum = activity.mGameBombNum;
			this.mGameHintNum = activity.mGameHintNum;
			this.mGameFreezeNum = activity.mGameFreezeNum;
		}
		
		this.mGameScores = activity.mGameScore;
		this.mRemainGameTime = mTotalGameTime;
		
		resumeGame(activity); 						// 继续游戏
	}
	
	/**
	 * @title resumeGame
	 * @description 继续游戏
	 * @param activity：调用的activity
	 * @return
	 */
	@SuppressWarnings("static-access")
	public void resumeGame(HughieGameMainActivity activity) {
		this.mGameMainActivity = activity;
		this.mGameApplication = (HughieGameApplication) mGameMainActivity.getApplicationContext();
		// mGameSurfaceParent是一个嵌套的游戏视图布置版面，通过Activity中布局文件id找到游戏面板放入的位置
		LinearLayout mGameSurfaceParent = (LinearLayout) mGameMainActivity.findViewById(R.id.game_main_surface_layout);
		// 实例并初始化HughieGameSurfaceView（游戏视图surface类）
		mGameSurfaceView = new HughieGameSurfaceView(mGameMainActivity);
		// 设置图片显示的宽度
		mGameSurfaceView.setmIconWidth(mGameApplication.mIconWidth);
		// 设置图片显示的高度
		mGameSurfaceView.setmIconHeight(mGameApplication.mIconHeight);
		// 将HughieGameSurfaceView中获得的资源文件导入进来
		mGameSurfaceView.setmGameSurfaceIcons(HughieGameMainActivity.mGameIcons);
		// 在onCreate()方法中初始化布局后，map中对应的便是图片位置信息的数据，将其设置并传入HughieGameSurfaceView布局中
		mGameSurfaceView.setmGameSurfaceMap(mGameSurfaceMap);
		// 设置关卡并将其传入HughieGameSurfaceView中
		mGameSurfaceView.mGameCurrentLevel = mGameLevel;
		
		//注册HughieGameSurfaceView的OnGameSurfaceItemClickListener监听，鼠标点击监听
		mGameSurfaceView.setOnGameSurfaceItemClickListener(mOnGameSurfaceItemClickListener);
		//将HughieGameSurfaceView游戏视图放到.xml文件中定义的LinearLayout位置上实现了视图的嵌套
		mGameSurfaceParent.addView(mGameSurfaceView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		//开始时间设置为系统当前时间-游戏中已经用掉的时间
		mStartGameTime = System.currentTimeMillis() - (mTotalGameTime - mRemainGameTime) * 1000;
		//申明一个Timer()对象用来控制时间和帮助信息
		mGameSurfaceTimer = new Timer();
		mGameSurfaceTimer.schedule(new GameSurfaceTimerTask(), 0, 33);
	}
	
	// 定义上面用到的mGameSurfaceView的OnGameSurfaceItemClickListener监听
	// 监听的工作原理如下，用来监控的点击的图片位置，并判断是否能够消除
	private OnGameSurfaceItemClickListener mOnGameSurfaceItemClickListener = new OnGameSurfaceItemClickListener() {
		@Override
		public void onGameSurfaceItemClick(Point point) {
			mGameApplication.mSoundUtils.playGameSoundByid(5, 0);
			// 定义单元格选择的list
			List<Point> mIconSelectedList = mGameSurfaceView.getmIconSelectedList();
			// 如果已经选中一个, 判断是否可以消除，否则选中当前点击的图片
			if(mIconSelectedList.size() == 1) {
				//getGameLink是连连看的核心算法，point表示当前点
				if(getGameLink(mIconSelectedList.get(0), point)) {
					mIconSelectedList.add(point);
					//connect sound
					mGameApplication.mSoundUtils.playGameSoundByid(4, 0);
					// 获取分数
					mGameScores += 20;
					// 绘制连连看两张图片消除的连线
					mGameSurfaceView.drawGameConnectedPath(mGameIconConnectedPathList.toArray(new Point[] {}));
					// 延时更新
					mRefreshHandler.refreshSleep(500L);
				} else {
					//判断是否为两张相同的icon图片，如果是，则警告
					if(mGameSurfaceMap[mIconSelectedList.get(0).x][mIconSelectedList.get(0).y] == mGameSurfaceMap[point.x][point.y])
						mGameApplication.mSoundUtils.playGameSoundByid(7, 0);
					
					// 不可消除的则更新选中的图片
					// 不可消除那么先清除之前保存的图片
					mIconSelectedList.clear();
					// mIconSelectedList保存当前点点击的图片位置（点）
					mIconSelectedList.add(point);
					mGameSurfaceView.setmIconSelectedList(mIconSelectedList);
					// 直接更新
					mGameSurfaceView.invalidate();
				}
			} else {
				mIconSelectedList.add(point);
				mGameSurfaceView.setmIconSelectedList(mIconSelectedList);
				mGameSurfaceView.invalidate();
			}
		}
	};
	
	//记录有三条折线（有两个折点）中的两个折点
	List<Point> mGamePointE1 = new ArrayList<Point>();
	List<Point> mGamePointE2 = new ArrayList<Point>();
	/**
	 * @title getGameLink
	 * @description 连连看核心算法实现
	 * 						记录两个折点中的两个点
	 * @param origPoint: 第一个折点
	 * @param destPoint: 第二个折点
	 * @return boolean
	 */
	private boolean getGameLink(Point origPoint, Point destPoint) {
		//点击同一张图片两次，返回false
		if(origPoint.equals(destPoint))
			return false;
		
		//mGameSurfacePathList用来消除两张图片时路劲经过的折点数（包括本身要消除的两张图片坐标）
		mGameIconConnectedPathList.clear();
		//首先要保证origPoint和destPoint点对应的图片是同一张
		if(mGameSurfaceMap[origPoint.x][origPoint.y] == mGameSurfaceMap[destPoint.x][destPoint.y]) {
			// 两张图片能够直接连接，则
			if(getGameLinkDirect(origPoint, destPoint)) {
				mGameIconConnectedPathList.add(origPoint);
				mGameIconConnectedPathList.add(destPoint);
				mAddGameTime = true;
				return true;
			}
			
			// 一个折点，考虑到两种可能的情况，mGameIconConnectedPathList中应该有3个点，画直线的时候是两两之间进行的
			Point mLinkTempPoint = new Point(origPoint.x, destPoint.y);
			//判断origPoint和p，以及p和destPoint都能否直接连接
			if(mGameSurfaceMap[mLinkTempPoint.x][mLinkTempPoint.y] == 0) {
				if(getGameLinkDirect(origPoint, mLinkTempPoint) && getGameLinkDirect(mLinkTempPoint, destPoint)) {
					mGameIconConnectedPathList.add(origPoint);
					mGameIconConnectedPathList.add(mLinkTempPoint);
					mGameIconConnectedPathList.add(destPoint);
					mAddGameTime = true;
					return true;
				}
			}
			
			mLinkTempPoint = new Point(destPoint.x, origPoint.y);
			//判断origPoint和p，以及p和destPoint都能否直接连接
			if(mGameSurfaceMap[mLinkTempPoint.x][mLinkTempPoint.y] == 0) {
				if(getGameLinkDirect(origPoint, mLinkTempPoint) && getGameLinkDirect(mLinkTempPoint, destPoint)) {
					mGameIconConnectedPathList.add(origPoint);
					mGameIconConnectedPathList.add(mLinkTempPoint);
					mGameIconConnectedPathList.add(destPoint);
					mAddGameTime = true;
					return true;
				}
			}
			
			// 两个折点
			// 横向搜索，结果集保存在mGamePointE1和mGamePointE2中，结果集中的点必须要可以直接用线连接起来，即同一行或者同一列且中间无阻碍getGameLinkDirect
			getExpandHorizontal(origPoint, mGamePointE1);
			getExpandHorizontal(destPoint, mGamePointE2);
			// 判断是否可以连接
			for(Point pt1 : mGamePointE1) {
				for(Point pt2 : mGamePointE2) {
					if(pt1.x == pt2.x) {
						if(getGameLinkDirect(pt1, pt2)) {
							mGameIconConnectedPathList.add(origPoint);//起始点
							mGameIconConnectedPathList.add(pt1);//第一转折点
							mGameIconConnectedPathList.add(pt2);//第二转折点
							mGameIconConnectedPathList.add(destPoint);//终止点
							mAddGameTime = true;
							return true;
						}
					}
				}
			}
			
			// 纵向搜索，结果集保存在mGamePointE1和mGamePointE2中，结果集中的点必须要可以直接用线连接起来，即同一行或者同一列且中间无阻碍getGameLinkDirect
			getExpandVertical(origPoint, mGamePointE1);
			getExpandVertical(destPoint, mGamePointE2);
			// 判断是否可以连接
			for(Point pt1 : mGamePointE1) {
				for(Point pt2 : mGamePointE2) {
					if(pt1.y == pt2.y) {
						if(getGameLinkDirect(pt1, pt2)) {
							mGameIconConnectedPathList.add(origPoint);//起始点
							mGameIconConnectedPathList.add(pt1);//第一转折点
							mGameIconConnectedPathList.add(pt2);//第二转折点
							mGameIconConnectedPathList.add(destPoint);//终止点
							mAddGameTime = true;
							return true;
						}
					}
				}
			}
			
			return false;
		}
		
		return false;
	}
	
	/**
	 * @title getGameLinkDirect
	 * @description 判断两张图片能否直接相连
	 * @param point1: 起始点
	 * @param point2: 终结点
	 * @return boolean
	 */
	private boolean getGameLinkDirect(Point point1, Point point2) {
		// 判断纵向(列)位置上是否有符合条件的图片组
		if(point1.x == point2.x) {
			int mPointYMin = Math.min(point1.y, point2.y);
			int mPointYMax = Math.max(point1.y, point2.y);
			
			// 定义一个mLinkFlag变量，用来判断是否直接相连，默认为true
			boolean mLinkFlag = true;
			// 以下for和if是两个图片在纵坐标上中间部分全部没有图片，则返回true。只要中间位置有一个不为空，则说明不能直连。故返回false
			for(int y = mPointYMin + 1; y < mPointYMax; y++) {
				//如果在纵坐标位置上不为空，mLinkFlag为false
				if(mGameSurfaceMap[point1.x][y] != 0)
					mLinkFlag = false;
			}
			
			if(mLinkFlag)
				return true;
		}
		
		//判断横向(行)位置上是否有符合条件的图片组
		if(point1.y == point2.y) {
			int mPointXMin = Math.min(point1.x, point2.x);
			int mPointXMax = Math.max(point1.x, point2.x);
			
			// 定义一个mLinkFlag变量，用来判断是否直接相连，默认为true
			boolean mLinkFlag = true;
			//以下for和if是两个图片在横坐标上中间部分全部没有图片，则返回true。只要中间位置有一个不为空，则说明不能直连。故返回false
			for(int x = mPointXMin + 1; x < mPointXMax; x++) {
				if(mGameSurfaceMap[x][point1.y] != 0)
					mLinkFlag = false;
			}
			
			if(mLinkFlag)
				return true;
		}
		
		return false;
	}
	
	/**
	 * @title getExpandHorizontal
	 * @description 水平扫描延伸，结果集保存到ArryList中
	 * @param point: 基准点
	 * @param horizontalPointList: 基准点水平延伸的点组合
	 * @return 
	 */
	@SuppressWarnings("static-access")
	private void getExpandHorizontal(Point point, List<Point> horizontalPointList) {
		// 首先清空horizontalPointList
		horizontalPointList.clear();
		// 往点point的右侧扫描
		for(int x = point.x + 1; x < mGameApplication.mMaxColumn; x++) {
			// 首先要判断这个点是空位置才行
			if(mGameSurfaceMap[x][point.y] != 0){
				break;
			}
			
			horizontalPointList.add(new Point(x, point.y));
		}
		
		// 往点point的左侧扫描
		for(int x = point.x - 1; x >= 0; x--) {
			// 首先要判断这个点是空位置才行
			if(mGameSurfaceMap[x][point.y] != 0){
				break;
			}
			
			horizontalPointList.add(new Point(x, point.y));
		}
	}
	
	/**
	 * @title getExpandVertical
	 * @description 纵向扫描延伸，结果集保存到ArryList中
	 * @param point: 基准点
	 * @param verticalPointList: 基准点竖直延伸的点组合
	 * @return 
	 */
	@SuppressWarnings("static-access")
	private void getExpandVertical(Point point, List<Point> verticalPointList) {
		// 清空verticalPointList
		verticalPointList.clear();
		// 往点point的下侧扫描
		for(int y = point.y + 1; y < mGameApplication.mMaxRow; y++) {
			// 首先要判断这个点是空位置才行
			if(mGameSurfaceMap[point.x][y] != 0) {
				break;
			}
			
			verticalPointList.add(new Point(point.x, y));
		}
		
		// 往点point的上侧扫描
		for(int y = point.y - 1; y >= 0; y--) {
			if(mGameSurfaceMap[point.x][y] != 0) {
				break;
			}
			
			verticalPointList.add(new Point(point.x, y));
		}
	}
	
	/**
	 * @title generateGameSurfaceMap
	 * @description 生成初始化布局，即图片按顺序填充如表格。
	 * 						到最后一张图后又从第一张开始填充。知道整个二维数组填充完毕
	 * @param
	 * @return
	 */
	@SuppressWarnings("static-access")
	private void generateGameSurfaceMap() {
		int mIconsCount = 1;
		mGameSurfaceMap = new int[mGameApplication.mMaxColumn][mGameApplication.mMaxRow];
		for(int i = 1; i < mGameApplication.mMaxColumn - 1; i++) {
			for(int j = 1; j < mGameApplication.mMaxRow - 1; j += 2) {
				//保证countY是偶数
				mGameSurfaceMap[i][j] = mIconsCount;
				mGameSurfaceMap[i][j + 1] = mIconsCount;
				mIconsCount++;
				//如果mIconsCount取的值超过了图片总数，则从新从第一张图片开始取
				if(mIconsCount == HughieGameMainActivity.mIconsCount + 1)
					mIconsCount = 1;
			}
		}
		
		shuffleGameSurfaceMap();
		shuffleGameSurfaceMap();
	}
	
	/**
	 * @title shuffleGameSurfaceMap
	 * @description 随机交换，图片位置改变
	 * @param
	 * @return
	 */
	@SuppressWarnings("static-access")
	private void shuffleGameSurfaceMap() {
		// 随机生成数
		Random mRandom = new Random();
		// 定义获取随机生成数的数值
		int mTempGameNum;
		//定义变量mGamePointList来记录单元格下标的位置列表
		List<Point> mGamePointList = new ArrayList<Point>();
		
		for(int y =1; y < mGameApplication.mMaxRow - 1; y++) {
			for(int x = 1; x < mGameApplication.mMaxColumn - 1; x++) {
				// 判断单元格是否有加载图片资源
				if(mGameSurfaceMap[x][y] != 0) {
					Point p = new Point();
					p.x = x;
					p.y = y;
					mGamePointList.add(p);
				}
			}
		}
		
		//获取mGamePointList的长度
		int mPointCount = mGamePointList.size();
		for(int i = 0; i < mPointCount; i++) {
			mTempGameNum = mRandom.nextInt(mPointCount);
			//获取current点的下标
			Point mCurrentPoint = mGamePointList.get(i);
			//获取随机数点获取的下标
			Point mChangePoint = mGamePointList.get(mTempGameNum);
			
			//current点和随机数点的位置交换图片资源
			int mGamePointValue = mGameSurfaceMap[mCurrentPoint.x][mCurrentPoint.y];
			mGameSurfaceMap[mCurrentPoint.x][mCurrentPoint.y] = mGameSurfaceMap[mChangePoint.x][mChangePoint.y];
			mGameSurfaceMap[mChangePoint.x][mChangePoint.y] = mGamePointValue;
		}
		
		// 如果出现游戏布局无解的情况，则需要重新交换图片
		if(getGameSurfaceDie())
			shuffleGameSurfaceMap();
	}
	
	/**
	 * @title getGameSurfaceDie
	 * @description 判断游戏布局无解的代码，利用遍历的方法对每张图片进行遍历，看是否能够连接，只要有一个能连则有解，
	 * 						否则就是无解的了
	 * @param
	 * @return boolean
	 */
	private boolean getGameSurfaceDie() {
		for(int y = 1; y < mGameApplication.mMaxRow - 1; y++) {
			for(int x = 1; x < mGameApplication.mMaxColumn - 1; x++) {
				// 如果下标为（x，y）的icon还存在，则需要进行如下判断
				if(mGameSurfaceMap[x][y] != 0) {
					for(int j = y; j < mGameApplication.mMaxRow - 1; j++) {
						if(j == y) {
							for(int i = x + 1; i < mGameApplication.mMaxColumn - 1; i++) {
								// 遍历中，如果发现两张一样的图片且他们之间可以进行连线则说明有解，返回false
								if(mGameSurfaceMap[i][j] == mGameSurfaceMap[x][y] && getGameLink(new Point(x, y), new Point(i, j)))
									return false;
							}
						} else {
							for(int i = 1; i < mGameApplication.mMaxColumn - 1; i++) {
								// 遍历中，如果发现两张一样的图片且他们之间可以进行连线则说明有解，返回false
								if(mGameSurfaceMap[i][j] == mGameSurfaceMap[x][y] && getGameLink(new Point(x, y), new Point(i, j)))
									return false;
							}
						}
					}
				}
			}
		}
		
		return true;
	}
	
	/**
	 * @title getGameSurfaceWin
	 * @description 判断连连看游戏是否为赢局，所谓赢局，就是界面上的所有icon都消除完了
	 * @param
	 * @return boolean
	 */
	@SuppressWarnings("static-access")
	private boolean getGameSurfaceWin() {
		for(int x = 0; x < mGameApplication.mMaxColumn; x++) {
			for(int y = 0; y < mGameApplication.mMaxRow; y++) {
				// 只要游戏在规定的时间内还有icon图片没消除，那么游戏就没结束，即没赢
				if(mGameSurfaceMap[x][y] != 0)
					return false;
			}
		}
		
		return true;
	}
	
	//计算分数
	private void get_game_scores(){
		mGameScores = mGameScores + 20;
	}
	
	//实现boom爆炸判断
	private boolean boomConnect(){
		for(int y = 1; y < this.mGameApplication.mMaxRow - 1; y++){
			for(int x = 1; x < this.mGameApplication.mMaxColumn - 1; x++){
				if(mGameSurfaceMap[x][y] != 0){
					for(int j = y; j < this.mGameApplication.mMaxRow - 1; j++){
						if(j == y){
							for(int i = x + 1; i < this.mGameApplication.mMaxColumn - 1; i++){
								if(mGameSurfaceMap[i][j] == mGameSurfaceMap[x][y] && gameBoom(new Point(x, y), new Point(i, j))){
									return false;
								}
							}
						}else{
							for(int i = 1; i < this.mGameApplication.mMaxColumn - 1; i++){
								if(mGameSurfaceMap[i][j] == mGameSurfaceMap[x][y] && gameBoom(new Point(x, y), new Point(i, j))){
									return false;
								}
							}
						}
					}
				}
			}
		}
		
		return true;
	}
	
	//判断是否适合爆炸条件
	private boolean gameBoom(Point p1, Point p2){
		//点击同一张图片两次，返回false
		if(p1.equals(p2)){
			return false;
		}
		
		//path用来消除两张图片时路劲经过的折点数（包括本身要消除的两张图片坐标）
		mGameIconConnectedPathList.clear();
		
		//首先要保证p1和p2点对应的图片是同一张
		if (mGameSurfaceMap[p1.x][p1.y] == mGameSurfaceMap[p2.x][p2.y]){
			mGameIconConnectedPathList.add(p1);
			mGameIconConnectedPathList.add(p2);
			mAddGameTime = true;
			return true;
		}
		
		return false;
	}
	
	//refresh功能的实现
	public void autoRefresh(){
		if(mGameFreezeNum == 0){
			game_toast(STATUS_TOAST_NO_FRESH);
			return;
		}
		mGameApplication.mSoundUtils.playGameSoundByid(8, 0);
		mGameFreezeNum--;
		shuffleGameSurfaceMap();
		mRefreshHandler.refreshSleep(500L);
	}
	
	//boom功能的实现
	public void autoBoom(){
		if(mGameBombNum == 0){
			game_toast(STATUS_TOAST_NO_BOOM);
			return;
		}
		mGameApplication.mSoundUtils.playGameSoundByid(1, 0);
		mGameBombNum--;
		get_game_scores();
		boomConnect();
		mGameSurfaceView.drawGameBombedPath(mGameIconConnectedPathList.toArray(new Point[] {}));
		mRefreshHandler.refreshSleep(500L);
	}
	
	//hint功能的实现
	public void autoHint(){
		if(mGameHintNum == 0){
			game_toast(STATUS_TOAST_NO_HINT);
			return;
		}
		mGameApplication.mSoundUtils.playGameSoundByid(9, 0);
		mGameHintNum--;
		getGameSurfaceDie();
		mGameSurfaceView.drawGameHintedPath(mGameIconConnectedPathList.toArray(new Point[] {}));
		mRefreshHandler.refreshSleep(500L);
	}
	
	//freeze功能的实现
	public void autoFreeze(){
		if(mGameFreezeNum == 0){
			game_toast(STATUS_TOAST_NO_FREEZE);
			return;
		}
		
		if(!mGameSurfaceView.mGameFreeze){
			mGameFreezeNum--;
			mGameSurfaceView.mGameFreeze = true;
			mGameSurfaceView.mGameFreezeStatus = mGameSurfaceView.ANIM_GAME_FREEZE_SHOW;
			mFreezeGameTime = mRemainGameTime;
			mStartGameTime += 4;
			mRefreshHandler.refreshSleep(500L);
		}else{
			game_toast(STATUS_TOAST_FREEZE_IN_TIME);
		}
	}
	
	/**
	 * @title setGamePause
	 * @description 暂停功能
	 * @param
	 * @return
	 */
	public void setGamePause() {
		if (mGameSurfaceTimer != null){
			mGameSurfaceTimer.cancel();
			mGameResume = true;
		}	
	}
	
	//自定义toast显示
	private void game_toast(int tool_code){
		LayoutInflater inflater = mGameMainActivity.getLayoutInflater();
		View game_toast_view = inflater.inflate(R.layout.define_toast, 
				(ViewGroup)mGameMainActivity.findViewById(R.id.toast_layout));
		//toast title
		TextView game_toast_title = (TextView)game_toast_view.findViewById(R.id.toast_title);
		//toast image
		ImageView game_toast_img = (ImageView)game_toast_view.findViewById(R.id.toast_img);
		//toast content
		TextView game_toast_content = (TextView)game_toast_view.findViewById(R.id.toast_context);
		
		//设置字体
		Typeface fonttype = Typeface.createFromAsset(mGameMainActivity.getAssets(), "hobostd.otf");
		game_toast_title.setTypeface(fonttype);
		game_toast_content.setTypeface(fonttype);
		
		game_toast_title.setText(R.string.str_game_main_toast_title_txt);
		if(tool_code == STATUS_TOAST_NO_FRESH){
			game_toast_view.setBackgroundResource(R.drawable.hughie_game_main_toast_error_background);
			game_toast_img.setBackgroundResource(R.drawable.imgv_game_indicator_cancel);
			game_toast_content.setTextColor(Color.BLACK);
			game_toast_content.setText(R.string.str_game_main_toast_refresh_content_txt);
		}else if(tool_code == STATUS_TOAST_NO_BOOM){
			game_toast_view.setBackgroundResource(R.drawable.hughie_game_main_toast_error_background);
			game_toast_img.setBackgroundResource(R.drawable.imgv_game_indicator_cancel);
			game_toast_content.setTextColor(Color.BLACK);
			game_toast_content.setText(R.string.str_game_main_toast_bomb_content_txt);
		}else if(tool_code == STATUS_TOAST_NO_HINT){
			game_toast_view.setBackgroundResource(R.drawable.hughie_game_main_toast_error_background);
			game_toast_img.setBackgroundResource(R.drawable.imgv_game_indicator_cancel);
			game_toast_content.setTextColor(Color.BLACK);
			game_toast_content.setText(R.string.str_game_main_toast_hint_content_txt);
		}else if(tool_code == STATUS_TOAST_NO_FREEZE){
			game_toast_view.setBackgroundResource(R.drawable.hughie_game_main_toast_error_background);
			game_toast_img.setBackgroundResource(R.drawable.imgv_game_indicator_cancel);
			game_toast_content.setTextColor(Color.BLACK);
			game_toast_content.setText(R.string.str_game_main_toast_freeze_content_txt);
		}else if(tool_code == STATUS_TOAST_FREEZE_IN_TIME){
			game_toast_view.setBackgroundResource(R.drawable.hughie_game_main_toast_error_background);
			game_toast_img.setBackgroundResource(R.drawable.imgv_game_indicator_cancel);
			game_toast_content.setTextColor(Color.BLACK);
			game_toast_content.setText(R.string.str_game_main_toast_freeze_in_time_content_txt);
		}
		
		game_toast = new Toast(mGameMainActivity.getApplication());
		game_toast.setGravity(Gravity.CENTER, 0, 180);
		game_toast.setDuration(Toast.LENGTH_SHORT);
		game_toast.setView(game_toast_view);
		game_toast.show();
	}
	
	
	class GameSurfaceTimerTask extends TimerTask {
		@Override
		public void run() {
			// 更新剩余时间
			if(mAddGameTime) {
				mGameAddTimeCount++;
				mAddGameTime = false;
				if (mGameAddTimeCount % 2 ==  0)
					mStartGameTime += 1000;
			}
			
			if(mGameSurfaceView.mGameFreeze){
				if(mRemainGameTime <= mFreezeGameTime - 3){
					mGameSurfaceView.mGameFreezeStatus = mGameSurfaceView.ANIM_GAME_FREEZE_ALERT;
				}
				
				if(mRemainGameTime <= mFreezeGameTime - 4){
					mGameSurfaceView.mGameFreeze = false;
					mGameSurfaceView.mGameFreezeStatus = mGameSurfaceView.ANIM_GAME_FREEZE_NONE;
					mFreezeGameTime = 0;
				}
			}
			
			mRemainGameTime = mTotalGameTime - (System.currentTimeMillis() - mStartGameTime) / 1000;
			Message msg = new Message();
			msg.what = GameSurfaceRefreshHandler.GAME_SURFACE_REFRESH_UPDATE_TXT;
			mRefreshHandler.sendMessage(msg);
		}
	}
	
	class GameSurfaceRefreshHandler extends Handler {
		public static final int GAME_SURFACE_REFRESH_UPDATE_TXT = 0;
		public static final int GAME_SURFACE_REFRESH_UPDATE_IMAGE = 1;
		
		//之前此处有个错误是将handleMessage写成了handlerMessage,导致游戏中文本框不显示和视图更新不及时
		//这个方法并不需要调用，是Handler内接口的实现。是一个消息接收器，所以不能随意命名
		@SuppressWarnings("static-access")
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
				// 更新文本框
				case GAME_SURFACE_REFRESH_UPDATE_TXT:
					// game controller refresh
					TextView mControllerRefreshTv = (TextView) mGameMainActivity.findViewById(R.id.game_main_tool_bottom_refresh_num_tv);
					mControllerRefreshTv.setText(String.valueOf(mGameRefreshNum));
					mGameMainActivity.sp.edit().putInt(HughieSPManager.SP_GameRefresh, mGameRefreshNum).commit();
					// game controller bomb
					TextView mControllerBombTv = (TextView) mGameMainActivity.findViewById(R.id.game_main_tool_bottom_bomb_num_tv);
					mControllerBombTv.setText(String.valueOf(mGameBombNum));
					mGameMainActivity.sp.edit().putInt(HughieSPManager.SP_GameBomb, mGameBombNum).commit();
					// game controller hint
					TextView mControllerHintTv = (TextView) mGameMainActivity.findViewById(R.id.game_main_tool_bottom_hint_num_tv);
					mControllerHintTv.setText(String.valueOf(mGameHintNum));
					mGameMainActivity.sp.edit().putInt(HughieSPManager.SP_GameHint, mGameHintNum).commit();
					
					// game controller freeze
					TextView mControllerFreezeTv = (TextView) mGameMainActivity.findViewById(R.id.game_main_tool_bottom_freeze_num_tv);
					mControllerFreezeTv.setText(String.valueOf(mGameFreezeNum));
					mGameMainActivity.sp.edit().putInt(HughieSPManager.SP_GameFreeze, mGameFreezeNum).commit();
					
					// game controller score
					TextView mControllerScoreTv = (TextView) mGameMainActivity.findViewById(R.id.game_main_function_score_tv);
					// 用于+1的加分动画
					int mGameScore = Integer.valueOf(mControllerScoreTv.getText().toString());
					if(mGameResume){
						mControllerScoreTv.setText(String.valueOf(mGameScores));
						mGameResume = false;
					} else {
						if(mGameScore < mGameScores) {
							mGameScore += 1;
							mControllerScoreTv.setText(String.valueOf(mGameScore));
						}
					}
					
					mGameMainActivity.sp.edit().putInt(HughieSPManager.SP_GameScore, mGameScores).commit();
					
					mGameSurfaceView.drawGameProgress(mRemainGameTime, mTotalGameTime);
					if (mRemainGameTime <= 0) {
						mGameSurfaceTimer.cancel();
						mGameMainActivity.setGameState(HughieGameMainActivity.TAG_GAME_MAIN_STATE_LOSE);
					}
					
					break;
				// 更新视图
				case GAME_SURFACE_REFRESH_UPDATE_IMAGE:
					mGameSurfaceView.invalidate();
					// 判断是否为赢局，如果是则进行相应操作，如果是死局，则继续重新变换icon的布局
					if(getGameSurfaceWin()) {
						// 奖励，刷新，炸弹，提示以及冻结都自动加1
						mGameRefreshNum++;
						mGameBombNum++;
						mGameHintNum++;
						mGameFreezeNum++;
						mGameSurfaceView.mGameCurrentLevel = mGameLevel;
						// 判断游戏结束的状态, 如果超出时间在15s之类，则为1星，如果在15-30s之内，则为2星，如果超出30s，则为3星
						// 一星
						if(mRemainGameTime > 0 && mRemainGameTime <= 15) {
							mGameMainActivity.mGameFinishStatus = mGameMainActivity.TAG_GAME_MAIN_STATUS_WIN_STAR1;
						// 二星
						} else if(mRemainGameTime > 15 && mRemainGameTime <= 30) {
							mGameMainActivity.mGameFinishStatus = mGameMainActivity.TAG_GAME_MAIN_STATUS_WIN_STAR2;
						// 三星
						} else {
							mGameMainActivity.mGameFinishStatus = mGameMainActivity.TAG_GAME_MAIN_STATUS_WIN_STAR3;
						}
						
						mGameSurfaceTimer.cancel();
						mGameMainActivity.setGameState(HughieGameMainActivity.TAG_GAME_MAIN_STATE_WIN);
					} else if(getGameSurfaceDie()) {
						shuffleGameSurfaceMap();
					}
					break;
			}
		}
		
		// 延时刷新视图
		public void refreshSleep(long delayMillis) {
			removeMessages(0);
			Message msg = new Message();
			msg.what = GAME_SURFACE_REFRESH_UPDATE_IMAGE;
			sendMessageDelayed(msg, delayMillis);
		}
	}
}

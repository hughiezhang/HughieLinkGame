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
 * ��Ϸ������������
 * @ClassName: HughieGameController
 * @author hughiezhang
 * @since 2015-10-19 09:50
 */
public class HughieGameController {
	public HughieGameApplication mGameApplication;						 
	public HughieGameMainActivity mGameMainActivity;					//��Ϸ����activity
	
	private HughieGameSurfaceView mGameSurfaceView;					//�Զ������Ϸ��ͼ�࣬����������Ϸ����
	
	private int[][] mGameSurfaceMap;												// ����������򲼾ֵ����飬֮ǰ��HughieGameSurfaceView�е�
																									// mapֻ�Ƕ���������һ�����ã��������н�Ϊ֮����ռ䣬���ɳ�ʼ�����֡�
	
	private List<Point> mGameIconConnectedPathList = new ArrayList<Point>();		// �������ߵ���ʼ����۵�
	
	/*
	 * ˢ�½�����������֣�1.ʵ������������������HughieGameSurfaceView������ʱˢ�£�2.������Ϸʣ��ʱ�����ҳ��ˢ��
	 * 2����Ҫ�õ�һ��Timer����Timer����������һ���̣߳���ˢ�½�����Ҫ��UI�߳���ִ�У����Գ�����Ҫ�õ�Handler������ˢ��
	 * 1,2����ͨ����һ������õ�Handler���Ͳ�ͬ����Ϣ���в�ͬ��ˢ�²���
	 */
	private GameSurfaceRefreshHandler mRefreshHandler = new GameSurfaceRefreshHandler();
	
	// ������Ϸ�ļ�ʱ���Ͱ��������ĸ��£�����ͨ��һ��TimerTask��ʵ��
	private static final long mTotalGameTime = 120;						// ��Ϸ������ʱ��
	private long mRemainGameTime;												// ��Ϸʣ��ʱ��
	private long mStartGameTime;													// ��Ϸ�Ŀ�ʼʱ��
	private long mFreezeGameTime;												// ��Ϸ����ʱ��
	private Timer mGameSurfaceTimer;											// ��Ϸʱ���ʱ��
	
	public int mGameLevel = 1;														// ��Ϸ�ؿ���
	public int mGameMaxLevel = 1;													// ��Ϸ��ɵ����ؿ���
	private int mGameRefreshNum = 1;											// ���õ�ˢ�´���
	private int mGameBombNum = 3;												// ���õ�ը������
	private int mGameHintNum = 3;													// ���õ���ʾ����
	private int mGameFreezeNum = 3;												// ���õĶ������
	private int mGameScores = 0;													//	��Ϸ����
	
	private boolean mAddGameTime = false;									// �ж�ʱ���Ƿ���Ҫ����
	private boolean mGameResume = false;									// �ж��Ƿ���ͣ
	
	private int mGameAddTimeCount = 0;										// ��Ϸ�������ʱ��
	
	
	
	
	//game alert toast
	private Toast game_toast = null;
	//��¼toast��ʾ״̬
	private final int STATUS_TOAST_NO_FRESH = 0;
	private final int STATUS_TOAST_NO_BOOM = 1;
	private final int STATUS_TOAST_NO_HINT = 2;
	private final int STATUS_TOAST_NO_FREEZE = 3;
	private final int STATUS_TOAST_FREEZE_IN_TIME = 4;
	
	/**
	 * @title startGame
	 * @description ��ʼ��Ϸ
	 * @param activity�����õ�activity
	 * @param state: ��Ϸ��ʼ��״̬
	 * @return
	 */
	public void startGame(HughieGameMainActivity activity, int state){
		// ������Ϸ���ֵĺ���
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
		
		resumeGame(activity); 						// ������Ϸ
	}
	
	/**
	 * @title resumeGame
	 * @description ������Ϸ
	 * @param activity�����õ�activity
	 * @return
	 */
	@SuppressWarnings("static-access")
	public void resumeGame(HughieGameMainActivity activity) {
		this.mGameMainActivity = activity;
		this.mGameApplication = (HughieGameApplication) mGameMainActivity.getApplicationContext();
		// mGameSurfaceParent��һ��Ƕ�׵���Ϸ��ͼ���ð��棬ͨ��Activity�в����ļ�id�ҵ���Ϸ�������λ��
		LinearLayout mGameSurfaceParent = (LinearLayout) mGameMainActivity.findViewById(R.id.game_main_surface_layout);
		// ʵ������ʼ��HughieGameSurfaceView����Ϸ��ͼsurface�ࣩ
		mGameSurfaceView = new HughieGameSurfaceView(mGameMainActivity);
		// ����ͼƬ��ʾ�Ŀ��
		mGameSurfaceView.setmIconWidth(mGameApplication.mIconWidth);
		// ����ͼƬ��ʾ�ĸ߶�
		mGameSurfaceView.setmIconHeight(mGameApplication.mIconHeight);
		// ��HughieGameSurfaceView�л�õ���Դ�ļ��������
		mGameSurfaceView.setmGameSurfaceIcons(HughieGameMainActivity.mGameIcons);
		// ��onCreate()�����г�ʼ�����ֺ�map�ж�Ӧ�ı���ͼƬλ����Ϣ�����ݣ��������ò�����HughieGameSurfaceView������
		mGameSurfaceView.setmGameSurfaceMap(mGameSurfaceMap);
		// ���ùؿ������䴫��HughieGameSurfaceView��
		mGameSurfaceView.mGameCurrentLevel = mGameLevel;
		
		//ע��HughieGameSurfaceView��OnGameSurfaceItemClickListener���������������
		mGameSurfaceView.setOnGameSurfaceItemClickListener(mOnGameSurfaceItemClickListener);
		//��HughieGameSurfaceView��Ϸ��ͼ�ŵ�.xml�ļ��ж����LinearLayoutλ����ʵ������ͼ��Ƕ��
		mGameSurfaceParent.addView(mGameSurfaceView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		//��ʼʱ������Ϊϵͳ��ǰʱ��-��Ϸ���Ѿ��õ���ʱ��
		mStartGameTime = System.currentTimeMillis() - (mTotalGameTime - mRemainGameTime) * 1000;
		//����һ��Timer()������������ʱ��Ͱ�����Ϣ
		mGameSurfaceTimer = new Timer();
		mGameSurfaceTimer.schedule(new GameSurfaceTimerTask(), 0, 33);
	}
	
	// ���������õ���mGameSurfaceView��OnGameSurfaceItemClickListener����
	// �����Ĺ���ԭ�����£�������صĵ����ͼƬλ�ã����ж��Ƿ��ܹ�����
	private OnGameSurfaceItemClickListener mOnGameSurfaceItemClickListener = new OnGameSurfaceItemClickListener() {
		@Override
		public void onGameSurfaceItemClick(Point point) {
			mGameApplication.mSoundUtils.playGameSoundByid(5, 0);
			// ���嵥Ԫ��ѡ���list
			List<Point> mIconSelectedList = mGameSurfaceView.getmIconSelectedList();
			// ����Ѿ�ѡ��һ��, �ж��Ƿ��������������ѡ�е�ǰ�����ͼƬ
			if(mIconSelectedList.size() == 1) {
				//getGameLink���������ĺ����㷨��point��ʾ��ǰ��
				if(getGameLink(mIconSelectedList.get(0), point)) {
					mIconSelectedList.add(point);
					//connect sound
					mGameApplication.mSoundUtils.playGameSoundByid(4, 0);
					// ��ȡ����
					mGameScores += 20;
					// ��������������ͼƬ����������
					mGameSurfaceView.drawGameConnectedPath(mGameIconConnectedPathList.toArray(new Point[] {}));
					// ��ʱ����
					mRefreshHandler.refreshSleep(500L);
				} else {
					//�ж��Ƿ�Ϊ������ͬ��iconͼƬ������ǣ��򾯸�
					if(mGameSurfaceMap[mIconSelectedList.get(0).x][mIconSelectedList.get(0).y] == mGameSurfaceMap[point.x][point.y])
						mGameApplication.mSoundUtils.playGameSoundByid(7, 0);
					
					// ���������������ѡ�е�ͼƬ
					// ����������ô�����֮ǰ�����ͼƬ
					mIconSelectedList.clear();
					// mIconSelectedList���浱ǰ������ͼƬλ�ã��㣩
					mIconSelectedList.add(point);
					mGameSurfaceView.setmIconSelectedList(mIconSelectedList);
					// ֱ�Ӹ���
					mGameSurfaceView.invalidate();
				}
			} else {
				mIconSelectedList.add(point);
				mGameSurfaceView.setmIconSelectedList(mIconSelectedList);
				mGameSurfaceView.invalidate();
			}
		}
	};
	
	//��¼���������ߣ��������۵㣩�е������۵�
	List<Point> mGamePointE1 = new ArrayList<Point>();
	List<Point> mGamePointE2 = new ArrayList<Point>();
	/**
	 * @title getGameLink
	 * @description �����������㷨ʵ��
	 * 						��¼�����۵��е�������
	 * @param origPoint: ��һ���۵�
	 * @param destPoint: �ڶ����۵�
	 * @return boolean
	 */
	private boolean getGameLink(Point origPoint, Point destPoint) {
		//���ͬһ��ͼƬ���Σ�����false
		if(origPoint.equals(destPoint))
			return false;
		
		//mGameSurfacePathList������������ͼƬʱ·���������۵�������������Ҫ����������ͼƬ���꣩
		mGameIconConnectedPathList.clear();
		//����Ҫ��֤origPoint��destPoint���Ӧ��ͼƬ��ͬһ��
		if(mGameSurfaceMap[origPoint.x][origPoint.y] == mGameSurfaceMap[destPoint.x][destPoint.y]) {
			// ����ͼƬ�ܹ�ֱ�����ӣ���
			if(getGameLinkDirect(origPoint, destPoint)) {
				mGameIconConnectedPathList.add(origPoint);
				mGameIconConnectedPathList.add(destPoint);
				mAddGameTime = true;
				return true;
			}
			
			// һ���۵㣬���ǵ����ֿ��ܵ������mGameIconConnectedPathList��Ӧ����3���㣬��ֱ�ߵ�ʱ��������֮����е�
			Point mLinkTempPoint = new Point(origPoint.x, destPoint.y);
			//�ж�origPoint��p���Լ�p��destPoint���ܷ�ֱ������
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
			//�ж�origPoint��p���Լ�p��destPoint���ܷ�ֱ������
			if(mGameSurfaceMap[mLinkTempPoint.x][mLinkTempPoint.y] == 0) {
				if(getGameLinkDirect(origPoint, mLinkTempPoint) && getGameLinkDirect(mLinkTempPoint, destPoint)) {
					mGameIconConnectedPathList.add(origPoint);
					mGameIconConnectedPathList.add(mLinkTempPoint);
					mGameIconConnectedPathList.add(destPoint);
					mAddGameTime = true;
					return true;
				}
			}
			
			// �����۵�
			// ���������������������mGamePointE1��mGamePointE2�У�������еĵ����Ҫ����ֱ������������������ͬһ�л���ͬһ�����м����谭getGameLinkDirect
			getExpandHorizontal(origPoint, mGamePointE1);
			getExpandHorizontal(destPoint, mGamePointE2);
			// �ж��Ƿ��������
			for(Point pt1 : mGamePointE1) {
				for(Point pt2 : mGamePointE2) {
					if(pt1.x == pt2.x) {
						if(getGameLinkDirect(pt1, pt2)) {
							mGameIconConnectedPathList.add(origPoint);//��ʼ��
							mGameIconConnectedPathList.add(pt1);//��һת�۵�
							mGameIconConnectedPathList.add(pt2);//�ڶ�ת�۵�
							mGameIconConnectedPathList.add(destPoint);//��ֹ��
							mAddGameTime = true;
							return true;
						}
					}
				}
			}
			
			// ���������������������mGamePointE1��mGamePointE2�У�������еĵ����Ҫ����ֱ������������������ͬһ�л���ͬһ�����м����谭getGameLinkDirect
			getExpandVertical(origPoint, mGamePointE1);
			getExpandVertical(destPoint, mGamePointE2);
			// �ж��Ƿ��������
			for(Point pt1 : mGamePointE1) {
				for(Point pt2 : mGamePointE2) {
					if(pt1.y == pt2.y) {
						if(getGameLinkDirect(pt1, pt2)) {
							mGameIconConnectedPathList.add(origPoint);//��ʼ��
							mGameIconConnectedPathList.add(pt1);//��һת�۵�
							mGameIconConnectedPathList.add(pt2);//�ڶ�ת�۵�
							mGameIconConnectedPathList.add(destPoint);//��ֹ��
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
	 * @description �ж�����ͼƬ�ܷ�ֱ������
	 * @param point1: ��ʼ��
	 * @param point2: �ս��
	 * @return boolean
	 */
	private boolean getGameLinkDirect(Point point1, Point point2) {
		// �ж�����(��)λ�����Ƿ��з���������ͼƬ��
		if(point1.x == point2.x) {
			int mPointYMin = Math.min(point1.y, point2.y);
			int mPointYMax = Math.max(point1.y, point2.y);
			
			// ����һ��mLinkFlag�����������ж��Ƿ�ֱ��������Ĭ��Ϊtrue
			boolean mLinkFlag = true;
			// ����for��if������ͼƬ�����������м䲿��ȫ��û��ͼƬ���򷵻�true��ֻҪ�м�λ����һ����Ϊ�գ���˵������ֱ�����ʷ���false
			for(int y = mPointYMin + 1; y < mPointYMax; y++) {
				//�����������λ���ϲ�Ϊ�գ�mLinkFlagΪfalse
				if(mGameSurfaceMap[point1.x][y] != 0)
					mLinkFlag = false;
			}
			
			if(mLinkFlag)
				return true;
		}
		
		//�жϺ���(��)λ�����Ƿ��з���������ͼƬ��
		if(point1.y == point2.y) {
			int mPointXMin = Math.min(point1.x, point2.x);
			int mPointXMax = Math.max(point1.x, point2.x);
			
			// ����һ��mLinkFlag�����������ж��Ƿ�ֱ��������Ĭ��Ϊtrue
			boolean mLinkFlag = true;
			//����for��if������ͼƬ�ں��������м䲿��ȫ��û��ͼƬ���򷵻�true��ֻҪ�м�λ����һ����Ϊ�գ���˵������ֱ�����ʷ���false
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
	 * @description ˮƽɨ�����죬��������浽ArryList��
	 * @param point: ��׼��
	 * @param horizontalPointList: ��׼��ˮƽ����ĵ����
	 * @return 
	 */
	@SuppressWarnings("static-access")
	private void getExpandHorizontal(Point point, List<Point> horizontalPointList) {
		// �������horizontalPointList
		horizontalPointList.clear();
		// ����point���Ҳ�ɨ��
		for(int x = point.x + 1; x < mGameApplication.mMaxColumn; x++) {
			// ����Ҫ�ж�������ǿ�λ�ò���
			if(mGameSurfaceMap[x][point.y] != 0){
				break;
			}
			
			horizontalPointList.add(new Point(x, point.y));
		}
		
		// ����point�����ɨ��
		for(int x = point.x - 1; x >= 0; x--) {
			// ����Ҫ�ж�������ǿ�λ�ò���
			if(mGameSurfaceMap[x][point.y] != 0){
				break;
			}
			
			horizontalPointList.add(new Point(x, point.y));
		}
	}
	
	/**
	 * @title getExpandVertical
	 * @description ����ɨ�����죬��������浽ArryList��
	 * @param point: ��׼��
	 * @param verticalPointList: ��׼����ֱ����ĵ����
	 * @return 
	 */
	@SuppressWarnings("static-access")
	private void getExpandVertical(Point point, List<Point> verticalPointList) {
		// ���verticalPointList
		verticalPointList.clear();
		// ����point���²�ɨ��
		for(int y = point.y + 1; y < mGameApplication.mMaxRow; y++) {
			// ����Ҫ�ж�������ǿ�λ�ò���
			if(mGameSurfaceMap[point.x][y] != 0) {
				break;
			}
			
			verticalPointList.add(new Point(point.x, y));
		}
		
		// ����point���ϲ�ɨ��
		for(int y = point.y - 1; y >= 0; y--) {
			if(mGameSurfaceMap[point.x][y] != 0) {
				break;
			}
			
			verticalPointList.add(new Point(point.x, y));
		}
	}
	
	/**
	 * @title generateGameSurfaceMap
	 * @description ���ɳ�ʼ�����֣���ͼƬ��˳���������
	 * 						�����һ��ͼ���ִӵ�һ�ſ�ʼ��䡣֪��������ά����������
	 * @param
	 * @return
	 */
	@SuppressWarnings("static-access")
	private void generateGameSurfaceMap() {
		int mIconsCount = 1;
		mGameSurfaceMap = new int[mGameApplication.mMaxColumn][mGameApplication.mMaxRow];
		for(int i = 1; i < mGameApplication.mMaxColumn - 1; i++) {
			for(int j = 1; j < mGameApplication.mMaxRow - 1; j += 2) {
				//��֤countY��ż��
				mGameSurfaceMap[i][j] = mIconsCount;
				mGameSurfaceMap[i][j + 1] = mIconsCount;
				mIconsCount++;
				//���mIconsCountȡ��ֵ������ͼƬ����������´ӵ�һ��ͼƬ��ʼȡ
				if(mIconsCount == HughieGameMainActivity.mIconsCount + 1)
					mIconsCount = 1;
			}
		}
		
		shuffleGameSurfaceMap();
		shuffleGameSurfaceMap();
	}
	
	/**
	 * @title shuffleGameSurfaceMap
	 * @description ���������ͼƬλ�øı�
	 * @param
	 * @return
	 */
	@SuppressWarnings("static-access")
	private void shuffleGameSurfaceMap() {
		// ���������
		Random mRandom = new Random();
		// �����ȡ�������������ֵ
		int mTempGameNum;
		//�������mGamePointList����¼��Ԫ���±��λ���б�
		List<Point> mGamePointList = new ArrayList<Point>();
		
		for(int y =1; y < mGameApplication.mMaxRow - 1; y++) {
			for(int x = 1; x < mGameApplication.mMaxColumn - 1; x++) {
				// �жϵ�Ԫ���Ƿ��м���ͼƬ��Դ
				if(mGameSurfaceMap[x][y] != 0) {
					Point p = new Point();
					p.x = x;
					p.y = y;
					mGamePointList.add(p);
				}
			}
		}
		
		//��ȡmGamePointList�ĳ���
		int mPointCount = mGamePointList.size();
		for(int i = 0; i < mPointCount; i++) {
			mTempGameNum = mRandom.nextInt(mPointCount);
			//��ȡcurrent����±�
			Point mCurrentPoint = mGamePointList.get(i);
			//��ȡ��������ȡ���±�
			Point mChangePoint = mGamePointList.get(mTempGameNum);
			
			//current�����������λ�ý���ͼƬ��Դ
			int mGamePointValue = mGameSurfaceMap[mCurrentPoint.x][mCurrentPoint.y];
			mGameSurfaceMap[mCurrentPoint.x][mCurrentPoint.y] = mGameSurfaceMap[mChangePoint.x][mChangePoint.y];
			mGameSurfaceMap[mChangePoint.x][mChangePoint.y] = mGamePointValue;
		}
		
		// ���������Ϸ�����޽�����������Ҫ���½���ͼƬ
		if(getGameSurfaceDie())
			shuffleGameSurfaceMap();
	}
	
	/**
	 * @title getGameSurfaceDie
	 * @description �ж���Ϸ�����޽�Ĵ��룬���ñ����ķ�����ÿ��ͼƬ���б��������Ƿ��ܹ����ӣ�ֻҪ��һ���������н⣬
	 * 						��������޽����
	 * @param
	 * @return boolean
	 */
	private boolean getGameSurfaceDie() {
		for(int y = 1; y < mGameApplication.mMaxRow - 1; y++) {
			for(int x = 1; x < mGameApplication.mMaxColumn - 1; x++) {
				// ����±�Ϊ��x��y����icon�����ڣ�����Ҫ���������ж�
				if(mGameSurfaceMap[x][y] != 0) {
					for(int j = y; j < mGameApplication.mMaxRow - 1; j++) {
						if(j == y) {
							for(int i = x + 1; i < mGameApplication.mMaxColumn - 1; i++) {
								// �����У������������һ����ͼƬ������֮����Խ���������˵���н⣬����false
								if(mGameSurfaceMap[i][j] == mGameSurfaceMap[x][y] && getGameLink(new Point(x, y), new Point(i, j)))
									return false;
							}
						} else {
							for(int i = 1; i < mGameApplication.mMaxColumn - 1; i++) {
								// �����У������������һ����ͼƬ������֮����Խ���������˵���н⣬����false
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
	 * @description �ж���������Ϸ�Ƿ�ΪӮ�֣���νӮ�֣����ǽ����ϵ�����icon����������
	 * @param
	 * @return boolean
	 */
	@SuppressWarnings("static-access")
	private boolean getGameSurfaceWin() {
		for(int x = 0; x < mGameApplication.mMaxColumn; x++) {
			for(int y = 0; y < mGameApplication.mMaxRow; y++) {
				// ֻҪ��Ϸ�ڹ涨��ʱ���ڻ���iconͼƬû��������ô��Ϸ��û��������ûӮ
				if(mGameSurfaceMap[x][y] != 0)
					return false;
			}
		}
		
		return true;
	}
	
	//�������
	private void get_game_scores(){
		mGameScores = mGameScores + 20;
	}
	
	//ʵ��boom��ը�ж�
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
	
	//�ж��Ƿ��ʺϱ�ը����
	private boolean gameBoom(Point p1, Point p2){
		//���ͬһ��ͼƬ���Σ�����false
		if(p1.equals(p2)){
			return false;
		}
		
		//path������������ͼƬʱ·���������۵�������������Ҫ����������ͼƬ���꣩
		mGameIconConnectedPathList.clear();
		
		//����Ҫ��֤p1��p2���Ӧ��ͼƬ��ͬһ��
		if (mGameSurfaceMap[p1.x][p1.y] == mGameSurfaceMap[p2.x][p2.y]){
			mGameIconConnectedPathList.add(p1);
			mGameIconConnectedPathList.add(p2);
			mAddGameTime = true;
			return true;
		}
		
		return false;
	}
	
	//refresh���ܵ�ʵ��
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
	
	//boom���ܵ�ʵ��
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
	
	//hint���ܵ�ʵ��
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
	
	//freeze���ܵ�ʵ��
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
	 * @description ��ͣ����
	 * @param
	 * @return
	 */
	public void setGamePause() {
		if (mGameSurfaceTimer != null){
			mGameSurfaceTimer.cancel();
			mGameResume = true;
		}	
	}
	
	//�Զ���toast��ʾ
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
		
		//��������
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
			// ����ʣ��ʱ��
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
		
		//֮ǰ�˴��и������ǽ�handleMessageд����handlerMessage,������Ϸ���ı�����ʾ����ͼ���²���ʱ
		//�������������Ҫ���ã���Handler�ڽӿڵ�ʵ�֡���һ����Ϣ�����������Բ�����������
		@SuppressWarnings("static-access")
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
				// �����ı���
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
					// ����+1�ļӷֶ���
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
				// ������ͼ
				case GAME_SURFACE_REFRESH_UPDATE_IMAGE:
					mGameSurfaceView.invalidate();
					// �ж��Ƿ�ΪӮ�֣�������������Ӧ��������������֣���������±任icon�Ĳ���
					if(getGameSurfaceWin()) {
						// ������ˢ�£�ը������ʾ�Լ����ᶼ�Զ���1
						mGameRefreshNum++;
						mGameBombNum++;
						mGameHintNum++;
						mGameFreezeNum++;
						mGameSurfaceView.mGameCurrentLevel = mGameLevel;
						// �ж���Ϸ������״̬, �������ʱ����15s֮�࣬��Ϊ1�ǣ������15-30s֮�ڣ���Ϊ2�ǣ��������30s����Ϊ3��
						// һ��
						if(mRemainGameTime > 0 && mRemainGameTime <= 15) {
							mGameMainActivity.mGameFinishStatus = mGameMainActivity.TAG_GAME_MAIN_STATUS_WIN_STAR1;
						// ����
						} else if(mRemainGameTime > 15 && mRemainGameTime <= 30) {
							mGameMainActivity.mGameFinishStatus = mGameMainActivity.TAG_GAME_MAIN_STATUS_WIN_STAR2;
						// ����
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
		
		// ��ʱˢ����ͼ
		public void refreshSleep(long delayMillis) {
			removeMessages(0);
			Message msg = new Message();
			msg.what = GAME_SURFACE_REFRESH_UPDATE_IMAGE;
			sendMessageDelayed(msg, delayMillis);
		}
	}
}

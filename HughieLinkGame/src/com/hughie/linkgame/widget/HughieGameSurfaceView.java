package com.hughie.linkgame.widget;

import java.util.ArrayList;
import java.util.List;

import com.hughie.link.support.common.HughieLoggerManager;
import com.hughie.linkgame.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;

/**
 * 游戏主界面的surface界面
 * @ClassName: HughieGameSurfaceView
 * @author hughiezhang
 * @since 2015-10-22 17:35
 */
public class HughieGameSurfaceView extends View {
	public static int mGameCurrentLevel;								// 游戏的当前关卡
	
	private int mIconWidth;													// 连连看单元格宽度
	private int mIconHeight;												// 连连看单元格高度
	
	private int mOffsetX;														// 图片在横向上的偏移值
	private int mOffsetY;														// 图片在纵向上的偏移值
	
	private int[][] mGameSurfaceMap;									// 二维数组，其下标对应为视图的表格。而其内容是为存储图片的id（图片名）
	private Bitmap[] mGameSurfaceIcons;								// 图片资源
	
	private List<Point> mIconSelectedList;							// 游戏中，点击图标后用该数组来保存被选中图片的坐标
	public int mIconSelectedTime;										// 定义select animation显示不同图片的时间
	
	private Point[] mIconConnectedPath;								// 用来存储连线折点的数组
	private int mIconConnectedTime;									// 定义单元格连线的animation时间
	
	private int mScoreAnimTime;											// 定义获取分数的animation时间
	private int mConnectedGoodTime;									// 定义connect连接完成后爆炸的animation时间
	
	private Point[] mIconBombPath;										// 用来存储bomb点的数组
	public int mIconBombedTime;										// 定义boom爆炸的animation的时间
	
	private List<Point> mIconHintedList;								// 用来存储hint点的数组
	public int mIconHintedTime;											// 定义hint显示的animation的时间
	
	private long mRemainGameTime;									// 游戏进度剩余时间
	private long mTotalGameTime;										// 游戏进度总时间
	
	public boolean mGameFreeze;										// 定义是否为freeze状态
	public int mGameFreezeStatus;										// 定义freeze的状态
	
	public HughieGameSurfaceBackground mBackSprite;		// 游戏的背景
	public HughieGameSurfaceTimer mTimerSprite;				// 游戏的timer
	
	// 游戏中，点击图标后的animation图片
	private int[] mGameSelectedAnimationImages = {R.drawable.imgv_game_surface_anim_select1, R.drawable.imgv_game_surface_anim_select2,
			R.drawable.imgv_game_surface_anim_select3, R.drawable.imgv_game_surface_anim_select4, R.drawable.imgv_game_surface_anim_select5};
	//定义hint显示的animation图片
	private int[] mGameHintedAnimationImages = {R.drawable.imgv_game_surface_anim_hint1, R.drawable.imgv_game_surface_anim_hint2, 
			R.drawable.imgv_game_surface_anim_hint3, R.drawable.imgv_game_surface_anim_hint4, R.drawable.imgv_game_surface_anim_hint5};
	// 定义单元格connected横线连线的animation图片
	private int[] mConnectedHorizontalAnimationImages = {R.drawable.imgv_game_surface_anim_horizontal1, R.drawable.imgv_game_surface_anim_horizontal2,
			R.drawable.imgv_game_surface_anim_horizontal3, R.drawable.imgv_game_surface_anim_horizontal4};
	// 定义单元格connected竖线连线的animation图片
	private int[] mConnectedVerticalAnimationImages = {R.drawable.imgv_game_surface_anim_vertical1, R.drawable.imgv_game_surface_anim_vertical2,
			R.drawable.imgv_game_surface_anim_vertical3, R.drawable.imgv_game_surface_anim_vertical4};
	// 定义单元格connected左上角连线的animation图片
	private int[] mConnectedTopLeftAnimationImages = {R.drawable.imgv_game_surface_anim_topleft1, R.drawable.imgv_game_surface_anim_topleft2,
			R.drawable.imgv_game_surface_anim_topleft3, R.drawable.imgv_game_surface_anim_topleft4};
	// 定义单元格connected右上角连线的animation图片
	private int[] mConnectedTopRightAnimationImages = {R.drawable.imgv_game_surface_anim_topright1, R.drawable.imgv_game_surface_anim_topright2,
			R.drawable.imgv_game_surface_anim_topright3, R.drawable.imgv_game_surface_anim_topright4};
	// 定义单元格connected左下角连线的animation图片
	private int[] mConnectedBottomLeftAnimationImages = {R.drawable.imgv_game_surface_anim_bottomleft1, R.drawable.imgv_game_surface_anim_bottomleft2,
			R.drawable.imgv_game_surface_anim_bottomleft3, R.drawable.imgv_game_surface_anim_bottomleft4};
	// 定义单元格connected右下角连线的animation图片
	private int[] mConnectedBottomRightAnimationImages = {R.drawable.imgv_game_surface_anim_bottomright1, R.drawable.imgv_game_surface_anim_bottomright2,
			R.drawable.imgv_game_surface_anim_bottomright3, R.drawable.imgv_game_surface_anim_bottomright4};
	// 定义获取分数后的animation图片
	private int[] mGameScoreAnimationImages = {R.drawable.imgv_game_surface_anim_score1, R.drawable.imgv_game_surface_anim_score2, R.drawable.imgv_game_surface_anim_score3,
			R.drawable.imgv_game_surface_anim_score4, R.drawable.imgv_game_surface_anim_score5, R.drawable.imgv_game_surface_anim_score6};
	// 定义单元格连接之后爆炸的animation图片
	private int[] mConnectedGoodAnimationImages = {R.drawable.imgv_game_surface_anim_connected_good1, R.drawable.imgv_game_surface_anim_connected_good2,
			R.drawable.imgv_game_surface_anim_connected_good3, R.drawable.imgv_game_surface_anim_connected_good4};
	// 定义boom爆炸的animation图片
	private int[] mGameBombedAnimationImages = {R.drawable.imgv_game_surface_anim_bomb1, R.drawable.imgv_game_surface_anim_bomb2, 
			R.drawable.imgv_game_surface_anim_bomb3, R.drawable.imgv_game_surface_anim_bomb4, R.drawable.imgv_game_surface_anim_bomb5, 
			R.drawable.imgv_game_surface_anim_bomb6, R.drawable.imgv_game_surface_anim_bomb7, R.drawable.imgv_game_surface_anim_bomb7, 
			R.drawable.imgv_game_surface_anim_bomb9, R.drawable.imgv_game_surface_anim_bomb10, R.drawable.imgv_game_surface_anim_bomb11, 
			R.drawable.imgv_game_surface_anim_bomb12, R.drawable.imgv_game_surface_anim_bomb13, R.drawable.imgv_game_surface_anim_bomb14};
	
	private OnGameSurfaceItemClickListener mOnGameSurfaceItemClickListener;			// 图片点击事件的监听器
	
	// 定义连连看连线节点图标绘制的选择状态
	public static final int ANIM_GAME_VIEW_HORIZONTAL = 1000;				// 横坐标animation显示
	public static final int ANIM_GAME_VIEW_VETICAL = 1001;						// 纵坐标animation显示
	public static final int ANIM_GAME_VIEW_TOPLEFT = 1002;						// 左上角转折animation显示
	public static final int ANIM_GAME_VIEW_TOPRIGHT = 1003;					// 右上角转折animation显示
	public static final int ANIM_GAME_VIEW_BOTTOMLEFT = 1004;				// 左下角转折animation显示
	public static final int ANIM_GAME_VIEW_BOTTOMRIGHT = 1005;			// 右下角转折animation显示
	
	// 定义game freeze的几种状态
	public static final int ANIM_GAME_FREEZE_NONE = 2000;			// 无冻结
	public static final int ANIM_GAME_FREEZE_SHOW = 2001;			// 显示冻结
	public static final int ANIM_GAME_FREEZE_ALERT = 2002;			// 冻结提示
	
	
	
	
	//游戏组的最大关卡
	public static final int s_LevelCount = 16;
	
	public static final int s_Up = 0;
	public static final int s_Down = 1;
	public static final int s_Left = 2;
	public static final int s_Right = 3;
	
	//定义游戏的关卡模式
	public static final int GAME_LEVEL_NORMAL = 1;				//正常模式
	public static final int GAME_LEVEL_ALLLEFT = 2;				//全部向左
	public static final int GAME_LEVEL_ALLRIGHT = 3;			//全部向右
	public static final int GAME_LEVEL_ALLUP = 4;				//全部向上
	public static final int GAME_LEVEL_ALLDOWN = 5;				//全部向下
	public static final int GAME_LEVEL_LEFTRIGHTLEAVE = 6;		//左右分离
	public static final int GAME_LEVEL_LEFTRIGHTFOLD = 7;		//左右合拢
	public static final int GAME_LEVEL_UPDOWNLEAVE = 8;			//上下分离
	public static final int GAME_LEVEL_UPDOWNFOLD = 9;			//上下合拢
	public static final int GAME_LEVEL_ALLUPLEFT = 10;			//全部向左上角靠拢
	public static final int GAME_LEVEL_ALLUPRIGHT = 11;			//全部向右上角靠拢
	public static final int GAME_LEVEL_ALLDOWNLEFT = 12;		//全部向左下角靠拢
	public static final int GAME_LEVEL_ALLDOWNRIGHT = 13;		//全部向右下角靠拢
	public static final int GAME_LEVEL_LEFTUPDOWNLEAVE = 14;	//向左的上下分离
	public static final int GAME_LEVEL_RIGHTUPDOWNFOLD = 15;	//向右的上下靠拢
	public static final int GAME_LEVEL_UPLEFTRIGHTLEAVE = 16;	//向上的左右分离
	
	public HughieGameSurfaceView(Context context) {
		super(context);
		
		initGameSurfaceData();
		
		//游戏的背景界面
		this.mBackSprite = new HughieGameSurfaceBackground();
		mBackSprite.initGameBackground(getContext());
		//游戏的timer显示
		this.mTimerSprite = new HughieGameSurfaceTimer();
		mTimerSprite.initGameTimer(mTotalGameTime, context.getApplicationContext(), true);
	}
	
	// 初始化相应数据
	private void initGameSurfaceData() {
		this.mOffsetX = 0;															// 横向偏移值默认为0
		this.mOffsetY = 0;															// 纵向偏移值默认为0
		this.mRemainGameTime = 0;											// 游戏进度的剩余时间默认为0
		this.mTotalGameTime = 120;											// 游戏进度的总时间设置为2分钟
		this.mIconSelectedList = new ArrayList<Point>();
		this.mIconHintedList = new ArrayList<Point>();
		this.mGameFreeze = false;												// 游戏默认为未冻结
		this.mGameFreezeStatus = ANIM_GAME_FREEZE_NONE;	// 游戏默认状态为无冻结		
	}
	
	/*
	 * map数组是游戏的布局数组，是带边界的。而边界如果和在界面中与其他位置统一来处理会浪费很多屏幕空间，
	 * 因此，通过mOffsetX，mOffsetY变量来调整。下面是用来计算mOffsetX和mOffsetY的
	 */ 
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		/*
		 * 如果changed为true，
		 * 横向偏移（即边界图片未能显示的部分）= icon图片宽度 - ((right - left)即手机屏幕（视图）的宽度 - 横向图片总数 * 图片宽度) / 2
		 * 如果不这么做那么左侧距离屏幕的距离应该为一个图片的宽度（即边界没用的0表示的位置），就浪费了屏幕空间，纵向同理
		 */
		if (changed) {
			mOffsetX = mIconWidth - (right - left - 10 - (mGameSurfaceMap.length - 2) * mIconWidth) / 2;
			mOffsetY = mIconHeight - (bottom - top - 75 - (mGameSurfaceMap[0].length - 2) * mIconHeight) / 2; 
		}
	}
	
	// 核心部分，重写onDraw()用以显示游戏界面
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		mBackSprite.drawGameSelf(canvas);
		mTimerSprite.drawGameSelf(canvas, (int)mRemainGameTime, mGameFreeze, mGameFreezeStatus);
		
		drawIconBitmaps(canvas);							//	绘制连连看单元格图片
		drawIconSelected(canvas);							// 绘制单元格选择的动画
		drawIconHinted(canvas);								// 绘制提示连连看单元格的动画
		
		// 表示mIconLinkedPath距离至少为2的情况下绘制连线（至少要两张图片才可以消除）
		if(mIconConnectedPath != null && mIconConnectedPath.length >= 2) {
			if(mIconBombPath == null)
				drawIconConnected(canvas);						// 绘制单元格连接的动画
			
			// 下面操作效果为：下次刷新时候将对应两个图片（最开始一个，最结尾一个）和连线都消除掉
			Point p = mIconConnectedPath[0];
			mGameSurfaceMap[p.x][p.y] = 0;
			p = mIconConnectedPath[mIconConnectedPath.length - 1];
			mGameSurfaceMap[p.x][p.y] = 0;
			//选择区清空
			mIconSelectedList.clear();
			
			if(mIconBombPath == null) {
				drawIconConnectedScore(canvas); 	
				drawIconConnectedGood(canvas, p);
			} else {
				drawIconBombed(canvas, p);
			}
		}
	}
	
	/**
	 * @title drawIconBitmaps
	 * @description 绘制连连看单元格图片
	 * @param canvas：画布
	 * @return
	 */
	private void drawIconBitmaps(Canvas canvas) {
		// 将图片一张一张的按规定大小，规定位置画到画布上
		for(int i = 0; i < mGameSurfaceMap.length; i++) {
			for(int j = 0; j < mGameSurfaceMap[i].length; j++) {
				if(mGameSurfaceMap[i][j] != 0) {
					// mGameSurfaceIcons为图片资源索引，其下标值要比真是值小1
					Bitmap mIconBitmap = mGameSurfaceIcons[mGameSurfaceMap[i][j] - 1];
					Point p = getIndex2Screen(i, j);
					canvas.drawBitmap(mIconBitmap, null, new Rect(p.x + i, p.y - 5 + j, p.x + i + mIconWidth, 
							p.y - 5 + j + mIconHeight), null);
				}
			}
		}
	}
	
	/**
	 * @title drawIconSelected
	 * @description 绘制单元格选择的动画
	 * @param canvas：画布
	 * @return
	 */
	private void drawIconSelected(Canvas canvas) {
		mIconSelectedTime %= 25;
		for(Point point : mIconSelectedList) {											//遍历点击了那两个点
			// 通过getIndex2Screen将对应图片在布局中的点坐标转换成在屏幕中的左上角的屏幕左坐标
			// 将当前点击屏幕像素x，y转换成对应图片的下标
			Point p = getIndex2Screen(point.x, point.y);
			// 在图片被选中的位置画一个与图片大小一致的矩形，给人高亮的感觉
			canvas.drawBitmap(((BitmapDrawable) (getResources().getDrawable(mGameSelectedAnimationImages[mIconSelectedTime / 5]))).getBitmap(), 
					null, new RectF(p.x + point.x - 0.08F * mIconWidth, p.y - 5 + point.y -0.08F * mIconHeight, p.x + point.x + 1.08F * mIconWidth, 
					p.y - 5 + point.y + 1.08F * mIconHeight), null);
		}
		mIconSelectedTime++;
	}
	
	/**
	 * @title drawIconHinted
	 * @description 绘制提示连连看单元格的动画
	 * @param canvas：画布
	 * @return
	 */
	private void drawIconHinted(Canvas canvas) {
		this.mIconHintedTime %= 25;
		for(Point position : mIconHintedList){
			// 通过方法inedx2screen将对应图片在布局中的点坐标转换成在屏幕中的左上角的屏幕点坐标
			// 将当前点击屏幕像素x,y转换成对应图片的下标
			Point p = getIndex2Screen(position.x, position.y);
			//绘制p1点的hint显示animation
			if(mGameSurfaceMap[position.x][position.y] != 0){
				canvas.drawBitmap(((BitmapDrawable)getResources().getDrawable(mGameHintedAnimationImages[mIconHintedTime / 5])).getBitmap(), null,
					new RectF(p.x + position.x - mIconWidth * 0.08F, p.y + 5 + position.y - mIconHeight * 0.08F, p.x + position.x + mIconWidth * 1.08F,
					p.y + 5 + position.y + mIconHeight * 1.08F), null);
			}
		}
		mIconHintedTime++;
	}
	
	/**
	 * @title drawIconConnected
	 * @description 绘制连连看单元格连接消除的动画
	 * @param canvas：画布
	 * @return
	 */
	private void drawIconConnected(Canvas canvas) {
		HughieLoggerManager.println("mIconConnectedTime: " + mIconConnectedTime);
		if(mIconConnectedTime < 24) {
			if(mIconConnectedTime < 12) {
				// 判断是否为两点可以直接相连
				if(mIconConnectedPath.length == 2) {
					drawTwoIconsConnected(canvas);
				} else if(mIconConnectedPath.length == 3) {
					drawThreeIconsConnected(canvas);
				} else if(mIconConnectedPath.length == 4) {
					drawFourIconsConnected(canvas);
				}
			}
			mIconConnectedTime++; 
		} else {
			mIconConnectedTime = 0;
		}
	}
	
	/**
	 * @title drawIconConnectedScore
	 * @description 绘制连连看连接消除后获取分数的动画
	 * @param canvas：画布
	 * @return
	 */
	private void drawIconConnectedScore(Canvas canvas) {
		if(mScoreAnimTime < 24) {
			mScoreAnimTime += 1;
			// 获取分数的动画
			if(mScoreAnimTime < 15) 
				canvas.drawBitmap(((BitmapDrawable) getResources().getDrawable(R.drawable.imgv_game_surface_addscore)).getBitmap(), 
						getIndex2Screen(mIconConnectedPath[mIconConnectedPath.length - 1].x, mIconConnectedPath[mIconConnectedPath.length - 1].y).x 
						+ mScoreAnimTime * (8 - mIconConnectedPath[mIconConnectedPath.length - 1].x) * mIconWidth / 15, 
						getIndex2Screen(mIconConnectedPath[mIconConnectedPath.length - 1].x, mIconConnectedPath[mIconConnectedPath.length - 1].y).y
						- mScoreAnimTime * mIconConnectedPath[mIconConnectedPath.length - 1].y * mIconHeight / 15, null);
			//	获取分数后的动画
			else if(mScoreAnimTime >= 15)
				canvas.drawBitmap(((BitmapDrawable) getResources().getDrawable(mGameScoreAnimationImages[(mScoreAnimTime - 15) / 2])).getBitmap(), 
						getIndex2Screen(8, 0).x, getIndex2Screen(8, 0).y - mIconHeight, null);
		} else {
			mScoreAnimTime = 0;
		}
	}
	
	/**
	 * @title drawIconConnectedGood
	 * @description 绘制连连看连接消除后爆炸的animation动画
	 * @param canvas：画布
	 * @param point
	 * @return
	 */
	private void drawIconConnectedGood(Canvas canvas, Point point) {
		if(mConnectedGoodTime < 24) {
			if(mConnectedGoodTime >= 12)
				drawConnectionGoodXY(mIconConnectedPath[0].x, mIconConnectedPath[0].y, mIconConnectedPath[mIconConnectedPath.length - 1].x, 
						mIconConnectedPath[mIconConnectedPath.length - 1].y, canvas);
				
			mConnectedGoodTime += 1;
		} else {
			mConnectedGoodTime = 0;
			tidyGameMap(mIconConnectedPath[0], point);
			mIconConnectedPath = null;
		}
	}
	
	/**
	 * @title drawIconBombed
	 * @description 绘制连连看单元格爆炸的动画
	 * @param canvas：画布
	 * @param point
	 * @return
	 */
	private void drawIconBombed(Canvas canvas, Point point) {
		if(mIconBombedTime < 42){
			//绘制p1点的炸弹animation
			canvas.drawBitmap(((BitmapDrawable)getResources().getDrawable(mGameBombedAnimationImages[mIconBombedTime / 3])).getBitmap(), null,
				new RectF(getIndex2Screen(mIconConnectedPath[0].x, mIconConnectedPath[0].y).x - mIconWidth * 0.2F, 
				getIndex2Screen(mIconConnectedPath[0].x, mIconConnectedPath[0].y).y + 5 - mIconHeight * 0.2F,
				getIndex2Screen(mIconConnectedPath[0].x, mIconConnectedPath[0].y).x + mIconWidth * 1.3F,
				getIndex2Screen(mIconConnectedPath[0].x, mIconConnectedPath[0].y).y + 5 + mIconHeight * 1.3F), null);
			//绘制p2点的炸弹animation
			canvas.drawBitmap(((BitmapDrawable)getResources().getDrawable(mGameBombedAnimationImages[mIconBombedTime / 3])).getBitmap(), null, 
				new RectF(getIndex2Screen(mIconConnectedPath[1].x, mIconConnectedPath[1].y).x - mIconWidth * 0.2F, 
				getIndex2Screen(mIconConnectedPath[1].x, mIconConnectedPath[1].y).y + 5 - mIconHeight * 0.2F, 
				getIndex2Screen(mIconConnectedPath[1].x, mIconConnectedPath[1].y).x + mIconWidth * 1.3F, 
				getIndex2Screen(mIconConnectedPath[1].x, mIconConnectedPath[1].y).y + 5 + mIconHeight * 1.3F), null);
			mIconBombedTime++;
		}else{
			mIconBombedTime = 0;
			mIconBombPath = null;
			tidyGameMap(mIconConnectedPath[0], point);
			mIconConnectedPath = null;
		}
	}
	
	/**
	 * @title drawTwoIconsConnected
	 * @description 绘制只有两个点的连连看连接动画
	 * @param canvas：画布
	 * @return
	 */
	private void drawTwoIconsConnected(Canvas canvas) {
		//横向
		if(mIconConnectedPath[0].y == mIconConnectedPath[1].y) {
			//绘制点point1
			drawConnectionXYPoint(mIconConnectedPath[0].x, mIconConnectedPath[0].y, ANIM_GAME_VIEW_HORIZONTAL, canvas);
			//绘制点point2
			drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_HORIZONTAL, canvas);
		}
		
		//纵向
		if(mIconConnectedPath[0].x == mIconConnectedPath[1].x) {
			//绘制点point1
			drawConnectionXYPoint(mIconConnectedPath[0].x, mIconConnectedPath[0].y, ANIM_GAME_VIEW_VETICAL, canvas);
			//绘制点point2
			drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_VETICAL, canvas);
		}
		
		//绘制除节点外的连线
		drawConnectionXY(mIconConnectedPath[0].x, mIconConnectedPath[0].y, mIconConnectedPath[1].x, mIconConnectedPath[1].y, canvas);
	}
	
	/**
	 * @title drawThreeIconsConnected
	 * @description 绘制只有三个点的连连看连接动画
	 * @param canvas：画布
	 * @return
	 */
	private void drawThreeIconsConnected(Canvas canvas) {
		//绘制点point1，首先需要根据起始点和转折点之间是横坐标相同还是纵坐标相同，来判断point1是横向还是纵向
		if(mIconConnectedPath[0].y == mIconConnectedPath[1].y)
			drawConnectionXYPoint(mIconConnectedPath[0].x, mIconConnectedPath[0].y, ANIM_GAME_VIEW_HORIZONTAL, canvas);
		else if(mIconConnectedPath[0].x == mIconConnectedPath[1].x)
			drawConnectionXYPoint(mIconConnectedPath[0].x, mIconConnectedPath[0].y, ANIM_GAME_VIEW_VETICAL, canvas);
		
		//绘制转折点，需要通过point1和point2的纵坐标来进行判断，即point1在point2的上面还是下面
		//point1在point2上面
		if(mIconConnectedPath[0].y < mIconConnectedPath[2].y) {
			// 左上角转折动画，转折点和point1纵坐标相等，和pont2横坐标相等，且point1在point2的右边
			if(mIconConnectedPath[1].y == mIconConnectedPath[0].y && mIconConnectedPath[1].x == mIconConnectedPath[2].x &&
					mIconConnectedPath[0].x > mIconConnectedPath[2].x) {
				drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_TOPLEFT, canvas);
			// 右上角转折动画，转折点和point1纵坐标相等，和pont2横坐标相等，且point1在point2的左边
			} else if(mIconConnectedPath[1].y == mIconConnectedPath[0].y && mIconConnectedPath[1].x == mIconConnectedPath[2].x &&
					mIconConnectedPath[0].x < mIconConnectedPath[2].x) {
				drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_TOPRIGHT, canvas);
			// 左下角转折动画，转折点和point1横坐标相等，和pont2纵坐标相等，且point1在point2的左边
			} else if(mIconConnectedPath[1].x == mIconConnectedPath[0].x && mIconConnectedPath[1].y == mIconConnectedPath[2].y &&
					mIconConnectedPath[0].x < mIconConnectedPath[2].x) {
				drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_BOTTOMLEFT, canvas);
			// 右下角转折动画，转折点和point1横坐标相等，和pont2纵坐标相等，且point1在point2的右边
			} else if(mIconConnectedPath[1].x == mIconConnectedPath[0].x && mIconConnectedPath[1].y == mIconConnectedPath[2].y &&
					mIconConnectedPath[0].x > mIconConnectedPath[2].x) {
				drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_BOTTOMRIGHT, canvas);
			}
		//point1在point2下面
		} else if(mIconConnectedPath[0].y > mIconConnectedPath[2].y) {
			// 左上角转折动画，转折点和point1横坐标相等，和pont2纵坐标相等，且point1在point2的左边
			if(mIconConnectedPath[1].x == mIconConnectedPath[0].x && mIconConnectedPath[1].y == mIconConnectedPath[2].y &&
					mIconConnectedPath[0].x < mIconConnectedPath[2].x) {
				drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_TOPLEFT, canvas);
			// 右上角转折动画，转折点和point1横坐标相等，和pont2纵坐标相等，且point1在point2的右边
			} else if(mIconConnectedPath[1].x == mIconConnectedPath[0].x && mIconConnectedPath[1].y == mIconConnectedPath[2].y &&
					mIconConnectedPath[0].x > mIconConnectedPath[2].x) {
				drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_TOPRIGHT, canvas);
			// 左下角转折动画，转折点和point1纵坐标相等，和pont2横坐标相等，且point1在point2的右边
			} else if(mIconConnectedPath[1].y == mIconConnectedPath[0].y && mIconConnectedPath[1].x == mIconConnectedPath[2].x &&
					mIconConnectedPath[0].x > mIconConnectedPath[2].x) {
				drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_BOTTOMLEFT, canvas);
			// 右下角转折动画，转折点和point1纵坐标相等，和point2横坐标相等，且point1在point2的左边
			} else if(mIconConnectedPath[1].y == mIconConnectedPath[0].y && mIconConnectedPath[1].x == mIconConnectedPath[2].x &&
					mIconConnectedPath[0].x < mIconConnectedPath[2].x) {
				drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_BOTTOMRIGHT, canvas);
			}
		}
		
		// 绘制点point2，首先需要根据起始点和转折点之间是横坐标相同还是纵坐标相同，来判断point2是横向还是纵向
		if(mIconConnectedPath[1].y == mIconConnectedPath[2].y)
			drawConnectionXYPoint(mIconConnectedPath[2].x, mIconConnectedPath[2].y, ANIM_GAME_VIEW_HORIZONTAL, canvas);
		else if(mIconConnectedPath[1].x == mIconConnectedPath[2].x)
			drawConnectionXYPoint(mIconConnectedPath[2].x, mIconConnectedPath[2].y, ANIM_GAME_VIEW_VETICAL, canvas);
		
		// 绘制除节点外的连线
		drawConnectionXY(mIconConnectedPath[0].x, mIconConnectedPath[0].y, mIconConnectedPath[1].x, mIconConnectedPath[1].y, canvas);
		drawConnectionXY(mIconConnectedPath[1].x, mIconConnectedPath[1].y, mIconConnectedPath[2].x, mIconConnectedPath[2].y, canvas);
	}
	
	/**
	 * @title drawFourIconsConnected
	 * @description 绘制只有四个点的连连看连接动画
	 * @param canvas：画布
	 * @return
	 */
	private void drawFourIconsConnected(Canvas canvas) {
		// 绘制点point1, 首先判断是水平延伸还是竖直延伸
		// 水平延伸
		if(mIconConnectedPath[0].y == mIconConnectedPath[1].y) 
			drawConnectionXYPoint(mIconConnectedPath[0].x, mIconConnectedPath[0].y, ANIM_GAME_VIEW_HORIZONTAL, canvas);
		// 竖直延伸
		else if(mIconConnectedPath[0].x == mIconConnectedPath[1].x) 
			drawConnectionXYPoint(mIconConnectedPath[0].x, mIconConnectedPath[0].y, ANIM_GAME_VIEW_VETICAL, canvas);
		
		// 绘制转折点mGamePointE1，判断point1在mGamePointE2的上方还是下方
		// point1在mGamePointE2的上方
		if(mIconConnectedPath[0].y < mIconConnectedPath[2].y) {
			//左上角转折，转折点mGamePointE1和point1的纵坐标相等，和转折点mGamePointE2的横坐标相等，且point1在转折点mGamePointE2的右边
			if(mIconConnectedPath[1].y == mIconConnectedPath[0].y && mIconConnectedPath[1].x == mIconConnectedPath[2].x &&
					mIconConnectedPath[0].x > mIconConnectedPath[2].x) {
				drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_TOPLEFT, canvas);
			//右上角转折，转折点mGamePointE1和point1的纵坐标相等，和转折点mGamePointE2的横坐标相等，且point1在转折点mGamePointE2的左边
			} else if(mIconConnectedPath[1].y == mIconConnectedPath[0].y && mIconConnectedPath[1].x == mIconConnectedPath[2].x &&
					mIconConnectedPath[0].x < mIconConnectedPath[2].x) {
				drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_TOPRIGHT, canvas);
			//左下角转折，转折点mGamePointE1和point1的横坐标相等，和转折点mGamePointE2的纵坐标相等，且point1在转折点mGamePointE2的左边
			} else if(mIconConnectedPath[1].x == mIconConnectedPath[0].x && mIconConnectedPath[1].y == mIconConnectedPath[2].y &&
					mIconConnectedPath[0].x < mIconConnectedPath[2].x) {
				drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_BOTTOMLEFT, canvas);
			// 右下角转折，转折点mGamePointE1和point1的横坐标相等，和转折点mGamePointE2的纵坐标相等，且point1在转折点mGamePointE2的右边
			} else if(mIconConnectedPath[1].x == mIconConnectedPath[0].x && mIconConnectedPath[1].y == mIconConnectedPath[1].y &&
					mIconConnectedPath[0].x > mIconConnectedPath[2].x) {
				drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_BOTTOMRIGHT, canvas);
			}
		// point1在mGamePointE2的下方
		} else if(mIconConnectedPath[0].y > mIconConnectedPath[2].y) {
			//左上角转折，转折点mGamePointE1和point1的横坐标相等，和转折点mGamePointE2的纵坐标相等，且point1在转折点mGamePointE2的左边
			if(mIconConnectedPath[1].x == mIconConnectedPath[0].x && mIconConnectedPath[1].y == mIconConnectedPath[2].y &&
					mIconConnectedPath[0].x < mIconConnectedPath[2].x) {
				drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_TOPLEFT, canvas);
			// 右上角转折，转折点mGamePointE1和point1的横坐标相等，和转折点mGamePointE2的纵坐标相等，且point1在转折点mGamePointE2的右边
			} else if(mIconConnectedPath[1].x == mIconConnectedPath[0].x && mIconConnectedPath[1].y == mIconConnectedPath[2].y &&
					mIconConnectedPath[0].x > mIconConnectedPath[2].x) {
				drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].x, ANIM_GAME_VIEW_TOPRIGHT, canvas);
			// 左下角转折，转折点mGamePointE1和point1的纵坐标相等，和转折点mGamePointE2的横坐标相等，且point1在转折点mGamePointE2的右边
			} else if(mIconConnectedPath[1].y == mIconConnectedPath[0].y && mIconConnectedPath[1].x == mIconConnectedPath[2].x &&
					mIconConnectedPath[0].x > mIconConnectedPath[2].x) {
				drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_BOTTOMLEFT, canvas);
			// 右下角转折，转折点mGamePointE1和point1的纵坐标相等，和转折点mGamePointE2的横坐标相等，且point1在转折点mGamePointE2的左边
			} else if(mIconConnectedPath[1].y == mIconConnectedPath[0].y && mIconConnectedPath[1].x == mIconConnectedPath[2].x &&
					mIconConnectedPath[0].x < mIconConnectedPath[2].x) {
				drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_BOTTOMRIGHT, canvas);
			}
		}
		
		// 绘制转折点mGamePointE2，判断mGamePointE1在point2的上方还是下方
		// mGamePointE1在point2的上方
		if(mIconConnectedPath[1].y < mIconConnectedPath[3].y) {
			// 左上角转折，转折点mGamePointE2和转折点mGamePointE1的纵坐标相等，和终止点point2的横坐标相等，且转折点mGamePointE1在终止点point2的右边
			if(mIconConnectedPath[2].y == mIconConnectedPath[1].y && mIconConnectedPath[2].x == mIconConnectedPath[3].x &&
					mIconConnectedPath[1].x > mIconConnectedPath[3].x) {
				drawConnectionXYPoint(mIconConnectedPath[2].x, mIconConnectedPath[2].y, ANIM_GAME_VIEW_TOPLEFT, canvas);
			// 右上角转折，转折点mGamePointE2和转折点mGamePointE1的纵坐标相等，和终止点point2的横坐标相等，且转折点mGamePointE1在终止点point2的左边
			} else if(mIconConnectedPath[2].y == mIconConnectedPath[1].y && mIconConnectedPath[2].x == mIconConnectedPath[3].x &&
					mIconConnectedPath[1].x < mIconConnectedPath[3].x) {
				drawConnectionXYPoint(mIconConnectedPath[2].x, mIconConnectedPath[2].y, ANIM_GAME_VIEW_TOPRIGHT, canvas);
			// 左下角转折，转折点mGamePointE2和转折点mGamePointE1的横坐标相等，和终止点point2的纵坐标相等，且转折点mGamePointE1在终止点point2的左边
			} else if(mIconConnectedPath[2].x == mIconConnectedPath[1].x && mIconConnectedPath[2].y == mIconConnectedPath[3].y &&
					mIconConnectedPath[1].x < mIconConnectedPath[3].x) {
				drawConnectionXYPoint(mIconConnectedPath[2].x, mIconConnectedPath[2].y, ANIM_GAME_VIEW_BOTTOMLEFT, canvas);
			// 右下角转折，转折点mGamePointE2和转折点mGamePointE1的横坐标相等，和终止点point2的纵坐标相等，且转折点mGamePointE1在终止点point2的右边
			} else if(mIconConnectedPath[2].x == mIconConnectedPath[1].x && mIconConnectedPath[2].y == mIconConnectedPath[3].y &&
					mIconConnectedPath[1].x > mIconConnectedPath[3].x) {
				drawConnectionXYPoint(mIconConnectedPath[2].x, mIconConnectedPath[2].y, ANIM_GAME_VIEW_BOTTOMRIGHT, canvas);
			}
		// mGamePointE1在point2的下方
		} else if(mIconConnectedPath[1].y > mIconConnectedPath[3].y) {
			// 左上角转折，转折点mGamePointE2和转折点mGamePointE1的横坐标相等，和终止点point2的纵坐标相等，且转折点mGamePointE1在终止点point2的左边
			if(mIconConnectedPath[2].x == mIconConnectedPath[1].x && mIconConnectedPath[2].y == mIconConnectedPath[3].y &&
					mIconConnectedPath[1].x < mIconConnectedPath[3].x) {
				drawConnectionXYPoint(mIconConnectedPath[2].x, mIconConnectedPath[2].y, ANIM_GAME_VIEW_TOPLEFT, canvas);
			// 右上角转折，转折点mGamePointE2和转折点mGamePointE1的横坐标相等，和终止点point2的纵坐标相等，且转折点mGamePointE1在终止点point2的右边
			} else if(mIconConnectedPath[2].x == mIconConnectedPath[1].x && mIconConnectedPath[2].y == mIconConnectedPath[3].y &&
					mIconConnectedPath[1].x > mIconConnectedPath[3].x) {
				drawConnectionXYPoint(mIconConnectedPath[2].x, mIconConnectedPath[2].y, ANIM_GAME_VIEW_TOPRIGHT, canvas);
			// 左下角转折，转折点mGamePointE2和转折点mGamePointE1的纵坐标相等，和终止点point2的横坐标相等，且转折点mGamePointE1在终止点point2的右边
			} else if(mIconConnectedPath[2].y == mIconConnectedPath[1].y && mIconConnectedPath[2].x == mIconConnectedPath[3].x &&
					mIconConnectedPath[1].x > mIconConnectedPath[3].x) {
				drawConnectionXYPoint(mIconConnectedPath[2].x, mIconConnectedPath[2].y, ANIM_GAME_VIEW_BOTTOMLEFT, canvas);
			// 右下角转折，转折点mGamePointE2和转折点mGamePointE1的纵坐标相等，和终止点point2的横坐标相等，且转折点mGamePointE1在终止点point2的左边
			} else if(mIconConnectedPath[2].y == mIconConnectedPath[1].y && mIconConnectedPath[2].x == mIconConnectedPath[3].x &&
					mIconConnectedPath[1].x < mIconConnectedPath[3].x) {
				drawConnectionXYPoint(mIconConnectedPath[2].x, mIconConnectedPath[2].y, ANIM_GAME_VIEW_BOTTOMRIGHT, canvas);
			}
		}
		
		// 绘制点point2，首先判断是水平延伸还是竖直延伸
		// 水平延伸
		if(mIconConnectedPath[2].y == mIconConnectedPath[3].y) 
			drawConnectionXYPoint(mIconConnectedPath[3].x, mIconConnectedPath[3].y, ANIM_GAME_VIEW_HORIZONTAL, canvas);
		// 竖直延伸
		else if(mIconConnectedPath[2].x == mIconConnectedPath[3].x) 
			drawConnectionXYPoint(mIconConnectedPath[3].x, mIconConnectedPath[3].y, ANIM_GAME_VIEW_VETICAL, canvas);
		
		// 绘制除节点外的连线（包含3条折线）
		drawConnectionXY(mIconConnectedPath[0].x, mIconConnectedPath[0].y, mIconConnectedPath[1].x, mIconConnectedPath[1].y, canvas);
		drawConnectionXY(mIconConnectedPath[1].x, mIconConnectedPath[1].y, mIconConnectedPath[2].x, mIconConnectedPath[2].y, canvas);
		drawConnectionXY(mIconConnectedPath[2].x, mIconConnectedPath[2].y, mIconConnectedPath[3].x, mIconConnectedPath[3].y, canvas);
	}
	
	/**
	 * @title drawConnectionXY
	 * @description 绘制两个点之间连线的animation，（除去两个点之前的连线）
	 * @param pointX1：起始点的横坐标
	 * @param pointY1：起始点的纵坐标
	 * @param pointX2：终止点的横坐标
	 * @param pointY2：终止点的纵坐标
	 * @param canvas： 画布
	 * @return
	 */
	private void drawConnectionXY(int pointX1, int pointY1, int pointX2, int pointY2, Canvas canvas) {
		//横向
		for(int i = pointX1 + 1; i < pointX2; i++) {
			canvas.drawBitmap(((BitmapDrawable) getResources().getDrawable(mConnectedHorizontalAnimationImages[mIconConnectedTime % 4])).getBitmap(), 
					null, new RectF(getIndex2Screen(i, pointY1).x, getIndex2Screen(pointX1, pointY1).y, getIndex2Screen(i, pointY1).x + mIconWidth, 
					getIndex2Screen(pointX1, pointY1).y + mIconHeight), null);
		}
		
		for(int i = pointX2 + 1; i < pointX1; i++) {
			canvas.drawBitmap(((BitmapDrawable) getResources().getDrawable(mConnectedHorizontalAnimationImages[mIconConnectedTime % 4])).getBitmap(), 
					null, new RectF(getIndex2Screen(i, pointY1).x, getIndex2Screen(pointX1, pointY1).y, getIndex2Screen(i, pointY1).x + mIconWidth, 
					getIndex2Screen(pointX1, pointY1).y + mIconHeight), null);
		}
		
		//纵向
		for(int i = pointY1 + 1; i < pointY2; i++) {
			canvas.drawBitmap(((BitmapDrawable) getResources().getDrawable(mConnectedVerticalAnimationImages[mIconConnectedTime % 4])).getBitmap(), 
					null, new RectF(getIndex2Screen(pointX1, pointY1).x + 5, getIndex2Screen(pointX1, i).y + 5, getIndex2Screen(pointX1, pointY1).x + 5 + mIconWidth, 
					getIndex2Screen(pointX1, i).y + 5 + mIconHeight), null);
		}
		
		for(int i = pointY2 + 1; i < pointY1; i++) {
			canvas.drawBitmap(((BitmapDrawable) getResources().getDrawable(mConnectedVerticalAnimationImages[mIconConnectedTime % 4])).getBitmap(), 
					null, new RectF(getIndex2Screen(pointX1, pointY1).x + 5, getIndex2Screen(pointX1, i).y + 5, getIndex2Screen(pointX1, pointY1).x + 5 + mIconWidth, 
					getIndex2Screen(pointX1, i).y + 5 + mIconHeight), null);
		}
	}
	
	/**
	 * @title drawConnectionXYPoint
	 * @description 绘制两个点的animation
	 * @param pointX：横坐标
	 * @param pointY： 总坐标
	 * @param direction：方向
	 * @param canvas：画布
	 * @return
	 */
	private void drawConnectionXYPoint(int pointX, int pointY, int direction, Canvas canvas) {
		switch(direction) {
			// 横向
			case ANIM_GAME_VIEW_HORIZONTAL:
				canvas.drawBitmap(((BitmapDrawable) getResources().getDrawable(mConnectedHorizontalAnimationImages[mIconConnectedTime % 4])).getBitmap(), 
						null, new RectF(getIndex2Screen(pointX, pointY).x, getIndex2Screen(pointX, pointY).y, getIndex2Screen(pointX, pointY).x + mIconWidth, 
						getIndex2Screen(pointX, pointY).y + mIconHeight), null);
				break;
			// 纵向
			case ANIM_GAME_VIEW_VETICAL:
				canvas.drawBitmap(((BitmapDrawable) getResources().getDrawable(mConnectedVerticalAnimationImages[mIconConnectedTime % 4])).getBitmap(), 
						null, new RectF(getIndex2Screen(pointX, pointY).x + 5, getIndex2Screen(pointX, pointY).y + 5, getIndex2Screen(pointX, pointY).x + 5 + mIconWidth, 
						getIndex2Screen(pointX, pointY).y + 5 + mIconHeight), null);
				break;
			// 左上角
			case ANIM_GAME_VIEW_TOPLEFT:
				canvas.drawBitmap(((BitmapDrawable) getResources().getDrawable(mConnectedTopLeftAnimationImages[mIconConnectedTime % 4])).getBitmap(), 
						null, new RectF(getIndex2Screen(pointX, pointY).x + 0.5F, getIndex2Screen(pointX, pointY).y + 5.5F, getIndex2Screen(pointX, pointY).x + 0.5F + mIconWidth, 
						getIndex2Screen(pointX, pointY).y + 5.5F + mIconHeight), null);
				break;
			// 右上角
			case ANIM_GAME_VIEW_TOPRIGHT:
				canvas.drawBitmap(((BitmapDrawable) getResources().getDrawable(mConnectedTopRightAnimationImages[mIconConnectedTime % 4])).getBitmap(), 
						null, new RectF(getIndex2Screen(pointX, pointY).x + 0.5F, getIndex2Screen(pointX, pointY).y + 5.5F, getIndex2Screen(pointX, pointY).x + 0.5F + mIconWidth, 
						getIndex2Screen(pointX, pointY).y + 5.5F + mIconHeight), null);
				break;
			// 左下角
			case ANIM_GAME_VIEW_BOTTOMLEFT:
				canvas.drawBitmap(((BitmapDrawable) getResources().getDrawable(mConnectedBottomLeftAnimationImages[mIconConnectedTime % 4])).getBitmap(), 
						null, new RectF(getIndex2Screen(pointX, pointY).x + 0.5F, getIndex2Screen(pointX, pointY).y + 5.5F, getIndex2Screen(pointX, pointY).x + 0.5F + mIconWidth, 
						getIndex2Screen(pointX, pointY).y + 5.5F + mIconHeight), null);
				break;
			// 右下角
			case ANIM_GAME_VIEW_BOTTOMRIGHT:
				canvas.drawBitmap(((BitmapDrawable) getResources().getDrawable(mConnectedBottomRightAnimationImages[mIconConnectedTime % 4])).getBitmap(), 
						null, new RectF(getIndex2Screen(pointX, pointY).x + 0.5F, getIndex2Screen(pointX, pointY).y + 5.5F, getIndex2Screen(pointX, pointY).x + 0.5F + mIconWidth, 
						getIndex2Screen(pointX, pointY).y + 5.5F + mIconHeight), null);
				break;
			default:
				canvas.drawBitmap(((BitmapDrawable) getResources().getDrawable(mConnectedHorizontalAnimationImages[mIconConnectedTime % 4])).getBitmap(), 
						null, new RectF(getIndex2Screen(pointX, pointY).x, getIndex2Screen(pointX, pointY).y, getIndex2Screen(pointX, pointY).x + mIconWidth, 
						getIndex2Screen(pointX, pointY).y + mIconHeight), null);
				break;
		}
	}
	
	/**
	 * @title drawConnectionGoodXY
	 * @description 绘制两个点连接消除后爆炸的animation动画
	 * @param pointX1：起始点的横坐标
	 * @param pointY1：起始点的纵坐标
	 * @param pointX2：终止点的横坐标
	 * @param pointY2：终止点的纵坐标
	 * @param canvas：画布
	 * @return
	 */
	private void drawConnectionGoodXY(int pointX1, int pointY1, int pointX2, int pointY2, Canvas canvas) {
		// 绘制point1连接后爆炸的animation动画
		canvas.drawBitmap(((BitmapDrawable) getResources().getDrawable(mConnectedGoodAnimationImages[(mConnectedGoodTime - 12) / 4])).getBitmap(), 
				null, new RectF(getIndex2Screen(pointX1, pointY1).x, getIndex2Screen(pointX1, pointY1).y - 10.0F, getIndex2Screen(pointX1, pointY1).x + 1.3F * mIconWidth, 
				getIndex2Screen(pointX1, pointY1).y - 10.0F  + 1.3F * mIconHeight), null);
		// 绘制point2连接后爆炸的animation动画
		canvas.drawBitmap(((BitmapDrawable) getResources().getDrawable(mConnectedGoodAnimationImages[(mConnectedGoodTime - 12) / 4])).getBitmap(), 
				null, new RectF(getIndex2Screen(pointX2, pointY2).x, getIndex2Screen(pointX2, pointY2).y - 10.0F, getIndex2Screen(pointX2, pointY2).x + 1.3F * mIconWidth, 
				getIndex2Screen(pointX2, pointY2).y - 10.0F + 1.3F * mIconHeight), null);
	}
	
	/**
	 * @title getIndex2Screen
	 * @description 将对应图片在布局中的点坐标转换成在屏幕中的左上角的屏幕点坐标
	 * @param x：对应图片在布局中的点坐标的横坐标
	 * @param y:  对应图片在布局中的点坐标的纵坐标
	 * @return Point
	 */
	private Point getIndex2Screen(int x, int y) {
		return new Point(x * mIconWidth - mOffsetX, y * mIconHeight - mOffsetY);
	}
	
	/**
	 * @title getScreen2Index
	 * @description 将屏幕坐标与图片在布局中的屏幕像素坐标相互转换
	 * 						根据图片左上角屏幕的像素点坐标（x,y）,得到该图片在二维数组中的下标值
	 * @param x：图片左上角屏幕的像素点坐标的横坐标
	 * @param y:  图片左上角屏幕的像素点坐标的纵坐标
	 * @return Point
	 */
	private Point getScreen2Index(int x, int y) {
		return new Point((x + mOffsetX) / mIconWidth, (y + mOffsetY) / mIconHeight);
	}
	
	/**
	 * @title onTouchEvent
	 * @description 监听用户点击了那个单元格图片，得到图片在mGameSurfaceMap数组中的下标值
	 * @param event：event事件
	 * @return boolean
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() != MotionEvent.ACTION_DOWN) 
			return false;
		
		// 获取点击的(x, y)坐标值
		int mTouchX = (int) event.getX();
		int mTouchY = (int) event.getY();
		
		// 得到点击单元格图片在mGameSurfaceMap数组中的下标值
		Point p = getScreen2Index(mTouchX, mTouchY);
		// 如果鼠标点击在外部空白处，则无效
		if(p.x < 1 || p.x > mGameSurfaceMap.length - 1 || p.y < 1 || p.y > mGameSurfaceMap[0].length - 1)
			return false;
		
		if(mGameSurfaceMap[p.x][p.y] != 0 && mOnGameSurfaceItemClickListener != null)
			mOnGameSurfaceItemClickListener.onGameSurfaceItemClick(p);
		
		return true;
	}
	
	/**
	 * @title drawGameProgress
	 * @description 设置游戏进度时间
	 * @param remainGameTime：剩余时间
	 * @param totalGameTime: 总时间
	 * @return
	 */
	public void drawGameProgress(long remainGameTime, long totalGameTime) {
		this.mRemainGameTime = remainGameTime;
		this.mTotalGameTime = totalGameTime;
		invalidate();
	}
	
	/**
	 * @title drawGameConnectedPath
	 * @description 设置游戏连接消除的点坐标集合
	 * @param connectedPth：游戏连接消除的点坐标数组
	 * @return
	 */
	public void drawGameConnectedPath(Point[] connectedPath) {
		this.mIconConnectedPath = connectedPath;
		// 这个刷新有时间延迟以达到动画效果
		invalidate();
	}
	
	/**
	 * @title drawGameBombedPath
	 * @description 设置游戏炸弹的点坐标集合
	 * @param bombedPath：游戏炸弹的点坐标数组
	 * @return
	 */
	public void drawGameBombedPath(Point[] bombedPath) {
		this.mIconConnectedPath = bombedPath;
		this.mIconBombPath = bombedPath;
		// 这个刷新有时间延迟以达到动画效果
		invalidate();
	}
	
	/**
	 * @title drawGameHintedPath
	 * @description 设置游戏提示的点坐标集合
	 * @param hintedPath：游戏提示的点坐标数组
	 * @return
	 */
	public void drawGameHintedPath(Point[] hintedPath) {
		mIconHintedList.add(hintedPath[0]);
		mIconHintedList.add(hintedPath[hintedPath.length - 1]);
		invalidate();
	}
	
	//判断不同的关卡难度
	public void tidyGameMap(Point p1, Point p2){
		switch ((mGameCurrentLevel - 1) % s_LevelCount + 1){
			case GAME_LEVEL_NORMAL:
				break;
			// 全部向左
			case GAME_LEVEL_ALLLEFT:
				AllGameLeft(p1, p2);
				break;
			//全部向右
			case GAME_LEVEL_ALLRIGHT:
				AllGameRight(p1, p2);
				break;
			//全部向上
			case GAME_LEVEL_ALLUP:
				AllGameUp(p1, p2);
				break;
			//全部向下
			case GAME_LEVEL_ALLDOWN:
				AllGameDown(p1, p2);
				break;
			//左右分离
			case GAME_LEVEL_LEFTRIGHTLEAVE:
				GameLeftRightLeave(p1, p2);
				break;
			//左右合拢
			case GAME_LEVEL_LEFTRIGHTFOLD:
				GameLeftRightFold(p1, p2);
				break;
			//上下分离
			case GAME_LEVEL_UPDOWNLEAVE:
				GameUpDownLeave(p1, p2);
				break;
			//上下合拢
			case GAME_LEVEL_UPDOWNFOLD:
				GameUpDownFold(p1, p2);
				break;
			//全部向左上角靠拢
			case GAME_LEVEL_ALLUPLEFT:
				AllGameUpLeft(p1, p2);
				break;
			//全部向右上角靠拢
			case GAME_LEVEL_ALLUPRIGHT:
				AllGameUpRight(p1, p2);
				break;
			//全部向左下角靠拢
			case GAME_LEVEL_ALLDOWNLEFT:
				AllGameDownLeft(p1, p2);
				break;
			//全部向右下角靠拢
			case GAME_LEVEL_ALLDOWNRIGHT:
				AllGameDownRight(p1, p2);
				break;
			//向左的上下分离
			case GAME_LEVEL_LEFTUPDOWNLEAVE:
				GameLeftUpDownLeave(p1, p2);
				break;
			//向右的上下靠拢
			case GAME_LEVEL_RIGHTUPDOWNFOLD:
				GameRightUpDownFold(p1, p2);
				break;
			//向上的左右分离
			case GAME_LEVEL_UPLEFTRIGHTLEAVE:
				GameUpLeftRightLeave(p1, p2);
				break;
		}
	}
	
	//全部向左
	public void AllGameLeft(Point p1, Point p2){
		int nGameColumn = mGameSurfaceMap.length - 2;
		List<Point> lGamePoint = new ArrayList<Point>();
		// x = 1 为看齐线
		if (p1.x > p2.x){
			lGamePoint.add(p1);
			lGamePoint.add(p2);
		}else{
			lGamePoint.add(p2);
			lGamePoint.add(p1);
		}
		
		for (int k = 0; k < lGamePoint.size(); k++){
			Point p = lGamePoint.get(k);
			MovePoints(p, s_Right, nGameColumn);
		}
	}
	
	//全部向右
	public void AllGameRight(Point p1, Point p2){
		List<Point> lGamePoint = new ArrayList<Point>();
		// x = 1 为看齐线
		if (p1.x < p2.x){
			lGamePoint.add(p1);
			lGamePoint.add(p2);
		}else{
			lGamePoint.add(p2);
			lGamePoint.add(p1);
		}
		
		for(int k = 0; k < lGamePoint.size(); k++){
			Point p = lGamePoint.get(k);
			MovePoints(p, s_Left, 0);
		}
	}
	
	//全部向上
	public void AllGameUp(Point p1, Point p2){
		int nGameRow = mGameSurfaceMap[0].length - 2;
		List<Point> lGamePoint = new ArrayList<Point>();
		// y = 1 为看齐线
		if(p1.y > p2.y){
			lGamePoint.add(p1);
			lGamePoint.add(p2);
		}else{
			lGamePoint.add(p2);
			lGamePoint.add(p1);
		}
		
		for(int k = 0; k < lGamePoint.size(); k++){
			Point p = lGamePoint.get(k);
			MovePoints(p, s_Down, nGameRow);
		}
	}
	
	//全部向下
	public void AllGameDown(Point p1, Point p2){
		List<Point> lGamePoint = new ArrayList<Point>();
		// y = 1 为看齐线
		if (p1.y < p2.y){
			lGamePoint.add(p1);
			lGamePoint.add(p2);
		}else{
			lGamePoint.add(p2);
			lGamePoint.add(p1);
		}
		
		for(int k = 0; k < lGamePoint.size(); k++){
			Point p = lGamePoint.get(k);
			MovePoints(p, s_Up, 0);
		}
	}
	
	//左右分离
	public void GameLeftRightLeave(Point p1, Point p2){
		int nGameColumn = mGameSurfaceMap.length - 2;
		List<Point> lGamePoint = new ArrayList<Point>();
		//P2离中心更近，先处理P2点造成的空洞
		if(Math.abs(p1.x - nGameColumn / 2) > Math.abs(p2.x - nGameColumn / 2)){
			lGamePoint.add(p2);
			lGamePoint.add(p1);
		}else{
			lGamePoint.add(p1);
			lGamePoint.add(p2);
		}
		
		for(int k = 0; k < lGamePoint.size(); k++){
			Point p = lGamePoint.get(k);
			if(p.x > nGameColumn / 2){
				MovePoints(p, s_Left, nGameColumn / 2 + 1);
			}else{
				MovePoints(p, s_Right, nGameColumn / 2);
			}
		}
	}
	
	//左右合拢
	public void GameLeftRightFold(Point p1, Point p2){
		int nGameColumn = mGameSurfaceMap.length - 2;
		List<Point> lGamePoint = new ArrayList<Point>();
		//P2离中心更近，后处理P2点造成的空洞
		if(Math.abs(p1.x - nGameColumn / 2) > Math.abs(p2.x - nGameColumn / 2)){
			lGamePoint.add(p1);
			lGamePoint.add(p2);
		}else{
			lGamePoint.add(p2);
			lGamePoint.add(p1);
		}
		
		for(int k = 0; k < lGamePoint.size(); k++){
			Point p = lGamePoint.get(k);
			if(p.x > nGameColumn / 2){
				MovePoints(p, s_Right, nGameColumn);
			}else{
				MovePoints(p, s_Left, 1);
			}
		}
	}
	
	//上下分离
	public void GameUpDownLeave(Point p1, Point p2){
		int nGameRow = mGameSurfaceMap[0].length - 2;
		List<Point> lGamePoint = new ArrayList<Point>();
		//P2离中心更近，先处理P2点造成的空洞
		if(Math.abs(p1.y - nGameRow / 2) > Math.abs(p2.y - nGameRow / 2)){
			lGamePoint.add(p2);
			lGamePoint.add(p1);
		}else{
			lGamePoint.add(p1);
			lGamePoint.add(p2);
		}
		
		for(int k = 0; k < lGamePoint.size(); k++){
			Point p = lGamePoint.get(k);
			if (p.y > nGameRow / 2){
				MovePoints(p, s_Up, nGameRow / 2 + 1);
			}else{
				MovePoints(p, s_Down, nGameRow / 2);
			}
		}
	}
	
	//上下合拢
	public void GameUpDownFold(Point p1, Point p2){
		int nGameRow = mGameSurfaceMap[0].length - 2;
		List<Point> lGamePoint = new ArrayList<Point>();
		//P2离中心更近，后处理P2点造成的空洞
		if(Math.abs(p1.y - nGameRow / 2) > Math.abs(p2.y - nGameRow / 2)){
			lGamePoint.add(p1);
			lGamePoint.add(p2);
		}else{
			lGamePoint.add(p2);
			lGamePoint.add(p1);
		}
		
		for(int k = 0; k < lGamePoint.size(); k++){
			Point p = lGamePoint.get(k);
			if (p.y > nGameRow / 2){
				MovePoints(p, s_Down, nGameRow);
			}else{
				MovePoints(p, s_Up, 1);
			}
		}
	}
	
	//全部向左上角靠拢
	public void AllGameUpLeft(Point p1, Point p2){
		int nGameColumn = mGameSurfaceMap.length - 2;
		int nGameRow = mGameSurfaceMap[0].length - 2;
		List<Point> lGamePoint = new ArrayList<Point>();
		// y = 1 为看齐线
		if(p1.y > p2.y){
			lGamePoint.add(p1);
			lGamePoint.add(p2);
		}else{
			lGamePoint.add(p2);
			lGamePoint.add(p1);
		}
		
		for(int k = 0; k < lGamePoint.size(); k++){
			Point p = lGamePoint.get(k);
			//下面的先向上移动
			MovePoints(p, s_Down, nGameRow);
		}
		
		for(int k = 0; k < lGamePoint.size(); k++){
			Point p = lGamePoint.get(k);
			for(int i = p.y; i <= nGameRow; i++){
				if(mGameSurfaceMap[p.x][i] == 0){
					Point pNew = new Point(p.x, i);
					// 右边的向左移动
					MovePoints(pNew, s_Right, nGameColumn);
				}
			} 
		}
	}
	
	//全部向右上角靠拢
	public void AllGameUpRight(Point p1, Point p2){
		int nGameRow = mGameSurfaceMap[0].length - 2;
		List<Point> lGamePoint = new ArrayList<Point>();
		// y = 1 为看齐线
		if(p1.y > p2.y){
			lGamePoint.add(p1);
			lGamePoint.add(p2);
		}else{
			lGamePoint.add(p2);
			lGamePoint.add(p1);
		}
		
		for(int k = 0; k < lGamePoint.size(); k++){
			Point p = lGamePoint.get(k);
			//下面的先向上移动
			MovePoints(p, s_Down, nGameRow);
		}
		
		for(int k = 0; k < lGamePoint.size(); k++){
			Point p = lGamePoint.get(k);
			for(int i = p.y; i >= 0; i--){
				if(mGameSurfaceMap[p.x][i] == 0){
					Point pNew = new Point(p.x, i);
					// 左边的向右移动
					MovePoints(pNew, s_Left, 0);
				}
			}
		}
	}
	
	//全部向右下角靠拢
	public void AllGameDownRight(Point p1, Point p2){
		List<Point> lGamePoint = new ArrayList<Point>();
		// y = 1 为看齐线
		if (p1.y < p2.y){
			lGamePoint.add(p1);
			lGamePoint.add(p2);
		}else{
			lGamePoint.add(p2);
			lGamePoint.add(p1);
		}
		
		for(int k = 0; k < lGamePoint.size(); k++){
			Point p = lGamePoint.get(k);
			//上面的先向下移动
			MovePoints(p, s_Up, 0);
		}
		
		for(int k = 0; k < lGamePoint.size(); k++){
			Point p = lGamePoint.get(k);
			for(int i = p.y; i >= 0; i--){
				if(mGameSurfaceMap[p.x][i] == 0){
					Point pNew = new Point(p.x, i);
					// 左边的向右移动
					MovePoints(pNew, s_Left, 0);
				}
			}
		}
	}
	
	//全部向左下角靠拢
	public void AllGameDownLeft(Point p1, Point p2){
		int nGameColumn = mGameSurfaceMap.length - 2;
		int nGameRow = mGameSurfaceMap[0].length - 2;
		List<Point> lGamePoint = new ArrayList<Point>();
		// y = 1 为看齐线
		if (p1.y < p2.y){
			lGamePoint.add(p1);
			lGamePoint.add(p2);
		}else{
			lGamePoint.add(p2);
			lGamePoint.add(p1);
		}
		
		for(int k = 0; k < lGamePoint.size(); k++){
			Point p = lGamePoint.get(k);
			//上面的先向下移动
			MovePoints(p, s_Up, 0);
		}
		
		for(int k = 0; k < lGamePoint.size(); k++){
			Point p = lGamePoint.get(k);
			for(int i = p.y; i <= nGameRow; i++){
				if(mGameSurfaceMap[p.x][i] == 0){
					Point pNew = new Point(p.x, i);
					// 右边的向左移动
					MovePoints(pNew, s_Right, nGameColumn);
				}
			} 
		}
	}
	
	//向左的上下分离
	public void GameLeftUpDownLeave(Point p1, Point p2){
		int nGameColumn = mGameSurfaceMap.length - 2;
		int nGameRow = mGameSurfaceMap[0].length - 2;
		List<Point> lGamePoint = new ArrayList<Point>();
		//P2离中心更近，先处理P2点造成的空洞
		if(Math.abs(p1.y - nGameRow / 2) > Math.abs(p2.y - nGameRow / 2)){
			lGamePoint.add(p2);
			lGamePoint.add(p1);
		}else{
			lGamePoint.add(p1);
			lGamePoint.add(p2);
		}
		
		for(int k = 0; k < lGamePoint.size(); k++){
			Point p = lGamePoint.get(k);
			if (p.y > nGameRow / 2){
				MovePoints(p, s_Up, nGameRow / 2 + 1);
			}else{
				MovePoints(p, s_Down, nGameRow / 2);
			}
		}
		
		for(int k = 0; k < lGamePoint.size(); k++){
			Point p = lGamePoint.get(k);
			for(int i = p.y; i <= nGameRow; i++){
				if(mGameSurfaceMap[p.x][i] == 0){
					Point pNew = new Point(p.x, i);
					// 右边的向左移动
					MovePoints(pNew, s_Right, nGameColumn);
				}
			} 
		}
	}
	
	//向右的上下靠拢
	public void GameRightUpDownFold(Point p1, Point p2){
		int nGameRow = mGameSurfaceMap[0].length - 2;
		List<Point> lGamePoint = new ArrayList<Point>();
		//P2离中心更近，后处理P2点造成的空洞
		if (Math.abs(p1.y - nGameRow / 2) > Math.abs(p2.y - nGameRow / 2)){
			lGamePoint.add(p1);
			lGamePoint.add(p2);
		}else{
			lGamePoint.add(p2);
			lGamePoint.add(p1);
		}
		
		for(int k = 0; k < lGamePoint.size(); k++){
			Point p = lGamePoint.get(k);
			if (p.y > nGameRow / 2){
				MovePoints(p, s_Down, nGameRow);
			}else{
				MovePoints(p, s_Up, 1);
			}
		}
		
		for(int k = 0; k < lGamePoint.size(); k++){
			Point p = lGamePoint.get(k);
			for(int i = p.y; i >= 0; i--){
				if(mGameSurfaceMap[p.x][i] == 0){
					Point pNew = new Point(p.x, i);
					// 左边的向右移动
					MovePoints(pNew, s_Left, 0);
				}
			}
		}
	}
	
	//向上的左右分离
	public void GameUpLeftRightLeave(Point p1, Point p2){
		int nGameColumn = mGameSurfaceMap.length - 2;
		int nGameRow = mGameSurfaceMap[0].length - 2;
		List<Point> lGamePoint = new ArrayList<Point>();
		//P2离中心更近，先处理P2点造成的空洞
		if (Math.abs(p1.x - nGameColumn / 2) > Math.abs(p2.x - nGameColumn / 2)){
			lGamePoint.add(p2);
			lGamePoint.add(p1);
		}else{
			lGamePoint.add(p1);
			lGamePoint.add(p2);
		}
		
		for(int k = 0; k < lGamePoint.size(); k++){
			Point p = lGamePoint.get(k);
			if (p.x > nGameColumn / 2){
				MovePoints(p, s_Left, nGameColumn / 2 + 1);
			}else{
				MovePoints(p, s_Right, nGameColumn / 2);
			}
		}
		
		for(int k = 0; k < lGamePoint.size(); k++){
			Point p = lGamePoint.get(k);
			for(int i = p.x; i <= nGameColumn; i++){
				if(mGameSurfaceMap[i][p.y] == 0){
					Point pNew = new Point(i, p.y);
					// 下边的向上移动
					MovePoints(pNew, s_Down, nGameRow);
				}
			}
		}
	}
		
	
	public void MovePoints(Point p, int direction, int end){
		int nGameFill;					//要被填充的
		int nGameMove; 					//要被移动到填充点的
		switch (direction){
			//要移动上方的，也就是上方的往下移
			case s_Up:
				nGameFill = p.y;
				nGameMove = p.y - 1;
				while(nGameMove >= end){
					while(mGameSurfaceMap[p.x][nGameMove] == 0 && nGameMove > end){
						nGameMove--;
					}
					mGameSurfaceMap[p.x][nGameFill] = mGameSurfaceMap[p.x][nGameMove];
					mGameSurfaceMap[p.x][nGameMove] = 0;
					nGameFill--;
					nGameMove--;
				}
				break;
			//要移动下方的，也就是下方的往上移
			case s_Down:
				nGameFill = p.y;
				nGameMove = p.y + 1;
				while(nGameMove <= end){
					while(mGameSurfaceMap[p.x][nGameMove] == 0 && nGameMove < end){
						nGameMove++;
					}
					mGameSurfaceMap[p.x][nGameFill] = mGameSurfaceMap[p.x][nGameMove];
					mGameSurfaceMap[p.x][nGameMove] = 0;
					nGameFill++;
					nGameMove++;
				}
				break;
			//要移动左边的，也就是左边的往右移
			case s_Left:
				nGameFill = p.x;
				nGameMove = p.x-1;
				while (nGameMove >= end){
					while (mGameSurfaceMap[nGameMove][p.y] == 0 && nGameMove > end){
						nGameMove--;
					}
					mGameSurfaceMap[nGameFill][p.y] = mGameSurfaceMap[nGameMove][p.y];
					mGameSurfaceMap[nGameMove][p.y] = 0;
					nGameFill--;
					nGameMove--;
				}
				break;
			//要移动右边的，也就是右边的往左移
			case s_Right:
				nGameFill = p.x;
				nGameMove = p.x + 1;
				while(nGameMove <= end){
					while (mGameSurfaceMap[nGameMove][p.y] == 0 && nGameMove < end){
						nGameMove++;
					}
					mGameSurfaceMap[nGameFill][p.y] = mGameSurfaceMap[nGameMove][p.y];
					mGameSurfaceMap[nGameMove][p.y] = 0;
					nGameFill++;
					nGameMove++;
				}
				break;
		}
	}
	
	//	获取连连看单元格的宽度
	public int getmIconWidth() {
		return mIconWidth;
	}
	
	// 设置连连看单元格的宽度
	public void setmIconWidth(int mIconWidth) {
		this.mIconWidth = mIconWidth;
	}
	
	// 获取连连看单元格的高度
	public int getmIconHeight() {
		return mIconHeight;
	}
	
	// 设置连连看单元格的高度
	public void setmIconHeight(int mIconHeight) {
		this.mIconHeight = mIconHeight;
	}
	
	// 获取连连看游戏的图片资源
	public Bitmap[] getmGameSurfaceIcons() {
		return mGameSurfaceIcons;
	}
	
	// 设置连连看游戏的图片资源
	public void setmGameSurfaceIcons(Bitmap[] mGameSurfaceIcons) {
		this.mGameSurfaceIcons = mGameSurfaceIcons;
	}
	
	// 获取连连看单元格的位置坐标
	public int[][] getmGameSurfaceMap() {
		return mGameSurfaceMap;
	}
	
	// 设置连连看单元格的位置坐标
	public void setmGameSurfaceMap(int[][] mGameSurfaceMap) {
		this.mGameSurfaceMap = mGameSurfaceMap;
		invalidate();
	}
	
	// 获取被选择的单元格数组
	public List<Point> getmIconSelectedList() {
		return mIconSelectedList;
	}

	public void setmIconSelectedList(List<Point> mIconSelectedList) {
		this.mIconSelectedList = mIconSelectedList == null ? new ArrayList<Point>() : mIconSelectedList;
	}
	
	//设置连连看连连看单元格的监听器
	public void setOnGameSurfaceItemClickListener(OnGameSurfaceItemClickListener onGameSurfaceItemClickListener) {
		this.mOnGameSurfaceItemClickListener = onGameSurfaceItemClickListener;
	}
	
	
	public interface OnGameSurfaceItemClickListener {
		public void onGameSurfaceItemClick(Point point);
	}
}

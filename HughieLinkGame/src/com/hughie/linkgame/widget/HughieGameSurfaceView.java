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
 * ��Ϸ�������surface����
 * @ClassName: HughieGameSurfaceView
 * @author hughiezhang
 * @since 2015-10-22 17:35
 */
public class HughieGameSurfaceView extends View {
	public static int mGameCurrentLevel;								// ��Ϸ�ĵ�ǰ�ؿ�
	
	private int mIconWidth;													// ��������Ԫ����
	private int mIconHeight;												// ��������Ԫ��߶�
	
	private int mOffsetX;														// ͼƬ�ں����ϵ�ƫ��ֵ
	private int mOffsetY;														// ͼƬ�������ϵ�ƫ��ֵ
	
	private int[][] mGameSurfaceMap;									// ��ά���飬���±��ӦΪ��ͼ�ı�񡣶���������Ϊ�洢ͼƬ��id��ͼƬ����
	private Bitmap[] mGameSurfaceIcons;								// ͼƬ��Դ
	
	private List<Point> mIconSelectedList;							// ��Ϸ�У����ͼ����ø����������汻ѡ��ͼƬ������
	public int mIconSelectedTime;										// ����select animation��ʾ��ͬͼƬ��ʱ��
	
	private Point[] mIconConnectedPath;								// �����洢�����۵������
	private int mIconConnectedTime;									// ���嵥Ԫ�����ߵ�animationʱ��
	
	private int mScoreAnimTime;											// �����ȡ������animationʱ��
	private int mConnectedGoodTime;									// ����connect������ɺ�ը��animationʱ��
	
	private Point[] mIconBombPath;										// �����洢bomb�������
	public int mIconBombedTime;										// ����boom��ը��animation��ʱ��
	
	private List<Point> mIconHintedList;								// �����洢hint�������
	public int mIconHintedTime;											// ����hint��ʾ��animation��ʱ��
	
	private long mRemainGameTime;									// ��Ϸ����ʣ��ʱ��
	private long mTotalGameTime;										// ��Ϸ������ʱ��
	
	public boolean mGameFreeze;										// �����Ƿ�Ϊfreeze״̬
	public int mGameFreezeStatus;										// ����freeze��״̬
	
	public HughieGameSurfaceBackground mBackSprite;		// ��Ϸ�ı���
	public HughieGameSurfaceTimer mTimerSprite;				// ��Ϸ��timer
	
	// ��Ϸ�У����ͼ����animationͼƬ
	private int[] mGameSelectedAnimationImages = {R.drawable.imgv_game_surface_anim_select1, R.drawable.imgv_game_surface_anim_select2,
			R.drawable.imgv_game_surface_anim_select3, R.drawable.imgv_game_surface_anim_select4, R.drawable.imgv_game_surface_anim_select5};
	//����hint��ʾ��animationͼƬ
	private int[] mGameHintedAnimationImages = {R.drawable.imgv_game_surface_anim_hint1, R.drawable.imgv_game_surface_anim_hint2, 
			R.drawable.imgv_game_surface_anim_hint3, R.drawable.imgv_game_surface_anim_hint4, R.drawable.imgv_game_surface_anim_hint5};
	// ���嵥Ԫ��connected�������ߵ�animationͼƬ
	private int[] mConnectedHorizontalAnimationImages = {R.drawable.imgv_game_surface_anim_horizontal1, R.drawable.imgv_game_surface_anim_horizontal2,
			R.drawable.imgv_game_surface_anim_horizontal3, R.drawable.imgv_game_surface_anim_horizontal4};
	// ���嵥Ԫ��connected�������ߵ�animationͼƬ
	private int[] mConnectedVerticalAnimationImages = {R.drawable.imgv_game_surface_anim_vertical1, R.drawable.imgv_game_surface_anim_vertical2,
			R.drawable.imgv_game_surface_anim_vertical3, R.drawable.imgv_game_surface_anim_vertical4};
	// ���嵥Ԫ��connected���Ͻ����ߵ�animationͼƬ
	private int[] mConnectedTopLeftAnimationImages = {R.drawable.imgv_game_surface_anim_topleft1, R.drawable.imgv_game_surface_anim_topleft2,
			R.drawable.imgv_game_surface_anim_topleft3, R.drawable.imgv_game_surface_anim_topleft4};
	// ���嵥Ԫ��connected���Ͻ����ߵ�animationͼƬ
	private int[] mConnectedTopRightAnimationImages = {R.drawable.imgv_game_surface_anim_topright1, R.drawable.imgv_game_surface_anim_topright2,
			R.drawable.imgv_game_surface_anim_topright3, R.drawable.imgv_game_surface_anim_topright4};
	// ���嵥Ԫ��connected���½����ߵ�animationͼƬ
	private int[] mConnectedBottomLeftAnimationImages = {R.drawable.imgv_game_surface_anim_bottomleft1, R.drawable.imgv_game_surface_anim_bottomleft2,
			R.drawable.imgv_game_surface_anim_bottomleft3, R.drawable.imgv_game_surface_anim_bottomleft4};
	// ���嵥Ԫ��connected���½����ߵ�animationͼƬ
	private int[] mConnectedBottomRightAnimationImages = {R.drawable.imgv_game_surface_anim_bottomright1, R.drawable.imgv_game_surface_anim_bottomright2,
			R.drawable.imgv_game_surface_anim_bottomright3, R.drawable.imgv_game_surface_anim_bottomright4};
	// �����ȡ�������animationͼƬ
	private int[] mGameScoreAnimationImages = {R.drawable.imgv_game_surface_anim_score1, R.drawable.imgv_game_surface_anim_score2, R.drawable.imgv_game_surface_anim_score3,
			R.drawable.imgv_game_surface_anim_score4, R.drawable.imgv_game_surface_anim_score5, R.drawable.imgv_game_surface_anim_score6};
	// ���嵥Ԫ������֮��ը��animationͼƬ
	private int[] mConnectedGoodAnimationImages = {R.drawable.imgv_game_surface_anim_connected_good1, R.drawable.imgv_game_surface_anim_connected_good2,
			R.drawable.imgv_game_surface_anim_connected_good3, R.drawable.imgv_game_surface_anim_connected_good4};
	// ����boom��ը��animationͼƬ
	private int[] mGameBombedAnimationImages = {R.drawable.imgv_game_surface_anim_bomb1, R.drawable.imgv_game_surface_anim_bomb2, 
			R.drawable.imgv_game_surface_anim_bomb3, R.drawable.imgv_game_surface_anim_bomb4, R.drawable.imgv_game_surface_anim_bomb5, 
			R.drawable.imgv_game_surface_anim_bomb6, R.drawable.imgv_game_surface_anim_bomb7, R.drawable.imgv_game_surface_anim_bomb7, 
			R.drawable.imgv_game_surface_anim_bomb9, R.drawable.imgv_game_surface_anim_bomb10, R.drawable.imgv_game_surface_anim_bomb11, 
			R.drawable.imgv_game_surface_anim_bomb12, R.drawable.imgv_game_surface_anim_bomb13, R.drawable.imgv_game_surface_anim_bomb14};
	
	private OnGameSurfaceItemClickListener mOnGameSurfaceItemClickListener;			// ͼƬ����¼��ļ�����
	
	// �������������߽ڵ�ͼ����Ƶ�ѡ��״̬
	public static final int ANIM_GAME_VIEW_HORIZONTAL = 1000;				// ������animation��ʾ
	public static final int ANIM_GAME_VIEW_VETICAL = 1001;						// ������animation��ʾ
	public static final int ANIM_GAME_VIEW_TOPLEFT = 1002;						// ���Ͻ�ת��animation��ʾ
	public static final int ANIM_GAME_VIEW_TOPRIGHT = 1003;					// ���Ͻ�ת��animation��ʾ
	public static final int ANIM_GAME_VIEW_BOTTOMLEFT = 1004;				// ���½�ת��animation��ʾ
	public static final int ANIM_GAME_VIEW_BOTTOMRIGHT = 1005;			// ���½�ת��animation��ʾ
	
	// ����game freeze�ļ���״̬
	public static final int ANIM_GAME_FREEZE_NONE = 2000;			// �޶���
	public static final int ANIM_GAME_FREEZE_SHOW = 2001;			// ��ʾ����
	public static final int ANIM_GAME_FREEZE_ALERT = 2002;			// ������ʾ
	
	
	
	
	//��Ϸ������ؿ�
	public static final int s_LevelCount = 16;
	
	public static final int s_Up = 0;
	public static final int s_Down = 1;
	public static final int s_Left = 2;
	public static final int s_Right = 3;
	
	//������Ϸ�Ĺؿ�ģʽ
	public static final int GAME_LEVEL_NORMAL = 1;				//����ģʽ
	public static final int GAME_LEVEL_ALLLEFT = 2;				//ȫ������
	public static final int GAME_LEVEL_ALLRIGHT = 3;			//ȫ������
	public static final int GAME_LEVEL_ALLUP = 4;				//ȫ������
	public static final int GAME_LEVEL_ALLDOWN = 5;				//ȫ������
	public static final int GAME_LEVEL_LEFTRIGHTLEAVE = 6;		//���ҷ���
	public static final int GAME_LEVEL_LEFTRIGHTFOLD = 7;		//���Һ�£
	public static final int GAME_LEVEL_UPDOWNLEAVE = 8;			//���·���
	public static final int GAME_LEVEL_UPDOWNFOLD = 9;			//���º�£
	public static final int GAME_LEVEL_ALLUPLEFT = 10;			//ȫ�������Ͻǿ�£
	public static final int GAME_LEVEL_ALLUPRIGHT = 11;			//ȫ�������Ͻǿ�£
	public static final int GAME_LEVEL_ALLDOWNLEFT = 12;		//ȫ�������½ǿ�£
	public static final int GAME_LEVEL_ALLDOWNRIGHT = 13;		//ȫ�������½ǿ�£
	public static final int GAME_LEVEL_LEFTUPDOWNLEAVE = 14;	//��������·���
	public static final int GAME_LEVEL_RIGHTUPDOWNFOLD = 15;	//���ҵ����¿�£
	public static final int GAME_LEVEL_UPLEFTRIGHTLEAVE = 16;	//���ϵ����ҷ���
	
	public HughieGameSurfaceView(Context context) {
		super(context);
		
		initGameSurfaceData();
		
		//��Ϸ�ı�������
		this.mBackSprite = new HughieGameSurfaceBackground();
		mBackSprite.initGameBackground(getContext());
		//��Ϸ��timer��ʾ
		this.mTimerSprite = new HughieGameSurfaceTimer();
		mTimerSprite.initGameTimer(mTotalGameTime, context.getApplicationContext(), true);
	}
	
	// ��ʼ����Ӧ����
	private void initGameSurfaceData() {
		this.mOffsetX = 0;															// ����ƫ��ֵĬ��Ϊ0
		this.mOffsetY = 0;															// ����ƫ��ֵĬ��Ϊ0
		this.mRemainGameTime = 0;											// ��Ϸ���ȵ�ʣ��ʱ��Ĭ��Ϊ0
		this.mTotalGameTime = 120;											// ��Ϸ���ȵ���ʱ������Ϊ2����
		this.mIconSelectedList = new ArrayList<Point>();
		this.mIconHintedList = new ArrayList<Point>();
		this.mGameFreeze = false;												// ��ϷĬ��Ϊδ����
		this.mGameFreezeStatus = ANIM_GAME_FREEZE_NONE;	// ��ϷĬ��״̬Ϊ�޶���		
	}
	
	/*
	 * map��������Ϸ�Ĳ������飬�Ǵ��߽�ġ����߽�������ڽ�����������λ��ͳһ��������˷Ѻܶ���Ļ�ռ䣬
	 * ��ˣ�ͨ��mOffsetX��mOffsetY��������������������������mOffsetX��mOffsetY��
	 */ 
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		/*
		 * ���changedΪtrue��
		 * ����ƫ�ƣ����߽�ͼƬδ����ʾ�Ĳ��֣�= iconͼƬ��� - ((right - left)���ֻ���Ļ����ͼ���Ŀ�� - ����ͼƬ���� * ͼƬ���) / 2
		 * �������ô����ô��������Ļ�ľ���Ӧ��Ϊһ��ͼƬ�Ŀ�ȣ����߽�û�õ�0��ʾ��λ�ã������˷�����Ļ�ռ䣬����ͬ��
		 */
		if (changed) {
			mOffsetX = mIconWidth - (right - left - 10 - (mGameSurfaceMap.length - 2) * mIconWidth) / 2;
			mOffsetY = mIconHeight - (bottom - top - 75 - (mGameSurfaceMap[0].length - 2) * mIconHeight) / 2; 
		}
	}
	
	// ���Ĳ��֣���дonDraw()������ʾ��Ϸ����
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		mBackSprite.drawGameSelf(canvas);
		mTimerSprite.drawGameSelf(canvas, (int)mRemainGameTime, mGameFreeze, mGameFreezeStatus);
		
		drawIconBitmaps(canvas);							//	������������Ԫ��ͼƬ
		drawIconSelected(canvas);							// ���Ƶ�Ԫ��ѡ��Ķ���
		drawIconHinted(canvas);								// ������ʾ��������Ԫ��Ķ���
		
		// ��ʾmIconLinkedPath��������Ϊ2������»������ߣ�����Ҫ����ͼƬ�ſ���������
		if(mIconConnectedPath != null && mIconConnectedPath.length >= 2) {
			if(mIconBombPath == null)
				drawIconConnected(canvas);						// ���Ƶ�Ԫ�����ӵĶ���
			
			// �������Ч��Ϊ���´�ˢ��ʱ�򽫶�Ӧ����ͼƬ���ʼһ�������βһ���������߶�������
			Point p = mIconConnectedPath[0];
			mGameSurfaceMap[p.x][p.y] = 0;
			p = mIconConnectedPath[mIconConnectedPath.length - 1];
			mGameSurfaceMap[p.x][p.y] = 0;
			//ѡ�������
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
	 * @description ������������Ԫ��ͼƬ
	 * @param canvas������
	 * @return
	 */
	private void drawIconBitmaps(Canvas canvas) {
		// ��ͼƬһ��һ�ŵİ��涨��С���涨λ�û���������
		for(int i = 0; i < mGameSurfaceMap.length; i++) {
			for(int j = 0; j < mGameSurfaceMap[i].length; j++) {
				if(mGameSurfaceMap[i][j] != 0) {
					// mGameSurfaceIconsΪͼƬ��Դ���������±�ֵҪ������ֵС1
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
	 * @description ���Ƶ�Ԫ��ѡ��Ķ���
	 * @param canvas������
	 * @return
	 */
	private void drawIconSelected(Canvas canvas) {
		mIconSelectedTime %= 25;
		for(Point point : mIconSelectedList) {											//�����������������
			// ͨ��getIndex2Screen����ӦͼƬ�ڲ����еĵ�����ת��������Ļ�е����Ͻǵ���Ļ������
			// ����ǰ�����Ļ����x��yת���ɶ�ӦͼƬ���±�
			Point p = getIndex2Screen(point.x, point.y);
			// ��ͼƬ��ѡ�е�λ�û�һ����ͼƬ��Сһ�µľ��Σ����˸����ĸо�
			canvas.drawBitmap(((BitmapDrawable) (getResources().getDrawable(mGameSelectedAnimationImages[mIconSelectedTime / 5]))).getBitmap(), 
					null, new RectF(p.x + point.x - 0.08F * mIconWidth, p.y - 5 + point.y -0.08F * mIconHeight, p.x + point.x + 1.08F * mIconWidth, 
					p.y - 5 + point.y + 1.08F * mIconHeight), null);
		}
		mIconSelectedTime++;
	}
	
	/**
	 * @title drawIconHinted
	 * @description ������ʾ��������Ԫ��Ķ���
	 * @param canvas������
	 * @return
	 */
	private void drawIconHinted(Canvas canvas) {
		this.mIconHintedTime %= 25;
		for(Point position : mIconHintedList){
			// ͨ������inedx2screen����ӦͼƬ�ڲ����еĵ�����ת��������Ļ�е����Ͻǵ���Ļ������
			// ����ǰ�����Ļ����x,yת���ɶ�ӦͼƬ���±�
			Point p = getIndex2Screen(position.x, position.y);
			//����p1���hint��ʾanimation
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
	 * @description ������������Ԫ�����������Ķ���
	 * @param canvas������
	 * @return
	 */
	private void drawIconConnected(Canvas canvas) {
		HughieLoggerManager.println("mIconConnectedTime: " + mIconConnectedTime);
		if(mIconConnectedTime < 24) {
			if(mIconConnectedTime < 12) {
				// �ж��Ƿ�Ϊ�������ֱ������
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
	 * @description ���������������������ȡ�����Ķ���
	 * @param canvas������
	 * @return
	 */
	private void drawIconConnectedScore(Canvas canvas) {
		if(mScoreAnimTime < 24) {
			mScoreAnimTime += 1;
			// ��ȡ�����Ķ���
			if(mScoreAnimTime < 15) 
				canvas.drawBitmap(((BitmapDrawable) getResources().getDrawable(R.drawable.imgv_game_surface_addscore)).getBitmap(), 
						getIndex2Screen(mIconConnectedPath[mIconConnectedPath.length - 1].x, mIconConnectedPath[mIconConnectedPath.length - 1].y).x 
						+ mScoreAnimTime * (8 - mIconConnectedPath[mIconConnectedPath.length - 1].x) * mIconWidth / 15, 
						getIndex2Screen(mIconConnectedPath[mIconConnectedPath.length - 1].x, mIconConnectedPath[mIconConnectedPath.length - 1].y).y
						- mScoreAnimTime * mIconConnectedPath[mIconConnectedPath.length - 1].y * mIconHeight / 15, null);
			//	��ȡ������Ķ���
			else if(mScoreAnimTime >= 15)
				canvas.drawBitmap(((BitmapDrawable) getResources().getDrawable(mGameScoreAnimationImages[(mScoreAnimTime - 15) / 2])).getBitmap(), 
						getIndex2Screen(8, 0).x, getIndex2Screen(8, 0).y - mIconHeight, null);
		} else {
			mScoreAnimTime = 0;
		}
	}
	
	/**
	 * @title drawIconConnectedGood
	 * @description ��������������������ը��animation����
	 * @param canvas������
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
	 * @description ������������Ԫ��ը�Ķ���
	 * @param canvas������
	 * @param point
	 * @return
	 */
	private void drawIconBombed(Canvas canvas, Point point) {
		if(mIconBombedTime < 42){
			//����p1���ը��animation
			canvas.drawBitmap(((BitmapDrawable)getResources().getDrawable(mGameBombedAnimationImages[mIconBombedTime / 3])).getBitmap(), null,
				new RectF(getIndex2Screen(mIconConnectedPath[0].x, mIconConnectedPath[0].y).x - mIconWidth * 0.2F, 
				getIndex2Screen(mIconConnectedPath[0].x, mIconConnectedPath[0].y).y + 5 - mIconHeight * 0.2F,
				getIndex2Screen(mIconConnectedPath[0].x, mIconConnectedPath[0].y).x + mIconWidth * 1.3F,
				getIndex2Screen(mIconConnectedPath[0].x, mIconConnectedPath[0].y).y + 5 + mIconHeight * 1.3F), null);
			//����p2���ը��animation
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
	 * @description ����ֻ������������������Ӷ���
	 * @param canvas������
	 * @return
	 */
	private void drawTwoIconsConnected(Canvas canvas) {
		//����
		if(mIconConnectedPath[0].y == mIconConnectedPath[1].y) {
			//���Ƶ�point1
			drawConnectionXYPoint(mIconConnectedPath[0].x, mIconConnectedPath[0].y, ANIM_GAME_VIEW_HORIZONTAL, canvas);
			//���Ƶ�point2
			drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_HORIZONTAL, canvas);
		}
		
		//����
		if(mIconConnectedPath[0].x == mIconConnectedPath[1].x) {
			//���Ƶ�point1
			drawConnectionXYPoint(mIconConnectedPath[0].x, mIconConnectedPath[0].y, ANIM_GAME_VIEW_VETICAL, canvas);
			//���Ƶ�point2
			drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_VETICAL, canvas);
		}
		
		//���Ƴ��ڵ��������
		drawConnectionXY(mIconConnectedPath[0].x, mIconConnectedPath[0].y, mIconConnectedPath[1].x, mIconConnectedPath[1].y, canvas);
	}
	
	/**
	 * @title drawThreeIconsConnected
	 * @description ����ֻ������������������Ӷ���
	 * @param canvas������
	 * @return
	 */
	private void drawThreeIconsConnected(Canvas canvas) {
		//���Ƶ�point1��������Ҫ������ʼ���ת�۵�֮���Ǻ�������ͬ������������ͬ�����ж�point1�Ǻ���������
		if(mIconConnectedPath[0].y == mIconConnectedPath[1].y)
			drawConnectionXYPoint(mIconConnectedPath[0].x, mIconConnectedPath[0].y, ANIM_GAME_VIEW_HORIZONTAL, canvas);
		else if(mIconConnectedPath[0].x == mIconConnectedPath[1].x)
			drawConnectionXYPoint(mIconConnectedPath[0].x, mIconConnectedPath[0].y, ANIM_GAME_VIEW_VETICAL, canvas);
		
		//����ת�۵㣬��Ҫͨ��point1��point2���������������жϣ���point1��point2�����滹������
		//point1��point2����
		if(mIconConnectedPath[0].y < mIconConnectedPath[2].y) {
			// ���Ͻ�ת�۶�����ת�۵��point1��������ȣ���pont2��������ȣ���point1��point2���ұ�
			if(mIconConnectedPath[1].y == mIconConnectedPath[0].y && mIconConnectedPath[1].x == mIconConnectedPath[2].x &&
					mIconConnectedPath[0].x > mIconConnectedPath[2].x) {
				drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_TOPLEFT, canvas);
			// ���Ͻ�ת�۶�����ת�۵��point1��������ȣ���pont2��������ȣ���point1��point2�����
			} else if(mIconConnectedPath[1].y == mIconConnectedPath[0].y && mIconConnectedPath[1].x == mIconConnectedPath[2].x &&
					mIconConnectedPath[0].x < mIconConnectedPath[2].x) {
				drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_TOPRIGHT, canvas);
			// ���½�ת�۶�����ת�۵��point1��������ȣ���pont2��������ȣ���point1��point2�����
			} else if(mIconConnectedPath[1].x == mIconConnectedPath[0].x && mIconConnectedPath[1].y == mIconConnectedPath[2].y &&
					mIconConnectedPath[0].x < mIconConnectedPath[2].x) {
				drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_BOTTOMLEFT, canvas);
			// ���½�ת�۶�����ת�۵��point1��������ȣ���pont2��������ȣ���point1��point2���ұ�
			} else if(mIconConnectedPath[1].x == mIconConnectedPath[0].x && mIconConnectedPath[1].y == mIconConnectedPath[2].y &&
					mIconConnectedPath[0].x > mIconConnectedPath[2].x) {
				drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_BOTTOMRIGHT, canvas);
			}
		//point1��point2����
		} else if(mIconConnectedPath[0].y > mIconConnectedPath[2].y) {
			// ���Ͻ�ת�۶�����ת�۵��point1��������ȣ���pont2��������ȣ���point1��point2�����
			if(mIconConnectedPath[1].x == mIconConnectedPath[0].x && mIconConnectedPath[1].y == mIconConnectedPath[2].y &&
					mIconConnectedPath[0].x < mIconConnectedPath[2].x) {
				drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_TOPLEFT, canvas);
			// ���Ͻ�ת�۶�����ת�۵��point1��������ȣ���pont2��������ȣ���point1��point2���ұ�
			} else if(mIconConnectedPath[1].x == mIconConnectedPath[0].x && mIconConnectedPath[1].y == mIconConnectedPath[2].y &&
					mIconConnectedPath[0].x > mIconConnectedPath[2].x) {
				drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_TOPRIGHT, canvas);
			// ���½�ת�۶�����ת�۵��point1��������ȣ���pont2��������ȣ���point1��point2���ұ�
			} else if(mIconConnectedPath[1].y == mIconConnectedPath[0].y && mIconConnectedPath[1].x == mIconConnectedPath[2].x &&
					mIconConnectedPath[0].x > mIconConnectedPath[2].x) {
				drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_BOTTOMLEFT, canvas);
			// ���½�ת�۶�����ת�۵��point1��������ȣ���point2��������ȣ���point1��point2�����
			} else if(mIconConnectedPath[1].y == mIconConnectedPath[0].y && mIconConnectedPath[1].x == mIconConnectedPath[2].x &&
					mIconConnectedPath[0].x < mIconConnectedPath[2].x) {
				drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_BOTTOMRIGHT, canvas);
			}
		}
		
		// ���Ƶ�point2��������Ҫ������ʼ���ת�۵�֮���Ǻ�������ͬ������������ͬ�����ж�point2�Ǻ���������
		if(mIconConnectedPath[1].y == mIconConnectedPath[2].y)
			drawConnectionXYPoint(mIconConnectedPath[2].x, mIconConnectedPath[2].y, ANIM_GAME_VIEW_HORIZONTAL, canvas);
		else if(mIconConnectedPath[1].x == mIconConnectedPath[2].x)
			drawConnectionXYPoint(mIconConnectedPath[2].x, mIconConnectedPath[2].y, ANIM_GAME_VIEW_VETICAL, canvas);
		
		// ���Ƴ��ڵ��������
		drawConnectionXY(mIconConnectedPath[0].x, mIconConnectedPath[0].y, mIconConnectedPath[1].x, mIconConnectedPath[1].y, canvas);
		drawConnectionXY(mIconConnectedPath[1].x, mIconConnectedPath[1].y, mIconConnectedPath[2].x, mIconConnectedPath[2].y, canvas);
	}
	
	/**
	 * @title drawFourIconsConnected
	 * @description ����ֻ���ĸ�������������Ӷ���
	 * @param canvas������
	 * @return
	 */
	private void drawFourIconsConnected(Canvas canvas) {
		// ���Ƶ�point1, �����ж���ˮƽ���컹����ֱ����
		// ˮƽ����
		if(mIconConnectedPath[0].y == mIconConnectedPath[1].y) 
			drawConnectionXYPoint(mIconConnectedPath[0].x, mIconConnectedPath[0].y, ANIM_GAME_VIEW_HORIZONTAL, canvas);
		// ��ֱ����
		else if(mIconConnectedPath[0].x == mIconConnectedPath[1].x) 
			drawConnectionXYPoint(mIconConnectedPath[0].x, mIconConnectedPath[0].y, ANIM_GAME_VIEW_VETICAL, canvas);
		
		// ����ת�۵�mGamePointE1���ж�point1��mGamePointE2���Ϸ������·�
		// point1��mGamePointE2���Ϸ�
		if(mIconConnectedPath[0].y < mIconConnectedPath[2].y) {
			//���Ͻ�ת�ۣ�ת�۵�mGamePointE1��point1����������ȣ���ת�۵�mGamePointE2�ĺ�������ȣ���point1��ת�۵�mGamePointE2���ұ�
			if(mIconConnectedPath[1].y == mIconConnectedPath[0].y && mIconConnectedPath[1].x == mIconConnectedPath[2].x &&
					mIconConnectedPath[0].x > mIconConnectedPath[2].x) {
				drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_TOPLEFT, canvas);
			//���Ͻ�ת�ۣ�ת�۵�mGamePointE1��point1����������ȣ���ת�۵�mGamePointE2�ĺ�������ȣ���point1��ת�۵�mGamePointE2�����
			} else if(mIconConnectedPath[1].y == mIconConnectedPath[0].y && mIconConnectedPath[1].x == mIconConnectedPath[2].x &&
					mIconConnectedPath[0].x < mIconConnectedPath[2].x) {
				drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_TOPRIGHT, canvas);
			//���½�ת�ۣ�ת�۵�mGamePointE1��point1�ĺ�������ȣ���ת�۵�mGamePointE2����������ȣ���point1��ת�۵�mGamePointE2�����
			} else if(mIconConnectedPath[1].x == mIconConnectedPath[0].x && mIconConnectedPath[1].y == mIconConnectedPath[2].y &&
					mIconConnectedPath[0].x < mIconConnectedPath[2].x) {
				drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_BOTTOMLEFT, canvas);
			// ���½�ת�ۣ�ת�۵�mGamePointE1��point1�ĺ�������ȣ���ת�۵�mGamePointE2����������ȣ���point1��ת�۵�mGamePointE2���ұ�
			} else if(mIconConnectedPath[1].x == mIconConnectedPath[0].x && mIconConnectedPath[1].y == mIconConnectedPath[1].y &&
					mIconConnectedPath[0].x > mIconConnectedPath[2].x) {
				drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_BOTTOMRIGHT, canvas);
			}
		// point1��mGamePointE2���·�
		} else if(mIconConnectedPath[0].y > mIconConnectedPath[2].y) {
			//���Ͻ�ת�ۣ�ת�۵�mGamePointE1��point1�ĺ�������ȣ���ת�۵�mGamePointE2����������ȣ���point1��ת�۵�mGamePointE2�����
			if(mIconConnectedPath[1].x == mIconConnectedPath[0].x && mIconConnectedPath[1].y == mIconConnectedPath[2].y &&
					mIconConnectedPath[0].x < mIconConnectedPath[2].x) {
				drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_TOPLEFT, canvas);
			// ���Ͻ�ת�ۣ�ת�۵�mGamePointE1��point1�ĺ�������ȣ���ת�۵�mGamePointE2����������ȣ���point1��ת�۵�mGamePointE2���ұ�
			} else if(mIconConnectedPath[1].x == mIconConnectedPath[0].x && mIconConnectedPath[1].y == mIconConnectedPath[2].y &&
					mIconConnectedPath[0].x > mIconConnectedPath[2].x) {
				drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].x, ANIM_GAME_VIEW_TOPRIGHT, canvas);
			// ���½�ת�ۣ�ת�۵�mGamePointE1��point1����������ȣ���ת�۵�mGamePointE2�ĺ�������ȣ���point1��ת�۵�mGamePointE2���ұ�
			} else if(mIconConnectedPath[1].y == mIconConnectedPath[0].y && mIconConnectedPath[1].x == mIconConnectedPath[2].x &&
					mIconConnectedPath[0].x > mIconConnectedPath[2].x) {
				drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_BOTTOMLEFT, canvas);
			// ���½�ת�ۣ�ת�۵�mGamePointE1��point1����������ȣ���ת�۵�mGamePointE2�ĺ�������ȣ���point1��ת�۵�mGamePointE2�����
			} else if(mIconConnectedPath[1].y == mIconConnectedPath[0].y && mIconConnectedPath[1].x == mIconConnectedPath[2].x &&
					mIconConnectedPath[0].x < mIconConnectedPath[2].x) {
				drawConnectionXYPoint(mIconConnectedPath[1].x, mIconConnectedPath[1].y, ANIM_GAME_VIEW_BOTTOMRIGHT, canvas);
			}
		}
		
		// ����ת�۵�mGamePointE2���ж�mGamePointE1��point2���Ϸ������·�
		// mGamePointE1��point2���Ϸ�
		if(mIconConnectedPath[1].y < mIconConnectedPath[3].y) {
			// ���Ͻ�ת�ۣ�ת�۵�mGamePointE2��ת�۵�mGamePointE1����������ȣ�����ֹ��point2�ĺ�������ȣ���ת�۵�mGamePointE1����ֹ��point2���ұ�
			if(mIconConnectedPath[2].y == mIconConnectedPath[1].y && mIconConnectedPath[2].x == mIconConnectedPath[3].x &&
					mIconConnectedPath[1].x > mIconConnectedPath[3].x) {
				drawConnectionXYPoint(mIconConnectedPath[2].x, mIconConnectedPath[2].y, ANIM_GAME_VIEW_TOPLEFT, canvas);
			// ���Ͻ�ת�ۣ�ת�۵�mGamePointE2��ת�۵�mGamePointE1����������ȣ�����ֹ��point2�ĺ�������ȣ���ת�۵�mGamePointE1����ֹ��point2�����
			} else if(mIconConnectedPath[2].y == mIconConnectedPath[1].y && mIconConnectedPath[2].x == mIconConnectedPath[3].x &&
					mIconConnectedPath[1].x < mIconConnectedPath[3].x) {
				drawConnectionXYPoint(mIconConnectedPath[2].x, mIconConnectedPath[2].y, ANIM_GAME_VIEW_TOPRIGHT, canvas);
			// ���½�ת�ۣ�ת�۵�mGamePointE2��ת�۵�mGamePointE1�ĺ�������ȣ�����ֹ��point2����������ȣ���ת�۵�mGamePointE1����ֹ��point2�����
			} else if(mIconConnectedPath[2].x == mIconConnectedPath[1].x && mIconConnectedPath[2].y == mIconConnectedPath[3].y &&
					mIconConnectedPath[1].x < mIconConnectedPath[3].x) {
				drawConnectionXYPoint(mIconConnectedPath[2].x, mIconConnectedPath[2].y, ANIM_GAME_VIEW_BOTTOMLEFT, canvas);
			// ���½�ת�ۣ�ת�۵�mGamePointE2��ת�۵�mGamePointE1�ĺ�������ȣ�����ֹ��point2����������ȣ���ת�۵�mGamePointE1����ֹ��point2���ұ�
			} else if(mIconConnectedPath[2].x == mIconConnectedPath[1].x && mIconConnectedPath[2].y == mIconConnectedPath[3].y &&
					mIconConnectedPath[1].x > mIconConnectedPath[3].x) {
				drawConnectionXYPoint(mIconConnectedPath[2].x, mIconConnectedPath[2].y, ANIM_GAME_VIEW_BOTTOMRIGHT, canvas);
			}
		// mGamePointE1��point2���·�
		} else if(mIconConnectedPath[1].y > mIconConnectedPath[3].y) {
			// ���Ͻ�ת�ۣ�ת�۵�mGamePointE2��ת�۵�mGamePointE1�ĺ�������ȣ�����ֹ��point2����������ȣ���ת�۵�mGamePointE1����ֹ��point2�����
			if(mIconConnectedPath[2].x == mIconConnectedPath[1].x && mIconConnectedPath[2].y == mIconConnectedPath[3].y &&
					mIconConnectedPath[1].x < mIconConnectedPath[3].x) {
				drawConnectionXYPoint(mIconConnectedPath[2].x, mIconConnectedPath[2].y, ANIM_GAME_VIEW_TOPLEFT, canvas);
			// ���Ͻ�ת�ۣ�ת�۵�mGamePointE2��ת�۵�mGamePointE1�ĺ�������ȣ�����ֹ��point2����������ȣ���ת�۵�mGamePointE1����ֹ��point2���ұ�
			} else if(mIconConnectedPath[2].x == mIconConnectedPath[1].x && mIconConnectedPath[2].y == mIconConnectedPath[3].y &&
					mIconConnectedPath[1].x > mIconConnectedPath[3].x) {
				drawConnectionXYPoint(mIconConnectedPath[2].x, mIconConnectedPath[2].y, ANIM_GAME_VIEW_TOPRIGHT, canvas);
			// ���½�ת�ۣ�ת�۵�mGamePointE2��ת�۵�mGamePointE1����������ȣ�����ֹ��point2�ĺ�������ȣ���ת�۵�mGamePointE1����ֹ��point2���ұ�
			} else if(mIconConnectedPath[2].y == mIconConnectedPath[1].y && mIconConnectedPath[2].x == mIconConnectedPath[3].x &&
					mIconConnectedPath[1].x > mIconConnectedPath[3].x) {
				drawConnectionXYPoint(mIconConnectedPath[2].x, mIconConnectedPath[2].y, ANIM_GAME_VIEW_BOTTOMLEFT, canvas);
			// ���½�ת�ۣ�ת�۵�mGamePointE2��ת�۵�mGamePointE1����������ȣ�����ֹ��point2�ĺ�������ȣ���ת�۵�mGamePointE1����ֹ��point2�����
			} else if(mIconConnectedPath[2].y == mIconConnectedPath[1].y && mIconConnectedPath[2].x == mIconConnectedPath[3].x &&
					mIconConnectedPath[1].x < mIconConnectedPath[3].x) {
				drawConnectionXYPoint(mIconConnectedPath[2].x, mIconConnectedPath[2].y, ANIM_GAME_VIEW_BOTTOMRIGHT, canvas);
			}
		}
		
		// ���Ƶ�point2�������ж���ˮƽ���컹����ֱ����
		// ˮƽ����
		if(mIconConnectedPath[2].y == mIconConnectedPath[3].y) 
			drawConnectionXYPoint(mIconConnectedPath[3].x, mIconConnectedPath[3].y, ANIM_GAME_VIEW_HORIZONTAL, canvas);
		// ��ֱ����
		else if(mIconConnectedPath[2].x == mIconConnectedPath[3].x) 
			drawConnectionXYPoint(mIconConnectedPath[3].x, mIconConnectedPath[3].y, ANIM_GAME_VIEW_VETICAL, canvas);
		
		// ���Ƴ��ڵ�������ߣ�����3�����ߣ�
		drawConnectionXY(mIconConnectedPath[0].x, mIconConnectedPath[0].y, mIconConnectedPath[1].x, mIconConnectedPath[1].y, canvas);
		drawConnectionXY(mIconConnectedPath[1].x, mIconConnectedPath[1].y, mIconConnectedPath[2].x, mIconConnectedPath[2].y, canvas);
		drawConnectionXY(mIconConnectedPath[2].x, mIconConnectedPath[2].y, mIconConnectedPath[3].x, mIconConnectedPath[3].y, canvas);
	}
	
	/**
	 * @title drawConnectionXY
	 * @description ����������֮�����ߵ�animation������ȥ������֮ǰ�����ߣ�
	 * @param pointX1����ʼ��ĺ�����
	 * @param pointY1����ʼ���������
	 * @param pointX2����ֹ��ĺ�����
	 * @param pointY2����ֹ���������
	 * @param canvas�� ����
	 * @return
	 */
	private void drawConnectionXY(int pointX1, int pointY1, int pointX2, int pointY2, Canvas canvas) {
		//����
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
		
		//����
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
	 * @description �����������animation
	 * @param pointX��������
	 * @param pointY�� ������
	 * @param direction������
	 * @param canvas������
	 * @return
	 */
	private void drawConnectionXYPoint(int pointX, int pointY, int direction, Canvas canvas) {
		switch(direction) {
			// ����
			case ANIM_GAME_VIEW_HORIZONTAL:
				canvas.drawBitmap(((BitmapDrawable) getResources().getDrawable(mConnectedHorizontalAnimationImages[mIconConnectedTime % 4])).getBitmap(), 
						null, new RectF(getIndex2Screen(pointX, pointY).x, getIndex2Screen(pointX, pointY).y, getIndex2Screen(pointX, pointY).x + mIconWidth, 
						getIndex2Screen(pointX, pointY).y + mIconHeight), null);
				break;
			// ����
			case ANIM_GAME_VIEW_VETICAL:
				canvas.drawBitmap(((BitmapDrawable) getResources().getDrawable(mConnectedVerticalAnimationImages[mIconConnectedTime % 4])).getBitmap(), 
						null, new RectF(getIndex2Screen(pointX, pointY).x + 5, getIndex2Screen(pointX, pointY).y + 5, getIndex2Screen(pointX, pointY).x + 5 + mIconWidth, 
						getIndex2Screen(pointX, pointY).y + 5 + mIconHeight), null);
				break;
			// ���Ͻ�
			case ANIM_GAME_VIEW_TOPLEFT:
				canvas.drawBitmap(((BitmapDrawable) getResources().getDrawable(mConnectedTopLeftAnimationImages[mIconConnectedTime % 4])).getBitmap(), 
						null, new RectF(getIndex2Screen(pointX, pointY).x + 0.5F, getIndex2Screen(pointX, pointY).y + 5.5F, getIndex2Screen(pointX, pointY).x + 0.5F + mIconWidth, 
						getIndex2Screen(pointX, pointY).y + 5.5F + mIconHeight), null);
				break;
			// ���Ͻ�
			case ANIM_GAME_VIEW_TOPRIGHT:
				canvas.drawBitmap(((BitmapDrawable) getResources().getDrawable(mConnectedTopRightAnimationImages[mIconConnectedTime % 4])).getBitmap(), 
						null, new RectF(getIndex2Screen(pointX, pointY).x + 0.5F, getIndex2Screen(pointX, pointY).y + 5.5F, getIndex2Screen(pointX, pointY).x + 0.5F + mIconWidth, 
						getIndex2Screen(pointX, pointY).y + 5.5F + mIconHeight), null);
				break;
			// ���½�
			case ANIM_GAME_VIEW_BOTTOMLEFT:
				canvas.drawBitmap(((BitmapDrawable) getResources().getDrawable(mConnectedBottomLeftAnimationImages[mIconConnectedTime % 4])).getBitmap(), 
						null, new RectF(getIndex2Screen(pointX, pointY).x + 0.5F, getIndex2Screen(pointX, pointY).y + 5.5F, getIndex2Screen(pointX, pointY).x + 0.5F + mIconWidth, 
						getIndex2Screen(pointX, pointY).y + 5.5F + mIconHeight), null);
				break;
			// ���½�
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
	 * @description ��������������������ը��animation����
	 * @param pointX1����ʼ��ĺ�����
	 * @param pointY1����ʼ���������
	 * @param pointX2����ֹ��ĺ�����
	 * @param pointY2����ֹ���������
	 * @param canvas������
	 * @return
	 */
	private void drawConnectionGoodXY(int pointX1, int pointY1, int pointX2, int pointY2, Canvas canvas) {
		// ����point1���Ӻ�ը��animation����
		canvas.drawBitmap(((BitmapDrawable) getResources().getDrawable(mConnectedGoodAnimationImages[(mConnectedGoodTime - 12) / 4])).getBitmap(), 
				null, new RectF(getIndex2Screen(pointX1, pointY1).x, getIndex2Screen(pointX1, pointY1).y - 10.0F, getIndex2Screen(pointX1, pointY1).x + 1.3F * mIconWidth, 
				getIndex2Screen(pointX1, pointY1).y - 10.0F  + 1.3F * mIconHeight), null);
		// ����point2���Ӻ�ը��animation����
		canvas.drawBitmap(((BitmapDrawable) getResources().getDrawable(mConnectedGoodAnimationImages[(mConnectedGoodTime - 12) / 4])).getBitmap(), 
				null, new RectF(getIndex2Screen(pointX2, pointY2).x, getIndex2Screen(pointX2, pointY2).y - 10.0F, getIndex2Screen(pointX2, pointY2).x + 1.3F * mIconWidth, 
				getIndex2Screen(pointX2, pointY2).y - 10.0F + 1.3F * mIconHeight), null);
	}
	
	/**
	 * @title getIndex2Screen
	 * @description ����ӦͼƬ�ڲ����еĵ�����ת��������Ļ�е����Ͻǵ���Ļ������
	 * @param x����ӦͼƬ�ڲ����еĵ�����ĺ�����
	 * @param y:  ��ӦͼƬ�ڲ����еĵ������������
	 * @return Point
	 */
	private Point getIndex2Screen(int x, int y) {
		return new Point(x * mIconWidth - mOffsetX, y * mIconHeight - mOffsetY);
	}
	
	/**
	 * @title getScreen2Index
	 * @description ����Ļ������ͼƬ�ڲ����е���Ļ���������໥ת��
	 * 						����ͼƬ���Ͻ���Ļ�����ص����꣨x,y��,�õ���ͼƬ�ڶ�ά�����е��±�ֵ
	 * @param x��ͼƬ���Ͻ���Ļ�����ص�����ĺ�����
	 * @param y:  ͼƬ���Ͻ���Ļ�����ص������������
	 * @return Point
	 */
	private Point getScreen2Index(int x, int y) {
		return new Point((x + mOffsetX) / mIconWidth, (y + mOffsetY) / mIconHeight);
	}
	
	/**
	 * @title onTouchEvent
	 * @description �����û�������Ǹ���Ԫ��ͼƬ���õ�ͼƬ��mGameSurfaceMap�����е��±�ֵ
	 * @param event��event�¼�
	 * @return boolean
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() != MotionEvent.ACTION_DOWN) 
			return false;
		
		// ��ȡ�����(x, y)����ֵ
		int mTouchX = (int) event.getX();
		int mTouchY = (int) event.getY();
		
		// �õ������Ԫ��ͼƬ��mGameSurfaceMap�����е��±�ֵ
		Point p = getScreen2Index(mTouchX, mTouchY);
		// �����������ⲿ�հ״�������Ч
		if(p.x < 1 || p.x > mGameSurfaceMap.length - 1 || p.y < 1 || p.y > mGameSurfaceMap[0].length - 1)
			return false;
		
		if(mGameSurfaceMap[p.x][p.y] != 0 && mOnGameSurfaceItemClickListener != null)
			mOnGameSurfaceItemClickListener.onGameSurfaceItemClick(p);
		
		return true;
	}
	
	/**
	 * @title drawGameProgress
	 * @description ������Ϸ����ʱ��
	 * @param remainGameTime��ʣ��ʱ��
	 * @param totalGameTime: ��ʱ��
	 * @return
	 */
	public void drawGameProgress(long remainGameTime, long totalGameTime) {
		this.mRemainGameTime = remainGameTime;
		this.mTotalGameTime = totalGameTime;
		invalidate();
	}
	
	/**
	 * @title drawGameConnectedPath
	 * @description ������Ϸ���������ĵ����꼯��
	 * @param connectedPth����Ϸ���������ĵ���������
	 * @return
	 */
	public void drawGameConnectedPath(Point[] connectedPath) {
		this.mIconConnectedPath = connectedPath;
		// ���ˢ����ʱ���ӳ��Դﵽ����Ч��
		invalidate();
	}
	
	/**
	 * @title drawGameBombedPath
	 * @description ������Ϸը���ĵ����꼯��
	 * @param bombedPath����Ϸը���ĵ���������
	 * @return
	 */
	public void drawGameBombedPath(Point[] bombedPath) {
		this.mIconConnectedPath = bombedPath;
		this.mIconBombPath = bombedPath;
		// ���ˢ����ʱ���ӳ��Դﵽ����Ч��
		invalidate();
	}
	
	/**
	 * @title drawGameHintedPath
	 * @description ������Ϸ��ʾ�ĵ����꼯��
	 * @param hintedPath����Ϸ��ʾ�ĵ���������
	 * @return
	 */
	public void drawGameHintedPath(Point[] hintedPath) {
		mIconHintedList.add(hintedPath[0]);
		mIconHintedList.add(hintedPath[hintedPath.length - 1]);
		invalidate();
	}
	
	//�жϲ�ͬ�Ĺؿ��Ѷ�
	public void tidyGameMap(Point p1, Point p2){
		switch ((mGameCurrentLevel - 1) % s_LevelCount + 1){
			case GAME_LEVEL_NORMAL:
				break;
			// ȫ������
			case GAME_LEVEL_ALLLEFT:
				AllGameLeft(p1, p2);
				break;
			//ȫ������
			case GAME_LEVEL_ALLRIGHT:
				AllGameRight(p1, p2);
				break;
			//ȫ������
			case GAME_LEVEL_ALLUP:
				AllGameUp(p1, p2);
				break;
			//ȫ������
			case GAME_LEVEL_ALLDOWN:
				AllGameDown(p1, p2);
				break;
			//���ҷ���
			case GAME_LEVEL_LEFTRIGHTLEAVE:
				GameLeftRightLeave(p1, p2);
				break;
			//���Һ�£
			case GAME_LEVEL_LEFTRIGHTFOLD:
				GameLeftRightFold(p1, p2);
				break;
			//���·���
			case GAME_LEVEL_UPDOWNLEAVE:
				GameUpDownLeave(p1, p2);
				break;
			//���º�£
			case GAME_LEVEL_UPDOWNFOLD:
				GameUpDownFold(p1, p2);
				break;
			//ȫ�������Ͻǿ�£
			case GAME_LEVEL_ALLUPLEFT:
				AllGameUpLeft(p1, p2);
				break;
			//ȫ�������Ͻǿ�£
			case GAME_LEVEL_ALLUPRIGHT:
				AllGameUpRight(p1, p2);
				break;
			//ȫ�������½ǿ�£
			case GAME_LEVEL_ALLDOWNLEFT:
				AllGameDownLeft(p1, p2);
				break;
			//ȫ�������½ǿ�£
			case GAME_LEVEL_ALLDOWNRIGHT:
				AllGameDownRight(p1, p2);
				break;
			//��������·���
			case GAME_LEVEL_LEFTUPDOWNLEAVE:
				GameLeftUpDownLeave(p1, p2);
				break;
			//���ҵ����¿�£
			case GAME_LEVEL_RIGHTUPDOWNFOLD:
				GameRightUpDownFold(p1, p2);
				break;
			//���ϵ����ҷ���
			case GAME_LEVEL_UPLEFTRIGHTLEAVE:
				GameUpLeftRightLeave(p1, p2);
				break;
		}
	}
	
	//ȫ������
	public void AllGameLeft(Point p1, Point p2){
		int nGameColumn = mGameSurfaceMap.length - 2;
		List<Point> lGamePoint = new ArrayList<Point>();
		// x = 1 Ϊ������
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
	
	//ȫ������
	public void AllGameRight(Point p1, Point p2){
		List<Point> lGamePoint = new ArrayList<Point>();
		// x = 1 Ϊ������
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
	
	//ȫ������
	public void AllGameUp(Point p1, Point p2){
		int nGameRow = mGameSurfaceMap[0].length - 2;
		List<Point> lGamePoint = new ArrayList<Point>();
		// y = 1 Ϊ������
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
	
	//ȫ������
	public void AllGameDown(Point p1, Point p2){
		List<Point> lGamePoint = new ArrayList<Point>();
		// y = 1 Ϊ������
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
	
	//���ҷ���
	public void GameLeftRightLeave(Point p1, Point p2){
		int nGameColumn = mGameSurfaceMap.length - 2;
		List<Point> lGamePoint = new ArrayList<Point>();
		//P2�����ĸ������ȴ���P2����ɵĿն�
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
	
	//���Һ�£
	public void GameLeftRightFold(Point p1, Point p2){
		int nGameColumn = mGameSurfaceMap.length - 2;
		List<Point> lGamePoint = new ArrayList<Point>();
		//P2�����ĸ���������P2����ɵĿն�
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
	
	//���·���
	public void GameUpDownLeave(Point p1, Point p2){
		int nGameRow = mGameSurfaceMap[0].length - 2;
		List<Point> lGamePoint = new ArrayList<Point>();
		//P2�����ĸ������ȴ���P2����ɵĿն�
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
	
	//���º�£
	public void GameUpDownFold(Point p1, Point p2){
		int nGameRow = mGameSurfaceMap[0].length - 2;
		List<Point> lGamePoint = new ArrayList<Point>();
		//P2�����ĸ���������P2����ɵĿն�
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
	
	//ȫ�������Ͻǿ�£
	public void AllGameUpLeft(Point p1, Point p2){
		int nGameColumn = mGameSurfaceMap.length - 2;
		int nGameRow = mGameSurfaceMap[0].length - 2;
		List<Point> lGamePoint = new ArrayList<Point>();
		// y = 1 Ϊ������
		if(p1.y > p2.y){
			lGamePoint.add(p1);
			lGamePoint.add(p2);
		}else{
			lGamePoint.add(p2);
			lGamePoint.add(p1);
		}
		
		for(int k = 0; k < lGamePoint.size(); k++){
			Point p = lGamePoint.get(k);
			//������������ƶ�
			MovePoints(p, s_Down, nGameRow);
		}
		
		for(int k = 0; k < lGamePoint.size(); k++){
			Point p = lGamePoint.get(k);
			for(int i = p.y; i <= nGameRow; i++){
				if(mGameSurfaceMap[p.x][i] == 0){
					Point pNew = new Point(p.x, i);
					// �ұߵ������ƶ�
					MovePoints(pNew, s_Right, nGameColumn);
				}
			} 
		}
	}
	
	//ȫ�������Ͻǿ�£
	public void AllGameUpRight(Point p1, Point p2){
		int nGameRow = mGameSurfaceMap[0].length - 2;
		List<Point> lGamePoint = new ArrayList<Point>();
		// y = 1 Ϊ������
		if(p1.y > p2.y){
			lGamePoint.add(p1);
			lGamePoint.add(p2);
		}else{
			lGamePoint.add(p2);
			lGamePoint.add(p1);
		}
		
		for(int k = 0; k < lGamePoint.size(); k++){
			Point p = lGamePoint.get(k);
			//������������ƶ�
			MovePoints(p, s_Down, nGameRow);
		}
		
		for(int k = 0; k < lGamePoint.size(); k++){
			Point p = lGamePoint.get(k);
			for(int i = p.y; i >= 0; i--){
				if(mGameSurfaceMap[p.x][i] == 0){
					Point pNew = new Point(p.x, i);
					// ��ߵ������ƶ�
					MovePoints(pNew, s_Left, 0);
				}
			}
		}
	}
	
	//ȫ�������½ǿ�£
	public void AllGameDownRight(Point p1, Point p2){
		List<Point> lGamePoint = new ArrayList<Point>();
		// y = 1 Ϊ������
		if (p1.y < p2.y){
			lGamePoint.add(p1);
			lGamePoint.add(p2);
		}else{
			lGamePoint.add(p2);
			lGamePoint.add(p1);
		}
		
		for(int k = 0; k < lGamePoint.size(); k++){
			Point p = lGamePoint.get(k);
			//������������ƶ�
			MovePoints(p, s_Up, 0);
		}
		
		for(int k = 0; k < lGamePoint.size(); k++){
			Point p = lGamePoint.get(k);
			for(int i = p.y; i >= 0; i--){
				if(mGameSurfaceMap[p.x][i] == 0){
					Point pNew = new Point(p.x, i);
					// ��ߵ������ƶ�
					MovePoints(pNew, s_Left, 0);
				}
			}
		}
	}
	
	//ȫ�������½ǿ�£
	public void AllGameDownLeft(Point p1, Point p2){
		int nGameColumn = mGameSurfaceMap.length - 2;
		int nGameRow = mGameSurfaceMap[0].length - 2;
		List<Point> lGamePoint = new ArrayList<Point>();
		// y = 1 Ϊ������
		if (p1.y < p2.y){
			lGamePoint.add(p1);
			lGamePoint.add(p2);
		}else{
			lGamePoint.add(p2);
			lGamePoint.add(p1);
		}
		
		for(int k = 0; k < lGamePoint.size(); k++){
			Point p = lGamePoint.get(k);
			//������������ƶ�
			MovePoints(p, s_Up, 0);
		}
		
		for(int k = 0; k < lGamePoint.size(); k++){
			Point p = lGamePoint.get(k);
			for(int i = p.y; i <= nGameRow; i++){
				if(mGameSurfaceMap[p.x][i] == 0){
					Point pNew = new Point(p.x, i);
					// �ұߵ������ƶ�
					MovePoints(pNew, s_Right, nGameColumn);
				}
			} 
		}
	}
	
	//��������·���
	public void GameLeftUpDownLeave(Point p1, Point p2){
		int nGameColumn = mGameSurfaceMap.length - 2;
		int nGameRow = mGameSurfaceMap[0].length - 2;
		List<Point> lGamePoint = new ArrayList<Point>();
		//P2�����ĸ������ȴ���P2����ɵĿն�
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
					// �ұߵ������ƶ�
					MovePoints(pNew, s_Right, nGameColumn);
				}
			} 
		}
	}
	
	//���ҵ����¿�£
	public void GameRightUpDownFold(Point p1, Point p2){
		int nGameRow = mGameSurfaceMap[0].length - 2;
		List<Point> lGamePoint = new ArrayList<Point>();
		//P2�����ĸ���������P2����ɵĿն�
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
					// ��ߵ������ƶ�
					MovePoints(pNew, s_Left, 0);
				}
			}
		}
	}
	
	//���ϵ����ҷ���
	public void GameUpLeftRightLeave(Point p1, Point p2){
		int nGameColumn = mGameSurfaceMap.length - 2;
		int nGameRow = mGameSurfaceMap[0].length - 2;
		List<Point> lGamePoint = new ArrayList<Point>();
		//P2�����ĸ������ȴ���P2����ɵĿն�
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
					// �±ߵ������ƶ�
					MovePoints(pNew, s_Down, nGameRow);
				}
			}
		}
	}
		
	
	public void MovePoints(Point p, int direction, int end){
		int nGameFill;					//Ҫ������
		int nGameMove; 					//Ҫ���ƶ��������
		switch (direction){
			//Ҫ�ƶ��Ϸ��ģ�Ҳ�����Ϸ���������
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
			//Ҫ�ƶ��·��ģ�Ҳ�����·���������
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
			//Ҫ�ƶ���ߵģ�Ҳ������ߵ�������
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
			//Ҫ�ƶ��ұߵģ�Ҳ�����ұߵ�������
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
	
	//	��ȡ��������Ԫ��Ŀ��
	public int getmIconWidth() {
		return mIconWidth;
	}
	
	// ������������Ԫ��Ŀ��
	public void setmIconWidth(int mIconWidth) {
		this.mIconWidth = mIconWidth;
	}
	
	// ��ȡ��������Ԫ��ĸ߶�
	public int getmIconHeight() {
		return mIconHeight;
	}
	
	// ������������Ԫ��ĸ߶�
	public void setmIconHeight(int mIconHeight) {
		this.mIconHeight = mIconHeight;
	}
	
	// ��ȡ��������Ϸ��ͼƬ��Դ
	public Bitmap[] getmGameSurfaceIcons() {
		return mGameSurfaceIcons;
	}
	
	// ������������Ϸ��ͼƬ��Դ
	public void setmGameSurfaceIcons(Bitmap[] mGameSurfaceIcons) {
		this.mGameSurfaceIcons = mGameSurfaceIcons;
	}
	
	// ��ȡ��������Ԫ���λ������
	public int[][] getmGameSurfaceMap() {
		return mGameSurfaceMap;
	}
	
	// ������������Ԫ���λ������
	public void setmGameSurfaceMap(int[][] mGameSurfaceMap) {
		this.mGameSurfaceMap = mGameSurfaceMap;
		invalidate();
	}
	
	// ��ȡ��ѡ��ĵ�Ԫ������
	public List<Point> getmIconSelectedList() {
		return mIconSelectedList;
	}

	public void setmIconSelectedList(List<Point> mIconSelectedList) {
		this.mIconSelectedList = mIconSelectedList == null ? new ArrayList<Point>() : mIconSelectedList;
	}
	
	//������������������Ԫ��ļ�����
	public void setOnGameSurfaceItemClickListener(OnGameSurfaceItemClickListener onGameSurfaceItemClickListener) {
		this.mOnGameSurfaceItemClickListener = onGameSurfaceItemClickListener;
	}
	
	
	public interface OnGameSurfaceItemClickListener {
		public void onGameSurfaceItemClick(Point point);
	}
}

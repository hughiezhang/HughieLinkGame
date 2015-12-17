package com.hughie.linkgame.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.hughie.linkgame.R;
import com.hughie.linkgame.common.HughieGameApplication;
import com.hughie.linkgame.utils.HughieGameUtils;

/**
 * 游戏主界面的surface timer界面
 * @ClassName: HughieGameSurfaceTimer
 * @author hughiezhang
 * @since 2015-10-26 10:30
 */
public class HughieGameSurfaceTimer {
	public HughieGameApplication mGameApplication;
	private Bitmap[] mProgressAnimation;							//timer animation
	private Bitmap mTimerLight1;											//timer的progressbar进度显示
	private Bitmap mTimerLight2;											//timer的progressbar进度显示
	private Bitmap mTimerProgress;										//timer的progressbar进度条显示
	private Bitmap mTimerBackground;								//timer的progressbar背景显示
	
	private int mGameMaxTime;											//timer的最大时间
	private float mTimerLength;											//timer显示的长度
	private int mGameLightTime;											//进度条的时间
	
	private Paint mTimerPaint;												//画笔
	
	/**
	 * @title drawGameSelf
	 * @description timer绘制
	 * @param canvas：画布
	 * @param remainGameTime: timer的剩余时间
	 * @param gameFreeze: timer是否冻结
	 * @param gameFreezeStatus: timer的冻结状态
	 * @return
	 */
	public void drawGameSelf(Canvas canvas, int remainGameTime, boolean gameFreeze, 
			int gameFreezeStatus) {
		this.mGameLightTime = mGameMaxTime - remainGameTime;
		if(mGameMaxTime != -1) {
			//绘制timer的background背景
			canvas.drawBitmap(mTimerBackground, mGameApplication.displayWidth - 230.0F * mGameApplication.density, 
					3.0F * mGameApplication.density, mTimerPaint);
			if(gameFreeze) {
				
			} else {
				//绘制timer的进度条显示
				canvas.drawBitmap(mTimerProgress, null, new Rect(
						(int) (mGameApplication.displayWidth - 230.0F * mGameApplication.density + mTimerLength * mGameLightTime), //rect left 
						(int) (3.0F * mGameApplication.density), /*rect top*/(int) (mGameApplication.displayWidth - 60.0F * mGameApplication.density),//rect right 
						(int) (23.0F * mGameApplication.density)/*rect bottom*/), mTimerPaint);
				
				//绘制timer的mTimerLight1背景
				if(mGameLightTime == 0)
					canvas.drawBitmap(mTimerLight1, mGameApplication.displayWidth - 230.0F * mGameApplication.density + mTimerLength * mGameLightTime, 
							2.7F * mGameApplication.density, mTimerPaint);
				
				//绘制timer的mTimerLight2进度显示
				canvas.drawBitmap(mTimerLight2, mGameApplication.displayWidth - 230.0F * mGameApplication.density + mTimerLength * mGameLightTime, 
						2.7F * mGameApplication.density, mTimerPaint);
			}
		}
	}
	
	/**
	 * @title initGameTimer
	 * @description 初始化timer数据
	 * @param totalGameTime：timer的总时间
	 * @param context: timer的上下文
	 * @param check: timer是否暂停
	 * @return
	 */
	public void initGameTimer(long totalGameTime, Context context, boolean check) {
		this.mGameApplication = (HughieGameApplication) context.getApplicationContext();
		this.mProgressAnimation = new Bitmap[3];
		HughieGameUtils.initBitmap(mProgressAnimation, "imgv_game_surface_anim_time", context);
		this.mGameMaxTime = (int) totalGameTime;
		if(check) {
			if(mTimerLight1 == null)
				mTimerLight1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.imgv_game_surface_light1);
			
			if(mTimerLight2 == null)
				mTimerLight2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.imgv_game_surface_light2);
			
			if(mTimerProgress == null)
				mTimerProgress = BitmapFactory.decodeResource(context.getResources(), R.drawable.imgv_game_surface_timer_top_progress);
			
			if(mTimerBackground == null)
				mTimerBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.imgv_game_surface_timer_progress_background);
			
			this.mTimerPaint = new Paint();
			this.mTimerLength = 160.0F * mGameApplication.density / totalGameTime;
		}
	}
}

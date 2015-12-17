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
 * ��Ϸ�������surface timer����
 * @ClassName: HughieGameSurfaceTimer
 * @author hughiezhang
 * @since 2015-10-26 10:30
 */
public class HughieGameSurfaceTimer {
	public HughieGameApplication mGameApplication;
	private Bitmap[] mProgressAnimation;							//timer animation
	private Bitmap mTimerLight1;											//timer��progressbar������ʾ
	private Bitmap mTimerLight2;											//timer��progressbar������ʾ
	private Bitmap mTimerProgress;										//timer��progressbar��������ʾ
	private Bitmap mTimerBackground;								//timer��progressbar������ʾ
	
	private int mGameMaxTime;											//timer�����ʱ��
	private float mTimerLength;											//timer��ʾ�ĳ���
	private int mGameLightTime;											//��������ʱ��
	
	private Paint mTimerPaint;												//����
	
	/**
	 * @title drawGameSelf
	 * @description timer����
	 * @param canvas������
	 * @param remainGameTime: timer��ʣ��ʱ��
	 * @param gameFreeze: timer�Ƿ񶳽�
	 * @param gameFreezeStatus: timer�Ķ���״̬
	 * @return
	 */
	public void drawGameSelf(Canvas canvas, int remainGameTime, boolean gameFreeze, 
			int gameFreezeStatus) {
		this.mGameLightTime = mGameMaxTime - remainGameTime;
		if(mGameMaxTime != -1) {
			//����timer��background����
			canvas.drawBitmap(mTimerBackground, mGameApplication.displayWidth - 230.0F * mGameApplication.density, 
					3.0F * mGameApplication.density, mTimerPaint);
			if(gameFreeze) {
				
			} else {
				//����timer�Ľ�������ʾ
				canvas.drawBitmap(mTimerProgress, null, new Rect(
						(int) (mGameApplication.displayWidth - 230.0F * mGameApplication.density + mTimerLength * mGameLightTime), //rect left 
						(int) (3.0F * mGameApplication.density), /*rect top*/(int) (mGameApplication.displayWidth - 60.0F * mGameApplication.density),//rect right 
						(int) (23.0F * mGameApplication.density)/*rect bottom*/), mTimerPaint);
				
				//����timer��mTimerLight1����
				if(mGameLightTime == 0)
					canvas.drawBitmap(mTimerLight1, mGameApplication.displayWidth - 230.0F * mGameApplication.density + mTimerLength * mGameLightTime, 
							2.7F * mGameApplication.density, mTimerPaint);
				
				//����timer��mTimerLight2������ʾ
				canvas.drawBitmap(mTimerLight2, mGameApplication.displayWidth - 230.0F * mGameApplication.density + mTimerLength * mGameLightTime, 
						2.7F * mGameApplication.density, mTimerPaint);
			}
		}
	}
	
	/**
	 * @title initGameTimer
	 * @description ��ʼ��timer����
	 * @param totalGameTime��timer����ʱ��
	 * @param context: timer��������
	 * @param check: timer�Ƿ���ͣ
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

package com.hughie.linkgame.widget;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import com.hughie.linkgame.R;
import com.hughie.linkgame.common.HughieGameApplication;

/**
 * ��Ϸ�������surface��������
 * @ClassName: HughieGameSurfaceBackground
 * @author hughiezhang
 * @since 2015-10-23 11:30
 */
public class HughieGameSurfaceBackground {
	public HughieGameApplication mGameApplication;
	public Paint mGameBackPaint;										//���廭��
	public Bitmap mGameBackBitmap;									//���Ʊ���ͼƬ
	public Bitmap mGameTopBackBitmap;							//���ƶ��˱���ͼƬ
	public Bitmap mGameCloudBitmap1;								//�����Ʋ�1ͼƬ
	public Bitmap mGameCloudBitmap2;								//�����Ʋ�2ͼƬ
	private Point mGameCloudPoint1;									//�Ʋ�1��λ��
	private Point mGameCloudPoint2;									//�Ʋ�2��λ��
	
	//������Ϸ����
	public void drawGameSelf(Canvas canvas) {
		//����������
		int mBackWidth = mGameApplication.displayWidth;
		//��������߶�
		int mBackHeight = mGameApplication.displayHeight;
		//���ƽ�����η���
		Rect mGameBackRect = new Rect(0, 0, mBackWidth, mBackHeight);
		canvas.drawBitmap(mGameBackBitmap, null, mGameBackRect, mGameBackPaint);
		
		//�����Ʋ�1
		canvas.drawBitmap(mGameCloudBitmap1, mGameCloudPoint1.x, mGameCloudPoint1.y, mGameBackPaint);
		//�����Ʋ�2
		canvas.drawBitmap(mGameCloudBitmap2, mGameCloudPoint2.x, mGameCloudPoint2.y, mGameBackPaint);
		
		mGameCloudPoint1.x = mGameCloudPoint1.x + 1;
		mGameCloudPoint2.x = mGameCloudPoint2.x - 1;
		if(mGameCloudPoint1.x > mGameApplication.displayWidth)
			mGameCloudPoint1.x = 0 - mGameCloudBitmap1.getWidth();
		if(mGameCloudPoint2.x < 0 - mGameCloudBitmap2.getWidth())
			mGameCloudPoint2.x = mGameApplication.displayWidth;
		
		//���Ʊ߿�
		canvas.drawBitmap(mGameTopBackBitmap, null, new Rect(0, mGameApplication.mPaddingTop - (int)(30.0F * mGameApplication.density), 
				mGameApplication.displayWidth, mGameApplication.displayHeight), mGameBackPaint);
	}
	
	//��ʼ����Ϸ��������
	public void initGameBackground(Context context) {
		this.mGameApplication = (HughieGameApplication) context.getApplicationContext();
		this.mGameBackPaint = new Paint();
		this.mGameCloudPoint1 = new Point(100, 115);
		this.mGameCloudPoint2 = new Point(mGameApplication.displayWidth, mGameApplication.displayHeight / 3);
		switch(new Random().nextInt(5)) {
			case 0:
				this.mGameBackBitmap = BitmapFactory.decodeResource(context.getResources(), 
						R.drawable.imgv_game_surface_background1);
				this.mGameCloudBitmap1 = BitmapFactory.decodeResource(context.getResources(), 
						R.drawable.imgv_game_surface_cloud3);
				this.mGameCloudBitmap2 = BitmapFactory.decodeResource(context.getResources(), 
						R.drawable.imgv_game_surface_cloud4);
				this.mGameTopBackBitmap = BitmapFactory.decodeResource(context.getResources(), 
						R.drawable.imgv_game_surface_bgtop3);
				break;
			case 1:
				this.mGameBackBitmap = BitmapFactory.decodeResource(context.getResources(), 
						R.drawable.imgv_game_surface_background2);
				this.mGameCloudBitmap1 = BitmapFactory.decodeResource(context.getResources(), 
						R.drawable.imgv_game_surface_cloud1);
				this.mGameCloudBitmap2 = BitmapFactory.decodeResource(context.getResources(), 
						R.drawable.imgv_game_surface_cloud2);
				this.mGameTopBackBitmap = BitmapFactory.decodeResource(context.getResources(), 
						R.drawable.imgv_game_surface_bgtop2);
				break;
			case 2:
				this.mGameBackBitmap = BitmapFactory.decodeResource(context.getResources(), 
						R.drawable.imgv_game_surface_background3);
				this.mGameCloudBitmap1 = BitmapFactory.decodeResource(context.getResources(), 
						R.drawable.imgv_game_surface_cloud5);
				this.mGameCloudBitmap2 = BitmapFactory.decodeResource(context.getResources(), 
						R.drawable.imgv_game_surface_cloud6);
				this.mGameTopBackBitmap = BitmapFactory.decodeResource(context.getResources(), 
						R.drawable.imgv_game_surface_bgtop1);
				break;
			case 3:
				this.mGameBackBitmap = BitmapFactory.decodeResource(context.getResources(), 
						R.drawable.imgv_game_surface_background4);
				this.mGameCloudBitmap1 = BitmapFactory.decodeResource(context.getResources(), 
						R.drawable.imgv_game_surface_cloud3);
				this.mGameCloudBitmap2 = BitmapFactory.decodeResource(context.getResources(), 
						R.drawable.imgv_game_surface_cloud4);
				this.mGameTopBackBitmap = BitmapFactory.decodeResource(context.getResources(), 
						R.drawable.imgv_game_surface_bgtop3);
				break;
			case 4:
				this.mGameBackBitmap = BitmapFactory.decodeResource(context.getResources(), 
						R.drawable.imgv_game_surface_background5);
				this.mGameCloudBitmap1 = BitmapFactory.decodeResource(context.getResources(), 
						R.drawable.imgv_game_surface_cloud5);
				this.mGameCloudBitmap2 = BitmapFactory.decodeResource(context.getResources(), 
						R.drawable.imgv_game_surface_cloud6);
				this.mGameTopBackBitmap = BitmapFactory.decodeResource(context.getResources(), 
						R.drawable.imgv_game_surface_bgtop1);
				break;
			default:
				this.mGameBackBitmap = BitmapFactory.decodeResource(context.getResources(), 
						R.drawable.imgv_game_surface_background1);
				this.mGameCloudBitmap1 = BitmapFactory.decodeResource(context.getResources(), 
						R.drawable.imgv_game_surface_cloud3);
				this.mGameCloudBitmap2 = BitmapFactory.decodeResource(context.getResources(), 
						R.drawable.imgv_game_surface_cloud4);
				this.mGameTopBackBitmap = BitmapFactory.decodeResource(context.getResources(), 
						R.drawable.imgv_game_surface_bgtop3);
				break;
		}
	}
}

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
 * 游戏主界面的surface背景界面
 * @ClassName: HughieGameSurfaceBackground
 * @author hughiezhang
 * @since 2015-10-23 11:30
 */
public class HughieGameSurfaceBackground {
	public HughieGameApplication mGameApplication;
	public Paint mGameBackPaint;										//定义画笔
	public Bitmap mGameBackBitmap;									//绘制背景图片
	public Bitmap mGameTopBackBitmap;							//绘制顶端背景图片
	public Bitmap mGameCloudBitmap1;								//绘制云彩1图片
	public Bitmap mGameCloudBitmap2;								//绘制云彩2图片
	private Point mGameCloudPoint1;									//云彩1的位置
	private Point mGameCloudPoint2;									//云彩2的位置
	
	//绘制游戏背景
	public void drawGameSelf(Canvas canvas) {
		//背景界面宽度
		int mBackWidth = mGameApplication.displayWidth;
		//背景界面高度
		int mBackHeight = mGameApplication.displayHeight;
		//绘制界面矩形方框
		Rect mGameBackRect = new Rect(0, 0, mBackWidth, mBackHeight);
		canvas.drawBitmap(mGameBackBitmap, null, mGameBackRect, mGameBackPaint);
		
		//绘制云彩1
		canvas.drawBitmap(mGameCloudBitmap1, mGameCloudPoint1.x, mGameCloudPoint1.y, mGameBackPaint);
		//绘制云彩2
		canvas.drawBitmap(mGameCloudBitmap2, mGameCloudPoint2.x, mGameCloudPoint2.y, mGameBackPaint);
		
		mGameCloudPoint1.x = mGameCloudPoint1.x + 1;
		mGameCloudPoint2.x = mGameCloudPoint2.x - 1;
		if(mGameCloudPoint1.x > mGameApplication.displayWidth)
			mGameCloudPoint1.x = 0 - mGameCloudBitmap1.getWidth();
		if(mGameCloudPoint2.x < 0 - mGameCloudBitmap2.getWidth())
			mGameCloudPoint2.x = mGameApplication.displayWidth;
		
		//绘制边框
		canvas.drawBitmap(mGameTopBackBitmap, null, new Rect(0, mGameApplication.mPaddingTop - (int)(30.0F * mGameApplication.density), 
				mGameApplication.displayWidth, mGameApplication.displayHeight), mGameBackPaint);
	}
	
	//初始化游戏背景数据
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

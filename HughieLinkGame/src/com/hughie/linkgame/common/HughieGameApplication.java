package com.hughie.linkgame.common;

import android.app.Activity;
import android.content.Context;

import com.hughie.link.support.common.HughieApplication;
import com.hughie.link.support.util.HughieScreenManager;
import com.hughie.linkgame.R;
import com.hughie.linkgame.utils.HughieSoundUtils;
import com.umeng.analytics.MobclickAgent;


public class HughieGameApplication extends HughieApplication {
	private static HughieGameApplication mHughieGameApplication;
	
	public float density;										//获取密度值
	public int displayWidth;									//获取屏幕分辨率宽度
	public int displayHeight;								//获取屏幕分辨率高度
	public int mIconWidth;									//获取连连看单元格的宽度
	public int mIconHeight;									//获取连连看单元格的高度
	public int mPaddingLeft;								//获取连连看单元格的左边距
	public int mPaddingTop;								//获取连连看单元格的上边距
	
	public HughieSoundUtils mSoundUtils;
	
	// 列数maxColumn,行数maxRow 包括了边界（故实际单元格数量为8*10）
	public static int mMaxColumn = 10;				//显示最大的列数
	public static int mMaxRow = 12;					//显示最大的行数

	@Override
	public void onCreate() {
		super.onCreate();
		mHughieGameApplication = this; 
		
		HughieCrashHandler mHughieCrashHandler = HughieCrashHandler.getInstance();
		mHughieCrashHandler.init(getApplicationContext());
		
		//运行机器的密度值
		this.density = HughieScreenManager.getDensity();
		//运行机器的宽度
		this.displayWidth = HughieScreenManager.getScreenWidth();
		//运行机器的高度
		this.displayHeight = HughieScreenManager.getScreenHeight();
		
		//获取连连看单元格的高度和宽度
		this.mIconWidth =(int) ((displayWidth - 24) / mMaxColumn);
		this.mIconHeight = mIconWidth;
		
		//获取连连看单元格的左边距
		this.mPaddingLeft = (int) ((HughieScreenManager.getScreenWidth() - 10.0F * this.mIconWidth) / 2);
		//获取连连看单元格的上边距
		this.mPaddingTop = (int) ((HughieScreenManager.getScreenHeight() - 50.0F * HughieScreenManager.getDensity()
				- 12.0F * mIconWidth) / 2);
		
		this.mSoundUtils = new HughieSoundUtils(getApplicationContext());
	}
	
	public synchronized static HughieGameApplication getInstance(){
		return mHughieGameApplication;
	}
	
	public static final void setHughieActivityTheme(Activity activity){
		activity.setTheme(R.style.HughieAppTheme_Blue);
	}
	
	public static final void exitHughieApp(Context context) {
		// 友盟统计 退出时调用
		HughieApplicationManager.getInstance().exitApp(context);
		MobclickAgent.onKillProcess(context);
	}
}

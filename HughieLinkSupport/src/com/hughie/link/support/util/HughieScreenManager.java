package com.hughie.link.support.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * ÆÁÄ»¹ÜÀíÆ÷
 * @ClassName: HughieScreenManager
 * @author hughiezhang
  * @since 2015-08-26 14:49
 */
public class HughieScreenManager {
	public static HughieScreenManager mHughieScreenManager;
	private Context context;
	
	private static int screenWidth;								//ÆÁÄ»¿í¶È
    private static int screenHeight;
    private static float density;
	
	public static HughieScreenManager getInstance(Context context){
		if(mHughieScreenManager == null) {
			mHughieScreenManager = new HughieScreenManager();
			mHughieScreenManager.context = context;
			return mHughieScreenManager;
		}
		
		return mHughieScreenManager;
	}

	public static int getScreenWidth() {
		return screenWidth;
	}

	public static int getScreenHeight() {
		return screenHeight;
	}

	public static float getDensity() {
		return density;
	}
	
	public void init(){
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		screenHeight = dm.heightPixels;
		screenWidth = dm.widthPixels;
		density = dm.density;
	}
}

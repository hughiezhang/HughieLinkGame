package com.hughie.linkgame.utils;

import android.app.Activity;
import android.graphics.Point;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * @description ÅÐ¶ÏºáÆÁ»¹ÊÇÊúÆÁµÄutils
 * @ClassName: HughieTVFitUtils
 * @author hughiezhang
 * @since 2015-08-31 13:44
 */
public class HughieTVFitUtils {
	private static int screenW = 0;
	private static int screenH = 0;
	private static boolean isInit = false;
	
	private static boolean init(Activity activity){
		Point mPoint = HughieActivityUtils.getScreenSize(activity);
		if(mPoint.x > 0 && mPoint.y > 0){
			screenW = mPoint.x;
			screenH = mPoint.y;
			isInit = true;
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isTvMode(Activity activity) {
		if (isInit)
			return screenW > screenH;
		else{
			if(init(activity)){
				return screenW > screenH;
			}
			return false;
		}
	}
	
	public static void setTVImageScale(Activity activity, ImageView imageView){
		if(isTvMode(activity)) {
			imageView.setScaleType(ScaleType.FIT_CENTER);
			imageView.setBackgroundResource(0);
		}
	}
}

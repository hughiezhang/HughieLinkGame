package com.hughie.linkgame.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * app帮助工具类
 * @ClassName: HughieAppUtils
 * @author hughiezhang
 * @since 2015-09-16 16:06
 */
public class HughieAppUtils {
	/****
	 * 判断是否有网络
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if(info != null) {
				for(int i = 0; i < info.length; i++) {
					if(info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
}

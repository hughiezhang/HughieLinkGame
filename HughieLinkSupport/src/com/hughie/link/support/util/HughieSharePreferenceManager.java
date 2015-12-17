package com.hughie.link.support.util;

import android.content.Context;

/**
 * SharePreference管理器
 * @ClassName: HughieSharePreferenceManager
 * @author hughiezhang
  * @since 2015-08-26 14:37
 */
public class HughieSharePreferenceManager {
	private static Context mContext;
	
	private HughieSharePreferenceManager() {
		
	}
	
	/**
	 * 在AppAPPliction中配置一次就行
	 * @param context
	 */
	public static void init(Context context) {
		if (mContext == null) {
			mContext = context;
		}
	}
}

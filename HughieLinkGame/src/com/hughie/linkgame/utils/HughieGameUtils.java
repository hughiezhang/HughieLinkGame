package com.hughie.linkgame.utils;

import java.util.List;

import com.hughie.linkgame.common.HughieSPManager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 游戏工具类
 * @ClassName: HughieGameUtils
 * @author hughiezhang
 * @since 2015-10-12 11:13
 */
public class HughieGameUtils {
	// game pause apps packageName数组
	public static String[] mGamePauseAppsPackageName = {"com.dotsnumbers", "com.bwdclock", "com.crazybirdscrushsaga",
			"com.memorypuzzlepro", "com.pandacalculator"};
	
	public HughieGameUtils() {
		
	}
	
	//获取最大的关数
	public static int getMaxLevel(Context context) {
		return context.getSharedPreferences(HughieSPManager.SPDefault, Context.MODE_PRIVATE)
				.getInt(HughieSPManager.SP_GameMaxLevel, 1);
	}
	
	/**
	 * @title initBitmap
	 * @description 初始化图片资源
	 * @param bitmaps 图片资源数组
	 * @param strResource: 图片资源数组的前缀
	 * @param context: 上下文
	 * @return
	 */
	public static void initBitmap(Bitmap[] bitmaps, String strResource, Context context) {
		for(int i = 0; i < bitmaps.length; i++) {
			Object[] mObjects = new Object[2];
			mObjects[0] = strResource;
			mObjects[1] = Integer.valueOf(i + 1);
			bitmaps[i] = BitmapFactory.decodeResource(context.getResources(), context.getResources()
					.getIdentifier(String.format("%s%d", mObjects), "drawable", context.getPackageName()));
		}
	}
	
	/**
	 * @title isRunGamePauseApps
	 * @description 判断game pause apps是否已经运行
	 * @param packageName： apps的package name
	 * @return boolean
	 */
	@SuppressWarnings("unused")
	public static boolean isRunGamePauseApps(Context context, String packageName) {
		try {
			PackageInfo mPackageInfo;
			mPackageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
			Intent mIntent = new Intent(Intent.ACTION_MAIN, null);
			mIntent.setPackage(packageName);
			PackageManager mPackageManager = context.getPackageManager();
			List<ResolveInfo> mResolveApps = mPackageManager.queryIntentActivities(mIntent, 0);
			
			ResolveInfo mResolveInfo = mResolveApps.iterator().next();
			if(mResolveInfo != null) {
				return true;
			} else {
				return false;
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
}

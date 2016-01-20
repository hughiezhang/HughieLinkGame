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
 * ��Ϸ������
 * @ClassName: HughieGameUtils
 * @author hughiezhang
 * @since 2015-10-12 11:13
 */
public class HughieGameUtils {
	// game pause apps packageName����
	public static String[] mGamePauseAppsPackageName = {"com.dotsnumbers", "com.bwdclock", "com.crazybirdscrushsaga",
			"com.memorypuzzlepro", "com.pandacalculator"};
	
	public HughieGameUtils() {
		
	}
	
	//��ȡ���Ĺ���
	public static int getMaxLevel(Context context) {
		return context.getSharedPreferences(HughieSPManager.SPDefault, Context.MODE_PRIVATE)
				.getInt(HughieSPManager.SP_GameMaxLevel, 1);
	}
	
	/**
	 * @title initBitmap
	 * @description ��ʼ��ͼƬ��Դ
	 * @param bitmaps ͼƬ��Դ����
	 * @param strResource: ͼƬ��Դ�����ǰ׺
	 * @param context: ������
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
	 * @description �ж�game pause apps�Ƿ��Ѿ�����
	 * @param packageName�� apps��package name
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

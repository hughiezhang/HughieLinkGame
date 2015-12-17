package com.hughie.linkgame.dao;

import com.hughie.link.support.common.HughieLoggerManager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * @description 判断app版本
 * @ClassName: HughieVersionDao
 * @author hughiezhang
 * @since 2015-08-31 14:31
 */
public final class HughieVersionDao {
	public static final int getversionCode(Context context){
		try{
			PackageManager mPackageManager = context.getPackageManager();
			PackageInfo mPackageInfo = mPackageManager.getPackageInfo(context.getPackageName(), 0);
			return mPackageInfo.versionCode;
		}catch(Exception e){
			HughieLoggerManager.println("获取版本号出错");
			HughieLoggerManager.printStackTrace(e);
			return 0;
		}
	}
}

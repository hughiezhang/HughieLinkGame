package com.hughie.linkgame.common;

import com.hughie.link.support.common.HughieLoggerManager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 获取app运行信息的管理类
 * @ClassName: HughieAppRunInfoManager
 * @author hughiezhang
 * @since 2015-09-01 17:50
 */
public final class HughieAppRunInfoManager {
	public static final String getPackageVersionName(Context context){
		PackageManager mPackageManager = context.getPackageManager();
		try{
			PackageInfo mInfo = mPackageManager.getPackageInfo(context.getPackageName(), 0);
			return mInfo.versionName;
		} catch(Exception e){
			HughieLoggerManager.println("获取版本名字出错");
			HughieLoggerManager.printStackTrace(e);
			return "";
		}
	}
}

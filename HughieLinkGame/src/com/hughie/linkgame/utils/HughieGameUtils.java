package com.hughie.linkgame.utils;

import com.hughie.linkgame.common.HughieSPManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 游戏工具类
 * @ClassName: HughieGameUtils
 * @author hughiezhang
 * @since 2015-10-12 11:13
 */
public class HughieGameUtils {
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
}

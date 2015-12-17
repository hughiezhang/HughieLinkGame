package com.hughie.linkgame.utils;

import com.hughie.linkgame.common.HughieSPManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * ��Ϸ������
 * @ClassName: HughieGameUtils
 * @author hughiezhang
 * @since 2015-10-12 11:13
 */
public class HughieGameUtils {
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
}

package com.hughie.linkgame.utils;

import com.google.gson.Gson;

/**
 * ������������ӿ�
 * @ClassName: HughieNetListener
 * @author hughiezhang
 * @since 2015-09-11 14:51
 */
public class HughieGsonUtils {
	private final static Gson gson = new Gson();
	
	public static Gson getGsonInstance() {
		return gson;
	}
}

package com.hughie.link.support.util;

import java.io.InputStream;
import java.util.Properties;

import android.content.Context;

/**
 * 文件工具操作类
 * @ClassName: PropertiesUtils
 * @author hughiezhang
 * @since 2015.9.11 11:15
 */
public class HughiePropertiesUtils {
	public static Properties urlProps = null;
	
	/**
     * 在HughieApplication中初始化
     * @param c
     * @return
     */
	public static Properties init(Context context){
		if(urlProps == null){
			urlProps = new Properties();
			try {
				// 方法一：通过activity中的context攻取setting.properties的FileInputStream
				InputStream in = context.getAssets().open("appConfig.properties");
				// 方法二：通过class获取setting.properties的FileInputStream
                // InputStream in =
                // PropertiesUtill.class.getResourceAsStream("/assets/  setting.properties "));
				urlProps.load(in);
				in.close();
			} catch(Exception e1) {
				e1.printStackTrace();
			}
		}
		
		return urlProps;
	}
	
	public static Properties getProperties() {
		return urlProps;
	}
}

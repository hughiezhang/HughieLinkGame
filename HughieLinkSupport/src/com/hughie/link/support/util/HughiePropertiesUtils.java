package com.hughie.link.support.util;

import java.io.InputStream;
import java.util.Properties;

import android.content.Context;

/**
 * �ļ����߲�����
 * @ClassName: PropertiesUtils
 * @author hughiezhang
 * @since 2015.9.11 11:15
 */
public class HughiePropertiesUtils {
	public static Properties urlProps = null;
	
	/**
     * ��HughieApplication�г�ʼ��
     * @param c
     * @return
     */
	public static Properties init(Context context){
		if(urlProps == null){
			urlProps = new Properties();
			try {
				// ����һ��ͨ��activity�е�context��ȡsetting.properties��FileInputStream
				InputStream in = context.getAssets().open("appConfig.properties");
				// ��������ͨ��class��ȡsetting.properties��FileInputStream
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

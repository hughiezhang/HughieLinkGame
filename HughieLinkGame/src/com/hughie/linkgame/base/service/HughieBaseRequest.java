package com.hughie.linkgame.base.service;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.hughie.link.support.util.HughiePropertiesUtils;

import android.content.Context;

/**
 * 主请求，其他请求继承此请求
 * @ClassName: HughieBaseRequest
 * @author hughiezhang
 * @since 2015-09-11 10:38
 */
public abstract class HughieBaseRequest {
	private REQUESTTYPE mRequestType;
	private REQUESTSHOWTYPE mShowType;
	public RequestLoadType mLoadType;
	
	private Context mContext;
	
	public HughieBaseRequest() {
		this(REQUESTTYPE.GET, REQUESTSHOWTYPE.HIDE);
	}
	
	/**
	 * 构造函数
	 * @param REQUESTTYPE, GET和POST
	 * @author hughiezhang
	 */
	public HughieBaseRequest(REQUESTTYPE requestType, REQUESTSHOWTYPE showType) {
		setRequestType(requestType);
		setShowType(showType);
		setmLoadType(RequestLoadType.ALL);
	}
	
	
	
	public Context getContext() {
		return mContext;
	}

	public void setContext(Context mContext) {
		this.mContext = mContext;
	}
	
	public REQUESTTYPE getRequestType() {
		return mRequestType;
	}


	public void setRequestType(REQUESTTYPE mRequestType) {
		this.mRequestType = mRequestType;
	}

	public REQUESTSHOWTYPE getShowType() {
		return mShowType;
	}

	public void setShowType(REQUESTSHOWTYPE mShowType) {
		this.mShowType = mShowType;
	}
	
	public RequestLoadType getmLoadType() {
		return mLoadType;
	}

	public void setmLoadType(RequestLoadType mLoadType) {
		this.mLoadType = mLoadType;
	}
	
	/**
	 * 请求方式 get or post 默认为get
	 * @author hughiezhang
	 */
	public static enum REQUESTTYPE {
		GET,			// 默认get方式
		POST			// post方式
	}
	
	public static enum REQUESTSHOWTYPE {
		HIDE,		// 默认隐藏
		DISPLAY	// 展示加载框
	}
	
	/**
	 * 获取全部数据or加载更多
	 * @author hughiezhang
	 */
	public static enum RequestLoadType {
		ALL,		//全部加载
		MORE	//加载更多
	}
	
	
	private String imageFlag;

	public String getImageFlag() {
		return imageFlag;
	}

	public void setImageFlag(String imageFlag) {
		this.imageFlag = imageFlag;
	}
	
	//单张照片
	private File bitmaps;

	public File getBitmaps() {
		return bitmaps;
	}

	public void setBitmaps(File bitmaps) {
		this.bitmaps = bitmaps;
	}
	
	/**
	 * 获取当前请求保存的URL for example
	 * ,http://192.168.2.9/property/i/rules/list.do?communityId=1
	 * @return
	 */
	public abstract String getUrl();
	
	/**
	 * @return the sERVIER_URL
	 */
	public String getSERVIER_URL() {
		String URL = "";
		String environmentString = HughiePropertiesUtils.getProperties().getProperty("ENVIRONMENT");
		int environment = Integer.parseInt(environmentString);
		if (environment == 0) {
			URL = HughiePropertiesUtils.getProperties().getProperty("TEST_SERVIER_URL");	//测试服务器地址
		} else if (environment == 1) {
			URL = HughiePropertiesUtils.getProperties().getProperty("SERVIER_URL");	//正式服务器地址
		} else {
			URL = HughiePropertiesUtils.getProperties().getProperty("DEVELOP_SERVIER_URL");	//开发服务器地址 
		}
		
		return URL;
	}
	
	public String getPath() {
		String className = this.getClass().getSimpleName();
		String path = HughiePropertiesUtils.getProperties().getProperty(className);
		
		return path;
	}
	
	/**
	 * 包含images或image字段不会被反射为键值对
	 * @return Map<String, String>
	 */
	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<>();
		Field[] fileds = this.getClass().getDeclaredFields();
		Method method;
		try {
			for(Field field : fileds){
				if(field.getName().equals("path") || 
						field.getName().contains("image")) {
					continue;
				}
				
				StringBuilder nameBuilder = new StringBuilder(field.getName());
				nameBuilder.setCharAt(0, (nameBuilder.charAt(0) + "").toUpperCase().charAt(0));
				method = this.getClass().getMethod("get" + nameBuilder);
				if(method.invoke(this) != null){
					map.put(field.getName(), method.invoke(this) + "");
				}
			}
		} catch(NoSuchMethodException e) {
			e.printStackTrace();
		} catch(IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return map;
	}
}

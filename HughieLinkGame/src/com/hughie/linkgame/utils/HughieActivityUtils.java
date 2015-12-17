package com.hughie.linkgame.utils;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.view.Display;

/**
 * @description Activity帮助器类
 * @ClassName: HughieActivityUtils
 * @author hughiezhang
 * @since 2015-08-31 13:55
 */
public class HughieActivityUtils {
	public static class Params {
		
		public final ArrayList<NameValue> nameValueArray = new ArrayList<NameValue>();
		
		public static class NameValue {
			public final String name;
			public final Object value;
			public NameValue(String name, Object value) {
				this.name = name;
				this.value = value;
			}
		}
		
		public Params put(String name,String value){
			appendToParamsArray(name, value);
			return this;
		}
		
		public Params put(String name,int value){
			appendToParamsArray(name, value);
			return this;
		}
		
		public Params put(String name,boolean value){
			appendToParamsArray(name, value);
			return this;
		}
		
		public Params put(String name,float value){
			appendToParamsArray(name, value);
			return this;
		}
		
		public Params put(String name,long value){
			appendToParamsArray(name, value);
			return this;
		}
		
		public Params put(String name,double value){
			appendToParamsArray(name, value);
			return this;
		}
		
		public Params put(String name,Parcelable value){
			appendToParamsArray(name, value);
			return this;
		}
		
		private Params appendToParamsArray(String name,Object value){
			if(value != null && !"".equals(name) && !"".equals(value) ){
				nameValueArray.add(new NameValue(name, value));
			}
			return this;
		}
		
		public static Params build(){
			return new Params();
		}
	}
	
	/**
	 * @description 获取屏幕大小，单位px
	 * @param activity Activity
	 * @return 屏幕大小对象
	 */
	public static Point getScreenSize(Activity activity){
		Display mDisplay = activity.getWindowManager().getDefaultDisplay();
		Point mSize = new Point();
		mSize.set(mDisplay.getWidth(), mDisplay.getHeight());
		
		return mSize;
	}
	
	/**
	 * @description 返回字体
	 * @param Context context
	 * @param String fontTypeString 字体名称
	 * @return 字体
	 */
	public static Typeface getFontType(Context context, String fontTypeString){
		return Typeface.createFromAsset(context.getAssets(), fontTypeString);
	}
	
	/**
	 * 带返回请求进行Activity跳转
	 * @param activity 当前Activity
	 * @param targetActivity 目标Activity
	 * @param requestCode Activity请求码
	 */
	public static void switchTo(Activity activity, Class<? extends Activity> targetActivity, int requestCode) {
		Intent mIntent = new Intent(activity, targetActivity);
		switchTo(activity, mIntent, requestCode);
	}
	
	public static void switchTo(Activity activity,Intent intent,int requestCode) {
		// Note we want to go through this call for compatibility with
        // applications that may have overridden the method.
		activity.startActivityForResult(intent, requestCode);
	}
	
	/**
	 * 带参数和返回请求进行Activity跳转
	 * @param activity 当前Activity
	 * @param targetActivity 目标Activity
	 * @param params 参数
	 * @param requestCode Activity请求码
	 */
	public static void switchTo(Activity activity, Class<? extends Activity> targetActivity, Params params, int requestCode) {
		Intent mIntent = new Intent(activity, targetActivity);
		if( null != params ){
			for(Params.NameValue item : params.nameValueArray) {
				HughieIntentUtils.setValueToIntent(mIntent, item.name, item.value);
			}
		}
		switchTo(activity, mIntent, requestCode);
	}
	
	/**
	 * 带参数进行Activity跳转
	 * @param @param activity
	 * @param @param intent
	 * @param @param params 
	 * @return void 
	 * @throws
	 */
	public static void switchTo(Activity activity,Intent intent,Params params){
		if( null != params ){
			for(Params.NameValue item : params.nameValueArray){
				HughieIntentUtils.setValueToIntent(intent, item.name, item.value);
			}
		}
		switchTo(activity, intent);
	}
	
	/**
	 * 带参数进行Activity跳转
	 * @param activity 当前Activity
	 * @param target 目标Activity
	 * @param params 参数
	 */
	public static void switchTo(Activity activity,Class<? extends Activity> target,Params params){
		Intent mIntent = new Intent(activity, target);
		if( null != params ){
			for(Params.NameValue item : params.nameValueArray){
				HughieIntentUtils.setValueToIntent(mIntent, item.name, item.value);
			}
		}
		switchTo(activity, mIntent);
	}
	
	/**
	 * 根据给定的Intent进行Activity跳转
	 * @param activity 当前Activity
	 * @param intent 目标Activity的Intent
	 */
	public static void switchTo(Activity activity,Intent intent){
		switchTo(activity, intent, -1);
	}
	
	/**
	 * 跳转到某个Activity
	 * @param activity 当前Activity
	 * @param targetActivity 目标Activity
	 */
	public static void switchTo(Activity activity,Class<? extends Activity> targetActivity){
		switchTo(activity, new Intent(activity, targetActivity));
	}
}

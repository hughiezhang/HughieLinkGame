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
 * @description Activity��������
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
	 * @description ��ȡ��Ļ��С����λpx
	 * @param activity Activity
	 * @return ��Ļ��С����
	 */
	public static Point getScreenSize(Activity activity){
		Display mDisplay = activity.getWindowManager().getDefaultDisplay();
		Point mSize = new Point();
		mSize.set(mDisplay.getWidth(), mDisplay.getHeight());
		
		return mSize;
	}
	
	/**
	 * @description ��������
	 * @param Context context
	 * @param String fontTypeString ��������
	 * @return ����
	 */
	public static Typeface getFontType(Context context, String fontTypeString){
		return Typeface.createFromAsset(context.getAssets(), fontTypeString);
	}
	
	/**
	 * �������������Activity��ת
	 * @param activity ��ǰActivity
	 * @param targetActivity Ŀ��Activity
	 * @param requestCode Activity������
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
	 * �������ͷ����������Activity��ת
	 * @param activity ��ǰActivity
	 * @param targetActivity Ŀ��Activity
	 * @param params ����
	 * @param requestCode Activity������
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
	 * ����������Activity��ת
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
	 * ����������Activity��ת
	 * @param activity ��ǰActivity
	 * @param target Ŀ��Activity
	 * @param params ����
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
	 * ���ݸ�����Intent����Activity��ת
	 * @param activity ��ǰActivity
	 * @param intent Ŀ��Activity��Intent
	 */
	public static void switchTo(Activity activity,Intent intent){
		switchTo(activity, intent, -1);
	}
	
	/**
	 * ��ת��ĳ��Activity
	 * @param activity ��ǰActivity
	 * @param targetActivity Ŀ��Activity
	 */
	public static void switchTo(Activity activity,Class<? extends Activity> targetActivity){
		switchTo(activity, new Intent(activity, targetActivity));
	}
}

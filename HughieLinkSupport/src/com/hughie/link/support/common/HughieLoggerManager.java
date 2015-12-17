package com.hughie.link.support.common;

import java.util.Formatter;

import android.util.Log;

/**
 * @description 全局Logger类，用于合理控制logcat，便于debug和relaese，只查看通过HughieLogger打出来的log的指令是，adb logcat -s HughieLogger
 *                      如果用的不是单例的HughieLogger，只查看通过HughieLogger打出来的日志需要结合实例HughieLogger的TAG，adb logcat -s TAG
 * @author: hughiezhang
 * @since: 2015-08-26 17:00
 */
public class HughieLoggerManager {
	private static HughieLoggerManager instance = null;
	private static String tag = "HughieLogger";
	
	public HughieLoggerManager(String tag) {
		if(tag == null){
			throw new NullPointerException("HughieLogger non-null");
		}
		
		this.tag = tag;
	}
	
	public static HughieLoggerManager getInstance(){
		if(instance == null){
			instance = new HughieLoggerManager(tag);
		}
		
		return instance;
	}
	
	private static final ThreadLocal<ReusableFormatter> thread_local_formatter = new ThreadLocal<ReusableFormatter>(){
		@Override
		protected ReusableFormatter initialValue() {
			return new ReusableFormatter();
		}
	};
	
	public static String format(String msg, Object... args){
		ReusableFormatter formatter = thread_local_formatter.get();
		
		return formatter.format(msg, args);
	}
	
	/**
	 * 打印cache到的错误信息
	 * @param e
	 */
	public static final void printStackTrace(Exception e){
		if(e!=null){
			e.printStackTrace();
		}
	}
	
	/**
	 * 打印字符串
	 * @param str
	 */
	public static final void println(String s){
		if(s != null){
			int i = 0;
			while(s.length() > 0){
				i = s.length() > 2900 ? 2900 : s.length();
				System.out.println(s.substring(0, i));
				s = s.substring(i);
			}
		}
	}
	
	public static final void pintlnError(String s){
		if(s!=null){
			System.err.println(s);
		}
	}
	
	public static final void logD(String s){
		if(s != null){
			if(s.length() < 2900){
				Log.d(tag, s);
			} else {
				Log.d(tag, s.substring(0, 2900));
			}
		}
	}
	
	public static final void logD(String s, Object... args){
		if(s != null){
			if(s.length() < 2900){
				Log.d(tag, format(s, args));
			} else {
				Log.d(tag, format(s.substring(0, 2900), args));
			}
		}
	}
	
	public static final void logD(Throwable err, String s, Object... args){
		if(s != null){
			if(s.length() < 2900){
				Log.d(tag, format(s, args), err);
			} else {
				Log.d(tag, format(s.substring(0, 2900), args), err);
			}
		}
	}
	
	public static final void logE(String s){
		if(s != null){
			if(s.length() < 2900){
				Log.e(tag, s);
			} else {
				Log.e(tag, s.substring(0, 2900));
			}
		}
	}
	
	public static final void logE(String s, Object... args){
		if(s != null){
			if(s.length() < 2900){
				Log.e(tag, format(s, args));
			} else {
				Log.e(tag, format(s.substring(0, 2900), args));
			}
		}
	}
	
	public static final void logE(Throwable err, String s, Object... args){
		if(s != null){
			if(s.length() < 2900){
				Log.e(tag, format(s, args), err);
			} else {
				Log.e(tag, format(s.substring(0, 2900), args), err);
			}
		}
	}
	
	/**
	 * A little trick to reuse a formatter in the same thread
	 */
	private static class ReusableFormatter{
		private Formatter formatter;
		private StringBuilder builder;
		
		public ReusableFormatter() {
			builder = new StringBuilder();
			formatter = new Formatter(builder);
		}
		
		public String format(String s, Object... args){
			formatter.format(s, args);
			String str = builder.toString();
			builder.setLength(0);
			
			return str;
		}
	}
}

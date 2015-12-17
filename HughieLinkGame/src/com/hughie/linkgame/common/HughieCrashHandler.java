package com.hughie.linkgame.common;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import com.hughie.link.support.common.HughieLoggerManager;
import com.hughie.linkgame.R;
import com.umeng.analytics.MobclickAgent;

import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.os.Process;
import android.view.Gravity;
import android.widget.Toast;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 * @ClassName: HughieCrashHandler
 * @author hughiezhang
 * @since 2015-09-01 17:15
 */
public class HughieCrashHandler implements UncaughtExceptionHandler {
	// 系统默认的UncaughtException处理类
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	// HughieCrashHandler实例
	private static HughieCrashHandler mInstance = new HughieCrashHandler();
	// 程序的Context对象
	private Context mContext;
	
	/** 保证只有一个HughieCrashHandler实例 */
	public HughieCrashHandler() {

	}
	
	/** 获取HughieCrashHandler实例 ,单例模式 */
	public static HughieCrashHandler getInstance(){
		return mInstance;
	}
	
	/**
	 * @description 初始化
	 * @param context
	 */
	public void init(Context context) {
		this.mContext = context;
		// 获取系统默认的UncaughtException处理器
		this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	
	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		try {
			// 保存日志文件
			saveCrashInfo2File(ex);
			if(ex != null && mDefaultHandler != null){
				// 使用Toast来显示异常信息
				new Thread(){
					@Override
					public void run() {
						Looper.prepare();
						Toast t = Toast.makeText(mContext, "很抱歉,程序出现异常,3秒后退出!", Toast.LENGTH_LONG);
						t.setGravity(Gravity.CENTER, 0, 0);
						t.show();
						Looper.loop();
					}
				}.start();
				//打印日志
				ex.printStackTrace();
				try {
					Thread.sleep(3000);
				} catch(InterruptedException e){
					HughieLoggerManager.println("程序休眠也会出错?");
					HughieLoggerManager.printStackTrace(e);
				}
				
				// 退出程序
				Process.killProcess(Process.myPid());
				System.exit(1);
			} else {
				// 如果没有处理则让系统默认的异常处理器来处理
				mDefaultHandler.uncaughtException(thread, ex);
			}
		} catch (Exception e){
			HughieLoggerManager.println("程序崩溃处理时出错");
			HughieLoggerManager.printStackTrace(e);
		}
	}
	
	/**
	 * @description 保存错误信息到文件中
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	private void saveCrashInfo2File(Throwable ex){
		StringBuffer sb = new StringBuffer();
		sb.append("当前程序版本名：" + mContext.getString(R.string.app_name) + HughieAppRunInfoManager.getPackageVersionName(mContext))
			.append("\n手机型号：" + Build.MODEL)
			.append("\nSDK版本号：" + Build.VERSION.SDK)
			.append("\n系统版本号：" + Build.VERSION.RELEASE + "\n");
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while(cause != null){
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		try{
			FileOutputStream fos = new FileOutputStream(HughieFileManager.getLogFile());
			fos.write(sb.toString().getBytes());
			fos.close();
			//将错误日志上传到友盟服务器
			MobclickAgent.reportError(mContext, sb.toString());
		} catch(Exception e){
			HughieLoggerManager.println("保存日志文件出错");
			HughieLoggerManager.printStackTrace(e);
		}
	}
}

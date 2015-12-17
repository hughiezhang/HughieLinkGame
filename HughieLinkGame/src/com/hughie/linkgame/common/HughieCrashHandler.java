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
 * UncaughtException������,��������Uncaught�쳣��ʱ��,�и������ӹܳ���,����¼���ʹ��󱨸�.
 * @ClassName: HughieCrashHandler
 * @author hughiezhang
 * @since 2015-09-01 17:15
 */
public class HughieCrashHandler implements UncaughtExceptionHandler {
	// ϵͳĬ�ϵ�UncaughtException������
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	// HughieCrashHandlerʵ��
	private static HughieCrashHandler mInstance = new HughieCrashHandler();
	// �����Context����
	private Context mContext;
	
	/** ��ֻ֤��һ��HughieCrashHandlerʵ�� */
	public HughieCrashHandler() {

	}
	
	/** ��ȡHughieCrashHandlerʵ�� ,����ģʽ */
	public static HughieCrashHandler getInstance(){
		return mInstance;
	}
	
	/**
	 * @description ��ʼ��
	 * @param context
	 */
	public void init(Context context) {
		this.mContext = context;
		// ��ȡϵͳĬ�ϵ�UncaughtException������
		this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// ���ø�CrashHandlerΪ�����Ĭ�ϴ�����
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	
	/**
	 * ��UncaughtException����ʱ��ת��ú���������
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		try {
			// ������־�ļ�
			saveCrashInfo2File(ex);
			if(ex != null && mDefaultHandler != null){
				// ʹ��Toast����ʾ�쳣��Ϣ
				new Thread(){
					@Override
					public void run() {
						Looper.prepare();
						Toast t = Toast.makeText(mContext, "�ܱ�Ǹ,��������쳣,3����˳�!", Toast.LENGTH_LONG);
						t.setGravity(Gravity.CENTER, 0, 0);
						t.show();
						Looper.loop();
					}
				}.start();
				//��ӡ��־
				ex.printStackTrace();
				try {
					Thread.sleep(3000);
				} catch(InterruptedException e){
					HughieLoggerManager.println("��������Ҳ�����?");
					HughieLoggerManager.printStackTrace(e);
				}
				
				// �˳�����
				Process.killProcess(Process.myPid());
				System.exit(1);
			} else {
				// ���û�д�������ϵͳĬ�ϵ��쳣������������
				mDefaultHandler.uncaughtException(thread, ex);
			}
		} catch (Exception e){
			HughieLoggerManager.println("�����������ʱ����");
			HughieLoggerManager.printStackTrace(e);
		}
	}
	
	/**
	 * @description ���������Ϣ���ļ���
	 * @param ex
	 * @return �����ļ�����,���ڽ��ļ����͵�������
	 */
	private void saveCrashInfo2File(Throwable ex){
		StringBuffer sb = new StringBuffer();
		sb.append("��ǰ����汾����" + mContext.getString(R.string.app_name) + HughieAppRunInfoManager.getPackageVersionName(mContext))
			.append("\n�ֻ��ͺţ�" + Build.MODEL)
			.append("\nSDK�汾�ţ�" + Build.VERSION.SDK)
			.append("\nϵͳ�汾�ţ�" + Build.VERSION.RELEASE + "\n");
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
			//��������־�ϴ������˷�����
			MobclickAgent.reportError(mContext, sb.toString());
		} catch(Exception e){
			HughieLoggerManager.println("������־�ļ�����");
			HughieLoggerManager.printStackTrace(e);
		}
	}
}

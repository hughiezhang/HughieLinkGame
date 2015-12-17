package com.hughie.linkgame.common;

import android.app.Activity;
import android.content.Context;

import com.hughie.link.support.common.HughieApplication;
import com.hughie.link.support.util.HughieScreenManager;
import com.hughie.linkgame.R;
import com.hughie.linkgame.utils.HughieSoundUtils;
import com.umeng.analytics.MobclickAgent;


public class HughieGameApplication extends HughieApplication {
	private static HughieGameApplication mHughieGameApplication;
	
	public float density;										//��ȡ�ܶ�ֵ
	public int displayWidth;									//��ȡ��Ļ�ֱ��ʿ��
	public int displayHeight;								//��ȡ��Ļ�ֱ��ʸ߶�
	public int mIconWidth;									//��ȡ��������Ԫ��Ŀ��
	public int mIconHeight;									//��ȡ��������Ԫ��ĸ߶�
	public int mPaddingLeft;								//��ȡ��������Ԫ�����߾�
	public int mPaddingTop;								//��ȡ��������Ԫ����ϱ߾�
	
	public HughieSoundUtils mSoundUtils;
	
	// ����maxColumn,����maxRow �����˱߽磨��ʵ�ʵ�Ԫ������Ϊ8*10��
	public static int mMaxColumn = 10;				//��ʾ��������
	public static int mMaxRow = 12;					//��ʾ��������

	@Override
	public void onCreate() {
		super.onCreate();
		mHughieGameApplication = this; 
		
		HughieCrashHandler mHughieCrashHandler = HughieCrashHandler.getInstance();
		mHughieCrashHandler.init(getApplicationContext());
		
		//���л������ܶ�ֵ
		this.density = HughieScreenManager.getDensity();
		//���л����Ŀ��
		this.displayWidth = HughieScreenManager.getScreenWidth();
		//���л����ĸ߶�
		this.displayHeight = HughieScreenManager.getScreenHeight();
		
		//��ȡ��������Ԫ��ĸ߶ȺͿ��
		this.mIconWidth =(int) ((displayWidth - 24) / mMaxColumn);
		this.mIconHeight = mIconWidth;
		
		//��ȡ��������Ԫ�����߾�
		this.mPaddingLeft = (int) ((HughieScreenManager.getScreenWidth() - 10.0F * this.mIconWidth) / 2);
		//��ȡ��������Ԫ����ϱ߾�
		this.mPaddingTop = (int) ((HughieScreenManager.getScreenHeight() - 50.0F * HughieScreenManager.getDensity()
				- 12.0F * mIconWidth) / 2);
		
		this.mSoundUtils = new HughieSoundUtils(getApplicationContext());
	}
	
	public synchronized static HughieGameApplication getInstance(){
		return mHughieGameApplication;
	}
	
	public static final void setHughieActivityTheme(Activity activity){
		activity.setTheme(R.style.HughieAppTheme_Blue);
	}
	
	public static final void exitHughieApp(Context context) {
		// ����ͳ�� �˳�ʱ����
		HughieApplicationManager.getInstance().exitApp(context);
		MobclickAgent.onKillProcess(context);
	}
}

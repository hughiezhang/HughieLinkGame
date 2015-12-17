package com.hughie.linkgame.base;

import com.hughie.link.support.common.HughieLoggerManager;
import com.hughie.linkgame.R;
import com.hughie.linkgame.common.HughieApplicationManager;
import com.hughie.linkgame.common.HughieGameApplication;
import com.hughie.linkgame.common.HughieSPManager;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

/**
 * ��Activity, ����Actiity�̳д�Activity
 * @ClassName: HughieBaseActivity
 * @author hughiezhang
 * @since 2015-08-26 13:32
 */
public class HughieBaseActivity extends FragmentActivity {
	public static HughieGameApplication mApplication;
	public static SharedPreferences sp;
	protected Activity mActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HughieGameApplication.setHughieActivityTheme(this);
		mActivity = this;
		HughieApplicationManager.getInstance().addActivity(mActivity);
		HughieLoggerManager.println("Activity��HughieBaseActivity��onCreate��" + savedInstanceState);
		// TODO ok ��ȡ��������֪����û���� ��ҽ��û�и�
		while (sp == null) {
			sp = this.getSharedPreferences(HughieSPManager.SPDefault, Context.MODE_PRIVATE);
			HughieLoggerManager.println("Ϊnull�Ż�ȡ����ȡ��ѡ���ļ����" + sp.getAll().size());
		}
		
		if (mApplication == null) {
			mApplication = (HughieGameApplication) getApplication();
		}
		
		if(savedInstanceState != null){
			HughieLoggerManager.println("����,���ֻ������.");
		}
	}
	
	/**
	 * @description ����title���ֱ���
	 * @param int resID
	 */
	public void setTitleTvHead(int resID){
		TextView mTvHead = (TextView)findViewById(R.id.title_head_tv);
		if(mTvHead != null)
			mTvHead.setText(resID);
	}
	
	/**
	 * @description ����title���ֱ���
	 * @param String name
	 */
	public void setTitleTvHead(String name){
		TextView mTvHead = (TextView)findViewById(R.id.title_head_tv);
		if(mTvHead != null)
			mTvHead.setText(name);
	}
	
	/**
	 * @description ����title�������back��ť�Ƿ���ʾ
	 * @param int visibility
	 */
	public void setLeftVisibility(int visibility){
		this.findViewById(R.id.title_return_layout).setVisibility(visibility);
	}
	
	/**
	 * @description ����title�����Ҳఴť�Ƿ���ʾ
	 * @param int visibility
	 */
	public void setBtnRightVisibility(int visibility) {
		this.findViewById(R.id.title_right_btn).setVisibility(visibility);
	}
	
	/**
	 * @description ����title�����Ҳఴť��������ʾ
	 * @param int resId
	 */
	public void setBtnRight(int resID) {
		Button mBtnRight = (Button)this.findViewById(R.id.title_right_btn);
		mBtnRight.setVisibility(View.VISIBLE);
		mBtnRight.setText(resID);
	}
	
	/**
	 * @description ����title�����Ҳఴť��������ʾ
	 * @param String text
	 */
	public void setBtnRight(String text) {
		Button mBtnRight = (Button)this.findViewById(R.id.title_right_btn);
		mBtnRight.setVisibility(View.VISIBLE);
		if(Build.VERSION.SDK_INT >= 16)
			mBtnRight.setBackground(null);
		mBtnRight.setText(text);
	}
	
	/**
	 * @description ����title�������back��ť��λ��
	 * @param int left�����
	 * @param int top������
	 * @param int right���Ҳ�
	 * @param int bottom: �ײ�
	 */
	public void setBtnLeftPadding(int left, int top, int right, int bottom) {
		Button mBtnLeft = (Button)this.findViewById(R.id.title_left_btn);
		LayoutParams params = (LayoutParams)mBtnLeft.getLayoutParams();
		params.setMargins(left, top, right, bottom);
		mBtnLeft.setLayoutParams(params);
	}
	
	/**
	 * @description ����title�����Ҳఴť��λ��
	 * @param int left�����
	 * @param int top������
	 * @param int right���Ҳ�
	 * @param int bottom: �ײ�
	 */
	public void setBtnRightPadding(int left, int top, int right, int bottom){
		Button mBtnRight = (Button)this.findViewById(R.id.title_right_btn);
		LayoutParams params = (LayoutParams)mBtnRight.getLayoutParams();
		params.setMargins(left, top, right, bottom);
		mBtnRight.setLayoutParams(params);
	}
	
	/**
	 * @description ����title�����Ҳఴť�ı�����ʾ
	 * @param int resId
	 */
	public void setBtnRightBackground(int resID) {
		Button mBtnRight = (Button)this.findViewById(R.id.title_right_btn);
		if(mBtnRight != null){
			mBtnRight.setVisibility(View.VISIBLE);
			mBtnRight.setBackgroundResource(resID);
		}
	}
	
	/**
	 * @description ����title�����Ҳఴť�ı�����ʾ
	 * @param int resId
	 */
	public void setBtnRightDrawable(int resID){
		Button mBtnRight = (Button)this.findViewById(R.id.title_right_btn);
		mBtnRight.setBackgroundColor(getResources().getColor(R.color.transparent));
		mBtnRight.setVisibility(View.VISIBLE);
		Drawable drawable = getResources().getDrawable(resID);
		/// ��һ������Ҫ��,���򲻻���ʾ.
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		mBtnRight.setCompoundDrawables(null, null, drawable, null);
	}
	
	/**
	 * @description ����title�������back��ť��click����
	 * @param View v
	 */
	public void onClickHeadLeft(View v) {
		finish();
	}
	
	public void onClick(View v) {
		
	}
	
	/**
	 * @description ����title�����Ҳఴť��click����
	 * @param View v
	 */
	public void onClickHeadRight(View v) {
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		HughieLoggerManager.println("HughieBaseActivity���汣��,�Ա���ʱ֮��.");
	}
	
	@Override
	protected void onDestroy() {
		HughieApplicationManager.getInstance().finishActivity(this);
		super.onDestroy();
		System.gc();
	}
	
	/* 
	 * 1, ȷ�������е�Activity�ж����� MobclickAgent.onResume() ��MobclickAgent.onPause()��������������
	 *     �ý���������Ӧ�ó�������̣߳�Ҳ����Ӱ��Ӧ�ó�������ܡ�
	 * 2, ע�� �������Activity֮���м̳л��߿��ƹ�ϵ�벻Ҫͬʱ�ڸ�����Activity���ظ����onPause��
	 *     onResume���������������ظ�ͳ�ƣ��������������쳣���ߡ�(eg.ʹ��TabHost��TabActivity��
	 *     ActivityGroupʱ)��
	 * 3, ��Ӧ���ں�̨���г���30�루Ĭ�ϣ��ٻص�ǰ�ˣ�������Ϊ������������session(����)�������û��ص�
	 *     home��������������򣬾���һ��ʱ����ٷ���֮ǰ��Ӧ�á���ͨ���ӿڣ�
	 *     MobclickAgent.setSessionContinueMillis(long interval) ���Զ�����������������λΪ���룩��
	 * 4, ��������ߵ���Process.kill����System.exit֮��ķ���ɱ�����̣�������ڴ�֮ǰ����
	 *     MobclickAgent.onKillProcess(Context context)��������������ͳ�����ݡ�
	 * */
	
	@Override
	protected void onResume() {
		super.onResume();
		umengOnResume();
	}
	
	protected void umengOnResume() {
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		umengOnPause();
	}
	
	protected void umengOnPause() {
		MobclickAgent.onPause(this);
	}
}

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
 * 主Activity, 其他Actiity继承此Activity
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
		HughieLoggerManager.println("Activity：HughieBaseActivity：onCreate：" + savedInstanceState);
		// TODO ok 获取不到？不知道有没有用 名医汇没有改
		while (sp == null) {
			sp = this.getSharedPreferences(HughieSPManager.SPDefault, Context.MODE_PRIVATE);
			HughieLoggerManager.println("为null才获取，获取首选项文件结果" + sp.getAll().size());
		}
		
		if (mApplication == null) {
			mApplication = (HughieGameApplication) getApplication();
		}
		
		if(savedInstanceState != null){
			HughieLoggerManager.println("哈哈,我又活过来了.");
		}
	}
	
	/**
	 * @description 设置title布局标题
	 * @param int resID
	 */
	public void setTitleTvHead(int resID){
		TextView mTvHead = (TextView)findViewById(R.id.title_head_tv);
		if(mTvHead != null)
			mTvHead.setText(resID);
	}
	
	/**
	 * @description 设置title布局标题
	 * @param String name
	 */
	public void setTitleTvHead(String name){
		TextView mTvHead = (TextView)findViewById(R.id.title_head_tv);
		if(mTvHead != null)
			mTvHead.setText(name);
	}
	
	/**
	 * @description 设置title布局左侧back按钮是否显示
	 * @param int visibility
	 */
	public void setLeftVisibility(int visibility){
		this.findViewById(R.id.title_return_layout).setVisibility(visibility);
	}
	
	/**
	 * @description 设置title布局右侧按钮是否显示
	 * @param int visibility
	 */
	public void setBtnRightVisibility(int visibility) {
		this.findViewById(R.id.title_right_btn).setVisibility(visibility);
	}
	
	/**
	 * @description 设置title布局右侧按钮的文字显示
	 * @param int resId
	 */
	public void setBtnRight(int resID) {
		Button mBtnRight = (Button)this.findViewById(R.id.title_right_btn);
		mBtnRight.setVisibility(View.VISIBLE);
		mBtnRight.setText(resID);
	}
	
	/**
	 * @description 设置title布局右侧按钮的文字显示
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
	 * @description 设置title布局左侧back按钮的位置
	 * @param int left：左侧
	 * @param int top：顶侧
	 * @param int right：右侧
	 * @param int bottom: 底侧
	 */
	public void setBtnLeftPadding(int left, int top, int right, int bottom) {
		Button mBtnLeft = (Button)this.findViewById(R.id.title_left_btn);
		LayoutParams params = (LayoutParams)mBtnLeft.getLayoutParams();
		params.setMargins(left, top, right, bottom);
		mBtnLeft.setLayoutParams(params);
	}
	
	/**
	 * @description 设置title布局右侧按钮的位置
	 * @param int left：左侧
	 * @param int top：顶侧
	 * @param int right：右侧
	 * @param int bottom: 底侧
	 */
	public void setBtnRightPadding(int left, int top, int right, int bottom){
		Button mBtnRight = (Button)this.findViewById(R.id.title_right_btn);
		LayoutParams params = (LayoutParams)mBtnRight.getLayoutParams();
		params.setMargins(left, top, right, bottom);
		mBtnRight.setLayoutParams(params);
	}
	
	/**
	 * @description 设置title布局右侧按钮的背景显示
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
	 * @description 设置title布局右侧按钮的背景显示
	 * @param int resId
	 */
	public void setBtnRightDrawable(int resID){
		Button mBtnRight = (Button)this.findViewById(R.id.title_right_btn);
		mBtnRight.setBackgroundColor(getResources().getColor(R.color.transparent));
		mBtnRight.setVisibility(View.VISIBLE);
		Drawable drawable = getResources().getDrawable(resID);
		/// 这一步必须要做,否则不会显示.
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		mBtnRight.setCompoundDrawables(null, null, drawable, null);
	}
	
	/**
	 * @description 设置title布局左侧back按钮的click监听
	 * @param View v
	 */
	public void onClickHeadLeft(View v) {
		finish();
	}
	
	public void onClick(View v) {
		
	}
	
	/**
	 * @description 设置title布局右侧按钮的click监听
	 * @param View v
	 */
	public void onClickHeadRight(View v) {
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		HughieLoggerManager.println("HughieBaseActivity里面保存,以备不时之需.");
	}
	
	@Override
	protected void onDestroy() {
		HughieApplicationManager.getInstance().finishActivity(this);
		super.onDestroy();
		System.gc();
	}
	
	/* 
	 * 1, 确保在所有的Activity中都调用 MobclickAgent.onResume() 和MobclickAgent.onPause()方法，这两个调
	 *     用将不会阻塞应用程序的主线程，也不会影响应用程序的性能。
	 * 2, 注意 如果您的Activity之间有继承或者控制关系请不要同时在父和子Activity中重复添加onPause和
	 *     onResume方法，否则会造成重复统计，导致启动次数异常增高。(eg.使用TabHost、TabActivity、
	 *     ActivityGroup时)。
	 * 3, 当应用在后台运行超过30秒（默认）再回到前端，将被认为是两个独立的session(启动)，例如用户回到
	 *     home，或进入其他程序，经过一段时间后再返回之前的应用。可通过接口：
	 *     MobclickAgent.setSessionContinueMillis(long interval) 来自定义这个间隔（参数单位为毫秒）。
	 * 4, 如果开发者调用Process.kill或者System.exit之类的方法杀死进程，请务必在此之前调用
	 *     MobclickAgent.onKillProcess(Context context)方法，用来保存统计数据。
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

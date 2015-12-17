package com.hughie.linkgame.base;

import com.hughie.linkgame.common.HughieGameApplication;
import com.hughie.linkgame.common.HughieToastManager;
import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.view.KeyEvent;

/**
 * 主界面Activity, 其他主界面tab Actiity继承此Activity
 * @ClassName: HughieMainBaseActivity
 * @author hughiezhang
 * @since 2015-09-08 11:46
 */
public class HughieMainBaseActivity extends HughieBaseActivity {
	private long mExitTime = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
			if((System.currentTimeMillis() - mExitTime) > 2000){
				HughieToastManager.showInfo(mActivity, "再按一次退出连连看了");
				mExitTime = System.currentTimeMillis();
			} else {
				HughieGameApplication.exitHughieApp(HughieMainBaseActivity.this);
			}
			
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void umengOnResume() {
		MobclickAgent.onPageStart(this.getClass().getName());
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void umengOnPause() {
		MobclickAgent.onPageEnd(this.getClass().getName());
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}

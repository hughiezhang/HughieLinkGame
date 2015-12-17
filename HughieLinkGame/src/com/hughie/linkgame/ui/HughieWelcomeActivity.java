package com.hughie.linkgame.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hughie.link.support.common.HughieLoggerManager;
import com.hughie.linkgame.HughieMainActivity;
import com.hughie.linkgame.R;
import com.hughie.linkgame.base.HughieBaseActivity;
import com.hughie.linkgame.common.HughieCallBackMe;
import com.hughie.linkgame.common.HughieSPManager;
import com.hughie.linkgame.common.HughieToastManager;
import com.hughie.linkgame.dao.HughieVersionDao;
import com.hughie.linkgame.utils.HughieTVFitUtils;
import com.hughie.linkgame.widget.HughieCheckVersion;
import com.hughie.linkgame.widget.HughieGuideViewPager;

/**
 * @description 欢迎界面Activity
 * @ClassName: HughieWelcomeActivity
 * @author hughiezhang
 * @since 2015-08-31 09:52
 */
public class HughieWelcomeActivity extends HughieBaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_hughie_welcome);
		HughieTVFitUtils.setTVImageScale(mActivity, (ImageView)findViewById(R.id.welcome_icon_imgv));
		HughieLoggerManager.println("Activity：HughieWelcomeActivity：onCreate：" + savedInstanceState);
//		saveTabGameSettings();
		
		int mVersionCode = HughieVersionDao.getversionCode(this);
		if(mVersionCode > sp.getInt(HughieSPManager.SP_versionCode, -1)) {
			HughieLoggerManager.println("开始展示图片");
			showWelcome();
			
			sp.edit().putLong(HughieSPManager.SP_LastUpdateTime, 0).commit();
			sp.edit().putInt(HughieSPManager.SP_versionCode, mVersionCode).commit();
		} else {
			checkVersion(0);
		}
	}
	
	/**
	 * 新版本 介绍页面
	 */
	private void showWelcome(){
		final View mGuideLayout = findViewById(R.id.welcome_guide_layout);
		mGuideLayout.setVisibility(View.VISIBLE);
		final View mProgressBar = findViewById(R.id.welcome_display_progressbar);
		mProgressBar.setVisibility(View.GONE);
		final View mWelcomeBackGround = findViewById(R.id.welcome_icon_imgv);
		mWelcomeBackGround.setVisibility(View.GONE);
		
		HughieGuideViewPager mHughieGuideViewPager = new HughieGuideViewPager(this, (ViewPager)findViewById(R.id.welcome_guide_viewpager), 
				(LinearLayout)findViewById(R.id.welcome_guider_indicator));
		mHughieGuideViewPager.setmImgvIds(new int[]{R.drawable.imgv_guide_feature_0, R.drawable.imgv_guide_feature_1, R.drawable.imgv_guide_feature_2,
				R.drawable.imgv_guide_feature_3, R.drawable.imgv_guide_feature_4, R.drawable.imgv_welcome_splash});
		mHughieGuideViewPager.setmBgIds(new int[]{R.drawable.imgv_guide_feature_bg0, R.drawable.imgv_guide_feature_bg1, R.drawable.imgv_guide_feature_bg2,
				R.drawable.imgv_guide_feature_bg3, R.drawable.imgv_guide_feature_bg4});
		mHughieGuideViewPager.setmTvIds(new int[]{R.string.str_guide_pager_feature_00_txt, R.string.str_guide_pager_feature_01_txt, 
				R.string.str_guide_pager_feature_02_txt, R.string.str_guide_pager_feature_03_txt, R.string.str_guide_pager_feature_04_txt});
		HughieCallBackMe mWelcomeCallBack = new  HughieCallBackMe(){
			@Override
			public void callback() {
				mWelcomeBackGround.setVisibility(View.VISIBLE);
				mGuideLayout.setVisibility(View.GONE);
				mProgressBar.setVisibility(View.VISIBLE);
				checkVersion(1);
			}
		};
		mHughieGuideViewPager.setmHughieCallBackMe(mWelcomeCallBack);
		mHughieGuideViewPager.show();
	}
	
	private final void checkVersion(final int state){
		HughieCheckVersion mVersion = new HughieCheckVersion(this, new HughieCallBackMe() {
			@Override
			public void callback(String msg) {
				if(!TextUtils.isEmpty(msg))
					HughieToastManager.showInfo(HughieWelcomeActivity.this, msg);
				Intent mIntent;
				if(state == 0) {
					mIntent = new Intent(HughieWelcomeActivity.this, HughieMainActivity.class);
				} else {
					mIntent = new Intent(HughieWelcomeActivity.this, HughieGameIndexActivity.class);
				}
				startActivity(mIntent);
				finish();
			}
			
			@Override
			public void callback() {
				Intent mIntent;
				if(state == 0) {
					mIntent = new Intent(HughieWelcomeActivity.this, HughieMainActivity.class);
				} else {
					mIntent = new Intent(HughieWelcomeActivity.this, HughieGameIndexActivity.class);
				}
				startActivity(mIntent);
				finish();
			}
		});
		
		mVersion.updateVersion();
	}
	
	private void saveTabGameSettings() {
		boolean bState[] = {true, true, true, false, false};
		String code[] = {getString(R.string.str_game_grid_item_games_txt), getString(R.string.str_game_grid_item_rankings_txt), 
				getString(R.string.str_game_grid_item_shop_txt), getString(R.string.str_game_grid_item_kit_txt), getString(R.string.str_game_grid_item_about_txt)};
		int img[] = {1, 2, 3, 4, 5};
		
		int size = 0;
		int x_size = 0;
		for(int i = 0; i < 5; i++) {
			if(bState[i]){
				sp.edit().putString("code_" + size, code[i]).commit();
				sp.edit().putString("img_" + size, img[i] + "").commit();
				size++;
			} else {
				sp.edit().putString("x_code_" + x_size, code[i]).commit();
				sp.edit().putString("x_img_" + x_size, img[i] + "").commit();
				x_size++;
			}
			
			sp.edit().putInt("size", size).commit();
			sp.edit().putInt("x_size", x_size).commit();
		}
	}
}

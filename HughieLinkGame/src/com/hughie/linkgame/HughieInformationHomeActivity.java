package com.hughie.linkgame;

import android.os.Bundle;

import com.hughie.linkgame.base.HughieMainBaseActivity;

/**
 * 咨讯主界面
 * @ClassName: HughieInformationHomeActivity
 * @author hughiezhang
 * @since 2015-09-08 14:14
 */
public class HughieInformationHomeActivity extends HughieMainBaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_hughie_home_main_information);
		setTitleTvHead("咨讯");
	}
}

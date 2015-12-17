package com.hughie.linkgame;

import android.os.Bundle;

import com.hughie.linkgame.base.HughieMainBaseActivity;

/**
 * 圈子主界面
 * @ClassName: HughieCommunityHomeActivity
 * @author hughiezhang
 * @since 2015-09-08 14:29
 */
public class HughieCommunityHomeActivity extends HughieMainBaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_hughie_home_main_community);
		setTitleTvHead("圈子");
	}
}

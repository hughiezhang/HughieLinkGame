package com.hughie.linkgame;

import android.os.Bundle;
import android.view.View;

import com.hughie.linkgame.base.HughieMainBaseActivity;

/**
 * ��Ѷ������
 * @ClassName: HughieInformationHomeActivity
 * @author hughiezhang
 * @since 2015-09-08 14:14
 */
public class HughieInformationHomeActivity extends HughieMainBaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_hughie_home_main_information);
		
		initInformationHomeTitle();  					// ��ʼ����Ѷtitle
	}
	
	// ��ʼ����Ѷtitle
	private void initInformationHomeTitle() {
		setTitleTvHead(R.string.str_screen_tab_information_txt);
		setLeftVisibility(View.GONE);
	}
}

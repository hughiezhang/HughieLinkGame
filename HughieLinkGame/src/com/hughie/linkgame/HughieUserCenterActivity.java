package com.hughie.linkgame;

import android.os.Bundle;

import com.hughie.linkgame.base.HughieMainBaseActivity;

/**
 * �ҵ�������
 * @ClassName: HughieUserCenterActivity
 * @author hughiezhang
 * @since 2015-09-08 14:35
 */
public class HughieUserCenterActivity extends HughieMainBaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_hughie_user_center);
		setTitleTvHead("�ҵ�");
	}
}

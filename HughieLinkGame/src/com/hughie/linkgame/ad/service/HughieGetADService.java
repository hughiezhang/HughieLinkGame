package com.hughie.linkgame.ad.service;

import android.content.Context;

import com.hughie.linkgame.base.service.HughieBaseService;

public class HughieGetADService extends HughieBaseService<HughieGetADRequest, HughieGetADResponse> {

	public HughieGetADService(Context context) {
		super(context);
	}

	@Override
	public HughieGetADRequest newRequest() {
		return null;
	}

}

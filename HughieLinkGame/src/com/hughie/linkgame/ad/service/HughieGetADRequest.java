package com.hughie.linkgame.ad.service;

import com.hughie.linkgame.base.service.HughieBaseRequest;

/**
 * ��Ϸ�������
 * @ClassName: HughieGetADRequest
 * @author hughiezhang
 * @since 2015-09-11 14:19
 */
public class HughieGetADRequest extends HughieBaseRequest {

	@Override
	public String getUrl() {
		return getSERVIER_URL() + getPath();
	}

}

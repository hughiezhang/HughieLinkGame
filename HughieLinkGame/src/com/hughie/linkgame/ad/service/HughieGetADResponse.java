package com.hughie.linkgame.ad.service;

import java.util.List;

import com.hughie.linkgame.base.service.HughieBaseResponse;
import com.hughie.linkgame.entity.ADBean;

/**
 * ��Ϸ�����Ӧ
 * @ClassName: HughieGetADResponse
 * @author hughiezhang
 * @since 2015-09-16 17:02
 */
public class HughieGetADResponse extends HughieBaseResponse {
	private List<ADBean> data;
	
	public HughieGetADResponse() {
		super();
	}

	public List<ADBean> getData() {
		return data;
	}

	public void setData(List<ADBean> data) {
		this.data = data;
	}
}

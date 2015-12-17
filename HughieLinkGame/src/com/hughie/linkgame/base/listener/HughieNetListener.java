package com.hughie.linkgame.base.listener;

import com.hughie.linkgame.base.service.HughieBaseRequest;
import com.hughie.linkgame.base.service.HughieBaseResponse;

/**
 * ÍøÂçÇëÇó¼àÌý½Ó¿Ú
 * @ClassName: HughieNetListener
 * @author hughiezhang
 * @since 2015-09-11 14:51
 */
public interface HughieNetListener {
	public void onPrepare();
	public void onLoading();
	public void onComplete(String respondCode, HughieBaseRequest request, HughieBaseResponse response);
	public void onLoadSuccess(HughieBaseResponse response);
	public void onCancel();
	public void onFailed(Exception ex, HughieBaseResponse response);
}

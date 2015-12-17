package com.hughie.linkgame.base.service;

/**
 * ����Ӧ��������Ӧ�̳д���Ӧ
 * @ClassName: HughieBaseResponse
 * @author hughiezhang
 * @since 2015-09-11 14:25
 */
public class HughieBaseResponse {
	/**
	 * �������б�
	 * @author galis
	 * @date: 2014-3-7-����4:49:27
	 */
	
	public String result = "";
	
	public String message = "";
	
	public String errorCode = "";
	
	public HughieBaseResponse() {
		super();
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public String toString() {
		return "HughieBaseResponse [result=" + result + ", message=" + message
				+ ", errorCode=" + errorCode + "]";
	}
}

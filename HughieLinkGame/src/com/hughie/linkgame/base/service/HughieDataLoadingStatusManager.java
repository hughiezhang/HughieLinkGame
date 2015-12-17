package com.hughie.linkgame.base.service;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;

import com.hughie.link.support.common.HughieLoggerManager;
import com.hughie.linkgame.base.listener.HughieNetListener;
import com.hughie.linkgame.common.HughieToastManager;
import com.hughie.linkgame.utils.HughieGsonUtils;

/**
 * 数据加载状态管理类
 * @ClassName: HughieDataLoadingStatusManager
 * @author hughiezhang
 * @since 20150911 15:51
 */
public class HughieDataLoadingStatusManager implements HughieNetListener {
	private ProgressDialog mLoadingDialog;
	
	private Map<String,String> mResultMap;
	
	private String mTitle;
	
	public boolean isDebug;
	public boolean isCanShowDialog;//能否弹窗显示？
	public boolean isLoading;
	
	private String tag = "HughieDataLoadingStatusManager";
	
	private Context mContext;
	
	public static final String SUCCESS_RESULT = "000";		//返回成功
	public static final String FAILD_RESULT = "001";			//返回失败
	public static final String FAILD_PARAMS = "002";		//参数错误
	public static final String FAILD_USER_VITIFY = "003";//用户身份验证失败
    public static final String ABNORMAL_DATABASE = "004";//数据库异常
    public static final String VITIFY_CODE = "005";//验证码有误
    public static final String INVITE_MSG_FAILD = "006";//文件上传异常
    public static final String FAILD_INVITE_CODE = "007";//邀请码错误
    public static final String FAILD_MSG_SEND = "008";//短信邀请码发送失败
    public static final String FAILED_AUTHOR =  "010" ;//权限认证失败
    public static final String UNCORRECT_LOGIN_INFO = "011";//登录信息不正确
	
	
	public HughieDataLoadingStatusManager() {
		
	}
	
	/**
     * 
     * @param context
     * @param tag
     * @return
     */
	public static HughieDataLoadingStatusManager getInstance(Context context,String tag) {
		HughieDataLoadingStatusManager mStatusManager = new HughieDataLoadingStatusManager();
		mStatusManager.mContext = context;
		mStatusManager.initResultMap();
		mStatusManager.initLoadingDialog();
		mStatusManager.isDebug = Boolean.parseBoolean("true");
		mStatusManager.isCanShowDialog = false;
		mStatusManager.isLoading = false;
		mStatusManager.mTitle = "加载";
		mStatusManager.tag = tag;
		
		return mStatusManager;
	}
	
	/**
     * 初始化结果标志map
     */
	public void initResultMap() {
		mResultMap = new HashMap<String, String>();
		mResultMap.put(SUCCESS_RESULT, "返回成功");
		mResultMap.put(FAILD_RESULT, "返回失败");
		mResultMap.put(FAILD_PARAMS, "参数错误");
		mResultMap.put(FAILD_USER_VITIFY, "用户身份验证失败");
		mResultMap.put(ABNORMAL_DATABASE, "数据库异常");
		mResultMap.put(VITIFY_CODE, "验证码有误");
		mResultMap.put(INVITE_MSG_FAILD, "文件上传异常");
		mResultMap.put(FAILD_INVITE_CODE, "邀请码错误");
		mResultMap.put(FAILD_MSG_SEND, "短信邀请码发送失败");
		mResultMap.put(FAILED_AUTHOR, "权限认证失败");
		mResultMap.put(UNCORRECT_LOGIN_INFO, "登录信息不正确");
	}
	
	/**
     * 初始化加载框
     */
	public void initLoadingDialog() {
		mLoadingDialog = new ProgressDialog(mContext);
		mLoadingDialog.setCanceledOnTouchOutside(false);	//不能点击
		mLoadingDialog.setTitle("正在" + mTitle + "!!");
		mLoadingDialog.setCancelable(false);
		mLoadingDialog.setMessage("message test");
	}
	
	@Override
	public void onPrepare() {
		printLog("准备" + mTitle + "-----");
	}

	@Override
	public void onLoading() {
		if (isCanShowDialog && isLoading == false){
			mLoadingDialog.show();
			isLoading = true;
		}
		
		printLog(mTitle + "ing-----");
	}

	@Override
	public void onComplete(String respondCode, HughieBaseRequest request,
			HughieBaseResponse response) {
		if (isCanShowDialog) {
			showToast(mResultMap.get(response.getResult()));
		}
		
		printLog("result:" + HughieGsonUtils.getGsonInstance().toJson(response));
		printLog("-----" + mTitle + "完成");
	}

	@Override
	public void onLoadSuccess(HughieBaseResponse response) {

	}
	
	@Override
	public void onFailed(Exception ex, HughieBaseResponse response) {
		printLog(ex.toString());
		printLog("-----加载数据失败");
	}

	@Override
	public void onCancel() {
		onLoadSuccess(null);
		printLog("-----取消" + mTitle);
	}
	
	public void printLog(String msg) {
		if(isDebug){
			HughieLoggerManager.logD(tag + " message: " + msg);
		}
	}


	public void setTitle(String mTitle) {
		this.mTitle = mTitle;
	}
	
	/**
     * 设置是否能够弹窗显示
     * @param isCanShowDialog
     */
	public void setCanShowDialog(boolean isCanShowDialog) {
		this.isCanShowDialog = isCanShowDialog;
	}
	
	public void showToast(String msg) {
		HughieToastManager.showInfo(mContext, msg);
	}
}

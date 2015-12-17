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
 * ���ݼ���״̬������
 * @ClassName: HughieDataLoadingStatusManager
 * @author hughiezhang
 * @since 20150911 15:51
 */
public class HughieDataLoadingStatusManager implements HughieNetListener {
	private ProgressDialog mLoadingDialog;
	
	private Map<String,String> mResultMap;
	
	private String mTitle;
	
	public boolean isDebug;
	public boolean isCanShowDialog;//�ܷ񵯴���ʾ��
	public boolean isLoading;
	
	private String tag = "HughieDataLoadingStatusManager";
	
	private Context mContext;
	
	public static final String SUCCESS_RESULT = "000";		//���سɹ�
	public static final String FAILD_RESULT = "001";			//����ʧ��
	public static final String FAILD_PARAMS = "002";		//��������
	public static final String FAILD_USER_VITIFY = "003";//�û������֤ʧ��
    public static final String ABNORMAL_DATABASE = "004";//���ݿ��쳣
    public static final String VITIFY_CODE = "005";//��֤������
    public static final String INVITE_MSG_FAILD = "006";//�ļ��ϴ��쳣
    public static final String FAILD_INVITE_CODE = "007";//���������
    public static final String FAILD_MSG_SEND = "008";//���������뷢��ʧ��
    public static final String FAILED_AUTHOR =  "010" ;//Ȩ����֤ʧ��
    public static final String UNCORRECT_LOGIN_INFO = "011";//��¼��Ϣ����ȷ
	
	
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
		mStatusManager.mTitle = "����";
		mStatusManager.tag = tag;
		
		return mStatusManager;
	}
	
	/**
     * ��ʼ�������־map
     */
	public void initResultMap() {
		mResultMap = new HashMap<String, String>();
		mResultMap.put(SUCCESS_RESULT, "���سɹ�");
		mResultMap.put(FAILD_RESULT, "����ʧ��");
		mResultMap.put(FAILD_PARAMS, "��������");
		mResultMap.put(FAILD_USER_VITIFY, "�û������֤ʧ��");
		mResultMap.put(ABNORMAL_DATABASE, "���ݿ��쳣");
		mResultMap.put(VITIFY_CODE, "��֤������");
		mResultMap.put(INVITE_MSG_FAILD, "�ļ��ϴ��쳣");
		mResultMap.put(FAILD_INVITE_CODE, "���������");
		mResultMap.put(FAILD_MSG_SEND, "���������뷢��ʧ��");
		mResultMap.put(FAILED_AUTHOR, "Ȩ����֤ʧ��");
		mResultMap.put(UNCORRECT_LOGIN_INFO, "��¼��Ϣ����ȷ");
	}
	
	/**
     * ��ʼ�����ؿ�
     */
	public void initLoadingDialog() {
		mLoadingDialog = new ProgressDialog(mContext);
		mLoadingDialog.setCanceledOnTouchOutside(false);	//���ܵ��
		mLoadingDialog.setTitle("����" + mTitle + "!!");
		mLoadingDialog.setCancelable(false);
		mLoadingDialog.setMessage("message test");
	}
	
	@Override
	public void onPrepare() {
		printLog("׼��" + mTitle + "-----");
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
		printLog("-----" + mTitle + "���");
	}

	@Override
	public void onLoadSuccess(HughieBaseResponse response) {

	}
	
	@Override
	public void onFailed(Exception ex, HughieBaseResponse response) {
		printLog(ex.toString());
		printLog("-----��������ʧ��");
	}

	@Override
	public void onCancel() {
		onLoadSuccess(null);
		printLog("-----ȡ��" + mTitle);
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
     * �����Ƿ��ܹ�������ʾ
     * @param isCanShowDialog
     */
	public void setCanShowDialog(boolean isCanShowDialog) {
		this.isCanShowDialog = isCanShowDialog;
	}
	
	public void showToast(String msg) {
		HughieToastManager.showInfo(mContext, msg);
	}
}

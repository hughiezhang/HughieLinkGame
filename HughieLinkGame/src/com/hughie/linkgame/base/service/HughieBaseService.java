package com.hughie.linkgame.base.service;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.reflect.ParameterizedType;
import org.apache.http.client.ClientProtocolException;
import com.hughie.link.support.common.HughieApplication;
import com.hughie.link.support.common.HughieLoggerManager;
import com.hughie.linkgame.R;
import com.hughie.linkgame.base.component.HughieLoadingDialog;
import com.hughie.linkgame.base.listener.HughieNetListener;
import com.hughie.linkgame.base.service.HughieBaseRequest.REQUESTSHOWTYPE;
import com.hughie.linkgame.common.HughieToastManager;
import com.hughie.linkgame.dao.HughieHttpClient;
import com.hughie.linkgame.utils.HughieAppUtils;
import com.hughie.linkgame.utils.HughieGsonUtils;
import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

/**
 * 基本服务类, 其他服务类继承此服务类
 * @ClassName: HughieBaseService
 * @author hughiezhang
 * @since 2015-09-11 14:35
 */
public abstract class HughieBaseService<REQUEST extends HughieBaseRequest, RESPOND extends HughieBaseResponse> {
	private HughieDataLoadingStatusManager mStatusManager;//数据加载状态管理类
	private HughieApplication mApplication;
	private REQUEST request;
	private RESPOND response;
	private Class<RESPOND> mResponseType;
	private HughieNetListener mListener;//网络请求监听接口
	private HughieLoadingDialog mDialog;//加载进度提示框
	
	private String result;
	
	private Exception ex;
	
	private NetAsyncTask task;
	
	private boolean isCanShow;
	
	private HughieBaseService service;
	
	private Context mContext;
	
	@SuppressWarnings("unchecked")
	public HughieBaseService(Context context) {
		this.mContext = context;
		mApplication = (HughieApplication) mContext.getApplicationContext();
		ParameterizedType pType = (ParameterizedType) getClass().getGenericSuperclass();
		mResponseType = (Class) pType.getActualTypeArguments()[1];
		request = newRequest();
		mStatusManager = HughieDataLoadingStatusManager.getInstance(mContext, 
				this.getClass().getSimpleName());
		isCanShow = true;
		service = this;
	}
	
	public abstract REQUEST newRequest();
	
	/**
	 * 发起请求
	 */
	public void request(HughieNetListener listener) {
		if (request == null) {
			mStatusManager.printLog("request is null!!");
			return;
		}
		
		mStatusManager.printLog(request.getUrl() + "");
		
		this.mListener = listener;
		
		if(task != null && task.getStatus() ==android.os.AsyncTask.Status.RUNNING) {
			return;
		}
		
		task = new NetAsyncTask(request);
		excuteTask(task);
	}
	
	public void cancelTask() {
		if (task != null) {
			task.cancel(true);
		}
	}
	
	@SuppressLint("NewApi")
	public void excuteTask(AsyncTask<Void, Void, integer> task) {
		if(android.os.Build.VERSION.SDK_INT >= 11) {
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			task.execute();
		}
	}
	
	@SuppressWarnings("unused")
	public void setCanShowDialog(boolean flag) {
		mStatusManager.setCanShowDialog(flag);
	}
	
	public boolean isCanShow() {
		return isCanShow;
	}

	public void setCanShow(boolean isCanShow) {
		this.isCanShow = isCanShow;
	}

	public Context getContext() {
		return mContext;
	}

	public void setContext(Context context) {
		this.mContext = context;
	}

	public REQUEST getRequest() {
		return request;
	}

	public void setRequest(REQUEST request) {
		this.request = request;
		this.request.setContext(mContext);
	}
	
	public RESPOND getResponse() {
		return response;
	}

	@SuppressWarnings("unchecked")
	public void setResponse(HughieBaseResponse response) {
		this.response = (RESPOND) response;
	}
	
	
	class	NetAsyncTask extends AsyncTask<Void, Void, integer> {
		REQUEST request;
		
		public NetAsyncTask(REQUEST request) {
			this.request = request;
		}
		
		@Override
		protected void onPreExecute() {
			ex = null;
			mListener.onPrepare();
			mStatusManager.onPrepare();
			mStatusManager.onLoading();
			
			if(request.getShowType() ==REQUESTSHOWTYPE.DISPLAY && mDialog == null){
				mDialog = new HughieLoadingDialog(mContext);
				mDialog.showLoadingDialog(mContext.getResources().getString(R.string.str_common_loading_dialog_info_txt));
			}
		}
		
		@Override
		protected integer doInBackground(Void... params) {
			result = "";
			
			try {
				if(request.getRequestType() == HughieBaseRequest.REQUESTTYPE.GET) {
					result = doNetworkTaskByGet();	//默认是get方法
				} else {
					result = doNetworkTaskByPost(); //post方式上传
				}
				
				response = HughieGsonUtils.getGsonInstance().fromJson(result, 
						mResponseType); // Json格式化response-
				
				HughieLoggerManager.println("info" + "\n" + "result==" + result + "\n response::" + response);
				
				if(mListener != null) {
					mListener.onComplete(response.getResult(), request, response);
				}
				
				mStatusManager.onComplete(response.getResult(), request, response);
			} catch (ClientProtocolException e) {
				ex = e;
			} catch (InterruptedIOException e) {
				ex = e;
			} catch (IOException e) {
				ex = e;
			} catch(Exception e) {
				ex = e;
			}
			
			if (ex != null) {
				response = null;
			}
			
			return null;
		}
		
		@Override
		protected void onCancelled() {
			if(mDialog != null) {
				mDialog.closeLoadingDialog();
				mDialog = null;
			}
			
			super.onCancelled();
			mStatusManager.onCancel();
		}
		
		@Override
		protected void onPostExecute(integer result) {
			super.onPostExecute(result);
			if(request.getShowType() == REQUESTSHOWTYPE.DISPLAY && mDialog != null){
				mDialog.closeLoadingDialog();
				mDialog = null;
			} 
			
			if(response != null) {
				mListener.onLoadSuccess(response);
				mStatusManager.onLoadSuccess(response);
			} else {
				mListener.onFailed(ex, response);
				mStatusManager.onFailed(ex, response);
				if(HughieAppUtils.isNetworkAvailable(mContext)) {
					HughieToastManager.showInfo(mContext, "服务器连接异常");
				} else {
					HughieToastManager.showInfo(mContext, "当前网络不给力，请检查网络设置");
				}
			}
			
			task = null;
		}
	}
	
	protected String doNetworkTaskByGet() throws IOException, ClientProtocolException,
			InterruptedIOException {
		String content = "";
		String url = request.getUrl();
		if(mApplication.isCmwap()) {
			content = HughieHttpClient.getViaCmwap(url);
		} else if(!mApplication.isFast()) {
			content = HughieHttpClient.getViaBadNetwork(url);
		} else {
			content = HughieHttpClient.get(url);
		}
		
		return content;
	}
	
	protected String doNetworkTaskByPost() throws IOException, ClientProtocolException,
			InterruptedIOException {
		File bitmaps = request.getBitmaps();
		String ret = HughieHttpClient.getContentByPost(request.getUrl(), request.toMap(), 
				bitmaps, request.getImageFlag());
		
		return ret;
	}
}

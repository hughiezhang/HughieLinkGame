package com.hughie.linkgame.widget;

import com.hughie.linkgame.R;
import com.hughie.linkgame.common.HughieCallBackMe;
import com.hughie.linkgame.common.HughieToastManager;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengDownloadListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.sax.StartElementListener;
import android.widget.Toast;

/**
 * 版本更新
 * @ClassName: HughieCheckVersion
 * @author hughiezhang
 * @since 2015-09-02 16:07
 */
public class HughieCheckVersion {
	private Context mContext;
	private HughieCallBackMe mCallBack;
	
	public HughieCheckVersion(Context context, HughieCallBackMe callBack) {
		this.mContext = context;
		this.mCallBack = callBack;
	}
	
	public void updateVersion(){
		UmengUpdateAgent.setDownloadListener(null);
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener(){
			@Override
			public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
				switch(updateStatus){
					case UpdateStatus.Yes:
						UmengUpdateAgent.showUpdateDialog(mContext, updateInfo);
//						if(updateInfo != null){
//							showUpdateDialog(updateInfo.path, updateInfo.updateLog);
//						}
						break;
					case UpdateStatus.No: // has no update
						checkOver("没有更新");
//						Toast.makeText(mContext, "没有更新", Toast.LENGTH_SHORT).show();
						break;
					case UpdateStatus.NoneWifi: // none wifi
						checkOver("没有wifi连接， 只在wifi下更新");
//						Toast.makeText(mContext, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
						break;
					case UpdateStatus.Timeout: // time out
						checkOver("超时");
//						Toast.makeText(mContext, "超时", Toast.LENGTH_SHORT).show();
						break;
				}
			}
		});
		
		UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {
			@Override
			public void onClick(int status) {
				switch(status){
					case UpdateStatus.Update:
						break;
					case UpdateStatus.NotNow:
						checkOver("以后再说");
						break;
				}
			}
		});
		
		UmengUpdateAgent.update(mContext);
	}
	
	private final void checkOver(String msg) {
		mCallBack.callback(msg);
	}
	
	private void showUpdateDialog(final String downloadUrl, String message){
		AlertDialog.Builder mUpdateAlertDialog = new  AlertDialog.Builder(mContext);
		mUpdateAlertDialog.setIcon(R.drawable.imgv_hughie_linkgame_logo);
		mUpdateAlertDialog.setTitle(R.string.str_new_version_title_txt);
		mUpdateAlertDialog.setMessage(message);
		mUpdateAlertDialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				try{
					mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl)));
				}catch(Exception ex){
					
				}
			}
		}).setNegativeButton("以后再说", null);
		 if (!((Activity)mContext).isFinishing())
			 mUpdateAlertDialog.show();
	}
}

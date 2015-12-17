package com.hughie.linkgame.base.component;

import com.hughie.linkgame.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

/**
 * ���ؽ�����ʾ��
 * @ClassName: HughieLoadingDialog
 * @author hughiezhang
 * @since 20150914 10:59
 */
public class HughieLoadingDialog extends Dialog {
	public Dialog mDialog;
	
	public HughieLoadingDialog(Context context) {
		super(context);
		if(context instanceof Activity) { // ��ֹcontextʧЧ
			Activity activity = (Activity)context;
			while(activity.getParent() != null) {
				activity = activity.getParent();
			}
			
			context = activity;
		}
		
		mDialog = new AlertDialog.Builder(context).create();
		mDialog.setOnKeyListener(mKeyListener);
	}
	
	OnKeyListener mKeyListener = new OnKeyListener() {
		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if(keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_HOME) {
				return true;
			}
			
			return false;
		}
	};
	
	/****
	 * ��ʾ��ʾ��
	 */
	public void showLoadingDialog() {
		if(mDialog != null) {
			if(mDialog.getContext() instanceof Activity) { // ��ֹcontextʧЧ
				Activity activity = (Activity)mDialog.getContext();
				if(activity.isFinishing()){
					return;
				}
			}
			
			mDialog.show();
			mDialog.setContentView(R.layout.layout_hughie_common_loading_dialog);
		}
	}
	
	/****
	 * ��ʾ��ʾ����������ʾ
	 */
	public void showLoadingDialog(String msg) {
		if(mDialog != null) {
			if(mDialog.getContext() instanceof Activity){// ��ֹcontextʧЧ
				Activity activity = (Activity) mDialog.getContext();
				if (activity.isFinishing()) {
					return;
				}
			}
			
			try {
				mDialog.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			mDialog.setContentView(R.layout.layout_hughie_common_loading_dialog);
			TextView mDialogTv = (TextView) mDialog.findViewById(R.id.loading_process_dialog_tv);
			mDialogTv.setText(msg);
			mDialogTv.setVisibility(View.VISIBLE);
		}
	}
	
	/****
	 * �ر���ʾ��
	 */
	public void closeLoadingDialog() {
		try{
			if(mDialog != null) {
				if(mDialog.getContext() instanceof Activity) {	// ��ֹcontextʧЧ
					Activity activity = (Activity) mDialog.getContext();
					if(activity.isFinishing()) {
						return;
					}
				}
				
				mDialog.dismiss();
				mDialog = null;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}

package com.hughie.linkgame.screen;

import com.hughie.linkgame.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * �˵�����
 * @ClassName: HughieScreenDesktop
 * @author hughiezhang
 * @since 2015-09-07 09:44
 */
public class HughieScreenDesktop {
	private Context mContext;
	
	private View mScreenDesktopLayout;								//��ǰ�����View
	
	public HughieScreenDesktop(Context context) {
		this.mContext = context;
		
		// �󶨲��ֵ���ǰView
		this.mScreenDesktopLayout = LayoutInflater.from(mContext).inflate(R.layout.layout_hughie_screen_desktop, null);
	}
	
	/**
	 * @description ��ȡ�˵�����
	 * @return �˵������View
	 */
	public View getView(){
		return this.mScreenDesktopLayout;
	}
}

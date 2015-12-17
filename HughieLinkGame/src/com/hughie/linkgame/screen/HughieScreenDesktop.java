package com.hughie.linkgame.screen;

import com.hughie.linkgame.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * 菜单界面
 * @ClassName: HughieScreenDesktop
 * @author hughiezhang
 * @since 2015-09-07 09:44
 */
public class HughieScreenDesktop {
	private Context mContext;
	
	private View mScreenDesktopLayout;								//当前界面的View
	
	public HughieScreenDesktop(Context context) {
		this.mContext = context;
		
		// 绑定布局到当前View
		this.mScreenDesktopLayout = LayoutInflater.from(mContext).inflate(R.layout.layout_hughie_screen_desktop, null);
	}
	
	/**
	 * @description 获取菜单界面
	 * @return 菜单界面的View
	 */
	public View getView(){
		return this.mScreenDesktopLayout;
	}
}

package com.hughie.linkgame.screen;

import com.hughie.linkgame.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * 主界面tab
 * @ClassName: HughieScreenTab
 * @author hughiezhang
 * @since 2015-09-07 17:18
 */
public class HughieScreenTab {
	private View mScreenTab;
	
	private Context mContext;
	
	public HughieScreenTab(Context context) {
		this.mContext = context;
		this.mScreenTab = LayoutInflater.from(mContext).inflate(R.layout.layout_hughie_screen_tab, null);
		
	}
	
	/**
	 * @description 获取桌面tab
	 * @return 桌面tab的View
	 */
	public View getView() {
		return mScreenTab;
	}
}

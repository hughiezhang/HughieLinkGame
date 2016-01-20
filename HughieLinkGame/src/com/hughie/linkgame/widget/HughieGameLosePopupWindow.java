package com.hughie.linkgame.widget;

import com.hughie.linkgame.ui.HughieGameMainActivity;

import android.content.Context;
import android.widget.PopupWindow;

/**
 * 游戏败局对话框界面
 * @ClassName: HughieGameLosePopupWindow
 * @author hughiezhang
 * @since 2016-01-19 14:26
 */
public class HughieGameLosePopupWindow extends PopupWindow {
	private HughieGameMainActivity mGameMainActivity;
	private Context mContext;
	
	public HughieGameLosePopupWindow(Context context, HughieGameMainActivity gameMainActivity) {
		super(context);
		this.mContext = context;
		this.mGameMainActivity = gameMainActivity;
	}
}

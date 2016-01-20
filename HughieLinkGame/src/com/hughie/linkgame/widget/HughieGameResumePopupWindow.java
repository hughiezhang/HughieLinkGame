package com.hughie.linkgame.widget;

import com.hughie.linkgame.R;
import com.hughie.linkgame.utils.HughieGameUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.PopupWindow;

/**
 * 游戏暂停界面
 * @ClassName: HughieGameResumePopupWindow
 * @author hughiezhang
 * @since 2016-01-05 15:14
 */
public class HughieGameResumePopupWindow extends PopupWindow {
	private Button mAppsBtn01;																				// game pause apps01
	private Button mAppsBtn02;																				// game pause apps02
	private Button mAppsBtn03;																				// game pause apps03
	private Button mAppsBtn04;																				// game pause apps04
	private Button mAppsBtn05;																				// game pause apps05
	
	private Button mResumeBtn;																				// 继续按钮
	private Button mReplayBtn;																				// 重新开始按钮
	private Button mMenuBtn;																					// 餐单按钮
	
	private OnGamePausePopupWindowListener mOnGamePausePopupWindowListener;
	
	private Context mContext;
	
	public HughieGameResumePopupWindow(Context context) {
		super(context);
		this.mContext = context;
		
		// 设置暂对话框的相关布局控件
		View mGamePauseView = LayoutInflater.from(mContext).inflate(R.layout.layout_hughie_game_resume, null);
		setContentView(mGamePauseView);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.imgv_game_black_background));
		
		initGamePauseViews();
		initGamePauseEvents();
	}
	
	// 初始化暂停界面的控件
	private void initGamePauseViews() {
		if(getContentView() == null)
			return;
		
		// game pause apps01
		mAppsBtn01 = (Button) getContentView().findViewById(R.id.game_pause_apps01_btn);
		// game pause apps02
		mAppsBtn02 = (Button) getContentView().findViewById(R.id.game_pause_apps02_btn);
		// game pause apps03
		mAppsBtn03 = (Button) getContentView().findViewById(R.id.game_pause_apps03_btn);
		// game pause apps04
		mAppsBtn04 = (Button) getContentView().findViewById(R.id.game_pause_apps04_btn);
		// game pause apps05
		mAppsBtn05 = (Button) getContentView().findViewById(R.id.game_pause_apps05_btn);
		
		// 继续按钮
		mResumeBtn = (Button) getContentView().findViewById(R.id.game_pause_resume_btn);
		// 重新开始按钮
		mReplayBtn = (Button) getContentView().findViewById(R.id.game_pause_replay_btn);
		// 餐单按钮
		mMenuBtn = (Button) getContentView().findViewById(R.id.game_pause_menu_btn);
		
		// 设置game pause apps的tween动画
		Animation mAppsAnimation = AnimationUtils.loadAnimation(mContext, R.anim.hughie_anim_game_pause_apps);
		mAppsBtn01.setAnimation(mAppsAnimation);
		mAppsBtn02.setAnimation(mAppsAnimation);
		mAppsBtn03.setAnimation(mAppsAnimation);
		mAppsBtn04.setAnimation(mAppsAnimation);
		mAppsBtn05.setAnimation(mAppsAnimation);
		
		if(HughieGameUtils.isRunGamePauseApps(mContext, HughieGameUtils.mGamePauseAppsPackageName[0]))
			mAppsBtn01.setVisibility(View.GONE);
		if(HughieGameUtils.isRunGamePauseApps(mContext, HughieGameUtils.mGamePauseAppsPackageName[1]))
			mAppsBtn02.setVisibility(View.GONE);
		if(HughieGameUtils.isRunGamePauseApps(mContext, HughieGameUtils.mGamePauseAppsPackageName[2]))
			mAppsBtn03.setVisibility(View.GONE);
		if(HughieGameUtils.isRunGamePauseApps(mContext, HughieGameUtils.mGamePauseAppsPackageName[3]))
			mAppsBtn04.setVisibility(View.GONE);
		if(HughieGameUtils.isRunGamePauseApps(mContext, HughieGameUtils.mGamePauseAppsPackageName[4]))
			mAppsBtn05.setVisibility(View.GONE);
	}
	
	// 初始化暂停界面的监听
	private void initGamePauseEvents() {
		// game pause apps01点击监听
		mAppsBtn01.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mOnGamePausePopupWindowListener != null)
					mOnGamePausePopupWindowListener.onGamePauseApps01Click();
			}
		});
		
		// game pause apps02点击监听
		mAppsBtn02.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mOnGamePausePopupWindowListener != null)
					mOnGamePausePopupWindowListener.onGamePauseApps02Click();
			}
		});
		
		// game pause apps03点击监听
		mAppsBtn03.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mOnGamePausePopupWindowListener != null)
					mOnGamePausePopupWindowListener.onGamePauseApps03Click();
			}
		});
		
		// game pause apps04点击监听
		mAppsBtn04.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mOnGamePausePopupWindowListener != null)
					mOnGamePausePopupWindowListener.onGamePauseApps04Click();
			}
		});
		
		// game pause apps05点击监听
		mAppsBtn05.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mOnGamePausePopupWindowListener != null)
					mOnGamePausePopupWindowListener.onGamePauseApps05Click();
			}
		});
		
		// 继续按钮点击监听
		mResumeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mOnGamePausePopupWindowListener != null)
					mOnGamePausePopupWindowListener.onGamePauseResumeClick();
			}
		});
		
		// 重新开始按钮点击监听
		mReplayBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mOnGamePausePopupWindowListener != null)
					mOnGamePausePopupWindowListener.onGamePauseReplayClick();
			}
		});
		
		// 菜单按钮点击监听
		mMenuBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mOnGamePausePopupWindowListener != null)
					mOnGamePausePopupWindowListener.onGamePauseMenuClick();
			}
		});
	}
	
	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		if(mOnGamePausePopupWindowListener != null)
			mOnGamePausePopupWindowListener.onGamePauseDataCommand();
		
		super.showAtLocation(parent, gravity, x, y);
	}
	
	public void setOnGamePausePopupWindowListener(OnGamePausePopupWindowListener 
			onGamePausePopupWindowListener) {
		this.mOnGamePausePopupWindowListener = onGamePausePopupWindowListener;
	}
	
	public interface OnGamePausePopupWindowListener {
		public void onGamePauseDataCommand();															// 处理暂停界面的相关数据
		public void onGamePauseApps01Click();																// apps01点击监听
		public void onGamePauseApps02Click();																// apps02点击监听
		public void onGamePauseApps03Click();																// apps03点击监听
		public void onGamePauseApps04Click();																// apps04点击监听
		public void onGamePauseApps05Click();																// apps05点击监听
		public void onGamePauseResumeClick();															// 继续按钮点击监听
		public void onGamePauseReplayClick();																// 重新开始按钮点击监听
		public void onGamePauseMenuClick();																// 菜单按钮点击监听
	}
}

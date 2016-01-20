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
 * ��Ϸ��ͣ����
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
	
	private Button mResumeBtn;																				// ������ť
	private Button mReplayBtn;																				// ���¿�ʼ��ť
	private Button mMenuBtn;																					// �͵���ť
	
	private OnGamePausePopupWindowListener mOnGamePausePopupWindowListener;
	
	private Context mContext;
	
	public HughieGameResumePopupWindow(Context context) {
		super(context);
		this.mContext = context;
		
		// �����ݶԻ������ز��ֿؼ�
		View mGamePauseView = LayoutInflater.from(mContext).inflate(R.layout.layout_hughie_game_resume, null);
		setContentView(mGamePauseView);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.imgv_game_black_background));
		
		initGamePauseViews();
		initGamePauseEvents();
	}
	
	// ��ʼ����ͣ����Ŀؼ�
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
		
		// ������ť
		mResumeBtn = (Button) getContentView().findViewById(R.id.game_pause_resume_btn);
		// ���¿�ʼ��ť
		mReplayBtn = (Button) getContentView().findViewById(R.id.game_pause_replay_btn);
		// �͵���ť
		mMenuBtn = (Button) getContentView().findViewById(R.id.game_pause_menu_btn);
		
		// ����game pause apps��tween����
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
	
	// ��ʼ����ͣ����ļ���
	private void initGamePauseEvents() {
		// game pause apps01�������
		mAppsBtn01.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mOnGamePausePopupWindowListener != null)
					mOnGamePausePopupWindowListener.onGamePauseApps01Click();
			}
		});
		
		// game pause apps02�������
		mAppsBtn02.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mOnGamePausePopupWindowListener != null)
					mOnGamePausePopupWindowListener.onGamePauseApps02Click();
			}
		});
		
		// game pause apps03�������
		mAppsBtn03.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mOnGamePausePopupWindowListener != null)
					mOnGamePausePopupWindowListener.onGamePauseApps03Click();
			}
		});
		
		// game pause apps04�������
		mAppsBtn04.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mOnGamePausePopupWindowListener != null)
					mOnGamePausePopupWindowListener.onGamePauseApps04Click();
			}
		});
		
		// game pause apps05�������
		mAppsBtn05.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mOnGamePausePopupWindowListener != null)
					mOnGamePausePopupWindowListener.onGamePauseApps05Click();
			}
		});
		
		// ������ť�������
		mResumeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mOnGamePausePopupWindowListener != null)
					mOnGamePausePopupWindowListener.onGamePauseResumeClick();
			}
		});
		
		// ���¿�ʼ��ť�������
		mReplayBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mOnGamePausePopupWindowListener != null)
					mOnGamePausePopupWindowListener.onGamePauseReplayClick();
			}
		});
		
		// �˵���ť�������
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
		public void onGamePauseDataCommand();															// ������ͣ������������
		public void onGamePauseApps01Click();																// apps01�������
		public void onGamePauseApps02Click();																// apps02�������
		public void onGamePauseApps03Click();																// apps03�������
		public void onGamePauseApps04Click();																// apps04�������
		public void onGamePauseApps05Click();																// apps05�������
		public void onGamePauseResumeClick();															// ������ť�������
		public void onGamePauseReplayClick();																// ���¿�ʼ��ť�������
		public void onGamePauseMenuClick();																// �˵���ť�������
	}
}

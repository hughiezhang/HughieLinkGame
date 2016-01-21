package com.hughie.linkgame.widget;

import com.hughie.linkgame.R;
import com.hughie.linkgame.ui.HughieGameMainActivity;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

/**
 * 游戏败局对话框界面
 * @ClassName: HughieGameLosePopupWindow
 * @author hughiezhang
 * @since 2016-01-19 14:26
 */
public class HughieGameLosePopupWindow extends PopupWindow {
	private ImageView mAnimImgv;										// 游戏败局动画图片
	private Button mReplayBtn;											// 重新开始按钮
	private Button mMenuBtn;												// 餐单按钮							
	
	private OnGameLosePopupWindowListener mOnGameLosePopupWindowListener;
	
	private HughieGameMainActivity mGameMainActivity;
	private Context mContext;
	
	public HughieGameLosePopupWindow(Context context, HughieGameMainActivity gameMainActivity) {
		super(context);
		this.mContext = context;
		this.mGameMainActivity = gameMainActivity;
		
		// 设置败局对话框的相关布局控件
		View mGameLoseView = LayoutInflater.from(mContext).inflate(R.layout.layout_hughie_game_lose, null);
		setContentView(mGameLoseView);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.imgv_game_black_background));
		
		initGameLoseViews();
		initGameLoseEvents();
	}
	
	// 初始化败局界面的控件
	private void initGameLoseViews() {
		if(getContentView() == null)
			return;
		
		// 败局动画图片
		mAnimImgv = (ImageView) getContentView().findViewById(R.id.game_lose_anim_imgv);
		// 重新开始按钮
		mReplayBtn = (Button) getContentView().findViewById(R.id.game_lose_replay_btn);
		// 餐单按钮	
		mMenuBtn = (Button) getContentView().findViewById(R.id.game_lose_menu_btn);
	}
	
	// 初始化败局界面的监听
	private void initGameLoseEvents() {
		// 设置赢局动画
		mAnimImgv.setBackgroundResource(R.anim.hughie_anim_game_lose_popupwindow);
		final AnimationDrawable mLoseAnimationDrawable = (AnimationDrawable) mAnimImgv.getBackground();
		mAnimImgv.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
			boolean mLoseFirst = true;
			@Override
			public boolean onPreDraw() {
				if(mLoseFirst) {
					mLoseAnimationDrawable.start();
					mLoseFirst = false;
				}
				
				return true;
			}
		});
		
		TranslateAnimation mLoseTranslateAnimation = new TranslateAnimation(-400.0f, 0.0f, 0.0f, 0.0f);
		mLoseTranslateAnimation.setDuration(1000L);
		mAnimImgv.startAnimation(mLoseTranslateAnimation);
		
		// 重新开始按钮点击监听
		mReplayBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mOnGameLosePopupWindowListener != null)
					mOnGameLosePopupWindowListener.onGameLoseReplayClick();
			}
		});
		
		// 菜单按钮点击监听
		mMenuBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mOnGameLosePopupWindowListener != null)
					mOnGameLosePopupWindowListener.onGameLoseMenuClick();
			}
		});
	}
	
	public void setOnGameLosePopupWindowListener(OnGameLosePopupWindowListener 
			onGameLosePopupWindowListener) {
		this.mOnGameLosePopupWindowListener = onGameLosePopupWindowListener;
	}
	
	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		if(mOnGameLosePopupWindowListener != null) {
			mOnGameLosePopupWindowListener.onGameLoseMusicPlay();
			mOnGameLosePopupWindowListener.onGameLoseDataCommand();
		}
		super.showAtLocation(parent, gravity, x, y);
	}
	
	public static interface OnGameLosePopupWindowListener {
		public void onGameLoseMusicPlay();									// 播放败局的音乐
		public void onGameLoseDataCommand();							// 处理败局的相应数据
		public void onGameLoseReplayClick();								// 重新开始按钮点击监听
		public void onGameLoseMenuClick();									// 餐单按钮点击监听
	}
}

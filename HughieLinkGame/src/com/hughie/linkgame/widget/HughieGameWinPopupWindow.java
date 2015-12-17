package com.hughie.linkgame.widget;

import com.hughie.linkgame.R;
import com.hughie.linkgame.ui.HughieGameMainActivity;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

/**
 * 游戏赢局对话框界面
 * @ClassName: HughieGameWinPopupWindow
 * @author hughiezhang
 * @since 2015-11-10 18:04
 */
public class HughieGameWinPopupWindow extends PopupWindow {
	private ImageView mAnimImgv;									// 赢局动画图片
	private ImageView mStarImgv1;									// 第一颗星
	private ImageView mStarImgv2;									// 第二颗星
	private ImageView mStarImgv3;									// 第三颗星
	private Button mReplayBtn;										// 重新开始按钮
	private Button mNextBtn;											// 下一关按钮
	
	private HughieGameMainActivity mGameMainActivity;
	private Context mContext;
	
	private OnGameWinPopupWindowListener mOnGameWinPopupWindowListener;
	
	public HughieGameWinPopupWindow(Context context, HughieGameMainActivity gameMainActivity) {
		super(context);
		this.mContext = context;
		this.mGameMainActivity = gameMainActivity;
		
		// 设置赢局对话框的相关布局控件
		View mGameWinView = LayoutInflater.from(mContext).inflate(R.layout.layout_hughie_game_win, null);
		setContentView(mGameWinView);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		setBackgroundDrawable(context.getResources().getDrawable(R.drawable.imgv_game_black_background));
		
		initGameWinViews();
		initGameWinEvents();
	}
	
	// 初始化赢局界面的控件
	@SuppressWarnings("static-access")
	private void initGameWinViews() {
		if(getContentView() == null)
			return;
		
		// 赢局动画imageview
		mAnimImgv = (ImageView) getContentView().findViewById(R.id.game_win_anim_imgv);
		// 第一颗星
		mStarImgv1 = (ImageView) getContentView().findViewById(R.id.game_win_star1_imgv);
		// 第二颗星
		mStarImgv2 = (ImageView) getContentView().findViewById(R.id.game_win_star2_imgv);
		// 第三颗星
		mStarImgv3 = (ImageView) getContentView().findViewById(R.id.game_win_star3_imgv);
		
		// 重新开始按钮
		mReplayBtn = (Button) getContentView().findViewById(R.id.game_win_replay_btn);
		// 下一关按钮
		mNextBtn = (Button) getContentView().findViewById(R.id.game_win_next_btn);
		
		// 设置赢局的星级状态
//		getWinFinishStatus(mGameMainActivity.mGameFinishStatus);
	}
	
	// 设置赢局星级的状态
	@SuppressWarnings("static-access")
//	private void getWinFinishStatus(int finishStatus) {
//		// 一星
//		if(finishStatus == mGameMainActivity.TAG_GAME_MAIN_STATUS_WIN_STAR1) {
//			mStarImgv1.setBackgroundResource(R.drawable.imgv_game_main_win_star_checked);
//			mStarImgv2.setBackgroundResource(R.drawable.imgv_game_main_win_star_unchecked);
//			mStarImgv3.setBackgroundResource(R.drawable.imgv_game_main_win_star_unchecked);
//		// 二星
//		} else if(finishStatus == mGameMainActivity.TAG_GAME_MAIN_STATUS_WIN_STAR2) {
//			mStarImgv1.setBackgroundResource(R.drawable.imgv_game_main_win_star_checked);
//			mStarImgv2.setBackgroundResource(R.drawable.imgv_game_main_win_star_checked);
//			mStarImgv3.setBackgroundResource(R.drawable.imgv_game_main_win_star_unchecked);
//		} else if(finishStatus == mGameMainActivity.TAG_GAME_MAIN_STATUS_WIN_STAR3) {
//			mStarImgv1.setBackgroundResource(R.drawable.imgv_game_main_win_star_checked);
//			mStarImgv2.setBackgroundResource(R.drawable.imgv_game_main_win_star_checked);
//			mStarImgv3.setBackgroundResource(R.drawable.imgv_game_main_win_star_checked);
//		}
//	}
	
	// 初始化赢局界面的监听
	private void initGameWinEvents() {
		// 设置赢局动画
		mAnimImgv.setBackgroundResource(R.anim.hughie_anim_game_win_popupwindow);
		final AnimationDrawable mWinAnimationDrawale = (AnimationDrawable) mAnimImgv.getBackground();
		mAnimImgv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			boolean mWinFirst = true;
			@Override
			public boolean onPreDraw() {
				if(mWinFirst) {
					mWinAnimationDrawale.start();
					mWinFirst = false;
				} 
				
				return true;
			}
		});
		
		TranslateAnimation mWinTranslateAnimation = new TranslateAnimation(-400.0f, 0.0f, 0.0f, 0.0f);
		mWinTranslateAnimation.setDuration(1000L);
		mAnimImgv.startAnimation(mWinTranslateAnimation);
		
		// 重新开始按钮点击监听
		mReplayBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mOnGameWinPopupWindowListener != null)
					mOnGameWinPopupWindowListener.onGameWinReplayClick();
			}
		});
		
		// 下一关按钮点击监听
		mNextBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mOnGameWinPopupWindowListener != null)
					mOnGameWinPopupWindowListener.onGameWinNextClick();
			}
		});
	}
	
	public void setOnGameWinPopupWindowListener(OnGameWinPopupWindowListener 
			onGameWinPopupWindowListener) {
		this.mOnGameWinPopupWindowListener = onGameWinPopupWindowListener;
	}
	
	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		if(mOnGameWinPopupWindowListener != null) {
			mOnGameWinPopupWindowListener.onGameWinMusicPlay();
			mOnGameWinPopupWindowListener.onGameWinDataCommand();
		}
		super.showAtLocation(parent, gravity, x, y);
	}
	
	public static interface OnGameWinPopupWindowListener {
		public void onGameWinMusicPlay();								// 播放赢局的音乐
		public void onGameWinDataCommand();						// 处理赢局的相应数据
		public void onGameWinReplayClick();								// 重新开始按钮点击监听
		public void onGameWinNextClick();
	}
}

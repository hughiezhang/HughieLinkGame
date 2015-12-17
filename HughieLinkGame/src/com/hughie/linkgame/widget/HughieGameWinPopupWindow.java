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
 * ��ϷӮ�ֶԻ������
 * @ClassName: HughieGameWinPopupWindow
 * @author hughiezhang
 * @since 2015-11-10 18:04
 */
public class HughieGameWinPopupWindow extends PopupWindow {
	private ImageView mAnimImgv;									// Ӯ�ֶ���ͼƬ
	private ImageView mStarImgv1;									// ��һ����
	private ImageView mStarImgv2;									// �ڶ�����
	private ImageView mStarImgv3;									// ��������
	private Button mReplayBtn;										// ���¿�ʼ��ť
	private Button mNextBtn;											// ��һ�ذ�ť
	
	private HughieGameMainActivity mGameMainActivity;
	private Context mContext;
	
	private OnGameWinPopupWindowListener mOnGameWinPopupWindowListener;
	
	public HughieGameWinPopupWindow(Context context, HughieGameMainActivity gameMainActivity) {
		super(context);
		this.mContext = context;
		this.mGameMainActivity = gameMainActivity;
		
		// ����Ӯ�ֶԻ������ز��ֿؼ�
		View mGameWinView = LayoutInflater.from(mContext).inflate(R.layout.layout_hughie_game_win, null);
		setContentView(mGameWinView);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		setBackgroundDrawable(context.getResources().getDrawable(R.drawable.imgv_game_black_background));
		
		initGameWinViews();
		initGameWinEvents();
	}
	
	// ��ʼ��Ӯ�ֽ���Ŀؼ�
	@SuppressWarnings("static-access")
	private void initGameWinViews() {
		if(getContentView() == null)
			return;
		
		// Ӯ�ֶ���imageview
		mAnimImgv = (ImageView) getContentView().findViewById(R.id.game_win_anim_imgv);
		// ��һ����
		mStarImgv1 = (ImageView) getContentView().findViewById(R.id.game_win_star1_imgv);
		// �ڶ�����
		mStarImgv2 = (ImageView) getContentView().findViewById(R.id.game_win_star2_imgv);
		// ��������
		mStarImgv3 = (ImageView) getContentView().findViewById(R.id.game_win_star3_imgv);
		
		// ���¿�ʼ��ť
		mReplayBtn = (Button) getContentView().findViewById(R.id.game_win_replay_btn);
		// ��һ�ذ�ť
		mNextBtn = (Button) getContentView().findViewById(R.id.game_win_next_btn);
		
		// ����Ӯ�ֵ��Ǽ�״̬
//		getWinFinishStatus(mGameMainActivity.mGameFinishStatus);
	}
	
	// ����Ӯ���Ǽ���״̬
	@SuppressWarnings("static-access")
//	private void getWinFinishStatus(int finishStatus) {
//		// һ��
//		if(finishStatus == mGameMainActivity.TAG_GAME_MAIN_STATUS_WIN_STAR1) {
//			mStarImgv1.setBackgroundResource(R.drawable.imgv_game_main_win_star_checked);
//			mStarImgv2.setBackgroundResource(R.drawable.imgv_game_main_win_star_unchecked);
//			mStarImgv3.setBackgroundResource(R.drawable.imgv_game_main_win_star_unchecked);
//		// ����
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
	
	// ��ʼ��Ӯ�ֽ���ļ���
	private void initGameWinEvents() {
		// ����Ӯ�ֶ���
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
		
		// ���¿�ʼ��ť�������
		mReplayBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mOnGameWinPopupWindowListener != null)
					mOnGameWinPopupWindowListener.onGameWinReplayClick();
			}
		});
		
		// ��һ�ذ�ť�������
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
		public void onGameWinMusicPlay();								// ����Ӯ�ֵ�����
		public void onGameWinDataCommand();						// ����Ӯ�ֵ���Ӧ����
		public void onGameWinReplayClick();								// ���¿�ʼ��ť�������
		public void onGameWinNextClick();
	}
}

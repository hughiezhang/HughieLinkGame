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
 * ��Ϸ�ֶܾԻ������
 * @ClassName: HughieGameLosePopupWindow
 * @author hughiezhang
 * @since 2016-01-19 14:26
 */
public class HughieGameLosePopupWindow extends PopupWindow {
	private ImageView mAnimImgv;										// ��Ϸ�ֶܾ���ͼƬ
	private Button mReplayBtn;											// ���¿�ʼ��ť
	private Button mMenuBtn;												// �͵���ť							
	
	private OnGameLosePopupWindowListener mOnGameLosePopupWindowListener;
	
	private HughieGameMainActivity mGameMainActivity;
	private Context mContext;
	
	public HughieGameLosePopupWindow(Context context, HughieGameMainActivity gameMainActivity) {
		super(context);
		this.mContext = context;
		this.mGameMainActivity = gameMainActivity;
		
		// ���ðֶܾԻ������ز��ֿؼ�
		View mGameLoseView = LayoutInflater.from(mContext).inflate(R.layout.layout_hughie_game_lose, null);
		setContentView(mGameLoseView);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.imgv_game_black_background));
		
		initGameLoseViews();
		initGameLoseEvents();
	}
	
	// ��ʼ���ֽܾ���Ŀؼ�
	private void initGameLoseViews() {
		if(getContentView() == null)
			return;
		
		// �ֶܾ���ͼƬ
		mAnimImgv = (ImageView) getContentView().findViewById(R.id.game_lose_anim_imgv);
		// ���¿�ʼ��ť
		mReplayBtn = (Button) getContentView().findViewById(R.id.game_lose_replay_btn);
		// �͵���ť	
		mMenuBtn = (Button) getContentView().findViewById(R.id.game_lose_menu_btn);
	}
	
	// ��ʼ���ֽܾ���ļ���
	private void initGameLoseEvents() {
		// ����Ӯ�ֶ���
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
		
		// ���¿�ʼ��ť�������
		mReplayBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mOnGameLosePopupWindowListener != null)
					mOnGameLosePopupWindowListener.onGameLoseReplayClick();
			}
		});
		
		// �˵���ť�������
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
		public void onGameLoseMusicPlay();									// ���Űֵܾ�����
		public void onGameLoseDataCommand();							// ����ֵܾ���Ӧ����
		public void onGameLoseReplayClick();								// ���¿�ʼ��ť�������
		public void onGameLoseMenuClick();									// �͵���ť�������
	}
}

package com.hughie.linkgame.widget;

import com.hughie.linkgame.R;
import com.hughie.linkgame.common.HughieGameApplication;
import com.hughie.linkgame.utils.HughieActivityUtils;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * ��Ϸ��������
 * @ClassName: HughieGameHelpPopupWindow
 * @author hughiezhang
 * @since 2015-10-19 15:32
 */
public class HughieGameHelpPopupWindow extends PopupWindow {
	private ScrollView mGameHelpSv;
	private TextView mHelpBeforeRefreshTv;							//ˢ��ʹ��ǰ
	private TextView mHelpAfterRefreshTv;							//ˢ��ʹ�ú�
	private TextView mHelpBeforeBombTv;							//ը��ʹ��ǰ
	private TextView mHelpAfterBombTv;								//ը��ʹ�ú�
	private TextView mHelpBeforeHintTv;								//��ʾʹ��ǰ
	private TextView mHelpAfterHintTv;									//��ʾʹ�ú�
	private TextView mHelpBeforeFreezeTv;							//����ʹ��ǰ
	private TextView mHelpAfterFreezeTv;								//����ʹ�ú�
	private TextView mHelpRefreshContentTv;						//ˢ�±���
	private TextView mHelpBombContentTv;							//ը������
	private TextView mHelpHintContentTv;							//��ʾ����
	private TextView mHelpFreezeContentTv;							//�������
	private Button mHelpArrowDownBtn;								//���·�ҳ��ť
	private Button mHelpArrowUpBtn;									//���Ϸ�ҳ��ť
	private Button mHelpCloseBtn;										//�رհ�ť
	
	private int helpState;
	
	private HughieGameApplication mGameApplication;
	private Context mContext;
	
	private IGameHelpPopupWindowListener mGameHelpListener;
	
	public static final int MSG_HELP_BUTTON_ARROWDOWN = 1000;
	public static final int MSG_HELP_BUTTON_ARROWUP = 1001;

	public HughieGameHelpPopupWindow(Context context, HughieGameApplication gameApplication) {
		super(context);
		this.mContext = context;
		this.mGameApplication = gameApplication;
		View mGameHelpView = LayoutInflater.from(mContext).inflate(R.layout.layout_hughie_game_help, null);
		setContentView(mGameHelpView);
		setWidth(mGameApplication.displayWidth);
		setHeight(mGameApplication.displayHeight - 32);
		setBackgroundDrawable(context.getResources().getDrawable(R.drawable.imgv_game_black_background));
		
		initGameHelpViews();
		initGameHelpListener();
	}
	
	//��ʼ����������Ŀؼ�
	private void initGameHelpViews() {
		if(getContentView() == null)
			return;
		
		mGameHelpSv = (ScrollView) getContentView().findViewById(R.id.popup_game_help_sv);
		
		//before refresh
		mHelpBeforeRefreshTv = (TextView) getContentView().findViewById(R.id.popup_game_help_before_refresh_tv);
		mHelpBeforeRefreshTv.setTypeface(HughieActivityUtils.getFontType(mContext, "hobostd.otf"));
		
		//after refresh
		mHelpAfterRefreshTv = (TextView) getContentView().findViewById(R.id.popup_game_help_after_refresh_tv);
		mHelpAfterRefreshTv.setTypeface(HughieActivityUtils.getFontType(mContext, "hobostd.otf"));
		
		//before bomb
		mHelpBeforeBombTv = (TextView) getContentView().findViewById(R.id.popup_game_help_before_bomb_tv);
		mHelpBeforeBombTv.setTypeface(HughieActivityUtils.getFontType(mContext, "hobostd.otf"));
		
		//after bomb
		mHelpAfterBombTv = (TextView) getContentView().findViewById(R.id.popup_game_help_after_bomb_tv);
		mHelpAfterBombTv.setTypeface(HughieActivityUtils.getFontType(mContext, "hobostd.otf"));
		
		//before hint
		mHelpBeforeHintTv = (TextView) getContentView().findViewById(R.id.popup_game_help_before_hint_tv);
		mHelpBeforeHintTv.setTypeface(HughieActivityUtils.getFontType(mContext, "hobostd.otf"));
		
		//after hint
		mHelpAfterHintTv = (TextView) getContentView().findViewById(R.id.popup_game_help_after_hint_tv);
		mHelpAfterHintTv.setTypeface(HughieActivityUtils.getFontType(mContext, "hobostd.otf"));
		
		//before freeze
		mHelpBeforeFreezeTv = (TextView) getContentView().findViewById(R.id.popup_game_help_before_freeze_tv);
		mHelpBeforeFreezeTv.setTypeface(HughieActivityUtils.getFontType(mContext, "hobostd.otf"));
		
		//after freeze
		mHelpAfterFreezeTv = (TextView) getContentView().findViewById(R.id.popup_game_help_after_freeze_tv);
		mHelpAfterFreezeTv.setTypeface(HughieActivityUtils.getFontType(mContext, "hobostd.otf"));
		
		//help refresh
		mHelpRefreshContentTv = (TextView) getContentView().findViewById(R.id.popup_game_help_refresh_content_tv);
		mHelpRefreshContentTv.setTypeface(HughieActivityUtils.getFontType(mContext, "hobostd.otf"));
		
		//help bomb
		mHelpBombContentTv = (TextView) getContentView().findViewById(R.id.popup_game_help_bomb_content_tv);
		mHelpBombContentTv.setTypeface(HughieActivityUtils.getFontType(mContext, "hobostd.otf"));
		
		//help hint
		mHelpHintContentTv = (TextView) getContentView().findViewById(R.id.popup_game_help_hint_content_tv);
		mHelpHintContentTv.setTypeface(HughieActivityUtils.getFontType(mContext, "hobostd.otf"));
		
		//help freeze
		mHelpFreezeContentTv = (TextView) getContentView().findViewById(R.id.popup_game_help_freeze_content_tv);
		mHelpFreezeContentTv.setTypeface(HughieActivityUtils.getFontType(mContext, "hobostd.otf"));
		
		//help arraw down button
		mHelpArrowDownBtn = (Button) getContentView().findViewById(R.id.popup_game_help_arrow_down_btn);
		mHelpArrowDownBtn.setBackgroundResource(R.anim.hughie_anim_geme_help_arraw_down);
		
		//help arrow up button
		mHelpArrowUpBtn = (Button) getContentView().findViewById(R.id.popup_game_help_arrow_up_btn);
		mHelpArrowUpBtn.setBackgroundResource(R.anim.hughie_anim_geme_help_arraw_up);
		
		//help close button
		mHelpCloseBtn = (Button) getContentView().findViewById(R.id.popup_game_help_close_btn);
	}
	
	//��ʼ����������ؼ��ļ�����
	private void initGameHelpListener() {
		//arrow down������ʾ����
		final AnimationDrawable mArrowDownAnimationDrawable = (AnimationDrawable) mHelpArrowDownBtn.getBackground();
		mHelpArrowDownBtn.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			boolean mHelpFirst = true;
			public boolean onPreDraw() {
				if(mHelpFirst) {
					mArrowDownAnimationDrawable.start();
					mHelpFirst = false;
				}
				
				return true;
			}
		});
		
		//arrow up������ʾ����
		final AnimationDrawable mArrowUpAnimationDrawable = (AnimationDrawable) mHelpArrowUpBtn.getBackground();
		mHelpArrowUpBtn.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			boolean mHelpFirst = true;
			public boolean onPreDraw() {
				if(mHelpFirst) {
					mArrowUpAnimationDrawable.start();
					mHelpFirst = false;
				}
				
				return true;
			}
		});
		
		//arrow down�������
		mHelpArrowDownBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mGameHelpSv.scrollBy(0, 1000);
				mHelpArrowDownBtn.setVisibility(View.GONE);
				mHelpArrowUpBtn.setVisibility(View.VISIBLE);
			}
		});
		
		//arrow up�������
		mHelpArrowUpBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mGameHelpSv.scrollBy(0, -1000);
				mHelpArrowDownBtn.setVisibility(View.VISIBLE);
				mHelpArrowUpBtn.setVisibility(View.GONE);
			}
		});
		
		//help scroller��������
		mGameHelpSv.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_MOVE) {
					if(mGameHelpSv.getChildAt(0).getMeasuredHeight() - v.getHeight() == v.getScrollY()) {
						mHelpHandler.sendEmptyMessage(MSG_HELP_BUTTON_ARROWDOWN);
					} else if(v.getScrollY() == 0) {
						mHelpHandler.sendEmptyMessage(MSG_HELP_BUTTON_ARROWUP);
					}
				}
				
				return false;
			}
		});
		
		mHelpCloseBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mGameHelpListener != null)
					mGameHelpListener.onGameHelpCloseClick(helpState);
			}
		});
	}
	
	//game help handler
	Handler mHelpHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == MSG_HELP_BUTTON_ARROWDOWN) {
				mHelpArrowDownBtn.setVisibility(View.GONE);
				mHelpArrowUpBtn.setVisibility(View.VISIBLE);
			} else if(msg.what == MSG_HELP_BUTTON_ARROWUP) {
				mHelpArrowDownBtn.setVisibility(View.VISIBLE);
				mHelpArrowUpBtn.setVisibility(View.GONE);
			}
		}
	};
	
	//���arrow down��arrow up��animation
	public void clearGameArrowAnimation(){
		mHelpArrowDownBtn.clearAnimation();
		mHelpArrowUpBtn.clearAnimation();
	}
	
	public void setIGameHelpPopupWindowListener(IGameHelpPopupWindowListener listener) {
		this.mGameHelpListener = listener;
	} 
	
	public int getHelpState() {
		return helpState;
	}

	public void setHelpState(int helpState) {
		this.helpState = helpState;
	}



	public static interface IGameHelpPopupWindowListener {
		public void onGameHelpCloseClick(int helpState);
	}
}

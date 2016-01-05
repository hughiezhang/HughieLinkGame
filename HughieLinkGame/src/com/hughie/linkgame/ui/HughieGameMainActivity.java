package com.hughie.linkgame.ui;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hughie.linkgame.R;
import com.hughie.linkgame.base.HughieBaseActivity;
import com.hughie.linkgame.common.HughieGameApplication;
import com.hughie.linkgame.common.HughieSPManager;
import com.hughie.linkgame.utils.HughieActivityUtils;
import com.hughie.linkgame.utils.HughieGameController;
import com.hughie.linkgame.widget.HughieGameHelpPopupWindow;
import com.hughie.linkgame.widget.HughieGameHelpPopupWindow.IGameHelpPopupWindowListener;

/**
 * ��Ϸ������
 * @ClassName: HughieGameMainActivity
 * @author hughiezhang
 * @since 2015-10-14 10:07
 */
public class HughieGameMainActivity extends HughieBaseActivity implements
		OnClickListener {
	
	public static final String Extra_GameMode = "game_mode";
	public static final String Extra_GameLevel = "game_level";
	
	//game apps packageName����
	private String[] app_game_packages = {"com.dotsnumbers", "com.bwdclock", "com.crazybirdscrushsaga",
			"com.memorypuzzlepro", "com.pandacalculator"};
	
	public boolean bGamePauseMusicOn = false;			//��Ϸ��������֮ͣǰ�Ƿ��Ǵ򿪵�
	
	private TextView mChallengeTimerTv;					// challenge mode��ʱ��timer
	private TextView mGameScoreTv;						// ��Ϸ����textview
	private Button mMenuSettingsBtn;						//	game settings button
	private Button mMenuMusicBtn;							// game music button
	private Button mMenuHelpBtn;							// game help button
	private Button mMenuPauseBtn;							// game pause button
	private Button mToolRefreshBtn;						// tool refresh button
	private TextView mToolRefreshNumTv;				// tool refresh num textview
	private Button mToolBombBtn;							// tool bomb button
	private TextView mToolBombNumTv;					// tool bomb num textview
	private Button mToolHintBtn;								// tool hint button
	private TextView mToolHintNumTv;						// tool hint num textview
	private Button mToolFreezeBtn;							// tool freeze button
	private TextView mToolFreezeNumTv;					// tool freeze num textview
	
	private int mGameMode;									// ��ȡ��Ϸ��ģʽ������ģʽ������սģʽ
	private String mGameSort;									// ��ʾ��������ʾ��theme��food��animal����fruitģʽ
	public int mGameLevel;										// ��Ϸ�Ĺؿ���
	public int mGameMaxLevel;								// ��Ϸ��ɵ����ؿ���
	public int mGameRefreshNum;							// ��Ϸ��fresh����
	public int mGameBombNum;								// ��Ϸ��bomb����
	public int mGameHintNum;								// ��Ϸ��hint����
	public int mGameFreezeNum;							// ��Ϸ��freeze����
	public int mGameScore;									//	��Ϸ�ķ���
	
	// ��Ϸ�������浯����popupwindow
	private HughieGameHelpPopupWindow mGameHelpPopupWindow;
	
	public HughieGameApplication mGameApplication;
	private Context mContext;
	// ������Ϊ��̬������ʼ��Ϊnull�������⵽��ֵΪnull����˵���ǵ�һ�����г��򡣷���̬�����������ϴ����е�����
	private static HughieGameController mGameController = null;
	
	
	public static int mIconsCount = 20;
	public static Bitmap[] mGameIcons = new Bitmap[mIconsCount];
	
	//game food images
	private int[] mGameFoodImages = {R.drawable.imgv_game_main_sort_food1, R.drawable.imgv_game_main_sort_food2, 
		R.drawable.imgv_game_main_sort_food3, R.drawable.imgv_game_main_sort_food4, R.drawable.imgv_game_main_sort_food5, 
		R.drawable.imgv_game_main_sort_food6, R.drawable.imgv_game_main_sort_food7, R.drawable.imgv_game_main_sort_food8,
		R.drawable.imgv_game_main_sort_food9, R.drawable.imgv_game_main_sort_food10, R.drawable.imgv_game_main_sort_food11,
		R.drawable.imgv_game_main_sort_food12, R.drawable.imgv_game_main_sort_food13, R.drawable.imgv_game_main_sort_food14,
		R.drawable.imgv_game_main_sort_food15, R.drawable.imgv_game_main_sort_food16, R.drawable.imgv_game_main_sort_food17,
		R.drawable.imgv_game_main_sort_food18, R.drawable.imgv_game_main_sort_food19, R.drawable.imgv_game_main_sort_food20
	};
	
	//game fruit images
	private int[] mGameFruitImages = {R.drawable.imgv_game_main_sort_fruit1, R.drawable.imgv_game_main_sort_fruit2,
		R.drawable.imgv_game_main_sort_fruit3, R.drawable.imgv_game_main_sort_fruit4, R.drawable.imgv_game_main_sort_fruit5,
		R.drawable.imgv_game_main_sort_fruit6, R.drawable.imgv_game_main_sort_fruit7, R.drawable.imgv_game_main_sort_fruit8,
		R.drawable.imgv_game_main_sort_fruit9, R.drawable.imgv_game_main_sort_fruit10, R.drawable.imgv_game_main_sort_fruit11,
		R.drawable.imgv_game_main_sort_fruit12, R.drawable.imgv_game_main_sort_fruit13, R.drawable.imgv_game_main_sort_fruit14,
		R.drawable.imgv_game_main_sort_fruit15, R.drawable.imgv_game_main_sort_fruit16, R.drawable.imgv_game_main_sort_fruit17,
		R.drawable.imgv_game_main_sort_fruit18, R.drawable.imgv_game_main_sort_fruit19, R.drawable.imgv_game_main_sort_fruit20
	};
	
	//game animal images
	private int[] mGameAnimalImages = {R.drawable.imgv_game_main_sort_animal1, R.drawable.imgv_game_main_sort_animal2,
		R.drawable.imgv_game_main_sort_animal3, R.drawable.imgv_game_main_sort_animal4, R.drawable.imgv_game_main_sort_animal5,
		R.drawable.imgv_game_main_sort_animal6, R.drawable.imgv_game_main_sort_animal7, R.drawable.imgv_game_main_sort_animal8,
		R.drawable.imgv_game_main_sort_animal9, R.drawable.imgv_game_main_sort_animal10, R.drawable.imgv_game_main_sort_animal11,
		R.drawable.imgv_game_main_sort_animal12, R.drawable.imgv_game_main_sort_animal13, R.drawable.imgv_game_main_sort_animal14,
		R.drawable.imgv_game_main_sort_animal15, R.drawable.imgv_game_main_sort_animal16, R.drawable.imgv_game_main_sort_animal17,
		R.drawable.imgv_game_main_sort_animal18, R.drawable.imgv_game_main_sort_animal19, R.drawable.imgv_game_main_sort_animal20
	};
	
	// ������Ϸ�ļ���״̬����
	public static final int TAG_GAME_MAIN_STATE_MENU = 0;								//�˵�
	public static final int TAG_GAME_MAIN_STATE_GAME = 1;								//��Ϸ
	public static final int TAG_GAME_MAIN_STATE_WIN = 2;									//ʤ��
	public static final int TAG_GAME_MAIN_STATE_LOSE = 3;								//ʧ��
	public static final int TAG_GAME_MAIN_STATE_PAUSE = 4;								//��ͣ
	public static final int TAG_GAME_MAIN_STATE_HELP = 5;								//����
	public static final int TAG_GAME_MAIN_STATE_EXIT = 6;									//�˳�
	
	// ������Ϸ�����ļ���״̬
	public static final int TAG_GAME_MAIN_STATUS_FAIL = 1000;							//��Ϸʧ��
	public static final int TAG_GAME_MAIN_STATUS_WIN_STAR1 = 1001;				//ʤ�������һ����
	public static final int TAG_GAME_MAIN_STATUS_WIN_STAR2 = 1002;				//ʤ�������������
	public static final int TAG_GAME_MAIN_STATUS_WIN_STAR3 = 1003;				//ʤ�������������
	
	//����button���tag״̬
	public static final int TAG_BTN_SETTINGS_SHOW = 2000;						
	public static final int TAG_BTN_SETTINGS_UNSHOW = 2001;
	public static final int TAG_BTN_GAME_MUSIC_ON = 2002;
	public static final int TAG_BTN_GAME_MUSIC_OFF = 2003;
	public static final int TAG_BTN_GAME_HELP = 2004;
	public static final int TAG_BTN_GAME_PAUSE = 2005;
	public static final int TAG_BTN_TOOL_REFRESH = 2006;
	public static final int TAG_BTN_TOOL_BOMB = 2007;
	public static final int TAG_BTN_TOOL_HINT = 2008;
	public static final int TAG_BTN_TOOL_FREEZE = 2009;
	
	public static final int TAG_GAME_HELP_STATE_BEGINING = 0;													// ��һ�ؿ�ʼ����Ϸ��������Ϸ�����Ľ���
	public static final int TAG_GAME_HELP_STATE_MENU = 1;														// �����ť�󣬽�����Ϸ�����Ľ���
	
	// ͨ�����úͷ���activity��state�������Ϳ���֪����Ϸ����ʲô״̬
	private static int mGameState = TAG_GAME_MAIN_STATE_MENU;
	
	// ͨ�����úͷ�����Ϸ������mGameFinishStatus�������Ϳ���֪����Ϸ����������һ��״̬
	public static int mGameFinishStatus = TAG_GAME_MAIN_STATUS_FAIL;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		this.mGameApplication = (HughieGameApplication)getApplication();
		this.mContext = this;
		
		loadGameMainData();												// ��ʼ����Ϸ����
		loadGameMainViews(TAG_GAME_MAIN_STATE_GAME);			// ������Ϸ
	}
	
	/**
	 * @title loadGameMainData
	 * @description ��ʼ����Ϸ����
	 * @param
	 * @return 
	 */
	private void loadGameMainData() {
		//��sharedPreference�л�ȡ����
		this.mGameMode = (Integer) getIntent().getSerializableExtra(Extra_GameMode);
		this.mGameSort = sp.getString(HughieSPManager.SP_GameSort, HughieSPManager.SP_GameFood);
		if(mGameMode == 0) {
			this.mGameLevel = (Integer) getIntent().getSerializableExtra(Extra_GameLevel);
			sp.edit().putInt(HughieSPManager.SP_GameLevel, mGameLevel).commit();
			// ���¿�ʼ��Ϸʱ������Ϸ������0
			sp.edit().putInt(HughieSPManager.SP_GameScore, 0).commit();
		}
		
		this.mGameMaxLevel = sp.getInt(HughieSPManager.SP_GameMaxLevel, 1);
		this.mGameRefreshNum = sp.getInt(HughieSPManager.SP_GameRefresh, 1);
		this.mGameBombNum = sp.getInt(HughieSPManager.SP_GameBomb, 3);
		this.mGameHintNum = sp.getInt(HughieSPManager.SP_GameHint, 3);
		this.mGameFreezeNum = sp.getInt(HughieSPManager.SP_GameFreeze, 3);
		this.mGameScore = sp.getInt(HughieSPManager.SP_GameScore, 0);
	}
	
	/**
	 * @title loadGameMainViews
	 * @description ������Ϸ����
	 * @param
	 * @return 
	 */
	private void loadGameMainViews(int gameState) {
		if(mGameController == null) {
			//��Ϸ��controller, ʵ����controller
			mGameController = new HughieGameController();
			loadGameMainIcons();
		}
		
		mGameState = TAG_GAME_MAIN_STATE_GAME;
		setContentView(R.layout.layout_hughie_game_main);
		
		// ��ʼ��Ϸ
		if(gameState == TAG_GAME_MAIN_STATE_GAME) {
			//�ж��Ƿ�Ϊ��һ�أ�����ǵ�һ�أ���ʾhelp��������ǣ�����ʾ
			if(mGameLevel == 1) {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						showGameHelpWindow(TAG_GAME_HELP_STATE_BEGINING);
					}
				}, 300);
			} else {
				mGameController.startGame(HughieGameMainActivity.this, 0);
			} 
		// ��ϷӮ��
		} else if(gameState == TAG_GAME_MAIN_STATE_WIN) {
			ShowGameWinMenu();
//			showGameWinWindow();
		} else if(gameState == TAG_GAME_MAIN_STATE_LOSE) {
			ShowGameFailMenu();
		} else if(gameState == TAG_GAME_MAIN_STATE_PAUSE) {
			ShowGameResumeMenu();
		} else if(gameState == TAG_GAME_MAIN_STATE_HELP) {
			showGameHelpWindow(TAG_GAME_HELP_STATE_MENU);
		}
		
		initGameMainViews();
	}
	
	/**
	 * @title 
	 * @description ������������Ϸ����Ҫ��ͼƬ��Դ�ļ�
	 * 						��һ�ν�����Ϸʱ��Ҫ����ͼƬ�������±�˳�����icons�����У���map[i][j]��ά�����е�ֵ����icons���±�ֵ��һ��
	 * 						��icons���±��Ӧ��ͼƬ�����Ƕ�ά�����б������ֵ-1��Ӧ��ͼƬ
	 * @param 
	 * @return
	 */
	private void loadGameMainIcons() {
		this.mGameSort = sp.getString(HughieSPManager.SP_GameSort, HughieSPManager.SP_GameFood);
		//game foodͼƬ��Դ����
		if(!TextUtils.isEmpty(mGameSort) && mGameSort.equals(HughieSPManager.SP_GameFood)) {
			for(int i = 0; i < mGameFoodImages.length; i++) {
				mGameIcons[i] = ((BitmapDrawable)getResources().getDrawable(mGameFoodImages[i])).getBitmap();
			}
		} else if(!TextUtils.isEmpty(mGameSort) && mGameSort.equals(HughieSPManager.SP_GameFruit)) {
			for(int i = 0; i < mGameFruitImages.length; i++) {
				mGameIcons[i] = ((BitmapDrawable)getResources().getDrawable(mGameFruitImages[i])).getBitmap();
			}
		} else if(!TextUtils.isEmpty(mGameSort) && mGameSort.equals(HughieSPManager.SP_GameAnimal)) {
			for(int i = 0; i < mGameAnimalImages.length; i++) {
				mGameIcons[i] = ((BitmapDrawable)getResources().getDrawable(mGameAnimalImages[i])).getBitmap();
			}
		}
	}
	
	/**
	 * @title initGameMainViews
	 * @description ��ʼ�����ֿؼ�
	 * @param
	 * @return 
	 */
	private void initGameMainViews() {
		// challenge mode textview
		mChallengeTimerTv = (TextView) findViewById(R.id.game_main_function_challenge_timer_tv);
		int mChallengeVisiable = mGameMode == 0 ? View.INVISIBLE : View.VISIBLE;
		mChallengeTimerTv.setVisibility(mChallengeVisiable);
		
		// game score textview
		mGameScoreTv = (TextView) findViewById(R.id.game_main_function_score_tv);
		mGameScoreTv.setTypeface(HughieActivityUtils.getFontType(mContext, "hobostd.otf"));
		mGameScoreTv.setText(String.valueOf(mGameScore));
		
		// game settings button
		mMenuSettingsBtn = (Button) findViewById(R.id.game_main_menu_settings_btn);
		mMenuSettingsBtn.setTag(Integer.valueOf(TAG_BTN_SETTINGS_SHOW));
		mMenuSettingsBtn.setOnClickListener(this);
		
		// game music button
		mMenuMusicBtn = (Button) findViewById(R.id.game_main_menu_music_btn);
		if(sp.getBoolean(HughieSPManager.SP_GameBackMusic, true)){
			mMenuMusicBtn.setBackgroundResource(R.drawable.hughie_btn_game_main_menu_musicon_selector);
			this.mGameApplication.mSoundUtils.playBgGameMusic();
			// play game music
			mMenuMusicBtn.setTag(Integer.valueOf(TAG_BTN_GAME_MUSIC_ON));
			mMenuMusicBtn.setOnClickListener(this);
		}else{
			mMenuMusicBtn.setBackgroundResource(R.drawable.hughie_btn_game_main_menu_musicoff_selector);
			this.mGameApplication.mSoundUtils.stopBgMusic();
			// stop game music
			mMenuMusicBtn.setTag(Integer.valueOf(TAG_BTN_GAME_MUSIC_OFF));
			mMenuMusicBtn.setOnClickListener(this);
		}
		
		// game help button
		mMenuHelpBtn = (Button) findViewById(R.id.game_main_menu_help_btn);
		mMenuHelpBtn.setTag(Integer.valueOf(TAG_BTN_GAME_HELP));
		mMenuHelpBtn.setOnClickListener(this);
		
		//	game pause button
		mMenuPauseBtn = (Button) findViewById(R.id.game_main_menu_pause_btn);
		mMenuPauseBtn.setTag(Integer.valueOf(TAG_BTN_GAME_PAUSE));
		mMenuPauseBtn.setOnClickListener(this);
		
		//	tool refresh button
		mToolRefreshBtn = (Button) findViewById(R.id.game_main_tool_bottom_refresh_btn);
		mToolRefreshBtn.setTag(Integer.valueOf(TAG_BTN_TOOL_REFRESH));
		mToolRefreshBtn.setOnClickListener(this);
		
		// tool refresh num textview
		mToolRefreshNumTv = (TextView) findViewById(R.id.game_main_tool_bottom_refresh_num_tv);
		mToolRefreshNumTv.setTypeface(HughieActivityUtils.getFontType(mContext, "hobostd.otf"));
		mToolRefreshNumTv.setText(String.valueOf(mGameRefreshNum));
		
		// tool bomb button
		mToolBombBtn = (Button) findViewById(R.id.game_main_tool_bottom_bomb_btn);
		mToolBombBtn.setTag(Integer.valueOf(TAG_BTN_TOOL_BOMB));
		mToolBombBtn.setOnClickListener(this);
		
		// tool bomb num textview
		mToolBombNumTv = (TextView) findViewById(R.id.game_main_tool_bottom_bomb_num_tv);
		mToolBombNumTv.setTypeface(HughieActivityUtils.getFontType(mContext, "hobostd.otf"));
		mToolBombNumTv.setText(String.valueOf(mGameBombNum));
		
		// tool hint button
		mToolHintBtn = (Button) findViewById(R.id.game_main_tool_bottom_hint_btn);
		mToolHintBtn.setTag(Integer.valueOf(TAG_BTN_TOOL_HINT));
		mToolHintBtn.setOnClickListener(this);
		
		// tool hint num textview
		mToolHintNumTv = (TextView) findViewById(R.id.game_main_tool_bottom_hint_num_tv);
		mToolHintNumTv.setTypeface(HughieActivityUtils.getFontType(mContext, "hobostd.otf"));
		mToolHintNumTv.setText(String.valueOf(mGameHintNum));
		
		//tool freeze button
		mToolFreezeBtn = (Button) findViewById(R.id.game_main_tool_bottom_freeze_btn);
		mToolFreezeBtn.setTag(Integer.valueOf(TAG_BTN_TOOL_FREEZE));
		mToolFreezeBtn.setOnClickListener(this);
		
		//tool freeze num textview
		mToolFreezeNumTv = (TextView) findViewById(R.id.game_main_tool_bottom_freeze_num_tv);
		mToolFreezeNumTv.setTypeface(HughieActivityUtils.getFontType(mContext, "hobostd.otf"));
		mToolFreezeNumTv.setText(String.valueOf(mGameFreezeNum));
	}
	
	//��Ϸ��ɺ������һ������
	private void ShowGameWinMenu(){
		final AlertDialog dlg_win = new AlertDialog.Builder(this).setCancelable(false).create();
		dlg_win.show();
		Window win_window = dlg_win.getWindow();
		win_window.setContentView(R.layout.game_win_popwindow);
		
		//image win anim
		ImageView imgv_game_win_anim = (ImageView)win_window.findViewById(R.id.imgv_win_popup_anim);
		//image win star1
		ImageView imgv_game_win_star1 = (ImageView)win_window.findViewById(R.id.imgv_win_popup_star1);
		//image win star2
		ImageView imgv_game_win_star2 = (ImageView)win_window.findViewById(R.id.imgv_win_popup_star2);
		//image win star3
		ImageView imgv_game_win_star3 = (ImageView)win_window.findViewById(R.id.imgv_win_popup_star3);
		//button win replay
		Button btn_game_win_replay = (Button)win_window.findViewById(R.id.btn_win_popup_replay);
		//button win next
		Button btn_game_win_next = (Button)win_window.findViewById(R.id.btn_win_popup_next);
		
		//�жϹؿ�����ɵ����ؿ�
		this.mGameLevel = sp.getInt(HughieSPManager.SP_GameLevel, 1);
		mGameMaxLevel = sp.getInt(HughieSPManager.SP_GameMaxLevel, 1);
		// ������ؿ�С�ڵ�ǰ�ؿ�����ǰ�ؿ��������ؿ�
		if(mGameMaxLevel < mGameLevel)
			mGameMaxLevel = mGameLevel;
		
		// ����ؿ��Լ�����״̬���������
		sp.edit().putInt(HughieSPManager.SP_GameMaxLevel, 
				mGameMaxLevel == mGameLevel ? mGameMaxLevel + 1 : mGameMaxLevel).commit();
		sp.edit().putInt(HughieSPManager.SP_GameFinishStatus + mGameLevel, mGameFinishStatus).commit();
		
		mGameScore = sp.getInt(HughieSPManager.SP_GameScore, 0);
		mGameScoreTv.setText(String.valueOf(mGameScore));
		
		LinearLayout game_win_popupwindow = (LinearLayout)win_window.findViewById(R.id.game_win_popupwindow);
		LayoutParams sq_layout_win_para = game_win_popupwindow.getLayoutParams();
		sq_layout_win_para.width = mGameApplication.displayWidth - 5;
		sq_layout_win_para.height = mGameApplication.displayHeight - 5;
		game_win_popupwindow.setLayoutParams(sq_layout_win_para);
		mGameApplication.mSoundUtils.playGameSoundByid(3, 0);
		
		//����image win anim����
		imgv_game_win_anim.setBackgroundResource(R.anim.hughie_anim_game_win_popupwindow);
		final AnimationDrawable win_animationDrawable = (AnimationDrawable)imgv_game_win_anim.getBackground();
		imgv_game_win_anim.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			boolean mWinFirst = true;
			@Override
			public boolean onPreDraw() {
				// TODO Auto-generated method stub
				if(this.mWinFirst){
					win_animationDrawable.start();
					this.mWinFirst = false;
				}
				
				return true;
			}
		});
		
		TranslateAnimation win_translateAnimation = new TranslateAnimation(-400.0F, 0.0F, 0.0F, 0.0F);
		win_translateAnimation.setDuration(1000L);
		imgv_game_win_anim.startAnimation(win_translateAnimation);
		
		//������Ϸ�����״̬
		if(mGameFinishStatus == TAG_GAME_MAIN_STATUS_WIN_STAR1){
			imgv_game_win_star1.setBackgroundResource(R.drawable.imgv_game_main_win_star_checked);
			imgv_game_win_star2.setBackgroundResource(R.drawable.imgv_game_main_win_star_unchecked);
			imgv_game_win_star3.setBackgroundResource(R.drawable.imgv_game_main_win_star_unchecked);
		}else if(mGameFinishStatus == TAG_GAME_MAIN_STATUS_WIN_STAR2){
			imgv_game_win_star1.setBackgroundResource(R.drawable.imgv_game_main_win_star_checked);
			imgv_game_win_star2.setBackgroundResource(R.drawable.imgv_game_main_win_star_checked);
			imgv_game_win_star3.setBackgroundResource(R.drawable.imgv_game_main_win_star_unchecked);
		}else if(mGameFinishStatus == TAG_GAME_MAIN_STATUS_WIN_STAR3){
			imgv_game_win_star1.setBackgroundResource(R.drawable.imgv_game_main_win_star_checked);
			imgv_game_win_star2.setBackgroundResource(R.drawable.imgv_game_main_win_star_checked);
			imgv_game_win_star3.setBackgroundResource(R.drawable.imgv_game_main_win_star_checked);
		}
		
		//game win replay button������
		btn_game_win_replay.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mGameApplication.mSoundUtils.playGameSoundByid(0, 0);
				dlg_win.cancel();
				// ������replay���½�����Ϸ���򱣳ֵ�ǰ�Ĺؿ�����mGameLevel��mGameMaxLevel���ݱ�����sharepreference
				sp.edit().putInt(HughieSPManager.SP_GameLevel, mGameLevel).commit();
				// ��mGameScore���㲢����
				sp.edit().putInt(HughieSPManager.SP_GameScore, 0).commit();
				
				// ��Ϸ��ʼǰ����ȡsharepreference�еĹؿ�����
				mGameLevel = sp.getInt(HughieSPManager.SP_GameLevel, 1);
				mGameMaxLevel = sp.getInt(HughieSPManager.SP_GameMaxLevel, 1);
				
				// ��Ϸ��ʼǰ����ȡsharepreference�еķ�������
				mGameScore = sp.getInt(HughieSPManager.SP_GameScore, 0);
				mGameScoreTv.setText(String.valueOf(mGameScore));
				
				loadGameMainViews(TAG_GAME_MAIN_STATE_GAME);
			}
		});
		
		//game win next button������ 
		btn_game_win_next.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mGameApplication.mSoundUtils.playGameSoundByid(0, 0);
				dlg_win.cancel();
				//������next������һ�أ��򽫹ؿ��Զ���1������
				//��iLevel��iMaxLevel���ݱ�����sharepreference
				sp.edit().putInt(HughieSPManager.SP_GameLevel, mGameLevel + 1).commit();
				//��game score���㲢����
				sp.edit().putInt(HughieSPManager.SP_GameScore, 0).commit();
				
				int n = 1;
				//������һ�ؿ�ʼǰ����ȡsharepreference�еĹؿ�����
				mGameLevel = sp.getInt(HughieSPManager.SP_GameLevel, 1);
				mGameMaxLevel = sp.getInt(HughieSPManager.SP_GameMaxLevel, 1);
				
				//������Ϸ��ʼǰ����ȡsharepreference�еķ�������
				mGameScore = sp.getInt(HughieSPManager.SP_GameScore, 0);
				mGameScoreTv.setText(String.valueOf(mGameScore));
				mGameController.startGame(HughieGameMainActivity.this, n);
			}
		});
	}
	
	//��Ϸʧ�ܺ���ʾ�Ĳ���
	private void ShowGameFailMenu(){
		final AlertDialog dlg_fail = new AlertDialog.Builder(this).setCancelable(false).create();
		dlg_fail.show();
		Window fail_window = dlg_fail.getWindow();
		fail_window.setContentView(R.layout.game_fail_popwindow);
		
		//image fail anim
		ImageView imgv_game_fail_anim = (ImageView)fail_window.findViewById(R.id.imgv_fail_popup_anim);
		//image fail star1
		ImageView imgv_game_fail_star1 = (ImageView)fail_window.findViewById(R.id.imgv_fail_popup_star1);
		//image fail star2
		ImageView imgv_game_fail_star2 = (ImageView)fail_window.findViewById(R.id.imgv_fail_popup_star2);
		//image fail star3
		ImageView imgv_game_fail_star3 = (ImageView)fail_window.findViewById(R.id.imgv_fail_popup_star3);
		//button fail replay
		Button btn_game_fail_replay = (Button)fail_window.findViewById(R.id.btn_fail_popup_replay);
		//button fail menu
		Button btn_game_fail_menu = (Button)fail_window.findViewById(R.id.btn_fail_popup_menu);
		
		mGameScore = sp.getInt(HughieSPManager.SP_GameScore, 0);
		mGameScoreTv.setText(String.valueOf(this.mGameScore));
		
		LinearLayout game_fail_popupwindow = (LinearLayout)fail_window.findViewById(R.id.game_fail_popupwindow);
		LayoutParams sq_layout_fail_para = game_fail_popupwindow.getLayoutParams();
		sq_layout_fail_para.width = mGameApplication.displayWidth - 5;
		sq_layout_fail_para.height = mGameApplication.displayHeight - 5;
		game_fail_popupwindow.setLayoutParams(sq_layout_fail_para);
		mGameApplication.mSoundUtils.playGameSoundByid(2, 0);
		
		//����image fail anim����
		imgv_game_fail_anim.setBackgroundResource(R.anim.hughie_anim_game_lose_popupwindow);
		final AnimationDrawable fail_animationDrawable = (AnimationDrawable)imgv_game_fail_anim.getBackground();
		imgv_game_fail_anim.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			boolean mFailFirst = true;
			@Override
			public boolean onPreDraw() {
				// TODO Auto-generated method stub
				if(this.mFailFirst){
					fail_animationDrawable.start();
					this.mFailFirst = false;
				}
				
				return true;
			}
		});
		
		TranslateAnimation fail_translateAnimation = new TranslateAnimation(-400.0F, 0.0F, 0.0F, 0.0F);
		fail_translateAnimation.setDuration(1000L);
		imgv_game_fail_anim.setAnimation(fail_translateAnimation);
		
		//������Ϸ�����״̬
		imgv_game_fail_star1.setBackgroundResource(R.drawable.imgv_game_main_win_star_unchecked);
		imgv_game_fail_star2.setBackgroundResource(R.drawable.imgv_game_main_win_star_unchecked);
		imgv_game_fail_star3.setBackgroundResource(R.drawable.imgv_game_main_win_star_unchecked);
		
		//game fail replay button������
		btn_game_fail_replay.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mGameApplication.mSoundUtils.playGameSoundByid(0, 0);
				dlg_fail.cancel();
				//��game score���㲢����
				sp.edit().putInt(HughieSPManager.SP_GameScore, 0).commit();
				//������Ϸ��ʼǰ����ȡsharepreference�еķ�������
				mGameScore = sp.getInt(HughieSPManager.SP_GameScore, 0);
				mGameScoreTv.setText(String.valueOf(mGameScore));
				
				loadGameMainViews(TAG_GAME_MAIN_STATE_GAME);
			}
		});
		
		//game fail menu button������ 
		btn_game_fail_menu.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mGameApplication.mSoundUtils.playGameSoundByid(0, 0);
				dlg_fail.cancel();
				//��game score���㲢����
				sp.edit().putInt(HughieSPManager.SP_GameScore, 0).commit();
				//������Ϸ��ʼǰ����ȡsharepreference�еķ�������
				mGameScore = sp.getInt(HughieSPManager.SP_GameScore, 0);
				mGameScoreTv.setText(String.valueOf(mGameScore));
				finish();
			}
		});
	}
	
	//game��ͣ����ʾ�Ĳ���
	private void ShowGameResumeMenu(){
		final AlertDialog dlg_resume = new AlertDialog.Builder(this).setCancelable(false).create();
		dlg_resume.show();
		Window resume_window = dlg_resume.getWindow();
		resume_window.setContentView(R.layout.game_pause_popwindow);
		
		if(sp.getBoolean(HughieSPManager.SP_GameBackMusic, true)) {
			this.bGamePauseMusicOn = true;
			sp.edit().putBoolean(HughieSPManager.SP_GameBackMusic, false).commit();
			mGameApplication.mSoundUtils.stopBgMusic();
			mMenuMusicBtn.setBackgroundResource(R.drawable.hughie_btn_game_main_menu_musicoff_selector);
			mMenuMusicBtn.setTag(Integer.valueOf(TAG_BTN_GAME_MUSIC_OFF));
		}
		
		//button pause app1
		Button btn_game_pause_app1 = (Button)resume_window.findViewById(R.id.btn_game_pause_app1);
		//button pause app2
		Button btn_game_pause_app2 = (Button)resume_window.findViewById(R.id.btn_game_pause_app2);
		//button pause app3
		Button btn_game_pause_app3 = (Button)resume_window.findViewById(R.id.btn_game_pause_app3);
		//button pause app4
		Button btn_game_pause_app4 = (Button)resume_window.findViewById(R.id.btn_game_pause_app4);
		//button pause app5
		Button btn_game_pause_app5 = (Button)resume_window.findViewById(R.id.btn_game_pause_app5);
		//button pause resume
		Button btn_game_pause_resume = (Button)resume_window.findViewById(R.id.btn_game_pause_resume);
		//button pause replay
		Button btn_game_pause_replay = (Button)resume_window.findViewById(R.id.btn_game_pause_replay);
		//button pause menu
		Button btn_game_pause_menu = (Button)resume_window.findViewById(R.id.btn_game_pause_menu);
		
		LinearLayout game_pause_popupwindow = (LinearLayout)resume_window.findViewById(R.id.game_pause_popupwindow);
		LayoutParams sq_layout_pause_para = game_pause_popupwindow.getLayoutParams();
		sq_layout_pause_para.width = mGameApplication.displayWidth - 5;
		sq_layout_pause_para.height = mGameApplication.displayHeight - 5;
		game_pause_popupwindow.setLayoutParams(sq_layout_pause_para);
		
		//����app tween����
		Animation animation_game_app = AnimationUtils.loadAnimation(HughieGameMainActivity.this, R.anim.hughie_anim_game_main_apps);
		btn_game_pause_app1.setAnimation(animation_game_app);
		btn_game_pause_app2.setAnimation(animation_game_app);
		btn_game_pause_app3.setAnimation(animation_game_app);
		btn_game_pause_app4.setAnimation(animation_game_app);
		btn_game_pause_app5.setAnimation(animation_game_app);
		
		if(isRunGameApp(app_game_packages[0]))
			btn_game_pause_app1.setVisibility(View.GONE);
		if(isRunGameApp(app_game_packages[1]))
			btn_game_pause_app2.setVisibility(View.GONE);
		if(isRunGameApp(app_game_packages[2]))
			btn_game_pause_app3.setVisibility(View.GONE);
		if(isRunGameApp(app_game_packages[3]))
			btn_game_pause_app4.setVisibility(View.GONE);
		if(isRunGameApp(app_game_packages[4]))
			btn_game_pause_app5.setVisibility(View.GONE);
		
		//button pause app1����
		btn_game_pause_app1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mGameApplication.mSoundUtils.playGameSoundByid(0, 0);
				String game_link_app1 = "market://details?id=" + app_game_packages[0];
				Uri game_uri_app1 = Uri.parse(game_link_app1);
				Intent intent_game_app1 = new Intent(Intent.ACTION_VIEW, game_uri_app1);
				startActivity(intent_game_app1);
			}
		});
		
		//button pause app2
		btn_game_pause_app2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mGameApplication.mSoundUtils.playGameSoundByid(0, 0);
				String game_link_app2 = "market://details?id=" + app_game_packages[1];
				Uri game_uri_app2 = Uri.parse(game_link_app2);
				Intent intent_game_app2 = new Intent(Intent.ACTION_VIEW, game_uri_app2);
				startActivity(intent_game_app2);
			}
		});
		
		//button pause app3
		btn_game_pause_app3.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mGameApplication.mSoundUtils.playGameSoundByid(0, 0);
				String game_link_app3 = "market://details?id=" + app_game_packages[2];
				Uri game_uri_app3 = Uri.parse(game_link_app3);
				Intent intent_game_app3 = new Intent(Intent.ACTION_VIEW, game_uri_app3);
				startActivity(intent_game_app3);
			}
		});
		
		//button pause app4
		btn_game_pause_app4.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mGameApplication.mSoundUtils.playGameSoundByid(0, 0);
				String game_link_app4 = "market://details?id=" + app_game_packages[3];
				Uri game_uri_app4 = Uri.parse(game_link_app4);
				Intent intent_game_app4 = new Intent(Intent.ACTION_VIEW, game_uri_app4);
				startActivity(intent_game_app4);
			}
		});
		
		//button pause app5
		btn_game_pause_app5.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mGameApplication.mSoundUtils.playGameSoundByid(0, 0);
				String game_link_app5 = "market://details?id=" + app_game_packages[4];
				Uri game_uri_app5 = Uri.parse(game_link_app5);
				Intent intent_game_app5 = new Intent(Intent.ACTION_VIEW, game_uri_app5);
				startActivity(intent_game_app5);
			}
		});
		
		//button pause resume
		btn_game_pause_resume.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mGameApplication.mSoundUtils.playGameSoundByid(0, 0);
				dlg_resume.cancel();
				
				if(bGamePauseMusicOn == true && sp.getBoolean(HughieSPManager.SP_GameBackMusic, true) == false){
					bGamePauseMusicOn = false;
					sp.edit().putBoolean(HughieSPManager.SP_GameBackMusic, true).commit();
					mGameApplication.mSoundUtils.playBgGameMusic();
					mMenuMusicBtn.setBackgroundResource(R.drawable.hughie_btn_game_main_menu_musicon_selector);
					mMenuMusicBtn.setTag(Integer.valueOf(TAG_BTN_GAME_MUSIC_ON));
				}
				
				mGameController.resumeGame(HughieGameMainActivity.this);
			}
		});
		
		//button pause replay
		btn_game_pause_replay.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mGameApplication.mSoundUtils.playGameSoundByid(0, 0);
				dlg_resume.cancel();
				
				if(bGamePauseMusicOn == true && sp.getBoolean(HughieSPManager.SP_GameBackMusic, true) == false){
					bGamePauseMusicOn = false;
					sp.edit().putBoolean(HughieSPManager.SP_GameBackMusic, true).commit();
					mGameApplication.mSoundUtils.playBgGameMusic();
					mMenuMusicBtn.setBackgroundResource(R.drawable.hughie_btn_game_main_menu_musicon_selector);
					mMenuMusicBtn.setTag(Integer.valueOf(TAG_BTN_GAME_MUSIC_ON));
				}
				
				//��game score���㲢����
				sp.edit().putInt(HughieSPManager.SP_GameScore, 0).commit();
				//������Ϸ��ʼǰ����ȡsharepreference�еķ�������
				mGameScore = sp.getInt(HughieSPManager.SP_GameScore, 0);
				mGameScoreTv.setText(String.valueOf(mGameScore));
				
				loadGameMainViews(TAG_GAME_MAIN_STATE_GAME);
			}
		});
		
		//btn pause menu
		btn_game_pause_menu.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mGameApplication.mSoundUtils.playGameSoundByid(0, 0);
				dlg_resume.cancel();
				
				if(bGamePauseMusicOn == true && sp.getBoolean(HughieSPManager.SP_GameBackMusic, true) == false){
					bGamePauseMusicOn = false;
					sp.edit().putBoolean(HughieSPManager.SP_GameBackMusic, true).commit();
					mGameApplication.mSoundUtils.playBgGameMusic();
					mMenuMusicBtn.setBackgroundResource(R.drawable.hughie_btn_game_main_menu_musicon_selector);
					mMenuMusicBtn.setTag(Integer.valueOf(TAG_BTN_GAME_MUSIC_ON));
				}
				
				//��game score���㲢����
				sp.edit().putInt(HughieSPManager.SP_GameScore, 0).commit();
				//������Ϸ��ʼǰ����ȡsharepreference�еķ�������
				mGameScore = sp.getInt(HughieSPManager.SP_GameScore, 0);
				mGameScoreTv.setText(String.valueOf(mGameScore));
				
				finish();
			}
		});
	}
	
	/**
	 * @title showGameHelpWindow
	 * @description ��Ϸ�İ�������
	 * @param
	 * @return 
	 */
	private void showGameHelpWindow(int gameHelpState) {
		if(mGameHelpPopupWindow == null) {
			mGameHelpPopupWindow = new HughieGameHelpPopupWindow(mContext, mGameApplication);
			mGameHelpPopupWindow.setIGameHelpPopupWindowListener(new IGameHelpPopupWindowListener() {
				@Override
				public void onGameHelpCloseClick(int helpState) {
					if(mGameLevel == 1 && helpState == TAG_GAME_HELP_STATE_BEGINING) {
						mGameHelpPopupWindow.dismiss();
						mGameHelpPopupWindow.clearGameArrowAnimation();
						mGameController.startGame(HughieGameMainActivity.this, 0);
					} else {
						mGameHelpPopupWindow.dismiss();
						mGameHelpPopupWindow.clearGameArrowAnimation();
						mGameController.resumeGame(HughieGameMainActivity.this);
					}
				}
			});
		}
		mGameHelpPopupWindow.setHelpState(gameHelpState);
		mGameHelpPopupWindow.showAtLocation(((Activity)mContext).findViewById(R.id.game_main_layout), Gravity.TOP, 0, 0);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		AudioManager audioManager = null;
		audioManager = (AudioManager)getSystemService(Service.AUDIO_SERVICE);
		
		if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
			audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, 
					AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
			return true;
		}else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
			audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, 
					AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
			return true;
		}else if(keyCode == KeyEvent.KEYCODE_BACK){
			mGameApplication.mSoundUtils.playGameSoundByid(0, 0);
			HughieGameMainActivity.this.finish();
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}
	
	/**
	 * @title setGameState
	 * @description ������Ϸ������״̬����������ʾ����
	 * @param gameState����Ϸ������״̬
	 * @return
	 */
	@SuppressWarnings("static-access")
	public void setGameState(int gameState){
		this.mGameState = gameState;
		if(gameState == TAG_GAME_MAIN_STATE_WIN){
			loadGameMainViews(TAG_GAME_MAIN_STATE_WIN);
		}else if(gameState == TAG_GAME_MAIN_STATE_LOSE){
			loadGameMainViews(TAG_GAME_MAIN_STATE_LOSE);
		}
	}
	
	/**
	 * @title setGameHelpState
	 * @description �����ʾ����ҳ���Ĳ���
	 * @param
	 * @return
	 */
	public void setGameHelpState() {
		// ��ͣ��Ϸ
		mGameController.setGamePause();
		mGameState = TAG_GAME_MAIN_STATE_HELP;
		loadGameMainViews(TAG_GAME_MAIN_STATE_HELP);
	}
	
	/**
	 * @title setGamePauseState
	 * @description ��Ϸ�ݶ�
	 * @param
	 * @return
	 */
	public void setGamePauseState() {
		// ��ͣ��Ϸ
		mGameController.setGamePause();
		mGameState = TAG_GAME_MAIN_STATE_PAUSE;
		loadGameMainViews(TAG_GAME_MAIN_STATE_PAUSE);
	}
	
	/**
	 * @title showGameSettings
	 * @description ��ʾ��չ��game settings
	 * @param
	 * @return
	 */
	public void showGameSettings() {
		// ����game music button��ʾ��translateAnimation
		TranslateAnimation mShowMusicTranslateAnimation = new TranslateAnimation(45.0F * mGameApplication.density, 0.0F, 
				-40.0F * mGameApplication.density, 0.0F);
		mShowMusicTranslateAnimation.setDuration(300L);
		mMenuMusicBtn.setVisibility(View.VISIBLE);
		mMenuMusicBtn.startAnimation(mShowMusicTranslateAnimation);
		
		// ����game help button��ʾ��translateAnimation
		TranslateAnimation mShowHelpTranslateAnimation = new TranslateAnimation(0.0F, 0.0F, -40.0F * mGameApplication.density, 
				0.0F);
		mShowHelpTranslateAnimation.setDuration(300L);
		mMenuHelpBtn.setVisibility(View.VISIBLE);
		mMenuHelpBtn.startAnimation(mShowHelpTranslateAnimation);
		
		// ����game pause button��ʾ��translateAnimation
		TranslateAnimation mShowPauseTranslateAnimation = new TranslateAnimation(-45.0F * mGameApplication.density, 0.0F, 
				-40.0F * mGameApplication.density, 0.0F);
		mShowPauseTranslateAnimation.setDuration(300L);
		mMenuPauseBtn.setVisibility(View.VISIBLE);
		mMenuPauseBtn.startAnimation(mShowPauseTranslateAnimation);
	}
	
	/**
	 * @title unShowGameSettings
	 * @description ���ز���£game settings
	 * @param
	 * @return
	 */
	public void unShowGameSettings() {
		// ����game music button���ص�translateAnimation
		TranslateAnimation mUnshowMusicTranslateAnimation = new TranslateAnimation(0.0F, 45.0F * mGameApplication.density, 
				0.0F, -40.0F * mGameApplication.density);
		mUnshowMusicTranslateAnimation.setDuration(300L);
		mMenuMusicBtn.setVisibility(View.GONE);
		mMenuMusicBtn.startAnimation(mUnshowMusicTranslateAnimation);
		
		// ����game help button���ص�translateAnimation
		TranslateAnimation mUnshowHelpTranslateAnimation = new TranslateAnimation(0.0F, 0.0F, 0.0F, 
				-40.0F * mGameApplication.density);
		mUnshowHelpTranslateAnimation.setDuration(300L);
		mMenuHelpBtn.setVisibility(View.GONE);
		mMenuHelpBtn.startAnimation(mUnshowHelpTranslateAnimation);
		
		// ����game pause button���ص�translateAnimation
		TranslateAnimation mUnshowPauseTranslateAnimation = new TranslateAnimation(0.0F, -45.0F * mGameApplication.density, 
				0.0F, -40.0F * mGameApplication.density);
		mUnshowPauseTranslateAnimation.setDuration(300L);
		mMenuPauseBtn.setVisibility(View.GONE);
		mMenuPauseBtn.startAnimation(mUnshowPauseTranslateAnimation);
	}
	
	//�ж�game apps packageName�Ƿ��Ѿ�����
	private boolean isRunGameApp(String packageName){
		PackageInfo pi;
		try{
			pi = getPackageManager().getPackageInfo(packageName, 0);
			Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
			resolveIntent.setPackage(packageName);
			PackageManager pManager = getPackageManager();
			List<ResolveInfo> apps = pManager.queryIntentActivities(resolveIntent, 0);
			
			ResolveInfo ri = apps.iterator().next();
			if(ri != null){
				return true;
			}else{
				return false;
			}
		}catch(NameNotFoundException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	//�����Ϸ�а���home���������������л�����������activity�У���Ҫ�Զ���ͣ��Ϸ���ٴν����ǿ��Լ�����
	public void onPause(){
		super.onPause();
		this.mGameApplication.mSoundUtils.stopBgMusic();
		if(mGameState == TAG_GAME_MAIN_STATE_GAME){
			mGameState = TAG_GAME_MAIN_STATE_PAUSE;
			// ����Ϸ��ͣ����������Ҫ���еĴ�����
			mGameController.setGamePause();
		}
	}
	
	//�����Ϸ�а���home���������������л�����������activity�У���Ҫ�Զ���ͣ��Ϸ���ٴν����ǿ��Լ�����
	@Override
	public void onResume(){
		super.onResume();
		this.mGameApplication.mSoundUtils.playBgGameMusic();
		if(mGameState == TAG_GAME_MAIN_STATE_PAUSE){
			mGameState = TAG_GAME_MAIN_STATE_GAME;
			mGameScore = sp.getInt(HughieSPManager.SP_GameScore, 0);
			setContentView(R.layout.layout_hughie_game_main);
			mGameController.resumeGame(HughieGameMainActivity.this);
			initGameMainViews();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(mGameController != null){
			mGameController = null;
		}
		Log.d("++++hughiezhang WeAndroids++++", "Destroy====================================");
	}

	@Override
	public void onClick(View gameView) {
		// TODO Auto-generated method stub
		this.mGameApplication.mSoundUtils.playGameSoundByid(0, 0);
		
		switch(((Integer)gameView.getTag()).intValue()){
			//btn setting click, show game setting view
			case TAG_BTN_SETTINGS_SHOW:
				showGameSettings();
				gameView.setTag(Integer.valueOf(TAG_BTN_SETTINGS_UNSHOW));
				break;
			//btn setting click, unshow game setting view
			case TAG_BTN_SETTINGS_UNSHOW:
				unShowGameSettings();
				gameView.setTag(Integer.valueOf(TAG_BTN_SETTINGS_SHOW));
				break;
			//btn music click to set game music on
			case TAG_BTN_GAME_MUSIC_ON:
				sp.edit().putBoolean(HughieSPManager.SP_GameBackMusic, false).commit();
				mMenuMusicBtn.setBackgroundResource(R.drawable.hughie_btn_game_main_menu_musicoff_selector);
				mMenuMusicBtn.setTag(Integer.valueOf(TAG_BTN_GAME_MUSIC_OFF));
				this.mGameApplication.mSoundUtils.stopBgMusic();
				break;
			//btn music click to set game music off
			case TAG_BTN_GAME_MUSIC_OFF:
				sp.edit().putBoolean(HughieSPManager.SP_GameBackMusic, true).commit();
				mMenuMusicBtn.setBackgroundResource(R.drawable.hughie_btn_game_main_menu_musicon_selector);
				mMenuMusicBtn.setTag(Integer.valueOf(TAG_BTN_GAME_MUSIC_ON));
				this.mGameApplication.mSoundUtils.playBgGameMusic();
				break;
			
			//btn help click to show game help
			case TAG_BTN_GAME_HELP:
				setGameHelpState();
				break;
			//btn pause click to set game paused
			case TAG_BTN_GAME_PAUSE:
				setGamePauseState();
				break;
			//tool fresh button click
			case TAG_BTN_TOOL_REFRESH:
				mGameController.autoRefresh();
				break;
			//tool boom button click
			case TAG_BTN_TOOL_BOMB:
				mGameController.autoBoom();
				break;
			//tool hint button click
			case TAG_BTN_TOOL_HINT:
				mGameController.autoHint();
				break;
			//tool freeze button click
			case TAG_BTN_TOOL_FREEZE:
				mGameController.autoFreeze();
				break;
		}
	}
}

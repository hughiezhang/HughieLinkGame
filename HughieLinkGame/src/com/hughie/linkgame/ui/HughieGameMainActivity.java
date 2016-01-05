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
 * 游戏主界面
 * @ClassName: HughieGameMainActivity
 * @author hughiezhang
 * @since 2015-10-14 10:07
 */
public class HughieGameMainActivity extends HughieBaseActivity implements
		OnClickListener {
	
	public static final String Extra_GameMode = "game_mode";
	public static final String Extra_GameLevel = "game_level";
	
	//game apps packageName数组
	private String[] app_game_packages = {"com.dotsnumbers", "com.bwdclock", "com.crazybirdscrushsaga",
			"com.memorypuzzlepro", "com.pandacalculator"};
	
	public boolean bGamePauseMusicOn = false;			//游戏音乐在暂停之前是否是打开的
	
	private TextView mChallengeTimerTv;					// challenge mode的时间timer
	private TextView mGameScoreTv;						// 游戏分数textview
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
	
	private int mGameMode;									// 获取游戏的模式，剧情模式还是挑战模式
	private String mGameSort;									// 显示连连看显示的theme，food，animal还是fruit模式
	public int mGameLevel;										// 游戏的关卡数
	public int mGameMaxLevel;								// 游戏完成的最大关卡数
	public int mGameRefreshNum;							// 游戏的fresh数量
	public int mGameBombNum;								// 游戏的bomb数量
	public int mGameHintNum;								// 游戏的hint数量
	public int mGameFreezeNum;							// 游戏的freeze数量
	public int mGameScore;									//	游戏的分数
	
	// 游戏帮助界面弹出的popupwindow
	private HughieGameHelpPopupWindow mGameHelpPopupWindow;
	
	public HughieGameApplication mGameApplication;
	private Context mContext;
	// 控制器为静态变量初始化为null，如果检测到其值为null，则说明是第一次运行程序。否则静态变量将保存上次运行的数据
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
	
	// 定义游戏的几种状态变量
	public static final int TAG_GAME_MAIN_STATE_MENU = 0;								//菜单
	public static final int TAG_GAME_MAIN_STATE_GAME = 1;								//游戏
	public static final int TAG_GAME_MAIN_STATE_WIN = 2;									//胜利
	public static final int TAG_GAME_MAIN_STATE_LOSE = 3;								//失败
	public static final int TAG_GAME_MAIN_STATE_PAUSE = 4;								//暂停
	public static final int TAG_GAME_MAIN_STATE_HELP = 5;								//帮助
	public static final int TAG_GAME_MAIN_STATE_EXIT = 6;									//退出
	
	// 定义游戏结束的几种状态
	public static final int TAG_GAME_MAIN_STATUS_FAIL = 1000;							//游戏失败
	public static final int TAG_GAME_MAIN_STATUS_WIN_STAR1 = 1001;				//胜利并获得一颗星
	public static final int TAG_GAME_MAIN_STATUS_WIN_STAR2 = 1002;				//胜利并获得两颗星
	public static final int TAG_GAME_MAIN_STATUS_WIN_STAR3 = 1003;				//胜利并获得三颗星
	
	//定义button点击tag状态
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
	
	public static final int TAG_GAME_HELP_STATE_BEGINING = 0;													// 第一关开始打开游戏，进入游戏帮助的界面
	public static final int TAG_GAME_HELP_STATE_MENU = 1;														// 点击按钮后，进入游戏帮助的界面
	
	// 通过设置和访问activity的state变量，就可以知道游戏处于什么状态
	private static int mGameState = TAG_GAME_MAIN_STATE_MENU;
	
	// 通过设置和访问游戏结束的mGameFinishStatus变量，就可以知道游戏结束后处于哪一种状态
	public static int mGameFinishStatus = TAG_GAME_MAIN_STATUS_FAIL;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		this.mGameApplication = (HughieGameApplication)getApplication();
		this.mContext = this;
		
		loadGameMainData();												// 初始化游戏数据
		loadGameMainViews(TAG_GAME_MAIN_STATE_GAME);			// 加载游戏
	}
	
	/**
	 * @title loadGameMainData
	 * @description 初始化游戏数据
	 * @param
	 * @return 
	 */
	private void loadGameMainData() {
		//从sharedPreference中获取数据
		this.mGameMode = (Integer) getIntent().getSerializableExtra(Extra_GameMode);
		this.mGameSort = sp.getString(HughieSPManager.SP_GameSort, HughieSPManager.SP_GameFood);
		if(mGameMode == 0) {
			this.mGameLevel = (Integer) getIntent().getSerializableExtra(Extra_GameLevel);
			sp.edit().putInt(HughieSPManager.SP_GameLevel, mGameLevel).commit();
			// 重新开始游戏时，将游戏分数置0
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
	 * @description 加载游戏界面
	 * @param
	 * @return 
	 */
	private void loadGameMainViews(int gameState) {
		if(mGameController == null) {
			//游戏的controller, 实例化controller
			mGameController = new HughieGameController();
			loadGameMainIcons();
		}
		
		mGameState = TAG_GAME_MAIN_STATE_GAME;
		setContentView(R.layout.layout_hughie_game_main);
		
		// 开始游戏
		if(gameState == TAG_GAME_MAIN_STATE_GAME) {
			//判断是否为第一关，如果是第一关，显示help，如果不是，则不显示
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
		// 游戏赢局
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
	 * @description 加载连连看游戏所需要的图片资源文件
	 * 						第一次进行游戏时需要导入图片，并按下标顺序放入icons数组中，而map[i][j]二维数组中的值就是icons的下标值加一。
	 * 						即icons的下标对应的图片，就是二维数组中保存的数值-1对应的图片
	 * @param 
	 * @return
	 */
	private void loadGameMainIcons() {
		this.mGameSort = sp.getString(HughieSPManager.SP_GameSort, HughieSPManager.SP_GameFood);
		//game food图片资源加载
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
	 * @description 初始化布局控件
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
	
	//游戏完成后进行下一步操作
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
		
		//判断关卡和完成的最大关卡
		this.mGameLevel = sp.getInt(HughieSPManager.SP_GameLevel, 1);
		mGameMaxLevel = sp.getInt(HughieSPManager.SP_GameMaxLevel, 1);
		// 如果最大关卡小于当前关卡，则当前关卡就是最大关卡
		if(mGameMaxLevel < mGameLevel)
			mGameMaxLevel = mGameLevel;
		
		// 保存关卡以及结束状态等相关数据
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
		
		//设置image win anim动画
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
		
		//设置游戏的完成状态
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
		
		//game win replay button监听器
		btn_game_win_replay.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mGameApplication.mSoundUtils.playGameSoundByid(0, 0);
				dlg_win.cancel();
				// 如果点击replay重新进行游戏，则保持当前的关卡，将mGameLevel和mGameMaxLevel数据保存至sharepreference
				sp.edit().putInt(HughieSPManager.SP_GameLevel, mGameLevel).commit();
				// 将mGameScore清零并保存
				sp.edit().putInt(HughieSPManager.SP_GameScore, 0).commit();
				
				// 游戏开始前，获取sharepreference中的关卡数据
				mGameLevel = sp.getInt(HughieSPManager.SP_GameLevel, 1);
				mGameMaxLevel = sp.getInt(HughieSPManager.SP_GameMaxLevel, 1);
				
				// 游戏开始前，获取sharepreference中的分数数据
				mGameScore = sp.getInt(HughieSPManager.SP_GameScore, 0);
				mGameScoreTv.setText(String.valueOf(mGameScore));
				
				loadGameMainViews(TAG_GAME_MAIN_STATE_GAME);
			}
		});
		
		//game win next button监听器 
		btn_game_win_next.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mGameApplication.mSoundUtils.playGameSoundByid(0, 0);
				dlg_win.cancel();
				//如果点击next进行下一关，则将关卡自动加1并保存
				//将iLevel和iMaxLevel数据保存至sharepreference
				sp.edit().putInt(HughieSPManager.SP_GameLevel, mGameLevel + 1).commit();
				//将game score清零并保存
				sp.edit().putInt(HughieSPManager.SP_GameScore, 0).commit();
				
				int n = 1;
				//进行下一关开始前，获取sharepreference中的关卡数据
				mGameLevel = sp.getInt(HughieSPManager.SP_GameLevel, 1);
				mGameMaxLevel = sp.getInt(HughieSPManager.SP_GameMaxLevel, 1);
				
				//进行游戏开始前，获取sharepreference中的分数数据
				mGameScore = sp.getInt(HughieSPManager.SP_GameScore, 0);
				mGameScoreTv.setText(String.valueOf(mGameScore));
				mGameController.startGame(HughieGameMainActivity.this, n);
			}
		});
	}
	
	//游戏失败后显示的操作
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
		
		//设置image fail anim动画
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
		
		//设置游戏的完成状态
		imgv_game_fail_star1.setBackgroundResource(R.drawable.imgv_game_main_win_star_unchecked);
		imgv_game_fail_star2.setBackgroundResource(R.drawable.imgv_game_main_win_star_unchecked);
		imgv_game_fail_star3.setBackgroundResource(R.drawable.imgv_game_main_win_star_unchecked);
		
		//game fail replay button监听器
		btn_game_fail_replay.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mGameApplication.mSoundUtils.playGameSoundByid(0, 0);
				dlg_fail.cancel();
				//将game score清零并保存
				sp.edit().putInt(HughieSPManager.SP_GameScore, 0).commit();
				//进行游戏开始前，获取sharepreference中的分数数据
				mGameScore = sp.getInt(HughieSPManager.SP_GameScore, 0);
				mGameScoreTv.setText(String.valueOf(mGameScore));
				
				loadGameMainViews(TAG_GAME_MAIN_STATE_GAME);
			}
		});
		
		//game fail menu button监听器 
		btn_game_fail_menu.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mGameApplication.mSoundUtils.playGameSoundByid(0, 0);
				dlg_fail.cancel();
				//将game score清零并保存
				sp.edit().putInt(HughieSPManager.SP_GameScore, 0).commit();
				//进行游戏开始前，获取sharepreference中的分数数据
				mGameScore = sp.getInt(HughieSPManager.SP_GameScore, 0);
				mGameScoreTv.setText(String.valueOf(mGameScore));
				finish();
			}
		});
	}
	
	//game暂停后显示的操作
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
		
		//设置app tween动画
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
		
		//button pause app1监听
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
				
				//将game score清零并保存
				sp.edit().putInt(HughieSPManager.SP_GameScore, 0).commit();
				//进行游戏开始前，获取sharepreference中的分数数据
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
				
				//将game score清零并保存
				sp.edit().putInt(HughieSPManager.SP_GameScore, 0).commit();
				//进行游戏开始前，获取sharepreference中的分数数据
				mGameScore = sp.getInt(HughieSPManager.SP_GameScore, 0);
				mGameScoreTv.setText(String.valueOf(mGameScore));
				
				finish();
			}
		});
	}
	
	/**
	 * @title showGameHelpWindow
	 * @description 游戏的帮助界面
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
	 * @description 设置游戏结束的状态，并加载显示界面
	 * @param gameState：游戏结束的状态
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
	 * @description 点击显示帮助页面后的操作
	 * @param
	 * @return
	 */
	public void setGameHelpState() {
		// 暂停游戏
		mGameController.setGamePause();
		mGameState = TAG_GAME_MAIN_STATE_HELP;
		loadGameMainViews(TAG_GAME_MAIN_STATE_HELP);
	}
	
	/**
	 * @title setGamePauseState
	 * @description 游戏暂定
	 * @param
	 * @return
	 */
	public void setGamePauseState() {
		// 暂停游戏
		mGameController.setGamePause();
		mGameState = TAG_GAME_MAIN_STATE_PAUSE;
		loadGameMainViews(TAG_GAME_MAIN_STATE_PAUSE);
	}
	
	/**
	 * @title showGameSettings
	 * @description 显示并展开game settings
	 * @param
	 * @return
	 */
	public void showGameSettings() {
		// 设置game music button显示的translateAnimation
		TranslateAnimation mShowMusicTranslateAnimation = new TranslateAnimation(45.0F * mGameApplication.density, 0.0F, 
				-40.0F * mGameApplication.density, 0.0F);
		mShowMusicTranslateAnimation.setDuration(300L);
		mMenuMusicBtn.setVisibility(View.VISIBLE);
		mMenuMusicBtn.startAnimation(mShowMusicTranslateAnimation);
		
		// 设置game help button显示的translateAnimation
		TranslateAnimation mShowHelpTranslateAnimation = new TranslateAnimation(0.0F, 0.0F, -40.0F * mGameApplication.density, 
				0.0F);
		mShowHelpTranslateAnimation.setDuration(300L);
		mMenuHelpBtn.setVisibility(View.VISIBLE);
		mMenuHelpBtn.startAnimation(mShowHelpTranslateAnimation);
		
		// 设置game pause button显示的translateAnimation
		TranslateAnimation mShowPauseTranslateAnimation = new TranslateAnimation(-45.0F * mGameApplication.density, 0.0F, 
				-40.0F * mGameApplication.density, 0.0F);
		mShowPauseTranslateAnimation.setDuration(300L);
		mMenuPauseBtn.setVisibility(View.VISIBLE);
		mMenuPauseBtn.startAnimation(mShowPauseTranslateAnimation);
	}
	
	/**
	 * @title unShowGameSettings
	 * @description 隐藏并合拢game settings
	 * @param
	 * @return
	 */
	public void unShowGameSettings() {
		// 设置game music button隐藏的translateAnimation
		TranslateAnimation mUnshowMusicTranslateAnimation = new TranslateAnimation(0.0F, 45.0F * mGameApplication.density, 
				0.0F, -40.0F * mGameApplication.density);
		mUnshowMusicTranslateAnimation.setDuration(300L);
		mMenuMusicBtn.setVisibility(View.GONE);
		mMenuMusicBtn.startAnimation(mUnshowMusicTranslateAnimation);
		
		// 设置game help button隐藏的translateAnimation
		TranslateAnimation mUnshowHelpTranslateAnimation = new TranslateAnimation(0.0F, 0.0F, 0.0F, 
				-40.0F * mGameApplication.density);
		mUnshowHelpTranslateAnimation.setDuration(300L);
		mMenuHelpBtn.setVisibility(View.GONE);
		mMenuHelpBtn.startAnimation(mUnshowHelpTranslateAnimation);
		
		// 设置game pause button隐藏的translateAnimation
		TranslateAnimation mUnshowPauseTranslateAnimation = new TranslateAnimation(0.0F, -45.0F * mGameApplication.density, 
				0.0F, -40.0F * mGameApplication.density);
		mUnshowPauseTranslateAnimation.setDuration(300L);
		mMenuPauseBtn.setVisibility(View.GONE);
		mMenuPauseBtn.startAnimation(mUnshowPauseTranslateAnimation);
	}
	
	//判断game apps packageName是否已经运行
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
	
	//如果游戏中按了home键或则其他按键切换到了其他的activity中，则要自动暂停游戏。再次进入是可以继续玩
	public void onPause(){
		super.onPause();
		this.mGameApplication.mSoundUtils.stopBgMusic();
		if(mGameState == TAG_GAME_MAIN_STATE_GAME){
			mGameState = TAG_GAME_MAIN_STATE_PAUSE;
			// 对游戏暂停，控制器需要进行的处理函数
			mGameController.setGamePause();
		}
	}
	
	//如果游戏中按了home键或则其他按键切换到了其他的activity中，则要自动暂停游戏。再次进入是可以继续玩
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

package com.hughie.linkgame.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hughie.linkgame.R;
import com.hughie.linkgame.base.HughieBaseActivity;
import com.hughie.linkgame.common.HughieGameApplication;
import com.hughie.linkgame.common.HughieSPManager;
import com.hughie.linkgame.utils.HughieActivityUtils;

/**
 * 游戏欢迎界面
 * @ClassName: HughieGameWelcomeActivity
 * @author hughiezhang
 * @since 2015-10-08 16:00
 */
public class HughieGameWelcomeActivity extends HughieBaseActivity implements
		OnClickListener {
	private Button mWelcomeStoryBtn;								//故事模式btn
	private Button mWelcomeThemeBtn;							//主题btn
	private Button mWelcomeFoodBtn;								//食物按钮
	private Button mWelcomeAnimalBtn;							//动物按钮
	private Button mWelcomeFruitBtn;								//水果按钮
	private Button mWelcomeRankingBtn;							//评价按钮
	
	private ImageView mWelcomeChickenImgv;				//chicken图片
	private ImageView mWelcomeLogoImgv;					//logo图片
	
	private HughieGameApplication mGameApplication;
	
	private Context mContext;
	
	private final int TAG_WELCOME_BTN_STORY = 1001;			//tag story
	private final int TAG_WELCOME_BTN_RANKING = 1002;		//tag ranking
	private final int TAG_WELCOME_BTN_THEME = 1003;			//tag theme
	private final int TAG_WELCOME_BTN_FOOD = 1006;			//tag food
	private final int TAG_WELCOME_BTN_FRUIT = 1007;			//tag fruit
	private final int TAG_WELCOME_BTN_ANIMAL = 1008;		//tag animal
	private final int TAG_WELCOME_BTN_THEME_OFF = 1009;	//tag theme off
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_hughie_game_welcome);
		
		this.mContext = this;
		this.mGameApplication = (HughieGameApplication) getApplication();
		sp.edit().putString(HughieSPManager.SP_GameSort, HughieSPManager.SP_GameFood).commit();
		
		initGameWelcomeTitle();
		initGameWelcomeView();
	}
	
	//	初始化title布局
	private void initGameWelcomeTitle() {
		setTitleTvHead(R.string.str_screen_tab_game_txt);
	}
	
	//	初始化布局
	private void initGameWelcomeView() {
		//故事模式
		mWelcomeStoryBtn = (Button) findViewById(R.id.game_welcome_story_btn);
		mWelcomeStoryBtn.setTag(Integer.valueOf(TAG_WELCOME_BTN_STORY));
		mWelcomeStoryBtn.setOnClickListener(this);
		
		//主题
		mWelcomeThemeBtn = (Button) findViewById(R.id.game_welcome_theme_btn);
		mWelcomeThemeBtn.setTag(Integer.valueOf(TAG_WELCOME_BTN_THEME_OFF));
		mWelcomeThemeBtn.setOnClickListener(this);
		
		//食物
		mWelcomeFoodBtn = (Button) findViewById(R.id.game_welcome_food_btn);
		mWelcomeFoodBtn.setTag(Integer.valueOf(TAG_WELCOME_BTN_FOOD));
		mWelcomeFoodBtn.setOnClickListener(this);
		
		//动物
		mWelcomeAnimalBtn = (Button) findViewById(R.id.game_welcome_animal_btn);
		mWelcomeAnimalBtn.setTag(Integer.valueOf(TAG_WELCOME_BTN_ANIMAL));
		mWelcomeAnimalBtn.setOnClickListener(this);
		
		//水果
		mWelcomeFruitBtn = (Button) findViewById(R.id.game_welcome_fruit_btn);
		mWelcomeFruitBtn.setTag(Integer.valueOf(TAG_WELCOME_BTN_FRUIT));
		mWelcomeFruitBtn.setOnClickListener(this);
		
		//评价
		mWelcomeRankingBtn = (Button) findViewById(R.id.game_welcome_ranking_btn);
		mWelcomeRankingBtn.setTag(Integer.valueOf(TAG_WELCOME_BTN_RANKING));
		mWelcomeRankingBtn.setOnClickListener(this);
		
		//chicken
		mWelcomeChickenImgv = (ImageView) findViewById(R.id.game_welcome_chicken_imgv);
		
		//logo
		mWelcomeLogoImgv = (ImageView) findViewById(R.id.game_welcome_logo_imgv);
		
		changeGameTheme();
		initGameWelcomeDate();
	}
	
	//更换主题
	private void changeGameTheme() {
		//btn game theme food select
		if(sp.getString(HughieSPManager.SP_GameSort, HughieSPManager.SP_GameFood)
				.equals(HughieSPManager.SP_GameFood)) {
			mWelcomeFoodBtn.setBackgroundResource(R.drawable.imgv_game_welcome_btn_food_hot);
			mWelcomeAnimalBtn.setBackgroundResource(R.drawable.hughie_btn_game_welcome_animal_selector);
			mWelcomeFruitBtn.setBackgroundResource(R.drawable.hughie_btn_game_welcome_fruit_selector);
		}
		
		//btn game theme animal select
		if(sp.getString(HughieSPManager.SP_GameSort, HughieSPManager.SP_GameFood)
				.equals(HughieSPManager.SP_GameAnimal)) {
			mWelcomeFoodBtn.setBackgroundResource(R.drawable.hughie_btn_game_welcome_food_selector);
			mWelcomeAnimalBtn.setBackgroundResource(R.drawable.imgv_game_welcome_btn_animal_hot);
			mWelcomeFruitBtn.setBackgroundResource(R.drawable.hughie_btn_game_welcome_fruit_selector);
		}
		
		//btn game theme fruit select
		if(sp.getString(HughieSPManager.SP_GameSort, HughieSPManager.SP_GameFood)
				.equals(HughieSPManager.SP_GameFruit)) {
			mWelcomeFoodBtn.setBackgroundResource(R.drawable.hughie_btn_game_welcome_food_selector);
			mWelcomeAnimalBtn.setBackgroundResource(R.drawable.hughie_btn_game_welcome_animal_selector);
			mWelcomeFruitBtn.setBackgroundResource(R.drawable.imgv_game_welcome_btn_fruit_hot);
		}
	}
	
	//初始化游戏数据
	private void initGameWelcomeDate() {
		Bitmap mWelcomeLogoBitmap = BitmapFactory.decodeResource(getResources(), 
				R.drawable.imgv_game_welcome_logo_en);
		int mLogoWidth = mWelcomeLogoBitmap.getWidth();
		int mLogoHeight = mWelcomeLogoBitmap.getHeight();
		int mLogoDensityHeight = (int) (mGameApplication.displayWidth / mLogoWidth * mLogoHeight);
		ViewGroup.LayoutParams mLogoParams = mWelcomeLogoImgv.getLayoutParams();
		mLogoParams.width = mGameApplication.displayWidth * 4 / 5;
		mLogoParams.height = mLogoDensityHeight;
		mWelcomeLogoImgv.setLayoutParams(mLogoParams);
		mWelcomeLogoBitmap.recycle();
		if(mGameApplication.displayHeight == 480) {
			((RelativeLayout.LayoutParams)mWelcomeLogoImgv.getLayoutParams()).topMargin = 130;
			((RelativeLayout.LayoutParams)mWelcomeChickenImgv.getLayoutParams()).topMargin = 50;
		}
	}
	
	//set theme show
	public void setShowTheme() {
		//button food theme show
		TranslateAnimation mShowFoodTranslateAnimation = new TranslateAnimation(0.0F, 0.0F, 
				48.0F * mGameApplication.density, 0.0F);
		mShowFoodTranslateAnimation.setDuration(500L);
		mWelcomeFoodBtn.setVisibility(View.VISIBLE);
		mWelcomeFoodBtn.startAnimation(mShowFoodTranslateAnimation);
		
		//button animal theme show
		TranslateAnimation mShowAnimalTranslateAnimation = new TranslateAnimation(-70.0F * mGameApplication.density, 
				0.0F, 0.0F, 0.0F);
		mShowAnimalTranslateAnimation.setDuration(500L);
		mWelcomeAnimalBtn.setVisibility(View.VISIBLE);
		mWelcomeAnimalBtn.startAnimation(mShowAnimalTranslateAnimation);
		
		//button fruit theme show
		TranslateAnimation mShowFruitTranslateAnimation = new TranslateAnimation(-43.0F * mGameApplication.density, 
				0.0F, 37.0F * mGameApplication.density, 0.0F);
		mShowFruitTranslateAnimation.setDuration(500L);
		mWelcomeFruitBtn.setVisibility(View.VISIBLE);
		mWelcomeFruitBtn.startAnimation(mShowFruitTranslateAnimation);
	}
	
	//set theme unshow
	public void setUnshowTheme() {
		//button food theme unshow
		TranslateAnimation mUnshowFoodTranslateAnimation = new TranslateAnimation(0.0F, 0.0F, 
				0.0F, 48.0F * mGameApplication.density);
		mUnshowFoodTranslateAnimation.setDuration(500L);
		mWelcomeFoodBtn.setVisibility(View.GONE);
		mWelcomeFoodBtn.startAnimation(mUnshowFoodTranslateAnimation);
		
		//button animal theme unshow
		TranslateAnimation mUnshowAnimalTranslateAnimation = new TranslateAnimation(0.0F, 
				-70.0F * mGameApplication.density, 0.0F, 0.0F);
		mUnshowAnimalTranslateAnimation.setDuration(500L);
		mWelcomeAnimalBtn.setVisibility(View.GONE);
		mWelcomeAnimalBtn.startAnimation(mUnshowAnimalTranslateAnimation);
		
		//button fruit theme unshow
		TranslateAnimation mUnshowFruitTranslateAnimation = new TranslateAnimation(0.0F, 
				-43.0F * mGameApplication.density, 0.0F, 37.0F * mGameApplication.density);
		mUnshowFruitTranslateAnimation.setDuration(500L);
		mWelcomeFruitBtn.setVisibility(View.GONE);
		mWelcomeFruitBtn.startAnimation(mUnshowFruitTranslateAnimation);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == 4 && event.getRepeatCount() == 0) {
			mGameApplication.mSoundUtils.stopBgMusic();
			finish();
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		mGameApplication.mSoundUtils.playGameSoundByid(0, 0);
		switch(((Integer)v.getTag()).intValue()) {
			//story mode
			case TAG_WELCOME_BTN_STORY:
				Intent mStoryIntent = new Intent(mContext, HughieGameMapActivity.class);
				HughieActivityUtils.switchTo(HughieGameWelcomeActivity.this, mStoryIntent);
				break;
			//button theme click to show
			case TAG_WELCOME_BTN_THEME:
				setShowTheme();
				v.setTag(Integer.valueOf(TAG_WELCOME_BTN_THEME_OFF));
				break;
			//button theme click to unshow
			case TAG_WELCOME_BTN_THEME_OFF:
				setUnshowTheme();
				v.setTag(Integer.valueOf(TAG_WELCOME_BTN_THEME));
				break;
			//btn theme food click
			case TAG_WELCOME_BTN_FOOD:
				sp.edit().putString(HughieSPManager.SP_GameSort, HughieSPManager.SP_GameFood).commit();
				changeGameTheme();
				break;
			//btn theme animal click
			case 	TAG_WELCOME_BTN_ANIMAL:
				sp.edit().putString(HughieSPManager.SP_GameSort, HughieSPManager.SP_GameAnimal).commit();
				changeGameTheme();
				break;
			//btn theme fruit click
			case TAG_WELCOME_BTN_FRUIT:
				sp.edit().putString(HughieSPManager.SP_GameSort, HughieSPManager.SP_GameFruit).commit();
				changeGameTheme();
				break;
			default:
				break;
		}
	}
}

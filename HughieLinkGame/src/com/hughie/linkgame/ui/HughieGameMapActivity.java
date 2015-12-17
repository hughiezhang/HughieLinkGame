package com.hughie.linkgame.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hughie.linkgame.R;
import com.hughie.linkgame.base.HughieBaseActivity;
import com.hughie.linkgame.common.HughieGameApplication;
import com.hughie.linkgame.common.HughieSPManager;
import com.hughie.linkgame.utils.HughieActivityUtils;
import com.hughie.linkgame.utils.HughieGameUtils;

/**
 * 游戏模式界面
 * @ClassName: HughieGameMapActivity
 * @author hughiezhang
 * @since 2015-10-12 09:42
 */
public class HughieGameMapActivity extends HughieBaseActivity implements
		OnClickListener {
	private LinearLayout mMapHouseLayout1;
	private ImageView mMapHouseImgv1;
	private Button mMapHouseBtn1;
	
	private LinearLayout mMapHouseLayout2;
	private ImageView mMapHouseImgv2;
	private Button mMapHouseBtn2;
	
	private LinearLayout mMapHouseLayout3;
	private ImageView mMapHouseImgv3;
	private Button mMapHouseBtn3;
	
	private LinearLayout mMapHouseLayout4;
	private ImageView mMapHouseImgv4; 
	private Button mMapHouseBtn4;
	
	private LinearLayout mMapHouseLayout5;
	private ImageView mMapHouseImgv5;
	private Button mMapHouseBtn5;
	
	private HughieGameApplication mGameApplication;
	private Context mContext;
	
	private final int TAG_GAME_MAP_BTN_HOUSE1 = 1;					//tag button house1
	private final int TAG_GAME_MAP_BTN_HOUSE2 = 2;					//tag button house2
	private final int TAG_GAME_MAP_BTN_HOUSE3 = 3;					//tag button house3
	private final int TAG_GAME_MAP_BTN_HOUSE4 = 4;					//tag button house4
	private final int TAG_GAME_MAP_BTN_HOUSE5 = 5;					//tag button house5
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_hughie_game_map);
		
		mContext = this;
		mGameApplication = (HughieGameApplication) getApplication();
	}
	
	private void initGameMapViews() {
		//game house animation selector1
		AnimationSet mGoAnimationSet1 = (AnimationSet) AnimationUtils
				.loadAnimation(getApplicationContext(), R.anim.hughie_anim_go1);
		//game house animation selector2
		AnimationSet mGoAnimationSet2 = (AnimationSet) AnimationUtils
				.loadAnimation(getApplicationContext(), R.anim.hughie_anim_go2);
		
		//game house 1
		mMapHouseLayout1 = (LinearLayout) findViewById(R.id.game_map_house1_layout);
		mMapHouseImgv1 = (ImageView) findViewById(R.id.game_map_house1_imgv);
		mMapHouseBtn1 = (Button) findViewById(R.id.game_map_house1_btn);
		mMapHouseBtn1.setTag(Integer.valueOf(TAG_GAME_MAP_BTN_HOUSE1));
		mMapHouseBtn1.setOnClickListener(this);
		
		//game house 2
		mMapHouseLayout2 = (LinearLayout) findViewById(R.id.game_map_house2_layout);
		mMapHouseImgv2 = (ImageView) findViewById(R.id.game_map_house2_imgv);
		mMapHouseBtn2 = (Button) findViewById(R.id.game_map_house2_btn);
		mMapHouseBtn2.setTag(Integer.valueOf(TAG_GAME_MAP_BTN_HOUSE2));
		mMapHouseBtn2.setOnClickListener(this);
		
		//game house 3
		mMapHouseLayout3 = (LinearLayout) findViewById(R.id.game_map_house3_layout);
		mMapHouseImgv3 = (ImageView) findViewById(R.id.game_map_house3_imgv);
		mMapHouseBtn3 = (Button) findViewById(R.id.game_map_house3_btn);
		mMapHouseBtn3.setTag(Integer.valueOf(TAG_GAME_MAP_BTN_HOUSE3));
		mMapHouseBtn3.setOnClickListener(this);
		
		//game house 4
		mMapHouseLayout4 = (LinearLayout) findViewById(R.id.game_map_house4_layout);
		mMapHouseImgv4 = (ImageView) findViewById(R.id.game_map_house4_imgv);
		mMapHouseBtn4 = (Button) findViewById(R.id.game_map_house4_btn);
		mMapHouseBtn4.setTag(Integer.valueOf(TAG_GAME_MAP_BTN_HOUSE4));
		mMapHouseBtn4.setOnClickListener(this);
		
		//game house 5
		mMapHouseLayout5 = (LinearLayout) findViewById(R.id.game_map_house5_layout);
		mMapHouseImgv5 = (ImageView) findViewById(R.id.game_map_house5_imgv);
		mMapHouseBtn5 = (Button) findViewById(R.id.game_map_house5_btn);
		mMapHouseBtn5.setTag(Integer.valueOf(TAG_GAME_MAP_BTN_HOUSE5));
		mMapHouseBtn5.setOnClickListener(this);
		
		int mGameSence = (HughieGameUtils.getMaxLevel(mContext) + -1) / 16 + 1;
		
		mMapHouseBtn1.startAnimation(mGoAnimationSet1);
		
		//判断游戏组模式2是否开启
		if(mGameSence > 1) {
			mMapHouseImgv2.setBackgroundResource(R.drawable.imgv_game_map_house2_hot);
			mMapHouseBtn2.startAnimation(mGoAnimationSet2);
		}
		
		//判断游戏组模式3是否开启
		if(mGameSence > 2) {
			mMapHouseImgv3.setBackgroundResource(R.drawable.imgv_game_map_house3_hot);
			mMapHouseBtn3.startAnimation(mGoAnimationSet1);
		}
		
		//判断游戏组模式4是否开启
		if(mGameSence > 3) {
			mMapHouseImgv4.setBackgroundResource(R.drawable.imgv_game_map_house4_hot);
			mMapHouseBtn4.startAnimation(mGoAnimationSet2);
		}
		
		//判断游戏组模式5是否开启
		if(mGameSence > 4) {
			mMapHouseImgv5.setBackgroundResource(R.drawable.imgv_game_map_house5_hot);
			mMapHouseBtn5.startAnimation(mGoAnimationSet1);
		}
		
		//layout house1 position
		RelativeLayout.LayoutParams mMapHouseParams1 = (RelativeLayout.LayoutParams) mMapHouseLayout1.getLayoutParams();
		mMapHouseParams1.bottomMargin = (int) (80.0F * mGameApplication.displayHeight / 480.0F);
		mMapHouseParams1.rightMargin = (int) (60.0F * mGameApplication.displayHeight / 320.0F);
		
		//layout house2 position
		RelativeLayout.LayoutParams mMapHouseParams2 = (RelativeLayout.LayoutParams) mMapHouseLayout2.getLayoutParams();
		mMapHouseParams2.bottomMargin = (int) (160.0F * mGameApplication.displayHeight / 480.0F);
		mMapHouseParams2.rightMargin = (int) (15.0F * mGameApplication.displayHeight / 320.0F);
		
		//layout house3 position
		RelativeLayout.LayoutParams mMapHouseParams3 = (RelativeLayout.LayoutParams) mMapHouseLayout3.getLayoutParams();
		mMapHouseParams3.bottomMargin = (int) (160.0F * mGameApplication.displayHeight / 480.0F);
		mMapHouseParams3.leftMargin = (int) (11.0F * mGameApplication.displayHeight / 320.0F);
		
		//layout house4 position
		RelativeLayout.LayoutParams mMapHouseParams4 = (RelativeLayout.LayoutParams) mMapHouseLayout4.getLayoutParams();
		mMapHouseParams4.bottomMargin = (int) (240.0F * mGameApplication.displayHeight / 480.0F);
		mMapHouseParams4.rightMargin = (int) (10.5F * mGameApplication.displayHeight / 320.0F);
		
		//layout house5 position
		RelativeLayout.LayoutParams mMapHouseParams5 = (RelativeLayout.LayoutParams) mMapHouseLayout5.getLayoutParams();
		mMapHouseParams5.bottomMargin = (int) (275.0F * mGameApplication.displayHeight / 480.0F);
		mMapHouseParams5.leftMargin = (int) (28.0F * mGameApplication.displayHeight / 320.0F);
	}
	
	@Override
	protected void onResume() {
		initGameMapViews();
		super.onResume();
	}
	
	@Override
	protected void onStop() {
		mMapHouseBtn1.clearAnimation();
		mMapHouseBtn2.clearAnimation();
		mMapHouseBtn3.clearAnimation();
		mMapHouseBtn4.clearAnimation();
		mMapHouseBtn5.clearAnimation();
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void onClick(View v) {
		mGameApplication.mSoundUtils.playGameSoundByid(0, 0);
		Intent mIntent = new Intent(mContext, HughieGamePointActivity.class);
		mIntent.putExtra(HughieSPManager.SP_GameFrame, (Integer)v.getTag());
		HughieActivityUtils.switchTo(HughieGameMapActivity.this, mIntent);
	}
}

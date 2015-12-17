package com.hughie.linkgame.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import com.hughie.linkgame.R;
import com.hughie.linkgame.adapter.HughieGamePointAdapter;
import com.hughie.linkgame.adapter.HughieGamePointAdapter.OnGameLevelItemClickListener;
import com.hughie.linkgame.base.HughieBaseActivity;
import com.hughie.linkgame.common.HughieGameApplication;
import com.hughie.linkgame.common.HughieSPManager;
import com.hughie.linkgame.entity.LevelItemDetail;
import com.hughie.linkgame.utils.HughieActivityUtils;
import com.hughie.linkgame.utils.HughieGameUtils;

/**
 * 游戏关卡界面
 * @ClassName: HughieGamePointActivity
 * @author hughiezhang
 * @since 2015-10-13 10:31
 */
public class HughieGamePointActivity extends HughieBaseActivity 
		implements OnGameLevelItemClickListener {
	private GridView mGameLevelGv;
	private HughieGamePointAdapter mGamePointAdapter;
	
	private int mGameFrame;						//游戏的情景模式
	private int mGameMaxLevel;					//已完成的最大关数
	
	private List<LevelItemDetail> mGameLevelList;
	
	private HughieGameApplication mGameApplication;
	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_hughie_game_level);
		
		mContext = this;
		mGameApplication = (HughieGameApplication) getApplication();
		
		mGameFrame = (Integer) getIntent().getSerializableExtra(HughieSPManager.SP_GameFrame);
		mGameMaxLevel = HughieGameUtils.getMaxLevel(mContext);
		
		initGamePointViews();
	}
	
	//初始化控件
	private void initGamePointViews() {
		mGameLevelGv = (GridView) findViewById(R.id.game_level_gv);
		mGameLevelList = new ArrayList<LevelItemDetail>();
		int mFrameLevel = (mGameFrame - 1) * 16;
		for(int i = 1; i <= 16; i++) {
			int mGameLevel = i + mFrameLevel;
			LevelItemDetail mLevelItemDetail = new LevelItemDetail();
			//判断是否是已经完成的关卡
			if(mGameLevel > mGameMaxLevel) {
				mLevelItemDetail.setStateFlag(-1);
			} else {
				mLevelItemDetail.setStateFlag(mGameLevel);
			}
			
			mGameLevelList.add(mLevelItemDetail);
		}
		
		setGameLevelListAdapter(mGameLevelList);
	}
	
	//配置游戏关卡list的adapter
	private void setGameLevelListAdapter(List<LevelItemDetail> gameLevelList) {
		//判断游戏关卡gridview的adapter是否为空
		if(mGameLevelGv.getAdapter() == null) {
			mGamePointAdapter = new HughieGamePointAdapter(mContext, mGameLevelList, this);
			mGameLevelGv.setAdapter(mGamePointAdapter);
		} else {
			if(gameLevelList != null && gameLevelList.size() > 0) {
				mGamePointAdapter.getGameLevelList().clear();
				mGamePointAdapter.setGameLevelList(mGameLevelList);
				mGamePointAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void OnGameLevelItemClick(int position) {
		mGameApplication.mSoundUtils.playGameSoundByid(0, 0);
		//游戏的关数
		int mGameLevels = (mGameFrame - 1) * 16 + position + 1;
		if(mGameLevels <= mGameMaxLevel) {
			Intent mLevelIntent = new Intent(mContext, HughieGameMainActivity.class);
			mLevelIntent.putExtra(HughieGameMainActivity.Extra_GameMode, 0);
			mLevelIntent.putExtra(HughieGameMainActivity.Extra_GameLevel, mGameLevels);
			HughieActivityUtils.switchTo(HughieGamePointActivity.this, mLevelIntent);
			finish();
		}
	}
}

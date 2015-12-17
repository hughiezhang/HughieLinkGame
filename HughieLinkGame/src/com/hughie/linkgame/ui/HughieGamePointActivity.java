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
 * ��Ϸ�ؿ�����
 * @ClassName: HughieGamePointActivity
 * @author hughiezhang
 * @since 2015-10-13 10:31
 */
public class HughieGamePointActivity extends HughieBaseActivity 
		implements OnGameLevelItemClickListener {
	private GridView mGameLevelGv;
	private HughieGamePointAdapter mGamePointAdapter;
	
	private int mGameFrame;						//��Ϸ���龰ģʽ
	private int mGameMaxLevel;					//����ɵ�������
	
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
	
	//��ʼ���ؼ�
	private void initGamePointViews() {
		mGameLevelGv = (GridView) findViewById(R.id.game_level_gv);
		mGameLevelList = new ArrayList<LevelItemDetail>();
		int mFrameLevel = (mGameFrame - 1) * 16;
		for(int i = 1; i <= 16; i++) {
			int mGameLevel = i + mFrameLevel;
			LevelItemDetail mLevelItemDetail = new LevelItemDetail();
			//�ж��Ƿ����Ѿ���ɵĹؿ�
			if(mGameLevel > mGameMaxLevel) {
				mLevelItemDetail.setStateFlag(-1);
			} else {
				mLevelItemDetail.setStateFlag(mGameLevel);
			}
			
			mGameLevelList.add(mLevelItemDetail);
		}
		
		setGameLevelListAdapter(mGameLevelList);
	}
	
	//������Ϸ�ؿ�list��adapter
	private void setGameLevelListAdapter(List<LevelItemDetail> gameLevelList) {
		//�ж���Ϸ�ؿ�gridview��adapter�Ƿ�Ϊ��
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
		//��Ϸ�Ĺ���
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

package com.hughie.linkgame.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.hughie.linkgame.HughieMainActivity;
import com.hughie.linkgame.R;
import com.hughie.linkgame.base.HughieMainBaseActivity;
import com.hughie.linkgame.common.HughieToastManager;
import com.umeng.analytics.MobclickAgent;

/**
 * @description 游戏自定义首页
 * @ClassName: HughieGameIndexActivity
 * @author hughiezhang
 * @since 2015-09-22 11:47
 */
public class HughieGameIndexActivity extends HughieMainBaseActivity {
	private RelativeLayout mGameIndexLayout[] = new RelativeLayout[5];
	private CheckBox mGameIndexChx[] = new CheckBox[5];
	private int mGameIndexLayoutId[] = {R.id.game_index_grid1_layout, R.id.game_index_grid2_layout,
			R.id.game_index_grid3_layout, R.id.game_index_grid4_layout, R.id.game_index_grid5_layout};
	private int mGameIndexChxId[] = {R.id.game_index_grid1_chx, R.id.game_index_grid2_chx,
			R.id.game_index_grid3_chx, R.id.game_index_grid4_chx, R.id.game_index_grid5_chx};
	private boolean mGameIndexState[] = {true, true, true, false, false};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_hughie_game_index);
		initIndexTitle();
		initIndexView();
	}
	
	//初始化title布局
	private void initIndexTitle() {
		setTitleTvHead(R.string.str_game_index_title_txt);
		setLeftVisibility(View.GONE);
		setBtnRight(R.string.str_game_index_title_right_txt);
	}
	
	private void initIndexView() {
		for(int i = 0; i < 5; i++) {
			mGameIndexChx[i] = (CheckBox) findViewById(mGameIndexChxId[i]);
			mGameIndexChx[i].setOnCheckedChangeListener(mCheckedChangeListener);
			mGameIndexLayout[i] = (RelativeLayout) findViewById(mGameIndexLayoutId[i]);
			if(i < 3) mGameIndexLayout[i].setBackgroundColor(getResources().getColor(R.color.color_FFF3F3F3));
			if(i >= 2) mGameIndexLayout[i].setOnClickListener(mIndexOnClickListener);
		}
	}
	
	@Override
	public void onClickHeadRight(View v) {
		super.onClickHeadRight(v);
		if(getGameIndexCheckedCount() < 2) {
			HughieToastManager.showInfo(HughieGameIndexActivity.this, "请勾选至少2个模块, 定制个性化首页.");
		} else {
			saveTabGameSettings();
			Intent mIntent = new Intent(HughieGameIndexActivity.this, HughieMainActivity.class);
			startActivity(mIntent);
			finish();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			if(getGameIndexCheckedCount() < 2) {
				HughieToastManager.showInfo(HughieGameIndexActivity.this, "请勾选至少2个模块, 定制个性化首页.");
			} else {
				saveTabGameSettings();
				Intent mIntent = new Intent(HughieGameIndexActivity.this, HughieMainActivity.class);
				startActivity(mIntent);
				finish();
			}
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	private void saveTabGameSettings() {
		String code[] = {getResources().getString(R.string.str_game_grid_item_games_txt), getResources().getString(R.string.str_game_grid_item_rankings_txt),
				getResources().getString(R.string.str_game_grid_item_shop_txt), getResources().getString(R.string.str_game_grid_item_kit_txt), 
				getResources().getString(R.string.str_game_grid_item_about_txt)};
		int img[] = {1, 2, 3, 4, 5};
		// 保存到配置里
		int size = 0;
		int x_size = 0;
		for(int i = 0; i < 5; i++) {
			if(mGameIndexState[i]) {
				sp.edit().putString("code_" + size, code[i]).commit();
				sp.edit().putString("img_" + size, img[i] + "").commit();
				size++;
			} else {
				sp.edit().putString("x_code_" + x_size, code[i]).commit();
				sp.edit().putString("x_img_" + x_size, img[i] + "").commit();
				x_size++;
			}
		}
		
		sp.edit().putInt("size", size).commit();
		sp.edit().putInt("x_size", x_size).commit();
	}
	
	private int getGameIndexCheckedCount() {
		int mCount = 0;
		for(int i = 0; i < 5; i++) {
			if(mGameIndexState[i])
				mCount++;
		}
		
		return mCount;
	}
	
	private CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			for(int i = 0; i < 5; i++) {
				if(buttonView == mGameIndexChx[i]) {
					if(!isChecked) {
						mGameIndexState[i] = false;
						mGameIndexLayout[i].setBackgroundColor(getResources().getColor(R.color.color_FFFFFFFF));
					} else if(getGameIndexCheckedCount() < 8) {
						mGameIndexState[i] = true;
						mGameIndexLayout[i].setBackgroundColor(getResources().getColor(R.color.color_FFF3F3F3));
					} else {
						HughieToastManager.showInfo(HughieGameIndexActivity.this, "最多只能定制8个模块");
						mGameIndexChx[i].setChecked(false);
						mGameIndexState[i] = false;
					}
				}
			}
		}
	};
	
	private View.OnClickListener mIndexOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			for(int i = 0; i < 5; i++) {
				if(v == mGameIndexLayout[i]) {
					mGameIndexChx[i].setChecked(!mGameIndexState[i]);
				}
			}
		}
	};
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onDestroy() {
		saveTabGameSettings();
		super.onDestroy();
	}
}

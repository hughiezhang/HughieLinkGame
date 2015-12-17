package com.hughie.linkgame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.hughie.link.support.common.HughieLoggerManager;
import com.hughie.linkgame.ad.service.HughieGetADRequest;
import com.hughie.linkgame.ad.service.HughieGetADResponse;
import com.hughie.linkgame.ad.service.HughieGetADService;
import com.hughie.linkgame.base.HughieMainBaseActivity;
import com.hughie.linkgame.base.listener.HughieNetListener;
import com.hughie.linkgame.base.service.HughieBaseRequest;
import com.hughie.linkgame.base.service.HughieBaseResponse;
import com.hughie.linkgame.common.HughieToastManager;
import com.hughie.linkgame.entity.ADBean;
import com.hughie.linkgame.fragment.HughieFlipFragment;
import com.hughie.linkgame.ui.HughieGameWelcomeActivity;
import com.hughie.linkgame.utils.HughieActivityUtils;
import com.hughie.linkgame.widget.HughieDragGridView;
import com.hughie.linkgame.widget.HughieDragGridView.OnDragGridItemClickListener;

/**
 * 游戏主界面
 * @ClassName: HughieGameHomeActivity
 * @author hughiezhang
 * @since 2015-09-08 11:08
 */
public class HughieGameHomeActivity extends HughieMainBaseActivity {
	//广告
	private FrameLayout mGameAdLayout;
	private int mScreenWidth;//屏幕宽度
	private int mScreenHeight;//屏幕高度
	private int mGameAdWidth;//广告宽度
	private int mGameAdHeight;//广告高度
	
	private HughieDragGridView mGameMainGv;
	
	private HughieGetADService  mAdservice;
	private List<ADBean> mADBeans = new ArrayList<ADBean>();
	
	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_hughie_game_home_main);
		
		mContext = this;
		
		initGameTitle();
		initGameParams();
		initGameView();
		initGridViewData();
	}
	
	//初始化title布局
	private void initGameTitle(){
		setTitleTvHead(R.string.str_screen_tab_game_txt);
		setLeftVisibility(View.GONE);
	}
	
	//初始化布局
	private void initGameView(){
		mGameAdLayout = (FrameLayout) findViewById(R.id.game_ad_layout);
		setGameAdLayoutDimension();
		
		mGameMainGv = (HughieDragGridView) findViewById(R.id.game_main_gv);
		setGameGridListener();
	}
	
	private void initGameParams() {
		mAdservice = new HughieGetADService(mContext);
	}
	
	//设置广告
	private void setGameAdLayoutDimension(){
		if (mScreenWidth == 0 || mScreenHeight == 0) {
			DisplayMetrics metrics = getResources().getDisplayMetrics();
			mScreenWidth = metrics.widthPixels;
			mScreenHeight = metrics.heightPixels; 
		}
		
		//设置广告的高度和宽度 (单位:像素)
		mGameAdWidth = mScreenWidth;
		mGameAdHeight = mScreenHeight / 3;
		HughieLoggerManager.println("广告宽度:" + mGameAdWidth + ", 广告高度:" + mScreenHeight);
		ViewGroup.LayoutParams lpad = mGameAdLayout.getLayoutParams();
		lpad.width = mGameAdWidth;
		lpad.height = mGameAdHeight;
		mGameAdLayout.setLayoutParams(lpad);
	}
	
	//	加载广告
	private void onGameAdLoaded() {
		String[] images = getGameAdImages();
		if (images == null || images.length ==0) {
			return;
		}
		
		FragmentManager manager = getSupportFragmentManager();
		HughieFlipFragment mFlipFragment = (HughieFlipFragment) manager.findFragmentByTag("gamead");
		if(mFlipFragment == null) {
			FragmentTransaction mTransaction = manager.beginTransaction();
			mFlipFragment = new HughieFlipFragment();
			
			Bundle args = new Bundle();
			args.putStringArray("images", images);
			args.putInt("image_width", mGameAdWidth);
			args.putInt("image_height", mGameAdHeight);
			args.putInt("image_default", R.drawable.imgv_noimage_default);
			
			mFlipFragment.setArguments(args);
			mTransaction.replace(R.id.game_ad_layout, mFlipFragment, "gamead");
			mTransaction.commitAllowingStateLoss();
		}
	}
	
	private String[] getGameAdImages() {
		if(mADBeans == null)
			return null;
		
		String[] images = new String[mADBeans.size()];
		for(int i = 0; i < images.length; i++) {
			images[i] = mADBeans.get(i).getImageUrl();
		}
		
		return images;
	}
	
	//	加载广告数据以及加载广告
	private void loadGameAds(){
		HughieGetADRequest mGameAdRequest = new HughieGetADRequest();
		mAdservice.setRequest(mGameAdRequest);
		mAdservice.request(new HughieNetListener() {
			@Override
			public void onPrepare() {
				
			}
			
			@Override
			public void onLoading() {
				
			}
			
			@Override
			public void onComplete(String respondCode, HughieBaseRequest request, HughieBaseResponse response) {
				HughieGetADResponse mADResponse = (HughieGetADResponse) response;
				List<ADBean> beans = mADResponse.getData();
				if(beans.size() > 0) {
					mADBeans.clear();
					mADBeans.addAll(beans);
				}
			}
			
			@Override
			public void onLoadSuccess(HughieBaseResponse response) {
				onGameAdLoaded();
			}
			
			@Override
			public void onFailed(Exception ex, HughieBaseResponse response) {
				
			}
			
			@Override
			public void onCancel() {
				
			}
		});
	}
	
	@Override
	protected void onResume() {
		loadGameAds();
		HughieLoggerManager.println("HughieGameHomeActivity onResume");
		super.onResume();
	}
	
	//设置gridview的相关数据
	private void initGridViewData() {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		int mSize = sp.getInt("size", 0);
		for(int i = 0; i < mSize; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			String code = sp.getString("code_" + i, "");
			map.put("str1", code);
			map.put("str2", " ");
			map.put("str3", " ");
			map.put("str4", " ");
			map.put("code", code);
			map.put("img", sp.getString("img_" + i, ""));
			
			list.add(map);
		}
		mGameMainGv.setGridItemList(list);
	}
	
	//设置game grid的监听操作
	private void setGameGridListener() {
		mGameMainGv.setOnDragGridItemClickListener(new OnDragGridItemClickListener() {
			@Override
			public void OnGridItemPosChanged() {
				// 保存新的上栏显示在首页的自定义选项
				ArrayList<HashMap<String, String>> mGridChangedList = mGameMainGv.getGridItemList();
				
				int mIndex = 0;
				int mSize = mGridChangedList.size();
				for(int i = 0; i < mSize; i++) {
					HashMap<String, String> map2 = mGridChangedList.get(i);
					sp.edit().putString("code_" + mIndex, map2.get("code")).commit();		//名称
					sp.edit().putString("img_" + mIndex, map2.get("img")).commit();
					mIndex++;
				}
				
				sp.edit().putInt("size", mGridChangedList.size()).commit();
			}
			
			@Override
			public boolean OnGridItemDelete(int number) {
				HughieLoggerManager.println("delete: " + number);
				ArrayList<HashMap<String, String>> mGridDeleteList = mGameMainGv.getGridItemList();
				//必须剩下两个item，此时候不允许删除
				if(mGridDeleteList.size() == 2) {
					HughieToastManager.showInfo(mContext, "最后两个啦,留下我们吧.");
					return false;
				}
				
				if(mGridDeleteList.get(number).get("code")
						.equals(getResources().getString(R.string.str_game_grid_item_games_txt))){
					HughieToastManager.showInfo(mContext, "游戏选项是不能删除的哦.");
					return false;
				}
				
				HashMap<String, String> map = mGridDeleteList.get(number);
				
				// 把最后一个移动未下栏
				int mSize = sp.getInt("x_size", 0);
				sp.edit().putInt("x_size", mSize + 1).commit();
				sp.edit().putString("x_code_" + mSize, map.get("code")).commit();	// 名称
				sp.edit().putString("x_img_" + mSize, map.get("img")).commit();
				
				// 保存新的上栏显示在首页的荐
				sp.edit().putInt("size", mGridDeleteList.size() - 1).commit();
				int mIndex = 0;
				mSize = mGridDeleteList.size();
				for(int i = 0; i < mSize; i++) {
					if(i != number) {
						HashMap<String, String> map2 = mGridDeleteList.get(i);
						sp.edit().putString("code_" + mIndex, map2.get("code")).commit();
						sp.edit().putString("img_" + mIndex, map2.get("img")).commit();
						mIndex++;
					}
				}
				
				HughieToastManager.showInfo(mContext, "已移至[更多]");
				return true;
			}

			@Override
			public void OnGridItemWhenNormalState(String code) {
				//游戏选项
				if(code.equals(getResources().getString(R.string.str_game_grid_item_games_txt))){
					Intent mGameIntent = new Intent(mContext, HughieGameWelcomeActivity.class);
					HughieActivityUtils.switchTo(HughieGameHomeActivity.this, mGameIntent);
				//排行榜选项
				} else if(code.equals(getResources().getString(R.string.str_game_grid_item_rankings_txt))){
					HughieLoggerManager.println(code);
				//游戏商城选项
				} else if(code.equals(getResources().getString(R.string.str_game_grid_item_shop_txt))){
					HughieLoggerManager.println(code);
				//实用工具选项
				} else if(code.equals(getResources().getString(R.string.str_game_grid_item_kit_txt))){
					HughieLoggerManager.println(code);
				} else if(code.equals(getResources().getString(R.string.str_game_grid_item_about_txt))){
					HughieLoggerManager.println(code);
				}
			}

			@Override
			public void OnGridItemWhenBlankState() {
				HughieLoggerManager.println("更多更多");
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}

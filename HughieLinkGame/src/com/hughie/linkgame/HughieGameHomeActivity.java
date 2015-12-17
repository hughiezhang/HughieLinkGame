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
 * ��Ϸ������
 * @ClassName: HughieGameHomeActivity
 * @author hughiezhang
 * @since 2015-09-08 11:08
 */
public class HughieGameHomeActivity extends HughieMainBaseActivity {
	//���
	private FrameLayout mGameAdLayout;
	private int mScreenWidth;//��Ļ���
	private int mScreenHeight;//��Ļ�߶�
	private int mGameAdWidth;//�����
	private int mGameAdHeight;//���߶�
	
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
	
	//��ʼ��title����
	private void initGameTitle(){
		setTitleTvHead(R.string.str_screen_tab_game_txt);
		setLeftVisibility(View.GONE);
	}
	
	//��ʼ������
	private void initGameView(){
		mGameAdLayout = (FrameLayout) findViewById(R.id.game_ad_layout);
		setGameAdLayoutDimension();
		
		mGameMainGv = (HughieDragGridView) findViewById(R.id.game_main_gv);
		setGameGridListener();
	}
	
	private void initGameParams() {
		mAdservice = new HughieGetADService(mContext);
	}
	
	//���ù��
	private void setGameAdLayoutDimension(){
		if (mScreenWidth == 0 || mScreenHeight == 0) {
			DisplayMetrics metrics = getResources().getDisplayMetrics();
			mScreenWidth = metrics.widthPixels;
			mScreenHeight = metrics.heightPixels; 
		}
		
		//���ù��ĸ߶ȺͿ�� (��λ:����)
		mGameAdWidth = mScreenWidth;
		mGameAdHeight = mScreenHeight / 3;
		HughieLoggerManager.println("�����:" + mGameAdWidth + ", ���߶�:" + mScreenHeight);
		ViewGroup.LayoutParams lpad = mGameAdLayout.getLayoutParams();
		lpad.width = mGameAdWidth;
		lpad.height = mGameAdHeight;
		mGameAdLayout.setLayoutParams(lpad);
	}
	
	//	���ع��
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
	
	//	���ع�������Լ����ع��
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
	
	//����gridview���������
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
	
	//����game grid�ļ�������
	private void setGameGridListener() {
		mGameMainGv.setOnDragGridItemClickListener(new OnDragGridItemClickListener() {
			@Override
			public void OnGridItemPosChanged() {
				// �����µ�������ʾ����ҳ���Զ���ѡ��
				ArrayList<HashMap<String, String>> mGridChangedList = mGameMainGv.getGridItemList();
				
				int mIndex = 0;
				int mSize = mGridChangedList.size();
				for(int i = 0; i < mSize; i++) {
					HashMap<String, String> map2 = mGridChangedList.get(i);
					sp.edit().putString("code_" + mIndex, map2.get("code")).commit();		//����
					sp.edit().putString("img_" + mIndex, map2.get("img")).commit();
					mIndex++;
				}
				
				sp.edit().putInt("size", mGridChangedList.size()).commit();
			}
			
			@Override
			public boolean OnGridItemDelete(int number) {
				HughieLoggerManager.println("delete: " + number);
				ArrayList<HashMap<String, String>> mGridDeleteList = mGameMainGv.getGridItemList();
				//����ʣ������item����ʱ������ɾ��
				if(mGridDeleteList.size() == 2) {
					HughieToastManager.showInfo(mContext, "���������,�������ǰ�.");
					return false;
				}
				
				if(mGridDeleteList.get(number).get("code")
						.equals(getResources().getString(R.string.str_game_grid_item_games_txt))){
					HughieToastManager.showInfo(mContext, "��Ϸѡ���ǲ���ɾ����Ŷ.");
					return false;
				}
				
				HashMap<String, String> map = mGridDeleteList.get(number);
				
				// �����һ���ƶ�δ����
				int mSize = sp.getInt("x_size", 0);
				sp.edit().putInt("x_size", mSize + 1).commit();
				sp.edit().putString("x_code_" + mSize, map.get("code")).commit();	// ����
				sp.edit().putString("x_img_" + mSize, map.get("img")).commit();
				
				// �����µ�������ʾ����ҳ�ļ�
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
				
				HughieToastManager.showInfo(mContext, "������[����]");
				return true;
			}

			@Override
			public void OnGridItemWhenNormalState(String code) {
				//��Ϸѡ��
				if(code.equals(getResources().getString(R.string.str_game_grid_item_games_txt))){
					Intent mGameIntent = new Intent(mContext, HughieGameWelcomeActivity.class);
					HughieActivityUtils.switchTo(HughieGameHomeActivity.this, mGameIntent);
				//���а�ѡ��
				} else if(code.equals(getResources().getString(R.string.str_game_grid_item_rankings_txt))){
					HughieLoggerManager.println(code);
				//��Ϸ�̳�ѡ��
				} else if(code.equals(getResources().getString(R.string.str_game_grid_item_shop_txt))){
					HughieLoggerManager.println(code);
				//ʵ�ù���ѡ��
				} else if(code.equals(getResources().getString(R.string.str_game_grid_item_kit_txt))){
					HughieLoggerManager.println(code);
				} else if(code.equals(getResources().getString(R.string.str_game_grid_item_about_txt))){
					HughieLoggerManager.println(code);
				}
			}

			@Override
			public void OnGridItemWhenBlankState() {
				HughieLoggerManager.println("�������");
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}

package com.hughie.linkgame;

import java.util.ArrayList;
import java.util.List;

import com.hughie.link.support.common.HughieLoggerManager;
import com.hughie.linkgame.adapter.HughieDesktopSidebarAdapter;
import com.hughie.linkgame.adapter.HughieDesktopSidebarAdapter.OnDesktopSidebarItemClickListener;
import com.hughie.linkgame.common.HughieApplicationManager;
import com.hughie.linkgame.common.HughieGameApplication;
import com.hughie.linkgame.entity.ScreenDesktopSidebarDetail;
import com.hughie.linkgame.screen.HughieScreenDesktop;
import com.hughie.linkgame.screen.HughieScreenTab;
import com.hughie.linkgame.utils.HughieActivityUtils;
import com.hughie.linkgame.utils.HughieDesktopUtils;
import com.hughie.linkgame.widget.HughieFlipperLayout;
import com.hughie.linkgame.widget.HughieRoundImageView;
import com.hughie.linkgame.widget.HughieFlipperLayout.OnOpenHomeListener;
import com.umeng.analytics.MobclickAgent;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

/**
 * @description 主界面Activity
 * @ClassName: HughieMainActivity
 * @author hughiezhang
 * @since 2015-09-06 16:30
 */
public class HughieMainActivity extends TabActivity implements OnOpenHomeListener, 
		OnDesktopSidebarItemClickListener{
	public static HughieGameApplication mGameApplication;
	
	//当前显示内容的容器(继承于ViewGroup)
	private HughieFlipperLayout mScreenRoot;
	private HughieScreenDesktop mScreenDesktop;//菜单界面
	private HughieScreenTab mScreenTab;//tab界面
	
	//screen desktop布局
	private HughieRoundImageView mDesktopGameIconImgv;
	private TextView mDesktopGameNameTv;
	private TextView mDesktopGameDescriptionTv;
	private ListView mDesktopSidebarLv;
	private HughieDesktopSidebarAdapter mSidebarAdapter;
	
	private TabHost mTabs;
	
	//screen tab布局
	private RadioGroup mScreenTabRg;
	private RadioButton mScreenTabGameRb;
	private RadioButton mScreenTabInfoRb;
	private RadioButton mScreenTabCircleRb;
	private RadioButton mScreenTabMineRb;
	
	private ArrayList<ScreenDesktopSidebarDetail> mSidebarDetailList;
	private int[] mSidebarImgvs = new int[] {R.drawable.hughie_screen_desktop_sidebar_game_selector,
			R.drawable.hughie_screen_desktop_sidebar_information_selector, R.drawable.hughie_screen_desktop_sidebar_circle_selector, 
			R.drawable.hughie_screen_desktop_sidebar_mine_selector};
	private String[] mSidebarNames;
	
	private String TAB_GAME;							//游戏
	private String TAB_INFORMATION;				//咨讯
	private String TAB_CIRCLE;							//圈子
	private String TAB_MINE;							//我的
	private int CURRENT_RADIO_ID;
	
	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mContext = this;
		HughieGameApplication.setHughieActivityTheme(this);
		HughieApplicationManager.getInstance().addActivity(this);
		mGameApplication = (HughieGameApplication)getApplication();
		
		if (savedInstanceState != null) {
			HughieLoggerManager.println("哈哈,我又活过来了.在planB里面");
		}
		
		//创建容器,并设置全屏大小
		mScreenRoot = new HughieFlipperLayout(mContext);
		LayoutParams mScreenRootParams = new LayoutParams(LayoutParams.MATCH_PARENT, 
				LayoutParams.MATCH_PARENT);
		mScreenRoot.setLayoutParams(mScreenRootParams);
		
		//创建菜单界面和home主界面,并添加到容器中,用于初始显示
		mScreenDesktop = new HughieScreenDesktop(mContext);
		mScreenTab = new HughieScreenTab(mContext);
		mScreenRoot.addView(mScreenDesktop.getView(), mScreenRootParams);
		mScreenRoot.addView(mScreenTab.getView(), mScreenRootParams);
		
		setContentView(mScreenRoot);
		
		initScreenDesktopView();
		initScreenTabView();
		initScreenTabString();
		initScreenTabWidget();
		setCurrentScreenTab();
	}
	
	private void initScreenDesktopView(){
		mDesktopGameIconImgv = (HughieRoundImageView)mScreenDesktop.getView().findViewById(R.id.screen_desktop_game_icon_imgv);
		mDesktopGameNameTv = (TextView)mScreenDesktop.getView().findViewById(R.id.screen_desktop_game_name_tv);
		mDesktopGameNameTv.setTypeface(HughieActivityUtils.getFontType(mContext, "hobostd.otf"));
		mDesktopGameNameTv.setText(R.string.str_screen_desktop_game_name_txt);
		mDesktopGameDescriptionTv = (TextView)mScreenDesktop.getView().findViewById(R.id.screen_desktop_game_description_tv);
		mDesktopGameDescriptionTv.setTypeface(HughieActivityUtils.getFontType(mContext, "hobostd.otf"));
		mDesktopGameDescriptionTv.setText(R.string.str_screen_desktop_game_description_txt);
		mDesktopSidebarLv = (ListView)mScreenDesktop.getView().findViewById(R.id.screen_desktop_sidebar_lv);
		initSidebarData();
		HughieLoggerManager.println(mSidebarDetailList.toString());
		setSidebarAdapter(mSidebarDetailList);
	}
	
	private void initSidebarData(){
		mSidebarDetailList = new ArrayList<ScreenDesktopSidebarDetail>();
		mSidebarNames = getResources().getStringArray(R.array.screen_desktop_sidebar_name);
		for(int i = 0; i < mSidebarImgvs.length; i++){
			ScreenDesktopSidebarDetail screenDesktopSidebarDetail = new ScreenDesktopSidebarDetail();
			if(i < mSidebarNames.length){
				screenDesktopSidebarDetail.setSidebarImgv(mSidebarImgvs[i]);
				screenDesktopSidebarDetail.setSidebarName(mSidebarNames[i]);
				mSidebarDetailList.add(screenDesktopSidebarDetail);
			}
		}
	}
	
	//配置desktop sidebar的adapter
	private void setSidebarAdapter(ArrayList<ScreenDesktopSidebarDetail> sidebarDetailList){
		//判断sidebar listview的adapter是否为空
		if(mDesktopSidebarLv.getAdapter() == null){
			mSidebarAdapter = new HughieDesktopSidebarAdapter(mContext, sidebarDetailList, this);
			mDesktopSidebarLv.setAdapter(mSidebarAdapter);
		} else {
			if(sidebarDetailList != null && sidebarDetailList.size() != 0){
				mSidebarAdapter.getSidebarDetailList().clear();
				mSidebarAdapter.setSidebarDetailList(sidebarDetailList);
				mSidebarAdapter.notifyDataSetChanged();
			}
		}
	}
	
	private void initScreenTabView() {
		mScreenTabRg = (RadioGroup)mScreenTab.getView().findViewById(R.id.screen_tab_rg);
		mScreenTabGameRb = (RadioButton)mScreenTab.getView().findViewById(R.id.screen_tab_game_rb);
		mScreenTabGameRb.setTypeface(HughieActivityUtils.getFontType(mContext, "hobostd.otf"));
		mScreenTabInfoRb = (RadioButton)mScreenTab.getView().findViewById(R.id.screen_tab_information_rb);
		mScreenTabInfoRb.setTypeface(HughieActivityUtils.getFontType(mContext, "hobostd.otf"));
		mScreenTabCircleRb = (RadioButton)mScreenTab.getView().findViewById(R.id.screen_tab_circle_rb);
		mScreenTabCircleRb.setTypeface(HughieActivityUtils.getFontType(mContext, "hobostd.otf"));
		mScreenTabMineRb = (RadioButton)mScreenTab.getView().findViewById(R.id.screen_tab_mine_rb);
		mScreenTabMineRb.setTypeface(HughieActivityUtils.getFontType(mContext, "hobostd.otf"));
	}
	
	private void initScreenTabString(){
		TAB_GAME = getString(R.string.str_screen_tab_game_txt);
		TAB_INFORMATION = getString(R.string.str_screen_tab_information_txt);
		TAB_CIRCLE = getString(R.string.str_screen_tab_circle_txt);
		TAB_MINE = getString(R.string.str_screen_tab_mine_txt);
		CURRENT_RADIO_ID = R.id.screen_tab_game_rb;
	}
	
	private void initScreenTabWidget(){
		mTabs = getTabHost();
		TabSpec mSpec;
		//游戏
		mSpec = mTabs.newTabSpec(TAB_GAME).setIndicator(TAB_GAME);
		mSpec.setContent(new Intent(mContext, HughieGameHomeActivity.class));
		mTabs.addTab(mSpec);
		
		// 资讯
		mSpec = mTabs.newTabSpec(TAB_INFORMATION).setIndicator(TAB_INFORMATION);
		mSpec.setContent(new Intent(mContext, HughieInformationHomeActivity.class));
		mTabs.addTab(mSpec);
		
		//圈子
		mSpec = mTabs.newTabSpec(TAB_CIRCLE).setIndicator(TAB_CIRCLE);
		mSpec.setContent(new Intent(mContext, HughieCommunityHomeActivity.class));
		mTabs.addTab(mSpec);
		
		// 我的
		mSpec = mTabs.newTabSpec(TAB_MINE).setIndicator(TAB_MINE);
		mSpec.setContent(new Intent(mContext, HughieUserCenterActivity.class));
		mTabs.addTab(mSpec);
		
		mScreenTabRg.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId){
					case R.id.screen_tab_game_rb://游戏
						mTabs.setCurrentTabByTag(TAB_GAME);
						break;
					case R.id.screen_tab_information_rb://咨讯
						mTabs.setCurrentTabByTag(TAB_INFORMATION);
						break;
					case R.id.screen_tab_circle_rb://圈子
						mTabs.setCurrentTabByTag(TAB_CIRCLE);
						break;
					case R.id.screen_tab_mine_rb://我的
						mTabs.setCurrentTabByTag(TAB_MINE);
						break;
				}
			}
		});
	}
	
	private void setCurrentScreenTab(){
		mScreenTabRg.check(CURRENT_RADIO_ID);
	}
	
	/**
	 * 修改头像
	 * @param bitmap
	 * 修改的头像
	 */
	public void setDesktopIcon(Bitmap bitmap) {
		mDesktopGameIconImgv.setImageBitmap(bitmap);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(mContext);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(mContext);
	}
	

	@Override
	public void open() {
		if(mScreenRoot.getScreenState() == HughieFlipperLayout.SCREEN_STATE_CLOSE) {
			mScreenRoot.open();
		}
	}

	@Override
	public void onDesktopSidebarItemClick(int position) {
		switch(position){
			case HughieDesktopUtils.SIDEBAR_GAME:
				mScreenTabRg.check(CURRENT_RADIO_ID);
				mTabs.setCurrentTabByTag(TAB_GAME);
				open();
				break;
		}
	}
}

package com.hughie.linkgame.widget;

import java.util.ArrayList;

import com.hughie.linkgame.R;
import com.hughie.linkgame.common.HughieCallBackMe;
import com.hughie.linkgame.utils.HughieActivityUtils;
import com.hughie.linkgame.utils.HughieTVFitUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * welcome guidey引导页显示
 * @ClassName: HughieGuideViewPager
 * @author hughiezhang
 * @since 2015-08-31 17:19
 */
public class HughieGuideViewPager {
	private Context mContext;
	private ArrayList<View> mPageViews;
	private ViewPager mViewPager;
	private ViewGroup mGuideIndicatorLayout;
	private ImageView[] mPageIndicatorImgvs;
	private ImageView mPageIndicatorImgv;
	private HughieCallBackMe mHughieCallBackMe;
	
	private int[] mImgvIds;
	private int[] mBgIds;
	private int[] mTvIds;
	
	public HughieGuideViewPager(Context context, ViewPager viewPager, LinearLayout guideIndicatorLayout) {
		this.mContext = context;
		this.mViewPager = viewPager;
		this.mGuideIndicatorLayout = guideIndicatorLayout;
	}
	
	private final void initDefault(){
		if(mImgvIds == null){
			this.mImgvIds = new int[0];
		}
		
		if(mBgIds == null){
			this.mBgIds = new int[0];
		}
		
		if(mTvIds == null){
			this.mTvIds = new int[0];
		}
	}
	
	public final void show() {
		initDefault();
		mPageViews = new ArrayList<View>();
		for(int i = 0; i < mImgvIds.length; i++){
			LinearLayout mGuideLayout = null;
			if(i < mImgvIds.length - 1){
				mGuideLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_hughie_guide_pager, null);
				mGuideLayout.setBackgroundResource(mBgIds[i]);
				ImageView mGuideImgv = (ImageView)mGuideLayout.findViewById(R.id.guide_pager_feature_imgv);
				mGuideImgv.setImageResource(mImgvIds[i]);
				TextView mGuideTv = (TextView)mGuideLayout.findViewById(R.id.guide_pager_feature_tv);
				mGuideTv.setTypeface(HughieActivityUtils.getFontType(mContext, "hobostd.otf"));
				mGuideTv.setText(mTvIds[i]);
			}else{
				mGuideLayout = new LinearLayout(mContext);
				LinearLayout.LayoutParams ltp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT);
				ImageView imageView = new ImageView(mContext);
				imageView.setScaleType(ScaleType.CENTER_CROP);
				HughieTVFitUtils.setTVImageScale((Activity) mContext, imageView);
				imageView.setImageResource(mImgvIds[i]);
				mGuideLayout.addView(imageView, ltp);
			}
			
			mPageViews.add(mGuideLayout);
		}
		
		mPageIndicatorImgvs = new ImageView[mPageViews.size()];
		for(int i = 0; i < mPageViews.size(); i++){
			mPageIndicatorImgv = new ImageView(mContext);
			mPageIndicatorImgv.setPadding(10, 0, 10, 0);
			mPageIndicatorImgvs[i] = mPageIndicatorImgv;
			// 默认选中第一张图片
			if(i == 0){
				mPageIndicatorImgvs[i].setImageResource(R.drawable.imgv_page_indicator_hot);
			} else {
				mPageIndicatorImgvs[i].setImageResource(R.drawable.imgv_page_indicator_normal);
			}
			
			mGuideIndicatorLayout.addView(mPageIndicatorImgvs[i]);
		}
		
		mViewPager.setAdapter(new GuidePageAdapter());
		mViewPager.setOnPageChangeListener(new GuidePageChangeListener());
	}
	
	// 指引页面数据适配器
	class GuidePageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mPageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView(mPageViews.get(position));
		}
		
		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager)container).addView(mPageViews.get(position));
			return mPageViews.get(position);
		}
		
		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
			
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
		
		@Override
		public void startUpdate(View container) {

		}

		@Override
		public void finishUpdate(View container) {

		}
	}
	
	// 指引页面更改事件监听器
	class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		@Override
		public void onPageSelected(int arg0) {
			if(arg0 == mPageIndicatorImgvs.length - 1){
				if(mHughieCallBackMe != null){
					mHughieCallBackMe.callback();
				}
				
				return;
			}
			
			for(int i = 0; i < mPageIndicatorImgvs.length; i++){
				mPageIndicatorImgvs[arg0].setImageResource(R.drawable.imgv_page_indicator_hot);
				if (arg0 != i) {
					mPageIndicatorImgvs[i].setImageResource(R.drawable.imgv_page_indicator_normal);
				}
			}
		}
	}
	

	public int[] getmImgvIds() {
		return mImgvIds;
	}

	public void setmImgvIds(int[] mImgvIds) {
		this.mImgvIds = mImgvIds;
	}

	public int[] getmBgIds() {
		return mBgIds;
	}

	public void setmBgIds(int[] mBgIds) {
		this.mBgIds = mBgIds;
	}

	public HughieCallBackMe getmHughieCallBackMe() {
		return mHughieCallBackMe;
	}

	public void setmHughieCallBackMe(HughieCallBackMe mHughieCallBackMe) {
		this.mHughieCallBackMe = mHughieCallBackMe;
	}

	public int[] getmTvIds() {
		return mTvIds;
	}

	public void setmTvIds(int[] mTvIds) {
		this.mTvIds = mTvIds;
	}
}

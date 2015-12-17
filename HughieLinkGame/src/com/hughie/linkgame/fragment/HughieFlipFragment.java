package com.hughie.linkgame.fragment;

import java.util.ArrayList;

import com.hughie.linkgame.R;
import com.hughie.linkgame.widget.HughiePageControl;
import com.hughie.linkgame.widget.HughieViewPager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;

/**
 * 可以自动切屏的Fragment，用于广告横幅
 * @ClassName: HughieFlipFragment
 * @author hughiezhang
 * @since 2015-09-17 11:08
 */
public class HughieFlipFragment extends Fragment {
	/** 通过此参数名传递一个要轮换的图片路径数组,必须 */
	public final static String ARG_IMAGES_ARRAY = "images";

	/** 通过此参数名传递一个整形值来说明图片的宽度 */
	public final static String ARG_IMAGE_WIDTH = "image_width";
	/** 通过此参数名传递一个整形值来说明图片的高度 */
	public final static String ARG_IMAGE_HEIGHT = "image_height";

	/** 通过此参数名传递默认图片的resource id */
	public final static String ARG_IMAGE_DEFAULT = "image_default";

	/**
	 * 
	 * 作者：lisheng@yunlai.cn<br>
	 * 创建日期:2013-6-22<br>
	 * 类描述：当图片被点击时的回调<br>
	 */
	public static interface OnImageItemClickListener {
		/**
		 * 
		 * @param index
		 *            图片在你传递的数组中的索引
		 */
		void onClick(int index);
	}

	private String mImages[];
	private Handler mHandler;
	private int mWidth, mHeight, mDefaultImage, mCurrentItem;

	private ViewPager mPager;
	private HughiePageControl mIndicator;

	private boolean ready;

	private OnImageItemClickListener mOnImageItemClickListener;

	/**
	 * 设置点击图片时的监听，例子程序请看{@link HomeIndexFragment#onActivityCreated(Bundle)}以及
	 * {@link HomeIndexFragment#initView}
	 * 
	 * @param listener
	 */
	public void setOnImageItemClickListener(OnImageItemClickListener listener) {
		this.mOnImageItemClickListener = listener;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("current_item", mCurrentItem);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		initArguments(savedInstanceState);
		final Context context = getActivity();
		FrameLayout root = new FrameLayout(context);

		mPager = new HughieViewPager(context);
		mPager.setId(0x00ff0001);
		mPager.setAdapter(new FlipAdapter(getActivity(), mOnImageItemClickListener, mImages, mWidth, mHeight,
				mDefaultImage));
		//mPager.setBackgroundResource(R.color.white);
		
		if (mCurrentItem==0&&mImages.length > 1) {
			mCurrentItem = mImages.length * 1001;
		}

		mPager.setCurrentItem(mCurrentItem);

		mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				mCurrentItem = position;
				mIndicator.setCurrentPage(mCurrentItem % mImages.length);
			}
		});

		mPager.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});

		root.addView(mPager, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));

		mIndicator = new HughiePageControl(context);
		mIndicator.setPageCount(mImages.length);
		mIndicator.setCurrentPage(mCurrentItem % mImages.length);
		root.addView(mIndicator, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL));

		mIndicator.setVisibility(mImages.length > 1 ? View.VISIBLE : View.GONE);

		root.setLayoutParams(new FrameLayout.LayoutParams(mWidth, mHeight));
		ready = true;
		return root;
	}

	private void initArguments(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			mCurrentItem = savedInstanceState.getInt("current_item");
		}
		mImages = getArguments().getStringArray(ARG_IMAGES_ARRAY);
		if (mImages == null || mImages.length < 1) {
			throw new IllegalArgumentException("图片数组不能为空且长度必须大于0");
		}

		mWidth = getArguments().getInt(ARG_IMAGE_WIDTH, ViewGroup.LayoutParams.WRAP_CONTENT);
		mHeight = getArguments().getInt(ARG_IMAGE_HEIGHT, ViewGroup.LayoutParams.WRAP_CONTENT);
		mDefaultImage = getArguments().getInt(ARG_IMAGE_DEFAULT);

	}

	@Override
	public void onDestroyView() {
		mPager = null;
		mIndicator = null;
		ready = false;
		super.onDestroyView();
	}

	@Override
	public void onResume() {
		super.onResume();
		if(!interrupt){
			if (mHandler == null) {
				mHandler = new Handler();
			}
			mHandler.postDelayed(mFlipTask, 3000);
		}
	}
	
	
	boolean interrupt;
	
	public void resume(){
		interrupt=false;
		if (mHandler == null) {
			mHandler = new Handler();
		}
		//mHandler.postDelayed(mFlipTask, 3000);
		mHandler.postDelayed(mFlipTask, 5000);
	}
	
	
	public void pause(){
		interrupt=true;
		if (mHandler != null) {
			mHandler.removeCallbacks(mFlipTask);
			mHandler = null;
		}
	}
	

	@Override
	public void onPause() {
		if(!interrupt){
			if (mHandler != null) {
				mHandler.removeCallbacks(mFlipTask);
				mHandler = null;
			}
		}
		super.onPause();

	}

	Runnable mFlipTask = new Runnable() {
		@Override
		public void run() {
			if (isVisible() && ready) {
				mPager.setCurrentItem(++mCurrentItem, true);
			}
			//mHandler.postDelayed(mFlipTask, 3000);
			mHandler.postDelayed(mFlipTask, 5000);
		}
	};

	public static class FlipAdapter extends PagerAdapter {
		private String[] mImages;
		private int mSize, mWidth, mHeight, mPreset;
		private Context mContext;
		private ArrayList<View> mViews = new ArrayList<View>();
		private DisplayImageOptions options;
		private OnImageItemClickListener mListener;

		public FlipAdapter(Context context, OnImageItemClickListener listener, String[] images, int width, int height,
				int preset) {
			mImages = images;
			mSize = mImages.length;
			mContext = context;
			mWidth = width;
			mHeight = height;
			mPreset = preset;

			mListener = listener;
			options = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc().build();

		}

		@Override
		public int getCount() {
			return mSize == 1 ? 1 : Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			if (mSize > 1) {
				position = position % (mSize * 2);
			}

			if (mViews.size() > position) {
				View v = mViews.get(position);
				if (v != null) {
					container.addView(v);
					return v;
				}
			}

			FrameLayout root = new FrameLayout(mContext);
			// ---------------------
			ImageView iv = new ImageView(mContext);
			iv.setBackgroundResource(R.color.color_white);
			iv.setScaleType(ScaleType.CENTER_CROP);
			//iv.setScaleType(ScaleType.FIT_XY);
			if (mPreset != 0) {
				iv.setImageResource(mPreset);
			}

			if (mListener != null) {
				final int index = position % mSize;
				iv.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mListener.onClick(index);
					}
				});
			}

			root.addView(iv, new FrameLayout.LayoutParams(mWidth, mHeight));

			// --------------

			final ProgressBar progress = new ProgressBar(mContext, null, android.R.attr.progressBarStyle);
			root.addView(progress, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));

			ImageLoader.getInstance().displayImage(mImages[position % mSize], iv, options,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
							progress.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
							progress.setVisibility(View.GONE);
						}
					});

			root.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT));

			while (mViews.size() <= position) {
				mViews.add(null);
			}

			mViews.set(position, root);
			container.addView(mViews.get(position));
			return mViews.get(position);
		}

	}
}

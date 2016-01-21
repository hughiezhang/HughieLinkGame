package com.hughie.linkgame.widget;

import com.hughie.linkgame.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HughiePagerSlidingTabStrip extends HorizontalScrollView {
	private LinearLayout mTabsContainer;
	private ViewPager mPager;
	
	private int mTabsCount;																// tabs的tab数量
	
	private int mCurrentPosition = 0;													// tabs当前的位置
	private float mCurrentPositionOffset = 0f;										// tabs当前位置滑动的偏移量
	
	private Paint mRectPaint;																// 设置绘制tabs矩形的画笔
	
	private int mIndicatorColor = 0xFF666666;										// 设置tabs指示器的颜色
	private int mIndicatorResource = 0;												// 设置tabs指示器资源
	private Bitmap mIndicatorBitmap;													// 设置tabs指示器的图片资源
	
	private int mScrollOffset = 52;
	private int mIndicatorHeight = 8;													// 设置tabs指示器的高度
	private float mIndicatorWidthPercent = 1.0f;									// 设置tabs指示器的宽度比例
	private int mUnderlineHeight = 2;													// 设置tabs指示器底部线条的高度
	
	private int mTabsTextSize = 12;														// 设置tabs的字体大小
	private int mTabsTextColor = 0xFF666666;										// 设置tabs的字体颜色
	
	private final PageListener mPageListener = new PageListener();
	
	// @formatter:off
	private static final int[] ATTRS = new int[] { android.R.attr.textSize, android.R.attr.textColor };
	
	public HughiePagerSlidingTabStrip(Context context) {
		this(context, null);
	}
	
	public HughiePagerSlidingTabStrip(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public HughiePagerSlidingTabStrip(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		setFillViewport(true);
		setWillNotDraw(false);
		
		mTabsContainer = new LinearLayout(context);
		mTabsContainer.setOrientation(LinearLayout.HORIZONTAL);
		mTabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 
				LayoutParams.MATCH_PARENT));
		addView(mTabsContainer);
		
		DisplayMetrics mDisplayMetrics = getResources().getDisplayMetrics();
		
		mScrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
				mScrollOffset, mDisplayMetrics);
		mIndicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
				mIndicatorHeight, mDisplayMetrics);
		mUnderlineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
				mUnderlineHeight, mDisplayMetrics);
		mTabsTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 
				mTabsTextSize, mDisplayMetrics);
		
		// get system attrs (android:textSize and android:textColor)
		TypedArray mTypedArray = context.obtainStyledAttributes(ATTRS);
		
		mTabsTextSize = mTypedArray.getDimensionPixelSize(0, mTabsTextSize);
		mTabsTextColor = mTypedArray.getColor(1, mTabsTextColor);
		
		mTypedArray.recycle();
		
		// get custom attrs
		mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStrip);
		
		// tabs指示器的颜色
		mIndicatorColor = mTypedArray.getColor(R.styleable.PagerSlidingTabStrip_indicatorColor, mIndicatorColor);
		mIndicatorHeight = mTypedArray.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_indicatorHeight, 
				mIndicatorHeight);// tabs指示器的高度
		mIndicatorResource = mTypedArray.getResourceId(R.styleable.PagerSlidingTabStrip_indicatorResource, 
				mIndicatorResource);// tabs指示器资源
		mUnderlineHeight = mTypedArray.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_underlineHeight, 
				mUnderlineHeight);// tabs指示器底部线条的高度
		
		mTypedArray.recycle();
		
		mRectPaint = new Paint();
		mRectPaint.setAntiAlias(true);
		mRectPaint.setStyle(Style.FILL);
		
		if(mIndicatorResource > 0) {
			int mScreenWidth = mDisplayMetrics.widthPixels;
			mIndicatorBitmap = BitmapFactory.decodeResource(getResources(), mIndicatorResource);
			mIndicatorBitmap = Bitmap.createScaledBitmap(mIndicatorBitmap, mScreenWidth, mIndicatorHeight, false);
		}
	}
	
	public void setViewPager(ViewPager pager) {
		this.mPager = pager;
		if(mPager.getAdapter() == null) 
			throw new IllegalStateException("ViewPager does not have adapter instance.");
		
		mPager.setOnPageChangeListener(listener);
	}
	
	private void updateTabStyles() {
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private void scrollToChild(int position, int offset) {
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// draw indicator line
		mRectPaint.setColor(mIndicatorColor);
		
		// default: line below current tab
		View mCurrentTabs = mTabsContainer.getChildAt(mCurrentPosition);
		if(mCurrentTabs instanceof TextView) {

		}
	}
	
	private class PageListener implements OnPageChangeListener {
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			mCurrentPosition = position;
			mCurrentPositionOffset = positionOffset;
			
			scrollToChild(position, (int) positionOffset * mTabsContainer.getChildAt(position).getWidth());
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}

	public int getScrollOffset() {
		return mScrollOffset;
	}

	public void setScrollOffset(int mScrollOffset) {
		this.mScrollOffset = mScrollOffset;
		invalidate();
	}

	public int getTabsTextSize() {
		return mTabsTextSize;
	}

	public void setTabsTextSize(int tabsTextSize) {
		this.mTabsTextSize = tabsTextSize;
		updateTabStyles();
	}

	public int getTabsTextColor() {
		return mTabsTextColor;
	}

	public void setTabsTextColor(int tabsTextColor) {
		this.mTabsTextColor = tabsTextColor;
		updateTabStyles();
	}

	public int getIndicatorHeight() {
		return mIndicatorHeight;
	}

	public void setIndicatorHeight(int indicatorHeight) {
		this.mIndicatorHeight = indicatorHeight;
		invalidate();
	}

	public int getmIndicatorColor() {
		return mIndicatorColor;
	}

	public void setmIndicatorColor(int mIndicatorColor) {
		this.mIndicatorColor = mIndicatorColor;
		invalidate();
	}

	public int getUnderlineHeight() {
		return mUnderlineHeight;
	}

	public void setUnderlineHeight(int underlineHeight) {
		this.mUnderlineHeight = underlineHeight;
		invalidate();
	}
	
	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		SavedState savedState = (SavedState) state;
		super.onRestoreInstanceState(state);
		mCurrentPosition = savedState.mCurrentPosition;
		requestLayout();
	}
	
	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState savedState = new SavedState(superState);
		savedState.mCurrentPosition = mCurrentPosition;
		return super.onSaveInstanceState();
	}
	
	
	static class SavedState extends BaseSavedState {
		int mCurrentPosition;
		
		public SavedState(Parcelable superState) {
			super(superState);
		}

		public SavedState(Parcel source) {
			super(source);
			mCurrentPosition = source.readInt();
		}
		
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeInt(mCurrentPosition);
		}
		
		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
			@Override
			public SavedState createFromParcel(Parcel source) {
				return new SavedState(source);
			}

			@Override
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}
}

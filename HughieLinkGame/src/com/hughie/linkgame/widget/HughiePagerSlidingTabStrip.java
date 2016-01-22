package com.hughie.linkgame.widget;

import java.util.Locale;

import com.hughie.linkgame.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HughiePagerSlidingTabStrip extends HorizontalScrollView {
	public interface IconTabProvider {
		public int getPageIconResId(int position);
	}
	
	private LinearLayout mTabsContainer;
	private ViewPager mPager;
	
	private int mTabsCount;																// tabs��tab����
	
	private LinearLayout.LayoutParams mDefaultTabLayoutParams;			// ����Ĭ��tab�Ĳ��ֵĴ�С��λ��
	private LinearLayout.LayoutParams mExpandedTabLayoutParams;		// ���ÿɻ���tab�Ĳ��ִ�С��λ��
	private LinearLayout.LayoutParams mCurrentTabLayoutParams;		// ���õ�ǰtab�Ĳ��ִ�С��λ��
	
	private int mCurrentPosition = 0;													// tabs��ǰ��λ��
	private float mCurrentPositionOffset = 0f;										// tabs��ǰλ�û�����ƫ����
	
	// tab�ı���
	private int mTabBackgroundResId = R.drawable.hughie_game_sliding_tab_background;
	
	private Paint mRectPaint;																// ���û���tabs���εĻ���
	
	private int mIndicatorColor = 0xFF666666;										// ����tabsָʾ������ɫ
	private int mIndicatorResource = 0;												// ����tabsָʾ����Դ
	private Bitmap mIndicatorBitmap;													// ����tabsָʾ����ͼƬ��Դ
	private Rect mIndicatorSrcRect;
	private RectF mIndicatorDstRectF;
	
	private int mScrollOffset = 52;
	private int mIndicatorHeight = 8;													// ����tabsָʾ���ĸ߶�
	private float mIndicatorWidthPercent = 1.0f;									// ����tabsָʾ���Ŀ�ȱ���
	private int mUnderlineHeight = 2;													// ����tabsָʾ���ײ������ĸ߶�
	private int mTabPadding = 24;														// ����tab�ļ��
	
	private int mSelectTextColor = 0xFF666666;									// ����tabs��ǰѡ��tab��������ɫ
	
	private boolean mCheckedTabWidths = false;
	
	private boolean mShouldExpand = false;										// ����tabs�Ƿ���Թ���
	private boolean mTextAllCaps = true;											// ����tab�����Ƿ��д
	private Locale mLocale;																	// ����tab�����local
	
	private int mLastScrollX = 0;															// ����tabs���λ�û�����ƫ����
	private int mLastIndex = 0;															// ��¼tabs�������indexλ��
	private int mCurIndex = 0;																// ��¼tabs��ǰ��indexλ��
	
	private int mTabsTextSize = 12;														// ����tabs�������С
	private int mTabsTextColor = 0xFF666666;										// ����tabs��������ɫ
	private Typeface mTabTypeface = null;											// ����tab������
	private int mTabTypefaceStyle = Typeface.NORMAL;						// ����tab�������ʽ
	
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
		mTabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
				mTabPadding, mDisplayMetrics);
		mTabsTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 
				mTabsTextSize, mDisplayMetrics);
		
		// get system attrs (android:textSize and android:textColor)
		TypedArray mTypedArray = context.obtainStyledAttributes(ATTRS);
		
		mTabsTextSize = mTypedArray.getDimensionPixelSize(0, mTabsTextSize);
		mTabsTextColor = mTypedArray.getColor(1, mTabsTextColor);
		
		mTypedArray.recycle();
		
		// get custom attrs
		mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStrip);
		
		mIndicatorColor = mTypedArray.getColor(R.styleable.PagerSlidingTabStrip_indicatorColor, 
				mIndicatorColor);// tabsָʾ������ɫ
		mSelectTextColor = mTypedArray.getColor(R.styleable.PagerSlidingTabStrip_selectTextColor, 
				mSelectTextColor);// tabs��ǰѡ��tab��������ɫ
		mIndicatorHeight = mTypedArray.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_indicatorHeight, 
				mIndicatorHeight);// tabsָʾ���ĸ߶�
		mIndicatorResource = mTypedArray.getResourceId(R.styleable.PagerSlidingTabStrip_indicatorResource, 
				mIndicatorResource);// tabsָʾ����Դ
		mUnderlineHeight = mTypedArray.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_underlineHeight, 
				mUnderlineHeight);// tabsָʾ���ײ������ĸ߶�
		mTabPadding = mTypedArray.getResourceId(R.styleable.PagerSlidingTabStrip_tabPaddingLeftRight, 
				mTabPadding);// tab�ļ��
		mTabBackgroundResId = mTypedArray.getResourceId(R.styleable.PagerSlidingTabStrip_tabBackground, 
				mTabBackgroundResId);// tab�ı���
		mShouldExpand = mTypedArray.getBoolean(R.styleable.PagerSlidingTabStrip_shouldExpand, 
				mShouldExpand);// tabs�Ƿ���Թ���
		mScrollOffset = mTypedArray.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_scrollOffset, mScrollOffset);
		mTextAllCaps = mTypedArray.getBoolean(R.styleable.PagerSlidingTabStrip_textAllCaps, 
				mTextAllCaps);//	tab�����Ƿ��д
		
		mTypedArray.recycle();
		
		mRectPaint = new Paint();
		mRectPaint.setAntiAlias(true);
		mRectPaint.setStyle(Style.FILL);
		
		mDefaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
				LayoutParams.MATCH_PARENT);
		mExpandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
		if(mShouldExpand) {
			mCurrentTabLayoutParams = mExpandedTabLayoutParams;
		} else {
			mCurrentTabLayoutParams = mDefaultTabLayoutParams;
		}
		
		if(mLocale == null)
			mLocale = getResources().getConfiguration().locale;
		
		if(mIndicatorResource > 0) {
			int mScreenWidth = mDisplayMetrics.widthPixels;
			mIndicatorBitmap = BitmapFactory.decodeResource(getResources(), mIndicatorResource);
			mIndicatorBitmap = Bitmap.createScaledBitmap(mIndicatorBitmap, mScreenWidth, mIndicatorHeight, false);
			mIndicatorSrcRect = new Rect();
			mIndicatorDstRectF = new RectF();
		}
	}
	
	public void setViewPager(ViewPager pager) {
		this.mPager = pager;
		if(mPager.getAdapter() == null) 
			throw new IllegalStateException("ViewPager does not have adapter instance.");
		
		mPager.setOnPageChangeListener(listener);
		
		notifyDataSetChanged();
	}
	
	public void notifyDataSetChanged() {
		mLastIndex = 0;
		mCurIndex = 0;
		mTabsContainer.removeAllViews();
		
		mTabsCount = mPager.getAdapter().getCount();
		for(int i = 0; i < mTabsCount; i++) {
			if(mPager.getAdapter() instanceof IconTabProvider) {
				addIconTab(i, ((IconTabProvider) mPager.getAdapter()).getPageIconResId(i));
			} else {
				addTextTab(i, mPager.getAdapter().getPageTitle(i).toString());
			}
		}
		
		updateTabStyles();
		
		mCheckedTabWidths = false;
		
		getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				getViewTreeObserver().removeGlobalOnLayoutListener(this);
				mCurrentPosition = mPager.getCurrentItem();
				scrollToChild(mCurrentPosition, 0);
			}
		});
	}
	
	private void addTextTab(final int position, String title) {
		TextView mTab = new TextView(getContext());
		mTab.setText(title);
		mTab.setFocusable(true);
		mTab.setGravity(Gravity.CENTER);
		mTab.setSingleLine();
		
		mTab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPager.setCurrentItem(position);
			}
		});
		
		mTabsContainer.addView(mTab);
	}
	
	private void addIconTab(final int position, int resId) {
		ImageButton mTab = new ImageButton(getContext());
		mTab.setFocusable(true);
		mTab.setImageResource(resId);
		
		mTab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPager.setCurrentItem(position);
			}
		});
		
		mTabsContainer.addView(mTab);
	}
	
	private void updateTabStyles() {
		for(int i = 0; i < mTabsCount; i++) {
			View v = mTabsContainer.getChildAt(i);
			v.setLayoutParams(mCurrentTabLayoutParams);
			v.setBackgroundResource(mTabBackgroundResId);
			if(mShouldExpand) {
				v.setPadding(0, 0, 0, 0);
			} else {
				v.setPadding(mTabPadding, 0, mTabPadding, 0);
			}
			
			if(v instanceof TextView) {
				TextView mTab = (TextView) v;
				mTab.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTabsTextSize);
				mTab.setTypeface(mTabTypeface, mTabTypefaceStyle);
				mTab.setTextColor(mTabsTextColor);
				
				// setAllCaps() is only available from API 14, so the upper case
				// is made manually if we are on a
				// pre-ICS-build
				if(mTextAllCaps)
					mTab.setText(mTab.getText().toString().toUpperCase(mLocale));
			}
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		if(!mShouldExpand || MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED)
			return;
		
		int mMyWidth = getMeasuredWidth();
		int mChildWidth = 0;
		for(int i = 0; i < mTabsCount; i++) {
			mChildWidth += mTabsContainer.getChildAt(i).getMeasuredWidth();
		}
		
		if(!mCheckedTabWidths && mMyWidth > 0 && mChildWidth > 0) {
			if(mChildWidth < mMyWidth) {
				for(int i = 0; i < mTabsCount; i++) {
					mTabsContainer.getChildAt(i).setLayoutParams(mExpandedTabLayoutParams);
				}
			}
			
			mCheckedTabWidths = true;
		}
	}
	
	private void scrollToChild(int position, int offset) {
		if(mTabsCount == 0)
			return;
		
		int mNewScrollX = mTabsContainer.getChildAt(position).getLeft() + offset;
		
		if(position > 0 ||  offset > 0)
			mNewScrollX -= mScrollOffset;
		
		if(mNewScrollX != mLastScrollX) {
			mLastScrollX = mNewScrollX;
			scrollTo(mNewScrollX, 0);
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if(isInEditMode() || mTabsCount == 0)
			return;
		
		// draw indicator line
		mRectPaint.setColor(mIndicatorColor);
		
		// default: line below current tab
		View mCurrentTabs = mTabsContainer.getChildAt(mCurrentPosition);
		if(mCurrentTabs instanceof TextView) {
			((TextView) mTabsContainer.getChildAt(mLastIndex)).setTextColor(mTabsTextColor);
			((TextView) mTabsContainer.getChildAt(mCurIndex)).setTextColor(mSelectTextColor);
		}
		
		float mLineLeft = mCurrentTabs.getLeft();
		float mLineRight = mCurrentTabs.getRight();
		
		// if there is an offset, start interpolating left and right coordinates
		// between current and next tab
		if(mCurrentPositionOffset > 0f && mCurrentPosition < mTabsCount - 1) {
			View mNextTabs = mTabsContainer.getChildAt(mCurrentPosition + 1);
			float mNextTabLeft = mNextTabs.getLeft();
			float mNextTabRight = mNextTabs.getRight();
			mLineLeft = mCurrentPositionOffset * mNextTabLeft + (1f - mCurrentPositionOffset) * mLineLeft;
			mLineRight = mCurrentPositionOffset * mNextTabRight + (1f - mCurrentPositionOffset) * mLineRight;
		}
		
		float mPadding = (mLineRight - mLineLeft) * (1 - mIndicatorWidthPercent) / 2;
		mLineLeft = mLineLeft + mPadding;
		mLineRight = mLineRight - mPadding;
		
		if(mIndicatorResource > 0) {
			
		}
	}
	
	private class PageListener implements OnPageChangeListener {
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			mCurrentPosition = position;
			mCurrentPositionOffset = positionOffset;
			
			scrollToChild(position, (int) positionOffset * mTabsContainer.getChildAt(position).getWidth());
			
			invalidate();
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

	public void setSelectTextColor(int selectTextColor) {
		this.mSelectTextColor = selectTextColor;
		invalidate();
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
	
	
	
	
	public int getScrollOffset() {
		return mScrollOffset;
	}

	public void setScrollOffset(int mScrollOffset) {
		this.mScrollOffset = mScrollOffset;
		invalidate();
	}
	
	public boolean getShouldExpand() {
		return mShouldExpand;
	}

	public void setShouldExpand(boolean shouldExpand) {
		this.mShouldExpand = shouldExpand;
		if(mShouldExpand) {
			mCurrentTabLayoutParams = mExpandedTabLayoutParams;
		} else {
			mCurrentTabLayoutParams = mDefaultTabLayoutParams;
		}
		requestLayout();
	}
	
	public boolean isTextAllCaps() {
		return mTextAllCaps;
	}

	public void setTextAllCaps(boolean textAllCaps) {
		this.mTextAllCaps = textAllCaps;
	}

	public int getTextSize() {
		return mTabsTextSize;
	}

	public void setTextSize(int tabsTextSize) {
		this.mTabsTextSize = tabsTextSize;
		updateTabStyles();
	}
	
	public int getTextColor() {
		return mTabsTextColor;
	}

	public void setTextColor(int tabsTextColor) {
		this.mTabsTextColor = tabsTextColor;
		updateTabStyles();
	}
	
	public void setTextColorResource(int resId) {
		this.mTabsTextColor = getResources().getColor(resId);
		updateTabStyles();
	}
	
	public void setTypeface(Typeface typeface, int style) {
		this.mTabTypeface = typeface;
		this.mTabTypefaceStyle = style;
		updateTabStyles();
	}
	
	public int getTabBackground() {
		return mTabBackgroundResId;
	}

	public void setTabBackground(int tabBackgroundResId) {
		this.mTabBackgroundResId = tabBackgroundResId;
	}

	public int getTabPaddingLeftRight() {
		return mTabPadding;
	}

	public void setTabPaddingLeftRight(int tabPadding) {
		this.mTabPadding = tabPadding;
		updateTabStyles();
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

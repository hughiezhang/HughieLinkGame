package com.hughie.linkgame.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 主界面布局
 * @ClassName: HughieFlipperLayout
 * @author hughiezhang
 * @since 2015-09-06 16:31
 */
public class HughieFlipperLayout extends ViewGroup {
	private VelocityTracker mVelocityTracker;
	private Scroller mScroller;
	
	private int mWidth;
	
	private int mScreenState;
	private int mTouchState;
	private int mScrollState;
	
	private int mVelocityValue = 0;
	
	private boolean mOnClick = false;
	
	public static final int SCREEN_STATE_CLOSE = 0;
	public static final int SCREEN_STATE_OPEN = 1;
	
	public static final int TOUCH_STATE_RESTART = 0;
	public static final int TOUCH_STATE_SCROLLING = 1;
	
	public static final int SCROLL_STATE_NO_ALLOW = 0;
	public static final int SCROLL_STATE_ALLOW = 1;
	
	public HughieFlipperLayout(Context context) {
		super(context);
		this.mScroller = new Scroller(context);
		mWidth = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56,
				getResources().getDisplayMetrics());
		
		this.mScreenState = SCREEN_STATE_CLOSE;
		this.mTouchState = TOUCH_STATE_RESTART;
		this.mScrollState = SCROLL_STATE_NO_ALLOW;
	}
	
	public HughieFlipperLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HughieFlipperLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		for(int i = 0; i < getChildCount(); i++){
			View mChild = getChildAt(i);
			int mWidth = mChild.getMeasuredWidth();
			int mHeight = mChild.getMeasuredHeight();
			mChild.layout(0, 0, mWidth, mHeight);
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int mWidth = MeasureSpec.getSize(widthMeasureSpec);
		int mHeight = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(mWidth, mHeight);
		for(int i = 0; i < getChildCount(); i++){
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		obtainVelocityTracker(ev);
		switch(ev.getAction()){
			case MotionEvent.ACTION_DOWN:
				mTouchState = mScroller.isFinished() ? TOUCH_STATE_RESTART : TOUCH_STATE_SCROLLING;
				if (mTouchState == TOUCH_STATE_RESTART){
					int x = (int)ev.getX();
					int screenWidth = getWidth();
					if(x <= mWidth && mScreenState == SCREEN_STATE_CLOSE && mTouchState == TOUCH_STATE_RESTART
							|| x >= screenWidth - mWidth && mScreenState == SCREEN_STATE_OPEN && mTouchState == TOUCH_STATE_RESTART){
						if (mScreenState == SCREEN_STATE_OPEN){
							mOnClick = true;
						}
						mScrollState = SCROLL_STATE_ALLOW;
					}else{
						mOnClick = false;
						mScrollState = SCROLL_STATE_NO_ALLOW;
					}
				}else{
					return false;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				mVelocityTracker.computeCurrentVelocity(1000, ViewConfiguration.getMaximumFlingVelocity());
				if(mScrollState == SCROLL_STATE_ALLOW && getWidth() - (int) ev.getX() < mWidth){
					return true;
				}
				break;
			case MotionEvent.ACTION_UP:
				releaseVelocityTracker();
				if(mOnClick){
					mOnClick = false;
					mScreenState = SCREEN_STATE_CLOSE;
					mScroller.startScroll(this.getChildAt(1).getScrollX(), 0, -getChildAt(1).getScrollX(), 0, 800);
					invalidate();
				}
				break;
		}
		
		return super.dispatchTouchEvent(ev);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev){
		obtainVelocityTracker(ev);
		switch(ev.getAction()){
			case MotionEvent.ACTION_DOWN:
				mTouchState = mScroller.isFinished() ? TOUCH_STATE_RESTART : TOUCH_STATE_SCROLLING;
				if (mTouchState == TOUCH_STATE_SCROLLING) {
					return false;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				mOnClick = false;
				mVelocityTracker.computeCurrentVelocity(1000, ViewConfiguration.getMaximumFlingVelocity());
				if (mScrollState == SCROLL_STATE_ALLOW && Math.abs(mVelocityTracker.getXVelocity()) > 200) {
					return true;
				}
				break;
			case MotionEvent.ACTION_UP:
				releaseVelocityTracker();
				if(mScrollState == SCROLL_STATE_ALLOW && mScreenState == SCREEN_STATE_OPEN){
					return true;
				}
				break;
		}
		
		return super.onInterceptTouchEvent(ev);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		obtainVelocityTracker(event);
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				mTouchState = mScroller.isFinished() ? TOUCH_STATE_RESTART : TOUCH_STATE_SCROLLING;
				if (mTouchState == TOUCH_STATE_SCROLLING) {
					return false;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				mVelocityTracker.computeCurrentVelocity(1000, ViewConfiguration.getMaximumFlingVelocity());
				mVelocityValue = (int) mVelocityTracker.getXVelocity();
				getChildAt(1).scrollTo(-(int) event.getX(), 0);
				break;
			case MotionEvent.ACTION_UP:
				if(mScrollState == SCROLL_STATE_ALLOW){
					if (mVelocityValue > 2000){
						mScreenState = SCREEN_STATE_OPEN;
						mScroller.startScroll(this.getChildAt(1).getScrollX(), 0, 
								-(getWidth() - Math.abs(this.getChildAt(1).getScrollX()) - mWidth), 0, 250);
						invalidate();
					}else if (mVelocityValue < -2000) {
						mScreenState = SCREEN_STATE_CLOSE;
						mScroller.startScroll(getChildAt(1).getScrollX(), 0, -getChildAt(1).getScrollX(), 0, 250);
						invalidate();
					}else if (event.getX() < getWidth() / 2){
						mScreenState = SCREEN_STATE_CLOSE;
						mScroller.startScroll(getChildAt(1).getScrollX(), 0,
								-getChildAt(1).getScrollX(), 0, 800);
						invalidate();
					}else {
						mScreenState = SCREEN_STATE_OPEN;
						mScroller.startScroll(getChildAt(1).getScrollX(), 0, -(getWidth() - Math.abs(getChildAt(1)
								.getScrollX()) - mWidth), 0, 800);
						invalidate();
					}
				}
				break;
		}
		
		return super.onTouchEvent(event);
	}
	
	public void computeScroll(){
		super.computeScroll();
		if (mScroller.computeScrollOffset()) {
			getChildAt(1).scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		} 
	}
	
	private void obtainVelocityTracker(MotionEvent event){
		if (mVelocityTracker == null){
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
	}
	
	private void releaseVelocityTracker() {
		if (mVelocityTracker != null) {
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}
	}
	
	//打开screen desktop菜单项
	public void open(){
		mTouchState = mScroller.isFinished() ? TOUCH_STATE_RESTART : TOUCH_STATE_SCROLLING;
		if(mTouchState == TOUCH_STATE_RESTART){
			mScreenState = SCREEN_STATE_OPEN;
			mScroller.startScroll(getChildAt(1).getScrollX(), 0, 
					-(getWidth() - Math.abs(this.getChildAt(1).getScrollX()) - mWidth), 0, 800);
			invalidate();
		}
	}
	
	public int getScreenState() {
		return mScreenState;
	}
	
	
	public interface OnOpenHomeListener {
		public abstract void open();
	}
}

package com.hughie.linkgame.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class HughieViewPager extends ViewPager {
	float startX;
	float startY;
	float endX;
	float endY;
	float distanceX;
	
	public HughieViewPager(Context context) {
		super(context);
	}
	
	public HughieViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		boolean ret = super.dispatchTouchEvent(ev);
		if (ret) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startX = ev.getX();
				startY = ev.getY();
				distanceX = 0;
				break;
			case MotionEvent.ACTION_MOVE:
				distanceX = ev.getX() - endX;
				endX = ev.getX();
				endY = ev.getY();
				if (Math.abs(endY - startY) > 0 && Math.abs(endY - startY) > Math.abs(endX - startX)) {
					getParent().requestDisallowInterceptTouchEvent(false);
				} else {
					PagerAdapter adapter = getAdapter();
					if (adapter != null && getCurrentItem() == 0 && distanceX >0 || adapter != null
							&& getCurrentItem() == adapter.getCount() - 1 && distanceX < 0) {
						getParent().requestDisallowInterceptTouchEvent(false);
					} else {
						getParent().requestDisallowInterceptTouchEvent(true);
					}
				}
				break;
			default:
				getParent().requestDisallowInterceptTouchEvent(true);
				break;
			}
		}
		return ret;
	}
}

package com.sunsun.picproject.viewpager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * 可以获取当前显示的那个view的adapter
 * 
 */
public abstract class PrimaryItemPagerAdapter extends PagerAdapter {

	private View mCurrentView;

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		mCurrentView = (View) object;
	}

	public View getPrimaryItem() {
		return mCurrentView;
	}
}

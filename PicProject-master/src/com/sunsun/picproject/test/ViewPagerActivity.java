package com.sunsun.picproject.test;

import uk.co.senab.photoview.PhotoView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.kugou.framework.component.base.BaseFragmentActivity;
import com.sunsun.picproject.R;
import com.sunsun.picproject.view.ScrollableViewPager;

public class ViewPagerActivity extends BaseFragmentActivity {
	public static final String TAG = ViewPagerActivity.class.getSimpleName();

	public static void startViewPagerActivity(Context context) {
		Intent intent = new Intent(context, ViewPagerActivity.class);
		context.startActivity(intent);
	}

	private ViewPager mViewPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewPager = new ScrollableViewPager(this);
		setContentView(mViewPager);
		mViewPager.setAdapter(new SamplePagerAdapter());
	}

	static class SamplePagerAdapter extends PagerAdapter {

		private static int[] sDrawables = { R.drawable.wallpaper,
				R.drawable.wallpaper, R.drawable.wallpaper,
				R.drawable.wallpaper, R.drawable.wallpaper,
				R.drawable.wallpaper };

		@Override
		public int getCount() {
			return sDrawables.length;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());	
			photoView.setImageResource(sDrawables[position]);

			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

}

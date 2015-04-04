package com.sunsun.slidingmenu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.kugou.framework.component.base.BaseFragmentActivity;
import com.sunsun.picproject.R;
import com.sunsun.slidingmenu.Frament.ViewPagerZoomFragment;

public class ViewPagerZoomActivity extends BaseFragmentActivity {

	public static final String TAG = ViewPagerZoomActivity.class
			.getSimpleName();
	
	public static void startViewPagerZoomActivity(Context context,
			String url) {
		Intent intent = new Intent(context, ViewPagerZoomActivity.class);
		intent.putExtra(ViewPagerZoomFragment.LIST_KEY, url);
		context.startActivity(intent);
	}
	public static final String TAG_NAME_FRAGMENT = "fragment";
	private Fragment mFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_zoom_activity);
		mFragment = getSupportFragmentManager().findFragmentByTag(
				TAG_NAME_FRAGMENT);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		if (mFragment == null) {
			mFragment = new ViewPagerZoomFragment();
			Intent intent = getIntent();
			if (intent != null) {
				Bundle params = intent.getExtras();
				if (mFragment != null) {
					mFragment.setArguments(params);
				}
			}
			transaction.add(R.id.container, mFragment, TAG_NAME_FRAGMENT);
		}
		transaction.show(mFragment);
		transaction.commitAllowingStateLoss();
	}


}

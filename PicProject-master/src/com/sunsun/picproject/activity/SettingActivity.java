package com.sunsun.picproject.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.kugou.framework.component.base.BaseFragmentActivity;
import com.sunsun.picproject.R;
import com.sunsun.picproject.Frament.SettingFragment;
import com.sunsun.picproject.Frament.SpecialFragment;
import com.sunsun.picproject.view.TitleBarView;
import com.sunsun.picproject.view.TitleBarView.OnTitleBarItemClickListener;

public class SettingActivity extends BaseFragmentActivity {

	public static final String TAG = SettingActivity.class.getSimpleName();

	public static void startSpecialActivity(Context context) {
		Intent intent = new Intent(context, SettingActivity.class);
		context.startActivity(intent);
	}

	public static final String TAG_NAME_FRAGMENT = "fragment";
	private Fragment mFragment;
	private TitleBarView mTitleBarView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recommend_activity);
		mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
		mTitleBarView.setMode(TitleBarView.MODE_LEFT);
		mTitleBarView.setTitle(getString(R.string.setting));
		mTitleBarView.setLeftButtonDrawable(R.drawable.back_icon);
		mTitleBarView.setOnItemClickListener(new OnTitleBarItemClickListener() {

			@Override
			public void onTitleBarItemClick(int index) {
				onBackPressed();
			}
		});
		mFragment = (SpecialFragment) getSupportFragmentManager()
				.findFragmentByTag(TAG_NAME_FRAGMENT);
	}

	@Override
	public void onResume() {
		super.onResume();
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		if (mFragment == null) {
			mFragment = new SettingFragment();
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

	@Override
	protected String tag() {
		return TAG;
	}
}

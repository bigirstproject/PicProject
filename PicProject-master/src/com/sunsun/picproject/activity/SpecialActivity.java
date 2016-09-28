package com.sunsun.picproject.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.kugou.framework.component.base.BaseEmptyFragment.onTabBarListener;
import com.kugou.framework.component.base.BaseWorkerFragmentActivity;
import com.sunsun.picproject.R;
import com.sunsun.picproject.Frament.SpecialFragment;
import com.sunsun.picproject.view.TitleBarView;
import com.sunsun.picproject.view.TitleBarView.OnTitleBarItemClickListener;

public class SpecialActivity extends BaseWorkerFragmentActivity {

	public static final String TAG = SpecialActivity.class.getSimpleName();

	public static void startSpecialActivity(Context context, String url) {
		Intent intent = new Intent(context, SpecialActivity.class);
		intent.putExtra(SpecialFragment.URL, url);
		context.startActivity(intent);
	}

	public static final String TAG_NAME_FRAGMENT = "fragment";
	private SpecialFragment mFragment;
	private TitleBarView mTitleBarView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recommend_activity);
		mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
		mTitleBarView.setMode(TitleBarView.MODE_LEFT);
		mTitleBarView.setTitle("");
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
			mFragment = new SpecialFragment();
			Intent intent = getIntent();
			if (intent != null) {
				Bundle params = intent.getExtras();
				if (mFragment != null) {
					mFragment.setArguments(params);
				}
			}
			transaction.add(R.id.container, mFragment, TAG_NAME_FRAGMENT);
		}
		mFragment.setTabBarListener(new onTabBarListener() {

			@Override
			public void setTabBarListener(String tabTitle) {
				Message msg = new Message();
				msg.what = REPONSE_CODE;
				msg.obj = tabTitle;
				sendUiMessage(msg);
			}
		});
		transaction.show(mFragment);
		transaction.commitAllowingStateLoss();

	}

	@Override
	protected void handleBackgroundMessage(Message msg) {
	}

	public final int REPONSE_CODE = 200;

	@Override
	protected void handleUiMessage(Message msg) {
		super.handleUiMessage(msg);
		switch (msg.what) {
		case REPONSE_CODE:
			if (msg != null && msg.obj instanceof String) {
				String tabTitle = (String) msg.obj;
				if (!TextUtils.isEmpty(tabTitle)) {
					mTitleBarView.setTitle(tabTitle);
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected String tag() {
		return TAG;
	}
}

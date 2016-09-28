package com.sunsun.picproject.activity;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.banner.AdViewListener;
import net.youmi.android.spot.SpotManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.kugou.framework.component.base.BaseWorkerFragmentActivity;
import com.kugou.framework.component.preference.AppCommonPref;
import com.sunsun.picproject.R;
import com.sunsun.picproject.Frament.ViewPagerZoomFragment;
import com.sunsun.picproject.config.AppConfig;
import com.yhfd.edsa.WSA;

public class ViewPagerZoomActivity extends BaseWorkerFragmentActivity {

	public static final String TAG = ViewPagerZoomActivity.class
			.getSimpleName();

	public static void startViewPagerZoomActivity(Context context, String url) {
		Intent intent = new Intent(context, ViewPagerZoomActivity.class);
		intent.putExtra(ViewPagerZoomFragment.LIST_KEY, url);
		context.startActivity(intent);
	}

	public static final String TAG_NAME_FRAGMENT = "fragment";
	private Fragment mFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_framelayout_activity);
		mFragment = getSupportFragmentManager().findFragmentByTag(
				TAG_NAME_FRAGMENT);
		if (AppCommonPref.getInstance().getHasOpen() == AppConfig.OPEN) {
			// loadSpotAds();
			showBanner();
		}
		ShowAds();
	}

	/**
	 * 弹出艾盟广告
	 */
	public void ShowAds() {
		WSA mjokeM = WSA.getInstance(ViewPagerZoomActivity.this,
				"8a239023e5b21028866321b87b8c6c7b");
		mjokeM.show();
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

	@Override
	protected void handleBackgroundMessage(Message msg) {

	}

	@Override
	protected String tag() {
		return TAG;
	}

	private void loadSpotAds() {
		SpotManager.getInstance(this).loadSpotAds();
		SpotManager.getInstance(this)
				.setAnimationType(SpotManager.ANIM_ADVANCE);
		SpotManager.getInstance(this).setSpotOrientation(
				SpotManager.ORIENTATION_PORTRAIT);
		SpotManager.getInstance(ViewPagerZoomActivity.this).showSpotAds(
				ViewPagerZoomActivity.this, null);
	}

	private void showBanner() {

		// 广告条接口调用（适用于应用）
		// 将广告条adView添加到需要展示的layout控件中
		// LinearLayout adLayout = (LinearLayout) findViewById(R.id.adLayout);
		// AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		// adLayout.addView(adView);

		// 广告条接口调用（适用于游戏）

		// 实例化LayoutParams(重要)
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		// 设置广告条的悬浮位置
		layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT; // 这里示例为右下角
		// 实例化广告条
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		// 调用Activity的addContentView函数

		// 监听广告条接口
		adView.setAdListener(new AdViewListener() {

			@Override
			public void onSwitchedAd(AdView arg0) {
				Log.i("YoumiAdDemo", "广告条切换");
			}

			@Override
			public void onReceivedAd(AdView arg0) {
				Log.i("YoumiAdDemo", "请求广告成功");

			}

			@Override
			public void onFailedToReceivedAd(AdView arg0) {
				Log.i("YoumiAdDemo", "请求广告失败");
			}
		});
		this.addContentView(adView, layoutParams);
	}

	@Override
	public void onBackPressed() {
		// 如果有需要，可以点击后退关闭插播广告。
		if (!SpotManager.getInstance(this).disMiss()) {
			// 弹出退出窗口，可以使用自定义退屏弹出和回退动画,参照demo,若不使用动画，传入-1
			super.onBackPressed();
		}
	}

	@Override
	protected void onStop() {
		// 如果不调用此方法，则按home键的时候会出现图标无法显示的情况。
		SpotManager.getInstance(this).onStop();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		SpotManager.getInstance(this).onDestroy();
		super.onDestroy();
	}
}

package com.sunsun.picproject.activity;

import net.youmi.android.spot.SplashView;
import net.youmi.android.spot.SpotDialogListener;
import net.youmi.android.spot.SpotManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;

import com.kugou.framework.component.base.BaseFragmentActivity;
import com.kugou.framework.component.preference.AppCommonPref;
import com.sunsun.picproject.R;
import com.sunsun.picproject.config.AppConfig;

public class SplashActivity extends BaseFragmentActivity {
	private RelativeLayout splashRelativeLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);
		splashRelativeLayout = (RelativeLayout) findViewById(R.id.rl_splash);

		// 渐变展示启动屏
		AlphaAnimation animation = new AlphaAnimation(1.0f, 1.0f);
		animation.setDuration(1500);
		splashRelativeLayout.startAnimation(animation);

		animation.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation arg0) {
			}

			public void onAnimationRepeat(Animation arg0) {
			}

			public void onAnimationEnd(Animation arg0) {
				Intent it = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(it);
				finish();
			}
		});

		if (AppCommonPref.getInstance().getHasOpen() == AppConfig.OPEN) {
			SpotManager.getInstance(this).loadSplashSpotAds();
			SplashView splashView = new SplashView(this, MainActivity.class);
			setContentView(splashView.getSplashView());
			SpotManager.getInstance(this).showSplashSpotAds(this, splashView,
					new SpotDialogListener() {

						@Override
						public void onShowSuccess() {
							Log.i("YoumiAdDemo", "开屏展示成功");
						}

						@Override
						public void onShowFailed() {
							Log.i("YoumiAdDemo", "开屏展示失败。");
						}

						@Override
						public void onSpotClosed() {
							Log.i("YoumiAdDemo", "开屏关闭。");
						}
					});
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 10045) {
			Intent intent = new Intent(SplashActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	}

	@Override
	public void onBackPressed() {
	}

	@Override
	protected boolean needSwipeBackLayout() {
		return false;
	}
}

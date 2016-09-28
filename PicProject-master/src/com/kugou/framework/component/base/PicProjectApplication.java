package com.kugou.framework.component.base;

import net.youmi.android.AdManager;
import net.youmi.android.offers.OffersManager;
import net.youmi.android.onlineconfig.OnlineConfigCallBack;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.provider.Settings.Secure;

import com.kugou.framework.component.preference.AppCommonPref;
import com.sunsun.picproject.R;
import com.sunsun.picproject.Image.SwitchImageLoader;
import com.sunsun.picproject.config.AppConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.push.FeedbackPush;

public class PicProjectApplication extends BaseApplication {
	private static PicProjectApplication mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		initMobclickAgent();
		SwitchImageLoader.init(this);
		initYouMi();
	}

	private void initYouMi() {
		AdManager.getInstance(this).init(AppConfig.YOUMI_APP_ID,
				AppConfig.YOUMI_APP_SECRET, false);
		AdManager.getInstance(this).setUserDataCollect(true);
		OffersManager.getInstance(this).onAppLaunch();
		AdManager.getInstance(this).asyncGetOnlineConfig(
				getApplicationContext().getResources().getString(
						R.string.channel), new OnlineConfigCallBack() {

					/**
					 * 获取在线参数成功就会回调本方法（本回调方法执行在UI线程中）
					 */
					@Override
					public void onGetOnlineConfigSuccessful(String key,
							String value) {
						AppCommonPref.getInstance().putHasOpen(
								Integer.valueOf(value));
						// DWToast.makeText(
						// getInstance(),
						// String.format("在线参数获取结果：\nkey=%s, value=%s",
						// key, value), Toast.LENGTH_LONG).show();
					}

					/**
					 * 获取在线参数失败就会回调本方法（本回调方法执行在UI线程中）
					 */
					@Override
					public void onGetOnlineConfigFailed(String key) {

						// 获取在线参数失败，可能原因有：键值未设置或为空、网络异常、服务器异常
						// Toast.makeText(
						// getInstance(),
						// String.format(
						// "在线参数获取结果：\n获取在线key=%s失败!\n具体失败原因请查看Log，Log标签：YoumiSdk",
						// key), Toast.LENGTH_LONG).show();
					}
				});

	}

	private void initMobclickAgent() {
		FeedbackPush.getInstance(this).init(false);
		MobclickAgent.setDebugMode(false);
		MobclickAgent.openActivityDurationTrack(false);
		// MobclickAgent.updateOnlineConfig(this);
	}

	public static PicProjectApplication getInstance() {
		return mInstance;
	}

	/**
	 * 获取版本号
	 * 
	 * @return app_version
	 */
	public String getAppVersion() {
		PackageManager pm = this.getPackageManager();
		PackageInfo pi;
		String version = null;
		try {
			pi = pm.getPackageInfo(this.getPackageName(), 0);
			version = pi.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return version;
	}

	/**
	 * 获取版本号
	 * 
	 * @return app_versionCode
	 */
	public int getAppVersionCode() {
		PackageManager pm = this.getPackageManager();
		PackageInfo pi;
		int versionCode = -1;
		try {
			pi = pm.getPackageInfo(this.getPackageName(), 0);
			versionCode = pi.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	public String getDeviceId() {
		return Secure.getString(PicProjectApplication.getInstance()
				.getContentResolver(), Secure.ANDROID_ID);
	}
}

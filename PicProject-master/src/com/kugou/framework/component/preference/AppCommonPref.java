package com.kugou.framework.component.preference;

import android.content.Context;

import com.kugou.framework.component.base.PicProjectApplication;

public class AppCommonPref extends PreferenceOpenHelper {

	private volatile static AppCommonPref mInstance = null;

	private AppCommonPref(Context context, String prefname) {
		super(context, prefname);
	}

	public static AppCommonPref getInstance() {
		if (mInstance == null) {
			synchronized (AppCommonPref.class) {
				if (mInstance == null) {
					Context context = PicProjectApplication.getInstance();
					String name = context.getPackageName() + "appinfo";
					mInstance = new AppCommonPref(context, name);
				}
			}
		}
		return mInstance;
	}

	public String getNewVersionName() {
		return getString(Keys.NEW_VERSION_NAME, "");
	}

	public void putNewVersionName(String versionName) {
		putString(Keys.NEW_VERSION_NAME, versionName);
	}

	public String getNewVersionTitle() {
		return getString(Keys.NEW_VERSION_TITLE, "");
	}

	public void putNewVersionTitle(String versionTitle) {
		putString(Keys.NEW_VERSION_TITLE, versionTitle);
	}

	public String getNewVersionContent() {
		return getString(Keys.NEW_VERSION_CONTENT, "");
	}

	public void putNewVersionContent(String versionContent) {
		putString(Keys.NEW_VERSION_CONTENT, versionContent);
	}

	// public boolean getHasNewVersion() {
	// return getBoolean(Keys.HAVE_NEW_VERSION, false);
	// }
	//
	// public void putHasNewVersion(boolean value) {
	// putBoolean(Keys.HAVE_NEW_VERSION, value);
	// }

	public int getVersionMustUpdate() {
		return getInt(Keys.VERSION_MUST_UPDATE, 0);
	}

	public void putVersionMustUpdate(int code) {
		putInt(Keys.VERSION_MUST_UPDATE, code);
	}

	public int getNewVersionCode() {
		return getInt(Keys.NEW_VERSION_CODE, -1);
	}

	public void putNewVersionCode(int code) {
		putInt(Keys.NEW_VERSION_CODE, code);
	}

	public static interface Keys {

		/**
		 * 是否强制更新
		 */
		public static String VERSION_MUST_UPDATE = "version_must_update";

		/**
		 * 新版本号
		 */
		public static String NEW_VERSION_CODE = "new_version_code";

		/**
		 * 新版本名
		 */
		public static String NEW_VERSION_NAME = "new_version_name";

		/**
		 * 新版本标题
		 */
		public static String NEW_VERSION_TITLE = "new_version_title";

		/**
		 * 新版本内容
		 */
		public static String NEW_VERSION_CONTENT = "new_version_content";

	}
}

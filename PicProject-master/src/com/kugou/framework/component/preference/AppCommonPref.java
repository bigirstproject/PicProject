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
					String name = context.getPackageName() + "picProject";
					mInstance = new AppCommonPref(context, name);
				}
			}
		}
		return mInstance;
	}

	public int getHasOpen() {
		return getInt(Keys.HAS_OPEN, 0);
	}

	public void putHasOpen(int value) {
		putInt(Keys.HAS_OPEN, value);
	}

	public static interface Keys {
		/**
		 * 是否开启有米广告
		 */
		public static String HAS_OPEN = "has_open";
		
		/**
		 * 首页cache
		 */
		public static String PIC_MAIN_LIST_CAHCE = "pic_main_list_cache";
		
		/**
		 * 首页banner
		 */
		public static String PIC_MAIN_BANNER_CAHCE = "pic_main_banner_cache";
	}
}

package com.kugou.framework.component.base;

import com.sunsun.picproject.Image.SwitchImageLoader;

public class PicProjectApplication extends BaseApplication {
	private static PicProjectApplication mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		SwitchImageLoader.init(this);
	}
	
	public static PicProjectApplication getInstance() {
		return mInstance;
	}
}

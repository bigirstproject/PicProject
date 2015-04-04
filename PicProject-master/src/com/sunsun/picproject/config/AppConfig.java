package com.sunsun.picproject.config;

import com.kugou.framework.component.base.PicProjectApplication;

/**
 * 程序配置类：定义一些全局常量
 * 
 */
public class AppConfig {

	public static final float DENSITY = PicProjectApplication.getInstance()
			.getResources().getDisplayMetrics().density;
	public static final float SCALESITY = PicProjectApplication.getInstance()
			.getResources().getDisplayMetrics().scaledDensity;
	public static final int widthPx = PicProjectApplication.getInstance()
			.getResources().getDisplayMetrics().widthPixels;
	public static final int heightPx = PicProjectApplication.getInstance()
			.getResources().getDisplayMetrics().heightPixels;
	
	public static final boolean CRASHHANDLE = false;
	public static final boolean DEBUG = true;
	
	public static final String LOG_TAG = "DWYXApp";
}
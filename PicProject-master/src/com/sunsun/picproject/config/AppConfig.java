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
	
	/**
	 * 有米
	 */
	public static final String YOUMI_APP_ID = "23fd6906d594424a";
	public static final String YOUMI_APP_SECRET = "e461bab1e99710ce";
	
	public static final int CLOSE = 0;
	public static final int OPEN = 1;

	/**
	 * 分享
	 */
	public static final String WEIBO_APP_KEY = "3682745164";
	public static final String QQ_APP_ID = "1101354073";
	public static final String QQ_APP_KEY = "nBwCto03X4t2tyAp";
	public static final String WEIXIN_APP_KEY = "wx3f943956945cbee7";
	public static final String WEIXIN_APP_SECRET = "5d18625703d3ba453c07fbc663641b08";
}
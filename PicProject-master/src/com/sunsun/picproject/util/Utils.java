package com.sunsun.picproject.util;

import java.io.IOException;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;

import com.kugou.framework.component.preference.AppCommonPref;

public class Utils {
	
	public static int getAppCommonPref() {
		return AppCommonPref.getInstance().getHasOpen();
	}
	
	/**
	 * 设置壁纸
	 * @param context
	 * @param bitmap
	 */
	public static void setWallpaper(Context context,Bitmap bitmap) {
		try {
			WallpaperManager manager = WallpaperManager
					.getInstance(context);
			manager.setBitmap(bitmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

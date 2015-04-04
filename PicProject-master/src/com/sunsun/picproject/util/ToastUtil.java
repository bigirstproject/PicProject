package com.sunsun.picproject.util;

import android.widget.Toast;

import com.kugou.framework.component.base.PicProjectApplication;

public class ToastUtil {
	private static Toast mToast = null;

	public static void showToast(String msg) {
		if (mToast == null) {
			mToast = Toast.makeText(PicProjectApplication.getInstance(), msg,
					Toast.LENGTH_SHORT);
		} else {
			mToast.setText(msg);
		}
		mToast.show();
	}

	public static void showToast(int resId) {
		if (mToast == null) {
			mToast = Toast.makeText(PicProjectApplication.getInstance(), resId,
					Toast.LENGTH_SHORT);
		} else {
			mToast.setText(resId);
		}
		mToast.show();
	}

}

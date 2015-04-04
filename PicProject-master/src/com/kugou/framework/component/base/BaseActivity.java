package com.kugou.framework.component.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.kugou.framework.component.base.BaseWorkerFragment.BackgroundHandler;

/**
 * 描述 具备后台线程和UI线程更新
 * 
 * @author chenjinyuan
 * @since 2013-12-2 上午9:45:00
 */
public class BaseActivity extends Activity {

	private Toast mToast;

	protected HandlerThread mHandlerThread;

	protected BackgroundHandler mBackgroundHandler;

	protected Handler mUiHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			handleUiMessage(msg);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	/**
	 * 处理更新UI任务
	 * 
	 * @param msg
	 */
	protected void handleUiMessage(Message msg) {
	}

	/**
	 * 发送UI更新操作
	 * 
	 * @param msg
	 */
	protected void sendUiMessage(Message msg) {
		mUiHandler.sendMessage(msg);
	}

	/**
	 * 发送UI更新操作
	 * 
	 * @param what
	 */
	protected void sendEmptyUiMessage(int what) {
		mUiHandler.sendEmptyMessage(what);
	}

	/**
	 * 显示一个Toast类型的消息
	 * 
	 * @param msg
	 *            显示的消息
	 */
	public void showToast(String msg) {
		if (mToast == null) {
			mToast = Toast.makeText(getApplicationContext(), "",
					Toast.LENGTH_SHORT);
		}
		mToast.setText(msg);
		mToast.show();
	}

	/**
	 * 显示{@link Toast}通知
	 * 
	 * @param strResId
	 *            字符串资源id
	 */
	public void showToast(int strResId) {
		if (mToast == null) {
			mToast = Toast.makeText(getApplicationContext(), "",
					Toast.LENGTH_SHORT);
		}
		mToast.setText(strResId);
		mToast.show();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	/**
	 * 隐藏软键盘
	 */
	protected void hideSoftInput(Context context) {
		InputMethodManager manager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		// manager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
		if (BaseActivity.this.getCurrentFocus() != null) {
			manager.hideSoftInputFromWindow(BaseActivity.this.getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}

	}

	/**
	 * 控制软键盘的显示隐藏
	 */
	protected void showSoftInput() {
		InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		manager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}
}

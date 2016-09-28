package com.kugou.framework.component.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.sunsun.picproject.R;
import com.sunsun.picproject.view.SwipeBackLayout;
import com.umeng.analytics.MobclickAgent;

/**
 * 描述:抽象FragmentActivity，提供刷新UI的Handler
 * 
 */
public abstract class BaseFragmentActivity extends FragmentActivity {

	private Toast mToast;

	protected SwipeBackLayout layout;

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
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 竖屏
		if(needSwipeBackLayout()){
			layout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
					R.layout.base_activity, null);
			layout.attachToActivity(this);
		}
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置成全屏模式
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//
		// 横屏
	}
	
	protected boolean needSwipeBackLayout(){
		return true;
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
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(tag());
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(tag());
		MobclickAgent.onPause(this);
	}

	protected String tag() {
		return null;
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.anim.base_slide_right_in,
				R.anim.base_slide_remain);
	}

	// Press the back button in mobile phone
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, R.anim.base_slide_right_out);
	}

	/**
	 * 隐藏软键盘
	 */
	protected void hideSoftInput(Context context) {
		InputMethodManager manager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		// manager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
		if (BaseFragmentActivity.this.getCurrentFocus() != null) {
			manager.hideSoftInputFromWindow(BaseFragmentActivity.this
					.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
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

package com.sunsun.picproject.activity;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.sunsun.picproject.R;
import com.sunsun.picproject.Frament.ContentListFragment;
import com.sunsun.picproject.Frament.MotemeinvFragment;
import com.sunsun.picproject.Frament.PicFragment;
import com.sunsun.picproject.Frament.SiwameituiFragment;
import com.sunsun.picproject.Frament.StarFragment;
import com.sunsun.picproject.Frament.TiyumeinvFragment;
import com.sunsun.picproject.Frament.WangluomeinvFragment;
import com.sunsun.picproject.Frament.WeimeixiezhenFragment;
import com.sunsun.picproject.Frament.XinganmoteFragment;
import com.sunsun.picproject.Frament.gaoqingmeinvFragment;
import com.sunsun.picproject.event.SettingEvent;
import com.sunsun.picproject.event.ToggleEvent;
import com.sunsun.picproject.util.Utils;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateStatus;

import de.greenrobot.event.EventBus;

public class MainActivity extends AnimationBaseActivity {

	private long exitTime = 0;
	private String mCurrentFragmentTag = null;
	private String[] mFragments;

	public MainActivity() {
		super(R.string.action_bar, new CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				float scale = (float) (percentOpen * 0.25 + 0.75);
				canvas.scale(scale, scale, canvas.getWidth() / 2,
						canvas.getHeight() / 2);
			}
		});
	}

	private void addFragment(int resId, Fragment fg, String tag) {
		if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
			mCurrentFragmentTag = tag;
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			ft.add(resId, fg, tag);
			ft.commit();
		}
	}

	private void replaceFragment(int resId, Fragment fg, String tag) {
		if (!mCurrentFragmentTag.equals(tag)
				&& getSupportFragmentManager().findFragmentByTag(tag) == null) {
			mCurrentFragmentTag = tag;
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			ft.replace(resId, fg, tag);
			ft.commit();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
		// set the Above View
		setContentView(R.layout.content_frame);
		if (Utils.getAppCommonPref() == 1) {
			mFragments = getResources().getStringArray(R.array.fragment_title);
			addFragment(R.id.content_frame, new ContentListFragment(),
					mFragments[0]);
		} else {
			mFragments = getResources().getStringArray(
					R.array.test_fragment_title);
			PicFragment fragment = new PicFragment();
			addFragment(R.id.content_frame, fragment, mFragments[0]);
		}
		setUpdateMode();
	}

	/**
	 * 设置umeng更新模式
	 */
	private void setUpdateMode() {
		UmengUpdateAgent.setDefault();
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.setUpdateAutoPopup(true);
		UmengUpdateAgent.setRichNotification(true);
		UmengUpdateAgent.setUpdateUIStyle(UpdateStatus.STYLE_DIALOG);
		UmengUpdateAgent.update(MainActivity.this);
	}

	public void onEvent(ToggleEvent event) {
		if (event != null) {
			mActionBar.setTitle(event.getTable().getTitle());
			int id = event.getTable().getId();
			if (Utils.getAppCommonPref() == 0) {
				if (id == 0 || id == 1 || id == 2 || id == 3 || id == 4
						|| id == 5 || id == 6) {
					PicFragment fragment = new PicFragment();
					Bundle bundle = new Bundle();
					bundle.putString(PicFragment.URL, event.getTable().getUrl());
					bundle.putString(PicFragment.NEXTURL, event.getTable()
							.getNextUrl());
					fragment.setArguments(bundle);
					replaceFragment(R.id.content_frame, fragment,
							mFragments[id]);
				}
				toggle();
				return;
			}

			if (id == 0) {
				replaceFragment(R.id.content_frame, new ContentListFragment(),
						mFragments[id]);
			} else if (id == 1) {
				XinganmoteFragment fragment = new XinganmoteFragment();
				Bundle bundle = new Bundle();
				bundle.putString(XinganmoteFragment.URL, event.getTable()
						.getUrl());
				fragment.setArguments(bundle);
				replaceFragment(R.id.content_frame, fragment, mFragments[id]);
			} else if (id == 2) {
				SiwameituiFragment fragment = new SiwameituiFragment();
				Bundle bundle = new Bundle();
				bundle.putString(SiwameituiFragment.URL, event.getTable()
						.getUrl());
				fragment.setArguments(bundle);
				replaceFragment(R.id.content_frame, fragment, mFragments[id]);
			} else if (id == 3) {
				gaoqingmeinvFragment fragment = new gaoqingmeinvFragment();
				Bundle bundle = new Bundle();
				bundle.putString(gaoqingmeinvFragment.URL, event.getTable()
						.getUrl());
				fragment.setArguments(bundle);
				replaceFragment(R.id.content_frame, fragment, mFragments[id]);
			} else if (id == 4) {
				WangluomeinvFragment fragment = new WangluomeinvFragment();
				Bundle bundle = new Bundle();
				bundle.putString(WangluomeinvFragment.URL, event.getTable()
						.getUrl());
				fragment.setArguments(bundle);
				replaceFragment(R.id.content_frame, fragment, mFragments[id]);
			} else if (id == 5) {
				WeimeixiezhenFragment fragment = new WeimeixiezhenFragment();
				Bundle bundle = new Bundle();
				bundle.putString(WeimeixiezhenFragment.URL, event.getTable()
						.getUrl());
				fragment.setArguments(bundle);
				replaceFragment(R.id.content_frame, fragment, mFragments[id]);
			} else if (id == 6) {
				TiyumeinvFragment fragment = new TiyumeinvFragment();
				Bundle bundle = new Bundle();
				bundle.putString(TiyumeinvFragment.URL, event.getTable()
						.getUrl());
				fragment.setArguments(bundle);
				replaceFragment(R.id.content_frame, fragment, mFragments[id]);
			} else if (id == 7) {
				MotemeinvFragment fragment = new MotemeinvFragment();
				Bundle bundle = new Bundle();
				bundle.putString(MotemeinvFragment.URL, event.getTable()
						.getUrl());
				fragment.setArguments(bundle);
				replaceFragment(R.id.content_frame, fragment, mFragments[id]);
			} else if (id == 8) {
				StarFragment fragment = new StarFragment();
				Bundle bundle = new Bundle();
				bundle.putString(StarFragment.URL, event.getTable().getUrl());
				fragment.setArguments(bundle);
				replaceFragment(R.id.content_frame, fragment, mFragments[id]);
			}
			toggle();
		}
	}

	public void onEvent(SettingEvent event) {
		toggle();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				//System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

}

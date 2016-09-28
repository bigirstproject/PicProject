package com.sunsun.picproject.activity;

import android.os.Bundle;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;

public abstract class AnimationBaseActivity extends ActionBarBaseActivity {

	private CanvasTransformer mTransformer;

	public AnimationBaseActivity(int titleRes, CanvasTransformer transformer) {
		super(titleRes);
		mTransformer = transformer;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		// set the Above View
//		setContentView(R.layout.content_frame);
//		getSupportFragmentManager().beginTransaction()
//				.replace(R.id.content_frame, new MenuBehindListFragment()).commit();

		SlidingMenu sm = getSlidingMenu();
		setSlidingActionBarEnabled(true);
		sm.setBehindScrollScale(0.0f);
		sm.setBehindCanvasTransformer(mTransformer);
	}
	
	
}

package com.sunsun.slidingmenu.Frament;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kugou.framework.component.base.BaseWorkerFragment;

public class ViewPagerZoomFragment extends BaseWorkerFragment {
	
	public static final String LIST_KEY = "list_key";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
	}
	@Override
	protected void handleBackgroundMessage(Message msg) {

	}

}

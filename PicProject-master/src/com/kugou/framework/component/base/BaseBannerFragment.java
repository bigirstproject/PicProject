package com.kugou.framework.component.base;

import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.sunsun.picproject.activity.ViewPagerZoomActivity;
import com.sunsun.picproject.bean.BannerTable;
import com.sunsun.picproject.viewpager.BaseBannerView.OnBannerItemClickListener;

public abstract class BaseBannerFragment<E, W> extends BaseListFragment<E>
		implements OnBannerItemClickListener {

	protected BaseImageAdapter<W> mBannerAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (AddBanner()) {
			mBannerAdapter = initBannerAdapter();
			View viewHeader = addListViewHeader();
			if (viewHeader != null) {
				mListView.addHeaderView(viewHeader);
			}
		}
	}

	/**
	 * 是否需要添加banner（标示）
	 */
	protected boolean AddBanner() {
		return true;
	}

	/**
	 * banner的标示
	 */
	protected boolean bannerLogo() {
		return true;
	}

	/**
	 * listView头部
	 */
	protected View addListViewHeader() {
		return null;
	};

	/**
	 * 初始化Banner数据视图的adapter
	 * 
	 * @return
	 */
	protected BaseImageAdapter<W> initBannerAdapter() {
		return null;
	};

	/**
	 * banner数据
	 * 
	 * @param refresh
	 * @param data
	 * @param hasMore
	 * @param replace
	 */
	protected void requestBannerFinish(int refresh, List<W> data,
			boolean replace) {
		if (mBannerAdapter == null) {
			return;
		}
		if (mBannerAdapter != null) {
			mBannerAdapter.getDataSource().clear();
			mBannerAdapter.setDataSource(data);
		}
	}

	protected class TopPageChangedListener implements OnPageChangeListener {

		public TopPageChangedListener() {
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int position) {
			setBannerIndexs(position - 3);
		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}
	}

	protected void addBannerView(LinearLayout mBannerLogo, int tabs) {
		mBannerLogo.removeAllViews();
		for (int i = 0; i < tabs; i++) {
			android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(
					android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
					android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
			ImageView imageView = new ImageView(getActivity());
			imageView.setPadding(0, 0, 30, 0);
			mBannerLogo.addView(imageView, params);
		}
		setBannerIndexs(0);
	}

	protected void setBannerIndexs(int index) {

	};

	@Override
	public void onBannerItemClick(View view, ListAdapter adapter, int position) {
		BannerTable item = (BannerTable) adapter.getItem(position);
		switch (item.getType()) {
		case 1:
			ViewPagerZoomActivity.startViewPagerZoomActivity(getActivity(), item.getUrl());
			break;
		default:
			break;
		}

	}
}

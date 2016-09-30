package com.kugou.framework.component.base;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.huewu.pla.lib.MultiColumnPullToRefreshListView;
import com.huewu.pla.lib.MultiColumnPullToRefreshListView.OnRefreshListener;
import com.huewu.pla.lib.internal.PLA_AbsListView;
import com.huewu.pla.lib.internal.PLA_AbsListView.OnScrollListener;
import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.huewu.pla.lib.internal.PLA_AdapterView.OnItemClickListener;
import com.sunsun.picproject.R;

public abstract class WaterPullBaseFragment<E> extends BaseEmptyFragment
		implements OnItemClickListener {
	protected final int PULL_TO_REFRESH = 0;
	protected final int LOAD_MORE = 1;
	protected MultiColumnPullToRefreshListView mMultiColumnPullToRefreshListView;
	protected BaseImageAdapter<E> mListAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.waterpull_pull_to_refresh_act,
				container, false);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mMultiColumnPullToRefreshListView = (MultiColumnPullToRefreshListView) view
				.findViewById(R.id.waterpull_list);
		if (needEnptyView()) {
			setContainer(mMultiColumnPullToRefreshListView);
		}
		mMultiColumnPullToRefreshListView
				.setOnRefreshListener(new CustomOnRefreshListener());
		mMultiColumnPullToRefreshListView
				.setOnScrollListener(new OnScrollListener() {

					@Override
					public void onScrollStateChanged(
							PLA_AbsListView paramPLA_AbsListView, int paramInt) {

					}

					@Override
					public void onScroll(PLA_AbsListView paramPLA_AbsListView,
							int paramInt1, int paramInt2, int paramInt3) {
						if (paramInt1 + paramInt2 >= paramInt3) {
							loadMore();
						}
					}
				});
		mMultiColumnPullToRefreshListView.addFooterView(mInflater.inflate(
				R.layout.water_pull_footer, null));
		mListAdapter = initListAdapter();
		mMultiColumnPullToRefreshListView.setAdapter(mListAdapter);
		if (setListViewListener()) {
			mMultiColumnPullToRefreshListView.setOnItemClickListener(this);
		}
	}

	/**
	 * 数据请求
	 */
	protected void requestDataImpl(final int refreshStatus, int method,
			String url, Class<?> clazz) {

	}

	/**
	 * 是否需要加载网络或无数据（显示）
	 * 
	 * @return
	 */
	protected boolean needEnptyView() {
		return true;
	}

	/**
	 * 初始化List数据视图的adapter
	 * 
	 * @return
	 */
	protected abstract BaseImageAdapter<E> initListAdapter();

	/**
	 * 数据视图进行了上拉刷新操作
	 */
	protected void loadMore() {

	}

	/**
	 * 数据视图进行了下拉刷新操作
	 */
	public void onCustomRefresh() {

	}

	/**
	 * 设置listView item监听
	 */
	protected boolean setListViewListener() {
		return true;
	}

	/**
	 * 自定义listView item监听
	 */
	protected boolean customListViewListener() {
		return false;
	}

	/**
	 * 自定义listView 滑到最后一项
	 */
	protected void onListLastItemVisible() {

	}

	/**
	 * 自定义MultiColumnPullToRefreshListView 定义项监听
	 */
	public void onCustomItemClick(PLA_AdapterView<?> parent, View view,
			int position, long id) {
	}

	@Override
	public void onItemClick(PLA_AdapterView<?> parent, View view, int position,
			long id) {
		if (customListViewListener()) {
			onCustomItemClick(parent, view, position, id);
			return;
		}
	}

	private class CustomOnRefreshListener implements OnRefreshListener {

		@Override
		public void onRefresh() {
			onCustomRefresh();
		}

	}

	class ListItemListener implements OnLastItemVisibleListener {

		@Override
		public void onLastItemVisible() {
			onListLastItemVisible();
		}
	}

	/**
	 * 加载列表页数据
	 * 
	 * @param refresh
	 * @param data
	 * @param replace
	 */
	protected void requestListFinish(int refresh, List<E> data, boolean replace) {
		if (mListAdapter == null) {
			return;
		}
		if (mListAdapter != null) {
			if (refresh == PULL_TO_REFRESH) {
				if (replace) {
					mListAdapter.getDataSource().clear();
				}
				mListAdapter.setDataSource(data);
			} else {
				mListAdapter.addDataSource(data);
			}
		}
		mMultiColumnPullToRefreshListView.onRefreshComplete();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

}

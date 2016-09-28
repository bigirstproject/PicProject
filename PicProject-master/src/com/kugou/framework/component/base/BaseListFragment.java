package com.kugou.framework.component.base;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sunsun.picproject.R;

public abstract class BaseListFragment<E> extends BaseEmptyFragment implements
		OnItemClickListener {
	protected final int PULL_TO_REFRESH = 0;
	protected final int LOAD_MORE = 1;
	protected PullToRefreshListView mPullRefreshListView;
	protected ListView mListView;
	protected BaseImageAdapter<E> mListAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.base_list_fragment, container,
				false);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mPullRefreshListView = (PullToRefreshListView) view
				.findViewById(R.id.pull_refresh_list);
		if (needEnptyView()) {
			setContainer(mPullRefreshListView);
		}
		mPullRefreshListView.setMode(setRefreshMode());
		mPullRefreshListView
				.setOnRefreshListener(new CustomOnRefreshListener());
		mPullRefreshListView
				.setOnLastItemVisibleListener(new ListItemListener());
		mListView = mPullRefreshListView.getRefreshableView();
		mListAdapter = initListAdapter();
		mListView.setAdapter(mListAdapter);
		if (setListViewListener()) {
			mListView.setOnItemClickListener(this);
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
	 * listView刷新模式(上下、上、下、无四种模式)
	 * 
	 * @return
	 */
	protected Mode setRefreshMode() {
		return Mode.BOTH;
	}
	
	/**
	 * listView刷新模式(上下、上、下、无四种模式)
	 * 
	 * @return
	 */
	protected void setRefreshMode(Mode mode) {
		mPullRefreshListView.setMode(mode);
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
	public void onRefresh() {

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
	protected void onListLastItemVisible(){
		
	}

	/**
	 * 自定义listView 定义项监听
	 */
	public void onCustomItemClick(AdapterView<?> parent, View view,
			int position, long id) {
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (customListViewListener()) {
			onCustomItemClick(parent, view, position, id);
			return;
		}
	}

	private class CustomOnRefreshListener implements
			OnRefreshListener2<ListView> {

		@Override
		public void onPullDownToRefresh(
				PullToRefreshBase<ListView> paramPullToRefreshBase) {
			onRefresh();
		}

		@Override
		public void onPullUpToRefresh(
				PullToRefreshBase<ListView> paramPullToRefreshBase) {
			loadMore();
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
	 * @param hasMore
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
		mPullRefreshListView.onRefreshComplete();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
}

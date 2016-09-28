package com.kugou.framework.component.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.sunsun.picproject.R;

public abstract class BaseEmptyFragment extends BaseWorkerFragment {

	protected static final int VIEW_TYPE_EMPTY = 1;
	protected static final int VIEW_TYPE_DATA = 2;
	protected static final int VIEW_TYPE_LOADING = 3;

	protected LayoutInflater mInflater;
	private View mNoNetworkView;
	private Button mReLoadButton;
	private TextView mLoadTextingView;
	private View mDataLayout;
	private View mEmptyLayout;
	private TextView mEmptyTextView;
	private View mProgressBar;
	private View mProgressBarInner;
	private Animation mLoadingAnimation = null;

	protected String strEmptyReload;
	protected String strEmptyNoData;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.mInflater = inflater;
		strEmptyReload = getString(R.string.global_empty_reload);
		strEmptyNoData = getString(R.string.global_empty_no_data);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	/**
	 * 设置容器，并添加empty view
	 * 
	 * @param container
	 */
	protected void setContainer(View view) {
		if (view == null) {
			return;
		}
		this.mDataLayout = view;
		ViewGroup parent = (ViewGroup) view.getParent();
		if (mEmptyLayout == null) {
			mEmptyLayout = mInflater.inflate(R.layout.global_reload, parent,
					false);
			mNoNetworkView = mEmptyLayout.findViewById(R.id.load_fail_view);
			mReLoadButton = (Button) mEmptyLayout.findViewById(R.id.btn_reload);
			mReLoadButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onEmptyViewClicked();
				}
			});
			mProgressBar = mEmptyLayout.findViewById(R.id.loading_view);
			mProgressBarInner = mEmptyLayout
					.findViewById(R.id.reload_progressbar_inner);
			mLoadTextingView = (TextView) mEmptyLayout
					.findViewById(R.id.loading_text);
		} else {
			if (parent != null) {
				parent.removeView(mEmptyLayout);
			}
		}
		parent.addView(mEmptyLayout);
	}

	protected void showView(int viewType) {
		if (mDataLayout == null || mEmptyLayout == null) {
			return;
		}
		switch (viewType) {
		case VIEW_TYPE_DATA: {
			mDataLayout.setVisibility(View.VISIBLE);
			mEmptyLayout.setVisibility(View.GONE);
			mNoNetworkView.setVisibility(View.GONE);
			hidenLoading();
			break;
		}
		case VIEW_TYPE_EMPTY: {
			mDataLayout.setVisibility(View.GONE);
			mEmptyLayout.setVisibility(View.VISIBLE);
			mNoNetworkView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.GONE);
			hidenLoading();
			break;
		}
		case VIEW_TYPE_LOADING: {
			mDataLayout.setVisibility(View.GONE);
			mEmptyLayout.setVisibility(View.VISIBLE);
			mNoNetworkView.setVisibility(View.GONE);
			mProgressBar.setVisibility(View.VISIBLE);
			showLoading();
			break;
		}
		}
	}

	/**
	 * 为空，点击再一次加载
	 */
	protected void onEmptyViewClicked() {

	}

	protected void setEmptyText(String text) {
		if (mEmptyTextView != null) {
			mEmptyTextView.setText(text);
		}
	}

	protected void setLoadTextingView(String text) {
		if (mLoadTextingView != null) {
			mLoadTextingView.setText(text);
		}
	}

	/**
	 * 显示“正在加载”视图
	 */
	private void showLoading() {
		if (mLoadingAnimation == null) {
			mLoadingAnimation = AnimationUtils.loadAnimation(getActivity(),
					R.anim.article_detail_loading);
			mLoadingAnimation.setInterpolator(new LinearInterpolator());
			mLoadingAnimation.setFillAfter(true);// 动画停止时保持在该动画结束时的状态
		}
		mProgressBar.setVisibility(View.VISIBLE);
		if (mProgressBarInner != null) {
			mProgressBarInner.startAnimation(mLoadingAnimation);
		}
	}

	/**
	 * 隐藏“正在加载”视图
	 */
	protected void hidenLoading() {
		if (mProgressBarInner != null) {
			mProgressBarInner.clearAnimation();
		}
		mProgressBar.setVisibility(View.GONE);
	}

	protected onTabBarListener mOnTabBarListener;

	public void setTabBarListener(onTabBarListener listener) {
		this.mOnTabBarListener = listener;
	}

	public interface onTabBarListener {
		public void setTabBarListener(String tabTitle);
	}

}

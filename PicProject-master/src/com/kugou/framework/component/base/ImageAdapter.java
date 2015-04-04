package com.kugou.framework.component.base;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.sunsun.picproject.Image.SwitchImageLoader;

public abstract class ImageAdapter<E> extends BaseAdapter {

	protected SwitchImageLoader mImageLoader = SwitchImageLoader.getInstance();
	protected List<E> mDataSource = new ArrayList<E>();
	protected LayoutInflater mInflater;
	protected Context mContext;
	protected Resources mResource;

	public ImageAdapter(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mResource = context.getResources();
	}

	public void setDataSource(List<E> dataSource) {
		if (mDataSource != null) {
			mDataSource.clear();
		} else {
			mDataSource = new ArrayList<E>();
		}
		if (dataSource != null) {
			mDataSource.addAll(dataSource);
			notifyDataSetChanged();
		}
	}

	public void addDataSource(List<E> dataSource) {
		if (mDataSource == null) {
			mDataSource = new ArrayList<E>();
		}
		if (dataSource != null) {
			mDataSource.addAll(dataSource);
			notifyDataSetChanged();
		}
	}

	public List<E> getDataSource() {
		return mDataSource;
	}

	@Override
	public int getCount() {
		return mDataSource == null ? 0 : mDataSource.size();
	}

	@Override
	public E getItem(int position) {
		if (mDataSource == null || position < 0
				|| position >= mDataSource.size()) {
			return null;
		}
		return mDataSource.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	protected void displayImage(String url, ImageView view) {
		displayImage(url, view, null);
	}

	protected void displayImage(String url, ImageView view,
			DisplayImageOptions options) {

		displayImage(url, view, options, null);
	}

	public void displayImage(String url, ImageView imageView,
			DisplayImageOptions options, ImageLoadingListener listener) {

		mImageLoader.displayImage(url, imageView, options, listener, true);
	}

	protected void loadImage(String url, ImageLoadingListener listener) {
		mImageLoader.loadImage(url, listener);
	}

	/**
	 * To pause heavy data loading task for adapter to improve the performance
	 * if list view is doing UI operation like scrolling
	 */
	public void pause() {
		mImageLoader.pause();
	}

	/**
	 * Resume data loading when you finish UI operation
	 */
	public void resume() {
		mImageLoader.resume();
	}

}

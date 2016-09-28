package com.kugou.framework.component.base;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.sunsun.picproject.Image.SwitchImageLoader;

public abstract class BaseImageAdapter<E> extends BaseAdapter {

	protected SwitchImageLoader mImageLoader = SwitchImageLoader.getInstance();
	protected List<E> mDataSource = new ArrayList<E>();
	protected LayoutInflater mInflater;
	protected Context mContext;
	protected Resources mResource;

	public BaseImageAdapter(Context context) {
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

	public class CustomOnTouchListener implements OnTouchListener {
		Object e;

		public CustomOnTouchListener(Object e) {
			this.e = e;
		}

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_UP:
				changeLight((ImageView) view, 0);
				OnClickItem(e);
				// HomePageUserItemView.this.performClick();
				break;
			case MotionEvent.ACTION_DOWN:
				changeLight((ImageView) view, -10);
				break;
			case MotionEvent.ACTION_MOVE:
				// changeLight(view, 0);
				break;
			case MotionEvent.ACTION_CANCEL:
				changeLight((ImageView) view, 0);
				OnClickItem(e);
				break;
			default:
				break;
			}
			return true;
		}
	};

	protected CustomOnTouchListener CustomOnTouchItemListener(Object e) {
		return new CustomOnTouchListener(e);
	}

	protected void OnClickItem(Object e) {

	}

	private void changeLight(ImageView imageview, int brightness) {
		// if(brightness == 0){
		// imageview.setColorFilter(null);
		// return;
		// }
		ColorMatrix matrix = new ColorMatrix();
		matrix.set(new float[] { 1, 0, 0, 0, brightness, 0, 1, 0, 0,
				brightness, 0, 0, 1, 0, brightness, 0, 0, 0, 1, 0 });
		imageview.setColorFilter(new ColorMatrixColorFilter(matrix));
	}

}

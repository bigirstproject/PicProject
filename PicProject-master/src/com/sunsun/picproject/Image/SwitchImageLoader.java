package com.sunsun.picproject.Image;

import java.io.File;

import android.content.Context;
import android.widget.ImageView;

import com.kugou.framework.component.base.PicProjectApplication;
import com.nostra13.universalimageloader.cache.disc.impl.TotalSizeLimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.FailReason.FailType;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.sunsun.picproject.R;

/**
 * 封装了ImageLoader，添加网络检查
 * 
 */
public class SwitchImageLoader {

	private static SwitchImageLoader INSTANCE;
	private ImageLoader mImageLoader;
	public static DisplayImageOptions DEFAULT_DISPLAYER = getDisplayOptions(0); // 默认是0
	public static DisplayImageOptions DEFAULT_CHANNEL_SMALL_DISPLAYER = getDisplayOptions(R.drawable.ic_placeholder_small);
	public static DisplayImageOptions DEFAULT_CHANNEL_BIG_DISPLAYER = getDisplayOptions(R.drawable.ic_placeholder_big);
	public static DisplayImageOptions DEFAULT_ARTICLE_ITEM_DISPLAYER = getDisplayOptions(R.drawable.article_list_item_loading);
	public static DisplayImageOptions DEFAULT_ARTICLE_ITEM_BIG_DISPLAYER = getDisplayOptions(R.drawable.article_list_item_loading_big);

	public static DisplayImageOptions getDisplayOptions(int imageDefault) {
		return getDisplayOptions(imageDefault, imageDefault, imageDefault);
	}

	private static DisplayImageOptions getDisplayOptions(int imageOnFail,
			int imageOnLoading, int imageForEmptyUri) {
		return new DisplayImageOptions.Builder().resetViewBeforeLoading(true)
				.cacheInMemory(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.showImageOnFail(imageOnFail)
				.showImageOnLoading(imageOnLoading)
				.showImageForEmptyUri(imageForEmptyUri).build();
	}

	public SwitchImageLoader() {
		mImageLoader = ImageLoader.getInstance();
	}

	public static SwitchImageLoader getInstance() {
		if (INSTANCE == null) {
			synchronized (SwitchImageLoader.class) {
				if (INSTANCE == null) {
					INSTANCE = new SwitchImageLoader();
				}
			}
		}
		return INSTANCE;
	}

	public static void init(Context context) {
		// ImageUtil.setDiscCache(context);
		File individualCacheDir = StorageUtils
				.getIndividualCacheDirectory(context);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				PicProjectApplication.getInstance())
				.discCache(
						new TotalSizeLimitedDiscCache(individualCacheDir,
								1024 * 1024 * 10))
				.defaultDisplayImageOptions(DEFAULT_DISPLAYER).build();
		ImageLoader.getInstance().init(config);
	}

	public void displayImage(String url, ImageView view) {
		displayImage(url, view, true);
	}

	public void displayImage(String url, ImageView view,
			DisplayImageOptions options) {
		displayImage(url, view, options, true);
	}

	/**
	 * 
	 * @param url
	 * @param view
	 * @param forceUpdate
	 *            是否强制update
	 */
	public void displayImage(String url, ImageView view, boolean forceUpdate) {

		displayImage(url, view, null, forceUpdate);
	}

	/**
	 * 
	 * @param url
	 * @param view
	 * @param forceUpdate
	 *            是否强制update
	 */
	public void displayImage(String url, ImageView view,
			DisplayImageOptions options, boolean forceUpdate) {

		displayImage(url, view, options, null, forceUpdate);
	}

	public void displayImage(String url, ImageView imageView,
			ImageLoadingListener listener) {

		displayImage(url, imageView, null, listener, true);
	}

	public void displayImage(String url, ImageView imageView,
			DisplayImageOptions options, ImageLoadingListener listener,
			boolean forceUpdate) {

		if (forceUpdate || mImageLoader.getDiscCache().get(url).exists()) {
			mImageLoader.displayImage(url, imageView, options, listener);
		}
	}

	public void loadImage(String url, ImageLoadingListener listener) {
		loadImage(url, listener, true);
	}

	public void loadImage(String url, ImageLoadingListener listener,
			boolean forceUpdate) {
		if (forceUpdate || mImageLoader.getDiscCache().get(url).exists()) {
			mImageLoader.loadImage(url, listener);
		} else {
			if (listener != null) {
				listener.onLoadingFailed(url, null, new FailReason(
						FailType.NETWORK_DENIED, new Throwable(
								"当前仅在wifi下加载图片，加载失败")));
			}
		}
	}

	public void pause() {
		mImageLoader.pause();
	}

	public void resume() {
		mImageLoader.resume();
	}
}

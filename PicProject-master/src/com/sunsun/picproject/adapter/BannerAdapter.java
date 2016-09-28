package com.sunsun.picproject.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.kugou.framework.component.base.BaseImageAdapter;
import com.sunsun.picproject.R;
import com.sunsun.picproject.bean.BannerTable;
import com.sunsun.picproject.view.AutoAdjustImageView;

public class BannerAdapter extends BaseImageAdapter<BannerTable> {

	public BannerAdapter(Context context) {
		super(context);
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	private static final int VIEW_TYPE_IMAGE = 0;

	@Override
	public int getItemViewType(int position) {

		return VIEW_TYPE_IMAGE;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		switch (getItemViewType(position)) {
		case VIEW_TYPE_IMAGE: {
			convertView = getImageTypeView(convertView, position);
			break;
		}
		}
		return convertView;
	}

	private View getImageTypeView(View convertView, int position) {
		ImageViewHolder holder = null;
		if (convertView == null) {
			holder = new ImageViewHolder();
			convertView = mInflater.inflate(R.layout.banner_item, null);
			holder.mView = (AutoAdjustImageView) convertView
					.findViewById(R.id.image);
			convertView.setTag(holder);
		} else {
			holder = (ImageViewHolder) convertView.getTag();
		}
		displayImage(getItem(position).getImage(), holder.mView);
		return convertView;
	}

	private static class ImageViewHolder {
		AutoAdjustImageView mView;
	}

}

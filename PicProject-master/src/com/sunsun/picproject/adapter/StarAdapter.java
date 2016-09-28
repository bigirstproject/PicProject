package com.sunsun.picproject.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kugou.framework.component.base.BaseImageAdapter;
import com.sunsun.picproject.R;
import com.sunsun.picproject.activity.RecommendActivity;
import com.sunsun.picproject.bean.GalleryTable;

public class StarAdapter extends BaseImageAdapter<GalleryTable> {

	public static final int TYPE = 0;

	public StarAdapter(Context context) {
		super(context);
	}

	@Override
	public int getCount() {
		return mDataSource.size() % 2 == 0 ? mDataSource.size() / 2
				: mDataSource.size() / 2 + 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		int itemViewType = getItemViewType(position);
		if (convertView == null) {
			convertView = getConvertView(itemViewType);
		}
		holder = (ViewHolder) convertView.getTag();

		displayImage(mDataSource.get(position * 2).getImage(),
				holder.imageviewOne);
		// holder.imageviewOne.setOnClickListener(new OnClickItem(mDataSource
		// .get(position * 2)));
		holder.imageviewOne
				.setOnTouchListener(CustomOnTouchItemListener(mDataSource
						.get(position * 2)));
		holder.titleOne.setText(mDataSource.get(position * 2).getTitle());
		if (position * 2 + 1 < mDataSource.size()) {
			displayImage(mDataSource.get(position * 2 + 1).getImage(),
					holder.imageviewTwo);
			// holder.imageviewTwo.setOnClickListener(new
			// OnClickItem(mDataSource
			// .get(position * 2 + 1)));
			holder.imageviewTwo
					.setOnTouchListener(CustomOnTouchItemListener(mDataSource
							.get(position * 2 + 1)));
			holder.titleTwo.setText(mDataSource.get(position * 2 + 1)
					.getTitle());
		} else {
			holder.imageviewTwo.setVisibility(View.INVISIBLE);
			holder.titleTwo.setVisibility(View.INVISIBLE);
		}

		return convertView;
	}

	private View getConvertView(int itemViewType) {
		ViewHolder holder = null;
		View convertView = null;
		switch (itemViewType) {
		case TYPE:
			convertView = mInflater.inflate(R.layout.star_item, null);
			break;
		default:
			break;
		}
		holder = new ViewHolder();
		holder.imageviewOne = (ImageView) convertView
				.findViewById(R.id.gallery_one);
		holder.imageviewTwo = (ImageView) convertView
				.findViewById(R.id.gallery_two);
		holder.titleOne = (TextView) convertView.findViewById(R.id.text_one);
		holder.titleTwo = (TextView) convertView.findViewById(R.id.text_two);
		if (convertView != null) {
			convertView.setTag(holder);
		}
		return convertView;
	}

	// class OnClickItem implements OnClickListener {
	// GalleryTable item;
	//
	// public OnClickItem(GalleryTable item) {
	// this.item = item;
	// }
	//
	// @Override
	// public void onClick(View v) {
	// switch (item.getType()) {
	// case TYPE:
	// if (!TextUtils.isEmpty(item.getUrl())) {
	// RecommendActivity.startRecommendActivity(mContext,
	// item.getUrl());
	// }
	// break;
	// default:
	// break;
	// }
	//
	// }
	// }

	@Override
	protected void OnClickItem(Object e) {
		super.OnClickItem(e);
		GalleryTable item = (GalleryTable) e;
		if (!TextUtils.isEmpty(item.getUrl())) {
			RecommendActivity.startRecommendActivity(mContext, item.getUrl());
		}
	}

	@Override
	public int getItemViewType(int position) {
		return TYPE;
	}

	public int getViewTypeCount() {
		return 1;
	}

	private static class ViewHolder {
		ImageView imageviewOne;
		ImageView imageviewTwo;
		TextView titleOne;
		TextView titleTwo;
	}

}

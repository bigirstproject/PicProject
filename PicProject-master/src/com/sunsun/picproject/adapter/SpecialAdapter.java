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
import com.sunsun.picproject.activity.ViewPagerZoomActivity;
import com.sunsun.picproject.bean.GalleryTable;

public class SpecialAdapter extends BaseImageAdapter<GalleryTable> {

	public static final int TYPE = 0;

	public SpecialAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		int itemViewType = getItemViewType(position);
		if (convertView == null) {
			convertView = getConvertView(itemViewType);
		}
		holder = (ViewHolder) convertView.getTag();
		GalleryTable item = getItem(position);

		displayImage(item.getImage(), holder.image);
		holder.image.setOnTouchListener(CustomOnTouchItemListener(item));
		if (!TextUtils.isEmpty(item.getTitle())) {
			holder.title.setText(item.getTitle());
		}
		return convertView;
	}

	private View getConvertView(int itemViewType) {
		ViewHolder holder = null;
		View convertView = null;
		switch (itemViewType) {
		case TYPE:
			convertView = mInflater.inflate(R.layout.goods_item, null);
			break;
		default:
			break;
		}
		holder = new ViewHolder();
		holder.image = (ImageView) convertView.findViewById(R.id.image);
		holder.title = (TextView) convertView.findViewById(R.id.title);
		if (convertView != null) {
			convertView.setTag(holder);
		}
		return convertView;
	}

	@Override
	protected void OnClickItem(Object e) {
		super.OnClickItem(e);
		GalleryTable item = (GalleryTable) e;
		if (item != null && !TextUtils.isEmpty(item.getUrl())) {
			RecommendActivity.startRecommendActivity(mContext,
					item.getUrl());
		}
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
	// ViewPagerZoomActivity.startViewPagerZoomActivity(mContext,
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
	public int getItemViewType(int position) {
		return TYPE;
	}

	public int getViewTypeCount() {
		return 1;
	}

	private static class ViewHolder {
		ImageView image;
		TextView title;
	}

}

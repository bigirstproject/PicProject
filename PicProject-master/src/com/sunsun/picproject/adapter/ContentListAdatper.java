package com.sunsun.picproject.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kugou.framework.component.base.BaseImageAdapter;
import com.kugou.framework.component.debug.KGLog;
import com.sunsun.picproject.R;
import com.sunsun.picproject.activity.RecommendActivity;
import com.sunsun.picproject.activity.SpecialActivity;
import com.sunsun.picproject.activity.ViewPagerZoomActivity;
import com.sunsun.picproject.bean.GalleryTable;
import com.sunsun.picproject.bean.MainPagerTable;

public class ContentListAdatper extends BaseImageAdapter<MainPagerTable> {

	public static final int TYPE_ONE = 0;
	public static final int TYPE_TWO = 1;
	public static final int TYPE_THREE = 2;

	public ContentListAdatper(Context context) {
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
		MainPagerTable item = getItem(position);
		updateCorner(holder, item, convertView);
		return convertView;
	}

	private void updateCorner(ViewHolder holder, MainPagerTable model,
			View convertView) {
		if (model == null) {
			return;
		}
		switch (model.getType()) {
		case TYPE_ONE:
			holder.titleOne.setVisibility(View.VISIBLE);
			holder.titleOne.setText(model.getTitle());
			break;
		case TYPE_TWO:
			List<GalleryTable> galleryTwo = model.getGalleryTwo();
			for (int i = 0; i < galleryTwo.size(); i++) {
				if (galleryTwo.get(i) != null) {
					if (i % 2 == 0) {
						displayImage(galleryTwo.get(i).getImage(),
								holder.imageviewOne);
//						holder.imageviewOne.setOnClickListener(new OnClickItem(
//								galleryTwo.get(i)));
						holder.imageviewOne.setOnTouchListener(CustomOnTouchItemListener(galleryTwo.get(i)));
						KGLog.d(galleryTwo.get(i).getImage());
					} else if (i % 2 == 1) {
						displayImage(galleryTwo.get(i).getImage(),
								holder.imageviewTwo);
//						holder.imageviewTwo.setOnClickListener(new OnClickItem(
//								galleryTwo.get(i)));
						holder.imageviewTwo.setOnTouchListener(CustomOnTouchItemListener(galleryTwo.get(i)));
						KGLog.d(galleryTwo.get(i).getImage());
					}
				}
			}
			break;
		case TYPE_THREE:
			List<GalleryTable> galleryThree = model.getGalleryThree();
			for (int i = 0; i < galleryThree.size(); i++) {
				if (galleryThree.get(i) != null) {
					if (i % 3 == 0) {
						displayImage(galleryThree.get(i).getImage(),
								holder.imageviewOne);
						if (!TextUtils.isEmpty(galleryThree.get(i).getTitle())) {
							holder.titleOne.setVisibility(View.VISIBLE);
							holder.titleOne.setText(galleryThree.get(i)
									.getTitle());
						} else {
							holder.titleOne.setVisibility(View.GONE);
						}
//						holder.imageviewOne.setOnClickListener(new OnClickItem(
//								galleryThree.get(i)));
						holder.imageviewOne.setOnTouchListener(CustomOnTouchItemListener(galleryThree.get(i)));
						KGLog.d(galleryThree.get(i).getImage());
					} else if (i % 3 == 1) {
						displayImage(galleryThree.get(i).getImage(),
								holder.imageviewTwo);
						if (!TextUtils.isEmpty(galleryThree.get(i).getTitle())) {
							holder.titleTwo.setVisibility(View.VISIBLE);
							holder.titleTwo.setText(galleryThree.get(i)
									.getTitle());
						} else {
							holder.titleTwo.setVisibility(View.GONE);
						}
//						holder.imageviewTwo.setOnClickListener(new OnClickItem(
//								galleryThree.get(i)));
						holder.imageviewTwo.setOnTouchListener(CustomOnTouchItemListener(galleryThree.get(i)));
						KGLog.d(galleryThree.get(i).getImage());
					} else if (i % 3 == 2) {
						displayImage(galleryThree.get(i).getImage(),
								holder.imageviewThree);
						if (!TextUtils.isEmpty(galleryThree.get(i).getTitle())) {
							holder.titleThree.setVisibility(View.VISIBLE);
							holder.titleThree.setText(galleryThree.get(i)
									.getTitle());
						} else {
							holder.titleThree.setVisibility(View.GONE);
						}
//						holder.imageviewThree
//								.setOnClickListener(new OnClickItem(
//										galleryThree.get(i)));
						holder.imageviewThree.setOnTouchListener(CustomOnTouchItemListener(galleryThree.get(i)));
						KGLog.d(galleryThree.get(i).getImage());
					}
				}
			}
			break;
		default:
			break;
		}
	}

	private View getConvertView(int itemViewType) {
		ViewHolder holder = null;
		View convertView = null;
		switch (itemViewType) {
		case TYPE_ONE:
			convertView = mInflater.inflate(R.layout.common_one_title_layout,
					null);
			break;
		case TYPE_TWO:
			convertView = mInflater.inflate(R.layout.gallery_two_layout, null);
			break;
		case TYPE_THREE:
			convertView = mInflater
					.inflate(R.layout.gallery_three_layout, null);
			break;
		default:
			break;
		}
		holder = new ViewHolder();
		holder.titleOne = (TextView) convertView
				.findViewById(R.id.gallery_title_one);
		holder.imageviewOne = (ImageView) convertView
				.findViewById(R.id.gallery_one);
		holder.titleTwo = (TextView) convertView
				.findViewById(R.id.gallery_title_two);
		holder.imageviewTwo = (ImageView) convertView
				.findViewById(R.id.gallery_two);
		holder.titleThree = (TextView) convertView
				.findViewById(R.id.gallery_title_three);
		holder.imageviewThree = (ImageView) convertView
				.findViewById(R.id.gallery_three);
		if (convertView != null) {
			convertView.setTag(holder);
		}
		return convertView;
	}

//	class OnClickItem implements OnClickListener {
//		GalleryTable item;
//
//		public OnClickItem(GalleryTable item) {
//			this.item = item;
//		}
//
//		@Override
//		public void onClick(View v) {
//			switch (item.getType()) {
//			case TYPE_TWO:
//				if (!TextUtils.isEmpty(item.getUrl())) {
//					ViewPagerZoomActivity.startViewPagerZoomActivity(mContext,
//							item.getUrl());
//				}
//				break;
//			case TYPE_THREE:
//				if (!TextUtils.isEmpty(item.getUrl())
//						&& item.getRecommendId() == 5) {
//					SpecialActivity.startSpecialActivity(mContext, item.getUrl());
//				} else if (!TextUtils.isEmpty(item.getUrl())) {
//					RecommendActivity.startRecommendActivity(mContext,
//							item.getUrl());
//				}
//				break;
//			default:
//				break;
//			}
//
//		}
//	}
	
	@Override
	protected void OnClickItem(Object e) {
		super.OnClickItem(e);
		GalleryTable item =(GalleryTable) e;
		switch (item.getType()) {
		case TYPE_TWO:
			if (!TextUtils.isEmpty(item.getUrl())) {
				ViewPagerZoomActivity.startViewPagerZoomActivity(mContext,
						item.getUrl());
			}
			break;
		case TYPE_THREE:
			if (!TextUtils.isEmpty(item.getUrl())
					&& item.getRecommendId() == 5) {
				SpecialActivity.startSpecialActivity(mContext, item.getUrl());
			} else if (!TextUtils.isEmpty(item.getUrl())) {
				RecommendActivity.startRecommendActivity(mContext,
						item.getUrl());
			}
			break;
		default:
			break;
		}

	}

	@Override
	public int getItemViewType(int position) {
		MainPagerTable item = getItem(position);
		switch (item.getType()) {
		case MainPagerTable.TYPE_ONE:
			return TYPE_ONE;
		case MainPagerTable.TYPE_TWO:
			return TYPE_TWO;
		case MainPagerTable.TYPE_THREE:
			return TYPE_THREE;
		default:
			return TYPE_ONE;
		}
	}

	public int getViewTypeCount() {
		return 3;
	}

	private static class ViewHolder {
		TextView titleOne;
		ImageView imageviewOne;
		TextView titleTwo;
		ImageView imageviewTwo;
		TextView titleThree;
		ImageView imageviewThree;
	}

}

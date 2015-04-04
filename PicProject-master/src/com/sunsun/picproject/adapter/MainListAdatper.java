package com.sunsun.picproject.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kugou.framework.component.base.ImageAdapter;
import com.kugou.framework.component.debug.KGLog;
import com.sunsun.picproject.R;
import com.sunsun.picproject.test.ViewPagerActivity;
import com.sunsun.slidingmenu.bean.GalleryItemTable;
import com.sunsun.slidingmenu.bean.MainPagerTable;

public class MainListAdatper extends ImageAdapter<MainPagerTable> {

	public final int ONE_STYLE = 0;
	public final int TWO_STYLE = 1;
	public final int THREE_STYLE = 2;

	public MainListAdatper(Context context) {
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
		case ONE_STYLE:
			holder.titleOne.setVisibility(View.VISIBLE);
			holder.titleOne.setText(model.getTitle());
			break;
		case TWO_STYLE:
			List<GalleryItemTable> galleryTwo = model.getGalleryTwo();
			for (int i = 0; i < galleryTwo.size(); i++) {
				if (galleryTwo.get(i) != null) {
					if (i % 2 == 0) {
						displayImage(galleryTwo.get(i).getImage(),
								holder.imageviewOne);
						holder.imageviewOne.setOnClickListener(new OnClickItem(
								galleryTwo.get(i)));
						KGLog.d(galleryTwo.get(i).getImage());
					} else if (i % 2 == 1) {
						displayImage(galleryTwo.get(i).getImage(),
								holder.imageviewTwo);
						holder.imageviewTwo.setOnClickListener(new OnClickItem(
								galleryTwo.get(i)));
						KGLog.d(galleryTwo.get(i).getImage());
					} 
				}
			}
			break;
		case THREE_STYLE:
			List<GalleryItemTable> galleryThree = model.getGalleryThree();
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
						holder.imageviewOne.setOnClickListener(new OnClickItem(
								galleryThree.get(i)));
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
						holder.imageviewTwo.setOnClickListener(new OnClickItem(
								galleryThree.get(i)));
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
						holder.imageviewThree
								.setOnClickListener(new OnClickItem(
										galleryThree.get(i)));
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
		case ONE_STYLE:
			convertView = mInflater.inflate(R.layout.common_one_title_layout,
					null);
			break;
		case TWO_STYLE:
			convertView = mInflater.inflate(R.layout.gallery_two_layout, null);
			break;
		case THREE_STYLE:
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

	class OnClickItem implements OnClickListener {
		GalleryItemTable item;

		public OnClickItem(GalleryItemTable item) {
			this.item = item;
		}

		@Override
		public void onClick(View v) {
			Toast.makeText(mContext, item.getUrl(), Toast.LENGTH_SHORT)
					.show();
			ViewPagerActivity.startViewPagerActivity(mContext);
		}
	}

	@Override
	public int getItemViewType(int position) {
		MainPagerTable item = getItem(position);
		switch (item.getType()) {
		case MainPagerTable.ONE_STYLE:
			return ONE_STYLE;
		case MainPagerTable.TWO_STYLE:
			return TWO_STYLE;
		case MainPagerTable.THREE_STYLE:
			return THREE_STYLE;
		default:
			return ONE_STYLE;
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

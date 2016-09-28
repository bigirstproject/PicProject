package com.sunsun.picproject.Frament;

import java.util.ArrayList;

import net.youmi.android.offers.OffersManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kugou.framework.component.preference.AppCommonPref;
import com.sunsun.picproject.R;
import com.sunsun.picproject.activity.SettingActivity;
import com.sunsun.picproject.bean.MenuTable;
import com.sunsun.picproject.event.SettingEvent;
import com.sunsun.picproject.event.ToggleEvent;
import com.sunsun.picproject.util.Utils;

import de.greenrobot.event.EventBus;

public class MenuFragment extends Fragment implements OnItemClickListener,
		OnClickListener {

	private ListView mListView;
	private RelativeLayout mSetting;
	private RelativeLayout mRecommend;

	private String[] mTitle;

	private String[] mUrl;
	private String[] mNextUrl;;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Utils.getAppCommonPref() == 1) {
			mTitle = getActivity().getResources().getStringArray(
					R.array.column_title);
		} else {
			mTitle = getActivity().getResources().getStringArray(
					R.array.test_column_title);
		}

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.menu_listview_layout, null);
		mListView = (ListView) view.findViewById(R.id.listview);
		mSetting = (RelativeLayout) view.findViewById(R.id.setting);
		mRecommend = (RelativeLayout) view.findViewById(R.id.recommend);
		mSetting.setOnClickListener(this);
		if (Utils.getAppCommonPref() == 1) {
			mRecommend.setOnClickListener(this);
			mUrl = getActivity().getResources().getStringArray(
					R.array.column_url);
		} else {
			mUrl = getActivity().getResources().getStringArray(
					R.array.test_column_url);
			mNextUrl = getActivity().getResources().getStringArray(
					R.array.test_column_next_url);
			mRecommend.setVisibility(View.GONE);
			mRecommend.setOnClickListener(null);
		}
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting:
			SettingActivity.startSpecialActivity(getActivity());
			EventBus.getDefault().post(new SettingEvent());
			break;
		case R.id.recommend:
			OffersManager.getInstance(getActivity()).showOffersWall();
			break;
		default:
			break;
		}
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MenuAdapter memuAdapter = new MenuAdapter();
		mListView.setOnItemClickListener(this);
		mListView.setAdapter(memuAdapter);
		memuAdapter.setData(getData());
	}

	public ArrayList<MenuTable> getData() {
		ArrayList<MenuTable> listData = new ArrayList<MenuTable>();
		if (AppCommonPref.getInstance().getHasOpen() == 1) {
			for (int i = 0; i < mTitle.length; i++) {
				MenuTable table = new MenuTable();
				table.setId(i);
				table.setTitle(mTitle[i]);
				table.setUrl(mUrl[i]);
				listData.add(table);
			}
		} else {
			for (int i = 0; i < mTitle.length; i++) {
				MenuTable table = new MenuTable();
				table.setId(i);
				table.setTitle(mTitle[i]);
				table.setUrl(mUrl[i]);
				table.setNextUrl(mNextUrl[i]);
				listData.add(table);
			}
		}
		return listData;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		MenuTable table = (MenuTable) parent.getAdapter().getItem(position);
		ToggleEvent event = new ToggleEvent();
		event.setTable(table);
		EventBus.getDefault().post(event);
	}

	public class MenuAdapter extends BaseAdapter {

		private ArrayList<MenuTable> mListData;

		public void setData(ArrayList<MenuTable> listData) {
			this.mListData = listData;
			notifyDataSetChanged();
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewItem mViewItem = null;
			if (convertView == null) {
				mViewItem = new ViewItem();
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.common_listview_item, null);
				mViewItem.img = (ImageView) convertView
						.findViewById(R.id.row_icon);
				mViewItem.txt = (TextView) convertView
						.findViewById(R.id.row_title);
				convertView.setTag(mViewItem);
			} else {
				mViewItem = (ViewItem) convertView.getTag();
			}
			mViewItem.img.setImageResource(R.drawable.channel_icon);
			if (mListData != null) {
				mViewItem.txt.setText(mListData.get(position).getTitle());
			}
			return convertView;
		}

		@Override
		public int getCount() {
			if (mListData != null) {
				return mListData.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return mListData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

	}

	class ViewItem {
		ImageView img;
		TextView txt;
	}

}

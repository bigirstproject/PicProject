package com.sunsun.slidingmenu.Frament;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sunsun.picproject.R;
import com.sunsun.slidingmenu.bean.MenuBehindTable;
import com.sunsun.slidingmenu.event.ToggleEvent;

import de.greenrobot.event.EventBus;

public class MenuBehindListFragment extends Fragment implements
		OnItemClickListener {

	private ListView mListView;

	private String[] mTitle = new String[] { "性感美女", "丝袜美腿", "高清美女", "网络美女",
			"唯美写真", "体育美女", "模特美女", "明星专辑" };

	private String[] mUrl = new String[] { "http://m.4493.com/xingganmote/",
			"http://m.4493.com/siwameitui/", "http://m.4493.com/gaoqingmeinv/",
			"http://m.4493.com/wangluomeinv/",
			"http://m.4493.com/weimeixiezhen/", "http://m.4493.com/tiyumeinv/",
			"http://m.4493.com/motemeinv/", "http://m.4493.com/star/" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.menu_listview_layout, null);
		mListView = (ListView) view.findViewById(R.id.listview);
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		SampleAdapter adapter = new SampleAdapter();
		mListView.setOnItemClickListener(this);
		mListView.setAdapter(adapter);
		adapter.setData(getData());
	}

	public ArrayList<MenuBehindTable> getData() {
		ArrayList<MenuBehindTable> listData = new ArrayList<MenuBehindTable>();
		for (int i = 0; i < mTitle.length; i++) {
			MenuBehindTable table = new MenuBehindTable();
			table.setId(i + 1);
			table.setTitle(mTitle[i]);
			table.setPicture(mUrl[i]);
			listData.add(table);
		}
		return listData;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		MenuBehindTable table = (MenuBehindTable) parent.getAdapter().getItem(
				position);
		ToggleEvent event = new ToggleEvent();
		event.setTable(table);
		EventBus.getDefault().post(event);
	}

	public class SampleAdapter extends BaseAdapter {

		private ArrayList<MenuBehindTable> mListData;

		public void setData(ArrayList<MenuBehindTable> listData) {
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
			mViewItem.img.setImageResource(R.drawable.ic_launcher);
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

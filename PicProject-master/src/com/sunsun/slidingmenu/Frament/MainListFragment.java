package com.sunsun.slidingmenu.Frament;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kugou.framework.component.base.BaseWorkerFragment;
import com.sunsun.picproject.R;
import com.sunsun.picproject.adapter.MainListAdatper;
import com.sunsun.picproject.http.SimpleHttp;
import com.sunsun.picproject.util.Constants;
import com.sunsun.slidingmenu.bean.GalleryItemTable;
import com.sunsun.slidingmenu.bean.MainPagerTable;

public class MainListFragment extends BaseWorkerFragment {

	private PullToRefreshListView mPullRefreshListView;
	private ListView mListView;
	private MainListAdatper mAdapter;
	private int pagesize = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.common_listview_layout, null);
		mPullRefreshListView = (PullToRefreshListView) view
				.findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setMode(Mode.BOTH);
		mListView = mPullRefreshListView.getRefreshableView();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		sendEmptyBackgroundMessage(REQUEST_CODE);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mPullRefreshListView
				.setOnRefreshListener(new CustomOnRefreshListener2());
		mPullRefreshListView
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

					@Override
					public void onLastItemVisible() {
//						Toast.makeText(getActivity(), "End of List!",
//								Toast.LENGTH_SHORT).show();
					}
				});
		mAdapter = new MainListAdatper(getActivity());
		mListView.setAdapter(mAdapter);
	}

	class CustomOnRefreshListener2 implements OnRefreshListener2<ListView> {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			sendEmptyBackgroundMessage(REQUEST_CODE);
//			Toast.makeText(getActivity(), "onPullDownToRefresh",
//					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			Message requsetMsg = new Message();
			requsetMsg.what = REQUEST_MORE_CODE;
			requsetMsg.arg1 = pagesize;
			sendBackgroundMessage(requsetMsg);
//			Toast.makeText(getActivity(), "onPullUpToRefresh",
//					Toast.LENGTH_SHORT).show();
		}
	}

	public final int REQUEST_CODE = 0;
	public final int REPONSE_CODE = 1;
	public final int REQUEST_MORE_CODE = 2;
	public final int REPONSE_MORE_CODE = 3;

	@Override
	protected void handleBackgroundMessage(Message msg) {
		switch (msg.what) {
		case REQUEST_CODE:
			try {
				byte[] requestData = SimpleHttp.RequestGet(Constants.BASE_URL);
				String data = new String(requestData, "GBK");
				Document parse = Jsoup.parse(data, Constants.BASE_URL);
				Element body = parse.body();
				List<MainPagerTable> list = new ArrayList<MainPagerTable>();
				MainPagerTable mainTable = null;
				List<GalleryItemTable> table = null;

				/* 精选推荐标题 */
				Elements elements = body.getElementsByClass("titline");
				mainTable = new MainPagerTable();
				mainTable.setType(MainPagerTable.ONE_STYLE);
				mainTable.setTitle(elements.get(0).text());
				list.add(mainTable);
				/* 精选推荐标题 */

				/* 精选推荐 */
				elements = body.getElementsByClass("tjlist");
				if (elements == null) {
					return;
				}
				Element element = elements.get(0);
				Elements elementsByTag = element.getElementsByTag("a");
				for (int i = 0; i < elementsByTag.size(); i++) {
					Element element2 = elementsByTag.get(i);
					String attr2 = element2.attr("href");
					Elements elementsByTag2 = element2.getElementsByTag("img");
					if (elementsByTag2 != null && elementsByTag2.size() > 0) {
						Element element3 = elementsByTag2.get(elementsByTag2
								.size() - 1);
						String attr = element3.attr("src");
						if (i % 3 == 0) {
							mainTable = new MainPagerTable();
							mainTable.setType(MainPagerTable.THREE_STYLE);
							table = new ArrayList<GalleryItemTable>();
							mainTable.setGalleryThree(table);
							list.add(mainTable);
						}
						GalleryItemTable galleryItemTable = new GalleryItemTable();
						galleryItemTable.setId(i);
						galleryItemTable.setImage(attr);
						galleryItemTable.setUrl(attr2);
						table.add(galleryItemTable);
					}
				}
				/* 精选推荐 */

				/* 最新美图标题 */
				elements = body.getElementsByClass("titline");
				mainTable = new MainPagerTable();
				mainTable.setType(MainPagerTable.ONE_STYLE);
				mainTable.setTitle(elements.get(elements.size() - 1).text());
				list.add(mainTable);
				/* 最新美图标题 */

				/* 最新美图 */
				elements = body.getElementsByClass("listpic");
				if (elements == null) {
					return;
				}
				element = elements.get(0);
				elementsByTag = element.getElementsByTag("a");
				for (int i = 0; i < elementsByTag.size(); i++) {
					Element element2 = elementsByTag.get(i);
					String attr2 = element2.attr("href");
					Elements elementsByTag2 = element2.getElementsByTag("img");
					if (elementsByTag2 != null && elementsByTag2.size() > 0) {
						Element element3 = elementsByTag2.get(elementsByTag2
								.size() - 1);
						String attr = element3.attr("src");
						if (i % 2 == 0) {
							mainTable = new MainPagerTable();
							mainTable.setType(MainPagerTable.TWO_STYLE);
							table = new ArrayList<GalleryItemTable>();
							mainTable.setGalleryTwo(table);
							list.add(mainTable);
						}
						GalleryItemTable galleryItemTable = new GalleryItemTable();
						galleryItemTable.setId(i);
						galleryItemTable.setImage(attr);
						galleryItemTable.setUrl(attr2);
						table.add(galleryItemTable);
					}
				}
				/* 最新美图 */

				Message requsetMsg = new Message();
				requsetMsg.what = REPONSE_CODE;
				requsetMsg.obj = list;
				sendUiMessage(requsetMsg);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case REQUEST_MORE_CODE:
			if (msg != null) {
				int pagesize = (int) msg.arg1;
				if (pagesize < 0) {
					return;
				}
				byte[] requestData = null;
				try {
					requestData = SimpleHttp
							.RequestGet(Constants.MAIN_LOAD_MORE + pagesize
									+ Constants.URL_END);
					String data = new String(requestData, "GBK");
					if (TextUtils.isEmpty(data)) {
						return;
					}
					JSONArray json = new JSONArray(data);
					List<MainPagerTable> list = new ArrayList<MainPagerTable>();
					MainPagerTable mainTable = null;
					ArrayList<GalleryItemTable> table = null;
					for (int i = 0; i < json.length(); i++) {
						if (i % 2 == 0) {
							mainTable = new MainPagerTable();
							mainTable.setType(MainPagerTable.TWO_STYLE);
							table = new ArrayList<GalleryItemTable>();
							mainTable.setGalleryTwo(table);
							list.add(mainTable);
						}
						JSONObject jsonObject = json.getJSONObject(i);
						GalleryItemTable galleryItemTable = new GalleryItemTable();
						galleryItemTable.setId(Integer.valueOf(jsonObject
								.getString("id")));
						galleryItemTable
								.setTitle(jsonObject.getString("title"));
						galleryItemTable.setImage(Constants.BASE_URL
								+ jsonObject.getString("picture"));
						galleryItemTable.setUrl(Constants.BASE_URL
								+ "xingganmote/" + jsonObject.getString("id")+".html");
						table.add(galleryItemTable);
					}
					Message requsetMsg = new Message();
					requsetMsg.what = REPONSE_MORE_CODE;
					requsetMsg.obj = list;
					sendUiMessage(requsetMsg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleUiMessage(Message msg) {
		super.handleUiMessage(msg);
		switch (msg.what) {
		case REPONSE_CODE:
			if (msg != null && msg.obj instanceof List<?>) {
				List<MainPagerTable> list = (List<MainPagerTable>) msg.obj;
				if (list != null) {
					mAdapter.setDataSource(list);
					mPullRefreshListView.onRefreshComplete();
				}
			}
			break;
		case REPONSE_MORE_CODE:
			if (msg != null && msg.obj instanceof List<?>) {
				List<MainPagerTable> list = (List<MainPagerTable>) msg.obj;
				if (list != null) {
					mAdapter.addDataSource(list);
					mPullRefreshListView.onRefreshComplete();
					pagesize++;
				}
			}
			break;
		default:
			break;
		}
	}
}

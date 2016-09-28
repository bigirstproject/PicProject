package com.sunsun.picproject.Frament;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.kugou.framework.component.base.BaseImageAdapter;
import com.kugou.framework.component.base.BaseListFragment;
import com.sunsun.picproject.R;
import com.sunsun.picproject.activity.GoodsRecommendActivity;
import com.sunsun.picproject.adapter.SpecialAdapter;
import com.sunsun.picproject.bean.GalleryTable;
import com.sunsun.picproject.http.SimpleHttp;
import com.sunsun.picproject.util.Constants;
import com.sunsun.picproject.viewpager.InfiniteBannerView;

public class SpecialFragment extends BaseListFragment<GalleryTable> {

	private static final String TAG  =SpecialFragment.class.getName();

	public static final String URL = "url";
	public static final String TITLE = "title";

	private String url;
	private String morePageCode;
	private int pagesize = 2;
	private int mFirstPagerIndex = 1;
	private boolean hasData = true;
	private int mMoreIndex = 2;
	protected InfiniteBannerView mBannerView;
	protected LinearLayout mBannerLogo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			url = getArguments().getString(URL);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		showView(VIEW_TYPE_LOADING);
		sendEmptyBackgroundMessage(REQUEST_CODE);
	}

	@Override
	protected boolean customListViewListener() {
		return true;
	}

	@Override
	protected void loadMore() {
		super.loadMore();
		Message requsetMsg = new Message();
		requsetMsg.what = REQUEST_MORE_CODE;
		requsetMsg.arg1 = pagesize;
		sendBackgroundMessage(requsetMsg);
	}

	@Override
	public void onRefresh() {
		super.onRefresh();
		sendEmptyBackgroundMessage(REQUEST_CODE);
	}

	@Override
	protected void onListLastItemVisible() {
		if (!hasData) {
			mPullRefreshListView.onRefreshComplete();
			showToast(getActivity().getString(R.string.global_no_data));
			setRefreshMode(Mode.PULL_FROM_START);
		}
	}

	/**
	 * 下拉刷新成功
	 */
	private void setRefreshSuceess() {
		hasData = true;
		pagesize = mMoreIndex;
		setRefreshMode(Mode.BOTH);
	}
	
	@Override
	protected void onEmptyViewClicked() {
		super.onEmptyViewClicked();
		showView(VIEW_TYPE_LOADING);
		sendEmptyBackgroundMessage(REQUEST_CODE);
	}

	@Override
	public void onCustomItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		GalleryTable item = (GalleryTable) parent.getAdapter()
				.getItem(position);
		if (item != null) {
			GoodsRecommendActivity.startGoodsRecommendActivity(getActivity(),
					item.getUrl());
		}
	}

	public final int REQUEST_CODE = 100;
	public final int REPONSE_CODE = 200;
	public final int REQUEST_MORE_CODE = 300;
	public final int REPONSE_MORE_CODE = 400;
	public final int REQUEST_NO_DATA_CODE = 500;
	public final int REPONSE_NO_MORE_DATA_CODE = 600;
	public final int REPONSE_NO_DATA_CODE = 700;

	@Override
	protected void handleBackgroundMessage(Message msg) {
		switch (msg.what) {
		case REQUEST_CODE:
			try {
				if (TextUtils.isEmpty(url)) {
					return;
				}
				byte[] requestData = SimpleHttp.RequestGet(url);
				String data = new String(requestData, "GBK");
				Document parse = Jsoup.parse(data, url);
				Element body = parse.body();

				/* 标题 */
				Elements headerDiv = body.getElementsByClass("header");
				if (headerDiv != null) {
					Elements headerspan = headerDiv.get(0).getElementsByTag(
							"span");
					if (headerspan != null) {
						if (mOnTabBarListener != null) {
							mOnTabBarListener.setTabBarListener(headerspan
									.text());
						}
					}
				}
				/* 标题 */

				/* pageCode */
				Elements pageline = body.getElementsByClass("pageline");
				if (pageline != null) {
					Elements a = pageline.get(0).getElementsByTag("a");
					if (a != null) {
						morePageCode = a.attr("onClick");
					}
				}
				/* pageCode */

				/* 精选推荐 */
				List<GalleryTable> listData = new ArrayList<GalleryTable>();
				Elements elements = body.getElementsByClass("list_xilie");
				if (elements == null) {
					return;
				}
				Element element = elements.get(0);
				Elements elementsa = element.getElementsByTag("a");
				Elements elementsSpan = element.getElementsByTag("span");
				if (elements == null || elements.size() == 0) {
					return;
				}
				for (int i = 0; i < elementsa.size(); i++) {
					Element element2 = elementsa.get(i);
					String title = elementsSpan.get(i).text();
					char[] charArray = title.toCharArray();
					for (int j = 0; j < charArray.length; j++) {
						if (Character.isDigit(charArray[j])) {
							title = title.substring(0, j);
							break;
						}
					}
					Elements elementsByTag2 = element2.getElementsByTag("img");
					if (elementsByTag2 != null && elementsByTag2.size() > 0) {
						Element element3 = elementsByTag2.get(elementsByTag2
								.size() - 1);
						GalleryTable table = new GalleryTable();
						table.setId(i);
						table.setType(SpecialAdapter.TYPE);
						table.setTitle(title);
						table.setImage(element3.attr("src"));
						table.setUrl(Constants.BASE_URL.substring(0,
								Constants.BASE_URL.length())
								+ element2.attr("href"));
						listData.add(table);
					}
				}
				/* 精选推荐 */

				Message requsetMsg = new Message();
				requsetMsg.what = REPONSE_CODE;
				requsetMsg.obj = listData;
				sendUiMessage(requsetMsg);
			} catch (Exception e) {
				e.printStackTrace();
				sendEmptyUiMessage(REPONSE_NO_DATA_CODE);
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
					String url = Constants.XILIE_LOAD_MORE + pagesize;
					requestData = SimpleHttp.RequestGet(url);
					String data = new String(requestData, "UTF-8");
					if (TextUtils.isEmpty(data)) {
						sendEmptyUiMessage(REPONSE_NO_MORE_DATA_CODE);
						return;
					}
					Document parse = Jsoup.parse(data, url);
					Elements elements = parse.getElementsByTag("a");
					Elements elementsSpan = parse.getElementsByTag("span");
					if (elements == null) {
						sendEmptyUiMessage(REPONSE_NO_MORE_DATA_CODE);
						return;
					}
					List<GalleryTable> listData = new ArrayList<GalleryTable>();
					for (int i = 0; i < elements.size(); i++) {
						String title = elementsSpan.get(i).text();
						char[] charArray = title.toCharArray();
						for (int j = 0; j < charArray.length; j++) {
							if (Character.isDigit(charArray[j])) {
								title = title.substring(0, j);
								break;
							}
						}
						Element element = elements.get(i);
						GalleryTable table = new GalleryTable();
						table.setId(i);
						table.setTitle(title);
						table.setImage(element.getElementsByTag("img").get(0)
								.attr("src"));
						table.setUrl(Constants.BASE_URL.substring(0,
								Constants.BASE_URL.length())
								+ element.attr("href"));
						table.setType(SpecialAdapter.TYPE);
						listData.add(table);
					}
					Message requsetMsg = new Message();
					requsetMsg.what = REPONSE_MORE_CODE;
					requsetMsg.obj = listData;
					sendUiMessage(requsetMsg);
				} catch (Exception e) {
					sendEmptyUiMessage(REQUEST_NO_DATA_CODE);
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
				List<GalleryTable> data = (List<GalleryTable>) msg.obj;
				if (data != null) {
					requestListFinish(PULL_TO_REFRESH, data, true);
					showView(VIEW_TYPE_DATA);
					setRefreshSuceess();
				} else {
					showView(VIEW_TYPE_EMPTY);
				}
			}
			break;
		case REPONSE_MORE_CODE:
			if (msg != null && msg.obj instanceof List<?>) {
				List<GalleryTable> data = (List<GalleryTable>) msg.obj;
				if (data != null && data.size() > 0) {
					requestListFinish(LOAD_MORE, data, false);
					pagesize++;
				} else {
					sendEmptyUiMessage(REPONSE_NO_MORE_DATA_CODE);
				}
			}
			break;
		case REPONSE_NO_MORE_DATA_CODE:
			hasData = false;
			onListLastItemVisible();
			break;
		case REPONSE_NO_DATA_CODE:
			hasData = false;
			onListLastItemVisible();
			break;
		default:
			break;
		}
	}

	@Override
	protected BaseImageAdapter<GalleryTable> initListAdapter() {

		return new SpecialAdapter(getActivity());
	}

	@Override
	protected String tag() {
		return TAG;
	}

}

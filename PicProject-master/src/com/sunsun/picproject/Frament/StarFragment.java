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
import android.widget.LinearLayout;

import com.kugou.framework.component.base.BaseImageAdapter;
import com.kugou.framework.component.base.BaseListFragment;
import com.sunsun.picproject.adapter.RecommendAdapter;
import com.sunsun.picproject.adapter.StarAdapter;
import com.sunsun.picproject.bean.GalleryTable;
import com.sunsun.picproject.http.SimpleHttp;
import com.sunsun.picproject.util.Constants;
import com.sunsun.picproject.viewpager.InfiniteBannerView;

public class StarFragment extends BaseListFragment<GalleryTable> {
	private static final String TAG  =StarFragment.class.getName();

	public static final String URL = "url";

	private String url;
	private String morePageCode;
	private int pagesize = 2;
	private int mFirstPagerIndex = 1;
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
	protected void onEmptyViewClicked() {
		super.onEmptyViewClicked();
		showView(VIEW_TYPE_LOADING);
		sendEmptyBackgroundMessage(REQUEST_CODE);
	}

	public final int REQUEST_CODE = 100;
	public final int REPONSE_CODE = 200;
	public final int REQUEST_MORE_CODE = 300;
	public final int REPONSE_MORE_CODE = 400;
	public final int REQUEST_BANNER_CODE = 500;
	public final int REPONSE_BANNER_CODE = 600;
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

				List<GalleryTable> listData = new ArrayList<GalleryTable>();
				/* 精选推荐 */
				Elements elements = body.getElementsByClass("tjlist");
				if (elements == null) {
					return;
				}
				Element element = elements.get(0);
				Elements elementsByTag = element.getElementsByTag("a");
				if (elements == null || elements.size() == 0) {
					return;
				}
				for (int i = 0; i < elementsByTag.size(); i++) {
					Element element2 = elementsByTag.get(i);
					Elements elementsByTag2 = element2.getElementsByTag("img");
					if (elementsByTag2 != null && elementsByTag2.size() > 0) {
						Element element3 = elementsByTag2.get(elementsByTag2
								.size() - 1);
						GalleryTable table = new GalleryTable();
						table.setId(i);
						table.setType(RecommendAdapter.TYPE);
						table.setImage(element3.attr("src"));
						table.setUrl("http://m.4493.com"+element2.attr("href"));
						table.setTitle(element2.getElementsByTag("span").text());
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
					String url = Constants.STAR_MORE + pagesize;
					requestData = SimpleHttp.RequestGet(url);
					String data = new String(requestData, "UTF-8");
					if (TextUtils.isEmpty(data)) {
						return;
					}
					Document parse = Jsoup.parse(data, url);
					List<GalleryTable> listData = new ArrayList<GalleryTable>();
					Elements tags = parse.getElementsByTag("li");
					for (int i = 0; i < tags.size(); i++) {
						GalleryTable table = new GalleryTable();
						Elements a = tags.get(i).getElementsByTag("a");
						table.setUrl("http://m.4493.com"
								+ a.get(0).attr("href"));
						Elements imgs = a.get(0).getElementsByTag("img");
						table.setImage(imgs.get(imgs.size() - 1).attr("src"));
						table.setType(RecommendAdapter.TYPE);
						table.setTitle(a.get(0).getElementsByTag("span").get(0)
								.text());
						listData.add(table);
					}
					Message requsetMsg = new Message();
					requsetMsg.what = REPONSE_MORE_CODE;
					requsetMsg.obj = listData;
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
				List<GalleryTable> data = (List<GalleryTable>) msg.obj;
				if (data != null) {
					requestListFinish(PULL_TO_REFRESH, data, true);
					showView(VIEW_TYPE_DATA);
					pagesize = mMoreIndex;
				} else {
					showView(VIEW_TYPE_EMPTY);
				}
			}
			break;
		case REPONSE_MORE_CODE:
			if (msg != null && msg.obj instanceof List<?>) {
				List<GalleryTable> data = (List<GalleryTable>) msg.obj;
				if (data != null) {
					requestListFinish(LOAD_MORE, data, false);
					pagesize++;
				}
			}
			break;
		case REPONSE_NO_DATA_CODE:
			showView(VIEW_TYPE_EMPTY);
			break;
		default:
			break;
		}
	}

	@Override
	protected BaseImageAdapter<GalleryTable> initListAdapter() {

		return new StarAdapter(getActivity());
	}

	@Override
	protected String tag() {
		return TAG;
	}

}
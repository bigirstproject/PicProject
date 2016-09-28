package com.sunsun.picproject.Frament;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.kugou.framework.component.base.BaseImageAdapter;
import com.kugou.framework.component.base.WaterPullBaseFragment;
import com.sunsun.picproject.adapter.waterPulllAdapter;
import com.sunsun.picproject.bean.GalleryTable;
import com.sunsun.picproject.http.SimpleHttp;
import com.sunsun.picproject.util.Constants;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class PicFragment extends WaterPullBaseFragment<GalleryTable> {

	private static final String TAG = PicFragment.class.getName();

	public static final String URL = "url";
	public static final String NEXTURL = "next_url";

	private String url;
	private String nextUrl;
	private int pagesize = 1;
	private int mFirstPagerIndex = 1;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (getArguments() != null) {
			url = getArguments().getString(URL, Constants.HUAYUDIANYING);
			nextUrl = getArguments().getString(NEXTURL,
					Constants.HUAYUDIANYING_NEXT);
		} else {
			url = Constants.HUAYUDIANYING;
			nextUrl = Constants.HUAYUDIANYING_NEXT;
		}
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
	public void onCustomRefresh() {
		super.onCustomRefresh();
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
				byte[] requestData = SimpleHttp.RequestGet(url);
				String data = new String(requestData, "gb2312");
				Document parse = Jsoup.parse(data, url);
				Element body = parse.body();
				Elements elementsByClass = body.getElementsByClass("item1");
				List<GalleryTable> list = new ArrayList<GalleryTable>();
				for (int i = 0; i < elementsByClass.size(); i++) {
					Element element = elementsByClass.get(i);
					Elements elementsByTagA = element.getElementsByTag("a");
					Elements elementsByTagImg = element.getElementsByTag("img");
					GalleryTable item = new GalleryTable();
					item.setId(i);
					item.setUrl(Constants.FJ_BASE_URL
							+ elementsByTagA.get(0).attr("href"));
					// item.setTitle(elementsByTagA.get(0).attr("title"));
					item.setImage(elementsByTagImg.get(0).attr("src"));
					list.add(item);
				}
				Log.d("test", elementsByClass.toString());
				Message requsetMsg = new Message();
				requsetMsg.what = REPONSE_CODE;
				requsetMsg.obj = list;
				sendUiMessage(requsetMsg);
			} catch (Exception e) {
				e.printStackTrace();
				sendEmptyUiMessage(REPONSE_NO_DATA_CODE);
			}
			break;
		case REQUEST_MORE_CODE:
			if (msg != null) {
				try {
					byte[] requestData = SimpleHttp.RequestGet(nextUrl
							+ pagesize + Constants.END);
					String data = new String(requestData, "gb2312");
					Document parse = Jsoup.parse(data, nextUrl + pagesize
							+ Constants.END);
					Element body = parse.body();
					Elements elementsByClass = body.getElementsByClass("item1");
					List<GalleryTable> list = new ArrayList<GalleryTable>();
					for (int i = 0; i < elementsByClass.size(); i++) {
						Element element = elementsByClass.get(i);
						Elements elementsByTagA = element.getElementsByTag("a");
						Elements elementsByTagImg = element
								.getElementsByTag("img");
						GalleryTable item = new GalleryTable();
						item.setId(i);
						item.setUrl(Constants.FJ_BASE_URL
								+ elementsByTagA.get(0).attr("href"));
						// item.setTitle(elementsByTagA.get(0).attr("title"));
						item.setImage(elementsByTagImg.get(0).attr("src"));
						list.add(item);
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
				List<GalleryTable> data = (List<GalleryTable>) msg.obj;
				if (data != null && data.size() > 0) {
					requestListFinish(PULL_TO_REFRESH, data, false);
					showView(VIEW_TYPE_DATA);
					pagesize++;
				} else {
					showView(VIEW_TYPE_EMPTY);
				}
			} else {
				showView(VIEW_TYPE_EMPTY);
			}
			break;
		case REPONSE_MORE_CODE:
			if (msg != null && msg.obj instanceof List<?>) {
				List<GalleryTable> data = (List<GalleryTable>) msg.obj;
				if (data != null && data.size() > 0) {
					requestListFinish(LOAD_MORE, data, false);
					pagesize++;
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected BaseImageAdapter<GalleryTable> initListAdapter() {
		return new waterPulllAdapter(getActivity());
	}

	@Override
	protected String tag() {
		return TAG;
	}

}
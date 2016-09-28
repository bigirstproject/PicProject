package com.sunsun.picproject.Frament;

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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kugou.framework.component.base.BaseBannerFragment;
import com.kugou.framework.component.base.BaseImageAdapter;
import com.kugou.framework.component.preference.AppCommonPref.Keys;
import com.sunsun.picproject.R;
import com.sunsun.picproject.adapter.BannerAdapter;
import com.sunsun.picproject.adapter.ContentListAdatper;
import com.sunsun.picproject.bean.BannerTable;
import com.sunsun.picproject.bean.GalleryTable;
import com.sunsun.picproject.bean.MainPagerTable;
import com.sunsun.picproject.http.SimpleHttp;
import com.sunsun.picproject.util.Constants;
import com.sunsun.picproject.viewpager.InfiniteBannerView;

public class ContentListFragment extends
		BaseBannerFragment<MainPagerTable, BannerTable> {

	private static final String TAG = ContentListFragment.class.getName();

	private int pagesize = 2;
	private int mFirstPagerIndex = 1;
	private int mMoreIndex = 2;
	protected InfiniteBannerView mBannerView;
	protected LinearLayout mBannerLogo;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		showView(VIEW_TYPE_LOADING);
		new GetDiskCacheTask(Keys.PIC_MAIN_BANNER_CAHCE, 1).execute();
		new GetDiskCacheTask(Keys.PIC_MAIN_LIST_CAHCE, 2).execute();
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
				byte[] requestData = SimpleHttp.RequestGet(Constants.BASE_URL);
				String data = new String(requestData,"gbk");
				Document parse = Jsoup.parse(data, Constants.BASE_URL);
				Element body = parse.body();
				/* banner 轮播图 */
				Elements bannerElements = body.getElementsByClass("bingo");
				List<BannerTable> bannerData = new ArrayList<BannerTable>();
				for (int i = 0; i < bannerElements.size(); i++) {
					Element element = bannerElements.get(i);
					Elements elementsByTag = element.getElementsByTag("a");
					Element element2 = elementsByTag.get(0);
					BannerTable banner = new BannerTable();
					banner.setId(i);
					banner.setType(1);
					banner.setImage(element2.getElementsByTag("img").get(0)
							.attr("src"));
					banner.setUrl(element2.attr("href"));
					bannerData.add(banner);
				}
				if (bannerData != null && bannerData.size() > 0) {
					new SaveDiskCacheTask(bannerData,
							Keys.PIC_MAIN_BANNER_CAHCE).execute();
				}
				refreshResponseBannerData(bannerData);

				/* banner 轮播图 */

				List<MainPagerTable> list = new ArrayList<MainPagerTable>();
				MainPagerTable mainTable = null;
				List<GalleryTable> table = null;

				/* 精选推荐标题 */
				Elements elements = body.getElementsByClass("titline");
				mainTable = new MainPagerTable();
				mainTable.setType(MainPagerTable.TYPE_ONE);
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
							mainTable.setType(MainPagerTable.TYPE_THREE);
							table = new ArrayList<GalleryTable>();
							mainTable.setGalleryThree(table);
							list.add(mainTable);
						}
						GalleryTable galleryItemTable = new GalleryTable();
						galleryItemTable.setId(i);
						galleryItemTable.setRecommendId(i);
						galleryItemTable.setType(GalleryTable.TYPE_THREE);
						galleryItemTable.setImage(attr);
						galleryItemTable.setUrl(attr2);
						table.add(galleryItemTable);
					}
				}
				/* 精选推荐 */

				/* 最新美图标题 */
				elements = body.getElementsByClass("titline");
				mainTable = new MainPagerTable();
				mainTable.setType(MainPagerTable.TYPE_ONE);
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
							mainTable.setType(MainPagerTable.TYPE_TWO);
							table = new ArrayList<GalleryTable>();
							mainTable.setGalleryTwo(table);
							list.add(mainTable);
						}
						GalleryTable galleryItemTable = new GalleryTable();
						galleryItemTable.setId(i);
						galleryItemTable.setType(GalleryTable.TYPE_TWO);
						galleryItemTable.setImage(attr);
						galleryItemTable.setUrl(attr2);
						table.add(galleryItemTable);
					}
				}
				/* 最新美图 */
				if (list != null && list.size() > 0) {
					new SaveDiskCacheTask(list, Keys.PIC_MAIN_LIST_CAHCE)
							.execute();
				}
				refreshResponseListData(list);
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
					requestData = SimpleHttp
							.RequestGet(Constants.CONTENT_LOAD_MORE
									+ Constants.channel[0] + Constants.PAGE
									+ pagesize + Constants.URL_END);
					String data = new String(requestData, "GBK");
					if (TextUtils.isEmpty(data)) {
						return;
					}
					JSONArray json = new JSONArray(data);
					List<MainPagerTable> list = new ArrayList<MainPagerTable>();
					MainPagerTable mainTable = null;
					ArrayList<GalleryTable> table = null;
					for (int i = 0; i < json.length(); i++) {
						if (i % 2 == 0) {
							mainTable = new MainPagerTable();
							mainTable.setType(MainPagerTable.TYPE_TWO);
							table = new ArrayList<GalleryTable>();
							mainTable.setGalleryTwo(table);
							list.add(mainTable);
						}
						JSONObject jsonObject = json.getJSONObject(i);
						GalleryTable galleryItemTable = new GalleryTable();
						galleryItemTable.setId(Integer.valueOf(jsonObject
								.getString("id")));
						galleryItemTable.setType(GalleryTable.TYPE_TWO);
						galleryItemTable
								.setTitle(jsonObject.getString("title"));
						galleryItemTable.setImage(Constants.BASE_URL
								+ jsonObject.getString("picture"));
						galleryItemTable.setUrl(Constants.BASE_URL
								+ "xingganmote/" + jsonObject.getString("id")
								+ ".html");
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

	private void refreshResponseListData(List<MainPagerTable> list) {
		Message requsetMsg = new Message();
		requsetMsg.what = REPONSE_CODE;
		requsetMsg.obj = list;
		sendUiMessage(requsetMsg);
	}

	private void refreshResponseBannerData(List<BannerTable> bannerData) {
		Message bannerMsg = new Message();
		bannerMsg.what = REPONSE_BANNER_CODE;
		bannerMsg.obj = bannerData;
		sendUiMessage(bannerMsg);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleUiMessage(Message msg) {
		super.handleUiMessage(msg);
		switch (msg.what) {
		case REPONSE_CODE:
			if (msg != null && msg.obj instanceof List<?>) {
				List<MainPagerTable> data = (List<MainPagerTable>) msg.obj;
				if (data != null && data.size() > 0) {
					requestListFinish(PULL_TO_REFRESH, data, true);
					showView(VIEW_TYPE_DATA);
					pagesize = mMoreIndex;
				} else {
					showView(VIEW_TYPE_EMPTY);
				}
			} else {
				showView(VIEW_TYPE_EMPTY);
			}
			break;
		case REPONSE_MORE_CODE:
			if (msg != null && msg.obj instanceof List<?>) {
				List<MainPagerTable> data = (List<MainPagerTable>) msg.obj;
				if (data != null && data.size() > 0) {
					requestListFinish(LOAD_MORE, data, false);
					pagesize++;
				}
			}
			break;
		case REPONSE_BANNER_CODE:
			if (msg != null && msg.obj instanceof List<?>) {
				List<BannerTable> bannerData = (List<BannerTable>) msg.obj;
				if (bannerData != null && bannerData.size() > 0) {
					requestBannerFinish(PULL_TO_REFRESH, bannerData, true);
					if (bannerLogo()) {
						addBannerView(mBannerLogo, bannerData.size());
					}
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

	@SuppressWarnings("unchecked")
	@Override
	protected void getDiskCache(Object cache, int index) {
		super.getDiskCache(cache, index);
		if (cache != null && cache instanceof List<?>) {
			if (index == 1) {
				refreshResponseBannerData((List<BannerTable>) cache);
			} else if (index == 2) {
				refreshResponseListData((List<MainPagerTable>) cache);
			}
			showView(VIEW_TYPE_DATA);
		}
	}

	@Override
	protected View addListViewHeader() {
		View viewHeader = mInflater.inflate(R.layout.banner_layout, null);
		mBannerView = (InfiniteBannerView) viewHeader
				.findViewById(R.id.banner_list);
		mBannerLogo = (LinearLayout) viewHeader.findViewById(R.id.banner_logo);
		mBannerView.setListAdapter(mBannerAdapter);
		mBannerView.setOnPageChangeListener(new TopPageChangedListener());
		mBannerView.startScroll();
		mBannerView.setOnItemClickListener(this);
		return viewHeader;
	}

	@Override
	protected void setBannerIndexs(int index) {
		if (mBannerLogo == null || mBannerLogo.getChildCount() == 0) {
			return;
		}
		for (int i = 0; i < mBannerLogo.getChildCount(); i++) {
			ImageView viewChild = (ImageView) mBannerLogo.getChildAt(i);
			index = index % mBannerLogo.getChildCount();
			if (index == i) {
				viewChild.setImageResource(R.drawable.banner_pressed);
			} else {
				viewChild.setImageResource(R.drawable.banner_unpressed);
			}
		}
	}

	@Override
	protected BaseImageAdapter<BannerTable> initBannerAdapter() {
		return new BannerAdapter(getActivity());
	}

	@Override
	protected BaseImageAdapter<MainPagerTable> initListAdapter() {
		return new ContentListAdatper(getActivity());
	}

	@Override
	protected String tag() {
		return TAG;
	}

}
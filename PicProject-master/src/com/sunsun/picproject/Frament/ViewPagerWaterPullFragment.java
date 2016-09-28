package com.sunsun.picproject.Frament;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kugou.framework.component.base.BaseEmptyFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sunsun.picproject.R;
import com.sunsun.picproject.adapter.GalleryAdapter;
import com.sunsun.picproject.bean.ZoomPhotosTable.PicAddress;
import com.sunsun.picproject.http.SimpleHttp;
import com.sunsun.picproject.util.AnimationHelper;
import com.sunsun.picproject.util.Constants;
import com.sunsun.picproject.util.DWToast;
import com.sunsun.picproject.util.Utils;
import com.sunsun.picproject.viewpager.ScrollBannerView;

public class ViewPagerWaterPullFragment extends BaseEmptyFragment implements
		OnPageChangeListener, OnClickListener {

	public static final String TAG = ViewPagerWaterPullFragment.class
			.getSimpleName();

	public final int REQUEST_CODE = 100;
	public final int REPONSE_CODE = 200;
	public final int REQUEST_MORE_CODE = 300;
	public final int REPONSE_MORE_CODE = 400;
	public final int REPONSE_NO_DATA_CODE = 500;

	public static final int FROM_ARTILCE = 1;
	public static final int FROM_NEWS_LIST = 2;

	public static final String KEY_SELECT_POS = "select_pos";
	public static final String LIST_KEY = "list_key";
	public static final String TYPE_KEY = "type_key";

	public static final int POSITION_FIRST = 0;
	public static final int POSITION_LAST = -1;
	private final int FILT_DURATION = 20;

	private ScrollBannerView mBanner;
	private GalleryAdapter mGalleryAdapter;

	private LinearLayout mBottomBar;
	private TextView mTitleTextView;
	private TextView mPageSize;
	private ImageButton mBackImageView;
	private Button mWallpager;

	private Animation mAnimRadioUpToDownIn;
	private Animation mAnimRadioUpToDownOut;

	private List<PicAddress> mDatasource;
	private String url;
	private String mContent;
	private int mSelectPos;
	private int mTotalSize;

	private boolean isAnimating;
	private boolean mAllBarVisible = true;
	private List<PicAddress> listData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bunble = getArguments();
		if (bunble != null) {
			url = bunble.getString(LIST_KEY);
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initAmin();
		updateAdapterData(mSelectPos);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.zoom_gallery_activity, container,
				false);
		mBottomBar = (LinearLayout) view.findViewById(R.id.top_bar);
		mBanner = (ScrollBannerView) view.findViewById(R.id.pager);
		mWallpager = (Button) view.findViewById(R.id.wallpager);
		setContainer(mBanner);
		mTitleTextView = (TextView) view.findViewById(R.id.page_title);
		mPageSize = (TextView) view.findViewById(R.id.page_number);
		mBackImageView = (ImageButton) view.findViewById(R.id.back_img);
		mBackImageView.setOnClickListener(this);
		mWallpager.setOnClickListener(this);
		mGalleryAdapter = new GalleryAdapter(getActivity());
		mGalleryAdapter.setOnViewTapListener(listener);
		mBanner.setListAdapter(mGalleryAdapter);
		mBanner.setOnPageChangeListener(this);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		showView(VIEW_TYPE_LOADING);
		sendEmptyBackgroundMessage(REQUEST_CODE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_img:
			getActivity().onBackPressed();
			break;
		case R.id.wallpager:
			File file = ImageLoader
					.getInstance()
					.getDiscCache()
					.get(mGalleryAdapter.getDataSource().get(mSelectPos)
							.getUrl());
			if (file != null) {
				Bitmap decodeFile = BitmapFactory.decodeFile(file
						.getAbsolutePath());
				if (decodeFile != null) {
					Utils.setWallpaper(getActivity(), decodeFile);
					DWToast.makeText(
							getActivity(),
							getActivity().getResources().getString(
									R.string.set_wallerpager_sucess),
							Toast.LENGTH_SHORT).show();
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int position) {
		updateCurrentPos(position);
		mSelectPos = position;
	}

	@Override
	public void onPageScrollStateChanged(int position) {

	}

	private void initAmin() {
		mAnimRadioUpToDownIn = AnimationHelper.createAnimUpToDownIn(
				getActivity(), mAnimListener);
		mAnimRadioUpToDownOut = AnimationHelper.createAnimUpToDownOut(
				getActivity(), mAnimListener);
	}

	private static final int MSG_SHOW_RADIO = 1001;
	private static final int MSG_HIDE_RADIO = 1002;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SHOW_RADIO: {
				showMainRadioNow();
				break;
			}
			case MSG_HIDE_RADIO: {
				hideMainRadioNow();
				break;
			}
			}
		}
	};

	private void showMainRadioNow() {
		if (isAnimating) {
			return;
		}
		mAnimRadioUpToDownIn.cancel();
		mBottomBar.startAnimation(mAnimRadioUpToDownIn);
	}

	private void hideMainRadioNow() {
		if (isAnimating) {
			return;
		}
		mAnimRadioUpToDownOut.cancel();
		mBottomBar.startAnimation(mAnimRadioUpToDownOut);
	}

	private OnViewTapListener listener = new OnViewTapListener() {

		@Override
		public void onViewTap(View view, float x, float y) {
			if (mAllBarVisible) {
				hideMainRadio(FILT_DURATION);
			} else {
				showMainRadio(FILT_DURATION);
			}
		}

	};

	private void showMainRadio(int delay) {
		if (!mHandler.hasMessages(MSG_SHOW_RADIO)) {
			mHandler.removeMessages(MSG_HIDE_RADIO);
			mHandler.sendEmptyMessageDelayed(MSG_SHOW_RADIO, delay);
		}
	}

	private void hideMainRadio(int delay) {
		if (!mHandler.hasMessages(MSG_HIDE_RADIO)) {
			mHandler.removeMessages(MSG_SHOW_RADIO);
			mHandler.sendEmptyMessageDelayed(MSG_HIDE_RADIO, delay);
		}
	}

	private AnimationListener mAnimListener = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {
			isAnimating = true;
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			isAnimating = false;
			if (mAllBarVisible) {
				mAllBarVisible = false;
				onHeaderFooterHide();
			} else {
				mAllBarVisible = true;
				onHeaderFooterShow();
			}
		}
	};

	protected void onHeaderFooterShow() {
		mBackImageView.setClickable(true);
	}

	protected void onHeaderFooterHide() {
		mBackImageView.setClickable(false);
	}

	private void updateAdapterData(int selectPos) {
		if (mDatasource != null) {
			if (selectPos >= mDatasource.size()) {
				selectPos = 0;
			}
			if (selectPos == POSITION_LAST) {
				selectPos = getImageSum() - 1;
			}
			mGalleryAdapter.setDataSource(mDatasource);
			mBanner.setCurrentItem(selectPos, false);
			updateCurrentPos(selectPos);
		}
	}

	private void updateCurrentPos(int position) {
		String text = String.format("%d/%d", getImagePos(position),
				getImageSum());
		SpannableString spanText = new SpannableString(text);
		int index = text.indexOf("/");
		spanText.setSpan(new AbsoluteSizeSpan(14, true), 0, index,
				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		spanText.setSpan(new ForegroundColorSpan(Color.WHITE), 0, index,
				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		mPageSize.setText(spanText);
		if (!TextUtils.isEmpty(mContent)) {
			int indexOf = mContent.indexOf("/");
			if (indexOf >= 1) {
				mContent = mContent.substring(0, indexOf - 1);
			}
			mTitleTextView.setText(mContent);
		}
	}

	protected int getImagePos(int pagePos) {
		return pagePos + 1;
	}

	protected int getImageSum() {
		return mGalleryAdapter.getCount();
	}

	@Override
	protected void handleBackgroundMessage(Message msg) {
		switch (msg.what) {
		case REQUEST_CODE:
			if (TextUtils.isEmpty(url)) {
				return;
			}
			try {
				byte[] requestData = SimpleHttp.RequestGet(url);
				String data = new String(requestData, "gb2312");
				Document parse = Jsoup.parse(data, url);
				Element body = parse.body();
				Elements name = body.getElementsByClass("bread-nav");
				String text = name.get(name.size() - 1).text();
				mContent = text;
				int index = text.indexOf("/");
				mTotalSize = Integer.valueOf(text.substring(index + 1,
						text.length()));

				listData = new ArrayList<PicAddress>();
				Elements elements = body.getElementsByClass("pic");
				PicAddress item = new PicAddress();
				Elements elementsByTag = elements.get(0)
						.getElementsByTag("img");
				item.setId(0);
				item.setUrl(elementsByTag.get(0).attr("src"));
				listData.add(item);

				Message requsetMsg = new Message();
				requsetMsg.what = REQUEST_MORE_CODE;
				requsetMsg.obj = Constants.FJ_BASE_URL
						+ elements
								.get(0)
								.getElementsByTag("a")
								.get(elements.get(0).getElementsByTag("a")
										.size() - 1).attr("href");
				sendBackgroundMessage(requsetMsg);

			} catch (Exception e) {
				e.printStackTrace();
				sendEmptyUiMessage(REPONSE_NO_DATA_CODE);
			}
			break;
		case REQUEST_MORE_CODE:
			if (msg.obj != null && msg.obj instanceof String) {
				String nextUrl = (String) msg.obj;
				for (int i = 1; i < mTotalSize; i++) {
					try {
						byte[] requestData = SimpleHttp.RequestGet(nextUrl);
						String data = new String(requestData, "gb2312");
						Document parse = Jsoup.parse(data, nextUrl);
						Element body = parse.body();
						// Elements name = body.getElementsByClass("bread-nav");
						// String text = name.get(name.size() - 1).text();
						// mContent = text;
						// int index = text.indexOf("/");
						// TotalSize = Integer.valueOf(text.substring(index + 1,
						// text.length()));

						Elements elements = body.getElementsByClass("pic");
						PicAddress item = new PicAddress();
						Elements elementsByTag = elements.get(0)
								.getElementsByTag("img");
						item.setId(0);
						item.setUrl(elementsByTag.get(0).attr("src"));
						nextUrl = Constants.FJ_BASE_URL
								+ elements
										.get(0)
										.getElementsByTag("a")
										.get(elements.get(0)
												.getElementsByTag("a").size() - 1)
										.attr("href");
						listData.add(item);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				Message requsetMsg = new Message();
				requsetMsg.what = REPONSE_CODE;
				requsetMsg.obj = listData;
				sendUiMessage(requsetMsg);
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
				List<PicAddress> data = (List<PicAddress>) msg.obj;
				if (data != null) {
					mGalleryAdapter.setDataSource(data);
					updateCurrentPos(0);
					showView(VIEW_TYPE_DATA);
				} else {
					showView(VIEW_TYPE_EMPTY);
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
	protected void onEmptyViewClicked() {
		super.onEmptyViewClicked();
		showView(VIEW_TYPE_LOADING);
		sendEmptyBackgroundMessage(REQUEST_CODE);
	}

	@Override
	protected String tag() {
		return TAG;
	}
}

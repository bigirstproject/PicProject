package com.sunsun.picproject.Frament;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kugou.framework.component.base.BaseFragment;
import com.sunsun.picproject.R;
import com.sunsun.picproject.activity.AboutActivity;
import com.sunsun.picproject.util.DWToast;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengDownloadListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateConfig;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class SettingFragment extends BaseFragment implements OnClickListener {

	private static final String TAG = SettingFragment.class.getName();

	private RelativeLayout mFeedback;
	private RelativeLayout mCheckUpdate;
	private RelativeLayout mAbout;
	private FeedbackAgent agent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		agent = new FeedbackAgent(getActivity());
		agent.sync();
		UpdateConfig.setDebug(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.setting_fragment, container,
				false);
		mFeedback = (RelativeLayout) view.findViewById(R.id.rl_feedback);
		mCheckUpdate = (RelativeLayout) view.findViewById(R.id.rl_check_update);
		mAbout = (RelativeLayout) view.findViewById(R.id.rl_about);
		mFeedback.setOnClickListener(this);
		mCheckUpdate.setOnClickListener(this);
		mAbout.setOnClickListener(this);
		return view;
	}

	@Override
	protected String tag() {
		return TAG;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_feedback:
			agent.startFeedbackActivity();
			break;
		case R.id.rl_check_update:
			UmengUpdateAgent.setDefault();
			UmengUpdateAgent.setUpdateOnlyWifi(false);
			UmengUpdateAgent.setUpdateAutoPopup(true);
			UmengUpdateAgent.setRichNotification(true);
			UmengUpdateAgent.setUpdateUIStyle(UpdateStatus.STYLE_DIALOG);
			UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

				@Override
				public void onUpdateReturned(int updateStatus,
						UpdateResponse updateInfo) {
					switch (updateStatus) {
					case UpdateStatus.Yes:
						// DWToast.makeText(getActivity(), "发现更新",
						// Toast.LENGTH_SHORT).show();
						break;
					case UpdateStatus.No:
						DWToast.makeText(getActivity(), "没有更新",
								Toast.LENGTH_SHORT).show();
						break;
					case UpdateStatus.NoneWifi:
						DWToast.makeText(getActivity(), "没有wifi连接， 只在wifi下更新",
								Toast.LENGTH_SHORT).show();
						break;
					case UpdateStatus.Timeout:
						DWToast.makeText(getActivity(), "超时",
								Toast.LENGTH_SHORT).show();
						break;
					}
				}
			});
			UmengUpdateAgent.setDownloadListener(new UmengDownloadListener() {

				@Override
				public void OnDownloadUpdate(int progress) {
					// DWToast.makeText(getActivity(), "下载进度 : " + progress +
					// "%",
					// Toast.LENGTH_SHORT).show();
				}

				@Override
				public void OnDownloadStart() {
					DWToast.makeText(getActivity(), "下载开始", Toast.LENGTH_SHORT)
							.show();
				}

				@Override
				public void OnDownloadEnd(int result, String file) {
					switch (result) {
					case UpdateStatus.DOWNLOAD_COMPLETE_FAIL:
						DWToast.makeText(getActivity(), "下载失败",
								Toast.LENGTH_SHORT).show();
						break;
					case UpdateStatus.DOWNLOAD_COMPLETE_SUCCESS:
						DWToast.makeText(getActivity(),
								"下载成功\n下载文件位置 : " + file, Toast.LENGTH_SHORT)
								.show();
						break;
					case UpdateStatus.DOWNLOAD_NEED_RESTART:
						// 增量更新请求全包更新(请勿处理这种情况)
						break;
					}
				}
			});

			// UmengUpdateAgent.setDialogListener(new
			// UmengDialogButtonListener() {
			//
			// @Override
			// public void onClick(int status) {
			// switch (status) {
			// case UpdateStatus.Update:
			// Toast.makeText(getActivity(), "用户选择更新",
			// Toast.LENGTH_SHORT).show();
			// break;
			// case UpdateStatus.Ignore:
			// Toast.makeText(getActivity(), "用户选择忽略",
			// Toast.LENGTH_SHORT).show();
			// break;
			// case UpdateStatus.NotNow:
			// Toast.makeText(getActivity(), "用户选择取消",
			// Toast.LENGTH_SHORT).show();
			// break;
			// }
			// }
			// });

			UmengUpdateAgent.forceUpdate(getActivity());
			break;
		case R.id.rl_about:
			AboutActivity.startSpecialActivity(getActivity());
			break;
		default:
			break;
		}
	}
}

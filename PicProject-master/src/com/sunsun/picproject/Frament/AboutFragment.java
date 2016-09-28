package com.sunsun.picproject.Frament;

import java.util.Calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kugou.framework.component.base.BaseFragment;
import com.kugou.framework.component.base.PicProjectApplication;
import com.sunsun.picproject.R;

public class AboutFragment extends BaseFragment {

	private static final String TAG = AboutFragment.class.getName();
	private TextView versionTextView;
	private TextView copyrightTextView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.about_activity, container, false);
		versionTextView = (TextView) view.findViewById(R.id.tv_app_version);
		versionTextView.setText(String.format(
				getResources().getString(R.string.format_version),
				PicProjectApplication.getInstance().getAppVersion()));

		copyrightTextView = (TextView) view.findViewById(R.id.tv_copyright);
		copyrightTextView.setText(String.format(
				getResources().getString(R.string.copyright), Calendar
						.getInstance().get(Calendar.YEAR)));
		return view;
	}

	@Override
	protected String tag() {
		return TAG;
	}

}

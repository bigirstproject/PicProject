package com.sunsun.picproject.view;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunsun.picproject.R;

/**
 * 自定义的公用titlebar
 * 
 */
public class TitleBarView extends LinearLayout implements OnClickListener {

	public static final int MODE_BOTH = 0;
	public static final int MODE_LEFT = MODE_BOTH + 1;
	public static final int MODE_RIGHT = MODE_BOTH + 2;
	public static final int MODE_NONE = MODE_BOTH + 3;

	private Context context;
	private float density;

	private OnTitleBarItemClickListener onTitleBarItemClickListener;

	private TextView leftTextView;
	private TextView rightTextView;
	private TextView titleTextView;

	public TitleBarView(Context context) {
		this(context, null);
	}

	public TitleBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		density = context.getResources().getDisplayMetrics().density;
		this.setOrientation(LinearLayout.VERTICAL);
		initView();
	}

	private void initView() {
		RelativeLayout view = new RelativeLayout(context);
		view.setGravity(Gravity.CENTER_VERTICAL);
		view.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.MATCH_PARENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT));

		RelativeLayout relativeLayout = new RelativeLayout(context);
		relativeLayout.setGravity(Gravity.CENTER_VERTICAL);
		relativeLayout.setBackgroundColor(getResources().getColor(
				R.color.title_bar_bg));
		relativeLayout
				.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(
						android.widget.RelativeLayout.LayoutParams.MATCH_PARENT,
						(int) (48 * density)));

		android.widget.RelativeLayout.LayoutParams leftPparams = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.MATCH_PARENT);
		leftPparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
				RelativeLayout.TRUE);
		leftPparams
				.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

		android.widget.RelativeLayout.LayoutParams rightParams = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.MATCH_PARENT);
		rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
				RelativeLayout.TRUE);
		rightParams
				.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

		android.widget.RelativeLayout.LayoutParams titleParams = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.MATCH_PARENT);
		titleParams.addRule(RelativeLayout.CENTER_IN_PARENT,
				RelativeLayout.TRUE);
		titleParams
				.setMargins((int) (30 * density), 0, (int) (30 * density), 0);
		// params3.addRule(RelativeLayout.LEFT_OF, R.id.title_bar_right);
		// params3.addRule(RelativeLayout.RIGHT_OF, R.id.title_bar_left);

		leftTextView = new TextView(context);
		leftTextView.setTextColor(getResources().getColor(
				R.color.title_text_color));
		leftTextView.setTextSize(15);
		leftTextView.setGravity(Gravity.CENTER_VERTICAL);
		leftTextView.setPadding((int) (20 * density), 0, (int) (15 * density),
				0);
		leftTextView.setIncludeFontPadding(false);
		leftTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		leftTextView.setCompoundDrawablePadding((int) (6 * density));
		leftTextView.setBackgroundResource(R.drawable.titlebar_button_selector);
		leftTextView.setClickable(true);
		leftTextView.setOnClickListener(this);
		leftTextView.setTag(MODE_LEFT);
		leftTextView.setId(R.id.title_bar_left);
		relativeLayout.addView(leftTextView, leftPparams);

		rightTextView = new TextView(context);
		rightTextView.setTextColor(getResources().getColor(
				R.color.title_text_color));
		rightTextView.setTextSize(15);
		rightTextView.setGravity(Gravity.CENTER_VERTICAL);
		rightTextView.setPadding((int) (15 * density), 0, (int) (17 * density),
				0);
		rightTextView.setIncludeFontPadding(false);
		rightTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		rightTextView.setCompoundDrawablePadding((int) (6 * density));
		rightTextView
				.setBackgroundResource(R.drawable.titlebar_button_selector);
		rightTextView.setClickable(true);
		rightTextView.setOnClickListener(this);
		rightTextView.setTag(MODE_RIGHT);
		rightTextView.setId(R.id.title_bar_right);
		relativeLayout.addView(rightTextView, rightParams);

		titleTextView = new TextView(context);
		titleTextView.setTextColor(getResources().getColor(
				R.color.title_text_color));
		titleTextView.setTextSize(17);
		titleTextView.setSingleLine(true);
		titleTextView.setEllipsize(TruncateAt.END);
		titleTextView.setGravity(Gravity.CENTER);
		titleTextView.setIncludeFontPadding(false);
		titleTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		titleTextView.setCompoundDrawablePadding((int) (5 * density));
		titleTextView.setTag(MODE_BOTH);
		relativeLayout.addView(titleTextView, titleParams);

		view.addView(
				relativeLayout,
				new android.widget.RelativeLayout.LayoutParams(
						android.widget.RelativeLayout.LayoutParams.MATCH_PARENT,
						(int) (48 * density)));

		addView(view, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
	}

	public void addGapLine() {
		View line = new View(context);
		line.setBackgroundColor(getResources().getColor(
				R.color.title_line_color));
		line.setLayoutParams(new android.view.ViewGroup.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT, 1));
		addView(line, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
	}

	public void setLeftButtonText(String text) {
		leftTextView.setText(text);
	}

	public void setRightButtonText(String text) {
		rightTextView.setText(text);
	}

	public void setRightButtonSelected(boolean selected) {
		rightTextView.setSelected(selected);
	}

	public boolean isRightButtonSelected() {
		return rightTextView.isSelected();
	}

	public void setTitle(String text) {
		titleTextView.setText(text);
	}

	public void setLeftButtonTextColor(int color) {
		leftTextView.setTextColor(color);
	}

	public void setRightButtonTextColor(int color) {
		rightTextView.setTextColor(color);
	}

	public void setRightButtonTextSize(int size) {
		rightTextView.setTextSize(size);
	}

	public void setTitleTextColor(int color) {
		titleTextView.setTextColor(color);
	}

	public void setLeftButtonDrawableLeft(boolean drawLeft) {
		if (drawLeft) {
			leftTextView.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.back_icon, 0, 0, 0);
		} else {
			leftTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		}
	}

	public void setRightButtonDrawableLeft(boolean drawRight) {
		if (drawRight) {
			rightTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.back_icon, 0);
		} else {
			rightTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		}
	}

	public void setLeftButtonDrawable(int drawable) {
		leftTextView.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0);
	}

	public void setLeftButtonDrawablePadding(int padding) {
		leftTextView.setCompoundDrawablePadding((int) (padding * density));
	}

	public void setRightButtonDrawable(int drawable) {
		rightTextView
				.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0);
	}

	public void setRightButtonDrawablePadding(int padding) {
		rightTextView.setCompoundDrawablePadding((int) (padding * density));
	}

	public void setTitleDrawable(int drawable) {
		titleTextView
				.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0);
	}

	public void setTitleDrawablePadding(int padding) {
		titleTextView.setCompoundDrawablePadding((int) (padding * density));
	}

	public void setRightButtonBackground(int color) {
		rightTextView.setBackgroundColor(color);
	}

	public void setRightButtonPadding() {
		rightTextView.setPadding((int) (8 * density), (int) (5 * density),
				(int) (8 * density), (int) (6 * density));
	}

	public void setMode(int mode) {
		if (mode == MODE_BOTH) {
			leftTextView.setVisibility(View.VISIBLE);
			rightTextView.setVisibility(View.VISIBLE);
		} else if (mode == MODE_LEFT) {
			leftTextView.setVisibility(View.VISIBLE);
			rightTextView.setVisibility(View.INVISIBLE);
		} else if (mode == MODE_RIGHT) {
			leftTextView.setVisibility(View.INVISIBLE);
			rightTextView.setVisibility(View.VISIBLE);
		} else if (mode == MODE_NONE) {
			leftTextView.setVisibility(View.INVISIBLE);
			rightTextView.setVisibility(View.INVISIBLE);
		}
	}

	public void setOnItemClickListener(
			OnTitleBarItemClickListener onTitleBarItemClickListener) {
		this.onTitleBarItemClickListener = onTitleBarItemClickListener;
	}

	@Override
	public void onClick(View v) {
		if (onTitleBarItemClickListener != null) {
			onTitleBarItemClickListener.onTitleBarItemClick((Integer) (v
					.getTag()));
		}
	}

	public interface OnTitleBarItemClickListener {
		public void onTitleBarItemClick(int index);
	}
}
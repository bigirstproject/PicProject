package com.sunsun.picproject.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunsun.picproject.R;
import com.sunsun.picproject.config.AppConfig;

/**
 * 自定义的viewpager的指示卡
 * 
 */
public class TabStripView extends FrameLayout implements OnClickListener {

	private float density;

	private Context context;
	private int selectedIndex;
	private OnTabStripSwitchListener onSwitchListener;
	private LinearLayout linearLayout;
	private ImageView imageView;
	private List<TextView> textViews = new ArrayList<TextView>();
	private String[] tabs = { getResources().getString(R.string.recommend),  // tab
			getResources().getString(R.string.app_name) };
	private int tabStripViewColor = getResources().getColor(R.color.white);
	private int[] tabTextColors = {
			getResources().getColor(R.color.tab_text_selected_color),
			getResources().getColor(R.color.tab_text_normal_color) }; // 文字颜色数组:  [选中颜色，非选中颜色]
																		
	private int stripColor = R.color.strip_color;
	private int stripHeight = 3;

	public TabStripView(Context context) {
		this(context, null);
	}

	public TabStripView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}

	public void initView() {
		initTabView();
		initGapLine();
		initStripView();
	}

	private void initTabView() {

		density = context.getResources().getDisplayMetrics().density;
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (40 * density), Gravity.TOP);
		linearLayout = new LinearLayout(context);
		linearLayout.setGravity(Gravity.CENTER_VERTICAL);
		linearLayout
				.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(
						android.widget.RelativeLayout.LayoutParams.MATCH_PARENT,
						android.widget.RelativeLayout.LayoutParams.MATCH_PARENT));
		linearLayout.setBackgroundColor(tabStripViewColor);
		addTabView();
		this.addView(linearLayout, params);
	}

	private void addTabView() {
		linearLayout.removeAllViews();
		textViews.clear();
		ColorStateList colorStateList = createColorStateList(tabTextColors);
		for (int i = 0; i < tabs.length; i++) {

			android.widget.LinearLayout.LayoutParams params1 = new android.widget.LinearLayout.LayoutParams(
					android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
					android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);

			TextView tabTextView = new TextView(context);
			tabTextView.setText(tabs[i]);
			tabTextView.setTextColor(colorStateList);
			tabTextView.setTextSize(15);
			tabTextView.setGravity(Gravity.CENTER);
			tabTextView.setIncludeFontPadding(false);
			tabTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			tabTextView.setCompoundDrawablePadding((int) (5 * density));
			tabTextView.setClickable(true);
			tabTextView.setOnClickListener(this);
			tabTextView.setTag(i);
			textViews.add(tabTextView);
			linearLayout.addView(tabTextView, params1);
		}
	}

	/**
	 * 生成TabStripView tab 文本的颜色
	 * 
	 * @param tabTextColors
	 * @return ColorStateList
	 */
	private ColorStateList createColorStateList(int[] tabTextColors) {
		int[] colors = new int[] { tabTextColors[0], tabTextColors[1] };
		int[][] states = new int[2][];
		states[0] = new int[] { android.R.attr.state_selected };
		states[1] = new int[] {};
		ColorStateList colorList = new ColorStateList(states, colors);
		return colorList;
	}

	private void initStripView() {
		LayoutParams params = new LayoutParams(AppConfig.widthPx / tabs.length,
				(int) (stripHeight * density), Gravity.BOTTOM);
		imageView = new ImageView(context);
		imageView.setImageResource(stripColor);

		float offset = AppConfig.widthPx / tabs.length;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		imageView.setImageMatrix(matrix);// 设置动画初始位置
		this.addView(imageView, params);
	}

	private void initGapLine() {
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 1,
				Gravity.BOTTOM);

		View line = new View(context);
		line.setBackgroundColor(getResources().getColor(R.color.gap_line_bg));
		addView(line, params);
	}

	private void animateStrip(int selectedIndex) {
		int stripWidth = AppConfig.widthPx / tabs.length;
		Animation animation = new TranslateAnimation(stripWidth
				* TabStripView.this.selectedIndex, stripWidth * selectedIndex,
				0, 0);
		animation.setFillAfter(true);// True:图片停在动画结束位置
		animation.setDuration(250);
		imageView.startAnimation(animation);
	}

	/**
	 * 设置选中item
	 * 
	 * @param selectedIndex
	 */
	public void setSelectedIndex(int selectedIndex) {
		if (selectedIndex >= tabs.length) {
			return;
		}
		if (selectedIndex != TabStripView.this.selectedIndex) {
			View preItem = linearLayout.getChildAt(this.selectedIndex);
			preItem.setSelected(false);
			animateStrip(selectedIndex);
			this.selectedIndex = selectedIndex;
		}

		View selectedItem = linearLayout.getChildAt(this.selectedIndex);
		selectedItem.setSelected(true);
	}

	@Override
	public void onClick(View view) {
		int selectedIndex = (Integer) view.getTag();
		if (selectedIndex != TabStripView.this.selectedIndex) {
			setSelectedIndex(selectedIndex);
			if (onSwitchListener != null) {
				onSwitchListener.doSwitch(selectedIndex);
			}
		}
	}

	public interface OnTabStripSwitchListener {
		public void doSwitch(int selectedIndex);
	}

	public OnTabStripSwitchListener getOnSwitchListener() {
		return onSwitchListener;
	}

	public void setOnSwitchListener(OnTabStripSwitchListener onSwitchListener) {
		this.onSwitchListener = onSwitchListener;
	}

	public String[] getTabs() {
		return tabs;
	}

	public void setTabs(String[] tabs) {
		this.tabs = tabs;
		addTabView();
	}
	
	public void changeTabs(String[] tabs) {
		this.tabs = tabs;
		for (int i = 0; i< tabs.length; i++) {
			textViews.get(i).setText(tabs[i]);
		}
	}
	
	public void setTabText(int index, String text){
		textViews.get(index).setText(text);
	}

	public int[] getTabTextColors() {
		return tabTextColors;
	}

	public void setTabTextColors(int[] tabTextColors) {
		this.tabTextColors = tabTextColors;
		addTabView();
	}

	public int getTabStripViewColor() {
		return tabStripViewColor;
	}

	public void setTabStripViewColor(int tabStripViewColor) {
		this.tabStripViewColor = tabStripViewColor;
		linearLayout.setBackgroundColor(tabStripViewColor);
	}

	public int getStripColor() {
		return stripColor;
	}

	public void setStripColor(int stripColor) {
		this.stripColor = stripColor;
		imageView.setImageResource(stripColor);
	}

	public int getStripHeight() {
		return stripHeight;
	}

	public void setStripHeight(int stripHeight) {
		this.stripHeight = stripHeight;
		LayoutParams params = new LayoutParams(AppConfig.widthPx / tabs.length,
				(int) (stripHeight * density), Gravity.BOTTOM);
		imageView.setLayoutParams(params);
	}
}

package com.example.mobilesafe.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mobilesafe.R;

/**
 * 设置中心的自定义组合控件
 * 
 * @author Kevin
 * 
 */
public class SettingItemView extends RelativeLayout {

	private TextView tvTitle;
	private TextView tvDesc;
	private CheckBox cbStatus;
	private String mTitle;
	private String mDescOn;
	private String mDescOff;

	private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.example.mobilesafe";

	public SettingItemView(Context context) {
		super(context);
		initView();
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mTitle = attrs.getAttributeValue(NAMESPACE, "mytitle");// 根据属性名称,获取属性的值
		mDescOn = attrs.getAttributeValue(NAMESPACE, "desc_on");
		mDescOff = attrs.getAttributeValue(NAMESPACE, "desc_off");

		initView();

	}

	public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	/**
	 * 初始化布局
	 */
	public void initView() {
		// 将自定义好的布局文件设置给当前的SettingItemView
		View.inflate(getContext(), R.layout.view_setting_item, this);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvDesc = (TextView) findViewById(R.id.tv_desc);
		cbStatus = (CheckBox) findViewById(R.id.cb_status);
		setTitle(mTitle); // 设置标题
	}

	public void setTitle(String title) {
		tvTitle.setText(title);
	}

	public void setDesc(String desc) {
		tvDesc.setText(desc);
	}

	// 设颜色
	public void setColor(int color) {
		tvDesc.setTextColor(color);
	}

	/**
	 * 返回勾选状态
	 * 
	 * @return
	 */
	public boolean isChecked() {
		return cbStatus.isChecked();
	}

	public void setChecked(boolean check) {
		cbStatus.setChecked(check);
		if (check) {
			setDesc(mDescOn);
			setColor(Color.rgb(111, 22, 255));

		} else {
			setDesc(mDescOff);
			setColor(Color.rgb(22, 255, 111));
		}
	}
}

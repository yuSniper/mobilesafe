package com.example.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.mobilesafe.R;
import com.example.mobilesafe.utils.ToastUtils;

/**
 * 第3个设置向导页
 * 
 */
public class Setup3Activity extends BaseSetupActivity {

	public EditText etPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		etPhone = (EditText) findViewById(R.id.et_phone);

		// 获取电话号码默认显示在textview中
		String phone = mPref.getString("safe_phone", "");
		etPhone.setText(phone);
	}

	// 选择联系人
	public void selectContact(View view) {
		Intent intent = new Intent(this, ContactActivity.class);
		startActivityForResult(intent, 1);
		// startActivity(new Intent(this, Setup4Activity.class));
//		finish();
	}

	// 跳到下一页，点击与滑动事件方法在父类中
	@Override
	public void showNextPage() {
		// 注意过滤空格,跳到下一个页面的时候获取edittext中的数据
		String phone = etPhone.getText().toString().trim();

		if (TextUtils.isEmpty(phone)) {
			ToastUtils.showToast(this, "安全号码不能为空!");
			return;
		}
		// 保存安全号码
		mPref.edit().putString("safe_phone", phone).commit();

		startActivity(new Intent(this, Setup4Activity.class));
		finish();
		// 两个界面切换的动画
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	// 跳到上一页，点击与滑动事件方法在父类中
	@Override
	public void showPreviousPage() {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, Setup2Activity.class));
		finish();
		// 两个界面切换的动画
		overridePendingTransition(R.anim.tran_previous_in,
				R.anim.tran_previous_out);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// System.out.println("resultCode:" + resultCode);
		// System.out.println("requestCode:" + requestCode);

		if (resultCode == Activity.RESULT_OK) {
			String phone = data.getStringExtra("phone");
			phone = phone.replaceAll("-", "").replaceAll(" ", "");// 替换-和空格

			etPhone.setText(phone);// 把电话号码设置给输入框
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

}

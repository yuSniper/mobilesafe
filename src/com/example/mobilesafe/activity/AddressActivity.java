package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.example.mobilesafe.db.dao.AddressDao;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 归属地查询页面
 * 
 * @author yu_longji
 * 
 */
public class AddressActivity extends Activity {

	private EditText etNumber;
	private TextView tvResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		etNumber = (EditText) findViewById(R.id.et_number);
		tvResult = (TextView) findViewById(R.id.tv_result);
		// 监听EditText的变化
		etNumber.addTextChangedListener(new TextWatcher() {
			// 文字 发生变化时的回调
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String address = AddressDao.getAddress(s.toString());
				tvResult.setText(address);
			}

			// 文字变化前的回调
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			// 文字变化结束之后的回调
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 开始查询
	 */
	public void query(View view) {
		String number = etNumber.getText().toString().trim();
		if (!TextUtils.isEmpty(number)) {
			String address = AddressDao.getAddress(number);
			tvResult.setText(address);
		} else {
			// 设置动画
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			etNumber.startAnimation(shake);
			vibrate();
		}
	}

	/**
	 * 手机震动, 需要权限 android.permission.VIBRATE
	 */
	public void vibrate() {
		// 获取系统的震动器
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		vibrator.vibrate(new long[] { 1000, 2000, 1000, 3000 }, -1);
		// 先等待1秒,再震动2秒,再等待1秒,再震动3秒,
		// 参2等于-1表示只执行一次,不循环,
		// 参2等于0表示从头循环,
		// 参2表示从第几个位置开始循环

		// 取消震动vibrator.cancel()
	}
}

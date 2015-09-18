package com.example.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.example.mobilesafe.R;
import com.example.mobilesafe.utils.ToastUtils;

public class Setup4Activity extends BaseSetupActivity {

	private CheckBox cbProtect;
	private boolean protect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);

		cbProtect = (CheckBox) findViewById(R.id.cb_protect);
		protect = mPref.getBoolean("protect", false);
		if (protect) {
			cbProtect.setText("防盗保护已经开启");
			cbProtect.setChecked(true);
		} else {
			cbProtect.setText("防盗保护没有开启");
			cbProtect.setChecked(false);
		}

		// 当checkbox发生变化时,回调此方法
		cbProtect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					cbProtect.setText("防盗保护已经开启");
					mPref.edit().putBoolean("protect", true).commit();
				} else {
					cbProtect.setText("防盗保护没有开启");
					mPref.edit().putBoolean("protect", false).commit();
				}
			}
		});
	}

	@Override
	public void showNextPage() {
		protect = mPref.getBoolean("protect", false);
//		if (protect) {
			startActivity(new Intent(this, LostFindActivity.class));
			finish();
			// 两个界面切换的动画
			overridePendingTransition(R.anim.tran_in, R.anim.tran_out);

			// 更新sp,表示已经展示过设置向导了,下次进来就不展示啦
			mPref.edit().putBoolean("configed", true).commit();
//		} else {
//			ToastUtils.showToast(this, "防盗保护必须开启");
//		}

	}

	@Override
	public void showPreviousPage() {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, Setup3Activity.class));
		finish();
		// 两个界面切换的动画
		overridePendingTransition(R.anim.tran_previous_in,
				R.anim.tran_previous_out);
	}
}

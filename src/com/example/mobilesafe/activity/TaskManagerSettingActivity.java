package com.example.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.example.mobilesafe.R;
import com.example.mobilesafe.service.KillProcessService;
import com.example.mobilesafe.utils.SharedPreferencesUtils;
import com.example.mobilesafe.utils.SystemInfoUtils;

public class TaskManagerSettingActivity extends Activity {

	private CheckBox cb_status_kill_process;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initUI();

	}

	public void initUI() {
		setContentView(R.layout.activity_task_manager_setting);
		CheckBox cb_status = (CheckBox) findViewById(R.id.cb_status);
		// 设置是否选中
		cb_status.setChecked(SharedPreferencesUtils.getBoolean(
				TaskManagerSettingActivity.this, "is_show_system", false));

		cb_status.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				SharedPreferencesUtils.saveBoolean(
						TaskManagerSettingActivity.this, "is_show_system",
						isChecked);
			}
		});

		// 定时清理进程
		cb_status_kill_process = (CheckBox) findViewById(R.id.cb_status_kill_process);

		final Intent intent = new Intent(TaskManagerSettingActivity.this,
				KillProcessService.class);

		cb_status_kill_process
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							startService(intent);
						} else {
							stopService(intent);
						}
					}
				});

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (SystemInfoUtils.isServiceRunning(TaskManagerSettingActivity.this,
				"com.example.mobilesafe.service.KillProcessService")) {
			cb_status_kill_process.setChecked(true);
		} else {
			cb_status_kill_process.setChecked(false);
		}
	}

}

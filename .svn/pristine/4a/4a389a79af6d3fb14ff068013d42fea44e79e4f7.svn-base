package com.example.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.mobilesafe.R;
import com.example.mobilesafe.service.AddressService;
import com.example.mobilesafe.service.CallSmsSafeService;
import com.example.mobilesafe.service.WatchDogService;
import com.example.mobilesafe.utils.ServiceStatusUtils;
import com.example.mobilesafe.view.SettingClickView;
import com.example.mobilesafe.view.SettingItemView;

public class SettingActivity extends Activity {

	private SettingItemView sivUpdate;// 设置升级
	private SettingItemView sivAddress;// 设置归属地
	private SettingClickView scvAddressStyle;// 修改风格
	private SettingClickView scvAddressLocation; // 修改归属地位置
	private SharedPreferences mPref;

	private SettingItemView sv_watch_dog;// 看门狗
	private Intent watchDogIntent;
	// 黑名单
	private SettingItemView sv_blacknumber;
	private Intent callSmsSafeIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		initUpdateView();
		initBlackNumber();
		initWatchDogView();
		initAddressView();
		initAddressStyle();
		initAddressLocation();
	}

	/**
	 * 初始化自动更新开关
	 */
	public void initUpdateView() {
		sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
		// sivUpdate.setTitle("自动更新设置");
		boolean autoUpdate = mPref.getBoolean("auto_update", true);
		if (autoUpdate) {
			// sivUpdate.setDesc("自动更新已开启");
			sivUpdate.setChecked(true);
		} else {
			// sivUpdate.setDesc("自动更新已关闭");
			sivUpdate.setChecked(false);
		}

		sivUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断当前的勾选状态
				if (sivUpdate.isChecked()) {
					// 设置不勾选
					sivUpdate.setChecked(false);
					// sivUpdate.setDesc("自动更新已关闭");
					// 更新sp
					mPref.edit().putBoolean("auto_update", false).commit();
				} else {
					sivUpdate.setChecked(true);
					// sivUpdate.setDesc("自动更新已开启");
					// 更新sp
					mPref.edit().putBoolean("auto_update", true).commit();
				}
			}
		});
	}

	/**
	 * 黑名单设置
	 * 
	 */
	public void initBlackNumber() {

		sv_blacknumber = (SettingItemView) findViewById(R.id.sv_blacknumber);

		// 根据看门狗服务是否运行来更新checkbox
		boolean serviceRunning = ServiceStatusUtils.isServiceRunning(this,
				"com.example.mobilesafe.service.CallSmsSafeService");

		if (serviceRunning) {
			sv_blacknumber.setChecked(true);
		} else {
			sv_blacknumber.setChecked(false);
		}

		 callSmsSafeIntent = new Intent(SettingActivity.this,
				 CallSmsSafeService.class);
		sv_blacknumber.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				startService(new Intent(SettingActivity.this, CallSmsSafeService.class));
				
				if (sv_blacknumber.isChecked()) {
					System.out.println("black:" + sv_blacknumber.isChecked());
					sv_blacknumber.setChecked(false);
					// 停止拦截服务
//					stopService(new Intent(SettingActivity.this,
//							CallSmsSafeActivity.class));
					stopService(callSmsSafeIntent);
				} else {
					System.out.println("black:" + sv_blacknumber.isChecked());
					sv_blacknumber.setChecked(true);
					// 开启拦截服务
//					startService(new Intent(SettingActivity.this,
//							CallSmsSafeActivity.class));
					startService(callSmsSafeIntent);
				}
			}
		});

	}

	/**
	 * 看门狗设置
	 */
	public void initWatchDogView() {

		sv_watch_dog = (SettingItemView) findViewById(R.id.sv_watch_dog);

		// 根据看门狗服务是否运行来更新checkbox
		boolean serviceRunning = ServiceStatusUtils.isServiceRunning(this,
				"com.example.mobilesafe.service.WatchDogService");

		if (serviceRunning) {
			sv_watch_dog.setChecked(true);
		} else {
			sv_watch_dog.setChecked(false);
		}

		watchDogIntent = new Intent(this, WatchDogService.class);
		sv_watch_dog.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (sv_watch_dog.isChecked()) {

					sv_watch_dog.setChecked(false);
					// 停止拦截服务
					stopService(watchDogIntent);
				} else {

					sv_watch_dog.setChecked(true);
					// 开启拦截服务
					startService(watchDogIntent);
				}
			}
		});
	}

	/**
	 * 初始化归属地开关
	 */
	public void initAddressView() {
		sivAddress = (SettingItemView) findViewById(R.id.siv_address);
		// 根据归属地服务是否运行来更新checkbox
		boolean serviceRunning = ServiceStatusUtils.isServiceRunning(this,
				"com.example.mobilesafe.service.AddressService");

		if (serviceRunning) {
			sivAddress.setChecked(true);
		} else {
			sivAddress.setChecked(false);
		}

		sivAddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sivAddress.isChecked()) {
					sivAddress.setChecked(false);
					stopService(new Intent(SettingActivity.this,
							AddressService.class));// 停止归属地服务
				} else {
					sivAddress.setChecked(true);
					startService(new Intent(SettingActivity.this,
							AddressService.class));// 开启归属地服务
				}

			}
		});

	}

	final String[] items = new String[] { "半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿" };
	final int[] colors = new int[] { Color.rgb(255, 255, 255),
			Color.rgb(250, 130, 60), Color.rgb(0, 0, 255),
			Color.rgb(155, 155, 155), Color.rgb(0, 255, 0) };

	/**
	 * 修改提示框显示风格
	 */
	public void initAddressStyle() {
		scvAddressStyle = (SettingClickView) findViewById(R.id.scv_address_style);
		scvAddressStyle.setTitle("归属地提示框风格");
		// 读取保存的style
		int style = mPref.getInt("address_style", 0);
		scvAddressStyle.setDesc(items[style]);
		scvAddressStyle.setColor(colors[style]);
		scvAddressStyle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showSingleChooseDailog();
			}
		});

	}

	/**
	 * 弹出选择风格的单选框
	 */

	public void showSingleChooseDailog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("归属地提示框风格");
		// 读取保存的style
		int style = mPref.getInt("address_style", 0);

		builder.setSingleChoiceItems(items, style,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 保存选择的风格
						mPref.edit().putInt("address_style", which).commit();
						// 让dialog消失
						dialog.dismiss();
						// 更新组合控件的描述信息
						scvAddressStyle.setDesc(items[which]);
						scvAddressStyle.setColor(colors[which]);
					}
				});
		builder.setNegativeButton("取消", null);
		builder.show();
	}

	/**
	 * 修改归属地显示位置
	 */
	public void initAddressLocation() {
		scvAddressLocation = (SettingClickView) findViewById(R.id.scv_address_location);
		scvAddressLocation.setTitle("归属地提示框显示位置");
		scvAddressLocation.setDesc("设置归属地提示框的显示位置");
		scvAddressLocation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SettingActivity.this,
						DragViewActivity.class));
			}
		});
	}

}

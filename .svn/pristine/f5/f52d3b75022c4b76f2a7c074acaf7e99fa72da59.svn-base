package com.example.mobilesafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.mobilesafe.R;
import com.example.mobilesafe.service.LocationService;

/**
 * 拦截短信
 * 
 * @author yu_longji
 * 
 */
public class SmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		// 获取超级管理员
		DevicePolicyManager dpm = (DevicePolicyManager) context
				.getSystemService(Context.DEVICE_POLICY_SERVICE);

		Log.v("sms", "sms");
		// TODO Auto-generated method stub
		Object[] objects = (Object[]) intent.getExtras().get("pdus");
		// 短信最多140字节,
		// 超出的话,会分为多条短信发送,所以是一个数组,因为我们的短信指令很短,所以for循环只执行一次
		for (Object object : objects) {
			SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
			// 短信来源号码
			String originatingAddress = message.getOriginatingAddress();
			System.out.println("Address:" + originatingAddress);
			// 短信内容
			String messageBody = message.getMessageBody();
			if ("#*alarm*#".equals(messageBody)) {
				// 播放报警音乐, 即使手机调为静音,也能播放音乐, 因为使用的是媒体声音的通道,和铃声无关
				MediaPlayer player = MediaPlayer.create(context, R.raw.seeyou);
				// 左右声道 最大
				player.setVolume(1f, 1f);
				// 循环播放
				player.setLooping(true);
				player.start();

				// 中断短信的传递, 从而系统短信app就收不到内容了
				abortBroadcast();
				System.out.println("短信拦截");
			} else if ("#*location*#".equals(messageBody)) {
				// 获取经纬度坐标
				// 开启定位服务
				context.startService(new Intent(context, LocationService.class));
				SharedPreferences sp = context.getSharedPreferences("config",
						context.MODE_PRIVATE);

				String location = sp.getString("location",
						"getting location...");
				System.out.println("location:" + location);
				Log.v("tag", location + "");
				// 中断短信的传递, 从而系统短信app就收不到内容了
				abortBroadcast();
			} else if ("#*wipedata*#".equals(messageBody)) {
				System.out.println("远程清除数据.");
				dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
				abortBroadcast();
			} else if ("#*lockscreen*#".equals(messageBody)) {
				System.out.println("远程锁屏");
				dpm.resetPassword("123", 0);
				dpm.lockNow();
				abortBroadcast();
			}
		}
	}

}

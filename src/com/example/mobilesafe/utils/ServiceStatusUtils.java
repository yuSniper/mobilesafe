package com.example.mobilesafe.utils;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

/**
 * 服务状态工具类
 * 
 * @author yu_longji
 * 
 */
public class ServiceStatusUtils {
	/**
	 * 检测服务是否正在运行
	 */
	public static boolean isServiceRunning(Context context, String serviceName) {
		// 获取Activity管理器 得到系统中的activity服务
		ActivityManager am = (ActivityManager) context
				.getSystemService(Activity.ACTIVITY_SERVICE);
		// 获取系统所有正在运行的服务,最多返回100个
		List<RunningServiceInfo> runningServices = am.getRunningServices(100);

		for (RunningServiceInfo runningServiceInfo : runningServices) {
			// 获取服务的名称
			String className = runningServiceInfo.service.getClassName();
//			 System.out.println(className);
			// 判断指定服务存在是否存在
			if (className.equals(serviceName)) {
				return true;
			}
		}

		return false;
	}
}

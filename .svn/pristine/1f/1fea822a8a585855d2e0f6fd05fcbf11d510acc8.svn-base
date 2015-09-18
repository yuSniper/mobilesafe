package com.example.mobilesafe.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.example.mobilesafe.domain.AppInfo;

/**
 * 
 * 获取手机里面的所有的应用程序
 * 
 * @param context
 *            上下文
 * @author yu_longji
 * 
 */
public class AppInfoParser {
	public static List<AppInfo> getAppInfos(Context context) {
		// 得到一个java保证的 包管理器。
		PackageManager pm = context.getPackageManager();
		// 获取安装包的信息
		List<PackageInfo> packInfos = pm.getInstalledPackages(0);
		List<AppInfo> appinfos = new ArrayList<AppInfo>();
		for (PackageInfo packInfo : packInfos) {
			AppInfo appinfo = new AppInfo();
			// 获取包名
			String packname = packInfo.packageName;
			appinfo.setPackname(packname);
			// 程序图标
			Drawable icon = packInfo.applicationInfo.loadIcon(pm);
			appinfo.setIcon(icon);
			// 应用的名称
			String appname = packInfo.applicationInfo.loadLabel(pm).toString();
			appinfo.setName(appname);
			// 应用程序apk包的路径
			String apkpath = packInfo.applicationInfo.sourceDir;
			appinfo.setApkpath(apkpath);

			File file = new File(apkpath);
			// 应用的大小
			long appSize = file.length();
			appinfo.setAppSize(appSize);

			// 应用程序安装的位置。
			int flags = packInfo.applicationInfo.flags;// 二进制映射 大bit-map
			// 判断存储位置
			if ((ApplicationInfo.FLAG_EXTERNAL_STORAGE & flags) != 0) {
				// 外部存储
				appinfo.setInRom(false);
			} else {
				// 手机内存
				appinfo.setInRom(true);
			}
			// 判断是否是系统应用还是用户应用
			if ((ApplicationInfo.FLAG_SYSTEM & flags) != 0) {
				// 系统应用
				appinfo.setUserApp(false);
			} else {
				// 用户应用
				appinfo.setUserApp(true);
			}
			appinfos.add(appinfo);
			appinfo = null;
		}

		return appinfos;

	}
}

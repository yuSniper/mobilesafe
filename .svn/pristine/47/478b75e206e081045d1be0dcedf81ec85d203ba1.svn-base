package com.example.mobilesafe.activity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilesafe.R;
import com.example.mobilesafe.utils.ToastUtils;

/**
 * 缓存清理
 * 
 * @author yulongji
 * 
 */
public class CleanCacheActivity extends Activity {

	private PackageManager packageManager;
	private List<CachInfo> cachList;
	private ListView list_view;
	private ProgressBar pg;

	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			CacheAdapter cacheAdapter = new CacheAdapter();
			list_view.setAdapter(cacheAdapter);
			pg.setVisibility(View.INVISIBLE);
			if (msg.what == 1) {
				Toast.makeText(CleanCacheActivity.this, "全部清除", 0).show();
			}
			// cacheAdapter.notifyDataSetChanged();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		initUI();
	}

	public void initUI() {
		setContentView(R.layout.activity_clean_cache);

		list_view = (ListView) findViewById(R.id.list_view);
		pg = (ProgressBar) findViewById(R.id.pg);
		// 垃圾的集合
		cachList = new ArrayList<CachInfo>();
		packageManager = getPackageManager();
		new Thread() {

			public void run() {

				System.out.println("thread");
				// 安装到手机上面所有的应用程序
				List<PackageInfo> installedPackages = packageManager
						.getInstalledPackages(0);

				for (PackageInfo packageInfo : installedPackages) {
					getCacheSize(packageInfo);
				}

				handler.sendEmptyMessageDelayed(0, 1000);
			};
		}.start();

	}

	public class CacheAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return cachList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return cachList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder holder;
			if (convertView == null) {
				view = View.inflate(CleanCacheActivity.this,
						R.layout.item_clean_cache, null);
				holder = new ViewHolder();

				holder.icon = (ImageView) view.findViewById(R.id.iv_icon);
				holder.cacheSize = (TextView) view
						.findViewById(R.id.tv_cache_size);
				holder.appName = (TextView) view.findViewById(R.id.tv_name);

				view.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			holder.icon.setImageDrawable(cachList.get(position).icon);

			holder.cacheSize.setText("缓存大小："
					+ Formatter.formatFileSize(CleanCacheActivity.this,
							cachList.get(position).cachSize));
			holder.appName.setText(cachList.get(position).appName);
			return view;
		}
	}

	static class ViewHolder {
		ImageView icon;
		TextView appName;
		TextView cacheSize;
	}

	public void getCacheSize(PackageInfo packageInfo) {
		try {
			// Class<?> clazz = getClassLoader().loadClass("packageManager");
			// 通过反射获取到当前的方法
			Method method = PackageManager.class.getDeclaredMethod(
					"getPackageSizeInfo", String.class,
					IPackageStatsObserver.class);

			/*
			 * 第一个参数表示当前的这个方法有谁来调用的 第二个参数表示包名
			 */

			method.invoke(packageManager,
					packageInfo.applicationInfo.packageName,
					new MyIPackageStatsObserver(packageInfo));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public class MyIPackageStatsObserver extends IPackageStatsObserver.Stub {
		private PackageInfo packageInfo;

		public MyIPackageStatsObserver(PackageInfo packageInfo) {
			this.packageInfo = packageInfo;

		}

		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			// 获取当前手机应用的缓存
			long cacheSize = pStats.cacheSize;
			CachInfo cachInfo = new CachInfo();

			if (cacheSize > 0) {
				System.out.println("图片："
						+ packageInfo.applicationInfo.loadIcon(packageManager)
						+ "；当前应用的名称"
						+ packageInfo.applicationInfo.loadLabel(packageManager)
						+ "缓存的大小" + cacheSize);

				// /获取到图片
				Drawable icon = packageInfo.applicationInfo
						.loadIcon(packageManager);

				String appName = packageInfo.applicationInfo.loadLabel(
						packageManager).toString();

				cachInfo.icon = icon;
				cachInfo.cachSize = cacheSize;
				cachInfo.appName = appName;

				cachList.add(cachInfo);
				System.out.println("list:" + cachList.size());
			}
		}
	}

	static class CachInfo {
		Drawable icon;
		long cachSize;
		String appName;
	}

	/**
	 * 全部清除
	 * 
	 */
	public void cleanAll(View view) {
		// 获取到当前应用程序里面所有方法
		Method[] methods = PackageManager.class.getMethods();

		for (Method method : methods) {
			// 判断当前的方法名
			if (method.getName().equals("freeStorageAndNotify")) {

				try {
					method.invoke(packageManager, Integer.MAX_VALUE,
							new MyIPackageDataObserver());
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		Message msg = Message.obtain();
		msg.what = 1;
		handler.sendMessage(msg);
		handler.sendEmptyMessage(0);
//		ToastUtils.showToast(CleanCacheActivity.this, "全部清除");
		System.out.println("method");
	}

	public class MyIPackageDataObserver extends IPackageDataObserver.Stub {

		@Override
		public void onRemoveCompleted(String packageName, boolean succeeded)
				throws RemoteException {
			// TODO Auto-generated method stub

		}

	}

}

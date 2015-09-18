package com.example.mobilesafe.activity;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.db.dao.AntivirusDao;
import com.example.mobilesafe.utils.MD5Utils;

public class AntivirusActivity extends Activity {

	private TextView tv_init_virus;
	private ProgressBar pb;
	private ImageView iv_scanning;
	private LinearLayout ll_content;
	private ScrollView scrollView;

	// 扫描开始
	protected static final int BEGING = 1;
	// 扫描中
	protected static final int SCANING = 2;
	// 扫描结束
	protected static final int FINISH = 3;
	private Message message;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BEGING:
				tv_init_virus.setText("初始化八核引擎");
				break;
			case SCANING:
				// 病毒扫描中：
				TextView child = new TextView(AntivirusActivity.this);
				ScanInfo scanInfo = (ScanInfo) msg.obj;
				// 如果为true表示有病毒
				if (scanInfo.desc) {
					child.setTextColor(Color.RED);
					child.setText(scanInfo.appName + "有病毒");
				} else {
					child.setTextColor(Color.BLACK);
					// 为false表示没有病毒
					child.setText(scanInfo.appName + "扫描安全");
				}
				ll_content.addView(child, 0);
				// 自动滚动
				scrollView.post(new Runnable() {

					@Override
					public void run() {
						// 一直往下面进行滚动
						scrollView.fullScroll(ScrollView.FOCUS_DOWN);
					}
				});
				System.out.println(scanInfo.appName + "扫描安全");
				break;
			case FINISH:
				// 当扫描结束的时候。停止动画
				iv_scanning.clearAnimation();
				tv_init_virus.setText("扫描完成！");
				break;
			default:
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		initUI();
		initData();
	}

	public void initUI() {
		setContentView(R.layout.activity_antivirusa);
		iv_scanning = (ImageView) findViewById(R.id.iv_scanning);

		tv_init_virus = (TextView) findViewById(R.id.tv_init_virus);

		pb = (ProgressBar) findViewById(R.id.progressBar1);

		ll_content = (LinearLayout) findViewById(R.id.ll_content);

		scrollView = (ScrollView) findViewById(R.id.scrollView);

		/**
		 * 第一个参数表示开始的角度 第二个参数表示结束的角度 第三个参数表示参照自己 初始化旋转动画
		 */
		RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		// 设置动画的时间
		rotateAnimation.setDuration(5000);
		// 设置动画无限循环
		rotateAnimation.setRepeatCount(Animation.INFINITE);
		// 开始动画
		iv_scanning.startAnimation(rotateAnimation);

	}

	public void initData() {
		new Thread() {

			@Override
			public void run() {
				super.run();
				message = Message.obtain();
				message.what = BEGING;

				PackageManager packageManager = getPackageManager();
				// 获取到所有安装的应用程序
				List<PackageInfo> installedPackages = packageManager
						.getInstalledPackages(0);
				// 返回手机上面安装了多少个应用程序
				int size = installedPackages.size();
				// 设置进度条的最大值
				pb.setMax(size);
				int progress = 0;

				for (PackageInfo packageInfo : installedPackages) {
					ScanInfo scanInfo = new ScanInfo();
					// 获取到当前手机上面的app的名字
					String appName = packageInfo.applicationInfo.loadLabel(
							packageManager).toString();
					scanInfo.appName = appName;
					String packageName = packageInfo.applicationInfo.packageName;

					scanInfo.packageName = packageName;

					// 首先需要获取到每个应用程序的目录
					String sourceDir = packageInfo.applicationInfo.sourceDir;
					// 获取到文件的md5
					String md5 = MD5Utils.getFileMd5(sourceDir);

					// 判断当前的文件是否是病毒数据库里面
					File destFile = new File(getFilesDir(), "antivirus");

					String string = getFilesDir() + File.separator
							+ "antivirus.db";

					System.out.println("数据库名：" + string);
					// 判断当前的文件是否是病毒数据库里面
					String desc = AntivirusDao.checkFileVirus(md5, string);

					System.out.println("-------------------------");

					System.out.println(appName);

					System.out.println(md5);

					// 如果当前的描述信息等于null说明没有病毒
					if (desc == null) {
						scanInfo.desc = false;
					} else {
						scanInfo.desc = true;
					}
					progress++;

					SystemClock.sleep(100);
					pb.setProgress(progress);

					message = Message.obtain();
					message.what = SCANING;
					message.obj = scanInfo;
					handler.sendMessage(message);
				}
				message = Message.obtain();
				message.what = FINISH;
				handler.sendMessage(message);

			}

		}.start();
	}

	static class ScanInfo {
		boolean desc;
		String appName;
		String packageName;
	}

}

package com.example.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.db.dao.AddressDao;
import com.example.mobilesafe.utils.ToastUtils;

/**
 * 来电提醒的服务
 * 
 * @author yu_longji
 * 
 */
public class AddressService extends Service {

	private TelephonyManager tm;
	private MyListener listener;
	private OutCallReceiver receiver;
	private WindowManager mWM;
	private SharedPreferences mPref;

	private int winWidth;
	private int winHeight;
	private int startX;
	private int startY;

	private WindowManager.LayoutParams params;
	private View view;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mPref = getSharedPreferences("config", MODE_PRIVATE);

		// 获取系统的电话服务
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		// 监听来电的状态
		listener = new MyListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

		receiver = new OutCallReceiver();
		// 动态注册广播接收者
		IntentFilter filter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(receiver, filter);

	}

	class MyListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:// 电话铃声响了
				System.out.println("电话铃声响了");
				String address = AddressDao.getAddress(incomingNumber);
				// Toast.makeText(AddressService.this, address,
				// Toast.LENGTH_LONG)
				// .show();
				showToast(address);
				break;

			case TelephonyManager.CALL_STATE_IDLE:// 电话闲置状态
				if (mWM != null && view != null) {
					// 从window中移除view
					mWM.removeView(view);
					view = null;
				}
				break;

			default:
				break;
			}
		}

	}

	/**
	 * 监听去电的广播接受者 需要权限: android.permission.PROCESS_OUTGOING_CALLS
	 */
	class OutCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 获取去电电话号码
			String number = getResultData();
			// 归属地查询
			String address = AddressDao.getAddress(number);
			// ToastUtils.showToast(context, address);
			showToast(address);
		}
	}

	/**
	 * 自定义归属地浮窗 需要权限android.permission.SYSTEM_ALERT_WINDOW
	 */
	public void showToast(String text) {
		// 获取系统的窗口服务
		mWM = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		// 获取屏幕宽高
		winWidth = mWM.getDefaultDisplay().getWidth();
		winHeight = mWM.getDefaultDisplay().getHeight();
		// 获取窗口管理器中的布局参数
		params = new WindowManager.LayoutParams();
		// 获取布局宽高
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		// 标识
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		// 格式 , 透明
		params.format = PixelFormat.TRANSLUCENT;
		// 电话窗口。它用于电话交互（特别是呼入）。它置于所有应用程序之上，状态栏之下。
		params.type = WindowManager.LayoutParams.TYPE_PHONE;
		// 将重心位置设置为左上方,
		// 也就是(0,0)从左上方开始,而不是默认的重心位置
		params.gravity = Gravity.LEFT + Gravity.TOP;

		params.setTitle("Toast");

		int lastX = mPref.getInt("lastX", 0);
		int lastY = mPref.getInt("lastY", 0);
		// 设置浮窗的位置, 基于左上方的偏移量
		params.x = lastX;
		params.y = lastY;
		// 填充浮窗布局
		view = View.inflate(this, R.layout.toast_address, null);
		// 窗口的颜色
		int[] bgs = new int[] { R.drawable.call_locate_white,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green };
		// 从SharedPreferences 取出对应窗口颜色的数组编号
		int style = mPref.getInt("address_style", 0);
		// 根据存储的样式更新背景
		view.setBackgroundResource(bgs[style]);

		TextView tvText = (TextView) view.findViewById(R.id.tv_number);
		tvText.setText(text);
		// 将view添加在屏幕上(Window)
		mWM.addView(view, params);

		view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 初始化起点坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();

					// 计算移动偏移量
					int dx = endX - startX;
					int dy = endY - startY;

					// 更新浮窗位置
					params.x += dx;
					params.y += dy;

					// 防止坐标偏离屏幕
					// 左边
					if (params.x < 0) {
						params.x = 0;
					}
					// 上边
					if (params.y < 0) {
						params.y = 0;
					}
					// 右边
					if (params.x > winWidth - view.getWidth()) {
						params.x = winWidth - view.getWidth();
					}
					// 下边
					if (params.y > winHeight - view.getHeight()) {
						params.y = winWidth - view.getHeight();
					}
					// 更新布局
					mWM.updateViewLayout(view, params);
					// 重新初始化起点坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();

					break;
				case MotionEvent.ACTION_UP:
					// 记录坐标点
					Editor edit = mPref.edit();
					edit.putInt("lastX", params.x);
					edit.putInt("lastY", params.y);
					edit.commit();
					break;

				default:
					break;
				}

				return true;
			}
		});

	}

	/**
	 * 服务销毁的时候停止和注销来电监听和广播
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// 停止来电监听
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		// 注销广播
		unregisterReceiver(receiver);
	}
}

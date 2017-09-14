package com.itheima.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.db.dao.AddressDBDao;

public class ShowAddressService extends Service {
	protected static final String TAG = "ShowAddressService";
	private TelephonyManager tm;
	private MyPhoneListener listener;
	private OutCallReceiver receiver;
	private WindowManager wm;
	/**
	 * ��ʾ�ڴ����ϵ�view
	 */
	private View view;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		// ��ȡ�������ķ���
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		receiver = new OutCallReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(receiver, filter);
		// ע��һ���绰״̬�����ķ���
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyPhoneListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		unregisterReceiver(receiver);
		receiver = null;
		super.onDestroy();
	}

	private class MyPhoneListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				String address = AddressDBDao.findLocation(incomingNumber);
				// Toast.makeText(ShowAddressService.this, address, 1).show();
				showMyToast(address);
				break;

			case TelephonyManager.CALL_STATE_IDLE:// ����״̬
				if (view != null) {
					// �Ѵ����ϵ�view���Ƴ�
					wm.removeView(view);
					view = null;
				}
				break;
			}
			super.onCallStateChanged(state, incomingNumber);
		}
	}

	private class OutCallReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String number = getResultData();
			String address = AddressDBDao.findLocation(number);
			// Toast.makeText(ShowAddressService.this, address, 1).show();
			showMyToast(address);
		}
	}
	private int[] bgIcons={R.drawable.call_locate_white,R.drawable.call_locate_orange,
			R.drawable.call_locate_blue,R.drawable.call_locate_gray,
			R.drawable.call_locate_green};
	private WindowManager.LayoutParams params;
	/**
	 * ��ʾ�Զ������˾
	 * 
	 * @param text
	 *            ������
	 */
	public void showMyToast(String text) {
		//�Զ����view
		view = View.inflate(this, R.layout.toast_address, null);
		//��view��������һ�������ļ�����
		view.setOnTouchListener(new OnTouchListener() {
			int startX;
			int startY;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN://����
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE://�ƶ�
					int newX = (int) event.getRawX();
					int newY = (int) event.getRawY();
					int dx = newX - startX;
					int dy = newY - startY;
					params.x+=dx;
					params.y+=dy;
					wm.updateViewLayout(view, params);
					Log.i(TAG,"������Ļ�ϵ�ƫ����dx:"+dx);
					Log.i(TAG,"������Ļ�ϵ�ƫ����dy:"+dy);
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP://�뿪��Ļ
					break;
				}
				return true;
			}
		});
		SharedPreferences sp = getSharedPreferences("config", 0);
		int which = sp.getInt("which",0);
		view.setBackgroundResource(bgIcons[which]);
		TextView tv = (TextView) view.findViewById(R.id.tv_toast_address);
		tv.setText(text);
		params = new LayoutParams();
		params.gravity = Gravity.LEFT+Gravity.TOP;//����ϵΪ��������Ͻ�
		params.x = 20;//ˮƽ����ľ���
		params.y = 20;//��ֱ����ľ���
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		//��������,���Ա������͵����ϵͳ������,�嵥�ļ�����Ȩ��
		params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE; //2007
		wm.addView(view, params);
	}

}

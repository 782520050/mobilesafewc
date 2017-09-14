package com.itheima.mobilesafe.service;

import java.util.List;

import com.itheima.mobilesafe.activities.EnterPasswordActivity;
import com.itheima.mobilesafe.db.dao.ApplockDao;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class WatchDogService extends Service {
	public static final String TAG = "WatchDogService";
	private ActivityManager am;
	private boolean flag;
	private ApplockDao dao;
	private InnerReceiver receiver;
	/**
	 * ��ʱֹͣ�����İ���
	 */
	private String tempStopProtectPackname;
	/**
	 * ���еı������İ���
	 */
	private List<String> lockedPacknames;

	private MyObserver observer;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		dao = new ApplockDao(this);
		receiver = new InnerReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.itheima.mobilesafe.tempstopprotect");
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);

		registerReceiver(receiver, filter);
		// ��ȡ�������ݿ�������ȫ���İ���
		lockedPacknames = dao.findAll();
		Uri uri = Uri.parse("content://com.itheima.mobilesafe.applockdb");
		// ע�����ݹ۲��߹۲����ݿ����ݵı仯
		observer = new MyObserver(new Handler());
		getContentResolver().registerContentObserver(uri, true, observer);

		// �������Ź� ����ϵͳ���е�״̬��
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		startWatchDog();

		super.onCreate();
	}

	private void startWatchDog() {
		new Thread() {
			public void run() {
				flag = true;
				List<RunningTaskInfo> infos;
				String packname;
				// ������һ����������Ľ������û��������롣
				Intent intent = new Intent(WatchDogService.this,
						EnterPasswordActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				while (flag) {
					// ��ȡϵͳ�����������е�����ջ��Ϣ��������е�����ǰ�档���������������ļ��ϡ�
					infos = am.getRunningTasks(1);
					packname = infos.get(0).topActivity.getPackageName();
					// if(dao.find(packname)){//��Ҫ������//��ѯ�������ݿ⣬�޸�Ϊ��ѯ�ڴ�
					if (lockedPacknames.contains(packname)) {// ��ѯ�ڴ�
						// ����Ƿ���Ҫ��ʱֹͣ����
						if (packname.equals(tempStopProtectPackname)) {
							SystemClock.sleep(30);
							continue;
						}
						intent.putExtra("packname", packname);
						startActivity(intent);
					}
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		flag = false;
		getContentResolver().unregisterContentObserver(observer);
		observer = null;
	}

	private class InnerReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if ("com.itheima.mobilesafe.tempstopprotect".equals(action)) {
				tempStopProtectPackname = intent.getStringExtra("packname");
			} else if (Intent.ACTION_SCREEN_ON.equals(action)) {
				Log.i(TAG, "��Ļ�����ˣ��������Ź����߳�");
				if (flag == false) {
					startWatchDog();
				}
			} else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
				Log.i(TAG, "��Ļ�����ˣ��رտ��Ź����߳�");
				flag = false;
			}
		}
	}

	private class MyObserver extends ContentObserver {

		public MyObserver(Handler handler) {
			super(handler);
		}

		// ���ݹ۲��߹۲쵽���ݱ仯���õķ���
		@Override
		public void onChange(boolean selfChange) {
			Log.i(TAG, "���Ź���������ݹ۲��߹۲쵽�����ݿ�仯��������");
			lockedPacknames = dao.findAll();
			super.onChange(selfChange);
		}
	}
}

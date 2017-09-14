package com.itheima.mobilesafe.service;

import java.util.Timer;
import java.util.TimerTask;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.ui.receiver.MyWidget;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.ActivityManager.MemoryInfo;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

public class UpdateWidgetService extends Service {
	private Timer timer;
	private TimerTask task;
	private AppWidgetManager awm;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		//�õ�����С�ؼ��Ĺ�����
		awm = AppWidgetManager.getInstance(this);
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				//���ڸ��µ�������������view����������Ҫ���ֻ���ʿ���������view���ݸ�����
				//Ҫ�������һ����������ͣ�RemoteViews
				RemoteViews views = new RemoteViews(getPackageName(), R.layout.process_widget);
				views.setTextViewText(R.id.process_count, "�������е������"+getRunningProcessCount());
				views.setTextViewText(R.id.process_memory, "ʣ���ڴ�ռ䣺"+Formatter.formatFileSize(getApplicationContext(), getAvailMemory()));
				//���ڵ���ͼ����������һ��Ӧ�ó��������ִ�е���ͼ
				//�Զ���Ĺ㲥��Ϣ��
				Intent intent = new Intent();
				intent.setAction("com.itheima.mobilesafe.killall");
				PendingIntent pendingIntent = PendingIntent.getBroadcast(UpdateWidgetService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				//����ťע��һ������¼����¼���һ�����ڵ���ͼ
				views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
				ComponentName provider = new ComponentName(getApplicationContext(), MyWidget.class);
				awm.updateAppWidget(provider, views);
			}
		};
		timer.schedule(task, 0, 3000);
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		timer.cancel();
		task.cancel();
		timer = null;
		task = null;
	}
	/**
	 * ��ȡ�������еĽ��̵�����
	 * @return
	 */
	private int getRunningProcessCount(){
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		return am.getRunningAppProcesses().size();
	}
	/**
	 * ��ȡ�ֻ����õ��ڴ�ռ�
	 * @return
	 */
	private long getAvailMemory(){
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		//��ȡϵͳ��ǰ���ڴ���Ϣ�����ݷ���outInfo��������
		am.getMemoryInfo(outInfo);
		return outInfo.availMem;
	}
}

package com.itheima.mobilesafe.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itheima.mobilesafe.R;

public class AntiVirusActivity extends Activity {
	protected static final int FOUND_VIRUS = 1;
	protected static final int NOT_VIRUS = 2;
	protected static final int SCAN_FINISH = 3;
	private ImageView iv_scan;
	private ProgressBar pb_scan_status;
	/**
	 * ɨ����������
	 */
	private LinearLayout ll_container;
	private TextView tv_scan_status;
	
	private Handler hanlder = new Handler(){
		public void handleMessage(android.os.Message msg) {
			PackageInfo info;
			switch (msg.what) {
			case FOUND_VIRUS:
				info = (PackageInfo) msg.obj;
				TextView tv = new TextView(getApplicationContext());
				tv.setTextColor(Color.RED);
				tv.setText("���ֲ�����"+info.applicationInfo.loadLabel(getPackageManager()));
				ll_container.addView(tv, 0);
				break;
			case NOT_VIRUS:
				info = (PackageInfo) msg.obj;
				TextView tv2 = new TextView(getApplicationContext());
				tv2.setTextColor(Color.BLACK);
				tv2.setText("ɨ�谲ȫ��"+info.applicationInfo.loadLabel(getPackageManager()));
				ll_container.addView(tv2,0);
				break;
			case SCAN_FINISH:
				iv_scan.clearAnimation();
				iv_scan.setVisibility(View.INVISIBLE);
				tv_scan_status.setText("ɨ�����");
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anti_virus);
		pb_scan_status = (ProgressBar) findViewById(R.id.pb_scan_status);
		iv_scan = (ImageView) findViewById(R.id.iv_scan);
		ll_container = (LinearLayout) findViewById(R.id.ll_container);
		RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		ra.setDuration(2000);
		ra.setRepeatCount(Animation.INFINITE);
		ra.setRepeatMode(Animation.RESTART);
		iv_scan.startAnimation(ra);
		tv_scan_status = (TextView) findViewById(R.id.tv_scan_status);
		scanVirus();
		
	}
	/**
	 * ��ɱ�����ķ���
	 */
	private void scanVirus() {
		new Thread(){
			public void run() {
				//����ϵͳ�����ÿһ��Ӧ�ó���apk����ȡ����������
				PackageManager pm = getPackageManager();
				List<PackageInfo> infos = pm.getInstalledPackages(0);
				pb_scan_status.setMax(infos.size());
				int progress = 0;
				for(PackageInfo info:infos){
					String path = info.applicationInfo.sourceDir;
					String md5 = getFileMd5(path);
					//��ѯ���ݿ������ǲ����������¼������о��ǲ����������û�У�˵��Ӧ�ó���ɨ�谲ȫ
					SQLiteDatabase db = SQLiteDatabase.openDatabase(getFilesDir().getAbsolutePath()+"/antivirus.db", null, SQLiteDatabase.OPEN_READONLY);
					Cursor cursor = db.rawQuery("select desc from datable where md5=?", new String[]{md5});
					if(cursor.moveToNext()){
						String desc = cursor.getString(0);
						Message msg = Message.obtain();
						msg.what = FOUND_VIRUS;
						msg.obj = info;
						hanlder.sendMessage(msg);
					}else{
						Message msg = Message.obtain();
						msg.what = NOT_VIRUS;
						msg.obj = info;
						hanlder.sendMessage(msg);
					}
					progress++;
					SystemClock.sleep(50);
					pb_scan_status.setProgress(progress);
				}
				Message msg = Message.obtain();
				msg.what = SCAN_FINISH;
				hanlder.sendMessage(msg);
				
			};
		}.start();
	}
	/**
	 * �����ļ�·���õ��ļ���md5�㷨���ɵ�����ժҪ
	 * @param path
	 * @return
	 */
	private String getFileMd5(String path){
		try {
			File file = new File(path);
			//�õ�һ������ժҪ��
			MessageDigest digest = MessageDigest.getInstance("MD5");
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int len = 0;
			while((len = fis.read(buffer))!=-1){
				digest.update(buffer,0,len);
			}
			byte[] result = digest.digest();
			StringBuilder sb = new StringBuilder();
			for(byte b:result){
				int number = b&0xff;
				String str = Integer.toHexString(number);
				if(str.length()==1){
					sb.append("0");
				}
				sb.append(str);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	
}

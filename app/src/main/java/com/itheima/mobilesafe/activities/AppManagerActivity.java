package com.itheima.mobilesafe.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.domain.AppInfo;
import com.itheima.mobilesafe.engine.AppInfoProvider;
import com.itheima.mobilesafe.utils.DensityUtil;
import com.itheima.mobilesafe.utils.SystemInfoUtils;

public class AppManagerActivity extends Activity implements OnClickListener {
	private static final String TAG = "AppManagerActivity";
	private TextView tv_internal_size;
	private TextView tv_sd_size;
	/**
	 * ��ʾ���ݵ�listview
	 */
	private ListView lv_app;
	/**
	 * ���ڼ��ص����Բ���
	 */
	private LinearLayout ll_loading;
	/**
	 * �ֻ��������еİ�װ��Ӧ�ó�����Ϣ
	 */
	private List<AppInfo> appInfos;
	/**
	 * �û���Ӧ�ó��򼯺�
	 */
	private List<AppInfo> userAppInfos;

	/**
	 * ϵͳ��Ӧ�ó��򼯺�
	 */
	private List<AppInfo> systemAppInfos;
	private TextView tv_status;
	/**
	 * ������ǳ�����Ϣ���������� ���� ��Activity��ֻ��һ������������ڡ�
	 */
	private PopupWindow popupWindow;
	/**
	 * ж��
	 */
	private LinearLayout ll_uninstall;
	/**
	 * ����
	 */
	private LinearLayout ll_start;
	/**
	 * ����
	 */
	private LinearLayout ll_share;
	/**
	 * ��ʾӦ�ó�����Ϣ
	 */
	private LinearLayout ll_showinfo;

	/**
	 * ���������Ŀ������Ϣ
	 */
	private AppInfo clickedAppInfo;
	
	private AppManagerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appmanger);
		tv_internal_size = (TextView) findViewById(R.id.tv_internal_size);
		tv_sd_size = (TextView) findViewById(R.id.tv_sd_size);
		lv_app = (ListView) findViewById(R.id.lv_app);
		// ������ʾ���������
		tv_status = (TextView) findViewById(R.id.tv_status);
		// ��listviewע��һ�������ļ�����
		lv_app.setOnScrollListener(new OnScrollListener() {
			// ��������״̬�����仯��ʱ����õķ����� ��ֹ-->���� ����-->��ֹ
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			// һ��������ִ�еķ���
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (userAppInfos != null && systemAppInfos != null) {
					if (firstVisibleItem > userAppInfos.size()) {
						tv_status.setText("ϵͳ����" + systemAppInfos.size());
					} else {
						tv_status.setText("�û�����" + userAppInfos.size());
					}
				}
				if (popupWindow != null) {
					popupWindow.dismiss();
					popupWindow = null;
				}
			}
		});
		setAppInfoItemClickListener();
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		String internal_size = Formatter.formatFileSize(this,
				SystemInfoUtils.getInternalStorageFreeSize());
		String sd_size = Formatter.formatFileSize(this,
				SystemInfoUtils.getSDStorageFreeSize());
		tv_internal_size.setText("�����ڴ���ã�" + internal_size);
		tv_sd_size.setText("SD�����ã�" + sd_size);
		// ��ʾ���ڼ��ص�ui
		ll_loading.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				appInfos = AppInfoProvider
						.getAllAppInfos(AppManagerActivity.this);
				userAppInfos = new ArrayList<AppInfo>();
				systemAppInfos = new ArrayList<AppInfo>();
				for (AppInfo appInfo : appInfos) {
					if (appInfo.isSystemApp()) {// ϵͳ
						systemAppInfos.add(appInfo);
					} else {// �û�
						userAppInfos.add(appInfo);
					}
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// �������ڼ��ص�ui
						ll_loading.setVisibility(View.INVISIBLE);
						adapter = new AppManagerAdapter();
						lv_app.setAdapter(adapter);
					}
				});
			};
		}.start();
	}

	// �������ϵ�listview��itemע��һ������¼�
	private void setAppInfoItemClickListener() {
		lv_app.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (position == 0) {// ��0��λ�� ��һ��textview�ı�ǩ����ʾ�м����û�Ӧ�ó���
					return;
				} else if (position == (userAppInfos.size() + 1)) {
					return;
				} else if (position <= userAppInfos.size()) {// �û�����
					int newPosition = position - 1;// ��ȥ�û��ı�ǩtextviewռ�ݵ�λ��
					clickedAppInfo = userAppInfos.get(newPosition);
				} else {// ϵͳ����
					int newPosition = position - 1 - userAppInfos.size() - 1;
					clickedAppInfo = systemAppInfos.get(newPosition);
				}
				View contentView = View.inflate(AppManagerActivity.this,
						R.layout.item_popup_appinfo, null);
				ll_uninstall = (LinearLayout) contentView
						.findViewById(R.id.ll_uninstall);
				ll_share = (LinearLayout) contentView
						.findViewById(R.id.ll_share);
				ll_showinfo = (LinearLayout) contentView
						.findViewById(R.id.ll_showinfo);
				ll_start = (LinearLayout) contentView
						.findViewById(R.id.ll_start);
				ll_uninstall.setOnClickListener(AppManagerActivity.this);
				ll_share.setOnClickListener(AppManagerActivity.this);
				ll_showinfo.setOnClickListener(AppManagerActivity.this);
				ll_start.setOnClickListener(AppManagerActivity.this);

				if (popupWindow != null) {// �����Ļ���Ƿ��Ѿ������������壬�еĻ������̹ر�
					popupWindow.dismiss();
					popupWindow = null;
				}
				popupWindow = new PopupWindow(contentView, -2, -2);
				int[] location = new int[2];
				view.getLocationInWindow(location);
				// ����������popupwindow�ı���,͸���� �����ſ��Բ��š�
				popupWindow.setBackgroundDrawable(new ColorDrawable(
						Color.TRANSPARENT));
				int x = 65;//��λdip
				int px = DensityUtil.dip2px(getApplicationContext(), x);
				Log.i(TAG,"px="+px);
				popupWindow.showAtLocation(parent, Gravity.LEFT + Gravity.TOP,
						px, location[1]);
				// ָ�����Ŷ���
				ScaleAnimation sa = new ScaleAnimation(0.3f, 1.0f, 0.3f, 1.0f,
						Animation.RELATIVE_TO_SELF, 0,
						Animation.RELATIVE_TO_SELF, 0.5f);
				sa.setDuration(250);
				contentView.startAnimation(sa);
			}
		});
	}

	private class AppManagerAdapter extends BaseAdapter {
		/**
		 * ����listview�����ж��ٸ���Ŀ
		 */
		@Override
		public int getCount() {
			// ΪʲôҪ������1 �� ����������textview�ı�ǩ������listview��Ŀ�ĸ��������ˡ�
			return 1 + userAppInfos.size() + 1 + systemAppInfos.size();
		}

		/**
		 * ��ʾÿ����Ŀ��view����
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			AppInfo appInfo;
			if (position == 0) {// ��0��λ�� ��һ��textview�ı�ǩ����ʾ�м����û�Ӧ�ó���
				TextView tv = new TextView(getApplicationContext());
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				tv.setText("�û�����" + userAppInfos.size());
				return tv;
			} else if (position == (userAppInfos.size() + 1)) {
				TextView tv = new TextView(getApplicationContext());
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				tv.setText("ϵͳ����" + systemAppInfos.size());
				return tv;
			} else if (position <= userAppInfos.size()) {// �û�����
				int newPosition = position - 1;// ��ȥ�û��ı�ǩtextviewռ�ݵ�λ��
				appInfo = userAppInfos.get(newPosition);
			} else {// ϵͳ����
				int newPosition = position - 1 - userAppInfos.size() - 1;
				appInfo = systemAppInfos.get(newPosition);
			}

			// �Ѻÿ���xml����ת����view���󷵻ػ�ȥ
			View view;
			ViewHolder holder;
			if (convertView != null && convertView instanceof RelativeLayout) {// �ڸ�����ʷ����view�����ʱ�򣬲���Ҫ����Ƿ�Ϊ��
																				// ��Ҫ��������Ƿ���Ա�����
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(getApplicationContext(),
						R.layout.item_appinfo, null);
				holder = new ViewHolder();
				holder.iv_appIcon = (ImageView) view
						.findViewById(R.id.iv_appicon);
				holder.tv_appName = (TextView) view
						.findViewById(R.id.tv_appName);
				holder.tv_apkSize = (TextView) view
						.findViewById(R.id.tv_apkSize);
				holder.iv_install_location = (ImageView) view
						.findViewById(R.id.iv_install_location);
				view.setTag(holder);
			}
			holder.iv_appIcon.setImageDrawable(appInfo.getAppIcon());
			holder.tv_appName.setText(appInfo.getAppName());
			holder.tv_apkSize.setText("�����С��"
					+ Formatter.formatFileSize(getApplicationContext(),
							appInfo.getAppSize()));
			if (appInfo.isInRom()) {
				holder.iv_install_location.setImageResource(R.drawable.memory);
			} else {
				holder.iv_install_location.setImageResource(R.drawable.sd);
			}
			return view;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}

	/**
	 * ��ź��Ӷ��������
	 */
	static class ViewHolder {
		ImageView iv_appIcon;
		TextView tv_appName;
		TextView tv_apkSize;
		ImageView iv_install_location;
	}

	/**
	 * ����������Ŀ�ĵ���¼�
	 * 
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		if (popupWindow != null) {
			popupWindow.dismiss();
			popupWindow = null;
		}
		switch (v.getId()) {
		case R.id.ll_share:// ����
			Log.i(TAG, "����Ӧ�ó���" + clickedAppInfo.getAppName());
			shareApplication();
			break;
		case R.id.ll_uninstall:// ж��
			Log.i(TAG, "ж��Ӧ�ó���" + clickedAppInfo.getAppName());
			uninstallApplication();
			break;
		case R.id.ll_showinfo:// ��ʾӦ�ó�����Ϣ
			Log.i(TAG, "��ʾӦ�ó�����Ϣ" + clickedAppInfo.getAppName());
			showApplicationInfo();
			break;
		case R.id.ll_start:// ����
			Log.i(TAG, "����Ӧ�ó���" + clickedAppInfo.getAppName());
			startApplication();
			break;
		}
	}
	/**
	 * ��ʾӦ�ó�����ϸ��Ϣ
	 */
	private void showApplicationInfo() {
		Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setData(Uri.parse("package:"+clickedAppInfo.getPackName()));
		startActivity(intent);
	}
	/**
	 * ����Ӧ�ó���
	 */
	private void shareApplication() {
//		 <action android:name="android.intent.action.SEND" />
//         <category android:name="android.intent.category.DEFAULT" />
//         <data android:mimeType="text/plain" />
		Intent intent = new Intent();
		intent.setAction("android.intent.action.SEND");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "�Ƽ���ʹ��һ�������"+clickedAppInfo.getAppName()+"����ĺܺ���Ŷ");
		startActivity(intent);
	}

	/**
	 * ����һ��Ӧ�ó���
	 */
	private void startApplication() {
		PackageManager pm = getPackageManager();
		Intent intent = pm.getLaunchIntentForPackage(clickedAppInfo.getPackName());
		if(intent!=null){
			startActivity(intent);
		}else{
			Toast.makeText(this, "�Բ��𣬸�Ӧ���޷�������", Toast.LENGTH_LONG).show();
		}
		
	}
	/**
	 * ж��Ӧ�ó���
	 */
	private void uninstallApplication() {
//        <intent-filter>
//        <action android:name="android.intent.action.VIEW" />
//        <action android:name="android.intent.action.DELETE" />
//        <category android:name="android.intent.category.DEFAULT" />
//        <data android:scheme="package" />
//    </intent-filter>
		//ע��Ӧ�ó���ж�صĹ㲥������
		AppUninstallReceiver receiver = new AppUninstallReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addDataScheme("package");
		registerReceiver(receiver, filter);
		Intent intent = new Intent();
		intent.setAction("android.intent.action.DELETE");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setData(Uri.parse("package:"+clickedAppInfo.getPackName()));
		startActivity(intent);
		//����Ӧ�ó����Ƿ�ж�ص�������listview��������ݡ�
		
	}

	/**
	 * ��Activity�رյ��õķ���
	 */
	@Override
	protected void onDestroy() {
		if (popupWindow != null) {
			popupWindow.dismiss();
			popupWindow = null;
		}
		super.onDestroy();
	}
	
	private class AppUninstallReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			String data = intent.getData().toString();
			String packname = data.replace("package:", "");
			unregisterReceiver(this);
			AppInfo deleteAppInfo = null;
			//����ui����
			for(AppInfo appinfo: userAppInfos){
				if(appinfo.getPackName().equals(packname)){
					deleteAppInfo = appinfo;
				}
			}
			if(deleteAppInfo!=null){
				userAppInfos.remove(deleteAppInfo);
			}
			adapter.notifyDataSetChanged();
		}
	}
}

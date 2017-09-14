package com.itheima.mobilesafe.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.service.UpdateVirusDBService;
import com.itheima.mobilesafe.service.UpdateWidgetService;
import com.itheima.mobilesafe.utils.PackageInfoUtils;
import com.itheima.mobilesafe.utils.StreamTools;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class SplashActivity extends Activity {
	public static final String TAG = "SplashActivity";
	public static final int SHOW_UPDATE_DIALOG = 1;
	public static final int ERROR = 2;
	private TextView tv_splash_version;
	private ProgressDialog pd;
	/**
	 * �°汾apk������·��
	 */
	private String downloadpath;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SHOW_UPDATE_DIALOG:// ��ʾӦ�ø��¶Ի���
				String desc = (String) msg.obj;
				showUpdateDialog(desc);
				break;
			case ERROR:
				Toast.makeText(SplashActivity.this, "������-" + msg.obj, Toast.LENGTH_LONG).show();
				loadMainUI();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		String verison = PackageInfoUtils.getPackageVersion(this);
		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		tv_splash_version.setText("�汾:" + verison);
		// ���sp�����״̬,���Զ������Ƿ���
		SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
		boolean update = sp.getBoolean("update", true);
		if (update) {
			// �������̻߳�ȡ�������İ汾��Ϣ
			new Thread(new CheckVersionTask()).start();
		} else {
			new Thread() {
				public void run() {
					SystemClock.sleep(1500);
					loadMainUI();
				};
			}.start();
		}
		//�������������ݿ�
		copyDB("address.db");
		//������ɱ�������ݿ�
		copyDB("antivirus.db");
		
//		Intent intent = new Intent(this,UpdateVirusDBService.class);
//		startService(intent);
	}

	/**
	 * �������ݿ�
	 */
	private void copyDB(final String dbname) {
		File file = new File(getFilesDir(), dbname);
		if (file.exists() && file.length() > 0) {
			Log.i(TAG, "���ݿ����,���追��");
		} else {
			new Thread() {
				public void run() {
					// ��asset�ʲ�Ŀ¼��������ݿ��ļ�(��apk�����)�������ֻ�ϵͳ����
					try {
						InputStream is = getAssets().open(dbname);
						File file = new File(getFilesDir(), dbname);
						FileOutputStream fos = new FileOutputStream(file);
						byte[] buffer = new byte[1024];
						int len = -1;
						while ((len = is.read(buffer)) != -1) {
							fos.write(buffer, 0, len);
						}
						fos.close();
						is.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				};
			}.start();
		}
	}

	/**
	 * ��ʾ�Զ����µĶԻ���
	 * 
	 * @param desc
	 *            ����
	 */
	protected void showUpdateDialog(String desc) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setCancelable(false);
		builder.setTitle("��������");
		builder.setMessage(desc);
		builder.setPositiveButton("��������", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				pd = new ProgressDialog(SplashActivity.this);
				pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pd.show();
				HttpUtils http = new HttpUtils();
				File sdDir = Environment.getExternalStorageDirectory();
				File file = new File(sdDir, SystemClock.uptimeMillis() + ".apk");
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					http.download(downloadpath, file.getAbsolutePath(),
							new RequestCallBack<File>() {

								@Override
								public void onFailure(HttpException arg0,
										String arg1) {
									Toast.makeText(SplashActivity.this, "����ʧ��",
											Toast.LENGTH_LONG).show();
									loadMainUI();
									pd.dismiss();
								}

								@Override
								public void onLoading(long total, long current,
										boolean isUploading) {
									pd.setMax((int) total);
									pd.setProgress((int) current);
									super.onLoading(total, current, isUploading);

								}

								@Override
								public void onSuccess(
										ResponseInfo<File> fileinfo) {
									pd.dismiss();
									Toast.makeText(SplashActivity.this, "���سɹ�",
											Toast.LENGTH_LONG).show();
									// ���ǰ�װapk�ļ�
									Intent intent = new Intent();
									intent.setAction("android.intent.action.VIEW");
									intent.addCategory("android.intent.category.DEFAULT");
									intent.setDataAndType(
											Uri.fromFile(fileinfo.result),
											"application/vnd.android.package-archive");
									startActivity(intent);
								}

							});
				} else {
					// ����Ӧ�ó���������.
					Toast.makeText(SplashActivity.this, "sd��������,�޷��Զ�����", Toast.LENGTH_LONG)
							.show();
					loadMainUI();
				}
			}
		});
		builder.setNegativeButton("�´���˵", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				loadMainUI();
			}
		});
		builder.show();
	}

	private void loadMainUI() {
		Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
		startActivity(intent);
		finish();// �ر��Լ�,������ջ�˳�
	}

	/**
	 * ��ȡ���������õ����°汾��
	 */
	private class CheckVersionTask implements Runnable {
		@Override
		public void run() {
			Message msg = Message.obtain();
			long startTime = System.currentTimeMillis();
			try {
				String path = getResources().getString(R.string.url);
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(2000);
				int code = conn.getResponseCode();
				if (code == 200) {
					InputStream is = conn.getInputStream();
					String result = StreamTools.readStream(is);
					JSONObject json = new JSONObject(result);
					String serverVersion = json.getString("version");
					String description = json.getString("description");
					downloadpath = json.getString("downloadpath");
					Log.i(TAG, "�°汾������·��:" + downloadpath);
					Log.i(TAG, "�������汾:" + serverVersion);
					String localVersion = PackageInfoUtils
							.getPackageVersion(SplashActivity.this);
					if (localVersion.equals(serverVersion)) {
						Log.i(TAG, "�汾��һ��,��������,�������������");
						SystemClock.sleep(1500);
						loadMainUI();
					} else {
						Log.i(TAG, "�汾�Ų�һ��,��ʾ�û�����.");
						msg.what = SHOW_UPDATE_DIALOG;
						msg.obj = description;
					}
				} else {
					msg.what = ERROR;
					msg.obj = "code:410";
				}
			} catch (NotFoundException e) {
				msg.what = ERROR;
				msg.obj = "code:404";
				handler.sendMessage(msg);
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
				msg.what = ERROR;
				msg.obj = "code:405";
			} catch (IOException e) {
				e.printStackTrace();
				msg.what = ERROR;
				msg.obj = "code:408";
			} catch (JSONException e) {
				e.printStackTrace();
				msg.what = ERROR;
				msg.obj = "code:409";
			} finally {
				// ��������ߵ��⻨�ѵ�ʱ��
				long endTime = System.currentTimeMillis();
				long dTime = endTime - startTime;
				if (dTime > 2000) {

				} else {
					SystemClock.sleep(2000 - dTime);
				}
				handler.sendMessage(msg);
			}

		}
	}
}

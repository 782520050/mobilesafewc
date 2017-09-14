package com.itheima.mobilesafe.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.utils.SmsTools;
import com.itheima.mobilesafe.utils.SmsTools.BackupCallback;

public class CommonToolsActivity extends Activity {
	private static final String TAG = "CommonToolsActivity";
	private ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_tools);
	}
	/**
	 * �����������ز�ѯ
	 * @param view
	 */
	public void enterNumberQueryActivity(View view){
		Intent intent = new Intent(this,NumberQueryActivity.class);
		startActivity(intent);
	}
	/**
	 * ���ŵĻ�ԭ
	 * @param view
	 */
	public void smsRestore(View view){
		Log.i(TAG,"���Ż�ԭ");
	}
	/**
	 * ���ŵı���
	 * <xmlͷ>
	 * <infos>
	 * 	<info>
	 * 		<address>5556</address>
	 * 		<body>xx</body>
	 * 		<date></date>
	 * 		<type></type>
	 * 	</info>
	 * </infos>
	 * @param view
	 * �ʾ����м���ǡ������bug������
	 */
	public void smsBackup(View view){
		Log.i(TAG,"���ű���");
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.show();
		new Thread(){
			public void run() {
				SmsTools.backUpSms(CommonToolsActivity.this, new BackupCallback() {
					@Override
					public void onSmsBackup(int progress) {
						pd.setProgress(progress);
					}
					
					@Override
					public void beforeSmsBackup(int max) {
						pd.setMax(max);
					}
				});
				pd.dismiss();
			};
		}.start();
	}
	/**
	 * �������������
	 * @param view
	 */
	public void enterAppLock(View view){
		Intent intent = new Intent(this,ApplockActivity.class);
		startActivity(intent);
	}
}

package com.itheima.mobilesafe.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.R;

public class EnterPasswordActivity extends Activity {
	private ImageView iv_icon;
	private TextView tv_name;
	private EditText et_password;
	private String packname;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_pwd);
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
		tv_name = (TextView) findViewById(R.id.tv_name);
		et_password = (EditText) findViewById(R.id.et_password);
		Intent intent = getIntent();
		packname = intent.getStringExtra("packname");
		PackageManager pm = getPackageManager();
		try {
			ApplicationInfo appInfo = pm.getApplicationInfo(packname, 0);
			iv_icon.setImageDrawable(appInfo.loadIcon(pm));
			tv_name.setText(appInfo.loadLabel(pm));
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}
	public void enter(View view){
		String password = et_password.getText().toString().trim();
		if("123".equals(password)){
			//���߿��Ź��������Ѿ�������ȷ��������ʱ��ֹͣ�Ե�ǰӦ�ó���ı�����
			Intent intent = new Intent();
			intent.setAction("com.itheima.mobilesafe.tempstopprotect");
			intent.putExtra("packname", packname);
			sendBroadcast(intent);
			//������ȷ���ر���������Ľ���
			finish();
		}else{
			Toast.makeText(this, "�����������", Toast.LENGTH_LONG).show();
		}
	}
	@Override
	public void onBackPressed() {
//	     <action android:name="android.intent.action.MAIN" />
//       <category android:name="android.intent.category.HOME" />
//       <category android:name="android.intent.category.DEFAULT" />
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.HOME");
		intent.addCategory("android.intent.category.DEFAULT");
		startActivity(intent);
	}
	@Override
	protected void onStop() {
		super.onStop();
		finish();
	}
}

package com.itheima.mobilesafe.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.service.CallSmsSafeService;
import com.itheima.mobilesafe.service.ShowAddressService;
import com.itheima.mobilesafe.service.WatchDogService;
import com.itheima.mobilesafe.ui.SwitchImageView;
import com.itheima.mobilesafe.utils.ServiceStatusUtils;

public class SettingActivity extends Activity {
	//�������
	private SharedPreferences sp;
	//�Զ����µĿؼ�����
	private SwitchImageView siv_setting_update;
	private RelativeLayout rl_setting_update;
	//ɧ�����صĿؼ�����
	private SwitchImageView siv_callsmssafe;
	private RelativeLayout rl_setting_callsmssafe;
	
	//��������ʾ�ؼ�����
	private SwitchImageView siv_showlocation;
	private RelativeLayout rl_setting_showlocation;
	
	//�����ط���޸�
	private RelativeLayout rl_setting_changestyle;
	private String[] bgNames = {"��͸��","������","��ʿ��","������","ƻ����"};
	
	//�������ؼ�
	private SwitchImageView siv_applock;
	
	String s;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		//��ʼ��sp
		sp = getSharedPreferences("config", MODE_PRIVATE);
		siv_applock = (SwitchImageView) findViewById(R.id.siv_applock);
		//��ʼ���Զ���������
		siv_setting_update = (SwitchImageView) findViewById(R.id.siv_setting_update);
		siv_setting_update.setSwitchStatus(sp.getBoolean("update", true));
		rl_setting_update = (RelativeLayout) findViewById(R.id.rl_setting_update);
		rl_setting_update.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				siv_setting_update.changeSwitchStatus();
				Editor editor = sp.edit();
				editor.putBoolean("update", siv_setting_update.getSwitchStatus());
				editor.commit();
				//���濪�ص�״̬��sp
			}
		});
		//��ʼ��ɧ�����ص�����
		siv_callsmssafe = (SwitchImageView) findViewById(R.id.siv_callsmssafe);
		rl_setting_callsmssafe = (RelativeLayout) findViewById(R.id.rl_setting_callsmssafe);
		final Intent intent = new Intent(SettingActivity.this,CallSmsSafeService.class);
		//��ȡ��ǰ�������е�״̬,����״̬ȥ�޸Ľ�����ʾ������.
		boolean status = ServiceStatusUtils.isServiceRunning(this, "com.itheima.mobilesafe.service.CallSmsSafeService");
		siv_callsmssafe.setSwitchStatus(status);
		rl_setting_callsmssafe.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				siv_callsmssafe.changeSwitchStatus();
				boolean status = siv_callsmssafe.getSwitchStatus();
				if(status){
					startService(intent);
				}else{
					stopService(intent);
				}
			}
		});
		//��������ʾ����
		siv_showlocation = (SwitchImageView) findViewById(R.id.siv_showlocation);
		rl_setting_showlocation = (RelativeLayout) findViewById(R.id.rl_setting_showlocation);
		final Intent showAddressIntent = new Intent(this, ShowAddressService.class);
		boolean showAddressStatus = ServiceStatusUtils.isServiceRunning(this, "com.itheima.mobilesafe.service.ShowAddressService");
		siv_showlocation.setSwitchStatus(showAddressStatus);
		rl_setting_showlocation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				siv_showlocation.changeSwitchStatus();
				boolean status = siv_showlocation.getSwitchStatus();
				if(status){
					startService(showAddressIntent);
				}else{
					stopService(showAddressIntent);
				}
			}
		});
		
		//�޸Ĺ�������ʾ���
		rl_setting_changestyle=(RelativeLayout) findViewById(R.id.rl_setting_changestyle);
		rl_setting_changestyle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//�������޸ı����ĶԻ���
				AlertDialog.Builder builder = new Builder(SettingActivity.this);
				builder.setIcon(R.drawable.dialog_title_default_icon);
				builder.setTitle("��������ʾ����");
				builder.setSingleChoiceItems(bgNames, sp.getInt("which", 0), new  DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Editor editor = sp.edit();
						editor.putInt("which", which);
						editor.commit();
						dialog.dismiss();
					}
				});
				builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						s.equals("haha");
					}
				});
				builder.show();
			}
		});
		//�޸Ŀ��Ź����ص���ʾ״̬
		boolean applockStatus = ServiceStatusUtils.isServiceRunning(this, "com.itheima.mobilesafe.service.WatchDogService");
		siv_applock.setSwitchStatus(applockStatus);
	}
	
	
	public void changeApplockStatus(View view){
		Intent intent = new Intent(this,WatchDogService.class);
		if(ServiceStatusUtils.isServiceRunning(this, "com.itheima.mobilesafe.service.WatchDogService")){
			//�رշ���
			stopService(intent);
			siv_applock.setSwitchStatus(false);
		}else{
			//��������
			startService(intent);
			siv_applock.setSwitchStatus(true);
		}
	}
}

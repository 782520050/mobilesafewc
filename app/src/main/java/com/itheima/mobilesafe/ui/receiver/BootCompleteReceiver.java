package com.itheima.mobilesafe.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class BootCompleteReceiver extends BroadcastReceiver {
	private static final String TAG = "BootCompleteReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG,"�ֻ����������.");
		//�ж��û��Ƿ������ֻ������Ĺ���.
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		boolean protecting = sp.getBoolean("protecting", false);
		if(protecting){
			Log.i(TAG,"���������Ѿ�����,���sim���Ƿ�һ��.");
			//�û��󶨵�sim����
			String savedSim = sp.getString("sim", "");
			//��ȡ��ǰ�ֻ������sim����
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String currentSim = tm.getSimSerialNumber()+"afa";
			if(savedSim.equals(currentSim)){
				Log.i(TAG,"sim��һ��,���������ֻ�");
			}else{
				Log.i(TAG,"sim����һ��,�ֻ����ܱ���,���ͱ�������");
				String safenumber = sp.getString("safenumber", "");
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(safenumber, null, "SOS...phone may lost", null, null);
			}
		}
	}

}

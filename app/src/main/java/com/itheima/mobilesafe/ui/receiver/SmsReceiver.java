package com.itheima.mobilesafe.ui.receiver;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.service.LocationService;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {

	private static final String TAG = "SmsReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		for(Object obj:objs){
			SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
			String body = smsMessage.getMessageBody();
			if("#*location*#".equals(body)){
				Log.i(TAG,"�����ֻ���λ��..");
				//����һ����̨�ķ���,�ڷ��������ȡ�ֻ���λ��.
				Intent i = new Intent(context,LocationService.class);
				context.startService(i);
				abortBroadcast();
			}else if ("#*alarm*#".equals(body)){
				Log.i(TAG,"���ű�������..");
				MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
				player.start();
				abortBroadcast();
			}else if ("#*wipedata*#".equals(body)){
				Log.i(TAG,"�����������..");
				//��ȡϵͳ���豸����Ա�Ĺ�����
				DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
				dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
				abortBroadcast();
			}else if ("#*lockscreen*#".equals(body)){
				Log.i(TAG,"��������..");
				DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
				dpm.resetPassword("123", 0);
				dpm.lockNow();
				abortBroadcast();
			}
		}
	}
}

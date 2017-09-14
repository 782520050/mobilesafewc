package com.itheima.mobilesafe.service;

import java.lang.reflect.Method;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.itheima.mobilesafe.db.dao.BlackNumberDao;

public class CallSmsSafeService extends Service {
	public static final String Tag = "CallSmsSafeService";
	private TelephonyManager tm;
	private MyPhoneStateListener listener;
	private BlackNumberDao dao;
	private InnerSmsReceiver receiver;
	
	
	private class InnerSmsReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(Tag,"�����ڲ��㲥�����߽��յ��˶���");
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for(Object obj: objs){
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
				String body = smsMessage.getMessageBody();
				if(body.contains("fapiao")){
					//���ͷ��Ʊ���ĺ� �ִʼ���
					Log.i(Tag,"���ַ�Ʊ��������,����");
					abortBroadcast();
					return;
				}
				String sender = smsMessage.getOriginatingAddress();
				String mode = dao.find(sender);
				if("2".equals(mode)||"3".equals(mode)){
					Log.i(Tag,"���ֺ���������,����");
					abortBroadcast();
				}
			}
		}
	}
	

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		Log.i(Tag,"ɧ�����ط����Ѿ�����.");
		receiver = new InnerSmsReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		registerReceiver(receiver, filter);
		dao = new BlackNumberDao(this);
		//ע��һ���绰״̬�ļ�����.
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyPhoneStateListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		Log.i(Tag,"ɧ�����ط����Ѿ��ر�.");
		super.onDestroy();
		unregisterReceiver(receiver);
		receiver = null;
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;
	}

	/**
	 * �绰״̬�ļ�����
	 */
	private class MyPhoneStateListener extends PhoneStateListener{
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE://����״̬
				
				break;
			case TelephonyManager.CALL_STATE_RINGING://����״̬
				String mode = dao.find(incomingNumber);
				//1.�绰���� 2 ���� 3ȫ������.
				if("1".equals(mode)||"3".equals(mode)){
					Log.i(Tag,"�Ҷϵ绰");
					//��1.5�汾��,�Ҷϵ绰��api������һ������.
					endCall();//-->����ϵͳ�ײ�ķ��񷽷��Ҷϵ绰.
					//���������ṩ��������м�¼
					deleteCallLog(incomingNumber);
				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK://��ͨ�绰״̬.
				
				break;
			}
		}
	}

	/**
	 * �Ҷϵ绰
	 */
	public void endCall() {
		//ITelephony.Stub.asInterface(ServiceManager.getService(Context.TELEPHONY_SERVICE));
		try {
			Class clazz = getClassLoader().loadClass("android.os.ServiceManager");
			Method method = clazz.getDeclaredMethod("getService", String.class);
			IBinder iBinder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
			ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
			iTelephony.endCall();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ɾ������������ĺ��м�¼
	 * @param incomingNumber �������������
	 */
	public void deleteCallLog(final String incomingNumber) {
		final ContentResolver resolver = getContentResolver();
		final Uri uri = Uri.parse("content://call_log/calls");
		//�������ݹ۲��� �۲���м�¼�����ݿ�,��������˺��м�¼������ɾ�����м�¼
		resolver.registerContentObserver(uri, true, new ContentObserver(new Handler()) {
			@Override
			public void onChange(boolean selfChange) {
				//�����ݹ۲��߹۲쵽���ݿ�����ݱ仯��ʱ����õķ���.
				super.onChange(selfChange);
				resolver.delete(uri, "number=?", new String[]{incomingNumber});
			}
		});
	}
}

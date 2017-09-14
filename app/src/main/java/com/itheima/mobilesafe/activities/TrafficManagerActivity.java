package com.itheima.mobilesafe.activities;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;

import com.itheima.mobilesafe.R;

import android.app.Activity;
import android.net.TrafficStats;
import android.os.Bundle;
import android.text.format.Formatter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TrafficManagerActivity extends Activity {
	private TextView tv_total_traffic;
	private TextView tv_mobile_traffic;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_traffic_manager);
		// ʵ���������
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		// ��ȡҪǶ�������Ĳ���
		LinearLayout adLayout=(LinearLayout)findViewById(R.id.adLayout);
		// ����������뵽������
		adLayout.addView(adView);
		tv_total_traffic = (TextView) findViewById(R.id.tv_total_traffic);
		tv_mobile_traffic = (TextView) findViewById(R.id.tv_moblie_traffic);
		//��ȡȫ����translate��byte����ȡȫ���ϴ�������
		long totalTx = TrafficStats.getTotalTxBytes();
		//��ȡȫ����receive��byte����ȡȫ�������ص�����
		long totalRx = TrafficStats.getTotalRxBytes();
		long total = totalRx+totalTx;
		tv_total_traffic.setText(Formatter.formatFileSize(this, total));
		//�ƶ��������ص�������
		long moblieRx = TrafficStats.getMobileRxBytes();
		//�ƶ������ϴ���������
		long mobileTx = TrafficStats.getMobileTxBytes();
		long mobileTotal = mobileTx+moblieRx;
		tv_mobile_traffic.setText(Formatter.formatFileSize(this, mobileTotal));
		//����uid��ȡӦ�ó�����������ݡ�
		long rx = TrafficStats.getUidRxBytes(10078);
		long tx = TrafficStats.getUidTxBytes(10078);
		System.out.println("���أ�"+Formatter.formatFileSize(this, rx));
		System.out.println("�ϴ���"+Formatter.formatFileSize(this, tx));
		//rcv 266239 
		//snd 17372
	}
}

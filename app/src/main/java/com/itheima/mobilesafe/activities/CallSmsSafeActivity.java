package com.itheima.mobilesafe.activities;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.db.dao.BlackNumberDao;
import com.itheima.mobilesafe.domain.BlackNumberInfo;

public class CallSmsSafeActivity extends Activity {
	private BlackNumberDao dao;
	private ListView lv_callsms_safe;
	private CallSmsSafeAdapter adapter;
	private ImageView iv_callsms_safe_empty;
	private LinearLayout loading;
	/**
	 * �����������ȫ����Ϣ����
	 */
	private List<BlackNumberInfo> infos;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_callsms_safe);
		lv_callsms_safe = (ListView) findViewById(R.id.lv_callsms_safe);
		iv_callsms_safe_empty = (ImageView) findViewById(R.id.iv_callsms_safe_empty);
		loading = (LinearLayout) findViewById(R.id.loading);
		//��ȡȫ���ĺ���������
		dao = new BlackNumberDao(this);
		
		loading.setVisibility(View.VISIBLE);
		new Thread(){
			public void run() {
				infos = dao.findAll();
				adapter = new CallSmsSafeAdapter();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						lv_callsms_safe.setAdapter(adapter);
						loading.setVisibility(View.INVISIBLE);
					}
				});
			};
		}.start();
		
		
	}
	
	private class CallSmsSafeAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			int size =  infos.size();
			if(size>0){
				//listview���������� ����ͼƬ
				iv_callsms_safe_empty.setVisibility(View.INVISIBLE);
			}else{
				//listview����û������ ��ʾͼƬ
				iv_callsms_safe_empty.setVisibility(View.VISIBLE);
			}
			return size;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view ;
			ViewHolder holder;
			//������ʷ�����view����,����view���󴴽��ĸ�ʽ.
			if(convertView==null){
				view = View.inflate(CallSmsSafeActivity.this, R.layout.item_call_smssafe, null);
				//�൱��������,������¼���ӵ�����
				holder = new ViewHolder();
				holder.tv_phone = (TextView) view.findViewById(R.id.tv_item_blacknumber);
				holder.tv_mode = (TextView) view.findViewById(R.id.tv_item_mode);
				holder.iv_delete = (ImageView) view.findViewById(R.id.iv_item_delete);
				//�����������ڵ�ǰ��view��������.
				view.setTag(holder);
			}else{
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			//�����Ӻ��ӻ�Ƚ�����ʱ��,��Ҫ������Ĵ����һ�����Ż�.
			final BlackNumberInfo info = infos.get(position);
			holder.tv_phone.setText(info.getPhone());
			String mode = info.getMode(); //1�绰 2���� 3ȫ��
			if("1".equals(mode)){
				holder.tv_mode.setText("�绰����");
			}else if("2".equals(mode)){
				holder.tv_mode.setText("��������");
			}else{
				holder.tv_mode.setText("ȫ������");
			}
			holder.iv_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//�����ݿ�Ѽ�¼���Ƴ�
					boolean result = dao.delete(info.getPhone());
					if(result){
						Toast.makeText(CallSmsSafeActivity.this, "ɾ���ɹ�", Toast.LENGTH_LONG).show();
						//����ui����.
						infos.remove(info);//�Ƴ�listview�󶨵ļ������������
						adapter.notifyDataSetChanged();//֪ͨlistview��������ݸ���.
					}else{
						Toast.makeText(CallSmsSafeActivity.this, "ɾ��ʧ��", Toast.LENGTH_LONG).show();
					}
				}
			});
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
	 * view ������,�����洢�Ӻ��ӵ�����.
	 */
	class ViewHolder{
		TextView tv_phone;
		TextView tv_mode;
		ImageView iv_delete;
	}
	
	/**
	 * ��Ӻ���������
	 */
	public void addBlackNumber(View view){
		Intent intent = new Intent(this,AddBlackNumberActivity.class);
		//�����µĽ���,��ȡ����ֵ.
		startActivityForResult(intent, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data!=null){
			boolean flag = data.getBooleanExtra("flag", false);
			if(flag){
				//���»�ȡ����,
				//infos = dao.findAll();
				String phone = data.getStringExtra("phone");
				String mode = data.getStringExtra("mode");
				BlackNumberInfo info = new BlackNumberInfo();
				info.setPhone(phone);
				info.setMode(mode);
				infos.add(info);//������ӵ�����ֱ�Ӽ��뵽��������Ϳ�����.���������²�ѯ���ݿ�,Ӧ�ó����Ч�ʵõ������
				//֪ͨlistviewˢ�½���.
				adapter.notifyDataSetChanged();
				Toast.makeText(this, "��ӳɹ�", Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(this, "���ʧ��", Toast.LENGTH_LONG).show();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}

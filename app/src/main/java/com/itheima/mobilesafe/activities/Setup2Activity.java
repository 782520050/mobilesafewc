package com.itheima.mobilesafe.activities;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.itheima.mobilesafe.R;

public class Setup2Activity extends SetupBaseActivity {
	private RelativeLayout rl_setup2_bind;
	private ImageView iv_setup2_status;
	private TelephonyManager tm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		rl_setup2_bind = (RelativeLayout) findViewById(R.id.rl_setup2_bind);
		iv_setup2_status = (ImageView) findViewById(R.id.iv_setup2_status);
		//��ȡϵͳ����绰����ķ���.
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		//�ж��û��Ƿ�󶨹�sim��.
		String bindsim = sp.getString("sim", null);
		if(TextUtils.isEmpty(bindsim)){
			//���sp����û����Ϣ ˵��û�а�
			iv_setup2_status.setImageResource(R.drawable.unlock);
		}else{
			//���sp��������Ϣ,˵���Ѿ���
			iv_setup2_status.setImageResource(R.drawable.lock);
		}
		rl_setup2_bind.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//�ж�sim���Ƿ񱻰�
				String bindsim = sp.getString("sim", null);
				if(TextUtils.isEmpty(bindsim)){
					//û�а�,��Ҫ��,��sim���Ŵ��뵽sp
					String sim = tm.getSimSerialNumber();
					Editor editor = sp.edit();
					editor.putString("sim", sim);
					editor.commit();
					iv_setup2_status.setImageResource(R.drawable.lock);
				}else{
					//�Ѿ���,�����,��sp����洢��sim���������
					Editor editor = sp.edit();
					editor.putString("sim", null);
					editor.commit();
					iv_setup2_status.setImageResource(R.drawable.unlock);
				}
			}
		});
	}


	@Override
	public void next() {
		//�ж��û��Ƿ����sim������
		String bindsim = sp.getString("sim", null);
		if(TextUtils.isEmpty(bindsim)){
			Toast.makeText(this, "�ֻ�������Ч,�����Ȱ�sim��", Toast.LENGTH_LONG).show();
			return;
		}
		openNewActivityAndFinish(Setup3Activity.class);
		// �޸�Activity�л��Ķ���Ч��
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
	}

	@Override
	public void pre() {
		openNewActivityAndFinish(Setup1Activity.class);
		overridePendingTransition(R.anim.anim_pre_in, R.anim.anim_pre_out);
	}
}

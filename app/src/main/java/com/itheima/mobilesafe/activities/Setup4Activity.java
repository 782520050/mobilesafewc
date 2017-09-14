package com.itheima.mobilesafe.activities;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.itheima.mobilesafe.R;

public class Setup4Activity extends SetupBaseActivity {
	private CheckBox cb_setup4_status;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		cb_setup4_status = (CheckBox) findViewById(R.id.cb_setup4_status);
		cb_setup4_status.setChecked(sp.getBoolean("protecting", false));
		//����cb��״̬������
		cb_setup4_status.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Editor editor = sp.edit();
				editor.putBoolean("protecting", isChecked);
				editor.commit();
			}
		});
	}

	@Override
	public void next() {
		openNewActivityAndFinish(LostFindActivity.class);
		// ��һ����� ����Ӧ�ó����Ѿ��߹�һ��������.
		Editor editor = sp.edit();
		editor.putBoolean("configed", true);
		editor.commit();
		// �޸�Activity�л��Ķ���Ч��
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
	}

	@Override
	public void pre() {
		openNewActivityAndFinish(Setup3Activity.class);
		overridePendingTransition(R.anim.anim_pre_in, R.anim.anim_pre_out);

	}
}

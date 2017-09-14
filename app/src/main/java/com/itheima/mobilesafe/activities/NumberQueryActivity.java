package com.itheima.mobilesafe.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.db.dao.AddressDBDao;

public class NumberQueryActivity extends Activity {
	private EditText et_number;
	private TextView tv_location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_number_query);
		tv_location = (TextView) findViewById(R.id.tv_location);
		et_number = (EditText) findViewById(R.id.et_number);
		// ���ı������ע��һ�����ݱ仯�ļ�����.
		et_number.addTextChangedListener(new TextWatcher() {
			// ���ı��仯��ʱ����õķ���
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() >= 10) {
					String location = AddressDBDao.findLocation(s.toString());
					tv_location.setText("������Ϊ:" + location);
				}
			}

			// ���ı��仯֮ǰ���õķ���
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			// ���ı��仯֮����õķ���
			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	public void query(View view) {
		String number = et_number.getText().toString().trim();
		if (TextUtils.isEmpty(number)) {
			Toast.makeText(this, "���벻��Ϊ��", Toast.LENGTH_LONG).show();
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			et_number.startAnimation(shake);
			return;
		}
		String location = AddressDBDao.findLocation(number);
		tv_location.setText("������Ϊ:" + location);
	}
}

package com.itheima.mobilesafe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.itheima.mobilesafe.R;

public class Setup1Activity extends SetupBaseActivity {
	protected static final String TAG = "Setup1Activity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
	}

	

	@Override
	public void next() {
		openNewActivityAndFinish(Setup2Activity.class);
		// �޸�Activity�л��Ķ���Ч��
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
	}

	@Override
	public void pre() {

	}
}

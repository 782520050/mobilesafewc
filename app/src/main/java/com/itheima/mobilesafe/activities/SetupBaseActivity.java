package com.itheima.mobilesafe.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class SetupBaseActivity extends Activity {
	protected static final String TAG = "SetupBaseActivity";
	// 1.����һ������ʶ����
	private GestureDetector mGestureDetector;
	protected SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// 2.��ʼ������ʶ����
		mGestureDetector = new GestureDetector(this,
				new GestureDetector.SimpleOnGestureListener() {
					// ���û���ָ����Ļ�ϻ�����ʱ����õķ���
					// e1 ��ָ��һ�δ�������Ļ���¼�
					// e2 ��ָ�뿪��Ļһ˲���Ӧ���¼�
					// velocityX ˮƽ������ٶ�
					// velocityY ��ֱ������ٶ� ��λ����/s
					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						if (Math.abs(velocityX) < 200) {
							Log.i(TAG, "�ƶ���̫��,��Ч����");
							return true;
						}
						if (Math.abs(e2.getRawY() - e1.getRawY()) > 50) {
							Log.i(TAG, "��ֱ�����ƶ�����,��Ч����");
							return true;
						}
						if ((e1.getRawX() - e2.getRawX()) > 200) {
							Log.i(TAG, "���󻬶�,��ʾ��һ������");
							next();
							return true;
						}
						if ((e2.getRawX() - e1.getRawX()) > 200) {
							Log.i(TAG, "���һ���,��ʾ��һ������");
							pre();
							return true;
						}
						return super.onFling(e1, e2, velocityX, velocityY);
					}

				});
	}
	/**
	 * ���û���ָ����Ļ�ϴ�����ʱ����õķ���
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// ������ʶ����ʶ����������¼�
		mGestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	/**
	 * ��ʾ��һ��
	 */
	public abstract void next();
	/**
	 * ��ʾ��һ��
	 */
	public abstract void pre();
	
	
	public void showNext(View view){
		next();
	}
	
	/**
	 * ��ʾ��һ������
	 * @param view
	 */
	public void showPre(View view){
		pre();
	}
	
	/**
	 * ���µĽ��沢�ҹرյ���ǰҳ��
	 * @param cls
	 */
	public void openNewActivityAndFinish( Class<?> cls){
		Intent intent = new Intent(this,cls);
		startActivity(intent);
		finish();
	}
	
}

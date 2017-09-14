package com.itheima.mobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

/**
 * ������״̬�Ĺ�����
 */
public class ServiceStatusUtils {
	/**
	 * �ж�ĳ�������Ƿ�������״̬.
	 * @param context ������
	 * @param serviceFullName �����ȫ·������
	 * @return
	 */
	public static boolean isServiceRunning(Context context,String serviceFullName){
		//�õ�ϵͳ�Ľ��̹�����
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		//�õ�ϵͳ�����������еķ���
		List<RunningServiceInfo>  infos = am.getRunningServices(200);
		for(RunningServiceInfo info:infos){
			if(serviceFullName.equals(info.service.getClassName())){
				return true;
			}
		}
		return false;
	}
}

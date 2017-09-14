package com.itheima.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.domain.ProcessInfo;

/**
 * ������Ϣ��ҵ����
 */
public class ProcessInfoProvider {

	/**
	 * ��ȡ���е��������еĽ�����Ϣ
	 * 
	 * @param context
	 *            ������
	 * @return
	 */
	public static List<ProcessInfo> getRunningProcessInfos(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm = context.getPackageManager();
		List<RunningAppProcessInfo> infos = am.getRunningAppProcesses();
		List<ProcessInfo>  processInfos = new ArrayList<ProcessInfo>();
		for (RunningAppProcessInfo info : infos) {
			ProcessInfo processInfo = new ProcessInfo();
			// ������ʵ���Ͼ���Ӧ�ó���İ���
			String packName = info.processName;
			processInfo.setPackName(packName);
			long memSize = am.getProcessMemoryInfo(new int[]{info.pid})[0].getTotalPrivateDirty()*1024;
			processInfo.setMemSize(memSize);
			try {
				PackageInfo packInfo = pm.getPackageInfo(packName, 0);
				Drawable appIcon = packInfo.applicationInfo.loadIcon(pm);
				processInfo.setAppIcon(appIcon);
				String appName = packInfo.applicationInfo.loadLabel(pm).toString();
				processInfo.setAppName(appName);
				if((packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)!=0){
					//ϵͳ����
					processInfo.setUserProcess(false);
				}else{
					//�û�����
					processInfo.setUserProcess(true);
				}
			} catch (NameNotFoundException e) {
				processInfo.setAppName(packName);
				processInfo.setAppIcon(context.getResources().getDrawable(R.drawable.ic_launcher));
			}
			processInfos.add(processInfo);
		}
		return processInfos;
	}
}

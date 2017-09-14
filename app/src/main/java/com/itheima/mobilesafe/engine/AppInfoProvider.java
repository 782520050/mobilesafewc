package com.itheima.mobilesafe.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;

import com.itheima.mobilesafe.domain.AppInfo;

/**
 * ҵ�񷽷������ڻ�ȡϵͳ�������е�Ӧ�ó�����Ϣ
 */
public class AppInfoProvider {

	/**
	 * ��ȡϵͳ���е�Ӧ�ó�����Ϣ����
	 * @param context ������
	 * @return
	 */
	public static List<AppInfo> getAllAppInfos(Context context){
		//PackageManager ���������������ֻ������Ӧ�ó�����Ϣ��
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> packInfos = pm.getInstalledPackages(0);
		List<AppInfo> appInfos = new ArrayList<AppInfo>();
		for(PackageInfo packInfo : packInfos){
			AppInfo appInfo = new AppInfo();
			String packName = packInfo.packageName;
			Drawable appIcon = packInfo.applicationInfo.loadIcon(pm);
			String appName = packInfo.applicationInfo.loadLabel(pm).toString();
			String apkPath = packInfo.applicationInfo.sourceDir;
			//Ӧ�ó���ı�ǡ�flags�������Ǻܶ��ǵ�һ����ϡ�
			int flags = packInfo.applicationInfo.flags;
			if((flags&ApplicationInfo.FLAG_SYSTEM)!=0){
				//ϵͳӦ��
				appInfo.setSystemApp(true);
			}else{
				//�û�Ӧ��
				appInfo.setSystemApp(false);
			}
			if((flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE)!=0){
				//��װ��sd��
				appInfo.setInRom(false);
			}else{
				//��װ���ֻ��ڴ�
				appInfo.setInRom(true);
			}
			File file = new File(apkPath);
			long apkSize = file.length();
			appInfo.setAppIcon(appIcon);
			appInfo.setAppName(appName);
			appInfo.setAppSize(apkSize);
			appInfo.setPackName(packName);
			appInfos.add(appInfo);
		}
		SystemClock.sleep(1000);
		return appInfos;
	}
}

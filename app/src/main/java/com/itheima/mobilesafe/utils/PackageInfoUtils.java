package com.itheima.mobilesafe.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.widget.Button;

/**
 * ����Ϣ�Ĺ�����
 */
public class PackageInfoUtils {
	/**
	 * ��ȡӦ�ó���apk���İ汾��Ϣ
	 * 
	 * @param context
	 *            ������
	 * @return
	 */
	public static String getPackageVersion(Context context) {
		try {
			PackageInfo packinfo = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			String version = packinfo.versionName;
			return version;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "�����汾��ʧ��";
		}
	}
	/*���� ,������ʱû��ʲô����,������ĿҲ�Ѿ�����֪ͨ��ͣ��,����Ҫ����ʲô��ֱ��˵һ������*/
}

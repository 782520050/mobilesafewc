package com.itheima.mobilesafe.utils;

import java.io.File;

import android.os.Environment;

/**
 * ϵͳ��Ϣ�Ĺ�����
 * ��Ҫ��ȡ�ֻ���һЩ������Ϣ
 */
public class SystemInfoUtils {

	/**
	 * ��ȡϵͳ�ڲ��洢�ռ���ܴ�С
	 * @return
	 */
	public static long getInternalStorageSize(){
		File file = Environment.getDataDirectory();
		return file.getTotalSpace();
	}
	/**
	 * ��ȡϵͳ�ڲ��洢�ռ�Ŀ��ô�С
	 * @return
	 */
	public static long getInternalStorageFreeSize(){
		File file = Environment.getDataDirectory();
		return file.getFreeSpace();
	}
	/**
	 * ��ȡϵͳSD���洢�ռ�Ŀ��ô�С
	 * @return
	 */
	public static long getSDStorageFreeSize(){
		File file = Environment.getExternalStorageDirectory();
		return file.getFreeSpace();
	}
}

package com.itheima.mobilesafe.utils;

import java.io.File;

import android.os.Environment;

/**
 * 系统信息的工具类
 * 主要获取手机的一些参数信息
 */
public class SystemInfoUtils {

	/**
	 * 获取系统内部存储空间的总大小
	 * @return
	 */
	public static long getInternalStorageSize(){
		File file = Environment.getDataDirectory();
		return file.getTotalSpace();
	}
	/**
	 * 获取系统内部存储空间的可用大小
	 * @return
	 */
	public static long getInternalStorageFreeSize(){
		File file = Environment.getDataDirectory();
		return file.getFreeSpace();
	}
	/**
	 * 获取系统SD卡存储空间的可用大小
	 * @return
	 */
	public static long getSDStorageFreeSize(){
		File file = Environment.getExternalStorageDirectory();
		return file.getFreeSpace();
	}
}

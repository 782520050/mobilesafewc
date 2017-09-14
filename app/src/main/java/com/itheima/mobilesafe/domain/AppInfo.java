package com.itheima.mobilesafe.domain;

import android.graphics.drawable.Drawable;

/**
 * Ӧ�ó����ҵ��bean
 * ��������Ӧ�ó������Ϣ
 */
public class AppInfo {
	/**
	 * ͼ��
	 */
	private Drawable appIcon;
	/**
	 * ����
	 */
	private String appName;
	/**
	 * ����
	 */
	private String packName;
	/**
	 * ��С
	 */
	private long appSize;
	/**
	 * �Ƿ�װ���ֻ����ڲ��洢�ռ�
	 */
	private boolean inRom;
	/**
	 * �Ƿ���ϵͳӦ��
	 */
	private boolean systemApp;
	
	public boolean isInRom() {
		return inRom;
	}
	public void setInRom(boolean inRom) {
		this.inRom = inRom;
	}
	public boolean isSystemApp() {
		return systemApp;
	}
	public void setSystemApp(boolean systemApp) {
		this.systemApp = systemApp;
	}
	public Drawable getAppIcon() {
		return appIcon;
	}
	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getPackName() {
		return packName;
	}
	public void setPackName(String packName) {
		this.packName = packName;
	}
	public long getAppSize() {
		return appSize;
	}
	public void setAppSize(long appSize) {
		this.appSize = appSize;
	}
	@Override
	public String toString() {
		return "AppInfo [appName=" + appName + ", packName=" + packName
				+ ", appSize=" + appSize + "]";
	}
	
}

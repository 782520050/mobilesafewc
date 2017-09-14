package com.itheima.mobilesafe.domain;

import android.graphics.drawable.Drawable;

/**
 * ������Ϣ������ʵ��
 */
public class ProcessInfo {
	
	/**
	 * checkbox��״̬
	 */
	private boolean checked;
	/**
	 * ����ͼ�꣬Ӧ�ó���ͼ��
	 */
	private Drawable appIcon;
	/**
	 * Ӧ�ó�������
	 */
	private String appName;
	/**
	 * Ӧ�ó������
	 */
	private String packName;
	/**
	 * �ڴ�ռ�õĴ�С
	 */
	private long memSize;
	/**
	 * �Ƿ����û�����
	 */
	private boolean userProcess;
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
	public long getMemSize() {
		return memSize;
	}
	public void setMemSize(long memSize) {
		this.memSize = memSize;
	}
	public boolean isUserProcess() {
		return userProcess;
	}
	public void setUserProcess(boolean userProcess) {
		this.userProcess = userProcess;
	}
	@Override
	public String toString() {
		return "ProcessInfo [appName=" + appName + ", packName=" + packName
				+ ", memSize=" + memSize + ", userProcess=" + userProcess + "]";
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
}

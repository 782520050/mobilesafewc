package com.itheima.mobilesafe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;

import android.app.Application;
import android.os.Build;
import android.os.Environment;
/**
 * ��Ҫ���嵥�ļ�������
 * ��������ֻ���ʿ��Ӧ�ó���
 */
public class MobilesafeApplication extends Application {
	//Ӧ�ó��򴴽�֮ǰִ�еĵ�һ������
	//�ʺ���Ӧ�ó���ĳ�ʼ������
	@Override
	public void onCreate() {
		//��дϵͳ���쳣������
		super.onCreate();
		Thread.currentThread().setUncaughtExceptionHandler(new MyExceptionHandler());
	}
	
	
	private class MyExceptionHandler implements UncaughtExceptionHandler{
		//��������δ�����쳣��ʱ����õķ���
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			System.out.println("�������쳣�����Ǳ��粶���ˡ�����");
			StringBuffer sb = new StringBuffer();
			sb.append("time:");
			sb.append(System.currentTimeMillis()+"\n");
			Field[] fields = Build.class.getDeclaredFields();
			for(Field field:fields){
				try {
					String value = (String) field.get(null);
					String name = field.getName();
					sb.append(name+"="+value+"\n");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);	
			sb.append(sw.toString());
			try {
				File file = new File(Environment.getExternalStorageDirectory(),"error.log");
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(sb.toString().getBytes());
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			//��ɱ�ķ���. �����糬��
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}
	
}

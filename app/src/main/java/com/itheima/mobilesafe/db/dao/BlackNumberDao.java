package com.itheima.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

import com.itheima.mobilesafe.db.BlackNumberDBOpenHelper;
import com.itheima.mobilesafe.domain.BlackNumberInfo;

/**
 * ���������ݿ�,��ɾ�Ĳ��api
 *
 */
public class BlackNumberDao {
	private BlackNumberDBOpenHelper helper;
	/**
	 * �ڹ��췽�������ʼ��helper����.
	 * @param context
	 */
	public BlackNumberDao(Context context) {
		helper = new BlackNumberDBOpenHelper(context);
	}


	/**
	 * ��Ӻ���������
	 * @param phone �������绰����
	 * @param mode ����ģʽ 1�绰���� 2���� 3ȫ��
	 */
	public boolean add(String phone,String mode){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("phone", phone);
		values.put("mode", mode);
		long result = db.insert("blacknumber", null, values);
		db.close();//�ر����ݿ��ͷ���Դ
		if(result!=-1){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * ɾ������������
	 * @param phone
	 * @return
	 */
	public boolean delete(String phone){
		SQLiteDatabase db = helper.getWritableDatabase();
		int result = db.delete("blacknumber", "phone=?", new String[]{phone});
		db.close();
		if(result>0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * �޸ĺ��������������ģʽ
	 * @param phone ����������
	 * @param newMode �µ�����ģʽ
	 * @return
	 */
	public boolean updateMode(String phone,String newMode){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("mode", newMode);
		int result = db.update("blacknumber", values, "phone=?", new String[]{phone});
		db.close();
		if(result>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * ��ѯĳ�����������ģʽ
	 * @param phone ����
	 * @return ����ģʽ,�������null ����ǰ���벻�Ǻ���������.
	 */
	public String find(String phone){
		String mode = null;
		SQLiteDatabase db = helper.getReadableDatabase();//��ȡһ���ɶ������ݿ�
		Cursor cursor = db.query("blacknumber", new String[]{"mode"}, "phone=?", new String[]{phone}, null, null, null);
		if(cursor.moveToNext()){
			mode = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return mode;
	}
	/**
	 * ��ȡȫ���ĺ�����������Ϣ
	 * @return
	 */
	public List<BlackNumberInfo> findAll(){
		SQLiteDatabase db = helper.getReadableDatabase();//��ȡһ���ɶ������ݿ�
		List<BlackNumberInfo> infos  = new ArrayList<BlackNumberInfo>();
		Cursor cursor = db.query("blacknumber", new String[]{"_id","phone","mode"}, null, null, null, null, null);
		while(cursor.moveToNext()){
			String id = cursor.getString(0);
			String phone = cursor.getString(1);
			String mode = cursor.getString(2);
			BlackNumberInfo info = new BlackNumberInfo();
			info.setId(id);
			info.setPhone(phone);
			info.setMode(mode);
			infos.add(info);
		}
		cursor.close();
		db.close();
		SystemClock.sleep(1000);
		return infos;
	}
	
}

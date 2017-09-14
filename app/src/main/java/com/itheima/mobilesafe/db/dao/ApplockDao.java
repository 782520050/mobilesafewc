package com.itheima.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.itheima.mobilesafe.db.ApplockDBOpenHelper;

/**
 * ��������dao
 */
public class ApplockDao {
	private ApplockDBOpenHelper helper;
	private Context context;

	/**
	 * dao�Ĺ��췽��
	 * 
	 * @param context
	 *            ������
	 */
	public ApplockDao(Context context) {
		helper = new ApplockDBOpenHelper(context);
		this.context = context;
	}

	/**
	 * ���һ��Ҫ������Ӧ�ó�����Ϣ
	 * 
	 * @param packname
	 *            ����
	 */
	public void add(String packname) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("packname", packname);
		db.insert("lockinfo", null, values);
		db.close();
		// ����һ��֪ͨ��֪ͨ���ݹ۲���ĳ��·�������ݱ仯�ˡ�
		Uri uri = Uri.parse("content://com.itheima.mobilesafe.applockdb");
		context.getContentResolver().notifyChange(uri, null);
	}

	/**
	 * ɾ��һ��Ҫ������Ӧ�ó�����Ϣ
	 * 
	 * @param packname
	 *            ����
	 */
	public void delete(String packname) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("lockinfo", "packname=?", new String[] { packname });
		db.close();
		// ����һ��֪ͨ��֪ͨ���ݹ۲���ĳ��·�������ݱ仯�ˡ�
		Uri uri = Uri.parse("content://com.itheima.mobilesafe.applockdb");
		context.getContentResolver().notifyChange(uri, null);
	}

	/**
	 * ��ѯһ�������Ƿ�Ҫ������
	 * 
	 * @param packname
	 * @return
	 */
	public boolean find(String packname) {
		boolean result = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from lockinfo where packname=?",
				new String[] { packname });
		if (cursor.moveToNext()) {
			result = true;
		}
		cursor.close();
		db.close();
		return result;
	}

	/**
	 * ��ȡ���е������İ���
	 * 
	 * @return
	 */
	public List<String> findAll() {
		List<String> lockedPacknames = new ArrayList<String>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select packname from lockinfo", null);
		while (cursor.moveToNext()) {
			lockedPacknames.add(cursor.getString(0));
		}
		cursor.close();
		db.close();
		return lockedPacknames;
	}

}

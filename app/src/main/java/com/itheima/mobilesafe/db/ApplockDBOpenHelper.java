package com.itheima.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ApplockDBOpenHelper extends SQLiteOpenHelper {

	public ApplockDBOpenHelper(Context context) {
		super(context, "applock.db", null, 1);
	}

	//���ݿ��һ�α�������ʱ�����,�ʺϳ�ʼ�����ݿ�ı�ṹ
	@Override
	public void onCreate(SQLiteDatabase db) {
		//id ����������, packname ������Ӧ�ó���İ���
		db.execSQL("create table lockinfo (_id integer primary key autoincrement, packname varchar(20))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}

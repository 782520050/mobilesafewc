package com.itheima.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * ��������ص����ݿ�data access object
 */
public class AddressDBDao {
	/**
	 * ��ѯ�绰����Ĺ�������Ϣ
	 * 
	 * @param number
	 * @return
	 */
	public static String findLocation(String number) {
		String path = "/data/data/com.itheima.mobilesafe/files/address.db";
		String location = "���޴˺�";
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		// �ж� number �ǲ���һ���ֻ�����.
		// 1 [34578] + 9λ������
		// ^1[34578]\d{9}$
		boolean result = number.matches("^1[34578]\\d{9}$");
		if (result) {//�ֻ�����
			Cursor cursor = db
					.rawQuery(
							"select location from data2 where id = (select outkey from data1 where id = ?)",
							new String[] { number.substring(0, 7) });
			if (cursor.moveToNext()) {
				location = cursor.getString(0);
			}
			cursor.close();
		}else{//���ֻ�����
			switch (number.length()) {
			case 3: //110 119 120 999 
				location="�����绰";
				break;
			case 4://5556 ģ����
				location = "ģ����";
				break;
			case 5://
				location = "��ҵ�ͷ��绰";
				break;
			case 7:
				location = "���ص绰";
				break;
			case 8:
				location = "���ص绰";
				break;
			//01012345678	
			//075512345678
			default:
				if(number.length()>=10&&number.startsWith("0")){
					//��;�绰
					Cursor cursor = db.rawQuery("select location from data2 where area = ?", new String[]{number.substring(1, 3)});
					if(cursor.moveToNext()){
						String temp  = cursor.getString(0);
						location = temp.substring(0, temp.length()-2);//���˵����� �ƶ� ������ͨ
					}
					cursor.close();
					cursor = db.rawQuery("select location from data2 where area = ?", new String[]{number.substring(1, 4)});
					if(cursor.moveToNext()){
						String temp  = cursor.getString(0);
						location = temp.substring(0, temp.length()-2);
					}
					cursor.close();
				}
			}
		}
		db.close();
		return location;
	}
}

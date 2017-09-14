package com.itheima.mobilesafe.test;

import java.util.Random;

import com.itheima.mobilesafe.db.dao.BlackNumberDao;

import android.test.AndroidTestCase;

public class TestBlackNumberDao extends AndroidTestCase {
	public void testAdd() throws Exception {
		BlackNumberDao dao = new BlackNumberDao(getContext());
		long basenumber = 13500000000l;
		Random random = new Random();
		for(int i = 0;i<5000;i++){
			dao.add(String.valueOf(basenumber+i),String.valueOf(random.nextInt(3)+1));
		}
	}

	public void testDelete() throws Exception {
		BlackNumberDao dao = new BlackNumberDao(getContext());
		dao.delete("5558");
	}

	public void testUpdate() throws Exception {
		BlackNumberDao dao = new BlackNumberDao(getContext());
		dao.updateMode("5558", "2");
	}

	public void testFindMode() throws Exception {
		BlackNumberDao dao = new BlackNumberDao(getContext());
		String mode = dao.find("5558");
		if ("1".equals(mode)) {
			System.out.println("电话拦截");
		} else if ("2".equals(mode)) {
			System.out.println("短信拦截");
		} else if ("3".equals(mode)) {
			System.out.println("全部拦截");
		} else {
			System.out.println("不是黑名单号码");
		}
	}
}

package com.itheima.mobilesafe.fragment;

import java.io.File;

import com.itheima.mobilesafe.R;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CleanSDFragment extends Fragment {
	//����fragment��ʾ���ݵķ���
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return View.inflate(getActivity(), R.layout.fragment_clean_sd, null);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		File file = Environment.getExternalStorageDirectory();
		File[] files = file.listFiles();
		for(File f:files){
			if(f.isFile()){//�ļ�
				//.tmp .temp 
			}else{//�ļ���
				
			}
		}
	}
	
	
}

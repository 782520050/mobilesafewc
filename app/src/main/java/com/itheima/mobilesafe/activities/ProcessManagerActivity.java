package com.itheima.mobilesafe.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.domain.ProcessInfo;
import com.itheima.mobilesafe.engine.ProcessInfoProvider;

/**
 * ���̹���
 */
public class ProcessManagerActivity extends Activity {
	/**
	 * ��������
	 */
	private TextView tv_processcount;
	/**
	 * �ڴ�״̬
	 */
	private TextView tv_memory_status;
	/**
	 * �����б�
	 */
	private ListView lv_processinfos;
	
	/**
	 * ���н�����Ϣ�ļ���
	 */
	private List<ProcessInfo> infos;
	
	/**
	 * �����û�������Ϣ�ļ���
	 */
	private List<ProcessInfo> userInfos;
	
	/**
	 * ����ϵͳ������Ϣ�ļ���
	 */
	private List<ProcessInfo> systemInfos;
	
	private ProcessInfoAdapter adapter;
	/**
	 * ��¼�������еĽ�������
	 */
	private int runningProcessCount;
	/**
	 * ��¼ʣ����ڴ�ռ�
	 */
	private long availMem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//����ui����
		setContentView(R.layout.activity_process_manager);
		tv_processcount = (TextView) findViewById(R.id.tv_processcount);
		tv_memory_status = (TextView) findViewById(R.id.tv_memory_status);
		fillData();
		//��listviewע����Ŀ����¼�
		lv_processinfos.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//�õ�listviewĳ��λ�ö�Ӧ�Ķ���
				Object obj = lv_processinfos.getItemAtPosition(position);
				if(obj!=null){
					ProcessInfo info = (ProcessInfo) obj;
					if(info.getPackName().equals(getPackageName())){
						return;
					}
					CheckBox cb = (CheckBox) view.findViewById(R.id.cb);
					if(info.isChecked()){
						//ȡ��checkbox�Ĺ�ѡ
						cb.setChecked(false);
						info.setChecked(false);
					}else{
						//��ѡcheckbox
						cb.setChecked(true);
						info.setChecked(true);
					}
				}
			}
		});
	}
	//�������
	private void fillData() {
		runningProcessCount = getRunningProcessCount();
		tv_processcount.setText("�����н��̣�"+runningProcessCount+"��");
		availMem = getAvailMemory();
		tv_memory_status.setText("�����ڴ棺"+Formatter.formatFileSize(this, availMem));
		infos = ProcessInfoProvider.getRunningProcessInfos(this);
		userInfos = new ArrayList<ProcessInfo>();
		systemInfos = new ArrayList<ProcessInfo>();
		for(ProcessInfo info: infos){
			if(info.isUserProcess()){
				userInfos.add(info);
			}else{
				systemInfos.add(info);
			}
		}
		lv_processinfos = (ListView) findViewById(R.id.lv_processinfos);
		adapter = new ProcessInfoAdapter();
		lv_processinfos.setAdapter(adapter);
	}
	
	private class ProcessInfoAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return userInfos.size()+1+systemInfos.size()+1;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ProcessInfo info;
			if(position==0){
				TextView  tv = new TextView(getApplicationContext());
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("�û����̣�"+userInfos.size()+"��");
				return tv;
			}else if(position == (userInfos.size()+1)){
				TextView  tv = new TextView(getApplicationContext());
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("ϵͳ���̣�"+systemInfos.size()+"��");
				return tv;
			}else if(position<=userInfos.size()){//�û�����
				info = userInfos.get(position-1);
			}else{//ϵͳ����
				info = systemInfos.get(position-1-userInfos.size()-1);
			}
			View view;
			ViewHolder holder;
			if(convertView!=null&&convertView instanceof RelativeLayout){
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}else{
				view = View.inflate(getApplicationContext(), R.layout.item_process_item, null);
				holder = new ViewHolder();
				holder.iv = (ImageView) view.findViewById(R.id.iv_process_icon);
				holder.tv_mem = (TextView) view.findViewById(R.id.tv_process_memsize);
				holder.tv_name = (TextView) view.findViewById(R.id.tv_process_name);
				holder.cb = (CheckBox) view.findViewById(R.id.cb);
				view.setTag(holder);
			}
			if(info.getPackName().equals(getPackageName())){
				//��ǰitemΪ�ֻ���ʿ�Լ���Ӧ�ó���
				holder.cb.setVisibility(View.INVISIBLE);
			}else{
				holder.cb.setVisibility(View.VISIBLE);
			}
			holder.iv.setImageDrawable(info.getAppIcon());
			holder.tv_name.setText(info.getAppName());
			holder.tv_mem.setText("ռ���ڴ棺"+Formatter.formatFileSize(getApplicationContext(), info.getMemSize()));
			//ͨ��item���汣���״̬���½���checkbox��״̬
			holder.cb.setChecked(info.isChecked());
			return view;
		}
		@Override
		public Object getItem(int position) {
			ProcessInfo info;
			if(position==0){
				return null;
			}else if(position == (userInfos.size()+1)){
				return null;
			}else if(position<=userInfos.size()){//�û�����
				info = userInfos.get(position-1);
			}else{//ϵͳ����
				info = systemInfos.get(position-1-userInfos.size()-1);
			}
			return info;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}
	
	static class ViewHolder{
		ImageView iv;
		TextView tv_name ;
		TextView tv_mem;
		CheckBox cb;
	}
	
	/**
	 * ��ȡ�������еĽ��̵�����
	 * @return
	 */
	private int getRunningProcessCount(){
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		return am.getRunningAppProcesses().size();
	}
	/**
	 * ��ȡ�ֻ����õ��ڴ�ռ�
	 * @return
	 */
	private long getAvailMemory(){
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		//��ȡϵͳ��ǰ���ڴ���Ϣ�����ݷ���outInfo��������
		am.getMemoryInfo(outInfo);
		return outInfo.availMem;
	}
	/**
	 * ��������е�ѡ�еĽ���
	 * @param view
	 */
	public void killSelected(View view){
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		List<ProcessInfo> killedProcessInfos = new ArrayList<ProcessInfo>();
		for(ProcessInfo info: userInfos){
			if(info.isChecked()){
				am.killBackgroundProcesses(info.getPackName());
				killedProcessInfos.add(info);
			}
		}
		for(ProcessInfo info: systemInfos){
			if(info.isChecked()){
				am.killBackgroundProcesses(info.getPackName());
				killedProcessInfos.add(info);
			}
		}
		//����������ϣ�ˢ��listview�Ľ��档
		//fillData();
		long total = 0;
		for(ProcessInfo info:killedProcessInfos){
			total+=info.getMemSize();
			if(info.isUserProcess()){
				userInfos.remove(info);
			}else{
				systemInfos.remove(info);
			}
		}
		adapter.notifyDataSetChanged();
		Toast.makeText(this, "������"+killedProcessInfos.size()+"������,�ͷ���"+Formatter.formatFileSize(this, total)+"���ڴ�", Toast.LENGTH_LONG).show();
		runningProcessCount -=killedProcessInfos.size();
		tv_processcount.setText("�����н��̣�"+runningProcessCount+"��");
		availMem += total;
		tv_memory_status.setText("�����ڴ棺"+Formatter.formatFileSize(this, availMem));
		
	}
	/**
	 * һ��ȫѡ
	 * @param view
	 */
	public void selectAll(View view){
		for(ProcessInfo info: userInfos){
			if(info.getPackName().equals(getPackageName())){
				continue;
			}
			info.setChecked(true);
		}
		for(ProcessInfo info: systemInfos){
			info.setChecked(true);
		}
		adapter.notifyDataSetChanged();
	}
	/**
	 * һ����ѡ
	 * @param view
	 */
	public void selectOther(View view){
		for(ProcessInfo info: userInfos){
			if(info.getPackName().equals(getPackageName())){
				continue;
			}
			info.setChecked(!info.isChecked());
		}
		for(ProcessInfo info: systemInfos){
			info.setChecked(!info.isChecked());
		}
		adapter.notifyDataSetChanged();
	}
}

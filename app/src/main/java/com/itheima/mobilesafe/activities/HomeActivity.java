package com.itheima.mobilesafe.activities;

import net.youmi.android.AdManager;
import net.youmi.android.spot.SpotManager;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.utils.Md5Utils;

public class HomeActivity extends Activity {
	protected static final String TAG = "HomeActivity";
	private ImageView iv_home_logo;
	private GridView gv_item;
	private SharedPreferences sp;
	String[] names = new String[] { "�ֻ�����", "ɧ������", "����ܼ�", "���̹���", "����ͳ��",
			"�ֻ�ɱ��", "ϵͳ����", "���ù���" };
	int[] icons = new int[] { R.drawable.sjfd, R.drawable.srlj,
			R.drawable.rjgj, R.drawable.jcgl, R.drawable.lltj, R.drawable.sjsd,
			R.drawable.xtjs, R.drawable.cygj };
	String[] desc = new String[]{"Զ�̶�λ�ֻ�","ȫ������ɧ��","�����������","������������","����һĿ��Ȼ","�����޴�����",
			"ϵͳ������","���ù��ߴ�ȫ"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//���ù��,id,����,����ģʽ
		AdManager.getInstance(this).init("847377901819a2fb", "f2c5d51b6066058c", true);
		SpotManager.getInstance(this).loadSpotAds();
		setContentView(R.layout.activity_home);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		iv_home_logo = (ImageView) findViewById(R.id.iv_home_logo);
		gv_item = (GridView) findViewById(R.id.gv_home_item);
		ObjectAnimator oa = ObjectAnimator.ofFloat(iv_home_logo, "rotationY",
				45, 90, 135, 180, 225, 270, 315);
		oa.setDuration(3000);
		oa.setRepeatCount(ObjectAnimator.INFINITE);
		oa.setRepeatMode(ObjectAnimator.RESTART);
		oa.start();

		gv_item.setAdapter(new HomeAdapter());
		//��gridview����Ŀ���õ���¼�
		gv_item.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent;
				switch (position) {
				case 0://�ֻ�����
					//�ж��û��Ƿ����ù�����
					if(isSetupPwd()){
						Log.i(TAG,"������������Ľ���");
						showEnterPwdDialog();
					}else{
						Log.i(TAG,"������������Ľ���");
						showSetupPwdDialog();
					}
					break;
				case 1://ɧ������
					intent = new Intent(HomeActivity.this,CallSmsSafeActivity.class);
					startActivity(intent);
					break;
				case 2://���������
					intent = new Intent(HomeActivity.this,AppManagerActivity.class);
					startActivity(intent);
					break;
				case 3://���̹�����
					intent = new Intent(HomeActivity.this,ProcessManagerActivity.class);
					startActivity(intent);
					break;
				case 4://����ͳ��
					intent = new Intent(HomeActivity.this,TrafficManagerActivity.class);
					startActivity(intent);
					break;
				case 5://�ֻ�ɱ��
					intent = new Intent(HomeActivity.this,AntiVirusActivity.class);
					startActivity(intent);
					break;
				case 6://�ֻ�����
					intent = new Intent(HomeActivity.this,SystemOptisActivity.class);
					startActivity(intent);
					break;
				case 7: //���ù���
					intent = new Intent(HomeActivity.this,CommonToolsActivity.class);
					startActivity(intent);
					break;
				}
			}
		});
	}

	private AlertDialog dialog;
	/**
	 * ��ʾ��������Ի���,�Զ���Ի���
	 */
	protected void showSetupPwdDialog() {
		AlertDialog.Builder builder = new Builder(this);
		View view = View.inflate(this, R.layout.dialog_setup_pwd, null);
		builder.setView(view);
		final EditText et_pwd = (EditText) view.findViewById(R.id.et_dialog_pwd);
		final EditText et_pwd_confirm = (EditText) view.findViewById(R.id.et_dialog_pwd_confirm);
		Button bt_dialog_ok = (Button) view.findViewById(R.id.bt_dialog_ok);
		Button bt_dialog_cancle = (Button) view.findViewById(R.id.bt_dialog_cancle);
		bt_dialog_cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		bt_dialog_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String pwd = et_pwd.getText().toString().trim();
				String pwd_confirm = et_pwd_confirm.getText().toString().trim();
				if(TextUtils.isEmpty(pwd)||TextUtils.isEmpty(pwd_confirm)){
					Toast.makeText(HomeActivity.this, "���벻��Ϊ��", Toast.LENGTH_LONG).show();
					return;
				}
				if(!pwd.equals(pwd_confirm)){
					Toast.makeText(HomeActivity.this, "�����������벻һ��", Toast.LENGTH_LONG).show();
					return;
				}
				Editor editor = sp.edit();
				editor.putString("password", Md5Utils.encode(pwd));
				editor.commit();
				//�رնԻ���
				dialog.dismiss();
				//������������Ի���
				showEnterPwdDialog();
			}
		});
		//��ʾ�Ի���,�ѶԻ�������ø�����ĳ�Ա����
		dialog = builder.show();
	}

	/**
	 * ��ʾ��������Ի���
	 */
	protected void showEnterPwdDialog() {
		AlertDialog.Builder builder = new Builder(this);
		View view = View.inflate(this, R.layout.dialog_enter_pwd, null);
		builder.setView(view);
		final EditText et_pwd = (EditText) view.findViewById(R.id.et_dialog_pwd);
		Button bt_dialog_ok = (Button) view.findViewById(R.id.bt_dialog_ok);
		Button bt_dialog_cancle = (Button) view.findViewById(R.id.bt_dialog_cancle);
		bt_dialog_cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		bt_dialog_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//��ȡ�û����������
				String pwd = et_pwd.getText().toString().trim();
				if(TextUtils.isEmpty(pwd)){
					Toast.makeText(HomeActivity.this, "���벻��Ϊ��", Toast.LENGTH_LONG).show();
					return;
				}
				//��ȡԭ���û����õ�����
				String savedpwd = sp.getString("password", null);
				//�Ƚ�ԭ�����õ�������������������Ƿ�һ��.
				if(Md5Utils.encode(pwd).equals(savedpwd)){
					dialog.dismiss();
					//�ж��û��Ƿ����������򵼽���,����û��ǵ�һ��ʹ���ֻ���������,����ҳ�浽������
					boolean configed = sp.getBoolean("configed", false);
					if(configed){
						Log.i(TAG,"�û���ɹ�������,�����ֻ�������ui����");
						Intent intent = new Intent(HomeActivity.this,LostFindActivity.class);
						startActivity(intent);
					}else{
						Log.i(TAG,"�û�û����ɹ�������,���������򵼽���");
						Intent intent = new Intent(HomeActivity.this,Setup1Activity.class);
						startActivity(intent);
					}
					
				}else{
					Toast.makeText(HomeActivity.this, "�����������", Toast.LENGTH_LONG).show();
				}
			}
		});
		dialog = builder.show();
	}

	private class HomeAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return names.length;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(HomeActivity.this, R.layout.item_home, null);
			ImageView iv= (ImageView) view.findViewById(R.id.iv_homeitem_icon);
			TextView tv_item_title =(TextView) view.findViewById(R.id.tv_homeitem_title);
			TextView tv_item_desc =(TextView) view.findViewById(R.id.tv_homeitem_desc);
			iv.setImageResource(icons[position]);
			tv_item_title.setText(names[position]);
			tv_item_desc.setText(desc[position]);
			return view;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}
	
	/**
	 * ����������ý���
	 * @param view
	 */
	public void enterSettingActivity(View view){
		Intent intent = new Intent(this,SettingActivity.class);
		startActivity(intent);
	}
	/**
	 * �ж��û��Ƿ����ù�����
	 * @return
	 */
	private boolean isSetupPwd(){
		String password = sp.getString("password", null);
		if(TextUtils.isEmpty(password)){
			return false;
		}else{
			return true;
		}
	}
	
	public void onBackPressed() {
	    // �������Ҫ�����Ե�����˹رղ岥��档
	    if (!SpotManager.getInstance(this).disMiss()) {
	        // �����˳����ڣ�����ʹ���Զ������������ͻ��˶���,����demo,����ʹ�ö���������-1
	        super.onBackPressed();
	    }
	}
}
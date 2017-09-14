package com.itheima.mobilesafe.ui;

import com.itheima.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
/**
 * ����imageview
 */
public class SwitchImageView extends ImageView {
	/**
	 * ���ص�״̬,true��,false�ر�
	 */
	private boolean switchStatus = true;

	/**
	 * ��ȡ���ص�״̬
	 * @return
	 */
	public boolean getSwitchStatus() {
		return switchStatus;
	}
	/**
	 * ���ÿ��ص�״̬
	 * @param switchStatus
	 */
	public void setSwitchStatus(boolean switchStatus) {
		this.switchStatus = switchStatus;
		if(switchStatus){
			setImageResource(R.drawable.on);
		}else{
			setImageResource(R.drawable.off);
		}
	}
	/**
	 * �޸Ŀ��ص�״̬
	 */
	public void changeSwitchStatus(){
		setSwitchStatus(!switchStatus);
	}

	public SwitchImageView(Context context) {
		super(context);
	}

	public SwitchImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SwitchImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

}

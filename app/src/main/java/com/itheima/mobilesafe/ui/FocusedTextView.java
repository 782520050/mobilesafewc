package com.itheima.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
/**
 * �Զ���view����дview��д����ķ���
 */
public class FocusedTextView extends TextView {
	public FocusedTextView(Context context) {
		super(context);
	}

	public FocusedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FocusedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	//��д����ķ���,��ƭϵͳ textviewֱ�ӻ�ȡ�������.
	@Override
	public boolean isFocused() {
		return true;
	}
}

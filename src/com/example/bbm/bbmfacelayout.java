package com.example.bbm;
import java.util.ArrayList;
import java.util.List;

import com.example.manage.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * @文件描述 : 带表情的自定义输入框
 */
public class bbmfacelayout extends RelativeLayout implements
		OnItemClickListener, OnClickListener {

	private Context context;
	/** 输入框 */
	private EditText et_sendmessage;
	RelativeLayout moreChoose;
	public bbmfacelayout(Context context) {
		super(context);
		this.context = context;
	}
	public bbmfacelayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}
	public bbmfacelayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		onCreate();
	}

	private void onCreate() {
		Utils.handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what==Utils.CLOSE_MORE_OPERATE){
					if (moreChoose.getVisibility() == View.VISIBLE) {
						moreChoose.setVisibility(View.GONE);
					}
				}
			}
		};
		Init_View();
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_face:
			// 隐藏表情选择框
			Utils.handlerInput.sendEmptyMessage(Utils.CLOSE_INPUT);
			break;
		case R.id.sendmessage:
			// 隐藏更多操作选择框
			if (moreChoose.getVisibility() == View.VISIBLE) {
				moreChoose.setVisibility(View.GONE);
			}
			break;
		case R.id.bbm_facelayout_voice:
			if(bbmConstants.ifchange==false)
			{
				findViewById(R.id.bbm_publish_textarea).setVisibility(View.GONE);
				findViewById(R.id.bbm_facelayout_voice).setBackgroundResource(R.drawable.bbm_publish_text);
                bbmConstants.ifchange=true;
			}
			else
			{
				findViewById(R.id.bbm_publish_textarea).setVisibility(View.VISIBLE);
				findViewById(R.id.bbm_facelayout_voice).setBackgroundResource(R.drawable.bbm_facelayout_voice);
                bbmConstants.ifchange=false;
			}
			Log.d("TAG", "点击啦btnmore..");
			Utils.handlerInput.sendEmptyMessage(Utils.CLOSE_INPUT);
			if (moreChoose.getVisibility() == View.VISIBLE) {
				moreChoose.setVisibility(View.GONE);
			} else {
				moreChoose.setVisibility(View.VISIBLE);
			}
			break;

		}
	}
	/**
	 * 隐藏表情选择框
	 */
	public boolean hideFaceView() {
		// 隐藏表情选择框
		return false;
	}
	/**
	 * 初始化控件
	 */
	private void Init_View() {
		et_sendmessage = (EditText) findViewById(R.id.bbm_publish_textarea);
		et_sendmessage.setOnClickListener(this);
		//findViewById(R.id.btn_face).setOnClickListener(this);
		findViewById(R.id.bbm_facelayout_voice).setOnClickListener(this);
		//findViewById(R.id.camera).setOnClickListener(this);
		moreChoose=(RelativeLayout) findViewById(R.id.moreChoose);
		
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
}

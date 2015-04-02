package com.example.tsbx;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class MyItemClickListener implements OnItemClickListener
{

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		System.out.println("arg0="+arg0);
		System.out.println("arg1="+arg1);
		System.out.println("arg2="+arg2);
		System.out.println("arg3="+arg3);
	}

}

package com.example.manage;

import java.io.File;

import com.example.Around.AroundActivity;
import com.example.MyProfile.MyProfileActivity;
import com.example.Service.ServiceActivity;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;
import com.example.manage.*;

public class HomePage extends TabActivity
{   
    
   int[] ImageListlight={R.drawable.homepage_bottom_light1,R.drawable.homepage_bottom_light2,R.drawable.homepage_bottom_light3,R.drawable.homepage_bottom_light4};
   int[] ImageListdark={R.drawable.homepage_bottom_dark_1,R.drawable.homepage_bottom_dark_2,R.drawable.homepage_bottom_dark_3,R.drawable.homepage_bottom_dark_4};
   @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 //requestWindowFeature(Window.FEATURE_NO_TITLE);
		Tools.removeTitle(this);
		// Tools.fullscreen(true,this);
		setContentView(R.layout.homepage);
		creatAppDir(); 
		 final TabHost tabHost=getTabHost();
		 //composeLayout("tab2",R.drawable.ic_launcher)
		 final TabWidget tabWidget = tabHost.getTabWidget(); 
		 
		 tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("")
					.setContent(new Intent(this,MainMenu.class)));
			tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("")
					.setContent(new Intent(this,ServiceActivity.class)));
			tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("")
					.setContent(new Intent(this,AroundActivity.class)));
			tabHost.addTab(tabHost.newTabSpec("tab4").setIndicator("")
					.setContent(new Intent(this,MyProfileActivity.class)));
			
			tabHost.setCurrentTab(0);
			
		 for (int i = 0; i < tabWidget.getChildCount(); i++) { 
		 View vvv = tabWidget.getChildAt(i); 
		 //当点到某个标签时,用hit_color的TAB背景色 
		// if (tabHost.getCurrentTab() == i) { 
		// vvv.setBackgroundColor(Color.GRAY); 
		/// } else { 
		// vvv.setBackgroundDrawable(getResources().getDrawable(R.drawable.homepage_bottom1)); 
			 vvv.setBackgroundDrawable(getResources().getDrawable(ImageListdark[i]));
		// } 
		 }
		 View vvv = tabWidget.getChildAt(0); 
		 vvv.setBackgroundDrawable(getResources().getDrawable(ImageListlight[0]));
		 tabHost.setOnClickListener(new OnClickListener()
		 {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				System.out.println("dsfdsfdsa");
			}
			 
		 });
		 
		 tabHost.setOnTabChangedListener(new OnTabChangeListener() { 

			 public void onTabChanged(String tabId) { 

			 for (int i = 0; i < tabWidget.getChildCount(); i++) { 
			 View vvv = tabWidget.getChildAt(i); 
			 if (tabHost.getCurrentTab() == i) { 
			 vvv.setBackgroundColor(Color.alpha(30)); 
				 vvv.setBackgroundColor(Color.BLUE);
			vvv.setBackgroundDrawable(getResources().getDrawable( 
					 ImageListlight[i])); 
		 } else { 
			vvv.setBackgroundDrawable(getResources().getDrawable( 
					 ImageListdark[i]));
			 
			 
		 } 
			 } 
			 } 
			 }); 

		
		
	}
	public void creatAppDir()
	{
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			System.out.println("创建app文件夹");
			File f=new File(Environment.getExternalStorageDirectory(),"E-homeland");
	      	f.mkdirs();
	      	File ff=new File(Environment.getExternalStorageDirectory(),"E-homeland/audio");
	      	ff.mkdirs();
		}
		
	}
	 public View composeLayout(String s, int i) {    
         // 定义一个LinearLayout布局     
         LinearLayout layout = new LinearLayout(this);    
         // 设置布局垂直显示     
         layout.setOrientation(LinearLayout.VERTICAL);    
         ImageView iv = new ImageView(this);    
         // imageList.add(iv);    
         iv.setImageResource(i);    
         //设置图片布局  
         LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 50);    
         lp.setMargins(0, 0, 0, 0);    
         layout.addView(iv, lp);    
         // 定义TextView     
         TextView tv = new TextView(this);    
         tv.setGravity(Gravity.CENTER);    
         tv.setSingleLine(true);    
         tv.setText(s);    
         tv.setTextColor(Color.BLACK);    
         tv.setTextSize(10);    
         //设置Text布局  
         layout.addView(tv, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));    
         return layout;    
     }  
}


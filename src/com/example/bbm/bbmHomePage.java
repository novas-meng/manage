package com.example.bbm;


import java.io.File;

import com.example.communitynews.CommunityActivity;
import com.example.manage.R;
import com.example.tsbx.ChatActivity;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;


public class bbmHomePage extends TabActivity
{
    
   int[] ImageList={R.drawable.bbm_read_unpressed,R.drawable.bbm_publish_unpressed};
   int[] pressedImageList={R.drawable.bbm_read_pressed,R.drawable.bbm_publish_pressed};
   static Handler changeHandler;
   
   @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 //requestWindowFeature(Window.FEATURE_NO_TITLE);
		Tools.removeTitle(this);
		// Tools.fullscreen(true,this);
		setContentView(R.layout.bbm_homepage);
		Button bbm=(Button)findViewById(R.id.bbm_ret_button);
	bbm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				//startActivity(new Intent(getApplicationContext(),AroundActivity.class));
				bbmHomePage.this.finish();
			}
		});
	//	creatAppDir();
		 final TabHost tabHost=getTabHost();
		 changeHandler=new Handler()
		   {

			@Override
			public void handleMessage(Message msg) {
				// TODO 自动生成的方法存根
				bbmRefreshActivity.freshHandler.obtainMessage().sendToTarget();
				tabHost.setCurrentTab(0);
			}
			   
		   };
		 //composeLayout("tab2",R.drawable.ic_launcher)
		 final TabWidget tabWidget = tabHost.getTabWidget(); 
		 
		 tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("")
					.setContent(new Intent(this,bbmRefreshActivity.class)));
			tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("")
			.setContent(new Intent(this,bbmPublishActivity.class)));
		
		 for (int i = 0; i < tabWidget.getChildCount(); i++) { 
		 View vvv = tabWidget.getChildAt(i); 
		 vvv.setBackgroundDrawable(getResources().getDrawable(ImageList[i]));
		 }
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
			// vvv.setBackgroundColor(Color.alpha(30)); 
				// vvv.setBackgroundColor(Color.BLUE);
			vvv.setBackgroundDrawable(getResources().getDrawable( 
					pressedImageList[i] )); 
		 } else { 
			 vvv.setBackgroundDrawable(getResources().getDrawable( 
					 ImageList[i])); 
			 } 
			} 
		} 
	}); 		
}
	
	 public View composeLayout(String s, int i) {    
         LinearLayout layout = new LinearLayout(this);    
         layout.setOrientation(LinearLayout.VERTICAL);    
         ImageView iv = new ImageView(this);    
         iv.setImageResource(i);    
         LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 50);    
         lp.setMargins(0, 0, 0, 0);    
         layout.addView(iv, lp);    
         TextView tv = new TextView(this);    
         tv.setGravity(Gravity.CENTER);    
         tv.setSingleLine(true);    
         tv.setText(s);    
         tv.setTextColor(Color.BLACK);    
         tv.setTextSize(10);    
         layout.addView(tv, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));    
         return layout;    
     }  
}


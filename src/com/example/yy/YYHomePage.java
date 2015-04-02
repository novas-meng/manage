package com.example.yy;
import java.io.File;
import java.io.InputStream;

import com.example.bbm.Tools;
import com.example.bbm.bbmHomePage;
import com.example.manage.R;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
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


public class YYHomePage extends TabActivity
{
    
   int[] ImageList={R.drawable.yy_jiaofei,R.drawable.yy_pass};
   LinearLayout layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 //requestWindowFeature(Window.FEATURE_NO_TITLE);
		Tools.removeTitle(this);
		// Tools.fullscreen(true,this);
		setContentView(R.layout.yy_homepage);
	//	creatAppDir();
		  layout=(LinearLayout)findViewById(R.id.yylayout1);
		  layout.setDrawingCacheEnabled(true);
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inPreferredConfig = Bitmap.Config.RGB_565;
			opt.inPurgeable = true;
			opt.inInputShareable = true;
			InputStream is = getResources().openRawResource(
					 R.drawable.yy_background );
			Bitmap bm = BitmapFactory.decodeStream(is, null, opt);
			BitmapDrawable bd = new BitmapDrawable(getResources(), bm);
			layout.setBackgroundDrawable(bd);
			
		Button yy=(Button)findViewById(R.id.yy_ret_button);
		yy.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO 自动生成的方法存根
					//startActivity(new Intent(getApplicationContext(),AroundActivity.class));
					YYHomePage.this.finish();
				}
			});
		 Button addMsgButton=(Button)findViewById(R.id.yy_addmessage);
        addMsgButton.setOnClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				YYRefreshActivity.addMsgHandler.obtainMessage(1).sendToTarget();
			}       	
        });
		 final TabHost tabHost=getTabHost();
		 //composeLayout("tab2",R.drawable.ic_launcher)
		 final TabWidget tabWidget = tabHost.getTabWidget(); 
		 
		 tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("")
					.setContent(new Intent(this,YYRefreshActivity.class)));
		 tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("")
			.setContent(new Intent(this,YypasscardActivity.class)));
		 for (int i = 0; i < tabWidget.getChildCount(); i++) { 
			 View vvv = tabWidget.getChildAt(i); 
			  is = getResources().openRawResource(
					  ImageList[i]);
			 bm = BitmapFactory.decodeStream(is, null, opt);
			 bd = new BitmapDrawable(getResources(), bm);
			//layout.setBackgroundDrawable(bd);
			 vvv.setBackgroundDrawable(bd);
			
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
		//	vvv.setBackgroundDrawable(getResources().getDrawable( 
			//		 R.drawable.tab_change)); 
		 } else { 
		//	 vvv.setBackgroundDrawable(getResources().getDrawable( 
		//	 R.drawable.homepage_bottom2)); 
			 } 
			 } 
			 } 
			 }); 

		
		
	}
	
	 @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		 System.out.println("预约选项卡销毁");
		 System.gc();
		  layout.getDrawingCache().recycle();
		  this.finish();
		super.onDestroy();
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


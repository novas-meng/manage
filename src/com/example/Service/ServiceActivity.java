package com.example.Service;

import java.io.InputStream;

import com.example.logIn.Constants;
import com.example.manage.R;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ServiceActivity extends Activity{
	ImageView car;
	ImageView rebuy;
	ImageView busness;
	ImageView live;
	ImageView clean;
	ImageView health;
	ImageView green;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guanjia);
		  //给layout添加图片
        LinearLayout layout=(LinearLayout)findViewById(R.id.servicelayout1);
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		InputStream is = getResources().openRawResource(
				 R.drawable.bar_bg );
		Bitmap bm = BitmapFactory.decodeStream(is, null, opt);
		BitmapDrawable bd = new BitmapDrawable(getResources(), bm);
		layout.setBackgroundDrawable(bd);
		
		  LinearLayout layout1=(LinearLayout)findViewById(R.id.servicelayout2);
			is = getResources().openRawResource(
					 R.drawable.top_bg );
			 bm = BitmapFactory.decodeStream(is, null, opt);
			 bd = new BitmapDrawable(getResources(), bm);
			layout1.setBackgroundDrawable(bd);
	
		
		car=(ImageView)findViewById(R.id.aiche);
		car.setDrawingCacheEnabled(true);

		is = getResources().openRawResource(
				 R.drawable.aiche );
		 bm = BitmapFactory.decodeStream(is, null, opt);
		 bd = new BitmapDrawable(getResources(), bm);
		 car.setBackgroundDrawable(bd);
		
		
		rebuy=(ImageView)findViewById(R.id.rebuy);
		rebuy.setDrawingCacheEnabled(true);

		is = getResources().openRawResource(
				 R.drawable.rebuy );
		 bm = BitmapFactory.decodeStream(is, null, opt);
		 bd = new BitmapDrawable(getResources(), bm);
		 rebuy.setBackgroundDrawable(bd);
		
		busness=(ImageView)findViewById(R.id.busness);
		busness.setDrawingCacheEnabled(true);

		is = getResources().openRawResource(
				 R.drawable.busness );
		 bm = BitmapFactory.decodeStream(is, null, opt);
		 bd = new BitmapDrawable(getResources(), bm);
		 busness.setBackgroundDrawable(bd);
		
		live=(ImageView)findViewById(R.id.jz);
		live.setDrawingCacheEnabled(true);

		is = getResources().openRawResource(
				 R.drawable.jiazheng );
		 bm = BitmapFactory.decodeStream(is, null, opt);
		 bd = new BitmapDrawable(getResources(), bm);
		 live.setBackgroundDrawable(bd);
		 
		 
		clean=(ImageView)findViewById(R.id.baojie);
		clean.setDrawingCacheEnabled(true);

		is = getResources().openRawResource(
				 R.drawable.baojie );
		 bm = BitmapFactory.decodeStream(is, null, opt);
		 bd = new BitmapDrawable(getResources(), bm);
		 clean.setBackgroundDrawable(bd);
		
		health=(ImageView)findViewById(R.id.baojian);
		health.setDrawingCacheEnabled(true);

		is = getResources().openRawResource(
				 R.drawable.baojian );
		 bm = BitmapFactory.decodeStream(is, null, opt);
		 bd = new BitmapDrawable(getResources(), bm);
		 health.setBackgroundDrawable(bd);
		 
		 
		green=(ImageView)findViewById(R.id.lvhua);
		green.setDrawingCacheEnabled(true);

		is = getResources().openRawResource(
				 R.drawable.lvhua );
		 bm = BitmapFactory.decodeStream(is, null, opt);
		 bd = new BitmapDrawable(getResources(), bm);
		 green.setBackgroundDrawable(bd);
		
		car.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ServiceAdditionActivity.setclass("carecar");
				startActivity(new Intent(ServiceActivity.this,ServiceAdditionActivity.class));
			}
		});
        rebuy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ServiceAdditionActivity.setclass("rebuy");
				startActivity(new Intent(ServiceActivity.this,ServiceAdditionActivity.class));
			}
		});
        busness.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ServiceAdditionActivity.setclass("busness");
				startActivity(new Intent(ServiceActivity.this,ServiceAdditionActivity.class));
			}
		});
        live.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ServiceAdditionActivity.setclass("house");
				startActivity(new Intent(ServiceActivity.this,ServiceAdditionActivity.class));
			}
		});
        clean.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ServiceAdditionActivity.setclass("clean");
				startActivity(new Intent(ServiceActivity.this,ServiceAdditionActivity.class));
			}
		});
        health.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ServiceAdditionActivity.setclass("health");
				startActivity(new Intent(ServiceActivity.this,ServiceAdditionActivity.class));
			}
		});
        green.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ServiceAdditionActivity.setclass("green");
				startActivity(new Intent(ServiceActivity.this,ServiceAdditionActivity.class));
			}
		});
        System.out.println("asdsadsada");
		}
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK) {  
        	
            car.getDrawingCache().recycle();
        	 rebuy.getDrawingCache().recycle();
        	 busness.getDrawingCache().recycle();
        	 live.getDrawingCache().recycle();
        	 clean.getDrawingCache().recycle();
        	 health.getDrawingCache().recycle();
        	 green.getDrawingCache().recycle();
        	ServiceActivity.this.finish();
        	
            return true;   
        } else  
            return super.onKeyDown(keyCode, event);  
    }
}

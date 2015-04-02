package com.example.manage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Window;
import android.view.WindowManager;

public class Tools {
	public static void removeTitle(Activity a)
	{
		a.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	 public static void fullscreen(boolean enable,Activity a) {
	        if (enable) {
	            WindowManager.LayoutParams lp = a.getWindow().getAttributes();
	            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
	            a.getWindow().setAttributes(lp);
	            a.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	        } else {
	            WindowManager.LayoutParams attr = a.getWindow().getAttributes();
	            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
	            a.getWindow().setAttributes(attr);
	            a.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	        }
	 }
	 //image_path为图片的网络地址
	 public static Bitmap getUrlBitmap(String image_path) throws MalformedURLException, IOException
		{
			 BitmapFactory.Options options = new BitmapFactory.Options();  
	         options.inJustDecodeBounds = true;  
	         BitmapFactory.decodeStream(new URL(image_path).openStream(),null,options);  
	   
	           
	         int width=options.outWidth, height=options.outHeight;  
	         int scale=1;  
	         int temp=width>height?width:height;  
	         while(true){  
	             if(temp/2<240)  
	                 break;  
	             temp=temp/2;  
	             scale*=2;  
	         }  
	           
	         BitmapFactory.Options opt = new BitmapFactory.Options();  
	         opt.inSampleSize=scale;  
	         Bitmap mbitmap= BitmapFactory.decodeStream(new URL(image_path).openStream(), null, opt); 
	         return mbitmap;
		}
}

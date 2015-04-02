package com.example.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.example.manage.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class ImageShow extends Activity{
	String savepath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/E-homeland/imageshow/imageshow.png";
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imageshow);
		ImageView v=(ImageView)findViewById(R.id.showimage);
		
		
		try {
			v.setImageBitmap(getDiskBitmap(savepath));
			v.setScaleType(ScaleType.FIT_CENTER);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	private Bitmap getDiskBitmap(String pathString)  
	{  
	    Bitmap bitmap = null;  
	    try  
	    {  
	        File file = new File(pathString);  
	        if(file.exists())  
	        {  
	            bitmap = BitmapFactory.decodeFile(pathString);  
	        }  
	    } catch (Exception e)  
	    {  
	        // TODO: handle exception  
	    }  
	      
	      
	    return bitmap;  
	} 
}

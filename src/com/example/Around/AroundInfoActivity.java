package com.example.Around;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.communitynews.CommonUri;
import com.example.communitynews.DownLoadImage;

import com.example.communitynews.DownLoadImage.*;
import com.example.logIn.Constants;
import com.example.manage.R;
import com.example.tools.IP;
import com.example.tools.ImageCompress;
import com.example.tools.ImageShow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AroundInfoActivity extends Activity{
	private ProgressDialog dialog;
	String savepath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/E-homeland/aroundtemp/";
	String imgsavepath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/E-homeland/imageshow/";
	int width;
	protected void onCreate(Bundle savedInstanceState) {  
        // TODO Auto-generated method stub  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.aroundinfo);  
        WindowManager wm = this.getWindowManager();
        width= wm.getDefaultDisplay().getWidth();
        TextView com=(TextView)findViewById(R.id.community);
		TextView ecoin=(TextView)findViewById(R.id.EB);
		com.setText(Constants.Community);
		ecoin.setText("您的E币:"+Constants.ecoin);
        final ImageView image1=(ImageView)findViewById(R.id.aroundinfo_image1);
		final ImageView image2=(ImageView)findViewById(R.id.aroundinfo_image2);
		final ImageView image3=(ImageView)findViewById(R.id.aroundinfo_image3);
		final ImageView image4=(ImageView)findViewById(R.id.aroundinfo_image4);
        dialog=new ProgressDialog(this);
		dialog.setTitle("提示");
		dialog.setMessage("正在加载，请稍后...");
		dialog.show();
		File f=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/E-homeland");
	    if(!f.exists())
		   f.mkdirs();
	    File fn=new File(savepath);
	    if(!fn.exists())
	    	fn.mkdirs();
	    File f0=new File(savepath+"imgtemp");
	    if(!f0.exists())
	    	f0.mkdir();
	    	Intent x=getIntent();
	    String json=x.getStringExtra("json");
		System.out.println("aaa");
		//new MyTask().execute(json);
       setupinfo(json);
       
       
	}
	
	
	private void setimage(String url,final ImageView v,final String name)
	{
		File f=new File(savepath+"imgtemp/"+name);
		if(url.equals(""))
			v.setImageDrawable(null);
		else if(f.exists())
		{
			InputStream in;
			Bitmap bm = ImageCompress.getimage(savepath+"imgtemp/"+name+".png");
			float h= (float)width*((float) bm.getHeight()/(float)bm.getWidth());
		     
			   LayoutParams lp=v.getLayoutParams();
			   lp.height=(int)h;
			     v.setLayoutParams(lp);
			  v.setImageBitmap(bm);
			 // v.setScaleType(ScaleType.CENTER_CROP);
			 
			 
			 
		    
		  
            dialog.dismiss();
		}
		else{
		DownLoadImage downLoadImage1=new DownLoadImage(url);
		downLoadImage1.loadImage(new ImageCallback() {				
				@Override
			public void getBitmap(Bitmap bm) {
					try {
						ImageCompress.saveMyBitmap(name, bm, savepath+"imgtemp/");
						bm=ImageCompress.getimage(savepath+"imgtemp/"+name+".png");
						 float h= (float)width*((float) bm.getHeight()/(float)bm.getWidth());
					     
						   LayoutParams lp=v.getLayoutParams();
						   lp.height=(int)h;
						   
						     v.setLayoutParams(lp);
						  v.setImageBitmap(bm);
						//  v.setScaleType(ScaleType.CENTER_CROP);
						
						  
					dialog.dismiss();
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					
			}
		});	}
	}
	private void setupinfo(String v)
	{
		JSONObject info;
		try {
			info = new JSONObject(v);
		  
		TextView name= (TextView)findViewById(R.id.aroundinfo_name);
		TextView pername=(TextView)findViewById(R.id.aroundinfo_pername);
		TextView addr=(TextView)findViewById(R.id.aroundinfo_address);
		TextView phone=(TextView)findViewById(R.id.aroundinfo_phone);
		TextView word=(TextView)findViewById(R.id.aroundinfo_word);
		final ImageView image1=(ImageView)findViewById(R.id.aroundinfo_image1);
		final ImageView image2=(ImageView)findViewById(R.id.aroundinfo_image2);
		final ImageView image3=(ImageView)findViewById(R.id.aroundinfo_image3);
		final ImageView image4=(ImageView)findViewById(R.id.aroundinfo_image4);
		name.setText(info.getString("name"));
		pername.setText("负责人姓名:"+info.getString("pername"));
		addr.setText("地址:"+info.getString("addr"));
		phone.setText("电话:"+info.getString("phone"));
		final	String num=info.getString("name");
		phone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
			    intent.setAction("android.intent.action.DIAL");
			    intent.setData(Uri.parse("tel:"+num));
			    startActivity(intent);
			}
		});
		word.setText("商家留言:"+info.getString("words"));
		
		if(info.getString("img").length()!=2)
			 
		{
			String is=info.getString("certificate");
			String[] certificate=is.split("\\,");
			dialog.show();
			String imgstring=info.getString("img");
		imgstring=imgstring.substring(1, imgstring.length()-1);
		System.out.println(imgstring);
		String [] img=null;
		img=imgstring.split("\\,");
		for(int i=0;i<img.length;i++)
		{
			System.out.println(img[i]);
			System.out.println(certificate[i]);
			img[i]=img[i].substring(1, img[i].length()-1);
		}
		ArrayList<Integer> fins=new ArrayList<Integer>();
		for(int i=0;i<certificate.length;i++)
		{
			
			System.out.println(certificate[i]);
			if(certificate[i].equals("false"))
			{
				fins.add(i);
			}
		}
		if(fins.size()>1)
			
		{
			//Toast.makeText(getApplicationContext(), img[1], Toast.LENGTH_SHORT).show();
			System.out.println(IP.ip+":3000/uploads/"+img[1]);
		setimage(IP.ip+":3000/uploads/"+img[fins.get(1)],image2,img[fins.get(1)]);
		image2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Drawable img= image2.getDrawable();
				Bitmap bitmap= drawableToBitmap(img);
				try {
					ImageCompress.saveMyBitmap("imageshow", bitmap, imgsavepath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Intent draw= new Intent();
				draw.setClass(getApplicationContext(), ImageShow.class);
			
				startActivity(draw);
			}
		});}
		else ;
	    if(fins.size()>2)
	    {System.out.println(IP.ip+":3000/uploads/"+img[2]);
		setimage(IP.ip+":3000/uploads/"+img[fins.get(2)],image3,img[fins.get(2)]);
		image3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Drawable img= image3.getDrawable();
				Bitmap bitmap= drawableToBitmap(img);
				try {
					ImageCompress.saveMyBitmap("imageshow", bitmap, imgsavepath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Intent draw= new Intent();
				draw.setClass(getApplicationContext(), ImageShow.class);
			
				startActivity(draw);
			}
		});}
	    else ;
		if(fins.size()>3)
		{System.out.println(IP.ip+":3000/uploads/"+img[3]);
		setimage(IP.ip+":3000/uploads/"+img[fins.get(3)],image4,img[fins.get(3)]);
		image4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Drawable img= image4.getDrawable();
				Bitmap bitmap= drawableToBitmap(img);
				try {
					ImageCompress.saveMyBitmap("imageshow", bitmap, imgsavepath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Intent draw= new Intent();
				draw.setClass(getApplicationContext(), ImageShow.class);
			
				startActivity(draw);
			}
		});}
		if(fins.size()>0)
		{System.out.println(IP.ip+":3000/uploads/"+img[0]);
			setimage(IP.ip+":3000/uploads/"+img[fins.get(0)],image1,img[fins.get(0)]);
			
			image1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
					Drawable img= image1.getDrawable();
					Bitmap bitmap= drawableToBitmap(img);
					try {
						ImageCompress.saveMyBitmap("imageshow", bitmap, imgsavepath);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Intent draw= new Intent();
					draw.setClass(getApplicationContext(), ImageShow.class);
				
					startActivity(draw);
				}
			});}
		else ;
		}
		else dialog.dismiss();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Bitmap drawableToBitmap(Drawable drawable) {
		          // 取 drawable 的长宽
		          int w = drawable.getIntrinsicWidth();
		          int h = drawable.getIntrinsicHeight();
		  
		          // 取 drawable 的颜色格式
		          Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
		                  : Bitmap.Config.RGB_565;
		          // 建立对应 bitmap
		         Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		         // 建立对应 bitmap 的画布
		         Canvas canvas = new Canvas(bitmap);
		         drawable.setBounds(0, 0, w, h);
		         // 把 drawable 内容画到画布中
		         drawable.draw(canvas);
		         return bitmap;
		     }
	public byte[] Bitmap2Bytes(Bitmap bm) {
		         ByteArrayOutputStream baos = new ByteArrayOutputStream();
		         bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		         return baos.toByteArray();
		     }
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK) {  
        	
        	
        	AroundInfoActivity.this.finish();
        	
            return true;   
        } else  
            return super.onKeyDown(keyCode, event);  
    }
}

package com.example.communitynews;

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
import com.example.infoPush.infoPushActivity;
import com.example.manage.R;
import com.example.tools.IP;
import com.example.tools.ImageCompress;
import com.example.tools.ImageShow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CommunityInfoActivity extends Activity{
	private ProgressDialog dialog;
	 int width;
	String savepath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/E-homeland/communitynewtemp/";
	String imgsavepath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/E-homeland/imageshow/";
	protected void onCreate(Bundle savedInstanceState) {  
        // TODO Auto-generated method stub  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.communityinfo);  
        WindowManager wm = this.getWindowManager();
        width= wm.getDefaultDisplay().getWidth();
        System.out.println(width);
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
	    File fx=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/E-homeland/imageshow");
	    if(!fx.exists())
	    	fx.mkdirs();
	    File fk=new File(savepath+"imgtemp");
	    if(!fk.exists())
	    	fk.mkdirs();
	    
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
	private void setupinfo(String io)
	{
		JSONObject info;
		try {
			info = new JSONObject(io);
		String communityclass=info.getString("class");
		TextView v= (TextView)findViewById(R.id.communityclass);
		TextView title=(TextView)findViewById(R.id.info_title);
		TextView content=(TextView)findViewById(R.id.info_content);
		if(!info.has("img0"))
		dialog.dismiss();
		final ImageView image1=(ImageView)findViewById(R.id.info_image1);
		final ImageView image2=(ImageView)findViewById(R.id.info_image2);
		final ImageView image3=(ImageView)findViewById(R.id.info_image3);
		final ImageView image4=(ImageView)findViewById(R.id.info_image4);
		
		if(communityclass.equals("news"))
			v.setText("小区头条");
		else if(communityclass.equals("exposure"))
			v.setText("小区随拍");
		else if(communityclass.equals("activity"))
			v.setText("小区活动");
		else if(communityclass.equals("notice"))
			v.setText("社区公告");
		title.setText(info.getString("title"));
		content.setText(info.getString("content"));
		
		
		if(info.has("img0"))
			 
		{
			dialog.show();
			String img=info.getString("img0");
			setimage(IP.ip+":3000/uploads/"+img,image1,img);
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
		if(info.has("img1"))
			 
		{
			dialog.show();
			String img=info.getString("img1");
			setimage(IP.ip+":3000/uploads/"+img,image2,img);
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
		if(info.has("img2"))
			 
		{
			dialog.show();
			String img=info.getString("img2");
			setimage(IP.ip+":3000/uploads/"+img,image3,img);
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
		if(info.has("img3"))
			 
		{
			dialog.show();
			String img=info.getString("img3");
			setimage(IP.ip+":3000/uploads/"+img,image4,img);
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
			else ;
		

		}catch(JSONException e){ e.printStackTrace(); 
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
     
            setContentView(R.layout.null_view);
        	CommunityInfoActivity.this.finish();
        	
            return true;   
        } else  
            return super.onKeyDown(keyCode, event);  
    }
}

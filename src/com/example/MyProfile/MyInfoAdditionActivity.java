package com.example.MyProfile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.tools.FormFile;
import com.example.tools.ImageAdapter;
import com.example.tools.ImageCompress;
import com.example.tools.ScaleImageFromSdcardActivityforpic;
import com.example.tools.SocketHttpRequester;
import com.example.tools.Utils;
import com.example.manage.R;
import com.example.manage.R.id;
import com.example.manage.R.layout;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MyInfoAdditionActivity extends Activity{
	TextView album,photo;
	Bitmap bm=null;
	private String BASE_URL="http://192.168.1.125:3000";

	EditText username;
	JSONObject info;
	String name;
	int status=0;
	String image;
	Button confirm;
	String temp=Environment.getExternalStorageDirectory().getAbsolutePath()+"/E-homeland/myprofile/";
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myinfoaddition);
		album=(TextView)findViewById(R.id.album);
		photo=(TextView)findViewById(R.id.photo);
		username=(EditText)findViewById(R.id.editname);
		Bundle extra=getIntent().getExtras();
		if(extra!=null)  
			try {
				info=new JSONObject(extra.getString("userInfo"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		try {
			username.setText(info.getString("name"));
			image=info.getString("userimagepath");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		confirm=(Button)findViewById(R.id.myInfoaddconfirm);
		album.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//ImageAdapter.ClickList.clear();
				
				Intent intent=new Intent(MyInfoAdditionActivity.this,ScaleImageFromSdcardActivityforpic.class);
			    startActivityForResult(intent, Utils.SHOW_ALL_PICTURE);
			    
			}
		});
		photo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				File stDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/E-homeland/myprofile");
				  if (!stDir.exists()) {
				   stDir.mkdirs();
				  }
				  File destDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/E-homeland/myprofile/phototemp");
				  if (!destDir.exists()) {
				   destDir.mkdirs();
				  }
				String sFileFullPath =Environment.getExternalStorageDirectory().getAbsolutePath()+"/E-homeland/myprofile/phototemp"+"/userphoto"+".jpg";  
				File file = new File(sFileFullPath);  
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));  
				startActivityForResult(intent, 1);
			}
		});
		confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				name=username.getText().toString();
				
			}
		});
		}
	public void onActivityResult(int requestCode, int resultCode, Intent data) {  //照相机回调函数
	    if (requestCode == 1) {  
	        if (resultCode == RESULT_OK) {  
	        	System.out.println("photo");
	            bm=ImageCompress.getimage(Environment.getExternalStorageDirectory().getAbsolutePath()+"/E-homeland/myprofile/phototemp"+"/userphoto"+".jpg");
	            try {
					ImageCompress.saveMyBitmap("userimage", bm, Environment.getExternalStorageDirectory().getAbsolutePath()+"/E-homeland/myprofile/");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            // You can set bitmap to ImageView here  
	        }  }
	     else if(requestCode==Utils.SHOW_ALL_PICTURE)
	        {
	    	 if(ImageAdapter.ClickList.size()==0)
	    		 ;
	        	String path=ScaleImageFromSdcardActivityforpic.map.get(ImageAdapter.ClickList.get(0));
	            bm=ImageCompress.getimage(path);
	            System.out.println(path);
	             
	            try {
					ImageCompress.saveMyBitmap("userimage", bm, temp);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	     
	    
	    
	    
	        
	    }
	final Handler handler = new Handler(){
	    @SuppressWarnings("deprecation")
		@Override
	    public void handleMessage(Message msg) {
	       /// super.handleMessage(msg);
	       // Bundle data = msg.getData();
	       // val=data.getString("value");
	       // val = data.getString("value");
	        
	       // System.out.println("hello"+val);
	        //Log.i("mylog","请求结果-->" + val);
	    	if(msg.what==1)
	    		Toast.makeText(getApplicationContext(), "网络未连接", Toast.LENGTH_SHORT).show();
	    	
	    	else if(msg.what==0)
	    	{
	    		String ok=msg.obj.toString();
	    		if(ok=="done")
	    		{Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
	    		SharedPreferences sharedPreferences = getSharedPreferences("user", 
	            		Activity.MODE_PRIVATE); 
	    		SharedPreferences.Editor editor=sharedPreferences.edit();
	    		editor.putString("name", name);
	    		editor.putString("image", Environment.getExternalStorageDirectory().getAbsolutePath()+"/E-homeland/myprofile/phototemp"+"/userimage"+".png");
	    		editor.commit();
	    			}
	    		else
	    		{
	    			Toast.makeText(getApplicationContext(), "用户名不可用，请换一个", Toast.LENGTH_SHORT).show();
	    		}
	    	}
	    	
	    }
	};
	Runnable runnable = new Runnable(){    //发布信息的线程
	    @SuppressWarnings("deprecation")
		@Override
	    public void run() {
	        //
	        // TODO: http request.
	        //
	    	String rq=null;
	    	if(isNetworkConnected(getApplicationContext())==false)
				//Toast.makeText(getApplicationContext(), "网络未连接", Toast.LENGTH_SHORT).show();
				{
				Message msg=new Message();
		        msg.what=1;
		        msg.obj="noNet";
		        handler.obtainMessage(1,"noNet").sendToTarget();
		        
		        }
	    	else{
	    	File e=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/E-homeland/myprofile"+"/userimage.png");
	    	FormFile fm=new FormFile("userimage", e, "image","image/png");    
	    	String res=null;
	    	JSONObject conf=new JSONObject();
	    	try {
				conf.put("userid", info.get("userid"));
				conf.put("username", username.getText().toString());
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    	
	    	
				 try {
					
					 
					 //ImageCompress.cleanCustomCache("/sdcard/data/com.example.wy1/temp");  //清理缓存
					 Map<String, String> params = new HashMap<String, String>();
                    params.put("json", conf.toString());
                    String url=BASE_URL+"/user/postinfo";
					  SocketHttpRequester.post(url, params, fm);
					   
					   rq=SocketHttpRequester.request;
					  
				} catch (IOException o) {
					// TODO Auto-generated catch block
					o.printStackTrace();
				} catch (Exception o) {
					// TODO Auto-generated catch block
					o.printStackTrace();
				} 			
				 
				 Message msg=new Message();
				    msg.what=0;
				    msg.obj=rq;
				   
				    handler.obtainMessage(0,rq).sendToTarget();
	    	}
	        
	    }
	    
	};
	public boolean isNetworkConnected(Context context) {  //判断网络是否连接
	     if (context != null) {  
	          ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
	                  .getSystemService(Context.CONNECTIVITY_SERVICE);  
	          NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
	          if (mNetworkInfo != null) {  
	              return mNetworkInfo.isAvailable();  
	          }  
	      }  
	     return false;  
	 }  
	
	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		MyInfoAdditionActivity.this.finish();
		super.onDestroy();
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK) {  
        	
        	
        	MyInfoAdditionActivity.this.finish();
        	
            return true;   
        } else  
            return super.onKeyDown(keyCode, event);  
    }
	}


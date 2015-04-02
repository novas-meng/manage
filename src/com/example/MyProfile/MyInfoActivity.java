package com.example.MyProfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import com.example.manage.R;
import com.example.tools.IP;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class MyInfoActivity extends Activity{
	private TextView signature,userNameToShow,realnameToShow,adressToShow;
	private Button change1,change2,change3,change4;
	private ImageView face;
	private String serverImageUrl = "";
	private Bitmap bitmap = null;
	private String fileName = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myinfo);
		signature = (TextView)findViewById(R.id.signature);
		userNameToShow = (TextView)findViewById(R.id.userNameToShow);
		realnameToShow = (TextView)findViewById(R.id.realnameToShow);
		adressToShow = (TextView)findViewById(R.id.adressToShow);
		change1 = (Button)findViewById(R.id.myinfoChange1);
		change2 = (Button)findViewById(R.id.myinfo_change2);
		change3 = (Button)findViewById(R.id.myinfo_change3);
		change4 = (Button)findViewById(R.id.myinfo_change4);
		face = (ImageView)findViewById(R.id.profilePicture);
		//数据初始化
		File file = new File(Environment.getExternalStorageDirectory(),"E-homeland/MyInfo/upload.jpg");
		fileName = file.getPath();
		SharedPreferences mySharedPreferences = getSharedPreferences("user", Activity.MODE_PRIVATE);
		userNameToShow.setText(mySharedPreferences.getString("userName", ""));
		realnameToShow.setText(mySharedPreferences.getString("realname", "还未填写"));
		adressToShow.setText(mySharedPreferences.getString("address", ""));
		adressToShow.setTextSize((float) (adressToShow.getLineHeight()*0.4));
		signature.setText(mySharedPreferences.getString("signature", "还未填写"));
		serverImageUrl = mySharedPreferences.getString("imageUrl", "");
		System.out.println(serverImageUrl+"serverImage");
		if(serverImageUrl.equals("uploaded")){
			creatAppDir();
			try{
				File myCaptureFile = new File(Environment.getExternalStorageDirectory(),"E-homeland/MyInfo/upload.jpg");
				if(myCaptureFile.exists()){
					bitmap = BitmapFactory.decodeFile(myCaptureFile.getPath());
					face.setImageBitmap(bitmap);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
		}else{
			if(isWifiConnected()||isNetConnected())
				new Thread(runnable).start();
			else
				Toast.makeText(getBaseContext(), "网络没有连接", Toast.LENGTH_SHORT);
		}
		
		//设置修改按钮的监听器
		change1.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MyInfoActivity.this, MyInfoChangeActivity.class);
				startActivityForResult(intent, 0);
			}
			
		});
		change2.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MyInfoActivity.this, MyInfoChangeActivity.class);
				startActivityForResult(intent, 0);
			}
			
		});
		change3.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MyInfoActivity.this, MyInfoChangeActivity.class);
				startActivityForResult(intent, 0);
			}
			
		});
		change4.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MyInfoActivity.this, MyInfoChangeActivity.class);
				startActivityForResult(intent, 0);
			}
			
		});
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(resultCode == RESULT_OK){
			if(data.getBooleanExtra("ifChange", true)){
				SharedPreferences mySharedPreferences = getSharedPreferences("user", Activity.MODE_PRIVATE);
				userNameToShow.setText(mySharedPreferences.getString("userName", ""));
				realnameToShow.setText(mySharedPreferences.getString("realname", ""));
				adressToShow.setText(mySharedPreferences.getString("address", ""));
				signature.setText(mySharedPreferences.getString("signature", ""));
				bitmap = BitmapFactory.decodeFile(fileName);
				face.setImageBitmap(bitmap);
			}
			
		}
		//super.onActivityResult(requestCode, resultCode, data);
	}



	Runnable runnable = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(serverImageUrl.equals("[]")){
				System.out.println("getuserface");
				bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.userface);
				Message msg = new Message();
			      handler.sendMessage(msg);
			}else{
				if(serverImageUrl.length()>=2)
					serverImageUrl = serverImageUrl.substring(2,serverImageUrl.length()-2);
				serverImageUrl = IP.ip+":3000/uploads/"+serverImageUrl;
				HttpGet get = new HttpGet(serverImageUrl);
				  HttpClient client = new DefaultHttpClient();
				  
				  try {
				   HttpResponse response = client.execute(get);
				   if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
					   HttpEntity entity = response.getEntity();
					   InputStream is = entity.getContent();

					   bitmap = BitmapFactory.decodeStream(is); 
				   }else{
					   System.out.println("图片获取失败");
				   }
				  } catch (ClientProtocolException e) {
				   e.printStackTrace();
				  } catch (IOException e) {
					  //Toast.makeText(getBaseContext(), "图片获取失败", Toast.LENGTH_SHORT).show();
				   e.printStackTrace();
				  }
				  Message msg = new Message();
			      handler.sendMessage(msg);
			}
			}
			
		
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			System.out.println("setImageBitmap");
			face.setImageBitmap(bitmap);
			//存储图片/
			creatAppDir();
			FileOutputStream b = null;
			
			System.out.println(fileName);
			try {
				b = new FileOutputStream(fileName);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 80, b);// 把数据写入文件
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					b.flush();
					b.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	};
	
	/** 
     * 检测网络是否连接 
     *  
     * @return 
     */ 
    private boolean isNetConnected() {  
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);  
        if (cm != null) {  
            NetworkInfo[] infos = cm.getAllNetworkInfo();  
            if (infos != null) {  
                for (NetworkInfo ni : infos) {  
                    if (ni.isConnected()) {  
                        return true;  
                    }  
                }  
            }  
        }  
        return false;  
    }  
   
    /** 
     * 检测wifi是否连接 
     *  
     * @return 
     */ 
    private boolean isWifiConnected() {  
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);  
        if (cm != null) {  
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();  
            if (networkInfo != null 
                    && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {  
                return true;  
            }  
        }  
        return false;  
    }  
    
    public void creatAppDir()
	{
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			System.out.println("创建myinfo文件夹");
			File f=new File(Environment.getExternalStorageDirectory(),"E-homeland");
			if(!f.exists())
	      	f.mkdirs();
	      	File ff=new File(Environment.getExternalStorageDirectory(),"E-homeland/MyInfo");
	      	if(!ff.exists())
	      	ff.mkdirs();
		}	
	}
    
    @Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
    	MyInfoActivity.this.finish();
		super.onDestroy();
	}



	public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK) {  
        	
        	
        	MyInfoActivity.this.finish();
        	
            return true;   
        } else  
            return super.onKeyDown(keyCode, event);  
    }
}

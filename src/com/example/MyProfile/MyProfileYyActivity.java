package com.example.MyProfile;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.manage.R;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MyProfileYyActivity extends Activity{
	protected String res;
	private String BASE_URL="http://192.168.1.125:3000";
	private String userid;
	private String community;
	EditText uriedit;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yuyue);
		Button bian=(Button)findViewById(R.id.bian);
		final EditText k=(EditText)findViewById(R.id.uriinput);
		SharedPreferences mySharedPreferences = getSharedPreferences("user", Activity.MODE_PRIVATE); 
		SharedPreferences.Editor editor = mySharedPreferences.edit(); 
		editor.putString("name", "cuisoap"); 
		editor.putString("phonenum", "110110000");
		editor.putString("password", "123");
		editor.putBoolean("ifAutoLogin",false);
		editor.putString("community", "hesong3");
		editor.commit(); 
		bian.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				BASE_URL=k.getText().toString();
				Toast.makeText(getApplicationContext(), BASE_URL, Toast.LENGTH_SHORT).show();
				uriedit=(EditText)findViewById(R.id.uriinput);
				SharedPreferences sharedPreferences = getSharedPreferences("user", 
		        		Activity.MODE_PRIVATE); 
				if(sharedPreferences.contains("phonenum"))
		        {
		        	userid=sharedPreferences.getString("phonenum", "");
		        	Toast.makeText(getApplicationContext(), userid, Toast.LENGTH_SHORT).show();
		        }
		        else 
		        	Toast.makeText(getApplicationContext(), "用户未登录", Toast.LENGTH_SHORT).show();
		        if(sharedPreferences.contains("community"))
		        {
		        	community=sharedPreferences.getString("community", "");
		        	Toast.makeText(getApplicationContext(), community, Toast.LENGTH_SHORT).show();
		        	
		        }
		        else 
		        {
		            new Thread(runnable2).start();
		        }
		        
		        new Thread(runnable).start();
		       
			}
		});
	      
	      
		
		    
         
		//new Thread(runnable).start();
		//new Thread(runnable1).start();
	}
	
		 final Handler handler = new Handler(){
			    @SuppressWarnings("deprecation")
				@Override
			    public void handleMessage(Message msg) {
			      
			    	if(msg.what==1)
			    		Toast.makeText(getApplicationContext(), "网络未连接", Toast.LENGTH_SHORT).show();
			    	
			    	else if(msg.what==3)
			    	{
			    		try {
							JSONObject jso= new JSONObject(res);
							community=jso.getString("usercommunity");
							if(community=="")
								Toast.makeText(getApplicationContext(), "获取小区信息失败", Toast.LENGTH_SHORT).show();
							else Toast.makeText(getApplicationContext(), community, Toast.LENGTH_SHORT);
			    		} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    		
			    	}
			    	else if(msg.what==0)
			    	{
			    		System.out.println("chen");
			    		 res=(String)msg.obj;
			    		 
			    		 
			    		try {
							JSONObject jso= new JSONObject(res);
							JSONArray jsa=jso.getJSONArray("content");
			    			for(int i=0;i<=2;i++)
			    			{
			    				TableLayout tl = (TableLayout)findViewById(R.id.yyrecordlayout);
			    				
			    				tl.setShrinkAllColumns(true);
			    				TableRow tr = new TableRow(MyProfileYyActivity.this);				
			    				  TableRow.LayoutParams lp=new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			    				 lp.setMargins(1, 1, 1, 1);
			    				  tr.setLayoutParams(lp);
			    				// tr.setPadding(2, 2, 2, 2);
			    				
			    			  TextView	text1=new TextView(MyProfileYyActivity.this);
			    			  text1.setText(jsa.getJSONObject(i).getString("name"));
			    			  
			    			  text1.setGravity(Gravity.CENTER);
			    			  text1.setTextColor(Color.parseColor("#ffffff"));
			    			  text1.setTextSize(12);	
			    			  text1.setBackgroundColor(Color.parseColor("#CDC9C9"));
			    			  text1.setLayoutParams(getContentLayoutParams());
			    			  
			    			  //text1.setPadding(2, 2, 2, 2);
			    			  TextView	text2=new TextView(MyProfileYyActivity.this);
			    			  
			    			  text2.setText(jsa.getJSONObject(i).getString("extra"));
			    			  
			    			  text2.setGravity(Gravity.CENTER);
			    			  text2.setTextColor(Color.parseColor("#ffffff"));
			    			  text2.setTextSize(12);
			    			  text2.setLayoutParams(getContentLayoutParams());
			    			  //text2.setPadding(2, 2, 2, 2);
			    			  text2.setBackgroundColor(Color.parseColor("#CDC9C9"));
			    			  TextView	text3=new TextView(MyProfileYyActivity.this);
			    			  final String num=jsa.getJSONObject(i).getString("phonenum");
			    			  text3.setText(jsa.getJSONObject(i).getString("phonenum"));
			    			  text3.setLayoutParams(getContentLayoutParams());
			    			  //text3.setPadding(2, 2, 2, 2);
			    			  text3.setGravity(Gravity.CENTER);
			    			  text3.setTextColor(Color.parseColor("#ffffff"));
			    			  text3.setTextSize(12);
			    			  text3.setBackgroundColor(Color.parseColor("#CDC9C9"));
			    			  
                              TextView text4=new TextView(MyProfileYyActivity.this);
			    			  
			    			  text4.setText(jsa.getJSONObject(i).getString("time"));
			    			  
			    			  text4.setGravity(Gravity.CENTER);
			    			  text4.setTextColor(Color.parseColor("#ffffff"));
			    			  text4.setTextSize(12);
			    			  text4.setLayoutParams(getContentLayoutParams());
			    			  //text2.setPadding(2, 2, 2, 2);
			    			  text2.setBackgroundColor(Color.parseColor("#CDC9C9"));  
			    				tr.addView(text1);
			    				tr.addView(text2);
			    				tr.addView(text3);
			    				tr.addView(text4);
			    				tl.addView(tr);
			    				
			    			}
							
						} 
							catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
			    	}
			    }
			};
			Runnable runnable = new Runnable(){
			    
			   
			    InputStream is;
			    String strResult="123";
			    public void run() {
			        //
			    	 String url=BASE_URL+"/info/getinfo?get=yuyue&community="+community;
					HttpGet getMethod = new HttpGet(url);  
					
					if(isNetworkConnected(getApplicationContext())==false)
						//Toast.makeText(getApplicationContext(), "网络未连接", Toast.LENGTH_SHORT).show();
						{
						Message msg=new Message();
				        msg.what=1;
				        msg.obj="noNet";
				        handler.obtainMessage(1,"noNet").sendToTarget();
				        }
						
					//st=(TextView)findViewById(R.id.net1);
					//st.setText("sss");
					else
					try
					  {
					   //取得HttpClient对象
					   HttpClient httpclient = new DefaultHttpClient();
					   //请求HttpClient，取得HttpResponse
					   HttpResponse httpResponse = httpclient.execute(getMethod);
					   //请求成功
					   if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
					   {
					    //取得返回的字符串
					   strResult = EntityUtils.toString(httpResponse.getEntity(),"utf-8");
					    System.out.println(strResult+" aa");
					    Message msg=new Message();
					    msg.what=0;
					    msg.obj=strResult;
					   // msg.sendToTarget();
					    handler.obtainMessage(0,strResult).sendToTarget();
					   }
					   
					  }
					  catch (ClientProtocolException e)
					  {
					   ;
					  }
					  catch (IOException e)
					  {
					   ;
					  }
					  catch (Exception e)
					  {
					   ;
					  }  
			    }
			};

			Runnable runnable2 = new Runnable(){
			    
			    
				   
			    public void run() {
			        //
			    	String url=BASE_URL+"/user/getinfo?get=getuserinfo&userid="+userid;
					HttpGet getMethod = new HttpGet(url);  
					
					if(isNetworkConnected(getApplicationContext())==false)
						//Toast.makeText(getApplicationContext(), "网络未连接", Toast.LENGTH_SHORT).show();
						{
						Message msg=new Message();
				        msg.what=1;
				        msg.obj="noNet";
				        handler.obtainMessage(1,"noNet").sendToTarget();
				        }
						
					//st=(TextView)findViewById(R.id.net1);
					//st.setText("sss");
					else
					try
					  {
						
					   //取得HttpClient对象
					   HttpClient httpclient = new DefaultHttpClient();
					   //请求HttpClient，取得HttpResponse
					   HttpResponse httpResponse = httpclient.execute(getMethod);
					   //请求成功
					   if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
					   {
					    //取得返回的字符串
					  String strResult = EntityUtils.toString(httpResponse.getEntity(),"utf-8");
					    System.out.println(strResult+" bb");
					    Message msg=new Message();
					    msg.what=3;
					    msg.obj=strResult;
					   // msg.sendToTarget();
					    handler.obtainMessage(3,strResult).sendToTarget();
					   }
					   
					  }
					  catch (ClientProtocolException e)
					  {
					   ;
					  }
					  catch (IOException e)
					  {
					   ;
					  }
					  catch (Exception e)
					  {
					   ;
					  }  
			    }
			};
	
	
	
	public boolean isNetworkConnected(Context context) {  
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
	private TableRow.LayoutParams getContentLayoutParams(){
        TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        //为保证边框宽度均匀，唯有第一行需要topMargin，第一列需要leftMargin
        params.setMargins(2, 2, 2, 2);
        return params;
}
	
	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK) {  
        	
        	
        	MyProfileYyActivity.this.finish();
        	
            return true;   
        } else  
            return super.onKeyDown(keyCode, event);  
    }
}
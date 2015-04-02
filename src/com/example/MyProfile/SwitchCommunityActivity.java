package com.example.MyProfile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.SM;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.manage.HomePage;
import com.example.manage.R;


import com.example.tools.IP;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class SwitchCommunityActivity extends Activity{
	String community;
	CheckBox com1,com2,com3;
	String userid;
	private String BASE_URL=IP.ip+":3000";
	 private CornerListView cornerListView = null;  
	    private List<Map<String, String>> listData = null;  
	    private SimpleAdapter adapter = null;  
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.switchcommunity);
		 cornerListView = (CornerListView)findViewById(R.id.setting_list); 
		 SharedPreferences sm=getSharedPreferences("user", MODE_PRIVATE);
		 userid=sm.getString("userName", "123456");
		 System.out.println("insw");
	     new Thread(getcommunity).start();
	}
	
	final Handler handler = new Handler(){
	    @SuppressWarnings("deprecation")
		@Override
	    public void handleMessage(Message msg) {
         
	    	if(msg.what==1)
	    		Toast.makeText(getApplicationContext(), "网络未连接", Toast.LENGTH_SHORT).show();
	    	
	    	else if(msg.what==0)
    		{
	    		 SharedPreferences sm=getSharedPreferences("user", 
	    	        		Activity.MODE_PRIVATE);
	    		 SharedPreferences.Editor e=sm.edit();
	    		 e.putString("community", community);
	    		 e.commit();
    				Toast.makeText(getApplicationContext(), "小区已切换", Toast.LENGTH_SHORT).show();
    				startActivity(new Intent(getApplicationContext(),HomePage.class));
    		}
	    	else if(msg.what==2)
	    	{
	    		try {
					JSONObject com=new JSONObject(msg.obj.toString());
					System.out.println(com.toString());
					listData = new ArrayList<Map<String,String>>();  
					int i=0;
					while(com.has("com"+i))
					{
						Map<String,String> map=new HashMap<String, String>();
						map.put("text",com.getString("com"+i));
						listData.add(map);
						System.out.println(i);
						i++;
					}
					System.out.println("sss");
					
					 adapter = new SimpleAdapter(getApplicationContext(), listData, R.layout.switchcommunity_list_item ,  
				                new String[]{"text"}, new int[]{R.id.tv_system_title});  
				               cornerListView.setAdapter(adapter);  
				     cornerListView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO Auto-generated method stub
							community=listData.get(arg2).get("text");
							System.out.println(community);
							new Thread(postcom).start();
						}
					});          
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "获取小区列表失败", Toast.LENGTH_SHORT).show();
				}
	    		
	    	}
	    }
	};
Runnable getcommunity = new Runnable(){
	    
	    
	    public void run() {
	        //
	    	/*String	url=BASE_URL+"/getcommunity";
	    	HttpGet getMethod = new HttpGet(url);  
			
			if(isNetworkConnected(getApplicationContext())==false)
				//Toast.makeText(getApplicationContext(), "网络未连接", Toast.LENGTH_SHORT).show();
				{
				Message msg=new Message();
		        msg.what=1;
		        msg.obj="noNet";
		        handler.obtainMessage(1,"noNet").sendToTarget();
		        }
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
			    System.out.println(strResult+" aa");
			    Message msg=new Message();
			    msg.what=2;
			    msg.obj=strResult;
			   // msg.sendToTarget();
			    handler.obtainMessage(2,strResult).sendToTarget();
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
			  }  */
	    	JSONObject s=new JSONObject();
	    	try {
				s.put("com0", "xcm1");
				s.put("com1", "xcm2");
				s.put("com2", "第一小区");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	System.out.println(s.toString());
	    	handler.obtainMessage(2,s.toString()).sendToTarget();;
	    }
	};
Runnable postcom=new Runnable() {
		
		@Override
		public void run() {
	        //
	    	String url=BASE_URL+"/user/postinfo";
			HttpPost post = new HttpPost(url);  
			
			if(isNetworkConnected(getApplicationContext())==false)
				//Toast.makeText(getApplicationContext(), "网络未连接", Toast.LENGTH_SHORT).show();
				{
				Message msg=new Message();
		        msg.what=1;
		        msg.obj="noNet";
		        handler.obtainMessage(1,"noNet").sendToTarget();
		        }
				
			else
			try
			  {
				
			   //取得HttpClient对象
			   HttpClient httpclient = new DefaultHttpClient();
			  JSONObject s2= new JSONObject();
			  s2.put("userid", userid);
			  s2.put("community", community);
			  StringEntity se = new StringEntity(s2.toString(),"UTF-8");
			    se.setContentEncoding("UTF-8");
			    se.setContentType("application/json");
			    post.setEntity(se);
			    post.setHeader("Content-Type", "application/json; charset=utf-8");
			   //请求HttpClient，取得HttpResponse
			   HttpResponse httpResponse = httpclient.execute(post);
			   //请求成功
			   if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			   {
			    //取得返回的字符串
			  String strResult = EntityUtils.toString(httpResponse.getEntity(),"utf-8");
			    System.out.println(strResult+" bb");
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
	public boolean isNetworkConnected(Context context) {    //判断网络是否连接
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
		
	
	}

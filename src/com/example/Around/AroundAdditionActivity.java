package com.example.Around;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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

import com.example.tools.IP;
import com.example.tools.VerticalScrollTextView;
import com.example.logIn.Constants;
import com.example.manage.R;
import com.example.manage.R.drawable;
import com.example.manage.R.id;
import com.example.manage.R.layout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class AroundAdditionActivity extends Activity{
	private String BASE_URL=IP.ip+":3000/";
	
	protected String res;
	private String userid;
	private static String aroundclass="周边";
	private String community;
	ProgressDialog dialog;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aroundaddition);
		dialog=new ProgressDialog(this);
		dialog.setTitle("提示");
		dialog.setMessage("正在加载，请稍后...");
		TextView ac=(TextView)findViewById(R.id.aroundclass);
		ImageView bw=(ImageView)findViewById(R.id.backarrow);
		bw.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				//startActivity(new Intent(getApplicationContext(),AroundActivity.class));
				AroundAdditionActivity.this.finish();
			}
		});
		if(aroundclass.equals("food"))
		ac.setText("美食");
		else if(aroundclass.equals("entertainment"))
			ac.setText("娱乐");
		else if(aroundclass.equals("shop"))
			ac.setText("购物");
		else if(aroundclass.equals("hotal"))
			ac.setText("宾馆");
		else if(aroundclass.equals("extra"))
			ac.setText("其他");
		
		
		SharedPreferences mySharedPreferences = getSharedPreferences("user", Activity.MODE_PRIVATE); 
		
		
				
				//Toast.makeText(getApplicationContext(), BASE_URL, Toast.LENGTH_SHORT).show();
				
				SharedPreferences sharedPreferences = getSharedPreferences("user", 
		        		Activity.MODE_PRIVATE); 
				if(sharedPreferences.contains("userName"))
		        {
		        	userid=sharedPreferences.getString("userName", "");
		        	//Toast.makeText(getApplicationContext(), userid, Toast.LENGTH_SHORT).show();
		        }
		        else 
		        	Toast.makeText(getApplicationContext(), "哪里来的逗比，没有登录就进来，菊花不想要了吗！", Toast.LENGTH_SHORT).show();
		        if(sharedPreferences.contains("community"))
		        {
		        	community=sharedPreferences.getString("community", "");
		        	//Toast.makeText(getApplicationContext(), "存在"+community, Toast.LENGTH_SHORT).show();
		        	
		        }
		        else 
		        {
		        	Toast.makeText(getApplicationContext(), "小区信息缺失，正在获取", Toast.LENGTH_SHORT).show();
		            new Thread(runnable2).start();
		         
		        }
		        dialog.show();
		        
	    		new Thread(runnable).start();

		        
			
		
		   
		}
	public static void setclass(String sc)
	{
		aroundclass=sc;
	}
		 final Handler handler = new Handler(){
			    @SuppressWarnings("deprecation")
				@Override
			    public void handleMessage(Message msg) {
	
			    	if(msg.what==1)
			    		Toast.makeText(getApplicationContext(), "网络未连接", Toast.LENGTH_SHORT).show();
			    	else if(msg.what==2)
			    	{
			    		//Toast.makeText(getApplicationContext(), "news", Toast.LENGTH_SHORT).show();
			    		res=(String)msg.obj;
			    		String newscontent="";
			    		try { 
			    			System.out.println(res);
							JSONArray newsarray=new JSONArray(res);
							for(int i=0;i<newsarray.length();i++)
							{
							JSONObject newsobject=newsarray.getJSONObject(i);
							String ss=newsobject.getString("title")+":"+newsobject.getString("content");
							System.out.println("ss="+ss);
							newscontent=newscontent+'\n'+(i+1)+"."+ss;
				    		
				    		}
							VerticalScrollTextView v=new VerticalScrollTextView(AroundAdditionActivity.this);
				    		LinearLayout news=(LinearLayout)findViewById(R.id.news);
				    		
							LinearLayout.LayoutParams pm=new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
				    		pm.gravity=Gravity.CENTER;
				    		pm.setMargins(20, 20, 20, 20);
				    		v.setLayoutParams(pm);
				    		v.setTextSize(20);
				    		v.setText(newscontent);
				    		news.addView(v);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    		
			    		
			    	}
			    	else if(msg.what==3)
			    	{
			    		res=msg.obj.toString();
			    		try {
							JSONObject jso= new JSONObject(res);
							
							if(jso.has("community")==false)
								Toast.makeText(getApplicationContext(), "获取小区信息失败", Toast.LENGTH_SHORT).show();
							else {
								Toast.makeText(getApplicationContext(), community, Toast.LENGTH_SHORT);
							SharedPreferences sm=getSharedPreferences("user", MODE_PRIVATE);
							SharedPreferences.Editor editer=sm.edit();
							editer.putString("community", community);
							editer.commit();
							}} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    		
			    	}
			    	else if(msg.what==0)
			    	{
			    		System.out.println("chen");
			    		 res=msg.obj.toString();
			    		 System.out.println(res);
			    		 //Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
			    		 
			    		try {
							
							final JSONArray jsa=new JSONArray(res);
			    			for(int i=0;i<=jsa.length()-1;i++)
			    			{
			    				TableLayout tl = (TableLayout)findViewById(R.id.aroundtablelayout);
			    				final JSONObject jso=jsa.getJSONObject(i);
			    				String ver=jso.getString("verify");
			    				if(ver.equals("false"))
			    					continue;
			    				tl.setShrinkAllColumns(true);
			    				TableRow tr = new TableRow(AroundAdditionActivity.this);	
			    				tr.setBackgroundColor(Color.parseColor("#D4D4D4"));
			    				  TableRow.LayoutParams lp=new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			    				 lp.setMargins(1, 1, 1, 1);
			    				  tr.setLayoutParams(lp);
			    				// tr.setPadding(2, 2, 2, 2);
			    				
			    			  TextView	text1=new TextView(AroundAdditionActivity.this);
			    			  text1.setText(jsa.getJSONObject(i).getString("name"));
			    			  
			    			  text1.setGravity(Gravity.CENTER);
			    			  text1.setTextColor(Color.parseColor("#000000"));
			    			  text1.setTextSize(15);	
			    			  text1.setBackgroundColor(Color.parseColor("#ffffff"));
			    			  text1.setLayoutParams(getContentLayoutParams());
			    			  text1.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									Intent x= new Intent();
									x.setClass(getApplicationContext(), AroundInfoActivity.class);
									x.putExtra("json", jso.toString());
									startActivity(x);
								}
							});
			    			  //text1.setPadding(2, 2, 2, 2);
			    			  TextView	text2=new TextView(AroundAdditionActivity.this);
			    			  
			    			  text2.setText(jsa.getJSONObject(i).getString("addr"));
			    			  
			    			  text2.setGravity(Gravity.CENTER);
			    			  text2.setTextColor(Color.parseColor("#000000"));
			    			  text2.setTextSize(15);
			    			  text2.setLayoutParams(getContentLayoutParams());
			    			  //text2.setPadding(2, 2, 2, 2);
			    			  text2.setBackgroundColor(Color.parseColor("#ffffff"));
			    			  TextView	text3=new TextView(AroundAdditionActivity.this);
			    			  final String num=jsa.getJSONObject(i).getString("phone");
			    			  text3.setText(jsa.getJSONObject(i).getString("phone"));
			    			  text3.setLayoutParams(getContentLayoutParams());
			    			  //text3.setPadding(2, 2, 2, 2);
			    			  text3.setGravity(Gravity.CENTER);
			    			  text3.setTextColor(Color.parseColor("#000000"));
			    			  text3.setTextSize(15);
			    			  text3.setBackgroundColor(Color.parseColor("#ffffff"));
			    			  text3.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View arg0) {
										// TODO Auto-generated method stub
										Intent intent = new Intent();
									    intent.setAction("android.intent.action.DIAL");
									    intent.setData(Uri.parse("tel:"+num));
									    startActivity(intent);
									}
								});
			    				tr.addView(text1);
			    				tr.addView(text2);
			    				tr.addView(text3);
			    				tl.addView(tr);
			    				
			    			}
							dialog.dismiss();
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
			        String url=BASE_URL+"around/getaroundinfo?get="+aroundclass+"&community="+community;
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
			    	userid="123456";
			    	//Toast.makeText(getApplicationContext(), "获取用户信息", Toast.LENGTH_SHORT).show();
			    	String url=BASE_URL+":3000/user/getinfo?get=getuserinfo&userid="+userid;
			    	System.out.println("获取用户信息:"+url);
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
						System.out.println("121212121");
					   HttpClient httpclient = new DefaultHttpClient();
					   //请求HttpClient，取得HttpResponse
					   HttpResponse httpResponse = httpclient.execute(getMethod);
					   //请求成功
					   if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
					   {
					    //取得返回的字符串
					  String strResult = EntityUtils.toString(httpResponse.getEntity(),"utf-8");
					    System.out.println(strResult+" baab");
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
	private TableRow.LayoutParams getContentLayoutParams(){    //设置tablerow的属性的
        TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        //为保证边框宽度均匀，唯有第一行需要topMargin，第一列需要leftMargin
        params.setMargins(1, 1, 1, 1);
        return params;
}
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK) {  
        	
        	
        	AroundAdditionActivity.this.finish();
        	
            return true;   
        } else  
            return super.onKeyDown(keyCode, event);  
    }
	}



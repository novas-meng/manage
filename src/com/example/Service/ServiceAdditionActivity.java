package com.example.Service;

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

import com.example.Around.AroundAdditionActivity;
import com.example.logIn.Constants;
import com.example.manage.R;



import com.example.tools.IP;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ServiceAdditionActivity extends Activity{
	
	protected String res;
	private String BASE_URL=IP.ip+":3000/";
    private String color;
	private String userid;
	private String community;
	private static String serviceclass="管家服务";
	ProgressDialog dialog;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.serviceaddition);
		TextView ac=(TextView)findViewById(R.id.serviceclass);
		//Toast.makeText(getApplicationContext(), serviceclass, Toast.LENGTH_SHORT).show();
		dialog=new ProgressDialog(this);
		dialog.setTitle("提示");
		dialog.setMessage("正在加载，请稍后...");
		ImageView bw=(ImageView)findViewById(R.id.backarrow);
		bw.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				//startActivity(new Intent(getApplicationContext(),AroundActivity.class));
				ServiceAdditionActivity.this.finish();
			}
		});
		
		if(serviceclass.equals("busness"))
		{	ac.setText("商务服务");
		color="#EE3B3B";
		ac.setTextColor(Color.parseColor("#EE3B3B"));}
		else if(serviceclass.equals("rebuy"))
		{	ac.setText("物品回收");
		color="#CDAF95";
		ac.setTextColor(Color.parseColor("#CDAF95"));
		}
		else if(serviceclass.equals("carecar"))
		{	ac.setText("爱车服务");
		color="#40E0D0";
		ac.setTextColor(Color.parseColor("#40E0D0"));}
		else if(serviceclass.equals("health"))
		{	ac.setText("保健服务");
		color="#4F94CD";
		ac.setTextColor(Color.parseColor("#4F94CD"));}
		else if(serviceclass.equals("green"))
		{	ac.setText("绿化服务");
		color="#EE00EE";
		ac.setTextColor(Color.parseColor("#EE00EE"));}
		else if(serviceclass.equals("clean"))
			{ac.setText("保洁服务");
			color="#EE7942";
			ac.setTextColor(Color.parseColor("#EE7942"));
			}
		else if(serviceclass.equals("house"))
			{ac.setText("家政服务");
			color="#EEC900";
			ac.setTextColor(Color.parseColor("#EEC900"));
			}
		
		
		TableRow rk=(TableRow)findViewById(R.id.service_row);
		rk.setBackgroundColor(Color.parseColor(color));
				
				//Toast.makeText(getApplicationContext(), BASE_URL, Toast.LENGTH_SHORT).show();
				
				SharedPreferences sm = getSharedPreferences("user", 
		        		Activity.MODE_PRIVATE); 
				
		        	userid=sm.getString("userName", "");
		        	//Toast.makeText(getApplicationContext(), userid, Toast.LENGTH_SHORT).show();
		        
		        	community=sm.getString("community", "");
		        	//Toast.makeText(getApplicationContext(), userid+"|"+community, Toast.LENGTH_SHORT).show();
		        	
		        
		        dialog.show();
		        new Thread(runnable).start();
		       
		
	      
	      
		
		    
         
		
	}
	public static void setclass(String sc)
	{
		serviceclass=sc;
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
							community=jso.getString("community");
							if(community.equals(""))
								Toast.makeText(getApplicationContext(), "获取小区信息失败", Toast.LENGTH_SHORT).show();
							else {Toast.makeText(getApplicationContext(), community, Toast.LENGTH_SHORT).show();
							SharedPreferences sm=getSharedPreferences("user", MODE_PRIVATE);
							SharedPreferences.Editor editer=sm.edit();
							editer.putString("community", community);
							editer.commit();
							}
							} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    		
			    	}
			    	else if(msg.what==0)
			    	{
			    		System.out.println("chen");
			    		 res=(String)msg.obj;
			    		 System.out.println(res);
			    		 
			    		try {
			    			JSONArray jsa=new JSONArray(res);
			    			for(int i=0;i<jsa.length();i++)
			    			{
			    				TableLayout tl = (TableLayout)findViewById(R.id.servicelayout);
			    				
			    				tl.setShrinkAllColumns(true);
			    				TableRow tr = new TableRow(ServiceAdditionActivity.this);	
			    				tr.setBackgroundColor(Color.parseColor(color));
			    				  TableRow.LayoutParams lp=new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			    				 lp.setMargins(1, 1, 1, 1);
			    				  tr.setLayoutParams(lp);
			    				// tr.setPadding(2, 2, 2, 2);
			    				
			    			  TextView	text1=new TextView(ServiceAdditionActivity.this);
			    			  text1.setText(jsa.getJSONObject(i).getString("title"));
			    			 
			    			  text1.setGravity(Gravity.CENTER);
			    			  text1.setTextColor(Color.parseColor("#000000"));
			    			  text1.setTextSize(15);	
			    			  
			    			  text1.setBackgroundColor(Color.parseColor("#ffffff"));
			    			  text1.setLayoutParams(getContentLayoutParams(2));
			    			  
			    			  //text1.setPadding(2, 2, 2, 2);
			    			  TextView	text2=new TextView(ServiceAdditionActivity.this);
			    			  
			    			  text2.setText(jsa.getJSONObject(i).getString("money"));
			    			  
			    			  text2.setGravity(Gravity.CENTER);
			    			  text2.setTextColor(Color.parseColor("#000000"));
			    			  text2.setTextSize(15);
			    			  text2.setLayoutParams(getContentLayoutParams(3));
			    			  //text2.setPadding(2, 2, 2, 2);
			    			  text2.setBackgroundColor(Color.parseColor("#ffffff"));
			    			  TextView	text3=new TextView(ServiceAdditionActivity.this);
			    			  final String num=jsa.getJSONObject(i).getString("phone");
			    			  text3.setText(jsa.getJSONObject(i).getString("phone"));
			    			  text3.setLayoutParams(getContentLayoutParams(2));
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
			         String url=BASE_URL+"butler/butlerService?type="+serviceclass+"&community="+community;
					HttpGet getMethod = new HttpGet(url);  
					System.out.println(url);
					if(isNetworkConnected(getApplicationContext())==false)
						//Toast.makeText(getApplicationContext(), "����δ����", Toast.LENGTH_SHORT).show();
						{
						Message msg=new Message();
				        msg.what=1;
				        msg.obj="noNet";
				        handler.obtainMessage(1,"noNet").sendToTarget();
				        }
						
					else
					try
					  {
					   //ȡ��HttpClient����
					   HttpClient httpclient = new DefaultHttpClient();
					   //����HttpClient��ȡ��HttpResponse
					   HttpResponse httpResponse = httpclient.execute(getMethod);
					   //����ɹ�
					   if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
					   {
					    //ȡ�÷��ص��ַ���
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
			    	String url=BASE_URL+"user/getinfo?get=getuserinfo&userid="+userid;
					HttpGet getMethod = new HttpGet(url);  
					
					if(isNetworkConnected(getApplicationContext())==false)
						//Toast.makeText(getApplicationContext(), "����δ����", Toast.LENGTH_SHORT).show();
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
						
					   //ȡ��HttpClient����
					   HttpClient httpclient = new DefaultHttpClient();
					   //����HttpClient��ȡ��HttpResponse
					   HttpResponse httpResponse = httpclient.execute(getMethod);
					   //����ɹ�
					   if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
					   {
					    //ȡ�÷��ص��ַ���
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
	private TableRow.LayoutParams getContentLayoutParams(int weight){
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, LayoutParams.MATCH_PARENT);
        //Ϊ��֤�߿��Ⱦ��ȣ�Ψ�е�һ����ҪtopMargin����һ����ҪleftMargin
        params.weight=weight;
        params.setMargins(1, 1, 1, 1);
        return params;
}
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK) {  
        	
        	
        	ServiceAdditionActivity.this.finish();
        	
            return true;   
        } else  
            return super.onKeyDown(keyCode, event);  
    }
}


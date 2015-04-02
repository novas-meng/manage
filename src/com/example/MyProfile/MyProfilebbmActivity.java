package com.example.MyProfile;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.example.bbm.MyVoice;
import com.example.communitynews.DownLoadImage;
import com.example.communitynews.DownLoadImage.ImageCallback;
import com.example.manage.R;


import com.example.tools.IP;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MyProfilebbmActivity extends Activity{
	protected String res;
	private String BASE_URL=IP.ip+":3000";
	private String userid;
	MyAdapter adapter;
	ArrayList<Map<String, String>> listdata=null;
	private String community;
	EditText uriedit;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mybbmrecord);
		listdata=new ArrayList<Map<String, String>>();
		
				
				
		        adapter=new MyAdapter(this );
		       setuprecord();
		      
		    
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
			    	
			    }
			};
			private void  setuprecord(){
			    
			   listdata.clear();
			    InputStream is;
			    String strResult="123";
			    
			        //
			    	File bbmrecorddir=new File(Environment.getExternalStorageDirectory(),"E-homeland/bbm/sendMessage/");
					if(bbmrecorddir.exists())
					{
						final File[] files=bbmrecorddir.listFiles(new FileFilter()
				    	  {
							@Override
							public boolean accept(File arg0) {
								// TODO Auto-generated method stub
								return arg0.getName().endsWith(".bbm");
							}		  
				    	  });
				    	  for(File f:files)
				    	  {
				    		  try {
								FileInputStream fis=new FileInputStream(f);
								byte[] b;
								
								String type=f.getName().substring(0,1);
								System.out.println("type="+type);
								b=new byte[fis.available()];
								fis.read(b);
								String text=new String(b,0,b.length);
								System.out.println(type+"|"+text);
								Map<String, String>data= new HashMap<String, String>();
								data.put("type", type);
								data.put("choose", "false");
								String[] cont=text.split("##");
								System.out.println("0="+cont[0]);
								//System.out.println("1="+cont[1]);
								if(type.equals("1"))
								{data.put("content", "语音,播放请点击");
								
								  data.put("path", cont[0]);
								 
								  if(cont.length==2)
								  {
									  data.put("phone", cont[1]);
									  System.out.println("have phone");
								  }
								}
								else 
								{
									data.put("content",cont[0]);
									
									  if(cont.length==2)
									  {
										data.put("phone", cont[1]);
										  System.out.println("have phone");
									  }
								   
								}	
								
								listdata.add(data);
								}catch(IOException e){
				                     e.printStackTrace();
		                    	 }
								adapter.setData(listdata);
								ListView mv=(ListView)findViewById(R.id.myprofile_bbm_list);
								mv.setAdapter(adapter);
								adapter.notifyDataSetChanged();
								mv.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(
											AdapterView<?> arg0, View arg1,
											int arg2, long arg3) {
										// TODO Auto-generated method stub
										if(listdata.get(arg2).get("type").toString().equals("1"))
										{
											System.out.println(listdata.get(arg2).get("path").toString());
											MyVoice.startPlaying(listdata.get(arg2).get("path").toString());
											
										}
									}
								});
			                     }
			                     
				    	  final Button delete=(Button)findViewById(R.id.delete);
					       delete.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								// TODO 自动生成的方法存根
								ListView mv=(ListView)findViewById(R.id.myprofile_bbm_list);
								mv.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(AdapterView<?> arg0, View arg1,
											int arg2, long arg3) {
										// TODO 自动生成的方法存根
										if(listdata.get(arg2).get("choose").toString().equals("false"))
										{
										listdata.get(arg2).put("choose", "true");
										arg1.setBackgroundColor(Color.YELLOW);
										}
										else 
										{
											listdata.get(arg2).put("choose", "false");
											arg1.setBackgroundColor(Color.WHITE);
										}
									}
								});
								delete.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View arg0) {
										// TODO 自动生成的方法存根
										for(int i=0;i<listdata.size();i++)
										{
											System.out.println(i+"#"+listdata.get(i).get("choose"));
											if(listdata.get(i).get("choose").equals("true"))
											{
												files[i].delete();
												
											}
											
										}
										System.out.println(listdata.size());
										adapter.notifyDataSetChanged();
										setuprecord();
									}
								});
							}
						});
				    		  
					}
					
			    } 
			

			
	
	
	
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
	
	public class MyAdapter extends BaseAdapter{
		private Context context;
		private LayoutInflater layoutInflater;
		private List<Map<String,String>> list=null;
		public MyAdapter(Context context){
			this.context=context;
			layoutInflater=LayoutInflater.from(context);			
		}
		
		public void setData(List<Map<String,String>> list){
			this.list=list;
		}
		
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view=null;
			if(convertView==null){
				view=layoutInflater.inflate(R.layout.myprofile_bbm_list_item,null);				
			}else{
				view=convertView;
			}
			TextView type=(TextView)view.findViewById(R.id.bbmrecord_type);
			TextView content=(TextView)view.findViewById(R.id.bbmrecord_content);
			ImageView voice=(ImageView)view.findViewById(R.id.bbmrecord_voice);
			TextView phone=(TextView)view.findViewById(R.id.bbmrecord_phone);
			if(list.get(position).get("type").equals("0"))
			type.setText("类型:文字");
			else type.setText("类型:语音");
			if(list.get(position).get("type").equals("0"))
			content.setText("内容:"+list.get(position).get("content"));
			else 
			{	content.setVisibility(View.GONE);
			   voice.setVisibility(View.VISIBLE);
			}
			if(list.get(position).containsKey("phone"))
			phone.setText("电话:"+list.get(position).get("phone"));
			else phone.setText("");
			return view;
		}
		
		
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK) {  
        	
        	
        	MyProfilebbmActivity.this.finish();
        	
            return true;   
        } else  
            return super.onKeyDown(keyCode, event);  
    }
}

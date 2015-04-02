package com.example.MyProfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.manage.R;
import com.example.manage.listitem;









import com.example.tools.IP;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MyProfileXxfbActivity extends Activity{
	protected String res;
	private String BASE_URL=IP.ip+":3000";
	private String userid;
	private String community;
	private MyAdapter adapter=null;
	private  String getpath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/E-homeland/infopush";
	private ArrayList<Map<String, String>> listItem = new ArrayList<Map<String, String>>();
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myprofilexxfb);
	    adapter=new MyAdapter(this );
				setupinfo();
		        
		       
	}
	
		
			private void setupinfo()
			{
			      
			  
						File saveFile = new File(getpath, "infopush.ss");
						FileInputStream fin;
						try {
							fin = new FileInputStream(saveFile);
							int length = fin.available(); 

						    byte [] buffer = new byte[length]; 

						    fin.read(buffer);     

						    res = EncodingUtils.getString(buffer, "UTF-8"); 
 
						    fin.close();     
						    
						   System.out.println(res);
						    ArrayList<String> sTemp = new ArrayList<String>();
						    int a = -1;
						    while(res.length() > 0){
						    	a = res.indexOf("ß");
						    	sTemp.add(res.substring(0,a));
						    	res = res.substring(a+1,res.length());
						    }
						    for(int i = 0;i < sTemp.size();i+=5){
						    	HashMap<String, String> map = new HashMap<String, String>();   
						        map.put("type", sTemp.get(i));  
						        map.put("content",sTemp.get(i+1));  
						        map.put("price",sTemp.get(i+2));  
						        map.put("contact",sTemp.get(i+3));  
						        map.put("remark",sTemp.get(i+4));  
						        listItem.add(map);  
						        adapter.setData(listItem);
						        ListView mv=(ListView)findViewById(R.id.myprofile_xxfb_list);
						        mv.setAdapter(adapter);
						        adapter.notifyDataSetChanged();
						        		}
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					    
					    
					
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
				public View getView(final int position, View convertView, ViewGroup parent) {
					View view=null;
					if(convertView==null){
						view=layoutInflater.inflate(R.layout.myprofile_xxfb_list_item,null);				
					}else{
						view=convertView;
					}
					//TextView type=(TextView)view.findViewById(R.id.xxfbrecord_type);
					TextView content=(TextView)view.findViewById(R.id.xxfbrecord_content);
					TextView phone=(TextView)view.findViewById(R.id.xxfbrecord_contact);
					TextView price=(TextView)view.findViewById(R.id.xxfbrecord_price);
					TextView remark=(TextView)view.findViewById(R.id.xxfbrecord_remark);
					Button delete = (Button)view.findViewById(R.id.xxfb_delete);
					//type.setText("类型:"+list.get(position).get("type"));
					content.setText("内容:"+list.get(position).get("content"));
					phone.setText("电话:"+list.get(position).get("contact"));
					price.setText("价格:"+list.get(position).get("price"));
					remark.setText("备注:"+list.get(position).get("remark"));
					delete.setOnClickListener(new Button.OnClickListener(){

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							System.out.println("position"+position);
							listItem.remove(position);
							adapter.notifyDataSetChanged();
						}
						
					});
					return view;
				}
				
				
			}


			@Override
			public void onBackPressed() {
				// TODO Auto-generated method stub
				//存储修改后的信息
				String save = "";
				String temp = "";
				for(int i = 0;i < listItem.size();i++){
					temp = listItem.get(i).get("type")+"ß";
					temp += listItem.get(i).get("content")+"ß";
					temp += listItem.get(i).get("price")+"ß";
					temp += listItem.get(i).get("contact")+"ß";
					temp += listItem.get(i).get("remark")+"ß";
					save += temp;
				}
				System.out.println(save);
				creatAppDir();
				File saveFile = new File(Environment.getExternalStorageDirectory(), "E-homeLand/infopush/infopush.ss");
				if(!saveFile.exists())
					try {
						//saveFile.mkdirs();
						saveFile.createNewFile();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						System.out.println("create"+e1.toString());
					}
				
				try{ 
					
					//File saveFile = new File("E-homeland/infopush", "infopush.ss");
		        	FileOutputStream outStream = new FileOutputStream(saveFile);
		        	outStream.write(save.getBytes());
					outStream.close();
					

				    }catch(Exception e){ 

				          System.out.println("write"+e.toString()); 

				} 
				super.onBackPressed();
			}
			public void creatAppDir()
			{
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					System.out.println("创建infopush文件夹");
					File f=new File(Environment.getExternalStorageDirectory(),"E-homeland");
					if(!f.exists())
			      	f.mkdirs();
			      	File ff=new File(Environment.getExternalStorageDirectory(),"E-homeland/infopush");
			      	if(!ff.exists())
			      	ff.mkdirs();
				}	
			}
			Runnable runnable = new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost();
					 try {

						 List<NameValuePair> map = new ArrayList <NameValuePair>();
				         map.add(new BasicNameValuePair("json",""));
						httppost.setEntity(new UrlEncodedFormEntity(map));
						HttpResponse response = httpclient.execute(httppost);
						if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
							// 取得返回的字符串
							String back = EntityUtils.toString(response.getEntity());
						
							} else {
								String back = "返回失败";
								System.out.println(back);
							}
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}   
				}
				
			};
			@Override
			protected void onDestroy() {
				// TODO 自动生成的方法存根
				super.onDestroy();
			}
	

}

package com.example.infoPush;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bbm.bbmHomePage;
import com.example.manage.R;
import com.example.tools.IP;
import com.example.wu1.XListView;
import com.example.wu1.XListView.IXListViewListener;

@SuppressLint("SimpleDateFormat")
public class infoDetial extends Activity implements IXListViewListener{
	private ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>(); 
	static String type = "";
	String back = "";
	String estate = "";
	
	private SimpleAdapter listItemAdapter;
	private XListView listview;
	private Handler mHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.infodetial);
		  //给layout添加图片
        LinearLayout layout=(LinearLayout)findViewById(R.id.infodetaillayout1);
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		InputStream is = getResources().openRawResource(
				 R.drawable.bar_bg );
		Bitmap bm = BitmapFactory.decodeStream(is, null, opt);
		BitmapDrawable bd = new BitmapDrawable(getResources(), bm);
		layout.setBackgroundDrawable(bd);
		
		 LinearLayout layout1=(LinearLayout)findViewById(R.id.infodetaillayout2);
			is = getResources().openRawResource(
					 R.drawable.top_bg );
			 bm = BitmapFactory.decodeStream(is, null, opt);
			 bd = new BitmapDrawable(getResources(), bm);
			layout1.setBackgroundDrawable(bd);
		
			 LinearLayout layout2=(LinearLayout)findViewById(R.id.infodetaillayout3);
				is = getResources().openRawResource(
						 R.drawable.info_bg );
				 bm = BitmapFactory.decodeStream(is, null, opt);
				 bd = new BitmapDrawable(getResources(), bm);
				layout2.setBackgroundDrawable(bd);
			
		ImageView bbm=(ImageView)findViewById(R.id.infopush_ret);
		bbm.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO 自动生成的方法存根
					//startActivity(new Intent(getApplicationContext(),AroundActivity.class));
					infoDetial.this.finish();
				}
			});
		mHandler = new Handler();
		listview = (XListView)findViewById(R.id.infoDetialList);
		SharedPreferences mySharedPreferences = getSharedPreferences("user", Activity.MODE_PRIVATE);
		estate = mySharedPreferences.getString("community", "");
		TextView t=(TextView)findViewById(R.id.infoclass);
		if(type.equals("secondGood"))
		{
			t.setText("二手物品");
		}
		else if(type.equals("secondHouse"))
		{
			t.setText("二手房");
		}
		else if(type.equals("recruit"))
		{
			t.setText("招聘");
		}
		else if(type.equals("carPool"))
		{
			t.setText("拼车");
		}
		else if(type.equals("houseKeeping"))
		{
			t.setText("家政");
		}
		HashMap<String, Object> map = new HashMap<String, Object>();  
        map.put("xuhao", "序号");  
        map.put("content", "内容");  
        map.put("price", "价格");  
        map.put("contact", "联系方式");  
        map.put("remark", "备注");  
        listItem.add(map);  
        listItemAdapter = new SimpleAdapter(this,listItem, 
                R.layout.info_push_listitem, 
                new String[] {"xuhao","content", "price","contact","remark"},   
                new int[] {R.id.xuhao,R.id.neirong,R.id.jiage,R.id.lianxifangshi,R.id.beizhu}  
            );  
            listview.setAdapter(listItemAdapter); 
            listview.setPullRefreshEnable(true);
            listview.setXListViewListener(this);
		if(isWifiConnected()||isNetConnected())
			new Thread(runnable).start();
		else
			Toast.makeText(infoDetial.this, "网络没有连接", Toast.LENGTH_SHORT).show();
		
	}
	public static void setclass(String infotype) {
		type=infotype;
		
	}
	Runnable runnable = new Runnable(){
	    @Override
	    public void run() {
	        
	    	HttpClient client = new DefaultHttpClient();
			try {
				
				 HttpGet httpget = new HttpGet(IP.ip+":3000/user/getaroundinfo"+"?type="+type+"&community="+estate);     
				 System.out.println("type"+type);  
				HttpResponse response = client.execute(httpget);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					back = EntityUtils.toString(response.getEntity(),"UTF-8");
				//	System.out.println("返回数据"+back);
				
					} else {
						back = "[]";
						System.out.println(back);
					}			 
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Message msg = new Message();
			handler.sendMessage(msg);
	    		
	   	}
	};
	
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
    Handler handler = new Handler(){
	    @Override
	    public void handleMessage(Message msg) {
	        super.handleMessage(msg);
	        try{
		        if(!back.equals("[]")){
		        	
		        	listItem.clear();
		        	HashMap<String, Object> map1 = new HashMap<String, Object>();  
		            map1.put("xuhao", "序号");  
		            map1.put("content", "内容");  
		            map1.put("price", "价格");  
		            map1.put("contact", "联系方式");  
		            map1.put("remark", "备注");  
		            listItem.add(map1);  
					JSONArray jsonArray = new JSONArray(back);
					//System.out.println(jsonArray.toString());
					int num=0;
					 for(int i = 0; i < jsonArray.length();i++){
						 
						 JSONObject json = jsonArray.getJSONObject(i);
						 if(json.getString("verify").equals("false"))
							 continue;
						 num++;
						 HashMap<String, Object> map = new HashMap<String, Object>();  
					        map.put("xuhao", num+"");  
					        map.put("content", json.getString("content"));  
					        map.put("price", json.getString("price"));  
					        map.put("contact", json.getString("contact"));  
					        map.put("remark", json.getString("remark"));  
					        listItem.add(map); 
						 
					 }
				}
		        }catch(Exception e){
					System.out.println(e.toString());
				}
		        listItemAdapter.notifyDataSetChanged();
	     
	    }
	};
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if(isWifiConnected()||isNetConnected()){
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					HttpClient client = new DefaultHttpClient();
					try {
						
						 HttpGet httpget = new HttpGet(IP.ip+":3000/user/getaroundinfo"+"?type="+type+"&community="+estate);     
						 System.out.println("type"+type);  
						HttpResponse response = client.execute(httpget);
						if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
							back = EntityUtils.toString(response.getEntity(),"UTF-8");
							//System.out.println(back+"wuhao");
						
							} else {
								back = "[]";
								System.out.println(back);
							}
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try{
				        if(!back.equals("[]")){
				        	
				        	listItem.clear();
				        	HashMap<String, Object> map1 = new HashMap<String, Object>();  
				            map1.put("xuhao", "序号");  
				            map1.put("content", "内容");  
				            map1.put("price", "价格");  
				            map1.put("contact", "联系方式");  
				            map1.put("remark", "备注");  
				            listItem.add(map1);  
							JSONArray jsonArray = new JSONArray(back);	
							int num=0;
							 for(int i = 0; i < jsonArray.length();i++){
								 
								 JSONObject json = jsonArray.getJSONObject(i);
								 if(json.getString("verify").equals("false"))
								 {	 continue;
								 }
								 num++;
								 HashMap<String, Object> map = new HashMap<String, Object>();  
							        map.put("xuhao", num+"");  
							        map.put("content", json.getString("content"));  
							        map.put("price", json.getString("price"));  
							        map.put("contact", json.getString("contact"));  
							        map.put("remark", json.getString("remark"));  
							        listItem.add(map); 
								 
							 }
						}
				        }catch(Exception e){
							System.out.println(e.toString());
						}
				        listItemAdapter.notifyDataSetChanged();
					onLoad();
				}
			}, 2000);
		}else{
			Toast.makeText(infoDetial.this, "网络没有连接", Toast.LENGTH_SHORT).show();
		}	
	}
	private void onLoad() {
		SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("yyyy年MM月dd日    HH:mm:ss     ");     
		Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间     
		String    str    =    formatter.format(curDate);     
		listview.stopRefresh();
		listview.stopLoadMore();
		listview.setRefreshTime(str);
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK) {  
        	
        	setContentView(R.layout.null_view);
        	infoDetial.this.finish();
        	
            return true;   
        } else  
            return super.onKeyDown(keyCode, event);  
    }
}

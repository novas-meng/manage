package com.example.MyProfile;

import com.example.bbm.bbmHomePage;

import com.example.bbm.bbmMainActivity;
import com.example.logIn.Constants;
import com.example.logIn.logInActivity;
import com.example.manage.MainMenu;
import com.example.manage.R;
import com.example.manage.R.id;





import com.example.tools.IP;
import com.example.tsbx.MainActivity;
import com.example.user.UserRealInformationActivity;
import com.example.yy.YYMainActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import java.util.ArrayList;  
import java.util.HashMap;  
import java.util.List;  
import java.util.Map;  

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
  










import android.os.Bundle;  
import android.view.View;  
import android.widget.SimpleAdapter;  
  
public class MyProfileActivity extends Activity {  
  
    private CornerListView cornerListView1 = null;  
    private CornerListView cornerListView2 = null;  
    private List<Map<String, Object>> listData1 = null;  
    private List<Map<String, Object>> listData2 = null;  
    private SimpleAdapter adapter1 = null;  
    private SimpleAdapter adapter2 = null; 
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        // TODO Auto-generated method stub  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.myprofilelist);  
          
        cornerListView1 = (CornerListView)findViewById(R.id.setting_list1); 
        cornerListView2=(CornerListView)findViewById(R.id.setting_list2);
        setListData("");  
          
        adapter1 = new SimpleAdapter(getApplicationContext(), listData1, R.layout.myprofile_list_item ,  
                new String[]{"text","right"}, new int[]{R.id.tv_system_title,R.id.iv_system_right});  
                      
        cornerListView1.setAdapter(adapter1);  
        adapter2 = new SimpleAdapter(getApplicationContext(), listData2, R.layout.myprofile_list_item ,  
                new String[]{"text","right"}, new int[]{R.id.tv_system_title,R.id.iv_system_right});  
                        cornerListView2.setAdapter(adapter2);  
        new Thread(rm).start();
        ImageView update=(ImageView)findViewById(R.id.myprofile_update);
        update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				;
			}
		});
       cornerListView1.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			if(arg2==0)
			{
				startActivity(new Intent(getApplicationContext(),MyInfoActivity.class));
				
			}
			if(arg2==1)
			{
				startActivity(new Intent(getApplicationContext(),logInActivity.class));
			}
			if(arg2==2)
			{
				new Thread(rm).start();
			}
			
		}
	});   
       cornerListView2.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			if(arg2==0)
			{
				startActivity(new Intent(getApplicationContext(),MyProfilebbmActivity.class));
			}
			if(arg2==1)
			{
				
				startActivity(new Intent(getApplicationContext(),MyProfileTsbxActivity.class));
			}
			if(arg2==2)
			{
				startActivity(new Intent(getApplicationContext(),MyProfileXxfbActivity.class));
			}
			if(arg2==3)
			{
				Constants.type="YY";
		        new Thread(getButlerInfo).start();
			}
			if(arg2==4)
			{
				startActivity(new Intent(getApplicationContext(),FindPasswordActivity.class));
			}
			
		}
	});
       
        
    }  
  
      
    /**  
     * 设置列表数据  
     */  
    private void setListData(String coin){  
        listData1 = new ArrayList<Map<String,Object>>();  
        listData2 = new ArrayList<Map<String,Object>>();  
        Map<String,Object> map = new HashMap<String, Object>();  
        map.put("text", R.drawable.myprofile_info);  
        map.put("right", ">");
        listData1.add(map);  
   
        map = new HashMap<String, Object>();  
        map.put("text", R.drawable.myprofile_change);  
        map.put("right", ">");
        listData1.add(map);  
   
        map = new HashMap<String, Object>();  
        map.put("text", R.drawable.myprofile_eb);  
        map.put("right", coin);
        listData1.add(map);  
   
        map = new HashMap<String, Object>();  
        map.put("text", R.drawable.myprofile_bbm);  
        map.put("right", ">");
        listData2.add(map);  
   
        map = new HashMap<String, Object>();  
        map.put("text", R.drawable.myprofile_tsbxrecord);
        map.put("right", ">");
        listData2.add(map); 
        map = new HashMap<String, Object>();  
        map.put("text", R.drawable.myprofile_xxfb); 
        map.put("right", ">");
        listData2.add(map);
        map = new HashMap<String, Object>();  
        map.put("text", R.drawable.myprofile_yy);  
        map.put("right", ">");
        listData2.add(map);
        map = new HashMap<String, Object>();  
        map.put("text", R.drawable.myprofile_find);  
        map.put("right", ">");
        listData2.add(map);
        
    }  
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
    Handler handler = new Handler(){
	    @Override
	    public void handleMessage(Message msg) {
	       
	        
	          System.out.println(msg.obj.toString()+msg.what);
	          if(msg.what==0&&(!msg.obj.toString().equals("fail")))
	          {
	        	    setListData(msg.obj.toString());
	        	    adapter1=new SimpleAdapter(getApplicationContext(), listData1, R.layout.myprofile_list_item ,  
	                        new String[]{"text","right"}, new int[]{R.id.tv_system_title,R.id.iv_system_right});  
                    cornerListView1.setAdapter(adapter1);  
                    Constants.ecoin=msg.obj.toString();
	        	  
	          }
	    }
	    };
    Runnable rm=new Runnable() {
		
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			System.out.println("in rm");
			HashMap<String, String> params1 = new HashMap<String, String>();
			  params1.put("userid", Constants.username);
			 
			  String ul=IP.ip+":3000/getecoin";
			  try
			  {
				  com.example.yy.SocketHttpRequester.postString(ul, params1);
				   String  rq=com.example.yy.SocketHttpRequester.strResult;
				   System.out.println(rq);
				   Constants.ecoin=rq;
				   handler.obtainMessage(0, rq).sendToTarget();
			  }
			  catch(Exception e)
			  {
				  e.printStackTrace();
				   handler.obtainMessage(0,Constants.ecoin).sendToTarget();
			  }
			 
				
		}
	};
	Handler ButlerinfoHandler =new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(msg.what==1)
			{
				System.out.println("获取管家信息正确");
				String str=(String)msg.obj;
				try {
					JSONObject json=new JSONObject(str);
					String butlername=json.getString("id");
					String butlerphonenumber=json.getString("phonenumber");
					String butlerRealName=json.getString("realname");
					try
					{
						String butlerImg=json.getString("img");
						if(!butlerImg.equals("")&&!butlerImg.equals(" "))
						Constants.butlerImg=butlerImg;
						else
					    Constants.butlerHasImg=false;
					}
					catch(Exception e)
					{
						e.printStackTrace();
						System.out.println("管家没有上传图片");
						Constants.butlerHasImg=false;
					}
					Constants.butlername=butlername;
					Constants.butlerphonenumber=butlerphonenumber;
					System.out.println("Constant.butlername="+Constants.butlername);
					System.out.println("Constant.phonenummber="+Constants.butlerphonenumber);
					Intent intent=new Intent(getApplicationContext(),UserRealInformationActivity.class);
				    Bundle bundle=new Bundle();
				    bundle.putString("type",Constants.type);
				    intent.putExtras(bundle);
					startActivity(intent);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				Toast.makeText(getApplicationContext(),"请检查一下网络连接",Toast.LENGTH_SHORT).show();
				System.out.println("获取管家信息错误");
			}
		}
	};
	//获取管家的信息
   Runnable getButlerInfo=new Runnable()
   {

	@Override
	public void run() {
		System.out.println("获取管家信息");
		// TODO Auto-generated method stub
		//String httpUrl =IP.ip+":3000/user/getbutler?community="+Constants.Community; 
		System.out.println("Constants.Community="+Constants.Community);
		String httpUrl=IP.ip+":3000/user/getbutler?community="+Constants.Community;
		HttpGet getMethod = new HttpGet(httpUrl);  
		try {
	    HttpClient httpclient = new DefaultHttpClient(); 
	    
	    HttpResponse httpResponse=null;
	    httpResponse = httpclient.execute(getMethod);
	    String strResult = null;
	    if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)  
			{                
	    	System.out.println("成功从服务器获取第一小区");
			  strResult = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
			  System.out.println("从服务器获取到的管家信息为"+strResult);
			  ButlerinfoHandler.obtainMessage(1,strResult).sendToTarget();
		 	}
		 }
		catch (Exception e) 
		 {
		 	e.printStackTrace();
		 	ButlerinfoHandler.obtainMessage(0).sendToTarget();
		 }  
	}  
   };
   
}  
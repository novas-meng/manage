package com.example.yy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import com.example.bbm.bbmConstants;
import com.example.manage.R;
import com.example.tools.CurrentUser;
import com.example.tools.IP;
import com.example.wu1.AutoListView;
import com.example.wu1.AutoListView.OnLoadListener;
import com.example.wu1.AutoListView.OnRefreshListener;
import com.example.wu1.XListView;
import com.example.wu1.XListView.IXListViewListener;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Settings.Secure;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
 public class YYRefreshActivity extends Activity implements IXListViewListener
 {
   private XListView listview;
   private ArrayList<yyInformationItem> list=new ArrayList<yyInformationItem>();
   private yyInformationAdapter adapter;
   private EditText timeEdit;
   private EditText monthEdit;
   private EditText dayEdit;
   private EditText yearEdit;
   String sendText="";
   private String moneyType;
   //String YYMessageUrl=Constants.serverIp+"/appoint/send";
   String YYMessageUrl=IP.ip+":3000/appoint/send";
   int SEND_ERROR=0;
   int SEND_SUCCESS=1;
   int type=0;
   private String mDeviceID;
   private String MannagerContent;
   static  boolean isConnected;
   //Button addMsgButton;
   static Handler addMsgHandler;
   String address;
   String year;
   SharedPreferences mySharedPreferences ;
   private static final String[] costs={"物业费","车位费","电梯费","供暖费","电费","水费","燃气费","宽带通讯费"};
    @SuppressLint("NewApi") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yy_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
	    mySharedPreferences = getSharedPreferences("user", Activity.MODE_PRIVATE);
        address=mySharedPreferences.getString("address","");
        System.out.println("预约用户地址为"+address);
      //  year=Utils.getNowDate().split("-")[0];
        addMsgHandler=new Handler()
        {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if(msg.what==1)
				{
					type=1;
					setLayout(type); 
				}
			}
        	
        };
        /*
       // addMsgButton=(Button)findViewById(R.id.yy_addmessage);
        addMsgButton.setOnClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				type=1;
				setLayout(type); 
			}       	
        });
        */
        mDeviceID = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);            
        listview=(XListView)findViewById(R.id.yy_listview);
        Intent intent=this.getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null)
        {
        	   if(bundle.size()==2)
               {
               	//type=Integer.valueOf(bundle.getString("type"));
           		type=0;
               	MannagerContent=bundle.getString("content");    	
               }
              // else
               //{
               	//type=1;
               //}
        }
        //else
        //{
        	//type=1;
        //}
      //  String str=getMessageType();
       // System.out.println("接收到的信息为"+str);
        new Thread(getMessageRunnable).start();
       // Toast.makeText(MainActivity.this,str,Toast.LENGTH_SHORT).show();
    //    type=1;
        adapter=new yyInformationAdapter(list,YYRefreshActivity.this,new SpinnerSelectedListener());
		listview.setPullLoadEnable(true);
		listview.setXListViewListener(this);
        listview.setAdapter(adapter);  

       
        //setLayout(type);  
        isConnected=NetStateCheck.isNetConnected(YYRefreshActivity.this);
       // if(isConnected)
       //  startService(); 
      //  {
        //	Handler handler=new Handler();
        	//handler.post(runable);
       // }
      //   else
        // {
        	//Toast.makeText(YYMainActivity.this,"网络没有连接上",Toast.LENGTH_SHORT).show();
         //}
        }
    
    Runnable getMessageRunnable =new Runnable()
    {
    	public void run()
    	{
    		 String str=getMessageType(ButlerYYHandler);
    		// if(str.equals("[]"))
    		// {
    		//	 setLayout(1);
    		 //}
    		// System.out.println("获的的消息为"+str);
    	}
    };
    //获取管家的预约消息handler
  	Handler ButlerYYHandler=new Handler()
  	{
  		@Override
  		public void handleMessage(Message msg) {
  			// TODO Auto-generated method stub
  			if(msg.what==-1)
  			{
  				type=1;
  				setLayout(type);
  				Toast.makeText(YYRefreshActivity.this,"获取用户列表失败，请检查一下网络连接状态",Toast.LENGTH_SHORT).show();
  			}
  			else
  			{
  				String userList=(String)msg.obj;
  				System.out.println("从服务器获取到的用预约信息为"+userList);
  				if(userList.equals("[]"))
  				{
  					
  	    			 setLayout(1);
  				}
  				else
  				{
  					try {
  	  					JSONArray jsa=new JSONArray(userList);
  	  					for(int i=0;i<jsa.length();i++)
  	  	    			{		
  	  	    			  String username=jsa.getJSONObject(i).getString("user");
  	  	    			  String money=jsa.getJSONObject(i).getString("money");
  	  	    			  String content=jsa.getJSONObject(i).getString("content");
  	  	    			  String id=jsa.getJSONObject(i).getString("_id");

  	  	    			  System.out.println("username="+username+"content="+content+"money="+money);
  	  	    			  int type=Integer.valueOf(content.substring(5, 6));
  	  	    			  content=content.substring(6);
  	  	    			  yyInformationItem item=new yyInformationItem();
  	  	    			  item.setName(com.example.logIn.Constants.username);
  	  	    			  item.setBaseInformation1(content);
  	  	    			  item.setType(type);
  	  	    			  item.setId(id);

  	  	    			  list.add(item);
  	  	    			}			
  	  	    		  adapter.notifyDataSetChanged();
  	  				} catch (JSONException e) {
  	  					// TODO Auto-generated catch block
  	  					e.printStackTrace();
  	  				}				
  				} 				
  			}
  		}	
  	};
  	//启动mqtt服务器
    public void startService()
    {
    	 Thread mqttThread=new Thread(runable);
    	 try
    	 {
             mqttThread.start();
    	 }
    	 catch(Exception e)
    	 {
    		 e.printStackTrace();
    	 }
    }
    //启动mqtt服务器的runnable
    Runnable runable=new Runnable()
    {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			  Editor editor = getSharedPreferences(PushService.TAG, MODE_PRIVATE).edit();
		      editor.putString(PushService.PREF_DEVICE_ID, mDeviceID);
		     editor.commit();
			PushService.actionStart(getApplicationContext());	
		}
    };
    public void OnResponse(View v)
    {
    	System.out.println("在OnResponse");
    	
    	if(v.getId()==R.id.yy_guanjia_response)
    	{
    		  yyInformationItem item=new yyInformationItem();
  	        item.setType(1);
  	      
  	        item.setName(com.example.logIn.Constants.butlername+"管家");
  	        item.setBaseInformation1("我是"+com.example.logIn.Constants.Community+
  	        		address+"的业主，请你于");
  	      //  item.setBaseInformation2("上门收取"+year+"年度");
  	        item.setBaseInformation2("");
  	        list.add(item);
  	        adapter.notifyDataSetChanged();
    	}
    	
    }
    
    //根据服务器返回的type值确定具体布局
    public void setLayout(int type)
    {
    	//这个是管家主动发来的预约消息
    	if(type==0)
    	{
    		yyInformationItem item=new yyInformationItem();
    		item.setType(0);
 	        System.out.println("当前用户名为" +com.example.logIn.Constants.username);

    		item.setName(com.example.logIn.Constants.username);
    		//item.setBaseInformation1("您位于阳光小区A栋3区的住宅，2015年应该缴纳的物业费为1200元，我们可以去您家里" +
    				//"刷卡进行缴费，你可以预约_年_月_日");
    		item.setBaseInformation1(MannagerContent);
    		list.add(item);
    		adapter.notifyDataSetChanged();
    	}
    	//这个是用户主动去预约管家
    	if(type==1)
    	{
    		  yyInformationItem item=new yyInformationItem();
    	        item.setType(1);
     	        System.out.println("当前用户名为" +com.example.logIn.Constants.username);

    	        item.setName(com.example.logIn.Constants.butlername);
    	        item.setBaseInformation1("我是"+com.example.logIn.Constants.Community+address+"的业主，请你于");
    	       // item.setBaseInformation2("收取"+year+"年度");
    	        item.setBaseInformation2("");
    	        list.add(item);
    	        adapter.notifyDataSetChanged();
    	}
    	//这个是获取到的管家的预约成功的消息
    	if(type==2)
    	{
    		 yyInformationItem item=new yyInformationItem();
 	        item.setType(2);
 	        System.out.println("当前用户名为" +com.example.logIn.Constants.username);
 	        item.setName(com.example.logIn.Constants.username);
 	       // item.setBaseInformation1("您的预约已经成功，我们将于——月_日_时去您家收取物业费1000元，收费员为李明，他将与您联系");
 	       item.setBaseInformation1(MannagerContent);
 	        list.add(item);
 	        adapter.notifyDataSetChanged();
    	}
    }
    //从服务器端获取是否有管家发来的信息，如果没有管家发来的信息，就由用户去发，否则就先显示管家信息，然后用户去回复
    public String getMessageType(Handler handler)
    {
    	String strResult = null;
    	System.out.println("当前用户为"+CurrentUser.username);
		String httpUrl = IP.ip+":3000/appoint/appoint_manage?type=owner&user="+CurrentUser.username+"&community=第一小区";  
		HttpGet getMethod = new HttpGet(httpUrl);  
		try {
	    HttpClient httpclient = new DefaultHttpClient();  
	    HttpResponse httpResponse=null;
	    httpResponse = httpclient.execute(getMethod);
	  
	    if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)  
			{                 	
			  strResult = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
			 // ButlerYYHandler.obtainMessage(1,strResult).sendToTarget();
		      handler.obtainMessage(1,strResult).sendToTarget();
		 	}
		 }
		catch (Exception e) 
		 {
		 	e.printStackTrace();
		 	//ButlerYYHandler.obtainMessage(0).sendToTarget();
		 	handler.obtainMessage(-1).sendToTarget();
		 }  
    return strResult;
    }
    Runnable sendMessageRunnable =new Runnable()
    {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			year=yearEdit.getText().toString();
			sendText=com.example.logIn.Constants.butlername+"我是"+com.example.logIn.Constants.Community+address+"的业主，请你于"
			+monthEdit.getText().toString()+"月"+dayEdit.getText()
					.toString()+"日"+timeEdit.getText().toString()+"时上门收取"+year+"年度"+moneyType;
		//	sendText="mengfanshan";
			HashMap<String,String> map=new HashMap<String,String>();
            map.put("user",com.example.logIn.Constants.username);
            map.put("type","物业费");
            map.put("kind","owner");
            map.put("content", sendText);
            map.put("community",com.example.logIn.Constants.Community);
            try
            {
                SocketHttpRequester.postString(YYMessageUrl,map);	
                messagehandler.obtainMessage(SEND_SUCCESS).sendToTarget();
            }
            catch(Exception e)
            {
            	e.printStackTrace();
            	messagehandler.obtainMessage(SEND_ERROR).sendToTarget();
            }
		}    	
    };
    Handler messagehandler=new Handler()
    {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(msg.what==SEND_ERROR)
			{
            	Toast.makeText(YYRefreshActivity.this,"预约消息发送失败",Toast.LENGTH_SHORT).show();
			}
			else
			{
			//	YYDataStore.saveYYMessage(0,sendText);
            	Toast.makeText(YYRefreshActivity.this,"预约消息发送成功",Toast.LENGTH_SHORT).show();
			}
		}    	
    };
    public void sendYYMessage(View v)
    {
    	if(v.getId()==R.id.yy_send&&check())
    	//if(v.getId()==R.id.yy_send)
    	{
        	System.out.println("发送预约消息");
    	   new Thread(sendMessageRunnable).start();
    	}
    }
    public boolean check()
    {
    	  monthEdit=(EditText)findViewById(R.id.yy_yezhu_edittext1);
          dayEdit=(EditText)findViewById(R.id.yy_yezhu_edittext2);
          timeEdit=(EditText)findViewById(R.id.yy_yezhu_edittext3);
          yearEdit=(EditText)findViewById(R.id.yy_yezhu_edittext4);
          System.out.println("月"+monthEdit.getText().toString());
          System.out.println("日"+dayEdit.getText().toString());
          System.out.println("时"+timeEdit.getText().toString());
    	if(yearEdit.getText().toString().equals("")||timeEdit.getText().toString().equals("")||monthEdit.getText().toString().equals("")||dayEdit.getText().toString().equals(""))
    	{
    		System.out.println("信息添加不完整");
    		Toast.makeText(YYRefreshActivity.this,"您的日期还没有添加完整",Toast.LENGTH_SHORT).show();
    		return false;
    	}
    	int month=0;
    	int day=0;
    	int time=0;
    	try
    	{
    	 month=Integer.valueOf(monthEdit.getText().toString());
         day=Integer.valueOf(dayEdit.getText().toString());
         time=Integer.valueOf(timeEdit.getText().toString());
    	}
        catch(Exception e)
        {
        	Toast.makeText(YYRefreshActivity.this,"输入的日期格式不对哦",Toast.LENGTH_SHORT).show();
        }
    	if(month>12||month<1||day<1||day>31||time<1||time>24)
    	{
    		Toast.makeText(YYRefreshActivity.this,"您的日期添加的格式错误",Toast.LENGTH_SHORT).show();
    	}
    	return true;
    }
    class SpinnerSelectedListener implements OnItemSelectedListener{
   	 
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {
          //  view.setText("你的血型是："+m[arg2]);
        	System.out.println("点击了"+costs[arg2]);
        	moneyType=costs[arg2];
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
    //用于刷新内容的handler
    Handler refreshHandler=new Handler()
    {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(msg.what==-1)
			{
			    Toast.makeText(YYRefreshActivity.this,"获取刷新内容失败，请检查一下网络设置",Toast.LENGTH_SHORT).show();
			}
			else
			{
				list.clear();
				String userList=(String)msg.obj;
  				System.out.println("从服务器获取到的用预约信息为"+userList);
  				if(userList.equals("[]"))
  				{
  					
  	    			 setLayout(1);
  				}
  				else
  				{
  					try {
  	  					JSONArray jsa=new JSONArray(userList);
  	  					for(int i=0;i<jsa.length();i++)
  	  	    			{		
  	  	    			  String username=jsa.getJSONObject(i).getString("user");
  	  	    			  String money=jsa.getJSONObject(i).getString("money");
  	  	    			  String content=jsa.getJSONObject(i).getString("content");
  	  	    			  String id=jsa.getJSONObject(i).getString("_id");
  	  	    			  System.out.println("username="+username+"content="+content+"money="+money);
  	  	    			  int type=Integer.valueOf(content.substring(5, 6));
  	  	    			  content=content.substring(6);
  	  	    			  yyInformationItem item=new yyInformationItem();
  	  	    			  item.setName(com.example.logIn.Constants.username);
  	  	    			  item.setBaseInformation1(content);
  	  	    			  item.setType(type);
  	  	    			  item.setId(id);
  	  	    			  list.add(item);
  	  	    			}			
  	  					
  	  	    		  adapter.notifyDataSetChanged();
  	  				} catch (JSONException e) {
  	  					// TODO Auto-generated catch block
  	  					e.printStackTrace();
  	  				}				
  				} 				
			}
		}
    };
    public void loadData(final int what)
    {
    	listview.stopRefresh();
		listview.stopLoadMore();
		listview.setRefreshTime(Utils.getNowDate());
    	getMessageType(refreshHandler);
    }
    
	

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		System.out.println("刷新内容");
		loadData(AutoListView.REFRESH);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}
	 //删除信息的handler
    Handler delInformationHandler=new Handler()
   {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(msg.what==1)
			{
				System.out.println("删除预约消息成功");
				Toast.makeText(YYRefreshActivity.this.getApplicationContext(),"删除预约消息成功",Toast.LENGTH_SHORT).show();
			}
			else
			{
				System.out.println("删除预约消息失败");
				Toast.makeText(YYRefreshActivity.this,"删除预约消息失败，请检查一下网络设置",Toast.LENGTH_SHORT).show();
			}
		} 
   };
  //删除信息的runnable
   Runnable delInformationRunnable=new Runnable()
  {
  	public void run()
  	{
  		HashMap<String,String> map=new HashMap<String,String>();
  		System.out.println("id="+Constants.id);
          map.put("_id",Constants.id);
          try
          {
      		SocketHttpRequester.postString(IP.ip+":3000/appoint/del", map);
      		delInformationHandler.obtainMessage(1).sendToTarget();       		
          }
          catch(Exception e)
          {
          	e.printStackTrace();
          	delInformationHandler.obtainMessage(0).sendToTarget();
          } 		
  	}
  };
  //删除预约消息
  public void delInformation(View v)
  {
  	if(v.getId()==R.id.yy_del)
  	{
  		String[] str=v.getContentDescription().toString().split("&&");
  		String id=str[2];
  		int position=Integer.parseInt(str[1]);
  		String del=str[0];
  		list.remove(position);
  		adapter.notifyDataSetChanged();
  		System.out.println("id="+id+"position="+position+"del="+del);
  		Constants.id=id;
  		if(del.equals("del"))
  		new Thread(delInformationRunnable).start();
  	}
  }
 
}

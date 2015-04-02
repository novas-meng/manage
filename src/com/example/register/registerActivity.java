package com.example.register;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.cloopen.rest.sdk.CCPRestSmsSDK;
import com.example.infoPush.infoDetial;
import com.example.logIn.logInActivity;
import com.example.manage.HomePage;
import com.example.manage.MainMenu;
import com.example.manage.R;
import com.example.tools.IP;
import com.example.tools.Utils;
import com.example.utils.Constants;
import com.example.welcome.welcomeActivity;
import com.example.wu1.SocketHttpRequester;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Settings.Secure;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class registerActivity extends Activity{
	private JSONObject obj = new JSONObject(); 
	private TextView registerTV;
	private TextView authcode;
	private EditText userNameEdit,userPassword,phoneNumber,userPasswordConfirm;
	private EditText CAPTCHA;
	private String val = "";
	private EditText dong,danyuan,hao;
	private Spinner houseEstate;
	private String mDeviceID;
	private boolean hasSend=false;
	HashMap<String, String> params = new HashMap<String, String>();   
	SharedPreferences userpreferences;
	ArrayList<String> allEstate = new ArrayList<String>();
	ArrayAdapter<String> estateAdapter;
	String code;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity);
		Button bbm=(Button)findViewById(R.id.reg_ret_button);
		bbm.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO 自动生成的方法存根
					//startActivity(new Intent(getApplicationContext(),AroundActivity.class));
					registerActivity.this.finish();
				}
			});
		new Thread(communitylist).start();
		 if (android.os.Build.VERSION.SDK_INT > 9) {
	            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	            StrictMode.setThreadPolicy(policy);
	        }
		userpreferences=getSharedPreferences("user",Activity.MODE_PRIVATE); 
		mDeviceID = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID); 
		//获取控件
		CAPTCHA = (EditText)findViewById(R.id.CAPTCHA);
		//CAPTCHA.setText("1111");
		houseEstate = (Spinner)findViewById(R.id.houseEstate);
		dong = (EditText)findViewById(R.id.dong);
		dong.setText("");
		danyuan = (EditText)findViewById(R.id.danyuan);
		danyuan.setText("");
		hao = (EditText)findViewById(R.id.hao);
		hao.setText("");
		userNameEdit = (EditText)findViewById(R.id.userNameEdit);
		userPassword = (EditText)findViewById(R.id.userPassword);
		userPasswordConfirm = (EditText)findViewById(R.id.userPasswordConfirm);
		phoneNumber = (EditText)findViewById(R.id.phoneNumber);
		registerTV = (TextView)findViewById(R.id.register);
		//添加下拉列表
		String []estate = {};
		for(int i = 0;i < estate.length;i++){
			allEstate.add(estate[i]);
		}
	    estateAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,allEstate);
		estateAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		houseEstate.setAdapter(estateAdapter);
		String []number = {"1","2","3"};
		ArrayList<String> allNumber = new ArrayList<String>();
		for(int i = 0;i < number.length;i++){
			allNumber.add(number[i]);
		}
		TextView read=(TextView)findViewById(R.id.reg_wenzi);
		read.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(registerActivity.this,readActivity.class);
				startActivity(intent);
			}
			 
		});
		ArrayAdapter<String> numberAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,allNumber);
		numberAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		//添加相应事件
		registerTV.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String passWord,passWordConfirm,name,phone;
				name = userNameEdit.getText().toString();
				passWord = userPassword.getText().toString();
				passWordConfirm = userPasswordConfirm.getText().toString();
				phone = phoneNumber.getText().toString();
				System.out.println(passWord+"password");
				System.out.println(passWordConfirm+"passwordconfirm");
				if(!isNetConnected()&&!isWifiConnected()){
					Toast.makeText(registerActivity.this,"网络没有连接",Toast.LENGTH_SHORT).show();
				}else if(userNameEdit.getText().toString().length()<3||userNameEdit.getText().toString().length()>10){
					Toast.makeText(registerActivity.this,"用户名不得小于3位或多于10位",Toast.LENGTH_LONG).show();
				}else if(!passWord.equals(passWordConfirm)){
					Toast.makeText(registerActivity.this,"两次密码不一致",Toast.LENGTH_LONG).show();
				}
				else if(passWord.equals(passWordConfirm)){
					if(passWord.length()<6){
						Toast.makeText(registerActivity.this,"密码长度不得小于6位",Toast.LENGTH_LONG).show();
					}else if(phoneNumber.getText().toString().length()!=11&&!checkPhone())
					{
						Toast.makeText(registerActivity.this,"请输入电话号码",Toast.LENGTH_SHORT).show();
					}
					else if(!CAPTCHA.getText().toString().equals(code))
					{
						Toast.makeText(registerActivity.this,"验证码输入错误",Toast.LENGTH_SHORT).show();
					}
					else{
						try {
							obj.put("name", userNameEdit.getText().toString());
							obj.put("password", getMD5Str(userNameEdit.getText().toString()+userPassword.getText().toString()));
							obj.put("community", houseEstate.getSelectedItem().toString());
							obj.put("phone", phoneNumber.getText().toString());
							obj.put("address", dong.getText().toString()+"-"+danyuan.getText().toString()+"-"
																	+hao.getText().toString());
							
							//obj.put("IMDE", mDeviceID);
							System.out.println(obj.toString());
							userpreferences.edit().putString("userName", userNameEdit.getText().toString()).commit();
							userpreferences.edit().putString("password", getMD5Str(userNameEdit.getText().toString()+userPassword.getText().toString())).commit();
							userpreferences.edit().putString("community", houseEstate.getSelectedItem().toString()).commit();
							userpreferences.edit().putString("phone", phoneNumber.getText().toString()).commit();
							userpreferences.edit().putString("address", dong.getText().toString()+"栋-"+danyuan.getText().toString()+"单元-"
																	+hao.getText().toString()+"号").commit();
                            if(dong.getText().toString().equals("")||danyuan.getText().toString().equals("")||hao.getText().toString().equals(""))
                            {
                            	userpreferences.edit().putString("realinformation","false").commit();
                            	System.out.println("注册信息没有添加完整");
                            }
                            else
                            {
                            	userpreferences.edit().putString("realinformation","true").commit();
                            	System.out.println("注册信息添加完整");
                            }
							params.put("name", userNameEdit.getText().toString());
							params.put("password", getMD5Str(userNameEdit.getText().toString()+userPassword.getText().toString()));
							
							params.put("community", houseEstate.getSelectedItem().toString());
							params.put("phone", phoneNumber.getText().toString());
							params.put("address", dong.getText().toString()+"-"+danyuan.getText().toString()+"-"
																	+hao.getText().toString());
							obj.put("imei", mDeviceID);
							System.out.println(params.toString());
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							System.out.println(e.toString());
						}   
						new Thread(runnable).start();
					}	
				}
				
			}
			
		});
		authcode=(TextView)findViewById(R.id.reg_sendMessage);
		authcode.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(hasSend)
				{
					Toast.makeText(registerActivity.this,"您已经发送过验证码了，请15秒后重试",Toast.LENGTH_SHORT).show();
					
				}
				if(!hasSend)
				{
					if(phoneNumber.getText().length()!=11)
					{
						System.out.println("手机号码输入错误");
					}
					else
					{
						Toast.makeText(registerActivity.this,"已经发送验证码，请注意查看",Toast.LENGTH_SHORT).show();
						System.out.println("发送验证码");
						hasSend=true;
						Timer timer = new Timer();//timer中有一个线程,这个线程不断执行task
						   TimerTask task = new TimerTask() { //timertask实现runnable接口,TimerTask类就代表一个在指定时间内执行的task
						   @Override
						   public void run() {
						     hasSend=false;
						   }
						  };
						   timer.schedule(task, 1000 * 15);
						HashMap<String, Object> result = null;

						//初始化SDK
						CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
						
						//******************************注释*********************************************
						//*初始化服务器地址和端口                                                       *
						//*沙盒环境（用于应用开发调试）：restAPI.init("sandboxapp.cloopen.com", "8883");*
						//*生产环境（用户应用上线使用）：restAPI.init("app.cloopen.com", "8883");       *
						//*******************************************************************************
					//	restAPI.init("sandboxapp.cloopen.com", "8883");
						restAPI.init("app.cloopen.com", "8883"); 
						//******************************注释*********************************************
						//*初始化主帐号和主帐号令牌,对应官网开发者主账号下的ACCOUNT SID和AUTH TOKEN     *
						//*ACOUNT SID和AUTH TOKEN在登陆官网后，在“应用-管理控制台”中查看开发者主账号获取*
						//*参数顺序：第一个参数是ACOUNT SID，第二个参数是AUTH TOKEN。                   *
						//*******************************************************************************
						restAPI.setAccount("aaf98f89493ff1d301495a73ed62117d", "6224be832d88485ebad37719fdf7e574");
						
						
						//******************************注释*********************************************
						//*初始化应用ID                                                                 *
						//*测试开发可使用“测试Demo”的APP ID，正式上线需要使用自己创建的应用的App ID     *
						//*应用ID的获取：登陆官网，在“应用-应用列表”，点击应用名称，看应用详情获取APP ID*
						//*******************************************************************************
						restAPI.setAppId("aaf98f89493ff1d301495a7eb5a6118a");
						
						
						//******************************注释****************************************************************
						//*调用发送模板短信的接口发送短信                                                                  *
						//*参数顺序说明：                                                                                  *
						//*第一个参数:是要发送的手机号码，可以用逗号分隔，一次最多支持100个手机号                          *
						//*第二个参数:是模板ID，在平台上创建的短信模板的ID值；测试的时候可以使用系统的默认模板，id为1。    *
						//*系统默认模板的内容为“【云通讯】您使用的是云通讯短信模板，您的验证码是{1}，请于{2}分钟内正确输入”*
						//*第三个参数是要替换的内容数组。																														       *
						//**************************************************************************************************
						
						//**************************************举例说明***********************************************************************
						//*假设您用测试Demo的APP ID，则需使用默认模板ID 1，发送手机号是13800000000，传入参数为6532和5，则调用方式为           *
						//*result = restAPI.sendTemplateSMS("13800000000","1" ,new String[]{"6532","5"});																		  *
						//*则13800000000手机号收到的短信内容是：【云通讯】您使用的是云通讯短信模板，您的验证码是6532，请于5分钟内正确输入     *
						//*********************************************************************************************************************
				        code=Utils.getAuthCode();
				        System.out.println("自动生成的验证码为"+authcode);
						result = restAPI.sendTemplateSMS(phoneNumber.getText().toString(),"6493" ,new String[]{code,"5"});
						System.out.println("SDKTestGetSubAccounts result=" + result);
						if("000000".equals(result.get("statusCode"))){
							//正常返回输出data包体信息（map）
							HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
							Set<String> keySet = data.keySet();
							for(String key:keySet){
								Object object = data.get(key);
								System.out.println(key +" = "+object);
							}
						}else{
							//异常返回输出错误码和错误信息
							System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
						}
					}
					
				}
			}	
		});
	}
	private boolean checkPhone()
	{
		String number=phoneNumber.getText().toString();
		try
		{
			Long l=Long.parseLong(number);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	//获取MD5的值
    private String getMD5Str(String str) {  
        MessageDigest messageDigest = null;  
        try {  
            messageDigest = MessageDigest.getInstance("MD5");  
  
            messageDigest.reset();  
  
            messageDigest.update(str.getBytes("UTF-8"));  
        } catch (NoSuchAlgorithmException e) {  
            System.out.println("NoSuchAlgorithmException caught!");  
            System.exit(-1);  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
  
        byte[] byteArray = messageDigest.digest();  
  
        StringBuffer md5StrBuff = new StringBuffer();  
  
        for (int i = 0; i < byteArray.length; i++) {              
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)  
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));  
            else  
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));  
        }  
  
        return md5StrBuff.toString();  
    }  
    //处理用户注册线程返回的结果
    Handler handler = new Handler(){
	    @Override
	    public void handleMessage(Message msg) {
	       if(msg.what==1)
	       {
	    	   val=(String)msg.obj;
	    	   if(val.equals("success")){
		        	Intent intent = new Intent();
		        	intent.setClass(registerActivity.this,logInActivity.class);
		        	startActivity(intent);
		        	
		        }
		        else if(!val.equals("success"))
		        	Toast.makeText(registerActivity.this,"注册失败",Toast.LENGTH_LONG).show();
	       }
	       else
	       {
	    	   Toast.makeText(getApplicationContext(), "注册失败，用户名已经存在",Toast.LENGTH_SHORT).show();
	       }	       
	    }
	};	
	//用户注册线程
	Runnable runnable = new Runnable(){
	    @Override
	    public void run() {  
	    	String str=null;
	    	try
	    	{
	    		str=SocketHttpRequester.postString(IP.ip+":3000/register",params);    	
		        handler.obtainMessage(1,str).sendToTarget();
	    	} 
	    	catch(Exception e)
	    	{
		        handler.obtainMessage(0,str).sendToTarget();
	    	}
	    }
	};
	/** 
     * 判断数据网络是否连接
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
     *判断wifi是否连接 
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
    //获取小区列表handler
    Handler communityhandler=new Handler()
    {
    	public void handleMessage(Message msg)
    	{
    		if(msg.what==1)
    		{
    			String str=(String)msg.obj;
    			String[] strs=str.substring(1,str.length()-1).split(",");
    			System.out.println("获取的小区为"+str);
    			for(int i=0;i<strs.length;i++)
    				allEstate.add(strs[i].substring(1,strs[i].length()-1));
    			estateAdapter.notifyDataSetChanged();    		}
    		else
    		{
    			Toast.makeText(registerActivity.this,"获取小区列表错误",Toast.LENGTH_SHORT).show();
    		}
    	}
    };
	//获取小区列表的runnable
    Runnable communitylist=new Runnable()
    {
    	public void run()
    	{
    		String httpUrl =IP.ip+":3000/communitynew/getcommunitylist";  
			HttpGet getMethod = new HttpGet(httpUrl);  
			try {
		    HttpClient httpclient = new DefaultHttpClient();  
		    HttpResponse httpResponse=null;
		    httpResponse = httpclient.execute(getMethod);
		    String strResult = null;
		    if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)  
				{                 	
				  strResult = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
				  System.out.println("从服务器获取到的小区参数为"+strResult);
				  communityhandler.obtainMessage(1,strResult).sendToTarget();
			 	}
			 }
			catch (Exception e) 
			 {
			 	e.printStackTrace();
			 	communityhandler.obtainMessage(0).sendToTarget();
			 }  
    	}
    };
}

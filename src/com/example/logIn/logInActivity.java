package com.example.logIn;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.params.CookieSpecPNames;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.jivesoftware.smack.XMPPException;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.Settings.Secure;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.infoPush.infoPushActivity;
import com.example.manage.HomePage;
import com.example.manage.MainMenu;
import com.example.manage.R;
import com.example.register.registerActivity;
import com.example.tools.CurrentUser;
import com.example.tools.IP;
import com.example.tsbx.ClientConServer;
import com.example.tsbx.FaceConversionUtil;
import com.example.tsbx.MainActivity;
import com.example.tsbx.MyProgressBar;
import com.example.tsbx.UserOperateService;
import com.example.user.UserRealInformationActivity;

@SuppressLint("NewApi")
public class logInActivity extends Activity{
	private EditText phoneNumber,passWord;
	private Button login,register;
	private CheckBox rememberPassword;
	private String tmDevice;
	private String back = "";
	private String phone,passwordMD5;
	SharedPreferences setting;
	ProgressBar process;
	HandlerThread thread = new HandlerThread("MyHandlerThread"); 
	Handler mHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log_in);
		addImage();
		Button zhmm=(Button)findViewById(R.id.zhaohuimima);
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		InputStream is = getResources().openRawResource(
				 R.drawable.login_wangjimima );
		Bitmap bm = BitmapFactory.decodeStream(is, null, opt);
		BitmapDrawable bd = new BitmapDrawable(getResources(), bm);
		zhmm.setBackgroundDrawable(bd);
		zhmm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				startActivity(new Intent(getApplicationContext(),FindNewPassword.class));
			}
		});
		new Thread(new Runnable() {
			@Override
			public void run() {
				FaceConversionUtil.getInstace().getFileText(
						getApplication());
			}
		}).start();
		process=MyProgressBar.createProgressBar(logInActivity.this,null);
		process.setVisibility(View.GONE);
		 setting = getSharedPreferences("register.ini", 0);
		SharedPreferences mySharedPreferences = getSharedPreferences("user", Activity.MODE_PRIVATE);
		String name = mySharedPreferences.getString("userName", "");
		Boolean ifRememberPassword = mySharedPreferences.getBoolean("ifRememberPassword",false);
		String password = mySharedPreferences.getString("password", "");
		String username = mySharedPreferences.getString("userName", "");
		tmDevice = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID); 	
		phoneNumber = (EditText)findViewById(R.id.phoneNumberLogin);
		  //phoneNumber.setText(name);
		passWord = (EditText)findViewById(R.id.passwordLogin);
		if(ifRememberPassword)
		{
			passWord.setText(password);
            phoneNumber.setText(username);
		}
		login = (Button)findViewById(R.id.login);
		
		 is = getResources().openRawResource(
				 R.drawable.login_denglu );
	     bm = BitmapFactory.decodeStream(is, null, opt);
		 bd = new BitmapDrawable(getResources(), bm);
		 login.setBackgroundDrawable(bd);
		rememberPassword =(CheckBox)findViewById(R.id.rememberPassword);
		rememberPassword.setChecked(ifRememberPassword);
		register = (Button)findViewById(R.id.login_register);
		
		 is = getResources().openRawResource(
				 R.drawable.login_zhuce );
	     bm = BitmapFactory.decodeStream(is, null, opt);
		 bd = new BitmapDrawable(getResources(), bm);
		 register.setBackgroundDrawable(bd);
		login.setOnClickListener(new OnClickListener(){
				
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				phone = phoneNumber.getText().toString();
				String temp = phone+passWord.getText().toString();
				passwordMD5 = getMD5Str(temp);
				if(phone.isEmpty())
					Toast.makeText(getBaseContext(), "用户名为空",Toast.LENGTH_SHORT).show();
				else if(passWord.getText().toString().isEmpty())
					Toast.makeText(getBaseContext(), "密码为空", Toast.LENGTH_SHORT).show();
				else if(isWifiConnected()||isNetConnected()){
					process.setVisibility(View.VISIBLE);
					new Thread(runnable).start();
				}else
					Toast.makeText(getBaseContext(), "",Toast.LENGTH_SHORT).show();				
			}
			
		});
		register.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(logInActivity.this, registerActivity.class);
				startActivity(intent);
			}			
		});
	}
	
	public void addImage()
	{
		LinearLayout layout=(LinearLayout)findViewById(R.id.login_background);
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		InputStream is = getResources().openRawResource(
				 R.drawable.login_background );
		Bitmap bm = BitmapFactory.decodeStream(is, null, opt);
		BitmapDrawable bd = new BitmapDrawable(getResources(), bm);
		layout.setBackgroundDrawable(bd);
		
	}
	 /** 
     * MD5  
     */  
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
    Handler handler = new Handler(){
	    @Override
	    public void handleMessage(Message msg) {
	     if(msg.what==0)
	     {
				process.setVisibility(View.GONE);
	        	Toast.makeText(logInActivity.this,"登录失败，用户名或者密码不存在",Toast.LENGTH_SHORT).show();
	     }
	      if(msg.what==1)
	      { 
	    	  SharedPreferences mySharedPreferences = getSharedPreferences("user", Activity.MODE_PRIVATE); 
				SharedPreferences.Editor editor = mySharedPreferences.edit(); 
				Set<String> set = new HashSet<String>();
				
				editor.putString("userName", phoneNumber.getText().toString()).commit(); 
				editor.putString("password", passWord.getText().toString()).commit();
				editor.putBoolean("ifRememberPassword",rememberPassword.isChecked()).commit();
				//if(!mySharedPreferences.contains("petImageId"))
				//editor.putStringSet("petImageId", set);
				//if(!mySharedPreferences.contains("happyhomeImageId"))
			    //editor.putStringSet("petImageId", set);
			    editor.commit(); 
	    	    String str=(String)msg.obj;
	    	    System.out.println("收到的信息为"+str);
	    	    String[] strs=str.split("&");
		       // System.out.println("小区名称为"+strs[1]);
		        //Constants.Community=strs[1];
		        if(strs[0].equals("success"))	        	
		        {
		        	mySharedPreferences = getSharedPreferences("user", Activity.MODE_PRIVATE);
		    	    CurrentUser.username = mySharedPreferences.getString("userName", "");
		    	    Constants.username = mySharedPreferences.getString("userName", "");
		    	    Constants.password = mySharedPreferences.getString("password", "");
		        	//new Thread(userinfoRunnable).start();
	 	    	    String username= mySharedPreferences.getString("userName", "");
	 	    	    String password=mySharedPreferences.getString("password", "");
	 	    	    Boolean user_first = setting.getBoolean("FIRST",true);
	 	         if(user_first)
	 	         {	         	
	 	          System.out.println("向openfire注册");
	 	    	  new Thread(regRunnable).start();
		        	//new Thread(userinfoRunnable).start();
	 	         }
	 	         else
	 	         {
	 	        	//Intent intent = new Intent();
		        	//intent.setClass(logInActivity.this, HomePage.class);
		        	//startActivity(intent);	
	 	        	new Thread(userinfoRunnable).start();
	 			//	new Thread(loginRunable).start();
	 	        	// new Thread(getButlerInfo).start();
	 	         }
		                 	
		        }
		        else
		        {
  					process.setVisibility(View.GONE);
		        	Toast.makeText(logInActivity.this,"登录失败，用户名或者密码不存在",Toast.LENGTH_SHORT).show();
		        }
	      }
	      
	    }
	};
	//登录线程，使用cookie
	Runnable runnable = new Runnable(){
	    @Override
	    public void run() {
	    	/*
	    	BasicHttpParams params = new BasicHttpParams();
	    	// Set the timeout in milliseconds until a connection is established.  
	    	HttpConnectionParams.setConnectionTimeout(params,5000);
	    	// Set the default socket timeout (SO_TIMEOUT) 
	    	// in milliseconds which is the timeout for waiting for data.  
	    	HttpConnectionParams.setSoTimeout(params,5000);  
	    	 
	    	ConnManagerParams.setMaxTotalConnections(params, 5);
	    	ConnManagerParams.setTimeout(params,5000);
	    	*/
	    	HttpClient client = new DefaultHttpClient();
	    	/*
	    	CookieStore cookieStore = new BasicCookieStore();
	    	//Bind custom cookie store to the local context
	    	((AbstractHttpClient) client).setCookieStore(cookieStore);
	    	CookieSpecFactory csf = new CookieSpecFactory() {           
	    	public CookieSpec newInstance(HttpParams params) {
	    	    return new BrowserCompatSpec() {
	    	    @Override
	    	    public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException 
	    	    {
	    	        // Oh, I am easy
	    	        // allow all cookies
	    	        //log.debug("custom validate");
	    	    }
	    	    };
	    	}
	    	};
	    	((AbstractHttpClient) client).getCookieSpecs().register("oschina", csf);
	    	client.getParams().setParameter(ClientPNames.COOKIE_POLICY, "oschina");
	    	client.getParams().setParameter(CookieSpecPNames.SINGLE_COOKIE_HEADER, true);
	    	*/
			try {
				 HttpPost httppost = new HttpPost(IP.ip+":3000/login-mobile");   
				 List<NameValuePair> map = new ArrayList <NameValuePair>();
		         map.add(new BasicNameValuePair("name",phone));
		         System.out.println(phone);
		         map.add(new BasicNameValuePair("password",passwordMD5));
		         map.add(new BasicNameValuePair("imei",tmDevice));
				 httppost.setEntity(new UrlEncodedFormEntity(map));   				 
				 //client.execute(httppost);  
				HttpResponse response = client.execute(httppost);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					back = EntityUtils.toString(response.getEntity());
				   handler.obtainMessage(1,back).sendToTarget();
					} else 
					{
						handler.obtainMessage(0).sendToTarget();
					}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				handler.obtainMessage(0).sendToTarget();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				handler.obtainMessage(0).sendToTarget();
			} 
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
  
    private boolean is3gConnected() {  
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);  
        if (cm != null) {  
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();  
            if (networkInfo != null 
                    && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {  
                return true;  
            }  
        }  
        return false;  
    }  
    
    //处理登录openfire的handler类
    Handler openfirehandler=new Handler()
	{
		public void handleMessage(Message msg) {
			if(msg.what==1)
			{
				Constants.loginStatus=true;
				process.setVisibility(View.GONE);
				
			      //  setContentView(R.layout.null_view);
			        logInActivity.this.finish();
					
				Intent intent = new Intent();
	        	intent.setClass(logInActivity.this, HomePage.class);
	        	startActivity(intent);	
			}
			else
			{
				process.setVisibility(View.GONE);
				// setContentView(R.layout.null_view);
			        logInActivity.this.finish();
				Intent intent = new Intent();
	        	intent.setClass(logInActivity.this, HomePage.class);
	        	startActivity(intent);	
				//first.edit().putBoolean("FIRST", false).commit();
				//Intent i=new Intent(UserRealInformationActivity.this,MainActivity.class);
		    	//i.putExtra("LoginStatus","false");
		    	//startActivity(i);
			}
		}
	};
	//登录到openfire服务器
	 Runnable loginRunable = new Runnable() {
			
			@Override
			public void run() {
				//登陆子线程
				ClientConServer ccs = new ClientConServer(logInActivity.this);
				boolean loginStatus = ccs.login(Constants.username, Constants.password,"113.6.252.157",5222);
			    if(loginStatus)
			    { 	
			    	System.out.println("登录openfire success");
			    	openfirehandler.obtainMessage(1).sendToTarget();
			    	//Toast.makeText(getApplicationContext(), "登录成功",Toast.LENGTH_LONG).show();
			    }
			    else
			    {
			    	System.out.println("登录openfire wrong");
			    	openfirehandler.obtainMessage(0).sendToTarget();
			    	//Toast.makeText(getApplicationContext(), "登录失败",Toast.LENGTH_LONG).show(); 	
			    }
			}
		};
    //处理openfire注册的信息
    Handler regHandler=new Handler()
    {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(msg.what==1)
			{
	 	        setting.edit().putBoolean("FIRST", false).commit();
				Toast.makeText(logInActivity.this,"向openfire服务器注册成功",Toast.LENGTH_SHORT).show();
				//Intent intent = new Intent();
	        	//intent.setClass(logInActivity.this, HomePage.class);
	        	//startActivity(intent);	
			//	new Thread(loginRunable).start();
				//new Thread(getButlerInfo).start();
	        	new Thread(userinfoRunnable).start();
			}
			else
			{
				//new Thread(getButlerInfo).start();
				//new Thread(loginRunable).start();
	        	new Thread(userinfoRunnable).start();

				Toast.makeText(logInActivity.this,"向openfire服务器注册失败",Toast.LENGTH_SHORT).show();	
			}
		}
    };
    //管家登录的时候同时向openfire服务器注册；
    Runnable regRunnable = new Runnable(){

		@Override
		public void run() {
			android.os.Message msg = android.os.Message.obtain();
			try {
				new ClientConServer("113.6.252.157", 5222);
				Map<String,String> attributes = new HashMap<String, String>();
				UserOperateService	userOperateService = new UserOperateService();
				System.out.println("注册的用户名为"+Constants.username+" "+Constants.password);
				if (userOperateService.regAccount(Constants.username,Constants.password, attributes)){
                     regHandler.obtainMessage(1).sendToTarget();
				}else {
					regHandler.obtainMessage(1).sendToTarget();
				}
			} catch (XMPPException e) {
				regHandler.obtainMessage(1).sendToTarget();
				e.printStackTrace();
			}		
			//regHandler.obtainMessage(1).sendToTarget();
		}
		
	};
	
	Handler userinfoHandler=new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(msg.what==1)
			{
				String str=(String)msg.obj;
				System.out.println("str="+str);
				SharedPreferences mySharedPreferences = getSharedPreferences("user", Activity.MODE_PRIVATE);
				try {
					JSONObject obj=new JSONObject(str);
					String address=obj.getString("address");
					String community=obj.getString("community");
					
					String phonenumber=obj.getString("phone");
					String name=obj.getString("name");
					String realname=obj.getString("realname");
					Constants.realname=realname;
					if(realname.equals("")||realname.equals(" ")||address.equals("--")||address.split("-").length!=3)
					{
						System.out.println("信息不完整");
					}
					else
					{
						Constants.informationCompleteed=true;
					}
					try
					{
						String img = obj.getString("img");
						mySharedPreferences.edit().putString("imageUrl",img).commit();
						Constants.userImg=img;
						
					}
					catch(Exception e)
					{
						e.printStackTrace();
						System.out.println("用户没有上传图片");
						Constants.userHasImg=false;
					}
					Constants.ecoin=obj.getString("ecoin");
					Constants.Community = community;
					mySharedPreferences.edit().putString("address",address).commit();
					mySharedPreferences.edit().putString("community",community).commit();
					mySharedPreferences.edit().putString("phonenumber",phonenumber).commit();
					mySharedPreferences.edit().putString("name",name).commit();
					mySharedPreferences.edit().putString("realname",realname).commit();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				new Thread(getButlerInfo).start();
			}
		}
	};
	//从服务器获取用户信息
    Runnable userinfoRunnable=new Runnable()
    {
		@Override
		public void run() {
		System.out.println("获取用户的信息"+Constants.username);
			// TODO Auto-generated method stub
			String httpUrl =IP.ip+":3000/user/getinfo?userid="+Constants.username;  
			HttpGet getMethod = new HttpGet(httpUrl);  
			try {
		    HttpClient httpclient = new DefaultHttpClient();  
		    HttpResponse httpResponse=null;
		    httpResponse = httpclient.execute(getMethod);
		    String strResult = null;
		    if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)  
				{                 	
				  strResult = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
				  System.out.println("从服务器获取到的参数为"+strResult);
				  userinfoHandler.obtainMessage(1,strResult).sendToTarget();
			 	}
			 }
			catch (Exception e) 
			 {
			 	e.printStackTrace();
			 	userinfoHandler.obtainMessage(0).sendToTarget();
			 }  
			          			 		
		}	
    };
  //获取管家信息的处理类
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
  					process.setVisibility(View.GONE);
  					 //setContentView(R.layout.null_view);
 			        logInActivity.this.finish();
  					Intent intent = new Intent();
  		        	intent.setClass(logInActivity.this, HomePage.class);
  		        	startActivity(intent);	
  				} catch (JSONException e) {
  					// TODO Auto-generated catch block
  					e.printStackTrace();
  				}
  			}
  			else
  			{
  				Toast.makeText(logInActivity.this,"请检查一下网络连接",Toast.LENGTH_SHORT).show();
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

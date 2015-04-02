package com.example.user;

import java.util.HashMap;

import com.example.bbm.SocketHttpRequester;
import com.example.bbm.bbmDataStore;
import com.example.logIn.Constants;
import com.example.manage.R;
import com.example.tools.IP;
import com.example.tsbx.ChatActivity;
import com.example.tsbx.ClientConServer;
import com.example.tsbx.MainActivity;
import com.example.yy.YYHomePage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class UserRealInformationActivity extends Activity
{
    private SharedPreferences userinfo;
    private SharedPreferences first;
    private EditText realname,dong,danyuan,shi;
    private Button confirm;
    private RadioGroup group;
    private String type="房主";
    private String name;
    private String address;
    private String activity;
    private Bundle bundle;
    String activityString;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		userinfo=getSharedPreferences("user",Activity.MODE_PRIVATE);
		first=getSharedPreferences("first.ini",0);
		 Boolean user_first = first.getBoolean("FIRST",true);
		 Intent ii=getIntent();
		 bundle=ii.getExtras();
		 activityString=bundle.getString("type");
		    if(Constants.informationCompleteed)
		    {
		    	this.finish();
		    	if(activityString.equals("tsbx"))
		    	{
		    		this.finish();
		    		Intent i=new Intent(UserRealInformationActivity.this,MainActivity.class);
			    	i.putExtra("LoginStatus",Constants.loginStatus);
			    	startActivity(i);
		    	}
		    	else
		    	{
		    		Intent i=new Intent(UserRealInformationActivity.this,YYHomePage.class);
		    		startActivity(i);
		    	}
				//new Thread(loginRunable).start();
		    }
		 System.out.println("第一次启动");
   		setContentView(R.layout.activity_userrealinformation);
		 RadioGroup group = (RadioGroup)this.findViewById(R.id.real_choose);
		       //绑定一个匿名监听器
		 group.setOnCheckedChangeListener(new OnCheckedChangeListener() {   
		           @Override
		 public void onCheckedChanged(RadioGroup arg0, int arg1) 
		       {
		          int radioButtonId = arg0.getCheckedRadioButtonId();
	               //根据ID获取RadioButton的实例
		          RadioButton rb = (RadioButton)UserRealInformationActivity.this.findViewById(radioButtonId);
	               //更新文本内容，以符合选中项
		          System.out.println(rb.getText());
		          type=rb.getText().toString();
		         }
		      });
		realname=(EditText)findViewById(R.id.real_name_edit);
		dong=(EditText)findViewById(R.id.real_dong);
		danyuan=(EditText)findViewById(R.id.real_danyuan);
		shi=(EditText)findViewById(R.id.real_shi);
		confirm=(Button)findViewById(R.id.real_confirm);
		confirm.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				System.out.println("dfd"+realname.getText().toString());
				System.out.println("fdfds"+shi.getText().toString());
				System.out.println(danyuan.getText().toString());
				System.out.println(dong.getText().toString());
				if(realname.getText().toString().equals(""))
				{
					System.out.println("真实姓名为空");
					Toast.makeText(getApplicationContext(),"请填写真实姓名",Toast.LENGTH_SHORT).show();
				}
				else if(shi.getText().toString().equals("")||dong.getText().toString().equals("")||danyuan.getText().toString().equals(""))
				{
					Toast.makeText(getApplicationContext(),"请将住址填写完整",Toast.LENGTH_SHORT).show();
				}
				else
				{
					System.out.println("启动登录线程");
					name=realname.getText().toString();
					address=dong.getText().toString()+"栋-"+danyuan.getText().toString()+"单元-"+shi.getText().toString()+"室";
					userinfo.edit().putString("realName",realname.getText().toString()).commit();
					String address=dong.getText().toString()+"-"+danyuan.getText().toString()+"-"+shi.getText().toString();
					userinfo.edit().putString("address",address).commit();
				//	new Thread(loginRunable).start();
					new Thread(realInfoRunnable).start();
					//Intent i=new Intent(UserRealInformationActivity.this,MainActivity.class);
			    	//i.putExtra("LoginStatus",Constants.loginStatus);
			    	//startActivity(i);
				}
			}			
		});
	}
	/*
	Handler handler=new Handler()
	{
		public void handleMessage(Message msg) {
			if(msg.what==1)
			{
				first.edit().putBoolean("FIRST", false).commit();
				Intent i=new Intent(UserRealInformationActivity.this,MainActivity.class);
		    	i.putExtra("LoginStatus","true");
		    	startActivity(i);
			}
			else
			{
				first.edit().putBoolean("FIRST", false).commit();
				Intent i=new Intent(UserRealInformationActivity.this,MainActivity.class);
		    	i.putExtra("LoginStatus","false");
		    	startActivity(i);
			}
		}
	};
	//登录到openfire服务器
	 Runnable loginRunable = new Runnable() {
			
			@Override
			public void run() {
				//登陆子线程
				ClientConServer ccs = new ClientConServer(UserRealInformationActivity.this);
				boolean loginStatus = ccs.login(Constants.username, Constants.password,"113.6.252.157",5222);
			    if(loginStatus)
			    {
			    	
			    	System.out.println("success");
			    	handler.obtainMessage(1).sendToTarget();
			    	//Toast.makeText(getApplicationContext(), "登录成功",Toast.LENGTH_LONG).show();
			    }
			    else
			    {
			    	System.out.println("wrong");
			    	handler.obtainMessage(0).sendToTarget();
			    	//Toast.makeText(getApplicationContext(), "登录失败",Toast.LENGTH_LONG).show(); 	
			    }
			}
		};
		*/
		Handler realinfohandler=new Handler()
		{

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if(msg.what==1)
				{
					first.edit().putBoolean("FIRST", false).commit();
					Constants.informationCompleteed=true;
					Toast.makeText(UserRealInformationActivity.this,"更新用户信息成功",Toast.LENGTH_SHORT).show();
					//Intent i=new Intent(UserRealInformationActivity.this,MainActivity.class);
			    	//i.putExtra("LoginStatus",Constants.loginStatus);
			    	//startActivity(i);
					System.out.println("activitysString="+activityString);
					if(activityString.equals("tsbx"))
			    	{
			    		Intent i=new Intent(UserRealInformationActivity.this,ChatActivity.class);
				    	i.putExtra("LoginStatus",Constants.loginStatus);
				    	startActivity(i);
			    	}
			    	else
			    	{
			    		Intent i=new Intent(UserRealInformationActivity.this,YYHomePage.class);
			    		startActivity(i);
			    	}
				}
				else
				{
					Toast.makeText(UserRealInformationActivity.this,"更新用户信息失败",Toast.LENGTH_SHORT).show();

				}
			}
		};
		//上传用户的真实信息
		Runnable realInfoRunnable=new Runnable()
		{
			public void run()
			{
				System.out.println("上传用户的真实信息");
				HashMap<String,String> map=new HashMap<String,String>();
				map.put("name",Constants.username);
				map.put("realname",name);
				map.put("identify",type);
				map.put("address",address);
				try
				{
					System.out.println("上传用户的真实信息");
					SocketHttpRequester.postString(IP.ip+":3000/user/updateuserinfo", map);
					System.out.println("上传用户的真实信息");

					realinfohandler.obtainMessage(1).sendToTarget();
				}
				catch(Exception e)
				{
					e.printStackTrace();
					realinfohandler.obtainMessage(0).sendToTarget();

				}
			}
		};	
}

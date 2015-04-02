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
    private String type="����";
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
		 System.out.println("��һ������");
   		setContentView(R.layout.activity_userrealinformation);
		 RadioGroup group = (RadioGroup)this.findViewById(R.id.real_choose);
		       //��һ������������
		 group.setOnCheckedChangeListener(new OnCheckedChangeListener() {   
		           @Override
		 public void onCheckedChanged(RadioGroup arg0, int arg1) 
		       {
		          int radioButtonId = arg0.getCheckedRadioButtonId();
	               //����ID��ȡRadioButton��ʵ��
		          RadioButton rb = (RadioButton)UserRealInformationActivity.this.findViewById(radioButtonId);
	               //�����ı����ݣ��Է���ѡ����
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
					System.out.println("��ʵ����Ϊ��");
					Toast.makeText(getApplicationContext(),"����д��ʵ����",Toast.LENGTH_SHORT).show();
				}
				else if(shi.getText().toString().equals("")||dong.getText().toString().equals("")||danyuan.getText().toString().equals(""))
				{
					Toast.makeText(getApplicationContext(),"�뽫סַ��д����",Toast.LENGTH_SHORT).show();
				}
				else
				{
					System.out.println("������¼�߳�");
					name=realname.getText().toString();
					address=dong.getText().toString()+"��-"+danyuan.getText().toString()+"��Ԫ-"+shi.getText().toString()+"��";
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
	//��¼��openfire������
	 Runnable loginRunable = new Runnable() {
			
			@Override
			public void run() {
				//��½���߳�
				ClientConServer ccs = new ClientConServer(UserRealInformationActivity.this);
				boolean loginStatus = ccs.login(Constants.username, Constants.password,"113.6.252.157",5222);
			    if(loginStatus)
			    {
			    	
			    	System.out.println("success");
			    	handler.obtainMessage(1).sendToTarget();
			    	//Toast.makeText(getApplicationContext(), "��¼�ɹ�",Toast.LENGTH_LONG).show();
			    }
			    else
			    {
			    	System.out.println("wrong");
			    	handler.obtainMessage(0).sendToTarget();
			    	//Toast.makeText(getApplicationContext(), "��¼ʧ��",Toast.LENGTH_LONG).show(); 	
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
					Toast.makeText(UserRealInformationActivity.this,"�����û���Ϣ�ɹ�",Toast.LENGTH_SHORT).show();
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
					Toast.makeText(UserRealInformationActivity.this,"�����û���Ϣʧ��",Toast.LENGTH_SHORT).show();

				}
			}
		};
		//�ϴ��û�����ʵ��Ϣ
		Runnable realInfoRunnable=new Runnable()
		{
			public void run()
			{
				System.out.println("�ϴ��û�����ʵ��Ϣ");
				HashMap<String,String> map=new HashMap<String,String>();
				map.put("name",Constants.username);
				map.put("realname",name);
				map.put("identify",type);
				map.put("address",address);
				try
				{
					System.out.println("�ϴ��û�����ʵ��Ϣ");
					SocketHttpRequester.postString(IP.ip+":3000/user/updateuserinfo", map);
					System.out.println("�ϴ��û�����ʵ��Ϣ");

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

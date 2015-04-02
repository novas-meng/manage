package com.example.bbm;


import java.io.File;
import java.util.HashMap;
import java.util.Map;
import com.example.manage.R;
import com.example.tools.IP;
import com.example.tsbx.MainActivity;
import com.example.tsbx.MyProgressBar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

public class bbmPublishActivity extends Activity
{
     ImageButton voice;
     MyVoice myvoice=new MyVoice();
     Button button;
     Button readbutton;
     boolean isVoice=false;
     EditText contentedittext;
     EditText phoneedittext;
     String filename;
     //表示发送状态
     int Status=1;  
     String username;
     ProgressBar process;
     InputMethodManager imm;
     Handler statusHandler=new Handler()
     {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(msg.what==0)
			{
				Status=1;
				Toast.makeText(bbmPublishActivity.this,"发布出错，请检查一下网络",Toast.LENGTH_SHORT).show();
			}
			if(msg.what==1)
			{
				Status=1;
				process.setVisibility(View.GONE);
				 contentedittext.setText("");
                 phoneedittext.setText("");
				Toast.makeText(bbmPublishActivity.this,"发布成功",Toast.LENGTH_SHORT).show();
				//Intent intent=new Intent(bbmPublishActivity.this,bbmHomePage.class);
				//startActivity(intent);
				//bbmPublishActivity.this.setContentView(R.layout.null_view);
				//bbmPublishActivity.this.finish();
				bbmHomePage.changeHandler.obtainMessage().sendToTarget();
				
			}
		}
    	 
     };
     
	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
        setContentView(R.layout.null_view);
        bbmPublishActivity.this.finish();
		super.onDestroy();
	}
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-gener ated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbm_publish);
		process=MyProgressBar.createProgressBar(bbmPublishActivity.this,null);
		//process=(ProgressBar)findViewById(R.id.bbm_progressBar);
		process.setVisibility(View.GONE);
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		SharedPreferences mySharedPreferences = getSharedPreferences("user", Activity.MODE_PRIVATE);
		username = mySharedPreferences.getString("userName", "");
        contentedittext=(EditText)findViewById(R.id.bbm_publish_textarea);
        phoneedittext=(EditText)findViewById(R.id.bbm_publish_phone);
      
		addVoiceListener();
	    addListener();
	    Utils.handlerInput=new Handler(){
			@Override
			public void handleMessage(Message msg) {			
				if(msg.what==Utils.CLOSE_INPUT){//关闭软键盘
					imm.hideSoftInputFromWindow(bbmPublishActivity.this.getCurrentFocus().getWindowToken(),  
	                 InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		};
	   imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	    IntentFilter fliter=new IntentFilter();
	    fliter.addAction("PublishCompleted");
	    this.registerReceiver(new BroadcastReceiver()
	    {

			@Override
			   public void onReceive(Context arg0, Intent arg1) {
				// TODO Auto-generated method stub
				if(arg1.getAction().equals("PublishCompleted"))
				{
					process.setVisibility(View.GONE);
                    process.setProgress(100);					
                    contentedittext.setText("");
                    phoneedittext.setText("");
					Toast.makeText(bbmPublishActivity.this,"发布成功",Toast.LENGTH_LONG).show();
				}
			}
	    	
	    }, fliter);
	}
	public void addListener()
	{
		button=(Button)findViewById(R.id.bbm_publish_confirm);
		button.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(phoneedittext.getText().toString().length()!=11&&phoneedittext.getText().toString().length()!=8)
				{
					Toast.makeText(getApplicationContext(), "电话位数有误", Toast.LENGTH_SHORT).show();

				}
				else if(Check(contentedittext.getText().toString(),phoneedittext.getText().toString(),isVoice)==0)
				{
					process.setVisibility(View.VISIBLE);
					new Thread(new Runnable()
					{
						public void run()
						{
							if(isVoice==false)
							{
								HashMap<String,String> map=new HashMap<String,String>();
								map.put("user", username);
								map.put("phonenumber",phoneedittext.getText().toString());
								map.put("content", contentedittext.getText().toString());
								map.put("type", "0");
								bbmDataStore.storeSendInformation(0,(contentedittext.getText().toString()+"##"+phoneedittext.getText().toString()).getBytes());
								try
								{
									SocketHttpRequester.postString(IP.ip+":3000/helps/publishhelp", map);
									statusHandler.obtainMessage(1).sendToTarget();
								}
							    catch(Exception e)
							    {
							    	Status=0;
							    	statusHandler.obtainMessage(0).sendToTarget();
							    	e.printStackTrace();
							    }
								
							}
							else
							{
								 final File uploadFile = new File(myvoice.getAudioPath());  
					        	 Map<String, String> params = new HashMap<String, String>();  
	                             bbmDataStore.storeSendInformation(1,(myvoice.getAudioPath()+"##"+phoneedittext.getText().toString()).getBytes());
					             params.put("phonenumber",phoneedittext.getText().toString());  
					             params.put("user",username);
					             params.put("type", "1");
					             FormFile formfile = new FormFile("test.3gp", uploadFile, "voice", "image/jpeg");  
					             try {
									//UploadUtil.uploadFile(uploadFile, "http://192.168.1.125:3000/publishInformation");
					            	 SocketHttpRequester.post("http://113.6.252.157:3000/helps/publishhelp", params, formfile);
						          //   SocketHttpRequester.post("http://113.6.252.157:3000/pet/pushpet", params, formfile);

					            	 statusHandler.obtainMessage(1).sendToTarget();
								} catch (Exception e) {
									Status=0;
									statusHandler.obtainMessage(0).sendToTarget();
									e.printStackTrace();
									System.out.println("上传出错");
								}  
							}
							System.out.println("status="+Status);			
						}
					}).start();
				}			
			}
		});
	}
	public int Check(String content,String phonenumber,boolean isVoice)
	{
		if(!isVoice&&content.length()==0)
		{
			Toast.makeText(getApplicationContext(), "亲，您的信息还没有填呢",Toast.LENGTH_SHORT).show();
			return -1;
		}
		//if(phonenumber.length()==0)
		//{
			//Toast.makeText(getApplicationContext(), "亲，请输入您的手机号",Toast.LENGTH_SHORT).show();
			//return -1;
		//}
		//if(phonenumber.trim().length()!=11)
		//{
			//System.out.println("phonenumber.trim().length()"+phonenumber.trim().length()+"phonenumber="+phonenumber);
			//Toast.makeText(getApplicationContext(), "亲，您输入的手机号码位数不对哦",Toast.LENGTH_SHORT).show();
		//	return -1;
		//}
		/*
		try
		{
			Float number=Float.parseFloat(phonenumber);
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), "手机号码输入错误",Toast.LENGTH_SHORT).show();
            return -1;
		}
		*/
		return 0;
  	}
	public void addVoiceListener()
	   {
		   voice=(ImageButton)findViewById(R.id.bbm_publish_voice);
		   voice.setOnTouchListener(new OnTouchListener()
			{

				@Override
				public boolean onTouch(View arg0, MotionEvent event) {
					// TODO Auto-generated method stub
					switch(event.getAction())
					{
	                case MotionEvent.ACTION_DOWN:
	                	isVoice=true;
						System.out.println("手指按下");
						voice.setBackgroundResource(R.drawable.voice_end);
						System.out.println("手指按下");
						Toast.makeText(bbmPublishActivity.this,"正在准备",Toast.LENGTH_SHORT).show();
						Toast.makeText(bbmPublishActivity.this,"请说话",Toast.LENGTH_SHORT).show();
						filename="/bbm/audio/"+Utils.getCurrentTime()+"-send";
						myvoice=new MyVoice(filename);
						myvoice.startRecording();
						break;
					case MotionEvent.ACTION_UP:
						System.out.println("抬起手指");
						voice.setBackgroundResource(R.drawable.voice_start);
						myvoice.stopRecording();
						//byte[] res=myvoice.getAudioBytes(filename);
						//myvoice.startPlaying();
						//sendMessage("##",true,1);
						//sendVoiceToMannager(res);
					   break;
					}
				return true;
				}
			});
	   }
}

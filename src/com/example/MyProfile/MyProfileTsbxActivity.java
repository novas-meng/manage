package com.example.MyProfile;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import com.example.bbm.FormFile;
import com.example.bbm.SocketHttpRequester;
import com.example.bbm.bbmDataStore;
import com.example.logIn.Constants;
import com.example.manage.R;
import com.example.tools.IP;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tsbx.*;

public class MyProfileTsbxActivity extends Activity {
   boolean flag=false;
   ProgressBar process;
 //  EditText et_sendMessage;
 //  RelativeLayout ll_facechoose;
  // InputMethodManager imm;
   String filename;
  // ImageButton voice;
   int index;
   ImageButton btn_face;
   private ChatMsgAdapter mAdapter;
   private ListView listview;
    MyVoice myvoice=new MyVoice("myaudio");
   // XMPPConnection connection;
	NotificationManager noticeManager;
	Notification notice;
	Handler viewhandler;
	ChatManager chatmanger ;
	Chat chat;
	public static Bitmap userhead;
	public static Bitmap butlerhead;
	boolean NetisOk=false;
	static boolean isLogin=true;
    static List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();
    List<ChatMsgEntity> unSendmDataArrays = new ArrayList<ChatMsgEntity>();
    String Master=Constants.butlername+"@113.6.252.157";
    String manager=Constants.butlername;
	OffLineMessageManager offlineManager;
	MyOffLineMessageReceiver offLineListener;
    private ConnectivityManager connectivityManager;
	private NetworkInfo info;
	static Handler netListenerHandler;
	private String SendMsg="";
	NetStateListener net=new NetStateListener();
    @SuppressLint("NewApi") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tsbx_record);
        
       // if (android.os.Build.VERSION.SDK_INT > 9) {
       //     StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
         //   StrictMode.setThreadPolicy(policy);
      //  }
        addUserImage();
        addButlerImage();
        index=0;
       // Toast.makeText(getApplicationContext(), manager, Toast.LENGTH_SHORT).show();
        TextView last=(TextView)findViewById(R.id.last_page);
        TextView next=(TextView)findViewById(R.id.next_page);
        last.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(index<10)
					;
				else
				{
					index=index-10;
					mDataArrays=Tools.getLocalMessages(manager,MyProfileTsbxActivity.this,index);
					mAdapter=new ChatMsgAdapter(MyProfileTsbxActivity.this,mDataArrays);
					listview.setAdapter(mAdapter);
					
				}
			}
		});
        next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(mDataArrays.size()<10)
					;
				else
				{
					index=index+10;
					mDataArrays=Tools.getLocalMessages(manager,MyProfileTsbxActivity.this,index);
					mAdapter=new ChatMsgAdapter(MyProfileTsbxActivity.this,mDataArrays);
					listview.setAdapter(mAdapter);
					//mAdapter.notifyDataSetChanged();
				}
			}
		});
     
     process=MyProgressBar.createProgressBar(MyProfileTsbxActivity.this,null);
     process.setVisibility(View.GONE);
     
        mDataArrays=Tools.getLocalMessages(manager,getParent(),index);
      
     //   getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
      
     //   ConnectivityManager con=(ConnectivityManager)getSystemService(Activity.CONNECTIVITY_SERVICE);  
      //  boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();  
      
    //    creatAppDir();
        
        init();
       
      /*  if(NetisOk)
      
        initNetStateListener();*/
        listview.setOnItemClickListener(new MyItemClickListener());
      //  myvoice=new MyVoice(filename);
		//myvoice.startRecording();
       /*if(isNetworkConnected(getApplicationContext()))
        {if(isLogin)
        {
        	System.out.println("main函数获取离线消息错误");
            initConnection();
        	connection = (XMPPConnection) ClientConServer.connection;
            offlineManager=new OffLineMessageManager(connection,MyProfileTsbxActivity.this);
	        offlineManager.getOfflineMessages();
        } }*/
	  //  IntentFilter filter=new IntentFilter();
	//	filter.addAction("OffLineMessage");
		//filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		//offLineListener=new MyOffLineMessageReceiver();
		//this.registerReceiver(offLineListener, filter);
		
		//this.registerReceiver(net, filter);
    }
    /*
    //网络状态监听的类，当离线的时候可以自动登录
    public void initNetStateListener()
    {
    	netListenerHandler=new Handler()
    	{
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
			     if(msg.what==1)
			     {
			    	 if(!isLogin)
			    	 {
			    		 ClientConServer ccs = new ClientConServer(MyProfileTsbxActivity.this);
			 			boolean loginStatus = ccs.login(Constants.username,Constants. password, 
			 					"113.6.252.157",5222);
			 			if(loginStatus)
			 			{
			 				initConnection();
				        	connection = (XMPPConnection) ClientConServer.connection;
				            offlineManager=new OffLineMessageManager(connection,MyProfileTsbxActivity.this);
					        offlineManager.getOfflineMessages();
					        System.out.println("登录状态"+loginStatus);
					        isLogin=true;
			 			}			 			
			    	 }
			     }
			}	
    	};
    }
    */
    public void addUserImage()
    {
    	
    	if(Constants.userHasImg)
    	{
    		SharedPreferences mySharedPreferences = getSharedPreferences("user", Activity.MODE_PRIVATE);
        	String img = mySharedPreferences.getString("imageUrl", "");
        	if(img.equals("[]")){
        		//没有用户头像
        	}else{
        		if(img.length()>=2){
        			img = img.substring(2, img.length()-2);
        			img = IP.ip+":3000/uploads/"+img;
        			//从网络获取图片 地址为img
        			System.out.println("用户头像地址为="+img);
        			userhead=getBitmapFromServer(img);
        		}
        	}
    	}
    	else
    	{
    	
    		userhead=BitmapFactory.decodeResource(this.getApplicationContext().getResources(),R.drawable.yonghu);
    	}
    	com.example.tsbx.MainActivity.userhead=userhead;
    }
    public void addButlerImage()
    {
    	
    	if(Constants.butlerHasImg)
    	{
    		String img=Constants.butlerImg;
    		img = img.substring(2, img.length()-2);
			img = IP.ip+":3000/uploads/"+img;
			//从网络获取图片 地址为img
    		System.out.println("管家头像的地址为="+img);
			butlerhead=getBitmapFromServer(img);
    	}
    	else
    	{
    	
    	butlerhead = BitmapFactory.decodeResource(this.getApplicationContext().getResources(), R.drawable.kefu);
    	}
    	com.example.tsbx.MainActivity.butlerhead=butlerhead;
    }
    public static Bitmap getBitmapFromServer(String imagePath) {

		  HttpGet get = new HttpGet(imagePath);
		  HttpClient client = new DefaultHttpClient();
		  Bitmap pic = null;
		  try {
		   HttpResponse response = client.execute(get);
		   HttpEntity entity = response.getEntity();
		   InputStream is = entity.getContent();

		   pic = BitmapFactory.decodeStream(is);   

		  } catch (ClientProtocolException e) {
		   e.printStackTrace();
		  } catch (IOException e) {
		   e.printStackTrace();
		  }
		  return pic;
		 }
    //离线消息的接受类
    class MyOffLineMessageReceiver extends BroadcastReceiver
    {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			if(arg1.getAction().equals("OffLineMessage"))
			{
				ArrayList<String> offLineMessageList=arg1.getStringArrayListExtra("OffLineMessage");
				System.out.println("收到的离线消息数量为"+offLineMessageList.size());
				//offline.showOffLineNotification(offLineMessageList, MainActivity.this,LoginActivity.class);
				showOffLineNotice(offLineMessageList);
			}
			 if (arg1.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
	                Log.d("mark", "网络状态已经改变");
	                connectivityManager = (ConnectivityManager)      

	                                         getSystemService(Context.CONNECTIVITY_SERVICE);
	                info = connectivityManager.getActiveNetworkInfo();  
	                if(info != null && info.isAvailable()) {
	                    String name = info.getTypeName();
	                    Log.d("mark", "当前网络名称：" + name);
	                } else {
	                    Log.d("mark", "没有可用网络");
	                }
	            }
		}
    }
    //将获取的离线的消息显示在通知栏中
    public void showOffLineNotice(ArrayList<String> list)
    {
    	noticeManager=(NotificationManager)MyProfileTsbxActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
		if(list.size()>0)
		{
			notice=new Notification();
			notice.icon=R.drawable.chatchat;
			notice.tickerText=list.get(list.size()-1);
			notice.defaults=Notification.DEFAULT_SOUND;
	    	Intent intent=new Intent(MyProfileTsbxActivity.this,MyProfileTsbxActivity.class);
	    	PendingIntent pi=PendingIntent.getActivity(MyProfileTsbxActivity.this,0,intent, PendingIntent.FLAG_ONE_SHOT);
	    	notice.setLatestEventInfo(MyProfileTsbxActivity.this, "您有"+list.size()+"条新消息,来自",list.get(list.size()-1),pi);
	    	noticeManager.notify(1,notice);
		}
    }
 // 查看所有的sd路径  
    public String getSDCardPathEx() {  
        String mount = new String();  
        try {  
            Runtime runtime = Runtime.getRuntime();  
            Process proc = runtime.exec("mount");  
            InputStream is = proc.getInputStream();  
            InputStreamReader isr = new InputStreamReader(is);  
            String line;  
            BufferedReader br = new BufferedReader(isr);  
            while ((line = br.readLine()) != null) {  
                if (line.contains("secure"))  
                    continue;  
                if (line.contains("asec"))  
                    continue;  
      
                if (line.contains("fat")) {  
                    String columns[] = line.split(" ");  
                    if (columns != null && columns.length > 1) {  
                        mount = mount.concat("*" + columns[1] + "\n");  
                    }  
                } else if (line.contains("fuse")) {  
                    String columns[] = line.split(" ");  
                    if (columns != null && columns.length > 1) {  
                        mount = mount.concat(columns[1] + "\n");  
                    }  
                }  
            }  
        } catch (FileNotFoundException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return mount;  
    }  
    public void creatAppDir()
   	{
   		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
   		{
   			System.out.println("创建app文件夹");
   			File f=new File(Environment.getExternalStorageDirectory(),"E-homeland");
   			if(!f.exists())
   	      	f.mkdirs();
   	      	File ff=new File(Environment.getExternalStorageDirectory(),"E-homeland/audio");
   	      	if(!ff.exists())
   	      	ff.mkdirs();
   		}	
   	}
	//播放聊天信息中的语音
    public void voiceClick(View v)
    {
    	System.out.println("被点击了");
    	if(v.getId()==R.id.iv_voice)
    	{
    		System.out.println("点击了语音播放"+v.getContentDescription());
    		final String audioPath=v.getContentDescription().toString();
			myvoice.startPlaying(audioPath);
    	}
    }
    /*
    //初始化xmpp连接类，创建和管家的聊天,以及处理接收到的消息
    public void initConnection()
    {
    	 connection = (XMPPConnection) ClientConServer.connection;
 	     chatmanger = connection.getChatManager();
 		//创建一个聊天
 		chat =chatmanger.createChat(Master,new MessageListener()
 		 {
 			public void processMessage(Chat arg0,
 	    			org.jivesoftware.smack.packet.Message arg1) {
 	    		// TODO Auto-generated method stub
 	    		System.out.println("argo="+arg0);
 	    		byte[] res=(byte[]) arg1.getProperty("content");
 	    		System.out.println("arg1="+res.length);
 	    		System.out.println("接收到消息");
 	    		TSBXDataStore.creatTsbxDir(manager);
 	    		TSBXDataStore.SaveChatInformation(manager, 1,res);
 	    	    viewhandler.obtainMessage(1,res).sendToTarget();
 			}
 		 });
    }
    //更新聊天信息的显示
   */
   public void init()
   {
	   /*
		Utils.handlerInput=new Handler(){
			@Override
			public void handleMessage(Message msg) {			
				if(msg.what==Utils.CLOSE_INPUT){//关闭软键盘
					imm.hideSoftInputFromWindow(MyProfileTsbxActivity.this.getCurrentFocus().getWindowToken(),  
	                 InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		};
		*/
	//imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	
	
	listview=(ListView)findViewById(R.id.listview);
	
	mAdapter=new ChatMsgAdapter(MyProfileTsbxActivity.this,mDataArrays);
	listview.setAdapter(mAdapter);
	listview.setSelection(listview.getCount() - 1);
   }
   //对声音按钮添加类似于微信那种，一直按着说话那种的样式
 
   //将声音文件发送到服务器当中
   public void sendVoiceToMannager(final byte[] res)
   {
	   Runnable run=new Runnable()
	   {
		@Override
		public void run() 
		{
			// TODO Auto-generated method stub
			   try {	
					org.jivesoftware.smack.packet.Message msg=new org.jivesoftware.smack.packet.Message();
					msg.setProperty("content",res);
					TSBXDataStore.creatTsbxDir(manager);
					TSBXDataStore.SaveChatInformation(manager, 0,res);
					chat.sendMessage(msg);
				} catch (XMPPException e) {
					// TODO Auto-generated catch block
					System.out.println("发送信息错误");
				}
		} 
	   };
	   new Thread(run).start();
   }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public void btnClick(View v) {
    	;
        
    }
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);	
		Log.d("TAG", "requestCode"+requestCode+" ,resultCode"+resultCode);
		if(requestCode ==Utils.GET_CAMERA && resultCode == Activity.RESULT_OK && null != data){
			   String sdState=Environment.getExternalStorageState();
			   if(!sdState.equals(Environment.MEDIA_MOUNTED)){
				 //  Toast t=Utils.showToast(getApplicationContext(), "未找到SDK", Toast.LENGTH_LONG);
				   //t.show();
				System.out.println("fdsfdsf");
			    Log.d("TAG", "sd card unmount");
			    return;
			   }
			   new DateFormat();
			   String name= DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA))+".jpg";
			   Bundle bundle = data.getExtras();
			   //获取相机返回的数据，并转换为图片格式
			   System.out.println("储存相片");
			   Bitmap bitmap;
			   String filename = null;
			   bitmap = (Bitmap)bundle.get("data");
			   FileOutputStream fout = null;
			   //定义文件存储路径
			   File file = new File(Environment.getExternalStorageDirectory(),"E-homeland/tsbx/camera");
			   if(!file.exists()){
				   file.mkdirs();
			   }
			   System.out.println("新建文件夹");
			   filename=file.getPath()+"/"+name;
			   try {
			    fout = new FileOutputStream(filename);
			    //对图片进行压缩
			    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fout);
			    System.out.println("输出文件" );
			   } catch (FileNotFoundException e) {
			    e.printStackTrace();
			   }finally{
			    try {
			     fout.flush();
			     fout.close();
			    } catch (IOException e) {
			     e.printStackTrace();
			    }
			   }
			   Log.d("TAG", "相片路径"+filename);
			   //去另一个页面查看图片
			   Intent intent=new Intent(MyProfileTsbxActivity.this,CameraActivity.class);
			   intent.putExtra("camera", filename);
			   MyProfileTsbxActivity.this.startActivityForResult(intent, Utils.SHOW_CAMERA);

			   
		}
		else if(requestCode==Utils.SHOW_CAMERA&&resultCode==Utils.SHOW_CAMERA_RESULT){
			Bundle bundle = data.getExtras();
			Object camera=bundle.get("imgUrl");
			Log.d("TAG", "需要发送照相的图片到服务器"+camera.toString());
			//将图片发送到聊天界面
			if(camera.toString().length()>0){
				sendMessage("["+camera.toString()+"]",true,0);
			}
			//将图片异步发送到服务器，并且把发送的图片显示到聊天框中
			
		}
		else if(requestCode==Utils.SHOW_ALL_PICTURE&&resultCode==Utils.SHOW_PICTURE_RESULT){
			Log.d("TAG", "需要将选择的图片发送到服务器");
			List<String> bmpUrls=new ArrayList<String>();		
			Bundle bundle = data.getExtras();
			Object[] selectPictures=(Object[]) bundle.get("selectPicture");
			for (int i = 0; i < selectPictures.length; i++) {
				System.out.println( "selectPictures[i]"+selectPictures[i]);
				String bmpUrl=ScaleImageFromSdcardActivity.map.get(Integer.parseInt(selectPictures[i].toString()));
				bmpUrls.add(bmpUrl);
				Log.d("TAG", "bmp"+bmpUrl);
				sendMessage("["+bmpUrl+"]",true,0);
			}
			Log.d("TAG", "bmps.size()"+bmpUrls.size());
			Toast.makeText(MyProfileTsbxActivity.this, "选择图片数"+selectPictures.length, Toast.LENGTH_LONG).show();
			//将图片发送到服务器	
		}
	}
    //flag用来区分发送的信息，0表示发送的文字和图片，1表示发送的是语音
    public void sendMessage(String text,boolean isNew,int flag)
    {
    	MessageInfo info=new MessageInfo();
    	info.setMsg_content(text);
    	info.setMsg_acceptTime(Utils.getNowDate());
    	if(text.length()>0)
    	{
    		ChatMsgEntity entity=new ChatMsgEntity();
    		entity.setDate(info.getMsg_acceptTime());
    		System.out.println(flag);
    		//设定是否有语音，从而决定那种布局
    		entity.setMsgType(false);
    		entity.setSendStatus(isLogin);
    		if(flag==1)
    		{
    		    entity.setIsVoice(true);
    		    entity.setAudioPath(myvoice.getAudioPath());
    		}
    		else
    		{
    			entity.setIsVoice(false);
    		}
    		SpannableString ss=Tools.DecodeSendMessage(text,MyProfileTsbxActivity.this);
    		entity.setText(ss);
    		mDataArrays.add(entity);
    		mAdapter.notifyDataSetChanged();
    		listview.setSelection(listview.getCount() - 1);
    	//	et_sendMessage.setText("");
    	}
    	if(isLogin)
    	{
    		if(flag==0)
    		{
    			SendMsg=text;
    			process.setVisibility(View.VISIBLE);
    			//sendMessageToManager(text);
    			new Thread(senMsgRunnable).start();
    		}
    	}	
    }
    //发送消息的异步处理handler
    Handler senMsgHandler=new Handler()
    {
    	public void handleMessage(Message msg) {
    	   if(msg.what==1)
    	   {
    		   process.setVisibility(View.GONE);
    	   }
    	   else
    	   {
    		   process.setVisibility(View.GONE);
    		   Toast.makeText(MyProfileTsbxActivity.this,"消息发送失败",Toast.LENGTH_SHORT).show();
    	   }
    	}
    };
    //发送消息的Runnable
    Runnable senMsgRunnable=new Runnable()
    {
    	public void run()
    	{
			sendMessageToManager(SendMsg);
    	}
    };
    //将信息发送至服务器
    public void sendMessageToManager(String content)
    {
    	if(MsgSendToManager.isImage)
    	{
    		MsgSendToManager.isImage=false;
    		HashMap<String,String> map=new HashMap<String,String>();
	    	System.out.println("user="+Constants.username+"Cum"+Constants.Community);
			map.put("user",Constants.username);
			map.put("community",Constants.Community);
		    FormFile formfile = new FormFile("chat.jpeg",new File(MsgSendToManager.imagePath), "jpeg", "image/jpeg");  
	        try{
					//UploadUtil.uploadFile(uploadFile, "http://192.168.1.125:3000/publishInformation");
	         	SocketHttpRequester.post(IP.ip+":3000/backstage_response/top11",map, formfile);
			}
	        catch(Exception e)
	        {
	         	senMsgHandler.obtainMessage(0).sendToTarget();
	        	e.printStackTrace();
	        }
    	}
 		 try {
 			org.jivesoftware.smack.packet.Message msg=new org.jivesoftware.smack.packet.Message();
 			//发送图片和文字本质上都是字符串，放到Tools类中进行解析，图片的格式为[图片url]
 			byte[] res=Tools.StringToBytes(content);
 			TSBXDataStore.creatTsbxDir(manager);
	    	TSBXDataStore.SaveChatInformation(manager, 1,res);
 			msg.setProperty("content",res);
 			chat.sendMessage(msg);
         	senMsgHandler.obtainMessage(1).sendToTarget();
 		} catch (XMPPException e) {
         	senMsgHandler.obtainMessage(0).sendToTarget();
 			// TODO Auto-generated catch block
 			System.out.println("发送信息错误");
 		}
    }
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		MyProfileTsbxActivity.this.finish();
		super.onDestroy();
		
	//	this.unregisterReceiver(offLineListener);
		//this.unregisterReceiver(net);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		/*
		System.out.println("向本地服务器发送");
	    File f=new File(Environment.getExternalStorageDirectory(),"E-homeland/sendManager/");
	    if(!f.exists())
	    	f.mkdirs();
	    File sendfile=new File(f.getAbsolutePath()+Utils.getNowDate()+".txt");
	    try {
			FileWriter fw=new FileWriter(sendfile);
			for(int i=0;i<MsgSendToManager.msgList.size();i++)
			{
				String str=MsgSendToManager.msgList.get(i);
				fw.write(str);
				fw.write("\n");
			}
			fw.flush();
			fw.close();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
		HashMap<String,String> map=new HashMap<String,String>();
    	System.out.println("user="+Constants.username+"Cum"+Constants.Community);
		map.put("user",Constants.username);
		map.put("community",Constants.Community);
	    FormFile formfile = new FormFile("chat.txt",sendfile, "txt", "image/jpeg");  
        try{
				//UploadUtil.uploadFile(uploadFile, "http://192.168.1.125:3000/publishInformation");
         	SocketHttpRequester.post(IP.ip+":3000/backstage_response/top11",map, formfile);
         	MsgSendToManager.msgList.clear();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		*/
	}
	public static boolean isNetworkConnected(Context context) {  
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
	public  void saveImage(View v)
	{
		;
	}
}

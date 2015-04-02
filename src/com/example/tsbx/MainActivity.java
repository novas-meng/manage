package com.example.tsbx;


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
import com.example.bbm.bbmHomePage;
import com.example.logIn.Constants;
import com.example.manage.R;
import com.example.pet.petActivity;
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
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity {
   boolean flag=false;
   ProgressBar process;
   EditText et_sendMessage;
   RelativeLayout ll_facechoose;
   InputMethodManager imm;
   String filename;
   ImageButton voice;
   ImageButton btn_face;
   private ChatMsgAdapter mAdapter;
   private ListView listview;
    MyVoice myvoice=new MyVoice("myaudio");
    XMPPConnection connection;
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
	RelativeLayout layout,layout1,layout2;
	ImageView image1;
	NetStateListener net=new NetStateListener();
    @SuppressLint("NewApi") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tsbx_activity_main);
        
        
        //��layout���ͼƬ
       layout =(RelativeLayout)findViewById(R.id.chatView);
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		InputStream is = getResources().openRawResource(
				 R.drawable.yy_background );
		Bitmap bm = BitmapFactory.decodeStream(is, null, opt);
		BitmapDrawable bd = new BitmapDrawable(getResources(), bm);
		layout.setBackgroundDrawable(bd);
        layout.setDrawingCacheEnabled(true);
		
		  layout1=(RelativeLayout)findViewById(R.id.rl_layout);
			is = getResources().openRawResource(
					 R.drawable.bbm_title_background );
			 bm = BitmapFactory.decodeStream(is, null, opt);
			 bd = new BitmapDrawable(getResources(), bm);
			layout1.setBackgroundDrawable(bd);
	        layout1.setDrawingCacheEnabled(true);

			image1=(ImageView)findViewById(R.id.tsbx_right_btn);
			is = getResources().openRawResource(
					 R.drawable.tsbx_wenzi );
			 bm = BitmapFactory.decodeStream(is, null, opt);
			 bd = new BitmapDrawable(getResources(), bm);
			 image1.setBackgroundDrawable(bd);
		        image1.setDrawingCacheEnabled(true);

			  layout2=(RelativeLayout)findViewById(R.id.rl_bottom);
				is = getResources().openRawResource(
						 R.drawable.face_background );
				 bm = BitmapFactory.decodeStream(is, null, opt);
				 bd = new BitmapDrawable(getResources(), bm);
				layout2.setBackgroundDrawable(bd);
		        layout2.setDrawingCacheEnabled(true);

    	Button tsbx=(Button)findViewById(R.id.tsbx_ret_button);
    	tsbx.setOnClickListener(new OnClickListener() {
    			
    			@Override
    			public void onClick(View arg0) {
    				// TODO �Զ����ɵķ������
    				//startActivity(new Intent(getApplicationContext(),AroundActivity.class));
    				MainActivity.this.finish();
    			}
    		});
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
      addUserImage();
      addButlerImage();
     process=MyProgressBar.createProgressBar(MainActivity.this,null);
     process.setVisibility(View.GONE);
      //  Intent intent=this.getIntent();
       // UserTalkTo=intent.getStringExtra("userTo");
     //    Tools.getLocalMessages(manager,MainActivity.this);
       // Intent startServiceIntent=new Intent(this,NetStateService.class);
        //startService(startServiceIntent);
      Button button=(Button)findViewById(R.id.tsbx_phone);
      button.setContentDescription(Constants.butlerphonenumber);
        Intent intent=this.getIntent();
        boolean loginStatus=intent.getBooleanExtra("LoginStatus",true);
        System.out.println("LoginStatus="+loginStatus);
        if(!loginStatus)
        {
        	isLogin=false;
        	Toast.makeText(this,"��¼ʧ�ܣ�����һ������״̬",Toast.LENGTH_SHORT).show();
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
      /*
        ConnectivityManager con=(ConnectivityManager)getSystemService(Activity.CONNECTIVITY_SERVICE);  
        boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();  
       // boolean internet=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();  
        if(wifi){  
            //ִ����ز���  
        	NetisOk=true;
        }else{  
            Toast.makeText(getApplicationContext(),  
                    "�ף���������ô��", Toast.LENGTH_LONG)  
                    .show();  
        }  
        */
        //NetisOk=true;
        creatAppDir();
        init();
        freshView();
     //   if(NetisOk)
      
        initNetStateListener();
        listview.setOnItemClickListener(new MyItemClickListener());
      //  myvoice=new MyVoice(filename);
		//myvoice.startRecording();
        if(isLogin)
        {
        	System.out.println("main������ȡ������Ϣ����");
            initConnection();
        	connection = (XMPPConnection) ClientConServer.connection;
           // offlineManager=new OffLineMessageManager(connection,MainActivity.this);
	        //offlineManager.getOfflineMessages();
        } 
	    //IntentFilter filter=new IntentFilter();
		//filter.addAction("OffLineMessage");
	//	filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
	//	offLineListener=new MyOffLineMessageReceiver();
		//this.registerReceiver(offLineListener, filter);
		
		//this.registerReceiver(net, filter);
    }
    //����״̬�������࣬�����ߵ�ʱ������Զ���¼
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
			    		 ClientConServer ccs = new ClientConServer(MainActivity.this);
			 			boolean loginStatus = ccs.login(Constants.username,Constants. password, 
			 					"113.6.252.157",5222);
			 			if(loginStatus)
			 			{
			 				initConnection();
				        	connection = (XMPPConnection) ClientConServer.connection;
				            offlineManager=new OffLineMessageManager(connection,MainActivity.this);
					        offlineManager.getOfflineMessages();
					        System.out.println("��¼״̬"+loginStatus);
					        isLogin=true;
			 			}			 			
			    	 }
			     }
			}	
    	};
    }
    public void addUserImage()
    {
    	/*
    	if(Constants.userHasImg)
    	{
    		SharedPreferences mySharedPreferences = getSharedPreferences("user", Activity.MODE_PRIVATE);
        	String img = mySharedPreferences.getString("imageUrl", "");
        	if(img.equals("[]")){
        		//û���û�ͷ��
        	}else{
        		if(img.length()>=2){
        			img = img.substring(2, img.length()-2);
        			img = IP.ip+":3000/uploads/"+img;
        			//�������ȡͼƬ ��ַΪimg
        			System.out.println("�û�ͷ���ַΪ="+img);
        			userhead=getBitmapFromServer(img);
        		}
        	}
    	}
    	else
    	{
    	*/
    		userhead=BitmapFactory.decodeResource(this.getApplicationContext().getResources(),R.drawable.yonghu);
    	//}
    }
    public void addButlerImage()
    {
    	/*
    	if(Constants.butlerHasImg)
    	{
    		String img=Constants.butlerImg;
    		img = img.substring(2, img.length()-2);
			img = IP.ip+":3000/uploads/"+img;
			//�������ȡͼƬ ��ַΪimg
    		System.out.println("�ܼ�ͷ��ĵ�ַΪ="+img);
			butlerhead=getBitmapFromServer(img);
    	}
    	else
    	{
    	*/
    	butlerhead = BitmapFactory.decodeResource(this.getApplicationContext().getResources(), R.drawable.kefu);
    	//}
    	
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
    //������Ϣ�Ľ�����
    class MyOffLineMessageReceiver extends BroadcastReceiver
    {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			if(arg1.getAction().equals("OffLineMessage"))
			{
				ArrayList<String> offLineMessageList=arg1.getStringArrayListExtra("OffLineMessage");
				System.out.println("�յ���������Ϣ����Ϊ"+offLineMessageList.size());
				//offline.showOffLineNotification(offLineMessageList, MainActivity.this,LoginActivity.class);
				showOffLineNotice(offLineMessageList);
			}
			 if (arg1.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
	                Log.d("mark", "����״̬�Ѿ��ı�");
	                connectivityManager = (ConnectivityManager)      

	                                         getSystemService(Context.CONNECTIVITY_SERVICE);
	                info = connectivityManager.getActiveNetworkInfo();  
	                if(info != null && info.isAvailable()) {
	                    String name = info.getTypeName();
	                    Log.d("mark", "��ǰ�������ƣ�" + name);
	                } else {
	                    Log.d("mark", "û�п�������");
	                }
	            }
		}
    }
    //����ȡ�����ߵ���Ϣ��ʾ��֪ͨ����
    public void showOffLineNotice(ArrayList<String> list)
    {
    	noticeManager=(NotificationManager)MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
		if(list.size()>0)
		{
			notice=new Notification();
			notice.icon=R.drawable.chatchat;
			notice.tickerText=list.get(list.size()-1);
			notice.defaults=Notification.DEFAULT_SOUND;
		    notice.flags = Notification.FLAG_AUTO_CANCEL;  

	    	Intent intent=new Intent(MainActivity.this,MainActivity.class);
	    	PendingIntent pi=PendingIntent.getActivity(MainActivity.this,0,intent, PendingIntent.FLAG_ONE_SHOT);
	    	notice.setLatestEventInfo(MainActivity.this, "����"+list.size()+"������Ϣ,����"+Constants.butlername,list.get(list.size()-1),pi);
	    	noticeManager.notify(1,notice);
		}
    }

    public void creatAppDir()
   	{
   		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
   		{
   			System.out.println("����app�ļ���");
   			File f=new File(Environment.getExternalStorageDirectory(),"E-homeland");
   			if(!f.exists())
   	      	f.mkdirs();
   	      	File ff=new File(Environment.getExternalStorageDirectory(),"E-homeland/audio");
   	      	if(!ff.exists())
   	      	ff.mkdirs();
   		}	
   	}
	//����������Ϣ�е�����
    public void voiceClick(View v)
    {
    	System.out.println("�������");
    	if(v.getId()==R.id.iv_voice)
    	{
    		System.out.println("�������������"+v.getContentDescription());
    		final String audioPath=v.getContentDescription().toString();
    		//new Thread(new Runnable()
    		//{
				//@Override
				//public void run() {
					// TODO Auto-generated method stub
					myvoice.startPlaying(audioPath);
				//}
    	//	}).start();
    	}
    }
    //��ʼ��xmpp�����࣬�����͹ܼҵ�����,�Լ�������յ�����Ϣ
    public void initConnection()
    {
    	 connection = (XMPPConnection) ClientConServer.connection;
 	     chatmanger = connection.getChatManager();
 		//����һ������
 	     System.out.println("Master="+Master);
 		chat =chatmanger.createChat(Master,new MessageListener()
 		 {
 			public void processMessage(Chat arg0,
 	    			org.jivesoftware.smack.packet.Message arg1) {
 	    		// TODO Auto-generated method stub
 	    		System.out.println("argo="+arg0);
 	    		byte[] res=(byte[]) arg1.getProperty("content");
 	    		System.out.println("arg1="+res.length);
 	    		System.out.println("���յ���Ϣ");
 	    		TSBXDataStore.creatTsbxDir(manager);
 	    		TSBXDataStore.SaveChatInformation(manager, 1,res);
 	    	    viewhandler.obtainMessage(1,res).sendToTarget();
 			}
 		 });
    }
    //����������Ϣ����ʾ
    public void freshView()
    {
    	viewhandler=new Handler()
    	{
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if(msg.what==1)
				{
					System.out.println("��ʾ�յ�����Ϣ");
 	    			MessageInfo info=new MessageInfo();
 	        	   // info.setMsg_content(arg1.getBody());
 	                info.setMsg_acceptTime(Utils.getNowDate());
 	               ChatMsgEntity entity=new ChatMsgEntity();
 	      		   entity.setDate(info.getMsg_acceptTime());
 	      		   entity.setMsgType(true);
 	      		   entity.setSendStatus(isLogin);
 	      		  // String s=msg.obj.toString();
 	      		  byte[] s=(byte[])msg.obj;
 	      		  SpannableString ss=Tools.getDecodeResult(s,MainActivity.this);
 	      		  int type=Tools.decode(s);
 	      		  if(s[0]==1&&s[1]==1)
 	      		  {
 	      			  entity.setIsVoice(true);
 	      			  System.out.println("�������л�ȡ�������ļ�λ��Ϊ"+ss.toString());
 	      			  entity.setAudioPath(ss.toString());
 	      		  }
 	      		  else
 	      		  {
 	      			  System.out.println("type="+type);
 	      			  if(type==1)
 	      			  {
 	 	      			  entity.setImagePath(Constants.receiveImgPath);
                          entity.setHasImage(true);
 	      			  }	      			  
 	      			 entity.setText(ss);
 	      		  }
 	      		   mDataArrays.add(entity);
 	      		//mAdapter=new ChatMsgAdapter(MainActivity.this,mDataArrays);
 	      		//listview.setAdapter(mAdapter);
 	      		   mAdapter.notifyDataSetChanged();
					System.out.println("handler����");
					mAdapter.notifyDataSetChanged();
					mAdapter.notifyDataSetInvalidated();
					 listview.invalidateViews();
		 	       	listview.setSelection(listview.getBottom());
				}
			}   		
    	};
    }
   public void init()
   {
		Utils.handlerInput=new Handler(){
			@Override
			public void handleMessage(Message msg) {			
				if(msg.what==Utils.CLOSE_INPUT){//�ر������
					imm.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(),  
	                 InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		};
	imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	et_sendMessage=(EditText)findViewById(R.id.sendmessage);
	ll_facechoose=(RelativeLayout)findViewById(R.id.ll_facechoose);
	addVoiceListener();
	listview=(ListView)findViewById(R.id.listview);
	btn_face=(ImageButton) findViewById(R.id.btn_face);
	mAdapter=new ChatMsgAdapter(MainActivity.this,mDataArrays);
	listview.setAdapter(mAdapter);
	listview.setSelection(listview.getCount() - 1);
   }
   //��������ť���������΢�����֣�һֱ����˵�����ֵ���ʽ
   public void addVoiceListener()
   {
	   voice=(ImageButton)findViewById(R.id.chat_voice);
		   voice.setOnTouchListener(new OnTouchListener()
			{
				@Override
				public boolean onTouch(View arg0, MotionEvent event) {
					// TODO Auto-generated method stub
					switch(event.getAction())
					{
	                case MotionEvent.ACTION_DOWN:
						System.out.println("�ֻ�����");
						voice.setBackgroundResource(R.drawable.chat_voice_pressed);
					    filename=Utils.getNowDate();
					  //  byte[] ss=filename.getBytes();
					   // System.out.println("�ļ����Ĵ�СΪ"+ss.length);
						myvoice=new MyVoice(filename);
						Toast.makeText(MainActivity.this,"׼����",Toast.LENGTH_SHORT).show();
						Toast.makeText(MainActivity.this,"�뽲��",Toast.LENGTH_LONG).show();
					    myvoice.startRecording();
						break;
					case MotionEvent.ACTION_UP:
						System.out.println("̧����ָ");
						voice.setBackgroundResource(R.drawable.chat_voice_unpressed);
						myvoice.stopRecording();
						byte[] res=myvoice.getAudioBytes(filename);
						//myvoice.startPlaying();
						//TSBXDataStore.creatTsbxDir(manager);
					//	TSBXDataStore.SaveChatInformation(manager, 0,res);
						sendMessage("##",true,1);
					    if(isLogin)
						sendVoiceToMannager(res);
					   break;
					}
				return true;
				}
			});
	   }
   //�������ļ����͵�����������
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
					System.out.println("������Ϣ����");
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
        switch(v.getId())
        {
            case R.id.tsbx_phone:
            	Button b=(Button)findViewById(R.id.tsbx_phone);
            	Intent phoneintent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+b.getContentDescription()));  
                startActivity(phoneintent); 
                break;
            case R.id.iv_userhead:
        	System.out.println("���������");
        	break;
        	case R.id.btn_send:
        		String sendMsg=et_sendMessage.getText().toString();
        		if(sendMsg.length()>0)
        		{
        			sendMessage(sendMsg,true,0);
        		}
        		break;
        	case R.id.chat_picture:
    			Intent intent=new Intent(MainActivity.this,ScaleImageFromSdcardActivity.class);
    			MainActivity.this.startActivityForResult(intent, Utils.SHOW_ALL_PICTURE);
    			//�����л����������ұ߽��룬����˳�
    			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);  
        		// Intent intent = new Intent();  
                 /* ����Pictures����Type�趨Ϊimage */  
                 //intent.setType("image/*");  
                 /* ʹ��Intent.ACTION_GET_CONTENT���Action */  
                 //intent.setAction(Intent.ACTION_GET_CONTENT);   
                 /* ȡ����Ƭ�󷵻ر����� */  
                 //startActivityForResult(intent, 1);  
    			break;
        	case R.id.chat_camera:
    			//�������
    			Intent i = new Intent();
    			Intent intent_camera = getPackageManager()
    					.getLaunchIntentForPackage("com.android.camera");
    			if (intent_camera != null) {
    				i.setPackage("com.android.camera");
    			}
    			i.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
    			MainActivity.this.startActivityForResult(i, Utils.GET_CAMERA);
    			break;
        }
    }
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);	
		Log.d("TAG", "requestCode"+requestCode+" ,resultCode"+resultCode);
		if(requestCode ==Utils.GET_CAMERA && resultCode == Activity.RESULT_OK && null != data){
			   String sdState=Environment.getExternalStorageState();
			   if(!sdState.equals(Environment.MEDIA_MOUNTED)){
				 //  Toast t=Utils.showToast(getApplicationContext(), "δ�ҵ�SDK", Toast.LENGTH_LONG);
				   //t.show();
				//System.out.println("fdsfdsf");
			    Log.d("TAG", "sd card unmount");
			    return;
			   }
			   new DateFormat();
			   String name= DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA))+".jpg";
			   Bundle bundle = data.getExtras();
			   //��ȡ������ص����ݣ���ת��ΪͼƬ��ʽ
			   System.out.println("������Ƭ");
			   Bitmap bitmap;
			   String filename = null;
			   bitmap = (Bitmap)bundle.get("data");
			   FileOutputStream fout = null;
			   //�����ļ��洢·��
			   File file = new File(Environment.getExternalStorageDirectory(),"E-homeland/tsbx/camera");
			   if(!file.exists()){
				   file.mkdirs();
			   }
			   System.out.println("�½��ļ���");
			   filename=file.getPath()+"/"+name;
			   try {
			    fout = new FileOutputStream(filename);
			    //��ͼƬ����ѹ��
			    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fout);
			    System.out.println("����ļ�" );
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
			   Log.d("TAG", "��Ƭ·��"+filename);
			   //ȥ��һ��ҳ��鿴ͼƬ
			   Intent intent=new Intent(MainActivity.this,CameraActivity.class);
			   intent.putExtra("camera", filename);
			   MainActivity.this.startActivityForResult(intent, Utils.SHOW_CAMERA);

			   
		}
		else if(requestCode==Utils.SHOW_CAMERA&&resultCode==Utils.SHOW_CAMERA_RESULT){
		//else if(requestCode==1){
			//  Uri uri = data.getData(); 
	          //  String img_path = petActivity.getPath(getApplicationContext(), uri);
	           // Log.e("uri", uri.toString());   
	          //  bitmap = getimage(img_path);
			Bundle bundle = data.getExtras();
			Object camera=bundle.get("imgUrl");
	           // Object camera=img_path;
			Log.d("TAG", "��Ҫ���������ͼƬ��������"+camera.toString());
			//��ͼƬ���͵��������
			if(camera.toString().length()>0){
				Constants.imgPath=camera.toString();
				sendMessage("["+camera.toString()+"]",true,0);
			}
			//��ͼƬ�첽���͵������������Ұѷ��͵�ͼƬ��ʾ���������
			
		}
		else if(requestCode==Utils.SHOW_ALL_PICTURE&&resultCode==Utils.SHOW_PICTURE_RESULT){
			Log.d("TAG", "��Ҫ��ѡ���ͼƬ���͵�������");
			List<String> bmpUrls=new ArrayList<String>();		
			Bundle bundle = data.getExtras();
			Object[] selectPictures=(Object[]) bundle.get("selectPicture");
			for (int i = 0; i < selectPictures.length; i++) {
				System.out.println( "selectPictures[i]"+selectPictures[i]);
				String bmpUrl=ScaleImageFromSdcardActivity.map.get(Integer.parseInt(selectPictures[i].toString()));
				bmpUrls.add(bmpUrl);
				Log.d("TAG", "bmp"+bmpUrl);
				Constants.imgPath=bmpUrl;
				sendMessage("["+bmpUrl+"]",true,0);
			}
			Log.d("TAG", "bmps.size()"+bmpUrls.size());
			Toast.makeText(MainActivity.this, "ѡ��ͼƬ��"+selectPictures.length, Toast.LENGTH_LONG).show();
			//��ͼƬ���͵�������	
		}
	}
    //flag�������ַ��͵���Ϣ��0��ʾ���͵����ֺ�ͼƬ��1��ʾ���͵�������
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
    		//�趨�Ƿ����������Ӷ��������ֲ���
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
    			if(text.startsWith("[")&&text.endsWith("]"))
    			{
    				entity.setImagePath(Constants.imgPath);
    				entity.setHasImage(true);

    			}
    		}
    		SpannableString ss=Tools.DecodeSendMessage(text,MainActivity.this);
    		entity.setText(ss);
    		mDataArrays.add(entity);
    		mAdapter.notifyDataSetChanged();
    		listview.setSelection(listview.getCount() - 1);
    		et_sendMessage.setText("");
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
    //������Ϣ���첽����handler
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
    		   Toast.makeText(MainActivity.this,"��Ϣ����ʧ��",Toast.LENGTH_SHORT).show();
    	   }
    	}
    };
    //������Ϣ��Runnable
    Runnable senMsgRunnable=new Runnable()
    {
    	public void run()
    	{
    		try
    		{
    			sendMessageToManager(SendMsg);
    		}
    		catch(Exception e)
    		{
    			e.printStackTrace();

    		}
    	}
    };
    //����Ϣ������������
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
 			//����ͼƬ�����ֱ����϶����ַ������ŵ�Tools���н��н�����ͼƬ�ĸ�ʽΪ[ͼƬurl]
 			byte[] res=Tools.StringToBytes(content);
 			TSBXDataStore.creatTsbxDir(manager);
	    	TSBXDataStore.SaveChatInformation(manager,0,res);
 			msg.setProperty("content",res);
 		
 	 			chat.sendMessage(msg);
 		
         	senMsgHandler.obtainMessage(1).sendToTarget();
 		} catch (XMPPException e) {
         	senMsgHandler.obtainMessage(0).sendToTarget();
 			// TODO Auto-generated catch block
 			System.out.println("������Ϣ����");
 		}
    }
    //�鿴ͼƬ
    public void saveImage(View v)
	{
		if(v.getId()==R.id.tv_chatcontent)
		{
		  //  SpannableString ss=(SpannableString)v.getContentDescription();
			System.out.println("ͼƬΪ="+v.getContentDescription().toString());
			  if(v.getContentDescription().toString().startsWith("id=1"))
			  {
				 String saveimagepath=v.getContentDescription().toString().substring(4);
			     System.out.println("ͼƬ������Ϊ"+saveimagepath);
			     if(!saveimagepath.equals(null))
			     {
			    	 Intent intent=new Intent(MainActivity.this,ShowImageActivity.class);
					 intent.putExtra("filename",saveimagepath);
					 MainActivity.this.startActivityForResult(intent, Utils.SHOW_CAMERA);
			     }			
			  }
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("�򱾵ط���������");
	//	mDataArrays.clear();
	//	mAdapter.notifyDataSetChanged();
	    File f=new File(Environment.getExternalStorageDirectory(),"E-homeland/sendManager/");
	    if(!f.exists())
	    	f.mkdirs();
	    File sendfile=new File(f.getAbsolutePath()+"/"+Utils.getNowDate()+".txt");
	    try {
			FileWriter fw=new FileWriter(sendfile);
			for(int i=0;i<MsgSendToManager.msgList.size();i++)
			{
				String str=MsgSendToManager.msgList.get(i);
				System.out.println("д���ļ�������Ϊ="+str);
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
       layout.getDrawingCache().recycle();
       layout1.getDrawingCache().recycle();
       layout2.getDrawingCache().recycle();
       image1.getDrawingCache().recycle();
       this.finish();
		//this.unregisterReceiver(offLineListener);
		//this.unregisterReceiver(net);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
	}
    
}

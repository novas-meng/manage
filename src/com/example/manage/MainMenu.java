package com.example.manage;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.DefaultClientConnection;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import com.example.Around.AroundActivity;

import com.example.MyProfile.MyProfileActivity;
import com.example.Service.ServiceActivity;
import com.example.bbm.Utils;
import com.example.bbm.bbmHomePage;
import com.example.communitynews.CommonUri;
import com.example.communitynews.CommunityActivity;
import com.example.happyhome.HappyhomeActivity;
import com.example.infoPush.infoPushActivity;
import com.example.infoSearch.infoSearchActivity;
import com.example.infoSearch.lifeSearch;
import com.example.infoSearch.xxcxActivity;
import com.example.logIn.Constants;
import com.example.logIn.logInActivity;
import com.example.pet.petActivity;
import com.example.tools.IP;
import com.example.tools.RefreshableView;
import com.example.tools.RefreshableView.PullToRefreshListener;
import com.example.tools.VerticalScrollTextView;
import com.example.tsbx.ClientConServer;
import com.example.tsbx.MyProgressBar;
import com.example.user.UserRealInformationActivity;
import com.example.utils.MyApplication;
import com.example.utils.MyFloatView;
import com.example.xmlparser.MainMenuItem;
import com.example.xmlparser.MainMenuWhole;
import com.example.xmlparser.MainMenuXmlParser;
import com.example.xmlparser.Xml;
import com.example.yy.PushService;
import com.example.yy.YYHomePage;
import com.example.yy.YYMainActivity;
import com.example.yy.YypasscardActivity;

import android.annotation.SuppressLint;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.LauncherActivity.ListItem;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Settings.Secure;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class MainMenu extends ActivityGroup {
	private TextView tvSelectedItem = null;
	private RelativeLayout rlHeader = null;
	private RelativeLayout rlNewsMain = null;
	private LayoutParams params = null;
    Handler myhandler=null;
	ProgressBar process;
	private int itemWidth = 0;
	private int startX = 0;
	private ListView listview; 
	private Intent intent = null;
	private View vNewsMain = null;
	private List<listitem> list;
	static ArrayList<MainMenuItem> itemlist=new ArrayList<MainMenuItem>();
	//private WindowManager wm=null;
	//private WindowManager.LayoutParams wmParams=null;
	//private MyFloatView myfloatview=null;
	private String[] newsS=null;
	private String[] dates=null;
	private String[] titles={"小区头条","小区随拍","小区活动"};
	private String[] toptitles={"正在获取...","正在获取...","正在获取..."};
	private String[] msgs={"有0条新公告可以阅读","有0条新公告可以阅读","有0项新活动邀请您的参加"};	
	private Button imageview_sqgg=null;
	private Button imageview_tsbx=null;
	private Button imageview_xxfb=null;
	private Button imageview_shfw=null;
	private Button imageview_gjfw=null;
	private Button imageview_zbfw=null;
	private Button imageview_cwzj=null;
	private Button imageview_xfzj=null;
	private Button imageview_bbm=null;
	private Button imageview_yy=null;
	private Button imageview_bxts=null;
	private Button imageview_cxxx=null;
	private Button imageview_qd=null;
	private TextView communityview=null;
	private TextView ecoinview=null;
	private String community=null,userid=null;
	private RefreshableView refreshableView;
    private String mDeviceID;
    HandlerThread thread = new HandlerThread("MyHandlerThread"); 
    Handler mHandler;
   
    //Handler threadhandler1=new Handler();
   // Handler threadhandler2=new Handler();
  //  Handler threadhandler3=new Handler();
    public static int width,height;
	TextView myprofile;
    /** Called when the activity is first created. */
    @SuppressLint("NewApi") @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        //给layout添加图片
        LinearLayout layout=(LinearLayout)findViewById(R.id.mainmenu_mid1);
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		InputStream is = getResources().openRawResource(
				 R.drawable.mainmenu_mid1 );
		Bitmap bm = BitmapFactory.decodeStream(is, null, opt);
		BitmapDrawable bd = new BitmapDrawable(getResources(), bm);
		layout.setBackgroundDrawable(bd);
		
		 LinearLayout layout1=(LinearLayout)findViewById(R.id.mainmenu_mid2);
		  is = getResources().openRawResource(
					 R.drawable.mainmenu_mid2 );
			 bm = BitmapFactory.decodeStream(is, null, opt);
			 bd = new BitmapDrawable(getResources(), bm);
			layout1.setBackgroundDrawable(bd);
		
        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width=metrics.widthPixels;
        height=metrics.heightPixels; 
        community=Constants.Community;
        process=MyProgressBar.createProgressBar(MainMenu.this,null);
		process.setVisibility(View.GONE);
        mDeviceID = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);            

       // startService();
        communityview=(TextView)findViewById(R.id.community);
        ecoinview=(TextView)findViewById(R.id.EB);
        communityview.setText(Constants.Community);
        ecoinview.setText("您的E币:"+Constants.ecoin);
        thread.start(); 
	    mHandler = new Handler(thread.getLooper()); 
        refreshableView = (RefreshableView) findViewById(R.id.refreshable_view);  
        refreshableView.setOnRefreshListener(new PullToRefreshListener() {  
            @Override  
            public void onRefresh() {  
            //	thread = new HandlerThread("MyHandlerThread"); 
                  // new Thread(getnum1).start();  
                  //  thread.start(); 
            	    mHandler.post(getnum1);
            }  
        }, 0);  
      //  NewsXmlParser.setSlideTitles(toptitles);
        //new Thread(getnum2).start();
        //new Thread(getnum3).start();
       
          //  TextView mp=(TextView)findViewById(R.id.myprofile);
        //    mp.setOnClickListener(new OnClickListener() {		
			//	@Override
			//	public void onClick(View arg0) {
					// TODO Auto-generated method stub
				//	startActivity(new Intent(MainMenu.this,MyProfileActivity.class));
			//	}
			//});
            SharedPreferences sm=getSharedPreferences("user", MODE_PRIVATE);
            sm.edit().putString("community", Constants.Community).commit();
           // System.out.println("userid");
          //  Toast.makeText(getApplicationContext(),community, Toast.LENGTH_SHORT).show();
          //  System.out.println("communtity="+community);
            
           // new Thread(getnum1).start();
            mHandler.post(getnum1);
            imageview_qd=(Button)findViewById(R.id.qd);
            imageview_qd.setOnClickListener(new OnClickListener()
            {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					new Thread(checkqdrunnable).start();
				}
            
            });
            imageview_cxxx=(Button)findViewById(R.id.cxxx);
            imageview_cxxx.setOnClickListener(new OnClickListener()
            {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					//new Thread(checkqdrunnable).start();
				//	hideFloatView();
					Intent intent = new Intent();
					intent.setClass(MainMenu.this,xxcxActivity.class);
					startActivity(intent);
				}
            
            });
            
            imageview_yy=(Button)findViewById(R.id.yy);
            imageview_yy.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
				  //  Intent intent=new Intent(MainMenu.this,YYHomePage.class);
					Constants.type="YY";
			        new Thread(getButlerInfo).start();

					
				}
			});
            imageview_bbm=(Button)findViewById(R.id.bbm);
            imageview_bbm.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
				//	hideFloatView();
				//	wm.removeView(myfloatview);
					startActivity(new Intent(MainMenu.this,bbmHomePage.class));
				}
			});
            /*
            myprofile.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
				    startActivity(new Intent(MainMenu.this,MyProfileActivity.class));	
				}
			});
			*/
            imageview_sqgg=(Button)findViewById(R.id.sqgg);
            imageview_sqgg.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
				//	hideFloatView();
					CommunityActivity.setclass("notice");
				//	wm.removeView(myfloatview);
					Intent intent=new Intent(MainMenu.this,CommunityActivity.class);
					Bundle data=new Bundle();
					data.putString("json","");
					intent.putExtras(data);
					startActivity(intent);
				//	startActivity(new Intent(MainMenu.this,CommunityActivity.class));
				}
			});
            imageview_zbfw=(Button)findViewById(R.id.zbfw);
            imageview_zbfw.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
				//	wm.removeView(myfloatview);
				//	hideFloatView();
					startActivity(new Intent(MainMenu.this,AroundActivity.class));
				}
			});
            imageview_gjfw=(Button)findViewById(R.id.gjfw);
            imageview_gjfw.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					//wm.removeView(myfloatview);
					//hideFloatView();
			     startActivity(new Intent(MainMenu.this,ServiceActivity.class));
				}
			});
            imageview_xxfb = (Button)findViewById(R.id.xxfb);
            imageview_xxfb.setOnClickListener(new ImageView.OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					//wm.removeView(myfloatview);
				//	hideFloatView();
					Intent intent =new Intent();
					intent.setClass(MainMenu.this, infoPushActivity.class);
					startActivity(intent);
				}
            	
            });
            imageview_cwzj = (Button)findViewById(R.id.cwzj);
            imageview_cwzj.setOnClickListener(new ImageView.OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
				//	hideFloatView();
					//wm.removeView(myfloatview);
					Intent intent = new Intent();
					intent.setClass(MainMenu.this,petActivity.class);
					startActivity(intent);
				}
            	
            });
         
           imageview_shfw = (Button)findViewById(R.id.shfw);
           imageview_shfw.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			//	wm.removeView(myfloatview);
				//hideFloatView();
				Intent intent = new Intent();
				intent.setClass(MainMenu.this,infoSearchActivity.class);
				startActivity(intent);
			}
        	   
           });
           imageview_xfzj = (Button)findViewById(R.id.xfzj);
           imageview_xfzj.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//wm.removeView(myfloatview);
			//	hideFloatView();
				Intent intent = new Intent();
				intent.setClass(MainMenu.this, HappyhomeActivity.class);
				startActivity(intent);
			}
        	   
           });
           if(!isNetworkConnected(getApplicationContext()))
           {
        	   Toast.makeText(getApplicationContext(), "网络未连接", Toast.LENGTH_SHORT).show();
           }
        
        // 初始化控件
        try {
			setcontent(getList());
				initeViews();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	     //   creatFloatView();
	        addClickListener();
	        IntentFilter filter=new IntentFilter();
	        filter.addAction("XMLGet");
	       
	    }
	   /*
	   public void creatFloatView()
	   {
		   myfloatview=new MyFloatView(this);
		   myfloatview.setOnClickListener(new OnClickListener()
		   {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				System.out.println("点击悬浮窗");
				Toast.makeText(getApplicationContext(), "点击了悬浮窗",Toast.LENGTH_LONG).show();
				Constants.type="tsbx";
				process.setVisibility(View.VISIBLE);
		        new Thread(loginRunable).start();			
			}		   
		   });
		   myfloatview.setImageResource(R.drawable.mainmenu_voice);
		  // Bitmap bitmap=this.getResources(R.drawable.mainmenu_voice);
		   wm=(WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		   wmParams=((MyApplication)getApplication()).getLayoutParams();
		   wmParams.type=android.view.WindowManager.LayoutParams.TYPE_PHONE;
		   wmParams.format=PixelFormat.RGBA_8888;
		   wmParams.gravity=Gravity.RIGHT|Gravity.TOP;
		  wmParams.flags = android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		  
		   wmParams.x=0;
		   wmParams.y=0;
		   wmParams.width=android.view.WindowManager.LayoutParams.WRAP_CONTENT;
		   wmParams.height=android.view.WindowManager.LayoutParams.WRAP_CONTENT;
			System.out.println("width="+wmParams.width+" "+wmParams.height);

		   wm.addView(myfloatview,wmParams);
		   }
	   public void hideFloatView()
	   {
		 //  wmParams.width=0;
		  // wmParams.height=0;
			//System.out.println("width="+wmParams.width+" "+wmParams.height);
           myfloatview.setVisibility(View.GONE);
		  // wm.addView(myfloatview,wmParams);
		  // wm.updateViewLayout(myfloatview, wmParams);
	   }
	   */   
	    private void initeViews() {
	    	
	    //	intent = new Intent(MainMenu.this, TopicNews.class);
	    //	vNewsMain = getLocalActivityManager().startActivity(
	    			//"TopicNews", intent).getDecorView();
	    	//params = new LayoutParams(
	    		//	LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	    	//rlNewsMain = (RelativeLayout) findViewById(R.id.layout_news_main);
	    	//rlNewsMain.addView(vNewsMain, params);
	    	
	    	
	    
	    //	Xml x=new Xml("http://api.androidhive.info/music/music.xml",this);
	    	//Xml x=new Xml(IP.ip+":3000/indexControlCenter?community=",Constants.Community,this);
	    	//x.getXml();
	    	//InputStream is=x.getXmlStream();
	    	
	    }
    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
    	super.onDestroy();
		//wm.removeView(myfloatview);
	}
	/**
     * 获取屏幕的宽度
     * @return
     */
    public List<listitem> getList()
    {
    	
    	List<listitem> list=new ArrayList<listitem>();
    	for(int i=0;i<3;i++)
    	{
    		listitem item=new listitem();
    		item.setContent(msgs[i]);
    		item.setPic(R.drawable.mainmenu_toast);
    		item.setTitle(titles[i]);
    		list.add(item);
    	}
		return list;
    }
    public void setcontent(List<listitem> result)
    {
    	MainMenuAdapter adapter=new MainMenuAdapter(getApplicationContext(), result);
		// 为ListView设定适配器
    	listview=(ListView)findViewById(R.id.mainmenu_list);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(arg2==0)
				{  
					CommunityActivity.setclass("news");
					Intent intent=new Intent(MainMenu.this,CommunityActivity.class);
					Bundle data=new Bundle();
					data.putString("json",Constants.titles);
					intent.putExtras(data);
					startActivity(intent);
				}
				if(arg2==1)
				{   
					CommunityActivity.setclass("exposure");
					Intent intent=new Intent(MainMenu.this,CommunityActivity.class);
					Bundle data=new Bundle();
					data.putString("json",Constants.suipai);
					intent.putExtras(data);
					startActivity(intent);
					}
				if(arg2==2)
				{  
					CommunityActivity.setclass("activity");
					Intent intent=new Intent(MainMenu.this,CommunityActivity.class);
					Bundle data=new Bundle();
					data.putString("json",Constants.actiivty);
					intent.putExtras(data);
					startActivity(intent);					}
			}
		});
		adapter.notifyDataSetChanged();
    }
    private int getScreenWidth(){
    	WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int screenWidth = display.getWidth();
		return screenWidth;
    }
   private void addClickListener()
   {
	   imageview_bxts=(Button)findViewById(R.id.tsbx);
	   imageview_bxts.setOnClickListener(new OnClickListener()
	   {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Constants.type="tsbx";
			process.setVisibility(View.VISIBLE);
	        new Thread(loginRunable).start();
	        
			//Intent intent=new Intent(MainMenu.this,UserRealInformationActivity.class);
			//Bundle bundle=new Bundle();
			//bundle.putString("type","tsbx");
			//intent.putExtras(bundle);
			//startActivity(intent);
		}
		   
	   });
   }
   
	Runnable getnum1=new Runnable() {
		
		@Override
		public void run() {
			//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			//community="第一小区";
			//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	     //   System.out.println("community"+community);
	       String url=IP.ip+":3000/communitynew/getnewsid"+"?get=news&community="+community;

			// TODO Auto-generated method stub
			HttpClient httpclient=new DefaultHttpClient();
			HttpGet httpGet=new HttpGet(url);
			HttpResponse httpResponse;
			try {
				httpResponse = httpclient.execute(httpGet);
			
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			   {
			    //取得返回的字符串
			  String strResult = EntityUtils.toString(httpResponse.getEntity(),"utf-8");
			    //System.out.println(strResult+" bb");
			 
			    Message msg=new Message();
			    msg.what=0;
			    msg.obj=strResult;
			    Constants.titles=strResult;
			   // msg.sendToTarget();
			    handler.obtainMessage(0,strResult).sendToTarget();
			   }
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
	};
	Runnable getnum2=new Runnable() {
		
		@Override
		public void run() {
			//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			//community="第一小区";
			//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			String url=IP.ip+":3000/communitynew/getnewsid"+"?get=exposure&community="+community;

			// TODO Auto-generated method stub
			HttpClient httpclient=new DefaultHttpClient();
			HttpGet httpGet=new HttpGet(url);
			HttpResponse httpResponse;
			try {
				httpResponse = httpclient.execute(httpGet);
			
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			   {
			    //取得返回的字符串
			  String strResult = EntityUtils.toString(httpResponse.getEntity(),"utf-8");
			    //System.out.println(strResult+" bb");
			    Message msg=new Message();
			    msg.what=5;
			    msg.obj=strResult;
			   // msg.sendToTarget();
			    Constants.suipai=strResult;
			    handler.obtainMessage(2,strResult).sendToTarget();
			   }
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
	};
	Runnable getnum3=new Runnable() {
		
		@Override
		public void run() {
			//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			//community="第一小区";
			//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			String url=IP.ip+":3000/communitynew/getnewsid"+"?get=activity&community="+community;

			// TODO Auto-generated method stub
			HttpClient httpclient=new DefaultHttpClient();
			HttpGet httpGet=new HttpGet(url);
			HttpResponse httpResponse;
			try {
				httpResponse = httpclient.execute(httpGet);
			
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			   {
			    //取得返回的字符串
			  String strResult = EntityUtils.toString(httpResponse.getEntity(),"utf-8");
			    //System.out.println(strResult+" bb");
			   Constants.actiivty=strResult;
			    handler.obtainMessage(4,strResult).sendToTarget();
			   }
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
	};
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
	final Handler handler = new Handler(){
	    @SuppressWarnings("deprecation")
		@Override
	    public void handleMessage(Message msg) {

	    	if(msg.what==1)
	    		Toast.makeText(getApplicationContext(), "网络未连接", Toast.LENGTH_SHORT).show();
	    	else if(msg.what==3)
	    	{
	    		try {
	    			String res=msg.obj.toString();
	    			//System.out.println("userid="+res);
	    			Toast.makeText(getApplicationContext(),"userid="+res , Toast.LENGTH_SHORT).show();
					JSONObject jso= new JSONObject(res);
					
					community=jso.getString("community");
					if(community.equals(""))
						Toast.makeText(getApplicationContext(), "获取小区信息失败", Toast.LENGTH_SHORT).show();
					else {Toast.makeText(getApplicationContext(), community, Toast.LENGTH_SHORT);
					SharedPreferences sm=getSharedPreferences("user", MODE_PRIVATE);
					SharedPreferences.Editor editer=sm.edit();
					editer.putString("community", community);
					editer.commit();
					}
					} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		
	    	}
	    	else if(msg.what==0)
	    	{
	    	//	System.out.println("chen");
	    		String res=msg.obj.toString();
	    		 //System.out.println(res);
	    		//Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
	    		int num1=0;
	    		String num2="0",num3="0";
	    		try {
	    			//System.out.println(res);
					JSONArray rq= new JSONArray(res);
						num1=rq.length();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		msgs[0]="有"+num1+"条新公告可以阅读";
	    		//msgs[1]="有"+num2+"条新公告可以阅读";
	    		//msgs[2]="有"+num3+"条新活动邀请您参加";
	            setcontent(getList());
	            mHandler.removeCallbacks(getnum1);
	           // new Thread(getnum2).start();
	           // Thread k=new Thread(getnum2);
	            mHandler.post(getnum2);
				}
	    	else if(msg.what==2)
	    	{
	    		//System.out.println("chen");
	    		String res=msg.obj.toString();
	    		 //System.out.println(res);
	    		//Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
	    	
	    		int num2=0;
	    		try {
	    			//System.out.println(res);
					JSONArray rq= new JSONArray(res);
						num2=rq.length();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		
	    		msgs[1]="有"+num2+"条新公告可以阅读";
	    		
	            setcontent(getList());
	            mHandler.removeCallbacks(getnum2);
	          //  mHandler.post(getnum2);
	           // new Thread(getnum3).start();
	            mHandler.post(getnum3);
				} 
	    	else if(msg.what==4)
	    	{
	    		//System.out.println("chen");
	    		String res=msg.obj.toString();
	    		 //System.out.println(res);
	    		//Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
	    	
	    		int num3=0;
	    		try {
	    		//	System.out.println(res);
					JSONArray rq= new JSONArray(res);
						num3=rq.length();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		
	    		msgs[2]="有"+num3+"条新活动邀请您参加";
	    		
	            setcontent(getList());
	            System.out.println("终止thread");
	            mHandler.removeCallbacks(getnum3);
	         //  thread.stop(new Throwable());
	           // thread.quit();
	            refreshableView.finishRefreshing();  

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
				//	System.out.println("Constant.butlername="+Constants.butlername);
				//	System.out.println("Constant.phonenummber="+Constants.butlerphonenumber);
					//hideFloatView();
					Intent intent=new Intent(MainMenu.this,UserRealInformationActivity.class);
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
				Toast.makeText(MainMenu.this,"请检查一下网络连接",Toast.LENGTH_SHORT).show();
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
   
   //处理登录openfire的handler类
   Handler openfirehandler=new Handler()
	{
		public void handleMessage(Message msg) {
			if(msg.what==1)
			{
				Constants.loginStatus=true;
				process.setVisibility(View.GONE);
			//	hideFloatView();
				Intent intent=new Intent(MainMenu.this,UserRealInformationActivity.class);
			    Bundle bundle=new Bundle();
			    bundle.putString("type",Constants.type);
			    intent.putExtras(bundle);
				startActivity(intent);
			}
			else
			{
			    process.setVisibility(View.GONE);
				Toast.makeText(MainMenu.this,"亲您的网络不给力啊，请检查一下网络设置",Toast.LENGTH_SHORT).show();
				//process.setVisibility(View.GONE);
				//Intent intent = new Intent();
	        	//intent.setClass(MainMenu.this, HomePage.class);
	        	//startActivity(intent);	
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
				ClientConServer ccs = new ClientConServer(MainMenu.this);
				boolean loginStatus = ccs.login(Constants.username, Constants.password,"113.6.252.157",5222);
			    if(loginStatus)
			    { 	
			    	System.out.println("登录openfire success");
			    	openfirehandler.obtainMessage(1).sendToTarget();
			    	//Toast.makeText(getApplicationContext(), "登录成功",Toast.LENGTH_LONG).show();
			    }
			    else
			    {
  					//process.setVisibility(View.GONE);
			    	System.out.println("登录openfire wrong");
			    	openfirehandler.obtainMessage(0).sendToTarget();
			    	//Toast.makeText(getApplicationContext(), "登录失败",Toast.LENGTH_LONG).show(); 	
			    }
			}
		};
		
		
		
		Handler qdHandler=new Handler()
		{

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if(msg.what==0)
				{
					Toast.makeText(MainMenu.this,"签到失败",Toast.LENGTH_SHORT).show();
				}
				else
				{
					if(msg.obj.toString().equals("success"))
					Toast.makeText(MainMenu.this,"签到成功",Toast.LENGTH_SHORT).show();
					 ecoinview.setText("您的E币:"+(Integer.valueOf(Constants.ecoin)+10));
				}
			}
		};
		//签到线程
		Runnable qdrunnable = new Runnable(){
		    @Override
		    public void run() {
		    
		    	HttpClient client = new DefaultHttpClient();
				String currentdate=Utils.getCurrentTime().split("-")[2];
				try {
					 HttpPost httppost = new HttpPost(IP.ip+":3000/signin");   
					 List<NameValuePair> map = new ArrayList <NameValuePair>();
			         //map.add(new BasicNameValuePair("name",phone));
			        // System.out.println(phone);
			         map.add(new BasicNameValuePair("userid",Constants.username));
			         map.add(new BasicNameValuePair("signindate",currentdate));
					 httppost.setEntity(new UrlEncodedFormEntity(map));   				 
					 //client.execute(httppost);  
					HttpResponse response = client.execute(httppost);
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					String	back = EntityUtils.toString(response.getEntity());
					   qdHandler.obtainMessage(1,back).sendToTarget();
						} else 
						{
							qdHandler.obtainMessage(0).sendToTarget();
						}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

					qdHandler.obtainMessage(0).sendToTarget();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

					qdHandler.obtainMessage(0).sendToTarget();
				} 
		    }
		};
		Handler userinfoHandler=new Handler()
		{

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				//super.handleMessage(msg);
				if(msg.what==0)
				{
					Toast.makeText(MainMenu.this,"签到失败",Toast.LENGTH_SHORT).show();
				}
				else
				{
					try {
						JSONObject obj=new JSONObject((String)msg.obj);
						try
						{
							String date=obj.getString("signindate");
							String currentdate=Utils.getCurrentTime().split("-")[2];
							if(date.equals(currentdate))
							{
								Toast.makeText(MainMenu.this,"您今天已经签到了",Toast.LENGTH_SHORT).show();
							}
							else
							{
								new Thread(qdrunnable).start();
							}
						}
						catch(Exception e)
						{
							System.out.println("获取签到日期失败");
							new Thread(qdrunnable).start();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
			
		};
		//签到线程
		 Runnable checkqdrunnable=new Runnable()
		    {
				@Override
				public void run() {
				System.out.println("获取用户的信息"+Constants.username);
					// TODO Auto-generated method stub
					//String httpUrl =IP.ip+":3000/user/getinfo?userid="+Constants.username;
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
		@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			System.out.println("停止thread");
            //thread.quit();
			super.onResume();
		//	myfloatview.setVisibility(View.VISIBLE);
		//	System.out.println("on resume");
			//  wmParams.width=android.view.WindowManager.LayoutParams.WRAP_CONTENT;
			  // wmParams.height=android.view.WindowManager.LayoutParams.WRAP_CONTENT;
			   //wm.updateViewLayout(myfloatview,wmParams);
		}
		public boolean onKeyDown(int keyCode, KeyEvent event) {  
	        if (keyCode == KeyEvent.KEYCODE_BACK) 
	        { 
	        	new AlertDialog.Builder(this)
	            .setMessage("确定退出?")
	            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
	            setResult(RESULT_OK);//确定按钮事件
	            System.exit(0);
	            finish();
	            }
	            })
	            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
	             //取消按钮事件
	            }
	            })
	            .show();
	        }
			return true;
	        }
}


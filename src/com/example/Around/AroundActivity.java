package com.example.Around;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsoluteLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

import com.example.tools.IP;
import com.example.tools.VerticalScrollTextView;
import com.example.logIn.Constants;
import com.example.manage.R;
import com.example.manage.R.layout;

public class AroundActivity extends Activity {
	private String community;
	String res;
	String	BASE_URL=IP.ip+":3000/";
	ImageView ms;
	ImageView yl;
	ImageView gw;
	ImageView bg;
	ImageView qt;
	ImageView fb;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.around);
		
		  //给layout添加图片
        LinearLayout layout=(LinearLayout)findViewById(R.id.container);
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		InputStream is = getResources().openRawResource(
				 R.drawable.around_bg );
		Bitmap bm = BitmapFactory.decodeStream(is, null, opt);
		BitmapDrawable bd = new BitmapDrawable(getResources(), bm);
		layout.setBackgroundDrawable(bd);
		
		  LinearLayout layout1=(LinearLayout)findViewById(R.id.aroundlayout1);
			is = getResources().openRawResource(
					 R.drawable.bar_bg );
			 bm = BitmapFactory.decodeStream(is, null, opt);
			 bd = new BitmapDrawable(getResources(), bm);
			layout1.setBackgroundDrawable(bd);
		
			  LinearLayout layout2=(LinearLayout)findViewById(R.id.aroundlayout2);
				is = getResources().openRawResource(
						 R.drawable.top_bg );
				 bm = BitmapFactory.decodeStream(is, null, opt);
				 bd = new BitmapDrawable(getResources(), bm);
				layout2.setBackgroundDrawable(bd);
			
		ms=(ImageView)findViewById(R.id.ms);
		ms.setDrawingCacheEnabled(true);
		is = getResources().openRawResource(
				 R.drawable.food );
		 bm = BitmapFactory.decodeStream(is, null, opt);
		 bd = new BitmapDrawable(getResources(), bm);
		 ms.setBackgroundDrawable(bd);
		
		yl=(ImageView)findViewById(R.id.yl);
		yl.setDrawingCacheEnabled(true);

		is = getResources().openRawResource(
				 R.drawable.enterm );
		 bm = BitmapFactory.decodeStream(is, null, opt);
		 bd = new BitmapDrawable(getResources(), bm);
		 yl.setBackgroundDrawable(bd);
		
		gw=(ImageView)findViewById(R.id.gw);
		gw.setDrawingCacheEnabled(true);

		is = getResources().openRawResource(
				 R.drawable.shop );
		 bm = BitmapFactory.decodeStream(is, null, opt);
		 bd = new BitmapDrawable(getResources(), bm);
		 gw.setBackgroundDrawable(bd);
		
		bg=(ImageView)findViewById(R.id.bg);
		bg.setDrawingCacheEnabled(true);

		is = getResources().openRawResource(
				 R.drawable.hotal );
		 bm = BitmapFactory.decodeStream(is, null, opt);
		 bd = new BitmapDrawable(getResources(), bm);
		 bg.setBackgroundDrawable(bd);
		 
		 
		qt=(ImageView)findViewById(R.id.qt);
		qt.setDrawingCacheEnabled(true);

		is = getResources().openRawResource(
				 R.drawable.extra );
		 bm = BitmapFactory.decodeStream(is, null, opt);
		 bd = new BitmapDrawable(getResources(), bm);
		 qt.setBackgroundDrawable(bd);
		 
		 
		fb=(ImageView)findViewById(R.id.fb);
		fb.setDrawingCacheEnabled(true);

		is = getResources().openRawResource(
				 R.drawable.publish );
		 bm = BitmapFactory.decodeStream(is, null, opt);
		 bd = new BitmapDrawable(getResources(), bm);
		 fb.setBackgroundDrawable(bd);
		 
		SharedPreferences mySharedPreferences = getSharedPreferences("user", Activity.MODE_PRIVATE); 
		community=mySharedPreferences.getString("community", "");
		TextView com=(TextView)findViewById(R.id.community);
		TextView ecoin=(TextView)findViewById(R.id.EB);
		com.setText(Constants.Community);
		ecoin.setText("您的E币:"+Constants.ecoin);
        ms.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				AroundAdditionActivity.setclass("food");
				startActivity(new Intent(AroundActivity.this,AroundAdditionActivity.class));
			}
		});
       yl.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
		AroundAdditionActivity.setclass("entertainment");
		startActivity(new Intent(AroundActivity.this,AroundAdditionActivity.class));
	}
     });
      gw.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		AroundAdditionActivity.setclass("shop");
		startActivity(new Intent(AroundActivity.this,AroundAdditionActivity.class));
	}
    });
      bg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AroundAdditionActivity.setclass("hotal");
				startActivity(new Intent(AroundActivity.this,AroundAdditionActivity.class));
			}
		});
      qt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AroundAdditionActivity.setclass("extra");
				startActivity(new Intent(AroundActivity.this,AroundAdditionActivity.class));
			}
		});
      fb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(AroundActivity.this,AroundPublishActivity.class));
			}
		});
		if (savedInstanceState == null) {
			
		}
		//new Thread(runnable1).start();
	}
	Handler handler = new Handler(){
	    @Override
	    public void handleMessage(Message msg) {
	        super.handleMessage(msg);
	        Bundle data = msg.getData();
	        
	        if(msg.what==1)
	    		Toast.makeText(getApplicationContext(), "网络未连接", Toast.LENGTH_SHORT).show();
	       // System.out.println("hello"+val);
	        //Log.i("mylog","请求结果-->" + val);
	        else if(msg.what==2)
	    	{
	    		//Toast.makeText(getApplicationContext(), "news", Toast.LENGTH_SHORT).show();
	    		res=(String)msg.obj;
	    		String newscontent="";
	    		try { 
	    			System.out.println(res);
					JSONArray newsarray=new JSONArray(res);
					for(int i=0;i<newsarray.length();i++)
					{
					JSONObject newsobject=newsarray.getJSONObject(i);
					String ss=newsobject.getString("title")+":"+newsobject.getString("content");
					System.out.println("ss="+ss);
					newscontent=newscontent+'\n'+(i+1)+"."+ss;
		    		
		    		}
					VerticalScrollTextView v=new VerticalScrollTextView(AroundActivity.this);
		    		LinearLayout news=(LinearLayout)findViewById(R.id.news);
		    		
					LinearLayout.LayoutParams pm=new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		    		pm.gravity=Gravity.CENTER;
		    		pm.setMargins(20, 20, 20, 20);
		    		v.setLayoutParams(pm);
		    		v.setTextSize(20);
		    		v.setText(newscontent);
		    		news.addView(v);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		
	    		
	    	}
	        
	    }
	};
	Runnable runnable1 = new Runnable(){
	    
	    URL url;
	    InputStream is;
	    String strResult="123";
	    public void run() {
	        //
	    	String	url=BASE_URL+"news/getnews?get=news&community="+community;
	    	

	    	HttpGet getMethod = new HttpGet(url);  
			
			if(isNetworkConnected(getApplicationContext())==false)
				//Toast.makeText(getApplicationContext(), "网络未连接", Toast.LENGTH_SHORT).show();
				{
				Message msg=new Message();
		        msg.what=1;
		        msg.obj="noNet";
		        handler.obtainMessage(1,"noNet").sendToTarget();
		        }
			else
			try
			  {
				
			   //取得HttpClient对象
			   HttpClient httpclient = new DefaultHttpClient();
			   //请求HttpClient，取得HttpResponse
			   HttpResponse httpResponse = httpclient.execute(getMethod);
			   //请求成功
			   if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			   {
			    //取得返回的字符串
			   strResult = EntityUtils.toString(httpResponse.getEntity(),"utf-8");
			    System.out.println(strResult+" aa");
			    Message msg=new Message();
			    msg.what=2;
			    msg.obj=strResult;
			   // msg.sendToTarget();
			    handler.obtainMessage(2,strResult).sendToTarget();
			   }
			   
			  }
			  catch (ClientProtocolException e)
			  {
			   ;
			  }
			  catch (IOException e)
			  {
			   ;
			  }
			  catch (Exception e)
			  {
			   ;
			  }  
	    }
	};
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
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
	   ms.getDrawingCache().recycle();
       yl.getDrawingCache().recycle();
       gw.getDrawingCache().recycle();
       bg.getDrawingCache().recycle();
       qt.getDrawingCache().recycle();
       fb.getDrawingCache().recycle();
     	AroundActivity.this.finish();
		super.onDestroy();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK) {  
        	
        	 ms.getDrawingCache().recycle();
              yl.getDrawingCache().recycle();
            gw.getDrawingCache().recycle();
            bg.getDrawingCache().recycle();
           qt.getDrawingCache().recycle();
         fb.getDrawingCache().recycle();
        	AroundActivity.this.finish();
        	
            return true;   
        } else  
            return super.onKeyDown(keyCode, event);  
    }

}

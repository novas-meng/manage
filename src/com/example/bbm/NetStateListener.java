package com.example.bbm;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class NetStateListener extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-generated method stub
		/*
		 if (arg1.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
             Log.d("mark", "网络状态已经改变");
             ConnectivityManager   connectivityManager = (ConnectivityManager)      

                                      arg0.getSystemService(Context.CONNECTIVITY_SERVICE);
             NetworkInfo  info = connectivityManager.getActiveNetworkInfo();  
             if(info != null && info.isAvailable()) {
                 String name = info.getTypeName();
                 Log.d("mark", "当前网络名称：" + name);
             } else {
                 Log.d("mark", "没有可用网络");
             }
             */
		    ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
	        NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);  
	        NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);  
	        NetworkInfo activeInfo = manager.getActiveNetworkInfo();  
	       // Toast.makeText(context, "ssssmobile:"+mobileInfo.isConnected()+"\n"+"wifi:"+wifiInfo.isConnected()  
	      //                 +"\n"+"active:", 1).show();
	     //   System.out.println("mobileInfo="+mobileInfo);
	        if(mobileInfo!=null)
	        {
	        	if(mobileInfo.isConnected()||wifiInfo.isConnected())
		        {
		        //	if(!Constants.isLogin)
		        	//ChatActivity.netListenerHandler.obtainMessage(1).sendToTarget();
		        //	FriendListActivity.getUserListHandler.obtainMessage(1).sendToTarget();
		        }
		        if(!mobileInfo.isConnected()&&!wifiInfo.isConnected())
		        {
		        	//Constants.isLogin=false;
		        }
	        }
	        else
	        {
	        	if(wifiInfo.isConnected())
		        {
		        //	if(!Constants.isLogin)
		        //	ChatActivity.netListenerHandler.obtainMessage(1).sendToTarget();
		        //	FriendListActivity.getUserListHandler.obtainMessage(1).sendToTarget();
		        }
		        if(!wifiInfo.isConnected())
		        {
		        //	Constants.isLogin=false;
		        }
	        }
         }
	}



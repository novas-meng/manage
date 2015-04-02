package com.example.tsbx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.OfflineMessageManager;

import com.example.logIn.Constants;
import com.example.logIn.logInActivity;
import com.example.manage.R;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
public class OffLineMessageManager {
	 XMPPConnection connection;
	  NotificationManager noticeManager;
	  Notification notice;
	  Activity activity;
	  ArrayList<String> list=new ArrayList<String>();
	  String sender=new String();
	  public OffLineMessageManager(XMPPConnection connection,Activity activity)
	  {
		  this.connection=connection;
		  this.activity=activity;
	  }
	  public void showOffLineNotification(ArrayList<String> list,Context presentActivity,Class<Activity> class1)
	  {
			noticeManager=(NotificationManager)presentActivity.getSystemService(Context.NOTIFICATION_SERVICE);
			if(list.size()>0)
			{
				notice=new Notification();
				notice.icon=R.drawable.chatchat;
				notice.tickerText=list.get(list.size()-1);
				notice.defaults=Notification.DEFAULT_SOUND;
		    	Intent intent=new Intent(presentActivity,class1.getClass());
		    	PendingIntent pi=PendingIntent.getActivity(presentActivity,0,intent, PendingIntent.FLAG_ONE_SHOT);
		    	notice.setLatestEventInfo(presentActivity, "您有"+list.size()+"条新消息,来自jjj"+Constants.butlername,list.get(list.size()-1),pi);
		    	noticeManager.notify(1,notice);
			}
	  }
	  //获取离线消息的发送方，此方法必须在 getOfflineMessages之后执行
	  public String getOffLineMessageSender()
	  {
		  return sender.split("@")[0];
	  }
	  //获取离线消息的列表，会以广播的形式发送到指定的activity中，所以需要在指定activity中写出对应的
	  //广播接收类
	  public void getOfflineMessages()
	{
		final Handler handler=new Handler()
		{
			@Override
			public void handleMessage(android.os.Message msg) {
				// TODO Auto-generated method stub
				//super.handleMessage(msg);
				switch(msg.what)
				{
				case 0:
					System.out.println("没有离线消息");
					break;
				case 1:
					list=(ArrayList<String>)msg.obj;
					System.out.println("list.size="+list.size());
					Intent intent=new Intent();
					intent.putStringArrayListExtra("OffLineMessage",list);
					intent.setAction("OffLineMessage");
					activity.sendBroadcast(intent);
					/*
					noticeManager=(NotificationManager)ChatActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
					if(list.size()>0)
					{
						notice=new Notification();
						notice.icon=R.drawable.chatchat;
						notice.tickerText=list.get(list.size()-1);
						notice.defaults=Notification.DEFAULT_SOUND;
				    	Intent intent=new Intent(ChatActivity.this,MainActivity.class);
				    	PendingIntent pi=PendingIntent.getActivity(ChatActivity.this,0,intent, PendingIntent.FLAG_ONE_SHOT);
				    	notice.setLatestEventInfo(ChatActivity.this, "您有"+list.size()+"条新消息",list.get(list.size()-1),pi);
				    	noticeManager.notify(1,notice);
					}
				*/
				}
			}
			
		};
		Runnable run=new Runnable()
		{
			@Override
			public void run() {
				// TODO Auto-generated method stub
				 OfflineMessageManager offlineManager = new OfflineMessageManager(  
			                connection);  
			        try {  
			            Iterator<org.jivesoftware.smack.packet.Message> it = offlineManager  
			                    .getMessages();  
			            Map<String,ArrayList<Message>> offlineMsgs = new HashMap<String,ArrayList<Message>>();  
			              ArrayList<String> list=new ArrayList<String>();
			            while (it.hasNext()) {  
			                org.jivesoftware.smack.packet.Message message = it.next();  
			                System.out  
			                        .println("收到离线消息, Received from 【" + message.getFrom()  
			                                + "】 message: " + message.getBody());  
			               list.add(message.getBody());
			                String fromUser = message.getFrom().split("/")[0];  
			  
			                if(offlineMsgs.containsKey(fromUser))  
			                {  
			                    offlineMsgs.get(fromUser).add(message);  
			                }else{  
			                    ArrayList<Message> temp = new ArrayList<Message>();  
			                    temp.add(message);  
			                    offlineMsgs.put(fromUser, temp);  
			                }  
			            }  
			            Message msg=new Message();
			            handler.obtainMessage(1,list).sendToTarget();
			            //在这里进行处理离线消息集合......  
			            Set<String> keys = offlineMsgs.keySet();  
			            Iterator<String> offIt = keys.iterator();  
			            while(offIt.hasNext())  
			            {  
			                String key = offIt.next();  
			                ArrayList<Message> ms = offlineMsgs.get(key);  
			                System.out.println("key="+key);
			                sender=key;
			                System.out.println("value="+ms.toString());
			            }      
			            offlineManager.deleteMessages();  
			        } catch (Exception e) {  
			          e.printStackTrace();
			          Message msg=new Message();
			          handler.obtainMessage(0).sendToTarget();
			        }  
			        Presence presence = new Presence(Presence.Type.available);
			        try
			        {
					       connection.sendPacket(presence);
			        }
			        catch(Exception e)
			        {
			        	e.printStackTrace();
			        	try
			        	{
			        		ClientConServer ccs = new ClientConServer(activity);
						    ccs.login(Constants.username, Constants.password,"113.6.252.157",5222);
			        	}
			        	catch(Exception e1)
			        	{
			        		e1.printStackTrace();
			        	}
			        }
			}	
		};
		new Thread(run).start();
	}
}

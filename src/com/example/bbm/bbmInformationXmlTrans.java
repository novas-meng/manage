package com.example.bbm;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
//��xml���ת��Ϊlistview�е����
public class bbmInformationXmlTrans {
	String uri=null;
	String xml=null;
	byte[] b=null;
	InputStream is=null;
	private List<bbmInformationEntity> list;
	ArrayList<bbmInformationItem> itemlist=new ArrayList<bbmInformationItem>();
	Activity activity;
      public bbmInformationXmlTrans(String uri,Activity activity)
      {
    	  this.uri=uri;
    	  this.activity=activity;
      }
      public InputStream getXmlStream()
      {
    	  final Handler myhandler=new Handler()
  		{

  			@Override
  			public void handleMessage(Message msg) {
  				// TODO Auto-generated method stub
  				switch(msg.what)
  				{
  				case 0:
  					//System.out.println("��ȡurl����");
  					break;
  				case 1:
  					is=(InputStream)msg.obj;
  					
  					bbmInformationXML parser=new bbmInformationXML(activity);
  			        try {
  						parser.setXmlByInputStream(is);
  					} catch (Exception e1) {
  						// TODO Auto-generated catch block
  						e1.printStackTrace();
  					}
  			    	try
  			    	{
  			    		 //  System.out.println("���Խ���xml");
  			    			bbminformationWhole whole=parser.getbbminformationWhole();
  			    		//	System.out.println("����Ϊ"+whole.getInformationSize());
  			    			 itemlist=whole.getInformationList();
  			    			System.out.println("list.size="+itemlist.size());
  			    			Intent intent=new Intent();
  			    			intent.setAction("InformationUpdate");
  			    			StringBuilder ids=new StringBuilder();
  			    			StringBuilder contents=new StringBuilder();
  			    			StringBuilder phonenumbers=new StringBuilder();
  			    			for(int i=0;i<itemlist.size();i++)
  			    			{
  			    				ids.append(itemlist.get(i).getId()+"&&");
  			    				contents.append(itemlist.get(i).getContent()+"&&");
  			    				phonenumbers.append(itemlist.get(i).getPhoneNumber()+"&&");
  			    			}
  			    			intent.putExtra("id",ids.toString());
  			    			intent.putExtra("content", contents.toString());
  			    			intent.putExtra("phonenumber", phonenumbers.toString());
  			    			activity.sendBroadcast(intent);
  			    		//	for(int i=0;i<itemlist.size();i++)
  			    		//	{
  			    			//	System.out.println("���ǵ�"+i+"����Ϣ");
  			    			//	SlideImageLayout.setImage("http://image.tianjimedia.com/uploadImages/2014/266/47/040218M56ZD1_680x500.jpg");
  			    			//	System.out.println(itemlist.get(i).getContent());
  			    			//	System.out.println(itemlist.get(i).getId());
  			    			//	System.out.println(itemlist.get(i).getPhoneNumber());
  			    		//	}
  			    	}
  			    	catch(Exception e)
  			    	{
  			    		e.printStackTrace();
  			    		//System.out.println("����xml����");
  			    	}
  			    	
  					
  					break;
  				}
  			}
  			
  		};
  		 final Runnable myrun=new Runnable()
  	      {
  			@Override
  			public void run() {
  				// TODO Auto-generated method stub
  				 URL url;
					try {
						url = new URL(uri);
						 HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	    		    	  conn.setReadTimeout(5*1000);
	    		    	  conn.setRequestMethod("GET");
	    		    	  is= conn.getInputStream();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						myhandler.obtainMessage(0).sendToTarget();
					}
  		    	 myhandler.obtainMessage(1,is).sendToTarget();
  		    	 
  			}	  
  	      };
  	      new Thread(myrun).start();
return is;
      }
      
      
      
      public void getXml()
      {
    	  final Handler myhandler=new Handler()
    		{

    			@Override
    			public void handleMessage(Message msg) {
    				// TODO Auto-generated method stub
    				switch(msg.what)
    				{
    				case 0:
    					//System.out.println("��ȡurl����");
    					break;
    				case 1:
    					xml=(String)msg.obj;
    					System.out.println(xml);
    					break;
    				}
    			}
    			
    		};
    		 final Runnable myrun=new Runnable()
    	      {

    			@Override
    			public void run() {
    				// TODO Auto-generated method stub
    				 URL url;
					try {
						url = new URL(uri);
						 HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	    		    	  conn.setReadTimeout(5*1000);
	    		    	  conn.setRequestMethod("GET");
	    		    	  InputStream inStream = conn.getInputStream();
	    		    	b=new byte[inStream.available()];
	    		    	  inStream.read(b);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						myhandler.obtainMessage(0).sendToTarget();
					}
					System.out.println("获得的xml为"+new String(b,0,b.length));
    		    	 myhandler.obtainMessage(1,new String(b,0,b.length)).sendToTarget();
    		    	 
    			}
    	    	  
    	      };
    	      new Thread(myrun).start();
      }
     
}

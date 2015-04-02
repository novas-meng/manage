package com.example.xmlparser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.example.logIn.Constants;
import com.example.manage.MainMenu;
import com.example.manage.listitem;
import com.example.tools.IP;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

public class Xml {
	String uri=null;
	String xml=null;
	String arg=null;
	 Runnable myrun;
	byte[] b=null;
	InputStream is=null;
	private List<listitem> list;
	private Handler threadhandler=new Handler();
	ArrayList<MainMenuItem> itemlist=new ArrayList<MainMenuItem>();
	static Activity a;
      public Xml(String uri,String arg,Activity a)
      {
    	  this.uri=uri;
    	  this.a=a;
    	  this.arg=arg;
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
  					System.out.println("handler中接受到错误");
  					System.out.println(uri+"  "+arg+"  "+a);
  					// new Thread(myrun).start();
  					threadhandler.post(myrun);
  					;
  					break;
  				case 1:
  					is=(InputStream)msg.obj;
  					
  					MainMenuXmlParser parser=new MainMenuXmlParser(a);
  			        try {
  						parser.setXmlByInputStream(is);
  					} catch (Exception e1) {
  						// TODO Auto-generated catch block
  						e1.printStackTrace();
  					}
  			    	try
  			    	{
  			    		
  			    			MainMenuWhole whole=parser.getMainMenuWhole();
  			    			 itemlist=whole.getItemList();
  			    			System.out.println("list.size="+itemlist.size());
  			    			Intent intent=new Intent();
  			    			intent.setAction("XMLGet");
  			    			StringBuilder links=new StringBuilder();
  			    			StringBuilder contents=new StringBuilder();
  			    			
  			    			for(int i=0;i<itemlist.size();i++)
  			    			{
  			    				links.append(itemlist.get(i).getLink()+"&&");
  			    				contents.append(itemlist.get(i).getContent()+"&&");
  			    				
  			    			}
  			    			intent.putExtra("link",links.toString());
  			    			intent.putExtra("content", contents.toString());
  			    			
  			    			a.sendBroadcast(intent);
  			    			for(int i=0;i<itemlist.size();i++)
  			    			{
  			    			//	SlideImageLayout.setImage("http://image.tianjimedia.com/uploadImages/2014/266/47/040218M56ZD1_680x500.jpg");
  			    				
  			    				System.out.println(itemlist.get(i).getLink());
  			    				
  			    			}
  			    	}
  			    	catch(Exception e)
  			    	{
  			    		System.out.println("catch exception!!!");
  			    	}
  			    	
  					
  					break;
  				}
  			}
  			
  		};
  		
  		
  		 myrun=new Runnable()
  	      {
  			@Override
  			public void run() {
  				// TODO Auto-generated method stub
  				 URL url;
					try {
						arg=Constants.Community;
						System.out.println("成功获取到 "+uri+"参数为"+arg+"activity为"+a);
						String narg= URLEncoder.encode(arg, "utf-8");
						String  nuri=uri+narg;
						System.out.println(nuri);
						url = new URL(nuri);
						
						 HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	    		    	  conn.setReadTimeout(5*1000);
	    		    	  conn.setRequestMethod("GET");
	    		    	  is= conn.getInputStream();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						myhandler.obtainMessage(0).sendToTarget();
					}
  		    	 myhandler.obtainMessage(1,is).sendToTarget();
  		    	 
  			}	  
  	      };
  	    threadhandler.post(myrun);
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
    					break;
    				case 1:
    					xml=(String)msg.obj;
    					//System.out.println(xml);
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
    		    	 myhandler.obtainMessage(1,new String(b,0,b.length)).sendToTarget();
    		    	 
    			}
    	    	  
    	      };
    	      new Thread(myrun).start();
      }
      
     
}

package com.example.manage;

import java.io.File;
import java.net.URL;

import com.example.tools.IP;
import com.example.tools.ImageCompress;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class ImageUtils {
	private static String baseurl=IP.ip+":3000/uploads/";
	public static Bitmap getImage(final String uri)
	{
		 final Bitmap[] res=new Bitmap[1];
		final Handler myhandler=new Handler()
	   	{

				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					switch(msg.what)
					{
					case 0:
						//System.out.println("获取图片失败");
						break;
					case 1:
						//System.out.println("获取图片成功");
						res[0]=(Bitmap)msg.obj;
						break;
					}
				}
	   		
	   	};
	   
	   	 final Runnable myrun=new Runnable()
	   	{
	   		 Bitmap bitmap=null;
				@Override
				public void run() {
					   try  
		    	        {  
		    	            URL url = new URL(uri);  
		    	            bitmap = BitmapFactory.decodeStream(url.openStream());  
		    	            System.out.println("设置图片");
		    	        } catch (Exception e)  
		    	        {  
		    	        	myhandler.obtainMessage(0).sendToTarget();
		    	            // TODO Auto-generated catch block  
		    	            e.printStackTrace();  
		    	        }  
					   myhandler.obtainMessage(1,bitmap).sendToTarget();
					// TODO Auto-generated method stub
					System.out.println("线程");
					//myhandler.postDelayed(myrun, 100);
				}
	   		
	   	};
	   	new Thread(myrun).start();
	   	return res[0];
	}
   public static void setImageViewBitmap(final ImageView image,final String uri)
   {
	   final Handler myhandler=new Handler()
   	{

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what)
				{
				case 0:
					//System.out.println("获取图片失败");
					break;
				case 1:
					//System.out.println("获取图片成功");
					image.setImageBitmap((Bitmap)msg.obj);
					break;
				}
			}
   		
   	};
   
   	 final Runnable myrun=new Runnable()
   	{
   		 Bitmap bitmap = null;  
   	     
			@Override
			public void run() {
				   try  
	    	        {  String path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/E-homeland/Topicnewstemp/";
					  File basepath=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/E-homeland/");
					  if(!basepath.exists())
						  basepath.mkdir();
					  File xp=new File(path);
					  if(!xp.exists())
						  xp.mkdir();
	    	        File img=new File(path+uri+".png");
					   if(img.exists())
						   {bitmap=BitmapFactory.decodeFile(path+uri+".png");
						   System.out.println("已存在图片");
						   }
					   else
					   {
	    	            URL url = new URL(baseurl+uri);  
	    	          //  bitmap = BitmapFactory.decodeStream(url.openStream()); 
	    	            bitmap=Tools.getUrlBitmap(url.toString());
	    	           // image.setImageBitmap(bitmap);
	    	            System.out.println("设置图片");
	    	            ImageCompress.saveMyBitmap(uri, bitmap, path);
					   }
	    	        } catch (Exception e)  
	    	        {  
	    	        	myhandler.obtainMessage(0).sendToTarget();
	    	            // TODO Auto-generated catch block  
	    	            e.printStackTrace();  
	    	        }  
				   myhandler.obtainMessage(1,bitmap).sendToTarget();
				// TODO Auto-generated method stub
				System.out.println("线程");
				//myhandler.postDelayed(myrun, 100);
			}
   		
   	};
   	new Thread(myrun).start();
   }
}

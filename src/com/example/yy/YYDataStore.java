package com.example.yy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Environment;

public class YYDataStore {
	//type=0表示发送的消息，type=1表示收到的消息,用的是FileoutputStream
     public static void saveYYMessage(int type,String content)
     {
    	 File parentDir=new File(Environment.getExternalStorageDirectory(),"E-homeland/Yy");
    	 if(!parentDir.exists())
    	 {
    		 parentDir.mkdirs();
    	 }
    	 String fileName="";
    	 if(type==1)
    	 {
        	 fileName=parentDir.getAbsolutePath()+Utils.getNowDate()+"Received.yy";
    	 }
    	 else
    	 {
        	  fileName=parentDir.getAbsolutePath()+Utils.getNowDate()+"Send.yy";
    	 }
    	 File f=new File(fileName);
    	 try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 try {
			FileOutputStream fos=new FileOutputStream(f);
			fos.write(content.getBytes());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
}

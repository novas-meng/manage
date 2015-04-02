package com.example.tsbx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.os.Environment;

public class TSBXDataStore {
	//创建Tsbx的文件夹，首先判断是否存在，只有当不存在的时候，才创建
    public static void creatTsbxDir(String manager)
    {
    	if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
    		{
    			System.out.println("创建app文件夹");
    			File f=new File(Environment.getExternalStorageDirectory(),"E-homeland");
    			if(!f.exists())
    	      	f.mkdirs();
    	      	File ff=new File(Environment.getExternalStorageDirectory(),"E-homeland/TSBX");
    	      	if(!ff.exists())
    	      	ff.mkdirs();
    	    	File fff=new File(Environment.getExternalStorageDirectory(),"E-homeland/TSBX/"+manager);
    	    	if(!fff.exists())
    	      	fff.mkdirs();
    		}
    }
    //将聊天信息保存到chat.ty文件中，其中主目录是/TSBX/管家姓名，保存的信息格式为二进制，第一位0表示是发送的信息，1表示的收到的信息
    //日期是19个字节，然后是保存的内容
    public static void SaveChatInformation(String manager,int type,byte[] res)
    {
    	File f=new File(Environment.getExternalStorageDirectory(),"E-homeland/TSBX/"+manager+"/chat.ty");
        try {
			FileOutputStream fos=new FileOutputStream(f,true);
			String size=String.valueOf(res.length+Utils.getNowDate().getBytes().length+1);
			int length=size.getBytes().length;
			fos.write(String.valueOf(length).getBytes());
			fos.write(size.getBytes());
			if(type==0)
			fos.write('0');
			else
		    fos.write('1');
			fos.write(Utils.getNowDate().getBytes());
			fos.write(res);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}

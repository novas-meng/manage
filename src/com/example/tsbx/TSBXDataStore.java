package com.example.tsbx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.os.Environment;

public class TSBXDataStore {
	//����Tsbx���ļ��У������ж��Ƿ���ڣ�ֻ�е������ڵ�ʱ�򣬲Ŵ���
    public static void creatTsbxDir(String manager)
    {
    	if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
    		{
    			System.out.println("����app�ļ���");
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
    //��������Ϣ���浽chat.ty�ļ��У�������Ŀ¼��/TSBX/�ܼ��������������Ϣ��ʽΪ�����ƣ���һλ0��ʾ�Ƿ��͵���Ϣ��1��ʾ���յ�����Ϣ
    //������19���ֽڣ�Ȼ���Ǳ��������
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

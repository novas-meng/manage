package com.example.bbm;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import android.os.Environment;

public class bbmDataStore {
	  public static void creatAppDir(String dir)
		{
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			{
				System.out.println("����app�ļ���");
				File f=new File(Environment.getExternalStorageDirectory(),"E-homeland");
				if(!f.exists())
		      	f.mkdirs();
		      	File ff=new File(Environment.getExternalStorageDirectory(),"E-homeland/"+dir);
		      	if(!ff.exists())
		      	ff.mkdirs();
			}	
		}
	  //�洢���æ������Ϣ
      public static void storeOfflineInformation(String id,String content,String phonenumber)
      {
    	 creatAppDir("bbm");
    	 String data=id+"*#"+content+"*#"+phonenumber;
    	 File f=new File(Environment.getExternalStorageDirectory(),"E-homeland/bbm/bbmoffline.bbm");
    	 try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try {
			FileWriter fw=new FileWriter(f);
			fw.write(data);
			fw.flush();
			fw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }
     //�洢���͵İ��æ��Ϣ����ʽΪbbm,��ʽΪ��һλ��type��0��ʾ���֣�1��ʾ������Ȼ�������������
      public static void storeSendInformation(int type,byte[] res)
      {
    	  creatAppDir("bbm/sendMessage");
    	  File f=new File(Environment.getExternalStorageDirectory(),"E-homeland/bbm/sendMessage/"+type+Utils.getCurrentTime()+".bbm");
    	  try {
			FileOutputStream fos=new FileOutputStream(f);
			//fos.write(type);
			fos.write(res);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }
      public static void decodeSendInformation()
      {
    	  File parent=new File(Environment.getExternalStorageDirectory(),"E-homeland/bbm/sendMessage/");
    	  File[] files=parent.listFiles(new FileFilter()
    	  {
			@Override
			public boolean accept(File arg0) {
				// TODO Auto-generated method stub
				return arg0.getName().endsWith(".bbm");
			}		  
    	  });
    	  for(File f:files)
    	  {
    		  try {
				FileInputStream fis=new FileInputStream(f);
				byte[] b=new byte[1];
				fis.read(b);
				String type=new String(b,0,1);
				b=new byte[fis.available()-1];
				fis.read(b);
				String text=new String(b,0,b.length);
				if(type.equals("1"))
				{
					System.out.println("����һ�������ļ�,url="+text);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	  }
      }
      
}

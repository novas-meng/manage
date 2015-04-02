package com.example.tsbx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class FileUtils {
   public static List<String> getEmojiFile(Context context)
   {
	   List<String> list=new ArrayList<String>();
	   try {
		InputStream is=context.getResources().getAssets().open("emoji");
		BufferedReader br=new BufferedReader(new InputStreamReader(is,"GBK"));
		String str=null;
		while((str=br.readLine())!=null)
		{
			list.add(str);
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   return list;
   }
}

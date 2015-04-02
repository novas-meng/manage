package com.example.MyProfile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.bbm.FormFile;
import com.example.bbm.SocketHttpRequester;
import com.example.logIn.Constants;
import com.example.tools.IP;
import com.example.tsbx.*;
import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

public class Tools {
	static int index;
	public static ArrayList<ChatMsgEntity> getLocalMessages(String manager,Activity act,int start)
	{
		ArrayList<ChatMsgEntity> list=new ArrayList<ChatMsgEntity>();
		index=0;
		File f=new File(Environment.getExternalStorageDirectory(),"E-homeland/TSBX/"+manager+"/chat.ty");
		System.out.println(f.getAbsolutePath());
		try {
			
			FileInputStream fis=new FileInputStream(f);
			while(true)
			{
			  //读取文件大小的位数
				System.out.println("index="+index);
				byte[] weishu=new byte[1];
			   if(fis.read(weishu)==-1||index==start+10)
				   break;
			   
			   // String sizeWeishu=weishu.toString();
			    int WeishuOfSize=Integer.valueOf(new String(weishu,0,1));
			    System.out.println("字节数组位数为"+Integer.valueOf(new String(weishu,0,1)));
			    //根据位数读取，获取大小
			    byte[] s=new byte[WeishuOfSize];
			    fis.read(s);
			    
			    int size=Integer.valueOf(new String(s,0,s.length));
			    System.out.println("字节数为"+size);
			     //读取内容，读取之后进行解析
			    s=new byte[size];
			    fis.read(s);
			    if(index<start)
			    {index++;
					   continue;}
			    boolean isComMsg=true;
			    if(new String(s,0,1).equals("0"))
			    	isComMsg=false;
			    System.out.println("s[0]="+new String(s,0,1));
			    String date=new String(s,1,19);
			    System.out.println("date="+new String(s,1,19));
			    byte[] res=new byte[s.length-20]; 
			    System.arraycopy(s,20,res, 0,res.length);
			    SpannableString text=getDecodeResult(res,act);
			    System.out.println("text内容为"+text);
			    ChatMsgEntity entity=new ChatMsgEntity();
			    entity.setMsgType(isComMsg);
			    entity.setDate(date);
			    entity.setText(text);  
			    if(decode(res)==2)
			    entity.setIsVoice(true);
			    else
			    entity.setIsVoice(false);
			    String m=text.toString();
			    System.out.println("m="+m);
			    if(m.endsWith(".amr"))
			    {
			    	 entity.setIsVoice(true);
			    	 System.out.println("声音文件"+text.toString());
	                   entity.setAudioPath(text.toString());
			    }
			    else
			    {
			    	   entity.setIsVoice(false);
			    }
			    list.add(entity);
			    index++;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	//约定数据传输协议，所有的数据统一为二进制数组，二进制数组的前两位表示格式，00表示传输是文字，01表示传输的
	//是图片，11表示传输的是语音；
	public static Bitmap changeBitmapToSmaller(Bitmap bm)
	{
		    int width = bm.getWidth();
			int height = bm.getHeight();
			float scale =  720/ (float)height;
			float w=1280/(float)width;
			Matrix sMatrix = new Matrix();
			if(scale>w)
			sMatrix.postScale(scale, scale);
			else
				sMatrix.postScale(w, w);

			Bitmap bitmap = Bitmap.createBitmap(bm, 0, 0,
			bm.getWidth(), bm.getHeight(), sMatrix, true);
			return bitmap;
	}
	//解析要发送的数据
	public static SpannableString DecodeSendMessage(String text,Activity act)
	{
		System.out.println("test="+text);
		SpannableString res=new SpannableString(" ");
		if(text.startsWith("[")&&text.endsWith("]"))
		{
			String sub=text.substring(1,text.length()-1);
			System.out.println("图片路径为"+sub);
			MsgSendToManager.isImage=true;
			MsgSendToManager.imagePath=sub;
			
			Bitmap bm = BitmapFactory.decodeFile(sub);
		//	if(bm.getHeight()>700||bm.getWidth()>700)
		//	{
				System.out.println("");
				bm=changeBitmapToSmaller(bm);
		//	}
			ImageSpan is=new ImageSpan(bm);
			res.setSpan(is, 0, 1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		/*
		else if(text.equals("##"))
		{
			Bitmap bt=BitmapFactory.decodeResource(act.getResources(),R.drawable.voicesend);
			ImageSpan span=new ImageSpan(bt);
			res.setSpan(span, 0, 1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		*/
		else
		{
			    res=new SpannableString(text);
			    String regex="[{].+?[}]";
			   	Pattern p=Pattern.compile(regex);
			   	Matcher m=p.matcher(text);
			   	while(m.find())
			   	{
			   		String rpl=m.group();
			   		int start=m.start();
			   		System.out.println(start);
			   		System.out.println("rpl="+rpl);
			   		String strs[]=rpl.substring(1,rpl.length()-1).split(":");
			   		System.out.println("strs[0]="+strs[0]);
			   		int imgId=act.getResources().getIdentifier(strs[0],"drawable", act.getPackageName());
			   		Bitmap bitmap = BitmapFactory.decodeResource(act.getResources(),
							imgId);
			   		ImageSpan imageSpan = new ImageSpan(act, bitmap);
			   		res.setSpan(imageSpan, start,start+rpl.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			   	}
		}
		return res;
	}
	//将图片转化为字节数组
	  public static byte[] ImageToBytes(String uri)
	  {
		 
		  Bitmap bm = BitmapFactory.decodeFile(uri);
		  if(bm.getWidth()>700||bm.getHeight()>700)
		  bm=changeBitmapToSmaller(bm);
	      byte[] s=getBitmapByte(bm);
	      System.out.println("原始图片的大小为"+s.length);
	      byte[] res=new byte[s.length+2];
	      res[0]=0;
	      res[1]=1;
	      for(int i=0;i<s.length;i++)
	      {
	    	  res[i+2]=s[i];
	      }
	      return res;
	  }
	  //将图片和文字发送给服务器的类
	  public static byte[] StringToBytes(String uri)
	  {
		  byte[] res;
		  if(uri.startsWith("[")&&uri.endsWith("]"))
			{
				String sub=uri.substring(1,uri.length()-1);
				System.out.println("图片路径为"+sub);
				res=ImageToBytes(sub);
			}
		  else
		  {
		    	MsgSendToManager.msgList.add(Constants.username+"发送:"+uri);
			  byte[] s=uri.getBytes();
		      System.out.println("原始图片的大小为"+s.length);
		     res=new byte[s.length+2];
		      res[0]=0;
		      res[1]=0;
		     // System.arraycopy(s,0,res,2,s.length);
		      for(int i=0;i<s.length;i++)
		      {
		    	  res[i+2]=s[i];
		      }
		  }
		
	      return res;
	  }
	  //解析传过来的数据，当数据为0的时候说明传的是文字，1表示图片，2表示语音，-1表示格式错误
	  public static int decode(byte[] s)
	  {
		  if(s[1]==0&&s[0]==0)
		  {
			  return 0;
		  }
		  if(s[0]==0&&s[1]==1)
		  {
			  return 1;
		  }
		  if(s[0]==1&&s[1]==1)
		  {
			  return 2;
		  }
		  return -1;
	  }
	  //把获得字节数组进行解析，从而判断是图片，文字，还是语音
	  public static SpannableString getDecodeResult(byte[] s,Activity act)
	  {
		  SpannableString res=null;
		  switch(decode(s))
		  {
		  case 0:
			  System.out.println("接收到的是文字");
			  res=ByteToString(s,act);
			  break;
		  case 1:
			  System.out.println("接收到的是图片");
			  res=ByteToImage(s);
		      break;
		  case 2:
			  res=ByteToAudio(s);
		  }
		  return res;
	  }
	  //返回从网络中获取的语音文件存在本地的位置
	  public static SpannableString ByteToAudio(byte[] s)
	  {
		  byte[] res=new byte[s.length-2];
		  System.arraycopy(s,2,res,0,res.length);
		  String fileName = Environment.getExternalStorageDirectory().getAbsolutePath();
	       fileName += "/E-homeland/audio/"+Utils.getNowDate()+"-Receive.amr";
	       File f=new File(fileName);
	       try {
			FileOutputStream fos=new FileOutputStream(f);
			fos.write(res);
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	       SpannableString result=new SpannableString(fileName);
	       return result;
	  }
	  //将字节数组转化为图片
	  public static SpannableString ByteToImage(byte[] s)
	  {
		  Bitmap b=BitmapFactory.decodeByteArray(s,2,s.length-2);  
		  SpannableString res=new SpannableString(" ");
		  ImageSpan is=new ImageSpan(b);
		  res.setSpan(is,0,1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		  return res;
	  }
	  //将字节数组转化为字符串
	  public static SpannableString ByteToString(byte[] s,Activity act)
	  {
		  byte[] b=new byte[s.length-2];
		  System.arraycopy(s,2,b,0,b.length);
		  String text=new String(b);
	    	MsgSendToManager.msgList.add("agxcul"+"回复:"+text);
		   SpannableString res=new SpannableString(text);
		    String regex="[{].+?[}]";
		   	Pattern p=Pattern.compile(regex);
		   	Matcher m=p.matcher(text);
		   	while(m.find())
		   	{
		   		String rpl=m.group();
		   		int start=m.start();
		   		System.out.println(start);
		   		System.out.println("rpl="+rpl);
		   		String strs[]=rpl.substring(1,rpl.length()-1).split(":");
		   		System.out.println("strs[0]="+strs[0]);
		   		int imgId=act.getResources().getIdentifier(strs[0],"drawable", act.getPackageName());
		   		Bitmap bitmap = BitmapFactory.decodeResource(act.getResources(),
						imgId);
		   		ImageSpan imageSpan = new ImageSpan(act, bitmap);
		   		res.setSpan(imageSpan, start,start+rpl.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		   	}
		  return res;
	  }
	  //获取图片的字节数组
	  public static byte[] getBitmapByte(Bitmap bitmap){   
		    ByteArrayOutputStream out = new ByteArrayOutputStream();   
		    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);   
		    try {   
		        out.flush();   
		        out.close();   
		    } catch (IOException e) {   
		        e.printStackTrace();   
		    }   
		    return out.toByteArray();   
		}   
}

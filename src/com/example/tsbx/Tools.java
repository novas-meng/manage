package com.example.tsbx;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.logIn.Constants;

import android.annotation.SuppressLint;
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
	
	public static ArrayList<ChatMsgEntity> getLocalMessages(String manager,Activity act)
	{
		ArrayList<ChatMsgEntity> list=new ArrayList<ChatMsgEntity>();
		System.out.println("这个是投诉报修");
		File f=new File(Environment.getExternalStorageDirectory(),"E-homeland/TSBX/"+manager+"/chat.ty");
		try {
			FileInputStream fis=new FileInputStream(f);
			while(true)		
			{
			  //读取文件大小的位数
				byte[] weishu=new byte[1];
			   if(fis.read(weishu)==-1)
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
			    boolean isComMsg=true;
			    if(new String(s,0,1).equals("0"))
			    {
			    	isComMsg=false;
                    System.out.println("这是发送的信息");
			    }
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
			    list.add(entity);
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
			float scale =800/ (float)height;
			float w=480/(float)width;
			Matrix sMatrix = new Matrix();
			if(scale>w)
			sMatrix.postScale(scale, scale);
			else
				sMatrix.postScale(w, w);

			Bitmap bitmap = Bitmap.createBitmap(bm, 0, 0,
			bm.getWidth(), bm.getHeight(), sMatrix, true);
			return bitmap;
	}
	private static int computeInitialSampleSize(BitmapFactory.Options options,
	        int minSideLength, int maxNumOfPixels) {
	    double w = options.outWidth;
	    double h = options.outHeight;
	  
	    int lowerBound = (maxNumOfPixels == -1) ? 1 :
	            (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
	    int upperBound = (minSideLength == -1) ? 128 :
	            (int) Math.min(Math.floor(w / minSideLength),
	            Math.floor(h / minSideLength));
	  
	    if (upperBound < lowerBound) {
	        return lowerBound;
	    }
	  
	    if ((maxNumOfPixels == -1) &&
	            (minSideLength == -1)) {
	        return 1;
	    } else if (minSideLength == -1) {
	        return lowerBound;
	    } else {
	        return upperBound;
	    }
	}
	public static int computeSampleSize(BitmapFactory.Options options,
	        int minSideLength, int maxNumOfPixels) {
	    int initialSize = computeInitialSampleSize(options, minSideLength,
	            maxNumOfPixels);
	  
	    int roundedSize;
	    if (initialSize <= 8) {
	        roundedSize = 1;
	        while (roundedSize < initialSize) {
	            roundedSize <<= 1;
	        }
	    } else {
	        roundedSize = (initialSize + 7) / 8 * 8;
	    }
	  
	    return roundedSize;
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
			/*
			FileInputStream iss;
			Bitmap bm = null;
	        BitmapFactory.Options opts = new BitmapFactory.Options();

			 opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
		        BitmapFactory.decodeFile(sub, opts);
		        opts.inSampleSize = computeSampleSize(opts, -1, 1024 * 800);
		 
		        opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true
		        opts.inPurgeable = true;
		        opts.inInputShareable = true;
		        opts.inDither = false;
		        opts.inPurgeable = true;
		        opts.inTempStorage = new byte[16 * 1024];
			try {
				iss = new FileInputStream(sub);
				 bm = BitmapFactory.decodeFileDescriptor(iss.getFD(), null,opts);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			//Bitmap bm = BitmapFactory.decodeFile(sub);
			//Bitmap bm=BitmapFactory.decodeStream(decodeBitmap(sub));
			Bitmap bm=decodeBitmap(sub);
		//	if(bm.getHeight()>700||bm.getWidth()>700)
		//	{
				System.out.println("");
				//bm=changeBitmapToSmaller(bm);
		//	}
			ImageSpan is=new ImageSpan(bm);
			//bm.recycle();
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
			    MsgSendToManager.msgList.add(Constants.username+":"+text);
			    MsgSendToManager.isImage=false;
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
		 
		//  Bitmap bm = BitmapFactory.decodeFile(uri);
		  Bitmap bm=decodeBitmap(uri);
		 // if(bm.getWidth()>700||bm.getHeight()>700)
		 // bm=changeBitmapToSmaller(bm);
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
		  b=Tools.changeBitmapToSmaller(b);
		  File f=new File(Environment.getExternalStorageDirectory(),"E-homeland/Image");
		  if(!f.exists()) 
		  f.mkdirs();
		  String fileName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/E-homeland/Image/"
				  +Utils.getNowDate()+"-received.jpg";
		 com.example.logIn.Constants.receiveImgPath=fileName;
		 File imageSave=new File(fileName);
		 try {
			imageSave.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 try {
			FileOutputStream fos=new FileOutputStream(imageSave);
			b.compress(Bitmap.CompressFormat.JPEG,50,fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		  MsgSendToManager.msgList.add(Constants.butlername+":"+text);
		    MsgSendToManager.isImage=false;
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
	  @SuppressLint("NewApi") public static Bitmap decodeBitmap(String path) {
		  
	        BitmapFactory.Options opts = new BitmapFactory.Options();
	        opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
	        BitmapFactory.decodeFile(path, opts);
	        opts.inSampleSize = computeSampleSize(opts, -1, 1024 * 800);
	 
	        opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true
	        opts.inPurgeable = true;
	        opts.inInputShareable = true;
	        opts.inDither = false;
	        opts.inPurgeable = true;
	        opts.inTempStorage = new byte[16 * 1024];
	        FileInputStream is = null;
	        Bitmap bmp = null;
	        InputStream ins = null;
	        ByteArrayOutputStream baos = null;
	        try {
	            is = new FileInputStream(path);
	            bmp = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);           double scale = getScaling(opts.outWidth * opts.outHeight, 1024 * 600);
	            Bitmap bmp2 = Bitmap.createScaledBitmap(bmp,
	                    (int) (opts.outWidth * scale),
	                    (int) (opts.outHeight * scale), true);
	         //   bmp.recycle();
	            baos = new ByteArrayOutputStream();
		        bmp2= changeBitmapToSmaller(bmp2);
	            bmp2.compress(Bitmap.CompressFormat.JPEG,10, baos);
	            int options=10;
	            /*
	            while(baos.toByteArray().length/1024>15)
	            {
	            	System.out.println("t图片大小为"+baos.toByteArray().length/1024+"  "+bmp2.getByteCount()/1024);
	            	baos.reset();
                    options=options-2;
	            	bmp2.compress(Bitmap.CompressFormat.JPEG,options, baos);

	            	
	            }
	            */
	            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		         bmp2= BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
		         
	         //   bmp2.recycle();
	            return bmp2;
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                is.close();
	               // ins.close();
	                baos.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            System.gc();
	        }
	        return null;
	    }
	 
	private static double getScaling(int src, int des) {
	/**
	 * 目标尺寸÷原尺寸 sqrt开方，得出宽高百分比
	 */
	    double scale = Math.sqrt((double) des / (double) src);
	    return scale;
	}
	public static Bitmap getUrlBitmap(String image_path) throws MalformedURLException, IOException
	{
		 BitmapFactory.Options options = new BitmapFactory.Options();  
         options.inJustDecodeBounds = true;  
         BitmapFactory.decodeStream(new URL(image_path).openStream(),null,options);  
   
           
         int width=options.outWidth, height=options.outHeight;  
         int scale=1;  
         int temp=width>height?width:height;  
         while(true){  
             if(temp/2<120)  
                 break;  
             temp=temp/2;  
             scale*=2;  
         }  
           
         BitmapFactory.Options opt = new BitmapFactory.Options();  
         opt.inSampleSize=scale;  
         Bitmap mbitmap= BitmapFactory.decodeStream(new URL(image_path).openStream(), null, opt); 
         return mbitmap;
	}
}

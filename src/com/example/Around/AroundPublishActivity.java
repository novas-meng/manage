package com.example.Around;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tools.FormFile;
import com.example.tools.IP;
import com.example.tools.ImageAdapter;
import com.example.tools.ImageCompress;
import com.example.tools.ScaleImageFromSdcardActivityforpic;
import com.example.tools.SocketHttpRequester;
import com.example.tools.Utils;
import com.example.tools.VerticalScrollTextView;
import com.example.logIn.Constants;
import com.example.manage.R;
import com.example.manage.R.layout;

public class AroundPublishActivity extends Activity{
	private ProgressDialog dialog;
	TextView food,enterm,shop,hotal,extra;    
    ImageView pic1,tp1,pic2,pic3,tp2,tp3,offer;
	private String mclass;   //记录发布的信息属于哪个类别
	private int photonum=0,certificatenum=0;  //记录照片数量
	//private String res;        //记录服务器返回的信息
    public static ArrayList<Integer> ClickList=new ArrayList<Integer>(); //记录选择照片的编号
	private JSONObject obj=null;  
	
	String basedir=Environment.getExternalStorageDirectory().getAbsolutePath()+"/E-homeland";
	String savepath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/E-homeland/aroundtemp/";
	private String BASE_URL=IP.ip+":3000/";
	String ba_url="http://192.168.199.176:3000/";
	String urlforpublish=BASE_URL+"/around/postaroundinfo";
	String urlforpublish1=ba_url+"around/postaroundinfo";
    String path,community=null,userid;   
	EditText name,pname,add,phone,words;
	CheckBox cb;
	
	String val;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.advertisement);
		dialog=new ProgressDialog(this);
		dialog.setTitle("提示");
		dialog.setMessage("正在上传，请稍后...");
		
		ImageView bw=(ImageView)findViewById(R.id.backarrow);
		bw.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				//startActivity(new Intent(getApplicationContext(),AroundActivity.class));
				AroundPublishActivity.this.finish();
			}
		});
		ImageCompress.cleanCustomCache(savepath+"phototemp/");  //清理缓存
        
		SharedPreferences sharedPreferences = getSharedPreferences("user", 
        		Activity.MODE_PRIVATE); 
		userid=sharedPreferences.getString("userName", "");
	    community=sharedPreferences.getString("community", "");
		//Toast.makeText(getApplicationContext(), community, Toast.LENGTH_SHORT).show();
		
		
	   pic1=(ImageView)findViewById(R.id.pic1);
	   pic2=(ImageView)findViewById(R.id.pic2);
	   pic3=(ImageView)findViewById(R.id.pic3);
	   tp1=(ImageView)findViewById(R.id.tp1);
	   tp2=(ImageView)findViewById(R.id.tp2);
	   tp3=(ImageView)findViewById(R.id.tp3);
	   food=(TextView)findViewById(R.id.cfood);
	   enterm=(TextView)findViewById(R.id.centertainment);
	   shop=(TextView)findViewById(R.id.cshop);
	   hotal=(TextView)findViewById(R.id.chotal);
	   extra=(TextView)findViewById(R.id.cextra);
	   cb=(CheckBox)findViewById(R.id.check);
	   offer=(ImageView)findViewById(R.id.offer);
	   name=(EditText)findViewById(R.id.name);
	   pname=(EditText)findViewById(R.id.pername);
	   add=(EditText)findViewById(R.id.add);
	   phone=(EditText)findViewById(R.id.phone);
	   words=(EditText)findViewById(R.id.words);
	   
	   pic1.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			ScaleImageFromSdcardActivityforpic.setIs(true);
			Intent intent=new Intent(AroundPublishActivity.this,ScaleImageFromSdcardActivityforpic.class);
		    startActivityForResult(intent, Utils.SHOW_ALL_PICTURE);
		}
	}); 
	   pic2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ScaleImageFromSdcardActivityforpic.setIs(false);
				Intent intent=new Intent(AroundPublishActivity.this,ScaleImageFromSdcardActivityforpic.class);
			    startActivityForResult(intent, Utils.SHOW_ALL_PICTURE);
			}
		}); 
	   pic3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ScaleImageFromSdcardActivityforpic.setIs(false);
				Intent intent=new Intent(AroundPublishActivity.this,ScaleImageFromSdcardActivityforpic.class);
			    startActivityForResult(intent, Utils.SHOW_ALL_PICTURE);
			}
		}); 
	   tp1.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				File destDir = new File(savepath+"certificatetemp");
				  if (!destDir.exists()) {
				   destDir.mkdirs();
				  }
				String sFileFullPath =savepath+"certificatetemp"+"/photo"+(certificatenum+1)+".jpg";
				File file = new File(sFileFullPath);  
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));  
				startActivityForResult(intent, 2);
			}
		});
	   tp2.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				File destDir = new File(savepath+"phototemp");
				  if (!destDir.exists()) {
				   destDir.mkdirs();
				  }
				String sFileFullPath =savepath+"phototemp"+"/photo"+(photonum+1)+".jpg"; 
				File file = new File(sFileFullPath);  
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));  
				startActivityForResult(intent, 1);
			}
		});
	   tp3.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				File destDir = new File(savepath+"phototemp");
				  if (!destDir.exists()) {
				   destDir.mkdirs();
				  }
				String sFileFullPath =savepath+"phototemp"+"/photo"+(photonum+1)+".jpg";  
				File file = new File(sFileFullPath);  
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));  
				startActivityForResult(intent, 1);
			}
		});
	   food.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			food.setTextColor(Color.parseColor("#ffffff"));
			enterm.setTextColor(Color.parseColor("#3A5FCD"));
			hotal.setTextColor(Color.parseColor("#76EE00"));
			shop.setTextColor(Color.parseColor("#A020F0"));
			extra.setTextColor(Color.parseColor("#00E5EE"));
			mclass="food";
		}
	});
	   enterm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				food.setTextColor(Color.parseColor("#ff0000"));
				enterm.setTextColor(Color.parseColor("#ffffff"));
				hotal.setTextColor(Color.parseColor("#76EE00"));
				shop.setTextColor(Color.parseColor("#A020F0"));
				extra.setTextColor(Color.parseColor("#00E5EE"));
				mclass="entertainment";
			}
		});
	   hotal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				food.setTextColor(Color.parseColor("#ff0000"));
				enterm.setTextColor(Color.parseColor("#3A5FCD"));
				hotal.setTextColor(Color.parseColor("#ffffff"));
				shop.setTextColor(Color.parseColor("#A020F0"));
				extra.setTextColor(Color.parseColor("#00E5EE"));
				mclass="hotal";
			}
		});
	   shop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				food.setTextColor(Color.parseColor("#ff0000"));
				enterm.setTextColor(Color.parseColor("#3A5FCD"));
				hotal.setTextColor(Color.parseColor("#76EE00"));
				shop.setTextColor(Color.parseColor("#ffffff"));
				extra.setTextColor(Color.parseColor("#00E5EE"));
				mclass="shop";
			}
		});
	   extra.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				
				// TODO Auto-generated method stub
				food.setTextColor(Color.parseColor("#ff0000"));
				enterm.setTextColor(Color.parseColor("#3A5FCD"));
				hotal.setTextColor(Color.parseColor("#76EE00"));
				shop.setTextColor(Color.parseColor("#A020F0"));
				extra.setTextColor(Color.parseColor("#ffffff"));
				mclass="extra";
			}
		});
	   offer.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			
			
				urlforpublish=BASE_URL+"around/postaroundinfo";
			// TODO Auto-generated method stub
				if(name.getText().toString().equals("")||pname.getText().toString().equals("")||add.getText().toString().equals("")||phone.getText().toString().equals(""))
				Toast.makeText(getApplicationContext(), "用户信息不全", Toast.LENGTH_SHORT).show();
				else if(phone.getText().toString().length()!=11&&phone.getText().toString().length()!=8)
				{
					Toast.makeText(getApplicationContext(), "电话位数有误", Toast.LENGTH_SHORT).show();

				}
				else if(mclass==null)
					Toast.makeText(getApplicationContext(), "未选类别", Toast.LENGTH_SHORT).show();
					else if(cb.isChecked()==false)
				Toast.makeText(getApplicationContext(), "您未同意发布协议",Toast.LENGTH_SHORT).show();
			else if(name.getText().toString()!=null&&pname.getText().toString()!=null&&add.getText().toString()!=null&&phone.getText().toString()!=null&&mclass!=null)
			{
				//Toast.makeText(getApplicationContext(), "正在上传，请稍候", Toast.LENGTH_SHORT).show();
				dialog.show();
				obj=new JSONObject();
				try {
					obj.put("name",name.getText().toString());
					obj.put("pername",pname.getText().toString());
					obj.put("addr", add.getText().toString());
					obj.put("phone", phone.getText().toString());
					obj.put("class", mclass);
					obj.put("community", community);
					obj.put("username",userid);
					if(words.getText().toString()!=null)
						obj.put("words",words.getText().toString());
					else obj.put("words",null);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
				//Toast.makeText(getApplicationContext(), obj.toString(), Toast.LENGTH_SHORT).show();
				new Thread(runnable).start();
			}
		}
	});
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data) {  //照相机回调函数
	    if (requestCode == 1) {  
	        if (resultCode == RESULT_OK) {  
	        	System.out.println("photo");
	            
	            photonum++;
	            // You can set bitmap to ImageView here  
	        }  
	    }  
	    if (requestCode == 2) {  
	        if (resultCode == RESULT_OK) {  
	        	System.out.println("certificate");
	            
	        	certificatenum++;
	            // You can set bitmap to ImageView here  
	        }  
	    }  
	}  
	
	Handler handler = new Handler(){
	    @Override
	    public void handleMessage(Message msg) {
	        super.handleMessage(msg);
	        
	        if(msg.what==1)    //网络未连接的处理
	    		Toast.makeText(getApplicationContext(), "网络未连接", Toast.LENGTH_SHORT).show();
	     
	        
	        
	        else if(msg.what==0)    //上传信息的处理
	        {
	        	dialog.dismiss();
	        	if(msg.obj.toString()=="fail")
	        		Toast.makeText(getApplicationContext(), "上传失败，请重新尝试", Toast.LENGTH_SHORT).show();
	        	else Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
	        }
	        else if(msg.what==2)    //上传信息的处理
	        {
	        	dialog.dismiss();
	        	if(msg.obj.toString()=="fail")
	        		Toast.makeText(getApplicationContext(), "证照上传失败，请重新尝试", Toast.LENGTH_SHORT).show();
	        	else ;
	        }
	    }
	};
	Runnable runnable1 = new Runnable(){    //获取新闻的线程
	    
	   
	    InputStream is;
	    String strResult="123";
	    public void run() {
	        //
	    	
	    	String url=BASE_URL+"news/getnews?get=news&community="+community;		
	    	System.out.println(url);
	    	HttpGet getMethod = new HttpGet(url);
			if(isNetworkConnected(getApplicationContext())==false)
				//Toast.makeText(getApplicationContext(), "网络未连接", Toast.LENGTH_SHORT).show();
				{
				Message msg=new Message();
		        msg.what=1;
		        msg.obj="noNet";
		        handler.obtainMessage(1,"noNet").sendToTarget();
		        }
				
			
			else
			try
			  {
			   //取得HttpClient对象
			   HttpClient httpclient = new DefaultHttpClient();
			   //请求HttpClient，取得HttpResponse
			   HttpResponse httpResponse = httpclient.execute(getMethod);
			   //请求成功
			   if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			   {
			    //取得返回的字符串
			   strResult = EntityUtils.toString(httpResponse.getEntity(),"utf-8");
			    System.out.println(strResult+" aa");
			    Message msg=new Message();
			    msg.what=2;
			    msg.obj=strResult;
			   // msg.sendToTarget();
			    handler.obtainMessage(2,strResult).sendToTarget();
			   }
			   
			  }
			  catch (ClientProtocolException e)
			  {
			   ;
			  }
			  catch (IOException e)
			  {
			   ;
			  }
			  catch (Exception e)
			  {
			   ;
			  }  
	    }
	};
	Runnable runnable = new Runnable(){    //发布信息的线程
	    @SuppressWarnings("deprecation")
		@Override
	    public void run() {
	        //
	        // TODO: http request.
	        //
	    	String rq=null;
	    	if(isNetworkConnected(getApplicationContext())==false)
				//Toast.makeText(getApplicationContext(), "网络未连接", Toast.LENGTH_SHORT).show();
				{
				Message msg=new Message();
		        msg.what=1;
		        msg.obj="noNet";
		        handler.obtainMessage(1,"noNet").sendToTarget();
		        
		        }
	    	else{
	    	
	    	FormFile fm[]=new FormFile[ImageAdapter.ClickList.size()+photonum+certificatenum] ;    
	    	String res=null;
				  
				 try {
					 System.out.println("111");
					 
					
					 File destDir = new File(basedir);
					  if (!destDir.exists()) {
					   destDir.mkdirs();
					  }
					  File dtDir = new File(basedir+"/aroundtemp");
					  if (!dtDir.exists()) {
					   dtDir.mkdirs();
					  }
					  File xtDir=new File(basedir+"/aroundtemp/pushphoto");
					  if (!xtDir.exists()) {
						   xtDir.mkdirs();
						  }
					  File xkDir=new File(basedir+"/aroundtemp/certificatephoto");
					  if (!xkDir.exists()) {
						   xkDir.mkdirs();
						  }
					//ImageCompress.cleanCustomCache(savepath);  //清理缓存
					  
					  
					  //
					for(int i=0;ImageAdapter.ClickList.size()>i;i++)  //选择的照片压缩并处理
					 {
						 System.out.println(i);
					 String path=ScaleImageFromSdcardActivityforpic.map.get(ImageAdapter.ClickList.get(i));
               	     System.out.println("img"+i);
               	     Bitmap bm=ImageCompress.getimage(path);
               	     ImageCompress.saveMyBitmap("image"+i, bm,savepath+"pushphoto/");
               	     path=savepath+"pushphoto/" + "image"+i + ".png";
               	     fm[i]=new FormFile("image"+i, new File(path), "image","image/png");
					 }
					 for(int j=0;j<photonum;j++)   //照片的处理
					 {
						 System.out.println(j);
					 String path=savepath+"phototemp"+"/photo"+(j+1)+".jpg";
               	     System.out.println("img"+j);
               	     Bitmap bm=ImageCompress.getimage(path);
               	     ImageCompress.saveMyBitmap("photoimage"+j, bm,savepath);
               	     path=savepath + "photoimage"+j + ".png";
               	     System.out.println(path);
               	     fm[j+ImageAdapter.ClickList.size()]=new FormFile("photoimage"+j, new File(path), "image","image/png");
					 } 
					 for(int j=0;j<certificatenum;j++)   //照片的处理
					 {
						 System.out.println(j);
					 String path=savepath+"certificatetemp"+"/photo"+(j+1)+".jpg";
               	     System.out.println("img"+j);
               	     Bitmap bm=ImageCompress.getimage(path);
               	     ImageCompress.saveMyBitmap("certificateimage"+j, bm,savepath+"certificatetemp/");
               	     path=savepath +"certificatetemp/"+ "certificateimage"+j + ".png";
               	     System.out.println(path);
               	     fm[j+ImageAdapter.ClickList.size()+photonum]=new FormFile("certificateimage"+j, new File(path), "image","image/png");
					 } 
					 System.out.println("照片数量为:"+(photonum+ImageAdapter.ClickList.size()));
					 Map<String, String> params = new HashMap<String, String>();
					 Boolean[] cet=new Boolean[ImageAdapter.iscertificate.size()+photonum+certificatenum];
					 for(int i=0;i<ImageAdapter.iscertificate.size();i++)
					 {
						 if(ImageAdapter.iscertificate.get(i).booleanValue()==true)
						 {
							 System.out.println("true");
							 cet[i]=true;
						 }
						 else 
						 {
							 System.out.println("false");
							 cet[i]=false;
						 }
					 }
					 for(int i=0;i<photonum;i++)
					 {
						 cet[i+ImageAdapter.iscertificate.size()]=false;
					 }
					 for(int i=0;i<certificatenum;i++)
					 {
						 cet[i+ImageAdapter.iscertificate.size()+photonum]=true;
					 }
					 String cett="";
					 for(int i=0;i<cet.length;i++)
					 {
						
						 System.out.println(i+" "+cet[i]);
						 if(!(i==cet.length-1))
						  cett=cett+cet[i]+",";
						 else
							 cett=cett+cet[i];
						
					 }
				     System.out.println(cett);
					 obj.put("certificate", cett);
                    params.put("json", obj.toString());
                    System.out.println(urlforpublish);
					  SocketHttpRequester.post(urlforpublish, params, fm);
					  rq=SocketHttpRequester.request;
					   System.out.println("repley"+rq);
					   Message msg=new Message();
					    msg.what=0;
					    msg.obj=rq;
					   
					    handler.obtainMessage(0,rq).sendToTarget();
					  
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "文件读取失败", Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "上传失败，请重试", Toast.LENGTH_SHORT).show();
				} 			
				 
				 
	    	}
	        
	    }
	    
	};
	
	public boolean isNetworkConnected(Context context) {    //判断网络是否连接
	     if (context != null) {  
	          ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
	                  .getSystemService(Context.CONNECTIVITY_SERVICE);  
	          NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
	          if (mNetworkInfo != null) {  
	              return mNetworkInfo.isAvailable();  
	          }  
	      }  
	     return false;  
	 }  

	public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK) {  
        	
        	
        	AroundPublishActivity.this.finish();
        	
            return true;   
        } else  
            return super.onKeyDown(keyCode, event);  
    }
     
   }


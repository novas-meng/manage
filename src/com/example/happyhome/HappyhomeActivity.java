package com.example.happyhome;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manage.R;
import com.example.pet.petActivity;
import com.example.pet.petConstants;
import com.example.tools.FormFile;
import com.example.tools.IP;
import com.example.tools.SocketHttpRequester;
import com.example.tsbx.MyProgressBar;
import com.example.tsbx.Tools;
import com.example.wu1.XListView;
import com.example.wu1.XListView.IXListViewListener;


@SuppressLint({ "NewApi", "SdCardPath" })
public class HappyhomeActivity extends Activity implements IXListViewListener{

	private int flag = 0;
	private int happyhomeNumber = 0;
	private int showNumber = 0;
	private Set<String> set;
	private ArrayList<String> happyhomeIdList = new ArrayList<String>();
	private String comment;
	private int positon;
	private TabHost myTabhost;
	private Button enjoy,share,onBackPressed;
	public ImageView iv;
	public static Bitmap hbitmap;
	private ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();  
	private String back;
	private JSONArray happyhomeJsonArray = new JSONArray();
	public static String himg_path;
	private XListView happyhomeListView;
	private myAdapter listItemAdapter;
	private ProgressBar pb;
	private ProgressBar process;
	private ArrayList<HashMap<String, Object>> testList = new ArrayList<HashMap<String, Object>>(); 
	private Handler mHandler,postHandler;
	private LinearLayout note,nul;
	private EditText name,variety;
	ImageView imageView ;  

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pet);
		LinearLayout layout=(LinearLayout)findViewById(R.id.petlayout);
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		InputStream is = getResources().openRawResource(
				 R.drawable.pet_background );
		Bitmap bm = BitmapFactory.decodeStream(is, null, opt);
		BitmapDrawable bd = new BitmapDrawable(getResources(), bm);
		layout.setBackgroundDrawable(bd);
	    imageView = (ImageView) findViewById(R.id.iv01);  
		name = (EditText)findViewById(R.id.petUploadName);
		name.setVisibility(View.GONE);
    	variety = (EditText)findViewById(R.id.petUploadVariety);
		note = (LinearLayout)findViewById(R.id.petVarietyLinearLayout);
		note.setBackgroundResource(R.drawable.happyhome_note);
		nul = (LinearLayout)findViewById(R.id.petNameLinearLayout);
		nul.setVisibility(View.GONE);
		onBackPressed = (Button)findViewById(R.id.petOnbackPressed);
		onBackPressed.setBackgroundResource(R.drawable.pet_happyhome);
		onBackPressed.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				happyConstants.getFinished.clear();
				happyConstants.freshFinished=true;
				HappyhomeActivity.this.onBackPressed();
			}
			
		});
		
		myTabhost = (TabHost)findViewById(R.id.petTabHost);
		myTabhost.setup();//实例化了tabWidget和tabContent 
		myTabhost.addTab(myTabhost.newTabSpec("欣赏").setIndicator("",getResources().getDrawable(R.drawable.changyongdianhua)).setContent(R.id.petScroll));
	    myTabhost.addTab(myTabhost.newTabSpec("发布").setIndicator("",getResources().getDrawable(R.drawable.xinxichaxun)).setContent(R.id.petLinear));
		myTabhost.setCurrentTab(0);
		final TabWidget tabWidget = myTabhost.getTabWidget();
	    for (int i =0; i < tabWidget.getChildCount(); i++) {  
	                tabWidget.getChildAt(i).getLayoutParams().height = 0;  
	                //tabWidget.getChildAt(i).getLayoutParams().width = 65;
	    }
		//浏览
		happyhomeListView = (XListView)findViewById(R.id.petScroll);
        mHandler = new Handler();
        postHandler=new Handler();
		creatAppDir();
		//String res = readFile();
		//System.out.println("读出来的"+res);
		//if(res.equals("[]"))
		//{
		process=MyProgressBar.createProgressBar(HappyhomeActivity.this,null);
		process.setVisibility(View.VISIBLE);
			onRefresh();
		//}
			/*
		else
		{
			try {
				happyhomeJsonArray = new JSONArray(res);
				System.out.println(happyhomeJsonArray.toString());
				flag = happyhomeJsonArray.length()/10;
				for(int i = 0;i < happyhomeJsonArray.length();i++){
					JSONObject json = happyhomeJsonArray.getJSONObject(i);
					JSONArray json1 = new JSONArray();
					String happyhomeComment = "";
					if(json.length()>5){
						 json1 = json.getJSONArray("comment");
						 if(json1.length()!=0)
							happyhomeComment = json1.getJSONObject(json1.length()-1).getString("content");
						 	//System.out.println(happyhomeComment+"chongwupinglun");
					 }
					HashMap<String, Object> map = new HashMap<String, Object>(); 
		            map.put("happyhomeImage", BitmapFactory.decodeFile(json.getString("imageUrl")));//图像资源的ID  
		            map.put("happyhomeName", json.getString("happyhomeOwner"));
		            map.put("happyhomeliuyan",json.getString("remark"));
		            map.put("happyhomeComment", happyhomeComment);  
		            listItem.add(map);  
					happyhomeIdList.add(json.getString("imageId"));
				}
				happyhomeNumber = happyhomeJsonArray.length();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		*/
        //生成适配器的Item和动态数组对应的元素  
        listItemAdapter = new myAdapter(this,listItem,//数据源   
            R.layout.happyhome_listitem,//ListItem的XML实现  
            //动态数组与ImageItem对应的子项          
            new String[] {"happyhomeImage","happyhomeName","happyhomeliuyan", "happyhomeComment"},   
            //ImageItem的XML文件里面的一个ImageView,两个TextView ID  
            new int[] {R.id.petImageView,R.id.petViewName,R.id.petLiuyan,R.id.petComment}  
        );  
         
        //添加并且显示  
        happyhomeListView.setAdapter(listItemAdapter);  
        happyhomeListView.setPullRefreshEnable(true);
        happyhomeListView.setXListViewListener(this);
		//发布
        pb = (ProgressBar)findViewById(R.id.petProgressBar);
        pb.setVisibility(View.GONE);
        
		Button button1 = (Button)findViewById(R.id.b01);  
        button1.setOnClickListener(new Button.OnClickListener(){  
            @Override  
            public void onClick(View v) {  
                Intent intent = new Intent();  
                /* 开启Pictures画面Type设定为image */  
                intent.setType("image/*");  
                /* 使用Intent.ACTION_GET_CONTENT这个Action */  
                intent.setAction(Intent.ACTION_GET_CONTENT);   
                /* 取得相片后返回本画面 */  
                startActivityForResult(intent, 1);  
            }  
              
        });  
        Button button2 = (Button)findViewById(R.id.b02);   
        button2.setOnClickListener(new Button.OnClickListener(){  
            @Override  
            public void onClick(View v) {  
            	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

				startActivityForResult(intent, 0);
                  
            }  
              
        });  
        Button button3 = (Button)findViewById(R.id.petUpload);
        button3.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//System.out.println("上传");
				pb.setVisibility(View.VISIBLE);
				//new Thread(runnable).start();
				postHandler.post(runnable);
			}
        	
        });
        enjoy = (Button)findViewById(R.id.petEnjoy);
		enjoy.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				myTabhost.setCurrentTab(0);
				enjoy.setBackgroundResource(R.drawable.pet_enjoy_pressed);
				share.setBackgroundResource(R.drawable.pet_share);
			}
			
		});
		share = (Button)findViewById(R.id.petShare);
		share.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				myTabhost.setCurrentTab(1);
				enjoy.setBackgroundResource(R.drawable.pet_enjoy);
				share.setBackgroundResource(R.drawable.pet_share_pressed);
			}
			
		});
	}
	
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
		creatAppDir();
        if (resultCode == RESULT_OK&&requestCode == 1) {  
        	System.out.println("requestCode"+requestCode);
            Uri uri = data.getData(); 
            himg_path = getPath(getApplicationContext(), uri);
            Log.e("uri", uri.toString());   
            hbitmap = getimage(himg_path);
			hbitmap = compressImage(hbitmap);
			//ImageView imageView = (ImageView) findViewById(R.id.iv01);  
			/* 将Bitmap设定到ImageView */  
			imageView.setImageBitmap(hbitmap);
            
            	//String imagepath = "/sdcard/E-home/happyhome/upload.jpg";
    			File myCaptureFile = new File(Environment.getExternalStorageDirectory(),"E-homeland/happyhome/upload.jpg");
    		      if(!myCaptureFile.exists()){
    		    	  try {
    					myCaptureFile.createNewFile();
    				} catch (IOException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    		      }
    		      String imagepath = myCaptureFile.getPath();
    		      try
    		      {
    		        BufferedOutputStream bos = new BufferedOutputStream
    		        (new FileOutputStream(myCaptureFile));
    		        
    		        // 采用压缩转档方法 
    		        hbitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
    		        
    		        // 调用flush()方法，更新BufferStream 
    		        bos.flush();
    		        
    		        // 结束OutputStream 
    		        bos.close(); 					       
    		      }catch (Exception e)
    		      { 
    		    	  System.out.println(e.toString());
    		      }
    		      himg_path = imagepath;
            
        }
        else if (resultCode == RESULT_OK&&requestCode==0) {
    		System.out.println("存储图片");
			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
				Log.i("TestFile",
						"SD card is not avaiable/writeable right now.");
				return;
			}
			String name = new DateFormat().format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA)) + ".jpg";	
			//Toast.makeText(getBaseContext(), name, Toast.LENGTH_LONG).show();
			Bundle bundle = data.getExtras();
			hbitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
			System.out.println(hbitmap.getByteCount());
			FileOutputStream b = null;
			File file = new File(Environment.getExternalStorageDirectory(),"E-homeland/happyhome/"+name);
			String fileName = file.getPath();
			himg_path = fileName;
			try {
				b = new FileOutputStream(fileName);
				hbitmap.compress(Bitmap.CompressFormat.JPEG, 80, b);// 把数据写入文件
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					b.flush();
					b.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
    	//ImageView imageView = (ImageView) findViewById(R.id.iv01);  
        /* 将Bitmap设定到ImageView */  
        imageView.setImageBitmap(hbitmap); 
        }
    }  

   
	Handler handlerException = new Handler(){
		@Override
	    public void handleMessage(Message msg) {
	        super.handleMessage(msg);
	        
	        Bundle data = msg.getData();
	        Toast.makeText(getBaseContext(), data.getString("flag"),Toast.LENGTH_SHORT).show();
	        if(data.getString("flag","").equals("发送成功")){
	        	//System.out.println("发送成功");
	        	//iv.setBackgroundDrawable(null);
	        	//iv.setImageBitmap(null);
	        	hbitmap.recycle();
	        	imageView.setBackground(null);
	        	imageView.setBackgroundDrawable(null);
	        	imageView.setImageBitmap(null);
	        	variety.setText("");
	        	SharedPreferences mySharedPreferences = getSharedPreferences("user", Activity.MODE_PRIVATE); 
				SharedPreferences.Editor editor = mySharedPreferences.edit(); 
				int credit = mySharedPreferences.getInt("credit", 0);
				credit += 1;
				editor.putInt("credit",credit).commit();
	        	pb.setVisibility(View.GONE);
	        	//System.out.println("跳标签");
	        	myTabhost.setCurrentTabByTag("欣赏");
	        	postHandler.removeCallbacks(runnable);
	        	onRefresh();
	        }
	        if(data.getString("flag","").equals("发送失败")){
	        	pb.setVisibility(View.GONE);
	        	Toast.makeText(HappyhomeActivity.this,"发送失败，请检查一下网络",Toast.LENGTH_SHORT).show();
	        }
	    }
	};
	 //上传宠物图片
	Runnable runnable = new Runnable(){
	    @Override
	    public void run() {
	        //
	        // TODO: http request.
	        //
	    	if(isWifiConnected()||isNetConnected()){
	    		
				
					final File uploadFile = new File(himg_path);   
					//System.out.println(himg_path);
		        	 Map<String, String> params = new HashMap<String, String>();  
		        	 String nameTemp = " ";
		        	 if(!name.getText().toString().equals("")&&!name.getText().toString().equals(null))
		        		 nameTemp = name.getText().toString();
		        	 String varietyTemp = " ";
		        	 if(!variety.getText().toString().equals("")&&!variety.getText().toString().equals(null))
		        		 varietyTemp = variety.getText().toString();
		             params.put("happyhomeName",nameTemp);
		             
		             params.put("happyhomeRemark",varietyTemp);
		             SharedPreferences mySharedPreferences = getSharedPreferences("user", Activity.MODE_PRIVATE);
		             params.put("happyhomeOwner", mySharedPreferences.getString("userName", ""));
		             params.put("community", mySharedPreferences.getString("community", "第一小区"));
		           //  System.out.println(params.toString());
		             FormFile formfile = new FormFile("happyhomephoto.jpg", uploadFile, "jpg", "image/jpeg"); 
		             //System.out.println(params.toString());
		             try {
		            	 SocketHttpRequester.post(IP.ip+":3000/happyhome/pushhappyhome", params, formfile);
		            	 String re="";
		            			re= SocketHttpRequester.request;
		            	 if(!re.equals("fail")){
		            		 Message msg = new Message();
		     		        Bundle data = new Bundle();
		     		        data.putString("flag","发送成功");
		     		        msg.setData(data);
		     		        handlerException.sendMessage(msg);
		            	 }
		            	 else{
		            		 Message msg = new Message();
			     		        Bundle data = new Bundle();
			     		        data.putString("flag","发送失败");
			     		        msg.setData(data);
			     		        //pb.setVisibility(View.GONE);
			     		        handlerException.sendMessage(msg);
		            	 }
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println(e.toString());
					}  
				
		        Message msg = new Message();
		        Bundle data = new Bundle();
		        data.putString("back",back);
		        msg.setData(data);
		       // handler.sendMessage(msg);
	    	}else{
	    		Message msg = new Message();
		        Bundle data = new Bundle();
		        data.putString("flag","没有网络连接");
		        msg.setData(data);
		        handlerException.sendMessage(msg);
	    	}
	    	
	    }
	};
	
	
	public class myAdapter extends BaseAdapter{
		public Context mContext;
		public int mResource;
		public List<HashMap<String, Object>> mData;
		public String []mFrom;
		public int []mTo;
		public LayoutInflater inflater;

		

		public myAdapter(Context context,List<HashMap<String, Object>> data,int resource,String []from,int []to
				) {
			super();
			mContext = context;
			mData = data;
			mResource = resource;
			mFrom = from;
			mTo = to;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mData.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int positon1, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			positon = positon1;
			final ViewHolder holder;
			if(convertView == null){
				convertView = LayoutInflater.from(mContext).inflate(mResource,parent,false);
				holder = new ViewHolder();
				holder.happyhomePhoto = (ImageView)convertView.findViewById(mTo[0]);
				holder.happyhomeInfo = (TextView)convertView.findViewById(mTo[1]);
				//holder.zan = (Button)convertView.findViewById(R.id.petZan);
				holder.comment = (Button)convertView.findViewById(R.id.petCommentUpload);
				holder.edit = (TextView)convertView.findViewById(mTo[2]);
				holder.happyhomeComment = (TextView)convertView.findViewById(mTo[3]);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			final Map<String,?> dataSet = mData.get(positon);
			if(dataSet == null)return null;
			final Object data0 = dataSet.get(mFrom[0]);
			final Object data1 = dataSet.get(mFrom[1]);
			final Object data2 = dataSet.get(mFrom[2]);
			final Object data3 = dataSet.get(mFrom[3]);
			//holder.happyhomePhoto.setImageResource((Integer)data0);		
			holder.happyhomePhoto.setImageBitmap((Bitmap)data0);
			holder.happyhomeInfo.setText(data1.toString());
			holder.edit.setText(data2.toString());
			holder.happyhomeComment.setText(data3.toString());
			//holder.zan.setTag(positon1);
			/*holder.zan.setOnClickListener(new Button.OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(mContext,"赞",Toast.LENGTH_SHORT).show();
					positon = Integer.parseInt(holder.zan.getTag().toString());
					new Thread(runnableUploadZan).start();
				}
				
			});*/
			holder.comment.setTag(positon1);
			holder.comment.setOnClickListener(new Button.OnClickListener(){

				@Override
				public void onClick(final View arg0) {
					// TODO Auto-generated method stub
					//final String comment;
					Toast.makeText(mContext,"评论",Toast.LENGTH_SHORT).show();
					final EditText inputServer = new EditText(HappyhomeActivity.this);
			        AlertDialog.Builder builder = new AlertDialog.Builder(HappyhomeActivity.this);
			        builder.setTitle("评论").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
			                .setNegativeButton("Cancel", null);
			        
			        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			            public void onClick(DialogInterface dialog, int which) {
			               comment = inputServer.getText().toString();
			              // System.out.println(positon+comment);
			               HashMap<String, Object> map = listItem.get(Integer.parseInt(holder.comment.getTag().toString()));
				           map.remove("happyhomeComment");
				           map.put("happyhomeComment", comment);
				           listItem.set(Integer.parseInt(holder.comment.getTag().toString()), map);
				        //   System.out.println("位置"+holder.comment.getTag().toString());
				           listItemAdapter.notifyDataSetChanged();
				           positon = Integer.parseInt(holder.comment.getTag().toString());
				           JSONObject json = new JSONObject();
				           JSONObject temp = new JSONObject();
				           JSONArray temp1 = new JSONArray();
				           try {
							json = (JSONObject) happyhomeJsonArray.get(Integer.parseInt(holder.comment.getTag().toString()));
							SharedPreferences mySharedPreferences = getSharedPreferences("user", Activity.MODE_PRIVATE);
							temp.put("content", comment);
							temp.put("user", mySharedPreferences.getString("userName", ""));
							if(json.length() > 5){
								temp1 = json.getJSONArray("comment");
								temp1.put(temp);
							}else{
								temp1.put(temp);
							}
							json.put("comment", temp1);
							happyhomeJsonArray.put(Integer.parseInt(holder.comment.getTag().toString()), json);
						//	System.out.println(petJsonArray.toString());
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				           new Thread(runnableUploadComment).start();
			             }
			        });
			        builder.show();
				}
				
			});
			
			
			
			return convertView;
		}
		class ViewHolder{
			ImageView happyhomePhoto;
			TextView happyhomeInfo;
			//Button zan;
			Button comment;
			TextView edit;
			TextView happyhomeComment;
		}
		
	}
	//发送宠物赞
	/*		Runnable runnableUploadZan = new Runnable(){
			    @Override
			    public void run() {
			        //
			        // TODO: http request.
			        //
			    	if(isWifiConnected()||isNetConnected()){
			    		HttpClient client = new DefaultHttpClient();
						try {
							 HttpPost httppost = new HttpPost(IP.ip+":3000/happyhome/zan");     
							 List<NameValuePair> map = new ArrayList <NameValuePair>();
					         map.add(new BasicNameValuePair("happyhomeImageId",happyhomeIdList.get(positon)));
					         map.add(new BasicNameValuePair("zan","zan+1"));
							 httppost.setEntity(new UrlEncodedFormEntity(map));   
	 
							HttpResponse response = client.execute(httppost);
							if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
								// 取得返回的字符串
								back = EntityUtils.toString(response.getEntity());
								System.out.println(back+"wuhao");
							
								} else {
									back = "返回失败";
									System.out.println(back);
								}
						} catch (ClientProtocolException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
						
						
					   // post(IP.ip+":3000/post",val);
				        Message msg = new Message();
				        Bundle data = new Bundle();
				        data.putString("back",back);
				        msg.setData(data);
				        //handler.sendMessage(msg);
			    	}
			    	else{
			    		Message msg = new Message();
				        Bundle data = new Bundle();
				        data.putString("flag","网络没有连接");
				        msg.setData(data);
				        handlerException.sendMessage(msg);
			    	}
			    	
			    }
			};*/
		 //发送宠物评论
		Runnable runnableUploadComment = new Runnable(){
		    @Override
		    public void run() {
		        //
		        // TODO: http request.
		        //
		    	if(isWifiConnected()||isNetConnected()){
		    	HttpClient client = new DefaultHttpClient();
				try {
					   
					 HttpPost httppost = new HttpPost(IP.ip+":3000/happyhome/comment");     
					 List<NameValuePair> map = new ArrayList <NameValuePair>();
			         map.add(new BasicNameValuePair("comment",comment));
			         SharedPreferences mySharedPreferences = getSharedPreferences("user", Activity.MODE_PRIVATE);
			         map.add(new BasicNameValuePair("commentUser",mySharedPreferences.getString("userName", "")));
			         map.add(new BasicNameValuePair("happyhomeImageId",happyhomeIdList.get(positon)));
			         UrlEncodedFormEntity temp = new UrlEncodedFormEntity(map,"utf-8");
			         httppost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
					 httppost.setEntity(temp);  
					 
					HttpResponse response = client.execute(httppost);
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						// 取得返回的字符串
						back = EntityUtils.toString(response.getEntity());
						System.out.println(back+"wuhao");
					
						} else {
							back = "返回失败";
							System.out.println(back);
						}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
				
			   
		        Message msg = new Message();
		        Bundle data = new Bundle();
		        data.putString("back",back);
		        msg.setData(data);}
		        //handler.sendMessage(msg);}
		        else{
		        	Message msg = new Message();
			        Bundle data = new Bundle();
			        data.putString("flag","网络没有连接");
			        msg.setData(data);
			        handlerException.sendMessage(msg);
		        }
		    }
		};

		
		public static Bitmap getBitmapFromServer(String imagePath) {

			  HttpGet get = new HttpGet(imagePath);
			  HttpClient client = new DefaultHttpClient();
			  Bitmap pic = null;
			  try {
			   HttpResponse response = client.execute(get);
			   HttpEntity entity = response.getEntity();
			   InputStream is = entity.getContent();
			   BitmapFactory.Options options = new BitmapFactory.Options();
			   options.inPreferredConfig = Config.RGB_565;
			   pic = BitmapFactory.decodeStream(is,null,options);   

			  } catch (ClientProtocolException e) {
			   e.printStackTrace();
			  } catch (IOException e) {
			   e.printStackTrace();
			  }
			  return pic;
			 }
		/** 
	     * 检测网络是否连接 
	     *  
	     * @return 
	     */ 
	    private boolean isNetConnected() {  
	        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);  
	        if (cm != null) {  
	            NetworkInfo[] infos = cm.getAllNetworkInfo();  
	            if (infos != null) {  
	                for (NetworkInfo ni : infos) {  
	                    if (ni.isConnected()) {  
	                        return true;  
	                    }  
	                }  
	            }  
	        }  
	        return false;  
	    }  
	   
	    /** 
	     * 检测wifi是否连接 
	     *  
	     * @return 
	     */ 
	    private boolean isWifiConnected() {  
	        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);  
	        if (cm != null) {  
	            NetworkInfo networkInfo = cm.getActiveNetworkInfo();  
	            if (networkInfo != null 
	                    && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {  
	                return true;  
	            }  
	        }  
	        return false;  
	    }  
	   
	    /** 
	     * 检测3G是否连接 
	     *  
	     * @return 
	     */ 
	    private boolean is3gConnected() {  
	        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);  
	        if (cm != null) {  
	            NetworkInfo networkInfo = cm.getActiveNetworkInfo();  
	            if (networkInfo != null 
	                    && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {  
	                return true;  
	            }  
	        }  
	        return false;  
	    }  
	   
	    public void jiexi(){
	    	creatAppDir();
	    	ArrayList<String> imgpathlist=new ArrayList<String>();
	    	happyConstants.imgidlist.clear();
	    	//解析返回的信息
	    	try{
			JSONArray jsonArray = new JSONArray(back);
			if(jsonArray.length() == 10)
				flag++;
			//System.out.println(jsonArray.toString());
			
			//先判断有没有重复的
			//jsonArray = check(jsonArray);
			//JSONArray jsonArray = new JSONArray();
			//testList.clear();
			 for(int i = 0; i < jsonArray.length();i++){
				 System.out.println("解析json");
				 JSONObject json = jsonArray.getJSONObject(i);
				 String imagePath = "";
				 String imageId = "";
				 String happyhomeName = "";
				 String happyhomeRemark = "";
				 String happyhomeOwner = "";
				 String happyhomeComment ="";
				 String imageUrl="";
				 JSONArray json1 = new JSONArray();
				 String url = json.getString("imageUrl");
				 url = url.substring(2,url.length()-2);
				 imageUrl=url;
				 imagePath = IP.ip+":3000/uploads/"+url;
				 imageId = json.getString("imageId");
				 happyhomeName = json.getString("happyName");
				 happyhomeRemark = json.getString("remark");
				 happyhomeOwner = json.getString("happyhomeOwner");
				 if(json.has("comment")){
					 json1 = json.getJSONArray("comment");
					 if(json1.length()!=0)
						happyhomeComment = json1.getJSONObject(json1.length()-1).getString("content");
					 	System.out.println(happyhomeComment+"chongwupinglun");
				 }
				 
				 imgpathlist.add(imagePath);
				 happyConstants.imgidlist.add(imageUrl);
				// Bitmap bm = getBitmapFromServer(imagePath);
				// System.out.println(imagePath);
					//存储图片
					//String imagepath = "/sdcard/E-homeland/happyhome/happyhome"+happyhomeNumber+".jpg";
					happyhomeNumber++;
					File myCaptureFile = new File(Environment.getExternalStorageDirectory(),"E-homeland/happyhome/"+imageUrl+".jpg");
				      if(!myCaptureFile.exists()){
				    	  try {
							myCaptureFile.createNewFile();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				      }
				      String imagepath = myCaptureFile.getPath();
				      /*
				      try
				      {
				        BufferedOutputStream bos = new BufferedOutputStream
				        (new FileOutputStream(myCaptureFile));
				        
				        // 采用压缩转档方法 
				        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
				        
				        // 调用flush()方法，更新BufferStream 
				        bos.flush();
				        System.out.println("cunchutupian");
				        // 结束OutputStream 
				        bos.close(); 					       
				      }catch (Exception e)
				      { 
				    	  System.out.println(e.toString());
				      }
				      */
				      //存其他信息
				      happyhomeIdList.add(imageId);
				      String s=happyConstants.getFinished.get(imageUrl);
						try
						{
							 if(!s.equals("true"))
								 happyConstants.getFinished.put(imageUrl,"false");
						}
						catch(Exception e)
						{
							 happyConstants.getFinished.put(imageUrl,"false");

						}
				      jsonArray.getJSONObject(i).put("imageUrl", imagepath);
						
						HashMap<String, Object> map1 = new HashMap<String, Object>();  
			       //     map1.put("happyhomeImage", getBitmapFromServer(imagePath));//图像资源的ID  
			            map1.put("happyhomeName",happyhomeOwner);  
			            map1.put("happyhomeliuyan",happyhomeRemark);
			            map1.put("happyhomeComment", happyhomeComment);  
			         //  System.out.println(happyhomeComment+"happyhomeComment");
			            testList.add(map1);  
			 }
			 
			//将jsonArray加入到happyhomeJsonArray中 
			 for(int i = 0;i < jsonArray.length();i++){
				 happyhomeJsonArray.put(jsonArray.getJSONObject(i));
			 }
			 getImgHandler.obtainMessage(1,imgpathlist).sendToTarget();
	    	}catch(Exception e){
	    		System.out.println(e.toString());
	    	}
	    }
	    //更新图片的线程
	    Handler freshImgHandler=new Handler()
	    {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				System.out.println("更新照片");
				int i=msg.what;
				Bitmap bm=(Bitmap)msg.obj;
				try
				{
					listItem.get(i).put("happyhomeImage",bm);
					listItemAdapter.notifyDataSetChanged();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}	    	
	    };
	    //从网络上获取图片的线程
	    Handler getImgHandler=new Handler()
	    {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if(msg.what==1)
				{
					final ArrayList<String> list=(ArrayList<String>)msg.obj;
					for(int j=0;j<list.size();j++)
					{
						final int k=j;
						new Thread(new Runnable()
						{
							@Override
							public void run() {
								// TODO Auto-generated method stub
							//	System.out.println("开启线程获取照片"+list.get(k));
								String path=happyConstants.imgidlist.get(k);
								File imgDir = new File(Environment.getExternalStorageDirectory(), "E-homeLand/happyhome");
                                if(!imgDir.exists())
                                	imgDir.mkdirs();
								File[] imgs=imgDir.listFiles();
                                int m=0;
                                Bitmap bm=null;
                                int tag=0;
                                if(imgs!=null)
                                {
                                	 for(m=0;m<imgs.length;m++)
                                     {
                                     	if(imgs[m].getPath().contains(path)&&happyConstants.getFinished.get(path).equals("true"))
                                     	{
                                     	//	System.out.println("本地已经存在这个图片了");
                                     		bm=BitmapFactory.decodeFile(imgs[m].getPath());
                                     		tag=1;
                                     		break;
                                     	}
                                     }
                                }
                               
                                if(tag==0)
                               {
                                
                                	 bm = getBitmapFromServer(list.get(k));
 									File myCaptureFile = new File(Environment.getExternalStorageDirectory(), "E-homeLand/happyhome/"+path+".jpg");
 								      if(!myCaptureFile.exists()){
 								    	  try {
 											myCaptureFile.createNewFile();
 										} catch (IOException e) {
 											// TODO Auto-generated catch block
 											e.printStackTrace();
 										}
 								      }
 								      String imagepath = myCaptureFile.getPath();
 								      try
 								      {
 								        BufferedOutputStream bos = new BufferedOutputStream
 								        (new FileOutputStream(myCaptureFile));		        
 								        // 采用压缩转档方法 
 								        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
 								        // 调用flush()方法，更新BufferStream 
 								        bos.flush();
 								      //  System.out.println("存储图片");
 								        // 结束OutputStream 
 								        bos.close(); 					       
 								      }catch (Exception e)
 								      { 
 								    	  System.out.println(e.toString());
 								    	  e.printStackTrace();
 								      }
 	                                	happyConstants.getFinished.put(path,"true");
                                }
								 
								 freshImgHandler.obtainMessage(k,bm).sendToTarget();
							}
							
						}).start();
					}
					
				}
			}    	
	    };
	    public void creatAppDir()
		{
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			{
				//System.out.println("创建happyhome文件夹");
				File f=new File(Environment.getExternalStorageDirectory(),"E-homeland");
				if(!f.exists())
		      	f.mkdirs();
		      	File ff=new File(Environment.getExternalStorageDirectory(),"E-homeland/happyhome");
		      	if(!ff.exists())
		      	ff.mkdirs();
			}	
		}
	    public String readFile(){
	    	String res = "[]";
	    	try{
				File saveFile = new File(Environment.getExternalStorageDirectory(),"E-homeland/happyhome/happyhome.ss");
				if(!saveFile.exists()){
					return "[]";
				}
				
				FileInputStream fin = new FileInputStream(saveFile);

			    int length = fin.available(); 

			    byte [] buffer = new byte[length]; 

			    fin.read(buffer);     

			    res = EncodingUtils.getString(buffer, "UTF-8"); 

			    fin.close();   
	    	}catch (Exception e){
	    		e.printStackTrace();
	    	}
	    	return res;
			    
	    }
	    
	    public JSONArray check(JSONArray jsonArray){
	    	//只查后10个
	    	JSONArray temp = new JSONArray();
	    	if(happyhomeJsonArray.length()==0){
	    		System.out.println("changduwei0");
	    		return jsonArray;
	    	}
	    	int check = 0;
	    	
	    	if(happyhomeJsonArray.length() > 10){
	    		for(int j = 0;j < jsonArray.length();j++){
		    		check = 0;
		    		try {
		    			for(int i = happyhomeJsonArray.length()-11;i < happyhomeJsonArray.length();i++){
		    				if(jsonArray.getJSONObject(j).getString("imageId").equals(happyhomeJsonArray.getJSONObject(i).getString("imageId"))){
		    					check = 1;
		    					break;
		    				}
		    			}
		    			if(check == 0){
	    					temp.put(jsonArray.getJSONObject(j));
	    				}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	}
	    	}
	    	else{
	    		System.out.println(happyhomeJsonArray.toString());
	    		for(int j = 0;j < jsonArray.length();j++){
		    		check = 0;
		    		try {
		    			for(int i = 0;i < happyhomeJsonArray.length();i++){
		    				if(jsonArray.getJSONObject(j).getString("imageId").equals(happyhomeJsonArray.getJSONObject(i).getString("imageId"))){
		    					check = 1;
		    					System.out.println(jsonArray.getJSONObject(j).get("imageId"));
		    					break;
		    				}
		    			}
		    			if(check == 0){
	    					temp.put(jsonArray.getJSONObject(j));
	    				}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	}
	    	}
	    	System.out.println(temp.toString());
			return temp;
	    }
	    
	  //图片压缩
	    private Bitmap compressImage(Bitmap image) {

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
			int options = 100;
			while ( baos.toByteArray().length / 1024>32) {	//循环判断如果压缩后图片是否大于500kb,大于继续压缩		
				baos.reset();//重置baos即清空baos
				image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
				options -= 10;//每次都减少20
			}
			ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
			BitmapFactory.Options op = new BitmapFactory.Options();
			op.inPreferredConfig = Config.RGB_565;
			Bitmap bitmap = BitmapFactory.decodeStream(isBm, null,op);//把ByteArrayInputStream数据生成图片
			return bitmap;
		}
	  //根据uri获取文件真实路径
	    /**
	     * Get a file path from a Uri. This will get the the path for Storage Access
	     * Framework Documents, as well as the _data field for the MediaStore and
	     * other file-based ContentProviders.
	     * 
	     * @param context
	     *            The context.
	     * @param uri
	     *            The Uri to query.
	     * @author paulburke
	     */
	    public static String getPath(final Context context, final Uri uri) {

	        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

	        // DocumentProvider
	        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
	            // ExternalStorageProvider
	            if (isExternalStorageDocument(uri)) {
	                final String docId = DocumentsContract.getDocumentId(uri);
	                final String[] split = docId.split(":");
	                final String type = split[0];

	                if ("primary".equalsIgnoreCase(type)) {
	                    return Environment.getExternalStorageDirectory() + "/" + split[1];
	                }

	                // TODO handle non-primary volumes
	            }
	            // DownloadsProvider
	            else if (isDownloadsDocument(uri)) {

	                final String id = DocumentsContract.getDocumentId(uri);
	                final Uri contentUri = ContentUris.withAppendedId(
	                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

	                return getDataColumn(context, contentUri, null, null);
	            }
	            // MediaProvider
	            else if (isMediaDocument(uri)) {
	                final String docId = DocumentsContract.getDocumentId(uri);
	                final String[] split = docId.split(":");
	                final String type = split[0];

	                Uri contentUri = null;
	                if ("image".equals(type)) {
	                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	                } else if ("video".equals(type)) {
	                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	                } else if ("audio".equals(type)) {
	                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	                }

	                final String selection = "_id=?";
	                final String[] selectionArgs = new String[] { split[1] };

	                return getDataColumn(context, contentUri, selection, selectionArgs);
	            }
	        }
	        // MediaStore (and general)
	        else if ("content".equalsIgnoreCase(uri.getScheme())) {
	            return getDataColumn(context, uri, null, null);
	        }
	        // File
	        else if ("file".equalsIgnoreCase(uri.getScheme())) {
	            return uri.getPath();
	        }

	        return null;
	    }

	    /**
	     * Get the value of the data column for this Uri. This is useful for
	     * MediaStore Uris, and other file-based ContentProviders.
	     * 
	     * @param context
	     *            The context.
	     * @param uri
	     *            The Uri to query.
	     * @param selection
	     *            (Optional) Filter used in the query.
	     * @param selectionArgs
	     *            (Optional) Selection arguments used in the query.
	     * @return The value of the _data column, which is typically a file path.
	     */
	    public static String getDataColumn(Context context, Uri uri, String selection,
	            String[] selectionArgs) {

	        Cursor cursor = null;
	        final String column = "_data";
	        final String[] projection = { column };

	        try {
	            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
	                    null);
	            if (cursor != null && cursor.moveToFirst()) {
	                final int column_index = cursor.getColumnIndexOrThrow(column);
	                return cursor.getString(column_index);
	            }
	        } finally {
	            if (cursor != null)
	                cursor.close();
	        }
	        return null;
	    }

	    /**
	     * @param uri
	     *            The Uri to check.
	     * @return Whether the Uri authority is ExternalStorageProvider.
	     */
	    public static boolean isExternalStorageDocument(Uri uri) {
	        return "com.android.externalstorage.documents".equals(uri.getAuthority());
	    }

	    /**
	     * @param uri
	     *            The Uri to check.
	     * @return Whether the Uri authority is DownloadsProvider.
	     */
	    public static boolean isDownloadsDocument(Uri uri) {
	        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	    }

	    /**
	     * @param uri
	     *            The Uri to check.
	     * @return Whether the Uri authority is MediaProvider.
	     */
	    public static boolean isMediaDocument(Uri uri) {
	        return "com.android.providers.media.documents".equals(uri.getAuthority());
	    }
	    //从文件读bitmap时压缩
	    private Bitmap getimage(String srcPath) {  
	        BitmapFactory.Options newOpts = new BitmapFactory.Options();  
	        //开始读入图片，此时把options.inJustDecodeBounds 设回true了  
	        newOpts.inJustDecodeBounds = true;  
	        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空  
	          
	        newOpts.inJustDecodeBounds = false;  
	        int w = newOpts.outWidth;  
	        int h = newOpts.outHeight;  
	        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
	        float hh = 800f;//这里设置高度为800f  
	        float ww = 480f;//这里设置宽度为480f  
	        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
	        int be = 1;//be=1表示不缩放  
	        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
	            be = (int) (newOpts.outWidth / ww);  
	        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
	            be = (int) (newOpts.outHeight / hh);  
	        }  
	        if (be <= 0)  
	            be = 1;  
	        newOpts.inSampleSize = be;//设置缩放比例  
	        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
	        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
	        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩  
	    }

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			// TODO Auto-generated method stub
			return super.onTouchEvent(event);
		}
    //用于刷新的Runnable
		Runnable freshRunnable=new Runnable()
		{
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(isNetConnected()||isWifiConnected()){
					HttpClient client = new DefaultHttpClient();
					try {   
						SharedPreferences mySharedPreferences = getSharedPreferences("user", Activity.MODE_PRIVATE);
						String community = mySharedPreferences.getString("community", "第一小区");
						 HttpGet httpget = new HttpGet(IP.ip+":3000/happyhome/happyhomeinfo?skip="+0+"&community="+community);
						HttpResponse response = client.execute(httpget);
						if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
							System.out.println("跳"+flag);
							// 取得返回的字符串
							back = EntityUtils.toString(response.getEntity(),"UTF-8");
						//	System.out.println("服务器返回的数值为"+back);
							} else {
								back = "返回失败";
								//System.out.println(back);
						}
						if(!back.equals("返回失败")&&!back.equals("[]")){
							jiexi();
						}
						
						
						
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					//petListView.onRefreshComplete();
					showNumber += testList.size();
				//	listItem.clear();
					//testList.addAll(listItem);
					listItem.clear();
					listItem.addAll(testList);
					//petListView.setResultSize(testList.size());
					listItemAdapter.notifyDataSetChanged();
					testList.clear();	
					onLoad();
					process.setVisibility(View.GONE);
					happyConstants.freshFinished=true;

					}else{
						Message msg = new Message();
						Bundle data = new Bundle();
						data.putString("flag","网络没有连接");
						msg.setData(data);
						handlerException.sendMessage(msg);
					}
			}			
		};
		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			process.setVisibility(View.VISIBLE);
			if(happyConstants.freshFinished)
			{
				happyConstants.freshFinished=false;
				mHandler.postDelayed(freshRunnable, 2000);
			}
		}

		@Override
		public void onLoadMore() {
			// TODO Auto-generated method stub
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					if(showNumber < happyhomeJsonArray.length()){
						int k = showNumber + 5;
						try{
						for(int j = showNumber;j < happyhomeJsonArray.length()&&j < k;j++){
							showNumber = j;
							JSONObject json = happyhomeJsonArray.getJSONObject(j);
							JSONArray json1 = new JSONArray();
							String happyhomeComment = "";
							if(json.length()>5){
								 json1 = json.getJSONArray("comment");
								 if(json1.length()!=0)
									happyhomeComment = json1.getJSONObject(json1.length()-1).getString("content");
								 	//System.out.println(happyhomeComment+"chongwupinglun");
							 }
							HashMap<String, Object> map = new HashMap<String, Object>(); 
				            map.put("happyhomeImage", BitmapFactory.decodeFile(json.getString("imageUrl")));//图像资源的ID  
				            map.put("happyhomeName", "名字:"+json.getString("happyhomeName")+"\n种类:"+json.getString("variety")+"\n主人:"+json.getString("happyhomeOwner"));  
				            map.put("happyhomeComment", happyhomeComment);  
				            listItem.add(map);  
							happyhomeIdList.add(json.getString("imageId"));
							listItemAdapter.notifyDataSetChanged();
						}
						}catch(Exception e){
							e.printStackTrace();
						}
					}
					onLoad();
				}
			}, 2000);
		}  
		private void onLoad() {
			SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("yyyy年MM月dd日    HH:mm:ss     ");     
			Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间     
			String    str    =    formatter.format(curDate);     
			happyhomeListView.stopRefresh();
			happyhomeListView.stopLoadMore();
			happyhomeListView.setRefreshTime(str);
		}
		@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			listItem.clear();
			testList.clear();
			happyConstants.getFinished.clear();
			happyConstants.freshFinished=true;
			System.out.println("销毁activity....");
			mHandler.removeCallbacks(freshRunnable);
			happyhomeListView.setBackground(null);
            setContentView(R.layout.null_view);
            System.gc();
          //  android.os.Process.killProcess(android.os.Process.myPid());          
            this.finish();
			super.onDestroy();

		}
		@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub  v
			File saveFile = new File(Environment.getExternalStorageDirectory(),"E-homeland/happyhome/happyhome.ss");
			if(!saveFile.exists())
				try {
					saveFile.createNewFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					//System.out.println("create"+e1.toString());
				}
			
			try{ 
	        	FileOutputStream outStream = new FileOutputStream(saveFile);
	        	outStream.write(happyhomeJsonArray.toString().getBytes());
				outStream.close();
				

			    }catch(Exception e){ 
			    	//System.out.println("write"+e.toString()); 
			} 
		 
			super.onBackPressed();
		}
}


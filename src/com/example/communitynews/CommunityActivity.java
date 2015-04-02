package com.example.communitynews;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.example.infoPush.infoPushActivity;
import com.example.logIn.Constants;
import com.example.manage.*;
import com.example.tools.IP;
import com.example.tools.ImageCompress;
import com.example.tools.ImageShow;
import com.example.wu1.XListView;
import com.example.wu1.XListView.IXListViewListener;


import com.example.Around.AroundActivity;
import com.example.Around.AroundAdditionActivity;
import com.example.bbm.MyVoice;
import com.example.bbm.Utils;
import com.example.communitynews.*;
import com.example.communitynews.DownLoadImage.ImageCallback;

import android.R.layout;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CommunityActivity extends Activity implements IXListViewListener{
	private XListView listview;
	private ProgressDialog dialog;
	int []flag;
	
	private MyAdapter adapter;
	String savepath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/E-homeland/communitynewtemp/";
	//String imgsavepath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/E-homeland/imageshow/";
	JSONArray jsonArray1;
	String community;
	private static String communityclass;
	List<Map<String,Object>> list;
	List<Map<String,Object>> historylist;
	private Handler threadhandler=new Handler();
	PrintStream ps;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("community class");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.communitynews);
		LinearLayout bg=(LinearLayout)findViewById(R.id.com_bg);
		ImageView bw=(ImageView)findViewById(R.id.backarrow);
		bw.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				//startActivity(new Intent(getApplicationContext(),AroundActivity.class));
				CommunityActivity.this.finish();
			}
		});
		File f=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/E-homeland");
	    if(!f.exists())
		   f.mkdirs();
	    /*
	    File fn=new File(savepath);
	    if(!fn.exists())
	    	fn.mkdirs();
	    File fx=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/E-homeland/imageshow");
	    if(!fx.exists())
	    	fx.mkdirs();
	    File fk=new File(savepath+"imgtemp");
	    if(!fk.exists())
	    	fk.mkdirs();
	    	*/
	    TextView v=(TextView)findViewById(R.id.communityclass);
		if(communityclass.equals("news"))
			{
			v.setText("小区头条");
			BitmapFactory.Options opt = new BitmapFactory.Options();

			opt.inPreferredConfig = Bitmap.Config.RGB_565;

			opt.inPurgeable = true;

			opt.inInputShareable = true;

			InputStream is = getResources().openRawResource(

					 R.drawable.comnews_newsbg );

			Bitmap bm = BitmapFactory.decodeStream(is, null, opt);

			BitmapDrawable bd = new BitmapDrawable(getResources(), bm);

			 //holder.iv.setBackgroundDrawable(bd);
			//bg.setBackgroundResource(R.drawable.comnews_newsbg);}
			bg.setBackgroundDrawable(bd);
			}
		else if(communityclass.equals("exposure"))
			{
			v.setText("小区随拍");
			BitmapFactory.Options opt = new BitmapFactory.Options();

			opt.inPreferredConfig = Bitmap.Config.RGB_565;

			opt.inPurgeable = true;

			opt.inInputShareable = true;

			InputStream is = getResources().openRawResource(

					 R.drawable.comnews_photobg );

			Bitmap bm = BitmapFactory.decodeStream(is, null, opt);

			BitmapDrawable bd = new BitmapDrawable(getResources(), bm);
			//bg.setBackgroundResource(R.drawable.comnews_photobg);
			bg.setBackgroundDrawable(bd);
			}
		else if(communityclass.equals("activity"))
			{
			v.setText("小区活动");
			BitmapFactory.Options opt = new BitmapFactory.Options();

			opt.inPreferredConfig = Bitmap.Config.RGB_565;

			opt.inPurgeable = true;

			opt.inInputShareable = true;

			InputStream is = getResources().openRawResource(

					 R.drawable.comnews_actbg );

			Bitmap bm = BitmapFactory.decodeStream(is, null, opt);

			BitmapDrawable bd = new BitmapDrawable(getResources(), bm);
		//	bg.setBackgroundResource(R.drawable.comnews_actbg);
			bg.setBackgroundDrawable(bd);

			} 
		else if(communityclass.equals("notice"))
		{
			v.setText("社区公告");
			BitmapFactory.Options opt = new BitmapFactory.Options();

			opt.inPreferredConfig = Bitmap.Config.RGB_565;

			opt.inPurgeable = true;

			opt.inInputShareable = true;

			InputStream is = getResources().openRawResource(

					 R.drawable.comnews_actbg );

			Bitmap bm = BitmapFactory.decodeStream(is, null, opt);

			BitmapDrawable bd = new BitmapDrawable(getResources(), bm);
		   // bg.setBackgroundResource(R.drawable.comnews_actbg);
			bg.setBackgroundDrawable(bd);
		} 
	    dialog=new ProgressDialog(this);
		dialog.setTitle("提示");
		dialog.setMessage("正在加载，请稍后...");
	   
	    
		
		listview=(XListView)findViewById(R.id.community_listview);
		listview.setPullLoadEnable(false);
		listview.setXListViewListener(this);
		//SharedPreferences sm=getSharedPreferences("user", MODE_PRIVATE);
		community=Constants.Community;
		
		//Toast.makeText(getApplicationContext(), community+"|"+communityclass, Toast.LENGTH_SHORT).show();
		
		adapter=new MyAdapter(this);		
		//new Thread(setinfo).start();
		Intent intent=this.getIntent();
		Bundle data=intent.getExtras();
		String json=data.getString("json");
		if(json.equals(""))
		threadhandler.post(setinfo);
		else
			freshView(json);
	}
	public void freshView(String json)
	{
		list=new ArrayList<Map<String,Object>>();
		try {
				JSONArray jsonArray=new JSONArray(json);
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				String date=sdf.format(new java.util.Date());				
				for(int i=0;i<jsonArray.length();i++){			
					JSONObject jsonObject=jsonArray.getJSONObject(i);
					Map<String,Object> map=new HashMap<String, Object>();
					map.put("id", jsonObject.getString("_id"));
					map.put("title",jsonObject.getString("title"));
					map.put("content",jsonObject.getString("content"));
					if(jsonObject.getString("img").length()!=2)
					{String imgstring=jsonObject.getString("img");
					imgstring=imgstring.substring(1, imgstring.length()-1);
					//System.out.println(imgstring);
					String [] img=null;
					img=imgstring.split("\\,");
					for(int j=0;j<img.length;j++)
					{
						img[j]=img[j].substring(1, img[j].length()-1);
					}
				for(int j=0;j<4;j++)
				{
					if(img.length>j)
						{
						map.put("img"+j, img[j]);
						 
						}
				}}
					list.add(map);
					
				}
				//ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String,Object> m=new HashMap<String, Object>();
	    m.put("title", "点击查看历史消息");
	    m.put("content", "  ");
	    list.add(m);
		handler.obtainMessage(0,list).sendToTarget();
	}
	public static void setclass(String Class)
	{
		communityclass=Class;
	}
	
	public class MyAdapter extends BaseAdapter{
		private Context context;
		private LayoutInflater layoutInflater;
		private List<Map<String,Object>> list=null;
		public MyAdapter(Context context){
			this.context=context;
			layoutInflater=LayoutInflater.from(context);			
		}
		
		public void setData(List<Map<String,Object>> list){
			this.list=list;
		}
		
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view=null;
			
				
			//if(convertView==null){
							
			//}else{
				//view=convertView;
			//}
				if(list.get(position).containsKey("img0"))
				{
					view=layoutInflater.inflate(R.layout.communittnews_item,null);	
			TextView title=(TextView)view.findViewById(R.id.item_title);
			TextView content=(TextView)view.findViewById(R.id.item_content);
			//final ImageView imageview1=(ImageView)view.findViewById(R.id.item_image1);
			
			if(list.get(position).get("title").toString().equals("以下是历史消息"))
				{
				 title.setText(list.get(position).get("title").toString());
				 content.setText("");
				// imageview1.setImageBitmap(null);
				}
			else
			{
			   title.setText(list.get(position).get("title").toString());
			if(list.get(position).get("content").toString().length()<20)
				content.setText(list.get(position).get("content").toString());
			else
			content.setText(list.get(position).get("content").toString().substring(0,20)+"...");
			}
			if(list.get(position).containsKey("img0"))
			{
				String name=list.get(position).get("img0").toString();

				//String path=IP.ip+":3000/uploads/"+list.get(position).get("img0").toString();
				//System.out.println(path);
			 //  imageview1.setImageDrawable(Drawable.createFromPath(savepath+"imgtemp/"+name+".png"));
			}
			else {
				//imageview1.setImageDrawable(null);
		//	imageview1.setVisibility(View.INVISIBLE);
			}}
				else 
				{
					view=layoutInflater.inflate(R.layout.communitynews_item_nopic,null);	
					TextView title=(TextView)view.findViewById(R.id.item_title);
					TextView content=(TextView)view.findViewById(R.id.item_content);
					
					if(list.get(position).get("title").toString().equals("以下是历史消息"))
						{
						 title.setText(list.get(position).get("title").toString());
						 content.setText("");
						}
					else
					{
					   title.setText(list.get(position).get("title").toString());
					if(list.get(position).get("content").toString().length()<20)
						content.setText(list.get(position).get("content").toString());
					else
					content.setText(list.get(position).get("content").toString().substring(0,20)+"...");
					}
					
				}
		
			
		
		
		return view;
		}
	}
	Handler handler=new Handler()
	{
		public void handleMessage(Message msg) {
			if(msg.what==0)
			{
				listview.stopRefresh();
				 listview.setRefreshTime(Utils.getCurrentTime());
				final List<Map<String,Object>> result=(List<Map<String,Object>>)msg.obj;
				flag=new int[result.size()];
				for(int i=0;i<result.size();i++)
				{
					//if(result.get(i).containsKey("img0"))
						//setimage(result.get(i).get("img0").toString(),i);
					//else 
						flag[i]=1;
				} 
				
				
				adapter.setData(result);
				adapter.notifyDataSetChanged();
				listview.setAdapter(adapter);
				listview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						if(!(arg2==0))
						{
					
						int tr=arg2-1;
						Intent x=new Intent(getApplicationContext(),CommunityInfoActivity.class);
						JSONObject data=new JSONObject();
						if(result.get(tr).get("title").toString().equals("点击查看历史消息"))
							{
							   sethistoryinfo();
							   adapter.setData(historylist);
							   adapter.notifyDataSetChanged();
								listview.setAdapter(adapter);
								listview.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(
											AdapterView<?> arg0, View arg1,
											int arg2, long arg3) {
										// TODO 自动生成的方法存根
										if(arg2==0)
											;
										else if(historylist.get(arg2-1).get("title").toString().equals("返回今日消息"))
												threadhandler.post(setinfo);
										else 
										{
											int tr=arg2-1;
											Intent x=new Intent(getApplicationContext(),CommunityInfoActivity.class);
											JSONObject data=new JSONObject();
											try {
												
												data.put("class", communityclass);
												data.put("title", historylist.get(tr).get("title"));
												data.put("content", historylist.get(tr).get("content"));
												if(historylist.get(tr).containsKey("img0"))
												{
													data.put("img0", historylist.get(tr).get("img0"));
												}
												if(historylist.get(tr).containsKey("img1"))
												{
													data.put("img1", historylist.get(tr).get("img1"));
												}
												if(historylist.get(tr).containsKey("img2"))
												{
													data.put("img2", historylist.get(tr).get("img2"));
												}
												if(historylist.get(tr).containsKey("img3"))
												{
													data.put("img3", historylist.get(tr).get("img3"));
												}
											} catch (JSONException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											
											x.putExtra("json", data.toString());
											startActivity(x);
										}
									}
								
								
								});
							}
						else
						{try {
							
							data.put("class", communityclass);
							data.put("title", result.get(tr).get("title"));
							data.put("content", result.get(tr).get("content"));
							if(result.get(tr).containsKey("img0"))
							{
								data.put("img0", result.get(tr).get("img0"));
							}
							if(result.get(tr).containsKey("img1"))
							{
								data.put("img1", result.get(tr).get("img1"));
							}
							if(result.get(tr).containsKey("img2"))
							{
								data.put("img2", result.get(tr).get("img2"));
							}
							if(result.get(tr).containsKey("img3"))
							{
								data.put("img3", result.get(tr).get("img3"));
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						x.putExtra("json", data.toString());
						startActivity(x);}}
					}
				});
				adapter.notifyDataSetChanged();
				// 隐藏对话框
				dialog.dismiss();
			}
			
		}
	};
	
  Runnable setinfo=new Runnable() {
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("开启获取消息线程");
		list=new ArrayList<Map<String,Object>>();
		if(AroundActivity.isNetworkConnected(getApplicationContext())==true)
		{
		//	;
		//}
		//else{
		//System.out.println("aaa");
		
		try {
			// 获取网络JSON格式数据
			//Toast.makeText(getApplicationContext(),"1221",Toast.LENGTH_SHORT).show();	
		HttpClient httpClient=new DefaultHttpClient();
		String url=CommonUri.PRODUCT_URL+"communitynew/getnewsid?get="+communityclass+"&community="+community;
			HttpGet httpGet=new HttpGet(url);
			HttpResponse httpResponse=httpClient.execute(httpGet);
			

				String jsonString=EntityUtils.toString(httpResponse.getEntity(),"utf-8");
				// 解析Json格式数据，并使用一个List<Map>存放
				JSONArray jsonArray=new JSONArray(jsonString);
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				String date=sdf.format(new java.util.Date());
				//File record=new File(savepath+"/"+communityclass+date+".rd");
				// PrintStream ps=new PrintStream(new FileOutputStream(record));					
				for(int i=0;i<jsonArray.length();i++){
					
					JSONObject jsonObject=jsonArray.getJSONObject(i);
					//System.out.println(jsonObject.toString());
					//ps.print(jsonString);
					Map<String,Object> map=new HashMap<String, Object>();
					map.put("id", jsonObject.getString("_id"));
					map.put("title",jsonObject.getString("title"));
					map.put("content",jsonObject.getString("content"));
					if(jsonObject.getString("img").length()!=2)
					{String imgstring=jsonObject.getString("img");
					imgstring=imgstring.substring(1, imgstring.length()-1);
					//System.out.println(imgstring);
					String [] img=null;
					img=imgstring.split("\\,");
					//System.out.println(img.length);
					
						
						
					for(int j=0;j<img.length;j++)
					{
						
						img[j]=img[j].substring(1, img[j].length()-1);
						//System.out.println(img[j]);
					}
					
				for(int j=0;j<4;j++)
				{
					if(img.length>j)
						{
						map.put("img"+j, img[j]);
						 
						}
				}}
					list.add(map);
					
				}
				//ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		}
		Map<String,Object> m=new HashMap<String, Object>();
	    m.put("title", "点击查看历史消息");
	    m.put("content", "  ");
	    list.add(m);
		handler.obtainMessage(0,list).sendToTarget();
	}
};
public void sethistoryinfo()
{
	historylist=new ArrayList<Map<String,Object>>();
	Map<String,Object> m=new HashMap<String, Object>();
    m.put("title", "返回今日消息");
    m.put("content", "  ");
    historylist.add(m);
	File cmdir=new File(savepath);
if(cmdir.exists())
{
	File[] files=cmdir.listFiles(new FileFilter()
	  {
		@Override
		public boolean accept(File arg0) {
			// TODO Auto-generated method stub
			return arg0.getName().endsWith(".rd");
		}		  
	  });
	
	 

	 for(File f:files)
	  {
		  try {
			String time = f.getName();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			
			String nowtime=sdf.format(new java.util.Date());
			if(time.contains(nowtime))
			{
				continue;
			}
			else if(!f.getName().contains(communityclass))
			{
				continue;
			}
			else 
			{
				
				FileInputStream fin = new FileInputStream(f);
				int length = fin.available(); 

			    byte [] buffer = new byte[length]; 

			    fin.read(buffer);     

			    String res = EncodingUtils.getString(buffer, "UTF-8"); 

			    fin.close();     
			    JSONArray jsa=new JSONArray(res);
			    for(int i=0;i<jsa.length();i++){
					int has=0;
					JSONObject jsonObject=jsa.getJSONObject(i);
					System.out.println(jsonObject.toString());
					for(int j=0;j<list.size();j++)
					{
						if(jsonObject.getString("_id").equals(list.get(j).get("id")))
							{has=1;
							break;}
					}
					for(int j=0;j<historylist.size();j++)
					{
						if(jsonObject.getString("_id").equals(historylist.get(j).get("id")))
							{has=1;
							break;}
					}
					if(has==1)
						continue;
					Map<String,Object> map=new HashMap<String, Object>();
					map.put("title",jsonObject.getString("title"));
					map.put("content",jsonObject.getString("content"));
					map.put("id",jsonObject.getString("_id"));
					if(jsonObject.getString("img").length()!=2)
					{String imgstring=jsonObject.getString("img");
					imgstring=imgstring.substring(1, imgstring.length()-1);
					System.out.println(imgstring);
					String [] img=null;
					img=imgstring.split("\\,");
					System.out.println(img.length);
					
						
						
					for(int j=0;j<img.length;j++)
					{
						
						img[j]=img[j].substring(1, img[j].length()-1);
						System.out.println(img[j]);
					}
					
				for(int j=0;j<4;j++)
				{
					if(img.length>j)
						{map.put("img"+j, img[j]);
						 
						}
				     
				}}
					historylist.add(map);
					
				}
			}
            }catch(IOException e){
            e.printStackTrace();
           	 } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            }	}
}
    public void clearNeedlessCache()
    {
    	File cmdir=new File(savepath);
    	sethistoryinfo();
    	if(cmdir.exists())
    	{
    		File[] files=cmdir.listFiles(new FileFilter()
    		  {
    			@Override
    			public boolean accept(File arg0) {
    				// TODO Auto-generated method stub
    				return arg0.getName().endsWith(".rd");
    			}		  
    		  });
    		
    		 

    		 for(File f:files)
    		  {
    			  try {
    				String time = f.getName();
    				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    				
    				String nowtime=sdf.format(new java.util.Date());
    				if(time.contains(nowtime))
    				{
    					continue;
    				}
    				else if(!f.getName().contains(communityclass))
    				{
    					continue;
    				}
    				else 
    				{
    					System.out.println(list.size());
    					System.out.println(f.getName());
    					FileInputStream fin = new FileInputStream(f);
    					int length = fin.available(); 

    				    byte [] buffer = new byte[length]; 

    				    fin.read(buffer);     

    				    String res = EncodingUtils.getString(buffer, "UTF-8"); 

    				    fin.close();     
    				    JSONArray jsa=new JSONArray(res);
    				    int [] index=new int[historylist.size()];
    				    
    				    for(int j=0;j<jsa.length();j++)
    				    { 
    				    	
    				    	for(int i=0;i<list.size()-1;i++)
    				    {
    				    	System.out.println(list.get(i).get("title").toString());
    				    	String id=list.get(i).get("id").toString();
    				    	if(jsa.getJSONObject(j).getString("_id").toString().equals(id))
    				    		{f.delete();
    				    	
    				    		 break;
    				    		}
    				    }
    				    	
    				    }
    				 
    				   
    				    	
    				    }
    				}catch(IOException e){
    		            e.printStackTrace();
    	           	 } catch (JSONException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			  }
    }
    	}
   

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	/*
	private void setimage(final String url,final int i)
	{
		flag[i]=0;
		File f=new File(savepath+"imgtemp/"+url+".png");
		if(f.exists())
			{System.out.println(url+ " is exist");
			flag[i]=1;}
		else{
		DownLoadImage downLoadImage1=new DownLoadImage(IP.ip+":3000/uploads/"+url);
		downLoadImage1.loadImage(new ImageCallback() {				
				@Override
			public void getBitmap(Bitmap bm) {
				try {
					ImageCompress.saveMyBitmap(url, bm, savepath+"imgtemp/");
					flag[i]=1;
					adapter.notifyDataSetChanged();
					listview.setAdapter(adapter);
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				
			}
		});	}
	}
	*/
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		System.out.println("销毁activiyt");
		 threadhandler.removeCallbacks(setinfo);
		 setContentView(R.layout.null_view);	
		 CommunityActivity.this.finish();
		super.onDestroy();
	}
	@Override
	public void onRefresh() {
		// TODO 自动生成的方法存根
		if(isdone())	
			threadhandler.post(setinfo);
	}
	@Override
	public void onLoadMore() {
		// TODO 自动生成的方法存根
		;
	}
	private boolean isdone()
	{
		
		for(int i=0;i<flag.length;i++)
		{
			if(!(flag[i]==1))
			return false;	
		}
		return true;
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK) { 
        	/*
        	if(flag==null)
    		{
    			//String path=savepath+"imgtemp/";
    			//File f=new File(path);
    			//if(f.exists())
    			//	f.delete();
    		}
        	else {
        	for(int i=0;i<flag.length;i++)
        	{
        		
        		if(flag[i]!=1)
        		{
        			String path=savepath+"imgtemp/"+list.get(i).get("img0")+".png";
        			File f=new File(path);
        			if(f.exists())
        				f.delete();
        		}
        	}}
        	//clearNeedlessCache();
        	*/
        	onDestroy();
        	
        	
            return true;   
        } else  
            return super.onKeyDown(keyCode, event);  
    }  
	
	
	}



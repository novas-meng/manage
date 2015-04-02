package com.example.infoPush;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.manage.R;
import com.example.tools.FormFile;
import com.example.tools.SocketHttpRequester;
import com.example.utils.Constants;

@SuppressLint("ShowToast")
public class infoUploadActivity extends Activity{
	private String save = "helloworld";
	private EditText content,price,contact,remark;
	private Spinner type;
	private ImageView upload;
	private ProgressBar pb;
	String back = "";
	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
        setContentView(R.layout.null_view);
		infoUploadActivity.this.finish();
		super.onDestroy();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_upload);
        LinearLayout layout=(LinearLayout)findViewById(R.id.infouploadlayout);
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		InputStream is = getResources().openRawResource(
				 R.drawable.info_uploadbg );
		Bitmap bm = BitmapFactory.decodeStream(is, null, opt);
		BitmapDrawable bd = new BitmapDrawable(getResources(), bm);
		layout.setBackgroundDrawable(bd);
		type = (Spinner)findViewById(R.id.infoUploadType);
		content = (EditText)findViewById(R.id.infoUploadContent);
		price = (EditText)findViewById(R.id.infoUploadPrice);
		contact = (EditText)findViewById(R.id.infoUploadContact);
		remark = (EditText)findViewById(R.id.infoUploadRemark);
		upload = (ImageView)findViewById(R.id.infoUpload);
		pb = (ProgressBar)findViewById(R.id.infoProgressBar);
		pb.setVisibility(View.GONE);
		ImageView image=(ImageView)findViewById(R.id.infouploadback);
		image.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				infoUploadActivity.this.setContentView(R.layout.null_view);
				infoUploadActivity.this.finish();
			}
			
		});
		String []estate = {"二手房","二手物品","招聘","拼车","家政"};
		ArrayList<String> allEstate = new ArrayList<String>();
		for(int i = 0;i < estate.length;i++){
			allEstate.add(estate[i]);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,allEstate);
		adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		type.setAdapter(adapter);
		
		upload.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(isNetConnected()||isWifiConnected()){
					if(content.getText().toString().equals(""))
						Toast.makeText(getApplicationContext(), "内容未填", Toast.LENGTH_SHORT).show();
					else if(price.getText().toString().equals(""))
						Toast.makeText(getApplicationContext(), "价格未填", Toast.LENGTH_SHORT).show();
					else if(contact.getText().toString().length()!=11&&contact.getText().toString().length()!=8)
					{
						Toast.makeText(getApplicationContext(), "电话位数有误", Toast.LENGTH_SHORT).show();

					}
					else{
					pb.setVisibility(View.VISIBLE);
					new Thread(runnable).start();}
				}else
					Toast.makeText(infoUploadActivity.this,"网络没有连接",Toast.LENGTH_SHORT).show();
				
			}
		});
	}
	

	Runnable runnable = new Runnable(){    //发布信息的线程
	    @SuppressWarnings("deprecation")
		@Override
	    public void run() {
	        //
	        // TODO: http request.
	        //
	    	try{
	    	
	    		JSONObject obj = new JSONObject();  
				 if(type.getSelectedItem().toString().equals("二手房")){
					 obj.put("type","secondHouse"); 
					 save = "secondHouseß";
				 }	 
				 else if(type.getSelectedItem().toString().equals("二手物品")){
					 obj.put("type","secondGood");
					 save = "secondGoodß";
				 } 
				 else if(type.getSelectedItem().toString().equals("招聘")){
					 obj.put("type","recruit");
					 save = "rescruitß";
				 } 
				 else if(type.getSelectedItem().toString().equals("拼车")){
					 obj.put("type","carPool");
					 save = "carPoolß";
				 }
				 else if(type.getSelectedItem().toString().equals("家政")){
					 obj.put("type","houseKeeping");
					 save = "houseKeepingß";
				 }
				 obj.put("content",content.getText().toString()); 
				 save += content.getText().toString()+"ß";
				 obj.put("price",price.getText().toString());   
				 save += price.getText().toString()+"ß";
				 obj.put("contact",contact.getText().toString());
				 save += contact.getText().toString()+"ß";
				 obj.put("remark",remark.getText().toString());
				 save += remark.getText().toString()+"ß";
				 obj.put("community", "第一小区");
				 FormFile fm[]= new FormFile[0] ;    
	    	
					 Map<String, String> params = new HashMap<String, String>();
                    params.put("json", obj.toString());
                    System.out.println(obj.toString());
					  SocketHttpRequester.post(Constants.serverIp+":3000/user/pushinfo", params, fm);
					  back=SocketHttpRequester.request;
					  
					  
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println(e.toString());
				} 			
				System.out.println("infopush"+back);
	    	Message msg = new Message();
	        Bundle data = new Bundle();
	        data.putString("back",back);
	        msg.setData(data);
	        handler.sendMessage(msg);
	    	}
	 };
	
	
	Handler handler = new Handler(){
	    @Override
	    public void handleMessage(Message msg) {
	        super.handleMessage(msg);
	        
	        Bundle data = msg.getData();
	        if(!back.equals("fail")){
	        	creatAppDir();
				File saveFile = new File(Environment.getExternalStorageDirectory(), "E-homeLand/infopush/infopush.ss");
				if(!saveFile.exists())
					try {
						//saveFile.mkdirs();
						saveFile.createNewFile();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						System.out.println("create"+e1.toString());
					}
				
				try{ 
					
					//File saveFile = new File("E-homeland/infopush", "infopush.ss");
		        	FileOutputStream outStream = new FileOutputStream(saveFile,true);
		        	outStream.write(save.getBytes());
					outStream.close();
					

				    }catch(Exception e){ 

				          System.out.println("write"+e.toString()); 

				} 
				SharedPreferences mySharedPreferences = getSharedPreferences("user", Activity.MODE_PRIVATE); 
				SharedPreferences.Editor editor = mySharedPreferences.edit(); 
				int credit = mySharedPreferences.getInt("credit", 0);
				credit += 1;
				editor.putInt("credit",credit).commit();
				pb.setVisibility(View.GONE);
				Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
	            setContentView(R.layout.null_view);
				infoUploadActivity.this.finish();
				
	        }else{
	        	//Toast.makeText(getBaseContext(), "发送失败", Toast.LENGTH_SHORT).show();
	        }
	    }

	};
	
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
    
    public void creatAppDir()
	{
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			System.out.println("创建infopush文件夹");
			File f=new File(Environment.getExternalStorageDirectory(),"E-homeland");
			if(!f.exists())
	      	f.mkdirs();
	      	File ff=new File(Environment.getExternalStorageDirectory(),"E-homeland/infopush");
	      	if(!ff.exists())
	      	ff.mkdirs();
		}	
	}

    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK) {  
            setContentView(R.layout.null_view);
        	infoUploadActivity.this.finish();
        	
            return true;   
        } else  
            return super.onKeyDown(keyCode, event);  
    }
}

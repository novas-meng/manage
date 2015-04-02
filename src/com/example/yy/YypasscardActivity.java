package com.example.yy;

import java.io.File;
import java.io.IOException;

import com.example.yy.*;
import com.example.tools.ImageCompress;
import com.example.manage.*;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class YypasscardActivity extends Activity{
	EditText month,day;
	EditText pernum,carcard;
	ImageView gt,wxshare;
	EditText passcardphone,passcardpernum,passcardcarcard,passcardtime_month,passcardtime_day;
	String path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/E-homeland/yypasscard";
	String  WX_APP_ID="wx8b5e9af02f2d85b5";
	IWXAPI wxApi;  
	private static final int THUMB_SIZE = 150;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yuyuepasscard);
		File fd=new File(path);
		if(!fd.exists())
			fd.mkdir();
		
		month=(EditText)findViewById(R.id.yy_month);
		day=(EditText)findViewById(R.id.yy_day);
		pernum=(EditText)findViewById(R.id.yy_num);
		carcard=(EditText)findViewById(R.id.yy_carcard);
		gt=(ImageView)findViewById(R.id.creatpasscard);
		
		//实例化  
		wxApi = WXAPIFactory.createWXAPI(this, WX_APP_ID,true);  
		wxApi.registerApp(WX_APP_ID); 
		
		passcardpernum=(EditText)findViewById(R.id.yy_num_view);
		passcardcarcard=(EditText)findViewById(R.id.yy_carcard_view);
		passcardtime_month=(EditText)findViewById(R.id.yy_month_view);
		passcardtime_day=(EditText)findViewById(R.id.yy_day_view);
	    wxshare=(ImageView)findViewById(R.id.yy_share);
		gt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			
				passcardtime_month.setText(month.getText());
				passcardtime_day.setText(day.getText());
				
				
				passcardpernum.setText(pernum.getText());
				passcardcarcard.setText(carcard.getText());
				
				
				LinearLayout passcard=(LinearLayout)findViewById(R.id.yypasscard);
				passcard.setVisibility(View.GONE);
				Bitmap passcardbitmap= GenerateBitmap.convertViewToBitmap(passcard);
				try {
					ImageCompress.saveMyBitmap("passcard", passcardbitmap, path+"/");
					//passcard.setVisibility(View.INVISIBLE);
					
					ImageView v=(ImageView)findViewById(R.id.yy_img);
					
					v.setImageBitmap(passcardbitmap);
					
					v.setVisibility(View.VISIBLE);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		wxshare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				sendImg();
			}
		});
		}
	private void sendImg() {
	    String imagePath = path+"/"+"passcard.png";
	   System.out.println(imagePath);
		File file = new File(imagePath);
		if (!file.exists()) {
			
			Toast.makeText(getApplicationContext(), "图片不存在", Toast.LENGTH_SHORT).show();
			
		}
		else {
		WXImageObject imgObj = new WXImageObject();
		imgObj.setImagePath(imagePath);
		
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = imgObj;
		
		Bitmap bmp = BitmapFactory.decodeFile(imagePath);
		Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
		bmp.recycle();
		msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
		
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = "小区e家人预约通行证";
		req.message = msg;
		req.scene =  SendMessageToWX.Req.WXSceneTimeline ;
		wxApi.sendReq(req);
		System.out.println("在sendreq之后");
		}
		
	    }

}

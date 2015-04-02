package com.example.logIn;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import org.dom4j.Text;

import com.example.MyProfile.FindPasswordActivity;
import com.example.manage.*;
import com.example.tools.IP;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FindNewPassword extends Activity{
	String userid;
	String newpassword;
	 protected void onCreate(Bundle savedInstanceState) {  
	        // TODO Auto-generated method stub  
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.inputuserid);
	        final EditText input=(EditText)findViewById(R.id.input);
	        Button com=(Button)findViewById(R.id.confirm);
	        com.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO 自动生成的方法存根
					userid=input.getText().toString();
					new Thread(rm).start();
				}
			});
	        }
	 Handler handler = new Handler(){
		    @Override
		    public void handleMessage(Message msg) {
		       
		        if(msg.what==0)
		        { System.out.println(msg.obj.toString());
		            final String [] info=msg.obj.toString().split("##");
		            System.out.println(info[0]);
		            System.out.println(info[1]);
		            setContentView(R.layout.findpassword);
		            final TextView ques=(TextView)findViewById(R.id.safe_ques);
		            final EditText ans=(EditText)findViewById(R.id.safe_ans);
		            final Button safeconf=(Button)findViewById(R.id.confirm);
		            ques.setText(info[0]);
		            safeconf.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO 自动生成的方法存根
							if(ans.getText().toString().equals(info[1]))
							{
								TextView tv=(TextView)findViewById(R.id.hint);
								tv.setText("请设定新密码");
								ans.setText("");
								ques.setText("");
								safeconf.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View arg0) {
										// TODO 自动生成的方法存根
										if(ans.getText().toString().equals(""))
										{
											Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
											
										}
										else 
										{
											newpassword=ans.getText().toString();
											new Thread(rm1).start();
										}
									}
								});
							}
							else 
								Toast.makeText(getApplicationContext(), "回答错误", Toast.LENGTH_SHORT).show();
						}
					});
		    }
		        else if(msg.what==1)
		        {
		        	System.out.println("1"+msg.obj.toString());
		        	FindNewPassword.this.finish();
		        }
		        	
		    }
		    };
Runnable rm=new Runnable() {
	
	@Override
	public void run() {
		// TODO 自动生成的方法存根
		HashMap<String, String> params1 = new HashMap<String, String>();
		System.out.println(userid);
		  params1.put("userid", userid);
		  String ul=IP.ip+":3000/getsafequestion";
		   com.example.yy.SocketHttpRequester.postString(ul, params1);
		   String  rq=com.example.yy.SocketHttpRequester.strResult;
		   System.out.println(rq);
		   handler.obtainMessage(0, rq).sendToTarget();
	}
};
Runnable rm1=new Runnable() {
	
	@Override
	public void run() {
		// TODO 自动生成的方法存根
		HashMap<String, String> params1 = new HashMap<String, String>();
		System.out.println(userid);
		  params1.put("userid", userid);
		  params1.put("password", getMD5Str(userid+newpassword));
		  String ul=IP.ip+":3000/usernewpassword";
		   com.example.yy.SocketHttpRequester.postString(ul, params1);
		   String  rq=com.example.yy.SocketHttpRequester.strResult;
		   System.out.println(rq);
		   handler.obtainMessage(1, rq).sendToTarget();
	}
};
private String getMD5Str(String str) {  
    MessageDigest messageDigest = null;  
    try {  
        messageDigest = MessageDigest.getInstance("MD5");   
        messageDigest.reset();  
        messageDigest.update(str.getBytes("UTF-8"));  
    } catch (NoSuchAlgorithmException e) {  
        System.out.println("NoSuchAlgorithmException caught!");  
        System.exit(-1);  
    } catch (UnsupportedEncodingException e) {  
        e.printStackTrace();  
    }  

    byte[] byteArray = messageDigest.digest();  

    StringBuffer md5StrBuff = new StringBuffer();  

    for (int i = 0; i < byteArray.length; i++) {              
        if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)  
            md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));  
        else  
            md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));  
    }  

    return md5StrBuff.toString();  
}  
}

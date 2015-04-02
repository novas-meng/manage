package com.example.MyProfile;




import java.util.HashMap;

import com.example.Around.AroundAdditionActivity;
import com.example.logIn.Constants;
import com.example.manage.*;
import com.example.tools.IP;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FindPasswordActivity extends Activity{
	int ques;
	String answer;
	String[] question={"你父亲的名字是","你母亲的名字是","你的名字是","你的小学名字是","你的朋友的名字"};
	  protected void onCreate(Bundle savedInstanceState) {  
	        // TODO Auto-generated method stub  
	        super.onCreate(savedInstanceState);
	       
	      
	        	
	        	setContentView(R.layout.findpasswordquestion);
	        final	EditText qs1=(EditText)findViewById(R.id.answer1);
	        final	EditText qs2=(EditText)findViewById(R.id.answer2);
	        final	EditText qs3=(EditText)findViewById(R.id.answer3);
	        final	EditText qs4=(EditText)findViewById(R.id.answer4);
	        final	EditText qs5=(EditText)findViewById(R.id.answer5);
	        ImageView bw=(ImageView)findViewById(R.id.backarrow);
			bw.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO 自动生成的方法存根
					//startActivity(new Intent(getApplicationContext(),AroundActivity.class));
					FindPasswordActivity.this.finish();
				}
			});
	          ImageView conf=(ImageView)findViewById(R.id.confirm);
	          System.out.println("in 0");
	          conf.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(!qs1.getText().toString().equals(""))
		        	{
		        		ques=1;
		        		answer=qs1.getText().toString();
		        		new Thread(rm).start();
		        		
		        	}
		        	else if(!qs2.getText().toString().equals(""))
		        	{
		        		ques=2;
		        		answer=qs2.getText().toString();
		        		
		        		new Thread(rm).start();
		        		
		        	}
		        	else if(!qs3.getText().toString().equals(""))
		        	{
		        		ques=3;
		        		answer=qs3.getText().toString();
		        		
		        		new Thread(rm).start();
		        		
		        	}
		        	else if(!qs4.getText().toString().equals(""))
		        	{
		        		ques=4;
		        		answer=qs4.getText().toString();
		        		
		        		new Thread(rm).start();
		        		
		        	}
		        	else if(!qs5.getText().toString().equals(""))
		        	{
		        		ques=5;
		        		answer=qs5.getText().toString();
		        		
		        		new Thread(rm).start();
		        		
		        	}
		        	else 
		        		Toast.makeText(getApplicationContext(), "未回答任何问题", Toast.LENGTH_SHORT).show();
				}
			});
	        
	       
	        }
	  Handler handler = new Handler(){
		    @Override
		    public void handleMessage(Message msg) {
		       
		        
		            if(msg.obj.toString().equals("success"))
		            {
		            	System.out.println("---");
		            	FindPasswordActivity.this.finish();
		            }
		            else 
		            	Toast.makeText(getApplicationContext(), "发送失败，请重试", Toast.LENGTH_SHORT).show();
		    }
		    };
Runnable rm=new Runnable() {
	
	@Override
	public void run() {
		// TODO 自动生成的方法存根
		HashMap<String, String> params1 = new HashMap<String, String>();
		  params1.put("userid", Constants.username);
		  params1.put("question", question[ques-1]);
		  params1.put("answer", answer);
		  String ul=IP.ip+":3000/usersafequestion";
		   com.example.yy.SocketHttpRequester.postString(ul, params1);
		   String  rq=com.example.yy.SocketHttpRequester.strResult;
		   System.out.println(rq);
		   handler.obtainMessage(0, rq).sendToTarget();
	}
};
}

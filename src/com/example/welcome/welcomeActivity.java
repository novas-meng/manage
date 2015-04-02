package com.example.welcome;

import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import com.example.logIn.logInActivity;
import com.example.manage.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;

public class welcomeActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		ImageView image=(ImageView)findViewById(R.id.welcome);
		BitmapFactory.Options opt = new BitmapFactory.Options();

		opt.inPreferredConfig = Bitmap.Config.RGB_565;

		opt.inPurgeable = true;

		opt.inInputShareable = true;

		InputStream is = getResources().openRawResource(

				 R.drawable.welcome );

		final Bitmap bm = BitmapFactory.decodeStream(is, null, opt);

		BitmapDrawable bd = new BitmapDrawable(getResources(), bm);

		 //holder.iv.setBackgroundDrawable(bd);
		//bg.setBackgroundResource(R.drawable.comnews_newsbg);}
		image.setBackgroundDrawable(bd);
		Timer timer = new Timer();//timer中有一个线程,这个线程不断执行task
		//The TimerTask class represents a task to run at a specified time. The task may be run once or repeatedly.
		   TimerTask task = new TimerTask() { //timertask实现runnable接口,TimerTask类就代表一个在指定时间内执行的task
		   @Override
		   public void run() {
			   bm.recycle();
			    welcomeActivity.this.finish();
		    Intent intent = new Intent(welcomeActivity.this, logInActivity.class);
		    startActivity(intent);
		   }
		  };
		// Schedule a task for single execution after a specified delay.
		   timer.schedule(task, 1000 * 1);//设
	}
   
}

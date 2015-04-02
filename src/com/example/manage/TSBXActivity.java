package com.example.manage;

import com.example.utils.MyVoice;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class TSBXActivity extends Activity
{
	MyVoice voice=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tsbx);
		final Button b=(Button)findViewById(R.id.tsbx_button);
		
		b.setOnTouchListener(new OnTouchListener()
		{

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				// TODO Auto-generated method stub
				switch(event.getAction())
				{
                 case MotionEvent.ACTION_DOWN:
					System.out.println("手机按下");
					b.setBackgroundResource(R.drawable.mainmenu_xxfb);
					 voice=new MyVoice();
					voice.startRecording();
					break;
				case MotionEvent.ACTION_UP:
					System.out.println("抬起手指");
					b.setBackgroundResource(R.drawable.mainmenu_sqgg);
					voice.stopRecording();
					voice.startPlaying();
				   break;
				}
			
			return true;
			}
		});
		
	}
    
}

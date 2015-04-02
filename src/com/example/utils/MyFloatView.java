package com.example.utils;

import com.example.manage.MainMenu;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;

public class MyFloatView extends ImageView
{
	private float TouchX;
	private float TouchY;
    private float StartX;
    private float StartY;
    private float x;
    private float y;
    private OnClickListener clickListener;
    private WindowManager wm=(WindowManager)getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
	private WindowManager.LayoutParams wmParams=((MyApplication)getContext().getApplicationContext()).getLayoutParams();
    public MyFloatView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
	   x=event.getRawX();
	   y=event.getRawY();
	   switch(event.getAction())
	   {
	   case MotionEvent.ACTION_DOWN:
		   TouchX=event.getX();
		   TouchY=event.getY();
		   StartX=x;
		   StartY=y;
		   break;
	   case MotionEvent.ACTION_MOVE:
		   updateView();
		   break;
	   case MotionEvent.ACTION_UP:
		   updateView();
		   TouchX=TouchY=0;
		   if(Math.abs(x-StartX)<5&&Math.abs(y-StartY)<5)
		   {
			   if(clickListener!=null)
			   {
				   clickListener.onClick(this);
			   }
		   }
		   break;
	   }
	   return true;
	}
	
    @Override
	public void setOnClickListener(OnClickListener l) {
		// TODO Auto-generated method stub
		this.clickListener=l;
	}
	private void updateView()
    {
    	wmParams.x=(int)(MainMenu.width-x);
    	wmParams.y=(int)(y-TouchY);
    	//wmParams.y=(int)(y-1920);

    	wm.updateViewLayout(this,wmParams);
    }
}

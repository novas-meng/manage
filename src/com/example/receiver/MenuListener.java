package com.example.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

 public class MenuListener extends BroadcastReceiver
    {
    	 
    	    	public void onReceive(Context arg0, Intent arg1) {
    	    		// TODO Auto-generated method stub
    	    	
    	    			if(arg1.getAction().equals("XMLGet"))
    	    			{
    	    				System.out.println(arg1.getAction());
    	    				System.out.println("获取到了广播");
    	    			}
    	    				/*
    	    				intent = new Intent(MainMenu.this, TopicNews.class);
    	    		    	vNewsMain = getLocalActivityManager().startActivity(
    	    		    			"TopicNews", intent).getDecorView();
    	    		    	params = new LayoutParams(
    	    		    			LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    	    		    	rlNewsMain = (RelativeLayout) findViewById(R.id.layout_news_main);
    	    		    	rlNewsMain.addView(vNewsMain, params);
    	    		    	*/
    	    			//}
    	    		}	   
    }
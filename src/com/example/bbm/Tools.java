package com.example.bbm;

import android.app.Activity;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

public class Tools {
	public static void removeTitle(Activity a)
	{
		a.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	 public static void fullscreen(boolean enable,Activity a) {
	        if (enable) {
	            WindowManager.LayoutParams lp = a.getWindow().getAttributes();
	            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
	            a.getWindow().setAttributes(lp);
	            a.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	        } else {
	            WindowManager.LayoutParams attr = a.getWindow().getAttributes();
	            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
	            a.getWindow().setAttributes(attr);
	            a.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	        }
	 }
}

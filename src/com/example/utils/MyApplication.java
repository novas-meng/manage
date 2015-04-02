package com.example.utils;

import android.app.Application;
import android.view.WindowManager;

public class MyApplication extends Application
{
     private WindowManager.LayoutParams wmlp=new WindowManager.LayoutParams();
     public WindowManager.LayoutParams getLayoutParams()
     {
    	 return wmlp;
     }
}

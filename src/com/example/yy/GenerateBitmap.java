package com.example.yy;

import android.graphics.Bitmap;
import android.view.View;
import android.view.View.MeasureSpec;

public class GenerateBitmap {
	
	public static Bitmap convertViewToBitmap(View view){  
	       view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));  
	        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());  
	        view.buildDrawingCache();  
	        Bitmap bitmap = view.getDrawingCache();  
	       return bitmap;  
	}
}

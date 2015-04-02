package com.example.tools;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;  
import java.util.List;  
  

import android.content.Context;  
import android.graphics.Canvas;  
import android.graphics.Paint;  
import android.util.AttributeSet;  
import android.util.Log;  
import android.widget.TextView;  
  
/*
 * ��ֱ�Զ�������Ļ   
 *
 * */
public class VerticalScrollTextView extends TextView {  
  
	public final static String TAG = VerticalScrollTextView.class.getSimpleName();
        private float step =0f;     
        private Paint mPaint=new Paint(); ;   
        private String text;   
        private float width;
        private int textsize;
        private int start;
        private List<String> textList = new ArrayList<String>();    //���б���textview����ʾ��Ϣ��   
       
        public VerticalScrollTextView(Context context, AttributeSet attrs) {   
            super(context, attrs);           
        }   
           
       
        public VerticalScrollTextView(Context context) {   
            super(context);           
        }   
        public void getstart()
        {
        	start=1;
        }
        public void stop()
        {
        	start=0;
        }
        @Override   
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {           
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);   
            width = MeasureSpec.getSize(widthMeasureSpec);      
            final int widthMode = MeasureSpec.getMode(widthMeasureSpec);     
            if (widthMode != MeasureSpec.EXACTLY) {      
                throw new IllegalStateException("ScrollLayout only canmCurScreen run at EXACTLY mode!");   
            }
            
            float mwidth=getCharacterWidth(this);
            textsize=(int)((width/mwidth)-(width/mwidth)%1)-2;
            text=getText().toString();  
            if(text==null|text.length()==0){   
                     
                return ;   
            }         
               
           //����Ĵ����Ǹ��ݿ�Ⱥ������С��������textview��ʾ��������   
       
            textList.clear();   
            StringBuilder builder =null;  
            for(int i=0;i<text.length();i++){  
              
            	 if(i%textsize==0){  
                    builder = new StringBuilder("");   
                }  
            	 
                 if(i%textsize<=textsize-1){   
                     builder.append(text.charAt(i));   
                 }  
                 if(i%textsize==textsize-1){   
                     textList.add(builder.toString());   
                 }
                else if(i==text.length()-1)
                	 textList.add(builder.toString());
                 
                   
            }  
            Log.e("textviewscroll",""+textList.size());   
             
  
        }   
       
       
        //�����������������������ʾ�����������ֻ��ڻ����ϣ�ʵʱ���¡�   
         @Override   
        public void onDraw(Canvas canvas) {   
        	 
           if(textList.size()==0)  return;   
             
          mPaint.setTextSize(this.getTextSize());//���������С  
            for(int i = 0; i < textList.size(); i++) {   
                canvas.drawText(textList.get(i), 0, this.getHeight()+(i+1)*mPaint.getTextSize()-step+30, mPaint);   
                
            }   
            invalidate();      
              
            step = step+0.6f;   
            if (step >= this.getHeight()+textList.size()*mPaint.getTextSize()) {   
                step = 0;   
            }           
        }   
         public float getCharacterWidth(TextView tv){
     		if(null == tv)
     			return 0f;
     		return getCharacterWidth(tv.getText().toString(),tv.getTextSize()) * tv.getScaleX();
     	}
         public float getCharacterWidth(String text, float size){
     		if(null == text || "".equals(text))
     			return 0;
     		float width = 0;
     		Paint paint = new Paint();
     		paint.setTextSize(size);
     		float text_width = paint.measureText(text);//�õ����峤��		
     		width = text_width/text.length();//ÿһ���ַ��ĳ���
     		return width;
     	}
       
}  
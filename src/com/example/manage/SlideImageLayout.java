package com.example.manage;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

/**
 * ���ɻ���ͼƬ���򲼾�
 * @Description: ���ɻ���ͼƬ���򲼾�

 * @File: SlideImageLayout.java

 * @Package com.image.indicator.layout

 * @Author Hanyonglu

 * @Date 2012-6-18 ����09:04:14

 * @Version V1.0
 */
public class SlideImageLayout 
{
	// ����ͼƬ��ArrayList
	private ArrayList<ImageView> imageList = null;
	private Activity activity = null;
	// Բ��ͼƬ����
	private ImageView[] imageViews = null; 
	private ImageView imageView = null;
	private NewsXmlParser parser = null;
	
	// ��ʾ��ǰ����ͼƬ������
	private int pageIndex = 0;
	
	public SlideImageLayout(Activity activity) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
		imageList = new ArrayList<ImageView>();
		parser = new NewsXmlParser();
		
	}
	
	/**
	 * ���ɻ���ͼƬ���򲼾�
	 * @param index
	 * @return
	 */
	
	public View getSlideImageLayout(int index,String url){
		// ����TextView��LinearLayout
		LinearLayout imageLinerLayout = new LinearLayout(activity);
		LinearLayout.LayoutParams imageLinerLayoutParames = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, 
				LinearLayout.LayoutParams.FILL_PARENT, 
				1);
		ImageView iv = new ImageView(activity);
		//System.out.println("uri="+uri);
		//iv.setBackgroundResource(index);
		ImageUtils.setImageViewBitmap(iv,url);
		iv.setOnClickListener(new ImageOnClickListener());
		imageLinerLayout.addView(iv,imageLinerLayoutParames);
		imageList.add(iv);
		
		return imageLinerLayout;
	}
	
	/**
	 * ��ȡLinearLayout
	 * @param view
	 * @param width
	 * @param height
	 * @return
	 */
	public View getLinearLayout(View view,int width,int height){
		LinearLayout linerLayout = new LinearLayout(activity);
		LinearLayout.LayoutParams linerLayoutParames = new LinearLayout.LayoutParams(
				width, 
				height,
				1);
		// �������Ҳ�Զ������ã�����Ȥ���Լ����á�
		linerLayout.setPadding(10, 0, 10, 0);
		linerLayout.addView(view, linerLayoutParames);
		
		return linerLayout;
	}
	
	/**
	 * ����Բ�����
	 * @param size
	 */
	public void setCircleImageLayout(int size){
		imageViews = new ImageView[size];
	}
	
	/**
	 * ����Բ��ͼƬ���򲼾ֶ���
	 * @param index
	 * @return
	 */
	public ImageView getCircleImageLayout(int index){
		imageView = new ImageView(activity);  
		imageView.setLayoutParams(new LayoutParams(10,10));
        imageView.setScaleType(ScaleType.FIT_XY);
        
        imageViews[index] = imageView;
         
        if (index == 0) {  
            //Ĭ��ѡ�е�һ��ͼƬ
            imageViews[index].setBackgroundResource(R.drawable.dot_selected);  
        } else {  
            imageViews[index].setBackgroundResource(R.drawable.dot_none);  
        }  
         
        return imageViews[index];
	}
	
	/**
	 * ���õ�ǰ����ͼƬ������
	 * @param index
	 */
	public void setPageIndex(int index){
		pageIndex = index;
	}
	
	// ����ҳ�����¼�������
    private class ImageOnClickListener implements OnClickListener{
    	@Override
    	public void onClick(View v) {
    		// TODO Auto-generated method stub
    		
    		
    	}
    }

	
}

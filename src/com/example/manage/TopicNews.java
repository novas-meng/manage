package com.example.manage;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.example.receiver.MenuListener;
import com.example.tools.IP;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * 头条新闻Activity
 * @Description: 头条新闻Activity

 * @File: TopicNews.java

 * @Package com.image.indicator.activity

 * @Author Hanyonglu

 * @Date 2012-6-18 上午08:24:36

 * @Version V1.0
 */
public class TopicNews extends Activity{
	// 滑动图片的集合
	private ArrayList<View> imagePageViews = null;
	private ViewGroup main = null;
	private String baseurl=IP.ip+":3000/uploads/";
	private ViewPager viewPager = null;
	// 当前ViewPager索引
	private int pageIndex = 0; 
	int m=0;
	// 包含圆点图片的View
	private ViewGroup imageCircleView = null;
	private ImageView[] imageCircleViews = null; 
	
	// 滑动标题
	private TextView tvSlideTitle = null;
	
	// 布局设置类
	private SlideImageLayout slideLayout = null;
	// 数据解析类
	private NewsXmlParser parser = null; 
	int currentItem=0;
	private ScheduledExecutorService scheduledExecutorService;  
     private Handler handler = new Handler() {  
	        public void handleMessage(android.os.Message msg) {  
	           // viewPager.setCurrentItem(currentItem);// 切换当前显示的图片  
	           
	        };  
	    };  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Translucent_NoTitleBar);
		
		// 初始化
		initeViews();
		 IntentFilter filter=new IntentFilter();
	        filter.addAction("XMLGet");
	        this.registerReceiver(new topicReceiver(), filter);
	}
	private class topicReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			System.out.println("重新添加图片");
			if(arg1.getAction().equals("XMLGet"))
			{
				viewPager.setOnPageChangeListener(new ImagePageChangeListener());
		        System.out.println("设置标题2");

				LayoutInflater inflater=getLayoutInflater();
				ViewGroup main = (ViewGroup)inflater.inflate(R.layout.page_topic_news, null);
				ViewPager viewPager = (ViewPager) main.findViewById(R.id.image_slide_page); 
				imagePageViews.clear();
				String uri=arg1.getStringExtra("link");
				String con=arg1.getStringExtra("content");
				String [] content=con.split("&&");
				for(int i=0;i<content.length;i++)
				{System.out.println("content  "+content[i]);
				   parser.setSlideTitles(content);
				}
				
				String[] uris=uri.split("&&");
				for(int k=0;k<uris.length;k++)
				{
					uris[k]=uris[k];
				}
				for(int i=0;i<uris.length;i++)
				{
					
					imagePageViews.add(slideLayout.getSlideImageLayout(parser.getSlideImages()[i],uris[i]));
					
				}
		        System.out.println(parser.getSlideTitles()[0]+parser.getSlideTitles()[1]);
				
		        tvSlideTitle = (TextView) main.findViewById(R.id.tvSlideTitle);

				tvSlideTitle.setText(parser.getSlideTitles()[0]);
				setContentView(main);
				
				// 设置ViewPager
		        viewPager.setAdapter(new SlideImageAdapter());  
		        viewPager.setOnPageChangeListener(new ImagePageChangeListener());

			}
			
		}
		
	}
	/**
	 * 初始化
	 */
	private void initeViews(){
		// 滑动图片区域
		imagePageViews = new ArrayList<View>();
		LayoutInflater inflater = getLayoutInflater();  
		main = (ViewGroup)inflater.inflate(R.layout.page_topic_news, null);
		viewPager = (ViewPager) main.findViewById(R.id.image_slide_page);  
	
		// 圆点图片区域
		parser = new NewsXmlParser();
		int length = parser.getSlideImages().length;
		imageCircleViews = new ImageView[length];
		LayoutParams imagelayout=new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		//imageCircleView = (ViewGroup) main.findViewById(R.id.layout_circle_images);
		for(int i=0;i<length;i++)
		{
			//imageCircleViews[i].setLayoutParams(imagelayout);
		}
		slideLayout = new SlideImageLayout(TopicNews.this);
		slideLayout.setCircleImageLayout(length);
		
		for(int i = 0;i < length;i++){
			imagePageViews.add(slideLayout.getSlideImageLayout(parser.getSlideImages()[i],"http://image.tianjimedia.com/uploadImages/2014/268/25/PLBGYT70TWW5_680x500.jpg"));
			imageCircleViews[i] = slideLayout.getCircleImageLayout(i);
			//imageCircleView.addView(slideLayout.getLinearLayout(imageCircleViews[i], 10, 10));
		}
		tvSlideTitle = (TextView) main.findViewById(R.id.tvSlideTitle);

		// 设置默认的滑动标题
		tvSlideTitle.setText(parser.getSlideTitles()[2]);
		
        System.out.println("设置标题");
		setContentView(main);
		
		// 设置ViewPager
        viewPager.setAdapter(new SlideImageAdapter());  
       // viewPager.setOnPageChangeListener(new ImagePageChangeListener());
    	viewPager.setCurrentItem(0);
	}
	
	// 滑动图片数据适配器
    private class SlideImageAdapter extends PagerAdapter {  
        @Override  
        public int getCount() { 
            return imagePageViews.size();  
        }  
  
        @Override  
        public boolean isViewFromObject(View arg0, Object arg1) {  
            return arg0 == arg1;  
        }  
  
        @Override  
        public int getItemPosition(Object object) {  
            // TODO Auto-generated method stub  
            return super.getItemPosition(object);  
        }  
  
        @Override  
        public void destroyItem(View arg0, int arg1, Object arg2) {  
            // TODO Auto-generated method stub  
            ((ViewPager) arg0).removeView(imagePageViews.get(arg1));  
        }  
  
        @Override  
        public Object instantiateItem(View arg0, int arg1) {  
            // TODO Auto-generated method stub  
        	((ViewPager) arg0).addView(imagePageViews.get(arg1));
            
            return imagePageViews.get(arg1);  
        }  
  
        @Override  
        public void restoreState(Parcelable arg0, ClassLoader arg1) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public Parcelable saveState() {  
            // TODO Auto-generated method stub  
            return null;  
        }  
  
        @Override  
        public void startUpdate(View arg0) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public void finishUpdate(View arg0) {  
            // TODO Auto-generated method stub  
  
        }  
    }
    
    // 滑动页面更改事件监听器
    private class ImagePageChangeListener implements OnPageChangeListener {
        @Override  
        public void onPageScrollStateChanged(int arg0) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public void onPageScrolled(int arg0, float arg1, int arg2) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public void onPageSelected( int index) {  
        
        	
        	System.out.println("m="+m);
        	pageIndex = index;
            slideLayout.setPageIndex(index);
        //	pageIndex = m;
        	//slideLayout.setPageIndex(m);
        	tvSlideTitle.setText(parser.getSlideTitles()[index]);
        	
            for (int i = 0; i < imageCircleViews.length; i++) {  
            	imageCircleViews[index].setBackgroundResource(R.drawable.dot_selected);
                
                if (index != i) {  
                	imageCircleViews[i].setBackgroundResource(R.drawable.dot_none);  
                }  
            }
        }  
    }
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		 scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();  
	        // 当Activity显示出来后，每两秒钟切换一次图片显示  
	        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 30, TimeUnit.SECONDS); 
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		scheduledExecutorService.shutdown();  
		super.onStop();
	}
	private class ScrollTask implements Runnable {  
		  
        public void run() {  
            synchronized (viewPager) {  
                System.out.println("currentItem: " + currentItem);  
                currentItem = (currentItem + 1) % 3;  
                handler.obtainMessage().sendToTarget(); // 通过Handler切换图片  
            }  
        }  
  
    }  
}

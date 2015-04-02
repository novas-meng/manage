package com.example.communitynews;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

public class DownLoadImage {
	private String image_path;

	public DownLoadImage(String image_path) {
		// 保存图片的下载地址
		this.image_path = image_path;
	}

	public void loadImage(final ImageCallback callback) {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				// 接受到消息后，调用接口回调的方法
			
				callback.getBitmap((Bitmap) msg.obj);
			}
		};
		// 开启一个新线程用于访问图片数据
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// 下载图片为Drawable对象
					 BitmapFactory.Options options = new BitmapFactory.Options();  
	                    options.inJustDecodeBounds = true;  
	                    BitmapFactory.decodeStream(new URL(image_path).openStream(),null,options);  
	              
	                      
	                    int width=options.outWidth, height=options.outHeight;  
	                    int scale=1;  
	                    int temp=width>height?width:height;  
	                    while(true){  
	                        if(temp/2<120)  
	                            break;  
	                        temp=temp/2;  
	                        scale*=2;  
	                    }  
	                      
	                    BitmapFactory.Options opt = new BitmapFactory.Options();  
	                    opt.inSampleSize=scale;  
	                    Bitmap mbitmap= BitmapFactory.decodeStream(new URL(image_path).openStream(), null, opt);  
				//	 Bitmap
					// mbitmap =BitmapFactory.decodeStream(new URL(image_path).openConnection().getInputStream());
					 if(mbitmap.getWidth()>2000)
						{ Matrix matrix = new Matrix(); 
						  matrix.postScale(0.3f,0.3f); //长和宽放大缩小的比例
						  mbitmap = Bitmap.createBitmap(mbitmap,0,0,mbitmap.getWidth(),mbitmap.getHeight(),matrix,true);
						}
					// 把图片对象包装成一个消息发送给Handler
					Message message = Message.obtain();
					message.what = 1;
					message.obj = mbitmap;
					handler.sendMessage(message);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	// 定义一个公开的接口，用于执行回调操作
	public interface ImageCallback {
		public void getBitmap(Bitmap bm);
	}
}

package com.example.MyProfile;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import com.example.manage.R;
import com.example.tools.FormFile;
import com.example.tools.IP;
import com.example.tools.SocketHttpRequester;
import com.example.utils.Constants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MyInfoChangeActivity extends Activity{
	public static Bitmap bitmap;
	private String img_path = "";
	private EditText signature,userNameToShow,realnameToShow;
	private TextView adressToShow;
	private Button change,selectImage,takePhoto,onback;
	private ImageView face;
	private ProgressDialog MyDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.my_profile_change);
		signature = (EditText)findViewById(R.id.signatureChange);
		userNameToShow = (EditText)findViewById(R.id.userNameToShowChange);
		realnameToShow = (EditText)findViewById(R.id.realnameToShowChange);
		adressToShow = (TextView)findViewById(R.id.adressToShowChange);
		change = (Button)findViewById(R.id.changeUserInfoButtonUpload);
		selectImage = (Button)findViewById(R.id.myProfileChangeSelectImage);
		takePhoto = (Button)findViewById(R.id.myProfileChangeTakePhoto);
		face = (ImageView)findViewById(R.id.profileChangePicture);
		onback = (Button)findViewById(R.id.myinfochangeOnbackpressed);
		//数据初始化
		SharedPreferences mySharedPreferences = getSharedPreferences("user", Activity.MODE_PRIVATE);
		userNameToShow.setText(mySharedPreferences.getString("userName", ""));
		realnameToShow.setText(mySharedPreferences.getString("realName", ""));
		adressToShow.setText(mySharedPreferences.getString("address", ""));
		signature.setText(mySharedPreferences.getString("signature", ""));
		File myCaptureFile = new File(Environment.getExternalStorageDirectory(),"E-homeland/MyInfo/upload.jpg");
		if(myCaptureFile.exists()){
			img_path = myCaptureFile.getPath();
			bitmap = BitmapFactory.decodeFile(myCaptureFile.getPath());
			face.setImageBitmap(bitmap);
		}
		
		onback.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MyInfoChangeActivity.this.onBackPressed();
			}
			
		});
		
		selectImage.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();  
                // 开启Pictures画面Type设定为image 
                intent.setType("image/*");  
                 //使用Intent.ACTION_GET_CONTENT这个Action   
                intent.setAction(Intent.ACTION_GET_CONTENT);   
                // 取得相片后返回本画面  
                startActivityForResult(intent, 1);  
			}
			
		});
		takePhoto.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

				startActivityForResult(intent, 0);
			}
			
		});
		change.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MyDialog = ProgressDialog.show(MyInfoChangeActivity.this, " " , " 正在上传 ", true);
				new Thread(runnable).start();
			}
			
		});
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
		creatAppDir();
        if (resultCode == RESULT_OK&&requestCode == 1) {  
        	System.out.println("requestCode"+requestCode);
            Uri uri = data.getData(); 
            img_path = getPath(getApplicationContext(), uri);
            Log.e("uri", uri.toString());   
            bitmap = getimage(img_path);
			bitmap = compressImage(bitmap);  
			/* 将Bitmap设定到ImageView */  
			face.setImageBitmap(bitmap);
            
            	//String imagepath = "/sdcard/E-homeland/MyInfo/upload.jpg";
    			File myCaptureFile = new File(Environment.getExternalStorageDirectory(),"E-homeland/MyInfo/upload.jpg");
    		      if(!myCaptureFile.exists()){
    		    	  try {
    					myCaptureFile.createNewFile();
    				} catch (IOException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    		      }
    		      String imagepath = myCaptureFile.getPath();
    		      try
    		      {
    		        BufferedOutputStream bos = new BufferedOutputStream
    		        (new FileOutputStream(myCaptureFile));
    		        
    		        // 采用压缩转档方法 
    		        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
    		        
    		        // 调用flush()方法，更新BufferStream 
    		        bos.flush();
    		        
    		        // 结束OutputStream 
    		        bos.close(); 					       
    		      }catch (Exception e)
    		      { 
    		    	  System.out.println(e.toString());
    		      }
    		      img_path = imagepath;
            
        }
        else if (resultCode == RESULT_OK&&requestCode==0) {
        		System.out.println("存储图片");
    			String sdStatus = Environment.getExternalStorageState();
    			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
    				Log.i("TestFile",
    						"SD card is not avaiable/writeable right now.");
    				return;
    			}
    			String name = new DateFormat().format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA)) + ".jpg";	
    			//Toast.makeText(getBaseContext(), name, Toast.LENGTH_LONG).show();
    			Bundle bundle = data.getExtras();
    			bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
    			System.out.println(bitmap.getByteCount());
    			FileOutputStream b = null;
    			File file = new File(Environment.getExternalStorageDirectory(),"E-homeland/MyInfo/upload.jpg");
    			String fileName = file.getPath();
    			System.out.println(fileName);
    			img_path = fileName;
    			try {
    				b = new FileOutputStream(fileName);
    				bitmap.compress(Bitmap.CompressFormat.JPEG, 80, b);// 把数据写入文件
    			} catch (FileNotFoundException e) {
    				e.printStackTrace();
    			} finally {
    				try {
    					b.flush();
    					b.close();
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
    			}
            /* 将Bitmap设定到ImageView */  
            face.setImageBitmap(bitmap);  
        };
       // super.onActivityResult(requestCode, resultCode, data);  
    }  
	
	Runnable runnable = new Runnable(){
	    @Override
	    public void run() {
	        //
	        // TODO: http request.
	        //
	    	if(isWifiConnected()||isNetConnected()){
				
					final File uploadFile = new File(img_path);   
					System.out.println(img_path);
					SharedPreferences mySharedPreferences = getSharedPreferences("user", Activity.MODE_PRIVATE);
					String signatureTemp = " ";
					if(!signature.getText().toString().equals("")&&!signature.getText().toString().equals(null))
						signatureTemp = signature.getText().toString();
					String nameTemp = " ";
					if(!userNameToShow.getText().toString().equals("")&&!userNameToShow.getText().toString().equals(null))
						nameTemp = userNameToShow.getText().toString();
					String realnameTemp = " ";
					if(!realnameToShow.getText().toString().equals("")&&!realnameToShow.getText().toString().equals(null))
						realnameTemp = realnameToShow.getText().toString();
					String addressTemp = " ";
					if(!adressToShow.getText().toString().equals("")&&!adressToShow.getText().toString().equals(null))
						addressTemp = adressToShow.getText().toString();
		        	 Map<String, String> params = new HashMap<String, String>();   
		             params.put("_id",mySharedPreferences.getString("_id", ""));
		             params.put("signature",signatureTemp);
		             params.put("name",nameTemp);
		             System.out.println(nameTemp+mySharedPreferences.getString("password", ""));
		             params.put("password", getMD5Str(nameTemp+mySharedPreferences.getString("password", "")));
		             params.put("realname", realnameTemp);
		             params.put("address",addressTemp);
		             System.out.println(params.toString());
		             SharedPreferences.Editor editor = mySharedPreferences.edit(); 
		             editor.putString("signature", signatureTemp).commit();
		             editor.putString("name", nameTemp).commit();
		             editor.putString("realname", realnameTemp).commit();
		             editor.putString("address", addressTemp).commit();
		             editor.putString("imageUrl", "uploaded").commit();
		             
		             FormFile formfile = new FormFile("face.jpg", uploadFile, "profile", "image/jpeg"); 
		             try {
		            	 SocketHttpRequester.post(IP.ip+":3000/user/changeuserinfo", params, formfile);
		            	
		            		 String str=SocketHttpRequester.request;
		            		 if(!str.equals("fail")){
		            			 Message msg = new Message();
				     		        Bundle data = new Bundle();
				     		        data.putString("flag","发送成功");
				     		        msg.setData(data);
				     		        handlerException.sendMessage(msg);
		            		 }else{
		            			 Message msg = new Message();
				     		        Bundle data = new Bundle();
				     		        data.putString("flag","发送失败");
				     		        msg.setData(data);
				     		        handlerException.sendMessage(msg);
		            		 }
		            		
		            	 
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println(e.toString());
					}  
				
		        
	    	}else{
	    		Message msg = new Message();
		        Bundle data = new Bundle();
		        data.putString("flag","没有网络连接");
		        msg.setData(data);
		        handlerException.sendMessage(msg);
	    	}
	    	
	    }
	};
	
	Handler handlerException = new Handler(){
		@Override
	    public void handleMessage(Message msg) {
	        super.handleMessage(msg);
	        
	        Bundle data = msg.getData();
	        Toast.makeText(getBaseContext(), data.getString("flag"),Toast.LENGTH_SHORT).show();
	        if(data.getString("flag").equals("发送成功")){
	        	MyDialog.dismiss();
	        	Intent intent = new Intent();
	    		intent.putExtra("ifChange", true);
	    		setResult(RESULT_OK, intent);
	        	MyInfoChangeActivity.this.finish();
	        }
	    }
	};
	
	/** 
     * 检测网络是否连接 
     *  
     * @return 
     */ 
    private boolean isNetConnected() {  
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);  
        if (cm != null) {  
            NetworkInfo[] infos = cm.getAllNetworkInfo();  
            if (infos != null) {  
                for (NetworkInfo ni : infos) {  
                    if (ni.isConnected()) {  
                        return true;  
                    }  
                }  
            }  
        }  
        return false;  
    }  
   
    /** 
     * 检测wifi是否连接 
     *  
     * @return 
     */ 
    private boolean isWifiConnected() {  
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);  
        if (cm != null) {  
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();  
            if (networkInfo != null 
                    && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {  
                return true;  
            }  
        }  
        return false;  
    }  

    
    //图片压缩
    private Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while ( baos.toByteArray().length / 1024>32) {	//循环判断如果压缩后图片是否大于500kb,大于继续压缩		
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;//每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
		return bitmap;
	}
    //根据uri获取文件真实路径
    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     * 
     * @param context
     *            The context.
     * @param uri
     *            The Uri to query.
     * @author paulburke
     */
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     * 
     * @param context
     *            The context.
     * @param uri
     *            The Uri to query.
     * @param selection
     *            (Optional) Filter used in the query.
     * @param selectionArgs
     *            (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
            String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    //从文件读bitmap时压缩
    private Bitmap getimage(String srcPath) {  
        BitmapFactory.Options newOpts = new BitmapFactory.Options();  
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了  
        newOpts.inJustDecodeBounds = true;  
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空  
          
        newOpts.inJustDecodeBounds = false;  
        int w = newOpts.outWidth;  
        int h = newOpts.outHeight;  
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
        float hh = 800f;//这里设置高度为800f  
        float ww = 480f;//这里设置宽度为480f  
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
        int be = 1;//be=1表示不缩放  
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
            be = (int) (newOpts.outWidth / ww);  
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
            be = (int) (newOpts.outHeight / hh);  
        }  
        if (be <= 0)  
            be = 1;  
        newOpts.inSampleSize = be;//设置缩放比例  
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩  
    }
    
    public void creatAppDir()
	{
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			System.out.println("创建myinfo文件夹");
			File f=new File(Environment.getExternalStorageDirectory(),"E-homeland");
			if(!f.exists())
	      	f.mkdirs();
	      	File ff=new File(Environment.getExternalStorageDirectory(),"E-homeland/MyInfo");
	      	if(!ff.exists())
	      	ff.mkdirs();
		}	
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("ifChange", false);
		setResult(RESULT_OK, intent);
		super.onBackPressed();
	}
	/** 
     * MD5  
     */  
    private String getMD5Str(String str) {  
        MessageDigest messageDigest = null;  
        try {  
            messageDigest = MessageDigest.getInstance("MD5");   
            messageDigest.reset();  
            messageDigest.update(str.getBytes("UTF-8"));  
        } catch (NoSuchAlgorithmException e) {  
            System.out.println("NoSuchAlgorithmException caught!");  
            System.exit(-1);  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
 
        byte[] byteArray = messageDigest.digest();  
  
        StringBuffer md5StrBuff = new StringBuffer();  
  
        for (int i = 0; i < byteArray.length; i++) {              
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)  
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));  
            else  
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));  
        }  
  
        return md5StrBuff.toString();  
    }  
    
    @Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
    	
		super.onDestroy();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK) {  
        	
        	
        	MyInfoChangeActivity.this.finish();
        	
            return true;   
        } else  
            return super.onKeyDown(keyCode, event);  
    }
    
}

package com.example.tsbx;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.manage.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Bitmap.CompressFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class shootphoto extends Activity implements SurfaceHolder.Callback{


	  /* ����˽��Camera���� */
	public Bitmap bm ;
	  private Camera mCamera01;
	  private Button mButton01, mButton02, mButton03;
	  
	  /* ��Ϊreview����������Ƭ֮�� */
	  private ImageView mImageView01;
	  private TextView mTextView01;
	  private String TAG = "HIPPO";
	  private SurfaceView mSurfaceView01;
	  private SurfaceHolder mSurfaceHolder01;
	  //private int intScreenX, intScreenY;
	  
	  /* Ĭ�����Ԥ��ģʽΪfalse */
	  private boolean bIfPreview = false;
	  
	  /* ����������ͼƬ�洢�ڴ� */
	  private String strCaptureFilePath = "/sdcard/";
	  
	  /** Called when the activity is first created. */
	  @Override
	  public void onCreate(Bundle savedInstanceState)
	  {
	    super.onCreate(savedInstanceState);
	    
	    /* ʹӦ�ó���ȫ��Ļ���У���ʹ��title bar */
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.shoot_photo);
	    
	    /* �жϴ洢���Ƿ���� */
	    if(!checkSDCard())
	    {
	      /* ����Userδ��װ�洢�� */
	      mMakeTextToast
	      (
	        getResources().getText(0, "δ����sd��").toString(),
	        true
	      );
	    }
	    
	    /* ȡ����Ļ�������� */
	    DisplayMetrics dm = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(dm);
	    //intScreenX = dm.widthPixels;
	    //intScreenY = dm.heightPixels;
	    //Log.i(TAG, Integer.toString(intScreenX));
	        
	    mTextView01 = (TextView) findViewById(R.id.myTextView1);
	    mImageView01 = (ImageView) findViewById(R.id.myImageView1);
	    
	    /* ��SurfaceView��Ϊ���Preview֮�� */
	    mSurfaceView01 = (SurfaceView) findViewById(R.id.mSurfaceView1);
	    
	    /* ��SurfaceView��ȡ��SurfaceHolder���� */
	    mSurfaceHolder01 = mSurfaceView01.getHolder();
	    
	    /* Activity����ʵ��SurfaceHolder.Callback */
	    mSurfaceHolder01.addCallback(shootphoto.this);
	    
	    /* ���������Ԥ����С���ã��ڴ˲�ʹ�� */
	    //mSurfaceHolder01.setFixedSize(320, 240);
	    
	    /*
	     * ��SURFACE_TYPE_PUSH_BUFFERS(3)
	     * ��ΪSurfaceHolder��ʾ���� 
	     * */
	    mSurfaceHolder01.setType
	    (SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	    
	    mButton01 = (Button)findViewById(R.id.myButton1);
	    mButton02 = (Button)findViewById(R.id.myButton2);
	    mButton03 = (Button)findViewById(R.id.myButton3);
	    
	    /* �������Preview */
	    mButton01.setOnClickListener(new Button.OnClickListener()
	    {
	      

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			try {
				initCamera();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	    });
	    
	    /* ֹͣPreview����� */
	    mButton02.setOnClickListener(new Button.OnClickListener()
	    {
	      @Override
	      public void onClick(View arg0)
	      {
	        // TODO Auto-generated method stub
	        
	        /* �Զ���������������ر����Ԥ������ */
	        resetCamera();
	      }
	    });
	    
	    /* ���� */
	    mButton03.setOnClickListener(new Button.OnClickListener()
	    {
	      @Override
	      public void onClick(View arg0)
	      {
	        // TODO Auto-generated method stub
	        
	        /* ���洢�����ڲ��������գ��洢�ݴ�ͼ���ļ� */
	        if(checkSDCard())
	        {
	          /* �Զ������պ��� */
	          takePicture();
	        }
	        else 
	        {
	          /* �洢����������ʾ��ʾ */
	          mTextView01.setText
	          (
	            getResources().getText(0, "û��sd��").toString()
	          );
	        }
	      }
	    });
	  }
	  
	  /* �Զ����ʼ������� */
	  @SuppressLint("NewApi")
	private void initCamera() throws IOException
	  {
	    if(!bIfPreview)
	    {
	      /* ���������Ԥ��ģʽ�������� */
	      mCamera01 = Camera.open();
	    }
	    
	    if (mCamera01 != null && !bIfPreview)
	    {
	      Log.i(TAG, "inside the camera");
	      
	      /* ����Camera.Parameters���� */
	      Camera.Parameters parameters = mCamera01.getParameters();
	      
	      /* ������Ƭ��ʽΪJPEG */
	      parameters.setPictureFormat(PixelFormat.JPEG);
	      
	      /* ָ��preview����Ļ��С */
	      parameters.setPreviewSize(320, 240);
	      
	      /* ����ͼƬ�ֱ��ʴ�С */
	      parameters.setPictureSize(320, 240);
	      
	      /* ��Camera.Parameters������Camera */
	      mCamera01.setParameters(parameters);
	      
	      /* setPreviewDisplayΨһ�Ĳ���ΪSurfaceHolder */
	      mCamera01.setPreviewDisplay(mSurfaceHolder01);
	      
	      /* ��������Preview */
	      mCamera01.startPreview();
	      bIfPreview = true;
	    }
	  }
	  
	  /* ����ߢȡͼ�� */ 
	  private void takePicture()
	  {
	    if (mCamera01 != null && bIfPreview) 
	    {
	      /* ����takePicture()�������� */
	      mCamera01.takePicture
	      (shutterCallback, rawCallback,jpegCallback);
	    }
	  }
	  
	  /* ������� */
	  private void resetCamera()
	  {
	    if (mCamera01 != null && bIfPreview)
	    {
	      mCamera01.stopPreview();
	      /* ��չѧϰ���ͷ�Camera���� */
	      mCamera01.release();
	      mCamera01 = null;
	      bIfPreview = false;
	    }
	  }
	   
	  private ShutterCallback shutterCallback = new ShutterCallback()
	  { 
	    public void onShutter() 
	    { 
	      // Shutter has closed 
	    } 
	  }; 
	   
	  private PictureCallback rawCallback = new PictureCallback() 
	  { 
	    public void onPictureTaken(byte[] _data, Camera _camera) 
	    { 
	      // TODO Handle RAW image data 
	    } 
	  }; 

	  private PictureCallback jpegCallback = new PictureCallback() 
	  {
	    public void onPictureTaken(byte[] _data, Camera _camera)
	    {
	      // TODO Handle JPEG image data
	      
	      /* onPictureTaken����ĵ�һ��������Ϊ��Ƭ��byte */
	      bm = BitmapFactory.decodeByteArray
	                  (_data, 0, _data.length);
	      //pet.bitmap = bm;
	      /* �������ļ� */
	      Time t=new Time(); // or Time t=new Time("GMT+8"); ����Time Zone���ϡ�  
    	  t.setToNow(); // ȡ��ϵͳʱ�䡣  
    	  strCaptureFilePath += t.year;
    	  strCaptureFilePath += t.month;
    	  strCaptureFilePath += t.monthDay+"_";
    	  strCaptureFilePath += t.hour;
    	  strCaptureFilePath += t.minute;
    	  strCaptureFilePath += t.second + ".jpg";
    	  
	      File myCaptureFile = new File(strCaptureFilePath);
	      if(!myCaptureFile.exists()){
	    	  try {
				myCaptureFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      }
	      try
	      {
	    	  
	    	  
	        BufferedOutputStream bos = new BufferedOutputStream
	        (new FileOutputStream(myCaptureFile));
	        
	        /* ����ѹ��ת������ */
	        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
	        
	        /* ����flush()����������BufferStream */
	        bos.flush();
	        
	        /* ����OutputStream */
	        bos.close();
	        
	        /* �����������Ҵ洢��ϵ�ͼ�ļ�����ʾ���� */ 
	        mImageView01.setImageBitmap(bm);
	        
	        	        
	        /* ��ʾ��ͼ�ļ�������������������ر�Ԥ�� */
	        resetCamera();
	        
	        /* �����������������Ԥ�� */
	        initCamera();
	      }
	      catch (Exception e)
	      {
	        Log.e(TAG, e.getMessage());
	      }
	    }
	  };
	  
	  /* �Զ���ɾ���ļ����� */
	  private void delFile(String strFileName)
	  {
	    try
	    {
	      File myFile = new File(strFileName);
	      if(myFile.exists())
	      {
	        myFile.delete();
	    	 // pet.bitmap = BitmapFactory.decodeFile(strFileName);
	      }
	    }
	    catch (Exception e)
	    {
	      Log.e(TAG, e.toString());
	      e.printStackTrace();
	    }
	  }
	  
	  public void mMakeTextToast(String str, boolean isLong)
	  {
	    if(isLong==true)
	    {
	      Toast.makeText(shootphoto.this, str, Toast.LENGTH_LONG).show();
	    }
	    else
	    {
	      Toast.makeText(shootphoto.this, str, Toast.LENGTH_SHORT).show();
	    }
	  }
	  
	  private boolean checkSDCard()
	  {
	    /* �жϴ洢���Ƿ���� */
	    if(android.os.Environment.getExternalStorageState().equals
	    (android.os.Environment.MEDIA_MOUNTED))
	    {
	      return true;
	    }
	    else
	    {
	      return false;
	    }
	  }
	  
	  @Override
	  public void surfaceChanged
	  (SurfaceHolder surfaceholder, int format, int w, int h)
	  {
	    // TODO Auto-generated method stub
	    Log.i(TAG, "Surface Changed");
	  }
	  
	  @Override
	  public void surfaceCreated(SurfaceHolder surfaceholder)
	  {
	    // TODO Auto-generated method stub
	    Log.i(TAG, "Surface Changed");
	  }
	  
	  @Override
	  public void surfaceDestroyed(SurfaceHolder surfaceholder)
	  {
	    // TODO Auto-generated method stub
	    /* ��Surface�����ڣ���Ҫɾ��ͼƬ */
	    try
	    {
	      //delFile(strCaptureFilePath);
	    }
	    catch(Exception e)
	    {
	      e.printStackTrace();
	    }
	    Log.i(TAG, "Surface Destroyed");
	  }

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		this.setResult(0);
		//pet.img_path = strCaptureFilePath;
	}
	
	  
}

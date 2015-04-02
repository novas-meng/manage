package com.example.tsbx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;

public class MyVoice {
	 private MediaRecorder mRecorder=null;
	 private String mFileName=null;
	 MediaPlayer  mPlayer ;
	    public String getAudioPath()
	    {
	    	return mFileName;
	    }
       public MyVoice(String filename)
       {
		   mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
	       mFileName += "/E-homeland/audio/"+filename+".3gp";
	       mRecorder = new MediaRecorder();
           mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
         //  mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
           mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
           mRecorder.setOutputFile(mFileName);
          mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

           try {
               mRecorder.prepare();
           } catch(IOException e){
        	   e.printStackTrace();
               System.out.println("start prepare() failed");
           }

       }
       public byte[] getAudioBytes(String uri)
       {
    	   mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
	       mFileName += "/E-homeland/audio/"+uri+".3gp";
    	   byte[] res=null;
    	   byte[] result=null;
    	   File f=new File(mFileName);
    	   try {
			InputStream is=new FileInputStream(f);
			res=new byte[is.available()];
			
			is.read(res);
			result=new byte[res.length+2];
			result[0]=1;
			result[1]=1;
			System.arraycopy(res,0,result,2,res.length);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	   return result;
       }
       public void startPlaying(String filename)
       {
    	   //Uri uri=Uri.parse(filename);
    	  // MediaPlayer  mPlayer= MediaPlayer.create(act,uri);
    	   mPlayer= new MediaPlayer();
      	    mPlayer.setOnCompletionListener(new OnCompletionListener()
      	    {

   				@Override
   				public void onCompletion(MediaPlayer arg0) {
   					// TODO Auto-generated method stub
   					
   				}
      	    });  
      	  try {
       	  mPlayer.setDataSource(filename);
             mPlayer.prepare();
          
       } catch(IOException e){
           System.out.println("prepare() failed");
           e.printStackTrace();
       }
    	   mPlayer.start(); 	    
       }   
       public  void stopRecording()
       {
    	   if(mRecorder!=null)
    	   {
    		   mRecorder.stop();
               mRecorder.release();
               mRecorder = null;
    	   }       	    
       }
       @SuppressWarnings("deprecation")
	public void startRecording()
       {
    	   
           mRecorder.start();
       }
}

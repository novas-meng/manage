package com.example.utils;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;

public class MyVoice {
	 private MediaRecorder mRecorder=null;
	 private String mFileName=null;
	    private MediaPlayer   mPlayer=null;
       public MyVoice()
       {
		   mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
	        mFileName += "/E-homeland/audio/audiorecordtest.3gp";
       }
       public void startPlaying()
       {
    	  
    	        mPlayer = new MediaPlayer();
    	        try {
    	            mPlayer.setDataSource(mFileName);
    	            mPlayer.prepare();
    	            mPlayer.start();
    	        } catch(IOException e){
    	            System.out.println("prepare() failed");
    	        }
    	    
       }
       public  void stopRecording(){
           mRecorder.stop();
           mRecorder.release();
           mRecorder = null;
       }
       public void startRecording()
       {
    	   mRecorder = new MediaRecorder();
           mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
           mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
           mRecorder.setOutputFile(mFileName);
           mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

           try {
               mRecorder.prepare();
           } catch(IOException e){
               System.out.println("prepare() failed");
           }

           mRecorder.start();
       }
}

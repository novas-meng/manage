package com.example.tsbx;

import android.text.SpannableString;

public class ChatMsgEntity {
     private String name;
     private String date;
     private SpannableString text;
     //·¢ËÍ×´Ì¬
     private boolean status;
     private String audioPath;
     private boolean isComMsg=true;
     private boolean isVoice=false;
     private String imagePath;
     private boolean hasImage=false;
     public void setHasImage(Boolean hasImage)
     {
    	 this.hasImage=hasImage;
     }
     public boolean getHasImage()
     {
    	 return hasImage;
     }
     public void setImagePath(String imagePath)
     {
    	 this.imagePath=imagePath;
     }
     public String getImagePath()
     {
    	 return imagePath;
     }
     public void setSendStatus(boolean status)
     {
    	 this.status=status;
     }
     public boolean getSendStatus()
     {
    	 return status;
     }
     public void setAudioPath(String audioPath)
     {
    	 this.audioPath=audioPath;
     }
     public String getAudioPath()
     {
    	 return audioPath;
     }
     public void setIsVoice(boolean isVoice)
     {
    	 this.isVoice=isVoice;
     }
     public boolean getIsVoice()
     {
    	 return isVoice;
     }
     public  String getName()
     {
    	 return name;
     }
     public  void  setName(String name)
     {
    	 this.name=name;
     }
     public String getDate()
     {
    	 return date;
     }
     public void  setDate(String date)
     {
    	 this.date=date;
     }
     public SpannableString getText()
     {
    	 return text;
     }
     public void setText(SpannableString ss)
     {
    	 this.text=ss;
     }
     public boolean getMsgType()
     {
    	 return isComMsg;
     }
     public void setMsgType(boolean isComMsg)
     {
    	 this.isComMsg=isComMsg;
     }
     public ChatMsgEntity(String db_id, String name, String date, SpannableString text,
 			boolean status, boolean isComMsg) {
 		super();
 		this.name = name;
 		this.date = date;
 		this.text = text;
 		this.status = status;
 		this.isComMsg = isComMsg;
 	}
	public ChatMsgEntity() {
		// TODO Auto-generated constructor stub
	}
}

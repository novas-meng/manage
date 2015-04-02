package com.example.bbm;

public class bbmInformationEntity {
	private String content;
	private String phoneNumber;
	private boolean isVoice=false;
	private String voicePath;
	private int voiceTime;
    public bbmInformationEntity()
    {
    	
    }
    public void setContent(String content)
    {
    	this.content=content;
    }
    public String getContent()
    {
    	return content;
    }
    public void setPhoneNumber(String phoneNumber)
    {
    	this.phoneNumber=phoneNumber;
    }
    public String getPhoneNumber()
    {
    	return phoneNumber;
    }
    public void setIsVoice(boolean isVoice)
    {
    	this.isVoice=isVoice;
    }
    public boolean getIsVoice()
    {
    	return isVoice;
    }
    public void setVoicePath(String voicePath)
    {
    	this.voicePath=voicePath;
    }
    public String getVoicePath()
    {
    	return voicePath;
    }
    public void setVoiceTime(int voiceTime)
    {
    	this.voiceTime=voiceTime;
    }
    public int getVoiceTime()
    {
    	return voiceTime;
    }
}

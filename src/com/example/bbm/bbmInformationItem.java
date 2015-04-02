package com.example.bbm;

public class bbmInformationItem {
     private int id;
     private String content;
     private String phoneNumber;
     public bbmInformationItem()
     {
    	 
     }
     public void setId(int id)
     {
    	 this.id=id;
     }
     public int getId()
     {
    	 return id;
     }
     public void setContent(String content)
     {
    	 this.content=content;
     }
     public String getContent()
     {
    	return content;
     }
     public void SetPhoneNumber(String phoneNumber)
     {
    	 this.phoneNumber=phoneNumber;
     }
     public String getPhoneNumber()
     {
    	 return phoneNumber;
     }
}

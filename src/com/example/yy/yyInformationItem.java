package com.example.yy;


public class yyInformationItem {
     private String name;
     private String basic_text1;
     private String basic_text2;
     private int type;//0表示管家主动发来的预约信息，1表示业主向管家发出的预约信息，2表示管家回复业主预约成功的消息
     private String id;
     public void setId(String id)
     {
    	 this.id=id;
     }
     public String getId()
     {
    	 return id; 
     }
     public void setType(int type)
     {
    	 this.type=type;
     }
     public int getType()
     {
    	 return type;
     }
     public  void setName(String name)
     {
    	 this.name=name;
     }
     public String getName()
     {
    	 return name;
     }
     public void  setBaseInformation1(String basic_text1)
     {
    	 this.basic_text1=basic_text1;
     }
     public String getBaseInformation1()
     {
    	 return this.basic_text1;
     }
     public void setBaseInformation2(String basic_text2)
     {
    	 this.basic_text2=basic_text2;
     }
     public String getBaseInformation2()
     {
    	 return this.basic_text2;
     }
}

package com.example.yy;


public class yyInformationItem {
     private String name;
     private String basic_text1;
     private String basic_text2;
     private int type;//0��ʾ�ܼ�����������ԤԼ��Ϣ��1��ʾҵ����ܼҷ�����ԤԼ��Ϣ��2��ʾ�ܼһظ�ҵ��ԤԼ�ɹ�����Ϣ
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

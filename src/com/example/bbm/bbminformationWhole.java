package com.example.bbm;

import java.util.ArrayList;

public class bbminformationWhole {
ArrayList<bbmInformationItem> list=null;
   public bbminformationWhole()
   {
	   list=new ArrayList<bbmInformationItem>();
   }
   public void addInformationItem(bbmInformationItem item)
   {
	   System.out.println("��Ӱ��æ��Ϣ");
	   list.add(item);
   }
   public int getInformationSize()
   {
	   return list.size();
   }
   public ArrayList<bbmInformationItem> getInformationList()
   {
	   return list;
   }
}

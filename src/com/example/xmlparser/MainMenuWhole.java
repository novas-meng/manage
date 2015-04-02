package com.example.xmlparser;

import java.util.ArrayList;

public class MainMenuWhole {
    ArrayList<MainMenuItem> list=null;
    public MainMenuWhole()
    {
    	list=new ArrayList<MainMenuItem>();
    }
    public void addItem(MainMenuItem item)
    {
    	//System.out.println("Ìí¼ÓÎïÆ·");
    	list.add(item);
    for(int i=0;i<list.size();i++)
    	System.out.println(list.get(i).getLink());
    	
    }
    public int getItemCount()
    {
    	return list.size();
    }
    public ArrayList<MainMenuItem> getItemList()
    {
    	for(int i=0;i<list.size();i++)
    		System.out.println(list.get(i).getLink());
    	return list;
    }
}

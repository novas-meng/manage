package com.example.xmlparser;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.view.MenuItem;

public class MainMenuXml extends DefaultHandler
{
    final int LINK=1;
    final int CONTENT=2;
    final int COUNT=3;
    final int NEWS=4;
    final int DATE=5;
    int currentState=0;
    MainMenuWhole whole;
    MainMenuItem item;
  
    public MainMenuWhole getMainMenuWhole()
    {
    	//System.out.println("取得整个数据的时候");
    	 ArrayList<MainMenuItem> a =whole.list;
    	 for(int i=0;i<a.size();i++)
    	 System.out.println(a.get(i).getLink());
    	return whole;
    }
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
	String res=new String(ch,start,length);
	switch(currentState)
	{
	case LINK:
		item.setLink(res);
		currentState=0;
		break;
	case CONTENT:
		item.setContent(res);
		currentState=0;
		break;
	case COUNT:
		item.setCount(Integer.valueOf(res));
		currentState=0;
		break;
	case NEWS:
		item.setNews(res);
		currentState=0;
		break;
	case DATE:
		item.setDate(res);
		currentState=0;
		break;
		default:
			return;
	}
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		if(localName.equals("Image"))
		{
			//System.out.println("添加节点");
		//	System.out.println("item"+item.getLink());
			whole.addItem(item);
			item=new MainMenuItem();
			return;
		}
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		//System.out.println("进行初始化");
	 whole=new MainMenuWhole();
	 item=new MainMenuItem();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		if(localName.equals("HomePage"))
		{
			currentState=0;
			return;
		}
		if(localName.equals("Image"))
		{
			MainMenuItem item=new MainMenuItem();
			return;
		}
		if(localName.equals("link"))
		{
			currentState=LINK;
			return;
		}
		if(localName.equals("content"))
		{
			currentState=CONTENT;
			return;
		}
		if(localName.equals("count"))
		{
			currentState=COUNT;
			return;
		}
		if(localName.equals("news"))
		{
			currentState=NEWS;
			return;
		}
		if(localName.equals("date"))
		{
			currentState=DATE;
			return;
		}
		currentState=0;
	}
   
}

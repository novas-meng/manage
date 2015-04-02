package com.example.bbm;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class bbmInformationXmlParser extends DefaultHandler
{

	final int ID=1;
	final int CONTENT=2;
	final int PHONENUMBER=3;
	int currentState=0;
	bbminformationWhole whole;
	bbmInformationItem item;
	public bbminformationWhole getInformationWhole()
	{
		return whole;
	}
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		String res=new String(ch,start,length);
		switch(currentState)
		{
		case ID:
			item.setId(Integer.valueOf(res));
			currentState=0;
			break;
		case CONTENT:
			item.setContent(res);
			currentState=0;
			break;
		case PHONENUMBER:
			item.SetPhoneNumber(res);
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
		if(localName.equals("information"))
		{
			//System.out.println("添加新的信息");
			whole.addInformationItem(item);
			item=new bbmInformationItem();
		}
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		System.out.println("进行解析xml的初始化");
		whole=new bbminformationWhole();
		item=new bbmInformationItem();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		if(localName.equals("bbmInformation"))
		{
		//	System.out.println("读到信息");
			currentState=0;
			return ;
		}
		if(localName.equals("information"))
		{
			bbmInformationItem item=new bbmInformationItem();
			//System.out.println("生成新的帮帮忙");
			return ;
		}
		if(localName.equals("id"))
		{
			currentState=ID;
			return ;
		}
		if(localName.equals("content"))
		{
			currentState=CONTENT;
			return ;
		}
		if(localName.equals("phonenumber"))
		{
			currentState=PHONENUMBER;
			return ;
		}
		currentState=0;
	}
   
}

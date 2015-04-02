package com.example.xmlparser;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.Activity;

public class MainMenuXmlParser {
    Activity activity;
    InputSource is=null;
    public MainMenuXmlParser(Activity activity)
    {
    	this.activity=activity;
    }
    public void  setXmlByInputStream(InputStream stream) throws Exception
    {
    	is=new InputSource(stream);
    //	byte[] b=new byte[stream.available()];
    	//stream.read(b);
    	//System.out.println(new String(b,0,b.length));
    }
	public MainMenuWhole getMainMenuWhole() throws ParserConfigurationException, SAXException, IOException
	{
		SAXParserFactory factory=SAXParserFactory.newInstance();
		SAXParser parser=factory.newSAXParser();
		XMLReader reader=parser.getXMLReader();
		MainMenuXml handler=new MainMenuXml();
		reader.setContentHandler(handler);
		// InputSource is = new InputSource(activity.getClassLoader().getResourceAsStream("rss.xml"));//取得本地xml文件
         //InputSource l=new InputSource();
		
		 reader.parse(is);
         return handler.getMainMenuWhole();
	}
}

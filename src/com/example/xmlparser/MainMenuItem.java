package com.example.xmlparser;

public class MainMenuItem {
   private String link;
   private String content;
   private int count;
   private String news;
   private String date;
   public MainMenuItem()
   {
	   
   }
   public void setDate(String date)
   {
	   this.date=date;
	   
   }
   public String getDate()
   {
	   return date;
   }
   public void setLink(String link)
   {
	   this.link=link;
   }
   public String getLink()
   {
	   return link;
   }
   public void setContent(String content)
   {
	   this.content=content;
   }
   public String getContent()
   {
	   return content;
   }
   public void setCount(int count)
   {
	   this.count=count;
   }
   public int getCount()
   {
	   return count;
   }
   public void setNews(String news)
   {
	   this.news=news;
   }
   public String getNews()
   {
	   return news;
   }
}

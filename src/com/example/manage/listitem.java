package com.example.manage;

public class listitem {
    private int picture;
    private String title;
    private String content;
    public listitem(int picture,String title,String content)
    {
    	this.picture=picture;
    	this.title=title;
    	this.content=content;
    }
    public listitem() {
		// TODO Auto-generated constructor stub
	}
	public void setTitle(String title)
    {
    	this.title=title;
    }
    public String getTitle()
    {
    	return title;
    }
    public void setContent(String content)
    {
    	this.content=content;
    }
    public String getContent()
    {
    	return content;
    }
    public void setPic(int picture)
    {
    	this.picture=picture;
    }
    public int getPic()
    {
    	return picture;
    }
}

package com.example.tsbx;

public class ChatEmoji {
	//图片的id
  private int id;
  //图片的文字描述
  private String character;
  //图片的文件名
  private String faceName;
  //图片的文件夹名称

  private String entryName;
  public void setEntryName(String entryName)
  {
	  this.entryName=entryName;
  }
  public String getEntryName()
  {
	  return entryName;
  }
  public int getId()
  {
	  return id;
  }
  public void setId(int id)
  {
	  this.id=id;
  }
  public String getCharacter()
  {
	  return character;
  }
  public void setCharacter(String character)
  {
	  this.character=character;
  }
  public String getfaceName()
  {
	  return faceName;
  }
  public void setFaceName(String faceName)
  {
	  this.faceName=faceName;
  }
}

package com.example.tsbx;

public class ChatEmoji {
	//ͼƬ��id
  private int id;
  //ͼƬ����������
  private String character;
  //ͼƬ���ļ���
  private String faceName;
  //ͼƬ���ļ�������

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

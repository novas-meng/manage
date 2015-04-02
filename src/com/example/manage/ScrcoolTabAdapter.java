package com.example.manage;


public class ScrcoolTabAdapter {  
      
    private int resource;  
  
    private String[] data;  
  
    public int getResource() {  
        return resource;  
    }  
  
    public void setResource(int resource) {  
        this.resource = resource;  
    }  
  
    public String[] getData() {  
        return data;  
    }  
  
    public void setData(String[] data) {  
        this.data = data;  
    }  
  
    public ScrcoolTabAdapter(int resource, String[] data) {  
        super();  
        this.resource = resource;  
        this.data = data;  
    }  
}  
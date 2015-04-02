package com.example.tools;
import java.util.ArrayList;
import java.util.List;

import com.example.manage.R;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ImageAdapter extends BaseAdapter{

    private Context ctx;
    
    private  List<Bitmap> bitmapList1 ;
    
    public LayoutInflater inflater;
    Resources res;
    public static ArrayList<Boolean> iscertificate=new  ArrayList<Boolean>();
    public static ArrayList<Integer> ClickList=new ArrayList<Integer>();
    /**
     * ��¼ѡ�����Ƭ���
     * @param position
     */
    public void add(int position,Boolean is){
    	int j=-1;
    	for (int i = 0; i < ClickList.size(); i++) {
			if(ClickList.get(i)==position){
				j=i;
				break;
			}
		}
    	if(j<0){
    		ClickList.add(position);
    		iscertificate.add(is);
    	}else{
    		ClickList.remove(j);
    		iscertificate.remove(j);
    	}
    }
    public ArrayList<Integer> getClickList() {
		return ClickList;
	}
    public ArrayList<Boolean> getiscertificate() {
		return iscertificate;
	}
	// ���캯��
    public ImageAdapter(Context context)
    {
        this.ctx = context;
        inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        res = ctx.getResources();
    }
    
  

	public void setImgMap(List<Bitmap> bitmapList1)
    {
        this.bitmapList1 = bitmapList1;
    }

   

  

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return bitmapList1.size();
    }

    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {	
        	convertView = inflater.inflate(R.layout.adapter_grid_item, null);
        }
        // ��ImageView������Դ
        ImageView imageView = (ImageView) convertView.findViewById(R.id.AdapterGridItemTwoImage);
        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.item_layout);
        // ���ò���ͼƬ120*120��ʾ
        //imageView.setLayoutParams(new GridView.LayoutParams(125, 125));
        
        // ��������ʾ��������
        //imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        

        //����
        if (bitmapList1 != null)
        {
            if (bitmapList1!= null && position < bitmapList1.size())
            {
            	int clickTemp=-1;
            	for (int i = 0; i < ClickList.size(); i++) {
        			if(ClickList.get(i)==position){
        				clickTemp=position;
        				break;
        			}
        		}
                if(clickTemp>=0){
                	//Log.d("TAG", "clickTemp"+clickTemp);
                	BitmapDrawable bd=new BitmapDrawable(bitmapList1.get(position));
                	layout.setBackgroundDrawable(bd);
                	imageView.setImageBitmap(null);
                	imageView.setBackgroundResource(R.drawable.picture_select);
                }else{
                	imageView.setImageBitmap(bitmapList1.get(position));
                	layout.setBackgroundResource(R.color.transparent);
                }
            }
            else
            {
                imageView.setImageResource(R.drawable.ic_launcher);
            }
        }
        return convertView;
    }
    public void addData(List<Bitmap> bitmapList1 ){
    	this.bitmapList1=bitmapList1;
    	notifyDataSetChanged();
    }
   

}

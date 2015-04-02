package com.example.infoSearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.manage.R;
import com.example.tsbx.MainActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;


@SuppressWarnings("deprecation")
public class infoSearchActivity extends Activity{

	private ListView mylistview;
	private ArrayList<String> list = new ArrayList<String>();
	private ArrayList<Integer> imgList=new ArrayList<Integer>();
	private ImageView back;
	ImageView image;
	infoAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_search); 
		//System.out.println("");
		initImgList();
	//	image=(ImageView)findViewById(R.id.xxcximg);
		//image.setDrawingCacheEnabled(true);
		back = (ImageView)findViewById(R.id.infoSearchRet);
		back.setOnClickListener(new ImageView.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				infoSearchActivity.this.finish();
			}
			
		});
		//
		setListData();
        mylistview = (ListView)findViewById(R.id.infoSearchListView);
	//	SimpleAdapter adapter = new SimpleAdapter(this,getData(),R.layout.xxcx_list_item,
		//		new String[]{"img"},new int[]{R.id.xxcximg});
         adapter=new infoAdapter(this,imgList);
		mylistview.setAdapter(adapter);
		mylistview.setOnItemClickListener(new ListView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int position,
					long arg3) {
				// TODO Auto-generated method stub    
				
                new AlertDialog.Builder(infoSearchActivity.this)
	            .setMessage("确定拨打"+list.get(position)+"?")
	            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
	            	Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+list.get(position)));  
	                startActivity(intent); 
	            }
	            })
	            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
	             //取消按钮事件
	            }
	            })
	            .show();
			}
        	
        });
	}
	private void initImgList()
	{
        imgList.add(R.drawable.changyongdianhua1);
        imgList.add(R.drawable.changyongdianhua2);
        imgList.add(R.drawable.changyongdianhua3);
        imgList.add(R.drawable.changyongdianhua4);
        imgList.add(R.drawable.changyongdianhua5);
        imgList.add(R.drawable.changyongdianhua6);
        imgList.add(R.drawable.changyongdianhua7);
        imgList.add(R.drawable.changyongdianhua8);
        imgList.add(R.drawable.changyongdianhua10);
        imgList.add(R.drawable.changyongdianhua11);
        imgList.add(R.drawable.changyongdianhua12);
        imgList.add(R.drawable.changyongdianhua13);
        imgList.add(R.drawable.changyongdianhua14);
        imgList.add(R.drawable.changyongdianhua15);
        imgList.add(R.drawable.changyongdianhua16);
        imgList.add(R.drawable.changyongdianhua17);
        imgList.add(R.drawable.changyongdianhua18);
        imgList.add(R.drawable.changyongdianhua19);
        imgList.add(R.drawable.changyongdianhua20);
        imgList.add(R.drawable.changyongdianhua21);
        imgList.add(R.drawable.changyongdianhua22);
        imgList.add(R.drawable.changyongdianhua23);
        imgList.add(R.drawable.changyongdianhua24);
        imgList.add(R.drawable.changyongdianhua25);
        imgList.add(R.drawable.changyongdianhua26);
        imgList.add(R.drawable.changyongdianhua27);
        imgList.add(R.drawable.changyongdianhua28);
        imgList.add(R.drawable.changyongdianhua29);
        imgList.add(R.drawable.changyongdianhua30);
        imgList.add(R.drawable.changyongdianhua31);


	}
	private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
 
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("img", R.drawable.changyongdianhua1);
        list.add(map);
        imgList.add(R.drawable.changyongdianhua1);
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("img", R.drawable.changyongdianhua2);
        list.add(map1);
        
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("img", R.drawable.changyongdianhua3);
        list.add(map2);
        
        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("img", R.drawable.changyongdianhua4);
        list.add(map3);
        Map<String, Object> map4 = new HashMap<String, Object>();
        
        map4.put("img", R.drawable.changyongdianhua5);
        list.add(map4);
        Map<String, Object> map5 = new HashMap<String, Object>();
        
        map5.put("img", R.drawable.changyongdianhua6);
        list.add(map5);
        Map<String, Object> map6 = new HashMap<String, Object>();
        
        map6.put("img", R.drawable.changyongdianhua7);
        list.add(map6);
        Map<String, Object> map7 = new HashMap<String, Object>();
        
        map7.put("img", R.drawable.changyongdianhua8);
        list.add(map7);
        Map<String, Object> map8 = new HashMap<String, Object>();
        
        map8.put("img", R.drawable.changyongdianhua10);
        list.add(map8);
        Map<String, Object> map9 = new HashMap<String, Object>();
        
        map9.put("img", R.drawable.changyongdianhua11);
        list.add(map9);
        Map<String, Object> map10 = new HashMap<String, Object>();
        
        map10.put("img", R.drawable.changyongdianhua12);
        list.add(map10);
        Map<String, Object> map11 = new HashMap<String, Object>();
        
        map11.put("img", R.drawable.changyongdianhua13);
        list.add(map11);
        Map<String, Object> map12 = new HashMap<String, Object>();
        
        map12.put("img", R.drawable.changyongdianhua14);
        list.add(map12);
        Map<String, Object> map13 = new HashMap<String, Object>();
        
        map13.put("img", R.drawable.changyongdianhua15);
        list.add(map13);
        Map<String, Object> map14 = new HashMap<String, Object>();
        
        map14.put("img", R.drawable.changyongdianhua16);
        list.add(map14);
        Map<String, Object> map15 = new HashMap<String, Object>();
        
        map15.put("img", R.drawable.changyongdianhua17);
        list.add(map15);
        Map<String, Object> map16 = new HashMap<String, Object>();
        
        map16.put("img", R.drawable.changyongdianhua18);
        list.add(map16);
        Map<String, Object> map17 = new HashMap<String, Object>();
        
        map17.put("img", R.drawable.changyongdianhua19);
        list.add(map17);
        Map<String, Object> map18 = new HashMap<String, Object>();
        
        map18.put("img", R.drawable.changyongdianhua20);
        list.add(map18);
        Map<String, Object> map19 = new HashMap<String, Object>();
        
        map19.put("img", R.drawable.changyongdianhua21);
        list.add(map19);
        Map<String, Object> map20 = new HashMap<String, Object>();
        
        map20.put("img", R.drawable.changyongdianhua22);
        list.add(map20);
        Map<String, Object> map21 = new HashMap<String, Object>();
        
        map21.put("img", R.drawable.changyongdianhua23);
        list.add(map21);
        Map<String, Object> map22 = new HashMap<String, Object>();
        
        map22.put("img", R.drawable.changyongdianhua24);
        list.add(map22);
        Map<String, Object> map23 = new HashMap<String, Object>();
        
        map23.put("img", R.drawable.changyongdianhua25);
        list.add(map23);
        Map<String, Object> map24 = new HashMap<String, Object>();
        
        map24.put("img", R.drawable.changyongdianhua26);
        list.add(map24);
        Map<String, Object> map25 = new HashMap<String, Object>();
        
        map25.put("img", R.drawable.changyongdianhua27);
        list.add(map25);
        Map<String, Object> map26 = new HashMap<String, Object>();
        
        map26.put("img", R.drawable.changyongdianhua28);
        list.add(map26);
        Map<String, Object> map27 = new HashMap<String, Object>();
        
        map27.put("img", R.drawable.changyongdianhua29);
        list.add(map27);
        Map<String, Object> map28 = new HashMap<String, Object>();
        
        map28.put("img", R.drawable.changyongdianhua30);
        list.add(map28);
        Map<String, Object> map29 = new HashMap<String, Object>();
        
        map29.put("img", R.drawable.changyongdianhua31);
        list.add(map29);
         
        return list;
    } 
	private void setListData(){
		list.add("12345");
		list.add("84565938");
		list.add("84565933");
		list.add("84565912");
		list.add("12122");
		//6
		list.add("12319");
		list.add("12358");
		list.add("84838110");
		list.add("95598");
		//11
		list.add("10109998");
		list.add("84678631");
		list.add("12315");
		list.add("12121");
		list.add("84600000");
		//16
		list.add("84517388");
		list.add("84600000");
		list.add("84517388");
		list.add("84882110");
		//20
		list.add("12365");
		list.add("12333");
		list.add("12395");
		list.add("55622813");
		list.add("95105188");
		//25
		list.add("84600000");
		list.add("12366");
		list.add("88958658");
		list.add("53648645");
		list.add("84883003");
		//30
		list.add("86605515");
		list.add("55601200");
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK) {  
        	list.clear();
        	adapter.notifyDataSetChanged();
        	//image.getDrawingCache().recycle();
        	this.setContentView(R.layout.null_view);
        	infoSearchActivity.this.finish();
        	
            return true;   
        } else  
            return super.onKeyDown(keyCode, event);  
    }
	
}

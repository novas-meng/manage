package com.example.infoSearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.example.manage.R;


@SuppressWarnings("deprecation")
public class lifeSearch extends Activity {

	private TabHost myTabhost;
	private ListView listview;
	private ListView mylistview;
	private myAdapter listItemAdapter;
	private ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
	private ArrayList<String> list = new ArrayList<String>();
	private ArrayList<String> url = new ArrayList<String>();
	private Button bus,train,flight,express;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	
	    this.setContentView(R.layout.life_search);
		listview = (ListView)findViewById(R.id.infoSearchListView);
		HashMap<String, Object> map = new HashMap<String, Object>();   
        map.put("name", "医院");  
        map.put("number","120");  
        listItem.add(map);  
        HashMap<String, Object> map1 = new HashMap<String, Object>();   
        map1.put("name", "派出所");  
        map1.put("number","110");  
        listItem.add(map1);  
        
      //生成适配器的Item和动态数组对应的元素  
        listItemAdapter = new myAdapter(this,listItem,//数据源   
            R.layout.info_search_listitem,//ListItem的XML实现  
            //动态数组与ImageItem对应的子项          
            new String[] {"name","number"},   
            //ImageItem的XML文件里面的一个ImageView,两个TextView ID  
            new int[] {R.id.frequentlyUsedPhonesName,R.id.frequentlyUsedPhonesNumber,R.id.infoSearchDial}  
        );  

       
        //添加并且显示  
        listview.setAdapter(listItemAdapter); 
        
        
       
        
       
	}
	public class myAdapter extends BaseAdapter{
		public Context mContext;
		public int mResource;
		public List<HashMap<String, Object>> mData;
		public String []mFrom;
		public int []mTo;
		public LayoutInflater inflater;

		

		public myAdapter(Context context,List<HashMap<String, Object>> data,int resource,String []from,int []to
				) {
			super();
			mContext = context;
			mData = data;
			mResource = resource;
			mFrom = from;
			mTo = to;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mData.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int positon, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder holder;
			if(convertView == null){
				convertView = LayoutInflater.from(mContext).inflate(mResource,parent,false);
				holder = new ViewHolder();
				holder.name = (TextView)convertView.findViewById(mTo[0]);
				holder.number = (TextView)convertView.findViewById(mTo[1]);
				holder.dial = (Button)convertView.findViewById(mTo[2]);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			final Map<String,?> dataSet = mData.get(positon);
			if(dataSet == null)return null;
			final Object data0 = dataSet.get(mFrom[0]);
			final Object data1 = dataSet.get(mFrom[1]);
			//final Object data2 = dataSet.get(mFrom[2]);
			
			holder.name.setText(data0.toString());
			holder.number.setText(data1.toString());
			holder.dial.setOnClickListener(new Button.OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String number = data1.toString();  
	                //用intent启动拨打电话  
	                Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+number));  
	                startActivity(intent);  
				}
				
			});
			
			
			return convertView;
		}
		class ViewHolder{
			TextView name;
			TextView number;
			Button dial;
		}
		
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK) {  
        	
        	
        	lifeSearch.this.finish();
        	
            return true;   
        } else  
            return super.onKeyDown(keyCode, event);  
    }
}

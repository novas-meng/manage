package com.example.infoSearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.infoPush.infoDetial;
import com.example.manage.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class xxcxActivity extends Activity{

	private ListView lv;
	private ArrayList<String> url = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xxcx);
		ImageView bbm=(ImageView)findViewById(R.id.xxcx_ret);
		bbm.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO 自动生成的方法存根
					//startActivity(new Intent(getApplicationContext(),AroundActivity.class));
					xxcxActivity.this.finish();
				}
			});
		lv = (ListView)findViewById(R.id.xxcxListView);
	//	url.add("http://www.12306.cn/mormhweb/");
		url.add("http://touch.qunar.com/h5/train/?from=touchindex");
       // url.add("http://www.8684.cn");
		url.add("http://wap.8684.cn/bus.php");
        url.add("http://www.kiees.cn");
      //  url.add("http://flight.qunar.com/status/alphlet_order.jsp?ex_track=bd_aladding_flightsk_title");
      url.add("http://touch.qunar.com/h5/flight/?bd_source=8684_touch");
        //url.add("http://www.hrbgaj.gov.cn/zxbs/wscx.htm");
        url.add("http://m.46644.com/tool/illegal/");
		SimpleAdapter adapter = new SimpleAdapter(this,getData(),R.layout.xxcx_list_item,
				new String[]{"img"},new int[]{R.id.xxcximg});
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new ListView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();        
		        intent.setAction("android.intent.action.VIEW"); 
		        Uri content_url = Uri.parse(url.get(position));   
		        intent.setData(content_url);  
		        startActivity(intent);
			}
        	
        });
	}
	private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
 
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("img", R.drawable.xxcx_train);
        list.add(map);
 
        map = new HashMap<String, Object>();
        map.put("img", R.drawable.xxcx_bus);
        list.add(map);
 
        map = new HashMap<String, Object>();
        map.put("img", R.drawable.xxcx_express);
        list.add(map);
        
        map = new HashMap<String, Object>();
        map.put("img", R.drawable.xxcx_flight);
        list.add(map);
        
        map = new HashMap<String, Object>();
        map.put("img", R.drawable.xxcx_weizhang);
        list.add(map);
         
        return list;
    }
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK) {  
        	
        	
        	xxcxActivity.this.finish();
        	
            return true;   
        } else  
            return super.onKeyDown(keyCode, event);  
    }
}

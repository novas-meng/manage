package com.example.yy;

import java.util.ArrayList;

import com.example.manage.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class yyInformationAdapter extends BaseAdapter
{
    ArrayList<yyInformationItem> list;
    Context context;
    LayoutInflater flater;
    private Button curDel_btn;
	private float x,ux;
    private static final String[] costs={"物业费","车位费","电梯费","供暖费","电费","水费","燃气费","宽带通讯费"};
    OnItemSelectedListener spinnerListener;
    public yyInformationAdapter(ArrayList<yyInformationItem> list,Context context,OnItemSelectedListener spinnerListener)
    {
    	this.list=list;
    	this.context=context;
    	this.spinnerListener=spinnerListener;
    	flater=LayoutInflater.from(context);
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}
   
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View convertview, ViewGroup arg2) {
		// TODO Auto-generated method stub
		yyInformationItem entity=list.get(position);
		final ViewHolder viewholder=new ViewHolder();
		if(entity.getType()==0)
		{
			convertview=flater.inflate(R.layout.yy_list_item_guanjia_confirm,null);
			viewholder.btnDel = (Button) convertview.findViewById(R.id.yy_del);
			viewholder.btnDel.setContentDescription("del&&"+position+"&&"+entity.getId());
			viewholder.text1=(TextView)convertview.findViewById(R.id.yy_guanjia_yezhuname);
			viewholder.text2=(TextView)convertview.findViewById(R.id.yy_guanjia_content);
			viewholder.button1=(Button)convertview.findViewById(R.id.yy_guanjia_response);
			viewholder.text1.setText(entity.getName());
			viewholder.text2.setText(entity.getBaseInformation1());
		//	viewholder.button1=(Button)convertview.findViewById(R.id.yy_guanjia_response);
			
		}
		else if(entity.getType()==1)
		{
			convertview=flater.inflate(R.layout.yy_list_item_yezhu,null);
			viewholder.btnDel = (Button) convertview.findViewById(R.id.yy_del);
			viewholder.btnDel.setContentDescription("nodel&&"+position+"&&"+entity.getId());
			viewholder.text1=(TextView)convertview.findViewById(R.id.yy_yezhu_guanjia_text);
			viewholder.text2=(TextView)convertview.findViewById(R.id.yy_yezhu_text1);
			viewholder.text3=(TextView)convertview.findViewById(R.id.yy_yezhu_text2);
			viewholder.edittext1=(EditText)convertview.findViewById(R.id.yy_yezhu_edittext1);
			viewholder.edittext2=(EditText)convertview.findViewById(R.id.yy_yezhu_edittext2);
			viewholder.edittext3=(EditText)convertview.findViewById(R.id.yy_yezhu_edittext3);
			viewholder.edittext4=(EditText)convertview.findViewById(R.id.yy_yezhu_edittext4);
            viewholder.button1=(Button)convertview.findViewById(R.id.yy_send);
            viewholder.text1.setText(entity.getName());
            viewholder.text2.setText(entity.getBaseInformation1());
           // viewholder.text3.setText(entity.getBaseInformation2());
            String date=Utils.getNowDate();
            System.out.println("date="+date);
            String[] strs=date.split("-");
            viewholder.edittext1.setText(strs[1]);
            viewholder.edittext2.setText(strs[2]);
            viewholder.edittext3.setText(strs[3]);
            viewholder.edittext4.setText(strs[0]);
            viewholder.spinner=(Spinner)convertview.findViewById(R.id.yy_yezhu_charge);
        	ArrayAdapter<String> adapter=new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,costs);
			 adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			viewholder.spinner.setAdapter(adapter);
			viewholder.spinner.setVisibility(View.VISIBLE);
			viewholder.spinner.setOnItemSelectedListener(spinnerListener);
            //viewholder.datepicker=(DatePicker)convertview.findViewById(R.id.yezhu_yuyue_date);
          //  ((LinearLayout) ((ViewGroup)viewholder.datepicker.getChildAt(0)).getChildAt(0)).getChildAt(0).setVisibility(View.GONE);
			//为删除按钮添加监听事件，实现点击删除按钮时删除该项
			
		}
		else
		{
			convertview=flater.inflate(R.layout.yy_list_item_guanjia_success,null);
			viewholder.btnDel = (Button) convertview.findViewById(R.id.yy_del);
			viewholder.btnDel.setContentDescription("del&&"+position+"&&"+entity.getId());
			viewholder.text1=(TextView)convertview.findViewById(R.id.yy_guanjia_success_yezhuname);
			viewholder.text2=(TextView)convertview.findViewById(R.id.yy_guanjia_success_content);
			viewholder.text1.setText(entity.getName());
			viewholder.text2.setText(entity.getBaseInformation1());
		}
		/*
		viewholder.btnDel.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
	    		String id=v.getContentDescription().toString();
				YYMainActivity.delInformation(id);
			    list.remove(position);
				notifyDataSetChanged();
				
			}
		});	
		*/		
		return convertview;
	}
 class  ViewHolder
 {
	 EditText edittext1;
	 EditText edittext2;
	 EditText edittext3;
	 EditText edittext4;
	 TextView text1;
	 TextView text2;
	 TextView text3;
	 Button button1;
	 Spinner spinner;
     Button btnDel;
 }
}

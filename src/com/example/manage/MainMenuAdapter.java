package com.example.manage;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainMenuAdapter extends BaseAdapter
{

	private List<listitem> list;
	Context context;
	private LayoutInflater inflater;
	ImageView img;
	TextView title;
	TextView content;
	public MainMenuAdapter(Context context,List<listitem> list)
	{
		this.context=context;
		this.list=list;
		inflater=LayoutInflater.from(context);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView==null)
		{
			convertView=inflater.inflate(R.layout.mainmenu_item_list,null);
		}
		img=(ImageView)convertView.findViewById(R.id.mainmenu_item_pic);
		title=(TextView)convertView.findViewById(R.id.mainmenu_item_title);
		content=(TextView)convertView.findViewById(R.id.mainmenu_item_content);
		img.setBackgroundResource(list.get(position).getPic());
		title.setText(list.get(position).getTitle());
		content.setText(list.get(position).getContent());
		return convertView;
	}

}

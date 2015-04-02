package com.example.tsbx;

import java.util.List;

import com.example.manage.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class FaceAdapter extends BaseAdapter
{
    private List<ChatEmoji> data;
    private LayoutInflater inflater;
    private Context context;
    public FaceAdapter(Context context,List<ChatEmoji> list)
    {
    	this.context=context;
    	this.data=list;
    	inflater=LayoutInflater.from(context);
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ChatEmoji emoji=data.get(position);
		ViewHolder viewholder=null;
		if(convertView==null)
		{
			viewholder=new ViewHolder();
			convertView=inflater.inflate(R.layout.item_face,null);
			viewholder.iv_face=(ImageView)convertView.findViewById(R.id.item_iv_face);
			convertView.setTag(viewholder);
		}
		else
		{
			viewholder=(ViewHolder)convertView.getTag();
		}
		viewholder.iv_face.setBackgroundResource(emoji.getId());
		return convertView;
	}
   class ViewHolder
   {
	   ImageView iv_face;
   }
}

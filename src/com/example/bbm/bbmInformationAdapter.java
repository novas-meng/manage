package com.example.bbm;

import java.util.List;

import com.example.manage.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class bbmInformationAdapter extends BaseAdapter
{
   private List<bbmInformationEntity> entityList;
   private LayoutInflater inflater;
   private Context context;
   public bbmInformationAdapter(Context context,List<bbmInformationEntity> entityList)
   {
	   this.context=context;
	   this.entityList=entityList;
	   inflater=LayoutInflater.from(context);
   }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return entityList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return entityList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertview, ViewGroup arg2) {
		// TODO Auto-generated method stub
		bbmInformationEntity entity=entityList.get(arg0);
		ViewHolder viewholder=new ViewHolder();
		boolean isVoice=entity.getIsVoice();
		if(isVoice)
		{
			convertview=inflater.inflate(R.layout.bbm_voice_item,null);
			viewholder.voice=(Button)convertview.findViewById(R.id.bbm_item_voice);
		//	viewholder.voice.setText("       "+entity.getVoiceTime()+"");
			viewholder.voice.setContentDescription(entity.getContent());
		}
		else
		{
			convertview=inflater.inflate(R.layout.bbm_text_item,null);
			viewholder.content=(TextView)convertview.findViewById(R.id.bbm_item_text);
			viewholder.content.setText("  "+entity.getContent());
		}
		viewholder.phoneNumber=(Button)convertview.findViewById(R.id.bbm_item_phone);
		if(entity.getPhoneNumber().equals("null"))
		{
			System.out.println("帮帮忙 null");
			viewholder.phoneNumber.setVisibility(View.GONE);
		}
		viewholder.phoneNumber.setText(entity.getPhoneNumber());
		viewholder.phoneNumber.setContentDescription(entity.getPhoneNumber());
		return convertview;
	}
   class ViewHolder
   {
	   TextView content;
	   Button phoneNumber;
	   Button voice;
   }
}

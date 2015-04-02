package com.example.tsbx;

import java.util.List;

import com.example.manage.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ChatMsgAdapter extends BaseAdapter
{
     private Context context;
     private List<ChatMsgEntity> coll;
     private LayoutInflater inflater;
	 public ChatMsgAdapter(Context context,List<ChatMsgEntity> coll)
	{
	   this.coll=coll;
	   this.context=context;
	   inflater=LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return coll.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return coll.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	@Override
	public View getView(int position, View convertview, ViewGroup parent) {
		// TODO Auto-generated method stub
		ChatMsgEntity entity=coll.get(position);
		
   		// System.out.println("  ≈‰∆˜"+entity.getMsgType()+" "+entity.getText());
   		
		boolean isComMsg=entity.getMsgType();
		boolean isVoice=entity.getIsVoice();
		ViewHolder viewholder=new ViewHolder();
		//if(convertview==null)
		//{
			if(isComMsg)
			{
				if(isVoice)
				{
					convertview=inflater.inflate(R.layout.chatting_item_msg_voice_left,null);
					viewholder.voicePlay=(ImageView)convertview.findViewById(R.id.iv_voice);
					viewholder.voicePlay.setContentDescription(entity.getAudioPath()); 
					viewholder.userhead=(ImageView)convertview.findViewById(R.id.iv_userhead);

					viewholder.userhead.setImageBitmap(MainActivity.butlerhead);
				}
				else
				{
					convertview=inflater.inflate(R.layout.chatting_item_msg_text_left,null);
					viewholder.tvContent=(TextView)convertview.findViewById(R.id.tv_chatcontent);
					viewholder.tvContent.setText(entity.getText());
					if(entity.getHasImage())
					viewholder.tvContent.setContentDescription("id=1"+entity.getImagePath());
					else
				    viewholder.tvContent.setContentDescription("id=0");
					viewholder.userhead=(ImageView)convertview.findViewById(R.id.iv_userhead);
			    	Bitmap butlerhead = BitmapFactory.decodeResource(context.getResources(), R.drawable.kefu);
					viewholder.userhead.setImageBitmap(MainActivity.butlerhead);
				//	viewholder.userhead.setImageBitmap(butlerhead);

				}
				
			}
			else
			{
				if(isVoice)
				{
					convertview=inflater.inflate(R.layout.chatting_item_msg_voice_right,null);
					viewholder.voicePlay=(ImageView)convertview.findViewById(R.id.iv_voice);
					viewholder.voicePlay.setContentDescription(entity.getAudioPath()); 
					viewholder.userhead=(ImageView)convertview.findViewById(R.id.iv_userhead);
					viewholder.userhead.setImageBitmap(MainActivity.userhead);
				}
				else
				{
					convertview=inflater.inflate(R.layout.chatting_item_msg_text_right,null);
					viewholder.tvContent=(TextView)convertview.findViewById(R.id.tv_chatcontent);
					viewholder.tvContent.setText(entity.getText());
					if(entity.getHasImage())
					viewholder.tvContent.setContentDescription("id=1"+entity.getImagePath());
					else
					viewholder.tvContent.setContentDescription("id=0");	
					viewholder.userhead=(ImageView)convertview.findViewById(R.id.iv_userhead);
					viewholder.userhead.setImageBitmap(MainActivity.userhead);
				}	
			}
			
			viewholder.tvSendTime=(TextView)convertview.findViewById(R.id.tv_sendtime);
			viewholder.progressBar1 = (ProgressBar) convertview
					.findViewById(R.id.progressBar1);
			viewholder.sendFail = (ImageView) convertview
					.findViewById(R.id.sendFail);
			if(!isComMsg)
			{
				if(entity.getSendStatus())
				{
					viewholder.sendFail.setVisibility(View.GONE);
				}
				else
				{
					viewholder.sendFail.setVisibility(View.VISIBLE);
				}
			}	
			viewholder.isComMsg = isComMsg;
			viewholder.isVoice=isVoice;
			//convertview.setTag(viewholder);
		//}
		//else
		//{
		//	viewholder=(ViewHolder)convertview.getTag();
		//}
		viewholder.tvSendTime.setText(entity.getDate());
		return convertview;
	}
   class ViewHolder
   {
	   public TextView tvSendTime;
		public TextView tvContent;
		public ProgressBar progressBar1;
		public ImageView sendFail;
		public ImageView voicePlay;
		public ImageView userhead;
		public boolean isComMsg = true;
		public boolean isVoice=true;
   } 
}

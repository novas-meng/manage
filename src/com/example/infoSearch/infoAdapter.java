package com.example.infoSearch;

import java.io.InputStream;
import java.util.ArrayList;

import com.example.manage.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class infoAdapter extends BaseAdapter
{
	private Context context;
	private LayoutInflater inflater;
	private ArrayList<Integer> list;
   public infoAdapter(Context context,ArrayList<Integer> list)
   {
	   this.list=list;
	   this.context=context;
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
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder=null;
		int id=list.get(arg0);
		if(arg1==null)
		{
			holder=new ViewHolder();
			arg1=inflater.inflate(R.layout.xxcx_list_item,null);
			holder.image=(ImageView)arg1.findViewById(R.id.xxcximg);
			holder.image.setDrawingCacheEnabled(true);
			arg1.setTag(holder);
		}
		else
		{
			holder=(ViewHolder)arg1.getTag();
		}
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		InputStream is =context.getResources().openRawResource(id);
		Bitmap bm = BitmapFactory.decodeStream(is, null, opt);
		BitmapDrawable bd = new BitmapDrawable(context.getResources(), bm);
		holder.image.setBackgroundDrawable(bd);
		return arg1;
	}
   class ViewHolder
   {
	   ImageView image;
   }
}

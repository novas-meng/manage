package com.example.infoPush;

import java.io.InputStream;

import com.example.manage.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class infoPushActivity extends Activity {
	private ImageView secondHouse, secondGood, recruit, infoPush, carPool,
			houseKeeping;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_push);
		ImageView pushinfoup=(ImageView)findViewById(R.id.pushinfoup);
		BitmapFactory.Options opt = new BitmapFactory.Options();

		opt.inPreferredConfig = Bitmap.Config.RGB_565;

		opt.inPurgeable = true;

		opt.inInputShareable = true;

		InputStream is = getResources().openRawResource(

				 R.drawable.pushinfo_background );

		Bitmap bm = BitmapFactory.decodeStream(is, null, opt);

		BitmapDrawable bd = new BitmapDrawable(getResources(), bm);

		 //holder.iv.setBackgroundDrawable(bd);
		//bg.setBackgroundResource(R.drawable.comnews_newsbg);}
		pushinfoup.setBackgroundDrawable(bd);
		 LinearLayout layout1=(LinearLayout)findViewById(R.id.infopushlayout1);
			is = getResources().openRawResource(
					 R.drawable.infopush_background );
			 bm = BitmapFactory.decodeStream(is, null, opt);
			 bd = new BitmapDrawable(getResources(), bm);
			layout1.setBackgroundDrawable(bd);
		
		secondHouse = (ImageView) findViewById(R.id.infoPushSecondHandHouse);
		secondHouse.setDrawingCacheEnabled(true);
		secondGood = (ImageView) findViewById(R.id.infoPushSecondHandGood);
		secondGood.setDrawingCacheEnabled(true);

		recruit = (ImageView) findViewById(R.id.infoPushRecruit);
		recruit.setDrawingCacheEnabled(true);

		infoPush = (ImageView) findViewById(R.id.infoPush);
		infoPush.setDrawingCacheEnabled(true);

		
		carPool = (ImageView) findViewById(R.id.infoPushCarPool);
		carPool.setDrawingCacheEnabled(true);

		houseKeeping = (ImageView) findViewById(R.id.infoPushHouseKeeping);
		houseKeeping.setDrawingCacheEnabled(true);

		secondHouse.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				infoDetial.setclass("secondHouse");
				Intent intent = new Intent();
				System.out.println("secondHouse");
				intent.setClass(infoPushActivity.this, infoDetial.class);
				startActivity(intent);
			}

		});
		secondGood.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				infoDetial.setclass("secondGood");
				Intent intent = new Intent();
				System.out.println("secondGood");
				intent.setClass(infoPushActivity.this, infoDetial.class);
				startActivity(intent);
			}

		});
		recruit.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				infoDetial.setclass("recruit");
				Intent intent = new Intent();
				System.out.println("secondHouse");
				intent.setClass(infoPushActivity.this, infoDetial.class);
				startActivity(intent);
			}

		});
		infoPush.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent intent = new Intent();
				System.out.println("infoPush");
				intent.setClass(infoPushActivity.this, infoUploadActivity.class);
				startActivity(intent);

			}

		});
		carPool.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				infoDetial.setclass("carPool");
				Intent intent = new Intent();
				System.out.println("carPool");
				intent.setClass(infoPushActivity.this, infoDetial.class);
				startActivity(intent);

			}

		});
		houseKeeping.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				infoDetial.setclass("houseKeeping");
				Intent intent = new Intent();
				System.out.println("houseKeeping");
				intent.setClass(infoPushActivity.this, infoDetial.class);
				startActivity(intent);

			}

		});

	}

	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
        setContentView(R.layout.null_view);
        System.gc();
        secondHouse.getDrawingCache().recycle();
        secondGood.getDrawingCache().recycle();
        recruit.getDrawingCache().recycle();
        infoPush.getDrawingCache().recycle();
        carPool.getDrawingCache().recycle();
        houseKeeping.getDrawingCache().recycle();

		infoPushActivity.this.finish();
		super.onDestroy();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			infoPushActivity.this.finish();

			return true;
		} else
			return super.onKeyDown(keyCode, event);
	}
}

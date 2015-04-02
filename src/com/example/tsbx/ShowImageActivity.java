package com.example.tsbx;
import com.example.manage.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

public class ShowImageActivity extends Activity {
	ImageView camera;
	 InputMethodManager imm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_showimage);
		init();
	}
	Object imgUrl;
	private void init() {
		camera=(ImageView) findViewById(R.id.showimage_picture);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		imgUrl = bundle.get("filename");
		//Bitmap bitmap = (Bitmap)msg.get("data");
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 1;
		//Bitmap bitmap =BitmapFactory.decodeFile(imgUrl.toString(),options);
		
		Bitmap bitmap=Tools.decodeBitmap(imgUrl.toString());
		camera.setImageBitmap(bitmap);
	}

	public void btnClick(View v){
		switch (v.getId()) {
		case R.id.showimage_save:
			Toast.makeText(ShowImageActivity.this,"图片保存在"+imgUrl.toString(),Toast.LENGTH_SHORT).show();
			ShowImageActivity.this.finish();
			break;
		case R.id.showimage_cancel:
			ShowImageActivity.this.finish();
			break;
		case R.id.showimage_back:	
			ShowImageActivity.this.finish();
			break;
		}
	}
	

}

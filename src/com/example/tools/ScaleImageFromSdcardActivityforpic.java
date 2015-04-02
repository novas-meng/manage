package com.example.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.manage.R;


import android.R.color;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class ScaleImageFromSdcardActivityforpic extends Activity implements
		OnScrollListener{
	public static Boolean is=false;
	Bitmap bm;
	int IndexPposition = 1;
	int pageCount = 30;
	Context mContext = ScaleImageFromSdcardActivityforpic.this;
	public static HashMap<Integer, String> map = new HashMap<Integer, String>();// �������MediaStore��ȡ����Ƭ·��
	int counter = 0;// �����ܹ��ж�����ͼƬ��ַ��·��������
	int getImgCounter = 0;// �����ܹ��ж�����ͼƬ��ַ��·��������
	Cursor cursor;// �����α�������ͼƬ��ַ
	private GridView gridview;
	List<Bitmap> bitmapList = new ArrayList<Bitmap>();// ���ͼƬ
    
	ImageAdapter imageAdapter;
	GestureDetector mGestureDetector;
	private int visibleLastIndex = 0; // ���Ŀ���������
	private int visibleItemCount; // ��ǰ���ڿɼ�������
	myThread mt;
	boolean flag = true;// ��¼���������߳��Ƿ���
    public static void setIs(Boolean i)
    {
    	is=i;
    }
	private Handler myHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				flag = true;
				imageAdapter.addData(bitmapList);
				gridview.setSelection(visibleLastIndex - visibleItemCount + 1); // ����ѡ����
				break;
			case 2:
				if (mt != null) {
					mt.interrupted();
					Log.d("TAG", "�̹߳ر�");
				}
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.select_image_from_sdcard);
		OperateSdcard();

		init();
	}

	/**
	 * ��ʼ������
	 */
	public void init() {

		imageAdapter = new ImageAdapter(ScaleImageFromSdcardActivityforpic.this);
		gridview = (GridView) findViewById(R.id.gridview);
		imageAdapter.setImgMap(bitmapList);
		gridview.setAdapter(imageAdapter);
		gridview.setOnScrollListener(this); // ��ӻ�������
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
//				Toast.makeText(getApplicationContext(), "" + position,
//						Toast.LENGTH_LONG).show();
				
				imageAdapter.add(position,is);
				imageAdapter.notifyDataSetChanged();
			}
		});
	}

	/**
	 * ��ť�¼�
	 */
	public void btnClick(View v) {
		switch (v.getId()) {
		case R.id.image_btn_back:
			ScaleImageFromSdcardActivityforpic.this.finish();
			// imageAdapter.setImgMap(null);
			imageAdapter.ClickList.clear();// ���ѡ������
			// �����л�����������߽��룬�ұ��˳�
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_rigth);
			break;
		case R.id.btn_preview:
			if(imageAdapter.ClickList.size()>0){
				Intent intent = new Intent(ScaleImageFromSdcardActivityforpic.this,
						PicturesShowActivityforpic.class);
				Object[] data = imageAdapter.getClickList().toArray();
				intent.putExtra("selectPicture", data);
				ScaleImageFromSdcardActivityforpic.this.startActivityForResult(intent,
						Utils.SHOW_SELECT_PICTURE);
				// �����л����������ұ߽��룬����˳�
				overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
				imageAdapter.ClickList.clear();// ���ѡ������
				imageAdapter.notifyDataSetChanged();
			}else{
				Toast.makeText(ScaleImageFromSdcardActivityforpic.this, "��ѡ��ͼƬ", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.btn_get_picture:
			Log.d("TAG", "����ѡ���ͼƬ");
			//Intent intent = new Intent();
			//Object[] data = imageAdapter.getClickList().toArray();
			//intent.putExtra("selectPicture", data);
			//Log.d("TAG", "����ѡ���ͼƬsc" + data.toString());
			//ScaleImageFromSdcardActivity.this.setResult(
			//		Utils.SHOW_PICTURE_RESULT, intent);

			ScaleImageFromSdcardActivityforpic.this.finish();
			// �����л�����������߽��룬�ұ��˳�
			//overridePendingTransition(R.anim.in_from_left, R.anim.out_to_rigth);
			//imageAdapter.ClickList.clear();// ���ѡ������
			break;
		case R.id.btn_clear_select:
			imageAdapter.ClickList.clear();// ���ѡ������
			imageAdapter.notifyDataSetChanged();
			break;
		}
	}

	// ����MediaStore������sd������ȡ��Ƭ
	public void OperateSdcard() {
		String[] proj = { MediaStore.Images.Media.DATA };
		Uri mUri = Uri.parse("content://media/external/images/media");
		cursor = managedQuery(mUri, proj, null, null, null);
		counter = cursor.getCount();
		// Log.d("TAG", "counter1"+counter);
		new Thread() {
			@Override
			public void run() {
				try {
					cursor.moveToPosition((IndexPposition - 1) * pageCount);
					for (int i = 0; i < pageCount; i++) {
						if (cursor != null) {
							if (counter > (IndexPposition - 1) * pageCount + i) {
								int column_index = cursor
										.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
								String fileName = cursor
										.getString(column_index);
								// String url=cursor.get
								// Log.d("TAG", i+"fileName..."+fileName);
								BitmapFactory.Options options = new BitmapFactory.Options();
								options.inSampleSize = 4;
								bm = BitmapFactory
										.decodeFile(fileName, options);
								if (bm != null) {
									map.put(getImgCounter, fileName);
									getImgCounter = getImgCounter + 1;
									//Log.d("TAG", "bm" + bm);
									Bitmap btm2 = Bitmap.createScaledBitmap(bm,
											150, 150, false); // ����������Զ������Ĵ�С
									bitmapList.add(btm2);
									myHandler.sendEmptyMessage(1);
								}

							}
							cursor.moveToNext();

						}
					}
				} catch (Exception e) {
					Log.d("TAG", "����ͼƬ�Ǽ��ع������˳���cursor�ƶ��쳣");
				}
			}
		}.start();
	}

	class myThread extends Thread {
		@Override
		public void run() {
			Log.d("TAG", "�߳̿�ʼ");
			try {
				cursor.moveToPosition((IndexPposition - 1) * pageCount);
				for (int i = 0; i < pageCount - 10; i++) {
					if (counter > (IndexPposition - 1) * pageCount + i) {
						int column_index = cursor
								.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						String fileName = cursor.getString(column_index);
						//Log.d("TAG", i + "fileName..." + fileName);
						BitmapFactory.Options options = new BitmapFactory.Options();
						options.inSampleSize = 4;
						bm = BitmapFactory.decodeFile(fileName, options);
						if (bm != null) {
							map.put(getImgCounter, fileName);
							getImgCounter = getImgCounter + 1;
							//Log.d("TAG", "bm" + bm);
							Bitmap btm2 = Bitmap.createScaledBitmap(bm, 150,
									150, false); // ����������Զ������Ĵ�С
							bitmapList.add(btm2);
							myHandler.sendEmptyMessage(1);
						}
					}

					cursor.moveToNext();
				}
			} catch (Exception e) {
				Log.d("TAG", "����ͼƬ�Ǽ��ع������˳���cursor�ƶ��쳣");
			}
			myHandler.sendEmptyMessage(2);// �ر��߳�
			Log.d("TAG", "����20�����");

		}
	}

	public String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // �ж�sd���Ƿ����
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// ��ȡ��Ŀ¼
		}
		return sdDir.toString();

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.visibleItemCount = visibleItemCount;
		visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
		// Log.d("TAG","visibleLastIndex.."+visibleLastIndex+"\t visibleItemCount"+visibleItemCount
		// );

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int itemsLastIndex = imageAdapter.getCount() - 1; // ���ݼ����һ�������
		int lastIndex = itemsLastIndex - 5; // ���ϵײ���loadMoreView��
		// Log.d("TAG", "lastIndex"+lastIndex);
		// Log.d("TAG", "itemsLastIndex"+itemsLastIndex);
		// Log.d("TAG", "scrollState"+scrollState);
		if (flag && visibleLastIndex >= lastIndex) {
			flag = false;
			// ������Զ�����,��������������첽�������ݵĴ���
			// Log.d("TAG", "������������"+lastIndex);
			// Log.d("TAG", "������������"+itemsLastIndex);
			// Log.d("TAG", "IndexPposition.."+IndexPposition);
			IndexPposition = IndexPposition + 1;
			Log.d("TAG", "IndexPposition.." + IndexPposition);
			mt = new myThread();
			mt.start();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == Utils.SHOW_SELECT_PICTURE
				&& resultCode == Utils.SHOW_SELECT_PICTURE_RESULT) {
			//��ȡ��������ѡ���ͼƬ
			Bundle bundle = data.getExtras();
			Object[] datas=(Object[]) bundle.get("selectPicture");
			//����intent��ѡ���ͼƬ���͵���һ��activity
			Intent intent = new Intent();
			intent.putExtra("selectPicture", datas);
			
			Log.d("TAG", "����ѡ���ͼƬsc" + datas.toString());
			ScaleImageFromSdcardActivityforpic.this.setResult(
					Utils.SHOW_PICTURE_RESULT, intent);

			ScaleImageFromSdcardActivityforpic.this.finish();
			// �����л�����������߽��룬�ұ��˳�
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_rigth);
			imageAdapter.ClickList.clear();// ���ѡ������
		}

	}

}

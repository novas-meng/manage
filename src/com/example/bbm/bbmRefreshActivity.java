package com.example.bbm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import com.example.manage.R;
import com.example.tools.IP;
import com.example.wu1.AutoListView;
import com.example.wu1.AutoListView.OnLoadListener;
import com.example.wu1.AutoListView.OnRefreshListener;
import com.example.wu1.XListView;
import com.example.wu1.XListView.IXListViewListener;

import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

public class bbmRefreshActivity extends Activity implements IXListViewListener {
	private List<bbmInformationEntity> entityList = new ArrayList<bbmInformationEntity>();
	private bbmInformationAdapter entityAdapter;
	private static XListView listview;
	ProgressBar process;
	String FileGetUrl = IP.ip + ":3000/uploads/";
	static Handler freshHandler;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			List<bbmInformationEntity> result = (List<bbmInformationEntity>) msg.obj;
			switch (msg.what) {
			case AutoListView.REFRESH:
				entityList.clear();
				entityList.addAll(result);
				break;
			case AutoListView.LOAD:
				entityList.addAll(result);
				break;
			}
			entityAdapter.notifyDataSetChanged();
		};
	};
    
	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
        setContentView(R.layout.null_view);
		bbmRefreshActivity.this.finish();
		super.onDestroy();
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbm_activity_main);
		// process=(ProgressBar)findViewById(R.id.bbm_main_progressBar);
		process = createProgressBar(this, null);
		process.setVisibility(View.GONE);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		Intent startServiceIntent = new Intent(this, NetStateService.class);
		startService(startServiceIntent);
		entityList = new ArrayList<bbmInformationEntity>();
		listview = (XListView) findViewById(R.id.bbm_listview);
		listview.setPullLoadEnable(true);
		listview.setXListViewListener(this);
		init();
		
		IntentFilter fliter = new IntentFilter();
		fliter.addAction("InformationUpdate");
		this.registerReceiver(new myInformationUpdateReceiver(), fliter);
		// IntentFilter filter=new IntentFilter();
		// filter.addAction("OffLineMessage");
		fliter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		// offLineListener=new MyOffLineMessageReceiver();
		// this.registerReceiver(offLineListener, filter);
		this.registerReceiver(new NetStateListener(), fliter);
		freshHandler=new Handler()
		{

			@Override
			public void handleMessage(Message msg) {
				// TODO 自动生成的方法存根
				bbmConstants.skip = 0;
				listview.stopRefresh();
				listview.stopLoadMore();
				listview.setRefreshTime(Utils.getCurrentTime());
				String url = IP.ip + ":3000/helps/gethelp?skip=" + bbmConstants.skip;
				bbmInformationXmlTrans xml = new bbmInformationXmlTrans(url,
						bbmRefreshActivity.this);
				InputStream is = xml.getXmlStream();
				bbmConstants.what = AutoListView.REFRESH;
			}
		};
	}

	class myInformationUpdateReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			if (arg1.getAction().equals("InformationUpdate")) {

				String id = arg1.getStringExtra("id");
				String[] ids = id.split("&&");
				String content = arg1.getStringExtra("content");
				System.out.println("content=" + content);
				String[] contents = content.split("&&");
				String phonenumber = arg1.getStringExtra("phonenumber");
				String[] phonenumbers = phonenumber.split("&&");
				List<bbmInformationEntity> list = new ArrayList<bbmInformationEntity>();
				if (!content.isEmpty()) {
					for (int i = 0; i < ids.length; i++) {
						System.out.println("添加的数据个数" + ids.length);
						bbmInformationEntity entity = new bbmInformationEntity();
						if (ids[i].equals("1")) {
							entity.setIsVoice(true);
							entity.setContent(contents[i]);
							entity.setPhoneNumber(phonenumbers[i]);
						} else {
							entity.setIsVoice(false);
							entity.setContent(contents[i]);
							entity.setPhoneNumber(phonenumbers[i]);
						}
						list.add(entity);
					}
				}
				handler.obtainMessage(bbmConstants.what, list).sendToTarget();
				/*
				 * Message msg = handler.obtainMessage(); msg.what = 1; msg.obj
				 * = getData(); handler.sendMessage(msg);
				 */
			}
		}

	}

	public void init() {

		entityAdapter = new bbmInformationAdapter(bbmRefreshActivity.this,
				entityList);
		listview.setAdapter(entityAdapter);
		initData();
	}

	private void initData() {
		bbmConstants.skip = 0;
		loadData(AutoListView.LOAD);
	}

	public  void loadData(final int what) {
		// 从服务器获取信息
		listview.stopRefresh();
		listview.stopLoadMore();
		listview.setRefreshTime(Utils.getCurrentTime());
		String url = IP.ip + ":3000/helps/gethelp?skip=" + bbmConstants.skip;
		bbmInformationXmlTrans xml = new bbmInformationXmlTrans(url,
				bbmRefreshActivity.this);
		InputStream is = xml.getXmlStream();
		bbmConstants.what = what;

	}

	// 测试数据
	public List<bbmInformationEntity> getData() {
		List<bbmInformationEntity> list = new ArrayList<bbmInformationEntity>();
		for (int i = 0; i < 10; i++) {
			bbmInformationEntity entity = new bbmInformationEntity();
			entity.setContent(i + "");
			entity.setIsVoice(false);
			entity.setPhoneNumber("13274603175" + i);
			list.add(entity);
		}
		bbmInformationEntity entity = new bbmInformationEntity();
		entity.setContent("fdsfsdf" + "");
		entity.setIsVoice(true);
		entity.setVoiceTime(7);
		entity.setPhoneNumber("13274603175m");
		list.add(entity);
		return list;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public  void onRefresh() {
		// TODO Auto-generated method stub
		System.out.println("刷新内容");
		bbmConstants.skip = 0;
		loadData(AutoListView.REFRESH);
	}

	public void CallPhone(View v) {
		if (v.getId() == R.id.bbm_item_phone) {
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ v.getContentDescription()));
			startActivity(intent);
		}
	}

	// 文件处理的handler类
	Handler myfilehandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == 0) {
				process.setVisibility(View.GONE);
				System.out.println("获取失败");
			} else {
				process.setVisibility(View.GONE);
				System.out.println("获取成功");
			}
		}
	};
	// 从网上获取音频文件
	Runnable filerun = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			URL url;
			// FileGetUrl="http://113.6.252.157:3000/uploads/fe32eca35d900719410cb7aade96373c.png";
			HttpGet get = new HttpGet(FileGetUrl);
			HttpClient client = new DefaultHttpClient();
			Bitmap pic = null;
			try {
				HttpResponse response = client.execute(get);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					HttpEntity entity = response.getEntity();
					InputStream is = entity.getContent();
					System.out.println("is=" + is.available());
					// Bitmap p = BitmapFactory.decodeStream(is);
					// System.out.println("图片大小为"+p.getByteCount());
					File f = new File(
							Environment.getExternalStorageDirectory(),
							"E-homeland/bbm/audio/");
					if (!f.exists())
						f.mkdirs();
					String path = Utils.getCurrentTime();
					File audiofile = new File(f.getAbsolutePath() + "/" + path
							+ "-Received.wav");
					try {
						FileOutputStream fos = new FileOutputStream(audiofile);
						byte[] receBytes = new byte[1024];
						int len = 0;
						while ((len = is.read(receBytes)) != -1) {
							fos.write(receBytes, 0, len);
						}
						// is.read(receBytes);
						// fos.write(receBytes);
						fos.flush();
						fos.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					System.out.println("声音路径为=" + audiofile.getAbsolutePath());
					MediaPlayer mPlayer = new MediaPlayer();
					myfilehandler.obtainMessage(0).sendToTarget();
					try {
						mPlayer.setDataSource(audiofile.getAbsolutePath());
						mPlayer.prepare();
						mPlayer.start();
					} catch (IOException e) {
						System.out.println("prepare() failed");
						e.printStackTrace();
					}
				} else {
					myfilehandler.obtainMessage(0).sendToTarget();
					System.out.println("图片获取失败");
				}

			} catch (ClientProtocolException e) {
				myfilehandler.obtainMessage(0).sendToTarget();
				e.printStackTrace();
			} catch (IOException e) {
				myfilehandler.obtainMessage(0).sendToTarget();
				// Toast.makeText(getBaseContext(), "图片获取失败",
				// Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}

	};

	// 播放从网上获取到的音频
	public void bbmMainVoicePlay(View v) {
		System.out.println("被按下");
		if (v.getId() == R.id.bbm_item_voice) {
			process.setVisibility(View.VISIBLE);
			System.out.println("内容为" + v.getContentDescription());
			FileGetUrl = IP.ip + ":3000/uploads/" + v.getContentDescription();
			System.out.println("获取文件的路径为" + FileGetUrl);
			new Thread(filerun).start();

		}
	}

	private ProgressBar createProgressBar(Activity activity,
			Drawable customIndeterminateDrawable) {
		// activity根部的ViewGroup，其实是一个FrameLayout
		FrameLayout rootContainer = (FrameLayout) activity
				.findViewById(android.R.id.content);
		// 给progressbar准备一个FrameLayout的LayoutParams
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		// 设置对其方式为：屏幕居中对其
		lp.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;

		ProgressBar progressBar = new ProgressBar(activity);
		progressBar.setVisibility(View.GONE);
		progressBar.setLayoutParams(lp);
		// 自定义小菊花
		if (customIndeterminateDrawable != null) {
			progressBar.setIndeterminateDrawable(customIndeterminateDrawable);
		}
		// 将菊花添加到FrameLayout中
		rootContainer.addView(progressBar);
		return progressBar;
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		bbmConstants.skip++;
		loadData(AutoListView.LOAD);
	}

}

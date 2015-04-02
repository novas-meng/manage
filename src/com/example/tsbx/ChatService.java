package com.example.tsbx;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.EncodingUtils;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.SimpleAdapter;
/**
 * 聊天服务类，处理聊天信息
 * @author michael
 *
 */
public class ChatService {

	XMPPConnection connection = (XMPPConnection) ClientConServer.connection;
	private MessageUnReadListenner unreadmessagelistener = new MessageUnReadListenner();
	//消息Handler，用于和主线程UI进行交互，刷新UI数据
	private Handler messageListenHandler;
	
	//全局消息句柄，用于Notification，刷新UI
	private Handler totalMessageListenHandler;
	
	//chatmanger用于处理当前的聊天
	private ChatManager chatmanger = connection.getChatManager();
	//创建一个聊天
	private Chat chat ;
	
	SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
	
	SimpleDateFormat sdfdate = new SimpleDateFormat("yyyy-MM-dd");
	
	
	public ChatService(){}
	
	public ChatService(Handler _handler){
		this.messageListenHandler = _handler;
	}
	
	public ChatService(Context _context, Handler _handler ){
		this.totalMessageListenHandler = _handler;
		Log.i("messageInfo","Activity From:" + _context.getPackageName());
	}
	
	/**
	 * 构造方法
	 * @param _handler
	 * @param _userJID 用户的JID，这里用作聊天线程
	 */
	/**
	 * 监听所有的消息
	 */
	public void listenningMessage(){
		chatmanger.addChatListener(new ChatManagerListener() {
			
			@Override
			public void chatCreated(Chat chat, boolean createdLocally) {
				if (!createdLocally){
					chat.addMessageListener(unreadmessagelistener);
				}
				
				
				
				
				
			}
		});
	}
	class MessageUnReadListenner implements MessageListener{

		@Override
		public void processMessage(Chat chat, Message msg) {
			
			/*更新UI主线程，刷新聊天列表*/			
			android.os.Message message = android.os.Message.obtain();
			Map<String,Object> map = new HashMap<String, Object>();
			String chatTo = chat.getParticipant().substring(0, chat.getParticipant().indexOf("/"));
			System.out.println("userTo="+chatTo);
			map.put("userTo", chatTo);
			map.put("chatThreadId", chat.getThreadID());
			message.obj = map;

			//TODO 如果这里能得到当前的聊天线程，然后与未读消息线程进行比对，在进行下面的UI主线程交互，就好啦
			//但是问题在于。由于这个监听器是在MainActivity中实例化的，这时候，chat还没有初始化，所以通过chat
			//得不到聊天线程
			totalMessageListenHandler.sendMessage(message);
			
		}
		
	}

}




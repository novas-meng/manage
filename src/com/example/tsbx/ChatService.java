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
 * ��������࣬����������Ϣ
 * @author michael
 *
 */
public class ChatService {

	XMPPConnection connection = (XMPPConnection) ClientConServer.connection;
	private MessageUnReadListenner unreadmessagelistener = new MessageUnReadListenner();
	//��ϢHandler�����ں����߳�UI���н�����ˢ��UI����
	private Handler messageListenHandler;
	
	//ȫ����Ϣ���������Notification��ˢ��UI
	private Handler totalMessageListenHandler;
	
	//chatmanger���ڴ���ǰ������
	private ChatManager chatmanger = connection.getChatManager();
	//����һ������
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
	 * ���췽��
	 * @param _handler
	 * @param _userJID �û���JID���������������߳�
	 */
	/**
	 * �������е���Ϣ
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
			
			/*����UI���̣߳�ˢ�������б�*/			
			android.os.Message message = android.os.Message.obtain();
			Map<String,Object> map = new HashMap<String, Object>();
			String chatTo = chat.getParticipant().substring(0, chat.getParticipant().indexOf("/"));
			System.out.println("userTo="+chatTo);
			map.put("userTo", chatTo);
			map.put("chatThreadId", chat.getThreadID());
			message.obj = map;

			//TODO ��������ܵõ���ǰ�������̣߳�Ȼ����δ����Ϣ�߳̽��бȶԣ��ڽ��������UI���߳̽������ͺ���
			//�����������ڡ������������������MainActivity��ʵ�����ģ���ʱ��chat��û�г�ʼ��������ͨ��chat
			//�ò��������߳�
			totalMessageListenHandler.sendMessage(message);
			
		}
		
	}

}




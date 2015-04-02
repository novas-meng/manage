package com.example.yy;

import java.text.SimpleDateFormat;

import android.os.Handler;

public class Utils {
	public static final int GET_CAMERA =0x11;//�������?
	public static final int SHOW_CAMERA = 0x12;//��ʾ�����ͼ�?
	public static final int SHOW_CAMERA_RESULT = 0x13;//������Ƭ�鿴��Ĳ���?
	public static final int SHOW_ALL_PICTURE = 0x14;//�鿴ͼƬ
	public static final int SHOW_PICTURE_RESULT = 0x15;//�鿴ͼƬ����
	public static final int SHOW_SELECT_PICTURE = 0x16;//�鿴ѡ��ͼƬ
	public static final int SHOW_SELECT_PICTURE_RESULT = 0x17;//�鿴ѡ���ȷ������ͼ�?����
	//�ر������?
	public static int CLOSE_INPUT=0x01;
	//�رռӺŵĸ�������
	public static int CLOSE_MORE_OPERATE=0x02;
	//���ڹر������?
    public static Handler handlerInput;
    //���ڹرձ���Ľ���?
    public static Handler handler;
    public static Handler netStateChangedHandler;
    public static String getNowDate()
    {
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    	String now=sdf.format(System.currentTimeMillis());
    	return now;
    }
}

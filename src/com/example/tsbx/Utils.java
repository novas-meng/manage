package com.example.tsbx;

import java.text.SimpleDateFormat;

import android.os.Handler;

public class Utils {
	public static final int GET_CAMERA =0x11;//启动相机
	public static final int SHOW_CAMERA = 0x12;//显示照相的图片
	public static final int SHOW_CAMERA_RESULT = 0x13;//返回相片查看后的操作
	public static final int SHOW_ALL_PICTURE = 0x14;//查看图片
	public static final int SHOW_PICTURE_RESULT = 0x15;//查看图片返回
	public static final int SHOW_SELECT_PICTURE = 0x16;//查看选择图片
	public static final int SHOW_SELECT_PICTURE_RESULT = 0x17;//查看选择后确定发送图片返回
	//关闭软键盘
	public static int CLOSE_INPUT=0x01;
	//关闭加号的更多操作框
	public static int CLOSE_MORE_OPERATE=0x02;
	//用于关闭软键盘
    public static Handler handlerInput;
    //用于关闭表情的界面
    public static Handler handler;
    public static String getNowDate()
    {
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    	String now=sdf.format(System.currentTimeMillis());
    	return now;
    }
}

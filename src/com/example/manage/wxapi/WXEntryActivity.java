package com.example.manage.wxapi;

import java.util.logging.LogManager;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {  
    // IWXAPI 是第三方app和微信通信的openapi接口  
    private IWXAPI api;  
    
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        api = WXAPIFactory.createWXAPI(this, "这里替换第一步申请的APP_ID", false);  
        api.handleIntent(getIntent(), this);  
        super.onCreate(savedInstanceState);  
    }  
    @Override  
    public void onReq(BaseReq arg0) { 
    
    }   
  
    @Override  
    public void onResp(BaseResp resp) {  
       // LogManager.show(, "resp.errCode:" + resp.errCode + ",resp.errStr:"  
         //       + resp.errStr, 1);  
    	System.out.println("收到req");
    }
	
	
}  
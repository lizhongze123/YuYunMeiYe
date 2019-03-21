package cn.yuyun.yymy.web;

import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import cn.yuyun.yymy.ui.help.DiscoveryDetailActivity;


public class WebJavascriptInterface {

    private Context context;

    public WebJavascriptInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void wechatJump(String url){
        context.startActivity(new Intent(DiscoveryDetailActivity.startDiscoveryDetailActivity(context, url)));
    }

}

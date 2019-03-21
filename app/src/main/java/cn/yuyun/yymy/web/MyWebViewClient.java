package cn.yuyun.yymy.web;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Arrays;
import java.util.List;

import cn.lzz.utils.LogUtils;


public class MyWebViewClient extends WebViewClient {

    public static final List<String> ALLOWED_SCHEMES = Arrays.asList("http", "https");
    public static final String CALL_SCHEME = "tel";


      @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
          return super.shouldOverrideUrlLoading(view, request);
    }

    @Nullable
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        return super.shouldInterceptRequest(view, request);
    }

    protected boolean onLoadNormalUrl(WebView view, Uri uri) {
        LogUtils.v(uri.toString());
        view.loadUrl(uri.toString());
        return true;
    }

    protected void onCallPhone(String phone) {
    }

}

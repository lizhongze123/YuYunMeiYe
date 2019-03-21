package cn.yuyun.yymy.web;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;

import cn.lzz.utils.ToastUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.app.MyApplication;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.AboutBean;
import cn.yuyun.yymy.view.TitleBar;

public class WebViewActivity extends BaseActivity {

    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initViews();
        WebViewSettings.config(webView.getSettings());
        webView.addJavascriptInterface(new WebJavascriptInterface(this),"xw_user");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if (newProgress >= 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        //设置不用系统浏览器打开,直接显示在当前Webview
        webView.setWebViewClient(new MyWebViewClient());
        getAbout();
    }

    private void initViews() {
        titleBar.setTilte("关于我们");
        webView = (WebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(android.R.id.progress);
    }

    private void getAbout() {
        AppModelFactory.model().getAbout(new ProgressSubscriber<DataBean<AboutBean>>(WebViewActivity.this) {
            @Override
            public void onNext(DataBean<AboutBean> result) {
                if(result != null && result.data.url != null){
                    webView.loadUrl(result.data.url);
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), ex.message);
            }

        });

    }

    @Override
    protected void onDestroy() {
        WebViewHelper.destroy(webView);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (WebViewHelper.canGoBack(webView)) {
            webView.goBack();
        } else {
            finish();
        }
    }
}

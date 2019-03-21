package cn.yuyun.yymy.ui.help;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.ProgressSubscriber;
import com.githang.statusbar.StatusBarCompat;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseFragment;
import cn.yuyun.yymy.base.BaseNoTitleFragment;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.ResultDiscover;
import cn.yuyun.yymy.web.MyWebViewClient;
import cn.yuyun.yymy.web.WebJavascriptInterface;
import cn.yuyun.yymy.web.WebViewSettings;

/**
 * @author
 * @desc
 * @date
 */
public class DiscoveryFragment extends BaseNoTitleFragment {

    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected int getTheLayoutId() {
        return R.layout.activity_webview;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onResume() {
        StatusBarCompat.setStatusBarColor(getActivity(), getResources().getColor(R.color.title_bg));
        super.onResume();
    }

    @Override
    protected void initViews(View root) {
        super.initViews(root);
        titleBar.setTilte("发现");
        titleBar.setLineIsVisible(View.GONE);
        webView = (WebView) root.findViewById(R.id.webView);
        progressBar = (ProgressBar) root.findViewById(android.R.id.progress);
        WebViewSettings.config(webView.getSettings());
        webView.addJavascriptInterface(new WebJavascriptInterface(mContext),"App");
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
    }

    public class App{
        @JavascriptInterface
        public void wechatJump(String url){
            startActivity(new Intent(DiscoveryDetailActivity.startDiscoveryDetailActivity(mContext, url)));
        }
    }

    @Override
    protected void initData() {
        super.initData();
        webView.loadUrl("https://m.yuyunrj.com/discovery/index.html");
//        webView.loadUrl("https://mei.yuyunrj.com/discovery");
    }


}

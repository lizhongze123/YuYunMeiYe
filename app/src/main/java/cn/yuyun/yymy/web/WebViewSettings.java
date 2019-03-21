package cn.yuyun.yymy.web;

import android.annotation.SuppressLint;
import android.os.Build;
import android.webkit.WebSettings;

public class WebViewSettings {

    public static void config(WebSettings settings) {
        configJavaScript(settings);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public static void configJavaScript(WebSettings settings) {
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可
        settings.setJavaScriptEnabled(true);
        //支持通过JS打开新窗口
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            //设置可以访问文件
            settings.setAllowFileAccessFromFileURLs(false);
            settings.setAllowUniversalAccessFromFileURLs(false);
        }
    }

    private WebViewSettings() {
        // No instance.
    }
}

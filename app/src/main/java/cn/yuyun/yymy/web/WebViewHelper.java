package cn.yuyun.yymy.web;

import android.view.ViewGroup;
import android.webkit.WebView;

public class WebViewHelper {

    private static long prevGoBackTime;

    public static boolean canGoBack(WebView webView) {
        long current = System.currentTimeMillis();
        if (current < prevGoBackTime + 300) {
            return false;
        }
        prevGoBackTime = current;
        return webView.canGoBack();
    }

    public static void destroy(WebView webView) {
        webView.stopLoading();
        webView.removeAllViews();
        if (webView.getParent() instanceof ViewGroup) {
            ((ViewGroup) webView.getParent()).removeView(webView);
        }
        webView.destroy();
    }

    private WebViewHelper() {
        // No instance.
    }
}

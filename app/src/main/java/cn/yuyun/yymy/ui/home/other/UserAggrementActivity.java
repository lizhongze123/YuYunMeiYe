package cn.yuyun.yymy.ui.home.other;

import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.NoneProgressSubscriber;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.ResultUserAggrement;

/**
 * @author
 * @desc
 * @date
 */

public class UserAggrementActivity extends BaseActivity {

    private WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_notice_detail);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
    }

    private void initViews() {
        titleBar.setTilte("用户协议");
        webView = (WebView) findViewById(R.id.webView);
        getUserAggrement();
    }


    private void getUserAggrement() {
        AppModelFactory.model().getUserAggrement(new NoneProgressSubscriber<DataBean<ResultUserAggrement>>(mContext) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(DataBean<ResultUserAggrement> o) {
                if(null != o.data){
                    if(!TextUtils.isEmpty(o.data.contents)){
                        webView.loadDataWithBaseURL(null, o.data.contents, "text/html", "UTF-8", null);
                    }
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
            }
        });
    }

}

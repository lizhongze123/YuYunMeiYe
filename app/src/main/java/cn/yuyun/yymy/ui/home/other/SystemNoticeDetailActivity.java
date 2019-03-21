package cn.yuyun.yymy.ui.home.other;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.webkit.WebView;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.PageInfo;
import com.example.http.Presenter2;
import com.example.http.ProgressSubscriber;
import com.zzhoujay.richtext.RichText;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.ResultSystemNotice;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc 系统详细消息
 * @date
 */

public class SystemNoticeDetailActivity extends BaseActivity {

    private WebView webView;
    private ResultSystemNotice bean;

    public static Intent startSystemNoticeDetailActivity(Context context, ResultSystemNotice bean) {
        return new Intent(context, SystemNoticeDetailActivity.class).putExtra(EXTRA_DATA, bean);
    }

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
        titleBar.setTilte("系统消息");
        webView = (WebView) findViewById(R.id.webView);
        bean = (ResultSystemNotice) getIntent().getSerializableExtra(EXTRA_DATA);
        String info_txt = bean.content;
        webView.loadDataWithBaseURL(null, info_txt, "text/html", "UTF-8", null);
        getDetail();
    }

    private void getDetail() {
        AppModelFactory.model().getSystemNoticeDetail(bean.systemMsgId, new ProgressSubscriber<DataBean<ResultSystemNotice>>(mContext) {
            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean<ResultSystemNotice> result) {

            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.getMessage());
            }
        });
    }

}

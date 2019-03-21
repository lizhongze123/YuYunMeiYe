package cn.yuyun.yymy.ui.home.analysis;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.http.PageInfo;
import com.example.http.Presenter;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lzz.utils.DateTimeUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestAnalysisTotalWithGood;
import cn.yuyun.yymy.http.result.ResultAnalysisTotal;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author lzz
 * @desc 三级品项分析Activity
 * @date 2018/1/25
 */

public class AnalysisActivityDetail extends BaseActivity {

    private Presenter<ResultAnalysisTotal> presenter;
    private EasyRefreshLayout easyRefreshLayout;
    private RequestAnalysisTotalWithGood requestBean;
    private RecyclerView rvAnalysis;
    private AnalysisDetailAdapter analysisAdapter;

    private TimePickerView timePickerView;
    private int timeType = 0;

    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_end)
    TextView tvEnd;

    public static Intent startAnalysisActivityDetail (Context context, RequestAnalysisTotalWithGood requestBean){
        return new Intent(context, AnalysisActivityDetail.class).putExtra(EXTRA_DATA, requestBean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_detail);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        requestBean = (RequestAnalysisTotalWithGood) getIntent().getSerializableExtra(EXTRA_DATA);
        initViews();
        initData();
        configurePresenter();
    }


    private void initData() {
        requestBean.start = DateTimeUtils.getTimesMonthMorning();
        requestBean.end = DateTimeUtils.getNowTimeStamp();
        tvStart.setText(DateTimeUtils.getDateTimeText(DateTimeUtils.getTimesMonthMorning(), DateTimeUtils.FORMAT_DATE_UI));
        tvEnd.setText(DateTimeUtils.getNowTime());
        easyRefreshLayout.autoRefresh();
    }

    private void initViews() {
        titleBar.setTilte("品项分析表");
        rvAnalysis = (RecyclerView) findViewById(R.id.rv_analysis);
        rvAnalysis.setLayoutManager(new LinearLayoutManager(this));
        rvAnalysis.setHasFixedSize(true);
        analysisAdapter = new AnalysisDetailAdapter(this);
        rvAnalysis.setAdapter(analysisAdapter);
        easyRefreshLayout = (EasyRefreshLayout) findViewById(R.id.easylayout);
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(this));
        easyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                presenter.loadData(false);
            }

            @Override
            public void onRefreshing() {
                presenter.loadData(true);
            }
        });
        timePickerView = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (timeType == 0) {
                    tvStart.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATE_UI));
                    requestBean.start = date.getTime() / 1000;
                    easyRefreshLayout.autoRefresh();
                } else {
                    tvEnd.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATE_UI));
                    requestBean.end = date.getTime() / 1000;
                    easyRefreshLayout.autoRefresh();
                }
            }
        }).build();
    }

    private void configurePresenter() {
        presenter = new Presenter<>();
        presenter.setLoadDataListener(new Presenter.OnPresenterLoadListener<ResultAnalysisTotal>() {

            @Override
            public void onSuccess(final List<ResultAnalysisTotal> result, int total, final boolean isRefresh) {
                if (result != null) {
                    if (isRefresh){
                        analysisAdapter.notifyDataSetChanged(result);
                    }else{
                        analysisAdapter.addAll(result);
                    }
                }
            }

            @Override
            public void onFailed(boolean isRefresh) {
                showToast("加载失败，请稍候重试");
            }

            @Override
            public void onCompleted(boolean isRefresh) {
                easyRefreshLayout.refreshComplete();
                easyRefreshLayout.closeLoadView();
            }

            @Override
            public void onEmptyData() {
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<PageInfo<ResultAnalysisTotal>> subscriber, int pageIndex, int pageSize) {
                if(null != requestBean){
                    AppModelFactory.model().getAnalysisTotalWithGood(pageIndex, pageSize, requestBean, subscriber);
                }
            }
        });
    }

    @OnClick(R.id.rl_start)
    public void start(View v) {
        timeType = 0;
        timePickerView.show();
    }

    @OnClick(R.id.rl_end)
    public void end(View v) {
        timeType = 1;
        timePickerView.show();
    }

}

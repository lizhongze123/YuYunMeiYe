package cn.yuyun.yymy.ui.store;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.http.DataBean;
import com.example.http.PageInfo;
import com.example.http.Presenter2;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.LogUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.bean.RecordType;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestHandmakeList;
import cn.yuyun.yymy.http.request.RequestRecord;
import cn.yuyun.yymy.http.result.RecordBean;
import cn.yuyun.yymy.http.result.ResultHandmake;
import cn.yuyun.yymy.http.result.StaffBean;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.me.HandmakeDetailActivity;
import cn.yuyun.yymy.ui.me.RecordServiceDetailActivity;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;

/**
 * @author
 * @desc 手工费列表
 * @date
 */

public class ManualFeeListActivity extends BaseActivity {

    private Presenter2<ResultHandmake> presenter;

    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private ManualAdapter mAdapter;
    private StaffBean staffBean;
    private StoreBean storeBean;

    private int timeType = 0;
    private TimePickerView timePickerView;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.empty)
    EmptyLayout emptyLayout;

    private RequestHandmakeList requestBean = new RequestHandmakeList();

    public static Intent startManualFeeListActivity(Context context, StaffBean staffBean, StoreBean storeBean) {
        return new Intent(context, ManualFeeListActivity.class).putExtra(EXTRA_DATA, staffBean).putExtra(EXTRA_DATA2, storeBean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        configurePresenter();
        easyRefreshLayout.autoRefresh();
    }

    private void initViews() {
        staffBean = (StaffBean) getIntent().getSerializableExtra(EXTRA_DATA);
        storeBean = (StoreBean) getIntent().getSerializableExtra(EXTRA_DATA2);
        titleBar.setTilte("手工费");

        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ManualAdapter(this);
        mAdapter.setOnItemClickListener(new ManualAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultHandmake bean, int position) {
                Intent intent = new Intent(mContext, HandmakeDetailActivity.class);
                intent.putExtra(EXTRA_DATA, bean);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(mAdapter);
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
                    requestBean.start = date.getTime() / 1000;
                } else {
                    requestBean.end = date.getTime() / 1000;
                }
                if(requestBean.end - requestBean.start < 0){
                    showTextDialog("结束时间不能小于开始时间", false);
                    return;
                }
                if(!DateTimeUtils.minusDateForDaysCount(requestBean.start, requestBean.end, 92)){
                    showTextDialog("不支持超过三个月的查询", false);
                    return;
                }
                if (timeType == 0) {
                    tvStart.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATE_UI));
                }else{
                    tvEnd.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATE_UI));
                }
                presenter.loadData(true);
            }
        }).build();
        tvStart.setText(DateTimeUtils.getDateTimeText(DateTimeUtils.getTimesMonthMorning(), DateTimeUtils.FORMAT_DATE_UI));
        tvEnd.setText(DateTimeUtils.getNowTime());
        requestBean.start = DateTimeUtils.getTimesMonthMorning();
        requestBean.end = DateTimeUtils.getNowTimeStamp();
        requestBean.staffId = staffBean.staffId;
        requestBean.groupId = staffBean.groupId;
    }

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultHandmake>(){

            @Override
            public void onSuccess(List<ResultHandmake> result, int total, boolean isRefresh) {
                emptyLayout.setVisibility(View.GONE);
                easyRefreshLayout.setVisibility(View.VISIBLE);
                if(result != null){
                    if(isRefresh){
                        mAdapter.notifyDataSetChanged(result);
                    }else{
                        mAdapter.addAll(result);
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
                emptyLayout.setVisibility(View.VISIBLE);
                emptyLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
                easyRefreshLayout.setVisibility(View.GONE);
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultHandmake>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getHandmakeList(requestBean, pageIndex, pageSize, subscriber);
            }
        });
    }

    private void showDateTimePickerDialog() {
        timePickerView.show();
    }

    @OnClick(R.id.rl_start)
    public void start(View v) {
        timeType = 0;
        showDateTimePickerDialog();
    }

    @OnClick(R.id.rl_end)
    public void end(View v) {
        timeType = 1;
        showDateTimePickerDialog();
    }

}

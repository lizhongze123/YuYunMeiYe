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
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.bean.ConsumeType;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestPersonTime;
import cn.yuyun.yymy.http.result.PersonTimeListBean;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.me.BillDetailActivity;
import cn.yuyun.yymy.ui.me.adapter.PersonCountAdapter;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;

/**
 * @author
 * @desc 人次Activity
 * @date
 */

public class PersonTimeActivity extends BaseActivity{

    private Presenter2<PersonTimeListBean> presenter;
    private PersonCountAdapter countAdapter;
    private RequestPersonTime requestBean;
    private String staff_id;
    private StoreBean storeBean;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.empty)
    EmptyLayout emptyLayout;
    private int timeType = 0;
    private TimePickerView timePickerView;

    public static Intent startPersonTimeActivity(Context context, String staffId, StoreBean storeBean) {
        return new Intent(context, PersonTimeActivity.class).putExtra(EXTRA_DATA, staffId).putExtra(EXTRA_DATA2, storeBean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_count);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        staff_id = getIntent().getStringExtra(EXTRA_DATA);
        storeBean = (StoreBean) getIntent().getSerializableExtra(EXTRA_DATA2);
        initViews();
        initData();
        configurePresenter();
    }

    private void initData() {
        requestBean = new RequestPersonTime();
        requestBean.start = DateTimeUtils.getTimesMonthMorning();
        requestBean.end = DateTimeUtils.getNowTimeStamp();
        requestBean.staffId = staff_id;
        requestBean.group_id = storeBean.group_id;
    }

    private void initViews() {
        titleBar.setTilte("人次");
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        countAdapter = new PersonCountAdapter(this);
        recyclerView.setAdapter(countAdapter);
        countAdapter.setOnItemClickListener(new PersonCountAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(PersonTimeListBean bean, int position) {
                if (bean.type.equals(ConsumeType.VALUE_RECHARGE.desc)) {
                    startActivity(BillStorevalueDetailActivity.startBillStorevalueDetailActivity(mContext, bean, storeBean.group_id));
                }else{
                    startActivity(BillDetailActivity.startBillDetailActivity(mContext, bean, storeBean.group_id));
                }
            }
        });
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(this));
        easyRefreshLayout.autoRefresh();
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
                } else {
                    tvEnd.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATE_UI));
                    requestBean.end = date.getTime() / 1000;
                }
                if(requestBean.end - requestBean.start < 0){
                    showTextDialog("结束时间不能小于开始时间", false);
                    return;
                }
                presenter.loadData(true);
            }
        }).build();
        tvStart.setText(DateTimeUtils.getDateTimeText(DateTimeUtils.getTimesMonthMorning(), DateTimeUtils.FORMAT_DATE_UI));
        tvEnd.setText(DateTimeUtils.getNowTime());
    }

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<PersonTimeListBean>(){

            @Override
            public void onSuccess(List<PersonTimeListBean> result, int total, boolean isRefresh) {
                emptyLayout.setVisibility(View.GONE);
                easyRefreshLayout.setVisibility(View.VISIBLE);
                if(result != null){
                    if(isRefresh){
                        countAdapter.notifyDataSetChanged(result);
                    }else{
                        countAdapter.addAll(result);
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
            public void onLoad(Subscriber<DataBean<PageInfo<PersonTimeListBean>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getPersonTimeList(requestBean, pageIndex, pageSize, subscriber);
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

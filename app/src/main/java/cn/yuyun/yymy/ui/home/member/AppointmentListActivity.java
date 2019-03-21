package cn.yuyun.yymy.ui.home.member;

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
import com.example.http.DataBean;
import com.example.http.PageInfo;
import com.example.http.Presenter2;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lzz.utils.DateTimeUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestAppointmentList;
import cn.yuyun.yymy.http.result.ResultBook;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.store.member.MemberAppointmentDetailActivity;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author
 * @desc 会员详情的预约记录
 * @date
 */

public class AppointmentListActivity extends BaseActivity {

    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;
    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;

    private int timeType = 0;
    private TimePickerView timePickerView;

    private Presenter2<ResultBook> presenter;

    private AppointmentAdapter mAdapter;
    private ResultMemberBean memberBean;
    private StoreBean storeBean;

    private final int REQUEST_CODE = 1111;

    private int type;

    private RequestAppointmentList requestBean = new RequestAppointmentList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_communication_callback);
        EvenManager.register(this);

    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        configurePresenter();
        presenter.loadData(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if(notifyEvent.tag == NotifyEvent.RREFRESH){
            presenter.loadData(true);
        }
    }

    public static Intent startAppointmentListActivity(Context context, ResultMemberBean bean, StoreBean storeBean) {
        return new Intent(context, AppointmentListActivity.class).putExtra(EXTRA_DATA, bean).putExtra(EXTRA_DATA2, storeBean);
    }

    private void initViews() {
        titleBar.setTilte("预约记录");
        type = getIntent().getIntExtra(EXTRA_TYPE, 0);
        storeBean = (StoreBean) getIntent().getSerializableExtra(EXTRA_DATA2);
        memberBean = (ResultMemberBean) getIntent().getSerializableExtra(EXTRA_DATA);
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(mContext));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AppointmentAdapter(this);
        mAdapter.setOnItemClickListener(new AppointmentAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultBook bean, int position) {
                startActivity(MemberAppointmentDetailActivity.startMemberAppointmentDetailActivity(mContext, bean));
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
                    tvStart.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATE_UI));
                    requestBean.start_date = date.getTime() / 1000;
                } else {
                    tvEnd.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATE_UI));
                    requestBean.end_date = date.getTime() / 1000;
                }
                if(requestBean.end_date - requestBean.start_date < 0){
                    showTextDialog("结束时间不能小于开始时间", false);
                    return;
                }
                presenter.loadData(true);
            }
        }).build();
        tvStart.setText(DateTimeUtils.getDateTimeText(DateTimeUtils.getTimesMonthMorning(), DateTimeUtils.FORMAT_DATE_UI));
        tvEnd.setText(DateTimeUtils.getNowTime());
        requestBean.group_id = storeBean.group_id;
        requestBean.sp_id = storeBean.spId;
        requestBean.member_id = memberBean.memberId;
        requestBean.start_date = DateTimeUtils.getTimesMonthMorning();
        requestBean.end_date = DateTimeUtils.getNowTimeStamp();
    }

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultBook>() {

            @Override
            public void onSuccess(List<ResultBook> result, int total, boolean isRefresh) {
                emptyLayout.setVisibility(View.GONE);
                easyRefreshLayout.setVisibility(View.VISIBLE);
                if (isRefresh) {
                    mAdapter.notifyDataSetChanged(result);
                } else {
                    mAdapter.addAll(result);
                }

            }

            @Override
            public void onFailed(boolean isRefresh) {
                emptyLayout.setVisibility(View.VISIBLE);
                emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                easyRefreshLayout.setVisibility(View.GONE);
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
            public void onLoad(Subscriber<DataBean<PageInfo<ResultBook>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getStoreBookList(requestBean, pageIndex, pageSize, subscriber);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            easyRefreshLayout.autoRefresh();
        }
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

package cn.yuyun.yymy.ui.store.staff;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
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
import cn.yuyun.yymy.base.BaseNoTitleFragment;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestAppointmentList;
import cn.yuyun.yymy.http.result.ResultBook;
import cn.yuyun.yymy.http.result.StaffBean;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.store.book.AppointmentBookDetailActivity;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;

/**
 * @author
 * @desc 预约记录-员工
 * @date
 */

public class AppointmentListForStaffFragment extends BaseNoTitleFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;
    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.rl)
    RelativeLayout rl;
    private int timeType = 0;
    private TimePickerView timePickerView;
    private AppointmentForStaffAdapter mAdapter;
    private StaffBean staffBean;
    private StoreBean storeBean;

    private RequestAppointmentList requestBean = new RequestAppointmentList();

    private Presenter2<ResultBook> presenter;

    @Override
    protected int getTheLayoutId() {
        return R.layout.activity_record_communication_callback;
    }

    @Override
    protected void onBindViewBefore(View root) {
        super.onBindViewBefore(root);
        mContext = getContext();
    }

    public static AppointmentListForStaffFragment newInstance(StaffBean staffBean, StoreBean storeBean) {
        AppointmentListForStaffFragment fragment = new AppointmentListForStaffFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATA, staffBean);
        args.putSerializable(EXTRA_DATA2, storeBean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        if (getArguments() != null) {
            staffBean = (StaffBean) getArguments().getSerializable(EXTRA_DATA);
            storeBean = (StoreBean) getArguments().getSerializable(EXTRA_DATA2);
        }
    }

    @Override
    protected void initViews(View root) {
        super.initViews(root);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setHasFixedSize(true);
        mAdapter = new AppointmentForStaffAdapter(mContext);
        recyclerView.setAdapter(mAdapter);
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(mContext));
        easyRefreshLayout.setLoadMoreModel(LoadModel.NONE);
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
       mAdapter.setOnItemClickListener(new AppointmentForStaffAdapter.OnMyItemClickListener() {
           @Override
           public void onItemClick(View view, ResultBook bean, int position) {
               startActivity(new Intent(AppointmentBookDetailActivity.startAppointmentBookDetailActivity(mContext, bean)));
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
    }

    @Override
    protected void initData() {
        super.initData();
        configurePresenter();
        requestBean.group_id = storeBean.group_id;
        requestBean.staff_id = staffBean.staffId;
        requestBean.sp_id = storeBean.spId;
        requestBean.start_date = DateTimeUtils.getTimesMonthMorning();
        requestBean.end_date = DateTimeUtils.getNowTimeStamp();
        presenter.loadData(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if (notifyEvent.tag == NotifyEvent.RREFRESH) {
            presenter.loadData(true);
        }
    }

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultBook>() {

            @Override
            public void onSuccess(List<ResultBook> result, int total, boolean isRefresh) {
                rl.setVisibility(View.GONE);
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
                rl.setVisibility(View.VISIBLE);
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
                rl.setVisibility(View.VISIBLE);
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

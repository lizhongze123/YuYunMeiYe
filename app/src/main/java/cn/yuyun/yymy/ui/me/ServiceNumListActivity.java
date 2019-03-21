package cn.yuyun.yymy.ui.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lzz.utils.DateTimeUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.bean.RecordType;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestRecord;
import cn.yuyun.yymy.http.request.RequestServiceNum;
import cn.yuyun.yymy.http.result.RecordBean;
import cn.yuyun.yymy.http.result.ResultServiceNum;
import cn.yuyun.yymy.http.result.StaffBean;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.me.adapter.RecordAdapter;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author
 * @desc
 * @date
 */

public class ServiceNumListActivity extends BaseActivity {

    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.rl_start)
    RelativeLayout rlStart;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.rl_end)
    RelativeLayout rlEnd;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;
    @BindView(R.id.empty)
    EmptyLayout emptyLayout;

    private int timeType = 0;
    private TimePickerView timePickerView;

    private Presenter2<ResultServiceNum> presenter;
    private StaffBean staffBean;
    private StoreBean storeBean;
    private ServiceNumListAdapter mAdapter;

    private RequestServiceNum requestBean = new RequestServiceNum();

    public static Intent startServiceNumListActivity(Context context, StaffBean staffBean, StoreBean storeBean) {
        return new Intent(context, ServiceNumListActivity.class).putExtra(EXTRA_TYPE, RecordType.BEFORE).putExtra(EXTRA_DATA, staffBean).putExtra(EXTRA_DATA2, storeBean);
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
        titleBar.setTilte("项目数");
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(this));
        recyclerView.setLayoutManager( new LinearLayoutManager(this));

        mAdapter = new ServiceNumListAdapter(this);
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
                    requestBean.start_date = date.getTime() / 1000;
                } else {
                    requestBean.end_date = date.getTime() / 1000;
                }
                if(requestBean.end_date - requestBean.start_date < 0){
                    showTextDialog("结束时间不能小于开始时间", false);
                    return;
                }
                if(!DateTimeUtils.minusDateForDaysCount(requestBean.start_date, requestBean.end_date, 92)){
                    showTextDialog("不支持超过三个月的查询", false);
                    return;
                }
                if (timeType == 0) {
                    tvStart.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATETIME_YEAR_MONTH_DAY));
                }else{
                    tvEnd.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATETIME_YEAR_MONTH_DAY));
                }
                presenter.loadData(true);
            }
        }).build();
        tvStart.setText(DateTimeUtils.getDateTimeText(DateTimeUtils.getTimesMonthMorning(), DateTimeUtils.FORMAT_DATETIME_YEAR_MONTH_DAY));
        tvEnd.setText(DateTimeUtils.getDateTimeText(DateTimeUtils.getNowTimeStamp(), DateTimeUtils.FORMAT_DATETIME_YEAR_MONTH_DAY));
        requestBean.start_date = DateTimeUtils.getTimesMonthMorning();
        requestBean.end_date = DateTimeUtils.getNowTimeStamp();
        requestBean.group_id = storeBean.group_id;
        if (null != staffBean) {
            requestBean.staff_id = staffBean.staffId;
        } else {
            requestBean.staff_id = LoginInfoPrefs.getInstance(mContext).getStaffId();
        }
        if(null != storeBean && !TextUtils.isEmpty(storeBean.spId)){
            List<String>list = new ArrayList<>();
            list.add(storeBean.spId);
            requestBean.sp_id_list = list;
        }else{
            requestBean.sp_id_list = new ArrayList<>();
        }
    }

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultServiceNum>() {

            @Override
            public void onSuccess(List<ResultServiceNum> result, int total, boolean isRefresh) {
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
            public void onLoad(Subscriber<DataBean<PageInfo<ResultServiceNum>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getServiceNumList(requestBean, pageIndex, pageSize, subscriber);
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

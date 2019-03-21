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
import cn.yuyun.yymy.http.result.RecordBean;
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

public class RecordListActivity extends BaseActivity {

    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.rl_start)
    RelativeLayout rlStart;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.rl_end)
    RelativeLayout rlEnd;
    @BindView(R.id.ll_filter)
    LinearLayout llFilter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;
    @BindView(R.id.empty)
    EmptyLayout emptyLayout;

    private int timeType = 0;
    private TimePickerView timePickerView;

    private Presenter2<RecordBean> presenter;
    public RecordType type;
    private StaffBean staffBean;
    private StoreBean storeBean;
    private LinearLayoutManager mLayoutManager;
    private RecordAdapter mAdapter;

    private RequestRecord requestBean = new RequestRecord();

    public static Intent startRecordBeforeActivity(Context context, StaffBean staffBean, StoreBean storeBean) {
        return new Intent(context, RecordListActivity.class).putExtra(EXTRA_TYPE, RecordType.BEFORE).putExtra(EXTRA_DATA, staffBean).putExtra(EXTRA_DATA2, storeBean);
    }

    public static Intent startRecordSaleActivity(Context context, StaffBean staffBean, StoreBean storeBean) {
        return new Intent(context, RecordListActivity.class).putExtra(EXTRA_TYPE, RecordType.SALE).putExtra(EXTRA_DATA, staffBean).putExtra(EXTRA_DATA2, storeBean);
    }

    public static Intent startRecordServiceActivity(Context context, StaffBean staffBean, StoreBean storeBean) {
        return new Intent(context, RecordListActivity.class).putExtra(EXTRA_TYPE, RecordType.SERVICE).putExtra(EXTRA_DATA, staffBean).putExtra(EXTRA_DATA2, storeBean);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if (notifyEvent.tag == NotifyEvent.RREFRESH) {
            presenter.loadData(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        EvenManager.register(this);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        configurePresenter();
        easyRefreshLayout.autoRefresh();
    }

    private void initViews() {
        type = (RecordType) getIntent().getSerializableExtra(EXTRA_TYPE);
        staffBean = (StaffBean) getIntent().getSerializableExtra(EXTRA_DATA);
        storeBean = (StoreBean) getIntent().getSerializableExtra(EXTRA_DATA2);
        titleBar.setTilte(type.toString());

        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(this));

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        mAdapter = new RecordAdapter(this, type);
        mAdapter.setOnItemClickListener(new RecordAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, RecordBean bean, int position) {
                if (type == RecordType.BEFORE) {
                    startActivity(RecordDetailActivity.startRecordBeforeActivity(RecordListActivity.this, bean));
                } else if (type == RecordType.SALE) {
                    startActivity(RecordDetailActivity.startRecordSaleActivity(RecordListActivity.this, bean));
                } else {
                    Intent intent = new Intent(RecordListActivity.this, RecordServiceDetailActivity.class);
                    intent.putExtra(EXTRA_DATA, bean.obj);
                    startActivity(intent);
                }

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
        requestBean.start = DateTimeUtils.getTimesMonthMorning();
        requestBean.end = DateTimeUtils.getNowTimeStamp();
        requestBean.recordType = type;
        if (null != staffBean) {
            requestBean.staffId = staffBean.staffId;
        } else {
            requestBean.staffId = LoginInfoPrefs.getInstance(mContext).getStaffId();
        }
    }

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<RecordBean>() {

            @Override
            public void onSuccess(List<RecordBean> result, int total, boolean isRefresh) {
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
//                showEmpty();
                emptyLayout.setVisibility(View.VISIBLE);
                emptyLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
                easyRefreshLayout.setVisibility(View.GONE);
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<RecordBean>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getRecordList(requestBean, LoginInfoPrefs.getInstance(mContext).getGroupId(), pageIndex, pageSize, subscriber);
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

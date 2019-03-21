package cn.yuyun.yymy.ui.home.member;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import cn.yuyun.yymy.constan.Constans;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.ResultCommunication;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.ui.home.AddCommunicationActivity;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;

/**
 * @author
 * @desc
 * @date
 */

public class CommunicationListActivity extends BaseActivity {

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
    @BindView(R.id.tv_reset)
    TextView tvReset;

    private long start;
    private long end;

    private int timeType = 0;
    private TimePickerView timePickerView;

    private Presenter2<ResultCommunication> presenter;
    private CommunicationAdapter mAdapter;
    private ResultMemberBean memberBean;
    private StoreBean storeBean;
    private final int REQUEST_CODE = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EvenManager.register(this);
        setContentView(R.layout.activity_record_communication_callback);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    private void initDatas() {
        configurePresenter();
        presenter.loadData(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if(notifyEvent.tag == NotifyEvent.RREFRESH_EDIT_COMMUNICATION){
            presenter.loadData(true);
        }
    }

    public static Intent startCommunicationRecordListActivity(Context context, ResultMemberBean bean, StoreBean storeBean) {
        return new Intent(context, CommunicationListActivity.class).putExtra(EXTRA_DATA, bean).putExtra(EXTRA_DATA2, storeBean);
    }

    private void initViews() {
        memberBean = (ResultMemberBean) getIntent().getSerializableExtra(EXTRA_DATA);
        storeBean = (StoreBean) getIntent().getSerializableExtra(EXTRA_DATA2);
        tvReset.setVisibility(View.VISIBLE);
        if(null != memberBean){
            titleBar.setTilte(memberBean.memberName + "的沟通记录");
        }
        if(Constans.isStoreLoginer){
            titleBar.setRightIcon(R.drawable.icon_add_two);
            titleBar.setRightOnClicker(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(AddCommunicationActivity.startFromCommunication(mContext, memberBean), REQUEST_CODE);
                }
            });
        }
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new CommunicationAdapter(this);
        mAdapter.setOnItemClickListener(new CommunicationAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultCommunication bean, int position) {
                startActivity(CommunicationDetailActivity.startCommunicationDetailActivity(mContext, bean));
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
                    tvStart.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATETIME_YEAR_MONTH_DAY));
                    start = date.getTime() / 1000;
                } else {
                    tvEnd.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATETIME_YEAR_MONTH_DAY));
                    end = date.getTime() / 1000;
                }
                if(end - start < 0){
                    showTextDialog("结束时间不能小于开始时间", false);
                    return;
                }
                if(start != 0){
                    tvReset.setEnabled(true);
                    tvReset.setTextColor(Color.parseColor("#3f3f3f"));
                }
                presenter.loadData(true);
            }
        }).build();
        start = 0;
        end = DateTimeUtils.getNowTimeStamp();
    }

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultCommunication>() {

            @Override
            public void onSuccess(List<ResultCommunication> result, int total, boolean isRefresh) {
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
            public void onLoad(Subscriber<DataBean<PageInfo<ResultCommunication>>> subscriber, int pageIndex, int pageSize) {
                if(null != memberBean.memberId && null != storeBean.group_id){
                    AppModelFactory.model().getCommunicationListMember(memberBean.memberId, storeBean.group_id, start, end, pageIndex, pageSize, subscriber);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            presenter.loadData(true);
        }
    }

    private void showDateTimePickerDialog() {
        timePickerView.show();
    }

    @OnClick(R.id.tv_reset)
    public void reset(View v) {
        start = 0;
        tvStart.setText("开始时间");
        tvEnd.setText("结束时间");
        tvReset.setTextColor(Color.parseColor("#CCCCCC"));
        tvReset.setEnabled(false);
        presenter.loadData(true);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EvenManager.unregister(this);
    }
}

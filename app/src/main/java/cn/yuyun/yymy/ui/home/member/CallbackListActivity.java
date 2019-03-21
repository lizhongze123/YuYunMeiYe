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
import cn.yuyun.yymy.http.result.ResultCallback;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.ui.home.AddCommunicationActivity;
import cn.yuyun.yymy.view.EmptyLayout;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc 回访记录
 * @date
 */

public class CallbackListActivity extends BaseActivity {

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

    private Presenter2<ResultCallback> presenter;

    private CallbackAdapter mAdapter;
    private ResultMemberBean memberBean;

    private final int REQUEST_CODE = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_communication_callback);
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
        if(notifyEvent.tag == NotifyEvent.RREFRESH_EDIT_COMMUNICATION){
            presenter.loadData(true);
        }
    }

    public static Intent startCallbackListActivity(Context context, ResultMemberBean bean) {
        return new Intent(context, CallbackListActivity.class).putExtra(EXTRA_DATA, bean);
    }

    private void initViews() {
        titleBar.setTilte("回访记录");
        tvReset.setVisibility(View.VISIBLE);
        if(Constans.isStoreLoginer){
            titleBar.setRightIcon(R.drawable.icon_add_two);
            titleBar.setRightOnClicker(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(AddCommunicationActivity.startFromCallback(mContext, memberBean), REQUEST_CODE);
                }
            });
        }
        memberBean = (ResultMemberBean) getIntent().getSerializableExtra(EXTRA_DATA);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new CallbackAdapter(this);
        mAdapter.setOnItemClickListener(new CallbackAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultCallback bean, int position) {
                startActivity(CommunicationDetailActivity.startCallbackDetailActivity(mContext, bean));
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
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultCallback>(){

            @Override
            public void onSuccess(List<ResultCallback> result, int total, boolean isRefresh) {
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
            public void onLoad(Subscriber<DataBean<PageInfo<ResultCallback>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getCallbackListMember(memberBean.memberId, start, end,pageIndex, pageSize, subscriber);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE){
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

}

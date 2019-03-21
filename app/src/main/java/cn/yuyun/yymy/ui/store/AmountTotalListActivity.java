package cn.yuyun.yymy.ui.store;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.NoneProgressSubscriber;
import com.example.http.PageInfo;
import com.example.http.Presenter2;
import com.example.http.ProgressSubscriber;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestResults;
import cn.yuyun.yymy.http.result.ResultGroup;
import cn.yuyun.yymy.http.result.ResultTotalAmount;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.ui.store.report.ResultTotalAmountOut;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;


/**
 * @author
 * @desc 实收金额
 * @date
 */

public class AmountTotalListActivity extends BaseActivity {

    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    @BindView(R.id.tv_amount)
    TextView tvAmount;

    private int timeType = 0;
    private TimePickerView timePickerView;

    private int pageIndex = 1;
    private int pageSize = 12;
    private int totalPage;
    private boolean isRefresh = true;

    public String type;
    private ResultsAdapter mAdapter;
    private RequestResults requestBean = new RequestResults();

    private List<String> spIdList;
    private String groupId;

    public static Intent startAmountTotalListActivity(Context context, List<String> spIdList, String groupId) {
        return new Intent(context, AmountTotalListActivity.class).putExtra(EXTRA_DATA, (Serializable) spIdList).putExtra(EXTRA_DATA2, groupId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        easyRefreshLayout.autoRefresh();
    }

    private void initViews() {
        titleBar.setTilte("实收金额");
        type = getIntent().getStringExtra(EXTRA_TYPE);
        groupId = getIntent().getStringExtra(EXTRA_DATA2);
        spIdList = (List<String>) getIntent().getSerializableExtra(EXTRA_DATA);
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mAdapter = new ResultsAdapter(this, 1);
        recyclerView.setAdapter(mAdapter);

        easyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                isRefresh = false;
                if (pageIndex > totalPage) {
                    showToast("没有更多数据了");
                    easyRefreshLayout.loadMoreComplete();
                }else{
                    getAmount();
                }
            }

            @Override
            public void onRefreshing() {
                isRefresh = true;
                pageIndex = 1;
                getAmount();
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
                easyRefreshLayout.autoRefresh();
            }
        }).build();
        tvStart.setText(DateTimeUtils.getDateTimeText(DateTimeUtils.getTimesMonthMorning(), DateTimeUtils.FORMAT_DATE_UI));
        tvEnd.setText(DateTimeUtils.getNowTime());
        requestBean.start_date = DateTimeUtils.getTimesMonthMorning();
        requestBean.end_date = DateTimeUtils.getNowTimeStamp();
        requestBean.group_id = groupId;
        requestBean.sp_id_list = spIdList;
    }


    private void getAmount() {
        AppModelFactory.model().getAmountTotalList(requestBean, pageIndex,pageSize, new ProgressSubscriber<DataBean<ResultTotalAmountOut>>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
                easyRefreshLayout.refreshComplete();
                easyRefreshLayout.loadMoreComplete();
                pageIndex = pageIndex + 1;
            }

            @Override
            public void onNext(DataBean<ResultTotalAmountOut> result) {
                if(null != result.data){
                    tvAmount.setText("总计:" + StringFormatUtils.formatTwoDecimal(result.data.amount));
                    if(null != result.data.detail){
                        totalPage = result.data.detail.pages;
                        if(result.data.detail.dataLsit.size() == 0){
                            showToast("没有数据");
                        }else {
                            if(isRefresh){
                                mAdapter.notifyDataSetChanged(result.data.detail.dataLsit);
                            }else{
                                mAdapter.addAll(result.data.detail.dataLsit);
                            }
                        }

                    }
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.message);
                easyRefreshLayout.refreshComplete();
                easyRefreshLayout.loadMoreComplete();
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

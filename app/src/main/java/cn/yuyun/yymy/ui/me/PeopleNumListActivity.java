package cn.yuyun.yymy.ui.me;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lzz.utils.DateTimeUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestPersonNumber;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.me.entity.PeopleNumberBean;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author
 * @desc 人数Activity
 * @date
 */

public class PeopleNumListActivity extends BaseActivity {

    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;
    @BindView(R.id.empty)
    EmptyLayout emptyLayout;
    private Presenter2<PeopleNumberBean> presenter;
    private int timeType = 0;
    private TimePickerView timePickerView;

    private PersonNumAdapter personNumAdapter;
    private RequestPersonNumber requestBean = new RequestPersonNumber();

    private String staffId;
    private StoreBean storeBean;

    public static Intent startPeopleNumListActivityFromStore(Context context, String staffId, StoreBean storeBean) {
        return new Intent(context, PeopleNumListActivity.class).putExtra(EXTRA_DATA, staffId).putExtra(EXTRA_DATA2, storeBean).putExtra(EXTRA_TYPE, "STORE");
    }

    public static Intent startPeopleNumListActivity(Context context, String staffId, StoreBean storeBean) {
        return new Intent(context, PeopleNumListActivity.class).putExtra(EXTRA_DATA, staffId).putExtra(EXTRA_DATA2, storeBean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_count);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    private void initDatas() {
        configurePresenter();
        requestBean.start = DateTimeUtils.getTimesMonthMorning();
        requestBean.end = DateTimeUtils.getNowTimeStamp();
        requestBean.staffId = staffId;
        List<String> spIdList = new ArrayList<>();
        spIdList.add(storeBean.spId);
        requestBean.spIdList = spIdList;
        requestBean.groupId = storeBean.group_id;
        easyRefreshLayout.autoRefresh();
    }

    private void initViews() {
        titleBar.setTilte("人数");
        staffId = getIntent().getStringExtra(EXTRA_DATA);
        storeBean = (StoreBean) getIntent().getSerializableExtra(EXTRA_DATA2);
        personNumAdapter = new PersonNumAdapter(mContext);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(personNumAdapter);
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
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<PeopleNumberBean>() {

            @Override
            public void onSuccess(List<PeopleNumberBean> result, int total, boolean isRefresh) {
                emptyLayout.setVisibility(View.GONE);
                easyRefreshLayout.setVisibility(View.VISIBLE);
                if (isRefresh) {
                    personNumAdapter.notifyDataSetChanged(result);
                } else {
                    personNumAdapter.addAll(result);
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
            public void onLoad(Subscriber<DataBean<PageInfo<PeopleNumberBean>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getPersonNumberList(requestBean, pageIndex, pageSize, subscriber);
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

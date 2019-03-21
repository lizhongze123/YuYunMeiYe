package cn.yuyun.yymy.ui.store.report;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.http.DataBean;
import com.example.http.PageInfo;
import com.example.http.Presenter2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.DeviceUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestReportBusinessDetail;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;
import cn.yuyun.yymy.http.result.ResultReportBusinessDetail;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.MyHorizontalScrollView;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author
 * @desc 营业明细表 -- 总部
 * @date
 */

public class ReportBusinessDetailAllActivity extends BaseActivity {

    @BindView(R.id.main_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.main_right_drawer_layout)
    RelativeLayout main_right_drawer_layout;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.rl_start)
    RelativeLayout rlStart;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.rl_end)
    RelativeLayout rlEnd;
    @BindView(R.id.sv_top)
    MyHorizontalScrollView svTop;
    @BindView(R.id.rv_left)
    RecyclerView rvLeft;
    @BindView(R.id.rv_analysis)
    RecyclerView rvRight;
    @BindView(R.id.sv_right)
    MyHorizontalScrollView svRight;
    @BindView(R.id.easyRefreshLayout)
    EasyRefreshLayout easyRefreshLayout;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;

    private ReportLeftAdapter leftAdapter;
    private ReportBusinessDetailRightAdapter rightAdapter;
    private int timeType = 0;
    private TimePickerView timePickerView;

    private Presenter2<ResultReportBusinessDetail> presenter;
    private LinearLayoutManager linearLayoutManager;

    private RequestReportBusinessDetail requestReport;

    private int index = 0;
    private boolean isLoading = false;

    @BindView(R.id.rv_card)
    RecyclerView rvCard;
    StringBuilder tempLevelName;
    HqStoreAdapter hqStoreAdapter;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    private String groupId;
    private List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> storeList;
    private List<String> tempSpIdList;

    public static Intent startReportBusinessDetailActivity(Context context, List<String> tempSpIdList, List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> storeList, String groupId) {
        return new Intent(context, ReportBusinessDetailAllActivity.class).putExtra(EXTRA_DATA2, (Serializable) tempSpIdList).putExtra(EXTRA_DATA, (Serializable) storeList).putExtra(EXTRA_TYPE, groupId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_business_detail_all);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initData();
    }

    private void initData() {
        requestReport = new RequestReportBusinessDetail();
        requestReport.sp_id_list = tempSpIdList;
        requestReport.group_id = groupId;
        requestReport.start_date = DateTimeUtils.getNowTimeStamp();
        requestReport.end_date = DateTimeUtils.getNowTimeStamp();
        configurePresenter();
        presenter.loadData(true);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        titleBar.setTilte("营业明细表");
        groupId = getIntent().getStringExtra(EXTRA_TYPE);
        tempSpIdList = (List<String>) getIntent().getSerializableExtra(EXTRA_DATA2);
        storeList = (List<ResultClassfiyStoreBean.OgServiceplacesRspListBean>) getIntent().getSerializableExtra(EXTRA_DATA);
        tempLevelName = new StringBuilder();
        linearLayoutManager = new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rvLeft.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvRight.setLayoutManager(linearLayoutManager);
        leftAdapter = new ReportLeftAdapter(mContext);
        rightAdapter = new ReportBusinessDetailRightAdapter(mContext);
        rvLeft.setAdapter(leftAdapter);
        rvRight.setAdapter(rightAdapter);

        easyRefreshLayout.setLoadMoreModel(LoadModel.NONE);
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(mContext));
        easyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {

            }

            @Override
            public void onRefreshing() {
                if(!isLoading){
                    presenter.loadData(true);
                }
            }
        });

        svTop.setScrollViewListener(new MyHorizontalScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(MyHorizontalScrollView scrollView, int x, int y, int oldx, int oldy) {
                svRight.scrollTo(x, y);
            }
        });
        svRight.setScrollViewListener(new MyHorizontalScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(MyHorizontalScrollView scrollView, int x, int y, int oldx, int oldy) {
                svTop.scrollTo(x, y);
            }
        });

        timePickerView = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (timeType == 0) {
                    tvStart.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATE_UI));
                    requestReport.start_date = date.getTime() / 1000;
                } else {
                    tvEnd.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATE_UI));
                    requestReport.end_date = date.getTime() / 1000;
                }
                if (requestReport.end_date - requestReport.start_date < 0) {
                    showTextDialog("结束时间不能小于开始时间", false);
                    return;
                }
                presenter.loadData(true);
            }
        }).build();
        tvStart.setText(DateTimeUtils.getNowTime());
        tvEnd.setText(DateTimeUtils.getNowTime());

        scrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN :

                        break;
                    case MotionEvent.ACTION_MOVE :
                        index++;
                        break;

                    default :
                        break;
                }
                if (event.getAction() == MotionEvent.ACTION_UP  && index > 0) {
                    index = 0;
                    View view = ((ScrollView) v).getChildAt(0);
                    if (view.getMeasuredHeight() <= v.getScrollY() + v.getHeight()) {
                        presenter.loadData(false);
                    }
                }
                return false;
            }
        });
        hqStoreAdapter = new HqStoreAdapter(mContext);
        rvCard.setLayoutManager(new GridLayoutManager(this, 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvCard.setAdapter(hqStoreAdapter);
        hqStoreAdapter.addAll(storeList);
    }


    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultReportBusinessDetail>() {

            @Override
            public void onSuccess(final List<ResultReportBusinessDetail> result, int total, final boolean isRefresh) {
                if (result != null) {
                    emptyLayout.setVisibility(View.GONE);
                    easyRefreshLayout.setVisibility(View.VISIBLE);
                    List<String> list = new ArrayList<>();
                   for (int i = 0, len = result.size(); i < len; i++) {
                        String s = result.get(i).consume_time;
                        list.add(s);
                    }
                    if (isRefresh) {
                        leftAdapter.notifyDataSetChanged(list);
                        rightAdapter.notifyDataSetChanged(result);
                    } else {
                        leftAdapter.addAll(list);
                        rightAdapter.addAll(result);
                    }
                }else{
                    leftAdapter.clear();
                    rightAdapter.clear();
                }
            }

            @Override
            public void onFailed(boolean isRefresh) {
                isLoading = false;
                dismissLoadingDialog();
                easyRefreshLayout.refreshComplete();
                easyRefreshLayout.closeLoadView();
                if (DeviceUtils.hasInternet(mContext)) {
                    showToast("加载失败，请稍候重试");
                }else{
                    emptyLayout.setVisibility(View.VISIBLE);
                    emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                }
            }

            @Override
            public void onCompleted(boolean isRefresh) {
                isLoading = false;
                easyRefreshLayout.refreshComplete();
                easyRefreshLayout.closeLoadView();
                dismissLoadingDialog();
            }

            @Override
            public void onEmptyData() {
                isLoading = false;
                leftAdapter.clear();
                rightAdapter.clear();
                emptyLayout.setVisibility(View.VISIBLE);
                emptyLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
                easyRefreshLayout.setVisibility(View.GONE);
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultReportBusinessDetail>>> subscriber, int pageIndex, int pageSize) {
                isLoading = true;
                showLoadingDialog("加载中");
                AppModelFactory.model().reportBusinessDetail(requestReport, pageIndex, pageSize, subscriber);
            }
        });
    }

    @OnClick(R.id.rl_start)
    public void start(View v) {
        timeType = 0;
        timePickerView.show();
    }

    @OnClick(R.id.rl_end)
    public void end(View v) {
        timeType = 1;
        timePickerView.show();
    }

    @OnClick(R.id.tv_positive)
    public void positive(View v) {
        openRightLayout(v);
        List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> listStore = hqStoreAdapter.getSelectedItem();
        if(null != listStore && listStore.size() > 0){
            tempLevelName.delete(0, tempLevelName.length());
            List<String> list = new ArrayList<>();
            for (int i = 0; i < listStore.size(); i++) {
                list.add(listStore.get(i).sp_id);
                tempLevelName.append(listStore.get(i).sp_name + "、");
            }
            requestReport.sp_id_list = list;
            tvAmount.setText(tempLevelName.toString().substring(0,tempLevelName.length() - 1));
        }else{
            requestReport.sp_id_list = tempSpIdList;
        }
        presenter.loadData(true);
        tvAmount.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tvAmount.setSingleLine(true);
        tvAmount.setSelected(true);
        tvAmount.setFocusable(true);
        tvAmount.setFocusableInTouchMode(true);
    }

    @OnClick(R.id.tv_negative)
    public void negative(View v) {
        openRightLayout(v);
        requestReport.sp_id_list = tempSpIdList;
        tempLevelName.delete(0, tempLevelName.length());
        tvAmount.setText("全部数据");
        hqStoreAdapter.clearAllSelection();
        presenter.loadData(true);
    }

    @OnClick(R.id.rl_filter)
    public void filter(View v) {
        DeviceUtils.hideSoftKeyboard(v, mContext);
        openRightLayout(v);
    }

    /**
     * 右边菜单开关事件
     */
    public void openRightLayout(View view) {
        if (drawerLayout.isDrawerOpen(main_right_drawer_layout)) {
            drawerLayout.closeDrawer(main_right_drawer_layout);
        } else {
            drawerLayout.openDrawer(main_right_drawer_layout);
        }
    }


}
package cn.yuyun.yymy.ui.home.analysis;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.NoneProgressSubscriber;
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
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestAnalysisOne;
import cn.yuyun.yymy.http.result.ResultAnalysisOne;
import cn.yuyun.yymy.http.result.ResultAnalysisOneTotal;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;
import cn.yuyun.yymy.ui.store.report.HqStoreAdapter;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.MyHorizontalScrollView;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author
 * @desc 品项分析一级 -- 总部
 * @date
 */

public class AnalysisOneAllActivity extends BaseActivity {

    @BindView(R.id.main_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.main_right_drawer_layout)
    RelativeLayout main_right_drawer_layout;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.iv_two)
    ImageView ivTwo;
    @BindView(R.id.iv_three)
    ImageView ivThree;
    @BindView(R.id.iv_four)
    ImageView ivFour;
    @BindView(R.id.iv_five)
    ImageView ivFive;
    @BindView(R.id.sv_top)
    MyHorizontalScrollView svTop;
    @BindView(R.id.rv_left)
    RecyclerView rvLeft;
    @BindView(R.id.rv_analysis)
    RecyclerView rvRight;
    @BindView(R.id.sv_right)
    MyHorizontalScrollView svRight;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.easyRefreshLayout)
    EasyRefreshLayout easyRefreshLayout;
    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;

    private AnalysisOneLeftAdapter leftAdapter;
    private AnalysisOneRightAdapter rightAdapter;
    private int timeType = 0;
    private TimePickerView timePickerView;

    private Presenter2<ResultAnalysisOne> presenter;

    private RequestAnalysisOne requestReport;
    private boolean isLoading = false;

    private int index = 0;

    @BindView(R.id.rv_card)
    RecyclerView rvCard;
    StringBuilder tempLevelName;
    HqStoreAdapter hqStoreAdapter;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    private String groupId;
    private List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> storeList;
    private List<String> tempSpIdList;

    @BindView(R.id.tv_oneTotal)
    TextView tvOneTotal;
    @BindView(R.id.tv_twoTotal)
    TextView tvTwoTotal;
    @BindView(R.id.tv_threeTotal)
    TextView tvThreeTotal;
    @BindView(R.id.tv_fourTotal)
    TextView tvFourTotal;
    @BindView(R.id.sv_bottom)
    MyHorizontalScrollView svBottom;

    public static Intent startAnalysisOneActivity(Context context, List<String> tempSpIdList, List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> storeList, String groupId) {
        return new Intent(context, AnalysisOneAllActivity.class).putExtra(EXTRA_DATA2, (Serializable) tempSpIdList).putExtra(EXTRA_DATA, (Serializable) storeList).putExtra(EXTRA_TYPE, groupId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_one_all);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initData();
    }

    private void initData() {
        requestReport = new RequestAnalysisOne();
        requestReport.sp_id_list = tempSpIdList;
        requestReport.group_id = groupId;

        requestReport.start_date = DateTimeUtils.getTimesMonthMorning();
        requestReport.end_date = DateTimeUtils.getNowTimeStamp();

        configurePresenter();
        presenter.loadData(true);
        getTotal();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        titleBar.setTilte("品项分析表");
        groupId = getIntent().getStringExtra(EXTRA_TYPE);
        tempSpIdList = (List<String>) getIntent().getSerializableExtra(EXTRA_DATA2);
        storeList = (List<ResultClassfiyStoreBean.OgServiceplacesRspListBean>) getIntent().getSerializableExtra(EXTRA_DATA);
        tempLevelName = new StringBuilder();
        rvLeft.setLayoutManager(new LinearLayoutManager(mContext){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvRight.setLayoutManager(new LinearLayoutManager(mContext){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        leftAdapter = new AnalysisOneLeftAdapter(mContext);
        rightAdapter = new AnalysisOneRightAdapter(mContext);
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
                svBottom.scrollTo(x, y);
            }
        });
        svBottom.setScrollViewListener(new MyHorizontalScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(MyHorizontalScrollView scrollView, int x, int y, int oldx, int oldy) {
                svRight.scrollTo(x, y);
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
        tvStart.setText(DateTimeUtils.getDateTimeText(DateTimeUtils.getTimesMonthMorning(), DateTimeUtils.FORMAT_DATE_UI));
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

        rightAdapter.setOnItemClickListener(new AnalysisOneRightAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(ResultAnalysisOne bean, int position) {
                requestReport.good_brand_id = bean.good_brand_id;
                startActivity(AnalysisTwoAllActivity.startAnalysisTwoActivity(mContext, requestReport));
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
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultAnalysisOne>() {

            @Override
            public void onSuccess(final List<ResultAnalysisOne> result, int total, final boolean isRefresh) {
                if (null != result) {
                    emptyLayout.setVisibility(View.GONE);
                    easyRefreshLayout.setVisibility(View.VISIBLE);
                    if (isRefresh) {
                        leftAdapter.notifyDataSetChanged(result);
                        rightAdapter.notifyDataSetChanged(result);
                    } else {
                        leftAdapter.addAll(result);
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
                dismissLoadingDialog();
                easyRefreshLayout.refreshComplete();
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
            public void onLoad(Subscriber<DataBean<PageInfo<ResultAnalysisOne>>> subscriber, int pageIndex, int pageSize) {
                isLoading = true;
                showLoadingDialog("加载中");
                requestReport.count = pageSize;
                requestReport.start_row = pageIndex;
                AppModelFactory.model().getAnalysisOne(requestReport, subscriber);
            }
        });
    }

    private void getTotal() {
        AppModelFactory.model().getAnalysisOneTotal(requestReport, new NoneProgressSubscriber<DataBean<ResultAnalysisOneTotal>>(mContext) {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(DataBean<ResultAnalysisOneTotal> result) {
                if (null != result.data) {
                    TextViewUtils.setTextOrEmpty(tvOneTotal, StringFormatUtils.formatTwoDecimal(result.data.total_pay_amount));
                    TextViewUtils.setTextOrEmpty(tvTwoTotal, StringFormatUtils.formatTwoDecimal(result.data.total_transaction_price));
                    TextViewUtils.setTextOrEmpty(tvThreeTotal, StringFormatUtils.formatTwoDecimal(result.data.total_achieve_percent));
                    TextViewUtils.setTextOrEmpty(tvFourTotal, StringFormatUtils.formatTwoDecimal(result.data.total_consume_amount));
                }
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(getString(R.string.NO_INTERNET));
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast(ex.message);
            }
        });
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
                tempLevelName.append(listStore.get(i).sp_name + ",");
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

    private int flag;

    private void setFlag(int value) {
        if (requestReport.sort == value) {
            flag++;
            if (flag > 2) {
                flag = 0;
            }
            if (flag == 0) {
                requestReport.sort_type = 0;
            } else if (flag == 1) {
                requestReport.sort_type = 1;
            } else {
                requestReport.sort_type = 2;
            }
        } else {
            requestReport.sort = value;

            if (flag > 2) {
                flag = 0;
            }
            flag++;
            if (flag == 0) {
                requestReport.sort_type = 0;
            } else if (flag == 1) {
                requestReport.sort_type = 1;
            } else {
                requestReport.sort_type = 2;
            }
        }
    }

    @OnClick(R.id.tv_two)
    public void two(View v) {
        setFlag(1);
        setSortStatus(ivTwo, flag);
        setStatusNormal(ivThree, ivFour, ivFive);
        presenter.loadData(true);
    }

    @OnClick(R.id.tv_three)
    public void three(View v) {
        setFlag(2);
        setSortStatus(ivThree, flag);
        setStatusNormal(ivTwo, ivFour, ivFive);
        presenter.loadData(true);
    }

    @OnClick(R.id.tv_four)
    public void four(View v) {
        setFlag(3);
        setSortStatus(ivFour, flag);
        setStatusNormal(ivTwo, ivThree, ivFive);
        presenter.loadData(true);
    }

    @OnClick(R.id.tv_five)
    public void five(View v) {
        setFlag(4);
        setSortStatus(ivFive, flag);
        setStatusNormal(ivTwo, ivThree, ivFour);
        presenter.loadData(true);
    }

    private void setSortStatus(ImageView iv, int status) {
        if (status == 0) {
            iv.setImageResource(R.drawable.order_normal);
        } else if (status == 1) {
            iv.setImageResource(R.drawable.order_ascending);
        } else {
            iv.setImageResource(R.drawable.order_descending);
        }
    }

    private void setStatusNormal(ImageView... args) {
        for (int i = 0; i < args.length; i++) {
            args[i].setImageResource(R.drawable.order_normal);
        }
    }
}

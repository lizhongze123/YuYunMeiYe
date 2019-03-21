package cn.yuyun.yymy.ui.store.report;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.NoneProgressSubscriber;
import com.example.http.PageInfo;
import com.example.http.Presenter2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.DeviceUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestReportLiabilities;
import cn.yuyun.yymy.http.request.RequestReportLiabilitiesThree;
import cn.yuyun.yymy.http.result.ResultLiabilitiesTwo;
import cn.yuyun.yymy.http.result.ResultReportLiabilitiesOneTotal;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.MyHorizontalScrollView;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;

/**
 * @author
 * @desc
 * @date
 */

public class ReportLiabilitiesTwoActivity extends BaseActivity {

    @BindView(R.id.tv_fixed)
    TextView tvFixed;
    @BindView(R.id.iv_one)
    ImageView ivOne;
    @BindView(R.id.iv_two)
    ImageView ivTwo;
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
    @BindView(R.id.totalDiagram)
    RelativeLayout totalDiagram;
    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;
    @BindView(R.id.tv_oneTotal)
    TextView tvOneTotal;
    @BindView(R.id.tv_twoTotal)
    TextView tvTwoTotal;
    @BindView(R.id.sv_bottom)
    MyHorizontalScrollView svBottom;

    private ReportLiabilitiesTwoAdapter rightAdapter;
    private ReportLeftAdapter leftAdapter;

    private RequestReportLiabilities requestBody;
    private Presenter2<ResultLiabilitiesTwo> presenter;

    private int index = 0;
    private String groupId;
    private List<String> tempSpIdList;

    public static Intent startReportLiabilitiesTwoActivity(Context context, List<String> tempSpIdList, String groupId) {
        return new Intent(context, ReportLiabilitiesTwoActivity.class).putExtra(EXTRA_DATA2, (Serializable) tempSpIdList).putExtra(EXTRA_DATA, groupId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_liabilities_two);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initData();
    }

    private void initData() {
        groupId = getIntent().getStringExtra(EXTRA_DATA);
        tempSpIdList = (List<String>) getIntent().getSerializableExtra(EXTRA_DATA2);
        requestBody = new RequestReportLiabilities();
        requestBody.group_id = groupId;
        requestBody.start_date = DateTimeUtils.getTimesMonthMorning();
        requestBody.end_date = DateTimeUtils.getNowTimeStamp();
        requestBody.sp_id_list = tempSpIdList;
        configurePresenter();
        presenter.loadData(true);
        getTotal();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        titleBar.setTilte("门店负债表");
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

        leftAdapter = new ReportLeftAdapter(mContext);
        rightAdapter = new ReportLiabilitiesTwoAdapter(mContext);
        rightAdapter.setOnItemClickListener(new ReportLiabilitiesTwoAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultLiabilitiesTwo bean, int position) {
                RequestReportLiabilitiesThree body = new RequestReportLiabilitiesThree();
                body.start_date = requestBody.start_date;
                body.end_date = requestBody.end_date;
                body.group_id = requestBody.group_id;
                body.sp_id_list = requestBody.sp_id_list;
                body.brand_id = bean.brand_id;
                startActivity(ReportLiabilitiesThreeActivity.startReportLiabilitiesThreeActivity(mContext, body));
            }
        });
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
                presenter.loadData(true);
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

    }

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultLiabilitiesTwo>() {

            @Override
            public void onSuccess(final List<ResultLiabilitiesTwo> result, int total, final boolean isRefresh) {
                if (null != result) {
                    emptyLayout.setVisibility(View.GONE);
                    easyRefreshLayout.setVisibility(View.VISIBLE);
                    List<String> list = new ArrayList<>();
                    for (int i = 0, len = result.size(); i < len; i++) {
                        String s = result.get(i).sp_name;
                        list.add(s);
                    }
                    if (isRefresh) {
                        leftAdapter.notifyDataSetChanged(list);
                        rightAdapter.notifyDataSetChanged(result);
                    } else {
                        leftAdapter.addAll(list);
                        rightAdapter.addAll(result);
                    }
                }
            }

            @Override
            public void onFailed(boolean isRefresh) {
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
                dismissLoadingDialog();
                easyRefreshLayout.refreshComplete();
            }

            @Override
            public void onEmptyData() {
                leftAdapter.clear();
                rightAdapter.clear();
                emptyLayout.setVisibility(View.VISIBLE);
                emptyLayout.setErrorType(EmptyLayout.NODATA);
                easyRefreshLayout.setVisibility(View.GONE);
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultLiabilitiesTwo>>> subscriber, int pageIndex, int pageSize) {
                showLoadingDialog("加载中");
                AppModelFactory.model().reportLiabilitesTwo(requestBody, pageIndex, pageSize, subscriber);
            }
        });
    }

    private void getTotal() {
        AppModelFactory.model().getReportLiabilitiesTwoTotal(requestBody, new NoneProgressSubscriber<DataBean<ResultReportLiabilitiesOneTotal>>(mContext) {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(DataBean<ResultReportLiabilitiesOneTotal> result) {
                if (null != result.data) {
                    TextViewUtils.setTextOrEmpty(tvOneTotal, StringFormatUtils.formatTwoDecimal(result.data.amount_still_here));
                    TextViewUtils.setTextOrEmpty(tvTwoTotal, StringFormatUtils.formatTwoDecimal(result.data.num_canbe_used));
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

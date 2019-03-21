package cn.yuyun.yymy.ui.store.report;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.http.ProgressSubscriber;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestReportLiabilities;
import cn.yuyun.yymy.http.result.ResultLiabilities;
import cn.yuyun.yymy.http.result.ResultReportLiabilitiesOneTotal;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.MyHorizontalScrollView;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;

/**
 * @author
 * @desc 门店负债表
 * @date
 */

public class ReportLiabilitiesActivity extends BaseActivity {

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
    @BindView(R.id.tv_threeTotal)
    TextView tvThreeTotal;
    @BindView(R.id.sv_bottom)
    MyHorizontalScrollView svBottom;

    private ReportLiabilitiesAdapter rightAdapter;
    private ReportLeftAdapter leftAdapter;

    private RequestReportLiabilities requestBody;

    private String groupId;
    private List<String> tempSpIdList;

    public static Intent startReportLiabilitiesActivity(Context context, List<String> tempSpIdList, String groupId) {
        return new Intent(context, ReportLiabilitiesActivity.class).putExtra(EXTRA_DATA2, (Serializable) tempSpIdList).putExtra(EXTRA_DATA, groupId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_liabilities);
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
        getData();
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
        rightAdapter = new ReportLiabilitiesAdapter(mContext);
        rightAdapter.setOnItemClickListener(new ReportLiabilitiesAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultLiabilities bean, int position) {
                startActivity(ReportLiabilitiesTwoActivity.startReportLiabilitiesTwoActivity(mContext, tempSpIdList, groupId));
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
                getData();
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
    }

    private void getTotal() {
        AppModelFactory.model().getReportLiabilitiesOneTotal(requestBody, new NoneProgressSubscriber<DataBean<ResultReportLiabilitiesOneTotal>>(mContext) {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(DataBean<ResultReportLiabilitiesOneTotal> result) {
                if (null != result.data) {
                    TextViewUtils.setTextOrEmpty(tvOneTotal, StringFormatUtils.formatTwoDecimal(result.data.canbe_consume));
                    TextViewUtils.setTextOrEmpty(tvTwoTotal, StringFormatUtils.formatTwoDecimal(result.data.arrears));
                    TextViewUtils.setTextOrEmpty(tvThreeTotal, StringFormatUtils.formatTwoDecimal(result.data.storedvalue));
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

    private void getData() {
        AppModelFactory.model().reportLiabilites(requestBody, new ProgressSubscriber<DataBean<List<ResultLiabilities>>>(mContext, false) {

            @Override
            public void onCompleted() {
                super.onCompleted();
                easyRefreshLayout.refreshComplete();
            }

            @Override
            public void onNext(DataBean<List<ResultLiabilities>> result) {
                if (null != result.data) {
                    List<String> list = new ArrayList<>();
                    for (int i = 0, len = result.data.size(); i < len; i++) {
                        String s = result.data.get(i).sp_name;
                        list.add(s);
                    }
                    leftAdapter.notifyDataSetChanged(list);
                    rightAdapter.notifyDataSetChanged(result.data);
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

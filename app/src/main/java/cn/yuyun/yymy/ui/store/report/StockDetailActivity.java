package cn.yuyun.yymy.ui.store.report;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.example.http.DataBean;
import com.example.http.PageInfo;
import com.example.http.Presenter2;

import java.util.List;

import butterknife.BindView;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.ResultStock;
import cn.yuyun.yymy.http.result.ResultStorehouseDetail;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc 库存详情
 * @date
 */

public class StockDetailActivity extends BaseActivity {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.rv_analysis)
    RecyclerView rvAnalysis;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;

    private StockDetailAdapter mAdapter;
    private ResultStock resultStock;

    private Presenter2<ResultStorehouseDetail> presenter;

    public static Intent startStockDetailActivity(Context context, ResultStock bean) {
        return new Intent(context, StockDetailActivity.class).putExtra(EXTRA_DATA, bean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    private void initDatas() {
        TextViewUtils.setTextOrEmpty(tvName, resultStock.productName);
        TextViewUtils.setTextOrEmpty(tvPrice, "指导总价:" + resultStock.guidePrice);
        TextViewUtils.setTextOrEmpty(tvAddress, "仓库名称:" + resultStock.shName);
        configurePresenter();
        easyRefreshLayout.autoRefresh();
    }

    private void initViews() {
        titleBar.setTilte("库存详情");
        resultStock = (ResultStock) getIntent().getSerializableExtra(EXTRA_DATA);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvAnalysis.setLayoutManager(mLayoutManager);
        mAdapter = new StockDetailAdapter(this);
        rvAnalysis.setAdapter(mAdapter);
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
    }

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultStorehouseDetail>() {

            @Override
            public void onSuccess(List<ResultStorehouseDetail> result, int total, final boolean isRefresh) {
                if (result != null) {
                    if (isRefresh) {
                        mAdapter.notifyDataSetChanged(result);
                    } else {
                        mAdapter.addAll(result);
                    }
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
            }


            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultStorehouseDetail>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getStorehouseDetail(resultStock.shId, resultStock.productId, pageIndex, pageSize, subscriber);
            }
        });
    }

}

package cn.yuyun.yymy.ui.store.report;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.NoneProgressSubscriber;
import com.example.http.PageInfo;
import com.example.http.Presenter2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestStock;
import cn.yuyun.yymy.http.request.RequestStorehouse;
import cn.yuyun.yymy.http.result.ResultStock;
import cn.yuyun.yymy.http.result.ResultStorehouse;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc 库存管理
 * @date
 */

public class StockActivity extends BaseActivity implements TextWatcher {

    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.ll_searchMyMember)
    LinearLayout llSearchMyMember;
    @BindView(R.id.rl_search)
    RelativeLayout rlSearch;
    @BindView(R.id.rv_analysis)
    RecyclerView rvAnalysis;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;
    @BindView(R.id.totalDiagram)
    LinearLayout totalDiagram;

    private StockAdapter stockAdapter;
    private StoreBean storeBean;
    private RequestStock requestStock;

    private Presenter2<ResultStock> presenter;

    @BindView(R.id.empty)
    EmptyLayout emptyLayout;

    public static Intent startStockActivity(Context context, StoreBean bean) {
        return new Intent(context, StockActivity.class).putExtra(EXTRA_DATA, bean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    private void initDatas() {
        configurePresenter();
        requestStock = new RequestStock();
        List<String>list = new ArrayList<>();
        if(storeBean.ogType == 1){
            requestStock.hq_id = storeBean.group_id;
        }else{
            list.add(storeBean.spId);
        }
        requestStock.shIdList = list;
        easyRefreshLayout.autoRefresh();
    }

    private void initViews() {
        titleBar.setTilte("库存管理");
        storeBean = (StoreBean) getIntent().getSerializableExtra(EXTRA_DATA);
        rvAnalysis.setLayoutManager(new LinearLayoutManager(this));
        stockAdapter = new StockAdapter(this);
        stockAdapter.setOnItemClickListener(new StockAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultStock bean, int position) {
                startActivity(StockDetailActivity.startStockDetailActivity(mContext, bean));
            }
        });
        rvAnalysis.setAdapter(stockAdapter);
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
        etInput.addTextChangedListener(this);
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(etInput.getText().toString())) {
                        requestStock.searchText = etInput.getText().toString().trim();
                        easyRefreshLayout.autoRefresh();
                    }
                }
                return false;
            }
        });
    }

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultStock>() {

            @Override
            public void onSuccess(List<ResultStock> result, int total, final boolean isRefresh) {
                emptyLayout.setVisibility(View.GONE);
                easyRefreshLayout.setVisibility(View.VISIBLE);
                if (null != result) {
                    if (isRefresh) {
                        stockAdapter.notifyDataSetChanged(result);
                    } else {
                        stockAdapter.addAll(result);
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
                emptyLayout.setVisibility(View.VISIBLE);
                emptyLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
                easyRefreshLayout.setVisibility(View.GONE);
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultStock>>> subscriber, int pageIndex, int pageSize) {
                showContent();
                requestStock.startRow = pageIndex;
                requestStock.count = pageSize;
                AppModelFactory.model().getStock(requestStock, subscriber);
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}

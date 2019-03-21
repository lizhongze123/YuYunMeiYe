package cn.yuyun.yymy.ui.store;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.NoneProgressSubscriber;
import com.example.http.PageInfo;
import com.example.http.Presenter2;
import com.example.http.ProgressSubscriber;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseNoTitleFragment;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestStoreAnalysis;
import cn.yuyun.yymy.http.result.ResultStoreAnalysis;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.ui.store.report.ResultStoreAnalysisOut;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;

/**
 * @author
 * @desc  门店分析 - 储值余额
 * @date
 */
public class StoreAnalysisFragment1 extends BaseNoTitleFragment {

    private EasyRefreshLayout easyRefreshLayout;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private StoreAnalysisAdapter mAdapter;
    private Context mContext;
    private TextView tvAmount;
    private List<String> spIdList;
    private String groupId;
    private int pageIndex = 1;
    private int pageSize = 12;
    private int totalPage;
    private boolean isRefresh = true;
    private RequestStoreAnalysis requestBean = new RequestStoreAnalysis();

    public static StoreAnalysisFragment1 newInstance(List<String> spIdList, String groupId) {
        StoreAnalysisFragment1 fragment = new StoreAnalysisFragment1();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATA, (Serializable) spIdList);
        args.putSerializable(EXTRA_DATA2, groupId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        if (getArguments() != null) {
            spIdList = (List<String>) getArguments().getSerializable(EXTRA_DATA);
            groupId = getArguments().getString(EXTRA_DATA2);
        }
    }

    @Override
    protected void onBindViewBefore(View root) {
        super.onBindViewBefore(root);
        mContext = getContext();
    }

    @Override
    protected int getTheLayoutId() {
        return R.layout.fragment_asset;
    }

    @Override
    protected void initViews(View root) {
        super.initViews(root);
        easyRefreshLayout = (EasyRefreshLayout) root.findViewById(R.id.easylayout);
        tvAmount = (TextView) root.findViewById(R.id.tv_amount);
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(this.getContext()));
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new StoreAnalysisAdapter(mContext, 0);
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
    }

    @Override
    protected void initData() {
        super.initData();
        requestBean.type = 2;
        requestBean.sp_id_list = spIdList;
        requestBean.groupId = groupId;
        easyRefreshLayout.autoRefresh();
    }

    private void getAmount() {
        AppModelFactory.model().storeAnalysis(requestBean, pageIndex, pageSize, new ProgressSubscriber<DataBean<ResultStoreAnalysisOut>>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
                easyRefreshLayout.refreshComplete();
                easyRefreshLayout.loadMoreComplete();
                pageIndex = pageIndex + 1;
            }

            @Override
            public void onNext(DataBean<ResultStoreAnalysisOut> result) {
                if(null != result.data){
                    tvAmount.setText(StringFormatUtils.formatTwoDecimal(result.data.amount.storedvalue));
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

    @Override
    public void onEmptyRefresh() {
        super.onEmptyRefresh();
        easyRefreshLayout.autoRefresh();
        getAmount();
    }
}


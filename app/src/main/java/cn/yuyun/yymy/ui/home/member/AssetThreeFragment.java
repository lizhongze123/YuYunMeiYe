package cn.yuyun.yymy.ui.home.member;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ajguan.library.EasyRefreshLayout;
import com.example.http.DataBean;
import com.example.http.PageInfo;
import com.example.http.Presenter;
import com.example.http.Presenter2;

import java.util.ArrayList;
import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseNoTitleFragment;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.http.result.ResultPackageAsset;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author 资产信息-套餐Fragment
 * @desc
 * @date 2018/1/15
 */
public class AssetThreeFragment extends BaseNoTitleFragment {

    private Presenter2<ResultPackageAsset> presenter;
    private EasyRefreshLayout easyRefreshLayout;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AssetPacketAdapter adapter;
    private Context mContext;
    private ResultMemberBean memberBean;

    public static AssetThreeFragment newInstance(ResultMemberBean param1) {
        AssetThreeFragment fragment = new AssetThreeFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATA, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        if (getArguments() != null) {
            memberBean = (ResultMemberBean) getArguments().getSerializable(EXTRA_DATA);
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
        root.findViewById(R.id.ll_total).setVisibility(View.GONE);
        easyRefreshLayout = (EasyRefreshLayout) root.findViewById(R.id.easylayout);
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(this.getContext()));
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new AssetPacketAdapter(mContext);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new AssetPacketAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultPackageAsset bean, int position) {
                startActivity(AssetPacketDetailActivity.startAssetPacketDetailActivity(mContext,bean));
            }
        });
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
        configurePresenter();
    }

    @Override
    protected void initData() {
        super.initData();
        easyRefreshLayout.autoRefresh();
    }

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultPackageAsset>(){

            @Override
            public void onSuccess(final List<ResultPackageAsset> result, int total, final boolean isRefresh) {
                if (result != null) {
                    if (isRefresh) {
//                        adapter.notifyDataSetChanged(result);
                    } else {
                        adapter.addAll(result);
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
                showEmpty();
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultPackageAsset>>> subscriber, int pageIndex, int pageSize) {
//                AppModelFactory.model().assetPackage(LoginInfoPrefs.getInstance(mContext).getGroupId(), 3, pageIndex, pageSize, memberBean.memberId, subscriber);
            }
        });
    }
}


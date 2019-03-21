package cn.yuyun.yymy.ui.home.leave;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ajguan.library.EasyRefreshLayout;
import com.example.http.DataBean;
import com.example.http.PageInfo;
import com.example.http.Presenter2;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseNoTitleFragment;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.LeaveBean;
import cn.yuyun.yymy.http.result.StaffBean;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc
 * @date 2018/1/26
 */
public class LeaveReviewedTwoFragment extends BaseNoTitleFragment {

    private Presenter2<LeaveBean> presenter;
    private RecyclerView recyclerView;
    private LeaveApplyAdapter mAdapter;
    private EasyRefreshLayout easyRefreshLayout;
    private StaffBean staffBean;

    public static LeaveReviewedTwoFragment newInstance(StaffBean staffBean) {
        LeaveReviewedTwoFragment fragment = new LeaveReviewedTwoFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATA, staffBean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        if (getArguments() != null) {
            staffBean = (StaffBean) getArguments().getSerializable(EXTRA_DATA);
        }
    }

    @Override
    protected void onBindViewBefore(View root) {
        super.onBindViewBefore(root);
        mContext = getContext();
    }


    @Override
    protected int getTheLayoutId() {
        return R.layout.activity_base_list;
    }


    @Override
    protected void initViews(View root) {
        super.initViews(root);
        EvenManager.register(this);
        easyRefreshLayout = (EasyRefreshLayout) root.findViewById(R.id.easylayout);
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(mContext));
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        mAdapter = new LeaveApplyAdapter(mContext);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new LeaveApplyAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, LeaveBean bean, int position) {
                startActivity(ReviewedDetailActivity.startReviewedDetailActivity(mContext, bean));
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

    }

    @Override
    protected void initData() {
        super.initData();
        configurePresenter();
        presenter.loadData(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if (notifyEvent.tag == NotifyEvent.RREFRESH) {
            presenter.loadData(true);
        }
    }

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<LeaveBean>() {

            @Override
            public void onSuccess(List<LeaveBean> result, int total, boolean isRefresh) {
                if (isRefresh) {
                    mAdapter.notifyDataSetChanged(result);
                } else {
                    mAdapter.addAll(result);
                }

            }

            @Override
            public void onFailed(boolean isRefresh) {
                showError();
            }

            @Override
            public void onCompleted(boolean isRefresh) {
                easyRefreshLayout.refreshComplete();
                easyRefreshLayout.closeLoadView();
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onEmptyData() {
                showEmpty(EmptyLayout.NODATA_ENABLE_CLICK);
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<LeaveBean>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getLeaveListToMe(LoginInfoPrefs.getInstance(mContext).getStaffId(), 2, pageIndex, pageSize, subscriber);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EvenManager.unregister(this);
    }
}

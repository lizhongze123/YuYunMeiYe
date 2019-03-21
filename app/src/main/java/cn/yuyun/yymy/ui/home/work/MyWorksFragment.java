package cn.yuyun.yymy.ui.home.work;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.NoneProgressSubscriber;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import cn.lzz.utils.ResoureUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseNoTitleFragment;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.PageInfoWork;
import cn.yuyun.yymy.http.result.ResultWork;
import cn.yuyun.yymy.http.result.StaffBean;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc
 * @date
 */

public class MyWorksFragment extends BaseNoTitleFragment {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;
    private StaffBean staffBean;
    private LinearLayoutManager mLinearLayoutManager;
    private MyWorksParentAdapter adapter;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.title)
    TextView title;
    private int pageIndex = 1;
    private int pageSize = 5;
    private int totalPage;
    private boolean isRefresh = true;

    @Override
    protected int getTheLayoutId() {
        return R.layout.fragment_mywork;
    }

    public static MyWorksFragment newInstance(StaffBean staffBean) {
        MyWorksFragment fragment = new MyWorksFragment();
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
    protected void initViews(final View root) {
        super.initViews(root);
        EvenManager.register(this);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        adapter = new MyWorksParentAdapter(getActivity(), staffBean);
        adapter.setOnItemClickListener(new AdapterListener2() {

            @Override
            public void onUnread() {

            }

            @Override
            public void onUserClicked() {

            }

            @Override
            public void onFavorites(ResultWork bean) {

            }

            @Override
            public void onLike(ResultWork bean, int parentPos, int childPos) {

            }

            @Override
            public void onComment(ResultWork bean, int parentPos, int childPos) {

            }

            @Override
            public void onItemClicked(ResultWork bean, int parentPos, int childPos) {
                startActivity(MomentsDetailActivity.startFromMy(mContext, bean, staffBean.baseon_id));
            }

        });
        recyclerView.setAdapter(adapter);
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(this.getContext()));
        easyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                isRefresh = false;
                if (pageIndex > totalPage) {
                    showToast("没有更多数据了");
                    easyRefreshLayout.loadMoreComplete();
                }else{
                    isRefresh = true;
                    pageIndex = 1;
                    getData();
                }
            }

            @Override
            public void onRefreshing() {
                isRefresh = true;
                pageIndex = 1;
                getData();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int i = getScollYDistance();
                if(i >= 160){
                    rlTitle.setBackgroundColor(ResoureUtils.getColor(mContext, R.color.f5));
                    title.setTextColor(Color.parseColor("#3f3f3f"));
                    ivBack.setImageResource(R.drawable.back_black);
                }else{
                    rlTitle.setBackgroundColor(Color.parseColor("#003f3f3f"));
                    title.setTextColor(Color.parseColor("#ffffff"));
                    ivBack.setImageResource(R.drawable.back_white);
                }
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        easyRefreshLayout.autoRefresh();
    }

    private void getData() {
        AppModelFactory.model().toMeWorkList(1, pageIndex, pageSize, staffBean.staffId, 0,new NoneProgressSubscriber<DataBean<PageInfoWork>>(mContext) {

            @Override
            public void onCompleted() {
                easyRefreshLayout.refreshComplete();
                easyRefreshLayout.loadMoreComplete();
                pageIndex = pageIndex + 1;
            }

            @Override
            public void onNext(DataBean<PageInfoWork> result) {
                if(null != result.data){
                    if(null != result.data.workReportVoPage){
                        totalPage = result.data.workReportVoPage.pages;
                        if(result.data.workReportVoPage.dataLsit.size() == 0){
                            showToast("没有数据");
                        }else {
                            if(isRefresh){
                                adapter.notifyDataSetChanged(result.data.workReportVoPage.dataLsit);
                            }else{
                                adapter.addAll(result.data.workReportVoPage.dataLsit);
                            }
                        }

                    }
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.getMessage());
                easyRefreshLayout.refreshComplete();
                easyRefreshLayout.loadMoreComplete();
            }

        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if (notifyEvent.tag == NotifyEvent.WORK) {
            easyRefreshLayout.autoRefresh();
        }
    }

    public int getScollYDistance() {
        int position = mLinearLayoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = mLinearLayoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }

}

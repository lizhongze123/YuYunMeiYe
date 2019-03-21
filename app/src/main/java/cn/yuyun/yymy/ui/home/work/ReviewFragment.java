package cn.yuyun.yymy.ui.home.work;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ajguan.library.EasyRefreshLayout;
import com.example.http.PageInfo;
import com.example.http.Presenter;

import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseNoTitleFragment;
import cn.yuyun.yymy.bean.RecordType;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.ResultWork;
import rx.Subscriber;

/**
 * @author lzz
 * @desc  待审核fragment
 * @date 2018/1/27
 */

public class ReviewFragment extends BaseNoTitleFragment {

    /**每次请求多少条*/
    private int pageRows = 20;

    private EasyRefreshLayout easyRefreshLayout;

    public RecordType type ;

    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;

    private NoticeAdapter mAdapter;
    private Presenter<ResultWork> presenter;

    @Override
    protected int getTheLayoutId() {
        return R.layout.activity_base_list;
    }

    @Override
    protected void initViews(final View root) {
        super.initViews(root);
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        mAdapter = new NoticeAdapter(mContext);
        mAdapter.setOnItemClickListener(new NoticeAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultWork bean, int position) {
//                startActivity(MomentsDetailActivity.startFromOther(mContext, bean.workReportDetailVo.workReportId));
            }
        });
        recyclerView.setAdapter(mAdapter);
        easyRefreshLayout = (EasyRefreshLayout) root.findViewById(R.id.easylayout);
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

    private void configurePresenter() {
        presenter = new Presenter<>();
        presenter.setLoadDataListener(new Presenter.OnPresenterLoadListener<ResultWork>() {

            @Override
            public void onSuccess(List<ResultWork> result, int total, boolean isRefresh) {
                if (result != null) {
                    mAdapter.notifyDataSetChanged(result);
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
            public void onLoad(Subscriber<PageInfo<ResultWork>> subscriber, int pageIndex, int pageSize) {

            }
        });
    }

}

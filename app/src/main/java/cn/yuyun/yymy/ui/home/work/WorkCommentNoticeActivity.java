package cn.yuyun.yymy.ui.home.work;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ajguan.library.EasyRefreshLayout;
import com.example.http.DataBean;
import com.example.http.PageInfo;
import com.example.http.Presenter2;

import java.util.List;

import butterknife.BindView;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.ResultWork;
import cn.yuyun.yymy.http.result.ResultWorkCommentNotice;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.ui.home.actions.ActionsAdapter;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

/**
 * @author
 * @desc 汇报评论通知列表
 * @date
 */

public class WorkCommentNoticeActivity extends BaseActivity {


    private Presenter2<ResultWorkCommentNotice> presenter;

    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private WorkCommentNoticeAdapter mAdapter;

    private final int REQUEST_CODE = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_list);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        configurePresenter();
        easyRefreshLayout.autoRefresh();
    }

   /* @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if (notifyEvent.tag == NotifyEvent.RREFRESH_DOT) {
            presenter.loadData(true);
        }
    }*/

    private void initViews() {
        titleBar.setTilte("汇报评论");
        easyRefreshLayout.setEnablePullToRefresh(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new WorkCommentNoticeAdapter(this);
        recyclerView.setAdapter(mAdapter);

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
    public void onEmptyRefresh() {
        super.onEmptyRefresh();
        presenter.loadData(true);
    }

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultWorkCommentNotice>() {

            @Override
            public void onSuccess(List<ResultWorkCommentNotice> result, int total, boolean isRefresh) {
                if (null != result) {
                    if(isRefresh){
                        mAdapter.notifyDataSetChanged(result);
                    }else{
                        mAdapter.addAll(result);
                    }
                }
            }

            @Override
            public void onFailed(boolean isRefresh) {
                if (mAdapter.getItemCount() > 0) {
                    showToast("请求失败");
                } else {
                    showError();
                }
            }

            @Override
            public void onCompleted(boolean isRefresh) {
                easyRefreshLayout.refreshComplete();
                easyRefreshLayout.closeLoadView();
            }

            @Override
            public void onEmptyData() {
                showEmpty(EmptyLayout.NODATA_ENABLE_CLICK);
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultWorkCommentNotice>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getWorkNoticeList(LoginInfoPrefs.getInstance(mContext).getStaffId(), pageIndex, pageSize, subscriber);
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            easyRefreshLayout.autoRefresh();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EvenManager.unregister(this);
    }
}

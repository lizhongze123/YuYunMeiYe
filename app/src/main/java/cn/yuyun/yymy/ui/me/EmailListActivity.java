package cn.yuyun.yymy.ui.me;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.PageInfo;
import com.example.http.Presenter;
import com.example.http.Presenter2;
import com.example.http.ProgressSubscriber;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestDelEmail;
import cn.yuyun.yymy.http.result.ResultEmailList;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

/**
 * @author
 * @desc
 * @date
 */

public class EmailListActivity extends BaseActivity {

    private Presenter2<ResultEmailList> presenter;
    private EasyRefreshLayout easyRefreshLayout;
    private RecyclerView recyclerView;
    private EmailListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_train_list);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        configurePresenter();
        presenter.loadData(true);
    }

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultEmailList>() {

            @Override
            public void onSuccess(List<ResultEmailList> result, int total, boolean isRefresh) {
                if (isRefresh) {
                    mAdapter.notifyDataSetChanged(result);
                } else {
                    mAdapter.addAll(result);
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
                showEmpty(EmptyLayout.NODATA_ENABLE_CLICK);
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultEmailList>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getEmailList(pageIndex, pageSize, subscriber);
            }
        });
    }

    private void initViews() {
        titleBar.setTilte("总裁信箱");
//        titleBar.setTvRight("删除");
        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAdapter.getItemCount() > 0){
                    setDelVisiable(mAdapter.getDelVisiable());
                }
            }
        });
       /* tvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAdapter.getSelectedItem().size() == 0){
                    showToast("请选择要删除的内容");
                    return;
                }else{
                    del(mAdapter.getSelectedItem());
                }
            }
        });*/
        easyRefreshLayout = (EasyRefreshLayout) findViewById(R.id.easylayout);
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(this));
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new EmailListAdapter(this);
        mAdapter.setOnItemClickListener(new EmailListAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultEmailList bean, int position) {
                startActivity(EmailDetailActivity.startEmailDetailActivity(mContext, bean));
            }
        });
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

    private void setDelVisiable(boolean isVisiable) {
        /*if(isVisiable){
            tvDel.setVisibility(View.GONE);
            mAdapter.setDelVisiable(false);
            titleBar.setTvRight("删除");
            easyRefreshLayout.setLoadMoreModel(LoadModel.COMMON_MODEL);
            easyRefreshLayout.setEnablePullToRefresh(true);
        }else{
            tvDel.setVisibility(View.VISIBLE);
            mAdapter.setDelVisiable(true);
            titleBar.setTvRight("完成");
            easyRefreshLayout.setLoadMoreModel(LoadModel.NONE);
            easyRefreshLayout.setEnablePullToRefresh(false);
        }*/
    }

    private void del(List<ResultEmailList> selectedItem) {
        RequestDelEmail body = new RequestDelEmail();
        body.status = -1;
        List<Integer>idList = new ArrayList<>();
        for (int i = 0; i < selectedItem.size(); i++) {
            idList.add(selectedItem.get(i).presidentMailboxId);
        }
        body.presidentMailboxIdList = idList;
        AppModelFactory.model().delEmail(body, new ProgressSubscriber<DataBean<Object>>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean<Object> result) {
                showToast("已删除");
                setDelVisiable(true);
                presenter.loadData(true);
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.message);
            }

        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if (notifyEvent.tag == NotifyEvent.RREFRESH_EMAIL) {
            presenter.loadData(true);
        }
    }

}

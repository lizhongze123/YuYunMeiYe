package cn.yuyun.yymy.ui.home.notice;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.NoneProgressSubscriber;
import com.example.http.PageInfo;
import com.example.http.Presenter2;
import com.example.http.ProgressSubscriber;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.bean.ListType;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestDelNotice;
import cn.yuyun.yymy.http.request.RequestNoticeLike;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

/**
 * @author
 * @desc
 * @date
 */

public class MyNoticeListActivity extends BaseActivity {

    private Presenter2<NoticeBean> presenter;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.et_search)
    EditText etSearch;
    private NoticeAdapter mAdapter;
    private boolean isDel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_train_list);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshEvent(NotifyEvent notifyEvent) {
        presenter.loadData(true);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        EvenManager.register(this);
        initViews();
        configurePresenter();
        easyRefreshLayout.autoRefresh();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if (notifyEvent.tag == NotifyEvent.RREFRESH) {
            presenter.loadData(true);
        }
    }

    private void initViews() {
        titleBar.setTilte("我的公告");
        titleBar.setRightIcon(R.drawable.ic_del_one);
        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDel){
                    if(mAdapter.getSelectedItem().size() == 0){
                        isDel = false;
                        mAdapter.setDelVisiable(false);
                        titleBar.setRightIcon(R.drawable.ic_del_one);
                        titleBar.setTvRight("");
                        easyRefreshLayout.setLoadMoreModel(LoadModel.COMMON_MODEL);
                        easyRefreshLayout.setEnablePullToRefresh(true);
                    }else{
                        delNotice(mAdapter.getSelectedItem());
                    }
                }else{
                    if(mAdapter.getItemCount() > 0){
                        mAdapter.setDelVisiable(true);
                        titleBar.setTvRight("确定");
                        titleBar.setRightIcon(0);
                        easyRefreshLayout.setLoadMoreModel(LoadModel.NONE);
                        easyRefreshLayout.setEnablePullToRefresh(false);
                        isDel = true;
                    }
                }
            }
        });
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new NoticeAdapter(this);
        mAdapter.setOnItemClickListener(new NoticeAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, NoticeBean bean, int position) {
                startActivity(NoticeDetailActivity.startNoticeDetailActivity(mContext, bean));
                bean.readFlag = true;
                mAdapter.refreshRead(position, bean);
            }

            @Override
            public void onFavorites(NoticeBean bean, int position) {
                submitFavorites(bean, position);
            }

            @Override
            public void onLike(NoticeBean bean, int position) {
                submitLike(bean, position);
            }

            @Override
            public void onComment(NoticeBean bean, int position) {
                startActivity(NoticeDetailActivity.startNoticeDetailActivity(mContext, bean));
            }

            @Override
            public void onDel(int count) {
                if(count == 0){
                    titleBar.setTvRight("确定");
                }else{
                    titleBar.setTvRight("确定(" + count + ")");
                }
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

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

/*    private void setDelVisiable(boolean isVisiable) {
        if(isVisiable){
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
        }
    }*/

    private void delNotice(List<NoticeBean> selectedItem) {
        RequestDelNotice requestDelNotice = new RequestDelNotice();
        requestDelNotice.status = -1;
        List<Integer>idList = new ArrayList<>();
        for (int i = 0; i < selectedItem.size(); i++) {
            idList.add(selectedItem.get(i).noticeVo.noticeId);
        }
        requestDelNotice.noticeId = idList;
        AppModelFactory.model().delNotice(requestDelNotice, new ProgressSubscriber<DataBean<Object>>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean<Object> result) {
                showToast("已删除");
                isDel = false;
                mAdapter.setDelVisiable(false);
                titleBar.setRightIcon(R.drawable.ic_del_one);
                titleBar.setTvRight("");
                easyRefreshLayout.setLoadMoreModel(LoadModel.COMMON_MODEL);
                easyRefreshLayout.setEnablePullToRefresh(true);
                presenter.loadData(true);
                EvenManager.sendEvent(new NotifyEvent(NotifyEvent.RREFRESH));
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast(ex.message);
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(mContext.getString(R.string.error_no_network));
            }

        });
    }

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<NoticeBean>(){

            @Override
            public void onSuccess(List<NoticeBean> result, int total, boolean isRefresh) {
                if(null != result){
                    if (isRefresh) {
                        mAdapter.notifyDataSetChanged(result);
                    } else {
                        mAdapter.addAll(result);
                    }
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
            public void onEmptyData() {
                showEmpty();
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<NoticeBean>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getNoticeList(ListType.MYSELF, pageIndex, pageSize, subscriber);

            }
        });

    }

    private void submitLike(final NoticeBean bean, final int position) {
        RequestNoticeLike body = new RequestNoticeLike();
        body.type = 2;
        body.noticeId = bean.noticeVo.noticeId;
        if (bean.likeFlag) {
            body.operation = 2;
        } else {
            body.operation = 1;
        }
        AppModelFactory.model().noticeLike(body, new NoneProgressSubscriber<Object>(mContext) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(Object o) {
                bean.likeFlag = !bean.likeFlag;
                if (bean.likeFlag) {
                    bean.likeCount = bean.likeCount + 1;
                } else {
                    if (bean.likeCount == 0) {
                        bean.likeCount = 0;
                    } else {
                        bean.likeCount = bean.likeCount - 1;
                    }
                }
                mAdapter.refreshLike(position, bean);
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast(ex.message);
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(mContext.getString(R.string.error_no_network));
            }
        });
    }

    private void submitFavorites(final NoticeBean bean, final int position) {
        RequestNoticeLike body = new RequestNoticeLike();
        body.type = 1;
        body.noticeId = bean.noticeVo.noticeId;
        if (bean.collectFlag) {
            body.operation = 2;
        } else {
            body.operation = 1;
        }
        AppModelFactory.model().noticeLike(body, new NoneProgressSubscriber<Object>(mContext) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(Object o) {
                bean.collectFlag = !bean.collectFlag;
                mAdapter.refreshFavorites(position, bean);
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast(ex.message);
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(mContext.getString(R.string.error_no_network));
            }
        });
    }

}
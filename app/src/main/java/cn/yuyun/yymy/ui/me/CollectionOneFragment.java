package cn.yuyun.yymy.ui.me;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ajguan.library.EasyRefreshLayout;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.NoneProgressSubscriber;
import com.example.http.PageInfo;
import com.example.http.Presenter;
import com.example.http.Presenter2;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseNoTitleFragment;
import cn.yuyun.yymy.bean.FavoritesStatus;
import cn.yuyun.yymy.bean.LikeStatus;
import cn.yuyun.yymy.bean.ListType;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestUnboxingLike;
import cn.yuyun.yymy.http.result.ResultUnboxingBean;
import cn.yuyun.yymy.http.result.ResultUnboxingLabel;
import cn.yuyun.yymy.ui.home.unboxing.UnboxingAdapter;
import cn.yuyun.yymy.ui.home.unboxing.UnboxingDetailActivity;
import cn.yuyun.yymy.utils.EvenManager;
import rx.Subscriber;

import static android.app.Activity.RESULT_OK;

/**
 * @author 我的收藏-晒单Fragment
 * @desc
 * @date 2018/1/15
 */
public class CollectionOneFragment extends BaseNoTitleFragment {

    private EasyRefreshLayout easyRefreshLayout;
    private RecyclerView recyclerView;
    private UnboxingAdapter mAdapter;
    private Presenter2<ResultUnboxingBean> presenter;
    private FloatingActionButton fab_add;

    private final int REQUEST_CODE = 1111;

    @Override
    protected int getTheLayoutId() {
        return R.layout.fragment_home_child_unboxing;
    }

    @Override
    protected void initViews(final View root) {
        super.initViews(root);
        EvenManager.register(this);
        easyRefreshLayout = (EasyRefreshLayout) root.findViewById(R.id.easylayout);
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setHasFixedSize(true);
        mAdapter = new UnboxingAdapter(mContext);
        mAdapter.setOnItemClickListener(new UnboxingAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultUnboxingBean bean, int position) {
                startActivityForResult(UnboxingDetailActivity.startUnboxingDetailActivityFromCollection(mContext, bean), REQUEST_CODE);
            }

            @Override
            public void onFavorites(ResultUnboxingBean bean, int position) {
                submitFavorites(bean, position);
            }

            @Override
            public void onLike(ResultUnboxingBean bean, int position) {
                submitLike(bean, position);
            }

            @Override
            public void onComment(ResultUnboxingBean bean, int position) {
                startActivityForResult(UnboxingDetailActivity.startUnboxingDetailActivityFromCollection(mContext, bean), REQUEST_CODE);
            }

            @Override
            public void onLabel(ResultUnboxingLabel.UnboxingLabelBean bean) {

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

    @Override
    protected void initData() {
        super.initData();
        configurePresenter();
        presenter.loadData(true);
    }

    private void submitLike(final ResultUnboxingBean bean, final int position) {
        RequestUnboxingLike body = new RequestUnboxingLike();
        body.type = 2;
        body.shareOrderId = bean.shareOrderVo.shareOrderId;
        if (bean.likeFlag) {
            body.operation = 2;
        } else {
            body.operation = 1;
        }
        AppModelFactory.model().unboxingLike(body, new NoneProgressSubscriber<Object>(mContext) {
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
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.message);
            }
        });
    }

    private void submitFavorites(final ResultUnboxingBean bean, final int position) {
        RequestUnboxingLike body = new RequestUnboxingLike();
        body.type = 1;
        body.shareOrderId = bean.shareOrderVo.shareOrderId;
        if (bean.collectFlag) {
            body.operation = 2;
        } else {
            body.operation = 1;
        }
        AppModelFactory.model().unboxingLike(body, new NoneProgressSubscriber<Object>(mContext) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(Object o) {
                mAdapter.remove(position);
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.message);
            }
        });
    }

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultUnboxingBean>() {

            @Override
            public void onSuccess(List<ResultUnboxingBean> result, int total, boolean isRefresh) {
                if (isRefresh) {
                    mAdapter.notifyDataSetChanged(result);
                } else {
                    mAdapter.addAll(result);
                }
            }

            @Override
            public void onFailed(boolean isRefresh) {
                easyRefreshLayout.refreshComplete();
                easyRefreshLayout.closeLoadView();
                showToast("加载失败，请稍候重试");
            }

            @Override
            public void onCompleted(boolean isRefresh) {
                easyRefreshLayout.refreshComplete();
                easyRefreshLayout.closeLoadView();
            }

            @Override
            public void onEmptyData() {
                mAdapter.clear();
                showEmpty();
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultUnboxingBean>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getUnboxingList(ListType.COLLECT, pageIndex, pageSize, subscriber);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if(notifyEvent.tag == NotifyEvent.COLLECTION_ONE){
            easyRefreshLayout.autoRefresh();
        }
    }

}


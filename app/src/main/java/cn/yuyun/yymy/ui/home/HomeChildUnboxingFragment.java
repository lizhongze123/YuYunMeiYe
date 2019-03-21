package cn.yuyun.yymy.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ajguan.library.EasyRefreshLayout;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.NoneProgressSubscriber;
import com.example.http.PageInfo;
import com.example.http.Presenter2;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.lzz.utils.DeviceUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseNoTitleFragment;
import cn.yuyun.yymy.bean.ListType;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.event.RefreshUnboxingEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestUnboxingLike;
import cn.yuyun.yymy.http.result.ResultUnboxingBean;
import cn.yuyun.yymy.http.result.ResultUnboxingLabel;
import cn.yuyun.yymy.ui.home.unboxing.FilterUnboxingActivity;
import cn.yuyun.yymy.ui.home.unboxing.PublishUnboxingActivity;
import cn.yuyun.yymy.ui.home.unboxing.UnboxingAdapter;
import cn.yuyun.yymy.ui.home.unboxing.UnboxingDetailActivity;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.utils.LruJsonCache;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

/**
 * @author
 * @desc
 * @date
 */
public class HomeChildUnboxingFragment extends BaseNoTitleFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;

    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;

    private UnboxingAdapter mAdapter;
    private Presenter2<ResultUnboxingBean> presenter;

    private final int REQUEST_CODE = 1111;
//    private boolean isHasCache;

    /**
     * 缓存框架
     */
    private LruJsonCache lruJsonCache;
    private String unboxingCacheKey = "unboxingList";

    public static HomeChildUnboxingFragment newInstance() {
        HomeChildUnboxingFragment fragment = new HomeChildUnboxingFragment();
        return fragment;
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        if (getArguments() != null) {

        }
    }

    @Override
    protected int getTheLayoutId() {
        return R.layout.fragment_home_child_unboxing;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(RefreshUnboxingEvent notifyEvent) {
        presenter.loadData(true);
    }

    @Override
    protected void initViews(View root) {
        super.initViews(root);
        EvenManager.register(this);
        root.findViewById(R.id.iv_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, PublishUnboxingActivity.class));
            }
        });
        emptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.loadData(true);
            }
        });
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(mContext));
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new UnboxingAdapter(mContext);
        mAdapter.setOnItemClickListener(new UnboxingAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultUnboxingBean bean, int position) {
                startActivityForResult(UnboxingDetailActivity.startUnboxingDetailActivity(mContext, bean), REQUEST_CODE);
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
                startActivityForResult(UnboxingDetailActivity.startUnboxingDetailActivity(mContext, bean), REQUEST_CODE);
            }

            @Override
            public void onLabel(ResultUnboxingLabel.UnboxingLabelBean bean) {
                List<ResultUnboxingLabel.UnboxingLabelBean> list = new ArrayList<>();
                list.add(bean);
                startActivity(FilterUnboxingActivity.startFilterUnboxingActivity(mContext, list));
            }
        });
        recyclerView.setAdapter(mAdapter);
        easyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                presenter.loadData(false);
//                if (isHasCache) {
//                    presenter.loadData(true);
//                } else {
//                    presenter.loadData(false);
//                }
            }

            @Override
            public void onRefreshing() {
                presenter.loadData(true);
            }
        });


    }

    private void submitFavorites(final ResultUnboxingBean bean, final int position) {
        RequestUnboxingLike body = new RequestUnboxingLike();
        body.type = 1;
        body.shareOrderId= bean.shareOrderVo.shareOrderId;
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
                bean.collectFlag = !bean.collectFlag;
                mAdapter.refreshFavorites(position, bean);
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.message);
            }
        });
    }


    private void submitLike(final ResultUnboxingBean bean, final int position) {
        RequestUnboxingLike body = new RequestUnboxingLike();
        body.type = 2;
        body.shareOrderId= bean.shareOrderVo.shareOrderId;
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

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultUnboxingBean>() {

            @Override
            public void onSuccess(List<ResultUnboxingBean> result, int total, boolean isRefresh) {
                //第2步：设置缓存数据，有效时间设置为1小时
//                rl_empty1.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.GONE);
                easyRefreshLayout.setVisibility(View.VISIBLE);
                if (isRefresh) {
                   /* isHasCache = false;
                    lruJsonCache.remove(unboxingCacheKey);
                    lruJsonCache.put(unboxingCacheKey, new Gson().toJson(result), 60 * 60 * 1);*/
                    mAdapter.notifyDataSetChanged(result);
                } else {
                    mAdapter.addAll(result);
                }
            }

            @Override
            public void onFailed(boolean isRefresh) {
                easyRefreshLayout.refreshComplete();
                easyRefreshLayout.closeLoadView();
                if (DeviceUtils.hasInternet(mContext)) {
                    showToast("加载失败，请稍候重试");
                }else{
                    emptyLayout.setVisibility(View.VISIBLE);
                    emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                }
            }

            @Override
            public void onCompleted(boolean isRefresh) {
                easyRefreshLayout.refreshComplete();
                easyRefreshLayout.closeLoadView();
            }

            @Override
            public void onEmptyData() {
                mAdapter.clear();
                emptyLayout.setVisibility(View.VISIBLE);
                emptyLayout.setErrorType(EmptyLayout.NODATA);
                easyRefreshLayout.setVisibility(View.GONE);
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultUnboxingBean>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getUnboxingList(ListType.ALL, pageIndex, pageSize, subscriber);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        configurePresenter();
        presenter.loadData(true);


       /* //第1步：创建LruJsonCache组件
        lruJsonCache = LruJsonCache.get(mContext);
        //第3步：从缓存中取数据
        String cacheData = lruJsonCache.getAsString(unboxingCacheKey);
        if (cacheData != null) {
            //如果缓存中有，就不访问网络
            //将json转为List
            List<ResultUnboxingBean> cacheList = new Gson().fromJson(cacheData, new TypeToken<List<ResultUnboxingBean>>() {
            }.getType());
            mAdapter.notifyDataSetChanged(cacheList);
            isHasCache = true;
        }*/
    }
}


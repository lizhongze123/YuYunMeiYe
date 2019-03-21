package cn.yuyun.yymy.ui.me;

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

import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseNoTitleFragment;
import cn.yuyun.yymy.bean.ListType;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestTrainLike;
import cn.yuyun.yymy.ui.home.train.TrainAdapter;
import cn.yuyun.yymy.ui.home.train.TrainBean;
import cn.yuyun.yymy.ui.home.train.TrainDetailActivity;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

/**
 * @author 我的收藏-培训Fragment
 * @desc
 * @date 2018/1/15
 */
public class CollectionFourFragment extends BaseNoTitleFragment {

    private Presenter2<TrainBean> presenter;

    private EasyRefreshLayout easyRefreshLayout;

    private RecyclerView recyclerView;

    private TrainAdapter mAdapter;

    private final int REQUEST_CODE = 1111;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if(notifyEvent.tag == NotifyEvent.COLLECTION_FOUR){
            easyRefreshLayout.autoRefresh();
        }
    }

    @Override
    protected int getTheLayoutId() {
        return R.layout.activity_base_list;
    }

    @Override
    protected void initViews(final View root) {
        super.initViews(root);
        EvenManager.register(this);
        easyRefreshLayout = (EasyRefreshLayout) root.findViewById(R.id.easylayout);
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(this.getContext()));
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);

        mAdapter = new TrainAdapter(this.getContext());
        mAdapter.setOnItemClickListener(new TrainAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, TrainBean bean, int position) {
                startActivity(TrainDetailActivity.startTrainDetailActivityFromCollection(mContext, bean));
            }

            @Override
            public void onFavorites(TrainBean bean, int position) {
                submitFavorites(bean, position);
            }

            @Override
            public void onLike(TrainBean bean, int position) {
                submitLike(bean, position);
            }

            @Override
            public void onComment(TrainBean bean, int position) {
                startActivity(TrainDetailActivity.startTrainDetailActivityFromCollection(mContext, bean));
            }

            @Override
            public void onDel(int count) {

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

    private void submitFavorites(final TrainBean bean, final int position) {
        RequestTrainLike body = new RequestTrainLike();
        body.type = 1;
        body.trainInfoId = bean.trainInfoVo.trainInfoId;
        if (bean.collectFlag) {
            body.operation = 2;
        } else {
            body.operation = 1;
        }
        AppModelFactory.model().trainLike(body, new NoneProgressSubscriber<Object>(mContext) {
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


    private void submitLike(final TrainBean bean, final int position) {
        RequestTrainLike body = new RequestTrainLike();
        body.type = 2;
        body.trainInfoId = bean.trainInfoVo.trainInfoId;
        if (bean.likeFlag) {
            body.operation = 2;
        } else {
            body.operation = 1;
        }
        AppModelFactory.model().trainLike(body, new NoneProgressSubscriber<Object>(mContext) {
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
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<TrainBean>(){

            @Override
            public void onSuccess(List<TrainBean> result, int total, boolean isRefresh) {
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
            public void onLoad(Subscriber<DataBean<PageInfo<TrainBean>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getTrainList(ListType.COLLECT, pageIndex, pageSize, subscriber);
            }
        });

    }


}


package cn.yuyun.yymy.ui.home.unboxing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ajguan.library.EasyRefreshLayout;
import com.example.http.ApiException;
import com.example.http.NoneProgressSubscriber;
import com.example.http.PageInfo;
import com.example.http.Presenter;

import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.base.BaseNoTitleFragment;
import cn.yuyun.yymy.bean.FavoritesStatus;
import cn.yuyun.yymy.bean.LikeStatus;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.ResultUnboxingBean;
import cn.yuyun.yymy.http.result.ResultUnboxingLabel;
import rx.Subscriber;

import static android.app.Activity.RESULT_OK;

/**
 * @author lzz
 * @desc 晒单界面
 * @date 2018/4/17
 */
public class UnboxingListFragment extends BaseNoTitleFragment{

    private EasyRefreshLayout easyRefreshLayout;
    private RecyclerView recyclerView;
    private UnboxingAdapter mAdapter;
    private Presenter<ResultUnboxingBean> presenter;

    private final int REQUEST_CODE = 1111;

    @Override
    protected int getTheLayoutId() {
        return R.layout.activity_base_list;
    }

    @Override
    protected void initViews(final View root) {
        super.initViews(root);
        titleBar.setTilte("晒单");
        titleBar.setTvRight("发布");
        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext, PublishUnboxingActivity.class), REQUEST_CODE);
            }
        });
        easyRefreshLayout = (EasyRefreshLayout) root.findViewById(R.id.easylayout);
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setHasFixedSize(true);
        mAdapter = new UnboxingAdapter(mContext);
        mAdapter.setOnItemClickListener(new UnboxingAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultUnboxingBean bean, int position) {

            }

            @Override
            public void onFavorites(ResultUnboxingBean bean, int position) {
                submitUnboxingFavorites(bean, position);
            }

            @Override
            public void onLike(ResultUnboxingBean bean, int position) {
                submitUnboxingLike(bean, position);
            }

            @Override
            public void onComment(ResultUnboxingBean bean, int position) {
                submitUnboxingFavorites(bean, position);
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

    private void submitUnboxingFavorites(final ResultUnboxingBean bean, final int position) {
        /*final FavoritesStatus status;
        if(bean.favoritesStatus == FavoritesStatus.Favorites){
            status = FavoritesStatus.UnFavorites;
        }else{
            status = FavoritesStatus.Favorites;
        }
        AppModelFactory.model().submitUnboxingFavorites(bean.id, status.value, new NoneProgressSubscriber<Object>(mContext) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(Object o) {
                bean.favoritesStatus = status;
                mAdapter.refreshFavorites(position, bean);
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.message);
            }
        });*/
    }

    private void submitUnboxingLike(final ResultUnboxingBean bean, final int position) {
       /* final LikeStatus status;
        if(bean.likeStatus == LikeStatus.Like){
            status = LikeStatus.UnLike;
        }else{
            status = LikeStatus.Like;
        }
        AppModelFactory.model().submitUnboxingLike(bean.id, status.value, new NoneProgressSubscriber<Object>(mContext) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(Object o) {
                bean.likeStatus = status;
                mAdapter.refreshLike(position, bean);
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.message);
            }
        });*/
    }

    private void configurePresenter() {
        /*presenter = new Presenter<>();
        presenter.setLoadDataListener(new Presenter.OnPresenterLoadListener<ResultUnboxingBean>(){

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
            public void onLoad(Subscriber<PageInfo<ResultUnboxingBean>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getUnboxingList(pageIndex, pageSize, subscriber);
            }
        });*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            easyRefreshLayout.autoRefresh();
        }
    }

}

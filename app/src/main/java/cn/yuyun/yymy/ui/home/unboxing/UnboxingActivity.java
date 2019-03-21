package cn.yuyun.yymy.ui.home.unboxing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.ajguan.library.EasyRefreshLayout;
import com.example.http.ApiException;
import com.example.http.NoneProgressSubscriber;
import com.example.http.PageInfo;
import com.example.http.Presenter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.bean.FavoritesStatus;
import cn.yuyun.yymy.bean.LikeStatus;
import cn.yuyun.yymy.event.RefreshUnboxingEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.ResultUnboxingBean;
import cn.yuyun.yymy.http.result.ResultUnboxingLabel;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.utils.LruJsonCache;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

/**
 * @author
 * @desc 晒单界面
 * @date
 */
public class UnboxingActivity extends BaseNoTitleActivity {

    private RelativeLayout rlOne;
    private RelativeLayout rlTwo;

    private EasyRefreshLayout easyRefreshLayout;
    private RecyclerView recyclerView;
    private UnboxingAdapter mAdapter;
    private Presenter<ResultUnboxingBean> presenter;

    private EasyRefreshLayout easyRefreshLayout2;
    private RecyclerView recyclerView2;
    private UnboxingAdapter mAdapter2;
    private Presenter<ResultUnboxingBean> presenter2;

    private RelativeLayout rl_empty2, rl_empty1;

    private final int REQUEST_CODE = 1111;
    private boolean isHasCache;


    /**
     * 缓存框架
     */
    private LruJsonCache lruJsonCache;
    private String unboxingCacheKey = "unboxingList";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_unboxing);
    }

    @Override
    protected void setUpViewAndData() {
        initViews();
        configurePresenter();
        configurePresenter2();

        //第1步：创建LruJsonCache组件
        lruJsonCache = LruJsonCache.get(this);
        //第3步：从缓存中取数据
        String cacheData = lruJsonCache.getAsString(unboxingCacheKey);
        if (cacheData != null) {
            //如果缓存中有，就不访问网络
            //将json转为List
            List<ResultUnboxingBean> cacheList = new Gson().fromJson(cacheData, new TypeToken<List<ResultUnboxingBean>>() {
            }.getType());
            mAdapter.notifyDataSetChanged(cacheList);
            isHasCache = true;
        }
        easyRefreshLayout.autoRefresh();
        presenter2.loadData(true);
    }

    private void initViews() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ((RadioGroup) findViewById(R.id.radioGroupTitle)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_one) {
                    rlOne.setVisibility(View.VISIBLE);
                    rlTwo.setVisibility(View.GONE);
                } else if (checkedId == R.id.rb_two) {
                    rlOne.setVisibility(View.GONE);
                    rlTwo.setVisibility(View.VISIBLE);
                }
            }
        });
        rlOne = (RelativeLayout) findViewById(R.id.rl_one);
        rlTwo = (RelativeLayout) findViewById(R.id.rl_two);
        findViewById(R.id.tv_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(LoginInfoPrefs.getInstance(mContext).getStaffId())) {
                    showTextDialog(getString(R.string.no_bind_staff), false);
                    return;
                }
                startActivityForResult(new Intent(mContext, PublishUnboxingActivity.class), REQUEST_CODE);
            }
        });
        initAll();
        initFavorites();
    }

    private void initFavorites() {
        rl_empty2 = (RelativeLayout) findViewById(R.id.rl_empty2);
        easyRefreshLayout2 = (EasyRefreshLayout) findViewById(R.id.easylayoutTwo);
        easyRefreshLayout2.setRefreshHeadView(new RefreshHeaderView(this));
        recyclerView2 = (RecyclerView) findViewById(R.id.recyclerViewTwo);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setHasFixedSize(true);
        mAdapter2 = new UnboxingAdapter(this);
        mAdapter2.setOnItemClickListener(new UnboxingAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultUnboxingBean bean, int position) {
                startActivity(UnboxingDetailActivity.startUnboxingDetailActivity(mContext, bean));
            }

            @Override
            public void onFavorites(ResultUnboxingBean bean, int position) {
                submitUnboxingFavorites(bean, position, 2);
            }

            @Override
            public void onLike(ResultUnboxingBean bean, int position) {
                submitUnboxingLike(bean, position, 2);
            }

            @Override
            public void onComment(ResultUnboxingBean bean, int position) {
                startActivity(UnboxingDetailActivity.startUnboxingDetailActivity(mContext, bean));
            }

            @Override
            public void onLabel(ResultUnboxingLabel.UnboxingLabelBean bean) {

            }
        });
        recyclerView2.setAdapter(mAdapter2);
        easyRefreshLayout2.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                presenter2.loadData(false);
            }

            @Override
            public void onRefreshing() {
                presenter2.loadData(true);
            }
        });
    }

    private void initAll() {
        rl_empty1 = (RelativeLayout) findViewById(R.id.rl_empty1);
        easyRefreshLayout = (EasyRefreshLayout) findViewById(R.id.easylayout);
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(this));
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mAdapter = new UnboxingAdapter(this);
        mAdapter.setOnItemClickListener(new UnboxingAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultUnboxingBean bean, int position) {
                startActivityForResult(UnboxingDetailActivity.startUnboxingDetailActivity(mContext, bean), REQUEST_CODE);
            }

            @Override
            public void onFavorites(ResultUnboxingBean bean, int position) {
                submitUnboxingFavorites(bean, position, 1);
            }

            @Override
            public void onLike(ResultUnboxingBean bean, int position) {
                submitUnboxingLike(bean, position, 1);
            }

            @Override
            public void onComment(ResultUnboxingBean bean, int position) {
                startActivityForResult(UnboxingDetailActivity.startUnboxingDetailActivity(mContext, bean), REQUEST_CODE);
            }

            @Override
            public void onLabel(ResultUnboxingLabel.UnboxingLabelBean bean) {

            }
        });
        recyclerView.setAdapter(mAdapter);
        easyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                if (isHasCache) {
                    presenter.loadData(true);
                } else {
                    presenter.loadData(false);
                }
            }

            @Override
            public void onRefreshing() {
                presenter.loadData(true);
            }
        });
    }

    private void submitUnboxingFavorites(final ResultUnboxingBean bean, final int position, final int type) {
       /* final FavoritesStatus status;
        if (bean.favoritesStatus == FavoritesStatus.Favorites) {
            status = FavoritesStatus.UnFavorites;
        } else {
            status = FavoritesStatus.Favorites;
        }
        AppModelFactory.model().submitUnboxingFavorites(bean.id, status.value, new NoneProgressSubscriber<Object>(mContext) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(Object o) {
                if (type == 1) {
                    bean.favoritesStatus = status;
                    mAdapter.refreshFavorites(position, bean);
                } else {
                    bean.favoritesStatus = status;
                    mAdapter2.refreshFavorites(position, bean);
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.getMessage());
            }
        });*/
    }

    private void submitUnboxingLike(final ResultUnboxingBean bean, final int position, final int type) {
       /* final LikeStatus status;
        if (bean.likeStatus == LikeStatus.Like) {
            status = LikeStatus.UnLike;
        } else {
            status = LikeStatus.Like;
        }
        AppModelFactory.model().submitUnboxingLike(bean.id, status.value, new NoneProgressSubscriber<Object>(mContext) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(Object o) {
                if (type == 1) {
                    bean.likeStatus = status;
                    if (status == LikeStatus.Like) {
                        bean.likeNumber = bean.likeNumber + 1;
                    } else {
                        if (bean.likeNumber == 0) {
                            bean.likeNumber = 0;
                        } else {
                            bean.likeNumber = bean.likeNumber - 1;
                        }
                    }
                    mAdapter.refreshLike(position, bean);
                } else {
                    bean.likeStatus = status;
                    if (status == LikeStatus.Like) {
                        bean.likeNumber = bean.likeNumber + 1;
                    } else {
                        if (bean.likeNumber == 0) {
                            bean.likeNumber = 0;
                        } else {
                            bean.likeNumber = bean.likeNumber - 1;
                        }
                    }
                    mAdapter2.refreshLike(position, bean);
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.message);
            }
        });*/
    }

    private void configurePresenter() {
        presenter = new Presenter<>();
        presenter.setLoadDataListener(new Presenter.OnPresenterLoadListener<ResultUnboxingBean>() {

            @Override
            public void onSuccess(List<ResultUnboxingBean> result, int total, boolean isRefresh) {
                //第2步：设置缓存数据，有效时间设置为1小时
                rl_empty1.setVisibility(View.GONE);
                if (isRefresh) {
                    isHasCache = false;
                    lruJsonCache.remove(unboxingCacheKey);
                    lruJsonCache.put(unboxingCacheKey, new Gson().toJson(result), 60 * 60 * 1);
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
                rl_empty1.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<PageInfo<ResultUnboxingBean>> subscriber, int pageIndex, int pageSize) {
//                AppModelFactory.model().getUnboxingList(pageIndex, pageSize, subscriber);
            }
        });
    }

    private void configurePresenter2() {
        presenter2 = new Presenter<>();
        presenter2.setLoadDataListener(new Presenter.OnPresenterLoadListener<ResultUnboxingBean>() {

            @Override
            public void onSuccess(List<ResultUnboxingBean> result, int total, boolean isRefresh) {
                rl_empty2.setVisibility(View.GONE);
                if (isRefresh) {
                    mAdapter2.notifyDataSetChanged(result);
                } else {
                    mAdapter2.addAll(result);
                }
            }

            @Override
            public void onFailed(boolean isRefresh) {
                showToast("加载失败，请稍候重试");
            }

            @Override
            public void onCompleted(boolean isRefresh) {
                easyRefreshLayout2.refreshComplete();
                easyRefreshLayout2.closeLoadView();
            }

            @Override
            public void onEmptyData() {
                mAdapter2.clear();
                rl_empty2.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<PageInfo<ResultUnboxingBean>> subscriber, int pageIndex, int pageSize) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            presenter.loadData(true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshUnboxingEvent(RefreshUnboxingEvent refreshUnboxingEvent) {
        presenter.loadData(true);
        presenter2.loadData(true);
    }

}

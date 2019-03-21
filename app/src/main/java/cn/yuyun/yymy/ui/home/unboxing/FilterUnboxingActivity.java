package cn.yuyun.yymy.ui.home.unboxing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.example.http.DataBean;
import com.example.http.PageInfo;
import com.example.http.Presenter;
import com.example.http.Presenter2;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.lzz.utils.DeviceUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.bean.ListType;
import cn.yuyun.yymy.event.RefreshUnboxingEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestFilterUnboxing;
import cn.yuyun.yymy.http.result.ResultUnboxingBean;
import cn.yuyun.yymy.http.result.ResultUnboxingLabel;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.utils.LruJsonCache;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc 晒单筛选后的界面
 * @date
 */
public class FilterUnboxingActivity extends BaseNoTitleActivity {

    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_select)
    TextView tvSelect;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;
    private UnboxingAdapter mAdapter;
    private Presenter2<ResultUnboxingBean> presenter;
    private final int REQUEST_CODE = 1111;
    private List<ResultUnboxingLabel.UnboxingLabelBean> dataList;
    private RequestFilterUnboxing requestBody = new RequestFilterUnboxing();

    public static Intent startFilterUnboxingActivity(Context context, List<ResultUnboxingLabel.UnboxingLabelBean> list) {
        return new Intent(context, FilterUnboxingActivity.class).putExtra(EXTRA_DATA, (Serializable) list);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_unboxing_filter);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        configurePresenter();
        if(null != dataList){
            List<Integer>list = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < dataList.size(); i++) {
                list.add(dataList.get(i).labelId);
                sb.append(dataList.get(i).labelName + ",");
            }
            String s = sb.substring(0, sb.length()-1);
            tvSelect.setText(s);
            requestBody.labelIds = list;
            easyRefreshLayout.autoRefresh();
        }
    }

    private void initViews() {
        dataList = (List<ResultUnboxingLabel.UnboxingLabelBean>) getIntent().getSerializableExtra(EXTRA_DATA);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initAll();
    }

    private void initAll() {
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                presenter.loadData(false);
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
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultUnboxingBean>() {

            @Override
            public void onSuccess(List<ResultUnboxingBean> result, int total, boolean isRefresh) {
                easyRefreshLayout.setVisibility(View.VISIBLE);
                emptyLayout.setVisibility(View.GONE);
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
                if (DeviceUtils.hasInternet(mContext)) {
                    showToast("加载失败，请稍候重试");
                }
            }

            @Override
            public void onCompleted(boolean isRefresh) {
                easyRefreshLayout.refreshComplete();
                easyRefreshLayout.closeLoadView();
            }

            @Override
            public void onEmptyData() {
                easyRefreshLayout.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
                emptyLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultUnboxingBean>>> subscriber, int pageIndex, int pageSize) {
                requestBody.pageIndex = pageIndex;
                requestBody.pageSize = pageSize;
                AppModelFactory.model().getUnboxingFilterList(requestBody, subscriber);
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
    }

}

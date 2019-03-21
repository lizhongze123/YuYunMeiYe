package cn.yuyun.yymy.ui.home.work;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.example.http.DataBean;
import com.example.http.PageInfo;
import com.example.http.Presenter2;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lzz.utils.DeviceUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestFilterWork;
import cn.yuyun.yymy.http.result.ResultWork;
import cn.yuyun.yymy.http.result.ResultWork2;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.me.entity.MemberBean;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author lzz
 * @desc 筛选后的工作汇报
 * @date
 */

public class ResultWorkActivity extends BaseActivity {

    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.empty)
    EmptyLayout emptyLayout;
    @BindView(R.id.rl_filter)
    LinearLayout rlFilter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;

    private Presenter2<ResultWork> presenter;

    private RequestFilterWork body;
    private ResultWorksAdapter mAdapter;

    public static Intent startResultWorkActivity (Context context, RequestFilterWork body){
        return new Intent(context, ResultWorkActivity.class).putExtra(EXTRA_DATA, body);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EvenManager.register(this);
        setContentView(R.layout.fragment_result_work);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initData();
    }

    private void initData() {
        configurePresenter();
        easyRefreshLayout.autoRefresh();
    }

    private void initViews() {
        titleBar.setTilte("汇报列表");
        body = (RequestFilterWork) getIntent().getSerializableExtra(EXTRA_DATA);
        rlFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new ResultWorksAdapter(mContext);
        mAdapter.setListener(new AdapterListener() {
           @Override
           public void onUserClicked() {

           }

           @Override
           public void onFavorites(ResultWork bean) {
               startActivity(MomentsDetailActivity.startFromOther(mContext, bean, UserfoPrefs.getInstance(mContext).getBaseonId()));
           }

           @Override
           public void onLike(ResultWork bean, int position) {
               startActivity(MomentsDetailActivity.startFromOther(mContext, bean, UserfoPrefs.getInstance(mContext).getBaseonId()));
           }

           @Override
           public void onComment(ResultWork bean, int position) {
               startActivity(MomentsDetailActivity.startFromOther(mContext, bean, UserfoPrefs.getInstance(mContext).getBaseonId()));
           }

           @Override
           public void onItemClicked(ResultWork bean, int position) {
               startActivity(MomentsDetailActivity.startFromOther(mContext, bean, UserfoPrefs.getInstance(mContext).getBaseonId()));
           }
       });
        recyclerView.setAdapter(mAdapter);
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(mContext));
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

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultWork>() {

            @Override
            public void onSuccess(final List<ResultWork> result, int total, final boolean isRefresh) {
                easyRefreshLayout.setVisibility(View.VISIBLE);
                emptyLayout.setVisibility(View.GONE);
                if (result != null) {
                    if (isRefresh) {
                        mAdapter.notifyDataSetChanged(result);
                    } else {
                        mAdapter.addAll(result);
                    }
                }
            }

            @Override
            public void onFailed(boolean isRefresh) {
                easyRefreshLayout.refreshComplete();
                easyRefreshLayout.closeLoadView();
                easyRefreshLayout.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
                emptyLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
            }

            @Override
            public void onCompleted(boolean isRefresh) {
                easyRefreshLayout.refreshComplete();
                easyRefreshLayout.closeLoadView();
            }

            @Override
            public void onEmptyData() {
                easyRefreshLayout.refreshComplete();
                easyRefreshLayout.closeLoadView();
                easyRefreshLayout.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
                emptyLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultWork>>> subscriber, int pageIndex, int pageSize) {
                body.pageIndex = pageIndex;
                body.pageSize = pageSize;
                AppModelFactory.model().filterWorkList(body, subscriber);
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if (notifyEvent.tag == NotifyEvent.WORK) {
            presenter.loadData(true);
        }
    }

}

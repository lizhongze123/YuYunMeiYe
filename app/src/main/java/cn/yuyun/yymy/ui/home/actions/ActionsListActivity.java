package cn.yuyun.yymy.ui.home.actions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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

import cn.lzz.utils.LogUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.bean.ListType;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestActionLike;
import cn.yuyun.yymy.http.result.ActionBean;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

/**
 * @author
 * @desc
 * @date
 */

public class ActionsListActivity extends BaseActivity {

    private Presenter2<ActionBean> presenter;

    private EasyRefreshLayout easyRefreshLayout;

    private RecyclerView recyclerView;

    private ActionsAdapter mAdapter;

    private final int REQUEST_CODE = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_list);
        EvenManager.register(this);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        configurePresenter();
        easyRefreshLayout.autoRefresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if (notifyEvent.tag == NotifyEvent.RREFRESH_DOT || notifyEvent.tag == NotifyEvent.RREFRESH) {
            presenter.loadData(true);
        }
    }

    private void initViews() {
        titleBar.setTilte("查看活动");

        if (UserfoPrefs.getInstance(mContext).getPermission()) {
            titleBar.setRightIcon(R.drawable.icon_add_two);
            titleBar.setRightOnClicker(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(LoginInfoPrefs.getInstance(mContext).getStaffId())) {
                        showTextDialog("该账号没有绑定员工", false);
                        return;
                    }
                    startActivityForResult(new Intent(mContext, AddActionActivity.class), REQUEST_CODE);
                }
            });
        }

        easyRefreshLayout = (EasyRefreshLayout) findViewById(R.id.easylayout);
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(this));
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        mAdapter = new ActionsAdapter(this);
        mAdapter.setOnItemClickListener(new ActionsAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ActionBean bean, int position) {
                startActivity(ActionsDetailActivity.startActionDetailActivity(mContext, bean));
                bean.readFlag = true;
                mAdapter.refreshRead(position, bean);
            }

            @Override
            public void onFavorites(ActionBean bean, int position) {
                submitFavorites(bean, position);
            }

            @Override
            public void onLike(ActionBean bean, int position) {
                submitLike(bean, position);
            }

            @Override
            public void onComment(ActionBean bean, int position) {
                startActivity(ActionsDetailActivity.startActionDetailActivity(mContext, bean));
                bean.readFlag = true;
                mAdapter.refreshRead(position, bean);
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
    public void onEmptyRefresh() {
        super.onEmptyRefresh();
        presenter.loadData(true);
    }

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ActionBean>() {

            @Override
            public void onSuccess(List<ActionBean> result, int total, boolean isRefresh) {
                if (null != result) {
                    showContent();
                    if (isRefresh) {
                        mAdapter.notifyDataSetChanged(result);
                    } else {
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
                showEmpty();
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ActionBean>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getActionsList(ListType.ALL, pageIndex, pageSize, subscriber);
            }
        });

    }

    private void submitLike(final ActionBean bean, final int position) {
        RequestActionLike body = new RequestActionLike();
        body.type = 2;
        body.latestActivityId = bean.latestActivityVo.latestActivityId;
        if (bean.likeFlag) {
            body.operation = 2;
        } else {
            body.operation = 1;
        }
        AppModelFactory.model().actionLike(body, new NoneProgressSubscriber<Object>(mContext) {
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


    private void submitFavorites(final ActionBean bean, final int position) {
        RequestActionLike body = new RequestActionLike();
        body.type = 1;
        body.latestActivityId = bean.latestActivityVo.latestActivityId;
        if (bean.collectFlag) {
            body.operation = 2;
        } else {
            body.operation = 1;
        }
        AppModelFactory.model().actionLike(body, new NoneProgressSubscriber<Object>(mContext) {
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

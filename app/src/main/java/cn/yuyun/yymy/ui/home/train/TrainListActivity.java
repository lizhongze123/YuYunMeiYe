package cn.yuyun.yymy.ui.home.train;

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

import butterknife.BindView;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.bean.ListType;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestTrainLike;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.home.actions.AddNoticeActivity;
import cn.yuyun.yymy.ui.home.notice.MyNoticeListActivity;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.dialog.BottomSelectDialogStaff;
import cn.yuyun.yymy.view.dialog.MoreDetailPopup;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

/**
 * @author
 * @desc
 * @date
 */

public class TrainListActivity extends BaseActivity {

    private Presenter2<TrainBean> presenter;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private TrainAdapter mAdapter;
    private final int REQUEST_CODE = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_list);
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
        titleBar.setTilte("培训资料");

        if (UserfoPrefs.getInstance(mContext).getPermission()) {
            titleBar.setRightIcon(R.drawable.icon_more);
            titleBar.setRightOnClicker(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(LoginInfoPrefs.getInstance(mContext).getStaffId())) {
                        showTextDialog("该账号没有绑定员工", false);
                        return;
                    }
                    new MoreDetailPopup(mContext, R.layout.popupwindow_train_list).setOnPopupClickListener(new MoreDetailPopup.OnPopupClickListener() {
                        @Override
                        public void onPositive(int pos) {
                            if(pos == 0){
                                startActivityForResult(new Intent(mContext, AddTrainActivity.class), REQUEST_CODE);
                            }else{
                                startActivity(new Intent(mContext, MyTrainListActivity.class));
                            }
                        }
                    }).showAtBottom(titleBar);

                }
            });
        }
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(this));
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        mAdapter = new TrainAdapter(this);
        mAdapter.setOnItemClickListener(new TrainAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, TrainBean bean, int position) {
                startActivity(TrainDetailActivity.startTrainDetailActivity(mContext, bean));
                bean.readFlag = true;
                mAdapter.refreshRead(position, bean);
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
                startActivity(TrainDetailActivity.startTrainDetailActivity(mContext, bean));
                bean.readFlag = true;
                mAdapter.refreshRead(position, bean);
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
            public void onLoad(Subscriber<DataBean<PageInfo<TrainBean>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getTrainList(ListType.ALL, pageIndex, pageSize, subscriber);
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
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            easyRefreshLayout.autoRefresh();
        }

    }

}

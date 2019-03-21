package cn.yuyun.yymy.ui.home.notice;

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
import com.example.http.Presenter;
import com.example.http.Presenter2;
import com.example.http.ProgressSubscriber;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.bean.ListType;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.event.RefreshUnboxingEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestNoticeLike;
import cn.yuyun.yymy.http.request.RequestRead;
import cn.yuyun.yymy.http.request.RequestTrainLike;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.home.actions.AddNoticeActivity;
import cn.yuyun.yymy.ui.home.actions.EditActionActivity;
import cn.yuyun.yymy.ui.home.train.TrainBean;
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

public class NoticeListActivity extends BaseActivity {

    private Presenter2<NoticeBean> presenter;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private final int REQUEST_CODE = 1111;
    private NoticeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        EvenManager.register(this);
        initViews();
        configurePresenter();
        easyRefreshLayout.autoRefresh();
    }

    private void initViews() {
        titleBar.setTilte("通知公告");
        if (UserfoPrefs.getInstance(mContext).getPermission()) {
            titleBar.setRightIcon(R.drawable.icon_more);
            titleBar.setRightOnClicker(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(LoginInfoPrefs.getInstance(mContext).getStaffId())) {
                        showTextDialog("该账号没有绑定员工", false);
                        return;
                    }
                    new MoreDetailPopup(mContext, R.layout.popupwindow_notice_list).setOnPopupClickListener(new MoreDetailPopup.OnPopupClickListener() {
                        @Override
                        public void onPositive(int pos) {
                            if(pos == 0){
                                startActivityForResult(new Intent(mContext, AddNoticeActivity.class), REQUEST_CODE);
                            }else{
                                startActivity(new Intent(mContext, MyNoticeListActivity.class));
                            }
                        }
                    }).showAtBottom(titleBar);

                }
            });
        }
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(this));
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        mAdapter = new NoticeAdapter(this);
        mAdapter.setOnItemClickListener(new NoticeAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, NoticeBean bean, int position) {
                startActivityForResult(NoticeDetailActivity.startNoticeDetailActivity(NoticeListActivity.this, bean), REQUEST_CODE);
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
                startActivityForResult(NoticeDetailActivity.startNoticeDetailActivity(NoticeListActivity.this, bean), REQUEST_CODE);
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

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<NoticeBean>() {

            @Override
            public void onSuccess(List<NoticeBean> result, int total, boolean isRefresh) {
                if (null != result) {
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
            public void onLoad(Subscriber<DataBean<PageInfo<NoticeBean>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getNoticeList(ListType.ALL, pageIndex, pageSize, subscriber);

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            easyRefreshLayout.autoRefresh();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshEvent(NotifyEvent notifyEvent) {
        if(notifyEvent.tag == NotifyEvent.RREFRESH_DOT){
            return;
        }
        presenter.loadData(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EvenManager.unregister(this);
    }
}

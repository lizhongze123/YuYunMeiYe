package cn.yuyun.yymy.ui.store.member;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ajguan.library.EasyRefreshLayout;
import com.example.http.DataBean;
import com.example.http.PageInfo;
import com.example.http.Presenter2;
import com.example.http.ProgressSubscriber;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestMemberFilter;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;

/**
 * @author
 * @desc 选择会员
 * @date
 */
public class SelectMemberListActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private String groupId;
    private List<String> tempSpIdList;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;

    private SelectMemberAdapter2 selectMemberAdapter;

    private Presenter2<ResultMemberBean> presenter;
    private RequestMemberFilter body = new RequestMemberFilter();

    public static Intent startSelectMemberListActivity(Context context, List<String> tempSpIdList , String groupId) {
        return new Intent(context, SelectMemberListActivity.class).putExtra(EXTRA_DATA2, (Serializable) tempSpIdList).putExtra(EXTRA_DATA, groupId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_member);
        EvenManager.register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        finish();
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    private void initDatas() {
        configurePresenter();
        body.spIdList = tempSpIdList;
        presenter.loadData(true);
    }

    private void initViews() {
        titleBar.setTilte("选择群发会员");
        titleBar.setTvRight("确定");
        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        groupId = getIntent().getStringExtra(EXTRA_DATA);
        tempSpIdList = (List<String>) getIntent().getSerializableExtra(EXTRA_DATA2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectMemberAdapter = new SelectMemberAdapter2(mContext);
        recyclerView.setAdapter(selectMemberAdapter);

        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(this));
        easyRefreshLayout.setEnablePullToRefresh(false);
        easyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                presenter.loadData(false);
            }

            @Override
            public void onRefreshing() {

            }
        });
    }

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultMemberBean>() {

            @Override
            public void onSuccess(List<ResultMemberBean> result, int total, final boolean isRefresh) {
                if (result != null) {
                    selectMemberAdapter.addAll(result);
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
            public void onLoad(Subscriber<DataBean<PageInfo<ResultMemberBean>>> subscriber, int pageIndex, int pageSize) {
                showContent();
                AppModelFactory.model().getMemberListOfStore(body, LoginInfoPrefs.getInstance(mContext).getGroupId(), pageIndex, pageSize, subscriber);
            }
        });
    }

    public void save() {
        if(selectMemberAdapter.getSelectedItem().size() == 0){
            showToast("请选择要发送的会员");
            return;
        }
       startActivity(new Intent(SendMessageActivity.startSendMessageActivity(mContext, selectMemberAdapter.getSelectedItem())));
    }
}

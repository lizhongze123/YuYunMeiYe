package cn.yuyun.yymy.ui.group;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ajguan.library.EasyRefreshLayout;
import com.example.http.DataBean;
import com.example.http.PageInfo;
import com.example.http.Presenter2;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestGroupList;
import cn.yuyun.yymy.http.request.RequestProject;
import cn.yuyun.yymy.http.result.ResultGroup;
import cn.yuyun.yymy.ui.me.SetActivity;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

/**
 * @author
 * @desc lzz
 * @date
 */

public class GroupListActivity extends BaseActivity {

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.rl_search)
    RelativeLayout rlSearch;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;
    @BindView(R.id.empty)
    EmptyLayout emptyLayout;

    private GroupListAdapter mAdapter;

    private Presenter2<ResultGroup> presenter;

    private RequestGroupList body = new RequestGroupList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
    }


    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        EvenManager.register(this);
        initViews();
        initData();
    }

    private void initData() {
        configurePresenter();
        easyRefreshLayout.autoRefresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if(notifyEvent.tag == NotifyEvent.LOGOUT){
            finish();
        }
    }

    private void initViews() {
        titleBar.setTilte("集团列表");
        titleBar.setLeftIcon(0);
        titleBar.setRightIcon(R.drawable.icon_title_set);
        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, SetActivity.class));
            }
        });

        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mAdapter = new GroupListAdapter(this);
        mAdapter.setOnItemClickListener(new GroupListAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(ResultGroup bean, int position) {
                startActivity(GroupActivity.startGroupActivity(mContext, bean));
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() <= 0) {
                    body.search_text = "";
                } else {
                    //网络搜索
                    body.search_text = s.toString();
                }
                presenter.loadData(true);
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
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultGroup>() {

            @Override
            public void onSuccess(List<ResultGroup> result, int total, boolean isRefresh) {
                emptyLayout.setVisibility(View.GONE);
                easyRefreshLayout.setVisibility(View.VISIBLE);
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
                if(mAdapter.getItemCount() > 0){
                    showToast("请求失败");
                }else{
                    easyRefreshLayout.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                    emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                }
            }

            @Override
            public void onEmptyData() {
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
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultGroup>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getGroupList(pageIndex, pageSize, body, subscriber);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EvenManager.unregister(this);
    }

    private boolean isExit = false;

    @Override
    public void onBackPressed() {
        if(isExit){
            super.onBackPressed();
        }else{
            isExit = true;
            showToast("再次点击将退出程序");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        }
    }
}

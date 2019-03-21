package cn.yuyun.yymy.ui.home.member.storemember;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.ajguan.library.EasyRefreshLayout;
import com.example.http.DataBean;
import com.example.http.PageInfo;
import com.example.http.Presenter2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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

/**
 * @author
 * @desc 门店下的会员列表
 * @date
 */

public class StoreMemberListActivity extends BaseActivity {

    @BindView(R.id.et_inputStore)
    EditText etInput;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;

    private Presenter2<ResultMemberBean> presenter;
    private StoreMemberListAdapter mAdapter;
    private String spId;
    private RequestMemberFilter body;

    public static Intent startMemberListActivity(Context context, String spId) {
        return new Intent(context, StoreMemberListActivity.class).putExtra(EXTRA_DATA, spId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_page);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
        configurePresenter();
        presenter.loadData(true);
    }

    private void initDatas() {
        body = new RequestMemberFilter();
        List<String> list = new ArrayList<>();
        list.add(spId);
        body.spIdList = list;
    }

    private void initViews() {
        titleBar.setTilte("会员列表");
        spId = getIntent().getStringExtra(EXTRA_DATA);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new StoreMemberListAdapter(mContext);
        recyclerView.setAdapter(mAdapter);
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(mContext));
        easyRefreshLayout.autoRefresh();
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
        mAdapter.setOnItemClickListener(new StoreMemberListAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultMemberBean bean, int position) {
                EvenManager.sendEvent(new NotifyEvent(bean, NotifyEvent.DATA));
                onBackPressed();
            }
        });
        etInput.addTextChangedListener(new TextWatcher() {
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
                    body.search_text = s.toString();
                }
                presenter.loadData(true);
            }
        });
    }

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultMemberBean>() {

            @Override
            public void onSuccess(List<ResultMemberBean> result, int total, final boolean isRefresh) {
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
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultMemberBean>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getMemberListOfStore(body, LoginInfoPrefs.getInstance(mContext).getGroupId(), pageIndex, pageSize, subscriber);
            }
        });
    }

}

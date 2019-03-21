package cn.yuyun.yymy.ui.home.member;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.example.http.DataBean;
import com.example.http.PageInfo;
import com.example.http.Presenter2;

import java.util.List;

import butterknife.BindView;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.http.result.ResultStoredvalueBean;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;

/**
 * @author lzz
 * @desc
 * @date
 */

public class StoredvalueListActivity extends BaseActivity {

    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_store)
    TextView tvStore;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;
    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;
    private Presenter2<ResultStoredvalueBean> presenter;

    private StoredvalueListAdapter mAdapter;

    private ResultMemberBean memberBean;
    private StoreBean storeBean;

    public static Intent startStoredvalueListActivity(Context context, ResultMemberBean memberBean, StoreBean storeBean) {
        return new Intent(context, StoredvalueListActivity.class).putExtra(EXTRA_DATA, memberBean).putExtra(EXTRA_DATA2, storeBean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storedvalue_list);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    private void initDatas() {
        configurePresenter();
        easyRefreshLayout.autoRefresh();
    }

    private void initViews() {
        titleBar.setTilte("储值列表");
        memberBean = (ResultMemberBean) getIntent().getSerializableExtra(EXTRA_DATA);
        storeBean = (StoreBean) getIntent().getSerializableExtra(EXTRA_DATA2);

        if (!TextUtils.isEmpty(memberBean.memberAvatar)) {
            if (memberBean.memberAvatar.startsWith(mContext.getString(R.string.HTTP))) {
                GlideHelper.displayImage(mContext, memberBean.memberAvatar, ivAvatar, memberBean.member_sex.resId);
            } else {
                GlideHelper.displayImage(mContext, getString(R.string.image_url_prefix) + memberBean.memberAvatar, ivAvatar, memberBean.member_sex.resId);
            }
        } else {
            GlideHelper.displayImage(mContext, memberBean.member_sex.resId, ivAvatar);
        }
        TextViewUtils.setTextOrEmpty(tvName, memberBean.memberName);
        if(TextUtils.isEmpty(memberBean.memberLevelName)){
            tvLevel.setVisibility(View.GONE);
        }else{
            tvLevel.setVisibility(View.VISIBLE);
            tvLevel.setText("("+ memberBean.memberLevelName + ")");
        }
        TextViewUtils.setTextOrEmpty(tvStore, "门店:" + memberBean.member_in_sp_name);
        mAdapter = new StoredvalueListAdapter(mContext);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(this));
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
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultStoredvalueBean>() {

            @Override
            public void onSuccess(List<ResultStoredvalueBean> result, int total, boolean isRefresh) {
                emptyLayout.setVisibility(View.GONE);
                easyRefreshLayout.setVisibility(View.VISIBLE);
                if (isRefresh) {
                    mAdapter.notifyDataSetChanged(result);
                } else {
                    mAdapter.addAll(result);
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
                emptyLayout.setVisibility(View.VISIBLE);
                emptyLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
                easyRefreshLayout.setVisibility(View.GONE);
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultStoredvalueBean>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getStoredvalueList(pageIndex, pageSize, storeBean.group_id, memberBean.memberId, subscriber);
            }
        });
    }


}

package cn.yuyun.yymy.ui.home.member;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;

import java.util.List;

import butterknife.BindView;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.bean.RecordType;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.me.entity.MemberBean;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;

/**
 * @author
 * @desc TA的推荐 onlyRefresh
 * @date
 */

public class RecommendListActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;

    private String memberId;
    private StoreBean storeBean;

    public RecordType type;

    private RecommendAdapter mAdapter;

    public static Intent startRecommendListActivity(Context context, String memberId, StoreBean storeBean) {
        return new Intent(context, RecommendListActivity.class).putExtra(EXTRA_DATA, memberId).putExtra(EXTRA_DATA2, storeBean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ta_recommend_list);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        memberId = getIntent().getStringExtra(EXTRA_DATA);
        storeBean = (StoreBean) getIntent().getSerializableExtra(EXTRA_DATA2);
        initViews();
    }

    private void initViews() {
        titleBar.setTilte("会员推荐");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecommendAdapter(this);
        recyclerView.setAdapter(mAdapter);
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(mContext));
        easyRefreshLayout.autoRefresh();
        easyRefreshLayout.setLoadMoreModel(LoadModel.NONE);
        easyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
            }

            @Override
            public void onRefreshing() {
                getData(memberId);
            }
        });

    }

    private void getData(String memberId) {
        AppModelFactory.model().getRecommendList(storeBean.group_id, memberId, new ProgressSubscriber<DataBean<List<MemberBean>>>(RecommendListActivity.this) {

            @Override
            public void onCompleted() {
                super.onCompleted();
                easyRefreshLayout.refreshComplete();
            }

            @Override
            public void onNext(DataBean<List<MemberBean>> result) {
                if (result != null) {
                    if(null != result.data && result.data.size() > 0){
                        mAdapter.notifyDataSetChanged(result.data);
                    }else{
                        showEmpty(EmptyLayout.NODATA_ENABLE_CLICK);
                    }
                }
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showError();
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast(ex.message);
            }
        });
    }

}

package cn.yuyun.yymy.ui.home.member.pmember;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.PageInfo;
import com.example.http.Presenter2;
import com.example.http.ProgressSubscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lzz.utils.DensityUtils;
import cn.lzz.utils.DeviceUtils;
import cn.lzz.utils.ResoureUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestPublicMember;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;
import cn.yuyun.yymy.http.result.ResultLevel;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.home.member.memberdata.MemberDetailActivity;
import cn.yuyun.yymy.ui.store.CardAdapter;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.ShadowDrawable;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc
 * @date
 */

public class PublicMemberListActivity extends BaseActivity{

    public DrawerLayout drawerLayout;
    private RelativeLayout main_right_drawer_layout;

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.rl_searchPublicMember)
    RelativeLayout rlSearchPublicMember;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.easylayout2)
    EasyRefreshLayout easylayout;
    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;

    @BindView(R.id.rv_card)
    RecyclerView rvCard;
    CardAdapter cardAdapter;
    @BindView(R.id.et1)
    EditText etOne;
    @BindView(R.id.et2)
    EditText etTwo;
    @BindView(R.id.tv_amount)
    TextView tvAmount;

    private RequestPublicMember requestPublicMember;
    private PublicMemberAdapter publicMemberAdapter;
    private Presenter2<ResultMemberBean> presenter;

    private ResultClassfiyStoreBean.OgServiceplacesRspListBean storeBean;

    public static Intent startPublicMemberListActivity(Context context, ResultClassfiyStoreBean.OgServiceplacesRspListBean bean) {
        return new Intent(context, PublicMemberListActivity.class).putExtra(EXTRA_DATA, bean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_member_list);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    private void initDatas() {
        configurePresenter();
        requestPublicMember = new RequestPublicMember();
        requestPublicMember.no_supervisor = 1;
        List<String>list = new ArrayList<>();
        list.add(storeBean.sp_id);
        requestPublicMember.spIdList = list;
        easylayout.autoRefresh();
    }

    private void initViews() {
        titleBar.setTilte("公共会员列表");
        storeBean = (ResultClassfiyStoreBean.OgServiceplacesRspListBean) getIntent().getSerializableExtra(EXTRA_DATA);
        ShadowDrawable.setShadowDrawable(llSearch, Color.parseColor("#ffffff"), DensityUtils.dip2px(mContext, 99),
                Color.parseColor("#80e0e0e0"),
                DensityUtils.dip2px(mContext, 5), 0, 0);
        easylayout.setRefreshHeadView(new RefreshHeaderView(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        easylayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                presenter.loadData(false);
            }

            @Override
            public void onRefreshing() {
                presenter.loadData(true);
            }
        });

        publicMemberAdapter = new PublicMemberAdapter(mContext);
        recyclerView.setAdapter(publicMemberAdapter);
        publicMemberAdapter.setOnItemClickListener(new PublicMemberAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultMemberBean bean, int position) {
                StoreBean storeBean = new StoreBean();
                storeBean.spId = bean.memberInSpId + "";
                storeBean.group_id = LoginInfoPrefs.getInstance(mContext).getGroupId();
                startActivity(MemberDetailActivity.startMemberDetailActivityFromPublic(mContext, bean.memberId, storeBean));
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
                    requestPublicMember.searchText = "";
                } else {
                    requestPublicMember.searchText = s.toString().trim();
                }
                presenter.loadData(true);
            }
        });
        initLayout();
    }

    public void initLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        //侧滑栏关闭手势滑动
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //设置菜单内容之外其他区域的背景色
        drawerLayout.setScrimColor(ResoureUtils.getColor(mContext, R.color.transparent_black));
        //左边菜单
        main_right_drawer_layout = (RelativeLayout) findViewById(R.id.main_right_drawer_layout);
        cardAdapter = new CardAdapter(mContext);
        rvCard.setLayoutManager(new GridLayoutManager(this, 2){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        cardAdapter.setOnItemClickListener(new CardAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultLevel bean, int position) {
                requestPublicMember.level_id = bean.levelId + "";
            }
        });
        rvCard.setAdapter(cardAdapter);
        getLevelData();
    }

    private void getLevelData() {

        AppModelFactory.model().getLevel(com.example.LoginInfoPrefs.getInstance(mContext).getGroupId(), new ProgressSubscriber<DataBean<List<ResultLevel>>>(mContext) {

            @Override
            public void onNext(DataBean<List<ResultLevel>> result) {
                if (null != result) {
                    cardAdapter.addAll(result.data);
                }
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast("请求失败，请稍后重试");
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(mContext.getString(R.string.error_no_network));
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });
    }

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultMemberBean>() {

            @Override
            public void onSuccess(List<ResultMemberBean> result, int total, final boolean isRefresh) {
                tvAmount.setText("公共会员" + total + "人");
                if (null != result) {
                    emptyLayout.setVisibility(View.GONE);
                    if (isRefresh) {
                        publicMemberAdapter.notifyDataSetChanged(result);
                    } else {
                        publicMemberAdapter.addAll(result);
                    }
                }
            }

            @Override
            public void onFailed(boolean isRefresh) {
                publicMemberAdapter.clear();
                emptyLayout.setVisibility(View.VISIBLE);
                emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                tvAmount.setText("公共会员0人");
            }

            @Override
            public void onCompleted(boolean isRefresh) {
                easylayout.refreshComplete();
                easylayout.closeLoadView();
            }

            @Override
            public void onEmptyData() {
                publicMemberAdapter.clear();
                emptyLayout.setVisibility(View.VISIBLE);
                emptyLayout.setErrorType(EmptyLayout.NODATA);
                tvAmount.setText("公共会员0人");
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultMemberBean>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getPublicMemberList(requestPublicMember, LoginInfoPrefs.getInstance(mContext).getGroupId(), pageIndex, pageSize, subscriber);
            }
        });
    }

    @OnClick(R.id.ll_filter)
    public void filter(View v) {
        DeviceUtils.hideSoftKeyboard(v, mContext);
        openRightLayout(v);
    }

    @OnClick(R.id.tv_positive)
    public void positive(View v) {
        openRightLayout(v);
        requestPublicMember.days_till_last_consume_time_start = etOne.getText().toString().trim();
        requestPublicMember.days_till_last_consume_time_end = etTwo.getText().toString().trim();
        presenter.loadData(true);
    }

    @OnClick(R.id.tv_negative)
    public void negative(View v) {
        requestPublicMember.days_till_last_consume_time_start = "";
        requestPublicMember.days_till_last_consume_time_start = "";
        etOne.setText("");
        etTwo.setText("");
        cardAdapter.setSelectPos(-1);
        requestPublicMember.level_id = "";
    }

    public void openRightLayout(View view) {
        if (drawerLayout.isDrawerOpen(main_right_drawer_layout)) {
            drawerLayout.closeDrawer(main_right_drawer_layout);
        } else {
            drawerLayout.openDrawer(main_right_drawer_layout);
        }
    }
}

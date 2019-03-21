package cn.yuyun.yymy.ui.home.cashier;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
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

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lzz.utils.DensityUtils;
import cn.lzz.utils.DeviceUtils;
import cn.lzz.utils.ResoureUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestMemberFilter;
import cn.yuyun.yymy.http.result.ResultLevel;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.ui.home.member.AddMemberActivity;
import cn.yuyun.yymy.ui.home.member.storemember.StoreMemberListAdapter;
import cn.yuyun.yymy.ui.store.CardAdapter;
import cn.yuyun.yymy.utils.EvenManager;
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

public class SelectCashierMemberActivity extends BaseActivity{

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
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_amountFilter)
    TextView tvAmountFilter;
    private String tempLevelName;

    private StoreMemberListAdapter mAdapter;
    private Presenter2<ResultMemberBean> presenter;
    private RequestMemberFilter body;
    private String spId;

    public static Intent startSelectCashierMemberActivity(Context context,  String spId) {
        return new Intent(context, SelectCashierMemberActivity.class).putExtra(EXTRA_DATA, spId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_member_list);
        EvenManager.register(this);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if(notifyEvent.tag == NotifyEvent.FINISH_CASHIER ){
            finish();
        }else if(notifyEvent.tag == NotifyEvent.RREFRESH){
            presenter.loadData(true);
        }
    }

    private void initDatas() {
        configurePresenter();
        body = new RequestMemberFilter();
        List<String> list = new ArrayList<>();
        list.add(spId);
        body.spIdList = list;
        easylayout.autoRefresh();
    }

    private void initViews() {
        titleBar.setTilte("会员列表");
        titleBar.setRightIcon(R.drawable.icon_add_two);
        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AddMemberActivity.startAddMemberActivityForCashier(mContext));
            }
        });
        spId = getIntent().getStringExtra(EXTRA_DATA);
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

        mAdapter = new StoreMemberListAdapter(mContext);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new StoreMemberListAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultMemberBean bean, int position) {
                startActivity(CashierMemberDetailActivity.startCashierMemberDetailActivity(mContext, bean.memberId));
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
                    body.search_text = s.toString();
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
                body.level_id = bean.levelId + "";
                tempLevelName = bean.name;
            }
        });
        rvCard.setAdapter(cardAdapter);
        getLevelData();
    }

    private void getLevelData() {

        AppModelFactory.model().getLevel(LoginInfoPrefs.getInstance(mContext).getGroupId(), new ProgressSubscriber<DataBean<List<ResultLevel>>>(mContext) {

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

    private int totalMember;

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultMemberBean>() {

            @Override
            public void onSuccess(List<ResultMemberBean> result, int total, final boolean isRefresh) {
                if (null != result) {
                    easylayout.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                    if (TextUtils.isEmpty(body.level_id)) {
                        totalMember = total;
                        tvAmount.setText("门店会员" + total + "人");
                    }
                    setNumber(total);
                    if (isRefresh) {
                        mAdapter.notifyDataSetChanged(result);
                    } else {
                        mAdapter.addAll(result);
                    }
                }
            }

            @Override
            public void onFailed(boolean isRefresh) {
                mAdapter.clear();
                emptyLayout.setVisibility(View.VISIBLE);
                emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                tvAmount.setText("门店会员0人");
            }

            @Override
            public void onCompleted(boolean isRefresh) {
                easylayout.refreshComplete();
                easylayout.closeLoadView();
            }

            @Override
            public void onEmptyData() {
                mAdapter.clear();
                emptyLayout.setVisibility(View.VISIBLE);
                emptyLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
                tvAmount.setText("门店会员0人");
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

    private void setNumber(int total) {
        if(!TextUtils.isEmpty(tempLevelName)){
            tvAmountFilter.setText(tempLevelName + total + "人");
        }else{
            tvAmountFilter.setText("");
        }

        tvAmountFilter.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tvAmountFilter.setSingleLine(true);
        tvAmountFilter.setSelected(true);
        tvAmountFilter.setFocusable(true);
        tvAmountFilter.setFocusableInTouchMode(true);
    }

    @OnClick(R.id.ll_filter)
    public void filter(View v) {
        DeviceUtils.hideSoftKeyboard(v, mContext);
        openRightLayout(v);
    }

    @OnClick(R.id.tv_positive)
    public void positive(View v) {
        openRightLayout(v);
        easylayout.autoRefresh();
    }

    @OnClick(R.id.tv_negative)
    public void negative(View v) {
        cardAdapter.setSelectPos(-1);
        body.level_id = "";
    }

    public void openRightLayout(View view) {
        if (drawerLayout.isDrawerOpen(main_right_drawer_layout)) {
            drawerLayout.closeDrawer(main_right_drawer_layout);
        } else {
            drawerLayout.openDrawer(main_right_drawer_layout);
        }
    }

    @Override
    protected void onDestroy() {
        EvenManager.unregister(this);
        super.onDestroy();
    }
}

package cn.yuyun.yymy.ui.home.member;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.NoneProgressSubscriber;
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
import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestClassfiyStore;
import cn.yuyun.yymy.http.request.RequestMyMember;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;
import cn.yuyun.yymy.http.result.ResultLevel;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.http.result.WarnningMemberBean;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.home.leave.SelectStoreListLeftAdapter;
import cn.yuyun.yymy.ui.home.leave.SelectStoreListRightAdapter;
import cn.yuyun.yymy.ui.home.member.memberdata.MemberDetailActivity;
import cn.yuyun.yymy.ui.home.member.pmember.PublicMemberListActivity;
import cn.yuyun.yymy.ui.store.CardAdapter;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.HTAlertDialog;
import cn.yuyun.yymy.view.ShadowDrawable;
import cn.yuyun.yymy.view.dialog.BirthdayDialog;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

/**
 * @author
 * @desc
 * @date
 */

public class MemberManagerActivity extends BaseNoTitleActivity {

    @BindView(R.id.main_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.main_right_drawer_layout)
    RelativeLayout main_right_drawer_layout;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.rb_one)
    RadioButton rbOne;
    @BindView(R.id.rb_two)
    RadioButton rbTwo;
    @BindView(R.id.radioGroupTitle)
    RadioGroup radioGroupTitle;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.iv_gift)
    ImageView ivGift;
    @BindView(R.id.rv_member)
    RecyclerView rvMember;
    @BindView(R.id.ll_searchMyMember)
    LinearLayout llSearchMyMember;
    @BindView(R.id.ll_store)
    LinearLayout llStore;

    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;

    @BindView(R.id.rl_one)
    RelativeLayout rlOne;
    @BindView(R.id.ll_two)
    LinearLayout llTwo;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easylayout;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.rv_card)
    RecyclerView rvCard;
    CardAdapter cardAdapter;

    @BindView(R.id.et_inputStore)
    EditText etInputStore;
    @BindView(R.id.et_input)
    EditText etInput;
    private TextWatcher memberTextWatcher;

    private SortMyMemberAdapter mAdapter;
    private final int REQUEST_CODE = 1111;

    @BindView(R.id.rv_left)
    RecyclerView rvLeft;
    @BindView(R.id.rv_right)
    RecyclerView rvRight;
    private SelectStoreListRightAdapter rightAdapter;
    private SelectStoreListLeftAdapter leftAdapter;
    /**门店数据*/
    private List<ResultClassfiyStoreBean> classfiyStoreList = new ArrayList<>();

    private String levelName;

    private Presenter2<ResultMemberBean> presenter;
    private Presenter2<ResultMemberBean> presenter2;
    private RequestMyMember requestMyMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_manage);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        EvenManager.register(this);
        initViews();
    }

    private void initViews() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        radioGroupTitle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_one) {
                    rlOne.setVisibility(View.VISIBLE);
                    llTwo.setVisibility(View.GONE);
                    ivAdd.setVisibility(View.VISIBLE);
                    ivGift.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.rb_two) {
                    rlOne.setVisibility(View.GONE);
                    llTwo.setVisibility(View.VISIBLE);
                    ivAdd.setVisibility(View.GONE);
                    ivGift.setVisibility(View.GONE);
                }
            }
        });
        ivGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter2.loadData(true);
            }
        });
        initMyselfView();
        initStoreView();
    }

    private void initStoreView() {
        ShadowDrawable.setShadowDrawable(llStore, Color.parseColor("#ffffff"), DensityUtils.dip2px(mContext, 99),
                Color.parseColor("#80e0e0e0"),
                DensityUtils.dip2px(mContext, 5), 0, 0);
        rvLeft.setLayoutManager(new LinearLayoutManager(this));
        rvRight.setLayoutManager(new LinearLayoutManager(this));
        rightAdapter = new SelectStoreListRightAdapter(this);
        leftAdapter = new SelectStoreListLeftAdapter(this);
        rvLeft.setAdapter(leftAdapter);
        rvRight.setAdapter(rightAdapter);
        leftAdapter.setOnItemClickListener(new SelectStoreListLeftAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultClassfiyStoreBean bean, int position) {
                rightAdapter.setIndex(position);
                leftAdapter.setCheckedPosition(position);
            }
        });
        rightAdapter.setOnItemClickListener(new SelectStoreListRightAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultClassfiyStoreBean.OgServiceplacesRspListBean bean, int position) {
                startActivity(PublicMemberListActivity.startPublicMemberListActivity(mContext, bean));
            }
        });

        etInputStore.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() <= 0) {
                    leftAdapter.notifyDataSetChanged(classfiyStoreList);
                    rightAdapter.notifyDataSetChanged(classfiyStoreList);
                }else {
                    //本地搜索
                    localSearch(s.toString());
                }
            }
        });

        getClassfiyStoreList();
    }

    private void localSearch(String text) {
        //返回数据
        List<ResultClassfiyStoreBean> filterclassfiyStoreList = new ArrayList<>();
        for (ResultClassfiyStoreBean bean : classfiyStoreList) {
            ResultClassfiyStoreBean bean1 = new ResultClassfiyStoreBean();
            bean1.name = bean.name;
            bean1.group_id = bean.group_id;
            bean1.id = bean.id;
            List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> ogList = new ArrayList<>();
            for (ResultClassfiyStoreBean.OgServiceplacesRspListBean ogServiceplacesRspListBean : bean.ogServiceplacesRspList) {
                if(ogServiceplacesRspListBean.sp_name.indexOf(text) >= 0){
                    ogList.add(ogServiceplacesRspListBean);
                }
            }
            if(ogList.size() > 0){
                bean1.ogServiceplacesRspList = ogList;
                filterclassfiyStoreList.add(bean1);
            }
        }

        leftAdapter.notifyDataSetChanged(filterclassfiyStoreList);
        rightAdapter.notifyDataSetChanged(filterclassfiyStoreList);
    }

    public void initLayout() {
        //侧滑栏关闭手势滑动
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //设置菜单内容之外其他区域的背景色
        drawerLayout.setScrimColor(ResoureUtils.getColor(mContext, R.color.transparent_black));
        //左边菜单
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
                requestMyMember.level_id = bean.levelId + "";
                levelName = bean.name;
            }
        });
        rvCard.setAdapter(cardAdapter);
        getLevelData();
    }

    private void getClassfiyStoreList() {
        RequestClassfiyStore requestClassfiyStore = new RequestClassfiyStore();
        AppModelFactory.model().getMyStoreList(LoginInfoPrefs.getInstance(mContext).getGroupId(), requestClassfiyStore, new NoneProgressSubscriber<DataBean<List<ResultClassfiyStoreBean>>>(mContext) {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(DataBean<List<ResultClassfiyStoreBean>> result) {
                if (null != result) {
                    if (null != result.data) {
                        //头部大区数据
                        List<ResultClassfiyStoreBean> dataList = new ArrayList<>();
                        for (int i = 0, iLength = result.data.size(); i < iLength; i++) {
                            //界面显示:只有大区下有门店才添加进来
                            if (result.data.get(i).ogServiceplacesRspList.size() > 0) {
                                dataList.add(result.data.get(i));
                            }
                        }
                        leftAdapter.notifyDataSetChanged(dataList);
                        rightAdapter.notifyDataSetChanged(dataList);
                        classfiyStoreList.addAll(dataList);

                    }
                }
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(mContext.getString(R.string.error_no_network));
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast(ex.message);
            }
        });
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
            public void onError(ApiException ex) {
                super.onError(ex);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if(notifyEvent.tag == NotifyEvent.RREFRESH){
            presenter.loadData(true);
        }
    }

    private void initMyselfView() {
        ShadowDrawable.setShadowDrawable(llSearchMyMember, Color.parseColor("#ffffff"), DensityUtils.dip2px(mContext, 99),
                Color.parseColor("#80e0e0e0"),
                DensityUtils.dip2px(mContext, 5), 0, 0);
        findViewById(R.id.iv_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(AddMemberActivity.startAddMemberActivityForNormal(mContext), REQUEST_CODE);
            }
        });
        mAdapter = new SortMyMemberAdapter(mContext);
        rvMember.setLayoutManager(new LinearLayoutManager(this));
        rvMember.setAdapter(mAdapter);
        // 设置字母导航触摸监听
        easylayout.setRefreshHeadView(new RefreshHeaderView(this));
        easylayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                presenter.loadData(false);
            }

            @Override
            public void onRefreshing() {
                cardAdapter.setSelectPos(-1);
                requestMyMember.level_id = "";
                levelName = "";
                presenter.loadData(true);
            }
        });

        mAdapter.setOnItemClickListener(new SortMyMemberAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultMemberBean bean, int position) {
                StoreBean storeBean = new StoreBean();
                storeBean.spId = bean.memberInSpId + "";
                storeBean.group_id = LoginInfoPrefs.getInstance(mContext).getGroupId();
                startActivity(MemberDetailActivity.startMemberDetailActivityWithId(mContext, bean.memberId, storeBean));
            }
        });
        memberTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() <= 0) {
                    requestMyMember.searchText = "";
                } else {
                    requestMyMember.searchText = s.toString().trim();
                }
                presenter.loadData(true);
            }
        };
        etInput.addTextChangedListener(memberTextWatcher);
        configurePresenter();
        configurePresenter2();
        requestMyMember = new RequestMyMember();
        requestMyMember.staff_id_list = new ArrayList<>();
        requestMyMember.staff_id_list.add(LoginInfoPrefs.getInstance(mContext).getStaffId());
        easylayout.autoRefresh();
        initLayout();
    }

    public void showItemDialog(final WarnningMemberBean bean, final int position) {
        final HTAlertDialog htAlertDialog = new HTAlertDialog(mContext, null, new String[]{getString(R.string.unbind)});
        htAlertDialog.init(new HTAlertDialog.OnItemClickListner() {
            @Override
            public void onClick(int pos) {
                if (pos == 0) {

                }
            }
        });
    }

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultMemberBean>() {

            @Override
            public void onSuccess(List<ResultMemberBean> result, int total, final boolean isRefresh) {
                tvAmount.setText("我的会员" + total + "人");
                emptyLayout.setVisibility(View.GONE);

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
                mAdapter.clear();
                emptyLayout.setVisibility(View.VISIBLE);
                emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                tvAmount.setText("我的会员0人");
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
                tvAmount.setText("我的会员0人");
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultMemberBean>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getMyMemberList(LoginInfoPrefs.getInstance(mContext).getGroupId(), pageIndex, pageSize, requestMyMember, subscriber);
            }
        });
    }

    private void configurePresenter2() {
        final RequestMyMember body = new RequestMyMember();
        body.staff_id_list = new ArrayList<>();
        body.staff_id_list.add(LoginInfoPrefs.getInstance(mContext).getStaffId());
        body.birth_day_tils = "7";
        presenter2 = new Presenter2<>();
        presenter2.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultMemberBean>() {

            @Override
            public void onSuccess(final List<ResultMemberBean> result, int total, final boolean isRefresh) {
                final BirthdayDialog dialog = new BirthdayDialog(mContext);
                dialog.setOnPositiveListener(new BirthdayDialog.OnPositiveListener() {
                    @Override
                    public void onPositive() {
                        dialog.dismiss();
                        startActivity(SendBirthdayMessageActivity.startSendBirthdayMessageActivity(mContext, result));
                    }
                });
                dialog.show();
                dialog.setTips("亲，你有" + total +"位尊贵的会员即将过生日，在这美好时刻，快点送上祝福吧");
            }

            @Override
            public void onFailed(boolean isRefresh) {
                showToast("近期没有过生日的会员");
            }

            @Override
            public void onCompleted(boolean isRefresh) {

            }

            @Override
            public void onEmptyData() {
                showToast("近期没有过生日的会员");
            }

            @Override
            public void onNoneMoreData() {

            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultMemberBean>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getMyMemberList(LoginInfoPrefs.getInstance(mContext).getGroupId(), pageIndex, pageSize, body, subscriber);
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
        presenter.loadData(true);
    }

    @OnClick(R.id.tv_negative)
    public void negative(View v) {
        cardAdapter.setSelectPos(-1);
        requestMyMember.level_id = "";
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
        super.onDestroy();
        EvenManager.unregister(this);
    }
}

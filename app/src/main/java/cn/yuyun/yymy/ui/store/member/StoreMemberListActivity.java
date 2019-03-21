package cn.yuyun.yymy.ui.store.member;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.PageInfo;
import com.example.http.Presenter2;
import com.example.http.ProgressSubscriber;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lzz.utils.DeviceUtils;
import cn.lzz.utils.ResoureUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.constan.Constans;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestMemberFilter;
import cn.yuyun.yymy.http.request.RequestModifyMemberStatus;
import cn.yuyun.yymy.http.result.ResultLevel;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.home.member.memberdata.MemberDetailActivity;
import cn.yuyun.yymy.ui.store.CardAdapter;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.dialog.BottomSelectDialogMember;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc 本店会员列表
 * @date
 */
public class StoreMemberListActivity extends BaseActivity {

    @BindView(R.id.main_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.main_right_drawer_layout)
    RelativeLayout main_right_drawer_layout;

    private Presenter2<ResultMemberBean> presenter;

    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;
    private StoreMemberAdapter storeMemberAdapter;
    private RequestMemberFilter body = new RequestMemberFilter();

    @BindView(R.id.recyclerView)
    SwipeMenuRecyclerView swipeRecyclerView;

    private StoreBean storeBean;

    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.rv_card)
    RecyclerView rvCard;
    CardAdapter cardAdapter;
    private BottomSelectDialogMember dialog;

    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_amountFilter)
    TextView tvAmountFilter;
    @BindView(R.id.et1)
    EditText etOne;
    @BindView(R.id.et2)
    EditText etTwo;

    private String tempName = "";
    private String tempLevelName;
    private int totalMember;

    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;
    private List<String> list = new ArrayList<>();

    public static Intent startMemberAnalysisActivity(Context context, StoreBean bean) {
        return new Intent(context, StoreMemberListActivity.class).putExtra(EXTRA_DATA, bean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_member_list);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        EvenManager.register(this);
        initViews();
        configurePresenter();
        initDatas();
    }

    private void initDatas() {
        easyRefreshLayout.autoRefresh();
        getLevelData();
    }

    private void initViews() {
        titleBar.setTilte("本店会员列表");
        titleBar.setTvRight("更多");
        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDialog();
            }
        });
        storeBean = (StoreBean) getIntent().getSerializableExtra(EXTRA_DATA);
        list.add(storeBean.spId);
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(this));
        swipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRecyclerView.setHasFixedSize(true);
        easyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                presenter.loadData(false);
            }

            @Override
            public void onRefreshing() {
                body.search_text = "";
                presenter.loadData(true);
            }
        });

        storeMemberAdapter = new StoreMemberAdapter(mContext);
        storeMemberAdapter.setOnItemClickListener(new StoreMemberAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultMemberBean bean, int position) {
                startActivity(MemberDetailActivity.startMemberDetailActivityWithId(mContext, bean.memberId, storeBean));
            }
        });
        if (Constans.isStoreLoginer) {
            // 创建菜单
            SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
                @Override
                public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int viewType) {
                    int height = ViewGroup.LayoutParams.MATCH_PARENT;
                    int width = getResources().getDimensionPixelSize(R.dimen.swipe_item);
                    SwipeMenuItem addItem = new SwipeMenuItem(mContext)
                            .setBackground(R.color.text_red)
                            .setText("删除")
                            .setTextColor(Color.WHITE)
                            .setWidth(width)
                            .setHeight(height);
                    // 添加菜单到左侧。
                    rightMenu.addMenuItem(addItem);
                    // 注意：哪边不想要菜单，那么不要添加即可。
                }
            };
            // 设置监听器。
            swipeRecyclerView.setSwipeMenuCreator(mSwipeMenuCreator);
            // 设置菜单Item点击监听。
            swipeRecyclerView.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
                @Override
                public void onItemClick(SwipeMenuBridge menuBridge) {
                    menuBridge.closeMenu();
                    // 左侧还是右侧菜单。
                    int direction = menuBridge.getDirection();
                    // RecyclerView的Item的position。
                    int adapterPosition = menuBridge.getAdapterPosition();
                    // 菜单在RecyclerView的Item中的Position。
                    int menuPosition = menuBridge.getPosition();
                    modifyMemberStatus(storeMemberAdapter.getItemBean(adapterPosition));
                }
            });
        }
        swipeRecyclerView.setAdapter(storeMemberAdapter);
        initLayout();
        initDialog();
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(etInput.getText().toString())) {
//                        reset();
                        body.search_text = etInput.getText().toString();
                    } else {
                        body.search_text = "";
                    }
                    presenter.loadData(true);
                }
                return false;
            }
        });
    }

    private void initDialog() {
        dialog = new BottomSelectDialogMember(this, R.style.select_MyDialogStyle);
        dialog.setItemClickListener(new BottomSelectDialogMember.ItemClickListener() {

            @Override
            public void oClick(int item) {
                switch (item) {
                    case 0:
                        dialog.dismiss();
                        break;
                    case 1:
                        if (Constans.isStoreLoginer) {
                            startActivity(AddStoreMemberActivity.startAddStoreMemberActivity(mContext, storeBean));
                        } else {
                            showToast(getString(R.string.PARTNER));
                        }
                        dialog.dismiss();
                        break;
                    case 2:
                        if (Constans.isStoreLoginer) {

                            startActivity(SelectMemberListActivity.startSelectMemberListActivity(mContext, list, storeBean.group_id));
                        } else {
                            showToast(getString(R.string.PARTNER));
                        }
                        dialog.dismiss();
                        break;
                    case 3:
                        startActivity(RecycledMemberListActivity.startMemberAnalysisActivity(mContext, list, storeBean.group_id));
                        dialog.dismiss();
                        break;
                    default:
                }
            }
        });
    }

    public void showSelectDialog() {
        if (dialog != null) {
            dialog.show();
        }
    }

    private void modifyMemberStatus(ResultMemberBean bean) {
        RequestModifyMemberStatus requestBean = new RequestModifyMemberStatus();
        requestBean.memberId = bean.memberId;
        requestBean.status = -1;
        AppModelFactory.model().modifyMemberStatus(requestBean, new ProgressSubscriber<Object>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(Object result) {
                showToast("已删除");
                presenter.loadData(true);
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.getMessage());
            }

        });
    }

    public void initLayout() {
        //设置菜单内容之外其他区域的背景色
        drawerLayout.setScrimColor(ResoureUtils.getColor(mContext, R.color.transparent_black));
        //侧滑栏关闭手势滑动
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //右边菜单
        cardAdapter = new CardAdapter(mContext);
        rvCard.setLayoutManager(new GridLayoutManager(this, 2) {
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
    }

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultMemberBean>() {

            @Override
            public void onSuccess(List<ResultMemberBean> result, int total, final boolean isRefresh) {
                if (null != result) {
                    easyRefreshLayout.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);

                    if (TextUtils.isEmpty(body.level_id) && TextUtils.isEmpty(body.dayStart) && TextUtils.isEmpty(body.dayEnd)) {
                        totalMember = total;
                        tvAmount.setText("会员" + total + "人");
                    }
                    setNumber(total);
                    if (isRefresh) {
                        storeMemberAdapter.notifyDataSetChanged(result);
                    } else {
                        storeMemberAdapter.addAll(result);
                    }
                }
            }

            @Override
            public void onFailed(boolean isRefresh) {
                storeMemberAdapter.clear();
                easyRefreshLayout.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
                emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                setNumber(0);
            }

            @Override
            public void onCompleted(boolean isRefresh) {
                easyRefreshLayout.refreshComplete();
                easyRefreshLayout.closeLoadView();
            }

            @Override
            public void onEmptyData() {
                storeMemberAdapter.clear();
                easyRefreshLayout.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
                emptyLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
                setNumber(0);
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultMemberBean>>> subscriber, int pageIndex, int pageSize) {
                showContent();
                body.spIdList = list;
                AppModelFactory.model().getMemberListOfStore(body, storeBean.group_id, pageIndex, pageSize, subscriber);
            }
        });
    }

    private void setNumber(int total) {
        if(!TextUtils.isEmpty(etOne.getText().toString()) && !TextUtils.isEmpty(etTwo.getText().toString())){
            tempName = "未到店天数" + etOne.getText().toString() + "天~" + etTwo.getText().toString() + "天";
        }else if(!TextUtils.isEmpty(etOne.getText().toString())){
            tempName = "未到店天数" + etOne.getText().toString() + "天";
        }else if(!TextUtils.isEmpty(etTwo.getText().toString())){
            tempName = "未到店天数" + etTwo.getText().toString() + "天";
        }else{
            tempName = "";
        }

        if(!TextUtils.isEmpty(tempName) && !TextUtils.isEmpty(tempLevelName)){
            tvAmountFilter.setText(tempName +  "、" + tempLevelName + total + "人");
        }else if(!TextUtils.isEmpty(tempLevelName)){
            tvAmountFilter.setText(tempLevelName + total + "人");
        }else if(!TextUtils.isEmpty(tempName)){
            tvAmountFilter.setText(tempName + total + "人");
        }else{
            tvAmountFilter.setText("");
        }

        tvAmountFilter.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tvAmountFilter.setSingleLine(true);
        tvAmountFilter.setSelected(true);
        tvAmountFilter.setFocusable(true);
        tvAmountFilter.setFocusableInTouchMode(true);
    }

    @OnClick(R.id.rl_filter)
    public void filter(View v) {
        DeviceUtils.hideSoftKeyboard(v, mContext);
        openRightLayout(v);
    }

    @OnClick(R.id.tv_positive)
    public void positive(View v) {
        openRightLayout(v);
        body.dayStart = etOne.getText().toString().trim();
        body.dayEnd = etTwo.getText().toString().trim();
        easyRefreshLayout.autoRefresh();
    }

    @OnClick(R.id.tv_negative)
    public void negative(View v) {
        openRightLayout(v);
        reset();
        easyRefreshLayout.autoRefresh();
    }

    private void reset() {
        body.dayStart = "";
        body.dayEnd = "";
        etOne.setText("");
        etTwo.setText("");
        body.level_id = "";
        tempLevelName = "";
        cardAdapter.setSelectPos(-1);
    }

    /**
     * 右边菜单开关事件
     */
    public void openRightLayout(View view) {
        if (drawerLayout.isDrawerOpen(main_right_drawer_layout)) {
            drawerLayout.closeDrawer(main_right_drawer_layout);
        } else {
            drawerLayout.openDrawer(main_right_drawer_layout);
        }
    }

    private void getLevelData() {
        AppModelFactory.model().getLevel(storeBean.group_id, new ProgressSubscriber<DataBean<List<ResultLevel>>>(mContext) {

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
        presenter.loadData(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EvenManager.unregister(this);
    }
}

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

import java.io.Serializable;
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
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;
import cn.yuyun.yymy.http.result.ResultLevel;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.home.member.memberdata.MemberDetailActivity;
import cn.yuyun.yymy.ui.store.CardAdapter;
import cn.yuyun.yymy.ui.store.report.HqStoreAdapter;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.WarnningDialog;
import cn.yuyun.yymy.view.dialog.BottomSelectDialogMember;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author
 * @desc 本店会员列表 -- 总部
 * @date
 */
public class StoreMemberListAllActivity extends BaseActivity {

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

    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.rv_card)
    RecyclerView rvCard;
    CardAdapter cardAdapter;
    private BottomSelectDialogMember dialog;
    @BindView(R.id.et1)
    EditText etOne;
    @BindView(R.id.et2)
    EditText etTwo;

    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;

    @BindView(R.id.rv_store)
    RecyclerView rvStore;
    StringBuilder tempAllName;
    String tempName = "";
    String levelName = "";
    HqStoreAdapter hqStoreAdapter;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_amountFilter)
    TextView tvAmountFilter;
    private String groupId;
    private List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> storeList;
    private List<String> tempSpIdList;

    private WarnningDialog warnningDialog;

    public static Intent startMemberAnalysisActivity(Context context, List<String> tempSpIdList, List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> storeList, String groupId) {
        return new Intent(context, StoreMemberListAllActivity.class).putExtra(EXTRA_DATA2, (Serializable) tempSpIdList).putExtra(EXTRA_DATA, (Serializable) storeList).putExtra(EXTRA_TYPE, groupId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_member_list_all);
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
        titleBar.setTilte("会员");
        titleBar.setTvRight("更多");
        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDialog();
            }
        });
        groupId = getIntent().getStringExtra(EXTRA_TYPE);
        tempSpIdList = (List<String>) getIntent().getSerializableExtra(EXTRA_DATA2);
        storeList = (List<ResultClassfiyStoreBean.OgServiceplacesRspListBean>) getIntent().getSerializableExtra(EXTRA_DATA);
        storeList.remove(0);
        tempAllName = new StringBuilder();
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
                presenter.loadData(true);
            }
        });

        storeMemberAdapter = new StoreMemberAdapter(mContext);
        storeMemberAdapter.setOnItemClickListener(new StoreMemberAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultMemberBean bean, int position) {
                StoreBean storeBean = new StoreBean();
                storeBean.spId = bean.memberInSpId;
                storeBean.group_id = groupId;
                startActivity(MemberDetailActivity.startMemberDetailActivityWithId(mContext, bean.memberId, storeBean));
            }
        });
        if(Constans.isStoreLoginer){
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
                    tempPos = adapterPosition;
                    warnningDialog.show();
                }
            });
            warnningDialog = new WarnningDialog(mContext, "确定删除？");
            warnningDialog.setOnPositiveListener(new WarnningDialog.OnDialogListener() {
                @Override
                public void onPositive() {
                    modifyMemberStatus(storeMemberAdapter.getItemBean(tempPos));
                    warnningDialog.dismiss();
                }

                @Override
                public void onNegative() {

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
                    if(!TextUtils.isEmpty(etInput.getText().toString())){
//                        reset();
                        body.search_text = etInput.getText().toString();
                    }else{
                        body.search_text = "";
                    }
                    easyRefreshLayout.autoRefresh();
                }
                return false;
            }
        });
    }

    int tempPos;

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
                        showToast("请进入到具体门店操作");
                        dialog.dismiss();
                        break;
                    case 2:
                        if(Constans.isStoreLoginer){
                            startActivity(SelectMemberListActivity.startSelectMemberListActivity(mContext, tempSpIdList, groupId));
                        }else{
                            showToast(getString(R.string.PARTNER));
                        }
                        dialog.dismiss();
                        break;
                    case 3:
                        startActivity(RecycledMemberListActivity.startMemberAnalysisActivity(mContext, tempSpIdList, groupId));
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
                levelName = bean.name;
            }
        });
        rvCard.setAdapter(cardAdapter);
        hqStoreAdapter = new HqStoreAdapter(mContext);
        rvStore.setLayoutManager(new GridLayoutManager(this, 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvStore.setAdapter(hqStoreAdapter);
        hqStoreAdapter.addAll(storeList);
    }

    private int totalMember;

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultMemberBean>() {

            @Override
            public void onSuccess(List<ResultMemberBean> result, int total, final boolean isRefresh) {
                if (result != null) {
                    easyRefreshLayout.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);

                    if(TextUtils.isEmpty(body.search_text) && TextUtils.isEmpty(body.level_id) && TextUtils.isEmpty(body.dayStart) && TextUtils.isEmpty(body.dayEnd)){
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
                AppModelFactory.model().getMemberListOfStore(body, groupId, pageIndex, pageSize, subscriber);
            }
        });
    }

    private void setNumber(int total){
        if(!TextUtils.isEmpty(etOne.getText().toString()) && !TextUtils.isEmpty(etTwo.getText().toString())){
            tempName = "未到店天数" + etOne.getText().toString() + "天~" + etTwo.getText().toString() + "天";
        }else if(!TextUtils.isEmpty(etOne.getText().toString())){
            tempName = "未到店天数" + etOne.getText().toString() + "天";
        }else if(!TextUtils.isEmpty(etTwo.getText().toString())){
            tempName = "未到店天数" + etTwo.getText().toString() + "天";
        }else{
            tempName = "";
        }

        if(!TextUtils.isEmpty(tempName)){
            tempName = tempName + "、";
        }

        if(!TextUtils.isEmpty(tempAllName.toString()) && !TextUtils.isEmpty(levelName)){
            tvAmountFilter.setText(tempName + tempAllName.toString() + "、" + levelName + total + "人");
        }else if(!TextUtils.isEmpty(tempAllName.toString())){
            tvAmountFilter.setText(tempName + tempAllName.toString() + total + "人");
        }else if(!TextUtils.isEmpty(levelName.toString())){
            tvAmountFilter.setText(tempName + levelName + total + "人");
        }else if(!TextUtils.isEmpty(tempName)){
            tvAmountFilter.setText(tempName.substring(0, tempName.length() - 1) + total + "人");
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
        List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> listStore = hqStoreAdapter.getSelectedItem();
        if(null != listStore && listStore.size() > 0){
            tempAllName.delete(0, tempAllName.length());
            List<String> list = new ArrayList<>();
            for (int i = 0; i < listStore.size(); i++) {
                list.add(listStore.get(i).sp_id);
                if(i == listStore.size() - 1){
                    tempAllName.append(listStore.get(i).sp_name);
                }else{
                    tempAllName.append(listStore.get(i).sp_name + "、");
                }
            }
            body.spIdList = list;
        }else{
            body.spIdList = new ArrayList<>();
        }
//        setNumber(0);
        easyRefreshLayout.autoRefresh();
    }

    @OnClick(R.id.tv_negative)
    public void negative(View v) {
        openRightLayout(v);
        reset();
        easyRefreshLayout.autoRefresh();
    }

    private void reset(){
        body.dayStart = "";
        body.dayEnd = "";
        etOne.setText("");
        etTwo.setText("");
        body.level_id = "";
        tempAllName.delete(0, tempAllName.length());
        levelName = "";
        tvAmountFilter.setText("");
        cardAdapter.setSelectPos(-1);
        body.spIdList = new ArrayList<>();
        hqStoreAdapter.clearAllSelection();
    }

    /** 右边菜单开关事件*/
    public void openRightLayout(View view) {
        if (drawerLayout.isDrawerOpen(main_right_drawer_layout)) {
            drawerLayout.closeDrawer(main_right_drawer_layout);
        } else {
            drawerLayout.openDrawer(main_right_drawer_layout);
        }
    }

    private void getLevelData() {

        AppModelFactory.model().getLevel(groupId, new ProgressSubscriber<DataBean<List<ResultLevel>>>(mContext) {

            @Override
            public void onNext(DataBean<List<ResultLevel>> result) {
                if (null != result) {
                    cardAdapter.addAll( result.data);
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

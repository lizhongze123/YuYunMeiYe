package cn.yuyun.yymy.ui.store.staff;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.NoneProgressSubscriber;
import com.example.http.PageInfo;
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
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.lzz.utils.DeviceUtils;
import cn.lzz.utils.ResoureUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.constan.Constans;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestModifyStaffStatus;
import cn.yuyun.yymy.http.request.RequestStoreStaff;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;
import cn.yuyun.yymy.http.result.StaffBean;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.store.StaffDetailActivity;
import cn.yuyun.yymy.ui.store.StoreStaffAdapter;
import cn.yuyun.yymy.ui.store.report.HqStoreAdapter;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.RadioGroupEx;
import cn.yuyun.yymy.view.dialog.BirthdayStaffDialog;
import cn.yuyun.yymy.view.dialog.BottomSelectDialogStaff;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author
 * @desc 本店员工列表--全部
 * @date
 */
public class StoreStaffListAllActivity extends BaseNoTitleActivity {

    @BindView(R.id.main_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.main_right_drawer_layout)
    RelativeLayout main_right_drawer_layout;

    @BindView(R.id.rv_in)
    SwipeMenuRecyclerView rvIn;
    @BindView(R.id.rv_out)
    SwipeMenuRecyclerView rvOut;

    @BindView(R.id.rg_one)
    RadioGroupEx rgOne;

    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.tv_hq)
    TextView tvHq;
    @BindView(R.id.cb_hq)
    CheckBox cbHq;
    @BindView(R.id.cb_out)
    CheckBox cbOut;

    @BindView(R.id.rb1)
    RadioButton rb1;
    @BindView(R.id.rb2)
    RadioButton rb2;
    @BindView(R.id.rb3)
    RadioButton rb3;

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_gift)
    ImageView ivGift;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;

    @BindView(R.id.rv_store)
    RecyclerView rvStore;
    StringBuilder tempLevelName;
    String tempName = "在职员工";
    HqStoreAdapter hqStoreAdapter;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    private String groupId;
    private List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> storeList;
    private List<String> tempSpIdList;

    private StoreStaffAdapter inAdapter;
    private StoreStaffAdapter outAdapter;

    private RequestStoreStaff requestBean = new RequestStoreStaff();
    private RequestStoreStaff body = new RequestStoreStaff();

    private BottomSelectDialogStaff dialog;
    private List<StaffBean> birthdayList;

    public static Intent startStaffAnalysisActivity(Context context, List<String> tempSpIdList, List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> storeList, String groupId) {
        return new Intent(context, StoreStaffListAllActivity.class).putExtra(EXTRA_DATA2, (Serializable) tempSpIdList).putExtra(EXTRA_DATA, (Serializable) storeList).putExtra(EXTRA_TYPE, groupId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_staff_list_all);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        EvenManager.register(this);
        initViews();
        initDatas();
    }

    private void initViews() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeRightLayout(v);
                showSelectDialog();
            }
        });
        groupId = getIntent().getStringExtra(EXTRA_TYPE);
        tempSpIdList = (List<String>) getIntent().getSerializableExtra(EXTRA_DATA2);
        storeList = (List<ResultClassfiyStoreBean.OgServiceplacesRspListBean>) getIntent().getSerializableExtra(EXTRA_DATA);
        tempLevelName = new StringBuilder();
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
               if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(etInput.getText().toString())) {
                        requestBean.searchText = etInput.getText().toString();
                    }else{
                        requestBean.searchText = "";
                    }
                   getData();
                }
                return false;
            }
        });
        initIn();
        initOut();
        initLayout();
        initDialog();
        ivGift.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(null == birthdayList || birthdayList.size() == 0){
                    showToast("近期没有过生日的员工");
                }else{
                    final BirthdayStaffDialog dialog = new BirthdayStaffDialog(mContext);
                    dialog.setOnPositiveListener(new BirthdayStaffDialog.OnPositiveListener() {
                        @Override
                        public void onPositive() {
                            dialog.dismiss();
                            startActivity(StaffBirthdayActivity.startStaffBirthdayActivity(mContext, birthdayList));
                        }
                    });
                    dialog.show();
                    SpannableString spannableString = new SpannableString("亲，你有" + (birthdayList.size()) +"位可爱的员工即将过生日，在这美好时刻，快去看看他（她）们吧");
                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FF9393"));
                    spannableString.setSpan(colorSpan, 4, spannableString.length() - 23, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    dialog.setTips(spannableString);
                }
            }
        });
        cbOut.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    requestBean.status = RequestStoreStaff.JobStatus.OUT;
                    requestBean.birthDay = "";
                    rgOne.clearCheck();
                    rb1.setEnabled(false);
                    rb2.setEnabled(false);
                    rb3.setEnabled(false);
                    if(cbHq.isChecked()){
                        tempName = "总部离职员工";
                    }else{
                        tempName = "离职员工";
                    }
                }else{
                    requestBean.status = RequestStoreStaff.JobStatus.IN;
                    rb1.setEnabled(true);
                    rb2.setEnabled(true);
                    rb3.setEnabled(true);
                    if(cbHq.isChecked()){
                        tempName = "总部在职员工";
                    }else{
                        tempName = "在职员工";
                    }
                }
            }
        });
    }

    private void initIn() {
        rvIn.setLayoutManager(new LinearLayoutManager(this));
        inAdapter = new StoreStaffAdapter(mContext);
        inAdapter.setOnItemClickListener(new StoreStaffAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, StaffBean bean, int position) {
                StoreBean storeBean = new StoreBean();
                storeBean.spId = bean.baseon_id;
                storeBean.group_id = groupId;
                startActivity(StaffDetailActivity.startStaffDetailActivity(mContext, bean, storeBean));
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
                            .setBackground(R.color.side_gray)
                            .setText("离职")
                            .setTextColor(Color.WHITE)
                            .setWidth(width)
                            .setHeight(height);
                    // 添加菜单到左侧。
                    rightMenu.addMenuItem(addItem);
                    // 注意：哪边不想要菜单，那么不要添加即可。
                }
            };
            // 设置监听器。
            rvIn.setSwipeMenuCreator(mSwipeMenuCreator);
            // 设置菜单Item点击监听。
            rvIn.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
                @Override
                public void onItemClick(SwipeMenuBridge menuBridge) {
                    menuBridge.closeMenu();
                    // 左侧还是右侧菜单。
                    int direction = menuBridge.getDirection();
                    // RecyclerView的Item的position。
                    int adapterPosition = menuBridge.getAdapterPosition();
                    // 菜单在RecyclerView的Item中的Position。
                    int menuPosition = menuBridge.getPosition();
                    modifyStaffStatus(inAdapter.getItemBean(adapterPosition), RequestStoreStaff.JobStatus.OUT);
                }
            });
        }
        rvIn.setAdapter(inAdapter);
    }

    private void initOut() {
        rvOut.setLayoutManager(new LinearLayoutManager(this));
        outAdapter = new StoreStaffAdapter(mContext);
        outAdapter.setOnItemClickListener(new StoreStaffAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, StaffBean bean, int position) {
                StoreBean storeBean = new StoreBean();
                storeBean.spId = bean.baseon_id;
                storeBean.group_id = groupId;
                startActivity(StaffDetailActivity.startStaffDetailActivity(mContext, bean, storeBean));
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
                            .setBackground(R.color.text_green)
                            .setText("入职")
                            .setTextColor(Color.WHITE)
                            .setWidth(width)
                            .setHeight(height);
                    SwipeMenuItem delItem = new SwipeMenuItem(mContext)
                            .setBackground(R.color.text_red)
                            .setText("删除")
                            .setTextColor(Color.WHITE)
                            .setWidth(width)
                            .setHeight(height);
                    // 添加菜单到左侧。
                    rightMenu.addMenuItem(addItem);
                    rightMenu.addMenuItem(delItem);
                    // 注意：哪边不想要菜单，那么不要添加即可。
                }
            };
            // 设置监听器。
            rvOut.setSwipeMenuCreator(mSwipeMenuCreator);
            // 设置菜单Item点击监听。
            rvOut.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
                @Override
                public void onItemClick(SwipeMenuBridge menuBridge) {
                    menuBridge.closeMenu();
                    // 左侧还是右侧菜单。
                    int direction = menuBridge.getDirection();
                    // RecyclerView的Item的position。
                    int adapterPosition = menuBridge.getAdapterPosition();
                    // 菜单在RecyclerView的Item中的Position。
                    int menuPosition = menuBridge.getPosition();

                    if (menuPosition == 0) {
                        showToast("入职");
                        modifyStaffStatus(outAdapter.getItemBean(adapterPosition), RequestStoreStaff.JobStatus.IN);
                    } else {
                        modifyStaffStatus(outAdapter.getItemBean(adapterPosition), RequestStoreStaff.JobStatus.RECYCLED);
                    }

                }
            });
        }
        rvOut.setAdapter(outAdapter);
    }

    public void showSelectDialog() {
        if (dialog != null) {
            dialog.show();
        }
    }

    private void initDialog() {
        dialog = new BottomSelectDialogStaff(this);
        dialog.setItemClickListener(new BottomSelectDialogStaff.ItemClickListener() {

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
                        startActivity(RecycledStaffListActivity.startRecycledStaffListActivityForAll(mContext,tempSpIdList, groupId));
                        dialog.dismiss();
                        break;
                    default:
                }
            }
        });
    }

    public void initLayout() {
        //侧滑栏关闭手势滑动
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //设置菜单内容之外其他区域的背景色
        drawerLayout.setScrimColor(ResoureUtils.getColor(mContext, R.color.transparent_black));
        hqStoreAdapter = new HqStoreAdapter(mContext);
        rvStore.setLayoutManager(new GridLayoutManager(this, 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvStore.setAdapter(hqStoreAdapter);
        hqStoreAdapter.setOnItemClickListener(new HqStoreAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick() {
                cbHq.setChecked(false);
            }
        });
        hqStoreAdapter.addAll(storeList);
        if(UserfoPrefs.getInstance(mContext).getPermissionHq() == 1){
            tvHq.setVisibility(View.VISIBLE);
            cbHq.setVisibility(View.VISIBLE);
            cbHq.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        hqStoreAdapter.clearAllSelection();
                        if(cbOut.isChecked()){
                            tempName = "总部离职员工";
                        }else{
                            tempName = "总部在职员工";
                        }
                    }else{
                        if(cbOut.isChecked()){
                            tempName = "离职员工";
                        }else{
                            tempName = "在职员工";
                        }
                    }
                }
            });
        }else{
            tvHq.setVisibility(View.GONE);
            cbHq.setVisibility(View.GONE);
        }
    }

    private void initDatas() {
        body.spIdList = tempSpIdList;
        getData();
        getBirthdayData();
    }

    private void modifyStaffStatus(StaffBean bean, RequestStoreStaff.JobStatus status) {
        RequestModifyStaffStatus requestBean = new RequestModifyStaffStatus();
        requestBean.staffId = bean.staffId;
        requestBean.status = status;
        AppModelFactory.model().modifyStaffStatus(requestBean, new ProgressSubscriber<Object>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(Object result) {
                showToast("操作成功");
                getData();
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.getMessage());
            }

        });
    }

    private void getData() {
        if (requestBean.status == RequestStoreStaff.JobStatus.OUT) {
            requestBean.birthDay = "";
        }
        AppModelFactory.model().getStoreStaffList(groupId, 1, 5000, requestBean, new ProgressSubscriber<DataBean<PageInfo<StaffBean>>>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean<PageInfo<StaffBean>> result) {

                if (requestBean.status == RequestStoreStaff.JobStatus.IN) {
                    rvIn.setVisibility(View.VISIBLE);
                    rvOut.setVisibility(View.GONE);

                    if (null != result.data && result.data.size > 0) {
                        if (null != result.data.dataLsit && result.data.dataLsit.size() != 0) {
                            inAdapter.notifyDataSetChanged(result.data.dataLsit);
                        } else {
                            rvIn.setVisibility(View.GONE);
                            emptyLayout.setVisibility(View.VISIBLE);
                            emptyLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
                            inAdapter.clear();
                        }
                    } else {
                        inAdapter.clear();
                        rvIn.setVisibility(View.GONE);
                        emptyLayout.setVisibility(View.VISIBLE);
                        emptyLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
                    }
                } else {
                    rvIn.setVisibility(View.GONE);
                    rvOut.setVisibility(View.VISIBLE);
                    if (null != result.data && result.data.size > 0) {
                        if (null != result.data.dataLsit && result.data.dataLsit.size() != 0) {
                            outAdapter.notifyDataSetChanged(result.data.dataLsit);
                        } else {
                            rvOut.setVisibility(View.GONE);
                            emptyLayout.setVisibility(View.VISIBLE);
                            emptyLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
                            outAdapter.clear();
                        }
                    } else {
                        outAdapter.clear();
                        rvOut.setVisibility(View.GONE);
                        emptyLayout.setVisibility(View.VISIBLE);
                        emptyLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
                    }
                }
                setNumber(result.data.dataLsit.size());
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast(ex.message);
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                emptyLayout.setVisibility(View.VISIBLE);
                emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                rvIn.setVisibility(View.GONE);
                rvOut.setVisibility(View.GONE);
            }
        });
    }

    private void getBirthdayData() {
        body.birthDay = "7";
        AppModelFactory.model().getStoreStaffList(groupId, 1, 1000, body, new NoneProgressSubscriber<DataBean<PageInfo<StaffBean>>>(mContext) {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(DataBean<PageInfo<StaffBean>> result) {

                if(null != result.data.dataLsit){
                    birthdayList = result.data.dataLsit;
                }

            }
        });
    }

    @OnClick(R.id.rl_filter)
    public void filter(View v) {
        DeviceUtils.hideSoftKeyboard(v, mContext);
        openRightLayout(v);
    }

    public void openRightLayout(View view) {
        if (drawerLayout.isDrawerOpen(main_right_drawer_layout)) {
            drawerLayout.closeDrawer(main_right_drawer_layout);
        } else {
            drawerLayout.openDrawer(main_right_drawer_layout);
        }
    }

    public void closeRightLayout(View view) {
        if (drawerLayout.isDrawerOpen(main_right_drawer_layout)) {
            drawerLayout.closeDrawer(main_right_drawer_layout);
        }
    }

    private void setNumber(int total){
        String str;
        if(TextUtils.isEmpty(tempLevelName.toString())){
            str = tempName + total + "人";
        }else{
            str = tempName + "、" + tempLevelName.toString() + total + "人";
        }
        tvAmount.setText(str);
        tvAmount.setSingleLine(true);
        tvAmount.setSelected(true);
        tvAmount.setFocusable(true);
        tvAmount.setFocusableInTouchMode(true);
        tvAmount.setEllipsize(TextUtils.TruncateAt.MARQUEE);
    }

    @OnClick(R.id.tv_positive)
    public void positive(View v) {
        openRightLayout(v);
        List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> listStore = hqStoreAdapter.getSelectedItem();
        List<String> list = new ArrayList<>();
        tempLevelName.delete(0, tempLevelName.length());
        for (int i = 0; i < listStore.size(); i++) {
            list.add(listStore.get(i).sp_id);
            tempLevelName.append(listStore.get(i).sp_name + "、");
        }
        requestBean.spIdList = list;
        if(cbHq.isChecked()){
            requestBean.baseon_type = "1";
        }else if(list.size() > 0){
            requestBean.baseon_type = "2";
        }else{
            requestBean.baseon_type = "";
        }
        setNumber(0);
        getData();
    }

    @OnClick(R.id.tv_negative)
    public void negative(View v) {
        openRightLayout(v);
        requestBean.birthDay = "";
        requestBean.searchText = "";
        rb1.setEnabled(true);
        rb2.setEnabled(true);
        rb3.setEnabled(true);
        rgOne.clearCheck();
        cbOut.setChecked(false);
        cbHq.setChecked(false);
        requestBean.baseon_type = "";
        requestBean.spIdList = tempSpIdList;
        hqStoreAdapter.clearAllSelection();
        tempName = "在职员工";
        tvAmount.setText(tempName);
        tempLevelName.delete(0, tempLevelName.length());
        requestBean.status = RequestStoreStaff.JobStatus.IN;
        getData();
    }

    @OnCheckedChanged({R.id.rb1, R.id.rb2, R.id.rb3})
    public void onCheckedChanged(CompoundButton view, boolean ischanged) {
        switch (view.getId()) {
            case R.id.rb1:
                if (ischanged) {
                    requestBean.birthDay = "7";
                    if(cbHq.isChecked()){
                        tempName = "总部在职员工、离员工生日时间7天";
                    }else{
                        tempName = "在职员工、离员工生日时间7天";
                    }
                }
                break;
            case R.id.rb2:
                if (ischanged) {
                    requestBean.birthDay = "15";
                    if(cbHq.isChecked()){
                        tempName = "总部在职员工、离员工生日时间15天";
                    }else{
                        tempName = "在职员工、离员工生日时间15天";
                    }
                }
                break;
            case R.id.rb3:
                if (ischanged) {
                    requestBean.birthDay = "30";
                    if(cbHq.isChecked()){
                        tempName = "总部在职员工、离员工生日时间30天";
                    }else{
                        tempName = "在职员工、离员工生日时间30天";
                    }
                }
                break;
            default:
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if(notifyEvent.tag == NotifyEvent.STAFF){
            getData();
        }
    }

}

package cn.yuyun.yymy.ui.store.staff;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import cn.yuyun.yymy.http.result.StaffBean;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.store.StaffDetailActivity;
import cn.yuyun.yymy.ui.store.StoreStaffAdapter;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.RadioGroupEx;
import cn.yuyun.yymy.view.dialog.BirthdayStaffDialog;
import cn.yuyun.yymy.view.dialog.BottomSelectDialogStaff;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc 本店员工列表
 * @date
 */
public class StoreStaffListActivity extends BaseNoTitleActivity implements TextWatcher {

    public DrawerLayout drawerLayout;
    private RelativeLayout main_right_drawer_layout;

    @BindView(R.id.rv_in)
    SwipeMenuRecyclerView rvIn;
    @BindView(R.id.rv_out)
    SwipeMenuRecyclerView rvOut;
    private StoreBean storeBean;

    @BindView(R.id.rg_one)
    RadioGroupEx rgOne;

    @BindView(R.id.et_input)
    EditText etInput;

    @BindView(R.id.rb1)
    RadioButton rb1;
    @BindView(R.id.rb2)
    RadioButton rb2;
    @BindView(R.id.rb3)
    RadioButton rb3;

    @BindView(R.id.tv_amount)
    TextView tvAmount;

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_gift)
    ImageView ivGift;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;
    @BindView(R.id.cb_out)
    CheckBox cbOut;

    private RequestStoreStaff requestBean = new RequestStoreStaff();
    private RequestStoreStaff body = new RequestStoreStaff();

    private BottomSelectDialogStaff dialog;
    private List<StaffBean>birthdayList;

    public static Intent startStaffAnalysisActivity(Context context, StoreBean bean) {
        return new Intent(context, StoreStaffListActivity.class).putExtra(EXTRA_DATA, bean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_staff_list);
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
        storeBean = (StoreBean) getIntent().getSerializableExtra(EXTRA_DATA);
        etInput.addTextChangedListener(this);
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(etInput.getText().toString())) {
                        requestBean.searchText = etInput.getText().toString();
                        getData();
                    }
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
                }else{
                    requestBean.status = RequestStoreStaff.JobStatus.IN;
                    rb1.setEnabled(true);
                    rb2.setEnabled(true);
                    rb3.setEnabled(true);
                }
            }
        });
    }

    private StoreStaffAdapter inAdapter;
    private StoreStaffAdapter outAdapter;

    private void initIn() {
        rvIn.setLayoutManager(new LinearLayoutManager(this));
        inAdapter = new StoreStaffAdapter(mContext);
        inAdapter.setOnItemClickListener(new StoreStaffAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, StaffBean bean, int position) {
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
                        if(Constans.isStoreLoginer){
                            startActivity(AddStaffActivity.startAddStaffActivity(mContext, storeBean));
                        }else{
                            showToast(getString(R.string.PARTNER));
                        }
                        dialog.dismiss();

                        break;
                    case 2:
                        List<String>list = new ArrayList<>();
                        list.add(storeBean.spId);
                        startActivity(RecycledStaffListActivity.startRecycledStaffListActivityForStore(mContext, list, storeBean.group_id));
                        dialog.dismiss();
                        break;
                    default:
                }
            }
        });
    }

    public void initLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        //侧滑栏关闭手势滑动
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //设置菜单内容之外其他区域的背景色
        drawerLayout.setScrimColor(ResoureUtils.getColor(mContext, R.color.transparent_black));
        //左边菜单
        main_right_drawer_layout = (RelativeLayout) findViewById(R.id.main_right_drawer_layout);
    }

    private void initDatas() {
        requestBean.baseon_type = "2";
        List<String> list = new ArrayList<>();
        list.add(storeBean.spId);
        requestBean.spIdList = list;
        body.spIdList = list;
        body.baseon_type = "2";
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
        AppModelFactory.model().getStoreStaffList(storeBean.group_id, 1, 1000, requestBean, new NoneProgressSubscriber<DataBean<PageInfo<StaffBean>>>(mContext) {


            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(DataBean<PageInfo<StaffBean>> result) {

                if (requestBean.status == RequestStoreStaff.JobStatus.IN) {
                    rvIn.setVisibility(View.VISIBLE);
                    rvOut.setVisibility(View.GONE);

                    if (null != result.data && result.data.size > 0) {
                        if (null != result.data.dataLsit && result.data.dataLsit.size() != 0) {
                            tvAmount.setText("在职员工" + result.data.dataLsit.size() + "人");
                            inAdapter.notifyDataSetChanged(result.data.dataLsit);
                        } else {
                            rvIn.setVisibility(View.GONE);
                            emptyLayout.setVisibility(View.VISIBLE);
                            emptyLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
                            inAdapter.clear();
                            tvAmount.setText("在职员工" + 0 + "人");
                        }
                    } else {
                        inAdapter.clear();
                        tvAmount.setText("在职员工" + 0 + "人");
                        rvIn.setVisibility(View.GONE);
                        emptyLayout.setVisibility(View.VISIBLE);
                        emptyLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
                    }

                } else {
                    rvIn.setVisibility(View.GONE);
                    rvOut.setVisibility(View.VISIBLE);
                    if (null != result.data && result.data.size > 0) {
                        if (null != result.data.dataLsit && result.data.dataLsit.size() != 0) {
                            tvAmount.setText("离职员工"  + result.data.dataLsit.size() + "人");
                            outAdapter.notifyDataSetChanged(result.data.dataLsit);
                        } else {
                            rvOut.setVisibility(View.GONE);
                            emptyLayout.setVisibility(View.VISIBLE);
                            emptyLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
                            tvAmount.setText("离职员工" + 0 + "人");
                            outAdapter.clear();
                        }

                    } else {
                        outAdapter.clear();
                        tvAmount.setText("离职员工"  + 0 + "人");
                        rvOut.setVisibility(View.GONE);
                        emptyLayout.setVisibility(View.VISIBLE);
                        emptyLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
                    }
                }

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
        AppModelFactory.model().getStoreStaffList(storeBean.group_id, 1, 1000, body, new NoneProgressSubscriber<DataBean<PageInfo<StaffBean>>>(mContext) {

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

    @OnClick(R.id.tv_positive)
    public void positive(View v) {
        openRightLayout(v);
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
        requestBean.status =  RequestStoreStaff.JobStatus.IN;
        getData();
    }

    @OnCheckedChanged({R.id.rb1, R.id.rb2, R.id.rb3})
    public void onCheckedChanged(CompoundButton view, boolean ischanged) {
        switch (view.getId()) {
            case R.id.rb1:
                if (ischanged) {
                    requestBean.birthDay = "7";
                }
                break;
            case R.id.rb2:
                if (ischanged) {
                    requestBean.birthDay = "15";
                }
                break;
            case R.id.rb3:
                if (ischanged) {
                    requestBean.birthDay = "30";
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

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        requestBean.searchText = s.toString().trim();
        getData();
    }

    @Override
    protected void onDestroy() {
        EvenManager.unregister(this);
        super.onDestroy();

    }
}

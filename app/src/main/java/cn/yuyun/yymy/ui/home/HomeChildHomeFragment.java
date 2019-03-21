package cn.yuyun.yymy.ui.home;

import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.autonavi.ae.gmap.maploader.ERROR_CODE;
import com.example.http.ApiException;
import com.example.http.ApiRequestInterceptor;
import com.example.http.DataBean;
import com.example.http.NoneProgressSubscriber;
import com.example.http.ProgressSubscriber;
import com.google.gson.Gson;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.example.amap.location.ALocationClientFactory;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.DeviceUtils;
import cn.lzz.utils.LogUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.lzz.utils.VersionUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseNoTitleFragment;
import cn.yuyun.yymy.bean.Sex;
import cn.yuyun.yymy.constan.Constans;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestActionLike;
import cn.yuyun.yymy.http.request.RequestAppInfo;
import cn.yuyun.yymy.http.request.RequestClassfiyStore;
import cn.yuyun.yymy.http.request.RequestLineData;
import cn.yuyun.yymy.http.result.AccountInfoBean;
import cn.yuyun.yymy.http.result.ActionBean;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;
import cn.yuyun.yymy.http.result.ResultGlobalPic;
import cn.yuyun.yymy.http.result.ResultLauchPic;
import cn.yuyun.yymy.http.result.ResultLineData;
import cn.yuyun.yymy.http.result.ResultUnread;
import cn.yuyun.yymy.http.result.ResultWeather;
import cn.yuyun.yymy.main.LoginActivity;
import cn.yuyun.yymy.main.PubActivity;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.StorePrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.home.actions.ActionsAdapter;
import cn.yuyun.yymy.ui.home.actions.ActionsDetailActivity;
import cn.yuyun.yymy.ui.home.actions.ActionsListActivity;
import cn.yuyun.yymy.ui.home.appointment.AppointmentActivity;
import cn.yuyun.yymy.ui.home.attendance.AttendanceSignActivity;
import cn.yuyun.yymy.ui.home.bill.BillManageActivity;
import cn.yuyun.yymy.ui.home.leave.LeaveActivity;
import cn.yuyun.yymy.ui.home.leave.SelectMyStoreListActivity;
import cn.yuyun.yymy.ui.home.leave.SelectStoreListActivity;
import cn.yuyun.yymy.ui.home.member.MemberManagerActivity;
import cn.yuyun.yymy.ui.home.cashier.SelectCashierMemberActivity;
import cn.yuyun.yymy.ui.home.notice.NoticeListActivity;
import cn.yuyun.yymy.ui.home.train.TrainListActivity;
import cn.yuyun.yymy.ui.home.work.WorkActivity;
import cn.yuyun.yymy.utils.AppUtils;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.utils.ScreenRotateUtils;
import cn.yuyun.yymy.utils.up.CheckUpdateManager;
import cn.yuyun.yymy.view.ItemButton;
import cn.yuyun.yymy.view.lineview.BusinessLineView;
import cn.yuyun.yymy.view.lineview.FundMode;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * @author
 * @desc
 * @date
 */
public class HomeChildHomeFragment extends BaseNoTitleFragment implements AMapLocationListener {

    @BindView(R.id.rv)
    RecyclerView rvAction;
    @BindView(R.id.layout_action)
    LinearLayout layout_action;
    @BindView(R.id.easyRefreshLayout)
    EasyRefreshLayout easyRefreshLayout;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_userName)
    TextView tvUserName;
    @BindView(R.id.ll_user)
    LinearLayout llUser;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_week)
    TextView tvWeek;
    @BindView(R.id.iv_weather)
    ImageView ivWeather;
    @BindView(R.id.tv_weather)
    TextView tvWeather;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.item_attendance)
    ItemButton itemAttendance;
    @BindView(R.id.item_work)
    ItemButton itemWork;
    @BindView(R.id.item_leave)
    ItemButton itemLeave;
    @BindView(R.id.item_notice)
    ItemButton itemNotice;
    @BindView(R.id.item_train)
    ItemButton itemTrain;
    @BindView(R.id.item_member)
    ItemButton itemMember;
    @BindView(R.id.item_appointment)
    ItemButton itemAppointment;
    @BindView(R.id.item_bill)
    ItemButton itemBill;
    @BindView(R.id.item_cashier)
    ItemButton itemCashier;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.ll_noStore)
    LinearLayout llNoStore;
    @BindView(R.id.ll_business)
    LinearLayout llBusiness;
    @BindView(R.id.scl)
    ScrollView mScrollView;
    @BindView(R.id.businessLineView1)
    BusinessLineView businessLineView1;
    @BindView(R.id.businessLineView2)
    BusinessLineView businessLineView2;
    @BindView(R.id.rb_one)
    RadioButton rbOne;
    @BindView(R.id.rb_two)
    RadioButton rbTwo;
    @BindView(R.id.radioGroupTitle)
    RadioGroup radioGroupTitle;
    @BindView(R.id.iv_enlarge)
    ImageView ivEnlarge;
    private AMapLocationClient locationClient;
    private RequestLineData requestLineData = new RequestLineData();
    private ActionsAdapter actionsAdapter;
    private OrientationEventListener mOrientationListener;
    private boolean isSigleStore;

    public static HomeChildHomeFragment newInstance() {
        HomeChildHomeFragment fragment = new HomeChildHomeFragment();
        return fragment;
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        if (getArguments() != null) {

        }
    }

    @Override
    protected int getTheLayoutId() {
        return R.layout.fragment_home_child_home;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mOrientationListener) {
            mOrientationListener.disable();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != mOrientationListener) {
            mOrientationListener.disable();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != mOrientationListener) {
            mOrientationListener.enable();
        }
    }

    /**
     * 是否横屏
     */
    boolean isLandscape = false;

    @Override
    protected void initViews(View root) {
        super.initViews(root);
        mOrientationListener = new OrientationEventListener(mContext,
                SensorManager.SENSOR_DELAY_NORMAL) {

            @Override
            public void onOrientationChanged(int orientation) {
                if(!ScreenRotateUtils.isOpenGravity(mContext)){
                    return;
                }
                if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
                    //平放监测不到有效的角度
                    return;
                }
                if (orientation > 350 || orientation < 10) {
                    //正常竖屏，稍微向左
                    orientation = 0;
                    isLandscape = false;
                } else if (orientation > 80 && orientation < 100) {
                    //向右横屏
                    orientation = 90;
                } else if (orientation > 170 && orientation < 190) {
                    //倒立手机
                    orientation = 180;
                } else if (orientation > 260 && orientation < 280) {
                    //向左横屏
                    orientation = 270;
                    if(!isLandscape){
                        startActivity(LandscapeLineviewActivity.startLandscapeLineviewActivity(mContext));
                    }
                    isLandscape = true;
                } else {
                    return;
                }
            }
        };
        if (mOrientationListener.canDetectOrientation()) {
            mOrientationListener.enable();
        } else {
            mOrientationListener.disable();
        }
        EvenManager.register(this);
        itemAttendance.init(R.drawable.home_attendance, "考勤打卡");
        itemWork.init(R.drawable.home_work, "工作汇报");
        itemLeave.init(R.drawable.home_leave, "请假申请");
        itemNotice.init(R.drawable.home_notice, "通知公告");
        itemTrain.init(R.drawable.home_train, "培训资料");
        itemMember.init(R.drawable.home_member, "会员管理");
        itemAppointment.init(R.drawable.home_appointment, "预约管理");
        itemBill.init(R.drawable.home_bill, "单据管理");
        itemCashier.init(R.drawable.home_cashier, "收银作业");
        actionsAdapter = new ActionsAdapter(mContext);
        rvAction.setAdapter(actionsAdapter);
        rvAction.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        radioGroupTitle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_one) {
                    businessLineView1.setVisibility(View.VISIBLE);
                    businessLineView2.setVisibility(View.GONE);
                } else if (checkedId == R.id.rb_two) {
                    businessLineView1.setVisibility(View.GONE);
                    businessLineView2.setVisibility(View.VISIBLE);
                }
            }
        });

        ivEnlarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LandscapeLineviewActivity.startLandscapeLineviewActivity(mContext));
            }
        });
        businessLineView1.setOnPressListener(new BusinessLineView.OnPressListener() {
            @Override
            public void onPress() {
                mScrollView.requestDisallowInterceptTouchEvent(true);
                easyRefreshLayout.setEnablePullToRefresh(false);
            }

            @Override
            public void onUp() {
                easyRefreshLayout.setEnablePullToRefresh(true);
            }
        });
        actionsAdapter.setOnItemClickListener(new ActionsAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ActionBean bean, int position) {
                startActivity(ActionsDetailActivity.startActionDetailActivity(mContext, bean));
            }

            @Override
            public void onFavorites(ActionBean bean, int position) {
                submitFavorites(bean, position);
            }

            @Override
            public void onLike(ActionBean bean, int position) {
                submitLike(bean, position);
            }

            @Override
            public void onComment(ActionBean bean, int position) {
                startActivity(ActionsDetailActivity.startActionDetailActivity(mContext, bean));
            }
        });

        easyRefreshLayout.setLoadMoreModel(LoadModel.NONE);
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(this.getContext()));
        easyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
            }

            @Override
            public void onRefreshing() {
                initData();
            }
        });

        root.findViewById(R.id.iv_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ActionsListActivity.class));
            }
        });
    }

    @OnClick({R.id.item_attendance, R.id.item_work, R.id.item_leave, R.id.item_notice, R.id.item_train, R.id.item_member, R.id.item_appointment, R.id.item_bill, R.id.item_cashier})
    public void onClickItem(View view) {
        switch (view.getId()) {
            case R.id.item_attendance:
                if (!isHasStaffId()) {
                    return;
                }
                startActivity(new Intent(mContext, AttendanceSignActivity.class));
                break;
            case R.id.item_work:
                if (!isHasStaffId()) {
                    return;
                }
                startActivity(new Intent(mContext, WorkActivity.class));
                break;
            case R.id.item_leave:
                if (!isHasStaffId()) {
                    return;
                }
                startActivity(new Intent(mContext, LeaveActivity.class));
                break;
            case R.id.item_notice:
                if (!isHasStaffId()) {
                    return;
                }
                startActivity(new Intent(mContext, NoticeListActivity.class));
                break;
            case R.id.item_train:
                startActivity(new Intent(mContext, TrainListActivity.class));
                break;
            case R.id.item_member:
                startActivity(new Intent(mContext, MemberManagerActivity.class));
                break;
            case R.id.item_appointment:
                if (!isHasStaffId()) {
                    return;
                }
                startActivity(new Intent(mContext, AppointmentActivity.class));
                break;
            case R.id.item_bill:
                if (!isHasStaffId()) {
                    return;
                }
                startActivity(new Intent(mContext, BillManageActivity.class));
                break;
            case R.id.item_cashier:
                if (UserfoPrefs.getInstance(mContext).getPermission()) {
                    if(isSigleStore){
                        if(null != requestLineData.sp_id_list && requestLineData.sp_id_list.size() != 0){
                            String spId = requestLineData.sp_id_list.get(0);
                            startActivity(SelectCashierMemberActivity.startSelectCashierMemberActivity(mContext, spId));
                        }else{
                            showToast("未绑定门店");
                        }
                    }else{
                        startActivity(SelectMyStoreListActivity.startTypeSelectCashierMember(mContext));
                    }
                }else{
                    showToast("抱歉，你没有权限操作");
                }
                break;
            default:
        }
    }

    private void submitLike(final ActionBean bean, final int position) {
        RequestActionLike body = new RequestActionLike();
        body.type = 2;
        body.latestActivityId = bean.latestActivityVo.latestActivityId;
        if (bean.likeFlag) {
            body.operation = 2;
        } else {
            body.operation = 1;
        }
        AppModelFactory.model().actionLike(body, new NoneProgressSubscriber<Object>(mContext) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(Object o) {
                bean.likeFlag = !bean.likeFlag;
                if (bean.likeFlag) {
                    bean.likeCount = bean.likeCount + 1;
                } else {
                    if (bean.likeCount == 0) {
                        bean.likeCount = 0;
                    } else {
                        bean.likeCount = bean.likeCount - 1;
                    }
                }
                actionsAdapter.refreshLike(position, bean);
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.message);
            }
        });
    }

    private void submitFavorites(final ActionBean bean, final int position) {
        RequestActionLike body = new RequestActionLike();
        body.type = 1;
        body.latestActivityId = bean.latestActivityVo.latestActivityId;
        if (bean.collectFlag) {
            body.operation = 2;
        } else {
            body.operation = 1;
        }
        AppModelFactory.model().actionLike(body, new NoneProgressSubscriber<Object>(mContext) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(Object o) {
                bean.collectFlag = !bean.collectFlag;
                actionsAdapter.refreshFavorites(position, bean);
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.message);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        initWeatherData();
        getAccountInfo(LoginInfoPrefs.getInstance(mContext).getUserName(), LoginInfoPrefs.getInstance(mContext).getPassword());
        getUnread();
        getGlobalPic();
        getClassfiyStoreList();
        CheckUpdateManager manager = new CheckUpdateManager(getActivity());
        manager.checkUpdate();
        getAppGuideIcon();
        sendAppInfo();
        NewbieGuide.with(this).setLabel("收银开单").addGuidePage(GuidePage.newInstance().addHighLight(itemCashier).setLayoutRes(R.layout.bbb)).show();
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
                        //缓存门店列表数据
                        Gson gson = new Gson();
                        String json = gson.toJson(result.data);
                        StorePrefs.getInstance(mContext).saveInfo(json);

                        List<String> spIdList = new ArrayList<>();
                        for (int i = 0, iLength = result.data.size(); i < iLength; i++) {
                            for (int j = 0, jLength = result.data.get(i).ogServiceplacesRspList.size(); j < jLength; j++) {
                                spIdList.add(result.data.get(i).ogServiceplacesRspList.get(j).sp_id);
                            }
                        }
                        if(spIdList.size() > 1){
                            isSigleStore = false;
                        }else{
                            isSigleStore = true;
                        }
                        requestLineData.start_date = DateTimeUtils.getTimesMonthMorning();
                        requestLineData.end_date = DateTimeUtils.getTimesMonthNight();
                        requestLineData.sp_id_list = spIdList;
                        requestLineData.group_id = LoginInfoPrefs.getInstance(getContext()).getGroupId();
                        if (null == requestLineData.sp_id_list || requestLineData.sp_id_list.size() == 0) {
                            llNoStore.setVisibility(View.VISIBLE);
                            llBusiness.setVisibility(View.GONE);
                        } else {
                            llNoStore.setVisibility(View.GONE);
                            llBusiness.setVisibility(View.VISIBLE);
                            getLineData(requestLineData);
                        }
                    }
                }
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast("网络异常，请检查网络");
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                layout_action.setVisibility(View.VISIBLE);
                showToast(ex.message);
            }

        });
    }

    private void getLineData(RequestLineData body) {
        AppModelFactory.model().getLineData(body, new ProgressSubscriber<DataBean<List<ResultLineData>>>(mContext) {
            @Override
            public void onNext(DataBean<List<ResultLineData>> result) {
                if (null != result.data) {
                    List<FundMode> fundModeList = new ArrayList<>();
                    List<FundMode> fundModeList2 = new ArrayList<>();
                    for (int i = 0; i < result.data.size(); i++) {
                        FundMode fundMode;
                        FundMode fundMode2;
                        if (null != result.data.get(i).analysisSpTurnoverSingleRsp) {
                            fundMode = new FundMode(result.data.get(i).date, DateTimeUtils.dateToWeek(result.data.get(i).date), StringFormatUtils.formatDecimal(result.data.get(i).analysisSpTurnoverSingleRsp.total_amount));
                            fundMode2 = new FundMode(result.data.get(i).date, DateTimeUtils.dateToWeek(result.data.get(i).date), StringFormatUtils.formatDecimal(result.data.get(i).analysisSpTurnoverSingleRsp.amount_consume));
                        } else {
                            fundMode = new FundMode(result.data.get(i).date, DateTimeUtils.dateToWeek(result.data.get(i).date), "0.00");
                            fundMode2 = new FundMode(result.data.get(i).date, DateTimeUtils.dateToWeek(result.data.get(i).date), "0.00");
                        }
                        fundModeList.add(fundMode);
                        fundModeList2.add(fundMode2);
                    }
                    businessLineView1.setDataList(fundModeList, BusinessLineView.TYPE_MOONTH);
                    businessLineView2.setDataList(fundModeList2, BusinessLineView.TYPE_MOONTH);
                }
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                if (easyRefreshLayout.isRefreshing()) {
                    easyRefreshLayout.refreshComplete();
                }
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                layout_action.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                layout_action.setVisibility(View.VISIBLE);
                LogUtils.e(ex.getMessage());
            }
        });
    }

    private void sendAppInfo() {
        RequestAppInfo body = new RequestAppInfo();
        body.appVersion =  VersionUtils.getAppVersionName(mContext);
        body.account = LoginInfoPrefs.getInstance(mContext).getUserName();
        body.password = LoginInfoPrefs.getInstance(mContext).getPassword();
        body.phoneType = "Android";
        body.phone = DeviceUtils.getSystemModel();
        body.phoneVersion = DeviceUtils.getSystemVersion();
        AppModelFactory.model().sendAppInfo(body, new NoneProgressSubscriber<Object>(mContext) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(Object o) {
                LogUtils.e("提交手机信息成功");
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                LogUtils.e("提交手机信息失败");
            }
        });
    }

    private void initWeatherData() {
        locationClient = ALocationClientFactory.createLocationClient(getContext(), ALocationClientFactory.createOnceOption(), this);
        locationClient.stopLocation();
        locationClient.startLocation();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        if (month.length() < 2) {
            month = "0" + month;
        }
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        if (day.length() < 2) {
            day = "0" + day;
        }
        String week = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(week)) {
            week = "星期日";
        } else if ("2".equals(week)) {
            week = "星期一";
        } else if ("3".equals(week)) {
            week = "星期二";
        } else if ("4".equals(week)) {
            week = "星期三";
        } else if ("5".equals(week)) {
            week = "星期四";
        } else if ("6".equals(week)) {
            week = "星期五";
        } else if ("7".equals(week)) {
            week = "星期六";
        }
        tvWeek.setText(week);
        tvDate.setText(year + "年" + month + "月" + day + "日");
    }

    private void getWeather(String location) {
        try {
            if (TextUtils.isEmpty(location)) {
                return;
            }
            String url = "https://api.seniverse.com/v3/weather/now.json?key=sfblxzx3xywuejhn&language=zh-Hans&unit=c&location=" + location;
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {

                }

                @Override
                public void onResponse(okhttp3.Call call, final okhttp3.Response response) {

                    if (null == getActivity()) {
                        return;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (null != response) {
                                String s = null;
                                try {
                                    s = response.body().string();
                                    if (!TextUtils.isEmpty(s)) {
                                        ResultWeather weather = new Gson().fromJson(s, ResultWeather.class);
                                        if (null != weather.results && weather.results.size() != 0) {
                                            String name = weather.results.get(0).location.name;
                                            String text = weather.results.get(0).now.text;
                                            String temperature = weather.results.get(0).now.temperature;
                                            tvWeather.setText(name + "：" + text + " " + temperature + " ℃");
                                            ivWeather.setImageResource(weather.results.get(0).now.getCode());
                                        }

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getUnread() {
        AppModelFactory.model().getUnread(new ProgressSubscriber<DataBean<ResultUnread>>(mContext) {
            @Override
            public void onNext(DataBean<ResultUnread> result) {
                if (result.data != null && result.data.actionsList.size() > 0) {
                    layout_action.setVisibility(View.GONE);
                    actionsAdapter.notifyDataSetChanged(result.data.actionsList);
                } else {
                    actionsAdapter.clear();
                    layout_action.setVisibility(View.VISIBLE);
                }
                itemNotice.showRedDot(result.data.noticeNoReadCount, false);
                itemTrain.showRedDot(result.data.trainInfoNoReadCount, false);
                itemWork.showRedDot(result.data.approveWorkCount, false);
                itemLeave.showRedDot(result.data.myReviewCount, false);
                ((HomeFragment) getParentFragment()).setDot(result.data.systemMsgCount);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                if (easyRefreshLayout.isRefreshing()) {
                    easyRefreshLayout.refreshComplete();
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
                layout_action.setVisibility(View.VISIBLE);
                showToast("请求失败，请稍后重试");
            }

        });
    }

    private void getGlobalPic() {
        AppModelFactory.model().getGlobalPic(UserfoPrefs.getInstance(mContext).getOgId(), new ProgressSubscriber<DataBean<ResultGlobalPic>>(mContext) {
            @Override
            public void onNext(DataBean<ResultGlobalPic> result) {
                if (null != result.data) {
                    Constans.globalPic = result.data;
                }
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                /*if (easyRefreshLayout.isRefreshing()) {
                    easyRefreshLayout.refreshComplete();
                }*/
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
        });
    }

    private void getAccountInfo(final String phoneNum, final String pwd) {
        AppModelFactory.model().getAccountInfo(phoneNum, new NoneProgressSubscriber<DataBean<AccountInfoBean>>(mContext) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(DataBean<AccountInfoBean> result) {
                if (null != result) {

                    if (null != result.data) {

                        if (null != result.data.staff) {

                            AccountInfoBean.StaffInfoBean bean = result.data.staff;

                            LoginInfoPrefs.getInstance(mContext).saveStaffId(bean.staff_id);
                            LoginInfoPrefs.getInstance(mContext).saveGroupId(bean.group_id);
                            UserfoPrefs.getInstance(mContext).setOg(bean.baseon_id, bean.baseon_type);
                            CrashReport.setUserId(mContext, phoneNum + "/" + pwd);

                            if (!TextUtils.isEmpty(bean.avatar)) {
                                if (bean.avatar.startsWith(getString(R.string.HTTP))) {
                                    GlideHelper.displayImage(mContext, bean.avatar, ivAvatar);
                                } else {
                                    GlideHelper.displayImage(mContext, getString(R.string.image_url_prefix) + bean.avatar, ivAvatar);
                                }
                            } else {
                                if (bean.sex.equals(Sex.MALE.desc)) {
                                    GlideHelper.displayImage(mContext, R.drawable.avatar_default_male, ivAvatar);
                                } else {
                                    GlideHelper.displayImage(mContext, R.drawable.avatar_default_female, ivAvatar);
                                }
                            }
                            TextViewUtils.setTextOrEmpty(tvUserName, bean.staff_name);
                            UserfoPrefs.getInstance(mContext).saveUserInfo(result.data.staff);
                            UserfoPrefs.getInstance(mContext).setGroupName(result.data.group.name);

                        }

                    }

                }
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                LogUtils.e(mContext.getString(R.string.error_no_network));
                showToast(mContext.getString(R.string.error_no_network));
            }

            @Override
            public void onRenewal(ApiException ex) {
                super.onRenewal(ex);
                reLogin();
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                AppUtils.clearThisUserInfo(mContext);
                showToast(ex.message);
                LogUtils.e("onNormalError-登录信息已过期，请重新登录");
            }

        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if (notifyEvent.tag == NotifyEvent.RREFRESH_DOT) {
            getUnread();
        }
    }

    private void reLogin() {
        showToast("登录信息已过期，请重新登录");
        LogUtils.e("登录信息已过期，请重新登录");
        AppUtils.clearThisUserInfo(mContext);
        //通知MainActivity销毁
        EvenManager.sendEvent(new NotifyEvent(NotifyEvent.LOGOUT));
        Intent intent = new Intent(mContext, LoginActivity.class);
        startActivity(intent);
        //它必需紧挨着startActivity()或者finish()函数之后调用"
        getActivity().overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
        getActivity().finish();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == ERROR_CODE.ERROR_NONE) {
            //获取定位信息
            double latitude = aMapLocation.getLatitude();
            double longitude = aMapLocation.getLongitude();
            getWeather(latitude + ":" + longitude);
        } else {
            //显示错误信息ErrCode是错误码，errInfo是错误信息，详见下方错误码表。
            LogUtils.d("erro info：" + aMapLocation.getErrorCode() + "---" + aMapLocation.getErrorInfo());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (null != locationClient) {
            locationClient.stopLocation();
        }
    }

    private void getAppGuideIcon() {
        if(TextUtils.isEmpty(LoginInfoPrefs.getInstance(mContext).getGroupId())){
            return;
        }
        AppModelFactory.model().getAppGuideIcon(LoginInfoPrefs.getInstance(mContext).getGroupId(), new NoneProgressSubscriber<DataBean<List<ResultLauchPic>>>(mContext) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(DataBean<List<ResultLauchPic>> o) {
                if(null != o.data){
                    Gson gson = new Gson();
                    String value = gson.toJson(o.data);
                    LoginInfoPrefs.getInstance(mContext).saveLauchPic(value);
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
            }
        });
    }

}


package cn.yuyun.yymy.ui.home.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.autonavi.ae.gmap.maploader.ERROR_CODE;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.example.amap.location.ALocationClientFactory;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.LogUtils;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestInSign;
import cn.yuyun.yymy.http.request.RequestOutSign;
import cn.yuyun.yymy.http.result.ResultAttendanceToday;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.store.StaffAttendanceRecordActivity;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.SignTimeView;
import cn.yuyun.yymy.view.dialog.AttendanceSignDialog;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author
 * @desc 考勤打卡
 * @date
 */
public class AttendanceSignActivity extends BaseActivity implements AMapLocationListener {

    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_rule)
    TextView tvRule;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.lineTopStart)
    View lineTopStart;
    @BindView(R.id.img_processStart)
    ImageView imgProcessStart;
    @BindView(R.id.lineBottomEnd)
    View lineBottomEnd;
    @BindView(R.id.img_processEnd)
    ImageView imgProcessEnd;
    @BindView(R.id.tv_onTime)
    TextView tvOnTime;
    @BindView(R.id.tv_signOnTime)
    TextView tvSignOnTime;
    @BindView(R.id.tv_onLate)
    TextView tvOnLate;
    @BindView(R.id.approval_people)
    TextView approvalPeople;
    @BindView(R.id.tv_offTime)
    TextView tvOffTime;
    @BindView(R.id.tv_signOffTime)
    TextView tvSignOffTime;
    @BindView(R.id.approval_state)
    LinearLayout approvalState;
    @BindView(R.id.tv_offLate)
    TextView tvOffLate;
    @BindView(R.id.rl_result)
    RelativeLayout rlResult;
    @BindView(R.id.insignView)
    SignTimeView signView;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.tv_onOut)
    TextView tvOnOut;
    @BindView(R.id.tv_offOut)
    TextView tvOffOut;
    @BindView(R.id.tv_currentAddress)
    TextView tvCurrentAddress;
    @BindView(R.id.iv_location)
    ImageView ivLocation;
    @BindView(R.id.ll_normal)
    LinearLayout llNormal;
    @BindView(R.id.ll_noPermission)
    LinearLayout llNoPermission;
    @BindView(R.id.ll_noRule)
    LinearLayout llNoRule;
    @BindView(R.id.ll_sign)
    LinearLayout llSign;
    @BindView(R.id.rl_record)
    RelativeLayout rlRecord;

    private AMapLocationClient locationClient;
    private ResultAttendanceToday rulesBean;
    private RequestOutSign requestOutSign = new RequestOutSign();

    private int signOnOff = 1;
    /**
     * 是否在打卡范围内
     */
    private boolean isInRange = false;

    private final int REQUEST_CODE = 1117;

    private int count = 0;
    private boolean isOn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_sign);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initData();
    }

    protected void initViews() {
        titleBar.setTilte("考勤打卡");

        if (UserfoPrefs.getInstance(mContext).getPermission()) {
            titleBar.setRightIcon(R.drawable.home_btn_rule);
            titleBar.setRightOnClicker(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mContext, AttendanceRuleActivity.class));
                }
            });
        }

        rlRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(StaffAttendanceRecordActivity.startStaffAttendanceRecordActivity(mContext, null));
            }
        });

        findViewById(R.id.tv_bind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, AttendanceRuleActivity.class));
            }
        });

        signView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rulesBean != null) {
                    //内勤打卡
                    if (isInRange) {
                        if (count >= 2) {
                            showToast("每天只能打两次内勤");
                            return;
                        }
                        insideSign();
                    } else {
                        //外勤打卡
                        startActivityForResult(AttendanceOutSignActivity.startAttendanceOutSignActivity(mContext, requestOutSign), REQUEST_CODE);
                    }
                } else {
                    showTextDialog("您还没有绑定考勤规则，请先绑定考勤规则", true);
                }
            }
        });

        locationClient = ALocationClientFactory.createLocationClient(mContext, ALocationClientFactory.createDefaultOption(15000), AttendanceSignActivity.this);

    }

    private void initData() {
        getAttendanceListWithTime(false, false);
    }


    /**
     * 打卡 打卡成功后在获取一次考勤记录
     */
    private void insideSign() {
        if (!hasInternet()) {
            showTextDialog("当前网络不可用", false);
            return;
        }

        if (rulesBean.internal_count == 2) {
            showToast("每天只能打两次内勤");
            return;
        }

        RequestInSign requestInSign = new RequestInSign();
        requestInSign.span_status = signOnOff;
        requestInSign.baseon_id = UserfoPrefs.getInstance(mContext).getOgId();
        requestInSign.baseon_type = UserfoPrefs.getInstance(mContext).getOgType();
        AppModelFactory.model().insideSign(requestInSign, new ProgressSubscriber<Object>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(Object result) {
                getAttendanceListWithTime(true, true);
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast(ex.message);
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast("网络异常，请检查网络");
            }

        });
    }



    /**
     * 获取此账号今天的考勤记录
     */
    private void getAttendanceListWithTime(final boolean isSign, final boolean isIn) {
        AppModelFactory.model().getAttendanceListToday(UserfoPrefs.getInstance(mContext).getBaseonType(), UserfoPrefs.getInstance(mContext).getBaseonId(), LoginInfoPrefs.getInstance(mContext).getStaffId(), new ProgressSubscriber<DataBean<ResultAttendanceToday>>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean<ResultAttendanceToday> result) {
                if (null != result.data) {
                    rulesBean = result.data;
                    ResultAttendanceToday bean = result.data;

                    if (TextUtils.isEmpty(bean.rule_name)) {
                        if (UserfoPrefs.getInstance(mContext).getPermission()) {
                            //没有考勤规则，但有添加规则权限
                            llNoRule.setVisibility(View.VISIBLE);
                            llSign.setVisibility(View.GONE);
                            llNoPermission.setVisibility(View.GONE);
                            rlRecord.setVisibility(View.GONE);
                        } else {
                            //没有考勤规则，没有添加规则权限
                            llNoPermission.setVisibility(View.VISIBLE);
                            llNoRule.setVisibility(View.GONE);
                            llSign.setVisibility(View.GONE);
                            rlRecord.setVisibility(View.GONE);
                        }
                    } else {
                        if(isSign){
                            if(isIn){
                                AttendanceSignDialog dialog = new AttendanceSignDialog(mContext);
                                dialog.show();
                                if(signOnOff == 1){
                                    dialog.setTips("上班打卡成功", DateTimeUtils.StringToDate(bean.time_up, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_HH_mm));
                                }else{
                                    dialog.setTips("下班打卡成功", DateTimeUtils.StringToDate(bean.time_down, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_HH_mm));
                                }
                            }else{
                                AttendanceSignDialog dialog = new AttendanceSignDialog(mContext);
                                dialog.show();
                                if(signOnOff == 1){
                                    dialog.setTips("外勤打卡成功", DateTimeUtils.StringToDate(bean.time_up, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_HH_mm));
                                }else{
                                    dialog.setTips("外勤打卡成功", DateTimeUtils.StringToDate(bean.time_down, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_HH_mm));
                                }
                            }
                        }
                        //有考勤规则
                        llNoPermission.setVisibility(View.GONE);
                        llNoRule.setVisibility(View.GONE);
                        llSign.setVisibility(View.VISIBLE);
                        rlRecord.setVisibility(View.VISIBLE);

                        if (result.data.type_up == ResultAttendanceToday.StatusOnType.NONE) {
                            signView.setTopText("上班打卡");
                            isOn = true;
                            signOnOff = 1;
                        } else if (result.data.type_down == ResultAttendanceToday.StatusOffType.NONE) {
                            signView.setTopText("下班打卡");
                            tvOnLate.setVisibility(View.VISIBLE);
                            TextViewUtils.setTextOrEmpty(tvOnLate, bean.type_up.desc);
                            tvOnLate.setSelected(bean.type_up.isSelected);
                            TextViewUtils.setTextOrEmpty(tvOnOut, "(" + bean.status_up.desc + ")");
                            isOn = false;
                            signOnOff = 2;
                        } else {
                            signView.setTopText("下班打卡");
                            tvOnLate.setVisibility(View.VISIBLE);
                            tvOnLate.setSelected(bean.type_up.isSelected);
                            TextViewUtils.setTextOrEmpty(tvOnLate, bean.type_up.desc);
                            TextViewUtils.setTextOrEmpty(tvOnOut, "(" + bean.status_up.desc + ")");

                            tvOffLate.setVisibility(View.VISIBLE);
                            TextViewUtils.setTextOrEmpty(tvOffLate, bean.type_down.desc);
                            tvOffLate.setSelected(bean.type_down.isSelected);
                            TextViewUtils.setTextOrEmpty(tvOffOut, "(" + bean.status_down.desc + ")");

                            isOn = false;
                            signOnOff = 2;
                        }

                        TextViewUtils.setTextOrEmpty(tvOnTime, bean.rule_up);
                        TextViewUtils.setTextOrEmpty(tvOffTime, bean.rule_down);
                        TextViewUtils.setTextOrEmpty(tvSignOnTime, DateTimeUtils.StringToDate(bean.time_up, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_HH_mm));
                        TextViewUtils.setTextOrEmpty(tvSignOffTime, DateTimeUtils.StringToDate(bean.time_down, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_HH_mm));
                        TextViewUtils.setTextOrEmpty(tvRule, bean.rule_name);
                        tvRule.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                        tvRule.setSingleLine(true);
                        tvRule.setSelected(true);
                        tvRule.setFocusable(true);
                        tvRule.setFocusableInTouchMode(true);

                        //2.开启定位，15秒一次定位
                        locationClient.stopLocation();
                        locationClient.startLocation();
                    }

                    if (!TextUtils.isEmpty(bean.head)) {
                        if (bean.head.startsWith(getString(R.string.HTTP))) {
                            GlideHelper.displayImage(mContext, bean.head, ResoureUtils.getDimension(mContext, R.dimen.sign_people_avatar), ivAvatar);
                        } else {
                            GlideHelper.displayImage(mContext, getString(R.string.image_url_prefix) + bean.head, ResoureUtils.getDimension(mContext, R.dimen.sign_people_avatar), ivAvatar);
                        }
                    }
                    TextViewUtils.setTextOrEmpty(tvName, bean.staff_name);

                } else {
                    llNoRule.setVisibility(View.VISIBLE);
                    llSign.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                tvRule.setText("");
                if (ex.code == 20103) {
                    if (UserfoPrefs.getInstance(mContext).getPermission()) {
                        //没有考勤规则，但有添加规则权限
                        llNoRule.setVisibility(View.VISIBLE);
                        llSign.setVisibility(View.GONE);
                        llNoPermission.setVisibility(View.GONE);
                        rlRecord.setVisibility(View.GONE);
                    } else {
                        //没有考勤规则，没有添加规则权限
                        llNoPermission.setVisibility(View.VISIBLE);
                        llNoRule.setVisibility(View.GONE);
                        llSign.setVisibility(View.GONE);
                        rlRecord.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast("网络异常，请检查网络");
            }

        });
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == ERROR_CODE.ERROR_NONE) {
            //获取定位信息
            double latitude = aMapLocation.getLatitude();
            double longitude = aMapLocation.getLongitude();
            String addressStr = aMapLocation.getCity() + aMapLocation.getDistrict() + aMapLocation.getStreet();
            if (!TextUtils.isEmpty(aMapLocation.getAoiName())) {
                addressStr += aMapLocation.getAoiName();
            } else {
                addressStr = addressStr + aMapLocation.getPoiName();
//                addressStr = addressStr + aMapLocation.getPoiName() + "附近";
            }
            if (rulesBean != null) {
                if (rulesBean.lat != 0 && rulesBean.lng != 0) {
                    float distance = AMapUtils.calculateLineDistance(new LatLng(latitude, longitude), new LatLng(rulesBean.lat, rulesBean.lng));
                    if (distance > rulesBean.effective) {
                        TextViewUtils.setTextOrEmpty(tvCurrentAddress, addressStr);
                        isInRange = false;
                        signView.setTopText("外勤打卡");
                        ivLocation.setVisibility(View.VISIBLE);
                        signView.setBackground(ResoureUtils.getDrawable(mContext, R.drawable.sign_out_bg));
                        LogUtils.d("定位成功：不在打卡范围内111111//--isInRange：" + isInRange + "//--distance" + distance + "//--" + latitude + "//--" + longitude);
                    } else {
                        ivLocation.setVisibility(View.GONE);
                        if (isOn) {
                            signView.setTopText("上班打卡");
                            signOnOff = 1;
                        } else {
                            signView.setTopText("下班打卡");
                            signOnOff = 2;
                        }
                        signView.setBackground(ResoureUtils.getDrawable(mContext, R.drawable.sign_in_bg));
                        tvCurrentAddress.setText("已在考勤范围之内");
                        isInRange = true;
                        LogUtils.d("定位成功：已在打卡范围内--isInRange:" + isInRange + "//--distance" + distance + "//--" + latitude + "//--" + longitude);
                    }
                } else {
                    TextViewUtils.setTextOrEmpty(tvCurrentAddress, addressStr);
                    signView.setBackground(ResoureUtils.getDrawable(mContext, R.drawable.sign_out_bg));
                    isInRange = false;
                    signView.setTopText("外勤打卡");
                    ivLocation.setVisibility(View.VISIBLE);
                    LogUtils.d("定位成功：不在打卡范围内222222");
                }
            } else {
                TextViewUtils.setTextOrEmpty(tvCurrentAddress, addressStr);
                signView.setBackground(ResoureUtils.getDrawable(mContext, R.drawable.sign_out_bg));
                isInRange = false;
                signView.setTopText("外勤打卡");
                ivLocation.setVisibility(View.VISIBLE);
                LogUtils.d("定位成功：不在打卡范围内33333");
            }
            requestOutSign.place = addressStr;
            requestOutSign.lat = latitude;
            requestOutSign.lng = longitude;
        } else {
            isInRange = false;
            tvCurrentAddress.setText("不在打卡范围内");
            signView.setBackground(ResoureUtils.getDrawable(mContext, R.drawable.sign_out_bg));
            LogUtils.d("定位成功：不在打卡范围内444444");
            signView.setTopText("外勤打卡");
            ivLocation.setVisibility(View.VISIBLE);
            LogUtils.d("定位失败：isInRange:" + isInRange);
            //显示错误信息ErrCode是错误码，errInfo是错误信息，详见下方错误码表。
            LogUtils.d("erro info：" + aMapLocation.getErrorCode() + "---" + aMapLocation.getErrorInfo());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        initData();
    }

    @Override
    public void onDestroy() {
        if (locationClient != null) {
            locationClient.stopLocation();
            locationClient.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (locationClient != null) {
            locationClient.stopLocation();
        }
    }

    private List<String> piclList = new ArrayList<>();



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            getAttendanceListWithTime(true, false);
        }
    }

}

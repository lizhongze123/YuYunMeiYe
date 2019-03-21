package cn.yuyun.yymy.main;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.autonavi.ae.gmap.maploader.ERROR_CODE;
import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Calendar;

import cn.example.amap.location.ALocationClientFactory;
import cn.lzz.utils.LogUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.constan.Constans;
import cn.yuyun.yymy.http.result.ResultWeather;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.home.actions.AddActionActivity;
import cn.yuyun.yymy.ui.home.actions.AddNoticeActivity;
import cn.yuyun.yymy.ui.home.appointment.AddAppointmentActivity;
import cn.yuyun.yymy.ui.home.leave.NewLeaveActivity;
import cn.yuyun.yymy.ui.home.member.AddMemberActivity;
import cn.yuyun.yymy.ui.home.unboxing.PublishUnboxingActivity;
import cn.yuyun.yymy.ui.home.work.PublishMomentsActivity;
import cn.yuyun.yymy.utils.BlurBuilder;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.dialog.TextDialog;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class PubActivity extends Activity implements View.OnClickListener, View.OnTouchListener, AMapLocationListener {

    private View mPanelView;
    private View mWork;
    private View mAppointment;
    private View mUnboxing;
    private View mLeave;
    private View mAction;
    private View mNotice;
    private View mCloseButton;
    private View mMember;

    private Animation mButtonInAnimation;
    private Animation mButtonInAnimationTwo;
    private Animation mButtonInAnimationThree;
    private Animation mButtonInAnimationFour;

    private Animation mButtonOutAnimation;
    private Animation mButtonOutAnimationTwo;
    private Animation mButtonOutAnimationThree;
    private Animation mButtonOutAnimationFour;
    private Animation mButtonOutAnimationFour2;

    private Animation mButtonScaleLargeAnimation;
    private Animation mButtonScaleSmallAnimation;
    private Animation mCloseRotateAnimation;

    private final int REQUEST_CODE = 1111;

    private TextView tvWeaher;
    private ImageView ivWeather;
    private AMapLocationClient locationClient;

    public static void show(Context context) {
        context.startActivity(new Intent(context, PubActivity.class));
        ((Activity) context).overridePendingTransition(
                android.view.animation.Animation.INFINITE,
                android.view.animation.Animation.INFINITE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置状态栏为全透明
        setContentView(R.layout.activity_pub);
//        StatusBarCompat.setStatusBarColor(this, getResources().getColor(android.R.color.transparent));
//        StatusBarCompat.setStatusBarColor(this, Color.TRANSPARENT);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.title_bg));
//        StatusBarCompat.resetActionBarContainerTopMargin(getWindow(), android.support.v7.appcompat.R.id.action_bar_container);
//        StatusBarCompat.setTranslucent(this.getWindow(),true);

        ImageView iv = (ImageView) findViewById(R.id.iv);
        iv.setImageBitmap(BlurBuilder.blur(iv));
        initView();
        initAnimation();
        openPanelView();
        locationClient = ALocationClientFactory.createLocationClient(PubActivity.this, ALocationClientFactory.createOnceOption(), this);
        locationClient.stopLocation();
        locationClient.startLocation();


    }

    private void getWeather(String location) {
        try {
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(null != response){
                                String s = null;
                                try {
                                    s = response.body().string();
                                    if(!TextUtils.isEmpty(s)){
                                        ResultWeather weather = new Gson().fromJson(s, ResultWeather.class);
                                        if(weather.results.size() != 0){
                                            String name = weather.results.get(0).location.name;
                                            String text = weather.results.get(0).now.text;
                                            String temperature = weather.results.get(0).now.temperature;
                                            tvWeaher.setText(name + "：" + text + " " + temperature + " ℃");
                                            ivWeather.setImageResource(weather.results.get(0).now.getCode());
                                        }

                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void initView() {
//        View addButton = findViewById(R.id.add);
        mPanelView = findViewById(R.id.panel);
        mCloseButton = findViewById(R.id.close);
        mWork = findViewById(R.id.ll_work);
        mAppointment = findViewById(R.id.ll_appointment);
        mUnboxing = findViewById(R.id.ll_unboxing);
        mLeave = findViewById(R.id.ll_leave);
        mAction = findViewById(R.id.ll_action);
        mNotice = findViewById(R.id.ll_notice);
        mMember = findViewById(R.id.ll_member);
        mWork.setOnClickListener(this);
        mMember.setOnClickListener(this);
        mAppointment.setOnClickListener(this);
        mUnboxing.setOnClickListener(this);
        mLeave.setOnClickListener(this);
        mAction.setOnClickListener(this);
        mNotice.setOnClickListener(this);

//        addButton.setOnClickListener(this);
        mCloseButton.setOnClickListener(this);

        mWork.setOnTouchListener(this);
        mMember.setOnTouchListener(this);
        mAppointment.setOnTouchListener(this);
        mUnboxing.setOnTouchListener(this);
        mLeave.setOnTouchListener(this);
        mAction.setOnTouchListener(this);
        mNotice.setOnTouchListener(this);

        if (!UserfoPrefs.getInstance(PubActivity.this).getPermission()) {
            mAction.setVisibility(View.INVISIBLE);
            mNotice.setVisibility(View.INVISIBLE);
        } else {
            mAction.setVisibility(View.VISIBLE);
            mNotice.setVisibility(View.VISIBLE);
        }

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        if(month.length() < 2){
            month = "0" + month;
        }
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        if(day.length() < 2){
            day = "0" + day;
        }
        String week = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
        if("1".equals(week)){
            week ="星期日";
        }else if("2".equals(week)){
            week ="星期一";
        }else if("3".equals(week)){
            week ="星期二";
        }else if("4".equals(week)){
            week ="星期三";
        }else if("5".equals(week)){
            week ="星期四";
        }else if("6".equals(week)){
            week ="星期五";
        }else if("7".equals(week)){
            week ="星期六";
        }
        ((TextView)findViewById(R.id.tv_day)).setText(day);
        ((TextView)findViewById(R.id.tv_date)).setText(month + "/" + year);
        ((TextView)findViewById(R.id.tv_week)).setText(week);
        tvWeaher = (TextView)findViewById(R.id.tv_weather);
        ivWeather = (ImageView) findViewById(R.id.iv_weather);

        TextView tvDesc = (TextView) findViewById(R.id.tv_desc);
        ImageView ivPic = (ImageView) findViewById(R.id.iv_pic);
        if(null != Constans.globalPic){
            if(null != Constans.globalPic.resource.get(8).resourceRspList){
                if(Constans.globalPic.resource.get(8).resourceRspList.size() > 0){
                    GlideHelper.displayImage(this, Constans.globalPic.resource.get(8).resourceRspList.get(0).url, ivPic);
                    TextViewUtils.setTextOrEmpty(tvDesc, Constans.globalPic.resource.get(8).resourceRspList.get(0).name);
                }
            }
        }
    }

    /**初始化动画*/
    private void initAnimation() {
        mButtonInAnimation = AnimationUtils.loadAnimation(this, R.anim.button_in);
        mButtonInAnimationTwo = AnimationUtils.loadAnimation(this, R.anim.button_in_two);
        mButtonInAnimationThree = AnimationUtils.loadAnimation(this, R.anim.button_in_three);
        mButtonInAnimationFour = AnimationUtils.loadAnimation(this, R.anim.button_in_four);

        mButtonOutAnimation = AnimationUtils.loadAnimation(this, R.anim.button_out);
        mButtonOutAnimationTwo = AnimationUtils.loadAnimation(this, R.anim.button_out_two);
        mButtonOutAnimationThree = AnimationUtils.loadAnimation(this, R.anim.button_out_three);
        mButtonOutAnimationFour = AnimationUtils.loadAnimation(this, R.anim.button_out_four);
        mButtonOutAnimationFour2 = AnimationUtils.loadAnimation(this, R.anim.button_out_four);
        mButtonOutAnimation.setFillAfter(true);
        mButtonOutAnimationTwo.setFillAfter(true);
        mButtonOutAnimationThree.setFillAfter(true);
        mButtonOutAnimationFour.setFillAfter(true);
        mButtonOutAnimationFour2.setFillAfter(true);

        mButtonScaleLargeAnimation = AnimationUtils.loadAnimation(this, R.anim.button_scale_to_large);
        mButtonScaleSmallAnimation = AnimationUtils.loadAnimation(this, R.anim.button_scale_to_small);
        mCloseRotateAnimation = AnimationUtils.loadAnimation(this, R.anim.close_rotate);

        mButtonOutAnimationThree.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 6个按钮的退出动画执行完毕后，将面板隐藏
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void openPanelView() {
        mPanelView.setVisibility(View.VISIBLE);
        mWork.startAnimation(mButtonInAnimation);
        mAppointment.startAnimation(mButtonInAnimationTwo);
        mUnboxing.startAnimation(mButtonInAnimationThree);
        mLeave.startAnimation(mButtonInAnimationFour);
        mMember.startAnimation(mButtonInAnimation);

        if (UserfoPrefs.getInstance(PubActivity.this).getPermission()) {
            mAction.startAnimation(mButtonInAnimationTwo);
            mNotice.startAnimation(mButtonInAnimationThree);
        }
//        mCloseButton.startAnimation(mCloseRotateAnimation);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mCloseButton, "rotation", 0f, 135f);
        objectAnimator.setDuration(300);
        objectAnimator.start();
    }

    private void closePanelView() {
        // 给6个按钮添加退出动画
//        mCloseButton.startAnimation(mCloseRotateAnimation);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mCloseButton, "rotation", 135f, 0f);
        objectAnimator.setDuration(300);
        objectAnimator.start();
        mMember.startAnimation(mButtonOutAnimation);
        mWork.startAnimation(mButtonOutAnimationTwo);

        mAppointment.startAnimation(mButtonOutAnimationTwo);
        mUnboxing.startAnimation(mButtonOutAnimationThree);
        mLeave.startAnimation(mButtonOutAnimationFour);

        if (UserfoPrefs.getInstance(PubActivity.this).getPermission()) {
            mAction.startAnimation(mButtonOutAnimationTwo);
            mNotice.startAnimation(mButtonOutAnimationTwo);
        }
    }

    @Override
    public void onBackPressed() {
        if (mPanelView.getVisibility() == View.VISIBLE) {
            closePanelView();
        }
    }

    @Override
    public boolean onTouch(final View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 手指按下，按钮执行放大动画
                v.startAnimation(mButtonScaleLargeAnimation);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 手指移开，按钮执行缩小动画
                v.startAnimation(mButtonScaleSmallAnimation);
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 缩小动画执行完毕后，将按钮的动画清除。这里的150毫秒是缩小动画的执行时间。
                        v.clearAnimation();
                        switch (v.getId()) {
                            case R.id.ll_work:
                                if(TextUtils.isEmpty(LoginInfoPrefs.getInstance(PubActivity.this).getStaffId())){
                                    showTextDialog(getString(R.string.no_bind_staff), false);
                                    return;
                                }
                                startActivity(new Intent(PubActivity.this, PublishMomentsActivity.class));
                                finish();
                                break;
                            case R.id.ll_appointment:
                                if(TextUtils.isEmpty(LoginInfoPrefs.getInstance(PubActivity.this).getStaffId())){
                                    showTextDialog(getString(R.string.no_bind_staff), false);
                                    return;
                                }
                                //总部员工账号禁止预约
                                if (UserfoPrefs.getInstance(PubActivity.this).getBaseonType().equals("1")) {
                                    showTextDialog("总部员工账号禁止预约", false);
                                } else {
                                    startActivity(new Intent(PubActivity.this, AddAppointmentActivity.class));
                                    finish();
                                }
                                break;
                            case R.id.ll_unboxing:
                                if(TextUtils.isEmpty(LoginInfoPrefs.getInstance(PubActivity.this).getStaffId())){
                                    showTextDialog(getString(R.string.no_bind_staff), false);
                                    return;
                                }
                                startActivityForResult(new Intent(PubActivity.this, PublishUnboxingActivity.class), REQUEST_CODE);
                                finish();
                                break;
                            case R.id.ll_leave:
                                if(TextUtils.isEmpty(LoginInfoPrefs.getInstance(PubActivity.this).getStaffId())){
                                    showTextDialog(getString(R.string.no_bind_staff), false);
                                    return;
                                }
                                startActivity(new Intent(PubActivity.this, NewLeaveActivity.class));
                                finish();
                                break;
                            case R.id.ll_action:
                                if(TextUtils.isEmpty(LoginInfoPrefs.getInstance(PubActivity.this).getStaffId())){
                                    showTextDialog(getString(R.string.no_bind_staff), false);
                                    return;
                                }
                                startActivity(new Intent(PubActivity.this, AddActionActivity.class));
                                finish();
                                break;
                            case R.id.ll_notice:
                                if(TextUtils.isEmpty(LoginInfoPrefs.getInstance(PubActivity.this).getStaffId())){
                                    showTextDialog(getString(R.string.no_bind_staff), false);
                                    return;
                                }
                                startActivity(new Intent(PubActivity.this, AddNoticeActivity.class));
                                finish();
                                break;
                            case R.id.ll_member:
                                if(TextUtils.isEmpty(LoginInfoPrefs.getInstance(PubActivity.this).getStaffId())){
                                    showTextDialog(getString(R.string.no_bind_staff), false);
                                    return;
                                }
                                startActivityForResult(AddMemberActivity.startAddMemberActivityForNormal(PubActivity.this), REQUEST_CODE);
                                finish();
                                break;
                            default:
                        }
                    }
                }, 150);
                break;
            default:
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                openPanelView();
                break;
            case R.id.close:
                closePanelView();
                break;
            default:
        }
    }

    private TextDialog textDialog;

    public void showTextDialog(final String text, final boolean isNeedCancel) {
        if (textDialog == null) {
            textDialog = new TextDialog(PubActivity.this);
        }
        textDialog.setText(text);
        textDialog.isNeedCancel(isNeedCancel);
        textDialog.show();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.activity_alpha_out);
//        overridePendingTransition(android.view.animation.Animation.INFINITE, android.view.animation.Animation.INFINITE);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        LogUtils.d("onLocationChanged");
        if (aMapLocation != null && aMapLocation.getErrorCode() == ERROR_CODE.ERROR_NONE) {
            //获取定位信息
            double latitude = aMapLocation.getLatitude();
            double longitude = aMapLocation.getLongitude();
            getWeather(latitude + ":" + longitude);
        }else{
            //显示错误信息ErrCode是错误码，errInfo是错误信息，详见下方错误码表。
            LogUtils.d("erro info：" + aMapLocation.getErrorCode()+"---"+aMapLocation.getErrorInfo());
        }
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
        if(locationClient != null){
            locationClient.stopLocation();
        }
    }
}

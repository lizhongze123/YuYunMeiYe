package cn.yuyun.yymy.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.ApiRequestInterceptor;
import com.example.http.DataBean;
import com.example.http.NoneProgressSubscriber;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.bugly.crashreport.CrashReport;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.lzz.utils.AppPermission;
import cn.lzz.utils.LogUtils;
import cn.lzz.utils.PreferenceUtils;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.ToastUtils;
import cn.lzz.utils.VersionUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.app.MyApplication;
import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.constan.Constans;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.AccountInfoBean;
import cn.yuyun.yymy.http.result.ResultLauchPic;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.group.GroupListActivity;
import cn.yuyun.yymy.utils.GlideHelper;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 *
 * @author
 * @date
 */
public class TransitionActivity extends BaseNoTitleActivity {

    private ImageView iv_pic;
    private TextView tv_jump;
    private ImageView iv_defultPic;
    private boolean isIn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //进入应用初始化设置成未登录状态
//        AppStatusManager.getInstance().setAppStatus(STATUS_NORMAL);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);
    }

    @Override
    protected void setUpViewAndData() {
        initView();
        initData();
    }

    private void initView() {
        iv_pic = (ImageView) findViewById(R.id.iv_pic);
        iv_defultPic = (ImageView) findViewById(R.id.iv_defultPic);
        tv_jump = (TextView) findViewById(R.id.tv_jump);
    }

    private void initData() {
        //设置欢迎界面
        String json = LoginInfoPrefs.getInstance(mContext).getLauchPic();
        if(TextUtils.isEmpty(json)){
            iv_defultPic.setImageDrawable(ResoureUtils.getDrawable(this, R.drawable.launch_pic));
        }else{
            Gson gson = new Gson();
            Type t = new TypeToken<List<ResultLauchPic>>() {}.getType();
            List<ResultLauchPic> list = new ArrayList<>();
            list = gson.fromJson(json, t);
            if(null != list && list.size() > 0){
                Random ra = new Random();
                int random = ra.nextInt(list.size());
                if (list.get(random).url.startsWith(getString(R.string.HTTP))) {
                    GlideHelper.displayImage(mContext, list.get(random).url, iv_defultPic);
                } else {
                    GlideHelper.displayImage(mContext, getString(R.string.image_url_prefix) + list.get(random).url, iv_defultPic);
                }
            }else{
                iv_defultPic.setImageDrawable(ResoureUtils.getDrawable(this, R.drawable.launch_pic));
            }
        }
        if (Build.VERSION.SDK_INT >= 23) {
            String[] permissionGroup = AppPermission.getNeedPermission(TransitionActivity.this);
            if(null !=  permissionGroup){
                requestPermission(permissionGroup);
                LogUtils.e("申请权限----"+ permissionGroup.length);
            }else{
                startApp();
                LogUtils.e("不用申请权限");
            }
        } else {
            startApp();
        }
    }

    private void requestPermission(String... permissions) {
        AndPermission.with(this)
                .permission(permissions)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        LogUtils.e("onGranted");
                        startApp();
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
                        LogUtils.e("onDenied");
                        if (AndPermission.hasAlwaysDeniedPermission(TransitionActivity.this, permissions)) {
                            showToast("部分权限被禁用,可能会导致部分功能失效");
                        }
                        startApp();
                    }
                })
                .start();
    }

    private void startApp() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DoAction();
            }
        }, 1500);
    }

    private void DoAction() {
        String currentVersion = VersionUtils.getAppVersionName(this);
        String version = PreferenceUtils.readString(this, "Version", "version","");
        if(!currentVersion.equals(version)){
            PreferenceUtils.write(this, "Version", "version", currentVersion);
            guide();
        }else{
            ComeingApp();
        }
    }

    /**
     * 直接进入app
     */
    private void ComeingApp() {
        if(TextUtils.isEmpty(LoginInfoPrefs.getInstance(TransitionActivity.this).getToken())){
            toMainActivity(MainActivity.class);
        }else{
            setTooken();
        }
    }

    /**
     * 进入指导界面
     */
    private void guide() {
        Intent intent = new Intent();
        intent.setClass(this, GuideActivity.class);
        startActivity(intent);
        finish();
    }

    private void setTooken(){
        ApiRequestInterceptor.getInstance().setToken(LoginInfoPrefs.getInstance(TransitionActivity.this).getToken());
        if(LoginInfoPrefs.getInstance(mContext).getLoginerType()){
            Constans.isStoreLoginer = true;
            toMainActivity(MainActivity.class);
        }else{
            Constans.isStoreLoginer = false;
            toMainActivity(GroupListActivity.class);
        }
    }

    private void getAccountInfo(final String phoneNum) {
        AppModelFactory.model().getAccountInfo(phoneNum, new NoneProgressSubscriber<DataBean<AccountInfoBean>>(TransitionActivity.this) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(DataBean<AccountInfoBean> result) {
                if (null != result) {

                    if(null != result.data){

                        if(null != result.data.staff){
                            //保存账号信息在本地
                            LoginInfoPrefs.getInstance(TransitionActivity.this).saveStaffId(result.data.staff.staff_id);
                            LoginInfoPrefs.getInstance(TransitionActivity.this).saveGroupId(result.data.staff.group_id);
                            UserfoPrefs.getInstance(TransitionActivity.this).setOg(result.data.staff.baseon_id, result.data.staff.baseon_type);
                            CrashReport.setUserId(TransitionActivity.this, phoneNum);
                            //获取权限
                            toMainActivity(MainActivity.class);
                        }else{
                            //没有绑定员工
                        }

                    }
                }
            }

        });

    }

    private void reLogin(){
        ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(),"获取账号信息有误，请重新登录");
        toMainActivity(LoginActivity.class);
    }

    private void toMainActivity(Class<?> clazz) {
        if (isIn) {
            return;
        }
        Intent intent = new Intent(this, clazz);
        if(clazz == MainActivity.class){
            if(getIntent().getStringExtra(EXTRA_DATA) != null){
                intent.putExtra(EXTRA_DATA,
                        getIntent().getStringExtra(EXTRA_DATA));
            }
        }else{}
        startActivity(intent);
        //它必需紧挨着startActivity()或者finish()函数之后调用"
        overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
        finish();
        isIn = true;
    }


}

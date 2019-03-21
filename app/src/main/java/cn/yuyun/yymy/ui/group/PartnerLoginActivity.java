package cn.yuyun.yymy.ui.group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.ApiRequestInterceptor;
import com.example.http.DataBean;
import com.example.http.NoneProgressSubscriber;
import com.example.http.ProgressSubscriber;
import com.githang.statusbar.StatusBarCompat;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import cn.lzz.utils.LogUtils;
import cn.lzz.utils.ToastUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.app.MyApplication;
import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.constan.Constans;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestLogin;
import cn.yuyun.yymy.http.request.RequestPartnerLogin;
import cn.yuyun.yymy.http.result.AccountInfoBean;
import cn.yuyun.yymy.http.result.TokenBean;
import cn.yuyun.yymy.main.LoginAccountBean;
import cn.yuyun.yymy.main.LoginActivity;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.SessionPrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.utils.AppUtils;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.EditTextWithDel;

/**
 * @author
 * @desc 合伙人登录Activity
 * @date
 */
public class PartnerLoginActivity extends BaseNoTitleActivity {

    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.et_phoneNum)
    EditTextWithDel etPhoneNum;
    @BindView(R.id.et_pwd)
    EditTextWithDel etPwd;
    @BindView(R.id.cb_eye)
    CheckBox cbEye;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.login_bg));
        setContentView(R.layout.activity_login_partner);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
    }

    private void initViews() {
        initSoftKeyboard();
        etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        cbEye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //设置密码可见
                    etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //设置密码不可见
                    etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etPhoneNum.getText().toString().trim())) {
                    ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), "请输入手机号码");
                    return;
                } else if (TextUtils.isEmpty(etPwd.getText().toString().trim())) {
                    ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), "请输入密码");
                    return;
                }
                login(etPhoneNum.getText().toString().trim(), etPwd.getText().toString().trim());
            }
        });

    }

    private void login(final String phoneNum, final String pwd) {
        AppModelFactory.model().accessTokenAdmin(new RequestPartnerLogin(phoneNum, pwd), new ProgressSubscriber<DataBean<TokenBean>>(this) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean<TokenBean> result) {
                if (result != null) {
                    if(null != result.data){
                        //记住密码，下次自动登录
                        LoginInfoPrefs.getInstance(mContext).saveLoginInfo(phoneNum, pwd);
                        //登录成功后保存tooken在本地
                        LoginInfoPrefs.getInstance(mContext).saveToken("bearer", result.data.accessToken);
                        //要先设置tooken
                        ApiRequestInterceptor.getInstance().setToken("bearer" + result.data.accessToken);
//                        SessionPrefs.getInstance(mContext).saveLoginInfo(phoneNum, pwd);
                        Constans.isStoreLoginer = false;
                        LoginInfoPrefs.getInstance(mContext).saveLoginerType(false);
                        startActivity(new Intent(mContext, GroupListActivity.class));
                        EvenManager.sendEvent(new NotifyEvent(NotifyEvent.LOGOUT));
                        PartnerLoginActivity.this.finish();
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
                showToast(getString(R.string.NO_INTERNET));
            }
        });

    }

    private InputMethodManager inputMethodManager;

    private void initSoftKeyboard() {
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    public void hideInputMenu() {
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}

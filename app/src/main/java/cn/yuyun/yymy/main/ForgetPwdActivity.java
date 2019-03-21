package cn.yuyun.yymy.main;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.ApiRequestInterceptor;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;
import com.githang.statusbar.StatusBarCompat;

import cn.jpush.android.api.JPushInterface;
import cn.lzz.utils.LogUtils;
import cn.lzz.utils.PhoneUtils;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.ToastUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.app.MyApplication;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestLogin;
import cn.yuyun.yymy.http.request.RequestRestPwd;
import cn.yuyun.yymy.http.result.TokenBean;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.SessionPrefs;
import cn.yuyun.yymy.utils.AppUtils;
import cn.yuyun.yymy.utils.EvenManager;

import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author
 * @desc
 * @date
 */
public class ForgetPwdActivity extends AppCompatActivity {

    private EditText etPhoneNum, etPwd, etVerificationCode;
    private CheckBox cbEye;
    private TextView tvVerificationCode, tvSubmit, tvTop;
    private MyCountDownTimer myCountDownTimer;

    private static String type_reset = "RESET";
    private static String type_forget = "forget";
    private String type;

    public static Intent startForgetPwdActivity(Context context) {
        return new Intent(context, ForgetPwdActivity.class).putExtra(EXTRA_TYPE, type_forget);
    }

    public static Intent startResetPwdActivity(Context context) {
        return new Intent(context, ForgetPwdActivity.class).putExtra(EXTRA_TYPE, type_reset);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.white));
        initViews();
    }

    private void initViews() {
        tvTop = (TextView) findViewById(R.id.tv_top);
        type = getIntent().getStringExtra(EXTRA_TYPE);
        if(type.equals(type_reset)){
            tvTop.setText("修改密码");
        }else{
            tvTop.setText("忘记密码");
        }

        etPhoneNum = (EditText) findViewById(R.id.et_phoneNum);
        etPwd = (EditText) findViewById(R.id.et_pwd);
        etVerificationCode = (EditText) findViewById(R.id.et_verificationCode);
        //输入监听
        TextChange textChange = new TextChange();
        etPhoneNum.addTextChangedListener(textChange);
        etPwd.addTextChangedListener(textChange);
        etVerificationCode.addTextChangedListener(textChange);
        cbEye = (CheckBox) findViewById(R.id.cb_eye);
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
        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvVerificationCode = (TextView) findViewById(R.id.tv_verificationCode);
        tvVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etPhoneNum.getText().toString().trim())) {
                    ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), "请输入手机号码");
                    return;
                }
                getVerificationCode(etPhoneNum.getText().toString().trim());
            }
        });
        tvSubmit = (TextView) findViewById(R.id.tv_submit);
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etPhoneNum.getText().toString().trim())) {
                    ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), "请输入手机号码");
                    return;
                } else if (TextUtils.isEmpty(etVerificationCode.getText().toString().trim())) {
                    ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), "请输入验证码");
                    return;
                } else if (TextUtils.isEmpty(etPwd.getText().toString().trim())) {
                    ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), "请输入密码");
                    return;
                } else if (etPwd.getText().toString().trim().length() < 6) {
                    ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), "请输入6位以上的密码");
                    return;
                }
                submit(etPhoneNum.getText().toString().trim(), etVerificationCode.getText().toString().trim(), etPwd.getText().toString().trim());
            }
        });
        myCountDownTimer = new MyCountDownTimer(60 * 1000, 1000);
    }

    private void submit(final String phoneNum, final String verificationCode, final String pwd) {
        RequestRestPwd body = new RequestRestPwd();
        body.username = phoneNum;
        body.sms_code = verificationCode;
        body.new_password = pwd;
        AppModelFactory.model().resetPwd(body, new ProgressSubscriber<DataBean<Object>>(this) {
            @Override
            public void onNext(DataBean<Object> result) {
                ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), "重置密码成功");
                if(type.equals(type_reset)){
                    JPushInterface.stopPush(getApplicationContext());
                    AppUtils.clearThisUserInfo(ForgetPwdActivity.this);
                    //要先设置tooken
                    ApiRequestInterceptor.getInstance().setToken("");
                    //通知MainActivity销毁
                    EvenManager.sendEvent(new NotifyEvent(NotifyEvent.LOGOUT));
                    Intent intent = new Intent(ForgetPwdActivity.this, LoginActivity.class);
                    startActivity(intent);
                    //它必需紧挨着startActivity()或者finish()函数之后调用"
                    overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
                    finish();
                }else{
                    finish();
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), "重置密码失败");
            }

        });

    }

    private void getVerificationCode(final String phoneNum) {
        AppModelFactory.model().getVerificationCode(phoneNum, new ProgressSubscriber<DataBean<Object>>(ForgetPwdActivity.this) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean<Object> result) {
                myCountDownTimer.start();
                ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), "已发送");
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), "获取验证码失败，请重试");
            }
        });

    }

    private class MyCountDownTimer extends CountDownTimer {

        MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tvVerificationCode.setClickable(false);
            tvVerificationCode.setText(millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish() {
            tvVerificationCode.setText("获取验证码");
            tvVerificationCode.setClickable(true);
        }
    }


    // EditText监听器
    private class TextChange implements TextWatcher {

        @Override
        public void afterTextChanged(Editable arg0) {

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {

        }

        @Override
        public void onTextChanged(CharSequence cs, int start, int before,
                                  int count) {
            boolean sign1 = etPhoneNum.getText().length() > 0;
            boolean sign2 = etVerificationCode.getText().length() > 0;
            boolean sign3 = etPwd.getText().length() > 0;
            if (sign1 & sign2 & sign3) {
                tvSubmit.setEnabled(true);
                tvSubmit.setBackground(ResoureUtils.getDrawable(ForgetPwdActivity.this, R.drawable.login_button_selector2));
                tvSubmit.setTextColor(ResoureUtils.getColor(ForgetPwdActivity.this, R.color.colorPrimary));
            } else {
                tvSubmit.setEnabled(false);
                tvSubmit.setBackground(ResoureUtils.getDrawable(ForgetPwdActivity.this, R.drawable.login_button_selector3));
                tvSubmit.setTextColor(ResoureUtils.getColor(ForgetPwdActivity.this, R.color.text_gray));
            }

        }
    }
}

package cn.yuyun.yymy.main;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.ApiRequestInterceptor;
import com.example.http.NoneProgressSubscriber;
import com.example.http.ProgressSubscriber;
import com.githang.statusbar.StatusBarCompat;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;
import cn.lzz.utils.LogUtils;
import cn.lzz.utils.ToastUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.app.MyApplication;
import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.constan.Constans;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import com.example.http.DataBean;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.yuyun.yymy.http.request.RequestLogin;
import cn.yuyun.yymy.http.result.AccountInfoBean;
import cn.yuyun.yymy.http.result.ResultPermission;
import cn.yuyun.yymy.http.result.ResultPermissionHq;
import cn.yuyun.yymy.http.result.TokenBean;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.SessionPrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.group.PartnerLoginActivity;
import cn.yuyun.yymy.ui.home.ItemBean;
import cn.yuyun.yymy.ui.home.other.UserAggrementActivity;
import cn.yuyun.yymy.utils.AppUtils;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.SoftKeyboardStateHelper;

/**
 * @author
 * @desc
 * @date
 */
public class LoginActivity extends BaseNoTitleActivity {

    /**浮动窗口*/
    private PopupWindow mPopupWindow;
    private LoginAdapter mOptionsAdapter;
    /**浮动窗口依附布局*/
    private LinearLayout parent;
    /** 浮动宽口的宽度*/
    private int pwidth;
    /** 浮动窗口显示标示符*/
    private boolean init_flag = false;
    private List<String> accountList = new ArrayList<>();

    @BindView(R.id.et_phoneNum)
    EditText etPhoneNum;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.tv_forget)
    TextView tvForgetPwd;
    @BindView(R.id.tv_submit)
    TextView tvLogin;
    @BindView(R.id.tv_registers)
    TextView tvRegister;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_partner)
    TextView tvPartner;
    @BindView(R.id.cb_down)
    CheckBox cbDown;
    @BindView(R.id.cb_eye)
    CheckBox cbEye;
    @BindView(R.id.iv_mascot)
    ImageView ivMascot;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.iv_hands)
    ImageView ivHands;
    @BindView(R.id.tv_userAggrement)
    TextView tvUserAggrement;
    private Animation mMascotAnimationIn;
    private Animation mMascotAnimationOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       /* setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }*/
        super.onCreate(savedInstanceState);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.login_bg));
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        EvenManager.register(this);
        initViews();
    }

    /**
     * 在此方法中初始化可以获得输入框的宽度，以便于创建同样宽的浮动窗口
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        while (!init_flag) {
            // 初始化UI
            initWedget();
            // 初始化浮动窗口
            initPopuWindow();
            init_flag = true;
        }
    }

    /**
     * 初始化UI控件
     */
    private void initWedget() {
        // 浮动窗口依附的布局
        this.parent = (LinearLayout) this.findViewById(R.id.ll_user);
        cbDown.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    hideInputMenu();
                    if(null != loginList && loginList.size() > 0){
                        if (init_flag) {
                            // 显示浮动窗口
                            mPopupWindow.showAsDropDown(parent, 0, -3);
                        }
                    }
                } else {
                    mPopupWindow.dismiss();
                }
            }
        });
        // 获取登陆数据
        getData();

        // 获取地址输入框的宽度，用于创建浮动窗口的宽度
        int w = parent.getWidth();
        pwidth = w;

    }

    private List<LoginAccountBean> loginList = new ArrayList<>();

    /**
     * 获取登录用户名数据
     */
    private void getData() {
        accountList = SessionPrefs.getInstance(LoginActivity.this).getAccount();
        for (int i = 0; i < accountList.size(); i++) {
            String avatar = SessionPrefs.getInstance(LoginActivity.this).getAvatar(accountList.get(i));
            LoginAccountBean bean = new LoginAccountBean();
            bean.avatar = avatar;
            bean.account = accountList.get(i);
            bean.staffName = accountList.get(i);
            loginList.add(bean);
        }
    }

    /**
     * 初始化浮动窗口
     */
    public void initPopuWindow() {
        // 浮动窗口的布局
        View loginwindow = (View) this.getLayoutInflater().inflate(R.layout.popwindow_login, null);
        RecyclerView listView = (RecyclerView) loginwindow.findViewById(R.id.rv);
        // 初始化适配器
        mOptionsAdapter = new LoginAdapter(this);
        mOptionsAdapter.setOnItemClickListener(new LoginAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                etPhoneNum.setText(accountList.get(position));
                String pwd = SessionPrefs.getInstance(LoginActivity.this).getPassword(accountList.get(position));
                String avatar = SessionPrefs.getInstance(LoginActivity.this).getAvatar(accountList.get(position));
                etPwd.setText(pwd);
                cbDown.setChecked(false);
                if (!TextUtils.isEmpty(avatar)) {
                    ivAvatar.setVisibility(View.VISIBLE);
                    if (avatar.startsWith(getString(R.string.HTTP))) {
                        GlideHelper.displayImage(LoginActivity.this, avatar, ivAvatar);
                    } else {
                        GlideHelper.displayImage(LoginActivity.this, getString(R.string.image_url_prefix) + avatar, ivAvatar);
                    }
                }else{
                    ivAvatar.setVisibility(View.GONE);
                }
            }

            @Override
            public void del(int position) {
                SessionPrefs.getInstance(LoginActivity.this).delAccount(accountList.get(position));
                accountList.remove(position);
                cbDown.setChecked(false);
            }

        });
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(mOptionsAdapter);
        mOptionsAdapter.addAll(loginList);
        // 定义一个浮动窗口，并设置
        mPopupWindow = new PopupWindow(loginwindow, pwidth,
                LinearLayout.LayoutParams.WRAP_CONTENT, false);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    private void initViews() {
        initSoftKeyboard();
        Animation mHandsAnimation = AnimationUtils.loadAnimation(this, R.anim.hands);
        Animation mMascotAnimation = AnimationUtils.loadAnimation(this, R.anim.mascot_nor);
        mMascotAnimationIn = AnimationUtils.loadAnimation(this, R.anim.mascot_in);
        mMascotAnimationOut = AnimationUtils.loadAnimation(this, R.anim.mascot_out);
        etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        etPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    ivMascot.startAnimation(mMascotAnimationIn);
                }else{
                    ivMascot.startAnimation(mMascotAnimationOut);
                }
            }
        });
        etPhoneNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != mPopupWindow && mPopupWindow.isShowing()){
                    mPopupWindow.dismiss();
                    cbDown.setChecked(false);
                }
            }
        });
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
                onBackPressed();
            }
        });

        tvPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, PartnerLoginActivity.class));
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String s = "";
                String s1 = "";
                if(TextUtils.isEmpty(LoginInfoPrefs.getInstance(LoginActivity.this).getToken())){
                    LogUtils.e("111111111111111");
                }else{
                    LogUtils.e("222222222222");
                }

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
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        tvForgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ForgetPwdActivity.startForgetPwdActivity(mContext));
            }
        });


        mHandsAnimation.setFillAfter(true);
        mMascotAnimation.setFillAfter(true);
        mMascotAnimationIn.setFillAfter(true);
        mMascotAnimationOut.setFillAfter(true);
//        ivHands.startAnimation(mHandsAnimation);
//        ivMascot.startAnimation(mMascotAnimation);

        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPhoneNum.setText("");
                ivAvatar.setVisibility(View.GONE);
            }
        });
        etPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(null != s.toString() && s.toString().length() > 0){
                    ivClear.setVisibility(View.VISIBLE);
                }else{
                    ivClear.setVisibility(View.GONE);
                }
            }
        });
        tvUserAggrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, UserAggrementActivity.class));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if(notifyEvent.tag == NotifyEvent.LOGOUT){
            finish();
        }
    }

    private void login(final String phoneNum, final String pwd) {
        AppUtils.clearThisUserInfo(LoginActivity.this);
        ApiRequestInterceptor.getInstance().setToken("");
        showLoadingDialog("加载中，请稍候...");
        AppModelFactory.model().accessToken(new RequestLogin(phoneNum, pwd), new NoneProgressSubscriber<DataBean<TokenBean>>(this) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(DataBean<TokenBean> result) {
                if (null != result) {
                    if(null != result.data){
                        EvenManager.sendEvent(new NotifyEvent(NotifyEvent.LOGIN));
                        //记住密码，下次自动登录
                        LoginInfoPrefs.getInstance(LoginActivity.this.getApplicationContext())
                                .saveLoginInfo(phoneNum, pwd);
                        //登录成功后保存tooken在本地
                        LoginInfoPrefs.getInstance(LoginActivity.this).saveToken(result.data.tokenType, result.data.accessToken);
                        LoginInfoPrefs.getInstance(mContext).saveLoginerType(true);
                        //要先设置tooken
                        ApiRequestInterceptor.getInstance().setToken(result.data.tokenType + result.data.accessToken);
                        SessionPrefs.getInstance(LoginActivity.this).saveLoginInfo(phoneNum, pwd);
                    }
                }
                //在登录成功后获取
                getAccountInfo(phoneNum, pwd);
            }

       /*     @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                dismissLoadingDialog();
                AppUtils.clearThisUserInfo(LoginActivity.this);
                LogUtils.e(ex.getMessage());
                ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), ex.message);
//                ToastUtils.toastInBottom(LoginActivity.this, "登录出错，请重试");
            }*/

            @Override
            public void onRenewal(ApiException ex) {
                super.onRenewal(ex);
                dismissLoadingDialog();
                AppUtils.clearThisUserInfo(LoginActivity.this);
                ToastUtils.toastInBottom(LoginActivity.this, "登录信息已过期，请重新登录");
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                dismissLoadingDialog();
                AppUtils.clearThisUserInfo(LoginActivity.this);
                LogUtils.e(ex.getMessage());
                ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), ex.message);
            }
        });

    }

    private void getAccountInfo(final String phoneNum, final String pwd) {
        AppModelFactory.model().getAccountInfo(phoneNum, new NoneProgressSubscriber<DataBean<AccountInfoBean>>(LoginActivity.this) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(DataBean<AccountInfoBean> result) {
                if (null != result) {
                    if(null != result.data){
                        if(null != result.data.staff){
                            SessionPrefs.getInstance(LoginActivity.this).saveAvatar(phoneNum, result.data.staff.avatar);
                            SessionPrefs.getInstance(LoginActivity.this).saveStaffName(phoneNum, result.data.staff.staff_name);
                            //保存账号信息在本地
                            LoginInfoPrefs.getInstance(LoginActivity.this).saveStaffId(result.data.staff.staff_id);
                            //设置推送别名
                            JPushInterface.resumePush(getApplicationContext());
                            if (null != result.data.staff.staff_id) {
                                String newStr = result.data.staff.staff_id.replace("-", "");
                                LogUtils.e(newStr);
                                JPushInterface.setAlias(LoginActivity.this, 1, newStr);
                            }
                            //设置推送标签
                            if (null != result.data.staff.group_id) {
                                Set<String> tagSet = new LinkedHashSet<>();
                                tagSet.add("group_id=" + result.data.staff.group_id);
                                JPushInterface.setTags(LoginActivity.this, 1, tagSet);
                            }
                            LoginInfoPrefs.getInstance(LoginActivity.this).saveGroupId(result.data.staff.group_id);
                            UserfoPrefs.getInstance(LoginActivity.this).setOg(result.data.staff.baseon_id, result.data.staff.baseon_type);
                            CrashReport.setUserId(LoginActivity.this, phoneNum + "/" + pwd);
                            //获取权限
                            getPermission(phoneNum);
                        }else{
                            //员工不存在，没有绑定员工
                            ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), "员工不存在");
                            AppUtils.clearThisUserInfo(LoginActivity.this);
                            SessionPrefs.getInstance(LoginActivity.this).delAccount(phoneNum);
                            dismissLoadingDialog();
                        }

                    }
                }
            }

            @Override
            public void onRenewal(ApiException ex) {
                super.onRenewal(ex);
                dismissLoadingDialog();
                AppUtils.clearThisUserInfo(LoginActivity.this);
                ToastUtils.toastInBottom(LoginActivity.this, "登录信息已过期，请重新登录");
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                dismissLoadingDialog();
                AppUtils.clearThisUserInfo(LoginActivity.this);
                LogUtils.e(ex.getMessage());
                ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), ex.message);
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                dismissLoadingDialog();
                ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), "网络出错");
            }


            /*
            @Override
            public void onRenewal(ApiException ex) {
                super.onRenewal(ex);
                dismissLoadingDialog();
                AppUtils.clearThisUserInfo(LoginActivity.this);
                ToastUtils.toastInBottom(LoginActivity.this, getString(R.string.app_tooken_tips));
            }

            @Override
            public void onOtherError(ApiException ex) {
                super.onOtherError(ex);
                dismissLoadingDialog();
                ToastUtils.toastInBottom(LoginActivity.this, "登录出错，请重试");
            }*/
        });

    }

    private void getPermission(final String phoneNum) {

        AppModelFactory.model().getPermission(LoginInfoPrefs.getInstance(this).getGroupId(), phoneNum, new NoneProgressSubscriber<DataBean<List<ResultPermission>>>(LoginActivity.this) {
            @Override
            public void onCompleted() {
                dismissLoadingDialog();
            }

            @Override
            public void onNext(DataBean<List<ResultPermission>> result) {
                //保存权限在本地
                if (null != result) {
                    if(null != result.data){
                        for (int i = 0; i < result.data.size(); i++) {
                            if (result.data.get(i).name.equals(getString(R.string.permission))) {
                                UserfoPrefs.getInstance(LoginActivity.this).setPermission(true);
                                break;
                            } else {
                                UserfoPrefs.getInstance(LoginActivity.this).setPermission(false);
                            }
                        }
                        Constans.isStoreLoginer = true;
                        ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), "登录成功");
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        LoginActivity.this.finish();
                    }

                }

            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                AppUtils.clearThisUserInfo(LoginActivity.this);
                ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), getString(R.string.app_permission_tips));
                LogUtils.e(ex.message);
            }
        });

    }


    @Override
    public void dismissLoadingDialog() {
        /**在移除view的时候，没有检查其activity的可用性，导致上述报错*/
        if ((null == LoginActivity.this) || this.isFinishing() || this.isRestricted()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                if (this.isDestroyed()) {
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } catch (Error e) {
                e.printStackTrace();
            }
        }

        super.dismissLoadingDialog();
    }

    @Override
    public void onBackPressed() {
//        EvenManager.sendEvent(new NotifyEvent(NotifyEvent.LOGIN));

        startActivity(new Intent(this, MainActivity.class));

        super.onBackPressed();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EvenManager.unregister(this);
    }
}

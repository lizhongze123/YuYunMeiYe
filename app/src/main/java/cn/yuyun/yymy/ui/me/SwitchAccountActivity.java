package cn.yuyun.yymy.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.ApiRequestInterceptor;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;
import cn.lzz.utils.LogUtils;
import cn.lzz.utils.ToastUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.app.MyApplication;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.constan.Constans;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestLogin;
import cn.yuyun.yymy.http.result.AccountInfoBean;
import cn.yuyun.yymy.http.result.ResultPermission;
import cn.yuyun.yymy.http.result.TokenBean;
import cn.yuyun.yymy.main.LoginAccountBean;
import cn.yuyun.yymy.main.LoginActivity;
import cn.yuyun.yymy.main.MainActivity;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.SessionPrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.utils.AppUtils;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.dialog.LogoutDialog;

/**
 * @author
 * @desc
 * @date
 */

public class SwitchAccountActivity extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.tv_del)
    TextView tvDel;
    @BindView(R.id.tv_logout)
    TextView tvLogout;

    private SwitchAccountAdapter mAdapter;
    private List<LoginAccountBean> loginList = new ArrayList<>();
    private LogoutDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_account);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
    }

    private void initViews() {
        titleBar.setTilte("账号切换");
        titleBar.setRightIcon(R.drawable.ic_del_one);
        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdapter.getItemCount() > 0) {
                    setDelVisiable(mAdapter.getDelVisiable());
                }
            }
        });
        tvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdapter.getSelectedItem().size() == 0) {
                    showToast("请选择要删除的账号");
                    return;
                } else {
                    delAccount(mAdapter.getSelectedItem());
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SwitchAccountAdapter(this);
        getData();
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new SwitchAccountAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(LoginAccountBean bean, int position) {
                if (mAdapter.getDelVisiable()) {
                    //删除
                    tvDel.setText("删除(" + mAdapter.getSelectedItem().size() + ")");
                } else {
                    //完成
                    login(bean.account, SessionPrefs.getInstance(mContext).getPassword(bean.account));
                }

            }
        });
        mAdapter.addAll(loginList);
        findViewById(R.id.tv_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog == null) {
                    dialog = new LogoutDialog(mContext);
                    dialog.setOnPositiveListener(new LogoutDialog.OnPositiveListener() {
                        @Override
                        public void onPositive() {
                            logout();
                        }
                    });
                    dialog.show();
                } else {
                    dialog.show();
                }
            }
        });
    }

    private void logout() {
        JPushInterface.stopPush(getApplicationContext());
        AppUtils.clearThisUserInfo(mContext);
        //要先设置tooken
        ApiRequestInterceptor.getInstance().setToken("");
        //通知MainActivity销毁
        EvenManager.sendEvent(new NotifyEvent(NotifyEvent.LOGOUT));
        Intent intent = new Intent(mContext, LoginActivity.class);
        startActivity(intent);
        //它必需紧挨着startActivity()或者finish()函数之后调用"
        overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
        finish();
    }

    private void delAccount(List<LoginAccountBean> selectedItem) {

        boolean isNeedLogout = false;

        for (int i = 0; i < selectedItem.size(); i++) {
            SessionPrefs.getInstance(mContext).delAccount(selectedItem.get(i).account);
            if (selectedItem.get(i).account.equals(LoginInfoPrefs.getInstance(mContext).getUserName())) {
                isNeedLogout = true;
            }
        }

        if (isNeedLogout) {
            logout();
        }else{
            getData();
            mAdapter.notifyDataSetChanged(loginList);
            setDelVisiable(mAdapter.getDelVisiable());
        }

        /*if(selectedItem.size() > 0){
            mAdapter.notifyDataSetChanged();
        }*/
    }

    /**
     * 获取登录用户名数据
     */
    private void getData() {
        loginList.clear();
        List<String> accountList;
        accountList = SessionPrefs.getInstance(this).getAccount();
        if (null != accountList) {
            for (int i = 0; i < accountList.size(); i++) {
                String avatar = SessionPrefs.getInstance(this).getAvatar(accountList.get(i));
                String staffName = SessionPrefs.getInstance(this).getStaffName(accountList.get(i));
                LoginAccountBean bean = new LoginAccountBean();
                bean.avatar = avatar;
                bean.account = accountList.get(i);
                bean.staffName = staffName;
                loginList.add(bean);
            }

            for (int i = 0; i < loginList.size(); i++) {
                if (loginList.get(i).account.equals(LoginInfoPrefs.getInstance(mContext).getUserName())) {
                    Collections.swap(loginList, 0, i);
                    break;
                }
            }
        }
    }

    private void login(final String phoneNum, final String pwd) {
        AppModelFactory.model().accessToken(new RequestLogin(phoneNum, pwd), new ProgressSubscriber<DataBean<TokenBean>>(this, false) {
            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean<TokenBean> result) {
                if (null != result) {
                    if (null != result.data) {
//                        EvenManager.sendEvent(new NotifyEvent(NotifyEvent.LOGIN));
                        AppUtils.clearThisUserInfo(mContext);
                        LoginInfoPrefs.getInstance(mContext.getApplicationContext()).saveLoginInfo(phoneNum, pwd);
                        //登录成功后保存tooken在本地
                        LoginInfoPrefs.getInstance(mContext).saveToken(result.data.tokenType, result.data.accessToken);
                        LoginInfoPrefs.getInstance(mContext).saveLoginerType(true);
                        //要先设置tooken
                        ApiRequestInterceptor.getInstance().setToken(result.data.tokenType + result.data.accessToken);
                    }
                }
                //在登录成功后获取
                getAccountInfo(phoneNum, pwd);
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                dismissLoadingDialog();
                ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), ex.message);
            }

        });

    }

    private void getAccountInfo(final String phoneNum, final String pwd) {
        AppModelFactory.model().getAccountInfo(phoneNum, new ProgressSubscriber<DataBean<AccountInfoBean>>(mContext, false) {
            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean<AccountInfoBean> result) {
                if (null != result) {
                    if (null != result.data) {
                        if (null != result.data.staff) {
                            SessionPrefs.getInstance(mContext).saveAvatar(phoneNum, result.data.staff.avatar);
                            SessionPrefs.getInstance(mContext).saveStaffName(phoneNum, result.data.staff.staff_name);
                            //保存账号信息在本地
                            LoginInfoPrefs.getInstance(mContext).saveStaffId(result.data.staff.staff_id);
                            //设置推送别名
                            JPushInterface.resumePush(getApplicationContext());
                            if (null != result.data.staff.staff_id) {
                                String newStr = result.data.staff.staff_id.replace("-", "");
                                LogUtils.e(newStr);
                                JPushInterface.setAlias(mContext, 1, newStr);
                            }
                            //设置推送标签
                            if (null != result.data.staff.group_id) {
                                Set<String> tagSet = new LinkedHashSet<>();
                                tagSet.add("group_id=" + result.data.staff.group_id);
                                JPushInterface.setTags(mContext, 1, tagSet);
                            }
                            LoginInfoPrefs.getInstance(mContext).saveGroupId(result.data.staff.group_id);
                            UserfoPrefs.getInstance(mContext).setOg(result.data.staff.baseon_id, result.data.staff.baseon_type);
                            CrashReport.setUserId(mContext, phoneNum + "/" + pwd);
                            //获取权限
                            getPermission(phoneNum);
                        } else {
                            //没有绑定员工
                        }

                    }
                }
            }
        });

    }

    private void getPermission(String phoneNum) {

        AppModelFactory.model().getPermission(LoginInfoPrefs.getInstance(this).getGroupId(), phoneNum, new ProgressSubscriber<DataBean<List<ResultPermission>>>(mContext, false) {
            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean<List<ResultPermission>> result) {
                //保存权限在本地
                if (null != result) {
                    if (null != result.data) {
                        for (int i = 0; i < result.data.size(); i++) {
                            if (result.data.get(i).name.equals(getString(R.string.permission))) {
                                UserfoPrefs.getInstance(mContext).setPermission(true);
                                break;
                            } else {
                                UserfoPrefs.getInstance(mContext).setPermission(false);
                            }
                        }
                        Constans.isStoreLoginer = true;
                        ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), "切换账号成功");
                        startActivity(new Intent(mContext, MainActivity.class));
                        SwitchAccountActivity.this.finish();
                    }

                }

            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                AppUtils.clearThisUserInfo(mContext);
                ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), getString(R.string.app_permission_tips));
                LogUtils.e(ex.message);
            }

        });

    }

    private void setDelVisiable(boolean isVisiable) {
        if (isVisiable) {
            tvDel.setVisibility(View.GONE);
            tvDel.setText("删除");
            tvLogout.setVisibility(View.VISIBLE);
            mAdapter.setDelVisiable(false);
            titleBar.setRightIcon(R.drawable.ic_del_one);
            titleBar.setTvRight("");
        } else {
            tvDel.setVisibility(View.VISIBLE);
            tvDel.setText("删除");
            tvLogout.setVisibility(View.GONE);
            mAdapter.setDelVisiable(true);
            titleBar.setTvRight("完成");
            titleBar.setRightIcon(0);
        }
    }


}

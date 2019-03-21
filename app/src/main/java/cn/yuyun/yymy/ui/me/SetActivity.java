package cn.yuyun.yymy.ui.me;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.ApiRequestInterceptor;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.jpush.android.api.JPushInterface;
import cn.lzz.utils.VersionUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.ResultVersion;
import cn.yuyun.yymy.main.ForgetPwdActivity;
import cn.yuyun.yymy.main.LoginActivity;
import cn.yuyun.yymy.main.ResetPwdActivity;
import cn.yuyun.yymy.utils.AppUtils;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.utils.up.CheckUpdateManager;
import cn.yuyun.yymy.view.dialog.LogoutDialog;
import cn.yuyun.yymy.view.dialog.VersionDialog;
import cn.yuyun.yymy.web.WebViewActivity;

/**
 * @author
 * @desc 设置Activity
 * @date
 */

public class SetActivity extends BaseActivity{

    private TextView tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EvenManager.register(this);
        setContentView(R.layout.activity_set);
    }

    @Override
    protected void setUpViewAndData() {
        initViews();
    }

    private void initViews() {
        titleBar.setTilte("系统设置");
        tvVersion = (TextView) findViewById(R.id.tv_version);
        tvVersion.setText("V" + VersionUtils.getAppVersionName(this));
        findViewById(R.id.ll_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
        findViewById(R.id.ll_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SetActivity.this, WebViewActivity.class));
            }
        });

        findViewById(R.id.ll_resetPwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ForgetPwdActivity.startResetPwdActivity(mContext));
            }
        });
        findViewById(R.id.ll_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SetActivity.this, SwitchAccountActivity.class));
            }
        });
    }

    private void check() {
        AppModelFactory.model().checkAppVersion(1, VersionUtils.getAppVersionName(mContext), new ProgressSubscriber<DataBean<ResultVersion>>(mContext) {

            @Override
            public void onNext(final DataBean<ResultVersion> result) {
                if(null != result.data){
                    final VersionDialog dialog = new VersionDialog(mContext);
                    dialog.setOnPositiveListener(new VersionDialog.OnPositiveListener() {
                        @Override
                        public void onPositive() {
                            CheckUpdateManager manager = new CheckUpdateManager(mContext);
                            manager.checkUpdate();
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    if (TextUtils.isEmpty(result.data.describes)) {
                        dialog.setTips("新版本V" + result.data.version);
                    } else {
                        dialog.setTips(result.data.describes);
                    }
                }else{
                    showToast("当前App已经是最新版本");
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast("当前App已经是最新版本");
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });
    }

    public void goToMarket(String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            mContext.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if(notifyEvent.tag == NotifyEvent.LOGOUT){
            finish();
        }
    }


}

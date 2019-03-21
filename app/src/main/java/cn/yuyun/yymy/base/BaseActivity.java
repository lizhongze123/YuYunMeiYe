package cn.yuyun.yymy.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.githang.statusbar.StatusBarCompat;
import com.jph.takephoto.app.TakePhotoFragmentActivity;

import butterknife.ButterKnife;
import cn.example.dialog.LoadingDialog;
import cn.lzz.utils.DeviceUtils;
import cn.lzz.utils.ToastUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.app.MyApplication;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.TitleBar;
import cn.yuyun.yymy.view.dialog.TextDialog;

public class BaseActivity extends TakePhotoFragmentActivity {

    protected Context mContext;
    protected View mBaseView;
    /**
     * 布局view
     */
    protected View mContentView;
    /**
     * 内容布局
     */
    protected RelativeLayout mContainer;
    /**
     * 数据为空
     */
    private EmptyLayout mErrorLayout;
    protected TitleBar titleBar;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        //竖屏
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 设置不自动弹出输入框
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        loadingDialog = new LoadingDialog(this);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.white));
    }

    protected void setUpViewAndData() {
        ButterKnife.bind(this);
    }

   /* protected void restartApp() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra(KEY_HOME_ACTION,ACTION_RESTART_APP);
        startActivity(intent);
    }*/

    /**
     * 普通内容，数据为空，错误，等待的显示
     */
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        mBaseView = View.inflate(mContext, R.layout.activity_base, null);
        mContentView = View.inflate(mContext, layoutResID, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContentView.setLayoutParams(params);
        mContainer = (RelativeLayout) mBaseView.findViewById(R.id.container);
        mContainer.addView(mContentView);
        getWindow().setContentView(mBaseView);
        init();
        setUpViewAndData();
       /* switch (AppStatusManager.getInstance().getAppStatus()) {
            case STATUS_FORCE_KILLED:
                restartApp();
                break;
            case STATUS_NORMAL:
                setUpViewAndData();
                break;
            default:
        }*/
    }

    protected void init() {
        titleBar = getView(R.id.titleBar);
        titleBar.setLeftIcon(R.drawable.back_black);
        titleBar.setLineIsVisible(View.VISIBLE);
        titleBar.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mErrorLayout = getView(R.id.error_layout);
        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onEmptyRefresh();
            }
        });
    }

    protected <T extends View> T getView(int id) {
        return (T) findViewById(id);
    }

    /**
     * 数据为空时的显示
     */
    protected void showEmpty() {
        mErrorLayout.setErrorType(EmptyLayout.NODATA);
        if (mContentView.getVisibility() != View.GONE) {
            mContentView.setVisibility(View.GONE);
        }
    }

    protected void showEmpty(int type) {
        mErrorLayout.setErrorType(type);
        if (mContentView.getVisibility() != View.GONE) {
            mContentView.setVisibility(View.GONE);
        }
    }

    protected void showError() {
        mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
        if (mContentView.getVisibility() != View.GONE) {
            mContentView.setVisibility(View.GONE);
        }
    }

    protected void dismissEmpty() {
        mErrorLayout.dismiss();
        mContentView.setVisibility(View.VISIBLE);
    }

    /**
     * 数据为空时的显示
     */
    protected void showContent() {
        mContentView.setVisibility(View.VISIBLE);
    }

    public void onEmptyRefresh() {

    }

    public void showLoadingDialog(final String tips) {
        if (null != loadingDialog && !loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    public void dismissLoadingDialog() {
        if (null != loadingDialog && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
    }

    public void showToast(String tips) {
        ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), tips);
    }

    public void showToastCenter(String tips) {
        ToastUtils.toastInCenter(this, tips);
    }

    private TextDialog textDialog;

    public void showTextDialog(final String text, final boolean isNeedCancel) {
        if (null == textDialog) {
            textDialog = new TextDialog(mContext);
        }
        textDialog.setText(text);
        textDialog.isNeedCancel(isNeedCancel);
        textDialog.show();
    }

    public boolean hasInternet(){
        return DeviceUtils.hasInternet(mContext);
    }

}

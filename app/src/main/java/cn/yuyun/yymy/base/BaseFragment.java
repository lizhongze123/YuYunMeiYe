package cn.yuyun.yymy.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jph.takephoto.app.TakePhotoFragment;

import java.io.Serializable;

import butterknife.ButterKnife;
import cn.lzz.utils.DeviceUtils;
import cn.lzz.utils.ToastUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.app.MyApplication;
import cn.yuyun.yymy.main.LoginActivity;
import cn.yuyun.yymy.main.RegisterActivity;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.TitleBar;
import cn.yuyun.yymy.view.dialog.TextDialog;


/**
 *
 */

@SuppressWarnings("WeakerAccess")
public abstract class BaseFragment extends TakePhotoFragment {

    protected Context mContext;
    protected View mRoot;
    protected Bundle mBundle;
    protected LayoutInflater mInflater;

    /**布局view*/
    protected View mContentView;
    /**内容布局*/
    protected RelativeLayout mContainer;
    /**加载失败*/
    private EmptyLayout mErrorLayout;

    protected TitleBar titleBar;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mContext = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getArguments();
        initBundle(mBundle);
        // 设置不自动弹出输入框
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != mRoot) {
            // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，
            // 要不然会发生这个rootview已经有parent的错误。
            ViewGroup parent = (ViewGroup) mRoot.getParent();
            if (null != parent){
                parent.removeView(mRoot);
            }
        } else {
            mContext = getContext();
            mRoot = inflater.inflate(R.layout.fragment_base, null);
            mContentView = View.inflate(mContext, getLayoutId(), null);
            mInflater = inflater;
            mContainer = (RelativeLayout) mRoot.findViewById(R.id.container);
            mContainer.addView(mContentView);
            // Do something
            onBindViewBefore(mRoot);
            // Bind view
            // Get savedInstanceState
            if (savedInstanceState != null) {
                onRestartInstance(savedInstanceState);
            }
            ButterKnife.bind(this, mRoot);
            // Init
            initViews(mRoot);
            initData();
        }
        return mRoot;
    }

    protected void onBindViewBefore(View root) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mBundle = null;
    }

    protected abstract int getLayoutId();

    protected void initBundle(Bundle bundle) {

    }

    protected void initViews(View root) {
        titleBar = getView(root, R.id.titleBar);
        mErrorLayout = getView(root, R.id.error_layout);
        mErrorLayout.setLoginListener(new EmptyLayout.LoginListener() {
            @Override
            public void onLogin() {
                startActivity(new Intent(mContext, LoginActivity.class));
                ((Activity)mContext).finish();
            }

            @Override
            public void onRegister() {
                startActivity(new Intent(mContext, RegisterActivity.class));
                ((Activity)mContext).finish();
            }
        });
        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onEmptyRefresh();
            }
        });
    }

    public void onEmptyRefresh(){

    }

    protected <T extends View> T getView(View root, int id) {
        return (T) root.findViewById(id);
    }

    protected void initData() {

    }

    protected <T extends View> T findView(int viewId) {
        return (T) mRoot.findViewById(viewId);
    }

    protected <T extends Serializable> T getBundleSerializable(String key) {
        if (mBundle == null) {
            return null;
        }
        return (T) mBundle.getSerializable(key);
    }

    /***
     * 从网络中加载数据
     *
     * @param viewId   view的id
     * @param imageUrl 图片地址
     */
    protected void setImageFromNet(int viewId, String imageUrl) {
        setImageFromNet(viewId, imageUrl, 0);
    }

    /***
     * 从网络中加载数据
     *
     * @param viewId      view的id
     * @param imageUrl    图片地址
     * @param placeholder 图片地址为空时的资源
     */
    protected void setImageFromNet(int viewId, String imageUrl, int placeholder) {
        ImageView imageView = findView(viewId);
//        setImageFromNet(imageView, imageUrl, placeholder);
    }

    /***
     * 从网络中加载数据
     *
     * @param imageView imageView
     * @param imageUrl  图片地址
     */
    protected void setImageFromNet(ImageView imageView, String imageUrl) {
//        setImageFromNet(imageView, imageUrl, 0);
    }


    protected void setText(int viewId, String text) {
        TextView textView = findView(viewId);
        if (TextUtils.isEmpty(text)) {
            return;
        }
        textView.setText(text);
    }

    protected void setText(int viewId, String text, String emptyTip) {
        TextView textView = findView(viewId);
        if (TextUtils.isEmpty(text)) {
            textView.setText(emptyTip);
            return;
        }
        textView.setText(text);
    }

    protected void setTextEmptyGone(int viewId, String text) {
        TextView textView = findView(viewId);
        if (TextUtils.isEmpty(text)) {
            textView.setVisibility(View.GONE);
            return;
        }
        textView.setText(text);
    }

    protected <T extends View> T setGone(int id) {
        T view = findView(id);
        view.setVisibility(View.GONE);
        return view;
    }

    protected <T extends View> T setVisibility(int id) {
        T view = findView(id);
        view.setVisibility(View.VISIBLE);
        return view;
    }

    protected void setInVisibility(int id) {
        findView(id).setVisibility(View.INVISIBLE);
    }

    protected void onRestartInstance(Bundle bundle) {

    }

    public void showToast(String tips) {
        ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), tips);
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

    public void showToastCenter(String tips) {
        ToastUtils.toastInCenter(getActivity(), tips);
    }

    public boolean hasInternet(){
        return DeviceUtils.hasInternet(mContext);
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

    protected void showLogin() {
        mErrorLayout.setErrorType(EmptyLayout.NO_LOGIN);
        if (mContentView.getVisibility() != View.GONE) {
            mContentView.setVisibility(View.GONE);
        }
    }

    public boolean isHasStaffId(){
        if (TextUtils.isEmpty(LoginInfoPrefs.getInstance(mContext).getStaffId())) {
            showTextDialog("该账号没有绑定员工", false);
            return false;
        }else{
            return true;
        }
    }

}

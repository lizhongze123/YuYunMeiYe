package cn.yuyun.yymy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.lzz.utils.DeviceUtils;
import cn.yuyun.yymy.R;


public class EmptyLayout extends LinearLayout{

    /**网络没有连接*/
    public static final int NETWORK_ERROR = 1;
    /**正在加载数据 */
    public static final int NETWORK_LOADING = 2;
    /**没有数据*/
    public static final int NODATA = 3;
    /**加载成功 不显示emptylayout了*/
    public static final int HIDE_LAYOUT = 4;
    /**没有数据时点击重试*/
    public static final int NODATA_ENABLE_CLICK = 5;
    /**没有登录*/
    public static final int NO_LOGIN = 6;
    public static final int NETWORK_ERROR_ENABLE_CLICK = 7;

    private final Context context;
    public ImageView img;
    public TextView tv;
    public TextView tvRefresh;
    private OnClickListener listener;
    private int mErrorState;
    private String strNoDataContent = "";
    private LinearLayout llLogin;
    private boolean mLoadingLocalFriend;

    public EmptyLayout(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public EmptyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_error_layout, this, false);
//      View view = View.inflate(context, R.layout.view_error_layout, null);
        img = (ImageView) view.findViewById(R.id.img_error_layout);
        tv = (TextView) view.findViewById(R.id.text_error_layout);
        tvRefresh = (TextView) view.findViewById(R.id.tv_refresh);
        llLogin = (LinearLayout) view.findViewById(R.id.ll_login);
        view.findViewById(R.id.tv_register).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loginListener.onRegister();
            }
        });
        view.findViewById(R.id.tv_login).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loginListener.onLogin();
            }
        });
        setBackgroundColor(-1);
        tvRefresh.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onClick(v);
                }
            }
        });
        addView(view);
        changeErrorLayoutBgMode(context);
    }

    public void changeErrorLayoutBgMode(Context context1) {
        // mLayout.setBackgroundColor(SkinsUtil.getColor(context1,
        // "bgcolor01"));
        // tv.setTextColor(SkinsUtil.getColor(context1, "textcolor05"));
    }

    public void dismiss() {
        mErrorState = HIDE_LAYOUT;
        setVisibility(View.GONE);
    }

    public int getErrorState() {
        return mErrorState;
    }

    public boolean isLoadError() {
        return mErrorState == NETWORK_ERROR;
    }

    public boolean isLoading() {
        return mErrorState == NETWORK_LOADING;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        onSkinChanged();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // MyApplication.getInstance().getAtSkinObserable().unregistered(this);
    }

    public void onSkinChanged() {
        // mLayout.setBackgroundColor(SkinsUtil
        // .getColor(getContext(), "bgcolor01"));
        // tv.setTextColor(SkinsUtil.getColor(getContext(), "textcolor05"));
    }

    public void setErrorMessage(String msg) {

    }

    /**
     * 新添设置背景
     * @param imgResource 图片的id
     * @param msg  图片下面的textView显示的文字
     *
     */
    public void setErrorImag(int imgResource, String msg) {
        try {

        } catch (Exception e) {

        }
    }

    public void setErrorType(int i) {
        setVisibility(View.VISIBLE);
        switch (i) {
            case NETWORK_ERROR:
                mErrorState = NETWORK_ERROR;
                if (DeviceUtils.hasInternet(context)) {
                    tv.setText(context.getString(R.string.error_data_null));
                    img.setBackgroundResource(R.drawable.data_null);
                } else {
                    tv.setText(context.getString(R.string.error_no_network));
                    img.setBackgroundResource(R.drawable.no_network);
                }
                img.setVisibility(VISIBLE);
                tv.setVisibility(VISIBLE);
                tvRefresh.setVisibility(VISIBLE);
                break;
            case NODATA:
                mErrorState = NODATA;
                img.setBackgroundResource(R.drawable.data_null);
                tv.setText(context.getString(R.string.error_data_null));
                img.setVisibility(VISIBLE);
                tv.setVisibility(VISIBLE);
                tvRefresh.setVisibility(VISIBLE);
                break;
            case NO_LOGIN:
                mErrorState = NO_LOGIN;
                img.setVisibility(View.GONE);
                llLogin.setVisibility(VISIBLE);
                break;
            case HIDE_LAYOUT:
                setVisibility(View.GONE);
                break;
            case NODATA_ENABLE_CLICK:
                mErrorState = NODATA_ENABLE_CLICK;
                if (DeviceUtils.hasInternet(context)) {
                    tv.setText(context.getString(R.string.error_data_null));
                    img.setBackgroundResource(R.drawable.data_null);
                } else {
                    tv.setText(context.getString(R.string.error_no_network));
                    img.setBackgroundResource(R.drawable.no_network);
                }
                img.setVisibility(VISIBLE);
                tv.setVisibility(VISIBLE);
                tvRefresh.setVisibility(GONE);
                break;
            default:
                break;
        }
    }

    public void setNoDataContent(String noDataContent) {
        strNoDataContent = noDataContent;
    }

    public void setOnLayoutClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public void setTvNoDataContent() {

    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == View.GONE){
            mErrorState = HIDE_LAYOUT;
        }
        super.setVisibility(visibility);
    }

    LoginListener loginListener;

    public interface LoginListener{
        void onLogin();
        void onRegister();
    }

    public void setLoginListener(LoginListener listener) {
        this.loginListener = listener;
    }
}

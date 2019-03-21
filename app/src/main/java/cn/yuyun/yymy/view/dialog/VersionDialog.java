package cn.yuyun.yymy.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import cn.yuyun.yymy.R;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/1/31
 */

public class VersionDialog extends Dialog {

    private Context mContext;

    private ImageView tv_cancel;
    private TextView tv_positive;
    private TextView tv_tips;
    private TextView tv_code;

    public VersionDialog(Context context) {
        super(context, R.style.tip_dialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.popup_window_activate_hint);
        initView();
        init();
    }

    private void initView() {
        tv_tips = (TextView) findViewById(R.id.updataversion_msg);
        tv_code = (TextView) findViewById(R.id.updataversioncode);
        tv_positive = (TextView) findViewById(R.id.ah_activate_btn);
        tv_positive.setText("立即升级");
        tv_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPositive();
            }
        });
        setTips("发现新版本");
    }

    private void init() {
        Window window = getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置为居中
        window.setWindowAnimations(R.style.bottom_menu_animation); // 添加动画效果
        // 宽度全屏
        WindowManager windowManager = ((Activity) mContext).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth() * 4 / 5; // 设置dialog宽度为屏幕的4/5
        getWindow().setAttributes(lp);
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
    }

    private OnPositiveListener mListener;

    public interface OnPositiveListener {
        void onPositive();
    }

    public void setOnPositiveListener(OnPositiveListener listener) {
        this.mListener = listener;
    }


    public void setTips(String tips){
        tv_tips.setText(tips);
        tv_tips.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    public void setCode(String code){
        tv_code.setText(code);
    }

}

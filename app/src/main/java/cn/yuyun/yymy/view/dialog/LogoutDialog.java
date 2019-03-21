package cn.yuyun.yymy.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import cn.yuyun.yymy.R;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/1/31
 */

public class LogoutDialog extends Dialog {

    private Context mContext;

    private TextView tv_cancel;
    private TextView tv_positive;

    public LogoutDialog(Context context) {
        super(context, R.style.tip_dialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.dialog_tips);
        initView();
        init();
    }

    private void initView() {
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_positive = (TextView) findViewById(R.id.tv_positive);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPositive();
                dismiss();
            }
        });
    }

    private void init() {
        Window window = this.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        WindowManager.LayoutParams p = window.getAttributes();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        window.setAttributes(p);
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(true);
    }

    private OnPositiveListener mListener;

    public interface OnPositiveListener {
        void onPositive();
    }

    public void setOnPositiveListener(OnPositiveListener listener) {
        this.mListener = listener;
    }


}

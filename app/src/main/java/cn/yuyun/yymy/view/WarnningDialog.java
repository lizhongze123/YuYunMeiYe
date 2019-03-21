package cn.yuyun.yymy.view;

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
 * @author
 * @desc
 * @date
 */

public class WarnningDialog extends Dialog {

    private Context mContext;

    private TextView tv_cancel;
    private TextView tv_positive;
    private TextView tv_tips;
    private String tips;

    public WarnningDialog(Context context, String tips) {
        super(context, R.style.tip_dialog);
        this.mContext = context;
        this.tips = tips;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.dialog_tips);
        initView();
        init();
    }

    private void initView() {
        tv_tips = (TextView) findViewById(R.id.tv_tips);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_positive = (TextView) findViewById(R.id.tv_positive);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onNegative();
                dismiss();
            }
        });
        tv_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPositive();
            }
        });
        setTips(tips);
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

    private OnDialogListener mListener;

    public interface OnDialogListener {
        void onPositive();
        void onNegative();
    }

    public void setOnPositiveListener(OnDialogListener listener) {
        this.mListener = listener;
    }

    public void setTips(String tips){
        tv_tips.setText(tips);
    }

}

package cn.yuyun.yymy.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import cn.yuyun.yymy.R;

/**
 * @author
 * @desc
 * @date
 */

public class CashierFinishDialog extends Dialog {

    private Context mContext;

    private TextView tv_positive;
    private TextView tv_negative;
    private TextView tv_desc;

    private CheckBox cbWechat, cbSms;

    public CashierFinishDialog(Context context) {
        super(context, R.style.tip_dialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.dialog_cashier);
        initView();
        init();
    }

    private void initView() {
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        tv_positive = (TextView) findViewById(R.id.tv_positive);
        tv_negative = (TextView) findViewById(R.id.tv_negative);
        cbWechat = (CheckBox) findViewById(R.id.cb_wechat);
        cbSms = (CheckBox) findViewById(R.id.cb_sms);
        tv_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbWechat.isChecked()){
                    mListener.onPositive(2);
                    dismiss();
                }else if(cbSms.isChecked()){
                    mListener.onPositive(1);
                    dismiss();
                }else if(cbWechat.isChecked() && cbSms.isChecked()){
                    mListener.onPositive(3);
                    dismiss();
                }
            }
        });
        tv_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onNegative();
                dismiss();
            }
        });
    }

    private void init() {
        Window window = getWindow();
        // 此处可以设置dialog显示的位置为居中
        window.setGravity(Gravity.CENTER);
        // 宽度全屏
        WindowManager windowManager = ((Activity) mContext).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        // 设置dialog宽度为屏幕的4/5
        lp.width = display.getWidth() * 7 / 10;
        getWindow().setAttributes(lp);
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
    }

    private OnPositiveListener mListener;

    public interface OnPositiveListener {
        void onPositive(int type);
        void onNegative();
    }

    public void setOnPositiveListener(OnPositiveListener listener) {
        this.mListener = listener;
    }


    public void setTips(String tips){
        tv_desc.setText(tips);
    }

}

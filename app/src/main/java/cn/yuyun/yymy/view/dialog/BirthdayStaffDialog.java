package cn.yuyun.yymy.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import cn.yuyun.yymy.R;

/**
 * @author
 * @desc
 * @date
 */

public class BirthdayStaffDialog extends Dialog {

    private Context mContext;

    private TextView tvPositive;
    private TextView tvTips;

    public BirthdayStaffDialog(Context context) {
        super(context, R.style.tip_dialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.dialog_staff_birthday);
        initView();
        init();
    }

    private void initView() {
        tvTips = (TextView) findViewById(R.id.tv_tips);
        tvPositive = (TextView) findViewById(R.id.tv_positive);
        tvPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPositive();
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
        lp.width = display.getWidth() * 2 / 3;
        getWindow().setAttributes(lp);
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

    public void setTips(String str){
        tvTips.setText(str);
    }

    public void setTips(SpannableString str){
        tvTips.setText(str);
    }

}

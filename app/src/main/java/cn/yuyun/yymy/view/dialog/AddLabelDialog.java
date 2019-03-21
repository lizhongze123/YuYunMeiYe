package cn.yuyun.yymy.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.yuyun.yymy.R;

/**
 * @author
 * @desc
 * @date
 */

public class AddLabelDialog extends Dialog {

    private Context mContext;

    private TextView tv_cancel;
    private TextView tv_positive;
    private EditText et_content;

    public AddLabelDialog(Context context) {
        super(context, R.style.tip_dialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.popup_window_add_label);
        initView();
        init();
    }

    private void initView() {
        et_content = (EditText) findViewById(R.id.et_content);
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
                mListener.onPositive(et_content.getText().toString().trim());
            }
        });
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
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(true);
    }

    private OnPositiveListener mListener;

    public interface OnPositiveListener {
        void onPositive(String content);
    }

    public void setOnPositiveListener(OnPositiveListener listener) {
        this.mListener = listener;
    }


}

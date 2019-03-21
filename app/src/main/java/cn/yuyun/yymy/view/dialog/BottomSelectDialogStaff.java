package cn.yuyun.yymy.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.LayoutRes;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import cn.yuyun.yymy.R;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/5/25
 */
public class BottomSelectDialogStaff extends Dialog implements View.OnClickListener{

    private Activity context;
    public static int ONE = 1;
    public static int TWO = 2;
    public static int CANCEL = 0;

    private TextView tvOne, tvTwo, tvCancel;

    public BottomSelectDialogStaff(Activity context) {
        super(context, R.style.select_MyDialogStyle);
        setContentView(R.layout.dialog_bottom_select_staff);
        this.context = context;
        init();
        initView();
    }

    public BottomSelectDialogStaff(Activity context, @LayoutRes int layout) {
        super(context, R.style.select_MyDialogStyle);
        setContentView(layout);
        this.context = context;
        init();
        initView();
    }

    private void initView() {
        tvOne = (TextView) findViewById(R.id.tv_one);
        tvTwo = (TextView) findViewById(R.id.tv_two);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvOne.setOnClickListener(this);
        tvTwo.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    private void init() {
        // 设置位置
        WindowManager manager = this.getWindow().getWindowManager();
        int width = manager.getDefaultDisplay().getWidth();
        Window window = this.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = width;
        // 弹出动画
        window.setWindowAnimations(R.style.select_anim_dialog);
        // 设置取消方式
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(true);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_one) {
            listener.oClick(ONE);
        } else if (i == R.id.tv_two) {
            listener.oClick(TWO);
        }else if (i == R.id.tv_cancel) {
            listener.oClick(CANCEL);
        }
    }

    public ItemClickListener listener;

    public interface ItemClickListener {
        void oClick(int item);
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public void showDialog(String strOne, String StrTwo) {
        tvOne.setText(strOne);
        tvTwo.setText(StrTwo);
        show();
    }
}

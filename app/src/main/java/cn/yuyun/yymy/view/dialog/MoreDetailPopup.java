package cn.yuyun.yymy.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import cn.lzz.utils.LogUtils;
import cn.yuyun.yymy.R;

/**
 * @author
 * @desc
 * @date
 */
public class MoreDetailPopup extends PopupWindow{

    private Context context;
    private int RESOURCE_ID;

    public MoreDetailPopup(Context context, @LayoutRes int layout) {
        super(context);
        this.context = context;
        this.RESOURCE_ID = layout;
        initalize();
    }

    private void initalize() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(RESOURCE_ID, null);
        view.findViewById(R.id.ll_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPositive(0);
                dismiss();
            }
        });
        view.findViewById(R.id.ll_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPositive(1);
                dismiss();
            }
        });
        setContentView(view);
        initWindow();
    }

    private void initWindow() {
        DisplayMetrics d = context.getResources().getDisplayMetrics();
//        this.setWidth((int) (d.widthPixels * 0.35));
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //0.0-1.0
        backgroundAlpha((Activity) context, 0.8f);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha((Activity) context, 1f);
            }
        });

    }

    /**设置添加屏幕的背景透明度*/
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    public void showAtBottom(View view) {
        //弹窗位置设置
//        showAsDropDown(view,  view.getWidth() -  , 0);
        showAsDropDown(view, view.getWidth(), -50);
        //showAtLocation(view, Gravity.TOP | Gravity.RIGHT, 10, 110);//有偏差
    }

    private OnPopupClickListener mListener;

    public interface OnPopupClickListener {
        void onPositive(int pos);
    }

    public MoreDetailPopup setOnPopupClickListener(OnPopupClickListener listener) {
        this.mListener = listener;
        return this;
    }

}
package cn.yuyun.yymy.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import cn.yuyun.yymy.R;


public class OnBindDialog {

    private Dialog mDialog;
    private TextView tv_cancel;
    private TextView tv_positive;
    private TextView tv_tips;

    public OnBindDialog(Context context) {

        mDialog = new Dialog(context, R.style.tip_dialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_tips, null);

        tv_tips = (TextView) view.findViewById(R.id.tv_tips);
        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_positive = (TextView) view.findViewById(R.id.tv_positive);

        tv_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if (mListener != null){
                    mListener.onPositive();
                }
            }
        });
        mDialog.setCancelable(true);
        mDialog.setContentView(view);
    }

    public void setPositiveText(String text) {
        tv_positive.setText(text);
    }

    public void setTips(String tips) {
        tv_tips.setText(tips);
    }

    public void show() {
        mDialog.show();
    }

    public void cancel() {
        mDialog.cancel();
    }

    public boolean isShowing() {
        return mDialog.isShowing();
    }

    private OnDialogListener mListener;

    public interface OnDialogListener {
        void onPositive();
        void onNegative();
    }

    public void setOnPositiveListener(OnDialogListener listener) {
        this.mListener = listener;
    }

}


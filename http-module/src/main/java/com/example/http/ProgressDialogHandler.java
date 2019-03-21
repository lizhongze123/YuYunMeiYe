package com.example.http;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import cn.example.dialog.LoadingDialog;
import cn.example.dialog.TextDialog;

class ProgressDialogHandler extends Handler {

    static final int SHOW_PROGRESS_DIALOG = 1;
    static final int DISMISS_PROGRESS_DIALOG = 2;
    static final int SHOW_TIPS_DIALOG = 3;

    private LoadingDialog progressDialog;
    private TextDialog textDialog;

    private Context context;
    private boolean cancelable;
    private String loadHint;
    private ProgressCancelListener mProgressCancelListener;

    ProgressDialogHandler(Context context, ProgressCancelListener mProgressCancelListener,
                          boolean cancelable) {

        this(context, "", mProgressCancelListener, cancelable);
    }

    public ProgressDialogHandler(Context context, String loadHint, ProgressCancelListener mProgressCancelListener,
                                 boolean cancelable) {
        super();
        this.context = context;
        this.mProgressCancelListener = mProgressCancelListener;
        this.loadHint = loadHint;
        this.cancelable = cancelable;
    }

    private void initProgressDialog(){
        if (progressDialog == null) {
            progressDialog = new LoadingDialog(context);
            progressDialog.setCancelable(cancelable);
            if (!TextUtils.isEmpty(loadHint)) {
                progressDialog.setLoadingText(loadHint);
            }else{
                progressDialog.setLoadingText("加载中");
            }

            if (cancelable) {
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        mProgressCancelListener.onCancelProgress();
                    }
                });
            }

            if (!progressDialog.isShowing()) {
                if(!((Activity)context).isFinishing()){
                    progressDialog.show();
                }
            }
        }
    }

    private void dismissProgressDialog(){
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void initTipsDialog(){
        if (textDialog == null) {
            textDialog = new TextDialog(context);
            textDialog.setText("当前网络不可用");
            textDialog.isNeedCancel(false);
            textDialog.show();
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                initProgressDialog();
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
            case SHOW_TIPS_DIALOG:
                initTipsDialog();
                break;
                default:
        }
    }
}

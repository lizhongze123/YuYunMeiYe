package com.example.http;

import android.content.Context;

/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 */
public abstract class ProgressSubscriber<T> extends ErrorSubscriber<T> implements ProgressCancelListener{

    private ProgressDialogHandler mProgressDialogHandler;
    private Context context;

    public ProgressSubscriber(Context context) {
        this.context = context;
        this.mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
    }

    public ProgressSubscriber(Context context, String loadHint) {
        this.context = context;
        this.mProgressDialogHandler = new ProgressDialogHandler(context, loadHint, this, true);
    }

    public ProgressSubscriber(Context context, boolean cancelable) {
        this.context = context;
        this.mProgressDialogHandler = new ProgressDialogHandler(context, this, cancelable);
    }

    private void showProgressDialog(){
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void showTipsDialog(){
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_TIPS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog(){
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
//            mProgressDialogHandler = null;
        }
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        showProgressDialog();
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onCompleted() {
        dismissProgressDialog();
    }


    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     * @param ex
     */
    @Override
    public void onError(ApiException ex) {
        if (!HttpUtils.hasInternet(context)) {
            onNoNetWorkError();
        }else{
            if (ex.code == ServerResultError.TOKEN_INVALID) {
                //token过期，此app重新登录获取token
                onRenewal(ex);
            }else{
                onNormalError(ex);
            }
        }
        onCompleted();
    }

    public void onRenewal(ApiException ex){

    }

    public void onNoNetWorkError(){

    }

    public void onNormalError(ApiException ex){

    }


    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}

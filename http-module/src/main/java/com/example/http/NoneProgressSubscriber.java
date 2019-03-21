package com.example.http;

import android.content.Context;
import android.text.TextUtils;

import com.example.LoginInfoPrefs;

public abstract class NoneProgressSubscriber<T> extends ErrorSubscriber<T> {

    private Context context;
    private ProgressDialogHandler mProgressDialogHandler;

    public NoneProgressSubscriber(Context context) {
        this.context = context;
        this.mProgressDialogHandler = new ProgressDialogHandler(context, null, true);
    }

    @Override
    protected void onError(ApiException ex) {
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

    public void onNoNetWorkError(){

    }

    public void onNormalError(ApiException ex){

    }

    public void onRenewal(ApiException ex){

    }

    private void showTipsDialog(){
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_TIPS_DIALOG).sendToTarget();
        }
    }
}

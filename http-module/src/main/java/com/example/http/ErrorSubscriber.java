package com.example.http;

import rx.Subscriber;


public abstract class ErrorSubscriber<T> extends Subscriber<T> {
    @Override
    public void onError(Throwable e) {

        if(e instanceof ApiException) {
            onError((ApiException) e);
        } else {
            onError(new ApiException(e, ExceptionMgr.ERROR.UNKNOWN));
        }
    }
    protected abstract void onError(ApiException ex);
}

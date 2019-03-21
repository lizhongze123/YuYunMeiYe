package com.example.http;

import rx.Observable;
import rx.functions.Func1;

/**
 * 拦截onError事件的拦截器
 */
public class ApiOnErrorFunc<T> implements Func1<Throwable, Observable<T>> {

    @Override
    public Observable<T> call(Throwable throwable) {
        return Observable.error(ExceptionMgr.handleException(throwable));
    }
}

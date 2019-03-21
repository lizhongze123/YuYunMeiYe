package com.example.http;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author lzz
 * @desc
 * @date 2018/1/12
 */

public class ApiModel<T> {

    private T apiModel;

    protected ApiModel(boolean isDebug, String baseUrl, Class<T>clazz){
        Retrofit retrofit = initRetrofit(baseUrl);
        apiModel = retrofit.create(clazz);
    }

    protected T api(){
        return apiModel;
    }

    private Retrofit initRetrofit(String baseUrl) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(ApiRequestInterceptor.getInstance());
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        builder.addInterceptor(logging);
        builder.connectTimeout(10, TimeUnit.SECONDS);

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(CustomGsonConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(builder.build())
                .build();
    }

    protected <T> void toSubscribe(Observable<T> observable, Subscriber<T> subscriber) {
        // ObserveOn()用于观察者设定观察者运行的线程,subscribeOn()是用于被观察者运行的线程
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

}

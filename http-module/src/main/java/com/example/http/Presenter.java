package com.example.http;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;


public class Presenter<T> {
    private static final int PAGE_SIZE = 20;
    private int pageIndex = 0;
    private int totalPage;
    private boolean isRefresh;
    private OnPresenterLoadListener<T> onPresenterLoadListener;
    private class PresenterSubscriber extends Subscriber<PageInfo<T>> {

        @Override
        public void onCompleted() {
            onPresenterLoadListener.onCompleted(isRefresh);
//            if (pageIndex > totalPage) {
//                onPresenterLoadListener.onNoneMoreData();
//            }
            pageIndex = pageIndex + 1;
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            onPresenterLoadListener.onFailed(isRefresh);
        }

        @Override
        public void onNext(PageInfo<T> pageInfo) {
            if(null != pageInfo){
                totalPage = pageInfo.pages;
                List<T> list = new ArrayList<>();
                if (pageInfo.dataLsit != null) {
                    list = pageInfo.dataLsit;
                    if(pageInfo.dataLsit.size() == 0){
                        onPresenterLoadListener.onEmptyData();
                    }else{
                        onPresenterLoadListener.onSuccess(list, pageInfo.total, isRefresh);
                    }
                }else{
                    onPresenterLoadListener.onEmptyData();
                }
            }else{
                onPresenterLoadListener.onEmptyData();
            }
        }
    }

    public void setLoadDataListener(OnPresenterLoadListener<T> onPresenterLoadListener) {
        this.onPresenterLoadListener = onPresenterLoadListener;
    }

    public void loadData(final boolean isRefresh) {
        this.isRefresh = isRefresh;
        if (isRefresh) {
            pageIndex = 1;
        } else {
            if (pageIndex > totalPage) {
                onPresenterLoadListener.onCompleted(false);
                onPresenterLoadListener.onNoneMoreData();
                return;
            }
        }
        onPresenterLoadListener.onLoad(new PresenterSubscriber(), pageIndex, PAGE_SIZE);
    }

    public static interface OnPresenterLoadListener<T> {
        void onSuccess(List<T> data, int total, boolean isRefresh);
        void onFailed(boolean isRefresh);

        void onCompleted(boolean isRefresh);

        /**没有更多数据*/
        void onNoneMoreData();

        /**数据为0*/
        void onEmptyData();

        void onLoad(Subscriber<PageInfo<T>> subscriber, int pageIndex, int pageSize);
    }
}

package cn.yuyun.yymy.ui.home.work;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/2/2
 */

public class SelectPeopleView extends RecyclerView {

    private Context mContext;
    private SelectPeopleAdapter mAdapter;
    GridLayoutManager mLayoutManager;

    public SelectPeopleView(Context context) {
        this(context, null);
    }

    public SelectPeopleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectPeopleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        if(mAdapter == null){
            mAdapter = new SelectPeopleAdapter(context);
        }
        mLayoutManager = new GridLayoutManager(mContext, 5){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        this.setLayoutManager(mLayoutManager);

    }

    public void addAll(List<SelectBean> dataListList) {
        mAdapter.addAll(dataListList);
    }

    public void notifyDataSetChanged(List<SelectBean> dataListList) {
        mAdapter.clear();
        mAdapter.addAll(dataListList);
    }

    public void setAdapter(SelectPeopleAdapter.OnClickCallBack callBack) {
        this.setAdapter(mAdapter);
        mAdapter.setOnSuccessCallBack(callBack);
    }

    public void setMax(int size){
        mAdapter.setMax(size);
    }

    public void setOff(boolean isOff){
        mAdapter.setOff(isOff);
    }
}

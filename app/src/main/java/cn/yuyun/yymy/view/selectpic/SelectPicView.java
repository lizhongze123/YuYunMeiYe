package cn.yuyun.yymy.view.selectpic;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.util.List;

import cn.yuyun.yymy.ui.home.work.SelectBean;
import cn.yuyun.yymy.ui.home.work.SelectPeopleAdapter;

/**
 * @author lzz
 * @desc lzz
 * @date
 */

public class SelectPicView extends RecyclerView {

    private Context mContext;
    private SelectPicAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    public SelectPicView(Context context) {
        this(context, null);
    }

    public SelectPicView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectPicView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        if(mAdapter == null){
            mAdapter = new SelectPicAdapter(context);
        }
        mLayoutManager = new GridLayoutManager(mContext, 3){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        this.setLayoutManager(mLayoutManager);

    }

    public void addAll(List<String> dataListList) {
        mAdapter.addAll(dataListList);
    }

    public void notifyDataSetChanged(List<String> dataListList) {
        mAdapter.clear();
        mAdapter.addAll(dataListList);
    }

    public void setAdapter(SelectPicAdapter.OnClickCallBack callBack) {
        this.setAdapter(mAdapter);
        mAdapter.setOnSuccessCallBack(callBack);
    }

    public void setMax(int size){
        mAdapter.setMax(size);
    }

    public void editable(boolean isEdit){
        mAdapter.editable(isEdit);
    }

    public void isDel(boolean isDel){
        mAdapter.isDel(isDel);
    }
}

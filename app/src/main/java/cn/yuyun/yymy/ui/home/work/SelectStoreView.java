package cn.yuyun.yymy.ui.home.work;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean.OgServiceplacesRspListBean;

import java.util.List;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/2/2
 */

public class SelectStoreView extends RecyclerView {

    private Context mContext;
    private SelectStoreAdapter mAdapter;
    GridLayoutManager mLayoutManager;

    public SelectStoreView(Context context) {
        this(context, null);
    }

    public SelectStoreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectStoreView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        if(mAdapter == null){
            mAdapter = new SelectStoreAdapter(context);
        }
        mLayoutManager = new GridLayoutManager(mContext, 5){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        this.setLayoutManager(mLayoutManager);

    }

    public void addAll(List<OgServiceplacesRspListBean> dataListList) {
        mAdapter.addAll(dataListList);
    }

    public void notifyDataSetChanged(List<OgServiceplacesRspListBean> dataListList) {
        mAdapter.clear();
        mAdapter.addAll(dataListList);
    }

    public void setAdapter(SelectStoreAdapter.OnClickCallBack callBack) {
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

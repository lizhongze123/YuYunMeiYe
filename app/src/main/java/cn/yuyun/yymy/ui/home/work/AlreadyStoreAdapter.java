package cn.yuyun.yymy.ui.home.work;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;
import cn.yuyun.yymy.http.result.ResultListStaff;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author
 * @desc
 * @date
 */
public class AlreadyStoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_already_store;
    private Context mContext;
    private List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> dataList = new ArrayList<>();

    public AlreadyStoreAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(RESOURCE_ID, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.bindItem(dataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void notifyDataSetChanged(List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivAvatar;

        ViewHolder(View view) {
            super(view);
            ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
        }

        private void bindItem(final ResultClassfiyStoreBean.OgServiceplacesRspListBean bean, final int position) {
            if(!TextUtils.isEmpty(bean.thumb_url)){
                if(bean.thumb_url.startsWith(mContext.getString(R.string.HTTP))){
                    GlideHelper.displayImage(mContext, bean.thumb_url, ivAvatar, R.drawable.default_store);
                }else{
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.thumb_url, ivAvatar, R.drawable.default_store);
                }
            }else{
                GlideHelper.displayImage(mContext, R.drawable.default_store, ivAvatar);
            }
        }
    }


}
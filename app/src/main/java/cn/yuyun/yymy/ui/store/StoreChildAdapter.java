package cn.yuyun.yymy.ui.store;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author
 * @desc
 * @date
 */
public class StoreChildAdapter extends RecyclerView.Adapter<StoreChildAdapter.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_store2;
    private Context mContext;
    private List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public StoreChildAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public StoreChildAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(RESOURCE_ID, parent, false);
        return new ViewHolder(rootView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindItem(dataList.get(position), position);
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

        public TextView tvName;
        public TextView tvStoreName;
        public TextView tvAddress;
        public ImageView ivAvatar;
        RelativeLayout rl;

        ViewHolder(View v, OnMyItemClickListener onItemClickListener) {
            super(v);
            tvName = (TextView) v.findViewById(R.id.tv_name);
            tvStoreName = (TextView) v.findViewById(R.id.tv_storeName);
            tvAddress = (TextView) v.findViewById(R.id.tv_address);
            ivAvatar = (ImageView) v.findViewById(R.id.iv_avatar);
            rl = (RelativeLayout) v.findViewById(R.id.rl);
        }

        private void bindItem(final ResultClassfiyStoreBean.OgServiceplacesRspListBean bean, final int position) {
            TextViewUtils.setTextOrEmpty(tvName, bean.chairman, "无");
            TextViewUtils.setTextOrEmpty(tvStoreName, bean.sp_name);
            TextViewUtils.setTextOrEmpty(tvAddress, bean.addr, "未填写门店地址");
            if(!TextUtils.isEmpty(bean.thumb_url)){
                if(bean.thumb_url.startsWith(mContext.getString(R.string.HTTP))){
                    GlideHelper.displayImage(mContext, bean.thumb_url, ivAvatar, R.drawable.default_store);
                }else{
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.thumb_url, ivAvatar, R.drawable.default_store);
                }
            }else{
                GlideHelper.displayImage(mContext, R.drawable.default_store, ivAvatar);
            }

            ivAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(bean, position);
                }
            });

            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != onItemClickListener){
                        onItemClickListener.onItemClick(bean, position);
                    }
                }
            });
        }
    }


    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(ResultClassfiyStoreBean.OgServiceplacesRspListBean bean, int position);
    }

}

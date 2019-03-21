package cn.yuyun.yymy.ui.home.leave;

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

import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.bean.RecordType;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.me.entity.MemberBean;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author lzz
 * @desc
 * @date 2018-01-17
 */
public class StoreListAdapter extends RecyclerView.Adapter<StoreListAdapter.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_store;
    private Context mContext;
    private List<StoreBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public StoreListAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

    public void notifyDataSetChanged(List<StoreBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<StoreBean> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private StoreListAdapter.OnMyItemClickListener onItemClickListener;
        private RelativeLayout rl;
        ImageView ivAvatar;
        TextView tvStoreName, tvName;
        int override;

        ViewHolder(View view,  StoreListAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
            tvStoreName = (TextView) view.findViewById(R.id.tv_storeName);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            rl = (RelativeLayout) view.findViewById(R.id.rl);
            this.onItemClickListener = onItemClickListener;
            override = ResoureUtils.getDimension(mContext, R.dimen.item_avatar_size);
        }

        private void bindItem(final StoreBean bean, final int position) {
            if(!TextUtils.isEmpty(bean.thumb_url)){
                if(bean.thumb_url.startsWith("http")){
                    GlideHelper.displayOverrideImage(mContext, bean.thumb_url, override, override,ivAvatar, R.drawable.default_store);
                }else{
                    GlideHelper.displayOverrideImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.thumb_url, R.drawable.default_store, override, override,ivAvatar);
                }
            }else{
                GlideHelper.displayOverrideImage(mContext, R.drawable.default_store, override, override,ivAvatar);
            }
            TextViewUtils.setTextOrEmpty(tvStoreName, bean.spName);
            TextViewUtils.setTextOrEmpty(tvName, bean.chairman, "无");
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, bean, position);
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, StoreBean bean, int position);
    }

}
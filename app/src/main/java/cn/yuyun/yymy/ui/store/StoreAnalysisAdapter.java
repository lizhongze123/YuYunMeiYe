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

import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.bean.Sex;
import cn.yuyun.yymy.http.result.ResultStoreAnalysis;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author
 * @desc
 * @date
 */
public class StoreAnalysisAdapter extends RecyclerView.Adapter<StoreAnalysisAdapter.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_store_analysis;
    private Context mContext;
    private List<ResultStoreAnalysis> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;
    int type;

    public StoreAnalysisAdapter(Context context,int type){
        this.mContext = context;
        this.type = type;
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

    public void notifyDataSetChanged(List<ResultStoreAnalysis> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultStoreAnalysis> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private StoreAnalysisAdapter.OnMyItemClickListener onItemClickListener;
        private RelativeLayout rl;
        TextView tvName, tv_cardNum, tvStatus, tvLevel;
        ImageView ivAvatar;
        int override;

        ViewHolder(View view,  StoreAnalysisAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tv_cardNum = (TextView) view.findViewById(R.id.tv_cardNum);
            tvStatus = (TextView) view.findViewById(R.id.tv_status);
            tvLevel = (TextView) view.findViewById(R.id.tv_vip);
            rl = (RelativeLayout) view.findViewById(R.id.rl);
            ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
            this.onItemClickListener = onItemClickListener;
            override = ResoureUtils.getDimension(mContext,R.dimen.item_avatar_size);
        }

        private void bindItem(final ResultStoreAnalysis bean, final int position) {
            TextViewUtils.setTextOrEmpty(tvName, bean.member_name);
            TextViewUtils.setTextOrEmpty(tv_cardNum, bean.member_mobile);
            if(type == 0){
                TextViewUtils.setTextOrEmpty(tvStatus, "¥" +StringFormatUtils.formatTwoDecimal(bean.storedvalue));
            }else if(type == 1){
                TextViewUtils.setTextOrEmpty(tvStatus, "¥" + StringFormatUtils.formatTwoDecimal(bean.arrears));
            }else{
                TextViewUtils.setTextOrEmpty(tvStatus, "¥" + StringFormatUtils.formatTwoDecimal(bean.canbe_consume));
            }
            if (!TextUtils.isEmpty(bean.member_avatar)) {
                if (bean.member_avatar.startsWith(mContext.getString(R.string.HTTP))) {
                    GlideHelper.displayImage(mContext, bean.member_avatar, ivAvatar);
                } else {
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.member_avatar, ivAvatar);
                }
            } else {
                if (bean.member_sex.equals(Sex.FEMALE)) {
                    GlideHelper.displayImage(mContext, R.drawable.avatar_default_female, ivAvatar);
                } else {
                    GlideHelper.displayImage(mContext, R.drawable.avatar_default_male, ivAvatar);
                }
            }
            if(TextUtils.isEmpty(bean.member_card_name)){
                tvLevel.setVisibility(View.GONE);
            }else{
                tvLevel.setVisibility(View.VISIBLE);
                tvLevel.setText("(" + bean.member_card_name + ")");
            }
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, ResultStoreAnalysis bean, int position);
    }

}
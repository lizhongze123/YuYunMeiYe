package cn.yuyun.yymy.ui.home.member;

import android.content.Context;
import android.graphics.Color;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.bean.DeadLineStatus;
import cn.yuyun.yymy.bean.Expired;
import cn.yuyun.yymy.http.result.ResultPackageAsset;
import cn.yuyun.yymy.http.result.ResultProduct;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.RoundAngleImageView;

/**
 * @author
 * @desc
 * @date
 */
public class AssetPacketAdapter extends RecyclerView.Adapter<AssetPacketAdapter.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_asset;
    private Context mContext;
    private List<ResultPackageAsset> ingList = new ArrayList<>();
    private List<ResultPackageAsset> finishList = new ArrayList<>();
    private List<ResultPackageAsset> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;
    private boolean isCashier;

    public AssetPacketAdapter(Context context){
        this.mContext = context;
    }

    public AssetPacketAdapter(Context context, boolean isCashier){
        this.mContext = context;
        this.isCashier = isCashier;
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

    public void notifyDataSetChanged(List<ResultPackageAsset> dataList , int type) {
        this.dataList.clear();
        if(type == 0){
            this.dataList.addAll(ingList);
        }else{
            this.dataList.addAll(finishList);
        }
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void setType(int type){
        notifyDataSetChanged(this.dataList, type);
    }

    public void addAll(List<ResultPackageAsset> dataList) {
        for (ResultPackageAsset bean : dataList) {
            if(bean.status == 1 && bean.expired == DeadLineStatus.ING){
                if(bean.num_refund == bean.total_num || bean.num_canbe_used == 0){
                    finishList.add(bean);
                }else {
                    ingList.add(bean);
                }
            }else{
                finishList.add(bean);
            }
        }
        this.dataList.addAll(ingList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        this.ingList.clear();
        this.finishList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private OnMyItemClickListener onItemClickListener;
        @BindView(R.id.iv_avatar)
        RoundAngleImageView ivAvatar;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_total)
        TextView tvTotal;
        @BindView(R.id.tv_surplus)
        TextView tvSurplus;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.rl)
        RelativeLayout rl;
        int override;
        @BindView(R.id.iv_status)
        ImageView ivStatus;
        @BindView(R.id.tv_consume)
        TextView tvConsume;

        ViewHolder(View view,  OnMyItemClickListener onItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.onItemClickListener = onItemClickListener;
            override = ResoureUtils.getDimension(mContext, R.dimen.item_avatar_size);
        }

        private void bindItem(final ResultPackageAsset bean, final int position) {
            if(isCashier){
                tvConsume.setVisibility(View.VISIBLE);
                tvConsume.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }else{
                tvConsume.setVisibility(View.GONE);
            }
            if(bean.status == 1){
                if(bean.expired == DeadLineStatus.ING){
                    if(bean.num_refund == bean.total_num){
                        TextViewUtils.setTextOrEmpty(tvStatus, "已退款");
                        ivStatus.setImageResource(R.color.status_already_refund);
                        tvStatus.setTextColor(ResoureUtils.getColor(mContext, R.color.status_already_refund));
                    }else if(bean.num_canbe_used == 0){
                        TextViewUtils.setTextOrEmpty(tvStatus, "已用完");
                        ivStatus.setImageResource(R.color.status_done);
                        tvStatus.setTextColor(ResoureUtils.getColor(mContext, R.color.status_done));
                    }else{
                        TextViewUtils.setTextOrEmpty(tvStatus, "使用中");
                        ivStatus.setImageResource(R.color.status_normal);
                        tvStatus.setTextColor(ResoureUtils.getColor(mContext, R.color.status_normal));
                    }
                }else{
                    TextViewUtils.setTextOrEmpty(tvStatus, "已过期");
                    ivStatus.setImageResource(R.color.status_expired);
                    tvStatus.setTextColor(ResoureUtils.getColor(mContext, R.color.status_expired));
                }
            }else{
                TextViewUtils.setTextOrEmpty(tvStatus, "已作废");
                ivStatus.setImageResource(R.color.status_cancel);
                tvStatus.setTextColor(ResoureUtils.getColor(mContext, R.color.status_cancel));
            }

            if (!TextUtils.isEmpty(bean.thumb_url)) {
                if (bean.thumb_url.startsWith(mContext.getString(R.string.HTTP))) {
                    GlideHelper.displayImage(mContext, bean.thumb_url, ivAvatar, R.color.loadding_img_bg);
                } else {
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.thumb_url, ivAvatar, R.color.loadding_img_bg);
                }
            } else {
                GlideHelper.displayImage(mContext, R.color.loadding_img_bg, ivAvatar);
            }
            if (bean.transaction_type == 0) {
                TextViewUtils.setTextOrEmpty(tvType, "赠");
                tvType.setBackgroundColor(ResoureUtils.getColor(mContext, R.color.status_done));
            } else {
                TextViewUtils.setTextOrEmpty(tvType, "买");
                tvType.setBackgroundColor(ResoureUtils.getColor(mContext, R.color.colorPrimary));
            }
            TextViewUtils.setTextOrEmpty(tvName, bean.package_name);
            TextViewUtils.setTextOrEmpty(tvTime, DateTimeUtils.StringToDate(bean.consume_time, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_DATETIME_TWO));
            tvTotal.setText("总次数:" + bean.total_num);
            tvSurplus.setText("剩余次数:" + bean.num_canbe_used);
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
        void onItemClick(View view, ResultPackageAsset bean, int position);
    }

}
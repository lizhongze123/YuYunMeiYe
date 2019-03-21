package cn.yuyun.yymy.ui.home.member.asset;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lzz.utils.DensityUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultAssetDetail.MemberAssetsConsumeBean.ConsumptionBillsConsumeRspListBean;
import cn.yuyun.yymy.http.result.ResultBillManagerTypeDetail.BillAllInfoBean;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.RoundAngleImageView;
import cn.yuyun.yymy.view.ShadowDrawable;

/**
 * @author
 * @desc
 * @date
 */
public class AssetConsumeAssetAdapter extends RecyclerView.Adapter<AssetConsumeAssetAdapter.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_asset_consume_in;
    private Context mContext;
    private List<ConsumptionBillsConsumeRspListBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public AssetConsumeAssetAdapter(Context context) {
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

    public void notifyDataSetChanged(List<ConsumptionBillsConsumeRspListBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ConsumptionBillsConsumeRspListBean> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_avatar)
        RoundAngleImageView ivAvatar;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_brand)
        TextView tvBrand;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.tv_cancel)
        TextView tvCancel;
        @BindView(R.id.rl)
        RelativeLayout rl;

        ViewHolder(View v, OnMyItemClickListener onItemClickListener) {
            super(v);
            ButterKnife.bind(this, v);
        }

        private void bindItem(final ConsumptionBillsConsumeRspListBean bean, final int position) {

            if(null != bean.goodRsp){
                rl.setVisibility(View.VISIBLE);
                tvCancel.setVisibility(View.GONE);
                if(!TextUtils.isEmpty(bean.goodRsp.thumb_url)){
                    if(bean.goodRsp.thumb_url.startsWith(mContext.getString(R.string.HTTP))){
                        GlideHelper.displayImage(mContext, bean.goodRsp.thumb_url, ivAvatar);
                    }else{
                        GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.goodRsp.thumb_url, ivAvatar);
                    }
                }else{
                    GlideHelper.displayImage(mContext, R.color.loadding_img_bg, ivAvatar);
                }
                TextViewUtils.setTextOrEmpty(tvName, bean.goodRsp.name);
                TextViewUtils.setTextOrEmpty(tvType, "类型：" + bean.goodRsp.type_name);
                TextViewUtils.setTextOrEmpty(tvBrand, "品牌：" + bean.goodRsp.brand_name);
                TextViewUtils.setTextOrEmpty(tvNum, "X" + bean.consume_times);
            }else{
                rl.setVisibility(View.GONE);
                tvCancel.setVisibility(View.VISIBLE);
            }
        }

    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, BillAllInfoBean bean, int position);
    }

}

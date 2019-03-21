package cn.yuyun.yymy.ui.home.member;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultProduct;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.RoundAngleImageView;

/**
 * @author
 * @desc
 * @date
 */
public class AssetProductAdapter extends RecyclerView.Adapter<AssetProductAdapter.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_asset;
    private Context mContext;
    private List<ResultProduct> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public AssetProductAdapter(Context context) {
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

    public void notifyDataSetChanged(List<ResultProduct> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultProduct> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
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
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.rl)
        RelativeLayout rl;
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_desc)
        TextView tvDesc;
        @BindView(R.id.iv_status)
        ImageView ivStatus;
        int override;

        ViewHolder(View view, AssetProductAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.onItemClickListener = onItemClickListener;
            override = ResoureUtils.getDimension(mContext, R.dimen.item_avatar_size);
        }

        private void bindItem(final ResultProduct bean, final int position) {
            tvStatus.setVisibility(View.GONE);
            ivStatus.setVisibility(View.GONE);
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
                TextViewUtils.setTextOrEmpty(tvDesc, "（赠送）");
            } else {
                TextViewUtils.setTextOrEmpty(tvDesc, "（购买）");
            }
            tvType.setVisibility(View.GONE);
            TextViewUtils.setTextOrEmpty(tvName, bean.product_name);
            TextViewUtils.setTextOrEmpty(tvTime, DateTimeUtils.StringToDate(bean.consume_time, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_DATETIME_TWO));
            tvTotal.setText("总数量:" + bean.total_num);
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
        void onItemClick(View view, ResultProduct bean, int position);
    }

}
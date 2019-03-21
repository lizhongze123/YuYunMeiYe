package cn.yuyun.yymy.ui.home.cashier;

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
import cn.yuyun.yymy.bean.DeadLineStatus;
import cn.yuyun.yymy.http.result.ResultCanbeUsedAssest;
import cn.yuyun.yymy.http.result.ResultPackageAsset;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.RoundAngleImageView;

/**
 * @author
 * @desc
 * @date
 */
public class AssetCanbeUsedAdapter extends RecyclerView.Adapter<AssetCanbeUsedAdapter.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_asset;
    private Context mContext;
    private List<ResultCanbeUsedAssest.ResultCanbeUsedAssestBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public AssetCanbeUsedAdapter(Context context){
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

    public void notifyDataSetChanged(List<ResultCanbeUsedAssest.ResultCanbeUsedAssestBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultCanbeUsedAssest.ResultCanbeUsedAssestBean> dataList) {
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

        private void bindItem(final ResultCanbeUsedAssest.ResultCanbeUsedAssestBean bean, final int position) {
            tvConsume.setVisibility(View.VISIBLE);
            if(null != bean.goodRsp){
                if (!TextUtils.isEmpty(bean.goodRsp.thumb_url)) {
                    if (bean.goodRsp.thumb_url.startsWith(mContext.getString(R.string.HTTP))) {
                        GlideHelper.displayImage(mContext, bean.goodRsp.thumb_url, ivAvatar, R.color.loadding_img_bg);
                    } else {
                        GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.goodRsp.thumb_url, ivAvatar, R.color.loadding_img_bg);
                    }
                } else {
                    GlideHelper.displayImage(mContext, R.color.loadding_img_bg, ivAvatar);
                }
            }

            if (bean.transaction_type == 0) {
                TextViewUtils.setTextOrEmpty(tvType, "赠");
                tvType.setBackgroundColor(ResoureUtils.getColor(mContext, R.color.status_done));
            } else {
                TextViewUtils.setTextOrEmpty(tvType, "买");
                tvType.setBackgroundColor(ResoureUtils.getColor(mContext, R.color.colorPrimary));
            }
            TextViewUtils.setTextOrEmpty(tvName, bean.asset_name);
            TextViewUtils.setTextOrEmpty(tvTime, DateTimeUtils.StringToDate(bean.transaction_time, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_DATETIME_TWO));
            tvTotal.setText("总次数:" + bean.num_total);
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
        void onItemClick(View view, ResultCanbeUsedAssest.ResultCanbeUsedAssestBean bean, int position);
    }

}
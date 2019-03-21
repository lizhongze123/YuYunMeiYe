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
import cn.yuyun.yymy.http.result.ResultDepositAsset;
import cn.yuyun.yymy.http.result.ResultPackageAsset;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.RoundAngleImageView;

/**
 * @author
 * @desc
 * @date
 */
public class AssetDepositAdapter extends RecyclerView.Adapter<AssetDepositAdapter.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_asset;
    private Context mContext;
    private List<ResultDepositAsset> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public AssetDepositAdapter(Context context){
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

    public void notifyDataSetChanged(List<ResultDepositAsset> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultDepositAsset> dataList) {
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
        @BindView(R.id.tv_type)
        TextView tvType;
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
        int override;
        @BindView(R.id.iv_status)
        ImageView ivStatus;

        ViewHolder(View view,  AssetDepositAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.onItemClickListener = onItemClickListener;
            override = ResoureUtils.getDimension(mContext, R.dimen.item_avatar_size);
        }

        private void bindItem(final ResultDepositAsset bean, final int position) {
            TextViewUtils.setTextOrEmpty(tvTime, DateTimeUtils.StringToDate(bean.createTime, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_DATETIME_TWO));
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, bean, position);
                    }
                }
            });
            if (!TextUtils.isEmpty(bean.memberAvatar)) {
                if (bean.memberAvatar.startsWith(mContext.getString(R.string.HTTP))) {
                    GlideHelper.displayImage(mContext, bean.memberAvatar, ivAvatar, R.color.loadding_img_bg);
                } else {
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.memberAvatar, ivAvatar, R.color.loadding_img_bg);
                }
            } else {
                GlideHelper.displayImage(mContext, R.color.loadding_img_bg, ivAvatar);
            }
            TextViewUtils.setTextOrEmpty(tvName, bean.name);
            TextViewUtils.setTextOrEmpty(tvStatus, bean.status.desc);
//            tvStatus.setBackgroundColor(ResoureUtils.getColor(mContext, bean.status.resId));
            tvStatus.setVisibility(View.GONE);
            ivStatus.setVisibility(View.GONE);
            tvType.setVisibility(View.GONE);
            TextViewUtils.setTextOrEmpty(tvTotal, "物品价值:" + bean.type.desc);
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, ResultDepositAsset bean, int position);
    }

}
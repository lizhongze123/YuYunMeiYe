package cn.yuyun.yymy.ui.store;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.http.result.ResultTotalAmount;
import cn.yuyun.yymy.utils.GlideHelper;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author lzz
 * @desc
 * @date
 */
public class ResultsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_results;
    private Context mContext;
    private List<ResultTotalAmount> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;
    private int type;

    public ResultsAdapter(Context context) {
        this.mContext = context;
    }

    public ResultsAdapter(Context context, int type) {
        this.mContext = context;
        this.type = type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(RESOURCE_ID, parent, false);
        return new ViewHolder(rootView, onItemClickListener);
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

    public void notifyDataSetChanged(List<ResultTotalAmount> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultTotalAmount> dataList) {
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
        CircleImageView ivAvatar;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_level)
        TextView tvLevel;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_store)
        TextView tvStore;
        @BindView(R.id.tv_mobile)
        TextView tvMobile;
        @BindView(R.id.tv_consumeTime)
        TextView tvConsumeTime;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.rl)
        RelativeLayout rl;

        ViewHolder(View view, OnMyItemClickListener onItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final ResultTotalAmount bean, final int position) {
            if (!TextUtils.isEmpty(bean.avatar)) {
                if (bean.avatar.startsWith(mContext.getString(R.string.HTTP))) {
                    GlideHelper.displayImage(mContext, bean.avatar, ivAvatar);
                } else {
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.avatar, ivAvatar);
                }
            } else {
                GlideHelper.displayImage(mContext, R.drawable.avatar_default_female, ivAvatar);
            }

            if (TextUtils.isEmpty(bean.member_level_name)) {
                tvLevel.setVisibility(View.GONE);
            } else {
                tvLevel.setVisibility(View.VISIBLE);
                TextViewUtils.setTextOrEmpty(tvLevel, "(" + bean.member_level_name + ")");
            }

            TextViewUtils.setTextOrEmpty(tvMobile, "手机号:" + bean.member_mobile);
            TextViewUtils.setTextOrEmpty(tvConsumeTime, "消费时间:" + bean.consume_time);
            if (type == 1) {
                TextViewUtils.setTextOrEmpty(tvStatus, "实收:" + StringFormatUtils.formatTwoDecimal(bean.amount_realpay));
                TextViewUtils.setTextOrEmpty(tvName, bean.member_name);
                TextViewUtils.setTextOrEmpty(tvStore, bean.cashier_sp_name);
                TextViewUtils.setTextOrEmpty(tvTime, StringFormatUtils.getUserDate(bean.create_time, DateTimeUtils.FORMAT_DATETIME));
            } else if (type == 2) {
                TextViewUtils.setTextOrEmpty(tvStatus, "消耗:" + StringFormatUtils.formatTwoDecimal(bean.consume_amount_sp));
                TextViewUtils.setTextOrEmpty(tvName, bean.member_name);
                TextViewUtils.setTextOrEmpty(tvStore, bean.cashier_sp_name);
                TextViewUtils.setTextOrEmpty(tvTime, StringFormatUtils.getUserDate(bean.consume_time, DateTimeUtils.FORMAT_DATETIME));
            } else {
                TextViewUtils.setTextOrEmpty(tvStatus, "业绩:" + StringFormatUtils.formatTwoDecimal(bean.store_performanc_sp));
                TextViewUtils.setTextOrEmpty(tvName, bean.member_name);
                TextViewUtils.setTextOrEmpty(tvStore, bean.cashier_sp_name);
                TextViewUtils.setTextOrEmpty(tvTime, StringFormatUtils.getUserDate(bean.consume_time, DateTimeUtils.FORMAT_DATETIME));
            }
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, ResultTotalAmount bean, int position);
    }

}

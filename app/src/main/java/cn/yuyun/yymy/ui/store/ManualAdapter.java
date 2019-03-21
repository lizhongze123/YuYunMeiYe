package cn.yuyun.yymy.ui.store;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import cn.yuyun.yymy.bean.ConsumeType;
import cn.yuyun.yymy.bean.RecordType;
import cn.yuyun.yymy.http.result.RecordBean;
import cn.yuyun.yymy.http.result.ResultHandmake;
import cn.yuyun.yymy.utils.GlideHelper;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author
 * @desc
 * @date
 */
public class ManualAdapter extends RecyclerView.Adapter<ManualAdapter.RecordHolder> {

    private int RESOURCE_ID = R.layout.item_manual;
    private Context context;
    private List<ResultHandmake> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public ManualAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(RESOURCE_ID, parent, false);
        return new RecordHolder(rootView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecordHolder holder, int position) {
        holder.bindItem(dataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void notifyDataSetChanged(List<ResultHandmake> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultHandmake> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class RecordHolder extends RecyclerView.ViewHolder {

        private ManualAdapter.OnMyItemClickListener onItemClickListener;
        @BindView(R.id.rl)
        RelativeLayout rl;
        @BindView(R.id.iv_avatar)
        CircleImageView ivAvatar;
        @BindView(R.id.iv_sex)
        ImageView ivSex;
        @BindView(R.id.rl_avatar)
        RelativeLayout rlAvatar;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_level)
        TextView tvLevel;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.ll_user)
        LinearLayout llUser;
        @BindView(R.id.tv_store)
        TextView tvStore;
        @BindView(R.id.rl_name)
        RelativeLayout rlName;
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.tv_consumeTime)
        TextView tvConsumeTime;
        @BindView(R.id.tv_count)
        TextView tvCount;

        RecordHolder(View view, ManualAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final ResultHandmake bean, final int position) {

           if (!TextUtils.isEmpty(bean.member_avatar)) {
                if (bean.member_avatar.startsWith(context.getString(R.string.HTTP))) {
                    GlideHelper.displayImage(context, bean.member_avatar, ivAvatar);
                } else {
                    GlideHelper.displayImage(context, context.getString(R.string.image_url_prefix) + bean.member_avatar, ivAvatar);
                }
            } else {
                GlideHelper.displayImage(context, R.drawable.avatar_default_female, ivAvatar);
            }
            TextViewUtils.setTextOrEmpty(tvName, bean.member_name);
            tvLevel.setVisibility(View.GONE);
            if (TextUtils.isEmpty(bean.level_name)) {
                tvLevel.setVisibility(View.VISIBLE);
            } else {
                tvLevel.setVisibility(View.GONE);
                TextViewUtils.setTextOrEmpty(tvLevel, "(" + bean.level_name + ")");
            }
            TextViewUtils.setTextOrEmpty(tvTime, bean.create_time);
            TextViewUtils.setTextOrEmpty(tvStore, bean.cashier_sp_name);
            TextViewUtils.setTextOrEmpty(tvType, "消耗价值:"+StringFormatUtils.formatTwoDecimal(bean.consume_amount_now));
            TextViewUtils.setTextOrEmpty(tvType, "员工消耗:"+StringFormatUtils.formatTwoDecimal(bean.consume_amount_staff));
            TextViewUtils.setTextOrEmpty(tvConsumeTime, "消费时间:" + bean.consume_time);
            TextViewUtils.setTextOrEmpty(tvCount, "手工费:" + StringFormatUtils.formatTwoDecimal(bean.handmake));

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
        void onItemClick(View view, ResultHandmake bean, int position);
    }

}
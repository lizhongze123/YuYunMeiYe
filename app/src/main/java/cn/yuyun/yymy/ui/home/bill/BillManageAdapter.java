package cn.yuyun.yymy.ui.home.bill;

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
import cn.yuyun.yymy.http.result.ResultBillManage;
import cn.yuyun.yymy.utils.GlideHelper;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author
 * @desc
 * @date
 */
public class BillManageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_bill_manage_list;
    private Context mContext;
    private List<ResultBillManage> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;
    int type;

    public BillManageAdapter(Context context, int type) {
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

    public void notifyDataSetChanged(List<ResultBillManage> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultBillManage> dataList) {
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
        ImageView ivAvatar;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_vip)
        TextView tvVip;
        @BindView(R.id.tv_store)
        TextView tvStore;
        @BindView(R.id.tv_one)
        TextView tvOne;
        @BindView(R.id.tv_two)
        TextView tvTwo;
        @BindView(R.id.tv_three)
        TextView tvThree;
        @BindView(R.id.tv_four)
        TextView tvFour;

        @BindView(R.id.tv_createTime)
        TextView tvCreateTime;
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.rl)
        RelativeLayout rl;

        ViewHolder(View view, OnMyItemClickListener onItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final ResultBillManage bean, final int position) {
            if (!TextUtils.isEmpty(bean.member_avatar)) {
                if (bean.member_avatar.startsWith(mContext.getString(R.string.HTTP))) {
                    GlideHelper.displayImage(mContext, bean.member_avatar, ivAvatar);
                } else {
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.member_avatar, ivAvatar);
                }
            } else {
                if(null != bean.member_sex){
                    GlideHelper.displayImage(mContext, bean.member_sex.resId, ivAvatar);
                }else{
                    GlideHelper.displayImage(mContext, R.drawable.avatar_default_female, ivAvatar);
                }
            }
            TextViewUtils.setTextOrEmpty(tvName, bean.member_name);
            TextViewUtils.setTextOrEmpty(tvStore, bean.member_in_sp_name);
            TextViewUtils.setTextOrEmpty(tvCreateTime, DateTimeUtils.StringToDate(bean.create_time, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_DATE_UI_TWO));
            if (TextUtils.isEmpty(bean.member_level_name)) {
                tvVip.setVisibility(View.GONE);
            } else {
                tvVip.setVisibility(View.VISIBLE);
                TextViewUtils.setTextOrEmpty(tvVip, "(" + bean.member_level_name + ")");
            }
            TextViewUtils.setTextOrEmpty(tvOne, "消费类型：" + bean.containTypeDesc);
            TextViewUtils.setTextOrEmpty(tvTwo, "消费金额："  + StringFormatUtils.formatTwoDecimal(bean.amountRealpay + bean.pay_wechat_pay + bean.pay_ali_pay + bean.pay_cash + bean.pay_pos + bean.pay_transfer));
            TextViewUtils.setTextOrEmpty(tvThree, "消费时间：" + DateTimeUtils.StringToDate(bean.consume_time, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_DATE_UI_TWO));
            TextViewUtils.setTextOrEmpty(tvFour, "消费单据号：" + bean.record_id);
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != onItemClickListener){
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
        void onItemClick(View view, ResultBillManage bean, int position);
    }

}
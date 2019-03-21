package cn.yuyun.yymy.ui.me.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.bean.ConsumeType;
import cn.yuyun.yymy.bean.RecordType;
import cn.yuyun.yymy.http.result.RecordBean;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author
 * @desc
 * @date
 */
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordHolder>{

    private int RESOURCE_ID = R.layout.item_record;
    private Context context;
    private List<RecordBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;
    RecordType type;

    public RecordAdapter(Context context, List<RecordBean> list, RecordType type){
        this.context = context;
        this.dataList = list;
        this.type = type;
    }

    public RecordAdapter(Context context, RecordType type){
        this.context = context;
        this.type = type;
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

    public void notifyDataSetChanged(List<RecordBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<RecordBean> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class RecordHolder extends RecyclerView.ViewHolder {

        private RecordAdapter.OnMyItemClickListener onItemClickListener;
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
        @BindView(R.id.rl)
        RelativeLayout rl;

        RecordHolder(View view,  RecordAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final RecordBean bean, final int position) {
            TextViewUtils.setTextOrEmpty(tvName, bean.obj.memberName);
            TextViewUtils.setTextOrEmpty(tvTime, bean.obj.consumeTime);
            tvStore.setText(bean.obj.cashierSpName);
            if(bean.related_consumption_type == ConsumeType.VALUE_RECHARGE || bean.related_consumption_type == ConsumeType.VALUE_REFUND){
                tvType.setText("商品名称:" + bean.related_consumption_type.desc);
                if(type == RecordType.SERVICE){
                    tvType.setText("商品名称:" + bean.obj.goodName);
                    tvConsumeTime.setText(bean.obj.consumeAmountNow + " × " + StringFormatUtils.formatPercent(bean.commission));
                }else {
                    double real = (bean.obj.pay_ali_pay + bean.obj.pay_cash + bean.obj.pay_pos + bean.obj.pay_transfer +bean.obj.pay_wechat_pay);
                    tvConsumeTime.setText("消费金额:" + StringFormatUtils.formatTwoDecimal(real));
                    tvCount.setText("业绩:" + StringFormatUtils.formatTwoDecimal(bean.achieve_amount));
                }
            }else{
                tvType.setText("商品名称:" + bean.obj.goodName);
                if(type == RecordType.SERVICE){
                    //服务记录
                    double real = bean.obj.consumeAmountNow * bean.commission;
                    tvNum.setText("消费类型:" + bean.related_consumption_type.desc);
                    tvCount.setText("消耗:" + StringFormatUtils.formatTwoDecimal(bean.achieve_amount));
                    tvConsumeTime.setText("耗卡金额:" + StringFormatUtils.formatTwoDecimal(bean.obj.consumeAmountNow));
                }else {
                    tvCount.setText("业绩:" + StringFormatUtils.formatTwoDecimal(bean.achieve_amount));
                    tvNum.setText("消费类型:" + bean.related_consumption_type.desc);
                    tvConsumeTime.setText("消费金额:" + StringFormatUtils.formatTwoDecimal(bean.obj.amountRealpay));
                }
            }
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onItemClickListener) {
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
        void onItemClick(View view, RecordBean bean, int position);
    }

}
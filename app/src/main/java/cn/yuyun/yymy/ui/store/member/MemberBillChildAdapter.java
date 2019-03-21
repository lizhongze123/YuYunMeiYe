package cn.yuyun.yymy.ui.store.member;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.bean.ConsumeType;
import cn.yuyun.yymy.http.result.ResultBillManagerTypeDetail.BillAllInfoBean;
import cn.yuyun.yymy.http.result.RulesBean;
import cn.yuyun.yymy.ui.home.attendance.StorePicAdapter;

/**
 * @author lzz
 * @desc
 * @date 2018-01-25
 */
public class MemberBillChildAdapter extends RecyclerView.Adapter<MemberBillChildAdapter.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_member_consume_bill_child;
    private Context mContext;
    private List<BillAllInfoBean> parentList = new ArrayList<>();

    public MemberBillChildAdapter(Context context){
        this.mContext = context;
    }

    public void addAll(List<BillAllInfoBean> dataList) {
        if(null != dataList){
            this.parentList.addAll(dataList);
            notifyDataSetChanged();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(RESOURCE_ID, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindItem(parentList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return parentList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvOne, tvTwo, tvThree, tvFour, tvFive, tvSix, tvSeven, tvEight;

        ViewHolder(View view) {
            super(view);
            tvOne =  view.findViewById(R.id.tv_one);
            tvTwo =  view.findViewById(R.id.tv_two);
            tvThree =  view.findViewById(R.id.tv_three);
            tvFour =  view.findViewById(R.id.tv_four);
            tvFive =  view.findViewById(R.id.tv_five);
            tvSix =  view.findViewById(R.id.tv_six);
            tvSeven = view.findViewById(R.id.tv_seven);
            tvEight = view.findViewById(R.id.tv_eight);
        }

        private void bindItem(final BillAllInfoBean bean, final int parentPos) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bean.staffPreSaleServiceRecordRspList.size(); i++) {
                sb.append(bean.staffPreSaleServiceRecordRspList.get(i).staff_name);
            }
            if(bean.consumeType == ConsumeType.BUY){
                tvOne.setText("消费类型:(购买)" + bean.good_name);
                tvTwo.setText("消费金额:" + StringFormatUtils.formatTwoDecimal(bean.transaction_price));
                tvThree.setText("实收金额:" + StringFormatUtils.formatTwoDecimal(bean.amount_realpay));
                tvFour.setText("详单编号:" + bean.bill_buy_id);
                tvFive.setText("服务人员:" + sb.toString());
                tvSix.setText("备注信息:" + bean.description);
            }else if(bean.consumeType == ConsumeType.GIVE){
                tvOne.setText("消费类型:(赠送)" + bean.good_name);
                tvTwo.setText("实收金额:" + StringFormatUtils.formatTwoDecimal(bean.amount_realpay));
                tvThree.setText("详单编号:" + bean.bill_giveaway_id);
                tvFour.setText("服务人员:" +sb.toString());
                tvFive.setText("备注信息:" + bean.description);
            }else if(bean.consumeType == ConsumeType.CARD){
                tvOne.setText("消费类型:(耗卡)" + bean.good_name);
                tvTwo.setText("消费金额:" + StringFormatUtils.formatTwoDecimal(bean.consume_amount_now));
                tvThree.setText("实收金额:" + StringFormatUtils.formatTwoDecimal(bean.amount_realpay));
                tvFour.setText("详单编号:" + bean.bill_consume_id);
                tvFive.setText("服务人员:"+ sb.toString());
                tvSix.setText("备注信息:" + bean.description);
            }else if(bean.consumeType == ConsumeType.REPAYMENT){
                tvOne.setText("消费类型:(还款)" + bean.good_name);
                tvTwo.setText("消费金额:" + StringFormatUtils.formatTwoDecimal(bean.amount_realpay));
                tvThree.setText("详单编号:" + bean.bill_repayment_id);
                tvFour.setText("服务人员:" + sb.toString());
                tvFive.setText("备注信息:" + bean.description);
            }else if(bean.consumeType == ConsumeType.REFUND){
                tvOne.setText("消费类型:(退款)" + bean.good_name);
                tvTwo.setText("消费金额:" + StringFormatUtils.formatTwoDecimal(bean.refund_amount_to_storedvalue));
                tvThree.setText("实退金额:" + StringFormatUtils.formatTwoDecimal(bean.transaction_price));
                tvFour.setText("详单编号:" + bean.bill_refund_id);
                tvFive.setText("服务人员:" + sb.toString());
                tvSix.setText("备注信息:" + bean.description);
            }

        }
    }

}
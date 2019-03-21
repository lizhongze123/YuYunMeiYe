package cn.yuyun.yymy.ui.store.report;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultReportBusinessDetail;

/**
 * @author
 * @desc
 * @date
 */
public class ReportBusinessDetailRightAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_report_business_detail_row;
    private Context context;
    private List<ResultReportBusinessDetail> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public ReportBusinessDetailRightAdapter(Context context) {
        this.context = context;
    }

    public void notifyDataSetChanged(List<ResultReportBusinessDetail> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<ResultReportBusinessDetail> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(RESOURCE_ID, parent, false);
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

    class ViewHolder extends RecyclerView.ViewHolder {

        private OnMyItemClickListener onItemClickListener;
        @BindView(R.id.tv_storeName)
        TextView tvStoreName;
        @BindView(R.id.tv_consumeType)
        TextView tvConsumeType;
        @BindView(R.id.tv_billNum)
        TextView tvBillNum;
        @BindView(R.id.tv_memberName)
        TextView tvMemberName;
        @BindView(R.id.tv_cardNum)
        TextView tvCardNum;
        @BindView(R.id.tv_service)
        TextView tvService;
        @BindView(R.id.tv_package)
        TextView tvPackage;
        @BindView(R.id.tv_product)
        TextView tvProduct;
        @BindView(R.id.tv_one)
        TextView tvOne;
        @BindView(R.id.tv_two)
        TextView tvTwo;
        @BindView(R.id.tv_three)
        TextView tvThree;
        @BindView(R.id.tv_four)
        TextView tvFour;
        @BindView(R.id.tv_five)
        TextView tvFive;
        @BindView(R.id.tv_six)
        TextView tvSix;
        @BindView(R.id.tv_seven)
        TextView tvSeven;
        @BindView(R.id.tv_eight)
        TextView tvEight;
        @BindView(R.id.tv_nine)
        TextView tvNine;
        @BindView(R.id.tv_ten)
        TextView tvTen;
        @BindView(R.id.tv_eleven)
        TextView tvEleven;
        @BindView(R.id.tv_twelve)
        TextView tvTwelve;
        @BindView(R.id.rl)
        RelativeLayout rl;

        ViewHolder(View view, OnMyItemClickListener onItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final ResultReportBusinessDetail bean, final int position) {
            if (position % 2 == 0) {
                rl.setBackgroundResource(R.color.white);
            } else {
                rl.setBackgroundResource(R.color.item_analysis);
            }
            TextViewUtils.setTextOrEmpty(tvStoreName, bean.sp_name);
            if(null != bean.consume_type_desc){
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < bean.consume_type_desc.size(); i++) {
                    sb.append(bean.consume_type_desc.get(i).desc + ",");
                }
            }else{
                tvConsumeType.setText("");
            }
            TextViewUtils.setTextOrEmpty(tvBillNum, bean.record_id);
            TextViewUtils.setTextOrEmpty(tvService, bean.service_desc);
            TextViewUtils.setTextOrEmpty(tvPackage, bean.package_desc);
            TextViewUtils.setTextOrEmpty(tvProduct, bean.product_desc);
            TextViewUtils.setTextOrEmpty(tvMemberName, bean.member_name);
            TextViewUtils.setTextOrEmpty(tvCardNum, bean.member_card);
            //实退金额
            TextViewUtils.setTextOrEmpty(tvOne, StringFormatUtils.formatTwoDecimal(bean.amount_refund_realpay));
            //成交金额
            TextViewUtils.setTextOrEmpty(tvTwo, StringFormatUtils.formatTwoDecimal(bean.transaction_price));
            //现金
            TextViewUtils.setTextOrEmpty(tvThree, StringFormatUtils.formatTwoDecimal(bean.pay_cash));
            //刷卡
            TextViewUtils.setTextOrEmpty(tvFour, StringFormatUtils.formatTwoDecimal(bean.pay_pos));
            //转账
            TextViewUtils.setTextOrEmpty(tvFive, StringFormatUtils.formatTwoDecimal(bean.pay_transfer));
            //微信
            TextViewUtils.setTextOrEmpty(tvSix, StringFormatUtils.formatTwoDecimal(bean.pay_wechat_pay));
            //支付宝
            TextViewUtils.setTextOrEmpty(tvSeven, StringFormatUtils.formatTwoDecimal(bean.pay_ali_pay));
            //储值
            TextViewUtils.setTextOrEmpty(tvEight, StringFormatUtils.formatTwoDecimal(bean.storedvalue));
            //积分余额
            TextViewUtils.setTextOrEmpty(tvNine, StringFormatUtils.formatTwoDecimal(bean.accumulate_sv));
            //免单
            TextViewUtils.setTextOrEmpty(tvTen, StringFormatUtils.formatTwoDecimal(bean.free_pay));
            //现金券
            TextViewUtils.setTextOrEmpty(tvEleven, StringFormatUtils.formatTwoDecimal(bean.cash_coupon_pay));
            //欠款
            TextViewUtils.setTextOrEmpty(tvTwelve, StringFormatUtils.formatTwoDecimal(bean.arrear));
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(ResultReportBusinessDetail bean, int position);
    }

}
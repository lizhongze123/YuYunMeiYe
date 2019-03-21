package cn.yuyun.yymy.ui.home.bill;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultBillManagerTypeDetail.BillAllInfoBean;

/**
 * @author
 * @desc
 * @date
 */
public class BillConsumeAdapter extends RecyclerView.Adapter<BillConsumeAdapter.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_bill_report_child_consume;
    private Context mContext;
    private List<BillAllInfoBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public BillConsumeAdapter(Context context) {
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
        if(dataList.size() == 0){
            return dataList.size();
        }else{
            if(mType == 0){
                return 1;
            }else{
                return dataList.size();
            }
        }
    }

    private int mType;

    public void setAllorOne(int type){
        mType = type;
        notifyDataSetChanged();
    }

    public int getAllorOne(){
        return mType;
    }

    public void notifyDataSetChanged(List<BillAllInfoBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<BillAllInfoBean> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tvName;
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
        @BindView(R.id.tv_night)
        TextView tvNight;

        ViewHolder(View v, OnMyItemClickListener onItemClickListener) {
            super(v);
            ButterKnife.bind(this, v);
        }

        private void bindItem(final BillAllInfoBean bean, final int position) {
            tvName.setText(bean.good_name);
            TextViewUtils.setText(tvOne, "消费门店：", bean.cashier_sp_name);
            tvTwo.setText("消耗次数：" + bean.consume_num_now);
            tvThree.setText("消耗价值：" + StringFormatUtils.formatDecimal(bean.consume_amount_now) + "元");
            tvFour.setText("服务时长：" + bean.consume_time_now  + "分钟");
            TextViewUtils.setTextOrEmpty(tvFive, "消耗时间：" + DateTimeUtils.StringToDate(bean.consume_time, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_DATETIME_TWO));
            tvSeven.setText("资产编号：" + bean.related_asset_id);
            tvEight.setText("消费单据号：" + bean.related_record_id);
            tvNight.setText("详单编号：" + bean.bill_consume_id);

            if(null != bean.staffPreSaleServiceRecordRspList){
                StringBuilder sb = new StringBuilder();
                for  ( int  i  =   0 ; i  <  bean.staffPreSaleServiceRecordRspList.size(); i ++ )  {
                    for  ( int  j  =  bean.staffPreSaleServiceRecordRspList.size()  -   1 ; j  >  i; j -- )  {
                        if  (bean.staffPreSaleServiceRecordRspList.get(j).staff_name.equals(bean.staffPreSaleServiceRecordRspList.get(i).staff_name))  {
                            bean.staffPreSaleServiceRecordRspList.remove(j);
                        }
                    }
                }
                for (int i = 0; i < bean.staffPreSaleServiceRecordRspList.size(); i++) {
                    if(i == bean.staffPreSaleServiceRecordRspList.size() - 1){
                        sb.append(bean.staffPreSaleServiceRecordRspList.get(i).staff_name);
                    }else{
                        sb.append(bean.staffPreSaleServiceRecordRspList.get(i).staff_name  + ";");
                    }
                }
                tvSix.setText("服务人员：" + sb.toString());
            }else{
                tvSix.setText("服务人员：");
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

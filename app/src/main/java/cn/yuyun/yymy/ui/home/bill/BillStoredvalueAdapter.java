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
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultBillManagerTypeDetail.BillAllInfoBean;

/**
 * @author
 * @desc
 * @date
 */
public class BillStoredvalueAdapter extends RecyclerView.Adapter<BillStoredvalueAdapter.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_bill_report_child_storedvalue;
    private Context mContext;
    private List<BillAllInfoBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public BillStoredvalueAdapter(Context context) {
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

        ViewHolder(View v, OnMyItemClickListener onItemClickListener) {
            super(v);
            ButterKnife.bind(this, v);
        }

        private void bindItem(final BillAllInfoBean bean, final int position) {
            tvOne.setText("消费门店:" + bean.cashier_sp_name);
            tvTwo.setText("储值金额:"  + (bean.current - bean.previous));
            tvThree.setText("单据类型:" + bean.related_consumption_type_desc);
            tvFour.setText("消费时间:" + bean.consume_time);
            tvSix.setText("消费单据号:" + bean.storedvalue_id);
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
                    sb.append(bean.staffPreSaleServiceRecordRspList.get(i).staff_name  + ";");
                }
                tvFive.setText("服务人员:" + sb.toString());
            }else{
                tvFive.setText("服务人员:");
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

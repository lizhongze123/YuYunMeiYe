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
import cn.yuyun.yymy.http.result.ResultAnalysisOne;
import cn.yuyun.yymy.http.result.ResultReportConsumeDetail;

/**
 * @author
 * @desc
 * @date
 */
public class ReportConsumeDetailRightAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_report_consume_detail_row;
    private Context context;
    private List<ResultReportConsumeDetail> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public ReportConsumeDetailRightAdapter(Context context) {
        this.context = context;
    }

    public void notifyDataSetChanged(List<ResultReportConsumeDetail> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultReportConsumeDetail> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
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
        @BindView(R.id.tv_billNum)
        TextView tvBillNum;
        @BindView(R.id.tv_memberName)
        TextView tvMemberName;
        @BindView(R.id.tv_cardNum)
        TextView tvCardNum;
        @BindView(R.id.tv_goodsType)
        TextView tvGoodsType;
        @BindView(R.id.tv_goodsName)
        TextView tvGoodsName;
        @BindView(R.id.tv_staffName)
        TextView tvStaffName;
        @BindView(R.id.tv_saleName)
        TextView tvSaleName;
        @BindView(R.id.tv_one)
        TextView tvOne;
        @BindView(R.id.tv_two)
        TextView tvTwo;
        @BindView(R.id.tv_three)
        TextView tvThree;
        @BindView(R.id.rl)
        RelativeLayout rl;

        ViewHolder(View view, OnMyItemClickListener onItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final ResultReportConsumeDetail bean, final int position) {
            if (position % 2 == 0) {
                rl.setBackgroundResource(R.color.white);
            } else {
                rl.setBackgroundResource(R.color.item_analysis);
            }
            TextViewUtils.setTextOrEmpty(tvOne, StringFormatUtils.formatTwoDecimal(bean.consume_amount_now));
            TextViewUtils.setTextOrEmpty(tvTwo, StringFormatUtils.formatTwoDecimal(bean.consume_amount_sp));
            TextViewUtils.setTextOrEmpty(tvThree, StringFormatUtils.formatTwoDecimal(bean.consume_amount_staff));
            TextViewUtils.setTextOrEmpty(tvStoreName, bean.sp_name);
            TextViewUtils.setTextOrEmpty(tvBillNum, bean.related_record_id);
            TextViewUtils.setTextOrEmpty(tvMemberName, bean.member_name);
            TextViewUtils.setTextOrEmpty(tvCardNum, bean.member_card);
            if(bean.related_asset_type == 1){
                tvGoodsType.setText("产品");
            }else if(bean.related_asset_type == 2){
                tvGoodsType.setText("项目");
            }else if(bean.related_asset_type == 3){
                tvGoodsType.setText("套餐");
            }
            TextViewUtils.setTextOrEmpty(tvGoodsName, bean.consume_assets_basic);
            TextViewUtils.setTextOrEmpty(tvStaffName, bean.mechanic_persons);

        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(ResultReportConsumeDetail bean, int position);
    }

}
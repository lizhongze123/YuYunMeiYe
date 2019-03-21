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
import cn.yuyun.yymy.http.result.ResultBusinessReport;

/**
 * @author
 * @desc
 * @date
 */
public class ReportBusinessStatisticsRightAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_report_business_statistics_row;
    private Context context;
    private List<ResultBusinessReport> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;
    private int flag;

    public ReportBusinessStatisticsRightAdapter(Context context) {
        this.context = context;
    }

    public void notifyDataSetChanged(List<ResultBusinessReport> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultBusinessReport> dataList) {
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
        @BindView(R.id.tv_store)
        TextView tvStore;
        @BindView(R.id.rl)
        RelativeLayout rl;

        ViewHolder(View view, OnMyItemClickListener onItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final ResultBusinessReport bean, final int position) {
            if(position == 0){
                flag = 0;
            }else{
                if(bean.date.equals(dataList.get(position - 1))){
                    flag = flag + 2;
                }else{
                    flag = flag + 1;
                }
            }
             if (flag % 2 == 0) {
                rl.setBackgroundResource(R.color.white);
            } else {
                rl.setBackgroundResource(R.color.item_analysis);
            }
            TextViewUtils.setTextOrEmpty(tvStore, bean.sp_name_in_sp_id_list);
            TextViewUtils.setTextOrEmpty(tvOne, StringFormatUtils.formatTwoDecimal(bean.analysisSpTurnoverSingleRsp.total_amount));
            TextViewUtils.setTextOrEmpty(tvTwo, StringFormatUtils.formatTwoDecimal(bean.analysisSpTurnoverSingleRsp.transaction_price));
            TextViewUtils.setTextOrEmpty(tvThree, StringFormatUtils.formatTwoDecimal(bean.analysisSpTurnoverSingleRsp.store_performance));
            TextViewUtils.setTextOrEmpty(tvFour, bean.analysisSpTurnoverSingleRsp.person_number + "");
            TextViewUtils.setTextOrEmpty(tvFive, bean.analysisSpTurnoverSingleRsp.person_times +"");
            TextViewUtils.setTextOrEmpty(tvSix, StringFormatUtils.formatTwoDecimal(bean.analysisSpTurnoverSingleRsp.amount_consume));
            TextViewUtils.setTextOrEmpty(tvSeven, bean.analysisSpTurnoverSingleRsp.service_numbers + "");
            TextViewUtils.setTextOrEmpty(tvEight, bean.analysisSpTurnoverSingleRsp.new_member_percent);
            TextViewUtils.setTextOrEmpty(tvNine, StringFormatUtils.formatTwoDecimal(bean.analysisSpTurnoverSingleRsp.refund_realpay));

        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(ResultBusinessReport bean, int position);
    }

}
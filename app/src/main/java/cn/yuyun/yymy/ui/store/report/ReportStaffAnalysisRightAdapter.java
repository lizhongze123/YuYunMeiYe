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
import cn.yuyun.yymy.http.result.ResultStaffAnalysis;

/**
 * @author
 * @desc
 * @date
 */
public class ReportStaffAnalysisRightAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_report_staff;
    private Context context;
    private List<ResultStaffAnalysis> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public ReportStaffAnalysisRightAdapter(Context context) {
        this.context = context;
    }

    public void notifyDataSetChanged(List<ResultStaffAnalysis> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<ResultStaffAnalysis> dataList) {
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
        @BindView(R.id.rl)
        RelativeLayout rl;

        ViewHolder(View view, OnMyItemClickListener onItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final ResultStaffAnalysis bean, final int position) {
            if (position % 2 == 0) {
                rl.setBackgroundResource(R.color.white);
            } else {
                rl.setBackgroundResource(R.color.item_analysis);
            }
            TextViewUtils.setTextOrEmpty(tvStoreName, bean.sp_name);
            TextViewUtils.setTextOrEmpty(tvOne, StringFormatUtils.formatTwoDecimal(bean.presale));
            TextViewUtils.setTextOrEmpty(tvTwo, StringFormatUtils.formatTwoDecimal(bean.presale_refund));
            TextViewUtils.setTextOrEmpty(tvThree, StringFormatUtils.formatTwoDecimal(bean.sale));
            TextViewUtils.setTextOrEmpty(tvFour, StringFormatUtils.formatTwoDecimal(bean.sale_refund));
            TextViewUtils.setTextOrEmpty(tvFive, StringFormatUtils.formatTwoDecimal(bean.consume));
            TextViewUtils.setTextOrEmpty(tvSix, StringFormatUtils.formatTwoDecimal(bean.handmake));
            TextViewUtils.setTextOrEmpty(tvSeven, StringFormatUtils.formatTwoDecimal(bean.person_times));
            TextViewUtils.setTextOrEmpty(tvEight, bean.person_number + "");
            TextViewUtils.setTextOrEmpty(tvNine, StringFormatUtils.formatTwoDecimal(bean.consume_count));
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(ResultStaffAnalysis bean, int position);
    }

}
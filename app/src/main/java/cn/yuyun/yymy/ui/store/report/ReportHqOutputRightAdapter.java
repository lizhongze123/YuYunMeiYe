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
import cn.yuyun.yymy.http.result.ResultReportStoreSale;

/**
 * @author
 * @desc
 * @date
 */
public class ReportHqOutputRightAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_report_hq_output_row;
    private Context context;
    private List<ResultReportStoreSale> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public ReportHqOutputRightAdapter(Context context) {
        this.context = context;
    }

    public void notifyDataSetChanged(List<ResultReportStoreSale> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<ResultReportStoreSale> dataList) {
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
        @BindView(R.id.tv_thirteen)
        TextView tvThirteen;
        @BindView(R.id.rl)
        RelativeLayout rl;

        ViewHolder(View view, OnMyItemClickListener onItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final ResultReportStoreSale bean, final int position) {
            if (position % 2 == 0) {
                rl.setBackgroundResource(R.color.white);
            } else {
                rl.setBackgroundResource(R.color.item_analysis);
            }
            TextViewUtils.setTextOrEmpty(tvOne, bean.getProduct_name());
            TextViewUtils.setTextOrEmpty(tvTwo, bean.getDate());
            TextViewUtils.setTextOrEmpty(tvThree, bean.getType());
            TextViewUtils.setTextOrEmpty(tvFour, bean.getGain_sh_name());
            TextViewUtils.setTextOrEmpty(tvFive, bean.getStock_number() + "");
            TextViewUtils.setTextOrEmpty(tvSix, StringFormatUtils.formatTwoDecimal(bean.getStore_total_amount()));
            TextViewUtils.setTextOrEmpty(tvSeven, StringFormatUtils.formatTwoDecimal(bean.getStaff_money()));
            TextViewUtils.setTextOrEmpty(tvEight, StringFormatUtils.formatTwoDecimal(bean.getStore_money()));
            TextViewUtils.setTextOrEmpty(tvNine, StringFormatUtils.formatTwoDecimal(bean.getGuideprice()));
            TextViewUtils.setTextOrEmpty(tvTen, StringFormatUtils.formatTwoDecimal(bean.getBuy_money()));
            TextViewUtils.setTextOrEmpty(tvEleven, bean.getCreate_person_name());
            TextViewUtils.setTextOrEmpty(tvTwelve, bean.getOperate_person_name());
            TextViewUtils.setTextOrEmpty(tvThirteen, bean.getOrder_id());
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(ResultReportStoreSale bean, int position);
    }

}
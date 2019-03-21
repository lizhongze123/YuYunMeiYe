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
import cn.yuyun.yymy.http.result.ResultReportConsumeDetail;
import cn.yuyun.yymy.http.result.ResultReportStoreSale;

/**
 * @author
 * @desc
 * @date
 */
public class ReportStoreSaleRightAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_report_store_sale_row;
    private Context context;
    private List<ResultReportStoreSale> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public ReportStoreSaleRightAdapter(Context context) {
        this.context = context;
    }

    public void notifyDataSetChanged(List<ResultReportStoreSale> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultReportStoreSale> dataList) {
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
        @BindView(R.id.tv_goodsName)
        TextView tvGoodsName;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_type)
        TextView tvType;
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
            TextViewUtils.setTextOrEmpty(tvGoodsName, bean.getProduct_name());
            TextViewUtils.setTextOrEmpty(tvDate, bean.getDate());
            TextViewUtils.setTextOrEmpty(tvType, bean.getType());
            TextViewUtils.setTextOrEmpty(tvOne, bean.getStock_number() + "");
            TextViewUtils.setTextOrEmpty(tvTwo, StringFormatUtils.formatTwoDecimal(bean.getStore_total_amount()));
            TextViewUtils.setTextOrEmpty(tvThree, StringFormatUtils.formatTwoDecimal(bean.getStaff_money()));
            TextViewUtils.setTextOrEmpty(tvFour, StringFormatUtils.formatTwoDecimal(bean.getStore_money()));
            TextViewUtils.setTextOrEmpty(tvFive, StringFormatUtils.formatTwoDecimal(bean.getGuideprice()));
            TextViewUtils.setTextOrEmpty(tvSix, StringFormatUtils.formatTwoDecimal(bean.getReal_selling_price()));
            TextViewUtils.setTextOrEmpty(tvSeven, bean.getCreate_person_name());
            TextViewUtils.setTextOrEmpty(tvEight, bean.getOperate_person_name());
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(ResultReportStoreSale bean, int position);
    }

}
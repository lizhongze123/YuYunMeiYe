package cn.yuyun.yymy.ui.home.analysis;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultAnalysisOne;
import cn.yuyun.yymy.http.result.ResultAnalysisTotal;

/**
 * @author
 * @desc
 * @date
 */
public class AnalysisOneRightAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_analysis_row;
    private Context context;
    private List<ResultAnalysisOne> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public AnalysisOneRightAdapter(Context context) {
        this.context = context;
    }

    public void notifyDataSetChanged(List<ResultAnalysisOne> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<ResultAnalysisOne> dataList) {
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

        private AnalysisOneRightAdapter.OnMyItemClickListener onItemClickListener;
        private RelativeLayout rl;
        TextView tvOne, tvTwo, tvThree, tvFour, tvFive;

        ViewHolder(View view, AnalysisOneRightAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            tvOne = (TextView) view.findViewById(R.id.tv_one);
            tvTwo = (TextView) view.findViewById(R.id.tv_two);
            tvThree = (TextView) view.findViewById(R.id.tv_three);
            tvFour = (TextView) view.findViewById(R.id.tv_four);
            tvFive = (TextView) view.findViewById(R.id.tv_five);
            rl = (RelativeLayout) view.findViewById(R.id.rl);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final ResultAnalysisOne bean, final int position) {
            if (position % 2 == 0) {
                rl.setBackgroundResource(R.color.white);
            } else {
                rl.setBackgroundResource(R.color.item_analysis);
            }
            TextViewUtils.setTextOrEmpty(tvOne, bean.asset_type_name);
            TextViewUtils.setTextOrEmpty(tvTwo, StringFormatUtils.formatTwoDecimal(bean.total_pay_amount));
            TextViewUtils.setTextOrEmpty(tvThree, StringFormatUtils.formatTwoDecimal(bean.total_transaction_price));
            TextViewUtils.setTextOrEmpty(tvFour, StringFormatUtils.formatTwoDecimal(bean.total_achieve_percent));
            TextViewUtils.setTextOrEmpty(tvFive, StringFormatUtils.formatTwoDecimal(bean.total_consume_amount));
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(bean, position);
                }
            });
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(ResultAnalysisOne bean, int position);
    }

}
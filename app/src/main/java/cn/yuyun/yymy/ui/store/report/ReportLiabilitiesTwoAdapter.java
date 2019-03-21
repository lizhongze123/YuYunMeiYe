package cn.yuyun.yymy.ui.store.report;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultLiabilitiesTwo;

/**
 * @author lzz
 * @desc
 * @date 2018-01-25
 */
public class ReportLiabilitiesTwoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_report_liabilities_two;
    private Context context;
    private List<ResultLiabilitiesTwo> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public ReportLiabilitiesTwoAdapter(Context context){
        this.context = context;
    }

    public void notifyDataSetChanged(List<ResultLiabilitiesTwo> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultLiabilitiesTwo> dataList) {
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

        private ReportLiabilitiesTwoAdapter.OnMyItemClickListener onItemClickListener;
        private LinearLayout ll;
        private RelativeLayout rl;
        TextView tvOne, tvTwo, tvThree, tvFour;

        ViewHolder(View view,  ReportLiabilitiesTwoAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            tvOne = (TextView) view.findViewById(R.id.tv_one);
            tvTwo = (TextView) view.findViewById(R.id.tv_two);
            tvThree = (TextView) view.findViewById(R.id.tv_three);
            tvFour = (TextView) view.findViewById(R.id.tv_four);
            ll = (LinearLayout) itemView.findViewById(R.id.ll);
            rl = (RelativeLayout) itemView.findViewById(R.id.rl);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final ResultLiabilitiesTwo bean, final int position) {

            if (position % 2 == 0) {
                rl.setBackgroundResource(R.color.white);
            } else {
                rl.setBackgroundResource(R.color.item_analysis);
            }

            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, bean, position);
                    }
                }
            });

            TextViewUtils.setTextOrEmpty(tvOne, bean.brand_name);
            TextViewUtils.setTextOrEmpty(tvTwo,  bean.asset_type);
            TextViewUtils.setTextOrEmpty(tvThree,  StringFormatUtils.formatTwoDecimal(bean.amount_still_here));
            TextViewUtils.setTextOrEmpty(tvFour,  bean.num_canbe_used + "");
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, ResultLiabilitiesTwo bean, int position);
    }

}
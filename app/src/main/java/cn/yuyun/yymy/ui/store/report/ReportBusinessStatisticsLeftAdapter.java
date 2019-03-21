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

import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultAnalysisOne;
import cn.yuyun.yymy.http.result.ResultBusinessReport;

/**
 * @author
 * @desc
 * @date
 */
public class ReportBusinessStatisticsLeftAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_analysis_single;
    private Context context;
    private List<ResultBusinessReport> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;
    private int flag;

    public ReportBusinessStatisticsLeftAdapter(Context context) {
        this.context = context;
    }

    public void notifyDataSetChanged(List<ResultBusinessReport> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<ResultBusinessReport> dataList) {
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

        private ReportBusinessStatisticsLeftAdapter.OnMyItemClickListener onItemClickListener;
        private RelativeLayout rl;
        TextView tvName;

        ViewHolder(View view, ReportBusinessStatisticsLeftAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            rl = (RelativeLayout) view.findViewById(R.id.rl);
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

            if(position == 0){
                tvName.setVisibility(View.VISIBLE);
                TextViewUtils.setTextOrEmpty(tvName, bean.date);
            }else{
                if(bean.date.equals(dataList.get(position - 1))){
                    tvName.setVisibility(View.GONE);
                }else{
                    tvName.setVisibility(View.VISIBLE);
                    TextViewUtils.setTextOrEmpty(tvName, bean.date);
                }
            }
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(ResultAnalysisOne bean, int position);
    }

}
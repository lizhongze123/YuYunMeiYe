package cn.yuyun.yymy.ui.home.analysis;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultMemberAnalysis;

/**
 * @author lzz
 * @desc
 * @date 2018-01-25
 */
public class AnalysisMemberAdapter extends RecyclerView.Adapter<AnalysisMemberAdapter.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_analysis_two;
    private Context context;
    private List<ResultMemberAnalysis> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public AnalysisMemberAdapter(Context context){
        this.context = context;
    }

    public void notifyDataSetChanged(List<ResultMemberAnalysis> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultMemberAnalysis> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(RESOURCE_ID, parent, false);
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

    class ViewHolder extends RecyclerView.ViewHolder {

        private AnalysisMemberAdapter.OnMyItemClickListener onItemClickListener;
        private LinearLayout ll;
        TextView tvOne, tvTwo, tvThree, tvFour;

        ViewHolder(View view,  AnalysisMemberAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            tvOne = (TextView) view.findViewById(R.id.tv_one);
            tvTwo = (TextView) view.findViewById(R.id.tv_two);
            tvThree = (TextView) view.findViewById(R.id.tv_three);
            tvFour = (TextView) view.findViewById(R.id.tv_four);
            ll = (LinearLayout) itemView.findViewById(R.id.ll);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final ResultMemberAnalysis bean, final int position) {
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, position);
                    }
                }
            });

        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, int position);
    }

}
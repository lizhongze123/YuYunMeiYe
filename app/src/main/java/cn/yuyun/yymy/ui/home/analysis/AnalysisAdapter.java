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
import cn.yuyun.yymy.http.result.ResultAnalysisTotal;

/**
 * @author lzz
 * @desc
 * @date 2018-01-25
 */
public class AnalysisAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int VIEW_TYPE = -1;
    private int RESOURCE_ID_EMPTY = R.layout.item_list_empty;
    private int RESOURCE_ID = R.layout.item_analysis_single;
    private Context context;
    private List<ResultAnalysisTotal> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;
    int type;

    public AnalysisAdapter(Context context){
        this.context = context;
    }

    public AnalysisAdapter(Context context, int type){
        this.context = context;
        this.type = type;
    }

    public void notifyDataSetChanged(List<ResultAnalysisTotal> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultAnalysisTotal> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == -1) {
            View emptyView = LayoutInflater.from(context).inflate(RESOURCE_ID_EMPTY, parent, false);
            return new EmptyHolder(emptyView);
        }else{
            View rootView = LayoutInflater.from(context).inflate(RESOURCE_ID, parent, false);
            return new ViewHolder(rootView, onItemClickListener);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.bindItem(dataList.get(position), position);
        }else{
            EmptyHolder emptyHolder = (EmptyHolder) holder;
            emptyHolder.bindItem();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(dataList.size() == 0){
            return VIEW_TYPE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
//        return dataList.size() > 0 ? dataList.size() : 1;
        return 20;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private AnalysisAdapter.OnMyItemClickListener onItemClickListener;
        private LinearLayout ll;
        TextView tvOne, tvTwo, tvThree, tvFour, tvFive;

        ViewHolder(View view,  AnalysisAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            tvOne = (TextView) view.findViewById(R.id.tv_one);
            tvTwo = (TextView) view.findViewById(R.id.tv_two);
            tvThree = (TextView) view.findViewById(R.id.tv_three);
            tvFour = (TextView) view.findViewById(R.id.tv_four);
            tvFive = (TextView) view.findViewById(R.id.tv_five);
            ll = (LinearLayout) itemView.findViewById(R.id.ll);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final ResultAnalysisTotal bean, final int position) {
            /*if(type == 3){
                tvFive.setVisibility(View.VISIBLE);
                TextViewUtils.setTextOrEmpty(tvFive, bean.sp_name);
                TextViewUtils.setTextOrEmpty(tvOne, bean.good_name);
                TextViewUtils.setTextOrEmpty(tvTwo, bean.member_name);
            }else if(type == 2){
                tvFive.setVisibility(View.VISIBLE);
                TextViewUtils.setTextOrEmpty(tvOne, bean.good_brand);
                TextViewUtils.setTextOrEmpty(tvTwo, bean.good_name);
                TextViewUtils.setTextOrEmpty(tvFive, bean.asset_type_name);
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(bean, position);
                        }
                    }
                });
            }else {
                TextViewUtils.setTextOrEmpty(tvOne, bean.good_brand_name);
                TextViewUtils.setTextOrEmpty(tvTwo, bean.asset_type_name);
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(bean, position);
                        }
                    }
                });
            }
            TextViewUtils.setTextOrEmpty(tvThree,  StringFormatUtils.formatTwoDecimal(bean.total_pay_amount - bean.total_refund_amount));
            TextViewUtils.setTextOrEmpty(tvFour,  StringFormatUtils.formatTwoDecimal(bean.total_consume_amount));*/
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(ResultAnalysisTotal bean, int position);
    }

    class EmptyHolder extends RecyclerView.ViewHolder {

        EmptyHolder(View view) {
            super(view);
        }

        private void bindItem() {

        }
    }

}
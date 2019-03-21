package cn.yuyun.yymy.ui.store;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.lzz.utils.ResoureUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.AppointmentBean;
import cn.yuyun.yymy.http.result.ResultLevel;

/**
 * @author lzz
 * @desc
 * @date 2018-01-17
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_filter;
    private Context context;
    private List<ResultLevel> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public CardAdapter(Context context) {
        this.context = context;
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

    public void notifyDataSetChanged(List<ResultLevel> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultLevel> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    public void setSelectPos(int pos) {
        selectedPos = pos;
        notifyDataSetChanged();
    }

    private int selectedPos = -1;

    public void clearSelection(int pos) {
        selectedPos = pos;
    }

    public void notifySelectedChange(int position){
        clearSelection(position);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CardAdapter.OnMyItemClickListener onItemClickListener;
        private RelativeLayout rl;
        TextView tvTime;

        ViewHolder(View view, CardAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            tvTime = (TextView) view.findViewById(R.id.tv_name);
            rl = (RelativeLayout) itemView.findViewById(R.id.rl);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final ResultLevel bean, final int position) {
            tvTime.setText(bean.name);
            if(position == selectedPos){
                tvTime.setSelected(true);
            }else{
                tvTime.setSelected(false);
            }
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifySelectedChange(getAdapterPosition());
                    onItemClickListener.onItemClick(v, bean, position);
                }
            });
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, ResultLevel bean, int position);
    }

}
package cn.yuyun.yymy.ui.home.appointment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.lzz.utils.ResoureUtils;
import cn.yuyun.yymy.R;

/**
 * @author lzz
 * @desc
 * @date 2018-01-17
 */
public class SelectTimeAdapter extends RecyclerView.Adapter<SelectTimeAdapter.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_time;
    private Context context;
    private List<String> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public SelectTimeAdapter(Context context, List<String> list) {
        this.context = context;
        this.dataList = list;
    }

    public SelectTimeAdapter(Context context) {
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

    public void notifyDataSetChanged(List<String> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<String> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    private int selectedPos = 0;

    public void clearSelection(int pos) {
        selectedPos = pos;
    }

    public void notifySelectedChange(int position){
        clearSelection(position);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private SelectTimeAdapter.OnMyItemClickListener onItemClickListener;
        private RelativeLayout rl;
        private TextView tvTime;

        ViewHolder(View view, SelectTimeAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            tvTime = (TextView) view.findViewById(R.id.tv_name);
            rl = (RelativeLayout) itemView.findViewById(R.id.rl);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final String bean, final int position) {
            tvTime.setText(bean);

            //动态添加上午和下午中间的间隔
            if(position == 8 || position == 9){
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) rl.getLayoutParams();
                lp.setMargins(0, 20, 0, 0);
                rl.setLayoutParams(lp);
            }

            if (selectedPos == position) {
                tvTime.setSelected(true);
            }else{
                tvTime.setSelected(false);
            }
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        notifySelectedChange(getAdapterPosition());
                        onItemClickListener.onItemClick(v, bean, position);
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, String bean, int position);
    }

}
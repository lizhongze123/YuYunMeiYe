package cn.yuyun.yymy.ui.home.appointment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.lzz.utils.ResoureUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.ui.home.notice.NoticeBean;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author lzz
 * @desc
 * @date 2018-01-17
 */
public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_time;
    private Context context;
    private List<String> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;
    private int type;

    public TimeAdapter(Context context, List<String> list) {
        this.context = context;
        this.dataList = list;
    }

    public TimeAdapter(Context context, int type) {
        this.context = context;
        this.type = type;
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

    class ViewHolder extends RecyclerView.ViewHolder {

        private TimeAdapter.OnMyItemClickListener onItemClickListener;
        private RelativeLayout rl;
        TextView tvTime;

        ViewHolder(View view, TimeAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            tvTime = (TextView) view.findViewById(R.id.tv_name);
            rl = (RelativeLayout) itemView.findViewById(R.id.rl);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final String bean, final int position) {
            if (type == 0) {
                tvTime.setTextColor(ResoureUtils.getColor(context, R.color.text_green));
            } if(type == 1){
                tvTime.setTextColor(ResoureUtils.getColor(context, R.color.text_red));
            }
            tvTime.setText(bean);
            if (onItemClickListener != null) {
                rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(v, bean, position);
                    }
                });
            }

        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, String bean, int position);
    }

}
package cn.yuyun.yymy.ui.home.appointment;

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

/**
 * @author lzz
 * @desc
 * @date 2018-01-17
 */
public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_time;
    private Context context;
    private List<AppointmentBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public AppointmentAdapter(Context context) {
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

    public void notifyDataSetChanged(List<AppointmentBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<AppointmentBean> dataList) {
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

        private AppointmentAdapter.OnMyItemClickListener onItemClickListener;
        private RelativeLayout rl;
        TextView tvTime;

        ViewHolder(View view, AppointmentAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            tvTime = (TextView) view.findViewById(R.id.tv_name);
            rl = (RelativeLayout) itemView.findViewById(R.id.rl);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final AppointmentBean bean, final int position) {
           /* String start = null,end = null;
            if(!TextUtils.isEmpty(bean.startTime)){
                start = (bean.startTime).substring(0, bean.startTime.length() - 3);
            }
            if(!TextUtils.isEmpty(bean.endTime)){
                end = (bean.endTime).substring(0, bean.endTime.length() - 3);
            }
            tvTime.setText(start + "~" + end);
            tvTime.setTextColor(ResoureUtils.getColor(context, bean.status.resId));
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v, bean, position);
                }
            });*/
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, AppointmentBean bean, int position);
    }

}
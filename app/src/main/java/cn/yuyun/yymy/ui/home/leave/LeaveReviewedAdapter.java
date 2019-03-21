package cn.yuyun.yymy.ui.home.leave;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.bean.RecordType;

/**
 * @author lzz
 * @desc
 * @date 2018-01-25
 */
public class LeaveReviewedAdapter extends RecyclerView.Adapter<LeaveReviewedAdapter.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_reviewed;
    private Context mContext;
    private List<ApprovePeopleBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;
    RecordType type;

    public LeaveReviewedAdapter(Context context, List<ApprovePeopleBean> list, RecordType type){
        this.mContext = context;
        this.dataList = list;
        this.type = type;
    }

    public LeaveReviewedAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(RESOURCE_ID, parent, false);
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

    public void notifyDataSetChanged(List<ApprovePeopleBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ApprovePeopleBean> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private LeaveReviewedAdapter.OnMyItemClickListener onItemClickListener;
        private RelativeLayout rl;

        ViewHolder(View view,  LeaveReviewedAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            this.onItemClickListener = onItemClickListener;
            rl = (RelativeLayout) itemView.findViewById(R.id.rl);
        }

        private void bindItem(final ApprovePeopleBean bean, final int position) {
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v,bean, position);
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, ApprovePeopleBean bean, int position);
    }

}
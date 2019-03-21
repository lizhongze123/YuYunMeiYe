package cn.yuyun.yymy.ui.store.staff;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.ui.store.attendance.AttendanceStaff;

/**
 * @author
 * @desc
 * @date
 */
public class AttendanceStoreStaffChildAdapter extends RecyclerView.Adapter<AttendanceStoreStaffChildAdapter.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_store_staff_attandaance_child;
    private Context mContext;
    private List<String> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public AttendanceStoreStaffChildAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public AttendanceStoreStaffChildAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

        public TextView tv_name;

        ViewHolder(View v, OnMyItemClickListener onItemClickListener) {
            super(v);
            tv_name = (TextView) v.findViewById(R.id.tv_name);
        }

        private void bindItem(final String bean, final int position) {
            tv_name.setText(bean);
        }
    }



    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, AttendanceStaff.AttendanceStaffBean bean, int position);
    }

}

package cn.yuyun.yymy.ui.store.staff;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
public class AttendanceStoreStaffParentAdapter extends RecyclerView.Adapter<AttendanceStoreStaffParentAdapter.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_store_staff_attandaance_parent;
    private Context mContext;
    private List<AttendanceStoreStaff> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public AttendanceStoreStaffParentAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public AttendanceStoreStaffParentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

    public void notifyDataSetChanged(List<AttendanceStoreStaff> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<AttendanceStoreStaff> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout rl;
        public TextView tv_status;
        public TextView tv_number;
        public ImageView iv_expand;
        public RecyclerView recyclerView;
        public AttendanceStoreStaffChildAdapter adapter;

        ViewHolder(View v, OnMyItemClickListener onItemClickListener) {
            super(v);
            rl = (RelativeLayout) v.findViewById(R.id.rl);
            tv_status = (TextView) v.findViewById(R.id.tv_status);
            tv_number = (TextView) v.findViewById(R.id.tv_number);
            iv_expand = (ImageView) v.findViewById(R.id.iv_expand);
            recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
            adapter = new AttendanceStoreStaffChildAdapter(mContext);
        }

        private void bindItem(final AttendanceStoreStaff bean, final int position) {
            tv_status.setText(bean.desc);
            if(position == 0 || position == 1){
                tv_number.setText(bean.number + "天");
            }else{
                tv_number.setText(bean.number + "次");
            }

            if(bean.number == 0){
                tv_number.setTextColor(Color.parseColor("#CCCCCC"));
            }else{
                tv_number.setTextColor(Color.parseColor("#274335"));
            }

            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerView.getVisibility() == View.VISIBLE){
                        recyclerView.setVisibility(View.GONE);
                    }else{
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(mContext){
                @Override
                public boolean canScrollVertically() {
                    return false;
                }

                @Override
                public boolean canScrollHorizontally() {
                    return false;
                }
            });
            recyclerView.setFocusableInTouchMode(false);
            recyclerView.requestFocus();
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged(bean.attendanceStaffBeanList);
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, AttendanceStaff bean, int position);
    }

}

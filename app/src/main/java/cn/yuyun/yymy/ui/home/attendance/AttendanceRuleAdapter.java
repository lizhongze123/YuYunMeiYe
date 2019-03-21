package cn.yuyun.yymy.ui.home.attendance;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.RulesBean;

/**
 * @author lzz
 * @desc
 * @date 2018-01-25
 */
public class AttendanceRuleAdapter extends RecyclerView.Adapter<AttendanceRuleAdapter.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_attendance_rule;
    private Context mContext;
    private List<RulesBean> parentList;
    OnMyItemClickListener onItemClickListener;

    public AttendanceRuleAdapter(Context context, List<RulesBean> parentList){
        this.mContext = context;
        this.parentList = parentList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(RESOURCE_ID, parent, false);
        return new ViewHolder(rootView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindItem(parentList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return parentList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private AttendanceRuleAdapter.OnMyItemClickListener onItemClickListener;
        private RecyclerView rv;
        StorePicAdapter storePicAdapter;
        GridLayoutManager mLayoutManager;
        TextView tvName, tvStartTime, tvEndTime, tvAddress, tvAdd;
        ImageView ivDel;
        private List<RulesBean.AttendanceOgListBean> childList = new ArrayList<>();

        ViewHolder(View view,  AttendanceRuleAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            this.onItemClickListener = onItemClickListener;
            rv = (RecyclerView) view.findViewById(R.id.recyclerView);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvStartTime = (TextView) view.findViewById(R.id.tv_startTime);
            tvEndTime = (TextView) view.findViewById(R.id.tv_endTime);
            tvAddress = (TextView) view.findViewById(R.id.tv_address);
            tvAdd = (TextView) view.findViewById(R.id.tv_add);
            ivDel = (ImageView) view.findViewById(R.id.iv_del);
            storePicAdapter = new StorePicAdapter(mContext, childList);
            mLayoutManager = new GridLayoutManager(mContext, 4);
            rv.setLayoutManager(mLayoutManager);
            rv.setAdapter(storePicAdapter);
        }

        private void bindItem(final RulesBean rulesBean, final int parentPos) {
            TextViewUtils.setTextOrEmpty(tvName, rulesBean.name);
            tvName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            tvName.setSingleLine(true);
            tvName.setSelected(true);
            tvName.setFocusable(true);
            tvName.setFocusableInTouchMode(true);
            if(rulesBean.startHour.length() != 2){
                rulesBean.startHour = "0" + rulesBean.startHour;
            }
            if(rulesBean.endHour.length() != 2){
                rulesBean.endHour = "0" + rulesBean.endHour;
            }
            if(rulesBean.startMin.length() != 2){
                rulesBean.startMin = "0" + rulesBean.startMin;
            }
            if(rulesBean.endMin.length() != 2){
                rulesBean.endMin = "0" + rulesBean.endMin;
            }
            TextViewUtils.setTextOrEmpty(tvStartTime, rulesBean.startHour + ":" + rulesBean.startMin);
            TextViewUtils.setTextOrEmpty(tvEndTime, rulesBean.endHour + ":" + rulesBean.endMin);
            TextViewUtils.setTextOrEmpty(tvAddress, rulesBean.place);
            if(rulesBean.attendanceOgList != null){
                childList.clear();
                childList.addAll(rulesBean.attendanceOgList);
                storePicAdapter.notifyDataSetChanged();
            }
            ivDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onDelItem(rulesBean, parentPos);
                }
            });
            storePicAdapter.setOnItemClickListener(new StorePicAdapter.OnMyItemClickListener() {
                @Override
                public void onItemClick(RulesBean rulesBean2, RulesBean.AttendanceOgListBean bean, int childPos) {
                    onItemClickListener.onUnbind(rulesBean, bean, childPos, ViewHolder.this);

                }
            });
            tvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onAdd(rulesBean, parentPos);
                }
            });
        }
    }

    public void unbindSuccess(ViewHolder holder,int childPos){
        holder.childList.remove(childPos);
        holder.storePicAdapter.notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, RulesBean bean, int position);
        void onDelItem(RulesBean bean, int position);
        void onUnbind(RulesBean rulesBean, RulesBean.AttendanceOgListBean bean, int childPos, ViewHolder holder);
        void onAdd(RulesBean bean, int parentPos);
    }

}
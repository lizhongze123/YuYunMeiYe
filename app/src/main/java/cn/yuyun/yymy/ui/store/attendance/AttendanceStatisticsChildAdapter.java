package cn.yuyun.yymy.ui.store.attendance;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultUnboxingBean;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author
 * @desc
 * @date
 */
public class AttendanceStatisticsChildAdapter extends RecyclerView.Adapter<AttendanceStatisticsChildAdapter.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_attendance_child;
    private Context mContext;
    private List<AttendanceStaff.AttendanceStaffBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public AttendanceStatisticsChildAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public AttendanceStatisticsChildAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

    public void notifyDataSetChanged(List<AttendanceStaff.AttendanceStaffBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<AttendanceStaff.AttendanceStaffBean> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_name;
        public ImageView iv_avatar;

        ViewHolder(View v, OnMyItemClickListener onItemClickListener) {
            super(v);
            tv_name = (TextView) v.findViewById(R.id.tv_name);
            iv_avatar = (ImageView) v.findViewById(R.id.iv_avatar);
        }

        private void bindItem(final AttendanceStaff.AttendanceStaffBean bean, final int position) {

            tv_name.setText(bean.name);
            if(!TextUtils.isEmpty(bean.avatar)){
                if(bean.avatar.startsWith(mContext.getString(R.string.HTTP))){
                    GlideHelper.displayImage(mContext, bean.avatar, iv_avatar);
                }else{
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.avatar, iv_avatar);
                }
            }else{
                GlideHelper.displayImage(mContext, R.drawable.avatar_default_female, iv_avatar);
            }

        }
    }



    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, AttendanceStaff.AttendanceStaffBean bean, int position);
    }

}

package cn.yuyun.yymy.ui.store.attendance;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultAttendanceStatistics;
import cn.yuyun.yymy.utils.GlideHelper;
import de.hdodenhof.circleimageview.CircleImageView;
import cn.yuyun.yymy.ui.store.attendance.RequestAttendanceStatisticsDetail.AttendanceType;

/**
 * @author
 * @desc
 * @date
 */
public class AttendanceStatisticsAdapter extends RecyclerView.Adapter<AttendanceStatisticsAdapter.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_attendance_detail;
    private Context mContext;
    private List<ResultAttendanceStatistics.WorkAttendanceRecordByDateVosBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;
    public AttendanceType type;

    public AttendanceStatisticsAdapter(Context context, AttendanceType type) {
        this.mContext = context;
        this.type = type;
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

    public void notifyDataSetChanged(List<ResultAttendanceStatistics.WorkAttendanceRecordByDateVosBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultAttendanceStatistics.WorkAttendanceRecordByDateVosBean> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_avatar)
        CircleImageView ivAvatar;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_store)
        TextView tvStore;
        @BindView(R.id.rl_name)
        RelativeLayout rlName;
        @BindView(R.id.tv_version)
        TextView tvVersion;
        @BindView(R.id.rl)
        RelativeLayout rl;
        private AttendanceStatisticsAdapter.OnMyItemClickListener onItemClickListener;

        ViewHolder(View v, OnMyItemClickListener onItemClickListener) {
            super(v);
            ButterKnife.bind(this, v);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final ResultAttendanceStatistics.WorkAttendanceRecordByDateVosBean bean, final int position) {
            if(null != onItemClickListener){
                rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(bean, position);
                    }
                });
            }
            if(!TextUtils.isEmpty(bean.create_person_avatar)){
                if(bean.create_person_avatar.startsWith(mContext.getString(R.string.HTTP))){
                    GlideHelper.displayImage(mContext, bean.create_person_avatar, ivAvatar);
                }else{
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.create_person_avatar, ivAvatar);
                }
            }else{
                GlideHelper.displayImage(mContext, R.drawable.avatar_default_female, ivAvatar);
            }
            TextViewUtils.setTextOrEmpty(tvName, bean.create_person_name);
            TextViewUtils.setTextOrEmpty(tvStore, "考勤组:" + bean.rule_name);
            //1.出勤人数 2.迟到 3.早退 4.旷工 5.全勤 6.请假 7.外勤
            if(type == AttendanceType.WORK){
                TextViewUtils.setTextOrEmpty(tvVersion, bean.ycqts_count + "次");
            }else if(type == AttendanceType.LATE){
                TextViewUtils.setTextOrEmpty(tvVersion, bean.lateCount + "次");
            }else if(type == AttendanceType.LEAVE_EARLY){
                TextViewUtils.setTextOrEmpty(tvVersion, bean.earlyCount + "次");
            }else if(type == AttendanceType.ABSENCE){
                TextViewUtils.setTextOrEmpty(tvVersion, bean.neglect_work_count + "次");
            }else if(type == AttendanceType.WORK_OUT){
                TextViewUtils.setTextOrEmpty(tvVersion, bean.out_work_count + "次");
            }

        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(ResultAttendanceStatistics.WorkAttendanceRecordByDateVosBean bean, int position);
    }

}

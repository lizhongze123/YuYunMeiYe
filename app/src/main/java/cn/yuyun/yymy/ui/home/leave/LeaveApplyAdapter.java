package cn.yuyun.yymy.ui.home.leave;

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
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.bean.RecordType;
import cn.yuyun.yymy.http.result.LeaveBean;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author
 * @desc
 * @date
 */
public class LeaveApplyAdapter extends RecyclerView.Adapter<LeaveApplyAdapter.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_apply;
    private Context mContext;
    private List<LeaveBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;
    RecordType type;

    public LeaveApplyAdapter(Context context, List<LeaveBean> list, RecordType type) {
        this.mContext = context;
        this.dataList = list;
        this.type = type;
    }

    public LeaveApplyAdapter(Context context) {
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

    public void notifyDataSetChanged(List<LeaveBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<LeaveBean> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private OnMyItemClickListener onItemClickListener;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_day)
        TextView tvDay;
        @BindView(R.id.tv_start)
        TextView tvStart;
        @BindView(R.id.tv_end)
        TextView tvEnd;
        @BindView(R.id.tv_position)
        TextView tvPosition;
        @BindView(R.id.iv_status)
        ImageView ivStatus;
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.ll)
        RelativeLayout ll;

        ViewHolder(View view, OnMyItemClickListener onItemClickListener) {
            super(view);
            this.onItemClickListener = onItemClickListener;
            ButterKnife.bind(this, view);
        }

        private void bindItem(final LeaveBean bean, final int position) {
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, bean, position);
                    }
                }
            });
            if (!TextUtils.isEmpty(bean.createPersonAvatar)) {
                if (bean.createPersonAvatar.startsWith(mContext.getString(R.string.HTTP))) {
                    GlideHelper.displayImage(mContext, bean.createPersonAvatar, ivAvatar, R.drawable.avatar_default_female);
                } else {
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.createPersonAvatar, ivAvatar, R.drawable.avatar_default_female);
                }
            } else {
                GlideHelper.displayImage(mContext, R.drawable.avatar_default_female, ivAvatar);
            }
            TextViewUtils.setTextOrEmpty(tvType, bean.reason.desc);
            TextViewUtils.setTextOrEmpty(tvDay, bean.timesLong + "天");
            TextViewUtils.setTextOrEmpty(tvName, bean.createPersonName);
            TextViewUtils.setTextOrEmpty(tvTime, DateTimeUtils.StringToDate(bean.create_time, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_DATE_UI_TWO));
            TextViewUtils.setTextOrEmpty(tvStart, DateTimeUtils.StringToDate(bean.startTime, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_DATETIME_TWO));
            TextViewUtils.setTextOrEmpty(tvEnd, DateTimeUtils.StringToDate(bean.endTime, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_DATETIME_TWO));
            TextViewUtils.setTextOrEmpty(tvPosition, bean.createPersonPosition);
            ivStatus.setImageDrawable(ResoureUtils.getDrawable(mContext, bean.approveStatus.bgId));
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, LeaveBean bean, int position);
    }

}
package cn.yuyun.yymy.ui.store.staff;

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
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultBook;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.RoundAngleImageView;

/**
 * @author lzz
 * @desc
 * @date 2018-01-17
 */
public class AppointmentForStaffAdapter extends RecyclerView.Adapter<AppointmentForStaffAdapter.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_appointment;
    private Context mContext;
    private List<ResultBook> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public AppointmentForStaffAdapter(Context context) {
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

    public void notifyDataSetChanged(List<ResultBook> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultBook> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_avatar)
        RoundAngleImageView ivAvatar;
        @BindView(R.id.tv_store)
        TextView tvStore;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.rl_name)
        RelativeLayout rlName;
        @BindView(R.id.tv_serviceTime)
        TextView tvServiceTime;
        @BindView(R.id.tv_service)
        TextView tvService;
        @BindView(R.id.tv_servicePerson)
        TextView tvServicePerson;
        @BindView(R.id.iv_status)
        ImageView ivStatus;
        @BindView(R.id.rl)
        RelativeLayout rl;
        private OnMyItemClickListener onItemClickListener;

        ViewHolder(View view, OnMyItemClickListener onItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final ResultBook bean, final int position) {
            if (!TextUtils.isEmpty(bean.memberAvatar)) {
                if (bean.memberAvatar.startsWith(mContext.getString(R.string.HTTP))) {
                    GlideHelper.displayImage(mContext, bean.memberAvatar, ivAvatar, R.drawable.avatar_default_female);
                } else {
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.memberAvatar, ivAvatar, R.drawable.avatar_default_female);
                }
            } else {
                GlideHelper.displayImage(mContext, R.drawable.avatar_default_female, ivAvatar);
            }
            TextViewUtils.setTextOrEmpty(tvStore, bean.spName);
            ivStatus.setImageDrawable(ResoureUtils.getDrawable(mContext, bean.status.imgId));
            TextViewUtils.setTextOrEmpty(tvService, "服务项目:" + bean.serviceName);
            tvServicePerson.setVisibility(View.GONE);
            TextViewUtils.setTextOrEmpty(tvServiceTime, "服务时间:" + bean.date + " " + bean.startTime + "~" + bean.endTime);
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
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
        void onItemClick(View view, ResultBook bean, int position);
    }

}
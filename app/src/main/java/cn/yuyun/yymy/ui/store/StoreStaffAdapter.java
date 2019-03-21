package cn.yuyun.yymy.ui.store;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.StringUtil;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.StaffBean;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author
 * @desc
 * @date
 */
public class StoreStaffAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_contact;
    private Context mContext;
    private List<StaffBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public StoreStaffAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(RESOURCE_ID, parent, false);
        return new ViewHolder(rootView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.bindItem(dataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public StaffBean getItemBean(int position) {
        return dataList.get(position);
    }

    public void notifyDataSetChanged(List<StaffBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<StaffBean> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private OnMyItemClickListener onItemClickListener;

        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.iv_birthday)
        ImageView ivBirthday;
        @BindView(R.id.iv_call)
        ImageView ivCall;
        @BindView(R.id.tv_lv_item_tag)
        TextView tvTag;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_positions)
        TextView tvPosition;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_birthday)
        TextView tvBirthday;
        @BindView(R.id.ll_item)
        LinearLayout ll;

        ViewHolder(View view, OnMyItemClickListener onItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final StaffBean bean, final int position) {
            if (!TextUtils.isEmpty(bean.avatar)) {
                if (bean.avatar.startsWith(mContext.getString(R.string.HTTP))) {
                    GlideHelper.displayImage(mContext, bean.avatar, ivAvatar);
                } else {
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.avatar, ivAvatar);
                }
            } else {
                GlideHelper.displayImage(mContext, bean.sex.resId, ivAvatar);
            }
            ivCall.setVisibility(View.GONE);
            tvTag.setVisibility(View.GONE);
            TextViewUtils.setTextOrEmpty(tvName, bean.staffName);
            TextViewUtils.setTextOrEmpty(tvTime, bean.entryTime);
            if (StringUtil.isEmpty(bean.positionName)) {
                tvPosition.setVisibility(View.GONE);
            } else {
                tvPosition.setVisibility(View.VISIBLE);
                TextViewUtils.setTextOrEmpty(tvPosition, "(" + bean.positionName + ")");
            }
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onItemClickListener) {
                        onItemClickListener.onItemClick(v, bean, position);
                    }
                }
            });
            if (null == bean.birthDayTillInfoRsp) {
                tvBirthday.setVisibility(View.GONE);
                ivBirthday.setVisibility(View.GONE);
            } else {
                int warnningDay = Integer.valueOf(bean.birthDayTillInfoRsp.birth_date_till_days);
                if (warnningDay >= 0 && warnningDay <= 7) {
                    tvBirthday.setText("距离生日还剩" + warnningDay + "天");
                    tvBirthday.setVisibility(View.VISIBLE);
                    ivBirthday.setVisibility(View.VISIBLE);
                }  else {
                    tvBirthday.setVisibility(View.GONE);
                    ivBirthday.setVisibility(View.GONE);
                }
            }
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, StaffBean bean, int position);
    }

}

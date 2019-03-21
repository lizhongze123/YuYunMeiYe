package cn.yuyun.yymy.ui.store.staff;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.StaffBean;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author lzz
 * @desc
 * @date
 */
public class RecycledStaffAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_contact;
    private Context mContext;
    private List<StaffBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public RecycledStaffAdapter(Context context) {
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

    public StaffBean getItemBean(int position) {
        return dataList.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private OnMyItemClickListener onItemClickListener;

        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
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
        @BindView(R.id.ll_item)
        LinearLayout ll;
        @BindView(R.id.rl)
        RelativeLayout rl;

        ViewHolder(View view, OnMyItemClickListener onItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final StaffBean bean, final int position) {
            rl.setBackgroundColor(ResoureUtils.getColor(mContext, R.color.white));
            if(!TextUtils.isEmpty(bean.avatar)){
                if(bean.avatar.startsWith(mContext.getString(R.string.HTTP))){
                    GlideHelper.displayImage(mContext, bean.avatar, ivAvatar);
                }else{
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.avatar, ivAvatar);
                }
            }else{
                GlideHelper.displayImage(mContext, bean.sex.resId, ivAvatar);
            }
            ivCall.setVisibility(View.GONE);
            tvTag.setVisibility(View.GONE);
            TextViewUtils.setTextOrEmpty(tvName, bean.staffName);
            TextViewUtils.setTextOrEmpty(tvTime, bean.entryTime);
            if(TextUtils.isEmpty(bean.positionName)){
                tvPosition.setVisibility(View.GONE);
            }else{
                tvPosition.setVisibility(View.VISIBLE);
                TextViewUtils.setTextOrEmpty(tvPosition, "(" + bean.positionName + ")");
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
package cn.yuyun.yymy.ui.store.member;

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

import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author lzz
 * @desc
 * @date 2018-01-17
 */
public class RecycledMemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_recycled_member;
    private Context mContext;
    private List<ResultMemberBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public RecycledMemberAdapter(Context context) {
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

    public void notifyDataSetChanged(List<ResultMemberBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultMemberBean> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    public ResultMemberBean getItemBean(int position) {
        return dataList.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private OnMyItemClickListener onItemClickListener;
        private ImageView ivAvatar;
        private TextView tvUserName, tvVip, tvTime, tvTag;

        ViewHolder(View view, OnMyItemClickListener onItemClickListener) {
            super(view);
            this.onItemClickListener = onItemClickListener;
            ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
            tvUserName = (TextView) view.findViewById(R.id.tv_userName);
            tvVip = (TextView) view.findViewById(R.id.tv_vip);
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            tvTag = (TextView) view.findViewById(R.id.tv_lv_item_tag);
        }

        private void bindItem(final ResultMemberBean bean, final int position) {
            tvTag.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(bean.memberAvatar)) {
                if (bean.memberAvatar.startsWith(mContext.getString(R.string.HTTP))) {
                    GlideHelper.displayImage(mContext, bean.memberAvatar, ivAvatar);
                } else {
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.memberAvatar, ivAvatar);
                }
            } else {
                GlideHelper.displayImage(mContext, bean.member_sex.resId, ivAvatar);
            }
            TextViewUtils.setTextOrEmpty(tvUserName, bean.memberName);
            if(TextUtils.isEmpty(bean.memberLevelName)){
                tvVip.setVisibility(View.GONE);
            }else{
                tvVip.setVisibility(View.VISIBLE);
                TextViewUtils.setTextOrEmpty(tvVip, "(" + bean.memberLevelName + ")");
            }
            TextViewUtils.setTextOrEmpty(tvTime, StringFormatUtils.getUserDate(bean.memberConsumptionLatestTime));

        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, ResultMemberBean bean, int position);
    }

}
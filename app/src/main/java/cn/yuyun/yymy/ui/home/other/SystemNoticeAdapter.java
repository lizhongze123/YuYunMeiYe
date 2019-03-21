package cn.yuyun.yymy.ui.home.other;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import cn.yuyun.yymy.http.result.ResultSystemNotice;

/**
 * @author
 * @desc
 * @date
 */
public class SystemNoticeAdapter extends RecyclerView.Adapter<SystemNoticeAdapter.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_system_notice;
    private Context mContext;
    private List<ResultSystemNotice> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public SystemNoticeAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(RESOURCE_ID, parent, false), onItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ViewHolder headerViewHolder = holder;
        headerViewHolder.bindItem(dataList.get(position), position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        holder.bindItem(dataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void notifyDataSetChanged(List<ResultSystemNotice> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultSystemNotice> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.rl)
        RelativeLayout rl;
        private OnMyItemClickListener onItemClickListener;
        int override;

        ViewHolder(View view, OnMyItemClickListener onItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.onItemClickListener = onItemClickListener;
            override = ResoureUtils.getDimension(mContext, R.dimen.item_pic);
        }

        private void bindItem(final ResultSystemNotice bean, final int position) {

            if(bean.readFlag){
                tvTitle.setTextColor(ResoureUtils.getColor(mContext, R.color.read));
                tvTime.setTextColor(ResoureUtils.getColor(mContext, R.color.read));
            }else{
                tvTitle.setTextColor(ResoureUtils.getColor(mContext, R.color.unread));
                tvTime.setTextColor(ResoureUtils.getColor(mContext, R.color.unread));
            }

            TextViewUtils.setTextOrEmpty(tvTitle, bean.title);
            TextViewUtils.setTextOrEmpty(tvTime, bean.createTime);
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != onItemClickListener){
                        onItemClickListener.onItemClick(bean, position);
                    }
                }
            });
        }
    }

    public interface OnMyItemClickListener {
        void onItemClick(ResultSystemNotice bean, int position);
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


}
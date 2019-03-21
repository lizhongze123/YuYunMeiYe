package cn.yuyun.yymy.ui.home.work;

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

import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultWork;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/2/25
 */

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_moments_notice;
    private Context mContext;
    private List<ResultWork> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public NoticeAdapter(Context context){
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

    public void notifyDataSetChanged(List<ResultWork> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultWork> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private NoticeAdapter.OnMyItemClickListener onItemClickListener;
        private RelativeLayout rl;
        ImageView ivAvatar;
        TextView tvName;
        TextView tvTime;
        ImageView ivMoments;
        TextView tvContent;
        int overrideSize;

        ViewHolder(View view,  NoticeAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            rl = (RelativeLayout) view.findViewById(R.id.rl);
            ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
            ivMoments = (ImageView) view.findViewById(R.id.iv_moments);
            tvContent = (TextView) view.findViewById(R.id.tv_content);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            this.onItemClickListener = onItemClickListener;
            overrideSize = ResoureUtils.getDimension(mContext, R.dimen.work_notice_avatar_size);
        }

        private void bindItem(final ResultWork bean, final int position) {
            /*TextViewUtils.setTextOrEmpty(tvContent, bean.content);
            TextViewUtils.setTextOrEmpty(tvName, bean.staffName);
            TextViewUtils.setTextOrEmpty(tvTime, bean.createTime);
            GlideHelper.displayImage(mContext, bean.staffAvatar, overrideSize, ivAvatar);

            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, bean, position);
                    }
                }
            });*/
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    interface OnMyItemClickListener {
        void onItemClick(View view, ResultWork bean, int position);
    }

}

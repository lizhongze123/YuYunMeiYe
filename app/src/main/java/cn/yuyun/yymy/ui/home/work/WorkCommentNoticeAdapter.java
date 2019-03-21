package cn.yuyun.yymy.ui.home.work;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ActionBean;
import cn.yuyun.yymy.http.result.ResultWorkCommentNotice;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.RoundAngleImageView;

/**
 * @author lzz
 * @desc
 * @date
 */
public class WorkCommentNoticeAdapter extends RecyclerView.Adapter<WorkCommentNoticeAdapter.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_moments_comment;
    private Context mContext;
    private List<ResultWorkCommentNotice> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public WorkCommentNoticeAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(RESOURCE_ID, parent, false), onItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.bindItem(dataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void notifyDataSetChanged(List<ResultWorkCommentNotice> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultWorkCommentNotice> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    public void remove(int pos) {
        this.dataList.remove(pos);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_avatar)
        RoundAngleImageView ivAvatar;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_commentTime)
        TextView tvCommentTime;
        @BindView(R.id.tv_comment)
        TextView tvComment;
        @BindView(R.id.tv_workTitle)
        TextView tvWorkTitle;
        @BindView(R.id.tv_workTime)
        TextView tvWorkTime;
        @BindView(R.id.tv_workContent)
        TextView tvWorkContent;
        private OnMyItemClickListener onItemClickListener;
        int override;

        ViewHolder(View view, OnMyItemClickListener onItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.onItemClickListener = onItemClickListener;
            override = ResoureUtils.getDimension(mContext, R.dimen.item_pic);
        }

        private void bindItem(final ResultWorkCommentNotice bean, final int position) {
            TextViewUtils.setTextOrEmpty(tvName, bean.staff_name);
            TextViewUtils.setTextOrEmpty(tvComment, bean.content);
            TextViewUtils.setTextOrEmpty(tvWorkTitle, bean.report.staff_name + "的工作汇报");
            TextViewUtils.setTextOrEmpty(tvWorkTime, bean.report.createTime);
            TextViewUtils.setTextOrEmpty(tvCommentTime, bean.create_time);
            TextViewUtils.setTextOrEmpty(tvWorkContent, bean.report.content);
            if (!TextUtils.isEmpty(bean.staff_avatar)) {
                if (bean.staff_avatar.startsWith(mContext.getString(R.string.HTTP))) {
                    GlideHelper.displayImage(mContext, bean.staff_avatar, ivAvatar);
                } else {
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.staff_avatar, ivAvatar);
                }
            }

        }
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, ActionBean bean, int position);
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
package cn.yuyun.yymy.ui.home.work;

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

import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultWork;
import cn.yuyun.yymy.http.result.ResultWorkComment;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author
 * @desc
 * @date
 */
public class WorkCommentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int VIEW_TYPE = -1;
    private int RESOURCE_ID = R.layout.item_unboxing_comment;
    private int RESOURCE_ID_EMPTY = R.layout.item_comment_list_empty;
    private Context mContext;
    private List<ResultWork.WorkReportApproveVosBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public WorkCommentListAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == -1) {
            View emptyView = LayoutInflater.from(mContext).inflate(RESOURCE_ID_EMPTY, parent, false);
            return new EmptyHolder(emptyView);
        }else{
            View rootView = LayoutInflater.from(mContext).inflate(RESOURCE_ID, parent, false);
            return new ViewHolder(rootView, onItemClickListener);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.bindItem(dataList.get(position), position);
        }else{
            EmptyHolder emptyHolder = (EmptyHolder) holder;
            emptyHolder.bindItem();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(dataList.size() == 0){
            return VIEW_TYPE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return dataList.size() > 0 ? dataList.size() : 1;
    }

    public void notifyDataSetChanged(List<ResultWork.WorkReportApproveVosBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultWork.WorkReportApproveVosBean> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private OnMyItemClickListener onItemClickListener;

        private ImageView profile_img, ivLikeDesc;
        private TextView profile_name, tv_content, profile_time;
        private int overrideSize;

        ViewHolder(View view,  WorkCommentListAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            this.onItemClickListener = onItemClickListener;
            profile_img = (ImageView) view.findViewById(R.id.profile_img);
            ivLikeDesc = (ImageView) view.findViewById(R.id.iv_likeDesc);
            profile_name = (TextView) view.findViewById(R.id.profile_name);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            profile_time = (TextView) view.findViewById(R.id.profile_time);
            overrideSize = ResoureUtils.getDimension(mContext, R.dimen.item_approve_avatar_size);
        }

        private void bindItem(final ResultWork.WorkReportApproveVosBean bean, final int position) {
            if(position == 0){
                ivLikeDesc.setVisibility(View.VISIBLE);
            }else{
                ivLikeDesc.setVisibility(View.INVISIBLE);
            }
            TextViewUtils.setTextOrEmpty(profile_name, bean.createPersonName);
            TextViewUtils.setTextOrEmpty(tv_content, bean.content);
            TextViewUtils.setTextOrEmpty(profile_time, DateTimeUtils.StringToDate(bean.modifyTime, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_DATE_UI_TWO));
            if(!TextUtils.isEmpty(bean.createPersonAvatar)){
                if (bean.createPersonAvatar.startsWith(mContext.getString(R.string.HTTP))) {
                    GlideHelper.displayImage(mContext, bean.createPersonAvatar, overrideSize, profile_img);
                }else {
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.createPersonAvatar, overrideSize, profile_img);
                }
            }else{
                GlideHelper.displayImage(mContext, R.drawable.avatar_default_female, overrideSize, profile_img);
            }
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, ResultWorkComment bean, int position);
    }


    class EmptyHolder extends RecyclerView.ViewHolder {

        EmptyHolder(View view) {
            super(view);
        }

        private void bindItem() {

        }
    }

}
package cn.yuyun.yymy.ui.home.leave;

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

import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author
 * @desc
 * @date
 */
public class ApplyPeopleAdapter extends RecyclerView.Adapter<ApplyPeopleAdapter.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_apply_people;
    private Context mContext;
    private List<ApprovePeopleBean.ApproveByLeaveIDBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public ApplyPeopleAdapter(Context context, List<ApprovePeopleBean.ApproveByLeaveIDBean> list){
        this.mContext = context;
        this.dataList = list;
    }

    public ApplyPeopleAdapter(Context context){
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

    public void notifyDataSetChanged(List<ApprovePeopleBean.ApproveByLeaveIDBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ApprovePeopleBean.ApproveByLeaveIDBean> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ApplyPeopleAdapter.OnMyItemClickListener onItemClickListener;
        private View lineTop, lineBottom;
        private ImageView ivProcess;
        private TextView tvName;
        private ImageView ivAvatar;
        private int overrideSize;

        ViewHolder(View view,  ApplyPeopleAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            this.onItemClickListener = onItemClickListener;
            lineTop = view.findViewById(R.id.lineTop);
            lineBottom = view.findViewById(R.id.lineBottom);
            ivProcess = (ImageView) view.findViewById(R.id.img_process);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
            overrideSize = ResoureUtils.getDimension(mContext, R.dimen.item_approve_avatar_size);
        }

        private void bindItem(final ApprovePeopleBean.ApproveByLeaveIDBean bean, final int position) {
            if(position == 0){
                lineTop.setVisibility(View.GONE);
            }else{
                lineTop.setVisibility(View.VISIBLE);
                lineTop.setBackgroundColor(ResoureUtils.getColor(mContext, dataList.get(position - 1).status.resId));
            }
            if(position == dataList.size() - 1){
                lineBottom.setVisibility(View.GONE);
            }else{
                lineBottom.setVisibility(View.VISIBLE);
            }
            TextViewUtils.setTextOrEmpty(tvName, bean.approve_person_name);
            ivProcess.setImageResource(bean.status.resId);
            lineBottom.setBackgroundColor(ResoureUtils.getColor(mContext, bean.status.resId));
            if(!TextUtils.isEmpty(bean.approve_person_avatar)){
                if (bean.approve_person_avatar.startsWith(mContext.getString(R.string.HTTP))) {
                    GlideHelper.displayImage(mContext, bean.approve_person_avatar, overrideSize, ivAvatar);
                }else {
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.approve_person_avatar, overrideSize, ivAvatar);
                }
            }else{
                GlideHelper.displayImage(mContext, R.drawable.avatar_default_female, overrideSize, ivAvatar);
            }
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, ApprovePeopleBean bean, int position);
    }

}
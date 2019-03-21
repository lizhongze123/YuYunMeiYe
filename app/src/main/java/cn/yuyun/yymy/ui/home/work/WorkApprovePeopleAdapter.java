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

import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultWork;
import cn.yuyun.yymy.ui.home.leave.ApprovePeopleBean;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author
 * @desc
 * @date
 */
public class WorkApprovePeopleAdapter extends RecyclerView.Adapter<WorkApprovePeopleAdapter.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_work_appovo;
    private Context mContext;
    private List<ResultWork.WorkReportApproveVosBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public WorkApprovePeopleAdapter(Context context){
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

        private WorkApprovePeopleAdapter.OnMyItemClickListener onItemClickListener;
        private TextView tvName;
        private ImageView ivAvatar;

        ViewHolder(View view,  WorkApprovePeopleAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            this.onItemClickListener = onItemClickListener;
            tvName = (TextView) view.findViewById(R.id.tv_name);
            ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
        }

        private void bindItem(final ResultWork.WorkReportApproveVosBean bean, final int position) {

            TextViewUtils.setTextOrEmpty(tvName, bean.createPersonName);
            if(!TextUtils.isEmpty(bean.createPersonAvatar)){
                if (bean.createPersonAvatar.startsWith("http")) {
                    GlideHelper.displayImage(mContext, bean.createPersonAvatar, ivAvatar);
                }else {
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.createPersonAvatar, ivAvatar);
                }
            }else{
                GlideHelper.displayImage(mContext, R.drawable.avatar_default_female, ivAvatar);
            }
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, ResultWork.WorkReportApproveVosBean bean, int position);
    }

}
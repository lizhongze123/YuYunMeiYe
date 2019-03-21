package cn.yuyun.yymy.ui.home.attendance;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import cn.yuyun.yymy.http.result.RulesBean;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author lzz
 * @desc
 * @date 2018-01-25
 */
public class StorePicAdapter extends RecyclerView.Adapter<StorePicAdapter.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_rule_store;
    private Context mContext;
    private List<RulesBean.AttendanceOgListBean> childList;
    OnMyItemClickListener onItemClickListener;

    public StorePicAdapter(Context context, List<RulesBean.AttendanceOgListBean> childList){
        this.mContext = context;
        this.childList = childList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(RESOURCE_ID, parent, false);
        return new ViewHolder(rootView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindItem(childList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return childList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private StorePicAdapter.OnMyItemClickListener onItemClickListener;

        TextView tvDel;
        TextView tvName;
        ImageView ivPic;
        int override;

        ViewHolder(View view,  StorePicAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            this.onItemClickListener = onItemClickListener;
            tvDel = (TextView) view.findViewById(R.id.item_tips);
            ivPic = (ImageView) view.findViewById(R.id.iv_avatar);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            override = ResoureUtils.getDimension(mContext, R.dimen.select_people_avatar);
        }

        private void bindItem(final RulesBean.AttendanceOgListBean bean, final int position) {
            tvDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(null, bean, position);
                }
            });
            if(bean.og_type == 1){
                TextViewUtils.setTextOrEmpty(tvName, "总部");
            }else{
                TextViewUtils.setTextOrEmpty(tvName, bean.og_name);
            }

        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(RulesBean rulesBean, RulesBean.AttendanceOgListBean bean, int position);
    }

}
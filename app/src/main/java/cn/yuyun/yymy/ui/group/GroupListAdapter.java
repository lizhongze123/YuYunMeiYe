package cn.yuyun.yymy.ui.group;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
import cn.yuyun.yymy.http.result.ResultGroup;
import cn.yuyun.yymy.http.result.ResultProject;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author lzz
 * @desc
 * @date 2018-01-17
 */
public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_group;
    private Context mContext;
    OnMyItemClickListener onItemClickListener;
    private List<ResultGroup> dataList = new ArrayList<>();
    private boolean isClickLike;

    public GroupListAdapter(Context context){
        this.mContext = context;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ViewHolder headerViewHolder = holder;
        headerViewHolder.bindItem(dataList.get(position), position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(RESOURCE_ID, parent, false);
        return new ViewHolder(rootView, onItemClickListener);
    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void notifyDataSetChanged(List<ResultGroup> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultGroup> dataList) {
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
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_one)
        TextView tvOne;
        @BindView(R.id.tv_two)
        TextView tvTwo;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.rl)
        RelativeLayout rl;

        private OnMyItemClickListener onItemClickListener;

        ViewHolder(View view, OnMyItemClickListener onItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final ResultGroup bean, final int position) {

            if(!TextUtils.isEmpty(bean.logo_url)){
                if(bean.logo_url.startsWith(mContext.getString(R.string.HTTP))){
                    GlideHelper.displayImage(mContext, bean.logo_url,ivAvatar);
                }else{
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.logo_url, ivAvatar, R.drawable.default_store);
                }
            }
            TextViewUtils.setTextOrEmpty(tvName, bean.name);
            TextViewUtils.setTextOrEmpty(tvOne, "负责人:" + bean.charge_man);
            TextViewUtils.setTextOrEmpty(tvTwo, "电话:" + bean.charge_mobile);
            TextViewUtils.setTextOrEmpty(tvTime, "总数:" + bean.auth_sp_numbers);

            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onItemClickListener) {
                        onItemClickListener.onItemClick(bean, position);
                    }
                }
            });
        }
    }

    public interface OnMyItemClickListener {
        void onItemClick(ResultGroup bean, int position);
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
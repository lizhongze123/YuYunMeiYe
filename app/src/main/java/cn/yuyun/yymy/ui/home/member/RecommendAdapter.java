package cn.yuyun.yymy.ui.home.member;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import cn.yuyun.yymy.bean.Sex;
import cn.yuyun.yymy.ui.me.entity.MemberBean;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author lzz
 * @desc
 * @date
 */
public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_member;
    private Context mContext;
    private List<MemberBean> dataList = new ArrayList<>();

    public RecommendAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(RESOURCE_ID, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindItem(dataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void notifyDataSetChanged(List<MemberBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<MemberBean> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivAvatar;
        TextView tvName, tvLevel, tvTime, tvTag;
        int override;

        ViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_userName);
            tvLevel = (TextView) view.findViewById(R.id.tv_vip);
            ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            tvTag = (TextView) view.findViewById(R.id.tv_lv_item_tag);
            override = ResoureUtils.getDimension(mContext,R.dimen.item_avatar_size);
        }

        private void bindItem(final MemberBean bean, final int position) {
            tvTag.setVisibility(View.GONE);
            TextViewUtils.setTextOrEmpty(tvName, bean.memberName);
            if(!TextUtils.isEmpty(bean.levelName)){
                tvLevel.setText(bean.levelName);
            }
            if(!TextUtils.isEmpty(bean.avatar)){
                if(bean.avatar.startsWith("http")){
                    GlideHelper.displayImage(mContext, bean.avatar, ivAvatar);
                }else{
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.avatar, ivAvatar);
                }
            }else{
                if(bean.sex == Sex.MALE || bean.sex == Sex.MALE2){
                    GlideHelper.displayImage(mContext, R.drawable.avatar_default_male, ivAvatar);
                }else{
                    GlideHelper.displayImage(mContext, R.drawable.avatar_default_female, ivAvatar);
                }
            }

            if(!TextUtils.isEmpty(bean.levelName)){
                tvLevel.setVisibility(View.VISIBLE);
                tvLevel.setText("(" + bean.levelName + ")");
            }else{
                tvLevel.setVisibility(View.GONE);
            }
            TextViewUtils.setTextOrEmpty(tvTime, bean.lastTime);
        }
    }

}
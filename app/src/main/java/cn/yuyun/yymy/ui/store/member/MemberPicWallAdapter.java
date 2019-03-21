package cn.yuyun.yymy.ui.store.member;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.ui.other.ViewBigImageActivity;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/9/28
 */
public class MemberPicWallAdapter extends RecyclerView.Adapter<MemberPicWallAdapter.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_member_pic_wall;
    private Context mContext;
    private List<String> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;
    private String type;

    public MemberPicWallAdapter(Context context, String type){
        this.mContext = context;
        this.type = type;
    }

    @Override
    public MemberPicWallAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

    public void notifyDataSetChanged(List<String> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<String> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView item_tips;
        public ImageView iv_avatar;
        private OnMyItemClickListener onItemClickListener;

        ViewHolder(View v, OnMyItemClickListener onItemClickListener) {
            super(v);
            item_tips = (ImageView) v.findViewById(R.id.item_tips);
            iv_avatar = (ImageView) v.findViewById(R.id.iv_avatar);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final String bean, final int position) {

            if (bean.startsWith(mContext.getString(R.string.HTTP))) {
                GlideHelper.displayImage(mContext,  bean, iv_avatar, R.color.loadding_img_bg);
            } else {
                GlideHelper.displayImage(mContext,  mContext.getString(R.string.image_url_prefix) + bean, iv_avatar, R.color.loadding_img_bg);
            }

            GlideHelper.displayImage(mContext,  bean, iv_avatar, R.color.loadding_img_bg);
            iv_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("code", position);
                    bundle.putStringArrayList("imageList", (ArrayList<String>) dataList);
                    mContext.startActivity(ViewBigImageActivity.startViewBigImageActivity(mContext, bundle));
                }
            });

            if(null == type){
                item_tips.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onDel(position);
                    }
                });
            }else{
                item_tips.setVisibility(View.GONE);
            }
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onDel(int pos);
    }

}

package cn.yuyun.yymy.ui.home.member;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.lzz.utils.StringUtil;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.WarnningMemberBean;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author
 * @desc
 * @date
 */
public class TaRecommendMemberAdapter extends RecyclerView.Adapter<TaRecommendMemberAdapter.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_member;
    private Context mContext;
    private List<WarnningMemberBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public TaRecommendMemberAdapter(Context context){
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

    public void notifyDataSetChanged(List<WarnningMemberBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<WarnningMemberBean> dataList) {
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

        private TaRecommendMemberAdapter.OnMyItemClickListener onItemClickListener;
        private RelativeLayout rl;
        private ImageView ivAvatar, ivDel;
        private TextView tvUserName, tvVip, tv_address, tvTime, tvTag;

        ViewHolder(View view,  TaRecommendMemberAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            rl = (RelativeLayout) view.findViewById(R.id.rl);
            this.onItemClickListener = onItemClickListener;
            ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
            tvUserName = (TextView) view.findViewById(R.id.tv_userName);
            tvVip = (TextView) view.findViewById(R.id.tv_vip);
            tv_address = (TextView) view.findViewById(R.id.tv_address);
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            tvTag = (TextView) view.findViewById(R.id.tv_lv_item_tag);
            ivDel = (ImageView) view.findViewById(R.id.iv_del);
        }

        private void bindItem(final WarnningMemberBean bean, final int position) {
            if(isDel){
                ivDel.setVisibility(View.VISIBLE);
                ivDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(null != onItemClickListener){
                            onItemClickListener.del(bean, position);
                        }
                    }
                });
            }else{
                ivDel.setVisibility(View.GONE);
            }

            tvTag.setVisibility(View.GONE);
            if(!TextUtils.isEmpty(bean.avatar)){
                if(bean.avatar.startsWith(mContext.getString(R.string.HTTP))){
                    GlideHelper.displayImage(mContext, bean.avatar, ivAvatar);
                }else{
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.avatar, ivAvatar);
                }
            }
            TextViewUtils.setTextOrEmpty(tvUserName, bean.name);
            if(StringUtil.isEmpty(bean.level_name)){
                tvVip.setVisibility(View.GONE);
            }else{
                tvVip.setVisibility(View.VISIBLE);
                TextViewUtils.setTextOrEmpty(tvVip, "(" + bean.level_name + ")");
            }
            TextViewUtils.setTextOrEmpty(tvTime, bean.cash_time);
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, WarnningMemberBean bean, int position);
        void del(WarnningMemberBean bean, int position);
    }

    public boolean isDel;

    public void setDelVisiable(boolean isDel){
        this.isDel = isDel;
        notifyDataSetChanged();
    }

    public boolean getDelVisiable(){
        return isDel;
    }

}
package cn.yuyun.yymy.ui.store.staff;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultCommunication;
import cn.yuyun.yymy.ui.home.unboxing.FillContent;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.RoundAngleImageView;

/**
 * @author
 * @desc
 * @date
 */
public class CommunicationForStaffAdapter extends RecyclerView.Adapter<CommunicationForStaffAdapter.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_communication;
    private Context mContext;
    private List<ResultCommunication> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public CommunicationForStaffAdapter(Context context) {
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

    public void notifyDataSetChanged(List<ResultCommunication> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultCommunication> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rl)
        RelativeLayout rl;
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_vip)
        TextView tvVip;
        @BindView(R.id.tv_store)
        TextView tvStore;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.tv_time)
        TextView tvTime;

        @BindView(R.id.rv)
        RecyclerView rv;
        private OnMyItemClickListener onItemClickListener;

        ViewHolder(View view, OnMyItemClickListener onItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final ResultCommunication bean, final int position) {
            if (null != bean.pictures) {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < bean.pictures.size(); i++) {
                    if (bean.pictures.get(i).startsWith(mContext.getString(R.string.HTTP))) {
                        list.add(bean.pictures.get(i));
                    } else {
                        list.add(mContext.getString(R.string.image_url_prefix) + bean.pictures.get(i));
                    }
                }
                rv.setVisibility(View.VISIBLE);
                FillContent.fillComunicationImgList(list, mContext, rv, true, true);
            } else {
                rv.setVisibility(View.GONE);
            }

            if(!TextUtils.isEmpty(bean.memberAvatar)){
                if(bean.memberAvatar.startsWith(mContext.getString(R.string.HTTP))){
                    GlideHelper.displayImage(mContext, bean.memberAvatar,ivAvatar);
                }else{
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.memberAvatar, ivAvatar);
                }
            }
            TextViewUtils.setTextOrEmpty(tvName, bean.memberName);
            if(!TextUtils.isEmpty(bean.memberLevelName)){
                tvVip.setVisibility(View.VISIBLE);
                TextViewUtils.setTextOrEmpty(tvVip, bean.memberLevelName);
            }else{
                tvVip.setVisibility(View.GONE);
            }
            TextViewUtils.setTextOrEmpty(tvStore, bean.spName);
            TextViewUtils.setTextOrEmpty(tvContent, bean.content);
            TextViewUtils.setTextOrEmpty(tvTime, bean.comTime);
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, bean, position);
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, ResultCommunication bean, int position);
    }

}
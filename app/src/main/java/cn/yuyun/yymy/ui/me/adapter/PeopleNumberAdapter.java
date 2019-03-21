package cn.yuyun.yymy.ui.me.adapter;

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

import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.ui.me.entity.PeopleNumberBean;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author lzz
 * @desc
 * @date 2018-01-31
 */
public class PeopleNumberAdapter extends RecyclerView.Adapter<PeopleNumberAdapter.PeopleHolder>{

    private int RESOURCE_ID = R.layout.item_people;
    private Context context;
    private List<PeopleNumberBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public PeopleNumberAdapter(Context context){
        this.context = context;
    }

    @Override
    public PeopleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(RESOURCE_ID, parent, false);
        return new PeopleHolder(rootView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(PeopleHolder holder, int position) {
        holder.bindItem(dataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void notifyDataSetChanged(List<PeopleNumberBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<PeopleNumberBean> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class PeopleHolder extends RecyclerView.ViewHolder {

        private PeopleNumberAdapter.OnMyItemClickListener onItemClickListener;
        private RelativeLayout rl;
        TextView tvUserName, tvTime, tvPrice, tvVip;
        ImageView ivAvatar;
        int overrideSize;

        PeopleHolder(View view,  PeopleNumberAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
            tvUserName = (TextView) view.findViewById(R.id.tv_userName);
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            tvPrice = (TextView) view.findViewById(R.id.tv_price);
            tvVip = (TextView) view.findViewById(R.id.tv_vip);
            rl = (RelativeLayout) itemView.findViewById(R.id.rl);
            this.onItemClickListener = onItemClickListener;
            overrideSize = (int) context.getResources().getDimension(R.dimen.item_avatar_size);
        }

        private void bindItem(final PeopleNumberBean bean, final int position) {
            tvPrice.setVisibility(View.GONE);
            if(!TextUtils.isEmpty(bean.getMember_avatar())){
                GlideHelper.displayCircleAvatar(context, context.getString(R.string.image_url_prefix) + bean.getMember_avatar(), overrideSize, ivAvatar);
            }else{
                ivAvatar.setImageResource(R.drawable.avatar_default_male);
            }
            TextViewUtils.setTextOrEmpty(tvTime, bean.getConsumption_latest_time());
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, position);
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, int position);
    }

}
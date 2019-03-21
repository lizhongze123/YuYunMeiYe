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

import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.ui.me.entity.MemberBean;
import cn.yuyun.yymy.ui.me.entity.PeopleBean;

/**
 * @author lzz
 * @desc
 * @date 2018-01-17
 */
public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberHolder>{

    private int RESOURCE_ID = R.layout.item_member;
    private Context context;
    private List<MemberBean> list;
    OnMyItemClickListener onItemClickListener;

    public MemberAdapter(Context context, List<MemberBean> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public MemberHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(RESOURCE_ID, parent, false);
        return new MemberHolder(rootView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(MemberHolder holder, int position) {
        holder.bindItem(list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MemberHolder extends RecyclerView.ViewHolder {

        private MemberAdapter.OnMyItemClickListener onItemClickListener;
        private RelativeLayout rl;
        TextView tvUserName, tvTime, tvPrice, tvVip;
        ImageView ivAvatar, ivCall;

        MemberHolder(View view,  MemberAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
            ivCall = (ImageView) view.findViewById(R.id.iv_call);
            tvUserName = (TextView) view.findViewById(R.id.tv_userName);
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            tvVip = (TextView) view.findViewById(R.id.tv_vip);
            rl = (RelativeLayout) itemView.findViewById(R.id.rl);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final MemberBean bean, final int position) {
            tvUserName.setText(bean.memberName);
            tvTime.setText(bean.lastTime);
            if(!TextUtils.isEmpty(bean.levelName)){
                tvVip.setText(bean.levelName);
            }
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
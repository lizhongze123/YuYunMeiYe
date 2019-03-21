package cn.yuyun.yymy.ui.me.adapter;

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
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.PersonTimeListBean;
import cn.yuyun.yymy.utils.GlideHelper;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author
 * @desc
 * @date
 */
public class PersonCountAdapter extends RecyclerView.Adapter<PersonCountAdapter.PeopleHolder>{

    private int RESOURCE_ID = R.layout.item_person_count;
    private Context mContext;
    private List<PersonTimeListBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public PersonCountAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public PeopleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(RESOURCE_ID, parent, false);
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

    public void notifyDataSetChanged(List<PersonTimeListBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<PersonTimeListBean> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class PeopleHolder extends RecyclerView.ViewHolder {

        private PersonCountAdapter.OnMyItemClickListener onItemClickListener;
        @BindView(R.id.rl)
        RelativeLayout rl;
        @BindView(R.id.iv_avatar)
        CircleImageView ivAvatar;
        @BindView(R.id.iv_sex)
        ImageView ivSex;
        @BindView(R.id.rl_avatar)
        RelativeLayout rlAvatar;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_level)
        TextView tvLevel;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.ll_user)
        LinearLayout llUser;
        @BindView(R.id.tv_store)
        TextView tvStore;
        @BindView(R.id.rl_name)
        RelativeLayout rlName;
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.tv_consumeTime)
        TextView tvConsumeTime;
        @BindView(R.id.tv_count)
        TextView tvCount;

        PeopleHolder(View view,  PersonCountAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final PersonTimeListBean bean, final int position) {

            if (!TextUtils.isEmpty(bean.member_avatar)) {
                if (bean.member_avatar.startsWith(mContext.getString(R.string.HTTP))) {
                    GlideHelper.displayImage(mContext, bean.member_avatar, ivAvatar);
                } else {
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.member_avatar, ivAvatar);
                }
            } else {
                GlideHelper.displayImage(mContext, R.drawable.avatar_default_female, ivAvatar);
            }
            TextViewUtils.setTextOrEmpty(tvName, bean.memberName);
            if (TextUtils.isEmpty(bean.member_card_name)) {
                tvLevel.setVisibility(View.VISIBLE);
            } else {
                tvLevel.setVisibility(View.GONE);
                TextViewUtils.setTextOrEmpty(tvLevel, "(" + bean.member_card_name + ")");
            }
            tvLevel.setVisibility(View.GONE);
//            TextViewUtils.setTextOrEmpty(tvTime, StringFormatUtils.getUserDate(bean.createTime, DateTimeUtils.FORMAT_DATETIME));
            TextViewUtils.setTextOrEmpty(tvStore, bean.spName);
            TextViewUtils.setTextOrEmpty(tvType, "消费类型:" + bean.type);
            TextViewUtils.setTextOrEmpty(tvNum, "单据编号:"  +  bean.record_id);
            TextViewUtils.setTextOrEmpty(tvConsumeTime, "消费时间:" + bean.consumeTime);
            TextViewUtils.setTextOrEmpty(tvCount, "人次:" + StringFormatUtils.formatTwoDecimal(bean.personTimes));
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(bean, position);
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(PersonTimeListBean bean, int position);
    }

}
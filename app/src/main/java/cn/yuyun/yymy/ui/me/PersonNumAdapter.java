package cn.yuyun.yymy.ui.me;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import cn.yuyun.yymy.ui.me.entity.PeopleNumberBean;
import cn.yuyun.yymy.utils.GlideHelper;
import de.hdodenhof.circleimageview.CircleImageView;

/***
 *
 */
public class PersonNumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_person_number;
    private Context mContext;
    private List<PeopleNumberBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public PersonNumAdapter(Context context) {
        this.mContext = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(RESOURCE_ID, parent, false);
        return new ViewHolder(rootView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.bindItem(dataList.get(position), position);
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

     class ViewHolder extends RecyclerView.ViewHolder {

        private OnMyItemClickListener onItemClickListener;
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

        ViewHolder(View view, OnMyItemClickListener onItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final PeopleNumberBean bean, final int position) {
            if (!TextUtils.isEmpty(bean.getMember_avatar())) {
                if (bean.getMember_avatar().startsWith(mContext.getString(R.string.HTTP))) {
                    GlideHelper.displayImage(mContext, bean.getMember_avatar(), ivAvatar);
                } else {
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.getMember_avatar(), ivAvatar);
                }
            }
            TextViewUtils.setTextOrEmpty(tvName, bean.getMember_name());
            tvLevel.setVisibility(View.GONE);
            if (TextUtils.isEmpty(bean.getMember_card_name())) {
                tvLevel.setVisibility(View.GONE);
            } else {
                tvLevel.setVisibility(View.VISIBLE);
                TextViewUtils.setTextOrEmpty(tvLevel, "(" + bean.getMember_card_name() + ")");
            }
            TextViewUtils.setTextOrEmpty(tvTime, StringFormatUtils.getUserDate(bean.getConsumption_latest_time(), DateTimeUtils.FORMAT_DATETIME));
            TextViewUtils.setTextOrEmpty(tvStore, bean.getSp_name());
            TextViewUtils.setTextOrEmpty(tvType, "消费类型:");
            TextViewUtils.setTextOrEmpty(tvNum, "单据编号:" + bean.getRecord_id());
            TextViewUtils.setTextOrEmpty(tvConsumeTime, "消费时间:" + bean.getConsumption_latest_time());
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, PeopleNumberBean bean, int position);
    }

}

package cn.yuyun.yymy.ui.store.staff;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.lzz.utils.DensityUtils;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.RulesBean;
import cn.yuyun.yymy.http.result.StaffBean;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.ShadowDrawable;

/**
 * @author lzz
 * @desc
 * @date 2018-01-25
 */
public class StaffBirthdayAdapter extends RecyclerView.Adapter<StaffBirthdayAdapter.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_staff_birthday;
    private Context mContext;
    private List<StaffBean> childList;
    OnMyItemClickListener onItemClickListener;

    public StaffBirthdayAdapter(Context context, List<StaffBean> childList){
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

        private StaffBirthdayAdapter.OnMyItemClickListener onItemClickListener;

        TextView tvName;
        TextView tvBirthday;
        ImageView ivAvatar;
        RelativeLayout rlIn;

        ViewHolder(View view,  StaffBirthdayAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            this.onItemClickListener = onItemClickListener;
            tvBirthday = (TextView) view.findViewById(R.id.tv_birthday);
            ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            rlIn = (RelativeLayout) view.findViewById(R.id.rl_in);
        }

        private void bindItem(final StaffBean bean, final int position) {

            ShadowDrawable.setShadowDrawable(rlIn, Color.parseColor("#ffffff"), DensityUtils.dip2px(mContext, 6),
                    Color.parseColor("#22000000"),
                    DensityUtils.dip2px(mContext, 3), 0, 0);

            if (!TextUtils.isEmpty(bean.avatar)) {
                if (bean.avatar.startsWith(mContext.getString(R.string.HTTP))) {
                    GlideHelper.displayImage(mContext, bean.avatar, ivAvatar);
                } else {
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.avatar, ivAvatar);
                }
            } else {
                GlideHelper.displayImage(mContext, R.drawable.avatar_default_female, ivAvatar);
            }

            TextViewUtils.setTextOrEmpty(tvName, bean.staffName);
            TextViewUtils.setTextOrEmpty(tvBirthday, "生日:" + bean.birth_month + "月" +  bean.birth_day +"日");
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(StaffBean bean, int position);
    }

}
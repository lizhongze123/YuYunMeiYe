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

import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultBirthdayMember;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.EmptyLayout;

/**
 * @author lzz
 * @desc
 * @date 2018-01-17
 */
public class BirthdayMemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int VIEW_TYPE = -1;
    private int RESOURCE_ID = R.layout.item_member;
    private int RESOURCE_ID_EMPTY = R.layout.item_list_empty_pic;
    private Context mContext;
    private List<ResultBirthdayMember> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public BirthdayMemberAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == -1) {
            View emptyView = LayoutInflater.from(mContext).inflate(RESOURCE_ID_EMPTY, parent, false);
            return new EmptyHolder(emptyView);
        }else{
            View rootView = LayoutInflater.from(mContext).inflate(RESOURCE_ID, parent, false);
            return new ViewHolder(rootView, onItemClickListener);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.bindItem(dataList.get(position), position);
        }else{
            EmptyHolder emptyHolder = (EmptyHolder) holder;
            emptyHolder.bindItem();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(dataList.size() == 0){
            return VIEW_TYPE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return dataList.size() > 0 ? dataList.size() : 1;
    }

    public void notifyDataSetChanged(List<ResultBirthdayMember> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultBirthdayMember> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class EmptyHolder extends RecyclerView.ViewHolder {

        private EmptyLayout emptyLayout;

        EmptyHolder(View view) {
            super(view);
            emptyLayout = view.findViewById(R.id.img_error_layout);
        }

        private void bindItem() {
            emptyLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private BirthdayMemberAdapter.OnMyItemClickListener onItemClickListener;
        private RelativeLayout rl;
        private ImageView ivAvatar,ivCall;
        private TextView tvUserName, tvVip, tv_address, tvTime, tvTag;

        ViewHolder(View view,  BirthdayMemberAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            rl = (RelativeLayout) view.findViewById(R.id.rl);
            this.onItemClickListener = onItemClickListener;
            ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
            ivCall = (ImageView) view.findViewById(R.id.iv_call);
            tvUserName = (TextView) view.findViewById(R.id.tv_userName);
            tvVip = (TextView) view.findViewById(R.id.tv_vip);
            tv_address = (TextView) view.findViewById(R.id.tv_address);
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            tvTag = (TextView) view.findViewById(R.id.tv_lv_item_tag);
        }

        private void bindItem(final ResultBirthdayMember bean, final int position) {
            ivCall.setVisibility(View.GONE);
            tvTag.setVisibility(View.GONE);
            if(!TextUtils.isEmpty(bean.avatar)){
                if(bean.avatar.startsWith(mContext.getString(R.string.HTTP))){
                    GlideHelper.displayImage(mContext, bean.avatar, ivAvatar);
                }else{
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.avatar, ivAvatar);
                }
            }else{
                GlideHelper.displayImage(mContext, bean.sex.resId, ivAvatar);
            }
            TextViewUtils.setTextOrEmpty(tvUserName, bean.name);
            tvVip.setVisibility(View.GONE);
            tv_address.setText("生日：");
            TextViewUtils.setTextOrEmpty(tvTime, bean.birth_year + "-" + bean.birth_month + "-" +bean.birth_day);
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, ResultBirthdayMember bean, int position);
    }

}
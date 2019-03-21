package cn.yuyun.yymy.ui.store.member;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author lzz
 * @desc
 * @date 2018-01-17
 */
public class StoreMemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_store_member;
    private Context mContext;
    private List<ResultMemberBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public StoreMemberAdapter(Context context){
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

    public void notifyDataSetChanged(List<ResultMemberBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultMemberBean> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    public ResultMemberBean getItemBean(int position){
        return dataList.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private StoreMemberAdapter.OnMyItemClickListener onItemClickListener;
        private RelativeLayout rl;
        private ImageView ivAvatar,ivCall, ivSex;
        private TextView tvUserName, tvVip, tv_address, tvTime, tvTag, tvManager;
        private View dot;

        ViewHolder(View view,  StoreMemberAdapter.OnMyItemClickListener onItemClickListener) {
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
            tvManager = (TextView) view.findViewById(R.id.tv_manager);
            ivSex = (ImageView) view.findViewById(R.id.iv_sex);
            dot = view.findViewById(R.id.dot);
        }

        private void bindItem(final ResultMemberBean bean, final int position) {
            ivCall.setVisibility(View.GONE);
            tvTag.setVisibility(View.GONE);
            if(!TextUtils.isEmpty(bean.memberAvatar)){
                if(bean.memberAvatar.startsWith(mContext.getString(R.string.HTTP))){
                    GlideHelper.displayImage(mContext, bean.memberAvatar, ivAvatar);
                }else{
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.memberAvatar, ivAvatar);
                }
            }else{
                GlideHelper.displayImage(mContext, bean.member_sex.resId, ivAvatar);
            }
            ivSex.setVisibility(View.GONE);
            TextViewUtils.setTextOrEmpty(tvUserName, bean.memberName);
            if(TextUtils.isEmpty(bean.memberLevelName)){
                tvVip.setVisibility(View.GONE);
            }else{
                tvVip.setVisibility(View.VISIBLE);
                TextViewUtils.setTextOrEmpty(tvVip, "(" + bean.memberLevelName + ")");
            }
            TextViewUtils.setTextOrEmpty(tvTime, StringFormatUtils.getUserDate(bean.memberConsumptionLatestTime));
            if(null != bean.staffMgrMemberRspList){
                if(bean.staffMgrMemberRspList.size() > 0){
                    dot.setVisibility(View.VISIBLE);
                }else{
                    dot.setVisibility(View.GONE);
                }
                SpannableStringBuilder ssb = new SpannableStringBuilder();
                for (int i = 0; i < bean.staffMgrMemberRspList.size(); i++) {
                    if(null != bean.staffMgrMemberRspList.get(i).staff_name){
                        if (i != (bean.staffMgrMemberRspList.size() - 1) && bean.staffMgrMemberRspList.size() > 1) {
                            ssb.append(bean.staffMgrMemberRspList.get(i).staff_name + ";");
                        } else {
                            ssb.append(bean.staffMgrMemberRspList.get(i).staff_name);
                        }
                    }
                }
                tvManager.setText(ssb);
                tvManager.setMovementMethod(LinkMovementMethod.getInstance());
            }else{
                dot.setVisibility(View.GONE);
            }
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != onItemClickListener){
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
        void onItemClick(View view, ResultMemberBean bean, int position);
    }

}
package cn.yuyun.yymy.ui.me;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultEmailList;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author lzz
 * @desc
 * @date 2018-03-12
 */
public class EmailListAdapter extends RecyclerView.Adapter<EmailListAdapter.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_email;
    private Context mContext;
    private List<ResultEmailList> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;
    public boolean isDel;
    private boolean[] isCheckedArray;

    public EmailListAdapter(Context context){
        this.mContext = context;
    }

    public void setDelVisiable(boolean isDel){
        this.isDel = isDel;
        isCheckedArray = new boolean[this.dataList.size()];
        notifyDataSetChanged();
    }

    /**获得选中条目的结果*/
    public List<ResultEmailList> getSelectedItem() {
        List<ResultEmailList> selectList = new ArrayList<>();
        for (int i = 0, len = dataList.size(); i < len; i++) {
            if (isCheckedArray[i]) {
                selectList.add(dataList.get(i));
            }
        }
        return selectList;
    }

    public boolean getDelVisiable(){
        return isDel;
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

    public void notifyDataSetChanged(List<ResultEmailList> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultEmailList> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private EmailListAdapter.OnMyItemClickListener onItemClickListener;
        private LinearLayout ll;
        TextView tvName, tvContent, tvTime, tvLevel, tvAddress;
        ImageView ivAvatar;
        CheckBox checkBox;

        ViewHolder(View view,  EmailListAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvContent = (TextView) view.findViewById(R.id.tv_content);
            tvAddress = (TextView) view.findViewById(R.id.tv_address);
            tvLevel = (TextView) view.findViewById(R.id.tv_level);
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            ll = (LinearLayout) view.findViewById(R.id.ll);
            ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
            checkBox = (CheckBox) view.findViewById(R.id.cb);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final ResultEmailList bean, final int position) {
            if(isDel){
                checkBox.setVisibility(View.VISIBLE);
            }else{
                checkBox.setVisibility(View.GONE);
            }
            TextViewUtils.setTextOrEmpty(tvName, bean.createPersonName, "未知");
            TextViewUtils.setTextOrEmpty(tvContent, bean.content);
            TextViewUtils.setTextOrEmpty(tvTime, bean.createTime);
            TextViewUtils.setTextOrEmpty(tvAddress, bean.baseon_desc);
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, bean, position);
                    }
                }
            });

            if(!TextUtils.isEmpty(bean.createPersonAvatar)){
                if(bean.createPersonAvatar.startsWith(mContext.getString(R.string.HTTP))){
                    GlideHelper.displayImage(mContext, bean.createPersonAvatar, ivAvatar, R.drawable.avatar_default_female);
                }else{
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.createPersonAvatar, ivAvatar, R.drawable.avatar_default_female);
                }
            }else{
                GlideHelper.displayImage(mContext, bean.createPersonAvatar, ivAvatar, R.drawable.avatar_default_female);
            }

            if(null != bean.createPersonPosition){
                tvLevel.setVisibility(View.VISIBLE);
                tvLevel.setText("(" +  bean.createPersonPosition + ")");
            }else{
                tvLevel.setVisibility(View.GONE);
            }

            if(null != isCheckedArray){
                checkBox.setOnCheckedChangeListener(null);
                checkBox.setChecked(isCheckedArray[position]);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        isCheckedArray[position] = isChecked;
                    }
                });
            }
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, ResultEmailList bean, int position);
    }

}
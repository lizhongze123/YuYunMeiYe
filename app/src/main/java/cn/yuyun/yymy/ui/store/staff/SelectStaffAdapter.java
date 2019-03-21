package cn.yuyun.yymy.ui.store.staff;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.http.result.StaffBean;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author lzz
 * @desc
 * @date 2018-01-17
 */
public class SelectStaffAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_select_staff;
    private Context mContext;
    private List<StaffBean> dataList = new ArrayList<>();
    private List<StaffBean> intentData = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public SelectStaffAdapter(Context context){
        this.mContext = context;
    }

    public SelectStaffAdapter(Context context, List<StaffBean> intentList){
        this.mContext = context;
        this.intentData = intentList;
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

    public void notifyDataSetChanged(List<StaffBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<StaffBean> list) {
        for (int i = 0, iLength = intentData.size(); i < iLength; i++) {
            for (int j = 0, jLength = list.size(); j < jLength; j++) {
                if(list.get(j).staffId.equals(intentData.get(i).staffId)){
                    list.get(j).isChecked = true;
                }
            }
        }
        this.dataList.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    /**获得选中条目的结果*/
    public List<StaffBean> getSelectedItem() {
        List<StaffBean> selectList = new ArrayList<>();
        for (int i = 0, len = dataList.size(); i < len; i++) {
            if (dataList.get(i).isChecked) {
                selectList.add(dataList.get(i));
            }
        }
        return selectList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private SelectStaffAdapter.OnMyItemClickListener onItemClickListener;
        private RelativeLayout rl;
        private CheckBox checkBox;
        private ImageView ivAvatar;
        private TextView tvName;
        private TextView tvLevel;
        private TextView tvTime;
        private ImageView ivSex;

        ViewHolder(View view,  SelectStaffAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            rl = (RelativeLayout) view.findViewById(R.id.rl);
            this.onItemClickListener = onItemClickListener;
            checkBox = (CheckBox) view.findViewById(R.id.cb);
            ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvLevel = (TextView) view.findViewById(R.id.tv_vip);
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            ivSex = (ImageView) view.findViewById(R.id.iv_sex);
        }

        private void bindItem(final StaffBean bean, final int position) {
            if(!TextUtils.isEmpty(bean.avatar)){
                if(bean.avatar.startsWith(mContext.getString(R.string.HTTP))){
                    GlideHelper.displayImage(mContext, bean.avatar, ivAvatar);
                }else{
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.avatar, ivAvatar);
                }
            }else{
                GlideHelper.displayImage(mContext, R.drawable.avatar_default_female, ivAvatar);
            }
            if(!TextUtils.isEmpty(bean.name)){
                TextViewUtils.setTextOrEmpty(tvName, bean.name);
            }else {
                TextViewUtils.setTextOrEmpty(tvName, bean.staffName);
            }
            tvLevel.setVisibility(View.GONE);
            ivSex.setVisibility(View.GONE);
            TextViewUtils.setTextOrEmpty(tvLevel, bean.positionName);
            TextViewUtils.setTextOrEmpty(tvTime, bean.entryTime);
            checkBox.setChecked(bean.isChecked);
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkBox.toggle();
                    bean.isChecked = checkBox.isChecked();
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
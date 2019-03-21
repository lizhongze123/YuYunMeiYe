package cn.yuyun.yymy.ui.store.member;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
public class SelectMemberAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_select_member;
    private Context mContext;
    private List<ResultMemberBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public SelectMemberAdapter2(Context context){
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


    public List<ResultMemberBean> getSelectedItem() {
        List<ResultMemberBean> selectList = new ArrayList<>();
        for (int i = 0, len = dataList.size(); i < len; i++) {
            if (dataList.get(i).isChecked) {
                selectList.add(dataList.get(i));
            }
        }
        return selectList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private SelectMemberAdapter2.OnMyItemClickListener onItemClickListener;
        private RelativeLayout rl;
        private CheckBox checkBox;
        private ImageView ivAvatar;
        private TextView tvName;
        private TextView tvLevel;
        private TextView tvTime;
        private ImageView ivSex;

        ViewHolder(View view,  SelectMemberAdapter2.OnMyItemClickListener onItemClickListener) {
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

        private void bindItem(final ResultMemberBean bean, final int position) {
            if(!TextUtils.isEmpty(bean.memberAvatar)){
                if(bean.memberAvatar.startsWith(mContext.getString(R.string.HTTP))){
                    GlideHelper.displayImage(mContext, bean.memberAvatar, ivAvatar);
                }else{
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.memberAvatar, ivAvatar);
                }
            }else{
                GlideHelper.displayImage(mContext, bean.member_sex.resId, ivAvatar);
            }
            GlideHelper.displayImage(mContext, bean.member_sex.sexId, ivSex);
            TextViewUtils.setTextOrEmpty(tvName, bean.memberName);
            if(TextUtils.isEmpty(bean.memberLevelName)){
                tvLevel.setVisibility(View.GONE);
            }else{
                tvLevel.setVisibility(View.VISIBLE);
                TextViewUtils.setTextOrEmpty(tvLevel, "(" + bean.memberLevelName + ")");
            }
            TextViewUtils.setTextOrEmpty(tvTime, StringFormatUtils.getUserDate(bean.memberConsumptionLatestTime));
            //先设置一次CheckBox的选中监听器，传入参数null
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setChecked(bean.isChecked);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    bean.isChecked = checkBox.isChecked();
                }
            });
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
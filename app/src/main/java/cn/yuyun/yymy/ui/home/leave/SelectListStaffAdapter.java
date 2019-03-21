package cn.yuyun.yymy.ui.home.leave;

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

import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.StringUtil;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultListStaff;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author
 * @desc
 * @date
 */
public class SelectListStaffAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_select_staff;
    private Context mContext;
    private List<ResultListStaff> dataList = new ArrayList<>();
    private List<ResultListStaff> intentData = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public SelectListStaffAdapter(Context context){
        this.mContext = context;
    }

    public SelectListStaffAdapter(Context context, List<ResultListStaff> intentList){
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

    public void notifyDataSetChanged(List<ResultListStaff> dataList) {
        this.dataList.clear();
        addAll(dataList);
    }

    public void addAll(List<ResultListStaff> list) {
        for (int i = 0, iLength = intentData.size(); i < iLength; i++) {
            for (int j = 0, jLength = list.size(); j < jLength; j++) {
                if(list.get(j).staff_id.equals(intentData.get(i).staff_id)){
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
    public List<ResultListStaff> getSelectedItem() {
       /* List<ResultListStaff> selectList = new ArrayList<>();
        for (int i = 0, len = dataList.size(); i < len; i++) {
            if (dataList.get(i).isChecked) {
                selectList.add(dataList.get(i));
            }
        }*/
        return intentData;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private SelectListStaffAdapter.OnMyItemClickListener onItemClickListener;
        private RelativeLayout rl;
        private CheckBox checkBox;
        private ImageView ivAvatar;
        private TextView tvName;
        private TextView tvLevel;
        private TextView tvTime;
        private ImageView ivSex;

        ViewHolder(View view,  SelectListStaffAdapter.OnMyItemClickListener onItemClickListener) {
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

        private void bindItem(final ResultListStaff bean, final int position) {
            if(!TextUtils.isEmpty(bean.avatar)){
                if(bean.avatar.startsWith(mContext.getString(R.string.HTTP))){
                    GlideHelper.displayImage(mContext, bean.avatar, ivAvatar);
                }else{
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.avatar, ivAvatar);
                }
            }else{
                GlideHelper.displayImage(mContext, R.drawable.avatar_default_female, ivAvatar);
            }
            TextViewUtils.setTextOrEmpty(tvName, bean.staff_name);
            if (StringUtil.isEmpty(bean.position_name)) {
                tvLevel.setVisibility(View.GONE);
            }else{
                tvLevel.setVisibility(View.VISIBLE);
                TextViewUtils.setTextOrEmpty(tvLevel, "(" + bean.position_name + ")");
            }
            ivSex.setVisibility(View.GONE);
            TextViewUtils.setTextOrEmpty(tvTime, bean.entry_time);
            checkBox.setChecked(bean.isChecked);

            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkBox.toggle();
                    bean.isChecked = checkBox.isChecked();
                    if(checkBox.isChecked()){
                        intentData.add(bean);
                    }else{
                        for (int i = 0, iLength = intentData.size(); i < iLength; i++) {
                            if(bean.staff_id.equals(intentData.get(i).staff_id)){
                                intentData.remove(i);
                                break;
                            }
                        }
                    }
                    onItemClickListener.onItemClick(itemView, bean, position);
                }
            });
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, ResultListStaff bean, int position);
    }

}
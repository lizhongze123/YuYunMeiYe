package cn.yuyun.yymy.ui.home.appointment;

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

import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultProject;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author
 * @desc
 * @date
 */
public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_project;
    private Context mContext;
    private List<ResultProject> dataList = new ArrayList<>();
    private List<ResultProject> intentData = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public ProjectListAdapter(Context context){
        this.mContext = context;
    }

    public ProjectListAdapter(Context context, List<ResultProject> list){
        this.mContext = context;
        this.intentData = list;
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

    public void notifyDataSetChanged(List<ResultProject> dataList) {
        this.dataList.clear();
        for (int i = 0, iLength = intentData.size(); i < iLength; i++) {
            for (int j = 0, jLength = dataList.size(); j < jLength; j++) {
                if(dataList.get(j).good_id.equals(intentData.get(i).good_id)){
                    dataList.get(j).isChecked = true;
                }
            }
        }
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultProject> dataList) {
        for (int i = 0, iLength = intentData.size(); i < iLength; i++) {
            for (int j = 0, jLength = dataList.size(); j < jLength; j++) {
                if(dataList.get(j).good_id.equals(intentData.get(i).good_id)){
                    dataList.get(j).isChecked = true;
                }
            }
        }
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    /**获得选中条目的结果*/
  /*  public List<ResultProject> getSelectedItem() {
        List<ResultProject> selectList = new ArrayList<>();
        for (int i = 0, len = dataList.size(); i < len; i++) {
            if (dataList.get(i).isChecked) {
                selectList.add(dataList.get(i));
            }
        }
        return selectList;
    }*/

    public List<ResultProject> getSelectedItem() {
        return intentData;
    }

    public List<ResultProject> getData() {
        return dataList;
    }

    public void check(boolean isChecked, ResultProject bean){
        if(isChecked){
            intentData.add(bean);
        }else{
            for (int i = 0; i < intentData.size(); i++) {
                if(intentData.get(i).good_id.equals(bean.good_id)){
                    intentData.remove(i);
                    break;
                }
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ProjectListAdapter.OnMyItemClickListener onItemClickListener;
        private RelativeLayout rl;
        ImageView ivAvatar;
        TextView tvName, tvPrice, tvDesc;
        CheckBox checkBox;
        int override;

        ViewHolder(View view,  ProjectListAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
            tvPrice = (TextView) view.findViewById(R.id.tv_price);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvDesc = (TextView) view.findViewById(R.id.tv_desc);
            rl = (RelativeLayout) view.findViewById(R.id.rl);
            checkBox = (CheckBox) view.findViewById(R.id.cb);
            this.onItemClickListener = onItemClickListener;
            override = ResoureUtils.getDimension(mContext, R.dimen.item_avatar_size);
        }

        private void bindItem(final ResultProject bean, final int position) {

            checkBox.setChecked(bean.isChecked);

            if (!TextUtils.isEmpty(bean.thumb_url)) {
                if (bean.thumb_url.startsWith(mContext.getString(R.string.HTTP))) {
                    GlideHelper.displayImage(mContext, bean.thumb_url, ivAvatar, R.color.loadding_img_bg);
                } else {
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.thumb_url, ivAvatar, R.color.loadding_img_bg);
                }
            } else {
                ivAvatar.setImageResource(R.color.loadding_img_bg);
            }

            TextViewUtils.setTextOrEmpty(tvName, bean.name);
            TextViewUtils.setTextOrEmpty(tvDesc, bean.description, "无");
            TextViewUtils.setTextOrEmpty(tvPrice, StringFormatUtils.formatDecimal(bean.guideprice));
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkBox.toggle();
                    bean.isChecked = checkBox.isChecked();
                    check(bean.isChecked, bean);
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
        void onItemClick(View view, ResultProject bean, int position);
    }

}
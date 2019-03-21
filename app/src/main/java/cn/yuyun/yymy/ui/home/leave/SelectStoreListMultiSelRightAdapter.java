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
import java.util.Collections;
import java.util.List;

import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean.OgServiceplacesRspListBean;
import cn.yuyun.yymy.http.result.ResultProject;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author lzz
 * @desc
 * @date 2018-01-17
 */
public class SelectStoreListMultiSelRightAdapter extends RecyclerView.Adapter<SelectStoreListMultiSelRightAdapter.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_store_multi_sel;
    private Context mContext;
    private List<ResultClassfiyStoreBean> dataList = new ArrayList<>();
    private List<OgServiceplacesRspListBean> intentData = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;
    private int mIndex;

    public SelectStoreListMultiSelRightAdapter(Context context) {
        this.mContext = context;
    }

    public SelectStoreListMultiSelRightAdapter(Context context, List<OgServiceplacesRspListBean> list){
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
        holder.bindItem(dataList.get(mIndex).ogServiceplacesRspList.get(position), position);
    }

    public void setIndex(int index) {
        mIndex = index;
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged(List<ResultClassfiyStoreBean> dataList) {
        this.dataList.clear();
        mIndex = 0;
        for (int i = 0, iLength = intentData.size(); i < iLength; i++) {
                for (int k = 0, kLength = dataList.size(); k < kLength; k++) {
                    for (int l = 0, lLength = dataList.get(k).ogServiceplacesRspList.size(); l < lLength; l++) {
                        if(dataList.get(k).ogServiceplacesRspList.get(l).sp_id.equals(intentData.get(i).sp_id)){
                            dataList.get(k).ogServiceplacesRspList.get(l).isChecked = true;
                        }
                    }
                }

        }
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

   /* public void notifyDataSetChanged(List<ResultClassfiyStoreBean> dataList) {
        this.dataList.clear();
        for (int i = 0, iLength = intentData.size(); i < iLength; i++) {
            for (int j = 0, jLength = intentData.get(i).ogServiceplacesRspList.size(); j < jLength; j++) {
                for (int k = 0, kLength = dataList.size(); k < kLength; k++) {
                    for (int l = 0, lLength = dataList.get(k).ogServiceplacesRspList.size(); l < lLength; l++) {
                        if(dataList.get(k).ogServiceplacesRspList.get(l).sp_id.equals(intentData.get(i).ogServiceplacesRspList.get(j).sp_id)){
                            dataList.get(k).ogServiceplacesRspList.get(l).isChecked = true;
                        }
                    }
                }
            }
        }
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }*/

    public void addAll(List<ResultClassfiyStoreBean> dataList) {
        for (int i = 0, iLength = intentData.size(); i < iLength; i++) {
            for (int k = 0, kLength = dataList.size(); k < kLength; k++) {
                for (int l = 0, lLength = dataList.get(k).ogServiceplacesRspList.size(); l < lLength; l++) {
                    if(dataList.get(k).ogServiceplacesRspList.get(l).sp_id.equals(intentData.get(i).sp_id)){
                        dataList.get(k).ogServiceplacesRspList.get(l).isChecked = true;
                    }
                }
            }

        }
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        mIndex = 0;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (dataList.size() == 0) {
            return dataList.size();
        } else {
            return dataList.get(mIndex).ogServiceplacesRspList.size();
        }

    }

    public List<OgServiceplacesRspListBean> getSelectedItem() {
        return intentData;
    }

    private void check(boolean isChecked, OgServiceplacesRspListBean bean){
        if(isChecked){
            intentData.add(bean);
        }else{
            intentData.remove(bean);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private SelectStoreListMultiSelRightAdapter.OnMyItemClickListener onItemClickListener;
        private RelativeLayout rl;
        ImageView ivAvatar;
        TextView tvStoreName, tvName;
        TextView tv_tag;
        CheckBox checkBox;
        int override;

        ViewHolder(View view, SelectStoreListMultiSelRightAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
            tvStoreName = (TextView) view.findViewById(R.id.tv_storeName);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tv_tag = (TextView) view.findViewById(R.id.tv_lv_item_tag);
            rl = (RelativeLayout) view.findViewById(R.id.rl);
            checkBox = (CheckBox) view.findViewById(R.id.cb);
            this.onItemClickListener = onItemClickListener;
            override = ResoureUtils.getDimension(mContext, R.dimen.item_avatar_size);
        }

        private void bindItem(final OgServiceplacesRspListBean bean, final int position) {

            checkBox.setChecked(bean.isChecked);

            if (!TextUtils.isEmpty(bean.thumb_url)) {
                if (bean.thumb_url.startsWith("http")) {
                    GlideHelper.displayOverrideImage(mContext, bean.thumb_url, override, override, ivAvatar, R.drawable.default_store);
                } else {
                    GlideHelper.displayOverrideImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.thumb_url, R.drawable.default_store, override, override, ivAvatar);
                }
            } else {
                GlideHelper.displayOverrideImage(mContext, R.drawable.default_store, override, override, ivAvatar);
            }
            TextViewUtils.setTextOrEmpty(tvStoreName, bean.sp_name);
            TextViewUtils.setTextOrEmpty(tvName, bean.chairman, "无");
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkBox.toggle();
                    bean.isChecked = checkBox.isChecked();
                    check(bean.isChecked, bean);
                    onItemClickListener.onItemClick(v, bean, position);
                }
            });

           /* if (position == 0) {
                tv_tag.setVisibility(View.VISIBLE);
                tv_tag.setText("管理门店");
                tv_tag.setBackgroundColor(ResoureUtils.getColor(mContext, R.color.colorPrimary));
            } else {

                if (bean.get(position - 1).status == bean.status) {
                    tv_tag.setVisibility(View.GONE);
                } else {
                    tv_tag.setVisibility(View.VISIBLE);
                    tv_tag.setText("停用门店");
                    tv_tag.setBackgroundColor(ResoureUtils.getColor(mContext, R.color.gray_cc));
                }

            }*/

        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, OgServiceplacesRspListBean bean, int position);
    }

}
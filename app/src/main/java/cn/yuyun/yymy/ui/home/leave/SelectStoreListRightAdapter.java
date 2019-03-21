package cn.yuyun.yymy.ui.home.leave;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.lzz.utils.LogUtils;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author
 * @desc
 * @date
 */
public class SelectStoreListRightAdapter extends RecyclerView.Adapter<SelectStoreListRightAdapter.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_store;
    private Context mContext;
    private List<ResultClassfiyStoreBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public SelectStoreListRightAdapter(Context context) {
        this.mContext = context;
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

    private int mIndex;

    public void setIndex(int index) {
        mIndex = index;
        normalCount = 0;
        notifyDataSetChanged();
    }

    int normalCount;

    @Override
    public int getItemCount() {
        if (dataList.size() == 0) {
            return dataList.size();
        } else {
            for (int i = 0; i < dataList.get(mIndex).ogServiceplacesRspList.size(); i++) {
                ResultClassfiyStoreBean.OgServiceplacesRspListBean bean = dataList.get(mIndex).ogServiceplacesRspList.get(i);
                if (bean.status != 1) {
                    normalCount = i;
                    break;
                } else if (i == dataList.get(mIndex).ogServiceplacesRspList.size() - 1) {
                    normalCount = dataList.get(mIndex).ogServiceplacesRspList.size();
                }
            }
            return dataList.get(mIndex).ogServiceplacesRspList.size();
        }

    }

    public void notifyDataSetChanged(List<ResultClassfiyStoreBean> dataList) {
        this.dataList.clear();
        mIndex = 0;
        for (ResultClassfiyStoreBean bean : dataList) {
            Collections.sort(bean.ogServiceplacesRspList);
        }
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

   /* public void addAll(List<ResultClassfiyStoreBean> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }*/

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private SelectStoreListRightAdapter.OnMyItemClickListener onItemClickListener;
        private RelativeLayout rl;
        ImageView ivAvatar;
        TextView tvStoreName, tvName;
        TextView tv_tag;
        int override;

        ViewHolder(View view, SelectStoreListRightAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
            tvStoreName = (TextView) view.findViewById(R.id.tv_storeName);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tv_tag = (TextView) view.findViewById(R.id.tv_lv_item_tag);
            rl = (RelativeLayout) view.findViewById(R.id.rl);
            this.onItemClickListener = onItemClickListener;
            override = ResoureUtils.getDimension(mContext, R.dimen.item_avatar_size);
        }

        private void bindItem(final ResultClassfiyStoreBean.OgServiceplacesRspListBean bean, final int position) {
            if (!TextUtils.isEmpty(bean.thumb_url)) {
                if (bean.thumb_url.startsWith(mContext.getString(R.string.HTTP))) {
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
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, bean, position);
                    }
                }
            });

            if (position == 0 && dataList.get(mIndex).ogServiceplacesRspList.get(position).status == 1) {
                tv_tag.setVisibility(View.VISIBLE);
                tv_tag.setText("管理门店(" + normalCount + ")");
                tv_tag.setTextColor(Color.parseColor("#ffffff"));
                tv_tag.setBackgroundColor(ResoureUtils.getColor(mContext, R.color.colorPrimary));
            } else if (position == 0 && dataList.get(mIndex).ogServiceplacesRspList.get(position).status != 1) {
                tv_tag.setVisibility(View.VISIBLE);
                tv_tag.setText("停用门店(" + (dataList.get(mIndex).ogServiceplacesRspList.size() - normalCount) + ")");
                tv_tag.setTextColor(Color.parseColor("#ffffff"));
                tv_tag.setBackgroundColor(Color.parseColor("#f5f5f5"));
            } else {
                if (dataList.get(mIndex).ogServiceplacesRspList.get(position - 1).status == bean.status) {
                    tv_tag.setVisibility(View.GONE);
                } else {
                    tv_tag.setVisibility(View.VISIBLE);
                    tv_tag.setTextColor(Color.parseColor("#CCCCCC"));
                    tv_tag.setText("停用门店(" + (dataList.get(mIndex).ogServiceplacesRspList.size() - normalCount) + ")");
                    tv_tag.setBackgroundColor(Color.parseColor("#f5f5f5"));
                }

            }

        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, ResultClassfiyStoreBean.OgServiceplacesRspListBean bean, int position);
    }

}
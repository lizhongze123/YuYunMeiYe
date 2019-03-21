package cn.yuyun.yymy.ui.home.work;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean.OgServiceplacesRspListBean;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author
 * @desc
 * @date
 */

public class SelectStoreAdapter extends RecyclerView.Adapter<SelectStoreAdapter.ViewHolder>{

    private int default_tatal = Integer.MAX_VALUE;
    private int RESOURCE_ID = R.layout.item_pic_del;
    private Context mContext;
    private List<OgServiceplacesRspListBean> dataList = new ArrayList<>();
    private boolean isOff;

    public SelectStoreAdapter(Context context, List<OgServiceplacesRspListBean> list){
        this.mContext = context;
        this.dataList = list;
    }

    public SelectStoreAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public SelectStoreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(RESOURCE_ID, parent, false);
        return new SelectStoreAdapter.ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(SelectStoreAdapter.ViewHolder holder, int position) {
        holder.bindItem(position);
    }

    @Override
    public int getItemCount() {
        int total = dataList.size();
        if(total < default_tatal){
            total++;
        }
        return total;
    }

    public void notifyDataSetChanged(List<OgServiceplacesRspListBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<OgServiceplacesRspListBean> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    public void setOff(boolean isOff){
        this.isOff = isOff;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivAvatar;
        private TextView tvTips;
        private TextView tvName;
        private int overrideSize;

        ViewHolder(View view) {
            super(view);
            tvTips = (TextView) view.findViewById(R.id.item_tips);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
            overrideSize = ResoureUtils.getDimension(mContext, R.dimen.select_people_avatar);
        }

        private void bindItem(final int position) {
            tvTips.setVisibility(View.GONE);
            if(position == dataList.size()){
                GlideHelper.displayOverrideImage(mContext, R.drawable.picture_add, overrideSize, overrideSize, ivAvatar);
                ivAvatar.setClickable(true);
                tvName.setText("");
                ivAvatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.onAdd();
                    }
                });
            }else{
                if(default_tatal == position || position > default_tatal){
                    ivAvatar.setVisibility(View.GONE);
                    ivAvatar.setClickable(false);
                    tvName.setText("");
                }else{
                    ivAvatar.setVisibility(View.VISIBLE);

                    if (!TextUtils.isEmpty(dataList.get(position).thumb_url)) {
                        if (dataList.get(position).thumb_url.startsWith(mContext.getString(R.string.HTTP))) {
                            GlideHelper.displayOverrideImage(mContext, dataList.get(position).thumb_url, overrideSize, overrideSize, ivAvatar, R.drawable.default_store);
                        } else {
                            GlideHelper.displayOverrideImage(mContext, mContext.getString(R.string.image_url_prefix) + dataList.get(position).thumb_url, R.drawable.default_store, overrideSize, overrideSize, ivAvatar);
                        }
                    } else {
                        GlideHelper.displayOverrideImage(mContext, R.drawable.default_store, overrideSize, overrideSize, ivAvatar);
                    }
                    ivAvatar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callback.onDel(position);
                        }
                    });
                    TextViewUtils.setTextOrEmpty(tvName, dataList.get(position).sp_name);
                }
            }
            if(isOff){
                ivAvatar.setClickable(false);
                ivAvatar.setAlpha(0.5f);
                tvName.setTextColor(Color.parseColor("#55274335"));
            }else{
                ivAvatar.setClickable(true);
                ivAvatar.clearColorFilter();
                ivAvatar.setAlpha(1.0f);
                tvName.setTextColor(Color.parseColor("#274335"));
            }
        }
    }

    public interface OnClickCallBack {
        void onAdd();

        void onDel(int pos);
    }

    private OnClickCallBack callback;

    public void setOnSuccessCallBack(OnClickCallBack callback) {
        this.callback = callback;
    }

    public void setMax(int size){
        default_tatal = size;
    }

}

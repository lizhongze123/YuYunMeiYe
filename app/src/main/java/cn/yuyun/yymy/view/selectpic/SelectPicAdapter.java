package cn.yuyun.yymy.view.selectpic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.lzz.utils.DensityUtils;
import cn.lzz.utils.ResoureUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author
 * @desc
 * @date
 */

public class SelectPicAdapter extends RecyclerView.Adapter<SelectPicAdapter.ViewHolder>{

    private int default_tatal = 3;
    /**是否可以编辑*/
    private boolean isEdit = true;
    /**是否显示删除*/
    private boolean isDel = true;
    private int RESOURCE_ID = R.layout.item_pic_wall;
    private Context mContext;
    private List<String> dataList = new ArrayList<>();

    public SelectPicAdapter(Context context, List<String> list){
        this.mContext = context;
        this.dataList = list;
    }

    public SelectPicAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public SelectPicAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(RESOURCE_ID, parent, false);
        SelectPicAdapter.ViewHolder viewHolder = new SelectPicAdapter.ViewHolder(rootView);
        setThreeImgSize(mContext, viewHolder.ivAvatar);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SelectPicAdapter.ViewHolder holder, int position) {
        holder.bindItem(position);
    }

    @Override
    public int getItemCount() {
        int total = dataList.size();
        if(isEdit){
            if(total < default_tatal){
                total++;
            }
        }
        return total;
    }

    public void notifyDataSetChanged(List<String> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<String> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivAvatar;
        private ImageView tvTips;
        private int overrideSize;

        ViewHolder(View view) {
            super(view);
            tvTips = (ImageView) view.findViewById(R.id.item_tips);
            ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
            overrideSize = ResoureUtils.getDimension(mContext, R.dimen.select_people_avatar);
        }

        private void bindItem(final int position) {

            if(position == dataList.size()){
                if(isEdit){
                    GlideHelper.displayImage(mContext, R.drawable.picture_add, ivAvatar);
                    tvTips.setVisibility(View.GONE);
                    ivAvatar.setClickable(true);
                    ivAvatar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callback.onAdd();
                        }
                    });
                }else{
                    tvTips.setVisibility(View.GONE);
                }
            }else{
                if(isEdit){
                    if(default_tatal == position || position > default_tatal){
                        ivAvatar.setVisibility(View.GONE);
                        ivAvatar.setClickable(false);
                        tvTips.setVisibility(View.GONE);
                    }else{
                        if(isDel){
                            tvTips.setVisibility(View.VISIBLE);
                            tvTips.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    callback.onDel(position);
                                }
                            });
                        }else{
                            tvTips.setVisibility(View.GONE);
                        }
                        ivAvatar.setVisibility(View.VISIBLE);
                        ivAvatar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callback.onBrowser(position);
                            }
                        });
                        GlideHelper.displayImageForSize(mContext, dataList.get(position),overrideSize, overrideSize, ivAvatar, R.drawable.photo_filter_image_empty);
                    }
                }else{
                    tvTips.setVisibility(View.GONE);
                    ivAvatar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callback.onBrowser(position);
                        }
                    });
                    GlideHelper.displayImageForSize(mContext, dataList.get(position),overrideSize, overrideSize, ivAvatar, R.drawable.photo_filter_image_empty);
                }

            }

        }
    }

    public interface OnClickCallBack {
        void onAdd();

        void onDel(int pos);

        void onBrowser(int pos);
    }

    private OnClickCallBack callback;

    public void setOnSuccessCallBack(OnClickCallBack callback) {
        this.callback = callback;
    }

    public void setMax(int size){
        default_tatal = size;
    }

    public void editable(boolean isEdit){
        this.isEdit = isEdit;
    }

    public void isDel(boolean isDel){
        this.isDel = isDel;
    }

    private static void setThreeImgSize(Context context, ImageView norImg) {
        FrameLayout.LayoutParams norImgLayout = (FrameLayout.LayoutParams) norImg.getLayoutParams();
        norImgLayout.width = (DensityUtils.getScreenWidth(context) - ResoureUtils.getDimension(context, R.dimen.x42)) / 3;
        norImgLayout.height = (DensityUtils.getScreenWidth(context) - ResoureUtils.getDimension(context, R.dimen.x42)) / 3;
    }

}

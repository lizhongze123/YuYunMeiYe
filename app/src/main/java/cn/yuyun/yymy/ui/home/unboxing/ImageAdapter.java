package cn.yuyun.yymy.ui.home.unboxing;

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
import cn.yuyun.yymy.R;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private List<String> mData;
    private Context mContext;
    private boolean isClick;
    private boolean isSave;

    public ImageAdapter(Context context, List<String>list, boolean isClick) {
        this.mData = list;
        this.mContext = context;
        this.isClick = isClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_unboxing_imageitem, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        //设置加载中的图片样式
        if (NewFeature.timeline_img_quality != NewFeature.thumbnail_quality) {
            setImgSize(mData, mContext, viewHolder.norImg);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        FillContent.fillImageList(mContext, mData, position, holder.norImg, holder.imageLabel, isClick, isSave);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setData(ArrayList<String> data) {
        this.mData = data;
    }

    public void setSave(boolean isSave){
        this.isSave = isSave;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//        public SubsamplingScaleImageView longImg;
        public ImageView norImg;
//        public GifImageView gifImg;
        public ImageView imageLabel;

        public ViewHolder(View itemView) {
            super(itemView);
//            longImg = (SubsamplingScaleImageView) itemView.findViewById(R.id.longImg);
            norImg = (ImageView) itemView.findViewById(R.id.norImg);
//            gifImg = (GifImageView) itemView.findViewById(R.id.gifView);
            imageLabel = (ImageView) itemView.findViewById(R.id.imageType);
        }
    }

    /**
     * 根据图片的数量，设置不同的尺寸
     *
     * @param datas
     * @param context
     * @param norImg
     */
    private static void setImgSize(List<String> datas, Context context, ImageView norImg) {
        if (datas == null || datas.size() == 0) {
            return;
        }

        if (datas.size() == 1) {
            setSingleImgSize(context, norImg);
        } else if (datas.size() == 2 || datas.size() == 4) {
            setDoubleImgSize(context, norImg);
        } else if (datas.size() == 3 || datas.size() >= 5) {
            setThreeImgSize(context, norImg);
        }
    }

    private static void setDoubleImgSize(Context context, ImageView norImg) {
        FrameLayout.LayoutParams norImgLayout = (FrameLayout.LayoutParams) norImg.getLayoutParams();
        norImgLayout.width = DensityUtils.getScreenWidth(context) / 3;
        norImgLayout.height = DensityUtils.getScreenWidth(context) / 3;
    }

    private static void setSingleImgSize(Context context, ImageView norImg) {
        FrameLayout.LayoutParams norImgLayout = (FrameLayout.LayoutParams) norImg.getLayoutParams();
        norImgLayout.width = DensityUtils.getScreenWidth(context) / 2;
        norImgLayout.height = DensityUtils.getScreenWidth(context) / 2;
    }

    private static void setThreeImgSize(Context context, ImageView norImg) {
        FrameLayout.LayoutParams norImgLayout = (FrameLayout.LayoutParams) norImg.getLayoutParams();
        norImgLayout.width = DensityUtils.getScreenWidth(context) / 3;
        norImgLayout.height = DensityUtils.getScreenWidth(context) / 3;
    }

    OnMyItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, int position);
    }

}

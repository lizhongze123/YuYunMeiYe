package cn.yuyun.yymy.ui.home.unboxing;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.yuyun.yymy.R;

public class ImageCommunicationAdapter extends RecyclerView.Adapter<ImageCommunicationAdapter.ViewHolder> {
    private List<String> mData;
    private Context mContext;
    private boolean isClick;
    private boolean isSave;

    public ImageCommunicationAdapter(Context context, List<String>list, boolean isClick) {
        this.mData = list;
        this.mContext = context;
        this.isClick = isClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_communication_imageitem, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
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
        public ImageView norImg;
        public ImageView imageLabel;

        public ViewHolder(View itemView) {
            super(itemView);
            norImg = (ImageView) itemView.findViewById(R.id.norImg);
            imageLabel = (ImageView) itemView.findViewById(R.id.imageType);
        }
    }

    OnMyItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, int position);
    }

}

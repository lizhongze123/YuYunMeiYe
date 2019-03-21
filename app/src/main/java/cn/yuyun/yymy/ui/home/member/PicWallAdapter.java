package cn.yuyun.yymy.ui.home.member;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.lzz.utils.ResoureUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultPicWall;
import cn.yuyun.yymy.http.result.RulesBean;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author lzz
 * @desc
 * @date 2018-01-25
 */
public class PicWallAdapter extends RecyclerView.Adapter<PicWallAdapter.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_pic_del;
    private Context mContext;
    private List<ResultPicWall> childList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public PicWallAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(RESOURCE_ID, parent, false);
        return new ViewHolder(rootView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindItem(childList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return childList.size();
    }

    public void notifyDataSetChanged(List<ResultPicWall> dataList) {
        this.childList.clear();
        this.childList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultPicWall> dataList) {
        this.childList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.childList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private PicWallAdapter.OnMyItemClickListener onItemClickListener;

        TextView tvDel;
        ImageView ivPic;
        int override;

        ViewHolder(View view, final PicWallAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            this.onItemClickListener = onItemClickListener;
            tvDel = (TextView) view.findViewById(R.id.item_tips);
            ivPic = (ImageView) view.findViewById(R.id.iv_avatar);
            override = ResoureUtils.getDimension(mContext, R.dimen.select_people_avatar);
        }

        private void bindItem(final ResultPicWall bean, final int position) {
            /*tvDel.setVisibility(View.GONE);
            GlideHelper.displayOverrideImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.thumb_url, override, override, ivPic);
            ivPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(bean, position);
                }
            });*/
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(ResultPicWall bean, int position);
    }

}
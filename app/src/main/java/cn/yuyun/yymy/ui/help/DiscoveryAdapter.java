package cn.yuyun.yymy.ui.help;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.StringUtil;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ActionBean;
import cn.yuyun.yymy.http.result.ResultDiscover;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author
 * @desc
 * @date
 */
public class DiscoveryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private int FAILED_IMG = R.drawable.photo_filter_image_empty;
    private int RESOURCE_ID_ONE = R.layout.item_discovery;
    private Context mContext;
    private List<ResultDiscover.ContentBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public DiscoveryAdapter(Context context){
        this.mContext = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(RESOURCE_ID_ONE, parent, false), onItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder headerViewHolder = (ViewHolder) holder;
        headerViewHolder.bindItem(dataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void notifyDataSetChanged(List<ResultDiscover.ContentBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultDiscover.ContentBean> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private DiscoveryAdapter.OnMyItemClickListener onItemClickListener;
        private LinearLayout rl;
        ImageView ivPicOne;
        TextView tvTitle, tvTime;
        private View line;
        ImageView ivAvatar;
        private FrameLayout flPic;
        int override;

        ViewHolder(View view,  DiscoveryAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            ivPicOne = (ImageView) view.findViewById(R.id.iv_pic_one);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
            flPic = (FrameLayout) view.findViewById(R.id.fl_pic);
            line = view.findViewById(R.id.line);
            this.onItemClickListener = onItemClickListener;
            override = ResoureUtils.getDimension(mContext, R.dimen.item_pic);
            rl = (LinearLayout) view.findViewById(R.id.rl);
        }

        private void bindItem(final ResultDiscover.ContentBean bean, final int position) {
            TextViewUtils.setTextOrEmpty(tvTitle, bean.title);
            TextViewUtils.setTextOrEmpty(tvTime, StringFormatUtils.getUserDate(bean.createTime));
            if(bean.appDiscoverImagesVoList.size() == 0){
                flPic.setVisibility(View.GONE);
            }else{
                flPic.setVisibility(View.VISIBLE);

                if(!TextUtils.isEmpty(bean.appDiscoverImagesVoList.get(0).thumbUrl)){
                    if(bean.appDiscoverImagesVoList.get(0).thumbUrl.startsWith(mContext.getString(R.string.HTTP))){
                        GlideHelper.displayOverrideImage(mContext, bean.appDiscoverImagesVoList.get(0).thumbUrl, override, override, ivPicOne, FAILED_IMG);
                    }else{
                        GlideHelper.displayOverrideImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.appDiscoverImagesVoList.get(0).thumbUrl, override, override, ivPicOne, FAILED_IMG);
                    }
                }

            }
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
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
        void onItemClick(View view, ResultDiscover.ContentBean bean, int position);
    }

}
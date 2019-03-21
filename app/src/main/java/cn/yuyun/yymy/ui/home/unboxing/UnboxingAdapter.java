package cn.yuyun.yymy.ui.home.unboxing;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.TextViewUtils;
import cn.lzz.utils.content.ContentTextUtil;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultUnboxingBean;
import cn.yuyun.yymy.http.result.ResultUnboxingLabel;
import cn.yuyun.yymy.utils.CommonUtils;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.FlowLayout;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author
 * @desc
 * @date
 */
public class UnboxingAdapter extends RecyclerView.Adapter<UnboxingAdapter.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_unboxing_normal;
    private Context mContext;
    private List<ResultUnboxingBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    private boolean isClickLike;

    public UnboxingAdapter(Context context) {
        this.mContext = context;
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
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            ViewHolder viewHolder = holder;
            if(isClickLike){
                if (dataList.get(position).likeFlag) {
                    viewHolder.ivLike.setSelected(true);
                } else {
                    viewHolder.ivLike.setSelected(false);
                }
                viewHolder.tvLike.setText("已获得" + dataList.get(position).likeCount + "个赞，" + dataList.get(position).commentCount + "条评论");
            }else{
                if (dataList.get(position).collectFlag) {
                    viewHolder.ivFavorites.setSelected(true);
                } else {
                    viewHolder.ivFavorites.setSelected(false);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void notifyDataSetChanged(List<ResultUnboxingBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultUnboxingBean> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    public void remove(int pos) {
        this.dataList.remove(pos);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.profile_img)
        CircleImageView profileImg;
        @BindView(R.id.profile_name)
        TextView profileName;
        @BindView(R.id.profile_position)
        TextView profilePosition;
        @BindView(R.id.profile_time)
        TextView profileTime;
        @BindView(R.id.iv_del)
        ImageView ivDel;
        @BindView(R.id.popover_arrow)
        ImageView popoverArrow;
        @BindView(R.id.titlebar_layout)
        LinearLayout titlebarLayout;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.favorities_delete)
        TextView favoritiesDelete;
        @BindView(R.id.iv_comment)
        ImageView ivComment;
        @BindView(R.id.bottombar_comment)
        RelativeLayout bottombarComment;
        @BindView(R.id.iv_favorites)
        ImageView ivFavorites;
        @BindView(R.id.bottombar_favorites)
        RelativeLayout bottombarFavorites;
        @BindView(R.id.iv_like)
        ImageView ivLike;
        @BindView(R.id.tv_like)
        TextView tvLike;
        @BindView(R.id.bottombar_like)
        RelativeLayout bottombarLike;
        @BindView(R.id.origin_ub_layout)
        LinearLayout originUbLayout;
        @BindView(R.id.flowlayout)
        FlowLayout flowlayout;
        @BindView(R.id.ll_label)
        LinearLayout llLabel;
        @BindView(R.id.iv)
        ImageView iv;
        LayoutInflater mInflater;

        ViewHolder(View v, OnMyItemClickListener onItemClickListener) {
            super(v);
            ButterKnife.bind(this, v);
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        private void bindItem(final ResultUnboxingBean bean, final int position) {
            if (null != bean.shareOrderVo.pictures) {
                if(bean.shareOrderVo.pictures.size() != 0){

                    if (bean.shareOrderVo.pictures.get(0).startsWith(mContext.getString(R.string.HTTP))) {
                        GlideHelper.displayImage(mContext, bean.shareOrderVo.pictures.get(0), iv);
                    } else {
                        GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.shareOrderVo.pictures.get(0), iv);
                    }

                    iv.setVisibility(View.VISIBLE);

                }else{
                    iv.setVisibility(View.GONE);
                }

            } else {
                iv.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(bean.shareOrderVo.createPersonAvatar)) {
                if (bean.shareOrderVo.createPersonAvatar.startsWith(mContext.getString(R.string.HTTP))) {
                    GlideHelper.displayImage(mContext, bean.shareOrderVo.createPersonAvatar, profileImg);
                } else {
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.shareOrderVo.createPersonAvatar, profileImg);
                }
            } else {
                GlideHelper.displayImage(mContext, R.drawable.avatar_default_female, profileImg);
            }
            TextViewUtils.setTextOrEmpty(profileName, bean.shareOrderVo.createPersonName);
            TextViewUtils.setTextOrEmpty(profileTime, DateTimeUtils.StringToDate(bean.shareOrderVo.createTime, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_DATETIME_TWO));
            TextViewUtils.setTextOrEmpty(profilePosition, bean.shareOrderVo.createPersonPosition);
            tvContent.setText(ContentTextUtil.getWeiBoContent(bean.shareOrderVo.content, mContext, tvContent));

            ivFavorites.setSelected(bean.collectFlag);
            ivLike.setSelected(bean.likeFlag);
            tvLike.setText("已获得" + bean.likeCount + "个赞，" + bean.commentCount + "条评论");

            bottombarLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onItemClickListener) {
                        isClickLike = true;
                        onItemClickListener.onLike(bean, position);
                    }
                }
            });
            bottombarFavorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onItemClickListener) {
                        isClickLike = false;
                        onItemClickListener.onFavorites(bean, position);
                    }
                }
            });
            bottombarComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onItemClickListener) {
                        onItemClickListener.onComment(bean, position);
                    }
                }
            });
            originUbLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onItemClickListener) {
                        onItemClickListener.onItemClick(v, bean, position);
                    }
                }
            });

            popoverArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    originUbLayout.setDrawingCacheEnabled(true);
                    originUbLayout.buildDrawingCache(true);
                }
            });

            if(null != bean.shareOrderVo.labelList && bean.shareOrderVo.labelList.size() != 0){
                llLabel.setVisibility(View.VISIBLE);
                flowlayout.removeAllViewsInLayout();
                for (int i = 0; i < bean.shareOrderVo.labelList.size(); i++) {
                    final ResultUnboxingLabel.UnboxingLabelBean labelBean = bean.shareOrderVo.labelList.get(i);
                    TextView rv = (TextView) mInflater.inflate(R.layout.item_unboxing_label_add3, flowlayout, false);
                    rv.setText(labelBean.labelName);
                    rv.setTextColor(Color.parseColor(CommonUtils.getRandomColor()));
                    rv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onItemClickListener.onLabel(labelBean);
                        }
                    });
                    flowlayout.addView(rv);
                }
            }else{
                llLabel.setVisibility(View.GONE);
            }
        }
    }

    private void btnLikeAnimation(final TextView tv, final ResultUnboxingBean bean, final int position) {
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -150);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setDuration(1200);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                tv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tv.setVisibility(View.GONE);
                onItemClickListener.onLike(bean, position);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        tv.startAnimation(animationSet);
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, ResultUnboxingBean bean, int position);

        void onFavorites(ResultUnboxingBean bean, int position);

        void onLike(ResultUnboxingBean bean, int position);

        void onComment(ResultUnboxingBean bean, int position);

        void onLabel(ResultUnboxingLabel.UnboxingLabelBean bean);
    }

    public void refreshFavorites(int position, ResultUnboxingBean bean) {
        dataList.set(position, bean);
        notifyItemChanged(position, "lzz");
    }

    public void refreshLike(int position, ResultUnboxingBean bean) {
        dataList.set(position, bean);
        notifyItemChanged(position, "lzz");
    }

}

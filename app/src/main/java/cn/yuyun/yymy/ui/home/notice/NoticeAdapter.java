package cn.yuyun.yymy.ui.home.notice;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.StringUtil;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.bean.FavoritesStatus;
import cn.yuyun.yymy.bean.LikeStatus;
import cn.yuyun.yymy.bean.RecordType;
import cn.yuyun.yymy.http.result.ActionBean;
import cn.yuyun.yymy.http.result.RecordBean;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.home.train.TrainAdapter;
import cn.yuyun.yymy.ui.home.train.TrainBean;
import cn.yuyun.yymy.ui.home.unboxing.FillContent;
import cn.yuyun.yymy.utils.GlideHelper;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author
 * @desc
 * @date
 */
public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

    private int FAILED_IMG = R.drawable.photo_filter_image_empty;
    private int RESOURCE_ID = R.layout.item_notice;
    private Context mContext;
    private List<NoticeBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;
    private boolean isClickLike;

    public NoticeAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(RESOURCE_ID, parent, false), onItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ViewHolder headerViewHolder = holder;
        headerViewHolder.bindItem(dataList.get(position), position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            ViewHolder viewHolder = holder;
            if (isClickLike) {
                viewHolder.ivLike.setSelected(dataList.get(position).likeFlag);
                viewHolder.tvLike.setText("已获得" + dataList.get(position).likeCount + "个赞，" + dataList.get(position).commentCount + "条评论");
            } else {
                viewHolder.ivFavorites.setSelected(dataList.get(position).collectFlag);
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void notifyDataSetChanged(List<NoticeBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<NoticeBean> dataList) {
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

    public boolean isDel;
    private boolean[] isCheckedArray;

    public void setDelVisiable(boolean isDel) {
        this.isDel = isDel;
        isCheckedArray = new boolean[this.dataList.size()];
        notifyDataSetChanged();
    }

    public boolean getDelVisiable() {
        return isDel;
    }

    /**
     * 获得选中条目的结果
     */
    public List<NoticeBean> getSelectedItem() {
        List<NoticeBean> selectList = new ArrayList<>();
        for (int i = 0, len = dataList.size(); i < len; i++) {
            if (isCheckedArray[i]) {
                selectList.add(dataList.get(i));
            }
        }
        return selectList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_avatar)
        CircleImageView ivAvatar;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.ll_pic)
        LinearLayout llPic;
        @BindView(R.id.iv_comment)
        ImageView ivComment;
        @BindView(R.id.iv_favorites)
        ImageView ivFavorites;
        @BindView(R.id.iv_like)
        ImageView ivLike;
        @BindView(R.id.iv_pic_one)
        ImageView ivPicOne;
        @BindView(R.id.iv_pic_two)
        ImageView ivPicTwo;
        @BindView(R.id.iv_pic_three)
        ImageView ivPicThree;
        @BindView(R.id.tv_like)
        TextView tvLike;
        @BindView(R.id.bottombar_like)
        RelativeLayout bottombarLike;
        @BindView(R.id.bottombar_favorites)
        RelativeLayout bottombarFavorites;
        @BindView(R.id.bottombar_comment)
        RelativeLayout bottombarComment;
        @BindView(R.id.ll)
        LinearLayout ll;
        @BindView(R.id.cb)
        CheckBox checkBox;

        private OnMyItemClickListener onItemClickListener;
        int override;

        ViewHolder(View view, OnMyItemClickListener onItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.onItemClickListener = onItemClickListener;
            override = ResoureUtils.getDimension(mContext, R.dimen.item_pic);
        }

        private void bindItem(final NoticeBean bean, final int position) {
            if (isDel) {
                checkBox.setVisibility(View.VISIBLE);
            } else {
                checkBox.setVisibility(View.GONE);
            }

            if (null != bean) {
                if (bean.readFlag) {
                    tvName.setTextColor(ResoureUtils.getColor(mContext, R.color.read));
                    tvTitle.setTextColor(ResoureUtils.getColor(mContext, R.color.read));
                    tvContent.setTextColor(ResoureUtils.getColor(mContext, R.color.read));
                    tvTime.setTextColor(ResoureUtils.getColor(mContext, R.color.read));
                } else {
                    tvName.setTextColor(ResoureUtils.getColor(mContext, R.color.unread));
                    tvTitle.setTextColor(ResoureUtils.getColor(mContext, R.color.unread));
                    tvContent.setTextColor(ResoureUtils.getColor(mContext, R.color.unread));
                    tvTime.setTextColor(ResoureUtils.getColor(mContext, R.color.unread));
                }
                tvTitle.setVisibility(View.GONE);
                TextViewUtils.setTextOrEmpty(tvName, bean.noticeVo.createPersonName);
                TextViewUtils.setTextOrEmpty(tvTime, DateTimeUtils.StringToDate(bean.noticeVo.createTime, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_DATETIME_TWO));
                TextViewUtils.setTextOrEmpty(tvContent, bean.noticeVo.content);

                if (null != bean.noticeVo.pictures && bean.noticeVo.pictures.size() > 0) {
                    llPic.setVisibility(View.VISIBLE);
                    ivPicOne.setVisibility(View.INVISIBLE);
                    ivPicTwo.setVisibility(View.INVISIBLE);
                    ivPicThree.setVisibility(View.INVISIBLE);
                    for (int i = 0; i < bean.noticeVo.pictures.size(); i++) {
                        if (i == 0) {
                            ivPicOne.setVisibility(View.VISIBLE);
                            ivPicTwo.setVisibility(View.INVISIBLE);
                            ivPicThree.setVisibility(View.INVISIBLE);
                            if (bean.noticeVo.pictures.get(i).startsWith(mContext.getString(R.string.HTTP))) {
                                GlideHelper.displayImage(mContext, bean.noticeVo.pictures.get(i), ivPicOne, R.color.loadding_img_bg);
                            } else {
                                GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.noticeVo.pictures.get(i), ivPicOne, R.color.loadding_img_bg);
                            }
                        } else if (i == 1) {
                            ivPicOne.setVisibility(View.VISIBLE);
                            ivPicTwo.setVisibility(View.VISIBLE);
                            ivPicThree.setVisibility(View.INVISIBLE);
                            if (bean.noticeVo.pictures.get(i).startsWith(mContext.getString(R.string.HTTP))) {
                                GlideHelper.displayImage(mContext, bean.noticeVo.pictures.get(i), ivPicTwo, R.color.loadding_img_bg);
                            } else {
                                GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.noticeVo.pictures.get(i), ivPicTwo, R.color.loadding_img_bg);
                            }
                        } else if (i == 2) {
                            ivPicOne.setVisibility(View.VISIBLE);
                            ivPicTwo.setVisibility(View.VISIBLE);
                            ivPicThree.setVisibility(View.VISIBLE);
                            if (bean.noticeVo.pictures.get(i).startsWith(mContext.getString(R.string.HTTP))) {
                                GlideHelper.displayImage(mContext, bean.noticeVo.pictures.get(i), ivPicThree, R.color.loadding_img_bg);
                            } else {
                                GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.noticeVo.pictures.get(i), ivPicThree, R.color.loadding_img_bg);
                            }
                        }
                    }
                } else {
                    llPic.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(bean.noticeVo.createPersonAvatar)) {
                    if (bean.noticeVo.createPersonAvatar.startsWith(mContext.getString(R.string.HTTP))) {
                        GlideHelper.displayImage(mContext, bean.noticeVo.createPersonAvatar, ivAvatar);
                    } else {
                        GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.noticeVo.createPersonAvatar, ivAvatar);
                    }
                }
                ivFavorites.setSelected(bean.collectFlag);
                ivLike.setSelected(bean.likeFlag);
                tvLike.setText("已获得" + bean.likeCount + "个赞，" + bean.commentCount + "条评论");
            }

            if (null != isCheckedArray) {
                checkBox.setOnCheckedChangeListener(null);
                checkBox.setChecked(isCheckedArray[position]);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        isCheckedArray[position] = isChecked;
                        onItemClickListener.onDel(getSelectedItem().size());
                    }
                });
            }

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
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onItemClickListener) {
                        if (isDel) {
                            checkBox.toggle();
                        } else {
                            onItemClickListener.onItemClick(v, bean, position);
                        }
                    }
                }
            });
        }
}

public interface OnMyItemClickListener {
    void onItemClick(View view, NoticeBean bean, int position);

    void onFavorites(NoticeBean bean, int position);

    void onLike(NoticeBean bean, int position);

    void onComment(NoticeBean bean, int position);

    void onDel(int count);
}

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void refreshFavorites(int position, NoticeBean bean) {
        dataList.set(position, bean);
        notifyItemChanged(position, "lzz");
    }

    public void refreshLike(int position, NoticeBean bean) {
        dataList.set(position, bean);
        notifyItemChanged(position, "lzz");
    }

    public void refreshRead(int position, NoticeBean bean) {
        dataList.set(position, bean);
        notifyItemChanged(position);
    }

}
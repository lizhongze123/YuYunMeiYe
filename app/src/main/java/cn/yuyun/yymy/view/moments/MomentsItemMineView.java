package cn.yuyun.yymy.view.moments;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.StringUtil;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultWork;
import cn.yuyun.yymy.http.result.ResultWorkComment;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 *
 * 朋友圈中公开的item布局
 *
 */

public class MomentsItemMineView extends LinearLayout {

    private TextView tvMonth;
    private TextView tvDay;
    private TextView tvYear;
    private TextView tvContent;
    private TextView tvCount;
    private TextView tvCount2;
    private ImageView ivThumbnail;

    private RelativeLayout bottombarComment;
    private TextView tvComment;
    private ImageView ivComment;
    private RelativeLayout bottombarLike;
    private TextView tvLike;
    private ImageView ivLike;
    private TextView tvCountTotal;
    private int FAILED_IMG = R.drawable.photo_filter_image_empty;
    private Context mContext;

    public MomentsItemMineView(Context context) {
        super(context);
        mContext = context;
        init(context);
    }

    public MomentsItemMineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(context);
    }

    public MomentsItemMineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_moments_public_item_mine, this);
        tvMonth = (TextView) findViewById(R.id.tv_month);
        tvDay = (TextView) findViewById(R.id.tv_day);
        tvYear = (TextView) findViewById(R.id.tv_year);
        tvContent = (TextView) findViewById(R.id.tv_content);
        tvCount = (TextView) findViewById(R.id.tv_count);
        tvCount2 = (TextView) findViewById(R.id.tv_count2);
        ivThumbnail = (ImageView) findViewById(R.id.iv_thumbnail);
        bottombarComment = (RelativeLayout) findViewById(R.id.bottombar_comment);
        tvComment = (TextView) findViewById(R.id.tv_comment);
        ivComment = (ImageView) findViewById(R.id.iv_comment);
        bottombarLike = (RelativeLayout) findViewById(R.id.bottombar_like);
        ivLike = (ImageView) findViewById(R.id.iv_like);
        tvCountTotal = (TextView) findViewById(R.id.tv_countTotal);
    }

    private List<ResultWorkComment> result = new ArrayList();

    public void initView(final ResultWork bean) {
        this.result = result;
        ivLike.setSelected(bean.selfLikeFlag);

        tvCountTotal.setText("已获" + bean.likeCount + "个赞，" + bean.commentCount + "条评论");
        if(null != bean.workReportDetailVo){
            TextViewUtils.setTextOrEmpty(tvContent, bean.workReportDetailVo.content);
            TextViewUtils.setTextOrEmpty(tvMonth, StringFormatUtils.getUserDate(bean.workReportDetailVo.createTime, DateTimeUtils.FORMAT_M_EN) + "月");
            TextViewUtils.setTextOrEmpty(tvDay, StringFormatUtils.getUserDate(bean.workReportDetailVo.createTime, DateTimeUtils.FORMAT_D_EN));
            if (bean.workReportDetailVo.picture.size() > 0) {
                ivThumbnail.setVisibility(View.VISIBLE);
                tvCount2.setVisibility(View.VISIBLE);
                tvCount.setVisibility(INVISIBLE);
                tvCount.setText("共" + bean.workReportDetailVo.picture.size() + "张");
                if (bean.workReportDetailVo.picture.get(0).startsWith(mContext.getString(R.string.HTTP))) {
                    GlideHelper.displayImage(mContext, bean.workReportDetailVo.picture.get(0), ivThumbnail, FAILED_IMG);
                } else {
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.workReportDetailVo.picture.get(0), ivThumbnail, FAILED_IMG);
                }
                tvContent.setBackgroundColor(ResoureUtils.getColor(mContext, R.color.white));
            }else{
                ivThumbnail.setVisibility(View.GONE);
                tvCount2.setVisibility(View.GONE);
                tvCount.setVisibility(GONE);
                tvContent.setBackgroundColor(ResoureUtils.getColor(mContext, R.color.work_bg));
            }
        }

        bottombarLike.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onMenuClickListener) {
                    onMenuClickListener.onLike(bean);
                }
            }
        });

        bottombarComment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onMenuClickListener) {
                    onMenuClickListener.onComment(bean);
                }
            }
        });
    }

    public void updateLikeView(List<ResultWorkComment> result) {

    }

    private OnMenuClickListener onMenuClickListener;

    public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        this.onMenuClickListener = onMenuClickListener;
    }

    public interface OnMenuClickListener {

        void onItemClick(View view, ResultWork bean);

        void onFavorites(ResultWork bean);

        void onLike(ResultWork bean);

        void onComment(ResultWork bean);

    }

    public void setTimeVisibility(boolean isVisibility){
        if(isVisibility){
            tvDay.setVisibility(VISIBLE);
            tvMonth.setVisibility(VISIBLE);
            tvYear.setVisibility(VISIBLE);
        }else{
            tvDay.setVisibility(INVISIBLE);
            tvMonth.setVisibility(INVISIBLE);
            tvYear.setVisibility(INVISIBLE);
        }
    }

    public void setLiked(boolean isLike){
        ivLike.setSelected(isLike);
    }

    public boolean getLiked(){
        return ivLike.isSelected();
    }

    public String getMyComment(){
        for (int i = 0; i < result.size(); i++) {
            if(result.get(i).approve_person_name.equals(UserfoPrefs.getInstance(mContext).getStaffName())){
               return result.get(i).approve_agreement;
            }
        }
        return null;
    }

}

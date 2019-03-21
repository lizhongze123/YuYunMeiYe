package cn.yuyun.yymy.view.moments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.LogUtils;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.StringUtil;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.bean.MomentsActionType;
import cn.yuyun.yymy.http.result.ResultWork;
import cn.yuyun.yymy.http.result.ResultWork2;
import cn.yuyun.yymy.http.result.ResultWorkComment;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.home.unboxing.FillContent;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 *
 */

public class MomentsItemView extends LinearLayout {

    private ImageView ivAvatar;
    private TextView tvNick;
    private TextView tvType;
    private TextView tvTime;
    private TextView tvContent;
    private RecyclerView rv;
    private RelativeLayout bottombarComment;
    private TextView tvComment;
    private ImageView ivComment;
    private RelativeLayout bottombarLike;
    private TextView tvLike;
    private ImageView ivLike;
    private RelativeLayout rlBottom;
    private TextView tvCommentContent;
    private TextView tvLikePerson;
    private RelativeLayout rlLikePerson;
    private int FAILED_IMG = R.drawable.photo_filter_image_empty;
    private Context mContext;

    public MomentsItemView(Context context) {
        super(context);
        mContext = context;
        init(context);
    }

    public MomentsItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(context);
    }

    public MomentsItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_moments_public_item, this);
        ivAvatar = (ImageView) findViewById(R.id.iv_avatar);
        tvNick = (TextView) findViewById(R.id.tv_nick);
        tvType = (TextView) findViewById(R.id.tv_type);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvContent = (TextView) findViewById(R.id.tv_content);
        tvComment = (TextView) findViewById(R.id.tv_comment);
        rv = (RecyclerView) findViewById(R.id.rv);
        bottombarComment = (RelativeLayout) findViewById(R.id.bottombar_comment);
        tvComment = (TextView) findViewById(R.id.tv_comment);
        ivComment = (ImageView) findViewById(R.id.iv_comment);
        bottombarLike = (RelativeLayout) findViewById(R.id.bottombar_like);
        tvLike = (TextView) findViewById(R.id.tv_like);
        ivLike = (ImageView) findViewById(R.id.iv_like);
        rlBottom = (RelativeLayout) findViewById(R.id.rl_bottom);
        rlLikePerson = (RelativeLayout) findViewById(R.id.rl_likePerson);
        tvCommentContent = (TextView) findViewById(R.id.tv_commentContent);
        tvLikePerson = (TextView) findViewById(R.id.tv_likePerson);
    }

    private List<ResultWorkComment> result = new ArrayList();

    public void initView(final ResultWork bean) {
        this.result = result;
        ivLike.setImageDrawable(ResoureUtils.getDrawable(mContext, R.drawable.wb_like_sel));
        ivComment.setImageDrawable(ResoureUtils.getDrawable(mContext, R.drawable.wb_comment));
        bottombarLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onMenuClickListener) {
                    onMenuClickListener.onLike(bean);
                }
            }
        });
        bottombarComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onMenuClickListener) {
                    onMenuClickListener.onComment(bean);
                }
            }
        });
        ivLike.setSelected(bean.selfLikeFlag);
        if(null != bean.workReportDetailVo){

            if(bean.readFlag){
                tvContent.setTextColor(Color.parseColor("#5e5e5e"));
            }else{
                tvContent.setTextColor(Color.parseColor("#3f3f3f"));
            }

            TextViewUtils.setTextOrEmpty(tvNick, bean.workReportDetailVo.createPersonName);
            TextViewUtils.setTextOrEmpty(tvContent, bean.workReportDetailVo.content);
            TextViewUtils.setTextOrEmpty(tvType, bean.workReportDetailVo.type.desc);
            TextViewUtils.setTextOrEmpty(tvTime, DateTimeUtils.StringToDate(bean.workReportDetailVo.createTime, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_DATETIME_TWO));
        }
        if (!TextUtils.isEmpty(bean.workReportDetailVo.createPersonAvatar)) {
            if (bean.workReportDetailVo.createPersonAvatar.startsWith(mContext.getString(R.string.HTTP))) {
                GlideHelper.displayImage(mContext, bean.workReportDetailVo.createPersonAvatar, ivAvatar);
            }else{
                GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.workReportDetailVo.createPersonAvatar, ivAvatar);
            }
        }else{
            GlideHelper.displayImage(mContext, R.drawable.avatar_default_female, ivAvatar);
        }
        if (null != bean.workReportDetailVo.picture) {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < bean.workReportDetailVo.picture.size(); i++) {
                if (bean.workReportDetailVo.picture.get(i).startsWith(mContext.getString(R.string.HTTP))) {
                    list.add(bean.workReportDetailVo.picture.get(i));
                } else {
                    list.add(mContext.getString(R.string.image_url_prefix) + bean.workReportDetailVo.picture.get(i));
                }
            }
            rv.setVisibility(View.VISIBLE);
            FillContent.fillComunicationImgList(list, mContext, rv, true, true);
        } else {
            rv.setVisibility(View.GONE);
        }
        initGoodView(tvLikePerson, bean.workReportlikeVos);
        //设置评论
        initCommentView(tvCommentContent, bean.workReportApproveVos);
    }


    public void updateLikeView(List<ResultWork.WorkReportApproveVosBean> result) {
        initGoodView(tvLikePerson, result);
    }

    public void updateCommentView(List<ResultWork.WorkReportApproveVosBean> result) {
        initCommentView(tvCommentContent, result);
    }

   /* private void initGoodView(TextView textView, List<ResultWork.WorkReportApproveVosBean> result) {
        if (result.size() == 0) {
            rlLikePerson.setVisibility(GONE);
            textView.setVisibility(GONE);
            return;
        } else {
            rlLikePerson.setVisibility(VISIBLE);
            textView.setVisibility(VISIBLE);
        }
        TextViewUtils.setTextOrEmpty(textView, person);
    }*/

    private void initGoodView(TextView textView, List<ResultWork.WorkReportApproveVosBean> result) {
        if(null == result){
            textView.setVisibility(View.GONE);
            rlLikePerson.setVisibility(GONE);
            return;
        }
        List<ResultWork.WorkReportApproveVosBean> list = new ArrayList<>();
        if (result.size() == 0) {
            textView.setVisibility(View.GONE);
            rlLikePerson.setVisibility(GONE);
            return;
        } else {
            for (int i = 0; i < result.size(); i++) {
                if(result.get(i).status == 1){
                    list.add(result.get(i));
                }
            }
            if(list.size() == 0){
                textView.setVisibility(View.GONE);
                rlLikePerson.setVisibility(GONE);
                return;
            }else{
                textView.setVisibility(View.VISIBLE);
                rlLikePerson.setVisibility(VISIBLE);
            }
        }

        SpannableStringBuilder ssb = new SpannableStringBuilder();
        int start = 0;
        for (int i = 0; i < list.size(); i++) {
            String nick = list.get(i).createPersonName;
            if (i != (list.size() - 1) && list.size() > 1) {
                ssb.append(nick + ",");
            } else {
                ssb.append(nick);
            }
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#576587"));
            ssb.setSpan(foregroundColorSpan, start,
                    start + nick.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            start = ssb.length();
        }
        textView.setText(ssb);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void initCommentView(TextView textView, List<ResultWork.WorkReportApproveVosBean> result) {
        if(null == result){
            return;
        }
        List<ResultWork.WorkReportApproveVosBean> list = new ArrayList<>();
        if (result.size() == 0) {
            textView.setVisibility(View.GONE);
            return;
        } else {
            for (int i = 0; i < result.size(); i++) {
                if(!TextUtils.isEmpty(result.get(i).content)){
                    list.add(result.get(i));
                }
            }
            if(list.size() == 0){
                textView.setVisibility(View.GONE);
                return;
            }else{
                textView.setVisibility(View.VISIBLE);
            }
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        int start = 0;

        for (int i = 0; i < list.size(); i++) {
            if(!TextUtils.isEmpty(list.get(i).content)){
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#576587"));
                String nick = list.get(i).createPersonName;
                String content = list.get(i).content;
                String content_0 = "";
                String content_1 = ": " + content;
                String content_2 = ": " + content + "\n";
                if (i == (list.size() - 1) || (list.size() == 1 && i == 0)) {
                    ssb.append(nick + content_1);
                    content_0 = content_1;
                } else {
                    ssb.append(nick + content_2);
                    content_0 = content_2;
                }
                ssb.setSpan(foregroundColorSpan, start,
                        start + nick.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                start = ssb.length();
            }
        }

        textView.setText(ssb);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
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

    public void setLiked(boolean isLiked){
        ivLike.setSelected(isLiked);
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

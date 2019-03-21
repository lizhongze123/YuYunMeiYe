package cn.yuyun.yymy.ui.home.unboxing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.NoneProgressSubscriber;
import com.example.http.PageInfo;
import com.example.http.ProgressSubscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.TextViewUtils;
import cn.lzz.utils.content.ContentTextUtil;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.bean.FavoritesStatus;
import cn.yuyun.yymy.bean.LikeStatus;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.event.RefreshUnboxingEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestDelUnboxing;
import cn.yuyun.yymy.http.request.RequestUnboxingComment;
import cn.yuyun.yymy.http.request.RequestUnboxingLike;
import cn.yuyun.yymy.http.result.ResultUnboxingBean;
import cn.yuyun.yymy.http.result.ResultUnboxingComment;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.SoftKeyboardStateHelper;
import cn.yuyun.yymy.view.WarnningDialog;
import de.hdodenhof.circleimageview.CircleImageView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;

/**
 * @author
 * @desc
 * @date
 */
public class UnboxingDetailActivity extends BaseActivity {

    @BindView(R.id.profile_img)
    ImageView profileImg;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    @BindView(R.id.profile_name)
    TextView profileName;
    @BindView(R.id.profile_time)
    TextView profileTime;
    @BindView(R.id.weiboComeFrom)
    TextView weiboComeFrom;
    @BindView(R.id.iv_del)
    ImageView ivDel;
    @BindView(R.id.popover_arrow)
    ImageView popoverArrow;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.rv_image)
    RecyclerView rvImage;
    @BindView(R.id.splitLine)
    ImageView splitLine;
    @BindView(R.id.favorities_delete)
    TextView favoritiesDelete;
    @BindView(R.id.rv_comment)
    RecyclerView rvComment;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.et_comment)
    EditText etComment;
    @BindView(R.id.iv_favorites)
    ImageView ivFavorites;
    @BindView(R.id.bottombar_favorites)
    RelativeLayout bottombarFavorites;
    @BindView(R.id.iv_like)
    ImageView ivLike;
    @BindView(R.id.bottombar_like)
    RelativeLayout bottombarLike;
    @BindView(R.id.re_edittext)
    LinearLayout reEdittext;
    @BindView(R.id.origin_weibo_layout)
    RelativeLayout originWeiboLayout;
    private ResultUnboxingBean bean;

    private InputMethodManager inputMethodManager;
    private SoftKeyboardStateHelper softKeyboardStateHelper;
    private SoftKeyboardStateHelper.SoftKeyboardStateListener softKeyboardStateListener;
    private UnboxingCommentListAdapter unboxingCommentListAdapter;
    private WarnningDialog warnningDialog;

    private int type;
    public static int TYPE_NORMAL = 0;
    public static int TYPE_COLLECTION = 1;

    public static Intent startUnboxingDetailActivity(Context context, ResultUnboxingBean bean) {
        return new Intent(context, UnboxingDetailActivity.class).putExtra(EXTRA_DATA, bean).putExtra(EXTRA_DATA2, TYPE_NORMAL);
    }

    public static Intent startUnboxingDetailActivityFromCollection(Context context, ResultUnboxingBean bean) {
        return new Intent(context, UnboxingDetailActivity.class).putExtra(EXTRA_DATA, bean).putExtra(EXTRA_DATA2, TYPE_COLLECTION);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_unboxing_detail);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initData();
    }

    private void initViews() {
        titleBar.setTilte("晒单详情");
        bean = (ResultUnboxingBean) getIntent().getSerializableExtra(EXTRA_DATA);
        type = getIntent().getIntExtra(EXTRA_DATA2, 0);
        if (UserfoPrefs.getInstance(mContext).getPermission() || bean.selfCreateFlag) {
            titleBar.setRightIcon(R.drawable.ic_del_one);
            titleBar.setRightOnClicker(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(LoginInfoPrefs.getInstance(mContext).getStaffId())) {
                        showTextDialog("该账号没有绑定员工", false);
                        return;
                    }
                    warnningDialog.show();
                }
            });
            warnningDialog = new WarnningDialog(mContext, "确定删除？");
            warnningDialog.setOnPositiveListener(new WarnningDialog.OnDialogListener() {
                @Override
                public void onPositive() {
                    del();
                    warnningDialog.dismiss();
                }

                @Override
                public void onNegative() {

                }
            });
        }
        bottombarLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitUnboxingLike();
            }
        });
        bottombarFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitUnboxingFavorites();
            }
        });
        rvComment.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        unboxingCommentListAdapter = new UnboxingCommentListAdapter(this);
        rvComment.setAdapter(unboxingCommentListAdapter);
        etComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {

                    String comment = etComment.getText().toString();
                    if (TextUtils.isEmpty(comment)) {
                        showToast("请输入评论内容");
                    }else{
                        hideInputMenu();
                        comment(etComment.getText().toString());
                        etComment.setText("");
                    }
                }
                return false;
            }
        });
        initSoftKeyboard();
    }

    private void initData() {
        if (null != bean.shareOrderVo.pictures) {
            for (int i = 0; i < bean.shareOrderVo.pictures.size(); i++) {
                String url = bean.shareOrderVo.pictures.get(i);
                if(!url.startsWith(mContext.getString(R.string.HTTP))){
                    bean.shareOrderVo.pictures.set(i, mContext.getString(R.string.image_url_prefix) + url);
                }
            }
            FillContent.fillNineImgList(bean.shareOrderVo.pictures, mContext, rvImage, true, true);
        }
        if (!TextUtils.isEmpty(bean.shareOrderVo.createPersonAvatar)) {
            if (bean.shareOrderVo.createPersonAvatar.startsWith(mContext.getString(R.string.HTTP))) {
                GlideHelper.displayImage(mContext, bean.shareOrderVo.createPersonAvatar, profileImg);
            } else {
                GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.shareOrderVo.createPersonAvatar, profileImg);
            }
        }
        TextViewUtils.setTextOrEmpty(profileName, bean.shareOrderVo.createPersonName);
        TextViewUtils.setTextOrEmpty(profileTime, DateTimeUtils.StringToDate(bean.shareOrderVo.createTime, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_DATETIME_TWO));
        TextViewUtils.setTextOrEmpty(tvPosition, bean.shareOrderVo.createPersonPosition);
        tvContent.setText(ContentTextUtil.getWeiBoContent(bean.shareOrderVo.content, mContext, tvContent));
        ivFavorites.setSelected(bean.collectFlag);
        ivLike.setSelected(bean.likeFlag);
        getCommentList();
        getUnboxingDetail();
    }

    private void del() {

        RequestDelUnboxing body = new RequestDelUnboxing();
        body.status = -1;
        List<Integer> list = new ArrayList<>();
        list.add(bean.shareOrderVo.shareOrderId);
        body.shareOrderIdList = list;

        AppModelFactory.model().delUnboxing(body, new ProgressSubscriber<Object>(mContext) {
            @Override
            public void onNext(Object o) {
                showToast("已删除");
                EvenManager.sendEvent(new RefreshUnboxingEvent());
                finish();
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.getMessage());
            }
        });
    }

    private void initSoftKeyboard() {
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        softKeyboardStateHelper = new SoftKeyboardStateHelper(findViewById(R.id.origin_weibo_layout));
        softKeyboardStateHelper.addSoftKeyboardStateListener(softKeyboardStateListener);
    }

    public void showInputMenu() {
        reEdittext.setVisibility(View.VISIBLE);
        etComment.requestFocus();
        inputMethodManager.showSoftInput(etComment, 0);
    }

    private void comment(String content) {
        if (TextUtils.isEmpty(LoginInfoPrefs.getInstance(mContext).getStaffId())) {
            showTextDialog(getString(R.string.no_bind_staff), false);
            return;
        }
        RequestUnboxingComment requestBean = new RequestUnboxingComment();
        requestBean.content = content;
        requestBean.shareOrderId = bean.shareOrderVo.shareOrderId;

        AppModelFactory.model().commentUnboxing(requestBean, new ProgressSubscriber<Object>(mContext) {
            @Override
            public void onNext(Object o) {
                EvenManager.sendEvent(new RefreshUnboxingEvent());
                showToast("评论成功");
                getCommentList();
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.getMessage());
            }
        });
    }

    private void getCommentList() {
        AppModelFactory.model().getUnboxingCommentList(bean.shareOrderVo.shareOrderId, 1, Integer.MAX_VALUE, new ProgressSubscriber<DataBean<PageInfo<ResultUnboxingComment>>>(mContext) {
            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean<PageInfo<ResultUnboxingComment>> pageInfoDataBean) {
                if(null != pageInfoDataBean){
                    if(null != pageInfoDataBean.data){
                        unboxingCommentListAdapter.notifyDataSetChanged(pageInfoDataBean.data.dataLsit);
                    }
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.getMessage());
            }
        });
    }

    private void getUnboxingDetail() {
        AppModelFactory.model().getUnboxingDetail(bean.shareOrderVo.shareOrderId, LoginInfoPrefs.getInstance(mContext).getUserName(), LoginInfoPrefs.getInstance(mContext).getStaffId(), LoginInfoPrefs.getInstance(mContext).getGroupId(), new ProgressSubscriber<DataBean<Object>>(mContext) {
            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean<Object> pageInfoDataBean) {

            }


            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.getMessage());
            }
        });
    }

    private void submitUnboxingLike() {
        RequestUnboxingLike body = new RequestUnboxingLike();
        body.type = 2;
        body.shareOrderId= bean.shareOrderVo.shareOrderId;
        if (bean.likeFlag) {
            body.operation = 2;
        } else {
            body.operation = 1;
        }
        AppModelFactory.model().unboxingLike(body, new ProgressSubscriber<Object>(mContext) {
            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(Object o) {
                EvenManager.sendEvent(new RefreshUnboxingEvent());
                bean.likeFlag = !bean.likeFlag;
                if (bean.likeFlag) {
                    bean.likeCount = bean.likeCount + 1;
                } else {
                    if (bean.likeCount == 0) {
                        bean.likeCount = 0;
                    } else {
                        bean.likeCount = bean.likeCount - 1;
                    }
                }
                ivLike.setSelected(bean.likeFlag);
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.message);
            }
        });
    }

    private void submitUnboxingFavorites() {
        RequestUnboxingLike body = new RequestUnboxingLike();
        body.type = 1;
        body.shareOrderId= bean.shareOrderVo.shareOrderId;
        if (bean.collectFlag) {
            body.operation = 2;
        } else {
            body.operation = 1;
        }
        AppModelFactory.model().unboxingLike(body, new NoneProgressSubscriber<Object>(mContext) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(Object o) {
                if(type == TYPE_COLLECTION){
                    EvenManager.sendEvent(new NotifyEvent(NotifyEvent.COLLECTION_ONE));
                    finish();
                }else{
                    bean.collectFlag = !bean.collectFlag;
                    ivFavorites.setSelected(bean.collectFlag);
                    EvenManager.sendEvent(new RefreshUnboxingEvent());
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.message);
            }
        });

    }

    public void hideInputMenu() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 300);

    }
}

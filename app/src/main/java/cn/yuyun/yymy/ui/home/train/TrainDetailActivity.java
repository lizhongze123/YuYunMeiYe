package cn.yuyun.yymy.ui.home.train;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.http.PageInfo;
import com.example.http.ProgressSubscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.StringUtil;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestDelTrain;
import cn.yuyun.yymy.http.request.RequestTrainComment;
import cn.yuyun.yymy.http.request.RequestTrainLike;
import cn.yuyun.yymy.http.result.ResultTrainComment;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.home.unboxing.FillContent;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.SoftKeyboardStateHelper;
import cn.yuyun.yymy.view.WarnningDialog;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;

/**
 * @author
 * @date
 */

public class TrainDetailActivity extends BaseActivity {

    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.rv_image)
    RecyclerView rvImage;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.rv_comment)
    RecyclerView rvComment;
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
    @BindView(R.id.tv_position)
    TextView tvPosition;

    private TrainBean bean;

    private WarnningDialog warnningDialog;

    private List<String> list = new ArrayList<>();

    private InputMethodManager inputMethodManager;
    private SoftKeyboardStateHelper softKeyboardStateHelper;
    private SoftKeyboardStateHelper.SoftKeyboardStateListener softKeyboardStateListener;

    private TrainCommentListAdapter trainCommentListAdapter;


    private int type;
    public static int TYPE_NORMAL = 0;
    public static int TYPE_COLLECTION = 1;

    public static Intent startTrainDetailActivity(Context context, TrainBean bean) {
        return new Intent(context, TrainDetailActivity.class).putExtra(EXTRA_DATA, bean).putExtra(EXTRA_DATA2, TYPE_NORMAL);
    }

    public static Intent startTrainDetailActivityFromCollection(Context context, TrainBean bean) {
        return new Intent(context, TrainDetailActivity.class).putExtra(EXTRA_DATA, bean).putExtra(EXTRA_DATA2, TYPE_COLLECTION);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_detail);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        getCommentList();
        getDetail();
    }

    private void initViews() {
        titleBar.setTilte("培训详情");
        bean = (TrainBean) getIntent().getSerializableExtra(EXTRA_DATA);
        type = getIntent().getIntExtra(EXTRA_DATA2, 0);
        if (UserfoPrefs.getInstance(mContext).getPermission() || bean.selfCreateFlag) {
            titleBar.setRightIcon(R.drawable.ic_del_one);
            titleBar.setRightOnClicker(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    warnningDialog.show();
                }
            });
        }
        if (!TextUtils.isEmpty(bean.trainInfoVo.createPersonAvatar)) {
            ivAvatar.setVisibility(View.VISIBLE);
            if (bean.trainInfoVo.createPersonAvatar.startsWith(getString(R.string.HTTP))) {
                GlideHelper.displayImage(this, bean.trainInfoVo.createPersonAvatar, (int) getResources().getDimension(R.dimen.title_avatar), ivAvatar);
            } else {
                GlideHelper.displayImage(this, getString(R.string.image_url_prefix) + bean.trainInfoVo.createPersonAvatar, (int) getResources().getDimension(R.dimen.title_avatar), ivAvatar);
            }
        }
        for (int i = 0; i < bean.trainInfoVo.pictures.size(); i++) {
            if (bean.trainInfoVo.pictures.get(i).startsWith(mContext.getString(R.string.HTTP))) {
                list.add(bean.trainInfoVo.pictures.get(i));
            } else {
                list.add(mContext.getString(R.string.image_url_prefix) + bean.trainInfoVo.pictures.get(i));
            }
        }
        FillContent.fillWorkImgList(list, mContext, rvImage, true, true);
        if(StringUtil.isEmpty(bean.trainInfoVo.createPersonPosition)){
            tvPosition.setText("");
        }else{
            TextViewUtils.setTextOrEmpty(tvPosition, bean.trainInfoVo.createPersonPosition);
        }
        TextViewUtils.setTextOrEmpty(tvName, bean.trainInfoVo.createPersonName);
        TextViewUtils.setTextOrEmpty(tvTime, DateTimeUtils.StringToDate(bean.trainInfoVo.createTime, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_DATE_UI_TWO));
        TextViewUtils.setTextOrEmpty(tvContent, bean.trainInfoVo.content);
        titleBar.setTilte(bean.trainInfoVo.title);
        warnningDialog = new WarnningDialog(mContext, "确定删除？");
        warnningDialog.setOnPositiveListener(new WarnningDialog.OnDialogListener() {
            @Override
            public void onPositive() {
                delTrain();
                warnningDialog.dismiss();
            }

            @Override
            public void onNegative() {

            }
        });

        etComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {

                    String comment = etComment.getText().toString();
                    if (TextUtils.isEmpty(comment)) {
                        showToast("请输入评论内容");
                    }else{
                        hideInputMenu();
                        commentTrain(etComment.getText().toString());
                        etComment.setText("");
                    }
                }
                return false;
            }
        });
        trainCommentListAdapter = new TrainCommentListAdapter(this);
        rvComment.setLayoutManager(new LinearLayoutManager(this));
        rvComment.setAdapter(trainCommentListAdapter);
        initSoftKeyboard();
        bottombarLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitLike();
            }
        });
        bottombarFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFavorites();
            }
        });
        ivFavorites.setSelected(bean.collectFlag);
        ivLike.setSelected(bean.likeFlag);
    }

    private void delTrain() {
        RequestDelTrain body = new RequestDelTrain();
        body.status = -1;
        List<Integer>list = new ArrayList<>();
        list.add(bean.trainInfoVo.trainInfoId);
        body.idList = list;

        AppModelFactory.model().delTrain(body, new ProgressSubscriber<DataBean<Object>>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean<Object> result) {
                showToast("已删除");
                EvenManager.sendEvent(new NotifyEvent(NotifyEvent.RREFRESH));
                finish();
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.getMessage());
            }

        });
    }

    private void getDetail() {
        AppModelFactory.model().getTrainDetail(bean.trainInfoVo.trainInfoId, new ProgressSubscriber<DataBean<Object>>(mContext) {
            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean<Object> pageInfoDataBean) {
                EvenManager.sendEvent(new NotifyEvent(NotifyEvent.RREFRESH_DOT));
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.message);
            }
        });
    }

    private void initSoftKeyboard() {
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        softKeyboardStateHelper = new SoftKeyboardStateHelper(findViewById(R.id.origin_weibo_layout));
    }

    public void hideInputMenu() {
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void commentTrain(String content) {

        RequestTrainComment requestBean = new RequestTrainComment();
        requestBean.content = content;
        requestBean.trainInfoId = bean.trainInfoVo.trainInfoId;

        AppModelFactory.model().commentTrain(requestBean, new ProgressSubscriber<Object>(mContext) {
            @Override
            public void onNext(Object o) {
                showToast("已评论");
                getCommentList();
                EvenManager.sendEvent(new NotifyEvent(NotifyEvent.RREFRESH));
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.message);
            }
        });
    }

    private void submitLike() {
        RequestTrainLike body = new RequestTrainLike();
        body.type = 2;
        body.trainInfoId = bean.trainInfoVo.trainInfoId;
        if (bean.likeFlag) {
            body.operation = 2;
        } else {
            body.operation = 1;
        }
        AppModelFactory.model().trainLike(body, new ProgressSubscriber<Object>(mContext) {
            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(Object o) {
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
                EvenManager.sendEvent(new NotifyEvent(NotifyEvent.RREFRESH));
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.message);
            }
        });
    }

    private void submitFavorites() {
        RequestTrainLike body = new RequestTrainLike();
        body.type = 1;
        body.trainInfoId = bean.trainInfoVo.trainInfoId;
        if (bean.collectFlag) {
            body.operation = 2;
        } else {
            body.operation = 1;
        }
        AppModelFactory.model().trainLike(body, new ProgressSubscriber<Object>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(Object o) {
                bean.collectFlag = !bean.collectFlag;
                if(type == TYPE_COLLECTION){
                    EvenManager.sendEvent(new NotifyEvent(NotifyEvent.COLLECTION_FOUR));
                    finish();
                }else{
                    ivFavorites.setSelected(bean.collectFlag);
                    EvenManager.sendEvent(new NotifyEvent(NotifyEvent.RREFRESH));
//                tvFavorites.setSelected(bean.favoritesStatus.resId);
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.message);
            }
        });
    }

    private void getCommentList() {
        AppModelFactory.model().getTrainCommentList(bean.trainInfoVo.trainInfoId, 1, Integer.MAX_VALUE, new ProgressSubscriber<DataBean<PageInfo<ResultTrainComment>>>(mContext) {
            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean<PageInfo<ResultTrainComment>> pageInfoDataBean) {
                if(null != pageInfoDataBean){
                    if(null != pageInfoDataBean.data){
                        trainCommentListAdapter.notifyDataSetChanged(pageInfoDataBean.data.dataLsit);
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

}

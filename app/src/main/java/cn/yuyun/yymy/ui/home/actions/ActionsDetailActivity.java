package cn.yuyun.yymy.ui.home.actions;

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

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lzz.utils.LogUtils;
import cn.lzz.utils.StringUtil;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.constan.Constans;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestActionComment;
import cn.yuyun.yymy.http.request.RequestActionLike;
import cn.yuyun.yymy.http.request.RequestActionsComment;
import cn.yuyun.yymy.http.request.RequestDelAction;
import cn.yuyun.yymy.http.request.RequestTrainComment;
import cn.yuyun.yymy.http.result.ActionBean;
import cn.yuyun.yymy.http.result.ResultActionComment;
import cn.yuyun.yymy.http.result.ResultActivityComment;
import cn.yuyun.yymy.http.result.ResultTrainComment;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.home.unboxing.FillContent;
import cn.yuyun.yymy.ui.other.ViewBigImageActivity;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.SoftKeyboardStateHelper;
import cn.yuyun.yymy.view.WarnningDialog;
import cn.yuyun.yymy.view.dialog.MemberDetailPopup;
import cn.yuyun.yymy.view.dialog.MoreDetailPopup;
import cn.yuyun.yymy.view.selectpic.SelectPicAdapter;
import cn.yuyun.yymy.view.selectpic.SelectPicView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author
 * @date
 * @desc
 */

public class ActionsDetailActivity extends BaseActivity {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.rv_image)
    RecyclerView rvImage;
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
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    private ActionBean actionBean;
    private WarnningDialog warnningDialog;

    private InputMethodManager inputMethodManager;
    private SoftKeyboardStateHelper softKeyboardStateHelper;
    private SoftKeyboardStateHelper.SoftKeyboardStateListener softKeyboardStateListener;

    private ActionsCommentListAdapter actionsCommentListAdapter;

    private int type;
    public static int TYPE_NORMAL = 0;
    public static int TYPE_COLLECTION = 1;

    private boolean isFirst = true;

    public static Intent startActionDetailActivity(Context context, ActionBean bean) {
        return new Intent(context, ActionsDetailActivity.class).putExtra(EXTRA_DATA, bean).putExtra(EXTRA_TYPE, TYPE_NORMAL);
    }

    public static Intent startActionDetailActivityFromCollection(Context context, ActionBean bean) {
        return new Intent(context, ActionsDetailActivity.class).putExtra(EXTRA_DATA, bean).putExtra(EXTRA_TYPE, TYPE_COLLECTION);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if (notifyEvent.tag == NotifyEvent.RREFRESH_EDIT_ACTIONS) {
            getDetail();
            EvenManager.sendEvent(new NotifyEvent(NotifyEvent.RREFRESH_DOT));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_detail);
        EvenManager.register(this);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initData();
    }

    private void initData() {
        getCommentList();
        getDetail();
    }

    private void initViews() {
        actionBean = (ActionBean) getIntent().getSerializableExtra(EXTRA_DATA);
        type = getIntent().getIntExtra(EXTRA_DATA2, 0);
        if (UserfoPrefs.getInstance(mContext).getPermission() || actionBean.selfCreateFlag) {
            titleBar.setRightIcon(R.drawable.icon_more);
            titleBar.setRightOnClicker(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(LoginInfoPrefs.getInstance(mContext).getStaffId())) {
                        showTextDialog("该账号没有绑定员工", false);
                        return;
                    }
                    new MoreDetailPopup(mContext, R.layout.popupwindow_more_detail).setOnPopupClickListener(new MoreDetailPopup.OnPopupClickListener() {
                        @Override
                        public void onPositive(int pos) {
                            if (pos == 0) {
                                startActivity(EditActionActivity.startEditActionActivity(mContext, actionBean));
                            } else {
                                warnningDialog.show();
                            }
                        }
                    }).showAtBottom(titleBar);

                }
            });
        }
        setData(actionBean);
        warnningDialog = new WarnningDialog(mContext, "确定删除？");
        warnningDialog.setOnPositiveListener(new WarnningDialog.OnDialogListener() {
            @Override
            public void onPositive() {
                delAction();
                warnningDialog.dismiss();
            }

            @Override
            public void onNegative() {

            }
        });
        actionsCommentListAdapter = new ActionsCommentListAdapter(this);
        rvComment.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvComment.setAdapter(actionsCommentListAdapter);
        initSoftKeyboard();
        etComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {

                    String comment = etComment.getText().toString();
                    if (TextUtils.isEmpty(comment)) {
                        showToast("请输入评论内容");
                    } else {
                        hideInputMenu();
                        commentAction(etComment.getText().toString());
                        etComment.setText("");
                    }
                }
                return false;
            }
        });
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
        ivFavorites.setSelected(actionBean.collectFlag);
        ivLike.setSelected(actionBean.likeFlag);
    }

    private void setData(ActionBean bean) {
        actionBean = bean;
        titleBar.setTilte(bean.latestActivityVo.title);
        TextViewUtils.setTextOrEmpty(tvName, bean.latestActivityVo.createPersonName);
        TextViewUtils.setTextOrEmpty(tvTime, bean.latestActivityVo.createTime);
        TextViewUtils.setTextOrEmpty(tvContent, bean.latestActivityVo.content);
        TextViewUtils.setTextOrEmpty(tvPosition, bean.latestActivityVo.createPersonPosition);
        if (!TextUtils.isEmpty(bean.latestActivityVo.createPersonAvatar)) {
            ivAvatar.setVisibility(View.VISIBLE);
            if (bean.latestActivityVo.createPersonAvatar.startsWith(getString(R.string.HTTP))) {
                GlideHelper.displayImage(mContext, bean.latestActivityVo.createPersonAvatar, (int) getResources().getDimension(R.dimen.title_avatar), ivAvatar);
            } else {
                GlideHelper.displayImage(mContext, getString(R.string.image_url_prefix) + bean.latestActivityVo.createPersonAvatar, (int) getResources().getDimension(R.dimen.title_avatar), ivAvatar);
            }
        }
        List<String> list = new ArrayList<>();
        for (int i = 0; i < bean.latestActivityVo.pictures.size(); i++) {
            if (bean.latestActivityVo.pictures.get(i).startsWith(mContext.getString(R.string.HTTP))) {
                list.add(bean.latestActivityVo.pictures.get(i));
            } else {
                list.add(mContext.getString(R.string.image_url_prefix) + bean.latestActivityVo.pictures.get(i));
            }
        }
        FillContent.fillNineImgList(list, mContext, rvImage, true, true);
    }

    private void initSoftKeyboard() {
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        softKeyboardStateHelper = new SoftKeyboardStateHelper(findViewById(R.id.origin_weibo_layout));
        softKeyboardStateHelper.addSoftKeyboardStateListener(softKeyboardStateListener);
    }

    private void getDetail() {
        AppModelFactory.model().getActionDetail(actionBean.latestActivityVo.latestActivityId, new ProgressSubscriber<DataBean<ActionBean>>(mContext) {
            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean<ActionBean> result) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    setData(result.data);
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.message);
            }
        });
    }

    private void delAction() {
        RequestDelAction body = new RequestDelAction();
        body.status = -1;
        List<Integer> list = new ArrayList<>();
        list.add(actionBean.latestActivityVo.latestActivityId);
        body.latestActivityIdList = list;
        AppModelFactory.model().delAction(body, new ProgressSubscriber<DataBean<Object>>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean<Object> result) {
                EvenManager.sendEvent(new NotifyEvent(NotifyEvent.RREFRESH_DOT));
                finish();
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast(ex.message);
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast("网络异常，请检查网络");
            }

        });
    }

    private void commentAction(String content) {

        RequestActionsComment requestBean = new RequestActionsComment();
        requestBean.content = content;
        requestBean.latestActivityId = actionBean.latestActivityVo.latestActivityId;

        AppModelFactory.model().commentAction(requestBean, new ProgressSubscriber<Object>(mContext) {
            @Override
            public void onNext(Object o) {
                showToast("已评论");
                getCommentList();
                EvenManager.sendEvent(new NotifyEvent(NotifyEvent.RREFRESH));
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast(ex.message);
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast("网络异常，请检查网络");
            }

        });
    }

    private void submitLike() {
        RequestActionLike body = new RequestActionLike();
        body.type = 2;
        body.latestActivityId = actionBean.latestActivityVo.latestActivityId;
        if (actionBean.likeFlag) {
            //取消
            body.operation = 2;
        } else {
            body.operation = 1;
        }
        AppModelFactory.model().actionLike(body, new ProgressSubscriber<Object>(mContext) {
            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(Object o) {
                actionBean.likeFlag = !actionBean.likeFlag;
                if (actionBean.likeFlag) {
                    actionBean.likeCount = actionBean.likeCount + 1;
                } else {
                    if (actionBean.likeCount == 0) {
                        actionBean.likeCount = 0;
                    } else {
                        actionBean.likeCount = actionBean.likeCount - 1;
                    }
                }
                ivLike.setSelected(actionBean.likeFlag);
                EvenManager.sendEvent(new NotifyEvent(NotifyEvent.RREFRESH_DOT));
            }


            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast(ex.message);
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast("网络异常，请检查网络");
            }

        });
    }

    private void submitFavorites() {
        RequestActionLike body = new RequestActionLike();
        body.type = 1;
        body.latestActivityId = actionBean.latestActivityVo.latestActivityId;
        if (actionBean.collectFlag) {
            body.operation = 2;
        } else {
            body.operation = 1;
        }
        AppModelFactory.model().actionLike(body, new NoneProgressSubscriber<Object>(mContext) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(Object o) {
                actionBean.collectFlag = !actionBean.collectFlag;
                if (type == TYPE_COLLECTION) {
                    EvenManager.sendEvent(new NotifyEvent(NotifyEvent.COLLECTION_TWO));
                    finish();
                } else {
                    ivFavorites.setSelected(actionBean.collectFlag);
                    EvenManager.sendEvent(new NotifyEvent(NotifyEvent.RREFRESH_DOT));
                }
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast(ex.message);
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast("网络异常，请检查网络");
            }

        });
    }

    private void getCommentList() {
        AppModelFactory.model().getActionCommentList(actionBean.latestActivityVo.latestActivityId, 1, Integer.MAX_VALUE, new ProgressSubscriber<DataBean<PageInfo<ResultActivityComment>>>(mContext) {
            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean<PageInfo<ResultActivityComment>> pageInfoDataBean) {
                if (null != pageInfoDataBean) {
                    if (null != pageInfoDataBean.data) {
                        actionsCommentListAdapter.notifyDataSetChanged(pageInfoDataBean.data.dataLsit);
                    }
                }
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast(ex.message);
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast("网络异常，请检查网络");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EvenManager.unregister(this);
    }
}

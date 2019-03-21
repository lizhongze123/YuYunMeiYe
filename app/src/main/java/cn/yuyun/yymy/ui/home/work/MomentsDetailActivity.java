package cn.yuyun.yymy.ui.home.work;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
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
import com.example.http.ProgressSubscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.bean.MomentsActionType;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestDelWork;
import cn.yuyun.yymy.http.request.RequestMomentsAction;
import cn.yuyun.yymy.http.result.ResultWork;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.home.unboxing.FillContent;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.RoundAngleImageView;
import cn.yuyun.yymy.view.SoftKeyboardStateHelper;
import cn.yuyun.yymy.view.WarnningDialog;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author
 * @desc
 * @date
 */

public class MomentsDetailActivity extends BaseActivity {

    @BindView(R.id.profile_img)
    RoundAngleImageView ivAvatar;
    @BindView(R.id.profile_name)
    TextView tvName;
    @BindView(R.id.profile_time)
    TextView tvTime;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    @BindView(R.id.titlebar_layout)
    LinearLayout titlebarLayout;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.rv_image)
    RecyclerView rvImage;
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

    @BindView(R.id.rv_approve)
    RecyclerView rvApprove;
    @BindView(R.id.rv_like)
    RecyclerView rvLike;
    @BindView(R.id.ll_approvoe)
    LinearLayout llApprovoe;
    @BindView(R.id.rl_likePerson)
    RelativeLayout rlLikePerson;

    private WorkCommentListAdapter workCommentListAdapter;

    private InputMethodManager inputMethodManager;
    private SoftKeyboardStateHelper softKeyboardStateHelper;
    private SoftKeyboardStateHelper.SoftKeyboardStateListener softKeyboardStateListener;

    private static String OTHER = "other";
    private static String MY = "my";
    private static String NOTICE = "notice";

    private String type;
    private int id;
    private String baseonId;
    private List<String> list = new ArrayList<>();
    private WarnningDialog warnningDialog;

    private ResultWork bean;

    private WorkApprovePeopleAdapter workApprovePeopleAdapter;
    private WorkLikePeopleAdapter workLikePeopleAdapter;

    public static Intent startFromNotice(Context context, int id, String baseonId) {
        return new Intent(context, MomentsDetailActivity.class).putExtra(EXTRA_DATA, id).putExtra(EXTRA_DATA2, baseonId).putExtra(EXTRA_TYPE, NOTICE);
    }

    public static Intent startFromOther(Context context, ResultWork bean, String baseonId) {
        return new Intent(context, MomentsDetailActivity.class).putExtra(EXTRA_DATA, bean).putExtra(EXTRA_DATA2, baseonId).putExtra(EXTRA_TYPE, OTHER);
    }

    public static Intent startFromMy(Context context, ResultWork bean, String baseonId) {
        return new Intent(context, MomentsDetailActivity.class).putExtra(EXTRA_DATA, bean).putExtra(EXTRA_DATA2, baseonId).putExtra(EXTRA_TYPE, MY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moments_detail);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initView();
    }

    private void initView() {
        titleBar.setTilte("工作汇报详情");

        baseonId = getIntent().getStringExtra(EXTRA_DATA2);
        type =  getIntent().getStringExtra(EXTRA_TYPE);
        if(type.equals(OTHER) || type.equals(MY)){
            bean = (ResultWork) getIntent().getSerializableExtra(EXTRA_DATA);
            setData(bean);
            getWorkDetail(bean.workReportDetailVo.workReportId, false);
        }else{
            id = getIntent().getIntExtra(EXTRA_DATA, 0);
            getWorkDetail(id, false);
        }
    }

    private void initSoftKeyboard() {
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        softKeyboardStateHelper = new SoftKeyboardStateHelper(findViewById(R.id.origin_weibo_layout));
        softKeyboardStateHelper.addSoftKeyboardStateListener(softKeyboardStateListener);
    }

    private void delWork() {
        RequestDelWork body = new RequestDelWork();
        body.status = -1;
        List<Integer>list = new ArrayList<>();
        list.add(bean.workReportDetailVo.workReportId);
        body.workReportIdList = list;
        AppModelFactory.model().delWork(body, new ProgressSubscriber<DataBean<Object>>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean<Object> result) {
                showToast("已删除");
                EvenManager.sendEvent(new NotifyEvent(NotifyEvent.WORK));
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
                showToast(mContext.getString(R.string.error_no_network));
            }

        });
    }

    private void comment(final String content, final MomentsActionType type, final int workId) {
        RequestMomentsAction requestMomentsAction = new RequestMomentsAction();
        requestMomentsAction.workReportId = workId;
        requestMomentsAction.select_all_people = bean.select_all_people;
        if(null != type){
            requestMomentsAction.status = type;
        }
        if (!TextUtils.isEmpty(content)) {
            requestMomentsAction.content = content;
        }

        AppModelFactory.model().commentWork(requestMomentsAction, new ProgressSubscriber<Object>(mContext) {
            @Override
            public void onNext(Object o) {
                //点赞或取消点赞成功
                getWorkDetail(workId, true);
                EvenManager.sendEvent(new NotifyEvent(NotifyEvent.WORK));
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast(ex.message);
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(mContext.getString(R.string.error_no_network));
            }
        });
    }

    private void getWorkDetail(int id, final boolean isComment) {
        AppModelFactory.model().getWorkDetail(id, LoginInfoPrefs.getInstance(mContext).getStaffId(), baseonId, new ProgressSubscriber<DataBean<ResultWork>>(mContext) {
            @Override
            public void onNext(DataBean<ResultWork> result) {
                if(null != result){
                    EvenManager.sendEvent(new NotifyEvent(NotifyEvent.RREFRESH_DOT));
                    if(null != result.data){
                        bean = result.data;
                        if(type.equals(NOTICE) || isComment){
                            setData(bean);
                        }
                        List<ResultWork.WorkReportApproveVosBean> comentList = new ArrayList<>();
                        for (int i = 0; i < bean.workReportApproveVos.size(); i++) {
                            if(!TextUtils.isEmpty(bean.workReportApproveVos.get(i).content)){
                                comentList.add(bean.workReportApproveVos.get(i));
                            }
                        }
                        workCommentListAdapter.notifyDataSetChanged(comentList);
                        workApprovePeopleAdapter.notifyDataSetChanged(bean.workReportApproveVos);
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
                showToast(mContext.getString(R.string.error_no_network));
            }
        });
    }

    private void setData(final ResultWork bean){
        if(null == bean){
            return;
        }
        if(bean.select_all_people == 1){
            llApprovoe.setVisibility(View.GONE);
        }else{
            llApprovoe.setVisibility(View.VISIBLE);
        }
        if(type.equals(MY)){
            reEdittext.setVisibility(View.GONE);
        }else {
            reEdittext.setVisibility(View.VISIBLE);
        }

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
        }

        if (!TextUtils.isEmpty(bean.workReportDetailVo.createPersonAvatar)) {
            ivAvatar.setVisibility(View.VISIBLE);
            if (bean.workReportDetailVo.createPersonAvatar.startsWith(getString(R.string.HTTP))) {
                GlideHelper.displayImage(this, bean.workReportDetailVo.createPersonAvatar, (int) getResources().getDimension(R.dimen.title_avatar), ivAvatar);
            } else {
                GlideHelper.displayImage(this, getString(R.string.image_url_prefix) + bean.workReportDetailVo.createPersonAvatar, (int) getResources().getDimension(R.dimen.title_avatar), ivAvatar);
            }
        }
        TextViewUtils.setTextOrEmpty(tvPosition, bean.workReportDetailVo.createPersonPosition);
        list.clear();
        for (int i = 0; i < bean.workReportDetailVo.picture.size(); i++) {
            if (bean.workReportDetailVo.picture.get(i).startsWith(mContext.getString(R.string.HTTP))) {
                list.add(bean.workReportDetailVo.picture.get(i));
            } else {
                list.add(mContext.getString(R.string.image_url_prefix) + bean.workReportDetailVo.picture.get(i));
            }
        }
        FillContent.fillComunicationImgList(list, mContext, rvImage, true, true);
        TextViewUtils.setTextOrEmpty(tvName, bean.workReportDetailVo.createPersonName);
        TextViewUtils.setTextOrEmpty(tvTime, DateTimeUtils.StringToDate(bean.workReportDetailVo.createTime, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_DATETIME_TWO));
        TextViewUtils.setTextOrEmpty(tvContent, bean.workReportDetailVo.content);

        if(reEdittext.getVisibility() == View.VISIBLE){
            bottombarLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(bean.selfLikeFlag){
                        comment(null, MomentsActionType.NO, bean.workReportDetailVo.workReportId);
                    }else{
                        comment(null, MomentsActionType.LIKE, bean.workReportDetailVo.workReportId);
                    }
                }
            });
            ivLike.setSelected(bean.selfLikeFlag);
            etComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEND) {
                        String comment = etComment.getText().toString();
                        if (TextUtils.isEmpty(comment)) {
                            showToast("请输入评论内容");
                        }else{
                            hideInputMenu();
                            comment(etComment.getText().toString(), null, bean.workReportDetailVo.workReportId);
                            etComment.setText("");
                        }
                    }
                    return false;
                }
            });
            initSoftKeyboard();
        }
        workCommentListAdapter = new WorkCommentListAdapter(this);
        rvComment.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        List<ResultWork.WorkReportApproveVosBean> comentList = new ArrayList<>();
        for (int i = 0; i < bean.workReportApproveVos.size(); i++) {
            if(!TextUtils.isEmpty(bean.workReportApproveVos.get(i).content)){
                comentList.add(bean.workReportApproveVos.get(i));
            }
        }
        workCommentListAdapter.addAll(comentList);
        rvComment.setAdapter(workCommentListAdapter);

        workApprovePeopleAdapter = new WorkApprovePeopleAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvApprove.setLayoutManager(linearLayoutManager);
        workApprovePeopleAdapter.addAll(bean.workReportApproveVos);
        rvApprove.setAdapter(workApprovePeopleAdapter);

        List<ResultWork.WorkReportApproveVosBean> list = new ArrayList<>();
        GridLayoutManager sgl = new GridLayoutManager(mContext, 10);
        rvLike.setLayoutManager(sgl);
        workLikePeopleAdapter = new WorkLikePeopleAdapter(mContext);
        rvLike.setAdapter(workLikePeopleAdapter);


        if(null == bean.workReportlikeVos){
            rlLikePerson.setVisibility(View.GONE);
        }else{
            if (bean.workReportlikeVos.size() == 0) {
                rlLikePerson.setVisibility(View.GONE);
            } else {
                for (int i = 0; i < bean.workReportlikeVos.size(); i++) {
                    if(bean.workReportlikeVos.get(i).status == 1){
                        list.add(bean.workReportlikeVos.get(i));
                    }
                }
                if(list.size() == 0){
                    rlLikePerson.setVisibility(View.GONE);
                }else{
                    rlLikePerson.setVisibility(View.VISIBLE);
                }
            }
        }

        workLikePeopleAdapter.addAll(list);

        warnningDialog = new WarnningDialog(mContext, "确定删除？");
        warnningDialog.setOnPositiveListener(new WarnningDialog.OnDialogListener() {
            @Override
            public void onPositive() {
                delWork();
                warnningDialog.dismiss();
            }

            @Override
            public void onNegative() {

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

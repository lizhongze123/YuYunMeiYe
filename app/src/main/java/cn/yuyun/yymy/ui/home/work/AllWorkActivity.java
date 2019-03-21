package cn.yuyun.yymy.ui.home.work;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
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

import com.ajguan.library.EasyRefreshLayout;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.NoneProgressSubscriber;
import com.example.http.ProgressSubscriber;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import cn.lzz.utils.ResoureUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.base.BaseFragment;
import cn.yuyun.yymy.bean.MomentsActionType;
import cn.yuyun.yymy.bean.Sex;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestMomentsAction;
import cn.yuyun.yymy.http.result.PageInfoWork;
import cn.yuyun.yymy.http.result.ResultWork;
import cn.yuyun.yymy.http.result.StaffBean;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.SoftKeyboardStateHelper;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;

/**
 * @author
 * @desc
 * @date
 */

public class AllWorkActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;
    @BindView(R.id.re_edittext)
    LinearLayout reEdittext;
    @BindView(R.id.et_comment)
    EditText etComment;

    private int pageIndex = 1;
    private int pageSize = 5;
    private int totalPage;
    private boolean isRefresh = true;
    private InputMethodManager inputMethodManager;
    private SoftKeyboardStateHelper softKeyboardStateHelper;
    private SoftKeyboardStateHelper.SoftKeyboardStateListener softKeyboardStateListener;
    private MomentsParentAdapter adapter;
    private ResultWork tempResultWork;
    private int tempParentPos, tempChildPos;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_moments);
        EvenManager.register(this);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initData();
    }

    protected void initViews() {
        EvenManager.register(this);
        initSoftKeyboard(titleBar);
        titleBar.setTilte("全部工作汇报");
        titleBar.setLeftIcon(R.drawable.back_black);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        adapter = new MomentsParentAdapter(this);
        adapter.setOnItemClickListener(new AdapterListener2() {

            @Override
            public void onUnread() {

            }

            @Override
            public void onUserClicked() {
                StaffBean staffBean = new StaffBean();
                if (UserfoPrefs.getInstance(mContext).getSEX().equals(Sex.FEMALE.desc)) {
                    staffBean.sex = Sex.FEMALE;
                } else {
                    staffBean.sex = Sex.MALE;
                }
                staffBean.staffId = LoginInfoPrefs.getInstance(mContext).getStaffId();
                staffBean.avatar = UserfoPrefs.getInstance(mContext).getAvatar();
                staffBean.staffName = UserfoPrefs.getInstance(mContext).getStaffName();
                startActivity(MyWorkActivity.startMyWorkActivityFromMyself(mContext, staffBean));
            }

            @Override
            public void onFavorites(ResultWork bean) {

            }

            @Override
            public void onLike(ResultWork bean, int parentPos, int childPos) {
                if (bean.selfLikeFlag) {
                    comment(null, bean, MomentsActionType.NO, parentPos, childPos);
                } else {
                    comment(null, bean, MomentsActionType.LIKE, parentPos, childPos);
                }
            }

            @Override
            public void onComment(ResultWork bean, int parentPos, int childPos) {
                tempResultWork = bean;
                tempParentPos = parentPos;
                tempChildPos = childPos;
                showInputMenu(parentPos, childPos);
            }

            @Override
            public void onItemClicked(ResultWork bean, int parentPos, int childPos) {
                if (!bean.readFlag) {
                    bean.readFlag = true;
                    adapter.refreshRead(parentPos, childPos, bean);
                }
                startActivity(MomentsDetailActivity.startFromOther(mContext, bean, UserfoPrefs.getInstance(mContext).getBaseonId()));
            }

        });
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.setAdapter(adapter);
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(mContext));
        easyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                isRefresh = false;
                if (pageIndex > totalPage) {
                    showToast("没有更多数据了");
                    easyRefreshLayout.loadMoreComplete();
                } else {
                    getData();
                }
            }

            @Override
            public void onRefreshing() {
                isRefresh = true;
                pageIndex = 1;
                getData();
            }
        });
        etComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    String comment = etComment.getText().toString();
                    if (TextUtils.isEmpty(comment)) {
                        showToast("请输入评论内容");
                    } else {
                        hideInputMenu();
                        comment(comment, tempResultWork, null, tempParentPos, tempChildPos);
                        etComment.setText("");
                    }
                }
                return false;
            }
        });
    }

    public void hideInputMenu() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                inputMethodManager.hideSoftInputFromWindow(AllWorkActivity.this.getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                reEdittext.setVisibility(View.GONE);
            }
        }, 300);
    }

    private void initSoftKeyboard(View root) {
        inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        softKeyboardStateHelper = new SoftKeyboardStateHelper(root);
        softKeyboardStateListener = new SoftKeyboardStateHelper.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {

            }

            @Override
            public void onSoftKeyboardClosed() {
                reEdittext.setVisibility(View.GONE);
            }
        };
        softKeyboardStateHelper.addSoftKeyboardStateListener(softKeyboardStateListener);
    }

    public void showInputMenu(int parentPos, int childPos) {
        reEdittext.setVisibility(View.VISIBLE);
        etComment.requestFocus();
        etComment.postDelayed(new Runnable() {
            @Override
            public void run() {
                inputMethodManager.showSoftInput(etComment, 0);
            }
        }, 200);
       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int listviewHeight = recyclerView.getMeasuredHeight();
                MomentsItemView momentsItemView = adapter.getItemView(position);
                int itemHeight = momentsItemView.getMeasuredHeight();
                mLayoutManager.scrollToPositionWithOffset(position + 1, listviewHeight - itemHeight);
            }
        }, 1000);*/
    }

    private void initData() {
        easyRefreshLayout.autoRefresh();
    }

    private void comment(final String content, final ResultWork resultWork, final MomentsActionType type, final int parentPos, final int childPos) {
        RequestMomentsAction requestMomentsAction = new RequestMomentsAction();
        requestMomentsAction.workReportId = resultWork.workReportDetailVo.workReportId;
        requestMomentsAction.status = type;
        requestMomentsAction.select_all_people = resultWork.select_all_people;
        if (!TextUtils.isEmpty(content)) {
            requestMomentsAction.content = content;
        }
        AppModelFactory.model().commentWork(requestMomentsAction, new ProgressSubscriber<Object>(mContext) {
            @Override
            public void onNext(Object o) {
                //点赞或取消点赞成功
                if (type == MomentsActionType.LIKE) {
                    resultWork.selfLikeFlag = true;
                    resultWork.likeCount = resultWork.likeCount + 1;
                    for (int i = 0; i < resultWork.workReportlikeVos.size(); i++) {
                        if (resultWork.workReportlikeVos.get(i).approvePerson.equals(LoginInfoPrefs.getInstance(mContext).getStaffId())) {
                            resultWork.workReportlikeVos.get(i).status = 1;
                        }
                    }
                } else if (type == MomentsActionType.NO) {
                    resultWork.selfLikeFlag = false;
                    resultWork.likeCount = resultWork.likeCount - 1;
                    for (int i = 0; i < resultWork.workReportlikeVos.size(); i++) {
                        if (resultWork.workReportlikeVos.get(i).approvePerson.equals(LoginInfoPrefs.getInstance(mContext).getStaffId())) {
                            resultWork.workReportlikeVos.get(i).status = 0;
                        }
                    }
                } else {
                    for (int i = 0; i < resultWork.workReportApproveVos.size(); i++) {
                        if (resultWork.workReportApproveVos.get(i).approvePerson.equals(LoginInfoPrefs.getInstance(mContext).getStaffId())) {
                            resultWork.workReportApproveVos.get(i).content = content;
                        }
                    }
                }
                adapter.refreshLike(parentPos, childPos, resultWork, recyclerView);
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast(ex.getMessage());
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(mContext.getString(R.string.error_no_network));
            }
        });
    }

    private void getData() {
        AppModelFactory.model().toMeWorkList(2, pageIndex, pageSize, LoginInfoPrefs.getInstance(mContext).getStaffId(), new NoneProgressSubscriber<DataBean<PageInfoWork>>(mContext) {

            @Override
            public void onCompleted() {
                easyRefreshLayout.refreshComplete();
                easyRefreshLayout.loadMoreComplete();
                pageIndex = pageIndex + 1;
            }

            @Override
            public void onNext(DataBean<PageInfoWork> result) {
                if (null != result.data) {
                    if (null != result.data.workReportVoPage) {
                        totalPage = result.data.workReportVoPage.pages;
                        if (result.data.workReportVoPage.dataLsit.size() == 0) {
                            showToast("没有数据");
                        } else {
                            if (isRefresh) {
                                adapter.notifyDataSetChanged(result.data.workReportVoPage.dataLsit);
                            } else {
                                adapter.addAll(result.data.workReportVoPage.dataLsit);
                            }
                        }

                    }
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.getMessage());
                easyRefreshLayout.refreshComplete();
                easyRefreshLayout.loadMoreComplete();
            }

        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if (notifyEvent.tag == NotifyEvent.WORK) {
            easyRefreshLayout.autoRefresh();
        }
    }

    public int getScollYDistance() {
        int position = mLinearLayoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = mLinearLayoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }
}

package cn.yuyun.yymy.ui.home.leave;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestSubmitApprove;
import cn.yuyun.yymy.http.result.LeaveBean;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.ui.home.unboxing.FillContent;
import cn.yuyun.yymy.utils.EvenManager;
import de.hdodenhof.circleimageview.CircleImageView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc
 * @date
 */

public class ReviewedDetailActivity extends BaseActivity {

    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_day)
    TextView tvDay;
    @BindView(R.id.ll_type)
    LinearLayout llType;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_endDesc)
    TextView tvEndDesc;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.iv_status)
    ImageView ivStatus;
    @BindView(R.id.ll)
    RelativeLayout ll;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    @BindView(R.id.selectPicView)
    RecyclerView selectPicView;
    @BindView(R.id.tv_refuse)
    TextView tvRefuse;
    @BindView(R.id.tv_agree)
    TextView tvAgree;
    @BindView(R.id.ll_selectResult)
    LinearLayout llSelectResult;

    private LeaveBean leaveBean;
    private int submitid;

    private List<String> list = new ArrayList<>();

    public static Intent startReviewedDetailActivity(Context context, LeaveBean bean) {
        return new Intent(context, ReviewedDetailActivity.class).putExtra(EXTRA_DATA, bean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviewed_detail);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        getData(leaveBean.id);
    }

    private void initViews() {
        titleBar.setTilte("请假详情");
        leaveBean = (LeaveBean) getIntent().getSerializableExtra(EXTRA_DATA);

        TextViewUtils.setTextOrEmpty(tvName, leaveBean.createPersonName);
        TextViewUtils.setTextOrEmpty(tvContent, "请假原因:" + leaveBean.content);
        TextViewUtils.setTextOrEmpty(tvTime, DateTimeUtils.StringToDate(leaveBean.create_time, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_DATE_UI_TWO));
        TextViewUtils.setTextOrEmpty(tvStart, DateTimeUtils.StringToDate(leaveBean.startTime, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_DATETIME_TWO));
        TextViewUtils.setTextOrEmpty(tvEnd, DateTimeUtils.StringToDate(leaveBean.endTime, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_DATETIME_TWO));
        TextViewUtils.setTextOrEmpty(tvDay, leaveBean.timesLong + "天");
        TextViewUtils.setTextOrEmpty(tvType, leaveBean.reason.desc);
        TextViewUtils.setTextOrEmpty(tvPosition, leaveBean.createPersonPosition);
        ivStatus.setImageDrawable(ResoureUtils.getDrawable(mContext, leaveBean.approveStatus.bgId));

        tvAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit(ApprovePeopleBean.ApproveStatus.AGREE);
            }
        });
        tvRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit(ApprovePeopleBean.ApproveStatus.REFUSE);
            }
        });
        for (int i = 0; i < leaveBean.imgUrls.size(); i++) {
            if (leaveBean.imgUrls.get(i).startsWith(mContext.getString(R.string.HTTP))) {
                list.add(leaveBean.imgUrls.get(i));
            } else {
                list.add(mContext.getString(R.string.image_url_prefix) + leaveBean.imgUrls.get(i));
            }
        }
        FillContent.fillWorkImgList(list, mContext, selectPicView, true, true);
        if (leaveBean.approveStatus == LeaveBean.LeaveStatus.WAITTING) {
            llSelectResult.setVisibility(View.VISIBLE);
        } else {
            llSelectResult.setVisibility(View.GONE);
        }

    }

    private void getData(int leaveId) {
        AppModelFactory.model().getLeaveApproveDeatail(leaveId, new ProgressSubscriber<DataBean<ApprovePeopleBean>>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean<ApprovePeopleBean> result) {
                if (result != null) {
                    for (int i = 0; i < result.data.approve_by_leaveID.size(); i++) {
                        if (result.data.approve_by_leaveID.get(i).approve_person.equals(LoginInfoPrefs.getInstance(mContext).getStaffId())) {
                            submitid = result.data.approve_by_leaveID.get(i).id;
                            if (result.data.approve_by_leaveID.get(i).status == ApprovePeopleBean.ApproveStatus.WAITING) {
                                llSelectResult.setVisibility(View.VISIBLE);
                            }
                        }
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

    private void submit(final ApprovePeopleBean.ApproveStatus status) {
        RequestSubmitApprove bean = new RequestSubmitApprove();
        bean.id = submitid;
        bean.approve_agreement = status.desc;
        bean.status = status;
        AppModelFactory.model().submitApprove(bean, new ProgressSubscriber<Object>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(Object result) {
                showToast("操作成功");
                EvenManager.sendEvent(new NotifyEvent(NotifyEvent.RREFRESH));
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
                showToast(mContext.getString(R.string.error_no_network));
            }

        });
    }

}

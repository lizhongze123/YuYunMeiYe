package cn.yuyun.yymy.ui.home.leave;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.StringUtil;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.LeaveBean;
import cn.yuyun.yymy.ui.home.unboxing.FillContent;
import cn.yuyun.yymy.ui.other.ViewBigImageActivity;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.selectpic.SelectPicAdapter;
import cn.yuyun.yymy.view.selectpic.SelectPicView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc
 * @date
 */

public class ApplyDetailActivity extends BaseActivity {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.tv_day)
    TextView tvDay;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    @BindView(R.id.iv_status)
    ImageView ivStatus;
    @BindView(R.id.selectPicView)
    RecyclerView selectPicView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private LeaveBean leaveBean;
    private ApplyPeopleAdapter mAdapter;
    private List<String> list = new ArrayList<>();

    public static Intent startApplyDetailActivity(Context context, LeaveBean bean) {
        return new Intent(context, ApplyDetailActivity.class).putExtra(EXTRA_DATA, bean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_detail);
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
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        mAdapter = new ApplyPeopleAdapter(this);
        recyclerView.setAdapter(mAdapter);

        TextViewUtils.setTextOrEmpty(tvName, leaveBean.createPersonName);
        TextViewUtils.setTextOrEmpty(tvContent, "请假原因:" + leaveBean.content);
        TextViewUtils.setTextOrEmpty(tvTime, DateTimeUtils.StringToDate(leaveBean.create_time, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_DATE_UI_TWO));
        TextViewUtils.setTextOrEmpty(tvStart, DateTimeUtils.StringToDate(leaveBean.startTime, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_DATETIME_TWO));
        TextViewUtils.setTextOrEmpty(tvEnd, DateTimeUtils.StringToDate(leaveBean.endTime, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_DATETIME_TWO));
        TextViewUtils.setTextOrEmpty(tvDay, leaveBean.timesLong + "天");
        TextViewUtils.setTextOrEmpty(tvType, leaveBean.reason.desc);
        TextViewUtils.setTextOrEmpty(tvPosition, leaveBean.createPersonPosition);
        ivStatus.setImageDrawable(ResoureUtils.getDrawable(mContext, leaveBean.approveStatus.bgId));
        if(null != leaveBean.imgUrls){
            for (int i = 0; i < leaveBean.imgUrls.size(); i++) {
                if (leaveBean.imgUrls.get(i).startsWith(mContext.getString(R.string.HTTP))) {
                    list.add(leaveBean.imgUrls.get(i));
                } else {
                    list.add(mContext.getString(R.string.image_url_prefix) + leaveBean.imgUrls.get(i));
                }
            }
        }
        FillContent.fillWorkImgList(list, mContext, selectPicView, true, true);
    }

    private void getData(int leaveId) {
        AppModelFactory.model().getLeaveApproveDeatail(leaveId, new ProgressSubscriber<DataBean<ApprovePeopleBean>>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean<ApprovePeopleBean> result) {
                if (null != result.data && null != result.data.approve_by_leaveID) {
                    Collections.sort(result.data.approve_by_leaveID);
                    if(leaveBean.approveStatus == LeaveBean.LeaveStatus.REFUESD){
                        for (int i = 0; i < result.data.approve_by_leaveID.size(); i++) {
                            result.data.approve_by_leaveID.get(i).status = ApprovePeopleBean.ApproveStatus.REFUSE;
                        }
                    }
                    mAdapter.notifyDataSetChanged(result.data.approve_by_leaveID);
                }
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast("请求失败，请稍后重试");
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(mContext.getString(R.string.error_no_network));
            }

        });
    }

}

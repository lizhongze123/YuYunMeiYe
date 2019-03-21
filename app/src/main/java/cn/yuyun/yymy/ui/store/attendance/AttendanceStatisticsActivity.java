package cn.yuyun.yymy.ui.store.attendance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;

import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.ui.store.attendance.RequestAttendanceStatisticsDetail.AttendanceType;


import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lzz.utils.DateTimeUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.ResultAttendanceStatistics;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc 考勤汇总
 * @date
 */

public class AttendanceStatisticsActivity extends BaseNoTitleActivity {

    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_work)
    TextView tvWork;
    @BindView(R.id.ll_work)
    LinearLayout llWork;
    @BindView(R.id.tv_full)
    TextView tvFull;
    @BindView(R.id.ll_full)
    LinearLayout llFull;
    @BindView(R.id.tv_late)
    TextView tvLate;
    @BindView(R.id.ll_late)
    LinearLayout llLate;
    @BindView(R.id.tv_leaveEarly)
    TextView tvLeaveEarly;
    @BindView(R.id.ll_leaveEarly)
    LinearLayout llLeaveEarly;
    @BindView(R.id.tv_absence)
    TextView tvAbsence;
    @BindView(R.id.ll_absence)
    LinearLayout llAbsence;
    @BindView(R.id.tv_workOut)
    TextView tvWorkOut;
    @BindView(R.id.ll_workOut)
    LinearLayout llWorkOut;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_store)
    TextView tvStore;
    private StoreBean storeBean;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    private TimePickerView timePickerView;
    private long startDate;
    private long endDate;

    public static Intent startAttendanceStatisticsActivity(Context context, StoreBean bean) {
        return new Intent(context, AttendanceStatisticsActivity.class).putExtra(EXTRA_DATA, bean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_statistics);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    private void initDatas() {
        startDate = DateTimeUtils.getTimesMonthMorning();
        endDate = DateTimeUtils.getTimesMonthNight();
        tvDate.setText(DateTimeUtils.getDateTimeText(DateTimeUtils.getTimesMonthMorning(), DateTimeUtils.FORMAT_YY_MM));
        getData();
    }

    private void initViews() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        storeBean = (StoreBean) getIntent().getSerializableExtra(EXTRA_DATA);
        tvStore.setText(storeBean.spName);
        timePickerView = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tvDate.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_YY_MM));
                startDate = date.getTime() / 1000;
                getData();
            }
        }).setType(new boolean[]{true, true, false, false, false, false}).build();
        rlTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerView.show();
            }
        });
    }

    private void getData() {
        AppModelFactory.model().getWorkAttendanceRecord(startDate, endDate, storeBean.spId, storeBean.ogType + "", new ProgressSubscriber<DataBean<ResultAttendanceStatistics>>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean<ResultAttendanceStatistics> result) {

                if (null != result.data) {
                    tvWork.setText(result.data.workPersonNum + "人");
                    if (result.data.workFullPersonNum == 0) {
                        tvFull.setEnabled(false);
                        llFull.setEnabled(false);
                    }
                    if (result.data.comeLatePersonNum == 0) {
                        tvLate.setEnabled(false);
                        llLate.setEnabled(false);
                    }
                    if (result.data.leaveEarlyPersonNum == 0) {
                        tvLeaveEarly.setEnabled(false);
                        llLeaveEarly.setEnabled(false);
                    }
                    if (result.data.absentPersonNum == 0) {
                        tvAbsence.setEnabled(false);
                        llAbsence.setEnabled(false);
                    }
                    if (result.data.workOutPersonNum == 0) {
                        tvWorkOut.setEnabled(false);
                        llWorkOut.setEnabled(false);
                    }
                    tvFull.setText(result.data.workFullPersonNum + "人");
                    tvLate.setText(result.data.comeLatePersonNum + "人");
                    tvLeaveEarly.setText(result.data.leaveEarlyPersonNum + "人");
                    tvAbsence.setText(result.data.absentPersonNum + "人");
                    tvWorkOut.setText(result.data.workOutPersonNum + "人");
                }

            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(getString(R.string.error_no_network));
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast(ex.message);
            }
        });
    }

    @OnClick({R.id.ll_work, R.id.ll_full, R.id.ll_late, R.id.ll_leaveEarly, R.id.ll_absence, R.id.ll_workOut})
    public void onClickItem(View view) {

        RequestAttendanceStatisticsDetail body = new RequestAttendanceStatisticsDetail();
        body.start = startDate;
        body.end = endDate;
        body.groupId = storeBean.group_id;
        body.spId = storeBean.spId;

        switch (view.getId()) {
            case R.id.ll_full:
                body.type = AttendanceType.FULL;
                break;
            case R.id.ll_work:
                body.type = AttendanceType.WORK;
                break;
            case R.id.ll_late:
                body.type = AttendanceType.LATE;
                break;
            case R.id.ll_leaveEarly:
                body.type = AttendanceType.LEAVE_EARLY;
                break;
            case R.id.ll_absence:
                body.type = AttendanceType.ABSENCE;
                break;
            case R.id.ll_workOut:
                body.type = AttendanceType.WORK_OUT;
                break;
            default:

        }

        startActivity(AttendanceStatisticsDetailActivity.startAttendanceStatisticsDetailActivity(mContext, body, storeBean));

    }


}

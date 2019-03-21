package cn.yuyun.yymy.ui.store.attendance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.DeviceUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.ResultAttendanceStatistics;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.store.attendance.RequestAttendanceStatisticsDetail.AttendanceType;
import cn.yuyun.yymy.ui.store.report.HqStoreRadioAdapter;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author
 * @desc 考勤汇总
 * @date
 */

public class AttendanceStatisticsAllActivity extends BaseNoTitleActivity {

    @BindView(R.id.main_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.main_right_drawer_layout)
    RelativeLayout main_right_drawer_layout;

    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
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

    private TimePickerView timePickerView;
    private long startDate;
    private long endDate;

    @BindView(R.id.rv_card)
    RecyclerView rvCard;
    String tempLevelName = "";
    HqStoreRadioAdapter hqStoreAdapter;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    private String groupId;
    private List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> storeList;
    private List<String> tempSpIdList;

    private String spId;
    private int ogType = 2;

    public static Intent startAttendanceStatisticsActivity(Context context, List<String> tempSpIdList, List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> storeList, String groupId) {
        return new Intent(context, AttendanceStatisticsAllActivity.class).putExtra(EXTRA_DATA2, (Serializable) tempSpIdList).putExtra(EXTRA_DATA, (Serializable) storeList).putExtra(EXTRA_TYPE, groupId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_statistics_all);
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
        getData(ogType, spId);
    }

    private void initViews() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        groupId = getIntent().getStringExtra(EXTRA_TYPE);
        tempSpIdList = (List<String>) getIntent().getSerializableExtra(EXTRA_DATA2);
        storeList = (List<ResultClassfiyStoreBean.OgServiceplacesRspListBean>) getIntent().getSerializableExtra(EXTRA_DATA);
        if(UserfoPrefs.getInstance(mContext).getPermissionHq() == 1){
            ResultClassfiyStoreBean.OgServiceplacesRspListBean bean = new ResultClassfiyStoreBean.OgServiceplacesRspListBean();
            bean.sp_id = groupId;
            bean.ogType = 1;
            bean.sp_name = "总部";
            storeList.add(0, bean);
            spId = groupId;
            ogType = 1;
            tvAmount.setText("总部");
        }else{
            ogType = 2;
            if(null != storeList && storeList.size() > 0){
                spId = storeList.get(0).sp_id;
                tvAmount.setText(storeList.get(0).sp_name);
            }
        }

        timePickerView = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tvDate.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_YY_MM));
                startDate = date.getTime() / 1000;
                getData(ogType, spId);
            }
        }).setType(new boolean[]{true, true, false, false, false, false}).build();
        rlTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerView.show();
            }
        });
        hqStoreAdapter = new HqStoreRadioAdapter(mContext);
        hqStoreAdapter.setOnItemClickListener(new HqStoreRadioAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultClassfiyStoreBean.OgServiceplacesRspListBean bean, int position) {
                spId = bean.sp_id;
                ogType = bean.ogType;
                tempLevelName = bean.sp_name;
            }
        });
        rvCard.setLayoutManager(new GridLayoutManager(this, 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvCard.setAdapter(hqStoreAdapter);
        hqStoreAdapter.addAll(storeList);

    }

    private void getData(int ogType, String spId) {
        AppModelFactory.model().getWorkAttendanceRecord(startDate, endDate, spId, ogType + "", new ProgressSubscriber<DataBean<ResultAttendanceStatistics>>(mContext) {

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
        body.groupId = groupId;
        body.spId = spId;

        switch (view.getId()) {
            case R.id.ll_work:
                body.type = AttendanceType.WORK;
                break;
            case R.id.ll_late:
                body.type = AttendanceType.LATE;
                break;
            case R.id.ll_full:
                body.type = AttendanceType.FULL;
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
//        startActivity(AttendanceStatisticsDetailActivity.startAttendanceStatisticsDetailActivity(mContext, body, storeBean));
    }


    @OnClick(R.id.tv_positive)
    public void positive(View v) {
        openRightLayout(v);
        getData(ogType, spId);
        tvAmount.setText(tempLevelName);
    }

    @OnClick(R.id.tv_negative)
    public void negative(View v) {
        openRightLayout(v);
        spId = hqStoreAdapter.getItem(0).sp_id;
        ogType = hqStoreAdapter.getItem(0).ogType;
        hqStoreAdapter.setSelectPos(0);
        tvAmount.setText(hqStoreAdapter.getItem(0).sp_name);
        getData(ogType, spId);
    }

    @OnClick(R.id.rl_filter)
    public void filter(View v) {
        DeviceUtils.hideSoftKeyboard(v, mContext);
        openRightLayout(v);
    }

    /**
     * 右边菜单开关事件
     */
    public void openRightLayout(View view) {
        if (drawerLayout.isDrawerOpen(main_right_drawer_layout)) {
            drawerLayout.closeDrawer(main_right_drawer_layout);
        } else {
            drawerLayout.openDrawer(main_right_drawer_layout);
        }
    }
}

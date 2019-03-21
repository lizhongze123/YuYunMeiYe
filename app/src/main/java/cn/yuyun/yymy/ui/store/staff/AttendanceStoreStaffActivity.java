package cn.yuyun.yymy.ui.store.staff;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.LogUtils;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.ResultAttendanceStatistics;
import cn.yuyun.yymy.http.result.ResultStoreStaffAttendance;
import cn.yuyun.yymy.http.result.StaffBean;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.store.StaffAttendanceRecordActivity;
import cn.yuyun.yymy.ui.store.attendance.AttendanceStaff;
import cn.yuyun.yymy.ui.store.attendance.AttendanceStatisticsAdapter;
import cn.yuyun.yymy.utils.GlideHelper;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;

/**
 * @author
 * @desc  考勤汇总
 * @date
 */

public class AttendanceStoreStaffActivity extends BaseNoTitleActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.title)
    TextView title;
    private TimePickerView timePickerView;
    private StaffBean staffBean;
    private StoreBean storeBean;

    private AttendanceStoreStaffParentAdapter adapter;

    public static Intent startAttendanceStoreStaffActivity(Context context, StoreBean storeBean ,StaffBean staffBean) {
        return new Intent(context, AttendanceStoreStaffActivity.class).putExtra(EXTRA_DATA, storeBean).putExtra(EXTRA_DATA2, staffBean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_staff_attendance);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    private void initDatas() {
        getData(DateTimeUtils.getNowTimeStamp());
    }

    private void initViews() {
        storeBean = (StoreBean) getIntent().getSerializableExtra(EXTRA_DATA);
        staffBean = (StaffBean) getIntent().getSerializableExtra(EXTRA_DATA2);
        adapter = new AttendanceStoreStaffParentAdapter(mContext);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        if (!TextUtils.isEmpty(staffBean.avatar)) {
            if (staffBean.avatar.startsWith(getString(R.string.HTTP))) {
                GlideHelper.displayImage(mContext, staffBean.avatar, ResoureUtils.getDimension(mContext, R.dimen.sign_people_avatar), ivAvatar);
            } else {
                GlideHelper.displayImage(mContext, getString(R.string.image_url_prefix) + staffBean.avatar, ResoureUtils.getDimension(mContext, R.dimen.sign_people_avatar), ivAvatar);
            }
        }
        TextViewUtils.setTextOrEmpty(tvName, staffBean.staffName);

        findViewById(R.id.ll_record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(StaffAttendanceRecordActivity.startStaffAttendanceRecordActivity(mContext, staffBean));
            }
        });
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerView.show();
            }
        });
        title.setText(DateTimeUtils.getDateTimeText(DateTimeUtils.getTimesMonthMorning(), DateTimeUtils.FORMAT_YY_MM));
        timePickerView = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                title.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_YY_MM));
                getData(date.getTime() / 1000);
            }
        }).setType(new boolean[]{true, true, false, false, false, false}).build();
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getData(long date) {
        AppModelFactory.model().getAttendanceStoreStaff(staffBean.staffId, date, storeBean.spId, new ProgressSubscriber<DataBean<ResultStoreStaffAttendance>>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean<ResultStoreStaffAttendance> result) {
                if(null != result.data){
                    List<AttendanceStoreStaff> parentList = new ArrayList<>();
                    AttendanceStoreStaff all = new AttendanceStoreStaff();
                    all.desc = "出勤天数";
                    all.number = result.data.getWorkNum();
                    all.attendanceStaffBeanList = new ArrayList<>();
                    AttendanceStoreStaff rest = new AttendanceStoreStaff();
                    rest.desc = "休息天数";
                    rest.number = result.data.getRestNum();
                    rest.attendanceStaffBeanList = new ArrayList<>();
                    AttendanceStoreStaff late = new AttendanceStoreStaff();
                    late.desc = "迟到";
                    late.number = result.data.getComeLateNum();
                    late.attendanceStaffBeanList = new ArrayList<>();
                    AttendanceStoreStaff leaveEarly = new AttendanceStoreStaff();
                    leaveEarly.desc = "早退";
                    leaveEarly.number = result.data.getLeaveEarlyNum();
                    leaveEarly.attendanceStaffBeanList = new ArrayList<>();
                    AttendanceStoreStaff absence = new AttendanceStoreStaff();
                    absence.desc = "旷工";
                    absence.number = result.data.getAbsentNum();
                    absence.attendanceStaffBeanList = new ArrayList<>();
                    AttendanceStoreStaff workOut = new AttendanceStoreStaff();
                    workOut.desc = "外勤";
                    workOut.number = result.data.getWorkOutNum();
                    workOut.attendanceStaffBeanList = new ArrayList<>();

                    for (String s : result.data.getWorkList()) {
                        all.attendanceStaffBeanList.add(s);
                    }
                    for (String s : result.data.getRestList()) {
                        rest.attendanceStaffBeanList.add(s);
                    }
                    for (ResultStoreStaffAttendance.EarlyListBean bean : result.data.getEarlyList()) {
                        late.attendanceStaffBeanList.add(bean.getDate() + "(" + bean.getWeek() + ")，" +  bean.getDesc());
                    }
                    for (ResultStoreStaffAttendance.LeaveEarlyListBean bean : result.data.getLeaveEarlyList()) {
                        leaveEarly.attendanceStaffBeanList.add(bean.getDate() + "(" + bean.getWeek() + ")，" +  bean.getDesc());
                    }
                    for (String s : result.data.getAbsentList()) {
                        absence.attendanceStaffBeanList.add(s);
                    }
                    for (String s : result.data.getWorkOutList()) {
                        workOut.attendanceStaffBeanList.add(s);
                    }
                    parentList.add(all);
                    parentList.add(rest);
                    parentList.add(late);
                    parentList.add(leaveEarly);
                    parentList.add(absence);
                    parentList.add(workOut);
                    adapter.notifyDataSetChanged(parentList);
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.message);
            }

        });
    }


}

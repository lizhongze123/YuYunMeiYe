package cn.yuyun.yymy.ui.home.attendance;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;

import butterknife.BindView;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseNoTitleFragment;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestAttendanceWithTime;
import cn.yuyun.yymy.http.result.ResultAttendanceWithTime;
import cn.yuyun.yymy.http.result.StaffBean;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.view.kcalendar.KCalendarView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author 考勤记录fragment
 * @desc
 * @date
 */
public class RecordFragment extends BaseNoTitleFragment {

    @BindView(R.id.kCalendarView)
    KCalendarView kCalendarView;
    @BindView(R.id.tv_signOnTime)
    TextView tvSignOnTime;
    @BindView(R.id.tv_onLate)
    TextView tvOnLate;
    @BindView(R.id.tv_signOffTime)
    TextView tvSignOffTime;
    @BindView(R.id.tv_offLate)
    TextView tvOffLate;
    @BindView(R.id.rv)
    RecyclerView rv;
    private SignRecordAdapter mAdapter;
    private StaffBean staffBean;

    @Override
    protected int getTheLayoutId() {
        return R.layout.fragment_attendance_record;
    }

    @Override
    protected void onBindViewBefore(View root) {
        super.onBindViewBefore(root);
        mContext = getContext();
    }

    public static RecordFragment newInstance(StaffBean staffBean) {
        RecordFragment fragment = new RecordFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATA, staffBean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        if (getArguments() != null) {
            staffBean = (StaffBean) getArguments().getSerializable(EXTRA_DATA);
        }
    }

    @Override
    protected void initViews(View root) {
        super.initViews(root);
        kCalendarView.setCalendarClickListener(new KCalendarView.CalendarClickListener() {
            @Override
            public void onClick(String dateFormat) {
                long start = DateTimeUtils.parseTimestamp(dateFormat + " 00:00");
                long end = DateTimeUtils.parseTimestamp(dateFormat + " 23:59") + 60000;
                getAttendanceListWithTime(start, end);
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(mContext){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rv.setHasFixedSize(true);
        mAdapter = new SignRecordAdapter(mContext);
        mAdapter.setOnItemClickListener(new SignRecordAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultAttendanceWithTime.AppAttendanceExternalBean bean, int position) {
                startActivity(SignRecordDetailActivity.startSignRecordDetailActivity(mContext, bean));
            }
        });
        rv.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        super.initData();
        getAttendanceListWithTime(DateTimeUtils.getTimesMorning(), DateTimeUtils.getTimesNight());
    }

    private void getAttendanceListWithTime(long start, long end) {
        RequestAttendanceWithTime bean = new RequestAttendanceWithTime();
        if (staffBean == null) {
            bean.baseon_type = UserfoPrefs.getInstance(mContext).getBaseonType();
            bean.baseon_id = UserfoPrefs.getInstance(mContext).getBaseonId();
            bean.staff_id = LoginInfoPrefs.getInstance(mContext).getStaffId();
        } else {
            bean.baseon_type = staffBean.baseon_type;
            bean.baseon_id = staffBean.baseon_id;
            bean.staff_id = staffBean.staffId;
        }
        bean.start = start;
        bean.end = start;
        AppModelFactory.model().getAttendanceListWithTime(bean, new ProgressSubscriber<DataBean<ResultAttendanceWithTime>>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean<ResultAttendanceWithTime> result) {

                if (result != null) {
                    tvSignOnTime.setText("");
                    tvSignOffTime.setText("");
                    tvOffLate.setVisibility(View.GONE);
                    tvOnLate.setVisibility(View.GONE);
                    for (int i = 0; i < result.data.AppAttendanceInternal.size(); i++) {
                        if (result.data.AppAttendanceInternal.get(i).span_status == 1) {
                            TextViewUtils.setTextOrEmpty(tvSignOnTime, result.data.AppAttendanceInternal.get(i).dateTime);
                            tvOnLate.setVisibility(View.VISIBLE);
                            TextViewUtils.setTextOrEmpty(tvOnLate, result.data.AppAttendanceInternal.get(i).status.desc);
                            tvOnLate.setSelected(result.data.AppAttendanceInternal.get(i).status.isSelected);
                        } else if (result.data.AppAttendanceInternal.get(i).span_status == 2) {
                            TextViewUtils.setTextOrEmpty(tvSignOffTime, result.data.AppAttendanceInternal.get(i).dateTime);
                            tvOffLate.setVisibility(View.VISIBLE);
                            TextViewUtils.setTextOrEmpty(tvOffLate, result.data.AppAttendanceInternal.get(i).status.desc);
                            tvOffLate.setSelected(result.data.AppAttendanceInternal.get(i).status.isSelected);
                        }
                    }
                    mAdapter.notifyDataSetChanged(result.data.AppAttendanceExternal);
                } else {
                    tvOffLate.setVisibility(View.GONE);
                    tvOnLate.setVisibility(View.GONE);
                    tvSignOnTime.setText("");
                    tvSignOffTime.setText("");
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
                showToast("网络异常，请检查网络");
            }

        });
    }

}

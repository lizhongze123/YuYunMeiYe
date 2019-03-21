package cn.yuyun.yymy.ui.store.attendance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.ResultAttendanceStatistics;
import cn.yuyun.yymy.http.result.StaffBean;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.store.attendance.RequestAttendanceStatisticsDetail.AttendanceType;
import cn.yuyun.yymy.ui.store.staff.AttendanceStoreStaffActivity;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;

/**
 * @author
 * @desc 考勤汇总
 * @date
 */

public class AttendanceStatisticsDetailActivity extends BaseActivity {

    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private AttendanceStatisticsAdapter adapter;
    private RequestAttendanceStatisticsDetail body;
    private StoreBean storeBean;

    public static Intent startAttendanceStatisticsDetailActivity(Context context, RequestAttendanceStatisticsDetail bean, StoreBean storeBean) {
        return new Intent(context, AttendanceStatisticsDetailActivity.class).putExtra(EXTRA_DATA, bean).putExtra(EXTRA_DATA2, storeBean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_statistics_detail);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    private void initDatas() {
        getData();
    }

    private void initViews() {
        body = (RequestAttendanceStatisticsDetail) getIntent().getSerializableExtra(EXTRA_DATA);
        storeBean = (StoreBean) getIntent().getSerializableExtra(EXTRA_DATA2);
        titleBar.setTilte(body.type.desc);
        adapter = new AttendanceStatisticsAdapter(mContext, body.type);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new AttendanceStatisticsAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(ResultAttendanceStatistics.WorkAttendanceRecordByDateVosBean bean, int position) {
                StaffBean staffBean = new StaffBean();
                staffBean.avatar = bean.create_person_avatar;
                staffBean.staffName = bean.create_person_name;
                staffBean.staffId = bean.staff_id;
                startActivity(AttendanceStoreStaffActivity.startAttendanceStoreStaffActivity(AttendanceStatisticsDetailActivity.this, storeBean, staffBean));
            }
        });

    }

    private void getData() {
        AppModelFactory.model().getWorkAttendanceRecordDetail(body.start, body.end, body.spId,"2",body.groupId, body.type, new ProgressSubscriber<DataBean<ResultAttendanceStatistics>>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean<ResultAttendanceStatistics> result) {
                if(null != result.data){

                    List<ResultAttendanceStatistics.WorkAttendanceRecordByDateVosBean> newList = new ArrayList<>();

                    for (ResultAttendanceStatistics.WorkAttendanceRecordByDateVosBean bean : result.data.workAttendanceRecordByDateVos) {

                        if(body.type == AttendanceType.WORK){
                            if(bean.ycqts_count != 0){
                                newList.add(bean);
                            }
                        }else if(body.type == AttendanceType.LATE){
                            if(bean.lateCount != 0 ){
                                newList.add(bean);
                            }
                        }else if(body.type == AttendanceType.LEAVE_EARLY){
                            if(bean.earlyCount != 0 ){
                                newList.add(bean);
                            }
                        }else if(body.type == AttendanceType.ABSENCE){
                            if(bean.neglect_work_count != 0 ){
                                newList.add(bean);
                            }
                        }else if(body.type == AttendanceType.WORK_OUT){
                            if(bean.out_work_count != 0 ){
                                newList.add(bean);
                            }
                        }
                    }

                    adapter.notifyDataSetChanged(newList);
                    tvDesc.setText("本月" + body.type.desc + newList.size() + "人");
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

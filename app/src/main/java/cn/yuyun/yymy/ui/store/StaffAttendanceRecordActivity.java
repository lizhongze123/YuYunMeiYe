package cn.yuyun.yymy.ui.store;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.result.StaffBean;
import cn.yuyun.yymy.ui.home.attendance.RecordFragment;
import cn.yuyun.yymy.ui.home.work.MyWorkActivity;
import cn.yuyun.yymy.ui.home.work.MyWorksFragment;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc
 * @date
 */

public class StaffAttendanceRecordActivity extends BaseActivity {

    private RecordFragment recordFragment;

    private StaffBean staffBean;

    public static Intent startStaffAttendanceRecordActivity (Context context, StaffBean staffBean){
        return new Intent(context, StaffAttendanceRecordActivity.class).putExtra(EXTRA_DATA, staffBean);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_my_work);
    }

    @Override
    protected void setUpViewAndData() {
        staffBean = (StaffBean) getIntent().getSerializableExtra(EXTRA_DATA);
        titleBar.setTilte("考勤记录");
        recordFragment= (RecordFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if(recordFragment == null){
            recordFragment = RecordFragment.newInstance(staffBean);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.contentFrame, recordFragment);
            transaction.commit();
        }
    }
}

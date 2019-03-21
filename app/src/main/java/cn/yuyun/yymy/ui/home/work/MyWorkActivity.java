package cn.yuyun.yymy.ui.home.work;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.http.result.StaffBean;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author lzz
 * @desc 工作汇报-朋友圈
 * @date 2018/1/23
 */

public class MyWorkActivity extends BaseNoTitleActivity {

    private MyWorksFragment myWorksFragment;
    private StaffBean staffBean;
    private static String MYSELF = "myself";
    private static String STAFF = "staff";
    private String type = "";

    public static Intent startMyWorkActivityFromMyself (Context context, StaffBean staffBean){
        return new Intent(context, MyWorkActivity.class).putExtra(EXTRA_DATA, staffBean).putExtra(EXTRA_TYPE, MYSELF);
    }

    public static Intent startMyWorkActivityFromStaff (Context context, StaffBean staffBean){
        return new Intent(context, MyWorkActivity.class).putExtra(EXTRA_DATA, staffBean).putExtra(EXTRA_TYPE, STAFF);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_my_work);
    }

    @Override
    protected void setUpViewAndData() {
        staffBean = (StaffBean) getIntent().getSerializableExtra(EXTRA_DATA);
        type = getIntent().getStringExtra(EXTRA_TYPE);
        if(type.equals(MYSELF)){
            titleBar.setTilte("我的工作汇报");
        }else{
            titleBar.setTilte("Ta的汇报");
        }
        myWorksFragment= (MyWorksFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if(myWorksFragment == null){
            myWorksFragment = MyWorksFragment.newInstance(staffBean);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.contentFrame, myWorksFragment);
            transaction.commit();
        }
    }
}

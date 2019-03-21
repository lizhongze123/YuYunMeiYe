package cn.yuyun.yymy.ui.group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.http.result.ResultGroup;
import cn.yuyun.yymy.http.result.StaffBean;
import cn.yuyun.yymy.ui.home.work.MyWorksFragment;
import cn.yuyun.yymy.ui.store.StoreFragment;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author
 * @desc
 * @date
 */

public class GroupActivity extends BaseNoTitleActivity {

    private GroupStoreFragment storeFragment;

    public static Intent startGroupActivity (Context context, ResultGroup resultGroup){
        return new Intent(context, GroupActivity.class).putExtra(EXTRA_DATA, resultGroup);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_my_work);
    }

    @Override
    protected void setUpViewAndData() {
        storeFragment= (GroupStoreFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        ResultGroup resultGroup = (ResultGroup) getIntent().getSerializableExtra(EXTRA_DATA);
        if(storeFragment == null){
            storeFragment = GroupStoreFragment.newInstance(resultGroup);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.contentFrame, storeFragment);
            transaction.commit();
        }
    }
}

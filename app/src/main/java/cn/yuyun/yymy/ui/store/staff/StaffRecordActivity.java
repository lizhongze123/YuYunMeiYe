package cn.yuyun.yymy.ui.store.staff;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.result.StaffBean;
import cn.yuyun.yymy.ui.home.adapter.MyFragmentPagerAdapter;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.home.attendance.RecordFragment;
import cn.yuyun.yymy.ui.store.StoreAnalysisFragment1;
import cn.yuyun.yymy.ui.store.StoreAnalysisFragment2;
import cn.yuyun.yymy.ui.store.StoreAnalysisFragment3;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;

/**
 * @author
 * @desc 员工记录
 * @date
 */

public class StaffRecordActivity extends BaseActivity{

    private ArrayList<String> mTitleList = new ArrayList<>(4);
    private ArrayList<Fragment> mFragments = new ArrayList<>(4);
    private ViewPager vp_container;
    private TabLayout tab;
    private StaffBean staffBean;
    private StoreBean storeBean;

    public static Intent startStaffRecordActivity (Context context, StaffBean staffBean, StoreBean storeBean){
        return new Intent(context, StaffRecordActivity.class).putExtra(EXTRA_DATA, staffBean).putExtra(EXTRA_DATA2, storeBean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset);
    }

    @Override
    protected void setUpViewAndData() {
        initViews();
        initData();
    }

    private void initViews() {
        titleBar.setTilte("记录中心");
        vp_container = (ViewPager) findViewById(R.id.vp_container);
        tab = (TabLayout) findViewById(R.id.tab);
        staffBean = (StaffBean) getIntent().getSerializableExtra(EXTRA_DATA);
        storeBean = (StoreBean) getIntent().getSerializableExtra(EXTRA_DATA2);
        initFragmentList();
    }

    private void initFragmentList() {
        mTitleList.add("请假");
        mTitleList.add("预约");
        mTitleList.add("沟通");
        mTitleList.add("回访");
        mFragments.add(LeaveApplyForStaffFragment.newInstance(staffBean));
        mFragments.add(AppointmentListForStaffFragment.newInstance(staffBean, storeBean));
        mFragments.add(CommunicationListForStaffFragment.newInstance(staffBean, storeBean));
        mFragments.add(RevisitListForStaffFragment.newInstance(staffBean));
    }

    protected void initData() {
        /**
         * 注意使用的是：getChildFragmentManager，
         * 这样setOffscreenPageLimit()就可以添加上，保留相邻3个实例，切换时不会卡
         * 但会内存溢出，在显示时加载数据
         */
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments, mTitleList);
        vp_container.setAdapter(myFragmentPagerAdapter);
        // 左右预加载页面的个数
        vp_container.setOffscreenPageLimit(1);
        myFragmentPagerAdapter.notifyDataSetChanged();
        tab.setTabMode(TabLayout.MODE_FIXED);
        tab.setupWithViewPager(vp_container);
    }

}

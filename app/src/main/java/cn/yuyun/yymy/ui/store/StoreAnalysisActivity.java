package cn.yuyun.yymy.ui.store;

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
import cn.yuyun.yymy.ui.home.adapter.MyFragmentPagerAdapter;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author
 * @desc 门店分析
 * @date
 */

public class StoreAnalysisActivity extends BaseActivity{

    private ArrayList<String> mTitleList = new ArrayList<>(4);
    private ArrayList<Fragment> mFragments = new ArrayList<>(4);
    private ViewPager vp_container;
    private TabLayout tab;
    private List<String> spIdList;
    private String groupId;

    public static Intent startStoreAnalysisActivityForOne (Context context, List<String> spIdList, String groupId){
        return new Intent(context, StoreAnalysisActivity.class).putExtra(EXTRA_DATA, (Serializable) spIdList).putExtra(EXTRA_DATA2, groupId).putExtra(EXTRA_TYPE, 0);
    }

    public static Intent startStoreAnalysisActivityForTwo (Context context, List<String> spIdList, String groupId){
        return new Intent(context, StoreAnalysisActivity.class).putExtra(EXTRA_DATA, (Serializable) spIdList).putExtra(EXTRA_DATA2, groupId).putExtra(EXTRA_TYPE, 1);
    }

    public static Intent startStoreAnalysisActivityForThree (Context context, List<String> spIdList, String groupId){
        return new Intent(context, StoreAnalysisActivity.class).putExtra(EXTRA_DATA, (Serializable) spIdList).putExtra(EXTRA_DATA2, groupId).putExtra(EXTRA_TYPE, 2);
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
        titleBar.setTilte("门店分析");
        vp_container = (ViewPager) findViewById(R.id.vp_container);
        tab = (TabLayout) findViewById(R.id.tab);
        spIdList = (List<String>) getIntent().getSerializableExtra(EXTRA_DATA);
        groupId = getIntent().getStringExtra(EXTRA_DATA2);
        initFragmentList();
    }

    private void initFragmentList() {
        mTitleList.add("储值余额");
        mTitleList.add("累计欠款");
        mTitleList.add("累未耗");
        mFragments.add(StoreAnalysisFragment1.newInstance(spIdList, groupId));
        mFragments.add(StoreAnalysisFragment2.newInstance(spIdList, groupId));
        mFragments.add(StoreAnalysisFragment3.newInstance(spIdList, groupId));
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

        if(getIntent().getIntExtra(EXTRA_TYPE, 1) == 0){
            vp_container.setCurrentItem(0);
        }else if(getIntent().getIntExtra(EXTRA_TYPE, 1) == 1){
            vp_container.setCurrentItem(1);
        }else{
            vp_container.setCurrentItem(2);
        }

    }

}

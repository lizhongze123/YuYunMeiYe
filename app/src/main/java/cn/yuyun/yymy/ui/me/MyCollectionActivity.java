package cn.yuyun.yymy.ui.me;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.ui.home.adapter.MyFragmentPagerAdapter;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc 我的收藏
 * @date
 */

public class MyCollectionActivity extends BaseActivity{

    private ArrayList<String> mTitleList = new ArrayList<>(4);
    private ArrayList<Fragment> mFragments = new ArrayList<>(4);
    private ViewPager vp_container;
    private TabLayout tab;
    private ResultMemberBean memberBean;

    @Override
    protected void setUpViewAndData() {
        initViews();
        initData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset);
    }

    private void initViews() {
        titleBar.setTilte("我的收藏");
        vp_container = (ViewPager) findViewById(R.id.vp_container);
        tab = (TabLayout) findViewById(R.id.tab);
        memberBean = (ResultMemberBean) getIntent().getSerializableExtra(EXTRA_DATA);
        initFragmentList();
    }

    private void initFragmentList() {
        mTitleList.add("晒单");
        mTitleList.add("活动");
        mTitleList.add("通知");
        mTitleList.add("培训");
        mFragments.add(new CollectionOneFragment());
        mFragments.add(new CollectionTwoFragment());
        mFragments.add(new CollectionThreeFragment());
        mFragments.add(new CollectionFourFragment());
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
        vp_container.setOffscreenPageLimit(3);
        myFragmentPagerAdapter.notifyDataSetChanged();
        tab.setTabMode(TabLayout.MODE_FIXED);
        tab.setupWithViewPager(vp_container);
    }

}

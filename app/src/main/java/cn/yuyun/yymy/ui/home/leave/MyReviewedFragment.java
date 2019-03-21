package cn.yuyun.yymy.ui.home.leave;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseNoTitleFragment;
import cn.yuyun.yymy.ui.home.adapter.MyFragmentPagerAdapter;
import cn.yuyun.yymy.utils.EvenManager;

/**
 * @author
 * @desc
 * @date
 */
public class MyReviewedFragment extends BaseNoTitleFragment {

    private ArrayList<String> mTitleList = new ArrayList<>(2);
    private ArrayList<Fragment> mFragments = new ArrayList<>(2);
    private ViewPager vp_container;
    private TabLayout tab;


    @Override
    protected int getTheLayoutId() {
        return R.layout.activity_asset;
    }

    @Override
    protected void onBindViewBefore(View root) {
        super.onBindViewBefore(root);
        mContext = getContext();
    }

    @Override
    protected void initViews(View root) {
        super.initViews(root);
        vp_container = (ViewPager) root.findViewById(R.id.vp_container);
        tab = (TabLayout) root.findViewById(R.id.tab);
        initFragmentList();
    }


    private void initFragmentList() {
        mTitleList.add("待审批");
        mTitleList.add("已审批");
        mFragments.add(LeaveReviewedOneFragment.newInstance(null));
        mFragments.add(LeaveReviewedTwoFragment.newInstance(null));
    }

    @Override
    protected void initData() {
        /**
         * 注意使用的是：getChildFragmentManager，
         * 这样setOffscreenPageLimit()就可以添加上，保留相邻3个实例，切换时不会卡
         * 但会内存溢出，在显示时加载数据
         */
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), mFragments, mTitleList);
        vp_container.setAdapter(myFragmentPagerAdapter);
        // 左右预加载页面的个数
        vp_container.setOffscreenPageLimit(3);
        myFragmentPagerAdapter.notifyDataSetChanged();
        tab.setTabMode(TabLayout.MODE_FIXED);
        tab.setupWithViewPager(vp_container);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EvenManager.unregister(this);
    }

}

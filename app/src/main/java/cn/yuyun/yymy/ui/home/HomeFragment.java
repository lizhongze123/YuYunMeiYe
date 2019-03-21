package cn.yuyun.yymy.ui.home;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lzz.utils.LogUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseNoTitleFragment;
import cn.yuyun.yymy.main.MainActivity;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.ui.home.adapter.MyFragmentPagerAdapter;
import cn.yuyun.yymy.ui.home.other.SearchGlobalActivity;
import cn.yuyun.yymy.ui.home.other.SystemNoticeListActivity;
import cn.yuyun.yymy.ui.home.unboxing.FilterUnboxingLabelActivity;
import cn.yuyun.yymy.view.CustomScrollViewPager;

/**
 * @author
 * @desc
 * @date
 */

public class HomeFragment extends BaseNoTitleFragment {

    @BindView(R.id.tv_home)
    TextView tvHome;
    @BindView(R.id.line_home)
    View lineHome;
    @BindView(R.id.tv_unboxing)
    TextView tvUnboxing;
    @BindView(R.id.line_unboxing)
    View lineUnboxing;
    @BindView(R.id.tv_dot)
    TextView tvDot;
    @BindView(R.id.rl_notice)
    RelativeLayout rlNotice;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.iv_label)
    ImageView ivLabel;
    private ArrayList<String> mTitleDataList = new ArrayList<>(2);
    private ArrayList<Fragment> mFragments = new ArrayList<>(2);

    @BindView(R.id.vp_container)
    CustomScrollViewPager vpContainer;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onResume() {
        StatusBarCompat.setStatusBarColor(getActivity(), getResources().getColor(R.color.title_bg));
        super.onResume();
    }

    @Override
    protected void initViews(View root) {
        super.initViews(root);
        if(TextUtils.isEmpty(LoginInfoPrefs.getInstance(getContext()).getToken())){
            showLogin();
            LogUtils.e("没有登录");
            ((MainActivity)getActivity()).doDiscovery();
        }else{
            LogUtils.e("登录了");
               //要先设置tooken
            com.example.http.ApiRequestInterceptor.getInstance().setToken(LoginInfoPrefs.getInstance(getContext()).getToken());
            mTitleDataList.add("首页");
            mTitleDataList.add("晒单");
            mFragments.add(HomeChildHomeFragment.newInstance());
            mFragments.add(HomeChildUnboxingFragment.newInstance());
            rlNotice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mContext, SystemNoticeListActivity.class));
                }
            });
            ivSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mContext, SearchGlobalActivity.class));
                }
            });
            ivLabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mContext, FilterUnboxingLabelActivity.class));
                }
            });
        }
    }

    @Override
    protected int getTheLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initData() {
        super.initData();
        if(TextUtils.isEmpty(LoginInfoPrefs.getInstance(getContext()).getToken())){
           return;
        }
        vpContainer.setScrollable(false);
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), mFragments, mTitleDataList);
        vpContainer.setAdapter(myFragmentPagerAdapter);
        // 左右预加载页面的个数
        vpContainer.setOffscreenPageLimit(1);
        vpContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    lineHome.setVisibility(View.VISIBLE);
                    lineUnboxing.setVisibility(View.INVISIBLE);
                }else{
                    lineHome.setVisibility(View.INVISIBLE);
                    lineUnboxing.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        myFragmentPagerAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.ll_home, R.id.ll_unboxing})
    public void toggle(View v) {
        if(v.getId() == R.id.ll_home){
            lineHome.setVisibility(View.VISIBLE);
            lineUnboxing.setVisibility(View.INVISIBLE);
            tvHome.setTextAppearance(mContext, R.style.home_tab_text_selected);
            tvUnboxing.setTextAppearance(mContext, R.style.home_tab_text_unselected);
            vpContainer.setCurrentItem(0);
            ivSearch.setVisibility(View.VISIBLE);
            rlNotice.setVisibility(View.VISIBLE);
            ivLabel.setVisibility(View.GONE);
        }else{
            lineHome.setVisibility(View.INVISIBLE);
            lineUnboxing.setVisibility(View.VISIBLE);
            tvUnboxing.setTextAppearance(mContext, R.style.home_tab_text_selected);
            tvHome.setTextAppearance(mContext, R.style.home_tab_text_unselected);
            vpContainer.setCurrentItem(1);
            ivSearch.setVisibility(View.GONE);
            rlNotice.setVisibility(View.GONE);
            ivLabel.setVisibility(View.VISIBLE);
        }
    }

    public void setDot(int num){
        if(num == 0){
            tvDot.setVisibility(View.GONE);
        }else {
            tvDot.setVisibility(View.VISIBLE);
            tvDot.setText(num + "");
        }
    }

}

package cn.yuyun.yymy.ui.home.leave;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

import butterknife.BindView;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.base.BaseNoTitleFragment;
import cn.yuyun.yymy.http.result.StaffBean;
import cn.yuyun.yymy.ui.home.adapter.MyFragmentPagerAdapter;
import cn.yuyun.yymy.utils.EvenManager;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author
 * @desc
 * @date
 */
public class LeaveActivity extends BaseNoTitleActivity {

    private MyReviewedFragment mReviewedFragment;
    private MyApplyFragment mApplyFragment;

    private Fragment[] fragmentAry = null;
    private int currentIndex;
    private int selectedIndex;
    private StaffBean staffBean;
    @BindView(R.id.rb_one)
    RadioButton rbOne;
    @BindView(R.id.rb_two)
    RadioButton rbTwo;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_add)
    ImageView ivAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_all);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initData();
    }

    private void initViews() {
        staffBean = (StaffBean) getIntent().getSerializableExtra(EXTRA_DATA);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, NewLeaveActivity.class));
            }
        });
        initFragmentList();
        rbOne.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    selectedIndex = 0;
                    if (selectedIndex != currentIndex) {
                        FragmentTransaction transation = getSupportFragmentManager().beginTransaction();
                        transation.hide(fragmentAry[currentIndex]);
                        if (!fragmentAry[selectedIndex].isAdded()) {
                            transation.add(R.id.main_container, fragmentAry[selectedIndex]);
                        }
                        transation.show(fragmentAry[selectedIndex]);
                        transation.commit();
                        currentIndex = selectedIndex;
                    }
                }
            }
        });
        rbTwo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    selectedIndex = 1;
                    if (selectedIndex != currentIndex) {
                        FragmentTransaction transation = getSupportFragmentManager().beginTransaction();
                        transation.hide(fragmentAry[currentIndex]);
                        if (!fragmentAry[selectedIndex].isAdded()) {
                            transation.add(R.id.main_container, fragmentAry[selectedIndex]);
                        }
                        transation.show(fragmentAry[selectedIndex]);
                        transation.commit();
                        currentIndex = selectedIndex;
                    }
                }
            }
        });
    }

    private void initFragmentList() {
        mReviewedFragment = new MyReviewedFragment();
        mApplyFragment = MyApplyFragment.newInstance(staffBean);
        fragmentAry = new Fragment[]{mReviewedFragment, mApplyFragment};
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_container, mReviewedFragment);
        fragmentTransaction.show(mReviewedFragment);
        fragmentTransaction.commit();
    }

    protected void initData() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EvenManager.unregister(this);
    }
}

package cn.yuyun.yymy.ui.home.member.memberdata;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;

import java.util.ArrayList;

import butterknife.BindView;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.constan.Constans;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.ui.home.adapter.MyFragmentPagerAdapter;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;
import static cn.yuyun.yymy.ui.home.member.memberdata.MemberDetailActivity.TYPE_PUBLIC;

/**
 * @author
 * @desc 会员详细资料
 * @date
 */

public class MemberDataActivity extends BaseNoTitleActivity{

    private ResultMemberBean memberBean;
    private String type;
    private FrameLayout flContainer;
    @BindView(R.id.radioGroupTitle)
    RadioGroup radioGroupTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.iv_edit)
    ImageView ivEdit;

    MemberDataOneFragment oneFragment;
    MemberDataThreeFragment threeFragment;

    public static Intent startMemberDataActivity(Context context, ResultMemberBean memberBean) {
        return new Intent(context, MemberDataActivity.class).putExtra(EXTRA_DATA, memberBean);
    }

    public static Intent startMemberDataActivityFromPublic(Context context, ResultMemberBean memberBean) {
        return new Intent(context, MemberDataActivity.class).putExtra(EXTRA_DATA, memberBean).putExtra(EXTRA_TYPE, TYPE_PUBLIC);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_data_detail);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        memberBean = (ResultMemberBean) getIntent().getSerializableExtra(EXTRA_DATA);
        type = getIntent().getStringExtra(EXTRA_TYPE);
        initViews();
    }

    private void initViews() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        flContainer = (FrameLayout) findViewById(R.id.fragment_container);

        if(null == type){
            ivAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //调用Fragment的方法
                    if(Constans.isStoreLoginer){
                        threeFragment.add();
                    }else{
                        showToast(getString(R.string.PARTNER));
                    }
                }
            });
            ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Constans.isStoreLoginer){
                        oneFragment.setEdit();
                        ivEdit.setVisibility(View.GONE);
                    }else{
                        showToast(getString(R.string.PARTNER));
                    }
                }
            });
        }else{
            //公共会员不能修改资料
            ivEdit.setVisibility(View.GONE);
            ivAdd.setVisibility(View.GONE);
        }
        radioGroupTitle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_one) {
                    FragmentTransaction transation = getSupportFragmentManager().beginTransaction();
                    transation.hide(threeFragment);
                    if (!oneFragment.isAdded()) {
                        transation.add(R.id.fragment_container, oneFragment);
                    }
                    transation.show(oneFragment);
                    transation.commit();

                    if(type == null){
                        ivEdit.setVisibility(View.VISIBLE);
                        ivAdd.setVisibility(View.GONE);
                    }else{
                        ivEdit.setVisibility(View.GONE);
                        ivAdd.setVisibility(View.GONE);
                    }
                } else if(checkedId == R.id.rb_two) {
                    FragmentTransaction transation = getSupportFragmentManager().beginTransaction();
                    transation.hide(oneFragment);
                    if (!threeFragment.isAdded()) {
                        transation.add(R.id.fragment_container, threeFragment);
                    }
                    transation.show(threeFragment);
                    transation.commit();
                    if(type == null){
                        ivEdit.setVisibility(View.GONE);
                        ivAdd.setVisibility(View.VISIBLE);
                    }else{
                        ivEdit.setVisibility(View.GONE);
                        ivAdd.setVisibility(View.GONE);
                    }
                    oneFragment.setNormal();
                }
            }
        });
        initFragmentList();

    }

    public void setSave(){
        ivEdit.setVisibility(View.VISIBLE);
    }

    private void initFragmentList() {
        oneFragment = MemberDataOneFragment.newInstance(memberBean, type);
        threeFragment = MemberDataThreeFragment.newInstance(memberBean, type);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, oneFragment);
        fragmentTransaction.show(oneFragment);
        fragmentTransaction.commit();
    }

}

package cn.yuyun.yymy.ui.home.work;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.NoneProgressSubscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.bean.Sex;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestReadWork;
import cn.yuyun.yymy.http.result.StaffBean;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.view.dialog.WorkPopup;

/**
 * @author lzz
 * @desc 工作汇报
 * @date
 */

public class WorkActivity extends BaseActivity{

    private MomentsUnreadFragment momentsUnreadFragment;
    private MomentsReadFragment momentsReadFragment;
    private Fragment[] fragmentAry = null;
    private int currentIndex;
    private int selectedIndex;
    @BindView(R.id.rb_one)
    RadioButton rbOne;
    @BindView(R.id.rb_two)
    RadioButton rbTwo;
    @BindView(R.id.tv_more)
    TextView tvMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
    }

    private void initViews() {
        titleBar.setTilte("工作汇报");
        titleBar.setRightIcon(R.drawable.icon_more);
        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, AllWorkActivity.class));
            }
        });
        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new WorkPopup(mContext).setOnPopupClickListener(new WorkPopup.OnPopupClickListener() {
                    @Override
                    public void onPositive(int pos) {
                        if(pos == 0){
                            startActivity(new Intent(mContext, PublishMomentsActivity.class));
                        }else if(pos == 1){
                            StaffBean staffBean = new StaffBean();
                            if (UserfoPrefs.getInstance(mContext).getSEX().equals(Sex.FEMALE.desc)) {
                                staffBean.sex = Sex.FEMALE;
                            } else {
                                staffBean.sex = Sex.MALE;
                            }
                            staffBean.staffId = LoginInfoPrefs.getInstance(mContext).getStaffId();
                            staffBean.avatar = UserfoPrefs.getInstance(mContext).getAvatar();
                            staffBean.staffName = UserfoPrefs.getInstance(mContext).getStaffName();
                            startActivity(MyWorkActivity.startMyWorkActivityFromMyself(mContext, staffBean));
                        }else{
                            //一键已读
                            read();
                        }
                    }
                }).showAtBottom(titleBar);


            }
        });
        initFragmentList();
    }

    private void initFragmentList() {
        momentsUnreadFragment = new MomentsUnreadFragment();
        momentsReadFragment = new MomentsReadFragment();
        fragmentAry = new Fragment[]{momentsUnreadFragment, momentsReadFragment};
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_container, momentsUnreadFragment);
        fragmentTransaction.show(momentsUnreadFragment);
        fragmentTransaction.commit();

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

    private void read() {
        RequestReadWork body = new RequestReadWork();
        AppModelFactory.model().readWork(body, new NoneProgressSubscriber<DataBean<Object>>(mContext) {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(DataBean<Object> result) {

            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.getMessage());
            }

        });
    }

}

package cn.yuyun.yymy.ui.home.member;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.RadioGroup;

import com.example.http.ApiException;
import com.example.http.ProgressSubscriber;

import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestBirthdayMember;
import cn.yuyun.yymy.http.result.ResultBirthdayMember;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;

/**
 * @author lzz
 * @desc 生日会员
 * @date 2018/3/31
 */

public class BirthdayMemberListActivity extends BaseActivity  implements RadioGroup.OnCheckedChangeListener{

    private BirthdayMemberAdapter mAdapter;
    private RecyclerView recyclerView;
    private int day = 7;
    private RadioGroup rg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday_member_list);

    }

    @Override
    protected void setUpViewAndData() {
        initViews();
        getData();
    }

    private void initViews() {
        titleBar.setTilte("生日会员");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        mAdapter = new BirthdayMemberAdapter(mContext);
        recyclerView.setAdapter(mAdapter);
        rg = (RadioGroup) findViewById(R.id.radioGroup);
        rg.check(R.id.rb1);
        rg.setOnCheckedChangeListener(this);
    }


    private void getData(){
        if(!TextUtils.isEmpty(UserfoPrefs.getInstance(mContext).getOgId())){
            RequestBirthdayMember requestBirthdayMember = new RequestBirthdayMember();
            requestBirthdayMember.sp_id = UserfoPrefs.getInstance(mContext).getOgId();
            requestBirthdayMember.staff_id = LoginInfoPrefs.getInstance(mContext).getStaffId();
            requestBirthdayMember.waring_day = day;
            requestBirthdayMember.baseon_type = UserfoPrefs.getInstance(mContext).getBaseonType();
            AppModelFactory.model().getBirthdayMember(requestBirthdayMember, new ProgressSubscriber<List<ResultBirthdayMember>>(mContext) {
                @Override
                public void onNext(List<ResultBirthdayMember> result) {
                    if(null != result){
                        mAdapter.notifyDataSetChanged(result);
                    }
                }

                @Override
                public void onError(ApiException ex) {
                    super.onError(ex);
                    showToast(ex.message);
                }
            });
        }



    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rb1:
                day = 7;
                break;
            case R.id.rb2:
                day = 15;
                break;
            case R.id.rb3:
                day = 30;
                break;
            default:
        }
        getData();
    }
}

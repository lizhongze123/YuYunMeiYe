package cn.yuyun.yymy.ui.home.member;

import android.os.Bundle;

import com.example.http.ApiException;
import com.example.http.ProgressSubscriber;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestEmail;

/**
 * @author lzz
 * @desc 会员消费分析Activity
 * @date 2018/1/23
 */

public class MemberAnalysisActivity extends BaseActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_analysis);
        initViews();
    }

    private void initViews() {
        titleBar.setTilte("消费分析");
    }


}

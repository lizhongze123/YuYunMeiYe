package cn.yuyun.yymy.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.lzz.utils.StringFormatUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestLineData;
import cn.yuyun.yymy.http.result.ResultLineData;
import cn.yuyun.yymy.ui.home.work.WorkLikePeopleAdapter;
import cn.yuyun.yymy.view.lineview.BusinessLineView;
import cn.yuyun.yymy.view.lineview.FundMode;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/4/27
 */
public class TestActivity extends BaseActivity{

    @BindView(R.id.rv_like)
    RecyclerView rvLike;
    TestAdapter testAdapter;
    WorkLikePeopleAdapter workLikePeopleAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
//        rvLike.setHasFixedSize(true);
        rvLike.setLayoutManager(new StaggeredGridLayoutManager(5,  StaggeredGridLayoutManager.VERTICAL));
//        rvLike.setAdapter(testAdapter = new TestAdapter(mContext));
        rvLike.setAdapter(workLikePeopleAdapter = new WorkLikePeopleAdapter(mContext));
    }
}

package cn.yuyun.yymy.ui.me;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.ajguan.library.EasyRefreshLayout;
import com.example.http.PageInfo;
import com.example.http.Presenter;

import java.util.List;

import cn.lzz.utils.DateTimeUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.request.RequestPersonTime;
import cn.yuyun.yymy.http.result.PersonTimeListBean;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.ui.me.adapter.PersonCountAdapter;
import cn.yuyun.yymy.ui.me.adapter.PeopleNumberAdapter;
import rx.Subscriber;

/**
 * @author lzz
 * @desc 人数人次Activity
 * @date 2018/1/17
 */

public class PeopleNumActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private Presenter<PersonTimeListBean> presenter;
    private EasyRefreshLayout easyRefreshLayout;
    private RecyclerView rvCount;

    private LinearLayout llCount, llNum;

    private PersonCountAdapter countAdapter;
    private RequestPersonTime requestBean;

    private RecyclerView rvNum;
    private PeopleNumberAdapter numberAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_num);

    }

    @Override
    protected void setUpViewAndData() {
        initViews();
        initData();
        configurePresenter();
    }

    private void initData() {
        requestBean = new RequestPersonTime();
        requestBean.start = DateTimeUtils.getTimesMonthMorning();
        requestBean.end = DateTimeUtils.getTimesMonthNight();
        requestBean.staffId = LoginInfoPrefs.getInstance(mContext).getStaffId();
    }

    private void initViews() {
        titleBar.setTilte("人数/人次");
        ((RadioGroup) findViewById(R.id.radioGroup)).setOnCheckedChangeListener(this);
        llCount = (LinearLayout) findViewById(R.id.ll_count);
        llNum = (LinearLayout) findViewById(R.id.ll_num);
        rvCount = (RecyclerView) findViewById(R.id.rv_count);
        rvNum = (RecyclerView) findViewById(R.id.rv_num);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(this);
        rvCount.setLayoutManager(mLayoutManager);
        rvNum.setLayoutManager(mLayoutManager2);

        numberAdapter = new PeopleNumberAdapter(this);
        rvNum.setAdapter(numberAdapter);

        countAdapter = new PersonCountAdapter(this);
        rvCount.setAdapter(countAdapter);

        easyRefreshLayout = (EasyRefreshLayout) findViewById(R.id.easylayout);
        easyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                presenter.loadData(false);
            }

            @Override
            public void onRefreshing() {
                presenter.loadData(true);
            }
        });

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        if (checkedId == R.id.rb_count) {
            llCount.setVisibility(View.VISIBLE);
            llNum.setVisibility(View.GONE);
        } else if (checkedId == R.id.rb_num) {
            llCount.setVisibility(View.GONE);
            llNum.setVisibility(View.VISIBLE);
        }
    }

    private void configurePresenter() {
        presenter = new Presenter<>();
        presenter.setLoadDataListener(new Presenter.OnPresenterLoadListener<PersonTimeListBean>(){

            @Override
            public void onSuccess(List<PersonTimeListBean> result, int total, boolean isRefresh) {
                showToast("success");
            }

            @Override
            public void onFailed(boolean isRefresh) {
                showToast("加载失败，请稍候重试");
            }

            @Override
            public void onCompleted(boolean isRefresh) {
                easyRefreshLayout.refreshComplete();
                easyRefreshLayout.closeLoadView();
            }

            @Override
            public void onEmptyData() {

            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<PageInfo<PersonTimeListBean>> subscriber, int pageIndex, int pageSize) {
//                AppModelFactory.model().getPersonTimeList(requestBean, pageIndex, pageSize, subscriber);
            }
        });
    }



}

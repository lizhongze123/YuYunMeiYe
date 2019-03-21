package cn.yuyun.yymy.ui.home.appointment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.PageInfo;
import com.example.http.Presenter2;
import com.example.http.ProgressSubscriber;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestProject;
import cn.yuyun.yymy.http.result.ResultProject;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;

/**
 * @author
 * @desc 选择预约项目
 * @date
 */

public class ProjectListActivity extends BaseActivity {

    private EasyRefreshLayout easyRefreshLayout;
    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private ProjectListAdapter mAdapter;
    private StoreBean storeBean;

    @BindView(R.id.et_search)
    EditText etSearch;

    private Presenter2<ResultProject> presenter;

    RequestProject body = new RequestProject();

    private List<ResultProject> mIntentResult;
    private List<ResultProject> mDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);
    }

    public static Intent startProjectListActivity(Context context, StoreBean storeBean, List<ResultProject> list) {
        return new Intent(context, ProjectListActivity.class).putExtra(EXTRA_DATA, storeBean).putExtra(EXTRA_DATA2, (Serializable) list);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initData();
    }

    private void initData() {
        configurePresenter();
        List<String>list = new ArrayList<>();
        if(null != storeBean){
            list.add(storeBean.spId);
        }else{
            list.add(UserfoPrefs.getInstance(mContext).getBaseonId());
        }
        body.spIdList = list;
        easyRefreshLayout.autoRefresh();
    }

    private void initViews() {
        titleBar.setTilte("选择项目");
        titleBar.setTvRight("确定");
        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAdapter.getSelectedItem().size() == 0){
                    showToast("请至少选择一个项目");
                    return;
                }else{
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_DATA, (Serializable) mAdapter.getSelectedItem());
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }
        });
        storeBean = (StoreBean) getIntent().getSerializableExtra(EXTRA_DATA);
        if(getIntent().getSerializableExtra(EXTRA_DATA2) != null){
            mIntentResult = (List<ResultProject>)getIntent().getSerializableExtra(EXTRA_DATA2);
        }else{
            mIntentResult = new ArrayList<>();
        }
        easyRefreshLayout = (EasyRefreshLayout) findViewById(R.id.easylayout);
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(this));
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ProjectListAdapter(this, mIntentResult);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() <= 0) {
                    body.search_text = "";
                } else {
                    //网络搜索
                    body.search_text = s.toString();
                    /*List<ResultProject> resultList = new ArrayList<>();
                    if(null != mDataList){
                        for (ResultProject bean : mDataList) {
                            if(bean.name.indexOf(s.toString()) >= 0){
                                resultList.add(bean);
                            }
                        }
                    }
                    mAdapter.notifyDataSetChanged(resultList);*/
                }
                presenter.loadData(true);
            }
        });

        recyclerView.setAdapter(mAdapter);
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

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultProject>(){

            @Override
            public void onSuccess(List<ResultProject> result, int total, boolean isRefresh) {
                if(null != result){
                    easyRefreshLayout.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                    if (isRefresh) {
                        mAdapter.notifyDataSetChanged(result);
                    } else {
                        mAdapter.addAll(result);
                    }
                }
            }

            @Override
            public void onFailed(boolean isRefresh) {
                showError();
            }

            @Override
            public void onCompleted(boolean isRefresh) {
                easyRefreshLayout.refreshComplete();
                easyRefreshLayout.closeLoadView();
            }

            @Override
            public void onEmptyData() {
                easyRefreshLayout.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
                emptyLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultProject>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getProjectList(LoginInfoPrefs.getInstance(mContext).getGroupId(), pageIndex, pageSize,body, subscriber);
            }
        });

    }

    /*private void getData() {
        AppModelFactory.model().getProjectList(LoginInfoPrefs.getInstance(mContext).getGroupId(), 1, Integer.MAX_VALUE, body, new ProgressSubscriber<DataBean<PageInfo<ResultProject>>>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
                easyRefreshLayout.refreshComplete();
            }

            @Override
            public void onNext(DataBean<PageInfo<ResultProject>> result) {
                if(null != result.data){
                    if(null != result.data.dataLsit){
                        mDataList = result.data.dataLsit;
                        mAdapter.clear();
                        if(result.data.dataLsit.size() == 0){
                            showEmpty();
                        }else{
                            mAdapter.addAll(result.data.dataLsit);
                        }
                    }
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.message);
            }

        });
    }*/


}

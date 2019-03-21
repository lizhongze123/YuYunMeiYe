package cn.yuyun.yymy.ui.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.NoneProgressSubscriber;
import com.example.http.PageInfo;
import com.example.http.Presenter2;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lzz.utils.DateTimeUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestBillManage;
import cn.yuyun.yymy.http.request.RequestClassfiyStore;
import cn.yuyun.yymy.http.request.RequestProjectAmount;
import cn.yuyun.yymy.http.request.RequestStoreBill;
import cn.yuyun.yymy.http.result.ResultBillManage;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.StorePrefs;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.home.bill.BillManageAdapter;
import cn.yuyun.yymy.ui.home.bill.BillManageDetailActivity;
import cn.yuyun.yymy.ui.home.bill.BillManageStorevalueDetailActivity;
import cn.yuyun.yymy.ui.home.bill.BillManageStorevalueSystemDetailActivity;
import cn.yuyun.yymy.ui.home.bill.BillManageSystemDetailActivity;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc 单据管理
 * @date
 */

public class ProjectAmountActivity extends BaseNoTitleActivity{

    private Presenter2<ResultBillManage> presenter;

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;
    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;

    private ProjectAmountAdapter adapter;
    private RequestProjectAmount requestBillManage = new RequestProjectAmount();
    private StoreBean storeBean;

    public static Intent startProjectAmountActivity(Context context, StoreBean bean) {
        return new Intent(context, ProjectAmountActivity.class).putExtra(EXTRA_DATA, bean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_amount);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    private void initDatas() {
        configurePresenter();
        easyRefreshLayout.autoRefresh();
        requestBillManage.start_date = DateTimeUtils.getTimesMonthMorning();
        requestBillManage.end_date = DateTimeUtils.getTimesMonthMorning();
        requestBillManage.group_id = storeBean.group_id;
    }

    private void initViews() {
        storeBean = (StoreBean) getIntent().getSerializableExtra(EXTRA_DATA);
        adapter = new ProjectAmountAdapter(mContext);
        rv.setLayoutManager(new LinearLayoutManager(this));
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(mContext));
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
        rv.setAdapter(adapter);

        emptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.loadData(true);
            }
        });
        emptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultBillManage>() {

            @Override
            public void onSuccess(List<ResultBillManage> result, int total, final boolean isRefresh) {
                if (null != result) {
                    emptyLayout.setVisibility(View.GONE);
                    easyRefreshLayout.setVisibility(View.VISIBLE);
                    if (isRefresh) {
                        adapter.notifyDataSetChanged(result);
                    } else {
                        adapter.addAll(result);
                    }
                }
            }

            @Override
            public void onFailed(boolean isRefresh) {
                easyRefreshLayout.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
                emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
            }

            @Override
            public void onCompleted(boolean isRefresh) {
                easyRefreshLayout.closeLoadView();
                easyRefreshLayout.refreshComplete();
            }

            @Override
            public void onEmptyData() {
                emptyLayout.setVisibility(View.VISIBLE);
                emptyLayout.setErrorType(EmptyLayout.NODATA);
                easyRefreshLayout.setVisibility(View.GONE);
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");

            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultBillManage>>> subscriber, int pageIndex, int pageSize) {
//                AppModelFactory.model().getProjectAmount(requestBillManage, LoginInfoPrefs.getInstance(mContext).getGroupId(), pageIndex, pageSize, subscriber);
            }
        });
    }

    private void getClassfiyStoreList() {
        RequestClassfiyStore requestClassfiyStore = new RequestClassfiyStore();
        AppModelFactory.model().getMyStoreList(LoginInfoPrefs.getInstance(mContext).getGroupId(), requestClassfiyStore, new NoneProgressSubscriber<DataBean<List<ResultClassfiyStoreBean>>>(mContext) {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(DataBean<List<ResultClassfiyStoreBean>> result) {
                if (null != result) {
                    if (null != result.data) {
                        //缓存门店列表数据
                        Gson gson = new Gson();
                        String json = gson.toJson(result.data);
                        StorePrefs.getInstance(mContext).saveInfo(json);

                        List<String> spIdList = new ArrayList<>();
                        for (int i = 0, iLength = result.data.size(); i < iLength; i++) {
                            for (int j = 0, jLength = result.data.get(i).ogServiceplacesRspList.size(); j < jLength; j++) {
                                spIdList.add(result.data.get(i).ogServiceplacesRspList.get(j).sp_id);
                            }
                        }
//                        requestStoreBill.spId = spIdList;
//                        presenter2.loadData(true);
                    }
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.message);
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(mContext.getString(R.string.error_no_network));
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast(ex.message);
            }
        });
    }


}

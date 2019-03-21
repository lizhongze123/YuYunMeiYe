package cn.yuyun.yymy.ui.home.bill;

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
import cn.yuyun.yymy.http.request.RequestStoreBill;
import cn.yuyun.yymy.http.result.ResultBillManage;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.StorePrefs;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

/**
 * @author
 * @desc
 * @date
 */

public class BillManageActivity extends BaseNoTitleActivity{

    private Presenter2<ResultBillManage> presenter;
    private Presenter2<ResultBillManage> presenter2;

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.rb_one)
    RadioButton rbOne;
    @BindView(R.id.rb_two)
    RadioButton rbTwo;
    @BindView(R.id.radioGroupTitle)
    RadioGroup radioGroupTitle;

    @BindView(R.id.ll_one)
    LinearLayout llOne;
    @BindView(R.id.ll_two)
    LinearLayout llTwo;

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.rvTwo)
    RecyclerView rvTwo;

    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.et_inputTwo)
    EditText etInputTwo;
    @BindView(R.id.rl_start)
    RelativeLayout rlStart;
    @BindView(R.id.rl_end)
    RelativeLayout rlEnd;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_end)
    TextView tvEnd;

    @BindView(R.id.rl_startTwo)
    RelativeLayout rlStartTwo;
    @BindView(R.id.rl_endTwo)
    RelativeLayout rlEndTwo;
    @BindView(R.id.tv_startTwo)
    TextView tvStartTwo;
    @BindView(R.id.tv_endTwo)
    TextView tvEndTwo;

    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;
    @BindView(R.id.easylayoutTwo)
    EasyRefreshLayout easyRefreshLayoutTwo;
    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;
    @BindView(R.id.emptyLayoutTwo)
    EmptyLayout emptyLayoutTwo;

    private int timeType = 0;
    private TimePickerView timePickerView;

    private int timeTypeTwo = 0;
    private TimePickerView timePickerViewTwo;

    private BillManageAdapter adapter;
    private BillManageAdapter adapter2;
    private RequestBillManage requestBillManage = new RequestBillManage();
    private RequestStoreBill requestStoreBill = new RequestStoreBill();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_manage);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    private void initDatas() {
        requestBillManage.start = DateTimeUtils.getTimesMonthMorning();
        requestBillManage.end = DateTimeUtils.getNowTimeStamp();
        if (!TextUtils.isEmpty(LoginInfoPrefs.getInstance(mContext).getStaffId())) {

            requestBillManage.staffId = LoginInfoPrefs.getInstance(mContext).getStaffId();
            configurePresenter();
            easyRefreshLayout.autoRefresh();
            configurePresenter2();
            String json = StorePrefs.getInstance(mContext).getJson();
            if (!TextUtils.isEmpty(json)) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<ResultClassfiyStoreBean>>() {
                }.getType();
                List<ResultClassfiyStoreBean> list = gson.fromJson(json, type);

                List<String> spIdList = new ArrayList<>();
                for (int i = 0, iLength = list.size(); i < iLength; i++) {
                    for (int j = 0, jLength = list.get(i).ogServiceplacesRspList.size(); j < jLength; j++) {
                        spIdList.add(list.get(i).ogServiceplacesRspList.get(j).sp_id);
                    }
                }
                requestStoreBill.spId = spIdList;
                presenter2.loadData(true);
            } else {
                getClassfiyStoreList();
            }
        }
    }

    private void initViews() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        radioGroupTitle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_one) {
                    llOne.setVisibility(View.VISIBLE);
                    llTwo.setVisibility(View.GONE);
                } else if (checkedId == R.id.rb_two) {
                    llOne.setVisibility(View.GONE);
                    llTwo.setVisibility(View.VISIBLE);
                }
            }
        });

        adapter = new BillManageAdapter(mContext, 0);
        adapter2 = new BillManageAdapter(mContext, 1);
        adapter.setOnItemClickListener(new BillManageAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultBillManage bean, int position) {
                if (bean.recordType == 1) {
                    startActivity(BillManageStorevalueDetailActivity.startBillManageDetailActivity(mContext, bean.record_id));
                } else {
                    startActivity(BillManageDetailActivity.startBillManageDetailActivity(mContext, bean.record_id));
                }
            }
        });
        adapter2.setOnItemClickListener(new BillManageAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultBillManage bean, int position) {
                if (bean.recordType == 1) {
                    startActivity(BillManageStorevalueSystemDetailActivity.startBillManageStorevalueSystemDetailActivity(mContext, bean));
                } else {
                    startActivity(BillManageSystemDetailActivity.startBillManageDetailActivity(mContext, bean));
                }
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(this));
        rvTwo.setLayoutManager(new LinearLayoutManager(this));
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(mContext));
        easyRefreshLayoutTwo.setRefreshHeadView(new RefreshHeaderView(mContext));
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
        easyRefreshLayoutTwo.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                presenter2.loadData(false);
            }

            @Override
            public void onRefreshing() {
                presenter2.loadData(true);
            }
        });
        rv.setAdapter(adapter);
        rvTwo.setAdapter(adapter2);
        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                requestBillManage.searchText = editable.toString().trim();
                presenter.loadData(true);
            }
        });
        etInputTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                requestStoreBill.searchText = editable.toString().trim();
                presenter2.loadData(true);
            }
        });

        tvStart.setText(DateTimeUtils.getDateTimeText(DateTimeUtils.getTimesMonthMorning(), DateTimeUtils.FORMAT_DATETIME_YEAR_MONTH_DAY));
        tvEnd.setText(DateTimeUtils.getDateTimeText(DateTimeUtils.getNowTimeStamp(), DateTimeUtils.FORMAT_DATETIME_YEAR_MONTH_DAY));
        timePickerView = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (timeType == 0) {
                    tvStart.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATETIME_YEAR_MONTH_DAY));
                    requestBillManage.start = date.getTime() / 1000;
                } else {
                    tvEnd.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATETIME_YEAR_MONTH_DAY));
                    requestBillManage.end = date.getTime() / 1000;
                }
                if (requestBillManage.end - requestBillManage.start < 0) {
                    showTextDialog("结束时间不能小于开始时间", false);
                    return;
                }
                presenter.loadData(true);
            }
        }).build();

        tvStartTwo.setText(DateTimeUtils.getDateTimeText(DateTimeUtils.getTimesMonthMorning(), DateTimeUtils.FORMAT_DATETIME_YEAR_MONTH_DAY));
        tvEndTwo.setText(DateTimeUtils.getDateTimeText(DateTimeUtils.getNowTimeStamp(), DateTimeUtils.FORMAT_DATETIME_YEAR_MONTH_DAY));

        timePickerViewTwo = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (timeTypeTwo == 0) {
                    tvStartTwo.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATETIME_YEAR_MONTH_DAY));
                    requestStoreBill.start = date.getTime() / 1000;
                } else {
                    tvEndTwo.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATETIME_YEAR_MONTH_DAY));
                    requestStoreBill.end = date.getTime() / 1000;
                }
                if (requestStoreBill.end - requestStoreBill.start < 0) {
                    showTextDialog("结束时间不能小于开始时间", false);
                    return;
                }
                presenter2.loadData(true);
            }
        }).build();
        requestStoreBill.start = DateTimeUtils.getTimesMonthMorning();
        requestStoreBill.end = DateTimeUtils.getNowTimeStamp();
        emptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.loadData(true);
            }
        });
        emptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter2.loadData(true);
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
                emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR_ENABLE_CLICK);
            }

            @Override
            public void onCompleted(boolean isRefresh) {
                easyRefreshLayout.closeLoadView();
                easyRefreshLayout.refreshComplete();
            }

            @Override
            public void onEmptyData() {
                emptyLayout.setVisibility(View.VISIBLE);
                emptyLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
                easyRefreshLayout.setVisibility(View.GONE);
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");

            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultBillManage>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getBillManage(requestBillManage, LoginInfoPrefs.getInstance(mContext).getGroupId(), pageIndex, pageSize, subscriber);
            }
        });
    }

    private void configurePresenter2() {
        presenter2 = new Presenter2<>();
        presenter2.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultBillManage>() {

            @Override
            public void onSuccess(List<ResultBillManage> result, int total, final boolean isRefresh) {
                if (null != result) {
                    emptyLayoutTwo.setVisibility(View.GONE);
                    easyRefreshLayoutTwo.setVisibility(View.VISIBLE);
                    if (isRefresh) {
                        adapter2.notifyDataSetChanged(result);
                    } else {
                        adapter2.addAll(result);
                    }
                }
            }

            @Override
            public void onFailed(boolean isRefresh) {
                easyRefreshLayoutTwo.setVisibility(View.GONE);
                emptyLayoutTwo.setVisibility(View.VISIBLE);
                emptyLayoutTwo.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
            }

            @Override
            public void onCompleted(boolean isRefresh) {
                easyRefreshLayoutTwo.closeLoadView();
                easyRefreshLayoutTwo.refreshComplete();
            }

            @Override
            public void onEmptyData() {
                emptyLayoutTwo.setVisibility(View.VISIBLE);
                emptyLayoutTwo.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
                easyRefreshLayoutTwo.setVisibility(View.GONE);
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");

            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultBillManage>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getStoreConsumeBill(requestStoreBill, LoginInfoPrefs.getInstance(mContext).getGroupId(), pageIndex, pageSize, subscriber);
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
                        requestStoreBill.spId = spIdList;
                        presenter2.loadData(true);
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

    @OnClick({R.id.rl_start, R.id.rl_startTwo})
    public void start(View v) {
        if(v.getId() == R.id.rl_start){
            timeType = 0;
            timePickerView.show();
        }else{
            timeTypeTwo = 0;
            timePickerViewTwo.show();
        }
    }

    @OnClick({R.id.rl_end, R.id.rl_endTwo})
    public void end(View v) {
        if(v.getId() == R.id.rl_end){
            timeType = 1;
            timePickerView.show();
        }else{
            timeTypeTwo = 1;
            timePickerViewTwo.show();
        }
    }

}

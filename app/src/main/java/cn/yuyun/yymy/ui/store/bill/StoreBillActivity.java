package cn.yuyun.yymy.ui.store.bill;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
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
import com.example.http.PageInfo;
import com.example.http.Presenter2;
import com.example.http.ProgressSubscriber;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.DeviceUtils;
import cn.lzz.utils.ResoureUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestStoreBill;
import cn.yuyun.yymy.http.result.ResultBillManage;
import cn.yuyun.yymy.http.result.ResultLevel;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.home.bill.BillManageConsumeAdapte;
import cn.yuyun.yymy.ui.store.CardAdapter;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc 门店单据
 * @date
 */

public class StoreBillActivity extends BaseNoTitleActivity {

    public DrawerLayout drawerLayout;
    private RelativeLayout main_right_drawer_layout;

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.rb_one)
    RadioButton rbOne;
    @BindView(R.id.rb_two)
    RadioButton rbTwo;
    @BindView(R.id.radioGroupTitle)
    RadioGroup radioGroupTitle;
    @BindView(R.id.relay_background)
    RelativeLayout relayBackground;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.rl_searchPublicMember)
    RelativeLayout rlSearchPublicMember;
    @BindView(R.id.tv_desc1)
    TextView tvDesc1;
    @BindView(R.id.rl_desc1)
    RelativeLayout rlDesc1;
    @BindView(R.id.tv_desc2)
    TextView tvDesc2;
    @BindView(R.id.rl_desc2)
    RelativeLayout rlDesc2;
    @BindView(R.id.filter_list)
    LinearLayout filterList;
    @BindView(R.id.ll_filter)
    RelativeLayout llFilter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easylayout;
    @BindView(R.id.recyclerView2)
    RecyclerView recyclerView2;
    @BindView(R.id.easylayout2)
    EasyRefreshLayout easylayout2;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    @BindView(R.id.rl_one)
    RelativeLayout rlOne;
    @BindView(R.id.rl_two)
    RelativeLayout rlTwo;
    @BindView(R.id.emptyLayoutOne)
    EmptyLayout emptyLayoutOne;
    @BindView(R.id.emptyLayoutTwo)
    EmptyLayout emptyLayoutTwo;

    @BindView(R.id.et1)
    EditText etOne;
    @BindView(R.id.et2)
    EditText etTwo;

    @BindView(R.id.rv_card)
    RecyclerView rvCard;
    CardAdapter cardAdapter;

    private int timeType = 1;

    private int radioType = 1;

    private TimePickerView timePickerView;

    private StoreBean storeBean;

    private Presenter2<ResultBillManage> presenter;
    private Presenter2<ResultBillManage> presenter2;
    private BillManageConsumeAdapte adapter;
    private BillManageConsumeAdapte adapter2;
    private RequestStoreBill requestStoreBill = new RequestStoreBill();


    public static Intent startStoreBillActivity(Context context, StoreBean storeBean) {
        return new Intent(context, StoreBillActivity.class).putExtra(EXTRA_DATA, storeBean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_bill);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initData();
    }

    private void initData() {
        List<String> spId = new ArrayList<>();
        spId.add(storeBean.spId);
        requestStoreBill.spId = spId;
        easylayout.autoRefresh();
        presenter2.loadData(true);
        getLevelData();
    }

    public void initLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        //侧滑栏关闭手势滑动
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //设置菜单内容之外其他区域的背景色
        drawerLayout.setScrimColor(ResoureUtils.getColor(mContext, R.color.transparent_black));
        //左边菜单
        main_right_drawer_layout = (RelativeLayout) findViewById(R.id.main_right_drawer_layout);
        cardAdapter = new CardAdapter(mContext);
        rvCard.setLayoutManager(new GridLayoutManager(this, 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        cardAdapter.setOnItemClickListener(new CardAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultLevel bean, int position) {
                requestStoreBill.level_id = bean.levelId + "";
            }
        });
        rvCard.setAdapter(cardAdapter);
    }

    private void initViews() {
        storeBean = (StoreBean) getIntent().getSerializableExtra(EXTRA_DATA);
        adapter = new BillManageConsumeAdapte(mContext, 0);
        adapter.setOnItemClickListener(new BillManageConsumeAdapte.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultBillManage bean, int position) {
                startActivity(StoreBillDetailActivity.startStoreBillDetailActivityConsume(mContext, bean, storeBean.group_id));
            }
        });
        recyclerView.setAdapter(adapter);

        adapter2 = new BillManageConsumeAdapte(mContext, 1);
        adapter2.setOnItemClickListener(new BillManageConsumeAdapte.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultBillManage bean, int position) {
                startActivity(StoreBillStorevalueDetailActivity.startStoreBillStorevalueDetailActivity(mContext, bean, storeBean.group_id));
            }
        });
        recyclerView2.setAdapter(adapter2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        configurePresenter();
        configurePresenter2();
        rbOne.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rlOne.setVisibility(View.VISIBLE);
                    rlTwo.setVisibility(View.GONE);
                    radioType = 1;
                }
            }
        });
        rbTwo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rlTwo.setVisibility(View.VISIBLE);
                    rlOne.setVisibility(View.GONE);
                    radioType = 2;
                }
            }
        });
        timePickerView = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (timeType == 1) {
                    tvDesc1.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATE_UI));
                    requestStoreBill.start = date.getTime() / 1000;
                    if (requestStoreBill.end - requestStoreBill.start < 0) {
                        showTextDialog("结束时间不能小于开始时间", false);
                        return;
                    }
                    if (radioType == 1) {
                        presenter.loadData(true);
                    } else {
                        presenter2.loadData(true);
                    }
                } else if (timeType == 2) {
                    tvDesc2.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATE_UI));
                    requestStoreBill.end = date.getTime() / 1000;
                    if (requestStoreBill.end - requestStoreBill.start < 0) {
                        showTextDialog("结束时间不能小于开始时间", false);
                        return;
                    }
                    if (radioType == 1) {
                        presenter.loadData(true);
                    } else {
                        presenter2.loadData(true);
                    }
                }
            }
        }).build();
        tvDesc1.setText(DateTimeUtils.getDateTimeText(DateTimeUtils.getTimesMonthMorning(), DateTimeUtils.FORMAT_DATE_UI));
        tvDesc2.setText(DateTimeUtils.getNowTime());
        requestStoreBill.start = DateTimeUtils.getTimesMonthMorning();
        requestStoreBill.end = DateTimeUtils.getNowTimeStamp();
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
                    requestStoreBill.searchText = "";
                } else {
                    requestStoreBill.searchText = s.toString().trim();
                }
                if (radioType == 1) {
                    presenter.loadData(true);
                } else {
                    presenter2.loadData(true);
                }
            }
        });
        easylayout.setRefreshHeadView(new RefreshHeaderView(this));
        easylayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                presenter.loadData(false);
            }

            @Override
            public void onRefreshing() {
                presenter.loadData(true);
            }
        });
        easylayout2.setRefreshHeadView(new RefreshHeaderView(this));
        easylayout2.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                presenter2.loadData(false);
            }

            @Override
            public void onRefreshing() {
                presenter2.loadData(true);
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initLayout();
    }

    @OnClick({R.id.rl_desc2, R.id.rl_desc1})
    public void selectTime(View view) {
        if (view.getId() == R.id.rl_desc1) {
            timeType = 1;
            timePickerView.show();
        } else if (view.getId() == R.id.rl_desc2) {
            timeType = 2;
            timePickerView.show();
        }
    }

    @OnClick(R.id.rl_filter)
    public void filter(View v) {
        DeviceUtils.hideSoftKeyboard(v, mContext);
        openRightLayout(v);
    }

    @OnClick(R.id.tv_positive)
    public void positive(View v) {
        openRightLayout(v);
        requestStoreBill.gerate_than = etOne.getText().toString().trim();
        requestStoreBill.less_than = etTwo.getText().toString().trim();
        if (radioType == 1) {
           easylayout.autoRefresh();
        } else {
           easylayout2.autoRefresh();
        }
    }

    @OnClick(R.id.tv_negative)
    public void negative(View v) {
        openRightLayout(v);
        requestStoreBill.gerate_than = "";
        requestStoreBill.less_than = "";
        etOne.setText("");
        etTwo.setText("");
        cardAdapter.setSelectPos(-1);
        requestStoreBill.level_id = "";
        if (radioType == 1) {
            easylayout.autoRefresh();
        } else {
            easylayout2.autoRefresh();
        }
    }

    public void openRightLayout(View view) {
        if (drawerLayout.isDrawerOpen(main_right_drawer_layout)) {
            drawerLayout.closeDrawer(main_right_drawer_layout);
        } else {
            drawerLayout.openDrawer(main_right_drawer_layout);
        }
    }

    private void getLevelData() {

        AppModelFactory.model().getLevel(storeBean.group_id, new ProgressSubscriber<DataBean<List<ResultLevel>>>(mContext) {

            @Override
            public void onNext(DataBean<List<ResultLevel>> result) {
                if (null != result) {
                    cardAdapter.addAll(result.data);
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });
    }


    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultBillManage>() {

            @Override
            public void onSuccess(List<ResultBillManage> result, int total, final boolean isRefresh) {
                if (null != result) {
                    emptyLayoutOne.setVisibility(View.GONE);
                    easylayout.setVisibility(View.VISIBLE);
                    if (isRefresh) {
                        adapter.notifyDataSetChanged(result);
                    } else {
                        adapter.addAll(result);
                    }
                }
            }

            @Override
            public void onFailed(boolean isRefresh) {
                easylayout.setVisibility(View.GONE);
                emptyLayoutOne.setVisibility(View.VISIBLE);
                emptyLayoutOne.setErrorType(EmptyLayout.NETWORK_ERROR);
            }

            @Override
            public void onCompleted(boolean isRefresh) {
                easylayout.closeLoadView();
                easylayout.refreshComplete();
            }

            @Override
            public void onEmptyData() {
                easylayout.setVisibility(View.GONE);
                emptyLayoutOne.setVisibility(View.VISIBLE);
                emptyLayoutOne.setErrorType(EmptyLayout.NODATA);
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultBillManage>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getStoreConsumeBill(requestStoreBill, storeBean.group_id, pageIndex, pageSize, subscriber);
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
                    easylayout2.setVisibility(View.VISIBLE);
                    if (isRefresh) {
                        adapter2.notifyDataSetChanged(result);
                    } else {
                        adapter2.addAll(result);
                    }
                }
            }

            @Override
            public void onFailed(boolean isRefresh) {
                emptyLayoutTwo.setVisibility(View.VISIBLE);
                emptyLayoutTwo.setErrorType(EmptyLayout.NETWORK_ERROR);
                easylayout2.setVisibility(View.GONE);
            }

            @Override
            public void onCompleted(boolean isRefresh) {
                easylayout2.closeLoadView();
                easylayout2.refreshComplete();
            }

            @Override
            public void onEmptyData() {
                easylayout2.setVisibility(View.GONE);
                emptyLayoutTwo.setVisibility(View.VISIBLE);
                emptyLayoutTwo.setErrorType(EmptyLayout.NODATA);
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");

            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultBillManage>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getStoreStoredvalueBill(requestStoreBill, storeBean.group_id, pageIndex, pageSize, subscriber);
            }
        });
    }

}

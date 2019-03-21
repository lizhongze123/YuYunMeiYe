package cn.yuyun.yymy.ui.store.report;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.http.DataBean;
import com.example.http.PageInfo;
import com.example.http.Presenter2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.DeviceUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestStoreOutput;
import cn.yuyun.yymy.http.result.ResultReportStoreSale;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.MyHorizontalScrollView;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc 门店出库表
 * @date
 */

public class ReportStoreSaleActivity extends BaseActivity {

    @BindView(R.id.main_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.main_right_drawer_layout)
    RelativeLayout main_right_drawer_layout;

    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.rl_start)
    RelativeLayout rlStart;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.rl_end)
    RelativeLayout rlEnd;
    @BindView(R.id.ll_filter)
    LinearLayout llFilter;
    @BindView(R.id.tv_fixed)
    TextView tvFixed;
    @BindView(R.id.tv_one)
    TextView tvOne;
    @BindView(R.id.iv_one)
    ImageView ivOne;
    @BindView(R.id.tv_two)
    TextView tvTwo;
    @BindView(R.id.iv_two)
    ImageView ivTwo;
    @BindView(R.id.tv_three)
    TextView tvThree;
    @BindView(R.id.iv_three)
    ImageView ivThree;
    @BindView(R.id.tv_four)
    TextView tvFour;
    @BindView(R.id.iv_four)
    ImageView ivFour;
    @BindView(R.id.tv_five)
    TextView tvFive;
    @BindView(R.id.iv_five)
    ImageView ivFive;
    @BindView(R.id.tv_six)
    TextView tvSix;
    @BindView(R.id.iv_six)
    ImageView ivSix;
    @BindView(R.id.sv_top)
    MyHorizontalScrollView svTop;
    @BindView(R.id.rv_left)
    RecyclerView rvLeft;
    @BindView(R.id.rv_analysis)
    RecyclerView rvRight;
    @BindView(R.id.sv_right)
    MyHorizontalScrollView svRight;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.easyRefreshLayout)
    EasyRefreshLayout easyRefreshLayout;
    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;

    private ReportLeftAdapter leftAdapter;
    private ReportStoreSaleRightAdapter rightAdapter;
    private int timeType = 0;
    private TimePickerView timePickerView;

    private Presenter2<ResultReportStoreSale> presenter;

    private RequestStoreOutput requestReport;

    private StoreBean storeBean;
    private int index = 0;

    private boolean isLoading = false;

    @BindView(R.id.rg_one)
    RadioGroup rgOne;
    @BindView(R.id.rb1)
    RadioButton rb1;
    @BindView(R.id.rb2)
    RadioButton rb2;
    @BindView(R.id.rb3)
    RadioButton rb3;
    @BindView(R.id.rb4)
    RadioButton rb4;
    @BindView(R.id.rb5)
    RadioButton rb5;
    @BindView(R.id.rb6)
    RadioButton rb6;
    @BindView(R.id.rb7)
    RadioButton rb7;
    @BindView(R.id.rb8)
    RadioButton rb8;
    @BindView(R.id.rb9)
    RadioButton rb9;
    @BindView(R.id.rb10)
    RadioButton rb10;
    @BindView(R.id.rb11)
    RadioButton rb11;
    @BindView(R.id.rb12)
    RadioButton rb12;
    @BindView(R.id.rb13)
    RadioButton rb13;
    @BindView(R.id.tv_amount)
    TextView tvAmount;

    public static Intent startReportStoreSaleActivity(Context context, StoreBean bean) {
        return new Intent(context, ReportStoreSaleActivity.class).putExtra(EXTRA_DATA, bean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_store_sale);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initData();
    }

    private void initData() {
        storeBean = (StoreBean) getIntent().getSerializableExtra(EXTRA_DATA);
        requestReport = new RequestStoreOutput();
        requestReport.group_id = storeBean.group_id;
        requestReport.start_date = DateTimeUtils.getTimesMonthMorning();
        requestReport.end_date = DateTimeUtils.getNowTimeStamp();
        List<String> list = new ArrayList<>();
        list.add(storeBean.spId);
        requestReport.sp_id_list = list;
        configurePresenter();
        presenter.loadData(true);
    }

    @OnCheckedChanged({R.id.rb1, R.id.rb2, R.id.rb3, R.id.rb4, R.id.rb5, R.id.rb6, R.id.rb7, R.id.rb8, R.id.rb9, R.id.rb10, R.id.rb11, R.id.rb12, R.id.rb13})
    public void onCheckedChanged(CompoundButton view, boolean ischanged) {
        switch (view.getId()) {
            case R.id.rb1:
                if (ischanged) {
                    requestReport.out_store_type = null;
                }
                break;
            case R.id.rb2:
                if (ischanged) {
                    requestReport.out_store_type = rb2.getText().toString();
                }
                break;
            case R.id.rb3:
                if (ischanged) {
                    requestReport.out_store_type = rb3.getText().toString();
                }
                break;
            case R.id.rb4:
                if (ischanged) {
                    requestReport.out_store_type = rb4.getText().toString();
                }
                break;
            case R.id.rb5:
                if (ischanged) {
                    requestReport.out_store_type = rb5.getText().toString();
                }
                break;
            case R.id.rb6:
                if (ischanged) {
                    requestReport.out_store_type = rb6.getText().toString();
                }
                break;
            case R.id.rb7:
                if (ischanged) {
                    requestReport.out_store_type = rb7.getText().toString();
                }
                break;
            case R.id.rb8:
                if (ischanged) {
                    requestReport.out_store_type = rb8.getText().toString();
                }
                break;
            case R.id.rb9:
                if (ischanged) {
                    requestReport.out_store_type = rb9.getText().toString();
                }
                break;
            case R.id.rb10:
                if (ischanged) {
                    requestReport.out_store_type = rb10.getText().toString();
                }
                break;
            case R.id.rb11:
                if (ischanged) {
                    requestReport.out_store_type = rb11.getText().toString();
                }
                break;
            case R.id.rb12:
                if (ischanged) {
                    requestReport.out_store_type = rb12.getText().toString();
                }
                break;
            case R.id.rb13:
                if (ischanged) {
                    requestReport.out_store_type = rb13.getText().toString();
                }
                break;
            default:
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        titleBar.setTilte("门店出库表");
        rvLeft.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvRight.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        leftAdapter = new ReportLeftAdapter(mContext);
        rightAdapter = new ReportStoreSaleRightAdapter(mContext);
        rvLeft.setAdapter(leftAdapter);
        rvRight.setAdapter(rightAdapter);

        easyRefreshLayout.setLoadMoreModel(LoadModel.NONE);
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(mContext));
        easyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {

            }

            @Override
            public void onRefreshing() {
                if (!isLoading) {
                    presenter.loadData(true);
                }
            }
        });

        svTop.setScrollViewListener(new MyHorizontalScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(MyHorizontalScrollView scrollView, int x, int y, int oldx, int oldy) {
                svRight.scrollTo(x, y);
            }
        });
        svRight.setScrollViewListener(new MyHorizontalScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(MyHorizontalScrollView scrollView, int x, int y, int oldx, int oldy) {
                svTop.scrollTo(x, y);
            }
        });
        timePickerView = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (timeType == 0) {
                    tvStart.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATE_UI));
                    requestReport.start_date = date.getTime() / 1000;
                } else {
                    tvEnd.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATE_UI));
                    requestReport.end_date = date.getTime() / 1000;
                }
                if (requestReport.end_date - requestReport.start_date < 0) {
                    showTextDialog("结束时间不能小于开始时间", false);
                    return;
                }
                presenter.loadData(true);
            }
        }).build();
        tvStart.setText(DateTimeUtils.getDateTimeText(DateTimeUtils.getTimesMonthMorning(), DateTimeUtils.FORMAT_DATE_UI));
        tvEnd.setText(DateTimeUtils.getNowTime());

        scrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        break;
                    case MotionEvent.ACTION_MOVE:
                        index++;
                        break;

                    default:
                        break;
                }
                if (event.getAction() == MotionEvent.ACTION_UP && index > 0) {
                    index = 0;
                    View view = ((ScrollView) v).getChildAt(0);
                    if (view.getMeasuredHeight() <= v.getScrollY() + v.getHeight()) {
                        presenter.loadData(false);
                    }
                }
                return false;
            }
        });

    }

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultReportStoreSale>() {

            @Override
            public void onSuccess(final List<ResultReportStoreSale> result, int total, final boolean isRefresh) {
                isLoading = false;
                if (null != result) {
                    emptyLayout.setVisibility(View.GONE);
                    easyRefreshLayout.setVisibility(View.VISIBLE);
                    List<String> list = new ArrayList<>();
                    for (int i = 0, len = result.size(); i < len; i++) {
                        String s = result.get(i).getSh_name();
                        list.add(s);
                    }
                    if (isRefresh) {
                        leftAdapter.notifyDataSetChanged(list);
                        rightAdapter.notifyDataSetChanged(result);
                    } else {
                        leftAdapter.addAll(list);
                        rightAdapter.addAll(result);
                    }
                }
            }

            @Override
            public void onFailed(boolean isRefresh) {
                isLoading = false;
                dismissLoadingDialog();
                easyRefreshLayout.refreshComplete();
                easyRefreshLayout.closeLoadView();
                if (DeviceUtils.hasInternet(mContext)) {
                    showToast("加载失败，请稍候重试");
                }else{
                    emptyLayout.setVisibility(View.VISIBLE);
                    emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR_ENABLE_CLICK);
                }
            }

            @Override
            public void onCompleted(boolean isRefresh) {
                isLoading = false;
                easyRefreshLayout.refreshComplete();
                easyRefreshLayout.closeLoadView();
                dismissLoadingDialog();
            }

            @Override
            public void onEmptyData() {
                isLoading = false;
                leftAdapter.clear();
                rightAdapter.clear();
                emptyLayout.setVisibility(View.VISIBLE);
                emptyLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
                easyRefreshLayout.setVisibility(View.GONE);
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultReportStoreSale>>> subscriber, int pageIndex, int pageSize) {
                isLoading = true;
                showLoadingDialog("加载中");
                requestReport.count = pageSize;
                requestReport.start_row = pageIndex;
                AppModelFactory.model().reportStoreSale(requestReport, subscriber);
            }
        });
    }

    @OnClick(R.id.rl_start)
    public void start(View v) {
        timeType = 0;
        timePickerView.show();
    }

    @OnClick(R.id.rl_end)
    public void end(View v) {
        timeType = 1;
        timePickerView.show();
    }

    private int flag;

    private void setFlag(int value) {
        if (requestReport.sort == value) {
            flag++;
            if (flag > 2) {
                flag = 0;
            }
            if (flag == 0) {
                requestReport.sort_type = 0;
            } else if (flag == 1) {
                requestReport.sort_type = 1;
            } else {
                requestReport.sort_type = 2;
            }
        } else {
            requestReport.sort = value;

            if (flag > 2) {
                flag = 0;
            }
            flag++;
            if (flag == 0) {
                requestReport.sort_type = 0;
            } else if (flag == 1) {
                requestReport.sort_type = 1;
            } else {
                requestReport.sort_type = 2;
            }
        }
    }

    @OnClick(R.id.tv_one)
    public void one(View v) {
        setFlag(5);
        setSortStatus(ivOne, flag);
        setStatusNormal(ivTwo, ivThree, ivFour, ivFive, ivSix);
        presenter.loadData(true);
    }

    @OnClick(R.id.tv_two)
    public void two(View v) {
        setFlag(6);
        setSortStatus(ivTwo, flag);
        setStatusNormal(ivOne, ivThree, ivFour, ivFive, ivSix);
        presenter.loadData(true);
    }

    @OnClick(R.id.tv_three)
    public void three(View v) {
        setFlag(1);
        setSortStatus(ivThree, flag);
        setStatusNormal(ivOne, ivTwo, ivFour, ivFive, ivSix);
        presenter.loadData(true);
    }

    @OnClick(R.id.tv_four)
    public void four(View v) {
        setFlag(2);
        setSortStatus(ivFour, flag);
        setStatusNormal(ivOne, ivTwo, ivThree, ivFive, ivSix);
        presenter.loadData(true);
    }

    @OnClick(R.id.tv_five)
    public void five(View v) {
        setFlag(3);
        setSortStatus(ivFive, flag);
        setStatusNormal(ivOne, ivTwo, ivThree, ivFour, ivSix);
        presenter.loadData(true);
    }

    @OnClick(R.id.tv_six)
    public void six(View v) {
        setFlag(4);
        setSortStatus(ivSix, flag);
        setStatusNormal(ivOne, ivTwo, ivThree, ivFour, ivFive);
        presenter.loadData(true);
    }

    private void setSortStatus(ImageView iv, int status) {
        if (status == 0) {
            iv.setImageResource(R.drawable.order_normal);
        } else if (status == 1) {
            iv.setImageResource(R.drawable.order_ascending);
        } else {
            iv.setImageResource(R.drawable.order_descending);
        }
    }

    private void setStatusNormal(ImageView... args) {
        for (int i = 0; i < args.length; i++) {
            args[i].setImageResource(R.drawable.order_normal);
        }
    }


    @OnClick(R.id.tv_positive)
    public void positive(View v) {
        openRightLayout(v);
        presenter.loadData(true);
        if(null == requestReport.out_store_type){
            tvAmount.setText("  全部类型");
        }else{
            tvAmount.setText(requestReport.out_store_type + "");
        }
    }

    @OnClick(R.id.tv_negative)
    public void negative(View v) {
        openRightLayout(v);
        rb1.setChecked(true);
        tvAmount.setText("全部数据");
        presenter.loadData(true);
    }

    @OnClick(R.id.rl_filter)
    public void filter(View v) {
        DeviceUtils.hideSoftKeyboard(v, mContext);
        openRightLayout(v);
    }

    /**
     * 右边菜单开关事件
     */
    public void openRightLayout(View view) {
        if (drawerLayout.isDrawerOpen(main_right_drawer_layout)) {
            drawerLayout.closeDrawer(main_right_drawer_layout);
        } else {
            drawerLayout.openDrawer(main_right_drawer_layout);
        }
    }
}

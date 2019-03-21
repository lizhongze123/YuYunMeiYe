package cn.yuyun.yymy.ui.store.report;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;

import java.io.Serializable;
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
import cn.yuyun.yymy.http.request.RequestReportBusiness;
import cn.yuyun.yymy.http.result.ResultBusinessReport;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.MyHorizontalScrollView;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author
 * @desc 营业统计表 -- 全部
 * @date
 */

public class ReportBusinessStatisticsAllActivity extends BaseActivity {

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
    @BindView(R.id.rb_day)
    RadioButton rbDay;
    @BindView(R.id.rb_month)
    RadioButton rbMonth;
    @BindView(R.id.rb_year)
    RadioButton rbYear;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.tv_filter)
    TextView tvFilter;
    @BindView(R.id.rl_filter)
    LinearLayout rlFilter;
    @BindView(R.id.rl)
    LinearLayout rl;
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
    @BindView(R.id.tv_seven)
    TextView tvSeven;
    @BindView(R.id.iv_seven)
    ImageView ivSeven;
    @BindView(R.id.tv_eight)
    TextView tvEight;
    @BindView(R.id.iv_eight)
    ImageView ivEight;
    @BindView(R.id.tv_nine)
    TextView tvNine;
    @BindView(R.id.iv_nine)
    ImageView ivNine;
    @BindView(R.id.sv_top)
    MyHorizontalScrollView svTop;
    @BindView(R.id.rv_left)
    RecyclerView rvLeft;
    @BindView(R.id.rv_analysis)
    RecyclerView rvRight;;
    @BindView(R.id.sv_right)
    MyHorizontalScrollView svRight;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.easyRefreshLayout)
    EasyRefreshLayout easyRefreshLayout;
    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;
    private RequestReportBusiness requestReport;

    private int type = 1;
    private int timeType = 0;
    private int monthType = 0;
    private int yearType = 0;
    private TimePickerView dayPickerView;
    private TimePickerView monthPickerView;
    private TimePickerView yearPickerView;
    private ReportBusinessStatisticsLeftAdapter leftAdapter;
    private ReportBusinessStatisticsRightAdapter rightAdapter;

    @BindView(R.id.rv_card)
    RecyclerView rvCard;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    StringBuilder tempLevelName;
    private HqStoreAdapter hqStoreAdapter;
    private String groupId;
    private List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> storeList;
    private List<String> tempSpIdList;

    public static Intent startBusinessReportActivity(Context context, List<String> tempSpIdList, List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> storeList, String groupId) {
        return new Intent(context, ReportBusinessStatisticsAllActivity.class).putExtra(EXTRA_DATA2, (Serializable) tempSpIdList).putExtra(EXTRA_DATA, (Serializable) storeList).putExtra(EXTRA_TYPE, groupId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_business_statistics_hq);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initData();
    }

    private void initData() {
        requestReport = new RequestReportBusiness();
        requestReport.group_id = groupId;
        requestReport.start_date = DateTimeUtils.getNowTimeStamp();
        requestReport.end_date = DateTimeUtils.getNowTimeStamp();
        requestReport.sp_id_list = tempSpIdList;
        getData();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        titleBar.setTilte("营业统计表");
        groupId = getIntent().getStringExtra(EXTRA_TYPE);
        tempSpIdList = (List<String>) getIntent().getSerializableExtra(EXTRA_DATA2);
        storeList = (List<ResultClassfiyStoreBean.OgServiceplacesRspListBean>) getIntent().getSerializableExtra(EXTRA_DATA);
        tempLevelName = new StringBuilder();
        rvLeft.setLayoutManager(new LinearLayoutManager(mContext){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvRight.setLayoutManager(new LinearLayoutManager(mContext){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        leftAdapter = new ReportBusinessStatisticsLeftAdapter(mContext);
        rightAdapter = new ReportBusinessStatisticsRightAdapter(mContext);
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
                getData();
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
        dayPickerView = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (timeType == 0) {
                    tvStart.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATE_UI));
                    requestReport.start_date = date.getTime() / 1000;
                } else {
                    tvEnd.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATE_UI));
                    requestReport.end_date = date.getTime() / 1000;
                }
                if(!tvStart.getText().toString().substring(0, 7).equals(tvEnd.getText().toString().substring(0, 7))){
                    showTextDialog("时间查询跨月，请选择月表查询模式", false);
                    return;
                }

                if (requestReport.end_date - requestReport.start_date < 0) {
                    showTextDialog("结束时间不能小于开始时间", false);
                    return;
                }
                getData();
            }
        }).build();
        monthPickerView = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (monthType == 0) {
                    tvStart.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_YY_MM));
                    requestReport.start_date = date.getTime() / 1000;
                } else {
                    tvEnd.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_YY_MM));
                    requestReport.end_date = date.getTime() / 1000;
                }
                if(!tvStart.getText().toString().substring(0, 7).equals(tvEnd.getText().toString().substring(0, 7))){
                    showTextDialog("时间查询跨年，请选择年表查询模式", false);
                    return;
                }
                if (requestReport.end_date - requestReport.start_date < 0) {
                    showTextDialog("结束时间不能小于开始时间", false);
                    return;
                }
                getData();
            }
        }).setType(new boolean[]{true, true, false, false, false, false}).build();
        yearPickerView = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (yearType == 0) {
                    tvStart.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_YY));
                    requestReport.start_date = date.getTime() / 1000;
                } else {
                    tvEnd.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_YY));
                    requestReport.end_date = date.getTime() / 1000;
                }
                if (requestReport.end_date - requestReport.start_date < 0) {
                    showTextDialog("结束时间不能小于开始时间", false);
                    return;
                }
                getData();
            }
        }).setType(new boolean[]{true, false, false, false, false, false}).build();
        tvStart.setText(DateTimeUtils.getNowTime());
        tvEnd.setText(DateTimeUtils.getNowTime());
        hqStoreAdapter = new HqStoreAdapter(mContext);
        rvCard.setLayoutManager(new GridLayoutManager(this, 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvCard.setAdapter(hqStoreAdapter);
        hqStoreAdapter.addAll(storeList);
    }

    private void getData() {
        AppModelFactory.model().reportBusinessStatistics(type, requestReport, new ProgressSubscriber<DataBean<List<ResultBusinessReport>>>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean<List<ResultBusinessReport>> result) {
                if (null != result.data) {
                    rightAdapter.notifyDataSetChanged(result.data);
                    leftAdapter.notifyDataSetChanged(result.data);
                }
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(getString(R.string.NO_INTERNET));
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
            }
        });
    }

    @OnClick(R.id.tv_positive)
    public void positive(View v) {
        openRightLayout(v);
        List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> listStore = hqStoreAdapter.getSelectedItem();
        if(null != listStore && listStore.size() > 0){
            tempLevelName.delete(0, tempLevelName.length());
            List<String> list = new ArrayList<>();
            for (int i = 0; i < listStore.size(); i++) {
                list.add(listStore.get(i).sp_id);
                tempLevelName.append(listStore.get(i).sp_name + "、");
            }
            requestReport.sp_id_list = list;
            tvAmount.setText(tempLevelName.toString().substring(0,tempLevelName.length() - 1));
        }else{
            requestReport.sp_id_list = tempSpIdList;
        }
        tvAmount.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tvAmount.setSingleLine(true);
        tvAmount.setSelected(true);
        tvAmount.setFocusable(true);
        tvAmount.setFocusableInTouchMode(true);
        getData();
    }

    @OnClick(R.id.tv_negative)
    public void negative(View v) {
        openRightLayout(v);
        requestReport.sp_id_list = tempSpIdList;
        hqStoreAdapter.clearAllSelection();
        getData();
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

    @OnClick(R.id.rl_start)
    public void start(View v) {
        if (type == 3) {
            yearType = 0;
            yearPickerView.show();
        } else if (type == 2) {
            monthType = 0;
            monthPickerView.show();
        } else {
            timeType = 0;
            dayPickerView.show();
        }

    }

    @OnClick(R.id.rl_end)
    public void end(View v) {
        if (type == 3) {
            yearType = 1;
            yearPickerView.show();
        } else if (type == 2) {
            monthType = 1;
            monthPickerView.show();
        } else {
            timeType = 1;
            dayPickerView.show();
        }
    }

    @OnCheckedChanged({R.id.rb_day, R.id.rb_month,  R.id.rb_year})
    public void onCheckedChanged(CompoundButton view, boolean ischanged) {
        switch (view.getId()) {
            case R.id.rb_day:
                if (ischanged) {
                    type = 1;
                    tvStart.setText(DateTimeUtils.getDateTimeText(DateTimeUtils.getTimesMonthMorning(), DateTimeUtils.FORMAT_DATE_UI));
                    tvEnd.setText(DateTimeUtils.getNowTime());
                    requestReport.start_date = DateTimeUtils.getTimesMonthMorning();
                    requestReport.end_date = DateTimeUtils.getNowTimeStamp();
                    getData();
                }
                break;
            case R.id.rb_month:
                if (ischanged) {
                    type = 2;
                    tvStart.setText(DateTimeUtils.getDateTimeText(DateTimeUtils.getCurrYearFirst(), DateTimeUtils.FORMAT_YY_MM));
                    tvEnd.setText(DateTimeUtils.getDateTimeText(DateTimeUtils.getTimesMonthMorning(), DateTimeUtils.FORMAT_YY_MM));
                    requestReport.start_date = DateTimeUtils.getCurrYearFirst().getTime() / 1000;
                    requestReport.end_date = DateTimeUtils.getNowTimeStamp();
                    getData();
                }
                break;
            case R.id.rb_year:
                if (ischanged) {
                    type = 3;
                    tvStart.setText(DateTimeUtils.getDateTimeText(DateTimeUtils.getTimesMonthMorning(), DateTimeUtils.FORMAT_YY));
                    tvEnd.setText(DateTimeUtils.getDateTimeText(DateTimeUtils.getTimesMonthMorning(), DateTimeUtils.FORMAT_YY));
                    requestReport.start_date = DateTimeUtils.getCurrYearFirst().getTime() / 1000;
                    requestReport.end_date = DateTimeUtils.getCurrYearFirst().getTime() / 1000;
                    getData();
                }
                break;
            default:
        }
    }

}

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
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
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
import com.example.http.NoneProgressSubscriber;
import com.example.http.PageInfo;
import com.example.http.Presenter2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.DeviceUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestStaffAnalysis;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;
import cn.yuyun.yymy.http.result.ResultStaffAnalysis;
import cn.yuyun.yymy.http.result.ResultStaffAnalysisTotal;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.MyHorizontalScrollView;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author
 * @desc 员工业绩报表 -- 全部
 * @date
 */

public class StaffAnalysisAllActivity extends BaseActivity {

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
    @BindView(R.id.tv_storeName)
    TextView tvStoreName;
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
    private ReportStaffAnalysisRightAdapter rightAdapter;
    private int timeType = 0;
    private TimePickerView timePickerView;

    @BindView(R.id.rv_card)
    RecyclerView rvCard;
    StringBuilder tempLevelName;
    HqStoreAdapter hqStoreAdapter;
    private int index = 0;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    private String groupId;
    private List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> storeList;
    private List<String> tempSpIdList;
    private RequestStaffAnalysis body = new RequestStaffAnalysis();

    @BindView(R.id.tv_oneTotal)
    TextView tvOneTotal;
    @BindView(R.id.tv_twoTotal)
    TextView tvTwoTotal;
    @BindView(R.id.tv_threeTotal)
    TextView tvThreeTotal;
    @BindView(R.id.tv_fourTotal)
    TextView tvFourTotal;
    @BindView(R.id.tv_fiveTotal)
    TextView tvFiveTotal;
    @BindView(R.id.tv_sixTotal)
    TextView tvSixTotal;
    @BindView(R.id.tv_sevenTotal)
    TextView tvSevenTotal;
    @BindView(R.id.tv_eightTotal)
    TextView tvEightTotal;
    @BindView(R.id.tv_nineTotal)
    TextView tvNineTotal;
    @BindView(R.id.sv_bottom)
    MyHorizontalScrollView svBottom;

    private Presenter2<ResultStaffAnalysis> presenter;

    public static Intent startStaffAnalysisActivity(Context context, List<String> tempSpIdList, List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> storeList, String groupId) {
        return new Intent(context, StaffAnalysisAllActivity.class).putExtra(EXTRA_DATA2, (Serializable) tempSpIdList).putExtra(EXTRA_DATA, (Serializable) storeList).putExtra(EXTRA_TYPE, groupId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_analysis_detail_all);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initData();
    }

    private void initData() {
        body.group_id = groupId;
        body.start_date = DateTimeUtils.getTimesMonthMorning();
        body.end_date = DateTimeUtils.getNowTimeStamp();
        body.sp_id_list = tempSpIdList;
        body.baseon_type = "2";
        configurePresenter();
        presenter.loadData(true);
        getTotal();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        titleBar.setTilte("员工业绩表");
        groupId = getIntent().getStringExtra(EXTRA_TYPE);
        tempSpIdList = (List<String>) getIntent().getSerializableExtra(EXTRA_DATA2);
        storeList = (List<ResultClassfiyStoreBean.OgServiceplacesRspListBean>) getIntent().getSerializableExtra(EXTRA_DATA);
        tempLevelName = new StringBuilder();
        rvRight.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvLeft.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        leftAdapter = new ReportLeftAdapter(mContext);
        rightAdapter = new ReportStaffAnalysisRightAdapter(mContext);
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
                presenter.loadData(true);
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
                svBottom.scrollTo(x, y);
            }
        });
        svBottom.setScrollViewListener(new MyHorizontalScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(MyHorizontalScrollView scrollView, int x, int y, int oldx, int oldy) {
                svRight.scrollTo(x, y);
            }
        });

        timePickerView = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (timeType == 0) {
                    tvStart.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATE_UI));
                    body.start_date = date.getTime() / 1000;
                } else {
                    tvEnd.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATE_UI));
                    body.end_date = date.getTime() / 1000;
                }
                if (body.end_date - body.start_date < 0) {
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
                    case MotionEvent.ACTION_DOWN :

                        break;
                    case MotionEvent.ACTION_MOVE :
                        index++;
                        break;

                    default :
                        break;
                }
                if (event.getAction() == MotionEvent.ACTION_UP  && index > 0) {
                    index = 0;
                    View view = ((ScrollView) v).getChildAt(0);
                    if (view.getMeasuredHeight() <= v.getScrollY() + v.getHeight()) {
                        presenter.loadData(false);
                    }
                }
                return false;
            }
        });
        hqStoreAdapter = new HqStoreAdapter(mContext);
        rvCard.setLayoutManager(new GridLayoutManager(this, 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvCard.setAdapter(hqStoreAdapter);
        hqStoreAdapter.setOnItemClickListener(new HqStoreAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick() {

            }
        });
        hqStoreAdapter.addAll(storeList);
    }

    private void getTotal() {
        AppModelFactory.model().getStaffAchieveTotal(body, new NoneProgressSubscriber<DataBean<ResultStaffAnalysisTotal>>(mContext) {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(DataBean<ResultStaffAnalysisTotal> result) {
                if (null != result.data) {
                    TextViewUtils.setTextOrEmpty(tvOneTotal, StringFormatUtils.formatTwoDecimal(result.data.presale));
                    TextViewUtils.setTextOrEmpty(tvTwoTotal, StringFormatUtils.formatTwoDecimal(result.data.presale_refund));
                    TextViewUtils.setTextOrEmpty(tvThreeTotal, StringFormatUtils.formatTwoDecimal(result.data.sale));
                    TextViewUtils.setTextOrEmpty(tvFourTotal, StringFormatUtils.formatTwoDecimal(result.data.sale_refund));
                    TextViewUtils.setTextOrEmpty(tvFiveTotal, StringFormatUtils.formatTwoDecimal(result.data.consume));
                    TextViewUtils.setTextOrEmpty(tvSixTotal, StringFormatUtils.formatTwoDecimal(result.data.handmake));
                    TextViewUtils.setTextOrEmpty(tvSevenTotal, StringFormatUtils.formatTwoDecimal(result.data.person_number));
                    TextViewUtils.setTextOrEmpty(tvEightTotal, StringFormatUtils.formatTwoDecimal(result.data.person_times));
                    TextViewUtils.setTextOrEmpty(tvNineTotal, StringFormatUtils.formatTwoDecimal(result.data.consume_count));
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
                showToast(ex.message);
            }
        });
    }

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultStaffAnalysis>() {

            @Override
            public void onSuccess(final List<ResultStaffAnalysis> result, int total, final boolean isRefresh) {
                if (null != result) {
                    emptyLayout.setVisibility(View.GONE);
                    easyRefreshLayout.setVisibility(View.VISIBLE);
                    List<String> list = new ArrayList<>();
                    for (int i = 0, len = result.size(); i < len; i++) {
                        String s = result.get(i).staff_name;
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
                dismissLoadingDialog();
                easyRefreshLayout.refreshComplete();
                if (DeviceUtils.hasInternet(mContext)) {
                    showToast("加载失败，请稍候重试");
                }else{
                    emptyLayout.setVisibility(View.VISIBLE);
                    emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                }
            }

            @Override
            public void onCompleted(boolean isRefresh) {
                easyRefreshLayout.refreshComplete();
                dismissLoadingDialog();
            }

            @Override
            public void onEmptyData() {
                leftAdapter.clear();
                rightAdapter.clear();
                emptyLayout.setVisibility(View.VISIBLE);
                emptyLayout.setErrorType(EmptyLayout.NODATA);
                easyRefreshLayout.setVisibility(View.GONE);
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultStaffAnalysis>>> subscriber, int pageIndex, int pageSize) {
                showLoadingDialog("加载中");
                AppModelFactory.model().getStaffAchieve(pageIndex, pageSize, body, subscriber);
            }
        });
    }

    @OnClick(R.id.tv_positive)
    public void positive(View v) {
        openRightLayout(v);
        List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> listStore = hqStoreAdapter.getSelectedItem();
        body.baseon_type = "2";
        tempLevelName.delete(0, tempLevelName.length());
        List<String> list = new ArrayList<>();
        for (int i = 0; i < listStore.size(); i++) {
            list.add(listStore.get(i).sp_id);
            tempLevelName.append(listStore.get(i).sp_name + "、");
        }
        body.sp_id_list = list;
        tvAmount.setText(tempLevelName.toString().substring(0,tempLevelName.length() - 1));
        presenter.loadData(true);
        tvAmount.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tvAmount.setSingleLine(true);
        tvAmount.setSelected(true);
        tvAmount.setFocusable(true);
        tvAmount.setFocusableInTouchMode(true);
    }

    @OnClick(R.id.tv_negative)
    public void negative(View v) {
        openRightLayout(v);
        body.sp_id_list = tempSpIdList;
        tempLevelName.delete(0, tempLevelName.length());
        tvAmount.setText("全部数据");
        body.baseon_type = "2";
        hqStoreAdapter.clearAllSelection();
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

    private int flag;

    private void setFlag(int value) {
        if (body.sort == value) {
            flag++;
            if (flag > 2) {
                flag = 0;
            }
            if (flag == 0) {
                body.sort_type = 0;
            } else if (flag == 1) {
                body.sort_type = 1;
            } else {
                body.sort_type = 2;
            }
        } else {
            body.sort = value;

            if (flag > 2) {
                flag = 0;
            }
            flag++;
            if (flag == 0) {
                body.sort_type = 0;
            } else if (flag == 1) {
                body.sort_type = 1;
            } else {
                body.sort_type = 2;
            }
        }
    }

    public void one(View v) {
        setFlag(1);
        setSortStatus(ivOne, flag);
        setStatusNormal(ivTwo, ivThree, ivFour, ivFive, ivSix, ivSeven, ivEight);
        presenter.loadData(true);
    }

    public void two(View v) {
        setFlag(2);
        setSortStatus(ivTwo, flag);
        setStatusNormal(ivOne, ivThree, ivFour, ivFive, ivSix, ivSeven, ivEight);
        presenter.loadData(true);
    }

    public void three(View v) {
        setFlag(3);
        setSortStatus(ivThree, flag);
        setStatusNormal(ivOne, ivTwo, ivFour, ivFive, ivSix, ivSeven, ivEight);
        presenter.loadData(true);
    }

    public void four(View v) {
        setFlag(4);
        setSortStatus(ivFour, flag);
        setStatusNormal(ivOne, ivTwo, ivThree, ivFive, ivSix, ivSeven, ivEight);
        presenter.loadData(true);
    }

    public void five(View v) {
        setFlag(5);
        setSortStatus(ivFive, flag);
        setStatusNormal(ivOne, ivTwo, ivThree, ivFour, ivSix, ivSeven, ivEight);
        presenter.loadData(true);
    }

    public void six(View v) {
        setFlag(6);
        setSortStatus(ivSix, flag);
        setStatusNormal(ivOne, ivTwo, ivThree, ivFour, ivFive, ivSeven, ivEight);
        presenter.loadData(true);
    }

    public void seven(View v) {
        setFlag(8);
        setSortStatus(ivSeven, flag);
        setStatusNormal(ivOne, ivTwo, ivThree, ivFour, ivFive, ivSix, ivEight);
        presenter.loadData(true);
    }

    public void eight(View v) {
        setFlag(9);
        setSortStatus(ivEight, flag);
        setStatusNormal(ivOne, ivTwo, ivThree, ivFour, ivFive, ivSix, ivSeven);
        presenter.loadData(true);
    }

}

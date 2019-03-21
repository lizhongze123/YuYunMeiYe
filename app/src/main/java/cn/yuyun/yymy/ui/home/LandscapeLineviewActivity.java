package cn.yuyun.yymy.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.NoneProgressSubscriber;
import com.example.http.ProgressSubscriber;
import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.example.dialog.LoadingDialog;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.LogUtils;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.ToastUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.app.MyApplication;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestClassfiyStore;
import cn.yuyun.yymy.http.request.RequestLineData;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean.OgServiceplacesRspListBean;
import cn.yuyun.yymy.http.result.ResultLineData;
import cn.yuyun.yymy.listener.CustomClickListener;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.StorePrefs;
import cn.yuyun.yymy.utils.ScreenRotateUtils;
import cn.yuyun.yymy.view.dialog.BottomDialog;
import cn.yuyun.yymy.view.dialog.StoreTopDialog;
import cn.yuyun.yymy.view.lineview.BusinessLineView;
import cn.yuyun.yymy.view.lineview.BusinessLineViewLandscape;
import cn.yuyun.yymy.view.lineview.FundMode;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc
 * @date
 */
public class LandscapeLineviewActivity extends AppCompatActivity {

    @BindView(R.id.businessLineView)
    BusinessLineViewLandscape businessLineView;
    @BindView(R.id.businessLineView2)
    BusinessLineViewLandscape businessLineView2;
    @BindView(R.id.rb_one)
    RadioButton rbOne;
    @BindView(R.id.rb_two)
    RadioButton rbTwo;
    @BindView(R.id.rl_one)
    RelativeLayout rlOne;
    @BindView(R.id.rl_two)
    RelativeLayout rlTwo;
    @BindView(R.id.iv_one)
    ImageView ivOne;
    @BindView(R.id.iv_two)
    ImageView ivTwo;
    @BindView(R.id.radioGroupTitle)
    RadioGroup radioGroupTitle;
    @BindView(R.id.tv_last)
    TextView tvLast;
    @BindView(R.id.ll_last)
    LinearLayout llLast;
    @BindView(R.id.ll_next)
    LinearLayout llNext;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.iv_next)
    ImageView ivNext;
    @BindView(R.id.iv_last)
    ImageView ivLast;
    @BindView(R.id.tv_month)
    TextView tvMonth;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.iv_expand)
    ImageView ivExpand;
    @BindView(R.id.iv_reduce)
    ImageView ivReduce;
    Calendar calMonth = Calendar.getInstance();
    Calendar calWeek = Calendar.getInstance();
    /**
     * 0 月  1周
     */
    private int type;
    RequestLineData requestLineData = new RequestLineData();
    List<String> spIdList = new ArrayList<>();
    OrientationEventListener mOrientationListener;

    private StoreTopDialog dialog;
    private int currentMonth;
    private int currentYear;
    private int currentWeek;

    private LoadingDialog loadingDialog;

    public static Intent startLandscapeLineviewActivity(Context context) {
        return new Intent(context, LandscapeLineviewActivity.class);
    }

    @Override
    public void onDestroy() {
        if(null != loadingDialog && loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
        mOrientationListener.disable();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landscapelineview);
        ButterKnife.bind(this);
        mOrientationListener = new OrientationEventListener(this,
                SensorManager.SENSOR_DELAY_NORMAL) {

            @Override
            public void onOrientationChanged(int orientation) {
                if(!ScreenRotateUtils.isOpenGravity(LandscapeLineviewActivity.this)){
                    return;
                }
                if(orientation == OrientationEventListener.ORIENTATION_UNKNOWN){
                    //平放监测不到有效的角度
                    return;
                }
                if(orientation > 350 || orientation < 10){
                    orientation = 0;
                    finish();
                }else if(orientation > 80 && orientation < 100){
                    orientation = 90;
                }else if(orientation > 170 && orientation < 190){
                    orientation = 180;
                }else if(orientation > 260 && orientation < 280){
                    orientation = 270;
                }else{
                    return;
                }

            }
        };
        if (mOrientationListener.canDetectOrientation()) {
            mOrientationListener.enable();
        } else {
            mOrientationListener.disable();
        }
        initView();
        initData();
    }

    private void initData() {
        getClassfiyStoreList();
    }

    private void initView() {
        loadingDialog = new LoadingDialog(LandscapeLineviewActivity.this);
        loadingDialog.setCancelable(false);
        spIdList = (List<String>) getIntent().getSerializableExtra(EXTRA_DATA);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        requestLineData.start_date = DateTimeUtils.getTimesMonthMorning();
        requestLineData.end_date = DateTimeUtils.getTimesMonthNight();
        requestLineData.sp_id_list = spIdList;
        requestLineData.group_id = LoginInfoPrefs.getInstance(this).getGroupId();
        tvMonth.setText((calMonth.get(Calendar.MONTH) + 1) + "月份");
        currentMonth = calMonth.get(Calendar.MONTH) + 1;
        currentYear = calMonth.get(Calendar.YEAR);
        currentWeek = calMonth.get(Calendar.WEEK_OF_YEAR);
        if(currentMonth == calMonth.get(Calendar.MONTH) + 1){
            tvNext.setEnabled(false);
            llNext.setEnabled(false);
            ivNext.setImageDrawable(ResoureUtils.getDrawable(LandscapeLineviewActivity.this, R.drawable.next_enable));
            tvNext.setTextColor(Color.parseColor("#cccccc"));
        }else{
            tvNext.setEnabled(true);
            llNext.setEnabled(true);
            ivNext.setImageDrawable(ResoureUtils.getDrawable(LandscapeLineviewActivity.this, R.drawable.next_able));
            tvNext.setTextColor(Color.parseColor("#3f3f3f"));
        }
        rlOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isMonth){
                    return;
                }
                isMonth = true;
                ivOne.setImageDrawable(ResoureUtils.getDrawable(LandscapeLineviewActivity.this, R.drawable.ic_checked));
                ivTwo.setImageDrawable(ResoureUtils.getDrawable(LandscapeLineviewActivity.this, R.drawable.ic_unchecked));
                type = 0;
                tvLast.setText("上月 ");
                tvNext.setText("下月");
                //每月1号
                calMonth.set(Calendar.DAY_OF_MONTH, 1);
                calMonth.set(Calendar.HOUR_OF_DAY, 0);
                calMonth.set(Calendar.MINUTE, 0);
                calMonth.set(Calendar.SECOND, 0);
                //上x个月
                calMonth.add(Calendar.MONTH, 0);
                requestLineData.start_date = calMonth.getTimeInMillis() / 1000;
                //获取某月最大天数
                int lastDay = calMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
                calMonth.set(Calendar.DAY_OF_MONTH, lastDay);
                requestLineData.end_date = calMonth.getTimeInMillis() / 1000;
                tvMonth.setVisibility(View.VISIBLE);
                tvMonth.setText((calMonth.get(Calendar.MONTH) + 1) + "月份");
                if(currentMonth == calMonth.get(Calendar.MONTH) + 1){
                    tvNext.setEnabled(false);
                    llNext.setEnabled(false);
                    ivNext.setImageDrawable(ResoureUtils.getDrawable(LandscapeLineviewActivity.this, R.drawable.next_enable));
                    tvNext.setTextColor(Color.parseColor("#cccccc"));
                }else{
                    tvNext.setEnabled(true);
                    llNext.setEnabled(true);
                    ivNext.setImageDrawable(ResoureUtils.getDrawable(LandscapeLineviewActivity.this, R.drawable.next_able));
                    tvNext.setTextColor(Color.parseColor("#3f3f3f"));
                }
                getLineData(requestLineData);
            }
        });
        rlTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isMonth){
                    return;
                }
                isMonth = false;
                ivTwo.setImageDrawable(ResoureUtils.getDrawable(LandscapeLineviewActivity.this, R.drawable.ic_checked));
                ivOne.setImageDrawable(ResoureUtils.getDrawable(LandscapeLineviewActivity.this, R.drawable.ic_unchecked));
                type = 1;
                tvLast.setText("上周");
                tvNext.setText("下周");
                int day_of_week = calWeek.get(Calendar.DAY_OF_WEEK) - 1;
                calWeek.set(Calendar.HOUR_OF_DAY, 0);
                calWeek.set(Calendar.MINUTE, 0);
                calWeek.set(Calendar.SECOND, 0);
                if (day_of_week == 0) {
                    day_of_week = 7;
                }
                calWeek.add(Calendar.DATE, 7 - day_of_week);
                requestLineData.end_date = calWeek.getTimeInMillis() / 1000;
                calWeek.add(Calendar.DATE, -6);
                requestLineData.start_date = calWeek.getTimeInMillis() / 1000;
                tvMonth.setVisibility(View.GONE);
                if(currentWeek == calWeek.get(Calendar.WEEK_OF_YEAR) && currentYear == calWeek.get(Calendar.YEAR)){
                    tvNext.setEnabled(false);
                    llNext.setEnabled(false);
                    ivNext.setImageDrawable(ResoureUtils.getDrawable(LandscapeLineviewActivity.this, R.drawable.next_enable));
                    tvNext.setTextColor(Color.parseColor("#cccccc"));
                }else{
                    tvNext.setEnabled(true);
                    llNext.setEnabled(true);
                    ivNext.setImageDrawable(ResoureUtils.getDrawable(LandscapeLineviewActivity.this, R.drawable.next_able));
                    tvNext.setTextColor(Color.parseColor("#3f3f3f"));
                }
                getLineData(requestLineData);
            }
        });
        radioGroupTitle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_one) {
                    businessLineView.setVisibility(View.VISIBLE);
                    businessLineView2.setVisibility(View.GONE);
                } else if (checkedId == R.id.rb_two) {
                    businessLineView.setVisibility(View.GONE);
                    businessLineView2.setVisibility(View.VISIBLE);
                }

            }
        });

        llLast.setOnClickListener(new CustomClickListener() {
            @Override
            protected void onSingleClick() {
                if (type == 0) {
                    //每月1号
                    calMonth.set(Calendar.DAY_OF_MONTH, 1);
                    calMonth.set(Calendar.HOUR_OF_DAY, 0);
                    calMonth.set(Calendar.MINUTE, 0);
                    calMonth.set(Calendar.SECOND, 0);
                    //上x个月
                    calMonth.add(Calendar.MONTH, -1);
                    requestLineData.start_date = calMonth.getTimeInMillis() / 1000;
                    //获取某月最大天数
                    int lastDay = calMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
                    calMonth.set(Calendar.DAY_OF_MONTH, lastDay);
                    requestLineData.end_date = calMonth.getTimeInMillis() / 1000;
                    tvMonth.setText((calMonth.get(Calendar.MONTH) + 1) + "月份");
                    if(currentMonth == calMonth.get(Calendar.MONTH) + 1 && currentYear == calMonth.get(Calendar.YEAR)){
                        tvNext.setEnabled(false);
                        llNext.setEnabled(false);
                        ivNext.setImageDrawable(ResoureUtils.getDrawable(LandscapeLineviewActivity.this, R.drawable.next_enable));
                        tvNext.setTextColor(Color.parseColor("#cccccc"));
                    }else{
                        tvNext.setEnabled(true);
                        llNext.setEnabled(true);
                        ivNext.setImageDrawable(ResoureUtils.getDrawable(LandscapeLineviewActivity.this, R.drawable.next_able));
                        tvNext.setTextColor(Color.parseColor("#3f3f3f"));
                    }
                } else {
                    int day_of_week = calWeek.get(Calendar.DAY_OF_WEEK) - 1;
                    if (day_of_week == 0) {
                        day_of_week = 7;
                    }
                    calWeek.add(Calendar.DATE, -1);
                    requestLineData.end_date = calWeek.getTimeInMillis() / 1000;
                    calWeek.add(Calendar.DATE, -6);
                    requestLineData.start_date = calWeek.getTimeInMillis() / 1000;
                    if(currentWeek == calWeek.get(Calendar.WEEK_OF_YEAR) && currentYear == calWeek.get(Calendar.YEAR)){
                        tvNext.setEnabled(false);
                        llNext.setEnabled(false);
                        tvNext.setTextColor(Color.parseColor("#cccccc"));
                        ivNext.setImageDrawable(ResoureUtils.getDrawable(LandscapeLineviewActivity.this, R.drawable.next_enable));
                    }else{
                        tvNext.setEnabled(true);
                        llNext.setEnabled(true);
                        tvNext.setTextColor(Color.parseColor("#3f3f3f"));
                        ivNext.setImageDrawable(ResoureUtils.getDrawable(LandscapeLineviewActivity.this, R.drawable.next_able));
                    }
                }
                getLineData(requestLineData);
            }

            @Override
            protected void onFastClick() {

            }
        });

        llNext.setOnClickListener(new CustomClickListener() {
            @Override
            protected void onSingleClick() {
                if (type == 0) {
                    //每月1号
                    calMonth.set(Calendar.DAY_OF_MONTH, 1);
                    calMonth.set(Calendar.HOUR_OF_DAY, 0);
                    calMonth.set(Calendar.MINUTE, 0);
                    calMonth.set(Calendar.SECOND, 0);
                    //下x个月
                    calMonth.add(Calendar.MONTH, 1);
                    requestLineData.start_date = calMonth.getTimeInMillis() / 1000;
                    int lastDay = calMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
                    calMonth.set(Calendar.DAY_OF_MONTH, lastDay);
                    requestLineData.end_date = calMonth.getTimeInMillis() / 1000;
                    tvMonth.setText((calMonth.get(Calendar.MONTH) + 1) + "月份");
                    if(currentMonth == calMonth.get(Calendar.MONTH) + 1 && currentYear == calMonth.get(Calendar.YEAR)){
                        tvNext.setEnabled(false);
                        llNext.setEnabled(false);
                        ivNext.setImageDrawable(ResoureUtils.getDrawable(LandscapeLineviewActivity.this, R.drawable.next_enable));
                        tvNext.setTextColor(Color.parseColor("#cccccc"));
                    }else{
                        tvNext.setEnabled(true);
                        llNext.setEnabled(true);
                        ivNext.setImageDrawable(ResoureUtils.getDrawable(LandscapeLineviewActivity.this, R.drawable.next_able));
                        tvNext.setTextColor(Color.parseColor("#3f3f3f"));
                    }
                } else {
                    int day_of_week = calWeek.get(Calendar.DAY_OF_WEEK) - 1;
                    if (day_of_week == 0) {
                        day_of_week = 7;
                    }
                    calWeek.add(Calendar.DATE, 13);
                    requestLineData.end_date = calWeek.getTimeInMillis() / 1000;
                    calWeek.add(Calendar.DATE, -6);
                    requestLineData.start_date = calWeek.getTimeInMillis() / 1000;
                    if(currentWeek == calWeek.get(Calendar.WEEK_OF_YEAR) && currentYear == calWeek.get(Calendar.YEAR)){
                        tvNext.setEnabled(false);
                        llNext.setEnabled(false);
                        tvNext.setTextColor(Color.parseColor("#cccccc"));
                        ivNext.setImageDrawable(ResoureUtils.getDrawable(LandscapeLineviewActivity.this, R.drawable.next_enable));
                    }else{
                        tvNext.setEnabled(true);
                        llNext.setEnabled(true);
                        tvNext.setTextColor(Color.parseColor("#3f3f3f"));
                        ivNext.setImageDrawable(ResoureUtils.getDrawable(LandscapeLineviewActivity.this, R.drawable.next_able));
                    }
                }

                getLineData(requestLineData);
            }

            @Override
            protected void onFastClick() {

            }
        });

        rlTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != dialog) {
                    dialog.show();
                }
            }
        });
        ivReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getClassfiyStoreList() {
        final RequestClassfiyStore requestClassfiyStore = new RequestClassfiyStore();
        AppModelFactory.model().getMyStoreList(LoginInfoPrefs.getInstance(this).getGroupId(), requestClassfiyStore, new NoneProgressSubscriber<DataBean<List<ResultClassfiyStoreBean>>>(this) {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(DataBean<List<ResultClassfiyStoreBean>> result) {
                if (null != result) {
                    if(null != result.data){

                        final List<String>spIdList = new ArrayList<>();
                        final List<OgServiceplacesRspListBean> storeList = new ArrayList<>();

                        //把所有门店取出来
                        for (int i = 0,iLength = result.data.size(); i < iLength; i++) {
                            for (int j = 0, jLength = result.data.get(i).ogServiceplacesRspList.size();j < jLength; j++) {
                                spIdList.add(result.data.get(i).ogServiceplacesRspList.get(j).sp_id);
                                storeList.add(result.data.get(i).ogServiceplacesRspList.get(j));
                            }
                        }
                        //第一个为"全部"
                        OgServiceplacesRspListBean bean = new OgServiceplacesRspListBean();
                        bean.sp_name = "全部";
                        storeList.add(0, bean);

                        requestLineData.start_date = DateTimeUtils.getTimesMonthMorning();
                        requestLineData.end_date = DateTimeUtils.getTimesMonthNight();
                        requestLineData.sp_id_list = spIdList;
                        requestLineData.group_id = LoginInfoPrefs.getInstance(LandscapeLineviewActivity.this).getGroupId();

                        if(null == requestLineData.sp_id_list || requestLineData.sp_id_list.size() == 0){
//                            llNoStore.setVisibility(View.VISIBLE);
//                            llBusiness.setVisibility(View.GONE);
                        }else{
//                            llNoStore.setVisibility(View.GONE);
//                            llBusiness.setVisibility(View.VISIBLE);
                            getLineData(requestLineData);
                        }

                        dialog = new StoreTopDialog(LandscapeLineviewActivity.this, storeList);
                        dialog.setClickListener(new StoreTopDialog.OnClickListener() {
                            @Override
                            public void onClick(OgServiceplacesRspListBean bean, int position) {
                                if(position == 0){
                                    requestLineData.sp_id_list = spIdList;
                                }else{
                                    List<String>list = new ArrayList<>();
                                    list.add(bean.sp_id);
                                    requestLineData.sp_id_list = list;
                                }
                                tvType.setText(bean.sp_name);
                                dialog.dismiss();
                                getLineData(requestLineData);
                            }

                            @Override
                            public void onDismiss() {

                            }

                        });

                    }
                }
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), "网络异常，请检查网络");
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), ex.message);
            }

        });
    }

    boolean isMonth = true;

    private void getLineData(RequestLineData body) {
        loadingDialog.show();
        AppModelFactory.model().getLineData(body, new NoneProgressSubscriber<DataBean<List<ResultLineData>>>(this) {
            @Override
            public void onNext(DataBean<List<ResultLineData>> result) {
                if (null != result.data) {
                    List<FundMode> fundModeList = new ArrayList<>();
                    List<FundMode> fundModeList2 = new ArrayList<>();
                    for (int i = 0; i < result.data.size(); i++) {
                        FundMode fundMode;
                        FundMode fundMode2;
                        if (null != result.data.get(i).analysisSpTurnoverSingleRsp) {
                            fundMode = new FundMode(result.data.get(i).date, DateTimeUtils.dateToWeek(result.data.get(i).date), StringFormatUtils.formatDecimal(result.data.get(i).analysisSpTurnoverSingleRsp.total_amount));
                            fundMode2 = new FundMode(result.data.get(i).date, DateTimeUtils.dateToWeek(result.data.get(i).date), StringFormatUtils.formatDecimal(result.data.get(i).analysisSpTurnoverSingleRsp.amount_consume));
                        } else {
                            fundMode = new FundMode(result.data.get(i).date, DateTimeUtils.dateToWeek(result.data.get(i).date), "0.00");
                            fundMode2 = new FundMode(result.data.get(i).date, DateTimeUtils.dateToWeek(result.data.get(i).date), "0.00");
                        }
                        fundModeList.add(fundMode);
                        fundModeList2.add(fundMode2);
                    }
                    if (isMonth) {
                        businessLineView.setDataList(fundModeList, BusinessLineView.TYPE_MOONTH);
                        businessLineView2.setDataList(fundModeList2, BusinessLineView.TYPE_MOONTH);
                    } else {
                        businessLineView.setDataList(fundModeList, BusinessLineView.TYPE_WEEK);
                        businessLineView2.setDataList(fundModeList2, BusinessLineView.TYPE_WEEK);
                    }
                }
            }

            @Override
            public void onCompleted() {
                loadingDialog.dismiss();
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                LogUtils.e(ex.getMessage());
            }
        });
    }

}

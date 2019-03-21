package cn.yuyun.yymy.ui.store;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.NoneProgressSubscriber;
import com.githang.statusbar.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestSpDetail;
import cn.yuyun.yymy.http.result.ResultSpDetail;
import cn.yuyun.yymy.ui.home.analysis.AnalysisOneActivity;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.store.attendance.AttendanceStatisticsActivity;
import cn.yuyun.yymy.ui.store.bill.StoreBillActivity;
import cn.yuyun.yymy.ui.store.book.AppointmentBookActivity;
import cn.yuyun.yymy.ui.store.member.StoreMemberListActivity;
import cn.yuyun.yymy.ui.store.report.FinanceReportActivity;
import cn.yuyun.yymy.ui.store.report.MemberAnalysisActivity;
import cn.yuyun.yymy.ui.store.report.ReportBrandContrastActivity;
import cn.yuyun.yymy.ui.store.report.ReportBrandContrastOneActivity;
import cn.yuyun.yymy.ui.store.report.ReportBusinessDetailActivity;
import cn.yuyun.yymy.ui.store.report.ReportBusinessStatisticsActivity;
import cn.yuyun.yymy.ui.store.report.ReportConsumeDetailActivity;
import cn.yuyun.yymy.ui.store.report.ReportHqOutputActivity;
import cn.yuyun.yymy.ui.store.report.ReportLiabilitiesActivity;
import cn.yuyun.yymy.ui.store.report.ReportStoreSaleActivity;
import cn.yuyun.yymy.ui.store.report.StaffAnalysisActivity;
import cn.yuyun.yymy.ui.store.report.StockActivity;
import cn.yuyun.yymy.ui.store.staff.StoreStaffListActivity;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.utils.MathUtils;
import cn.yuyun.yymy.view.lineview.HistogramMode;
import cn.yuyun.yymy.view.lineview.HistogramView;
import de.hdodenhof.circleimageview.CircleImageView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc
 * @date
 */

public class StoreDetailActivity extends BaseNoTitleActivity {

    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_charge)
    TextView tvCharge;
    @BindView(R.id.tv_mobile)
    TextView tvMobile;
    @BindView(R.id.histogramView)
    HistogramView histogramView;
    @BindView(R.id.tv_arrearsDesc)
    TextView tvArrearsDesc;
    @BindView(R.id.tv_peopleNumDesc)
    TextView tvPeopleNumDesc;
    @BindView(R.id.tv_peopleNum)
    TextView tvPeopleNum;
    @BindView(R.id.tv_peopleTime)
    TextView tvPeopleTime;
    @BindView(R.id.tv_newMember)
    TextView tvNewMember;
    @BindView(R.id.ll_personTime)
    LinearLayout llPersonTime;
    @BindView(R.id.ll_newMember)
    LinearLayout llNewMember;
    @BindView(R.id.ll_personNum)
    LinearLayout llPersonNum;
    @BindView(R.id.tv_storedValue)
    TextView tvStoredValue;
    @BindView(R.id.tv_arrears)
    TextView tvArrears;
    @BindView(R.id.tv_canbeUsed)
    TextView tvCanbeUsed;
    @BindView(R.id.tv_serviceNum)
    TextView tvServiceNum;
    private List<String> spIdList = new ArrayList<>();
    private StoreBean storeBean;
    private RequestSpDetail body;
    ResultSpDetail tempResultSpDetail;

    boolean storedValueWan = false;
    boolean arrearsWan = false;
    boolean canbeUsedWan = false;

    public static Intent startStoreDetailActivity(Context context, StoreBean bean) {
        return new Intent(context, StoreDetailActivity.class).putExtra(EXTRA_DATA, bean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.title_mine));
        setContentView(R.layout.activity_store_detail);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    private void initDatas() {
        body = new RequestSpDetail();
        body.start_date = DateTimeUtils.getTimesMonthMorning();
        body.end_date = DateTimeUtils.getNowTimeStamp();
        spIdList.add(storeBean.spId);
        body.sp_id_list = spIdList;
        body.group_id = storeBean.group_id;
        getSpDetails(body);
    }

    private void initViews() {
        titleBar.setTilte("门店详情");
        storeBean = (StoreBean) getIntent().getSerializableExtra(EXTRA_DATA);
        TextViewUtils.setTextOrEmpty(tvCharge, "负责人:" + storeBean.chairman, "无");
        TextViewUtils.setTextOrEmpty(tvMobile, storeBean.addr, "未填写门店地址");
        TextViewUtils.setTextOrEmpty(tvName, storeBean.spName);
        if (!TextUtils.isEmpty(storeBean.thumb_url)) {
            if (storeBean.thumb_url.startsWith(getString(R.string.HTTP))) {
                GlideHelper.displayImage(mContext, storeBean.thumb_url, ivAvatar, R.drawable.default_store);
            } else {
                GlideHelper.displayImage(mContext, getString(R.string.image_url_prefix) + storeBean.thumb_url, ivAvatar, R.drawable.default_store);
            }
        }
        tvStoredValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != tempResultSpDetail){
                    if(storedValueWan){
                        storedValueWan = false;
                        TextViewUtils.setTextOrEmpty(tvStoredValue, StringFormatUtils.formatDecimal(tempResultSpDetail.storedvalue));
                    }else{
                        if(tempResultSpDetail.storedvalue >= 10000){
                            storedValueWan = true;
                            tvStoredValue.setText(MathUtils.formatNum(tvStoredValue.getText().toString(), false));
                        }
                    }
                }

            }
        });
        tvArrears.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != tempResultSpDetail){
                    if(arrearsWan){
                        arrearsWan = false;
                        TextViewUtils.setTextOrEmpty(tvArrears, StringFormatUtils.formatDecimal(tempResultSpDetail.arrears));
                    }else{
                        if(tempResultSpDetail.arrears >= 10000){
                            arrearsWan = true;
                            tvArrears.setText(MathUtils.formatNum(tvArrears.getText().toString(), false));
                        }
                    }
                }

            }
        });
        tvCanbeUsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != tempResultSpDetail){
                    if(canbeUsedWan){
                        canbeUsedWan = false;
                        TextViewUtils.setTextOrEmpty(tvCanbeUsed, StringFormatUtils.formatDecimal(tempResultSpDetail.canbe_consume));
                    }else{
                        if(tempResultSpDetail.canbe_consume >= 10000){
                            canbeUsedWan = true;
                            tvCanbeUsed.setText(MathUtils.formatNum(tvCanbeUsed.getText().toString(), false));
                        }
                    }
                }

            }
        });
    }

    @OnClick({R.id.ll_storedValue, R.id.ll_canbeUsed,R.id.ll_arrears, R.id.iv_back, R.id.ll_liabilities, R.id.ll_storeSale, R.id.ll_staff, R.id.ll_member, R.id.ll_staffAnalysis, R.id.ll_memberAnalysis, R.id.ll_finance, R.id.ll_business, R.id.ll_analysis, R.id.ll_stock, R.id.ll_book, R.id.ll_attendance, R.id.ll_bill, R.id.ll_consumeDetail, R.id.ll_businessDetail, R.id.ll_brandContrast})
    public void onClickItem(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_analysis:
                //品项分析表
                startActivity(AnalysisOneActivity.startAnalysisOneActivity(mContext, storeBean));
                break;
            case R.id.ll_finance:
                //财务分析表
                startActivity(FinanceReportActivity.startFinanceReportActivity(mContext, storeBean));
                break;
            case R.id.ll_business:
                //营业统计表
                startActivity(ReportBusinessStatisticsActivity.startBusinessReportActivity(mContext, storeBean));
                break;
            case R.id.ll_staffAnalysis:
                //员工业绩表
                startActivity(StaffAnalysisActivity.startStaffAnalysisActivity(mContext, storeBean));
                break;
            case R.id.ll_memberAnalysis:
                //会员业绩表
                startActivity(MemberAnalysisActivity.startMemberAnalysisActivity(mContext, storeBean));
                break;
            case R.id.ll_businessDetail:
                //营业明细表
                startActivity(ReportBusinessDetailActivity.startReportBusinessDetailActivity(mContext, storeBean));
                break;
            case R.id.ll_consumeDetail:
                //消耗明细表
                startActivity(ReportConsumeDetailActivity.startReportConsumeDetailActivity(mContext, storeBean));
                break;
            case R.id.ll_brandContrast:
                //品项占比表
                startActivity(ReportBrandContrastOneActivity.startReportBrandContrastOneActivity(mContext, storeBean));
                break;
            case R.id.ll_storeSale:
                //门店出库表
                startActivity(ReportStoreSaleActivity.startReportStoreSaleActivity(mContext, storeBean));
                break;
            case R.id.ll_liabilities:
                //门店负债表
                startActivity(ReportLiabilitiesActivity.startReportLiabilitiesActivity(mContext, spIdList, storeBean.group_id));
                break;
            case R.id.ll_stock:
                //库存管理
                startActivity(StockActivity.startStockActivity(mContext, storeBean));
                break;
           case R.id.ll_storedValue:
                //门店分析
                startActivity(StoreAnalysisActivity.startStoreAnalysisActivityForOne(mContext, spIdList, storeBean.group_id));
                break;
            case R.id.ll_canbeUsed:
                //门店分析
                startActivity(StoreAnalysisActivity.startStoreAnalysisActivityForThree(mContext, spIdList, storeBean.group_id));
                break;
            case R.id.ll_arrears:
                //门店分析
                startActivity(StoreAnalysisActivity.startStoreAnalysisActivityForTwo(mContext, spIdList, storeBean.group_id));
                break;
            case R.id.ll_staff:
                //本店员工
                startActivity(StoreStaffListActivity.startStaffAnalysisActivity(mContext, storeBean));
                break;
            case R.id.ll_member:
                //本店会员
                startActivity(StoreMemberListActivity.startMemberAnalysisActivity(mContext, storeBean));
                break;
            case R.id.ll_book:
                //预约本
                startActivity(AppointmentBookActivity.startAppointmentBookActivity(mContext, storeBean));
                break;
            case R.id.ll_attendance:
                //考勤汇总
                startActivity(AttendanceStatisticsActivity.startAttendanceStatisticsActivity(mContext, storeBean));
                break;
            case R.id.ll_bill:
                //明细单据
                startActivity(StoreBillActivity.startStoreBillActivity(mContext, storeBean));
                break;
            default:
        }
    }

    private void getSpDetails(RequestSpDetail requestSpDetail) {
        AppModelFactory.model().getSpDetails(requestSpDetail, new NoneProgressSubscriber<DataBean<ResultSpDetail>>(mContext) {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(DataBean<ResultSpDetail> result) {
                if (null != result.data) {
                    tempResultSpDetail = result.data;
                    HistogramMode histogramMode1 = new HistogramMode();
                    HistogramMode histogramMode2 = new HistogramMode();
                    HistogramMode histogramMode3 = new HistogramMode();
                    histogramMode1.setColor(ResoureUtils.getColor(mContext, R.color.histogram_one));
                    histogramMode2.setColor(ResoureUtils.getColor(mContext, R.color.histogram_two));
                    histogramMode2.setColor(ResoureUtils.getColor(mContext, R.color.histogram_three));
                    double percnetTotal = result.data.totalAmount + result.data.amountConsume + result.data.storePerformance;
                    if (percnetTotal == 0) {
                        histogramMode1.setValue(0);
                        histogramMode2.setValue(0);
                        histogramMode3.setValue(0);
                    } else {
                        histogramMode1.setValue(result.data.totalAmount);
                        histogramMode2.setValue(result.data.amountConsume);
                        histogramMode3.setValue(result.data.storePerformance);
                    }
                    List<HistogramMode> data = new ArrayList<>();
                    data.add(histogramMode1);
                    data.add(histogramMode2);
                    data.add(histogramMode3);
                    histogramView.setDataList(data);
                    histogramView.setOnChartClickListener(new HistogramView.OnChartClickListener() {
                        @Override
                        public void onClick(HistogramMode bean, int pos) {
                            if (pos == 0) {
                                startActivity(AmountTotalListActivity.startAmountTotalListActivity(mContext, spIdList, storeBean.group_id));
                            } else if (pos == 1) {
                                startActivity(AmountConsumeListActivity.startAmountConsumeListActivity(mContext, spIdList, storeBean.group_id));
                            } else {
                                startActivity(ResultsListActivity.startFromTotalAmount(mContext, spIdList, storeBean.group_id));
                            }
                        }
                    });

                    if(result.data.storedvalue >= 10000){
                        storedValueWan = true;
                        tvStoredValue.setText(MathUtils.formatNum(result.data.storedvalue + "", false));
                    }else{
                        storedValueWan = false;
                        TextViewUtils.setTextOrEmpty(tvStoredValue, StringFormatUtils.formatDecimal(result.data.storedvalue));
                    }
                    if(result.data.arrears >= 10000){
                        arrearsWan = true;
                        tvArrears.setText(MathUtils.formatNum(result.data.arrears + "", false));
                    }else{
                        arrearsWan = false;
                        TextViewUtils.setTextOrEmpty(tvArrears, StringFormatUtils.formatDecimal(result.data.arrears));
                    }
                    if(result.data.canbe_consume >= 10000){
                        canbeUsedWan = true;
                        tvCanbeUsed.setText(MathUtils.formatNum(result.data.canbe_consume + "", false));
                    }else{
                        canbeUsedWan = false;
                        TextViewUtils.setTextOrEmpty(tvCanbeUsed, StringFormatUtils.formatDecimal(result.data.canbe_consume));
                    }

                    TextViewUtils.setTextOrEmpty(tvPeopleTime, result.data.person_times + "", "0");
                    TextViewUtils.setTextOrEmpty(tvNewMember, result.data.add_member + "", "0");
                    TextViewUtils.setTextOrEmpty(tvPeopleNum, result.data.person_number + "", "0");
                    TextViewUtils.setTextOrEmpty(tvServiceNum, result.data.service_numbers + "", "0");
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

}

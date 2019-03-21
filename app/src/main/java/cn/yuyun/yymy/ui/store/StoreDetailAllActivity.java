package cn.yuyun.yymy.ui.store;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.NoneProgressSubscriber;
import com.githang.statusbar.StatusBarCompat;

import java.io.Serializable;
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
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean.OgServiceplacesRspListBean;
import cn.yuyun.yymy.http.result.ResultSpDetail;
import cn.yuyun.yymy.ui.home.analysis.AnalysisOneAllActivity;
import cn.yuyun.yymy.ui.store.attendance.AttendanceStatisticsAllActivity;
import cn.yuyun.yymy.ui.store.bill.StoreBillHqActivity;
import cn.yuyun.yymy.ui.store.member.StoreMemberListAllActivity;
import cn.yuyun.yymy.ui.store.report.FinanceReportHqActivity;
import cn.yuyun.yymy.ui.store.report.MemberAnalysisAllActivity;
import cn.yuyun.yymy.ui.store.report.ReportBrandContrastAllActivity;
import cn.yuyun.yymy.ui.store.report.ReportBrandContrastOneAllActivity;
import cn.yuyun.yymy.ui.store.report.ReportBusinessDetailAllActivity;
import cn.yuyun.yymy.ui.store.report.ReportBusinessStatisticsAllActivity;
import cn.yuyun.yymy.ui.store.report.ReportConsumeDetailAllActivity;
import cn.yuyun.yymy.ui.store.report.ReportHqOutputHqActivity;
import cn.yuyun.yymy.ui.store.report.ReportLiabilitiesAllActivity;
import cn.yuyun.yymy.ui.store.report.ReportStoreSaleAllActivity;
import cn.yuyun.yymy.ui.store.report.StaffAnalysisAllActivity;
import cn.yuyun.yymy.ui.store.report.StockAllActivity;
import cn.yuyun.yymy.ui.store.staff.StoreStaffListAllActivity;
import cn.yuyun.yymy.utils.MathUtils;
import cn.yuyun.yymy.view.lineview.HistogramMode;
import cn.yuyun.yymy.view.lineview.HistogramView;
import de.hdodenhof.circleimageview.CircleImageView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author
 * @desc 门店详情 -- 全部
 * @date
 */

public class StoreDetailAllActivity extends BaseNoTitleActivity {

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
    ResultSpDetail tempResultSpDetail;
    private String groupId;
    private List<String> tempSpIdList;
    private List<OgServiceplacesRspListBean> storeList;

    boolean storedValueWan = false;
    boolean arrearsWan = false;
    boolean canbeUsedWan = false;

    public static Intent startStoreDetailActivity(Context context, List<String> tempSpIdList, List<OgServiceplacesRspListBean> storeList, String groupId) {
        return new Intent(context, StoreDetailAllActivity.class).putExtra(EXTRA_DATA2, (Serializable) tempSpIdList).putExtra(EXTRA_DATA, (Serializable) storeList).putExtra(EXTRA_TYPE, groupId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.title_mine));
        setContentView(R.layout.activity_store_all_detail);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    private void initDatas() {
        RequestSpDetail body = new RequestSpDetail();
        body.start_date = DateTimeUtils.getTimesMonthMorning();
        body.end_date = DateTimeUtils.getNowTimeStamp();
        body.sp_id_list = tempSpIdList;
        body.group_id = groupId;
        getSpDetails(body);
    }

    private void initViews() {
        titleBar.setTilte("门店详情");
        groupId = getIntent().getStringExtra(EXTRA_TYPE);
        tempSpIdList = (List<String>) getIntent().getSerializableExtra(EXTRA_DATA2);
        storeList = (List<OgServiceplacesRspListBean>) getIntent().getSerializableExtra(EXTRA_DATA);
        TextViewUtils.setTextOrEmpty(tvCharge, "负责人:无");
        TextViewUtils.setTextOrEmpty(tvMobile,"未填写门店地址");
        TextViewUtils.setTextOrEmpty(tvName, "全部");
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

    @OnClick({R.id.ll_storedValue, R.id.ll_canbeUsed,R.id.ll_arrears, R.id.iv_back, R.id.ll_liabilities, R.id.ll_storeSale, R.id.ll_staff, R.id.ll_member, R.id.ll_staffAnalysis, R.id.ll_memberAnalysis, R.id.ll_finance, R.id.ll_business, R.id.ll_analysis, R.id.ll_stock, R.id.ll_attendance, R.id.ll_bill, R.id.ll_consumeDetail, R.id.ll_businessDetail, R.id.ll_brandContrast})
    public void onClickItem(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_analysis:
                //品项分析表
                startActivity(AnalysisOneAllActivity.startAnalysisOneActivity(mContext, tempSpIdList, storeList, groupId));
                break;
            case R.id.ll_finance:
                //财务分析表
                startActivity(FinanceReportHqActivity.startFinanceReportActivity(mContext, tempSpIdList, storeList, groupId));
                break;
            case R.id.ll_business:
                //营业统计表
                startActivity(ReportBusinessStatisticsAllActivity.startBusinessReportActivity(mContext, tempSpIdList, storeList, groupId));
                break;
            case R.id.ll_staffAnalysis:
                //员工业绩表
                startActivity(StaffAnalysisAllActivity.startStaffAnalysisActivity(mContext, tempSpIdList, storeList, groupId));
                break;
            case R.id.ll_memberAnalysis:
                //会员排行表
                startActivity(MemberAnalysisAllActivity.startMemberAnalysisActivity(mContext, tempSpIdList, storeList, groupId));
                break;
            case R.id.ll_businessDetail:
                //营业明细表
                startActivity(ReportBusinessDetailAllActivity.startReportBusinessDetailActivity(mContext, tempSpIdList, storeList, groupId));
                break;
            case R.id.ll_consumeDetail:
                //消耗明细表
                startActivity(ReportConsumeDetailAllActivity.startReportConsumeDetailActivity(mContext, tempSpIdList, storeList, groupId));
                break;
            case R.id.ll_brandContrast:
                //品项占比表
                startActivity(ReportBrandContrastOneAllActivity.startReportBrandContrastActivity(mContext, tempSpIdList, storeList, groupId));
                break;
            case R.id.ll_storeSale:
                //门店出库表
                startActivity(ReportStoreSaleAllActivity.startReportStoreOutputActivityForAll(mContext, tempSpIdList, storeList, groupId));
                break;
            case R.id.ll_liabilities:
                //门店负债表
                startActivity(ReportLiabilitiesAllActivity.startReportLiabilitiesActivity(mContext, tempSpIdList, storeList, groupId));
                break;
            case R.id.ll_book:
                //预约本
//                startActivity(AppointmentBookActivity.startAppointmentBookActivity(mContext, storeBean));
                break;
            case R.id.ll_stock:
                //库存管理
                startActivity(StockAllActivity.startStockActivity(mContext, tempSpIdList, storeList, groupId));
                break;
//            startActivity(StoreAnalysisActivity.startStoreAnalysisActivity(mContext, tempSpIdList, groupId));
            case R.id.ll_staff:
                //本店员工
                startActivity(StoreStaffListAllActivity.startStaffAnalysisActivity(mContext,tempSpIdList, storeList, groupId));
                break;
            case R.id.ll_member:
                //本店会员
                startActivity(StoreMemberListAllActivity.startMemberAnalysisActivity(mContext,tempSpIdList, storeList, groupId));
                break;
            case R.id.ll_attendance:
                //考勤汇总
                startActivity(AttendanceStatisticsAllActivity.startAttendanceStatisticsActivity(mContext,tempSpIdList, storeList, groupId));
                break;
            case R.id.ll_bill:
                //明细单据
                startActivity(StoreBillHqActivity.startStoreBillActivity(mContext,tempSpIdList, storeList, groupId));
                break;
            case R.id.ll_storedValue:
                //门店分析
                startActivity(StoreAnalysisActivity.startStoreAnalysisActivityForOne(mContext, tempSpIdList, groupId));
                break;
            case R.id.ll_canbeUsed:
                //门店分析
                startActivity(StoreAnalysisActivity.startStoreAnalysisActivityForThree(mContext, tempSpIdList, groupId));
                break;
            case R.id.ll_arrears:
                //门店分析
                startActivity(StoreAnalysisActivity.startStoreAnalysisActivityForTwo(mContext, tempSpIdList, groupId));
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
                                startActivity(AmountTotalListActivity.startAmountTotalListActivity(mContext, tempSpIdList, groupId));
                            } else if (pos == 1) {
                                startActivity(AmountConsumeListActivity.startAmountConsumeListActivity(mContext, tempSpIdList, groupId));
                            } else {
                                startActivity(ResultsListActivity.startFromTotalAmount(mContext, tempSpIdList, groupId));
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

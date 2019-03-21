package cn.yuyun.yymy.ui.home.cashier;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.bean.RecordType;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.request.RequestCashier;
import cn.yuyun.yymy.http.request.RequestCashier.ListCbBuyQoBean;
import cn.yuyun.yymy.http.request.RequestCashier.StaffPreSaleServiceRecordQoListBean;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;
import cn.yuyun.yymy.http.result.ResultListStaff;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.http.result.ResultProject;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.ui.store.staff.MultipleChoiceStaffActivity;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.ToggleButton;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;

/**
 * @author
 * @desc
 * @date
 */

public class CashierBuyListActivity extends BaseActivity {

    @BindView(R.id.tv_toggleDesc)
    TextView tvToggleDesc;
    @BindView(R.id.toggleButton)
    ToggleButton toggleButton;
    @BindView(R.id.tv_createTime)
    TextView tvCreateTime;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.tv_amountDesc)
    TextView tvAmountDesc;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_nextStep)
    TextView tvNextStep;
    private CashierBuyListAdapter mAdapter;
    private List<ResultListStaff> staffIntentList = new ArrayList<>();
    private ResultMemberBean memberBean;
    private int tempPos;
    private TimePickerView timePickerView;
    private double realPay;
    private List<CashierBuyBean> list;
    private Date consumeDate;

    private RequestCashier body = new RequestCashier();

    public static Intent startCashierBuyListActivity(Context context, List<CashierBuyBean> list, ResultMemberBean memberBean) {
        return new Intent(context, CashierBuyListActivity.class).putExtra(EXTRA_DATA, (Serializable) list).putExtra(EXTRA_DATA2, memberBean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_buy_list);
        EvenManager.register(this);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    private void initDatas() {
        if (null != list) {
            for (CashierBuyBean cashierBuyBean : list) {
                cashierBuyBean.transaction_price = cashierBuyBean.guideprice * cashierBuyBean.total_num;
            }
            mAdapter.addAll(list);
        } else {
            list = new ArrayList<>();
        }
    }

    private void initViews() {
        titleBar.setTilte("购买清单");
        list = (List<CashierBuyBean>) getIntent().getSerializableExtra(EXTRA_DATA);
        memberBean = (ResultMemberBean) getIntent().getSerializableExtra(EXTRA_DATA2);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new CashierBuyListAdapter(mContext);
        rv.setAdapter(mAdapter);
        mAdapter.setStoredValue(memberBean.memberStoredValue);
        mAdapter.setOnItemClickListener(new CashierBuyListAdapter.OnMyItemClickListener() {

            @Override
            public void onSelect(int position, List<ResultListStaff> staffList, CashierBuyBean cashierBuyBean) {
                tempPos = position;
                ResultClassfiyStoreBean.OgServiceplacesRspListBean storeBean = new ResultClassfiyStoreBean.OgServiceplacesRspListBean();
                storeBean.sp_id = memberBean.memberInSpId;
                storeBean.group_id = LoginInfoPrefs.getInstance(mContext).getGroupId();

                if(null != mAdapter.getData(position).staffList && mAdapter.getData(position).staffList.size() > 0){
                    Intent intent = CommisionListActivity.startCommisionListActivityForBuy(mContext, mAdapter.getData(position).staffList, storeBean);
                    intent.putExtra("amount", cashierBuyBean.total_num * cashierBuyBean.transaction_price);
                    intent.putExtra("count", cashierBuyBean.total_num);
                    intent.putExtra("cashierBuyBean", cashierBuyBean);
                    startActivity(intent);
                }else{
                    Intent intent = MultipleChoiceStaffActivity.startFromCommisionStaffSelectForBuy(mContext, storeBean);
                    intent.putExtra("amount",cashierBuyBean.total_num * cashierBuyBean.transaction_price);
                    intent.putExtra("count", cashierBuyBean.total_num);
                    intent.putExtra("cashierBuyBean", cashierBuyBean);
                    startActivity(intent);
                }

               /* if (null != mAdapter.getData(position).staffList && mAdapter.getData(position).staffList.size() > 0) {
                    startActivity(CommisionListActivity.startCommisionListActivityForBuy(mContext, mAdapter.getData(position).staffList, storeBean));
                } else {
                    startActivity(MultipleChoiceStaffActivity.startFromCommisionStaffSelectForBuy(mContext, storeBean));
                }*/
            }

            @Override
            public void onTotal(double total) {
                realPay = total;
                tvAmount.setText(StringFormatUtils.formatTwoDecimal(total));
            }

            @Override
            public void del(int size, CashierBuyBean bean) {
                EvenManager.sendEvent(new NotifyEvent(bean, NotifyEvent.SELECT_PROJECT));
                if (size == 0) {
                    finish();
                }
            }

            @Override
            public void toast(String toast) {
                showToast(toast);
            }
        });
        toggleButton.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    body.consume_type = 2;
                    tvCreateTime.setVisibility(View.VISIBLE);
                    tvCreateTime.setText(DateTimeUtils.getDateTimeText(DateTimeUtils.getNowTimeStamp(), DateTimeUtils.FORMAT_DATETIME_TWO));
                } else {
                    body.consume_type = 1;
                    tvCreateTime.setVisibility(View.INVISIBLE);
                }
            }
        });
        tvCreateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerView.show();
            }
        });
        Calendar endDate = Calendar.getInstance();
        //最大日期是今天
        timePickerView = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tvCreateTime.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATETIME_TWO));
                consumeDate = date;
            }
        })
                // 默认全部显示
                .setType(new boolean[]{true, true, true, true, true, false})
                .setRangDate(null, endDate)
                .build();
        tvNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ListCbBuyQoBean> buyList = new ArrayList<>();
                for (CashierBuyBean cashierBuyBean : mAdapter.getData()) {
                    //购买的商品
                    ListCbBuyQoBean buyBean = new ListCbBuyQoBean();
                    buyBean.good_id = cashierBuyBean.good_id;
                    buyBean.asset_type = cashierBuyBean.asset_type;
                    buyBean.transaction_price = cashierBuyBean.transaction_price;
                    buyBean.amount_realpay = cashierBuyBean.amount_realpay;
                    buyBean.arrear_pay = cashierBuyBean.arrear_pay;
                    buyBean.total_num = cashierBuyBean.total_num;
                    buyBean.storedvalue_pay = cashierBuyBean.storedvalue_pay;
                    List<StaffPreSaleServiceRecordQoListBean> commisionStaffList = new ArrayList<>();
                    if (null != cashierBuyBean.staffList) {
                        List<RequestCashier.ListStaffpersonTimesQoBean> timesList = new ArrayList<>();
                        for (int i = 0; i < cashierBuyBean.staffList.size(); i++) {
                            if (cashierBuyBean.staffList.get(i).before != 0) {
                                StaffPreSaleServiceRecordQoListBean staffBean = new StaffPreSaleServiceRecordQoListBean();
                                staffBean.sale_type = RecordType.BEFORE;
                                staffBean.staff_id = cashierBuyBean.staffList.get(i).staff_id;
                                staffBean.achieve_amount = cashierBuyBean.staffList.get(i).before;
                                commisionStaffList.add(staffBean);
                            }
                            if (cashierBuyBean.staffList.get(i).sale != 0) {
                                StaffPreSaleServiceRecordQoListBean staffBean = new StaffPreSaleServiceRecordQoListBean();
                                staffBean.sale_type = RecordType.SALE;
                                staffBean.staff_id = cashierBuyBean.staffList.get(i).staff_id;
                                staffBean.achieve_amount = cashierBuyBean.staffList.get(i).sale;
                                commisionStaffList.add(staffBean);
                            }
                            RequestCashier.ListStaffpersonTimesQoBean tiemsBean = new RequestCashier.ListStaffpersonTimesQoBean();
                            tiemsBean.person_times = cashierBuyBean.staffList.get(i).person_times;
                            tiemsBean.staff_id = cashierBuyBean.staffList.get(i).staff_id;
                            timesList.add(tiemsBean);
                        }
                        body.list_staffperson_timesQo = timesList;
                    }
                    buyBean.staffPreSaleServiceRecordQoList = commisionStaffList;
                    buyList.add(buyBean);
                }
                body.list_cb_buyQo = buyList;
                body.member_id = memberBean.memberId;
                body.cashier_sp = memberBean.memberInSpId;
                if (toggleButton.getToggleStatu()) {
                    if(null != consumeDate){
                        body.consume_time = consumeDate.getTime() / 1000;
                    }
                } else {
                    body.consume_time = 0;
                }
                startActivity(CashierOverviewActivity.startCashierOverviewActivity(mContext, body, realPay));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if (notifyEvent.tag == NotifyEvent.FINISH_COMMISION_STAFF) {
            staffIntentList = (List<ResultListStaff>) notifyEvent.value;
            mAdapter.notifyStaffList(staffIntentList, tempPos);
        } else if(notifyEvent.tag == NotifyEvent.FINISH_CASHIER){
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("intent", (Serializable) mAdapter.getData());
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}

package cn.yuyun.yymy.ui.home.cashier;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.DeviceUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.VersionUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.bean.RecordType;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestCashier;
import cn.yuyun.yymy.http.request.RequestCashierCharge;
import cn.yuyun.yymy.http.request.RequestCashierNotify;
import cn.yuyun.yymy.http.result.ResultCashier;
import cn.yuyun.yymy.http.result.ResultCashierCharge;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;
import cn.yuyun.yymy.http.result.ResultListStaff;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.ui.home.bill.SignPaintActivity;
import cn.yuyun.yymy.ui.store.staff.MultipleChoiceStaffActivity;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.utils.MathUtils;
import cn.yuyun.yymy.view.ToggleButton;
import cn.yuyun.yymy.view.dialog.CashierFinishDialog;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;

/**
 * @author
 * @desc
 * @date
 */

public class CashierChargeActivity extends BaseActivity implements TextWatcher {

    @BindView(R.id.tv_toggleDesc)
    TextView tvToggleDesc;
    @BindView(R.id.toggleButton)
    ToggleButton toggleButton;
    @BindView(R.id.tv_createTime)
    TextView tvCreateTime;
    @BindView(R.id.et_cash)
    EditText etCash;
    @BindView(R.id.et_pos)
    EditText etPos;
    @BindView(R.id.et_transfer)
    EditText etTransfer;
    @BindView(R.id.et_wechat)
    EditText etWechat;
    @BindView(R.id.et_alipay)
    EditText etAlipay;
    @BindView(R.id.tv_staff)
    TextView tvStaff;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.rl_select)
    RelativeLayout rlSelect;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.tv_amountDesc)
    TextView tvAmountDesc;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_nextStep)
    TextView tvNextStep;
    @BindView(R.id.et_mark)
    EditText etMark;
    private ResultMemberBean memberBean;
    private List<ResultListStaff> staffIntentList = new ArrayList<>();
    private RequestCashierCharge requestBody;
    private TimePickerView timePickerView;
    private Date consumeDate;

    public static Intent startCashierChargeActivity(Context context, ResultMemberBean memberBean) {
        return new Intent(context, CashierChargeActivity.class).putExtra(EXTRA_DATA2, memberBean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_charge);
        EvenManager.register(this);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    private void initDatas() {

    }

    private void initViews() {
        titleBar.setTilte("充值");
        memberBean = (ResultMemberBean) getIntent().getSerializableExtra(EXTRA_DATA2);
        tvNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        requestBody = new RequestCashierCharge();
        etCash.setFilters(new InputFilter[]{new MoneyValueFilter()});
        etAlipay.setFilters(new InputFilter[]{new MoneyValueFilter()});
        etPos.setFilters(new InputFilter[]{new MoneyValueFilter()});
        etTransfer.setFilters(new InputFilter[]{new MoneyValueFilter()});
        etWechat.setFilters(new InputFilter[]{new MoneyValueFilter()});
        etCash.addTextChangedListener(this);
        etAlipay.addTextChangedListener(this);
        etPos.addTextChangedListener(this);
        etTransfer.addTextChangedListener(this);
        etWechat.addTextChangedListener(this);
        rlSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etCash.getText().toString().trim()) && TextUtils.isEmpty(etAlipay.getText().toString()) && TextUtils.isEmpty(etPos.getText().toString()) && TextUtils.isEmpty(etTransfer.getText().toString()) && TextUtils.isEmpty(etWechat.getText().toString())){
                    showToast("请先输入充值金额");
                    return;
                }
                ResultClassfiyStoreBean.OgServiceplacesRspListBean storeBean = new ResultClassfiyStoreBean.OgServiceplacesRspListBean();
                storeBean.sp_id = memberBean.memberInSpId;
                storeBean.group_id = LoginInfoPrefs.getInstance(mContext).getGroupId();

                if(null != staffIntentList && staffIntentList.size() > 0){
                    Intent intent = CommisionListActivity.startCommisionListActivityForCharge(mContext,staffIntentList, storeBean);
                    intent.putExtra("amount",  requestBody.amount);
                    startActivity(intent);
                }else{
                    Intent intent = MultipleChoiceStaffActivity.startFromCommisionStaffSelectForCharge(mContext, storeBean);
                    intent.putExtra("amount",  requestBody.amount);
                    startActivity(intent);
                }
            }
        });
        toggleButton.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if(on){
                    tvCreateTime.setVisibility(View.VISIBLE);
                    tvCreateTime.setText(DateTimeUtils.getDateTimeText(DateTimeUtils.getNowTimeStamp(), DateTimeUtils.FORMAT_DATETIME_TWO));
                    requestBody.consume_time = DateTimeUtils.getNowTimeStamp() + "";
                }else{
                    tvCreateTime.setVisibility(View.INVISIBLE);
                    requestBody.consume_time = "";
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
    }

    private void submit() {
        if(TextUtils.isEmpty(etCash.getText().toString().trim()) && TextUtils.isEmpty(etAlipay.getText().toString()) && TextUtils.isEmpty(etPos.getText().toString()) && TextUtils.isEmpty(etTransfer.getText().toString()) && TextUtils.isEmpty(etWechat.getText().toString())){
            showToast("请先输入充值金额");
            return;
        }
        requestBody.cashier_sp_id = memberBean.memberInSpId;
        requestBody.member_id = memberBean.memberId;
        if (toggleButton.getToggleStatu()) {
            if(null != consumeDate){
                requestBody.consume_time = (consumeDate.getTime() / 1000) + "";
            }
        } else {
            requestBody.consume_time = "";
        }
        if(!TextUtils.isEmpty(etMark.getText().toString())){
            StringBuilder sb = new StringBuilder();
            sb.append("---来自于手机端收银作业，Android设备机型 ");
            sb.append(DeviceUtils.getSystemModel());
            sb.append("  App版本");
            sb.append(VersionUtils.getAppVersionName(mContext));
            requestBody.description = etMark.getText().toString() + sb.toString();
        }else{
            StringBuilder sb = new StringBuilder();
            sb.append("---来自于手机端收银作业，Android设备机型 ");
            sb.append(DeviceUtils.getSystemModel());
            sb.append("  App版本");
            sb.append(VersionUtils.getAppVersionName(mContext));
            requestBody.description = sb.toString();
        }
        List<RequestCashier.StaffPreSaleServiceRecordQoListBean> commisionStaffList = new ArrayList<>();
        if(null != staffIntentList){
            List<RequestCashier.ListStaffpersonTimesQoBean> timesList = new ArrayList<>();
            for (int i = 0; i < staffIntentList.size(); i++) {
                if(staffIntentList.get(i).before != 0){
                    RequestCashier.StaffPreSaleServiceRecordQoListBean staffBean = new RequestCashier.StaffPreSaleServiceRecordQoListBean();
                    staffBean.sale_type = RecordType.BEFORE;
                    staffBean.staff_id = staffIntentList.get(i).staff_id;
                    staffBean.achieve_amount = staffIntentList.get(i).before;
                    commisionStaffList.add(staffBean);
                }
                if(staffIntentList.get(i).sale != 0){
                    RequestCashier.StaffPreSaleServiceRecordQoListBean staffBean = new RequestCashier.StaffPreSaleServiceRecordQoListBean();
                    staffBean.sale_type = RecordType.SALE;
                    staffBean.staff_id = staffIntentList.get(i).staff_id;
                    staffBean.achieve_amount = staffIntentList.get(i).sale;
                    commisionStaffList.add(staffBean);
                }
                RequestCashier.ListStaffpersonTimesQoBean tiemsBean = new RequestCashier.ListStaffpersonTimesQoBean();
                tiemsBean.person_times = staffIntentList.get(i).person_times;
                tiemsBean.staff_id = staffIntentList.get(i).staff_id;
                timesList.add(tiemsBean);
            }
            requestBody.staffPersonTimesQoList = timesList;
        }
        requestBody.staffPreSaleServiceRecordQoList = commisionStaffList;
        AppModelFactory.model().submitCashierCharge(requestBody, new ProgressSubscriber<DataBean<ResultCashierCharge>>(mContext, false) {
            @Override
            public void onNext(final DataBean<ResultCashierCharge> resultCashierDataBean) {
                CashierFinishDialog dialog = new CashierFinishDialog(mContext);
                dialog.setOnPositiveListener(new CashierFinishDialog.OnPositiveListener() {
                    @Override
                    public void onPositive(int type) {
                        if(null != resultCashierDataBean.data && null != resultCashierDataBean.data.record_id && resultCashierDataBean.data.record_id.size() > 0){
                            ResultCashier resultCashier = new ResultCashier();
                            resultCashier.record_id = resultCashierDataBean.data.record_id.get(0).storedvalue_id;
                            resultCashier.record_type = resultCashierDataBean.data.record_type;
                            send(type, resultCashier);
                        }

                    }

                    @Override
                    public void onNegative() {
                        startActivity(SignPaintActivity.startSignPaintActivityForCashier(mContext, resultCashierDataBean.data.record_id.get(0).storedvalue_id, resultCashierDataBean.data.record_type));
                        finish();
                    }
                });
                dialog.show();
                dialog.setTips("充值完成");
            }


            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast(ex.message);
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast("网络异常，请检查网络");
            }

        });
    }

    private void send(int type, final ResultCashier resultCashier) {
        RequestCashierNotify body = new RequestCashierNotify();
        body.record_id = resultCashier.record_id;
        body.record_type = resultCashier.record_type;
        body.send_type = type;
        AppModelFactory.model().cashierNotify(body, new ProgressSubscriber<DataBean<Object>>(mContext, false) {
            @Override
            public void onNext(DataBean<Object> result) {
                showToast("推送成功");
                startActivity(SignPaintActivity.startSignPaintActivityForCashier(mContext, resultCashier.record_id, resultCashier.record_type));
                finish();
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast("推送失败");
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast("网络异常，请检查网络");
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        double cash = MathUtils.parseDouble(etCash.getText().toString().trim());
        double wechat = MathUtils.parseDouble(etWechat.getText().toString().trim());
        double pos = MathUtils.parseDouble(etPos.getText().toString().trim());
        double transfer = MathUtils.parseDouble(etTransfer.getText().toString().trim());
        double alipay = MathUtils.parseDouble(etAlipay.getText().toString().trim());

        double total = cash + wechat + pos + transfer + alipay;

        requestBody.pay_ali_pay = alipay;
        requestBody.pay_cash = cash;
        requestBody.pay_pos = pos;
        requestBody.pay_transfer = transfer;
        requestBody.pay_wechat_pay = wechat;
        requestBody.amount = total;

        tvAmount.setText(StringFormatUtils.formatTwoDecimal(total));

        /*if (total == realPay) {
            tvNextStep.setClickable(true);
            tvNextStep.setBackgroundColor(ResoureUtils.getColor(mContext, R.color.colorPrimary));
        } else {
            tvNextStep.setClickable(false);
            tvNextStep.setBackgroundColor(ResoureUtils.getColor(mContext, R.color.gray_cc));
        }*/
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if (notifyEvent.tag == NotifyEvent.FINISH_COMMISION_STAFF) {
            staffIntentList = (List<ResultListStaff>) notifyEvent.value;
            StringBuilder sb = new StringBuilder();
            for (ResultListStaff resultListStaff : staffIntentList) {
                sb.append(resultListStaff.staff_name + " ");
            }
            tvStaff.setText(sb.toString());
        }else if(notifyEvent.tag == NotifyEvent.FINISH_CASHIER){
            finish();
        }
    }

}

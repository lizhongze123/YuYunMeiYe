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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lzz.utils.DeviceUtils;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.VersionUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestCashier;
import cn.yuyun.yymy.http.request.RequestCashierNotify;
import cn.yuyun.yymy.http.result.ResultCashier;
import cn.yuyun.yymy.http.result.ResultListStaff;
import cn.yuyun.yymy.ui.home.bill.SignPaintActivity;
import cn.yuyun.yymy.utils.MathUtils;
import cn.yuyun.yymy.view.dialog.CashierFinishDialog;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;

/**
 * @author
 * @desc
 * @date
 */

public class CashierOverviewActivity extends BaseActivity implements TextWatcher {

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
    @BindView(R.id.ll_top)
    LinearLayout llTop;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.tv_amountDesc)
    TextView tvAmountDesc;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_nextStep)
    TextView tvNextStep;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;
    @BindView(R.id.et_mark)
    EditText etMark;
    RequestCashier requestCashier;
    private double realPay;

    public static Intent startCashierOverviewActivity(Context context, RequestCashier requestCashier, double realPay) {
        return new Intent(context, CashierOverviewActivity.class).putExtra(EXTRA_DATA, (Serializable) requestCashier).putExtra(EXTRA_DATA2, realPay);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_overview);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    private void initDatas() {
        requestCashier = (RequestCashier) getIntent().getSerializableExtra(EXTRA_DATA);
        realPay = getIntent().getDoubleExtra(EXTRA_DATA2, 0);
        tvAmount.setText(realPay + "");
    }

    private void initViews() {
        titleBar.setTilte("收银结算");
        tvNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   requestCashier.pay_cash = Double.valueOf(etAlipay.getText().toString().trim());
                requestCashier.pay_total = Double.valueOf(etAlipay.getText().toString().trim());*/
                submit();
            }
        });
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
    }

    private void submit() {
        if(!TextUtils.isEmpty(etMark.getText().toString())){
            StringBuilder sb = new StringBuilder();
            sb.append("---来自于手机端收银作业，Android设备机型 ");
            sb.append(DeviceUtils.getSystemModel());
            sb.append("  App版本");
            sb.append(VersionUtils.getAppVersionName(mContext));
            requestCashier.description = etMark.getText().toString() + sb.toString();
        }else{
            StringBuilder sb = new StringBuilder();
            sb.append("---来自于手机端收银作业，Android设备机型 ");
            sb.append(DeviceUtils.getSystemModel());
            sb.append("  App版本");
            sb.append(VersionUtils.getAppVersionName(mContext));
            requestCashier.description = sb.toString();
        }

        AppModelFactory.model().submitCashier(requestCashier, new ProgressSubscriber<DataBean<ResultCashier>>(mContext, false) {
            @Override
            public void onNext(final DataBean<ResultCashier> resultCashierDataBean) {

                CashierFinishDialog dialog = new CashierFinishDialog(mContext);
                dialog.setOnPositiveListener(new CashierFinishDialog.OnPositiveListener() {
                    @Override
                    public void onPositive(int type) {
                        send(type, resultCashierDataBean.data);
                    }

                    @Override
                    public void onNegative() {
                        startActivity(SignPaintActivity.startSignPaintActivityForCashier(mContext, resultCashierDataBean.data.record_id,resultCashierDataBean.data.record_type ));
                        finish();
                    }
                });
                dialog.show();
                dialog.setTips("购买完成");
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

        requestCashier.pay_ali_pay = alipay;
        requestCashier.pay_cash = cash;
        requestCashier.pay_pos = pos;
        requestCashier.pay_transfer = transfer;
        requestCashier.pay_wechat_pay = wechat;
        requestCashier.pay_total = total;

        if(total == realPay){
            tvNextStep.setClickable(true);
            tvNextStep.setBackgroundColor(ResoureUtils.getColor(mContext, R.color.colorPrimary));
        }else{
            tvNextStep.setClickable(false);
            tvNextStep.setBackgroundColor(ResoureUtils.getColor(mContext, R.color.gray_cc));
        }
    }

}

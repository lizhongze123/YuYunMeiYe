package cn.yuyun.yymy.ui.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.bean.ConsumeType;
import cn.yuyun.yymy.bean.RecordType;
import cn.yuyun.yymy.http.result.RecordBean;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author
 * @desc
 * @date
 */

public class RecordDetailActivity extends BaseActivity {

    private RecordType type;
    private RecordBean recordBean;


    TextView tvConsumeType, tvGoodsName, tvConsumeBill, tvAssetNumber,tvConsumeStore, tvMemberName, tvConsumePrice, tvTruePrice, tvConsumeTime, tvPercent,tvRechargePrice;

    public static Intent startRecordBeforeActivity(Context context, RecordBean recordBean) {
        return new Intent(context, RecordDetailActivity.class).putExtra(EXTRA_TYPE, RecordType.BEFORE).putExtra(EXTRA_DATA, recordBean);
    }
    public static Intent startRecordSaleActivity(Context context,RecordBean recordBean) {
        return new Intent(context, RecordDetailActivity.class).putExtra(EXTRA_TYPE, RecordType.SALE).putExtra(EXTRA_DATA, recordBean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);
    }

    @Override
    protected void setUpViewAndData() {
        initViews();
        initDatas();
    }

    private void initViews() {
        type = (RecordType) getIntent().getSerializableExtra(EXTRA_TYPE);
        recordBean = (RecordBean) getIntent().getSerializableExtra(EXTRA_DATA);
        titleBar.setTilte(type.detail);

        tvConsumeType = (TextView) findViewById(R.id.tv_consumeType);
        tvGoodsName = (TextView) findViewById(R.id.tv_goodsName);
        tvConsumeBill = (TextView) findViewById(R.id.tv_consumeBill);
        tvAssetNumber = (TextView) findViewById(R.id.tv_assetNumber);
        tvConsumeStore = (TextView) findViewById(R.id.tv_consumeStore);
        tvMemberName = (TextView) findViewById(R.id.tv_memberName);
        tvConsumePrice = (TextView) findViewById(R.id.tv_consumePrice);
        tvTruePrice = (TextView) findViewById(R.id.tv_truePrice);
        tvConsumeTime = (TextView) findViewById(R.id.tv_consumeTime);
        tvPercent = (TextView) findViewById(R.id.tv_percent);
        tvRechargePrice = (TextView) findViewById(R.id.tv_rechargePrice);
    }

    private void initDatas() {

        if(recordBean.related_consumption_type == ConsumeType.VALUE_RECHARGE || recordBean.related_consumption_type == ConsumeType.VALUE_REFUND){
            findViewById(R.id.ll_goodsName).setVisibility(View.GONE);
            findViewById(R.id.ll_consumeBill).setVisibility(View.GONE);
            findViewById(R.id.ll_consumePrice).setVisibility(View.GONE);
            findViewById(R.id.ll_truePrice).setVisibility(View.GONE);
            double real = recordBean.obj.pay_ali_pay + recordBean.obj.pay_cash + recordBean.obj.pay_pos + recordBean.obj.pay_transfer + recordBean.obj.pay_wechat_pay;
            TextViewUtils.setTextOrEmpty(tvRechargePrice, StringFormatUtils.formatTwoDecimal(real));
            tvPercent.setText(real + " × " + StringFormatUtils.formatPercent(recordBean.commission));
        }else{
            findViewById(R.id.ll_rechargePrice).setVisibility(View.GONE);
            findViewById(R.id.ll_percent).setVisibility(View.GONE);
            TextViewUtils.setTextOrEmpty(tvGoodsName, recordBean.obj.goodName);
            TextViewUtils.setTextOrEmpty(tvConsumeBill, recordBean.obj.consumeBill);
            TextViewUtils.setTextOrEmpty(tvConsumePrice,StringFormatUtils.formatTwoDecimal(recordBean.obj.transactionPrice));
            TextViewUtils.setTextOrEmpty(tvTruePrice, StringFormatUtils.formatTwoDecimal(recordBean.obj.amountRealpay));

        }
        TextViewUtils.setTextOrEmpty(tvConsumeType, recordBean.related_consumption_type.desc);
        TextViewUtils.setTextOrEmpty(tvAssetNumber, recordBean.related_consumption_id);
        TextViewUtils.setTextOrEmpty(tvConsumeStore, recordBean.obj.cashierSpName);
        TextViewUtils.setTextOrEmpty(tvMemberName, recordBean.obj.memberName);
        TextViewUtils.setTextOrEmpty(tvConsumeTime, recordBean.obj.consumeTime);

    }

}

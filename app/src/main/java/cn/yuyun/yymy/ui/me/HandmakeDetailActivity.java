package cn.yuyun.yymy.ui.me;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.result.RecordBean;
import cn.yuyun.yymy.http.result.ResultHandmake;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc
 * @date
 */

public class HandmakeDetailActivity extends BaseActivity {

    private ResultHandmake bean;

    private TextView tvConsumeBill, tvConsumeDetail,tvConsumeStore, tvMemberName, tvConsumePrice, tvTruePrice, tvConsumeTime, tvPercent;
    private TextView tvServiceCount, tvServiceHours, tvConsumeValue, tvManualFee;
    private TextView tvServiceComment;

    private LinearLayout ll_percent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);

    }

    @Override
    protected void setUpViewAndData() {
        bean = (ResultHandmake) getIntent().getSerializableExtra(EXTRA_DATA);
        initViews();
        initDatas();
    }

    private void initViews() {
        titleBar.setTilte("消耗详情");
        tvConsumeBill = (TextView) findViewById(R.id.tv_consumeBill);
        tvConsumeDetail = (TextView) findViewById(R.id.tv_consumeDetail);
        tvConsumeStore = (TextView) findViewById(R.id.tv_consumeStore);
        tvMemberName = (TextView) findViewById(R.id.tv_memberName);
        tvConsumePrice = (TextView) findViewById(R.id.tv_consumePrice);
        tvTruePrice = (TextView) findViewById(R.id.tv_truePrice);
        tvConsumeTime = (TextView) findViewById(R.id.tv_consumeTime);
        tvPercent = (TextView) findViewById(R.id.tv_percent);
        tvServiceCount = (TextView) findViewById(R.id.tv_serviceCount);
        tvServiceHours = (TextView) findViewById(R.id.tv_serviceHours);
        tvConsumeValue = (TextView) findViewById(R.id.tv_consumeValue);
        tvManualFee = (TextView) findViewById(R.id.tv_manualFee);
        tvServiceComment = (TextView) findViewById(R.id.tv_serviceComment);
        ll_percent = (LinearLayout) findViewById(R.id.ll_percent);
        findViewById(R.id.tv_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加心得
//                startActivity(EmailActivity.startFromFeedBack(mContext, bean.consumeDetail));
            }
        });

    }

    private void initDatas() {
        TextViewUtils.setTextOrEmpty(tvConsumeBill, bean.related_record_id);
        TextViewUtils.setTextOrEmpty(tvConsumeDetail, bean.related_consumption_id);
        TextViewUtils.setTextOrEmpty(tvConsumeStore, bean.cashier_sp_name);
        TextViewUtils.setTextOrEmpty(tvMemberName, bean.member_name);

        TextViewUtils.setTextOrEmpty(tvServiceCount, bean.consumeNum + "");
//        TextViewUtils.setTextOrEmpty(tvServiceComment, bean.serveComment);
        TextViewUtils.setTextOrEmpty(tvServiceHours, bean.serveTime + "分钟");

        TextViewUtils.setTextOrEmpty(tvConsumeValue, StringFormatUtils.formatTwoDecimal(bean.consume_amount_now));
//        TextViewUtils.setTextOrEmpty(tvPercent,  StringFormatUtils.formatPercent(bean.staffPreSaleServiceRecordRspList.get(0).commission));
        TextViewUtils.setTextOrEmpty(tvManualFee, StringFormatUtils.formatTwoDecimal(bean.handmake));

        ll_percent.setVisibility(View.GONE);
    }

}

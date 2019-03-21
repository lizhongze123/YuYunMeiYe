package cn.yuyun.yymy.ui.me;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.result.RecordBean;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc
 * @date
 */

public class RecordServiceDetailActivity extends BaseActivity {

    private RecordBean.ObjBean recordBean;

    TextView tvConsumeBill, tvConsumeDetail,tvConsumeStore, tvMemberName, tvConsumePrice, tvTruePrice, tvConsumeTime, tvPercent;
    TextView tvServiceCount, tvServiceHours, tvConsumeValue, tvManualFee;
    TextView tvServiceComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);

    }

    @Override
    protected void setUpViewAndData() {
        recordBean = (RecordBean.ObjBean) getIntent().getSerializableExtra(EXTRA_DATA);
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
        findViewById(R.id.tv_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加心得
                startActivity(EmailActivity.startFromFeedBack(mContext, recordBean.consumeDetail));
            }
        });

    }

    private void initDatas() {
        TextViewUtils.setTextOrEmpty(tvConsumeBill, recordBean.consumeBill);
        TextViewUtils.setTextOrEmpty(tvConsumeDetail, recordBean.consumeDetail);
        TextViewUtils.setTextOrEmpty(tvConsumeStore, recordBean.cashierSpName);
        TextViewUtils.setTextOrEmpty(tvMemberName, recordBean.memberName);

        TextViewUtils.setTextOrEmpty(tvServiceCount, recordBean.consumeNum + "");
        TextViewUtils.setTextOrEmpty(tvServiceComment, recordBean.serveComment);
        TextViewUtils.setTextOrEmpty(tvServiceHours, recordBean.serveTime + "分钟");

        TextViewUtils.setTextOrEmpty(tvConsumeValue, StringFormatUtils.formatTwoDecimal(recordBean.consumeAmountNow));
        TextViewUtils.setTextOrEmpty(tvPercent,  StringFormatUtils.formatPercent(recordBean.staffPreSaleServiceRecordRspList.get(0).commission));
        TextViewUtils.setTextOrEmpty(tvManualFee, StringFormatUtils.formatTwoDecimal(recordBean.staffPreSaleServiceRecordRspList.get(0).handmake));
    }

}

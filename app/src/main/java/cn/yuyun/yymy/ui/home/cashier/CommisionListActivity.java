package cn.yuyun.yymy.ui.home.cashier;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.lzz.utils.StringFormatUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.result.ResultCanbeUsedAssest;
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
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author
 * @desc
 * @date
 */

public class CommisionListActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_nextStep)
    TextView tvNextStep;
    @BindView(R.id.tv_amountDesc)
    TextView tvAmountDesc;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    private List<ResultListStaff> staffList;
    private CommisionStaffAdapter mAdapter;
    private ResultClassfiyStoreBean.OgServiceplacesRspListBean storeBean;
    public static int TYPE_BUY = 0;
    public static int TYPE_CONSUME = 1;
    public static int TYPE_CHARGE = 2;
    private int type;
    private double amount;
    private double tempAmount;
    private int count;
    private ResultCanbeUsedAssest.ResultCanbeUsedAssestBean projectBean;
    private CashierBuyBean cashierBuyBean;

    public static Intent startCommisionListActivityForBuy(Context context, List<ResultListStaff> list, ResultClassfiyStoreBean.OgServiceplacesRspListBean bean) {
        return new Intent(context, CommisionListActivity.class).putExtra(EXTRA_DATA, (Serializable) list).putExtra(EXTRA_DATA2, bean).putExtra(EXTRA_TYPE, TYPE_BUY);
    }

    public static Intent startCommisionListActivityForConsume(Context context, List<ResultListStaff> list, ResultClassfiyStoreBean.OgServiceplacesRspListBean bean) {
        return new Intent(context, CommisionListActivity.class).putExtra(EXTRA_DATA, (Serializable) list).putExtra(EXTRA_DATA2, bean).putExtra(EXTRA_TYPE, TYPE_CONSUME);
    }

    public static Intent startCommisionListActivityForCharge(Context context, List<ResultListStaff> list, ResultClassfiyStoreBean.OgServiceplacesRspListBean bean) {
        return new Intent(context, CommisionListActivity.class).putExtra(EXTRA_DATA, (Serializable) list).putExtra(EXTRA_DATA2, bean).putExtra(EXTRA_TYPE, TYPE_CHARGE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if (notifyEvent.tag == NotifyEvent.FINISH_COMMISION) {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commision_staff_list);
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
        titleBar.setTilte("提成员工");
        staffList = (List<ResultListStaff>) getIntent().getSerializableExtra(EXTRA_DATA);
        storeBean = (ResultClassfiyStoreBean.OgServiceplacesRspListBean) getIntent().getSerializableExtra(EXTRA_DATA2);
        type = getIntent().getIntExtra(EXTRA_TYPE, 0);
        amount = getIntent().getDoubleExtra("amount", 0);
        count = getIntent().getIntExtra("count", 1);
        projectBean = (ResultCanbeUsedAssest.ResultCanbeUsedAssestBean) getIntent().getSerializableExtra("project");
        cashierBuyBean = (CashierBuyBean) getIntent().getSerializableExtra("cashierBuyBean");
        if (type == TYPE_BUY) {
            tvAmountDesc.setText("本单业绩");

            if (cashierBuyBean.achieve_statistics_type == 1) {
                //（成交价格 - 储值支付）* 总次数 *百分比
                tempAmount = (cashierBuyBean.amount_realpay) * cashierBuyBean.achieve_percent;
                tvAmount.setText(StringFormatUtils.formatTwoDecimal(tempAmount));
            } else {
                tempAmount = amount;
                tvAmount.setText(StringFormatUtils.formatTwoDecimal(amount));
            }

            double average = tempAmount / staffList.size();
            for (int i = 0; i < staffList.size(); i++) {
                staffList.get(i).sale = average;
                staffList.get(i).person_times = 1.0 / staffList.size();
            }
        } else if (type == TYPE_CONSUME) {
            tvAmountDesc.setText("本单消耗");
            if (projectBean.goodRsp.consume_statistics_type == 1) {
                if (null != projectBean.assetsPaymentInfoRsp) {
                    //（成交价格-现金券-免单-积分支付）/总次数*耗卡次数*耗卡百分比
                    tempAmount = (projectBean.transaction_price - projectBean.assetsPaymentInfoRsp.coupon_pay - projectBean.assetsPaymentInfoRsp.free_pay - projectBean.assetsPaymentInfoRsp.accumulate_sv_pay) / projectBean.num_total * count * projectBean.goodRsp.consume_percent;
                    tvAmount.setText(StringFormatUtils.formatTwoDecimal(tempAmount));
                } else {
                    //（成交价格-现金券-免单-积分支付）/总次数*耗卡次数*耗卡百分比
                    tempAmount = (projectBean.transaction_price - 0) / projectBean.num_total * count * projectBean.goodRsp.consume_percent;
                    tvAmount.setText(StringFormatUtils.formatTwoDecimal(tempAmount));
                }
            } else {
                tempAmount = amount;
                tvAmount.setText(StringFormatUtils.formatTwoDecimal(amount));
            }

            double average = tempAmount / staffList.size();
            double handmake = 0;
            if (null != projectBean) {
                if (projectBean.transaction_type == 0) {
                    //赠送
                    handmake = (count * projectBean.handmake_default_giveaway) / staffList.size();
                } else {
                    //购买
                    handmake = (count * projectBean.handmake_default_buy) / staffList.size();
                }
            }
            for (int i = 0; i < staffList.size(); i++) {
                staffList.get(i).sale = average;
                staffList.get(i).before = handmake;
                staffList.get(i).person_times = 1.0 / staffList.size();
            }
        } else {
            tvAmountDesc.setText("本单业绩");
            tempAmount = amount;
            tvAmount.setText(StringFormatUtils.formatTwoDecimal(amount));
            double average = tempAmount / staffList.size();
            for (int i = 0; i < staffList.size(); i++) {
                staffList.get(i).sale = average;
                staffList.get(i).person_times = 1.0 / staffList.size();
            }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new CommisionStaffAdapter(mContext, type);
        recyclerView.setAdapter(mAdapter);
        mAdapter.addAll(staffList);
        mAdapter.setOnItemClickListener(new CommisionStaffAdapter.OnMyItemClickListener() {
            @Override
            public void onSelect(int position) {

            }

            @Override
            public void onDel(int position, int size) {
                staffList.remove(position);

                if (type == TYPE_BUY) {
                    double average = tempAmount / staffList.size();
                    for (int i = 0; i < staffList.size(); i++) {
                        staffList.get(i).sale = average;
                        staffList.get(i).person_times = 1.0 / staffList.size();
                    }
                } else {
                    double average = tempAmount / staffList.size();
                    double handmake = 0;
                    if (null != projectBean) {
                        if (projectBean.transaction_type == 0) {
                            //赠送
                            handmake = (count * projectBean.handmake_default_giveaway) / staffList.size();
                        } else {
                            //购买
                            handmake = (count * projectBean.handmake_default_buy) / staffList.size();
                        }
                    }
                    for (int i = 0; i < staffList.size(); i++) {
                        staffList.get(i).sale = average;
                        staffList.get(i).before = handmake;
                        staffList.get(i).person_times = 1.0 / staffList.size();
                    }
                }

                mAdapter.notifyDataSetChanged();
            }
        });
        tvNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (type == TYPE_BUY) {
                    intent = MultipleChoiceStaffActivity.startFromCommisionStaffSelectForBuy(mContext, storeBean);
                    intent.putExtra("amount", amount);
                    intent.putExtra("count", cashierBuyBean.total_num);
                    intent.putExtra("cashierBuyBean", cashierBuyBean);
                } else if (type == TYPE_CONSUME) {
                    intent = MultipleChoiceStaffActivity.startFromCommisionStaffSelectForConsume(mContext, storeBean);
                    intent.putExtra("amount", amount);
                    intent.putExtra("count", count);
                    intent.putExtra("project", projectBean);
                } else {
                    intent = MultipleChoiceStaffActivity.startFromCommisionStaffSelectForCharge(mContext, storeBean);
                    intent.putExtra("amount", amount);
                    intent.putExtra("count", count);
                    intent.putExtra("project", projectBean);
                }
                intent.putExtra(EXTRA_DATA, (Serializable) staffList);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onBackPressed() {
        EvenManager.sendEvent(new NotifyEvent(mAdapter.getData(), NotifyEvent.FINISH_COMMISION_STAFF));
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        EvenManager.unregister(this);
        super.onDestroy();
    }
}

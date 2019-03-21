package cn.yuyun.yymy.ui.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lzz.utils.LogUtils;
import cn.lzz.utils.ToastUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.app.MyApplication;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.bean.ConsumeType;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.PersonTimeListBean;
import cn.yuyun.yymy.http.result.ResultBillManagerTypeDetail;
import cn.yuyun.yymy.ui.home.bill.BillBuyAdapter;
import cn.yuyun.yymy.ui.home.bill.BillConsumeAdapter;
import cn.yuyun.yymy.ui.home.bill.BillGiveAdapter;
import cn.yuyun.yymy.ui.home.bill.BillRefundAdapter;
import cn.yuyun.yymy.ui.home.bill.BillRepaymentAdapter;
import cn.yuyun.yymy.ui.home.bill.BillStoredvalueAdapter;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;

/**
 * @author
 * @desc
 * @date
 */

public class BillDetailActivity extends BaseActivity {

    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.iv_refund)
    ImageView ivRefund;
    @BindView(R.id.expand_refund)
    LinearLayout expandRefund;
    @BindView(R.id.rv_refund)
    RecyclerView rvRefund;
    @BindView(R.id.ll_refund)
    LinearLayout llRefund;
    @BindView(R.id.iv_give)
    ImageView ivGive;
    @BindView(R.id.expand_give)
    LinearLayout expandGive;
    @BindView(R.id.rv_give)
    RecyclerView rvGive;
    @BindView(R.id.ll_give)
    LinearLayout llGive;
    @BindView(R.id.iv_consume)
    ImageView ivConsume;
    @BindView(R.id.expand_consume)
    LinearLayout expandConsume;
    @BindView(R.id.rv_consume)
    RecyclerView rvConsume;
    @BindView(R.id.ll_consume)
    LinearLayout llConsume;
    @BindView(R.id.iv_buy)
    ImageView ivBuy;
    @BindView(R.id.expand_buy)
    LinearLayout expandBuy;
    @BindView(R.id.rv_buy)
    RecyclerView rvBuy;
    @BindView(R.id.ll_buy)
    LinearLayout llBuy;
    @BindView(R.id.iv_storedvalue)
    ImageView ivStoredvalue;
    @BindView(R.id.expand_storedvalue)
    LinearLayout expandStoredvalue;
    @BindView(R.id.rv_storedvalue)
    RecyclerView rvStoredvalue;
    @BindView(R.id.ll_storedvalue)
    LinearLayout llStoredvalue;
    @BindView(R.id.iv_repayment)
    ImageView ivRepayment;
    @BindView(R.id.expand_repayment)
    LinearLayout expandRepayment;
    @BindView(R.id.rv_repayment)
    RecyclerView rvRepayment;
    @BindView(R.id.ll_repayment)
    LinearLayout llRepayment;
    @BindView(R.id.ll1)
    LinearLayout ll1;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    String groupId;

    @OnClick({R.id.expand_refund, R.id.expand_give, R.id.expand_buy, R.id.expand_storedvalue, R.id.expand_repayment, R.id.expand_consume})
    public void expand(View view) {
        switch (view.getId()) {
            case R.id.expand_refund:
                if (rvRefund.getVisibility() == View.VISIBLE) {
                    rvRefund.setVisibility(View.GONE);
                    ivRefund.setRotation(360);
                } else {
                    rvRefund.setVisibility(View.VISIBLE);
                    ivRefund.setRotation(180);
                }
                break;
            case R.id.expand_give:
                if (rvGive.getVisibility() == View.VISIBLE) {
                    rvGive.setVisibility(View.GONE);
                    ivGive.setRotation(360);
                } else {
                    rvGive.setVisibility(View.VISIBLE);
                    ivGive.setRotation(180);
                }
                break;
            case R.id.expand_buy:
                if (rvBuy.getVisibility() == View.VISIBLE) {
                    rvBuy.setVisibility(View.GONE);
                    ivBuy.setRotation(360);
                } else {
                    ivBuy.setRotation(180);
                    rvBuy.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.expand_storedvalue:
                if (rvStoredvalue.getVisibility() == View.VISIBLE) {
                    rvStoredvalue.setVisibility(View.GONE);
                    ivStoredvalue.setRotation(360);
                } else {
                    rvStoredvalue.setVisibility(View.VISIBLE);
                    ivStoredvalue.setRotation(180);
                }
                break;
            case R.id.expand_repayment:
                if (rvRepayment.getVisibility() == View.VISIBLE) {
                    rvRepayment.setVisibility(View.GONE);
                    ivRepayment.setRotation(360);
                } else {
                    rvRepayment.setVisibility(View.VISIBLE);
                    ivRepayment.setRotation(180);
                }
                break;
            case R.id.expand_consume:
                if (rvConsume.getVisibility() == View.VISIBLE) {
                    rvConsume.setVisibility(View.GONE);
                    ivConsume.setRotation(360);
                } else {
                    rvConsume.setVisibility(View.VISIBLE);
                    ivConsume.setRotation(180);
                }
                break;
            default:
        }
    }

    public static Intent startBillDetailActivity(Context context, PersonTimeListBean bean, String groupId) {
        return new Intent(context, BillDetailActivity.class).putExtra(EXTRA_DATA, bean).putExtra(EXTRA_DATA2, groupId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    private void initViews() {
        titleBar.setTilte("人次详情");
    }

    private PersonTimeListBean bean;

    private void initDatas() {
        bean = (PersonTimeListBean) getIntent().getSerializableExtra(EXTRA_DATA);
        groupId = getIntent().getStringExtra(EXTRA_DATA2);
        detail();
    }

    private void detail() {
        if (bean.type.equals(ConsumeType.VALUE_RECHARGE.desc)) {
       /*     AppModelFactory.model().getBillStoredvalueDetail(groupId, bean.record_id, new ProgressSubscriber<DataBean<ResultBillManagerTypeDetail>>(mContext) {

                @Override
                public void onNext(DataBean<ResultBillManagerTypeDetail> result) {
                    if (null != result) {
                        setListData(result.data);
                    }
                }

                @Override
                public void onError(ApiException ex) {
                    super.onError(ex);
                    LogUtils.e(ex.getMessage());
                    ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), ex.message);
                }

                @Override
                public void onCompleted() {
                    super.onCompleted();
                }
            });*/
        } else {
            AppModelFactory.model().getBillConsumeDetail(groupId, bean.record_id, new ProgressSubscriber<DataBean<ResultBillManagerTypeDetail>>(mContext) {

                @Override
                public void onNext(DataBean<ResultBillManagerTypeDetail> result) {
                    if (null != result) {
                        setListData(result.data);
                    }
                }

                @Override
                public void onError(ApiException ex) {
                    super.onError(ex);
                    LogUtils.e(ex.getMessage());
                    ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), ex.message);
                }

                @Override
                public void onCompleted() {
                    super.onCompleted();
                }
            });
        }

    }

    private void setListData(ResultBillManagerTypeDetail result) {

        if (null != result.bill_repayment_info && result.bill_repayment_info.size() > 0) {
            llRepayment.setVisibility(View.VISIBLE);
            BillRepaymentAdapter adapter = new BillRepaymentAdapter(mContext);
            rvRepayment.setLayoutManager(new LinearLayoutManager(mContext) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            adapter.addAll(result.bill_repayment_info);
            rvRepayment.setAdapter(adapter);
        }

        if (null != result.bill_consume_info && result.bill_consume_info.size() > 0) {
            llConsume.setVisibility(View.VISIBLE);
            BillConsumeAdapter adapter = new BillConsumeAdapter(mContext);
            rvConsume.setLayoutManager(new LinearLayoutManager(mContext) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            adapter.addAll(result.bill_consume_info);
            rvConsume.setAdapter(adapter);
        }
        if (null != result.bill_giveaway_info && result.bill_giveaway_info.size() > 0) {
            llGive.setVisibility(View.VISIBLE);
            BillGiveAdapter adapter = new BillGiveAdapter(mContext);
            rvGive.setLayoutManager(new LinearLayoutManager(mContext) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            adapter.addAll(result.bill_giveaway_info);
            rvGive.setAdapter(adapter);
        }
        if (null != result.bill_storedvalue_info && result.bill_storedvalue_info.size() > 0) {
            llStoredvalue.setVisibility(View.VISIBLE);
            BillStoredvalueAdapter adapter = new BillStoredvalueAdapter(mContext);
            rvStoredvalue.setLayoutManager(new LinearLayoutManager(mContext) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            adapter.addAll(result.bill_storedvalue_info);
            rvStoredvalue.setAdapter(adapter);
        }
        if (null != result.bill_buy_info && result.bill_buy_info.size() > 0) {
            llBuy.setVisibility(View.VISIBLE);
            BillBuyAdapter adapter = new BillBuyAdapter(mContext);
            rvBuy.setLayoutManager(new LinearLayoutManager(mContext) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            adapter.addAll(result.bill_buy_info);
            rvBuy.setAdapter(adapter);
        }
        if (null != result.bill_refund_info && result.bill_refund_info.size() > 0) {
            llRefund.setVisibility(View.VISIBLE);
            BillRefundAdapter adapter = new BillRefundAdapter(mContext);
            rvRefund.setLayoutManager(new LinearLayoutManager(mContext) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            adapter.addAll(result.bill_refund_info);
            rvRefund.setAdapter(adapter);
        }
    }
}

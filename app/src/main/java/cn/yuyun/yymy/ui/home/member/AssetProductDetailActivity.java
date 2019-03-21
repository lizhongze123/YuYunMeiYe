package cn.yuyun.yymy.ui.home.member;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lzz.utils.DensityUtils;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.ResultAssetDetail;
import cn.yuyun.yymy.http.result.ResultProduct;
import cn.yuyun.yymy.http.result.ResultServiceAsset;
import cn.yuyun.yymy.ui.home.member.asset.AssetConsumeAdapter;
import cn.yuyun.yymy.ui.home.member.asset.AssetRefundAdapter;
import cn.yuyun.yymy.ui.home.member.asset.AssetRepaymentAdapter;
import cn.yuyun.yymy.view.ShadowDrawable;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc 会员资产-产品详情
 * @date 2018/1/15
 */

public class AssetProductDetailActivity extends BaseActivity {

    @BindView(R.id.tv_one)
    TextView tvOne;
    @BindView(R.id.tv_two)
    TextView tvTwo;
    @BindView(R.id.tv_three)
    TextView tvThree;
    @BindView(R.id.tv_four)
    TextView tvFour;
    @BindView(R.id.tv_five)
    TextView tvFive;
    @BindView(R.id.tv_six)
    TextView tvSix;
    @BindView(R.id.tv_seven)
    TextView tvSeven;
    @BindView(R.id.tv_eight)
    TextView tvEight;
    @BindView(R.id.tv_buyOne)
    TextView tvBuyOne;
    @BindView(R.id.tv_buyTwo)
    TextView tvBuyTwo;
    @BindView(R.id.tv_buyThree)
    TextView tvBuyThree;
    @BindView(R.id.tv_buyFour)
    TextView tvBuyFour;
    @BindView(R.id.tv_buyFive)
    TextView tvBuyFive;
    @BindView(R.id.tv_buySix)
    TextView tvBuySix;
    @BindView(R.id.tv_buySeven)
    TextView tvBuySeven;
    @BindView(R.id.rl_buy)
    RelativeLayout rlBuy;
    @BindView(R.id.ll_buy)
    LinearLayout llBuy;
    @BindView(R.id.tv_giveOne)
    TextView tvGiveOne;
    @BindView(R.id.tv_giveTwo)
    TextView tvGiveTwo;
    @BindView(R.id.tv_giveThree)
    TextView tvGiveThree;
    @BindView(R.id.rl_give)
    RelativeLayout rlGive;
    @BindView(R.id.ll_give)
    LinearLayout llGive;
    @BindView(R.id.rv_repayment)
    RecyclerView rvRepayment;
    @BindView(R.id.ll_repayment)
    LinearLayout llRepayment;
    @BindView(R.id.rv_refund)
    RecyclerView rvRefund;
    @BindView(R.id.ll_refund)
    LinearLayout llRefund;
    @BindView(R.id.rv_consume)
    RecyclerView rvConsume;
    @BindView(R.id.ll_consume)
    LinearLayout llConsume;
    private ResultProduct bean;

    private AssetConsumeAdapter assetConsumeAdapter;
    private AssetRepaymentAdapter assetRepaymentAdapter;
    private AssetRefundAdapter assetRefundAdapter;

    public static Intent startAssetDetailActivity(Context context, ResultProduct bean) {
        return new Intent(context, AssetProductDetailActivity.class).putExtra(EXTRA_DATA, bean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_product_detail);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    private void initViews() {
        titleBar.setTilte("产品详情");
        bean = (ResultProduct) getIntent().getSerializableExtra(EXTRA_DATA);
        ShadowDrawable.setShadowDrawable(rlBuy, Color.parseColor("#ffffff"), DensityUtils.dip2px(mContext, 6),
                Color.parseColor("#22000000"),
                DensityUtils.dip2px(mContext, 3), 0, 0);
        ShadowDrawable.setShadowDrawable(rlGive, Color.parseColor("#ffffff"), DensityUtils.dip2px(mContext, 6),
                Color.parseColor("#22000000"),
                DensityUtils.dip2px(mContext, 3), 0, 0);
        assetConsumeAdapter = new AssetConsumeAdapter(mContext);
        assetRepaymentAdapter = new AssetRepaymentAdapter(mContext);
        assetRefundAdapter = new AssetRefundAdapter(mContext);
        rvConsume.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvConsume.setAdapter(assetRepaymentAdapter);
        rvRepayment.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvRepayment.setAdapter(assetRepaymentAdapter);
        rvRefund.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvRefund.setAdapter(assetRefundAdapter);
    }

    private void initDatas() {
        getData();
    }

    private void getData() {
        AppModelFactory.model().assetDetail(bean.group_id, 1, bean.asset_id, new ProgressSubscriber<DataBean<ResultAssetDetail>>(mContext) {
            @Override
            public void onNext(DataBean<ResultAssetDetail> result) {
                if (null != result.data) {
                    TextViewUtils.setTextOrEmpty(tvOne, "产品名称：" + bean.product_name);
                    TextViewUtils.setTextOrEmpty(tvTwo, "资产编号：" + bean.asset_id);
                    TextViewUtils.setTextOrEmpty(tvThree, "购买时间：" + bean.consume_time);
                    TextViewUtils.setTextOrEmpty(tvFour, "成交价格：" + StringFormatUtils.formatTwoDecimal(bean.transaction_price));
                    TextViewUtils.setTextOrEmpty(tvFive, "已付金额：" + StringFormatUtils.formatTwoDecimal(bean.amount_already_paid));
                    TextViewUtils.setTextOrEmpty(tvSix, "欠款金额：" + StringFormatUtils.formatTwoDecimal(bean.amount_arrear));
                    TextViewUtils.setTextOrEmpty(tvSeven, "剩余金额：" + StringFormatUtils.formatTwoDecimal(bean.amount_canbe_refund));
                    TextViewUtils.setTextOrEmpty(tvEight, "资产总数：" + bean.total_num);

                    if (null != result.data.member_assets_consume && result.data.member_assets_consume.size() != 0) {
                        llConsume.setVisibility(View.VISIBLE);
                        assetConsumeAdapter.addAll(result.data.member_assets_consume);
                        rvConsume.setAdapter(assetConsumeAdapter);
                    }

                    if (null != result.data.member_assets_repayment && result.data.member_assets_repayment.size() != 0) {
                        llRepayment.setVisibility(View.VISIBLE);
                        assetRepaymentAdapter.addAll(result.data.member_assets_repayment);
                        rvRepayment.setAdapter(assetRepaymentAdapter);
                    }

                    if (null != result.data.member_assets_refund && result.data.member_assets_refund.size() != 0) {
                        llRefund.setVisibility(View.VISIBLE);
                        assetRefundAdapter.addAll(result.data.member_assets_refund);
                        rvRefund.setAdapter(assetRefundAdapter);
                    }

                    if (null != result.data.member_assets_buy && result.data.member_assets_buy.size() != 0) {
                        llBuy.setVisibility(View.VISIBLE);
                        tvBuyOne.setText("购买门店：" + result.data.member_assets_buy.get(0).cashier_sp_name);
                        tvBuyTwo.setText("实际付款：" + StringFormatUtils.formatTwoDecimal(result.data.member_assets_buy.get(0).amount_realpay));
                        tvBuyThree.setText("储值支付：" + StringFormatUtils.formatTwoDecimal(result.data.member_assets_buy.get(0).storedvalue_pay));
                        tvBuyFour.setText("免单支付：" + StringFormatUtils.formatTwoDecimal(result.data.member_assets_buy.get(0).free_pay));
                        tvBuyFive.setText("现金券支付：" + StringFormatUtils.formatTwoDecimal(result.data.member_assets_buy.get(0).coupon_pay));
                        tvBuySix.setText("购买日期：" + result.data.member_assets_buy.get(0).consume_time);
                        if(TextUtils.isEmpty(result.data.member_assets_buy.get(0).description)) {
                            tvBuySeven.setText("备注：无");
                        }else{
                            tvBuySeven.setText("备注：" + result.data.member_assets_buy.get(0).description);
                        }
                    }

                    if (null != result.data.member_assets_giveaway  && result.data.member_assets_giveaway.size() != 0) {
                        llGive.setVisibility(View.VISIBLE);
                        tvGiveOne.setText("购买门店：" + result.data.member_assets_giveaway.get(0).cashier_sp_name);
                        tvGiveTwo.setText("购买日期：" + result.data.member_assets_giveaway.get(0).create_time);
                        if(TextUtils.isEmpty(result.data.member_assets_giveaway.get(0).description)) {
                            tvGiveThree.setText("备注：无");
                        }else{
                            tvGiveThree.setText("备注：" + result.data.member_assets_giveaway.get(0).description);
                        }
                    }

                }
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast("请求失败，请稍后重试");
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(mContext.getString(R.string.error_no_network));
            }
        });
    }

}

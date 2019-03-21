package cn.yuyun.yymy.ui.store.bill;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lzz.utils.LogUtils;
import cn.lzz.utils.TextViewUtils;
import cn.lzz.utils.ToastUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.app.MyApplication;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.ResultBillManage;
import cn.yuyun.yymy.http.result.ResultBillManagerTypeDetail;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.home.bill.BillBuyAdapter;
import cn.yuyun.yymy.ui.home.bill.BillConsumeAdapter;
import cn.yuyun.yymy.ui.home.bill.BillGiveAdapter;
import cn.yuyun.yymy.ui.home.bill.BillRefundAdapter;
import cn.yuyun.yymy.ui.home.bill.BillRepaymentAdapter;
import cn.yuyun.yymy.ui.home.bill.BillStoredvalueAdapter;
import cn.yuyun.yymy.utils.GlideHelper;
import de.hdodenhof.circleimageview.CircleImageView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author
 * @desc 单据管理消费详情
 * @date
 */
public class StoreBillDetailActivity extends BaseActivity {

    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_vip)
    TextView tvVip;
    @BindView(R.id.ll_user)
    LinearLayout llUser;
    @BindView(R.id.tv_store)
    TextView tvStore;
    @BindView(R.id.tv_createTime)
    TextView tvCreateTime;
    @BindView(R.id.rl_info)
    RelativeLayout rlInfo;
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

    private String groupId;
    private String type;
    private static String TYPE_CONSUME = "type_consume";
    private static String TYPE_STOREVALUE = "type_storevalue";

    @OnClick({R.id.expand_refund, R.id.expand_give, R.id.expand_buy, R.id.expand_storedvalue, R.id.expand_repayment, R.id.expand_consume})
    public void expand(View view) {
        switch (view.getId()) {
            case R.id.expand_refund:
                if (billRefundAdapter.getAllorOne() == 0) {
                    ivRefund.setRotation(180);
                    billRefundAdapter.setAllorOne(1);
                } else {
                    ivRefund.setRotation(360);
                    billRefundAdapter.setAllorOne(0);
                }
                break;
            case R.id.expand_give:
                if (billGiveAdapter.getAllorOne() == 0) {
                    ivGive.setRotation(180);
                    billGiveAdapter.setAllorOne(1);
                } else {
                    ivGive.setRotation(360);
                    billGiveAdapter.setAllorOne(0);
                }
                break;
            case R.id.expand_buy:
                if (billBuyAdapter.getAllorOne() == 0) {
                    ivBuy.setRotation(180);
                    billBuyAdapter.setAllorOne(1);
                } else {
                    ivBuy.setRotation(360);
                    billBuyAdapter.setAllorOne(0);
                }
                break;
            case R.id.expand_storedvalue:

            case R.id.expand_repayment:
                if (billRepaymentAdapter.getAllorOne() == 0) {
                    ivRepayment.setRotation(180);
                    billRepaymentAdapter.setAllorOne(1);
                } else {
                    ivRepayment.setRotation(360);
                    billRepaymentAdapter.setAllorOne(0);
                }
                break;
            case R.id.expand_consume:
                if (billConsumeAdapter.getAllorOne() == 0) {
                    ivConsume.setRotation(180);
                    billConsumeAdapter.setAllorOne(1);
                } else {
                    ivConsume.setRotation(360);
                    billConsumeAdapter.setAllorOne(0);
                }
                break;
            default:
        }
    }

    public static Intent startStoreBillDetailActivityConsume(Context context, ResultBillManage bean, String groupId) {
        return new Intent(context, StoreBillDetailActivity.class).putExtra(EXTRA_DATA, bean).putExtra(EXTRA_DATA2, groupId).putExtra(EXTRA_TYPE, TYPE_CONSUME);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_bill_detail);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    private void initViews() {
        titleBar.setTilte("单据详情");
        initListView();
    }

    private ResultBillManage resultBillManage;

    private void initDatas() {
        resultBillManage = (ResultBillManage) getIntent().getSerializableExtra(EXTRA_DATA);
        groupId = getIntent().getStringExtra(EXTRA_DATA2);
        if (!TextUtils.isEmpty(resultBillManage.member_avatar)) {
            if (resultBillManage.member_avatar.startsWith(mContext.getString(R.string.HTTP))) {
                GlideHelper.displayImage(mContext, resultBillManage.member_avatar, ivAvatar);
            } else {
                GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + resultBillManage.member_avatar, ivAvatar);
            }
        } else {
            if (null != resultBillManage.member_sex) {
                GlideHelper.displayImage(mContext, resultBillManage.member_sex.resId, ivAvatar);
            } else {
                GlideHelper.displayImage(mContext, R.drawable.avatar_default_female, ivAvatar);
            }
        }
        TextViewUtils.setTextOrEmpty(tvName, resultBillManage.member_name);
        TextViewUtils.setTextOrEmpty(tvStore, resultBillManage.member_in_sp_name);
        TextViewUtils.setTextOrEmpty(tvCreateTime, resultBillManage.create_time);
        if (TextUtils.isEmpty(resultBillManage.member_level_name)) {
            tvVip.setVisibility(View.GONE);
        } else {
            tvVip.setVisibility(View.VISIBLE);
            TextViewUtils.setTextOrEmpty(tvVip, "(" + resultBillManage.member_level_name + ")");
        }

        type = getIntent().getStringExtra(EXTRA_TYPE);
        staffMgrRecordConsumeDetail();
    }

    private BillRepaymentAdapter billRepaymentAdapter;
    private BillConsumeAdapter billConsumeAdapter;
    private BillGiveAdapter billGiveAdapter;
    private BillBuyAdapter billBuyAdapter;
    private BillRefundAdapter billRefundAdapter;


    private void initListView() {
        billRepaymentAdapter = new BillRepaymentAdapter(mContext);
        rvRepayment.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvRepayment.setAdapter(billRepaymentAdapter);

        billConsumeAdapter = new BillConsumeAdapter(mContext);
        rvConsume.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvConsume.setAdapter(billConsumeAdapter);

        billGiveAdapter = new BillGiveAdapter(mContext);
        rvGive.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvGive.setAdapter(billGiveAdapter);

        billBuyAdapter = new BillBuyAdapter(mContext);
        rvBuy.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvBuy.setAdapter(billBuyAdapter);

        billRefundAdapter = new BillRefundAdapter(mContext);
        rvRefund.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvRefund.setAdapter(billRefundAdapter);
    }

    private void setListData(ResultBillManagerTypeDetail result) {
        if (result.bill_repayment_info.size() > 0) {
            llRepayment.setVisibility(View.VISIBLE);
            billRepaymentAdapter.addAll(result.bill_repayment_info);
            rvRepayment.setAdapter(billRepaymentAdapter);
        }
        if (result.bill_consume_info.size() > 0) {
            llConsume.setVisibility(View.VISIBLE);
            billConsumeAdapter.addAll(result.bill_consume_info);
            rvConsume.setAdapter(billConsumeAdapter);
        }
        if (result.bill_giveaway_info.size() > 0) {
            llGive.setVisibility(View.VISIBLE);
            billGiveAdapter.addAll(result.bill_giveaway_info);
            rvGive.setAdapter(billGiveAdapter);
        }
        if (result.bill_buy_info.size() > 0) {
            llBuy.setVisibility(View.VISIBLE);
            billBuyAdapter.addAll(result.bill_buy_info);
            rvBuy.setAdapter(billBuyAdapter);
        }
        if (result.bill_refund_info.size() > 0) {
            llRefund.setVisibility(View.VISIBLE);
            billRefundAdapter.addAll(result.bill_refund_info);
            rvRefund.setAdapter(billRefundAdapter);
        }
    }

    private void staffMgrRecordConsumeDetail() {

        AppModelFactory.model().staffMgrRecordConsumeDetail(groupId, resultBillManage.record_id, new ProgressSubscriber<DataBean<ResultBillManagerTypeDetail>>(mContext) {

            @Override
            public void onNext(DataBean<ResultBillManagerTypeDetail> result) {
                if (null != result) {
                    if (null != result.data) {
                        setListData(result.data);
                    }
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                LogUtils.e(ex.getMessage());
                ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), ex.getMessage());
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });

    }

}

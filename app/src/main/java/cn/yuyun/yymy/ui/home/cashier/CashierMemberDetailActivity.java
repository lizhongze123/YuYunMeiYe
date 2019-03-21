package cn.yuyun.yymy.ui.home.cashier;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.ResultCanbeUsedAssest;
import cn.yuyun.yymy.http.result.ResultListStaff;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.bannerview.RoundAngleImageView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc
 * @date
 */

public class CashierMemberDetailActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.iv_avatar)
    RoundAngleImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    @BindView(R.id.ll_name)
    LinearLayout llName;
    @BindView(R.id.tv_store)
    TextView tvStore;
    @BindView(R.id.tv_charge)
    TextView tvCharge;
    @BindView(R.id.tv_buy)
    TextView tvBuy;
    @BindView(R.id.rl_info)
    RelativeLayout rlInfo;
    @BindView(R.id.rb_one)
    RadioButton rbOne;
    @BindView(R.id.rb_two)
    RadioButton rbTwo;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.rvOne)
    RecyclerView rvOne;
    @BindView(R.id.empty_one)
    EmptyLayout emptyOne;
    @BindView(R.id.rl_one)
    RelativeLayout rlOne;
    @BindView(R.id.rvTwo)
    RecyclerView rvTwo;
    @BindView(R.id.empty_two)
    EmptyLayout emptyTwo;
    @BindView(R.id.rl_two)
    RelativeLayout rlTwo;
    @BindView(R.id.tv_storedValue)
    TextView tvStoredValue;
    @BindView(R.id.tv_arrears)
    TextView tvArrears;
    private ResultMemberBean memberBean;
    private String memberId;
    private String groupId;

    private AssetCanbeUsedAdapter assetCanbeUsedAdapter;
    private AssetCanbeUsedAdapter assetCanbeUsedAdapter2;

    public static Intent startCashierMemberDetailActivity(Context context, String memberId) {
        return new Intent(context, CashierMemberDetailActivity.class).putExtra(EXTRA_DATA, memberId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_member_detail);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        EvenManager.register(this);
        memberId = getIntent().getStringExtra(EXTRA_DATA);
        groupId = LoginInfoPrefs.getInstance(mContext).getGroupId();
        initViews();
        getMemberDetail();
        assetCanbeUsed();
    }

    @Override
    protected void onDestroy() {
        EvenManager.unregister(this);
        super.onDestroy();
    }

    private void initViews() {
        titleBar.setTilte("收银作业");
        if (null != memberBean) {
            initViewDatas(memberBean);
        }
        radioGroup.setOnCheckedChangeListener(this);
        rvOne.setLayoutManager(new LinearLayoutManager(mContext));
        rvTwo.setLayoutManager(new LinearLayoutManager(mContext));
        assetCanbeUsedAdapter = new AssetCanbeUsedAdapter(mContext);
        assetCanbeUsedAdapter2 = new AssetCanbeUsedAdapter(mContext);
        rvOne.setAdapter(assetCanbeUsedAdapter);
        rvTwo.setAdapter(assetCanbeUsedAdapter2);
        assetCanbeUsedAdapter.setOnItemClickListener(new AssetCanbeUsedAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultCanbeUsedAssest.ResultCanbeUsedAssestBean bean, int position) {

                if( (bean.transaction_price / bean.num_total) > bean.amount_still_here){
                    //成交价格 / 购买次数
                    //如果剩余金额小于1次的金额无法耗卡
                    showToast("耗卡金额不足");
                }else{
                    startActivity(CashierConsumeActivity.startCashierConsumeActivity(mContext, bean, memberBean));
                }
            }
        });
        assetCanbeUsedAdapter2.setOnItemClickListener(new AssetCanbeUsedAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultCanbeUsedAssest.ResultCanbeUsedAssestBean bean, int position) {
                showToast("敬请期待");
            }
        });
    }

    private void getMemberDetail() {
        AppModelFactory.model().memberDetail(groupId, memberId, new ProgressSubscriber<DataBean<ResultMemberBean>>(mContext) {
            @Override
            public void onNext(DataBean<ResultMemberBean> result) {
                if (null != result) {
                    memberBean = result.data;
                    initViewDatas(memberBean);
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

    private void assetCanbeUsed() {
        AppModelFactory.model().assetCanbeUsed(groupId, memberId, new ProgressSubscriber<DataBean<ResultCanbeUsedAssest>>(mContext) {
            @Override
            public void onNext(DataBean<ResultCanbeUsedAssest> result) {
                if (null != result.data && null != result.data.canbe_used_assets) {
                    List<ResultCanbeUsedAssest.ResultCanbeUsedAssestBean>serviceList = new ArrayList<>();
                    List<ResultCanbeUsedAssest.ResultCanbeUsedAssestBean>packetList = new ArrayList<>();
                    for (int i = 0; i < result.data.canbe_used_assets.size(); i++) {
                        if(result.data.canbe_used_assets.get(i).asset_type == 2){
                            serviceList.add(result.data.canbe_used_assets.get(i));
                        }else if(result.data.canbe_used_assets.get(i).asset_type == 3){
                            packetList.add(result.data.canbe_used_assets.get(i));
                        }
                    }

                    if(serviceList.size() > 0){
                        rvOne.setVisibility(View.VISIBLE);
                        emptyOne.setVisibility(View.GONE);
                        assetCanbeUsedAdapter.addAll(serviceList);
                    }else{
                        rvOne.setVisibility(View.GONE);
                        emptyOne.setVisibility(View.VISIBLE);
                        emptyOne.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
                    }
                    if(packetList.size() > 0){
                        rvTwo.setVisibility(View.VISIBLE);
                        emptyTwo.setVisibility(View.GONE);
                        assetCanbeUsedAdapter2.addAll(packetList);
                    }else{
                        rvTwo.setVisibility(View.GONE);
                        emptyTwo.setVisibility(View.VISIBLE);
                        emptyTwo.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
                    }
                }else{
                    rvOne.setVisibility(View.GONE);
                    emptyOne.setVisibility(View.VISIBLE);
                    rvTwo.setVisibility(View.GONE);
                    emptyTwo.setVisibility(View.VISIBLE);
                    emptyOne.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
                    emptyTwo.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
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

    private void initViewDatas(ResultMemberBean result) {
        if (null != memberBean) {
            if (!TextUtils.isEmpty(memberBean.memberAvatar)) {
                if (memberBean.memberAvatar.startsWith(getString(R.string.HTTP))) {
                    GlideHelper.displayImage(mContext, memberBean.memberAvatar, ivAvatar, memberBean.member_sex.resId);
                } else {
                    GlideHelper.displayImage(mContext, getString(R.string.image_url_prefix) + memberBean.memberAvatar, ivAvatar, memberBean.member_sex.resId);
                }
            } else {
                GlideHelper.displayImage(mContext, memberBean.member_sex.resId, ivAvatar);
            }
        }
        TextViewUtils.setTextOrEmpty(tvName, result.memberName);
        TextViewUtils.setTextOrEmpty(tvStore, "门店:" + result.member_in_sp_name);
        if (TextUtils.isEmpty(result.memberLevelName)) {
            tvPosition.setVisibility(View.GONE);
        } else {
            tvPosition.setVisibility(View.VISIBLE);
            tvPosition.setText("(" + result.memberLevelName + ")");
        }
        TextViewUtils.setTextOrEmpty(tvStoredValue, "" + StringFormatUtils.formatDecimal(result.memberStoredValue));
        TextViewUtils.setTextOrEmpty(tvArrears, "" + StringFormatUtils.formatDecimal(result.memberArrears));
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.rb_one:
                rlOne.setVisibility(View.VISIBLE);
                rlTwo.setVisibility(View.GONE);
                break;
            case R.id.rb_two:
                rlOne.setVisibility(View.GONE);
                rlTwo.setVisibility(View.VISIBLE);
                break;
            default:
        }
    }

    @OnClick({R.id.tv_buy, R.id.tv_charge})
    public void onClickItem(View view) {
        switch (view.getId()) {
            case R.id.tv_buy:
                startActivity(SelectMulProjectActivity.startSelectMulProjectActivity(mContext, memberBean));
                break;
            case R.id.tv_charge:
                startActivity(CashierChargeActivity.startCashierChargeActivity(mContext, memberBean));
                break;
            default:
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if (notifyEvent.tag == NotifyEvent.FINISH_COMMISION_STAFF) {

        }else if(notifyEvent.tag == NotifyEvent.FINISH_CASHIER){
            finish();
        }
    }


}

package cn.yuyun.yymy.ui.home.bill;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import butterknife.ButterKnife;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.ResultBillManage;
import cn.yuyun.yymy.http.result.ResultBillManagerStorevalueDetail;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.utils.GlideHelper;
import de.hdodenhof.circleimageview.CircleImageView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc 单据管理储值详情
 * @date
 */
public class BillManageStorevalueSystemDetailActivity extends BaseActivity {


    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_vip)
    TextView tvVip;
    @BindView(R.id.tv_store)
    TextView tvStore;
    @BindView(R.id.tv_createTime)
    TextView tvCreateTime;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.iv_storedvalue)
    ImageView ivStoredvalue;
    @BindView(R.id.expand_storedvalue)
    LinearLayout expandStoredvalue;
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
    @BindView(R.id.ll_storedvalue)
    LinearLayout llStoredvalue;

    private ResultBillManage resultBillManage;

    public static Intent startBillManageStorevalueSystemDetailActivity(Context context, ResultBillManage bean) {
        return new Intent(context, BillManageStorevalueSystemDetailActivity.class).putExtra(EXTRA_DATA, bean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_manage_storevalue_system_detail);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    private void initViews() {
        titleBar.setTilte("详单明细");
    }

    private void initDatas() {
        resultBillManage = (ResultBillManage) getIntent().getSerializableExtra(EXTRA_DATA);
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
            TextViewUtils.setTextOrEmpty(tvVip, resultBillManage.member_level_name);
        }
        staffMgrRecordStorevalueDetail();
    }

    private void staffMgrRecordStorevalueDetail() {

        AppModelFactory.model().staffMgrRecordStorevalueDetail(LoginInfoPrefs.getInstance(mContext).getGroupId(), resultBillManage.record_id, new ProgressSubscriber<DataBean<ResultBillManagerStorevalueDetail>>(mContext, false) {

            @Override
            public void onNext(DataBean<ResultBillManagerStorevalueDetail> result) {
                if (null != result) {
                    if (null != result.data) {

                        ResultBillManagerStorevalueDetail bean = result.data;
                        TextViewUtils.setTextOrEmpty(tvOne, "消费门店:" + bean.member_in_sp_name);
                        TextViewUtils.setTextOrEmpty(tvTwo, "储值金额:" + StringFormatUtils.formatTwoDecimal(bean.current - bean.previous));
                        TextViewUtils.setTextOrEmpty(tvThree, "单据类型:" + bean.related_consumption_type_desc);
                        TextViewUtils.setTextOrEmpty(tvFour, "消费时间:" + bean.create_time);
                        TextViewUtils.setTextOrEmpty(tvFive, "服务人员:");
                        TextViewUtils.setTextOrEmpty(tvSix, "消费单据号:" + bean.storedvalue_id);

                    }
                }
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast(ex.message);
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(getString(R.string.error_no_network));
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });

    }

}

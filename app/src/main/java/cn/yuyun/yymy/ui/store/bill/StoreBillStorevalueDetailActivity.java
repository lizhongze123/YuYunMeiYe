package cn.yuyun.yymy.ui.store.bill;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lzz.utils.LogUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.ResultBillManage;
import cn.yuyun.yymy.http.result.ResultBillManagerStorevalueDetail;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.utils.GlideHelper;
import de.hdodenhof.circleimageview.CircleImageView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;

/**
 * @author
 * @desc 单据管理消费详情
 * @date
 */
public class StoreBillStorevalueDetailActivity extends BaseActivity {

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
    @BindView(R.id.tv_seven)
    TextView tvSeven;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.ll_storedvalue)
    LinearLayout llStoredvalue;

    private String groupId;

    public static Intent startStoreBillStorevalueDetailActivity(Context context, ResultBillManage bean, String groupId) {
        return new Intent(context, StoreBillStorevalueDetailActivity.class).putExtra(EXTRA_DATA, bean).putExtra(EXTRA_DATA2, groupId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_bill_storevalue_detail);
        ButterKnife.bind(this);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    private void initViews() {
        titleBar.setTilte("储值单据详情");
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

        staffMgrRecordStorevalueDetail();

    }

    private void staffMgrRecordStorevalueDetail() {

        AppModelFactory.model().staffMgrRecordStorevalueDetail(groupId, resultBillManage.storedvalueId, new ProgressSubscriber<DataBean<ResultBillManagerStorevalueDetail>>(mContext) {

            @Override
            public void onNext(DataBean<ResultBillManagerStorevalueDetail> result) {
                if (null != result) {
                    if (null != result.data) {
                        ResultBillManagerStorevalueDetail bean = result.data;
                        TextViewUtils.setTextOrEmpty(tvOne, "消费门店:" + bean.cashier_sp_name);
                        TextViewUtils.setTextOrEmpty(tvTwo, "储值金额:"  + StringFormatUtils.formatTwoDecimal(bean.current));
                        TextViewUtils.setTextOrEmpty(tvThree, "单据类型:" + bean.related_consumption_type_desc);
                        TextViewUtils.setTextOrEmpty(tvFour, "消费时间:" + bean.consume_time);
                        TextViewUtils.setTextOrEmpty(tvFive, "服务人员:"  + bean.create_person_desc);
                        TextViewUtils.setTextOrEmpty(tvSeven, "消费单据号:"  + bean.storedvalue_id);
                    }
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                LogUtils.e(ex.getMessage());
                showToast(ex.getMessage());
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });

    }

}

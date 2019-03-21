package cn.yuyun.yymy.ui.store;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;

import butterknife.BindView;
import cn.lzz.utils.LogUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.lzz.utils.ToastUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.app.MyApplication;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.PersonTimeListBean;
import cn.yuyun.yymy.http.result.ResultBillManage;
import cn.yuyun.yymy.http.result.ResultBillManagerStorevalueDetail;
import cn.yuyun.yymy.http.result.ResultBillManagerTypeDetail;
import cn.yuyun.yymy.sp.LoginInfoPrefs;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;

/**
 * @author
 * @desc
 * @date
 */
public class BillStorevalueDetailActivity extends BaseActivity {

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
    private PersonTimeListBean bean;
    private String groupId;

    public static Intent startBillStorevalueDetailActivity(Context context, PersonTimeListBean bean, String groupId) {
        return new Intent(context, BillStorevalueDetailActivity.class).putExtra(EXTRA_DATA, bean).putExtra(EXTRA_DATA2, groupId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_storevalue_detail);
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

    private void initDatas() {
        bean = (PersonTimeListBean) getIntent().getSerializableExtra(EXTRA_DATA);
        groupId = getIntent().getStringExtra(EXTRA_DATA2);
        staffMgrRecordStorevalueDetail();
    }

    private void staffMgrRecordStorevalueDetail() {

        AppModelFactory.model().getBillStoredvalueDetail(groupId, bean.record_id, new ProgressSubscriber<DataBean<ResultBillManagerStorevalueDetail>>(mContext) {

            @Override
            public void onNext(DataBean<ResultBillManagerStorevalueDetail> result) {
                if (null != result && null != result.data) {
                    TextViewUtils.setTextOrEmpty(tvOne, "消费门店:" + result.data.cashier_sp_name);
                    TextViewUtils.setTextOrEmpty(tvTwo, "储值金额:" + StringFormatUtils.formatTwoDecimal(result.data.current - result.data.previous));
                    TextViewUtils.setTextOrEmpty(tvThree, "单据类型:" + result.data.related_consumption_type_desc);
                    TextViewUtils.setTextOrEmpty(tvFour, "消费时间:" + result.data.create_time);
                    TextViewUtils.setTextOrEmpty(tvFive, "服务人员:");
                    TextViewUtils.setTextOrEmpty(tvSix, "消费单据号:" + result.data.storedvalue_id);
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

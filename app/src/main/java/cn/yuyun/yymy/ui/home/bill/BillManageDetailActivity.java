package cn.yuyun.yymy.ui.home.bill;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.LogUtils;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.TextViewUtils;
import cn.lzz.utils.ToastUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.app.MyApplication;
import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestCertificate;
import cn.yuyun.yymy.http.result.ResultBillManage;
import cn.yuyun.yymy.http.result.ResultBillManagerTypeDetail;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.utils.MediaManager;
import cn.yuyun.yymy.view.WarnningDialog;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc 单据管理详情
 * @date
 */
public class BillManageDetailActivity extends BaseNoTitleActivity {

    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_createTime)
    TextView tvCreateTime;
    @BindView(R.id.tv_store)
    TextView tvStore;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_vip)
    TextView tvLevel;
    @BindView(R.id.btn_voice)
    AudioRecordButton btnVoice;
    @BindView(R.id.ll_play)
    LinearLayout llPlay;
    @BindView(R.id.tv_second)
    TextView tvSecond;
    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.ll2)
    LinearLayout ll2;
    @BindView(R.id.ll1)
    LinearLayout ll1;

    @BindView(R.id.iv_sign)
    ImageView ivSign;

    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.tv_del)
    TextView tvDel;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.iv_edit)
    ImageView ivEdit;

    private int REQUEST_SIGN = 2001;
    private int REQUEST_REMARK = 2002;

    private String recordId;

    @BindView(R.id.rv_refund)
    RecyclerView rvRefund;
    @BindView(R.id.rv_give)
    RecyclerView rvGive;
    @BindView(R.id.rv_consume)
    RecyclerView rvConsume;
    @BindView(R.id.rv_buy)
    RecyclerView rvBuy;
    @BindView(R.id.rv_storedvalue)
    RecyclerView rvStoredvalue;
    @BindView(R.id.rv_repayment)
    RecyclerView rvRepayment;

    @BindView(R.id.ll_refund)
    LinearLayout llRefund;
    @BindView(R.id.ll_give)
    LinearLayout llGive;
    @BindView(R.id.ll_consume)
    LinearLayout llConsume;
    @BindView(R.id.ll_buy)
    LinearLayout llBuy;
    @BindView(R.id.ll_storedvalue)
    LinearLayout llStoredvalue;
    @BindView(R.id.ll_repayment)
    LinearLayout llRepayment;

    @BindView(R.id.iv_refund)
    ImageView ivRefund;
    @BindView(R.id.iv_give)
    ImageView ivGive;
    @BindView(R.id.iv_consume)
    ImageView ivConsume;
    @BindView(R.id.iv_buy)
    ImageView ivBuy;
    @BindView(R.id.iv_storedvalue)
    ImageView ivStoredvalue;
    @BindView(R.id.iv_repayment)
    ImageView ivRepayment;
    @BindView(R.id.ll_remark)
    LinearLayout llRemark;
    @BindView(R.id.tv_reVoice)
    TextView tvReVoice;
    @BindView(R.id.tv_none)
    TextView tvNone;
    @BindView(R.id.rl_sign)
    RelativeLayout rlSign;

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

    public static Intent startBillManageDetailActivity(Context context, String recordId) {
        return new Intent(context, BillManageDetailActivity.class).putExtra(EXTRA_DATA, recordId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_manage_detail);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    private void setClickable(boolean isClickable) {
        llRemark.setClickable(isClickable);
        rlSign.setClickable(isClickable);
        tvDel.setClickable(isClickable);
        if (isClickable) {
            tvNone.setVisibility(View.GONE);
            if (TextUtils.isEmpty(audioData)) {
                btnVoice.setVisibility(View.VISIBLE);
            } else {
                btnVoice.setVisibility(View.GONE);
                tvNone.setVisibility(View.GONE);
            }
        } else {
            btnVoice.setVisibility(View.GONE);
            tvReVoice.setVisibility(View.GONE);
            if (TextUtils.isEmpty(audioData)) {
                tvNone.setVisibility(View.VISIBLE);
            } else {
                tvNone.setVisibility(View.GONE);
            }
        }
    }

    private void initViews() {
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSubmit.setVisibility(View.VISIBLE);
                ivEdit.setVisibility(View.GONE);
                setClickable(true);
                if (!TextUtils.isEmpty(autographData)) {
                    if (tvSubmit.getVisibility() == View.VISIBLE) {
                        tvDel.setVisibility(View.VISIBLE);
                    } else {
                        tvDel.setVisibility(View.GONE);
                    }
                }
                if (!TextUtils.isEmpty(audioData)) {
                    tvReVoice.setVisibility(View.VISIBLE);
                } else {
                    tvReVoice.setVisibility(View.GONE);
                }
            }
        });
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        rlSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(SignPaintActivity.startSignPaintActivity(mContext), REQUEST_SIGN);
            }
        });
        tvReVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llPlay.setVisibility(View.GONE);
                btnVoice.setVisibility(View.VISIBLE);
            }
        });
        llRemark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(BillRemarkActivity.startBillRemarkActivity(mContext, tvRemark.getText().toString()), REQUEST_REMARK);
            }
        });
        tvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDel.setVisibility(View.GONE);
                ivSign.setVisibility(View.GONE);
                autographData = "";
            }
        });
        ((RadioButton) (findViewById(R.id.rb1))).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ll1.setVisibility(View.VISIBLE);
                    ll2.setVisibility(View.GONE);
                    ivEdit.setVisibility(View.GONE);
                }
            }
        });
        ((RadioButton) (findViewById(R.id.rb2))).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ll1.setVisibility(View.GONE);
                    ll2.setVisibility(View.VISIBLE);
                    ivEdit.setVisibility(View.VISIBLE);
                }
            }
        });

        btnVoice.setAudioFinishRecorderListener(new AudioRecordButton.AudioFinishRecorderListener() {
            @Override
            public void onFinished(float seconds, String filePath) {
                uploadAudio(filePath);
            }
        });

        warnningDialog = new WarnningDialog(mContext, "您修改的信息暂未提交，退出将不保存，是否继续退出？");
        warnningDialog.setOnPositiveListener(new WarnningDialog.OnDialogListener() {
            @Override
            public void onPositive() {
                finish();
            }

            @Override
            public void onNegative() {

            }
        });
        setClickable(false);
        initListView();
    }

    private void initDatas() {
        recordId = getIntent().getStringExtra(EXTRA_DATA);
        staffMgrRecordDetail();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == RESULT_OK) {
            if (REQUEST_SIGN == requestCode) {
                tvDel.setVisibility(View.VISIBLE);
                ivSign.setVisibility(View.VISIBLE);
                autographData = data.getStringExtra(EXTRA_DATA);
                if (autographData.startsWith(getString(R.string.HTTP))) {
                    GlideHelper.displayImage(mContext, autographData, ivSign);
                } else {
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + autographData, ivSign);
                }
            } else if (REQUEST_REMARK == requestCode) {
                tvRemark.setText(data.getStringExtra(EXTRA_DATA));
                remarkData = data.getStringExtra(EXTRA_DATA);
            }
        }
    }

    private String audioData;
    private String autographData;
    private String remarkData;

    private String audioTemp = "";
    private String autographTemp = "";
    private String remarkTemp = "";

    private WarnningDialog warnningDialog;

    private void uploadAudio(final String path) {
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            AppModelFactory.model().uploadAudio(file, new ProgressSubscriber<DataBean<String>>(this) {
                @Override
                public void onNext(DataBean<String> o) {
                    if (null != o.data) {
                        audioData = o.data;
                        llPlay.setVisibility(View.VISIBLE);
                        btnVoice.setVisibility(View.GONE);
                        tvNone.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onError(ApiException ex) {
                    super.onError(ex);
                    ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), ex.getMessage());
                }
            });
        }

    }

    private void submit() {
        RequestCertificate body = new RequestCertificate();
        if (!TextUtils.isEmpty(audioData)) {
            body.audio = audioData;
        } else {
            body.audio = "";
        }
        if (!TextUtils.isEmpty(autographData)) {
            body.autograph = autographData;
        } else {
            body.autograph = "";
        }
        if (TextUtils.isEmpty(tvRemark.getText().toString())) {
            body.description = "";
        } else {
            body.description = tvRemark.getText().toString();
        }
        body.record_id = recordId;
        body.record_type = 2;
        AppModelFactory.model().postCertificate(body, new ProgressSubscriber<Object>(mContext) {

            @Override
            public void onNext(Object result) {
                showToast("提交成功");
                staffMgrRecordDetail();
                ivEdit.setVisibility(View.VISIBLE);
                tvSubmit.setVisibility(View.GONE);
                setClickable(false);
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), ex.message);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });
    }

    private ResultBillManagerTypeDetail resultBillManagerTypeDetail;

    private void staffMgrRecordDetail() {

        AppModelFactory.model().staffMgrRecordConsumeDetail(LoginInfoPrefs.getInstance(mContext).getGroupId(), recordId, new ProgressSubscriber<DataBean<ResultBillManagerTypeDetail>>(mContext, false) {

            @Override
            public void onNext(DataBean<ResultBillManagerTypeDetail> result) {
                if (null != result) {
                    if (null != result.data) {
                        if(null != result.data.record_info){
                            if (!TextUtils.isEmpty(result.data.record_info.member_avatar)) {
                                if (result.data.record_info.member_avatar.startsWith(mContext.getString(R.string.HTTP))) {
                                    GlideHelper.displayImage(mContext, result.data.record_info.member_avatar, ivAvatar);
                                } else {
                                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + result.data.record_info.member_avatar, ivAvatar);
                                }
                            } else {
                                GlideHelper.displayImage(mContext, R.drawable.avatar_default_female, ivAvatar);
                            }
                            TextViewUtils.setTextOrEmpty(tvName, result.data.record_info.member_name);
                            TextViewUtils.setTextOrEmpty(tvStore, result.data.record_info.member_in_sp_name);
                            TextViewUtils.setTextOrEmpty(tvCreateTime, DateTimeUtils.StringToDate(result.data.record_info.create_time, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_DATE_UI_TWO));
                            if (TextUtils.isEmpty(result.data.record_info.member_level_name)) {
                                tvLevel.setVisibility(View.GONE);
                            } else {
                                tvLevel.setVisibility(View.VISIBLE);
                                TextViewUtils.setTextOrEmpty(tvLevel, "(" + result.data.record_info.member_level_name + ")");
                            }
                        }

                        resultBillManagerTypeDetail = result.data;
                        setListData(result.data);
                        setCertificateData(result.data);
                    }
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                LogUtils.e(ex.getMessage());
                ToastUtils.toastInBottom(mContext, ex.getMessage());
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });
    }

    private void setCertificateData(ResultBillManagerTypeDetail result) {
        if (null != result.record_info) {
            if (null != result.record_info.consumptionCertificateRspList && result.record_info.consumptionCertificateRspList.size() > 0) {
                ResultBillManagerTypeDetail.RecordInfoBean.ConsumptionCertificateRspListBean bean = result.record_info.consumptionCertificateRspList.get(0);
                TextViewUtils.setTextOrEmpty(tvRemark, bean.description);
                if (!TextUtils.isEmpty(bean.autograph)) {
                    if (tvSubmit.getVisibility() == View.VISIBLE) {
                        tvDel.setVisibility(View.VISIBLE);
                    } else {
                        tvDel.setVisibility(View.GONE);
                    }
                    autographData = bean.autograph;
                    autographTemp = bean.autograph;

                    if (bean.autograph.startsWith(mContext.getString(R.string.HTTP))) {
                        GlideHelper.displayImage(mContext, bean.autograph, ivSign, R.drawable.timeline_image_failure);
                    } else {
                        GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.autograph, ivSign, R.drawable.timeline_image_failure);
                    }
                } else {
                    ivSign.setVisibility(View.VISIBLE);
                    GlideHelper.displayImage(mContext, R.drawable.sign_default2, ivSign);
                }
                if (TextUtils.isEmpty(bean.audio)) {
                    llPlay.setVisibility(View.GONE);
                    btnVoice.setVisibility(View.GONE);
                    tvNone.setVisibility(View.VISIBLE);
                } else {
                    audioData = bean.audio;
                    audioTemp = bean.audio;
                    llPlay.setVisibility(View.VISIBLE);
                    btnVoice.setVisibility(View.GONE);
                    tvNone.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(bean.description)) {
                    tvRemark.setText(bean.description);
                    remarkData = bean.description;
                    remarkTemp = bean.description;
                }
            }
        }
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

    class MyTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    }

    @OnClick(R.id.btn_play)
    public void play(View v) {
        if (!TextUtils.isEmpty(audioData)) {
            if (audioData.startsWith(getString(R.string.HTTP))) {
                MediaManager.play(audioData);
            } else {
                MediaManager.play(getString(R.string.audio_url_prefix) + audioData);
            }
        } else if (null != resultBillManagerTypeDetail.record_info) {
            if (null != resultBillManagerTypeDetail.record_info.consumptionCertificateRspList && resultBillManagerTypeDetail.record_info.consumptionCertificateRspList.size() > 0) {
                if (audioData.startsWith(getString(R.string.HTTP))) {
                    MediaManager.play(resultBillManagerTypeDetail.record_info.consumptionCertificateRspList.get(0).audio);
                } else {
                    MediaManager.play(getString(R.string.audio_url_prefix) + resultBillManagerTypeDetail.record_info.consumptionCertificateRspList.get(0).audio);
                }
            }
        }

    }

    @Override
    protected void onPause() {
        //保证在退出该页面时，终止语音播放
        MediaManager.release();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (!TextUtils.isEmpty(autographData)) {
            if (!autographTemp.equals(autographData)) {
                warnningDialog.show();
                return;
            }
        }
        if (!TextUtils.isEmpty(audioData)) {
            if (!audioTemp.equals(audioData)) {
                warnningDialog.show();
                return;
            }
        }
        if (!TextUtils.isEmpty(remarkData)) {
            if (!remarkTemp.equals(remarkData)) {
                warnningDialog.show();
                return;
            }
        }
        if (null != warnningDialog && warnningDialog.isShowing()) {
            warnningDialog.dismiss();
        }
        super.onBackPressed();
    }
}

package cn.yuyun.yymy.ui.home.bill;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.LogUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.lzz.utils.ToastUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.app.MyApplication;
import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestCertificate;
import cn.yuyun.yymy.http.result.ResultBillManage;
import cn.yuyun.yymy.http.result.ResultBillManagerStorevalueDetail;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.utils.MediaManager;
import cn.yuyun.yymy.view.WarnningDialog;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc 单据管理储值详情
 * @date
 */
public class BillManageStorevalueDetailActivity extends BaseNoTitleActivity {

    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
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
    @BindView(R.id.rb1)
    RadioButton rb1;
    @BindView(R.id.rb2)
    RadioButton rb2;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
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
    @BindView(R.id.ll1)
    LinearLayout ll1;
    @BindView(R.id.iv_sign)
    ImageView ivSign;
    @BindView(R.id.tv_del)
    TextView tvDel;
    @BindView(R.id.rl_sign)
    RelativeLayout rlSign;
    @BindView(R.id.btn_voice)
    AudioRecordButton btnVoice;
    @BindView(R.id.btn_play)
    Button btnPlay;
    @BindView(R.id.tv_reVoice)
    TextView tvReVoice;
    @BindView(R.id.ll_play)
    LinearLayout llPlay;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.ll_remark)
    LinearLayout llRemark;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.ll2)
    LinearLayout ll2;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.tv_none)
    TextView tvNone;
    @BindView(R.id.iv_edit)
    ImageView ivEdit;

    private int REQUEST_SIGN = 2001;
    private int REQUEST_REMARK = 2002;

    private String recordId;

    public static Intent startBillManageDetailActivity(Context context, String recordId) {
        return new Intent(context, BillManageStorevalueDetailActivity.class).putExtra(EXTRA_DATA, recordId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_manage_storevalue_detail);
        ButterKnife.bind(this);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    private void setClickable(boolean isClickable){
        llRemark.setClickable(isClickable);
        rlSign.setClickable(isClickable);
        tvDel.setClickable(isClickable);
        if(isClickable){
            tvNone.setVisibility(View.GONE);
            if(TextUtils.isEmpty(audioData)){
                btnVoice.setVisibility(View.VISIBLE);
            }else{
                btnVoice.setVisibility(View.GONE);
                tvNone.setVisibility(View.GONE);
            }
        }else{
            btnVoice.setVisibility(View.GONE);
            if(TextUtils.isEmpty(audioData)){
                tvNone.setVisibility(View.VISIBLE);
            }else{
                tvNone.setVisibility(View.GONE);
            }
        }
    }

    private void initViews() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
                if(!TextUtils.isEmpty(autographData)){
                    if(tvSubmit.getVisibility() == View.VISIBLE){
                        tvDel.setVisibility(View.VISIBLE);
                    }else{
                        tvDel.setVisibility(View.GONE);
                    }
                }
                if(!TextUtils.isEmpty(audioData)){
                    tvReVoice.setVisibility(View.VISIBLE);
                }else{
                    tvReVoice.setVisibility(View.GONE);
                }
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
        rb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ll1.setVisibility(View.VISIBLE);
                    ll2.setVisibility(View.GONE);
                    ivEdit.setVisibility(View.GONE);
                }
            }
        });
        rb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
    }

    private void initDatas() {
        recordId = getIntent().getStringExtra(EXTRA_DATA);
      /*  resultBillManage = (ResultBillManage) getIntent().getSerializableExtra(EXTRA_DATA);
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
        TextViewUtils.setTextOrEmpty(tvCreateTime, DateTimeUtils.StringToDate(resultBillManage.create_time, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_DATE_UI_TWO));
        if (TextUtils.isEmpty(resultBillManage.member_level_name)) {
            tvVip.setVisibility(View.GONE);
        } else {
            tvVip.setVisibility(View.VISIBLE);
            TextViewUtils.setTextOrEmpty(tvVip, resultBillManage.member_level_name);
        }*/
        staffMgrRecordStorevalueDetail();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == RESULT_OK) {
            if (REQUEST_SIGN == requestCode) {
                tvDel.setVisibility(View.VISIBLE);
                ivSign.setVisibility(View.VISIBLE);
                autographData = data.getStringExtra(EXTRA_DATA);
                if(autographData.startsWith(getString(R.string.HTTP))){
                    GlideHelper.displayImage(mContext, autographData, ivSign);
                }else{
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

    private ResultBillManagerStorevalueDetail resultBillManagerTypeDetail;

    private void uploadAudio(final String path) {
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            AppModelFactory.model().uploadAudio(file, new ProgressSubscriber<DataBean<String>>(this) {
                @Override
                public void onNext(DataBean<String> o) {
                    if(null != o.data){
                        audioData = o.data;
                        llPlay.setVisibility(View.VISIBLE);
                        btnVoice.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onError(ApiException ex) {
                    super.onError(ex);
                    ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), ex.message);
                }
            });
        }

    }

    private void submit() {
        RequestCertificate body = new RequestCertificate();
        if(!TextUtils.isEmpty(audioData)){
            body.audio = audioData;
        }else{
            body.audio = "";
        }
        if(!TextUtils.isEmpty(autographData)){
            body.autograph = autographData;
        }else{
            body.autograph = "";
        }
        if(TextUtils.isEmpty(tvRemark.getText().toString())){
            body.description = "";
        }else{
            body.description = tvRemark.getText().toString();
        }
        body.record_id = recordId;
        body.record_type = 1;
        AppModelFactory.model().postCertificate(body, new ProgressSubscriber<Object>(mContext) {

            @Override
            public void onNext(Object result) {
                showToast("提交成功");
                staffMgrRecordStorevalueDetail();
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

    private void staffMgrRecordStorevalueDetail() {

        AppModelFactory.model().staffMgrRecordStorevalueDetail(LoginInfoPrefs.getInstance(mContext).getGroupId(), recordId, new ProgressSubscriber<DataBean<ResultBillManagerStorevalueDetail>>(mContext, false) {

            @Override
            public void onNext(DataBean<ResultBillManagerStorevalueDetail> result) {
                if (null != result) {
                    if (null != result.data) {


                            if (!TextUtils.isEmpty(result.data.member_avatar)) {
                                if (result.data.member_avatar.startsWith(mContext.getString(R.string.HTTP))) {
                                    GlideHelper.displayImage(mContext, result.data.member_avatar, ivAvatar);
                                } else {
                                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + result.data.member_avatar, ivAvatar);
                                }
                            } else {
                                GlideHelper.displayImage(mContext, R.drawable.avatar_default_female, ivAvatar);
                            }
                            TextViewUtils.setTextOrEmpty(tvName, result.data.member_name);
                            TextViewUtils.setTextOrEmpty(tvStore, result.data.member_in_sp_name);
                            TextViewUtils.setTextOrEmpty(tvCreateTime, DateTimeUtils.StringToDate(result.data.create_time, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_DATE_UI_TWO));
                            if (TextUtils.isEmpty(result.data.member_level_name)) {
                                tvVip.setVisibility(View.GONE);
                            } else {
                                tvVip.setVisibility(View.VISIBLE);
                                TextViewUtils.setTextOrEmpty(tvVip, "(" + result.data.member_level_name + ")");
                            }


                        resultBillManagerTypeDetail = result.data;

                        ResultBillManagerStorevalueDetail bean = result.data;
                        TextViewUtils.setTextOrEmpty(tvOne, "消费门店：" + bean.member_in_sp_name);
                        TextViewUtils.setTextOrEmpty(tvTwo, "储值金额：" + StringFormatUtils.formatTwoDecimal(bean.current - bean.previous));
                        TextViewUtils.setTextOrEmpty(tvThree, "单据类型：" + bean.related_consumption_type_desc);
                        TextViewUtils.setTextOrEmpty(tvFour, "消费时间：" + DateTimeUtils.StringToDate(bean.create_time, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_DATETIME_TWO));
                        TextViewUtils.setTextOrEmpty(tvFive, "服务人员：");
                        TextViewUtils.setTextOrEmpty(tvSix, "消费单据号：" + bean.storedvalue_id);

                        if (null != bean.consumptionCertificateRspList && bean.consumptionCertificateRspList.size() != 0) {
                            setCertificateData(bean.consumptionCertificateRspList.get(0));
                        }
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

    private void setCertificateData(ResultBillManagerStorevalueDetail.ConsumptionCertificateRspListBean result) {
        if (!TextUtils.isEmpty(result.autograph)) {
            if(tvSubmit.getVisibility() == View.VISIBLE){
                tvDel.setVisibility(View.VISIBLE);
            }else{
                tvDel.setVisibility(View.GONE);
            }
            autographData = result.autograph;
            autographTemp = result.autograph;
            if (result.autograph.startsWith(mContext.getString(R.string.HTTP))) {
                GlideHelper.displayImage(mContext, result.autograph, ivSign, R.drawable.timeline_image_failure);
            } else {
                GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + result.autograph, ivSign, R.drawable.timeline_image_failure);
            }
        }else{
            ivSign.setVisibility(View.VISIBLE);
            GlideHelper.displayImage(mContext, R.drawable.sign_default2, ivSign);
        }
        if(TextUtils.isEmpty(result.audio)){
            llPlay.setVisibility(View.GONE);
            btnVoice.setVisibility(View.GONE);
            tvNone.setVisibility(View.VISIBLE);
        }else{
            audioData = result.audio;
            audioTemp = result.audio;
            llPlay.setVisibility(View.VISIBLE);
            btnVoice.setVisibility(View.GONE);
            tvNone.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(result.description)){
            tvRemark.setText(result.description);
            remarkData = result.description;
            remarkTemp = result.description;
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
        if(!TextUtils.isEmpty(audioData)){
            if (audioData.startsWith(getString(R.string.HTTP))) {
                MediaManager.play(audioData);
            }else{
                MediaManager.play(getString(R.string.audio_url_prefix) + audioData);
            }
        } else if(null != resultBillManagerTypeDetail.consumptionCertificateRspList){
            if(resultBillManagerTypeDetail.consumptionCertificateRspList.size() != 0){
                if (audioData.startsWith(getString(R.string.HTTP))) {
                    MediaManager.play(resultBillManagerTypeDetail.consumptionCertificateRspList.get(0).audio);
                }else{
                    MediaManager.play(getString(R.string.audio_url_prefix) + resultBillManagerTypeDetail.consumptionCertificateRspList.get(0).audio);
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
        if(!TextUtils.isEmpty(autographData)){
            if(!autographTemp.equals(autographData)){
                warnningDialog.show();
                return;
            }
        }
        if(!TextUtils.isEmpty(audioData)){
            if(!audioTemp.equals(audioData)){
                warnningDialog.show();
                return;
            }
        }
        if(!TextUtils.isEmpty(remarkData)){
            if(!remarkTemp.equals(remarkData)){
                warnningDialog.show();
                return;
            }
        }
        super.onBackPressed();
    }
}

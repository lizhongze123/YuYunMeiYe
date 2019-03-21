package cn.yuyun.yymy.ui.home.cashier;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.DeviceUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.lzz.utils.VersionUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.bean.RecordType;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestCashier;
import cn.yuyun.yymy.http.request.RequestCashier.ListCbConsumeQoBean;
import cn.yuyun.yymy.http.request.RequestCashier.StaffPreSaleServiceRecordQoListBean;
import cn.yuyun.yymy.http.request.RequestCashierNotify;
import cn.yuyun.yymy.http.result.ResultCanbeUsedAssest;
import cn.yuyun.yymy.http.result.ResultCashier;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;
import cn.yuyun.yymy.http.result.ResultListStaff;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.http.result.ResultProject;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.ui.home.bill.SignPaintActivity;
import cn.yuyun.yymy.ui.store.staff.MultipleChoiceStaffActivity;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.ToggleButton;
import cn.yuyun.yymy.view.dialog.CashierFinishDialog;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;

/**
 * @author
 * @desc
 * @date
 */

public class CashierConsumeActivity extends BaseActivity {

    @BindView(R.id.tv_toggleDesc)
    TextView tvToggleDesc;
    @BindView(R.id.toggleButton)
    ToggleButton toggleButton;
    @BindView(R.id.tv_createTime)
    TextView tvCreateTime;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_guidePrice)
    TextView tvGuidePrice;
    @BindView(R.id.tv_numCount)
    TextView tvNumCount;
    @BindView(R.id.tv_canbeUsed)
    TextView tvCanbeUsed;
    @BindView(R.id.tv_subtract)
    TextView tvSubtract;
    @BindView(R.id.et_total)
    EditText etTotal;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.et_timesLong)
    EditText etTimesLong;
    @BindView(R.id.tv_staff)
    TextView tvStaff;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.rl_select)
    RelativeLayout rlSelect;
    @BindView(R.id.tv_amountDesc)
    TextView tvAmountDesc;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_nextStep)
    TextView tvNextStep;
    private List<ResultListStaff> staffIntentList = new ArrayList<>();
    private ResultMemberBean memberBean;
    private TimePickerView timePickerView;
    private ResultCanbeUsedAssest.ResultCanbeUsedAssestBean projectBean;
    private RequestCashier body = new RequestCashier();
    private int numCount = 1;
    private ListCbConsumeQoBean consumeBean = new ListCbConsumeQoBean();

    public static Intent startCashierConsumeActivity(Context context, ResultCanbeUsedAssest.ResultCanbeUsedAssestBean bean, ResultMemberBean memberBean) {
        return new Intent(context, CashierConsumeActivity.class).putExtra(EXTRA_DATA, bean).putExtra(EXTRA_DATA2, memberBean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_consume);
        EvenManager.register(this);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    List<CashierBuyBean> list = new ArrayList<>();
    private Date consumeDate;

    private void initDatas() {
        if (null != projectBean.goodRsp) {
            if (!TextUtils.isEmpty(projectBean.goodRsp.thumb_url)) {
                if (projectBean.goodRsp.thumb_url.startsWith(mContext.getString(R.string.HTTP))) {
                    GlideHelper.displayImage(mContext, projectBean.goodRsp.thumb_url, ivAvatar, R.color.loadding_img_bg);
                } else {
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + projectBean.goodRsp.thumb_url, ivAvatar, R.color.loadding_img_bg);
                }
            } else {
                GlideHelper.displayImage(mContext, R.color.loadding_img_bg, ivAvatar);
            }
        }
        TextViewUtils.setTextOrEmpty(tvName, projectBean.asset_name);
        tvNumCount.setText("总次数:" + projectBean.num_total);
        tvCanbeUsed.setText("可用次数:" + projectBean.num_canbe_used);
        tvGuidePrice.setText("指导价:" + projectBean.transaction_price / projectBean.num_total);
        tvAmount.setText(StringFormatUtils.formatTwoDecimal(projectBean.transaction_price / projectBean.num_total));
        tvSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numCount == 1) {
                    numCount = 1;
                    etTotal.setText("1");
                } else {
                    numCount--;
                    etTotal.setText(numCount + "");
                }
                tvAmount.setText(StringFormatUtils.formatTwoDecimal(numCount * (projectBean.transaction_price / projectBean.num_total)));
            }
        });
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numCount++;
                if(numCount > projectBean.num_canbe_used){
                    showToast("耗卡次数不能大于可用次数");
                    numCount--;
                    return;
                }
                etTotal.setText(numCount + "");
                tvAmount.setText(StringFormatUtils.formatTwoDecimal(numCount * (projectBean.transaction_price / projectBean.num_total)));
            }
        });
    }

    private void initViews() {
        titleBar.setTilte("耗卡清单");
        memberBean = (ResultMemberBean) getIntent().getSerializableExtra(EXTRA_DATA2);
        projectBean = (ResultCanbeUsedAssest.ResultCanbeUsedAssestBean) getIntent().getSerializableExtra(EXTRA_DATA);
        toggleButton.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    body.consume_type = 2;
                    tvCreateTime.setVisibility(View.VISIBLE);
                    tvCreateTime.setText(DateTimeUtils.getDateTimeText(DateTimeUtils.getNowTimeStamp(), DateTimeUtils.FORMAT_DATETIME_TWO));
                } else {
                    body.consume_type = 1;
                    tvCreateTime.setVisibility(View.INVISIBLE);
                }
            }
        });
        tvCreateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerView.show();
            }
        });
        Calendar endDate = Calendar.getInstance();
        //最大日期是今天
        timePickerView = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tvCreateTime.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATETIME_TWO));
                consumeDate = date;
            }
        })
                // 默认全部显示
                .setType(new boolean[]{true, true, true, true, true, false})
                .setRangDate(null, endDate)
                .build();
        tvNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etTotal.getText().toString().trim())){
                    showToast("耗卡次数不能为0");
                    return;
                }
                int times = (int) (numCount * projectBean.transaction_price / projectBean.num_total);
                if(projectBean.amount_still_here < times){
                    showToast("本次耗卡金额大于剩余金额");
                    return;
                }
                List<RequestCashier.ListCbConsumeQoBean> consumeList = new ArrayList<>();
                consumeBean.asset_id = projectBean.asset_id;
                consumeBean.asset_type = projectBean.asset_type;
                consumeBean.consume_num_now = numCount;
                consumeBean.consume_amount_now = projectBean.transaction_price / projectBean.num_total * numCount;
                consumeBean.consume_time_now = Integer.valueOf(etTimesLong.getText().toString());
                List<StaffPreSaleServiceRecordQoListBean> commisionStaffList = new ArrayList<>();

                if (null != staffIntentList) {
                    List<RequestCashier.ListStaffpersonTimesQoBean> timesList = new ArrayList<>();
                    for (int i = 0; i < staffIntentList.size(); i++) {
                        if (staffIntentList.get(i).before == 0 && staffIntentList.get(i).sale == 0) {
                            return;
                        }else {
                            StaffPreSaleServiceRecordQoListBean staffBean = new StaffPreSaleServiceRecordQoListBean();
                            staffBean.achieve_amount = staffIntentList.get(i).sale;
                            staffBean.sale_type = RecordType.SERVICE;
                            staffBean.handmake = staffIntentList.get(i).before;
                            staffBean.staff_id = staffIntentList.get(i).staff_id;
                            staffBean.person_times = staffIntentList.get(i).person_times + "";
                            commisionStaffList.add(staffBean);
                        }
                        RequestCashier.ListStaffpersonTimesQoBean tiemsBean = new RequestCashier.ListStaffpersonTimesQoBean();
                        tiemsBean.person_times = staffIntentList.get(i).person_times;
                        tiemsBean.staff_id = staffIntentList.get(i).staff_id;
                        timesList.add(tiemsBean);
                    }
                    body.list_staffperson_timesQo = timesList;
                }
                consumeBean.staffPreSaleServiceRecordQoList = commisionStaffList;
                consumeList.add(consumeBean);
                body.list_cb_consumeQo = consumeList;
                body.member_id = memberBean.memberId;
                body.cashier_sp = memberBean.memberInSpId;
                if (toggleButton.getToggleStatu()) {
                    if(null != consumeDate){
                        body.consume_time = consumeDate.getTime() / 1000;
                    }
                } else {
                    body.consume_time = 0;
                }
                submit();
//                startActivity(SignPaintActivity.startSignPaintActivity(mContext));
            }
        });
        etTotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString().trim();
                int len = text.length();
                if (len == 0) {

                } else {
                    if (len == 1 && text.equals("0")) {
                        etTotal.removeTextChangedListener(this);
                        etTotal.setText("1");
                        numCount = 1;
                        etTotal.addTextChangedListener(this);
                    }

                    if(Integer.valueOf(etTotal.getText().toString().trim()) > projectBean.num_canbe_used){
                        showToast("耗卡次数不能大于可用次数");
                        etTotal.removeTextChangedListener(this);
                        etTotal.setText("1");
                        numCount = 1;
                        etTotal.addTextChangedListener(this);
                    }

                    numCount = Integer.valueOf(etTotal.getText().toString().trim());
                    tvAmount.setText(StringFormatUtils.formatTwoDecimal(numCount * (projectBean.transaction_price / projectBean.num_total)));

                }
            }
        });
        etTimesLong.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString().trim();
                int len = text.length();
                if (len == 0) {

                } else {
                    if (len == 1 && text.equals("0")) {
                        etTimesLong.removeTextChangedListener(this);
                        etTimesLong.setText("1");
                        etTimesLong.addTextChangedListener(this);
                    }
                }
            }
        });
        rlSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResultClassfiyStoreBean.OgServiceplacesRspListBean storeBean = new ResultClassfiyStoreBean.OgServiceplacesRspListBean();
                storeBean.sp_id = memberBean.memberInSpId;
                storeBean.group_id = LoginInfoPrefs.getInstance(mContext).getGroupId();
                if(null != staffIntentList && staffIntentList.size() > 0){
                    Intent intent = CommisionListActivity.startCommisionListActivityForConsume(mContext,staffIntentList, storeBean);
                    intent.putExtra("amount", numCount * (projectBean.transaction_price / projectBean.num_total));
                    intent.putExtra("project", projectBean);
                    intent.putExtra("count", numCount);
                    startActivity(intent);
                }else{
                    Intent intent = MultipleChoiceStaffActivity.startFromCommisionStaffSelectForConsume(mContext, storeBean);
                    intent.putExtra("amount", numCount * (projectBean.transaction_price / projectBean.num_total));
                    intent.putExtra("project", projectBean);
                    intent.putExtra("count", numCount);
                    startActivity(intent);
                }
            }
        });
    }

    private void submit() {

        StringBuilder sb = new StringBuilder();
        sb.append("---来自于手机端收银作业，Android设备机型 ");
        sb.append(DeviceUtils.getSystemModel());
        sb.append("  App版本");
        sb.append(VersionUtils.getAppVersionName(mContext));
        body.description = sb.toString();

        AppModelFactory.model().submitCashier(body, new ProgressSubscriber<DataBean<ResultCashier>>(mContext, false) {
            @Override
            public void onNext(final DataBean<ResultCashier> resultCashierDataBean) {
                CashierFinishDialog dialog = new CashierFinishDialog(mContext);
                dialog.setOnPositiveListener(new CashierFinishDialog.OnPositiveListener() {
                    @Override
                    public void onPositive(int type) {
                        send(type, resultCashierDataBean.data);
                    }

                    @Override
                    public void onNegative() {
                        startActivity(SignPaintActivity.startSignPaintActivityForCashier(mContext, resultCashierDataBean.data.record_id,resultCashierDataBean.data.record_type ));
                        finish();
                    }
                });
                dialog.show();
                dialog.setTips("耗卡完成");
            }


            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast(ex.message);
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast("网络异常，请检查网络");
            }

        });
    }

    private void send(int type, final ResultCashier resultCashier) {
        RequestCashierNotify body = new RequestCashierNotify();
        body.record_id = resultCashier.record_id;
        body.record_type = resultCashier.record_type;
        body.send_type = type;
        AppModelFactory.model().cashierNotify(body, new ProgressSubscriber<DataBean<Object>>(mContext, false) {
            @Override
            public void onNext(DataBean<Object> result) {
                showToast("推送成功");
                startActivity(SignPaintActivity.startSignPaintActivityForCashier(mContext, resultCashier.record_id, resultCashier.record_type));
                finish();
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast("推送失败");
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast("网络异常，请检查网络");
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if (notifyEvent.tag == NotifyEvent.FINISH_COMMISION_STAFF) {
            staffIntentList = (List<ResultListStaff>) notifyEvent.value;
            StringBuilder sb = new StringBuilder();
            for (ResultListStaff resultListStaff : staffIntentList) {
                sb.append(resultListStaff.staff_name + " ");
            }
            tvStaff.setText(sb.toString());
        }else if(notifyEvent.tag == NotifyEvent.FINISH_CASHIER){
            finish();
        }
    }

}

package cn.yuyun.yymy.ui.home.bill;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.http.ApiException;
import com.example.http.ProgressSubscriber;

import java.util.Date;

import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.ToastUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.app.MyApplication;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestCertificate;
import cn.yuyun.yymy.http.request.RequestCertificateNotice;
import cn.yuyun.yymy.http.result.ResultBillManage;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.utils.InputLengthFilter;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;

/**
 * @author
 * @desc
 * @date
 */
public class BillNoticekActivity extends BaseActivity {

    private EditText etContent;
    private ResultBillManage resultBillManage;
    private TimePickerView timePickerView;
    private TextView tvTime;
    private RequestCertificateNotice requestCertificateNotice = new RequestCertificateNotice();

    public static Intent startBillNoticekActivity(Context context, ResultBillManage bean) {
        return new Intent(context, BillNoticekActivity.class).putExtra(EXTRA_DATA, bean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_notice);
    }

    @Override
    protected void setUpViewAndData() {
        initViews();
    }

    private void initViews() {
        resultBillManage = (ResultBillManage) getIntent().getSerializableExtra(EXTRA_DATA);
        titleBar.setTilte("提醒设置");
        titleBar.setTvRight("提交");
        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etContent.getText().toString())) {
                    showToast("请输入内容");
                    return;
                }
                submit(etContent.getText().toString());
            }
        });

        etContent = (EditText) findViewById(R.id.et_content);
        tvTime = (TextView) findViewById(R.id.tv_time);
        findViewById(R.id.ll_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerView.show();
            }
        });

        timePickerView = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                requestCertificateNotice.notify_time = date.getTime() / 1000;
                tvTime.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATETIME));
            }
        })
                // 默认全部显示
                .setType(new boolean[]{true, true, true, true, true, false})
                .build();
    }

    private void submit(final String content) {
        requestCertificateNotice.group_id = LoginInfoPrefs.getInstance(mContext).getGroupId();
        requestCertificateNotice.record_id = resultBillManage.record_id;
        requestCertificateNotice.content = content;
        requestCertificateNotice.member_id = resultBillManage.member_id;
        requestCertificateNotice.create_person = UserfoPrefs.getInstance(mContext).getStaffName();
        requestCertificateNotice.staff_id = LoginInfoPrefs.getInstance(mContext).getStaffId();

        AppModelFactory.model().postCertificateNotice(requestCertificateNotice, new ProgressSubscriber<Object>(mContext) {

            @Override
            public void onNext(Object result) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_DATA, content);
                intent.putExtra(EXTRA_DATA2, tvTime.getText().toString());
                ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), "提交成功");
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.message);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });
    }

}

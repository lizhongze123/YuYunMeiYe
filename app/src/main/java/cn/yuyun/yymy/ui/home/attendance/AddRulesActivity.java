package cn.yuyun.yymy.ui.home.attendance;

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

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestRule;
import cn.yuyun.yymy.map.LocationBean;
import cn.yuyun.yymy.map.MapActivity;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.utils.EvenManager;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 *
 * @author
 * @date
 */

public class AddRulesActivity extends BaseActivity implements View.OnClickListener {

    TextView tvLocation, tvStartTime, tvEndTime;
    private final int REQUEST_CODE = 1113;
    /**
     * start or end
     */
    private int TIME_TYPE;
    private TimePickerView timePickerView;
    private final String TIME_FORMAT = "HH:mm";
    EditText etRule;
    private RequestRule requestBean = new RequestRule();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rules);

    }

    @Override
    protected void setUpViewAndData() {
        initViews();
    }

    private void initViews() {
        titleBar.setTilte("添加考勤规则");
        titleBar.setTvRight("保存");
        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataCheck()) {
                    submit();
                }
            }
        });
        tvLocation = (TextView) findViewById(R.id.tv_location);
        tvStartTime = (TextView) findViewById(R.id.tv_startTime);
        tvEndTime = (TextView) findViewById(R.id.tv_endTime);
        etRule = (EditText) findViewById(R.id.et_name);
        findViewById(R.id.ll_location).setOnClickListener(this);
        findViewById(R.id.ll_startTime).setOnClickListener(this);
        findViewById(R.id.ll_endTime).setOnClickListener(this);

        timePickerView = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (TIME_TYPE == 0) {
                    tvStartTime.setText(getTime(date, TIME_FORMAT));
                    requestBean.startHour = date.getHours() + "";
                    requestBean.startMin = date.getMinutes() + "";
                } else {
                    tvEndTime.setText(getTime(date, TIME_FORMAT));
                    requestBean.endHour = date.getHours() + "";
                    requestBean.endMin = date.getMinutes() + "";
                }
            }
        })
                // 默认全部显示
                .setType(new boolean[]{false, false, false, true, true, false})
                .build();

    }

    private void submit() {
        if (TextUtils.isEmpty(LoginInfoPrefs.getInstance(mContext).getStaffId())) {
            showTextDialog("该账号没有绑定员工", false);
            return;
        } else {
            requestBean.name = etRule.getText().toString();
            requestBean.effective = 200;
            AppModelFactory.model().addRules(requestBean, new ProgressSubscriber<Object>(mContext) {
                @Override
                public void onNext(Object result) {
                    showToast("添加成功");
                    finish();
                    //通知界面刷新
                    EvenManager.sendEvent(new NotifyEvent(NotifyEvent.RREFRESH));
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
    }

    private boolean dataCheck() {
        if (TextUtils.isEmpty(etRule.getText().toString())) {
            showToast("请输入规则名称");
            return false;
        }
        if (TextUtils.isEmpty(requestBean.startHour)) {
            showToast("请选择上班时间");
            return false;
        }
        if (TextUtils.isEmpty(requestBean.endHour)) {
            showToast("请选择下班时间");
            return false;
        }
        if (TextUtils.isEmpty(requestBean.place)) {
            showToast("请选择打卡位置");
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_startTime:
                TIME_TYPE = 0;
                timePickerView.show();
                break;
            case R.id.ll_endTime:
                TIME_TYPE = 1;
                timePickerView.show();
                break;
            case R.id.ll_location:
                startActivityForResult(new Intent(mContext, MapActivity.class), REQUEST_CODE);
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            if (data != null) {
                LocationBean bean = (LocationBean) data.getSerializableExtra(EXTRA_DATA);
                tvLocation.setText(bean.getContent());
                requestBean.place = bean.getContent();
                requestBean.lat = bean.getLat();
                requestBean.lng = bean.getLng();
            }
        }
    }

    private String getTime(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

}


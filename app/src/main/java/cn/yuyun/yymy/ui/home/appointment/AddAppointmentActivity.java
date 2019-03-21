package cn.yuyun.yymy.ui.home.appointment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.DeviceUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestAddAppointment;
import cn.yuyun.yymy.http.result.ResultAppointmentSetting;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.http.result.ResultProject;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.home.leave.SelectStoreListActivity;
import cn.yuyun.yymy.ui.home.leave.StoreListActivity;
import cn.yuyun.yymy.ui.home.member.storemember.StoreMemberListActivity;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.utils.GlideHelper;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc
 * @date
 */
public class AddAppointmentActivity extends BaseNoTitleActivity {

    @BindView(R.id.tv_store)
    TextView tvStore;
    @BindView(R.id.ll_selectStore)
    LinearLayout llSelectStore;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.ll_selectMember)
    LinearLayout llSelectMember;
    @BindView(R.id.tv_project)
    TextView tvProject;
    @BindView(R.id.ll_project)
    LinearLayout llProject;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.ll_date)
    LinearLayout llDate;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.ll_time)
    LinearLayout llTime;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_member)
    ImageView ivMember;
    @BindView(R.id.iv_project)
    ImageView ivProject;
    @BindView(R.id.iv_date)
    ImageView ivDate;
    @BindView(R.id.iv_time)
    ImageView ivTime;
    private final int REQUEST_CODE = 1114;
    private final int REQUEST_PROJECT = 1115;
    private final int REQUEST_TIME = 1116;
    private RequestAddAppointment requestBean;

    private OptionsPickerView<String> timePickerView;
    private OptionsPickerView<String> datePickerView;

    private List<ResultProject> mIntentResult;

    public static Intent startAddAppointmentActivity(Context context, Bundle bundle) {
        return new Intent(context, AddAppointmentActivity.class).putExtra(EXTRA_DATA, bundle);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EvenManager.register(this);
        setContentView(R.layout.activity_add_appointment);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        getSetting();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if (notifyEvent.tag == NotifyEvent.DATA) {
            ResultMemberBean bean = (ResultMemberBean) notifyEvent.value;
            TextViewUtils.setTextOrEmpty(tvName, bean.memberName);
            requestBean.member_id = bean.memberId;
            if(!TextUtils.isEmpty(bean.memberAvatar)){
                ivAvatar.setVisibility(View.VISIBLE);
                if (bean.memberAvatar.startsWith(mContext.getString(R.string.HTTP))) {
                    GlideHelper.displayImage(mContext, bean.memberAvatar, ivAvatar, R.drawable.avatar_default_female);
                } else {
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.memberAvatar,ivAvatar, R.drawable.avatar_default_female);
                }
            }else{
                ivAvatar.setVisibility(View.GONE);
            }
            ivMember.setSelected(true);
        }
    }

    private void initViews() {
        requestBean = new RequestAddAppointment();
        Bundle bundle = getIntent().getBundleExtra(EXTRA_DATA);
        if(null != bundle){
            date = bundle.getString("date");
            tvDate.setText(date);
            startTime = bundle.getString("start");
            endTime = bundle.getString("end");
            tvTime.setText(startTime + "~" + endTime);
            tvDate.setText(date);
        }
        if(TextUtils.isEmpty(tvName.getText().toString())){
            ivMember.setSelected(false);
        }else{
            ivMember.setSelected(true);
        }
        if(TextUtils.isEmpty(tvProject.getText().toString())){
            ivProject.setSelected(false);
        }else{
            ivProject.setSelected(true);
        }
        if(TextUtils.isEmpty(tvDate.getText().toString())){
            ivDate.setSelected(false);
        }else{
            ivDate.setSelected(true);
        }
        if(TextUtils.isEmpty(tvTime.getText().toString())){
            ivTime.setSelected(false);
        }else{
            ivTime.setSelected(true);
        }
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataCheck()) {
                    requestBean.startTime = DateTimeUtils.parseToStamp(date + " " + startTime, DateTimeUtils.FORMAT_DATETIME_TWO);
                    requestBean.endTime = DateTimeUtils.parseToStamp(date + " " + endTime, DateTimeUtils.FORMAT_DATETIME_TWO);
                    requestBean.name = UserfoPrefs.getInstance(mContext).getStaffName();
                    requestBean.phone = UserfoPrefs.getInstance(mContext).getMobile();
                    if (!TextUtils.isEmpty(etRemark.getText().toString())) {
                        requestBean.submit_notes = etRemark.getText().toString();
                    }
                    submit();
                }
            }
        });
        llSelectMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SelectStoreListActivity.startTypeSelectMember(mContext));
            }
        });
        llProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(ProjectListActivity.startProjectListActivity(mContext, null, mIntentResult), REQUEST_PROJECT);
            }
        });
        TextViewUtils.setTextOrEmpty(tvStore, UserfoPrefs.getInstance(mContext).getBaseonIdDesc());

        llTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != timePickerView) {
                    DeviceUtils.hideSoftKeyboard(v, mContext);
                    timePickerView.show();
                }
            }
        });

        llDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUtils.hideSoftKeyboard(v, mContext);
                datePickerView.show();
            }
        });

        final ArrayList<String> dateItems = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            dateItems.add(DateTimeUtils.getCustomDay2(i, DateTimeUtils.FORMAT_DATE_UI_TWO));
        }

        datePickerView = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvDate.setText(dateItems.get(options1));
                date = DateTimeUtils.getCustomDay2(options1, DateTimeUtils.FORMAT_DATE_UI_TWO);
                ivDate.setSelected(true);
            }
        }).build();
        datePickerView.setPicker(dateItems);
    }


    private String date;
    private String startTime;
    private String endTime;

    private void submit() {
        if (TextUtils.isEmpty(LoginInfoPrefs.getInstance(mContext).getStaffId())) {
            showTextDialog("该账号没有绑定员工", false);
            return;
        } else {
            requestBean.sp_id = UserfoPrefs.getInstance(mContext).getBaseonId();
            requestBean.name = UserfoPrefs.getInstance(mContext).getStaffName();
            List<String> mechanicList = new ArrayList<>();
            mechanicList.add(LoginInfoPrefs.getInstance(mContext).getStaffId());
            requestBean.mechanic_list = mechanicList;
            AppModelFactory.model().addAppointment(requestBean, new ProgressSubscriber<DataBean>(mContext) {

                @Override
                public void onCompleted() {
                    super.onCompleted();
                }

                @Override
                public void onNext(DataBean result) {
                    showToast("添加成功");
                    EvenManager.sendEvent(new NotifyEvent(NotifyEvent.APPOINTMENT));
                    finish();
                }

                @Override
                public void onError(ApiException ex) {
                    super.onError(ex);
                    showToast(ex.message);
                }

            });
        }
    }

    private void getSetting() {
        AppModelFactory.model().getAppointmentSetting(LoginInfoPrefs.getInstance(this).getGroupId(), new ProgressSubscriber<DataBean<ResultAppointmentSetting>>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean<ResultAppointmentSetting> result) {
                if (null != result.data) {
                    String start = result.data.appointmentStart.substring(0, result.data.appointmentStart.length() - 3);
                    String end = result.data.appointmentEnd.substring(0, result.data.appointmentEnd.length() - 3);
                    setStoreStartTime(start, end);
                }

            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.message);
            }

        });
    }

    private void setStoreStartTime(String start, String end){
        final ArrayList<String> options1Items = new ArrayList<>();
        String[] items = getResources().getStringArray(R.array.left_book_time);

        int open = 0;
        int close = 0;

        for (int i = 0; i < items.length; i++) {
            if(start.equals(items[i])){
                open = i;
            }
            if(end.equals(items[i])){
                close = i;
            }
        }

        for (int i = open; i <= close; i++) {
            options1Items.add(items[i]);
        }

        timePickerView = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if(null != options1Items){
                    if(options1 == options2){
                        showToast("开始时间不能等于结束时间");
                        return;
                    }
                    startTime = options1Items.get(options1);
                    endTime = options1Items.get(options2);
                    tvTime.setText(options1Items.get(options1) + "~" + options1Items.get(options2));
                    ivTime.setSelected(true);
                }

            }


        }).setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
            @Override
            public void onOptionsSelectChanged(int options1, int options2, int options3) {
                if(options2 < options1){
                    if(options1 == options1Items.size() - 1){
                        timePickerView.setSelectOptions(options1, options1);
                    }else{
                        timePickerView.setSelectOptions(options1, options1 + 1);
                    }
                }
            }
        }).setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
            @Override
            public void customLayout(View v) {
                final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_submit);
                final TextView tvCancel = (TextView) v.findViewById(R.id.tv_cancel);
                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timePickerView.returnData();
                        timePickerView.dismiss();
                    }
                });

                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timePickerView.dismiss();
                    }
                });

            }
        })      //设置选中项的颜色
                .setTextColorCenter(Color.parseColor("#1C1C1C"))
                //设置分割线的颜色
                .setDividerColor(Color.parseColor("#3DD08A"))
                .build();

        timePickerView.setNPicker(options1Items, options1Items, null);
        timePickerView.setSelectOptions(0, 1);
    }

    private boolean dataCheck() {
        String tips = "";
        if (TextUtils.isEmpty(tvName.getText().toString())) {
            tips = "请选择会员";
        } else if (TextUtils.isEmpty(tvStore.getText().toString())) {
            tips = "请选择门店";
        } else if (TextUtils.isEmpty(tvDate.getText().toString())) {
            tips = "请选择预约日期";
        } else if (TextUtils.isEmpty(tvTime.getText().toString())) {
            tips = "请选择预约时间";
        }
        if (TextUtils.isEmpty(tips)) {
            return true;
        } else {
            showToast(tips);
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && resultCode == RESULT_OK) {
             if (REQUEST_PROJECT == requestCode) {
                List<ResultProject> projectList = (List<ResultProject>) data.getSerializableExtra(EXTRA_DATA);
                mIntentResult = projectList;
                StringBuilder sb = new StringBuilder();
                List<String>projectId = new ArrayList<>();
                for (int i = 0, len = projectList.size(); i < len; i++) {
                    sb.append(projectList.get(i).name + ";");
                    if (i == len - 1) {
                        sb.deleteCharAt(sb.toString().length() - 1);
                    }
                    projectId.add(projectList.get(i).good_id);
                }
                TextViewUtils.setTextOrEmpty(tvProject, sb.toString());
                requestBean.service_list = projectId;
                ivProject.setSelected(true);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EvenManager.unregister(this);
    }
}

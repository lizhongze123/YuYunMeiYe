package cn.yuyun.yymy.ui.store.staff;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.example.takephoto.CustomHelper;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.PhoneUtils;
import cn.lzz.utils.ResoureUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.bean.Sex;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.event.RefreshPicEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestAddStaff;
import cn.yuyun.yymy.http.result.ResultPosition;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.utils.GlideHelper;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author 添加员工
 * @desc
 * @date
 */

public class AddStaffActivity extends BaseActivity {

    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.et_Name)
    EditText etName;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.et_mobileNum)
    EditText etMobileNum;
    @BindView(R.id.tv_entryTime)
    TextView tvCashTime;
    @BindView(R.id.et_staffNum)
    EditText etStaffNum;
    @BindView(R.id.tv_cross)
    TextView tvCross;
    @BindView(R.id.et_cardNum)
    EditText etCardNum;
    @BindView(R.id.tv_appointment)
    TextView tvAppointment;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    @BindView(R.id.et_emergencyContact)
    EditText etEmergencyContact;
    @BindView(R.id.et_emergencyContactMobile)
    EditText etEmergencyContactMobile;
    @BindView(R.id.tv_birthdayType)
    TextView tvBirthdayType;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    private OptionsPickerView<Sex> sexPickerView;
    private OptionsPickerView<String> crossPickerView;
    private TimePickerView timePickerView;

    private OptionsPickerView<String> birthdayTypePickerView;

    private OptionsPickerView<ResultPosition> positionPickerView;
    private RequestAddStaff requestAddStaff;
    private int timeType = 0;
    private CustomHelper customHelper;
    private int type;
    private StoreBean storeBean;
    private List<ResultPosition> resultPosition = new ArrayList<>();

    public static Intent startAddStaffActivity(Context context, StoreBean bean) {
        return new Intent(context, AddStaffActivity.class).putExtra(EXTRA_DATA, bean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    private void initViews() {
        titleBar.setTilte("新增员工");
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        tvSex.setText(Sex.FEMALE.desc);
        tvCashTime.setText(DateTimeUtils.getDate(System.currentTimeMillis(), DateTimeUtils.FORMAT_DATETIME));
        customHelper = new CustomHelper(AddStaffActivity.this).setCrop(true);
    }

    private void initDatas() {
        storeBean = (StoreBean) getIntent().getSerializableExtra(EXTRA_DATA);
        getPositionData();
        tvCross.setText("是");
        tvAppointment.setText("是");
        requestAddStaff = new RequestAddStaff();
        requestAddStaff.crossSp = 1;
        requestAddStaff.mechanic = "1";
        requestAddStaff.sex = Sex.FEMALE;
        requestAddStaff.entryTime = (System.currentTimeMillis() / 1000);

        final ArrayList<String> mDatas = new ArrayList<>();
        mDatas.add("是");
        mDatas.add("否");
        crossPickerView = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvCross.setText(mDatas.get(options1));
                if(type == 1){
                    tvAppointment.setText(mDatas.get(options1));
                    if ("是".equals(mDatas.get(options1))) {
                        requestAddStaff.mechanic = "1";
                    } else {
                        requestAddStaff.mechanic = "0";
                    }
                }else{
                    tvCross.setText(mDatas.get(options1));
                    if ("是".equals(mDatas.get(options1))) {
                        requestAddStaff.crossSp = 1;
                    } else {
                        requestAddStaff.crossSp = 0;
                    }
                }
            }
        }).build();
        crossPickerView.setPicker(mDatas);

        final ArrayList<String> birthdayDatas = new ArrayList<>();
        birthdayDatas.add("阳历");
        birthdayDatas.add("农历");
        birthdayTypePickerView = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvBirthdayType.setText(birthdayDatas.get(options1));
                if ("阳历".equals(birthdayDatas.get(options1))) {
                    requestAddStaff.birthType = "0";
                } else {
                    requestAddStaff.birthType = "1";
                }
            }
        }).build();
        birthdayTypePickerView.setPicker(birthdayDatas);

        timePickerView = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (timeType == 1) {
                    tvBirthday.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATE_UI));
                    String birthdayAry[] = tvBirthday.getText().toString().split("-");
                    requestAddStaff.birthYear = birthdayAry[0];
                    requestAddStaff.birthMonth = birthdayAry[1];
                    requestAddStaff.birthDay = birthdayAry[2];
                } else {
                    tvCashTime.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATETIME));
                    requestAddStaff.entryTime = date.getTime() / 1000;
                }
            }
        }).build();

        final ArrayList<Sex> mSexDatas = new ArrayList<>();
        mSexDatas.add(Sex.MALE2);
        mSexDatas.add(Sex.FEMALE);
        sexPickerView = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvSex.setText(mSexDatas.get(options1).desc);
                requestAddStaff.sex = mSexDatas.get(options1);
            }
        }).build();
        sexPickerView.setPicker(mSexDatas);

        positionPickerView = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvPosition.setText(resultPosition.get(options1).name);
                requestAddStaff.position = resultPosition.get(options1).positionId;
            }
        }).build();
    }


    private void submit() {
        if (TextUtils.isEmpty(etName.getText().toString().trim())) {
            showToast("请输入员工名字");
            return;
        } else if (TextUtils.isEmpty(etMobileNum.getText().toString().trim())) {
            showToast("请输入手机号");
            return;
        }
        if(!TextUtils.isEmpty(etStaffNum.getText().toString().trim())){
            requestAddStaff.staffNumber = etStaffNum.getText().toString().trim();
        }
        if(!TextUtils.isEmpty(etCardNum.getText().toString().trim())){
            requestAddStaff.idCard = etCardNum.getText().toString().trim();
        }
        if(!TextUtils.isEmpty(etEmergencyContact.getText().toString().trim())){
            requestAddStaff.emergencyPerson = etEmergencyContact.getText().toString().trim();
        }
        if(!TextUtils.isEmpty(etEmergencyContactMobile.getText().toString().trim())){
            requestAddStaff.emergencyMobile = etEmergencyContactMobile.getText().toString().trim();
        }
        //门店固定传2
        requestAddStaff.baseonType = storeBean.ogType + "";
        requestAddStaff.baseonId = storeBean.spId;
        requestAddStaff.name = etName.getText().toString().trim();
        requestAddStaff.idCard = etCardNum.getText().toString().trim();
        requestAddStaff.mobile = etMobileNum.getText().toString().trim();

        if (!TextUtils.isEmpty(path)) {
            File newFile = new File(path);
            uploadAvatar(newFile);
        }else{
            addStaff(null);
        }

    }

    private void uploadAvatar(File file){
        AppModelFactory.model().uploadPic(file, 1, new ProgressSubscriber<DataBean<String>>(mContext) {
            @Override
            public void onNext(DataBean<String> o) {
                if(null != o.data){
                    addStaff(o.data);
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.getMessage());
            }
        });

    }

    private void addStaff(String path) {
        if(!TextUtils.isEmpty(path)){
            requestAddStaff.avatar = path;
        }
        AppModelFactory.model().addStaff(requestAddStaff, new ProgressSubscriber<Object>(mContext) {

            @Override
            public void onNext(Object result) {
                showToast("添加成功");
                EvenManager.sendEvent(new NotifyEvent(NotifyEvent.STAFF));
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

    @OnClick(R.id.ll_avatar)
    public void selectPic(View v) {
        customHelper.onClick(getTakePhoto());
    }

    @OnClick(R.id.ll_sex)
    public void selectSex(View v) {
        showSexPickerDialog();
    }

    @OnClick(R.id.ll_cross)
    public void selectCross(View v) {
        type = 0;
        showCrossPickerDialog();
    }

    @OnClick(R.id.ll_appointment)
    public void selectAppointment(View v) {
        type = 1;
        showCrossPickerDialog();
    }

    @OnClick(R.id.ll_position)
    public void selectPosition(View v) {
        showPositionPickerDialog();
    }

    @OnClick(R.id.ll_entryTime)
    public void selectEntryTimeDialog(View v) {
        /*timeType = 0;
        timePickerView.show();*/
    }

    @OnClick(R.id.ll_birthday)
    public void selectBirthdayDialog(View v) {
        timeType = 1;
        timePickerView.show();
    }

    @OnClick(R.id.ll_birthdayType)
    public void selectBirthdayType(View v) {
        showBirthdayTypePickerDialog();
    }

    private void getPositionData() {
        AppModelFactory.model().getPosition(LoginInfoPrefs.getInstance(this).getGroupId(),new ProgressSubscriber<DataBean<List<ResultPosition>>>(mContext) {

            @Override
            public void onNext(DataBean<List<ResultPosition>> result) {
                if (null != resultPosition) {
                    resultPosition = result.data;
                    //一级选择器
                    positionPickerView.setPicker(result.data);
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });
    }

    private void showSexPickerDialog() {
        sexPickerView.show();
    }

    private void showPositionPickerDialog() {
        positionPickerView.show();
    }

    private void showCrossPickerDialog() {
        crossPickerView.show();
    }

    private void showBirthdayTypePickerDialog() {
        birthdayTypePickerView.show();
    }

    private void showDatePickerDialog() {
        // 控制时间范围
        Calendar calendar = Calendar.getInstance();
        //取整百
//        datePickerView.setRange((calendar.get(Calendar.YEAR) - 100) / 100 * 100
//                , calendar.get(Calendar.YEAR));

        Calendar userBirthCal = Calendar.getInstance();
        userBirthCal.setTimeInMillis(System.currentTimeMillis());
//        datePickerView.setSelectedItem(userBirthCal.get(Calendar.YEAR), userBirthCal.get(Calendar.MONTH) + 1, userBirthCal.get(Calendar.DAY_OF_MONTH));
//        datePickerView.show();
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    private String path;

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        path = result.getImages().get(0).getCompressPath();
        showAvatar(result.getImages());
    }

    private void showAvatar(ArrayList<TImage> images) {
        GlideHelper.displayImageForSize(mContext, new File(images.get(0).getOriginalPath()), ResoureUtils.getDimension(mContext, R.dimen.item_avatar_size), ResoureUtils.getDimension(mContext, R.dimen.item_avatar_size), ivAvatar);
    }

}

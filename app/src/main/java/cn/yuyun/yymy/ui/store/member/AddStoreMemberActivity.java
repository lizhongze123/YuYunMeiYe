package cn.yuyun.yymy.ui.store.member;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.example.takephoto.CustomHelper;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.DeviceUtils;
import cn.lzz.utils.LogUtils;
import cn.lzz.utils.PhoneUtils;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.bean.Sex;
import cn.yuyun.yymy.constan.Constans;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestAddMember;
import cn.yuyun.yymy.http.request.RequestMemberIsExist;
import cn.yuyun.yymy.http.result.ResultLevel;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.me.entity.MemberBean;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.utils.GlideHelper;
import de.hdodenhof.circleimageview.CircleImageView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author 门店添加会员
 * @desc
 * @date
 */

public class AddStoreMemberActivity extends BaseActivity {

    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.et_memberName)
    EditText etMemberName;
    @BindView(R.id.et_mobileNum)
    EditText etMobileNum;
    @BindView(R.id.tv_vipLevel)
    TextView tvVipLevel;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.et_cardNum)
    EditText etCardNum;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.tv_cashTime)
    TextView tvCashTime;
    @BindView(R.id.tv_store)
    TextView tvStore;
    @BindView(R.id.tv_cross)
    TextView tvCross;
    @BindView(R.id.tv_birthdayType)
    TextView tvBirthdayType;
    @BindView(R.id.et_desc)
    EditText etDesc;

    private int timeType = 0;
    private TimePickerView timePickerView;
    private OptionsPickerView<ResultLevel> levelPickerView;
    private OptionsPickerView<Sex> sexPickerView;
    private OptionsPickerView<String> crossPickerView;
    private List<ResultLevel> resultLevels;
    private RequestAddMember requestAddMember;
    private OptionsPickerView<String> birthdayTypePickerView;

    private CustomHelper customHelper;
    private StoreBean storeBean;

    public static Intent startAddStoreMemberActivity(Context context, StoreBean bean) {
        return new Intent(context, AddStoreMemberActivity.class).putExtra(EXTRA_DATA, bean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
        getData();
    }

    private void initViews() {
        titleBar.setTilte("添加会员");
        titleBar.setTvRight("保存");
        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        tvCashTime.setText(DateTimeUtils.getDate(System.currentTimeMillis(), DateTimeUtils.FORMAT_DATETIME));
        tvCross.setText("是");
        customHelper = new CustomHelper(AddStoreMemberActivity.this).setCrop(true);
        storeBean = (StoreBean) getIntent().getSerializableExtra(EXTRA_DATA);
        TextViewUtils.setTextOrEmpty(tvStore, storeBean.spName);
        if(null != Constans.globalPic){
            if(null != Constans.globalPic.resource && Constans.globalPic.resource.size() != 0){
                GlideHelper.displayImage(mContext, Constans.globalPic.resource.get(1).resourceRspList.get(0).url, ivAvatar);
            }
        }
    }

    private void initDatas() {
        requestAddMember = new RequestAddMember();
        requestAddMember.spId = storeBean.spId;
        requestAddMember.sex = Sex.FEMALE;
        requestAddMember.cross_sp = 1;
        requestAddMember.cashTime = System.currentTimeMillis() / 1000;
        tvSex.setText(Sex.FEMALE.desc);
        final ArrayList<String> mDatas = new ArrayList<>();
        mDatas.add("是");
        mDatas.add("否");
        crossPickerView = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvCross.setText(mDatas.get(options1));
                if ("是".equals(mDatas.get(options1))) {
                    requestAddMember.cross_sp = 1;
                } else {
                    requestAddMember.cross_sp = 0;
                }
            }
        }).build();
        crossPickerView.setPicker(mDatas);

        levelPickerView = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvVipLevel.setText(resultLevels.get(options1).name);
                requestAddMember.level_id = resultLevels.get(options1).levelId;
            }
        }).build();

        final ArrayList<Sex> mSexDatas = new ArrayList<>();
        mSexDatas.add(Sex.FEMALE);
        mSexDatas.add(Sex.MALE2);
        sexPickerView = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvSex.setText(mSexDatas.get(options1).desc);
                requestAddMember.sex = mSexDatas.get(options1);
                if(null != Constans.globalPic){
                    if (Sex.FEMALE.desc.equals(mDatas.get(options1))) {
                        GlideHelper.displayImage(mContext, Constans.globalPic.resource.get(1).resourceRspList.get(0).url, ivAvatar);
                    } else {
                        GlideHelper.displayImage(mContext, Constans.globalPic.resource.get(0).resourceRspList.get(0).url, ivAvatar);
                    }
                }
            }
        }).build();
        sexPickerView.setPicker(mSexDatas);

        timePickerView = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (timeType == 0) {
                    tvBirthday.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATE_UI));
                    String birthdayAry[] = tvBirthday.getText().toString().split("-");
                    requestAddMember.birth_year = birthdayAry[0];
                    requestAddMember.birth_month = birthdayAry[1];
                    requestAddMember.birth_day = birthdayAry[2];
                } else {
//                    tvCashTime.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATE_UI));
//                    requestAddMember.cashTime = DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATE_UI);
                }
            }
        }).build();

        final ArrayList<String> birthdayDatas = new ArrayList<>();
        birthdayDatas.add("农历");
        birthdayDatas.add("阳历");
        birthdayTypePickerView = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvBirthdayType.setText(birthdayDatas.get(options1));
                if ("阳历".equals(birthdayDatas.get(options1))) {
                    requestAddMember.birth_type = "1";
                } else {
                    requestAddMember.birth_type = "0";
                }
            }
        }).build();
        birthdayTypePickerView.setPicker(birthdayDatas);
    }

    @OnClick({R.id.ll_sex, R.id.ll_birthday, R.id.ll_vipLevel, R.id.ll_cross, R.id.ll_avatar, R.id.ll_birthdayType})
    public void clickItem(View v) {
        switch (v.getId()){
            case R.id.ll_sex:
                DeviceUtils.hideSoftKeyboard(v, mContext);
                showSexPickerDialog();
                break;
            case R.id.ll_birthday:
                DeviceUtils.hideSoftKeyboard(v, mContext);
                timeType = 0;
                timePickerView.show();
                break;
            case R.id.ll_vipLevel:
                DeviceUtils.hideSoftKeyboard(v, mContext);
                showLevelPickerDialog();
                break;
            case R.id.ll_cross:
                DeviceUtils.hideSoftKeyboard(v, mContext);
                showCrossPickerDialog();
                break;
            case R.id.ll_avatar:
                customHelper.onClick(getTakePhoto());
                break;
            case R.id.ll_birthdayType:
                DeviceUtils.hideSoftKeyboard(v, mContext);
                birthdayTypePickerView.show();
                break;
                default:
        }
    }

    private void submit() {
        if (TextUtils.isEmpty(etMemberName.getText().toString().trim())) {
            showToast("请输入会员名字");
            return;
        } else if (requestAddMember.sex == null) {
            showToast("请选择性别");
            return;
        } else if (TextUtils.isEmpty(etMobileNum.getText().toString().trim())) {
            showToast("请输入手机号");
            return;
        } else if (TextUtils.isEmpty(tvVipLevel.getText().toString().trim())) {
            showToast("请选择会员级别");
            return;
        } else if (TextUtils.isEmpty(tvStore.getText().toString().trim())) {
            showToast("请选择所属门店");
            return;
        } else if (TextUtils.isEmpty(tvCashTime.getText().toString().trim())) {
            showToast("请选择开户时间");
            return;
        }
        requestAddMember.name = etMemberName.getText().toString().trim();
        requestAddMember.member_card = etCardNum.getText().toString().trim();
        requestAddMember.mobile = etMobileNum.getText().toString().trim();
        if (!TextUtils.isEmpty(path)) {
            File newFile = new File(path);
            uploadAvatar(newFile);
        }else{
            addMember(null);
        }

    }

    private void uploadAvatar(File file){
        AppModelFactory.model().uploadPic(file, 2, new ProgressSubscriber<DataBean<String>>(mContext) {
            @Override
            public void onNext(DataBean<String> o) {
                if(null != o.data){
                    addMember(o.data);
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.getMessage());
            }
        });

    }

    private void addMember(String path) {
        if(!TextUtils.isEmpty(path)){
            requestAddMember.avatar = path;
        }
        if(!TextUtils.isEmpty(etDesc.getText().toString())){
            requestAddMember.description = etDesc.getText().toString();
        }
        AppModelFactory.model().addMember(requestAddMember, new ProgressSubscriber<DataBean<String>>(mContext) {

            @Override
            public void onNext(DataBean<String> result) {
                showToast("添加成功");
                EvenManager.sendEvent(new NotifyEvent());
                finish();
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.getMessage());
                LogUtils.e(ex.getMessage());
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });
    }

    private void getData() {
        AppModelFactory.model().getLevel(LoginInfoPrefs.getInstance(mContext).getGroupId(), new ProgressSubscriber<DataBean<List<ResultLevel>>>(mContext) {

            @Override
            public void onNext(DataBean<List<ResultLevel>> result) {
                if (null != result) {
                    resultLevels = result.data;
                    //一级选择器
                    levelPickerView.setPicker(result.data);
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

    private void showLevelPickerDialog() {
        levelPickerView.show();
    }

    private void showCrossPickerDialog() {
        crossPickerView.show();
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

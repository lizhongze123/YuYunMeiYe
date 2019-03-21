package cn.yuyun.yymy.ui.home.member;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import butterknife.OnClick;
import cn.example.takephoto.CustomHelper;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.DeviceUtils;
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
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;
import cn.yuyun.yymy.http.result.ResultLevel;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.home.cashier.CashierMemberDetailActivity;
import cn.yuyun.yymy.ui.home.leave.SelectMyStoreListActivity;
import cn.yuyun.yymy.ui.home.leave.SelectStoreListActivity;
import cn.yuyun.yymy.ui.home.leave.StoreListActivity;
import cn.yuyun.yymy.ui.me.entity.MemberBean;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.dialog.CashierAddMemberDialog;
import de.hdodenhof.circleimageview.CircleImageView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author
 * @desc
 * @date
 */

public class AddMemberActivity extends BaseActivity {

    private final int REQUEST_CODE = 1104;
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

    private OptionsPickerView<String> birthdayTypePickerView;

    private RequestAddMember requestAddMember;
    private CustomHelper customHelper;

    private int timeType = 0;
    private TimePickerView timePickerView;
    private OptionsPickerView<ResultLevel> levelPickerView;
    private OptionsPickerView<Sex> sexPickerView;
    private OptionsPickerView<String> crossPickerView;
    private List<ResultLevel> resultLevels;

    private int type;
    public final static int CASHIER = 1;
    public final static int NORMAL = 0;

    public static Intent startAddMemberActivityForNormal(Context context) {
        return new Intent(context, AddMemberActivity.class).putExtra(EXTRA_TYPE, NORMAL);
    }

    public static Intent startAddMemberActivityForCashier(Context context) {
        return new Intent(context, AddMemberActivity.class).putExtra(EXTRA_TYPE, CASHIER);
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
        titleBar.setTilte("新增会员");
        titleBar.setTvRight("保存");
        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        tvCashTime.setText(DateTimeUtils.getDate(System.currentTimeMillis(), DateTimeUtils.FORMAT_DATETIME_YEAR_MONTH_DAY));
        tvCross.setText("是");
        type = getIntent().getIntExtra(EXTRA_TYPE, 0);
        customHelper = new CustomHelper(AddMemberActivity.this).setCrop(true);
        if(null != Constans.globalPic){
            if(null != Constans.globalPic.resource && Constans.globalPic.resource.size() != 0){
                GlideHelper.displayImage(mContext, Constans.globalPic.resource.get(1).resourceRspList.get(0).url, ivAvatar);
            }
        }
    }

    private void initDatas() {
        requestAddMember = new RequestAddMember();
        requestAddMember.supervisor = 1;
        requestAddMember.sex = Sex.FEMALE;
        requestAddMember.cashTime = System.currentTimeMillis() / 1000;
        tvSex.setText(Sex.FEMALE.desc);
        final ArrayList<String> mDatas = new ArrayList<>();
        mDatas.add("是");
        mDatas.add("否");
        final ArrayList<Sex> mSexDatas = new ArrayList<>();
        mSexDatas.add(Sex.FEMALE);
        mSexDatas.add(Sex.MALE2);

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
                    tvCashTime.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATE_UI));
                    requestAddMember.cashTime = date.getTime() / 1000;
                }
            }
        }).build();

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
                if(null != resultLevels){
                    tvVipLevel.setText(resultLevels.get(options1).name);
                    requestAddMember.level_id = resultLevels.get(options1).levelId;
                }
            }
        }).build();

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

    @OnClick({R.id.ll_sex, R.id.ll_birthday, R.id.ll_vipLevel, R.id.ll_store, R.id.ll_cashTime, R.id.ll_cross, R.id.ll_avatar, R.id.ll_birthdayType})
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
            case R.id.ll_store:
                startActivityForResult(SelectMyStoreListActivity.startTypeSelectStoreFromAddMember(mContext), REQUEST_CODE);
                break;
            case R.id.ll_cashTime:
                DeviceUtils.hideSoftKeyboard(v, mContext);
                timeType = 1;
                showDatePickerDialog();
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
        }else if (TextUtils.isEmpty(tvVipLevel.getText().toString().trim())) {
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
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast(ex.message);
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(mContext.getString(R.string.error_no_network));
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
            public void onNext(final DataBean<String> result) {
                if (null != result.data) {
                    if(type == CASHIER){
                        CashierAddMemberDialog dialog = new CashierAddMemberDialog(mContext);
                        dialog.setOnPositiveListener(new CashierAddMemberDialog.OnPositiveListener() {
                            @Override
                            public void onPositive() {
                                startActivity(CashierMemberDetailActivity.startCashierMemberDetailActivity(mContext, result.data));
                                finish();
                            }

                            @Override
                            public void onNegative() {
                                finish();
                            }
                        });
                        dialog.show();
                        EvenManager.sendEvent(new NotifyEvent(NotifyEvent.RREFRESH));
                    }else{
                        showToast("添加成功");
                        EvenManager.sendEvent(new NotifyEvent(NotifyEvent.RREFRESH));
                        finish();
                    }
                }
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast(ex.message);
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(mContext.getString(R.string.error_no_network));
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
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast(ex.message);
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(mContext.getString(R.string.error_no_network));
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == RESULT_OK) {
            if (REQUEST_CODE == requestCode) {
                ResultClassfiyStoreBean.OgServiceplacesRspListBean storeBean= (ResultClassfiyStoreBean.OgServiceplacesRspListBean) data.getSerializableExtra(EXTRA_DATA);
                requestAddMember.spId = storeBean.sp_id;
                TextViewUtils.setTextOrEmpty(tvStore, storeBean.sp_name);
            }
        }
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

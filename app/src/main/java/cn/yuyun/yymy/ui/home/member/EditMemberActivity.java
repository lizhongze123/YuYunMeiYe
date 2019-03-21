package cn.yuyun.yymy.ui.home.member;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.http.ApiException;
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
import cn.example.takephoto.CustomHelper;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.bean.Sex;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestAddMember;
import cn.yuyun.yymy.http.result.ResultLevel;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.FlowLayout;
import de.hdodenhof.circleimageview.CircleImageView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author 编辑会员
 * @desc
 * @date 2018/1/15
 */

public class EditMemberActivity extends BaseActivity {

    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.ll_recordBefore)
    LinearLayout llRecordBefore;
    @BindView(R.id.et_memberName)
    EditText etMemberName;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_vipLevel)
    TextView tvVipLevel;
    @BindView(R.id.tv_cardNumber)
    TextView tvCardNumber;
    @BindView(R.id.tv_birthdayType)
    TextView tvBirthdayType;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.tv_store)
    TextView tvStore;
    @BindView(R.id.tv_isCross)
    TextView tvIsCross;
    @BindView(R.id.tv_cashTime)
    TextView tvCashTime;

    private RequestAddMember requestAddMember;
    private CustomHelper customHelper;
    private ResultMemberBean memberBean;

    private int timeType = 0;
    private TimePickerView timePickerView;
    private OptionsPickerView<ResultLevel> levelPickerView;
    private OptionsPickerView<Sex> sexPickerView;
    private OptionsPickerView<String> crossPickerView;
    private List<ResultLevel> resultLevels;

    public static Intent startEditMemberActivity(Context context, ResultMemberBean memberBean) {
        return new Intent(context, EditMemberActivity.class).putExtra(EXTRA_DATA, memberBean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_member);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDefaultDatas();
        initDatas();
        getData();
    }

    private void initViews() {
        memberBean = (ResultMemberBean) getIntent().getSerializableExtra(EXTRA_DATA);
        titleBar.setTilte("编辑会员");
        titleBar.setTvRight("完成");
        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        tvCardNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("会员卡号不可修改");
            }
        });
        tvStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("所属门店不可修改");
            }
        });
        tvCashTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("开户时间不可修改");
            }
        });
        findViewById(R.id.ll_sex).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSexPickerDialog();
            }
        });
        findViewById(R.id.ll_birthday).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeType = 0;
                timePickerView.show();
            }
        });
        findViewById(R.id.ll_vipLevel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLevelPickerDialog();
            }
        });
        findViewById(R.id.ll_cross).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCrossPickerDialog();
            }
        });
        findViewById(R.id.ll_avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customHelper.onClick(getTakePhoto());
            }
        });
        requestAddMember = new RequestAddMember();
        customHelper = new CustomHelper(EditMemberActivity.this).setCrop(true);
    }

    private void initDefaultDatas() {
        if (null != memberBean) {
            if (memberBean.memberAvatar.startsWith("http")) {
                GlideHelper.displayImage(mContext, memberBean.memberAvatar, ResoureUtils.getDimension(mContext, R.dimen.item_avatar_size), ivAvatar);
            } else {
                GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + memberBean.memberAvatar, ResoureUtils.getDimension(mContext, R.dimen.item_avatar_size), ivAvatar);
            }
            etMemberName.setText(memberBean.memberName);
            TextViewUtils.setTextOrEmpty(tvSex, memberBean.member_sex.desc);
            if (memberBean.member_cross_sp == 1) {
                tvIsCross.setText("是");
            } else {
                tvIsCross.setText("否");
            }
            tvCardNumber.setText(memberBean.memberCard);
            TextViewUtils.setTextOrEmpty(tvBirthday, memberBean.member_birth_year + "年" + memberBean.member_birth_month + "月" + memberBean.member_birth_day + "日");
            TextViewUtils.setTextOrEmpty(tvVipLevel, memberBean.memberLevelName);
            TextViewUtils.setTextOrEmpty(tvStore, memberBean.member_in_sp_name);
            TextViewUtils.setTextOrEmpty(tvCashTime, memberBean.memberCashTime);

            requestAddMember.spId = memberBean.memberInSpId;
            requestAddMember.level_id = memberBean.member_level_id;
            requestAddMember.member_card = memberBean.memberCard;
            requestAddMember.name = memberBean.memberName;
            requestAddMember.mobile = memberBean.memberMobile;
            requestAddMember.sex = memberBean.member_sex;
            requestAddMember.cross_sp = memberBean.member_cross_sp;
//            requestAddMember.cashTime = memberBean.memberCashTime;
            requestAddMember.birth_year = memberBean.member_birth_year;
            requestAddMember.birth_month = memberBean.member_birth_month;
            requestAddMember.birth_day = memberBean.member_birth_day;

        }
    }

    private void initDatas() {
        final ArrayList<String> mDatas = new ArrayList<>();
        mDatas.add("是");
        mDatas.add("否");
        final ArrayList<Sex> mSexDatas = new ArrayList<>();
        mSexDatas.add(Sex.MALE2);
        mSexDatas.add(Sex.FEMALE);

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
//                    requestAddMember.cashTime = DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATE_UI);
                }
            }
        }).build();

        crossPickerView = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvIsCross.setText(mDatas.get(options1));
                if ("是".equals(mDatas.get(options1))) {
                    requestAddMember.cross_sp = 1;
                } else {
                    requestAddMember.cross_sp = 0;
                }
            }
        }).build();

        levelPickerView = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvVipLevel.setText(resultLevels.get(options1).name);
                requestAddMember.level_id = resultLevels.get(options1).levelId;
            }
        }).build();

        sexPickerView = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvSex.setText(mSexDatas.get(options1).desc);
                requestAddMember.sex = mSexDatas.get(options1);
            }
        }).build();
        sexPickerView.setPicker(mSexDatas);
        crossPickerView.setPicker(mDatas);
    }


    private void submit() {
        if (TextUtils.isEmpty(etMemberName.getText().toString().trim())) {
            showToast("请输入会员名字");
            return;
        } else if (requestAddMember.sex == null) {
            showToast("请选择性别");
            return;
        } else if (TextUtils.isEmpty(tvBirthday.getText().toString().trim())) {
            showToast("请选择生日");
            return;
        } else if (TextUtils.isEmpty(tvVipLevel.getText().toString().trim())) {
            showToast("请选择会员级别");
            return;
        } else if (TextUtils.isEmpty(tvStore.getText().toString().trim())) {
            showToast("请选择所属门店");
            return;
        } else if (TextUtils.isEmpty(tvIsCross.getText().toString().trim())) {
            showToast("请选择是否跨店");
            return;
        } else if (TextUtils.isEmpty(tvCashTime.getText().toString().trim())) {
            showToast("请选择开户时间");
            return;
        }
        requestAddMember.name = etMemberName.getText().toString().trim();
        requestAddMember.member_card = tvCardNumber.getText().toString().trim();
        requestAddMember.member_id = memberBean.memberId;
        if (!TextUtils.isEmpty(path)) {
            File newFile = new File(path);
        }
        editMember();
    }

    private void editMember() {
        AppModelFactory.model().editMember(requestAddMember, new ProgressSubscriber<Object>(mContext) {

            @Override
            public void onNext(Object result) {
                showToast("修改成功");
                memberBean.memberInSpId = requestAddMember.spId;
                memberBean.member_level_id = requestAddMember.level_id;
                memberBean.memberCard = requestAddMember.member_card;
                memberBean.memberName = requestAddMember.name;
                memberBean.memberMobile = requestAddMember.mobile;
                memberBean.member_sex = requestAddMember.sex;
                memberBean.member_cross_sp = requestAddMember.cross_sp;
//                memberBean.memberCashTime = requestAddMember.cashTime;
                memberBean.member_birth_year = requestAddMember.birth_year;
                memberBean.member_birth_month = requestAddMember.birth_month;
                memberBean.member_birth_day = requestAddMember.birth_day;
                Intent intent = new Intent();
                intent.putExtra(EXTRA_DATA, memberBean);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast("编辑会员失败");
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });
    }

    private void getData() {
       /* AppModelFactory.model().getLevel(new ProgressSubscriber<List<ResultLevel>>(mContext) {

            @Override
            public void onNext(List<ResultLevel> result) {
                if (null != result) {
                    resultLevels = result;
                    //一级选择器
                    levelPickerView.setPicker(result);
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast("获取数据失败");
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });*/
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
//        datePickerView.setRange((calendar.get(Calendar.YEAR) - 100)/100*100
//                , calendar.get(Calendar.YEAR));

        Calendar userBirthCal = Calendar.getInstance();
        userBirthCal.setTimeInMillis(System.currentTimeMillis());
//        datePickerView.setSelectedItem(userBirthCal.get(Calendar.YEAR),userBirthCal.get(Calendar.MONTH) + 1,userBirthCal.get(Calendar.DAY_OF_MONTH));
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

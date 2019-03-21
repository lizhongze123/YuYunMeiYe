package cn.yuyun.yymy.ui.home.member.memberdata;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import cn.example.takephoto.CustomHelper;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseNoTitleFragment;
import cn.yuyun.yymy.bean.Sex;
import cn.yuyun.yymy.constan.Constans;
import cn.yuyun.yymy.event.RefreshMemberDataEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestAddMember;
import cn.yuyun.yymy.http.request.RequestLabel;
import cn.yuyun.yymy.http.result.ResultLabel;
import cn.yuyun.yymy.http.result.ResultLevel;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.FlowLayout;
import cn.yuyun.yymy.view.dialog.AddLabelDialog;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author 详细资料-基本资料Fragment
 * @desc
 * @date
 */
public class MemberDataOneFragment extends BaseNoTitleFragment {

    private final int REQUEST_CODE = 1104;
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.ll_avatar)
    LinearLayout llAvatar;
    @BindView(R.id.et_memberName)
    EditText etMemberName;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_vipLevel)
    TextView tvVipLevel;
    @BindView(R.id.et_cardNum)
    EditText etCardNum;
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
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.et_desc)
    EditText etDesc;

    private CustomHelper customHelper;
    private TimePickerView timePickerView;
    private OptionsPickerView<ResultLevel> levelPickerView;
    private OptionsPickerView<Sex> sexPickerView;
    private OptionsPickerView<String> crossPickerView;
    private OptionsPickerView<String> birthdayTypePickerView;
    private List<ResultLevel> resultLevels;
    private int timeType = 0;

    private ResultMemberBean memberBean;
    private String type;

    private RequestAddMember requestAddMember;

    public static MemberDataOneFragment newInstance(ResultMemberBean memberBean, String type) {
        MemberDataOneFragment fragment = new MemberDataOneFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATA, memberBean);
        args.putString(EXTRA_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        if (getArguments() != null) {
            memberBean = (ResultMemberBean) getArguments().getSerializable(EXTRA_DATA);
            type = getArguments().getString(EXTRA_TYPE);
            initBean(memberBean);
        }
    }

    private void initBean(ResultMemberBean memberBean) {
        if(null == memberBean){
            return;
        }
        requestAddMember = new RequestAddMember();
        requestAddMember.member_id = memberBean.memberId;
        requestAddMember.name = memberBean.memberName;
        requestAddMember.sex = memberBean.member_sex;
        requestAddMember.level_id = memberBean.member_level_id;
        requestAddMember.member_card = memberBean.memberCard;
        requestAddMember.birth_type = memberBean.member_birth_type;
        requestAddMember.birth_year = memberBean.member_birth_year;
        requestAddMember.birth_month = memberBean.member_birth_month;
        requestAddMember.birth_day = memberBean.member_birth_day;
        requestAddMember.spId = memberBean.memberInSpId;
        requestAddMember.cross_sp = memberBean.member_cross_sp;
        requestAddMember.description = memberBean.member_description;
        requestAddMember.cashTime = DateTimeUtils.parseToStamp(memberBean.memberCashTime, DateTimeUtils.FORMAT_DATETIME_UI);
    }

    @Override
    protected void onBindViewBefore(View root) {
        super.onBindViewBefore(root);
        mContext = getContext();
    }

    @Override
    protected int getTheLayoutId() {
        return R.layout.activity_member_data;
    }

    @Override
    protected void initViews(View root) {
        super.initViews(root);
        normal();
        if (null == type) {
            tvSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    normal();
                    submit();
                    tvSave.setVisibility(View.GONE);
                    MemberDataActivity parentActivity = (MemberDataActivity) getActivity();
                    parentActivity.setSave();
                }
            });
        }
        customHelper = new CustomHelper(this.getActivity()).setCrop(true);
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
        llAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customHelper.onClick(getTakePhoto());
            }
        });
        tvSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexPickerView.show();
            }
        });
        tvBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeType = 0;
                timePickerView.show();
            }
        });
        tvBirthdayType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthdayTypePickerView.show();
            }
        });
        tvVipLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelPickerView.show();
            }
        });
        tvIsCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crossPickerView.show();
            }
        });
    }

    public void setEdit(){
        edit();
        tvSave.setVisibility(View.VISIBLE);
    }

    public void setNormal(){
        normal();
        tvSave.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
            if(null == memberBean){
                return;
            }
            if (!TextUtils.isEmpty(memberBean.memberAvatar)) {
                if (memberBean.memberAvatar.startsWith(mContext.getString(R.string.HTTP))) {
                    GlideHelper.displayImage(mContext, memberBean.memberAvatar, ResoureUtils.getDimension(mContext, R.dimen.item_avatar_size), ivAvatar);
                } else {
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + memberBean.memberAvatar, ResoureUtils.getDimension(mContext, R.dimen.item_avatar_size), ivAvatar);
                }
            } else {
                GlideHelper.displayImage(mContext, memberBean.member_sex.resId, ivAvatar);
            }
            etMemberName.setText(memberBean.memberName);
            TextViewUtils.setTextOrEmpty(tvSex, memberBean.member_sex.desc);
            TextViewUtils.setTextOrEmpty(tvVipLevel, memberBean.memberLevelName);
            etCardNum.setText(memberBean.memberCard);
            if (memberBean.member_birth_type.equals("0")) {
                TextViewUtils.setTextOrEmpty(tvBirthdayType, "农历");
            } else {
                TextViewUtils.setTextOrEmpty(tvBirthdayType, "阳历");
            }
            if(memberBean.member_birth_year.equals("0") && memberBean.member_birth_month.equals("0") && memberBean.member_birth_day.equals("0")){
                tvBirthday.setText("");
            }else{
                TextViewUtils.setTextOrEmpty(tvBirthday, memberBean.member_birth_year + "年" + memberBean.member_birth_month + "月" + memberBean.member_birth_day + "日");
            }
            TextViewUtils.setTextOrEmpty(tvStore, memberBean.member_in_sp_name);
            if (memberBean.member_cross_sp == 1) {
                TextViewUtils.setTextOrEmpty(tvIsCross, "是");
            } else {
                TextViewUtils.setTextOrEmpty(tvIsCross, "否");
            }
            TextViewUtils.setTextOrEmpty(tvCashTime, memberBean.memberCashTime);
            etDesc.setText(memberBean.member_description);


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
                    requestAddMember.cashTime = date.getTime() / 1000;
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

        final ArrayList<String> birthdayDatas = new ArrayList<>();
        birthdayDatas.add("阳历");
        birthdayDatas.add("农历");
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
        if(Constans.isStoreLoginer){
            getLevel();
        }
    }

    private boolean isAdd;

    private void edit(){
        isAdd = true;
        etMemberName.setEnabled(true);
        etCardNum.setEnabled(true);
        etDesc.setEnabled(true);
        llAvatar.setEnabled(true);
        tvSex.setEnabled(true);
        tvVipLevel.setEnabled(true);
        tvBirthdayType.setEnabled(true);
        tvBirthday.setEnabled(true);
        tvStore.setEnabled(true);
    }

    private void normal(){
        etMemberName.setEnabled(false);
        etCardNum.setEnabled(false);
        etDesc.setEnabled(false);
        llAvatar.setEnabled(false);
        tvSex.setEnabled(false);
        tvVipLevel.setEnabled(false);
        tvBirthdayType.setEnabled(false);
        tvBirthday.setEnabled(false);
        tvStore.setEnabled(false);
        tvIsCross.setEnabled(false);
    }

    private void submit() {
        if (TextUtils.isEmpty(requestAddMember.name)) {
            showToast("请输入会员名字");
            return;
        }else{
            requestAddMember.name = etMemberName.getText().toString();
        }
        requestAddMember.member_card = etCardNum.getText().toString();
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            editMemberAvatar(file);
        }else{
            editMember(null);
        }

    }

    private void editMemberAvatar(File file){
        AppModelFactory.model().editMemberAvatar(file, memberBean.memberId, new ProgressSubscriber<DataBean<String>>(mContext) {
            @Override
            public void onNext(DataBean<String> o) {
                if(null != o.data){
                    editMember(o.data);
                }
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast("操作失败，请稍后重试");
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(mContext.getString(R.string.error_no_network));
            }
        });

    }

    private void editMember(String path) {
        if(!TextUtils.isEmpty(path)){
            requestAddMember.avatar = path;
        }
        if(TextUtils.isEmpty(etDesc.getText().toString())){
            requestAddMember.description = "";
        }else{
            requestAddMember.description = etDesc.getText().toString();
        }
        AppModelFactory.model().editMember(requestAddMember, new ProgressSubscriber<Object>(mContext) {

            @Override
            public void onNext(Object result) {
                EvenManager.sendEvent(new RefreshMemberDataEvent());
                showToast("修改成功");
                normal();
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast("操作失败，请稍后重试");
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

    private void getLevel() {
        AppModelFactory.model().getLevel(LoginInfoPrefs.getInstance(mContext).getGroupId(),new ProgressSubscriber<DataBean<List<ResultLevel>>>(mContext) {

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
                showToast("获取数据失败，请稍后重试");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == RESULT_OK) {
            if (REQUEST_CODE == requestCode) {
                ResultMemberBean bean = (ResultMemberBean) data.getSerializableExtra(EXTRA_DATA);
                memberBean = bean;
                initData();
                EvenManager.sendEvent(new RefreshMemberDataEvent());
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


package cn.yuyun.yymy.ui.me;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;
import com.githang.statusbar.StatusBarCompat;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.example.takephoto.CustomHelper;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.DensityUtils;
import cn.lzz.utils.LogUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.StringUtil;
import cn.lzz.utils.TextViewUtils;
import cn.lzz.utils.VersionUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseNoTitleFragment;
import cn.yuyun.yymy.bean.Sex;
import cn.yuyun.yymy.constan.Constans;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestModifyStaff;
import cn.yuyun.yymy.http.request.RequestStaffDetail;
import cn.yuyun.yymy.http.result.ResultStaffDetail;
import cn.yuyun.yymy.http.result.StaffBean;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.home.work.StaffPicWallActivity;
import cn.yuyun.yymy.ui.store.ManualFeeListActivity;
import cn.yuyun.yymy.ui.store.PersonTimeActivity;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.ShadowDrawable;
import cn.yuyun.yymy.view.ToggleButton;
import cn.yuyun.yymy.view.WarnningDialog;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author
 * @desc
 * @date
 */
public class MeFragment extends BaseNoTitleFragment {

    @BindView(R.id.tl_title)
    RelativeLayout tlTitle;
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_store)
    TextView tvStore;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    @BindView(R.id.toggleButton)
    ToggleButton toggleButton;
    @BindView(R.id.rl_info)
    RelativeLayout rlInfo;
    @BindView(R.id.iv_personTime)
    ImageView ivPersonTime;
    @BindView(R.id.tv_peopleTimeDesc)
    TextView tvPeopleTimeDesc;
    @BindView(R.id.tv_peopleTime)
    TextView tvPeopleTime;
    @BindView(R.id.ll_personTime)
    LinearLayout llPersonTime;
    @BindView(R.id.ll_newMember)
    LinearLayout llNewMember;
    @BindView(R.id.iv_manualfee)
    ImageView ivManualfee;
    @BindView(R.id.tv_manualfeeDesc)
    TextView tvManualfeeDesc;
    @BindView(R.id.tv_manualfee)
    TextView tvManualfee;
    @BindView(R.id.ll_manualFee)
    LinearLayout llManualFee;
    @BindView(R.id.iv_personNum)
    ImageView ivPersonNum;
    @BindView(R.id.tv_peopleNumDesc)
    TextView tvPeopleNumDesc;
    @BindView(R.id.tv_peopleNum)
    TextView tvPeopleNum;
    @BindView(R.id.tv_serviceNum)
    TextView tvServiceNum;
    @BindView(R.id.tv_newMember)
    TextView tvNewMember;
    @BindView(R.id.ll_personNum)
    LinearLayout llPersonNum;
    @BindView(R.id.ll_serviceNum)
    LinearLayout llServiceNum;
    @BindView(R.id.ll_top)
    LinearLayout llTop;
    @BindView(R.id.tv_before)
    TextView tvBefore;
    @BindView(R.id.ll_recordBefore)
    LinearLayout llRecordBefore;
    @BindView(R.id.tv_sale)
    TextView tvSale;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.ll_sale)
    LinearLayout llSale;
    @BindView(R.id.tv_service)
    TextView tvService;
    @BindView(R.id.ll_service)
    LinearLayout llService;
    @BindView(R.id.ll_collection)
    LinearLayout llCollection;
    @BindView(R.id.ll_picWall)
    LinearLayout llPicWall;
    @BindView(R.id.ll_email)
    LinearLayout llEmail;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.tv_toggleDesc)
    TextView tvToggleDesc;

    private CustomHelper customHelper;
    private String staffId;
    private StoreBean storeBean;
    private WarnningDialog warnningDialog;

    @Override
    protected int getTheLayoutId() {
        return R.layout.fragment_me;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onResume() {
        StatusBarCompat.setStatusBarColor(getActivity(), getResources().getColor(R.color.title_mine));
        super.onResume();
    }

    @Override
    protected void initViews(View root) {
        super.initViews(root);
        if (TextUtils.isEmpty(LoginInfoPrefs.getInstance(getContext()).getToken())) {
            showLogin();
            return;
        }
        tvVersion.setText("当前版本:V" + VersionUtils.getAppVersionName(mContext));
        staffId = LoginInfoPrefs.getInstance(mContext).getStaffId();
        storeBean = new StoreBean();
        if (UserfoPrefs.getInstance(mContext).getBaseonType().equals("1")) {
            //总部
            storeBean.spId = "";
        } else {
            storeBean.spId = UserfoPrefs.getInstance(mContext).getOgId();
        }
        storeBean.group_id = LoginInfoPrefs.getInstance(mContext).getGroupId();

        srl.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (TextUtils.isEmpty(staffId)) {
                    showTextDialog("该账号没有绑定员工", false);
                    if (srl.isRefreshing()) {
                        srl.setRefreshing(false);
                    }
                    return;
                }
                initData();
            }
        });

        toggleButton.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (null != warnningDialog) {
                    warnningDialog.show();
                }
            }
        });

        warnningDialog = new WarnningDialog(mContext, "确定开启/关闭预约？");
        warnningDialog.setOnPositiveListener(new WarnningDialog.OnDialogListener() {
            @Override
            public void onPositive() {
                modifyStatus(UserfoPrefs.getInstance(mContext).getMechanic());
                warnningDialog.dismiss();
            }

            @Override
            public void onNegative() {
                toggleButton.toggle2();
            }
        });
        customHelper = new CustomHelper(getActivity()).setCrop(true);

    }

    @Override
    protected void initData() {
        super.initData();

        if (TextUtils.isEmpty(LoginInfoPrefs.getInstance(getContext()).getToken())) {
            showLogin();
            return;
        }

        if (!hasInternet()) {
            showTextDialog(getString(R.string.NO_INTERNET), false);
            return;
        }

        if (TextUtils.isEmpty(staffId)) {
            showTextDialog(getString(R.string.no_bind_staff), false);
            return;
        }
        getUserInfo();
    }

    private void getUserInfo() {
        RequestStaffDetail body = new RequestStaffDetail();
        body.baseon_type = UserfoPrefs.getInstance(mContext).getBaseonType();
        body.group_id = LoginInfoPrefs.getInstance(mContext).getGroupId();
        body.staff_id = LoginInfoPrefs.getInstance(mContext).getStaffId();
        body.start_date = DateTimeUtils.getTimesMonthMorning();
        body.end_date = DateTimeUtils.getNowTimeStamp();
        body.staff_id = LoginInfoPrefs.getInstance(mContext).getStaffId();
        List<String> list = new ArrayList<>();
        if (UserfoPrefs.getInstance(mContext).getBaseonType().equals("1")) {
            //总部
            body.sp_id_list = list;
        } else {
            list.add(UserfoPrefs.getInstance(mContext).getOgId());
            body.sp_id_list = list;
        }
        AppModelFactory.model().staffDetail(body, new ProgressSubscriber<DataBean<ResultStaffDetail>>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
                if (srl.isRefreshing()) {
                    srl.setRefreshing(false);
                }
            }

            @Override
            public void onNext(DataBean<ResultStaffDetail> result) {
                if (null != result) {
                    if (null != result.data) {
                        ResultStaffDetail bean = result.data;
                        TextViewUtils.setTextOrEmpty(tvName, bean.staff_name);
                        TextViewUtils.setTextOrEmpty(tvStore, "门店：" + bean.sp_name);
                        if (StringUtil.isEmpty(bean.staff_position_name)) {
                            tvPosition.setVisibility(View.INVISIBLE);
                        } else {
                            tvPosition.setVisibility(View.VISIBLE);
                            TextViewUtils.setTextOrEmpty(tvPosition, "(" + bean.staff_position_name + ")");
                        }
                        TextViewUtils.setTextOrEmpty(tvTime, "入职时间：" + bean.staff_entry_time);
                        if (!TextUtils.isEmpty(bean.staff_avatar)) {
                            if (bean.staff_avatar.startsWith(getString(R.string.HTTP))) {
                                GlideHelper.displayImage(mContext, bean.staff_avatar, ivAvatar);
                            } else {
                                GlideHelper.displayImage(mContext, getString(R.string.image_url_prefix) + bean.staff_avatar, ivAvatar);
                            }
                        } else {
                            if (bean.staff_sex.equals(Sex.MALE.desc)) {
                                GlideHelper.displayImage(mContext, R.drawable.avatar_default_male, ivAvatar);
                            } else {
                                GlideHelper.displayImage(mContext, R.drawable.avatar_default_female, ivAvatar);
                            }
                        }
                        if (bean.mechanic == 1) {
                            toggleButton.setToggleOn();
                            tvToggleDesc.setText("关闭预约");
                        } else {
                            toggleButton.setToggleOff();
                            tvToggleDesc.setText("打开预约");
                        }
                        TextViewUtils.setTextOrEmpty(tvPeopleNum, bean.person_number + "");
                        TextViewUtils.setTextOrEmpty(tvPeopleTime, StringFormatUtils.formatTwoDecimal(bean.person_times));
                        TextViewUtils.setTextOrEmpty(tvManualfee, StringFormatUtils.formatTwoDecimal(bean.handmake));
                        TextViewUtils.setTextOrEmpty(tvBefore, StringFormatUtils.formatTwoDecimal(bean.presale));
                        TextViewUtils.setTextOrEmpty(tvSale, StringFormatUtils.formatTwoDecimal(bean.sale));
                        TextViewUtils.setTextOrEmpty(tvService, StringFormatUtils.formatTwoDecimal(bean.consume));
                        TextViewUtils.setTextOrEmpty(tvServiceNum, StringFormatUtils.formatTwoDecimal(bean.service_items));
                        TextViewUtils.setTextOrEmpty(tvNewMember, bean.new_member_count + "");
                    }
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                if (srl.isRefreshing()) {
                    srl.setRefreshing(false);
                }
                showToast(getString(R.string.relogin));
            }

        });

    }

    private void modifyStatus(int statuss) {
        if (TextUtils.isEmpty(staffId)) {
            showTextDialog(getString(R.string.no_bind_staff), false);
            return;
        }
        if (statuss == 0) {
            statuss = 1;
        } else {
            statuss = 0;
        }
        RequestModifyStaff requestModifyStaff = new RequestModifyStaff();
        requestModifyStaff.mechanic = statuss;
        requestModifyStaff.staff_id = staffId;

        final int finalStatuss = statuss;
        AppModelFactory.model().modifyStaff(requestModifyStaff, new ProgressSubscriber<Object>(mContext) {

            @Override
            public void onNext(Object result) {
                UserfoPrefs.getInstance(mContext).setMechanic(finalStatuss);
                showToast("更改状态成功");
                if (finalStatuss == 1) {
                    tvToggleDesc.setText("关闭预约");
                } else {
                    tvToggleDesc.setText("打开预约");
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast("更改状态失败");
            }

        });
    }

    @OnClick({R.id.iv_avatar, R.id.ll_personTime, R.id.ll_manualFee, R.id.ll_personNum, R.id.ll_recordBefore, R.id.ll_sale, R.id.ll_service, R.id.ll_collection, R.id.ll_picWall, R.id.ll_email, R.id.iv_set, R.id.ll_serviceNum})
    public void onClickItem(View view) {
        switch (view.getId()) {
            case R.id.iv_avatar:
                //更换头像
                if (!isHasStaffId()) {
                    return;
                }
                customHelper.onClick(getTakePhoto());
                break;
            case R.id.ll_personTime:
                if (!isHasStaffId()) {
                    return;
                }
                startActivity(PersonTimeActivity.startPersonTimeActivity(mContext, staffId, storeBean));
                break;
            case R.id.ll_manualFee:
                if (!isHasStaffId()) {
                    return;
                }
                StaffBean staffBean = new StaffBean();
                staffBean.staffId = staffId;
                staffBean.groupId = LoginInfoPrefs.getInstance(mContext).getGroupId();
                startActivity(ManualFeeListActivity.startManualFeeListActivity(mContext, staffBean, storeBean));
                break;
            case R.id.ll_personNum:
                if (!isHasStaffId()) {
                    return;
                }
                startActivity(PeopleNumListActivity.startPeopleNumListActivity(mContext, LoginInfoPrefs.getInstance(mContext).getStaffId(), storeBean));
                break;
            case R.id.ll_recordBefore:
                if (!isHasStaffId()) {
                    return;
                }
                startActivity(RecordListActivity.startRecordBeforeActivity(this.getContext(), null, storeBean));
                break;
            case R.id.ll_sale:
                if (!isHasStaffId()) {
                    return;
                }
                startActivity(RecordListActivity.startRecordSaleActivity(this.getContext(), null, storeBean));
                break;
            case R.id.ll_service:
                if (!isHasStaffId()) {
                    return;
                }
                startActivity(RecordListActivity.startRecordServiceActivity(this.getContext(), null, storeBean));
                break;
            case R.id.ll_collection:
                if (!isHasStaffId()) {
                    return;
                }
                startActivity(new Intent(getContext(), MyCollectionActivity.class));
                break;
            case R.id.ll_picWall:
                startActivity(StaffPicWallActivity.startStaffPicWallActivity(mContext, null, storeBean));
                break;
            case R.id.ll_email:
                if (!isHasStaffId()) {
                    return;
                }
                if (UserfoPrefs.getInstance(mContext).getPermission()) {
                    startActivity(new Intent(this.getContext(), EmailListActivity.class));
                } else {
                    startActivity(new Intent(this.getContext(), EmailActivity.class));
                }
                break;
            case R.id.iv_set:
                startActivity(new Intent(this.getContext(), SetActivity.class));
                break;
            case R.id.ll_serviceNum:
                startActivity(ServiceNumListActivity.startServiceNumListActivity(this.getContext(), null, storeBean));
                break;
            default:
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

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        modifyAvatar(result.getImages());
    }

    private void modifyAvatar(final ArrayList<TImage> images) {
        File file = null;
        if (!TextUtils.isEmpty(images.get(0).getCompressPath())) {
//            file = CompressHelper.getDefault(getContext()).compressToFile(new File(images.get(0).getOriginalPath()));
            file = new File(images.get(0).getCompressPath());
        }
        if (null != file) {
            AppModelFactory.model().modifyAvatar(new File(images.get(0).getCompressPath()), staffId, new ProgressSubscriber<Object>(mContext) {
                @Override
                public void onNext(Object result) {
                    showToast("更改成功");
                    Constans.CURRENT_USER_AVATAR = images.get(0).getCompressPath();
                    GlideHelper.displayImage(mContext, new File(images.get(0).getCompressPath()), ivAvatar);
                }

                @Override
                public void onError(ApiException ex) {
                    super.onError(ex);
                    showToast(ex.getMessage());
                    if (srl.isRefreshing()) {
                        srl.setRefreshing(false);
                    }
                }

            });
        }
    }

}

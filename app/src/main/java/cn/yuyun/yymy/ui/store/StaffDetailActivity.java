package cn.yuyun.yymy.ui.store;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;
import com.githang.statusbar.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.DensityUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.StringUtil;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestStaffDetail;
import cn.yuyun.yymy.http.result.ResultStaffDetail;
import cn.yuyun.yymy.http.result.StaffBean;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.home.member.TaCustomerActivity;
import cn.yuyun.yymy.ui.home.work.MyWorkActivity;
import cn.yuyun.yymy.ui.home.work.StaffPicWallActivity;
import cn.yuyun.yymy.ui.me.PeopleNumListActivity;
import cn.yuyun.yymy.ui.me.RecordListActivity;
import cn.yuyun.yymy.ui.me.ServiceNumListActivity;
import cn.yuyun.yymy.ui.store.staff.AttendanceStoreStaffActivity;
import cn.yuyun.yymy.ui.store.staff.StaffRecordActivity;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.ShadowDrawable;
import de.hdodenhof.circleimageview.CircleImageView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;

/**
 * @author
 * @desc 员工详情
 * @date
 */
public class StaffDetailActivity extends BaseNoTitleActivity {

    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_store)
    TextView tvStore;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    @BindView(R.id.tv_peopleTime)
    TextView tvPeopleTime;
    @BindView(R.id.tv_manualfee)
    TextView tvManualfee;
    @BindView(R.id.tv_peopleNum)
    TextView tvPeopleNum;
    @BindView(R.id.tv_before)
    TextView tvBefore;
    @BindView(R.id.tv_sale)
    TextView tvSale;
    @BindView(R.id.tv_service)
    TextView tvService;
    @BindView(R.id.tv_newMember)
    TextView tvNewMember;
    private StaffBean staffBean;
    @BindView(R.id.ll_manualFee)
    LinearLayout llManualFee;
    @BindView(R.id.ll_personNum)
    LinearLayout llPersonNum;
    @BindView(R.id.ll_newMember)
    LinearLayout llNewMember;
    @BindView(R.id.ll_recordBefore)
    LinearLayout llRecordBefore;
    @BindView(R.id.ll_sale)
    LinearLayout llSale;
    @BindView(R.id.ll_service)
    LinearLayout llService;
    @BindView(R.id.ll_personTime)
    LinearLayout llPersonTime;
    @BindView(R.id.ll_serviceNum)
    LinearLayout llServiceNum;
    @BindView(R.id.tv_tit)
    TextView tvTit;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_serviceNum)
    TextView tvServiceNum;

    private StoreBean storeBean;

    public static Intent startStaffDetailActivity(Context context, StaffBean bean, StoreBean storeBean) {
        return new Intent(context, StaffDetailActivity.class).putExtra(EXTRA_DATA, bean).putExtra(EXTRA_DATA2, storeBean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.title_mine));
        setContentView(R.layout.activity_staff_detail);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initData();
    }

    private void initViews() {
        tvTit.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        staffBean = (StaffBean) getIntent().getSerializableExtra(EXTRA_DATA);
        storeBean = (StoreBean) getIntent().getSerializableExtra(EXTRA_DATA2);

        ShadowDrawable.setShadowDrawable(llManualFee, Color.parseColor("#ffffff"), DensityUtils.dip2px(mContext, 8),
                Color.parseColor("#80e0e0e0"),
                DensityUtils.dip2px(mContext, 5), 0, 0);
        ShadowDrawable.setShadowDrawable(llPersonNum, Color.parseColor("#ffffff"), DensityUtils.dip2px(mContext, 8),
                Color.parseColor("#80e0e0e0"),
                DensityUtils.dip2px(mContext, 5), 0, 0);
        ShadowDrawable.setShadowDrawable(llPersonTime, Color.parseColor("#ffffff"), DensityUtils.dip2px(mContext, 8),
                Color.parseColor("#80e0e0e0"),
                DensityUtils.dip2px(mContext, 5), 0, 0);

        ShadowDrawable.setShadowDrawable(llRecordBefore, Color.parseColor("#ffffff"), DensityUtils.dip2px(mContext, 8),
                Color.parseColor("#80e0e0e0"),
                DensityUtils.dip2px(mContext, 5), 0, 0);
        ShadowDrawable.setShadowDrawable(llSale, Color.parseColor("#ffffff"), DensityUtils.dip2px(mContext, 8),
                Color.parseColor("#80e0e0e0"),
                DensityUtils.dip2px(mContext, 5), 0, 0);
        ShadowDrawable.setShadowDrawable(llService, Color.parseColor("#ffffff"), DensityUtils.dip2px(mContext, 8),
                Color.parseColor("#80e0e0e0"),
                DensityUtils.dip2px(mContext, 5), 0, 0);
        ShadowDrawable.setShadowDrawable(llServiceNum, Color.parseColor("#ffffff"), DensityUtils.dip2px(mContext, 8),
                Color.parseColor("#80e0e0e0"),
                DensityUtils.dip2px(mContext, 5), 0, 0);
        ShadowDrawable.setShadowDrawable(llNewMember, Color.parseColor("#ffffff"), DensityUtils.dip2px(mContext, 8),
                Color.parseColor("#80e0e0e0"),
                DensityUtils.dip2px(mContext, 5), 0, 0);
        srl.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUserInfo();
            }
        });
    }

    private void initData() {
        if (null == staffBean) {
            return;
        }
        TextViewUtils.setTextOrEmpty(tvName, staffBean.staffName);
        TextViewUtils.setTextOrEmpty(tvStore, "门店:" + staffBean.baseonIdDesc);
        TextViewUtils.setTextOrEmpty(tvTime, "入职时间:" + staffBean.entryTime);
        if (StringUtil.isEmpty(staffBean.positionName)) {
            tvPosition.setVisibility(View.GONE);
        } else {
            tvPosition.setVisibility(View.VISIBLE);
            TextViewUtils.setTextOrEmpty(tvPosition, "(" + staffBean.positionName + ")");
        }
        if (!TextUtils.isEmpty(staffBean.avatar)) {
            if (staffBean.avatar.startsWith(getString(R.string.HTTP))) {
                GlideHelper.displayImage(mContext, staffBean.avatar, ivAvatar);
            } else {
                GlideHelper.displayImage(mContext, getString(R.string.image_url_prefix) + staffBean.avatar, ivAvatar);
            }
        } else {
            GlideHelper.displayImage(mContext, staffBean.sex.resId, ivAvatar);
        }
        getUserInfo();
    }

    private void getUserInfo() {
        RequestStaffDetail body = new RequestStaffDetail();
        body.baseon_type = staffBean.baseon_type;
        body.group_id = staffBean.groupId;
        body.staff_id = staffBean.staffId;
        body.start_date = DateTimeUtils.getTimesMonthMorning();
        body.end_date = DateTimeUtils.getNowTimeStamp();
        body.staff_id = staffBean.staffId;
        List<String> list = new ArrayList<>();
        list.add(staffBean.baseon_id);
        body.sp_id_list = list;
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
                        TextViewUtils.setTextOrEmpty(tvPeopleTime, StringFormatUtils.formatTwoDecimal(bean.person_times));
                        TextViewUtils.setTextOrEmpty(tvManualfee, StringFormatUtils.formatTwoDecimal(bean.handmake));
                        TextViewUtils.setTextOrEmpty(tvPeopleNum, bean.person_number + "");
                        TextViewUtils.setTextOrEmpty(tvBefore, StringFormatUtils.formatTwoDecimal(bean.presale));
                        TextViewUtils.setTextOrEmpty(tvSale, StringFormatUtils.formatTwoDecimal(bean.sale));
                        TextViewUtils.setTextOrEmpty(tvService, StringFormatUtils.formatTwoDecimal(bean.consume));
                        TextViewUtils.setTextOrEmpty(tvNewMember, bean.new_member_count + "");
                        TextViewUtils.setTextOrEmpty(tvServiceNum, StringFormatUtils.formatTwoDecimal(bean.service_items));
                    }
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(getString(R.string.relogin));
                if (srl.isRefreshing()) {
                    srl.setRefreshing(false);
                }
            }

        });
    }

    @OnClick({R.id.ll_record, R.id.ll_serviceNum, R.id.ll_customer, R.id.ll_picWall, R.id.ll_attendance, R.id.ll_sale, R.id.ll_service, R.id.ll_work, R.id.ll_manualFee, R.id.ll_personNum, R.id.ll_personTime, R.id.ll_recordBefore, R.id.iv_back})
    public void onClickItem(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_work:
                startActivity(MyWorkActivity.startMyWorkActivityFromStaff(mContext, staffBean));
                break;
            case R.id.ll_customer:
                startActivity(TaCustomerActivity.startTaRecommendActivity(mContext, staffBean, storeBean));
                break;
            case R.id.ll_picWall:
                startActivity(StaffPicWallActivity.startStaffPicWallActivity(mContext, staffBean, storeBean));
                break;
            case R.id.ll_record:
                startActivity(StaffRecordActivity.startStaffRecordActivity(mContext, staffBean, storeBean));
                break;
            case R.id.ll_attendance:
                startActivity(AttendanceStoreStaffActivity.startAttendanceStoreStaffActivity(mContext, storeBean, staffBean));
                break;
            case R.id.ll_personNum:
                startActivity(PeopleNumListActivity.startPeopleNumListActivityFromStore(mContext, staffBean.staffId, storeBean));
                break;
            case R.id.ll_personTime:
                startActivity(PersonTimeActivity.startPersonTimeActivity(mContext, staffBean.staffId, storeBean));
                break;
            case R.id.ll_manualFee:
                startActivity(ManualFeeListActivity.startManualFeeListActivity(mContext, staffBean, storeBean));
                break;
            case R.id.ll_recordBefore:
                startActivity(RecordListActivity.startRecordBeforeActivity(mContext, staffBean, storeBean));
                break;
            case R.id.ll_sale:
                startActivity(RecordListActivity.startRecordSaleActivity(mContext, staffBean, storeBean));
                break;
            case R.id.ll_service:
                startActivity(RecordListActivity.startRecordServiceActivity(mContext, staffBean, storeBean));
                break;
            case R.id.ll_serviceNum:
                startActivity(ServiceNumListActivity.startServiceNumListActivity(mContext, staffBean, storeBean));
                break;
            default:

        }
    }

}

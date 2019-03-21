package cn.yuyun.yymy.ui.store.member;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.ProgressSubscriber;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestUpdateBook;
import cn.yuyun.yymy.http.result.AppointmentBean;
import cn.yuyun.yymy.http.result.ResultBook;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.bannerview.RoundAngleImageView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;


/**
 * @author lzz
 * @desc 会员预约详情Activity
 * @date
 */

public class MemberAppointmentDetailActivity extends BaseActivity {

    @BindView(R.id.iv_avatar)
    RoundAngleImageView ivAvatar;
    @BindView(R.id.tv_store)
    TextView tvStore;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_project)
    TextView tvProject;
    @BindView(R.id.tv_staff)
    TextView tvStaff;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.iv_result)
    ImageView ivResult;

    private ResultBook bean;

    public static Intent startMemberAppointmentDetailActivity(Context context, ResultBook bean) {
        return new Intent(context, MemberAppointmentDetailActivity.class).putExtra(EXTRA_DATA, bean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_appointment_detail);
        ButterKnife.bind(this);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
    }

    private void initViews() {
        titleBar.setTilte("预约详情");
        bean = (ResultBook) getIntent().getSerializableExtra(EXTRA_DATA);
        ivResult.setImageDrawable(ResoureUtils.getDrawable(mContext, bean.status.imgId));
        TextViewUtils.setTextOrEmpty(tvStore,  "消费门店:" + bean.spName);
        TextViewUtils.setTextOrEmpty(tvAddress, bean.sp_addr);
        TextViewUtils.setTextOrEmpty(tvDate, bean.date + " " + bean.startTime + "~" + bean.endTime);
        TextViewUtils.setTextOrEmpty(tvProject, bean.serviceName);
        TextViewUtils.setTextOrEmpty(tvStaff,  bean.mechanicStaffName);
        TextViewUtils.setTextOrEmpty(tvRemark, bean.submit_notes);

        if (!TextUtils.isEmpty(bean.memberAvatar)) {
            if (bean.sp_thumb_url.startsWith(mContext.getString(R.string.HTTP))) {
                GlideHelper.displayImage(mContext, bean.sp_thumb_url, ivAvatar, R.drawable.avatar_default_female);
            } else {
                GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.sp_thumb_url, ivAvatar, R.drawable.avatar_default_female);
            }
        } else {
            GlideHelper.displayImage(mContext, R.drawable.default_store, ivAvatar);
        }
    }

}

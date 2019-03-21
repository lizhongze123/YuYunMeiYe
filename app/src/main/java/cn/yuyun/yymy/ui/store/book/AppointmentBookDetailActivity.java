package cn.yuyun.yymy.ui.store.book;

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
import cn.yuyun.yymy.http.result.ResultBook;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.utils.GlideHelper;
import de.hdodenhof.circleimageview.CircleImageView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;


/**
 * @author
 * @desc 预约本详情Activity
 * @date
 */

public class AppointmentBookDetailActivity extends BaseActivity {

    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_vip)
    TextView tvVip;
    @BindView(R.id.ll_user)
    LinearLayout llUser;
    @BindView(R.id.tv_mobile)
    TextView tvMobile;
    @BindView(R.id.tv_store)
    TextView tvStore;
    @BindView(R.id.tv_staff)
    TextView tvStaff;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_project)
    TextView tvProject;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.iv_result)
    ImageView ivResult;

    @BindView(R.id.ll_selectResult)
    LinearLayout llSelectResult;

    private ResultBook resultBook;

    public static Intent startAppointmentBookDetailActivity(Context context, ResultBook bean) {
        return new Intent(context, AppointmentBookDetailActivity.class).putExtra(EXTRA_DATA, bean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
    }

    private void initViews() {
        titleBar.setTilte("预约详情");
        resultBook = (ResultBook) getIntent().getSerializableExtra(EXTRA_DATA);

        TextViewUtils.setTextOrEmpty(tvName, resultBook.memberName);
        TextViewUtils.setTextOrEmpty(tvStaff, resultBook.mechanicStaffName);
        TextViewUtils.setTextOrEmpty(tvMobile, resultBook.phone);
        TextViewUtils.setTextOrEmpty(tvProject, resultBook.serviceName);
        TextViewUtils.setTextOrEmpty(tvStore, resultBook.spName);
        TextViewUtils.setTextOrEmpty(tvRemark, resultBook.submit_notes);
        TextViewUtils.setTextOrEmpty(tvDate, resultBook.date + " " + resultBook.startTime + "~" + resultBook.endTime);
        if(TextUtils.isEmpty(resultBook.member_level_name)){
            tvVip.setVisibility(View.GONE);
        }else{
            tvVip.setVisibility(View.VISIBLE);
            TextViewUtils.setTextOrEmpty(tvVip, resultBook.member_level_name);
        }
        if (!TextUtils.isEmpty(resultBook.memberAvatar)) {
            if (resultBook.memberAvatar.startsWith(mContext.getString(R.string.HTTP))) {
                GlideHelper.displayImage(mContext, resultBook.memberAvatar, ivAvatar, R.drawable.avatar_default_female);
            } else {
                GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + resultBook.memberAvatar, ivAvatar, R.drawable.avatar_default_female);
            }
        } else {
            GlideHelper.displayImage(mContext, R.drawable.avatar_default_female, ivAvatar);
        }


        findViewById(R.id.tv_agree).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit(ResultBook.AppointmentType.AGREE);
            }
        });
        findViewById(R.id.tv_refuse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit(ResultBook.AppointmentType.REFUSED);
            }
        });

        if (resultBook.status == ResultBook.AppointmentType.REVIEWING) {
            llSelectResult.setVisibility(View.VISIBLE);
        } else {
            ivResult.setVisibility(View.VISIBLE);
            ivResult.setImageDrawable(ResoureUtils.getDrawable(mContext, resultBook.status.imgId));
        }

    }


    private void submit(final ResultBook.AppointmentType status) {
        RequestUpdateBook bean = new RequestUpdateBook();
        bean.sp_reservation_id = resultBook.id;
        bean.status = status;
        AppModelFactory.model().updateBook(bean, new ProgressSubscriber<Object>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(Object result) {
                showToast("操作成功");
                llSelectResult.setVisibility(View.GONE);
                ivResult.setVisibility(View.VISIBLE);
                ivResult.setImageDrawable(ResoureUtils.getDrawable(mContext, resultBook.status.imgId));
                EvenManager.sendEvent(new NotifyEvent(NotifyEvent.RREFRESH));
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.message);
            }

        });
    }


}

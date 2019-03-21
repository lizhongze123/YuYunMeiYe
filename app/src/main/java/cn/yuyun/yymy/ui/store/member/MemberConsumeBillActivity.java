package cn.yuyun.yymy.ui.store.member;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.PageInfo;
import com.example.http.Presenter2;
import com.example.http.ProgressSubscriber;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.DeviceUtils;
import cn.lzz.utils.LogUtils;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.TextViewUtils;
import cn.lzz.utils.ToastUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.app.MyApplication;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.bean.ConsumeType;
import cn.yuyun.yymy.bean.Sex;
import cn.yuyun.yymy.constan.Constans;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestStoreBill;
import cn.yuyun.yymy.http.result.ResultBillManage;
import cn.yuyun.yymy.http.result.ResultBillManagerTypeDetail;
import cn.yuyun.yymy.http.result.ResultBillManagerTypeDetail.BillAllInfoBean;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.utils.GlideHelper;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;

/**
 * @author
 * @desc
 * @date
 */
public class MemberConsumeBillActivity extends BaseActivity {

    public DrawerLayout drawerLayout;

    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.rl_avatar)
    RelativeLayout rlAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_vip)
    TextView tvVip;
    @BindView(R.id.ll_user)
    LinearLayout llUser;
    @BindView(R.id.tv_mobile)
    TextView tvMobile;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_one)
    TextView tvOne;
    @BindView(R.id.tv_two)
    TextView tvTwo;
    @BindView(R.id.rg_one)
    LinearLayout rgOne;
    @BindView(R.id.tv_negative)
    TextView tvNegative;
    @BindView(R.id.tv_positive)
    TextView tvPositive;
    @BindView(R.id.ll_submit)
    LinearLayout llSubmit;
    @BindView(R.id.main_right_drawer_layout)
    RelativeLayout mainRightDrawerLayout;
    @BindView(R.id.main_drawer_layout)
    DrawerLayout mainDrawerLayout;
    private RelativeLayout main_right_drawer_layout;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    private Presenter2<ResultBillManage> presenter;

    private RequestStoreBill requestStoreBill = new RequestStoreBill();

    private MemberBillAdapter mAdapter;

    private ResultMemberBean memberBean;
    private StoreBean storeBean;

    private int timeType = 0;
    private TimePickerView timePickerView;

    /**
     * 记录滑动到第几条
     */
    private int tempPos = 0;
    /**
     * 记录总数
     */
    private int amount;

    public static Intent startMemberConsumeBillActivity(Context context, ResultMemberBean memberBean, StoreBean storeBean) {
        return new Intent(context, MemberConsumeBillActivity.class).putExtra(EXTRA_DATA, memberBean).putExtra(EXTRA_DATA2, storeBean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_consume_bill);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initData();
    }

    private void initData() {
        List<String> spIdList = new ArrayList<>();
        spIdList.add(memberBean.memberInSpId);
        requestStoreBill.spId = spIdList;
        requestStoreBill.memberId = memberBean.memberId;
        presenter.loadData(true);
    }

    public void initLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        //侧滑栏关闭手势滑动
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //设置菜单内容之外其他区域的背景色
        drawerLayout.setScrimColor(ResoureUtils.getColor(mContext, R.color.transparent_black));
        //左边菜单
        main_right_drawer_layout = (RelativeLayout) findViewById(R.id.main_right_drawer_layout);
    }

    private void initViews() {
        memberBean = (ResultMemberBean) getIntent().getSerializableExtra(EXTRA_DATA);
        storeBean = (StoreBean) getIntent().getSerializableExtra(EXTRA_DATA2);
        titleBar.setTilte("消费单据");
        titleBar.setRightIcon(R.drawable.pop_msg);
        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Constans.isStoreLoginer){
                    List<ResultMemberBean> list = new ArrayList<>();
                    list.add(memberBean);
                    startActivity(new Intent(SendMessageActivity.startSendMessageActivity(mContext, list)));
                }else{
                    showToast(getString(R.string.PARTNER));
                }
            }
        });
        if (null != memberBean.memberAvatar && !TextUtils.isEmpty(memberBean.memberAvatar)) {
            if (memberBean.memberAvatar.startsWith(getString(R.string.HTTP))) {
                GlideHelper.displayImage(mContext, memberBean.memberAvatar, ivAvatar);
            } else {
                GlideHelper.displayImage(mContext, getString(R.string.image_url_prefix) + memberBean.memberAvatar, ivAvatar);
            }
        } else {
            if (memberBean.member_sex.equals(Sex.MALE.desc)) {
                GlideHelper.displayImage(mContext, R.drawable.avatar_default_male, ivAvatar);
            } else {
                GlideHelper.displayImage(mContext, R.drawable.avatar_default_female, ivAvatar);
            }
        }
        TextViewUtils.setTextOrEmpty(tvName, memberBean.memberName);
        TextViewUtils.setTextOrEmpty(tvMobile, memberBean.memberCard);
        if (TextUtils.isEmpty(memberBean.memberLevelName)) {
            tvVip.setVisibility(View.GONE);
        } else {
            tvVip.setVisibility(View.VISIBLE);
            TextViewUtils.setTextOrEmpty(tvVip, "(" + memberBean.memberLevelName + ")");
        }

        configurePresenter();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mAdapter = new MemberBillAdapter(this);

        mAdapter.setScrollToBottomListener(new MemberBillAdapter.OnScrollToBottomListener() {
            @Override
            public void onBottom(int position, ResultBillManage bean) {

                if (mAdapter.getItemCount() == 0) {
                    return;
                }

                if (position == mAdapter.getItemCount() - 1) {
                    //加载更多
                    presenter.loadData(false);
                } else {
                    tempPos = position + 1;
                    mRecyclerView.scrollToPosition(position + 1);
                    if (requestStoreBill.start == 0 && requestStoreBill.end == 0) {
                        tvAmount.setText("单据" + amount + "条 (" + (position + 2) + "/" + amount + ")");
                    } else {
                        tvAmount.setText(tvOne.getText().toString() + "~" + tvTwo.getText().toString() + "（" + (position + 2) + "/" + amount + ")");
                    }
                    staffMgrRecordDetail(mAdapter.getItem(position + 1));
                }
            }

            @Override
            public void onTop(int position, ResultBillManage bean) {
                if (position != 0) {
                    mRecyclerView.scrollToPosition(position - 1);
                    if (requestStoreBill.start == 0 && requestStoreBill.end == 0) {
                        tvAmount.setText("单据" + amount + "条 (" + (position) + "/" + amount + ")");
                    } else {
                        tvAmount.setText(tvOne.getText().toString() + "~" + tvTwo.getText().toString() + "（" + (position) + "/" + amount + ")");
                    }
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        timePickerView = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (timeType == 0) {
                    tvOne.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATETIME_YEAR_MONTH_DAY));
                    requestStoreBill.start = date.getTime() / 1000;
                } else {
                    tvTwo.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATETIME_YEAR_MONTH_DAY));
                    requestStoreBill.end = date.getTime() / 1000;
                }
            }
        }).build();
        initLayout();
    }

    private void staffMgrRecordDetail(ResultBillManage bean) {

        AppModelFactory.model().staffMgrRecordConsumeDetail(storeBean.group_id, bean.record_id, new ProgressSubscriber<DataBean<ResultBillManagerTypeDetail>>(mContext, false) {

            @Override
            public void onNext(DataBean<ResultBillManagerTypeDetail> result) {
                if (null != result) {
                    if (null != result.data) {
                        List<BillAllInfoBean> list = new ArrayList();
                        if (null != result.data.bill_buy_info) {
                            for (BillAllInfoBean billAllInfoBean : result.data.bill_buy_info) {
                                billAllInfoBean.consumeType = ConsumeType.BUY;
                                list.add(billAllInfoBean);
                            }
                        }
                        if (null != result.data.bill_consume_info) {
                            for (BillAllInfoBean billAllInfoBean : result.data.bill_consume_info) {
                                billAllInfoBean.consumeType = ConsumeType.CARD;
                                list.add(billAllInfoBean);
                            }
                        }
                        if (null != result.data.bill_giveaway_info) {
                            for (BillAllInfoBean billAllInfoBean : result.data.bill_giveaway_info) {
                                billAllInfoBean.consumeType = ConsumeType.GIVE;
                                list.add(billAllInfoBean);
                            }
                        }
                        if (null != result.data.bill_refund_info) {
                            for (BillAllInfoBean billAllInfoBean : result.data.bill_refund_info) {
                                billAllInfoBean.consumeType = ConsumeType.REFUND;
                                list.add(billAllInfoBean);
                            }
                        }
                        if (null != result.data.bill_repayment_info) {
                            for (BillAllInfoBean billAllInfoBean : result.data.bill_repayment_info) {
                                billAllInfoBean.consumeType = ConsumeType.REPAYMENT;
                                list.add(billAllInfoBean);
                            }
                        }
                        mAdapter.refresh(tempPos, list);
                    }
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                LogUtils.e(ex.getMessage());
                ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), ex.getMessage());
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });
    }


    @OnClick(R.id.tv_one)
    public void start(View v) {
        timeType = 0;
        showDateTimePickerDialog();
    }

    @OnClick(R.id.tv_two)
    public void end(View v) {
        timeType = 1;
        showDateTimePickerDialog();
    }

    private void showDateTimePickerDialog() {
        timePickerView.show();
    }

    @OnClick(R.id.ll_filter)
    public void filter(View v) {
        DeviceUtils.hideSoftKeyboard(v, mContext);
        openRightLayout(v);
    }

    @OnClick(R.id.tv_positive)
    public void positive(View v) {
        openRightLayout(v);

        if(!TextUtils.isEmpty(tvOne.getText().toString())){
            if(TextUtils.isEmpty(tvTwo.getText().toString())){
                showToast("请选择结束时间");
                return;
            }
        }
        if(!TextUtils.isEmpty(tvTwo.getText().toString())){
            if(TextUtils.isEmpty(tvOne.getText().toString())){
                showToast("请选择开始时间");
                return;
            }
        }

        if (requestStoreBill.end - requestStoreBill.start < 0) {
            showTextDialog("结束时间不能小于开始时间", false);
            return;
        }
        tvAmount.setText(tvOne.getText().toString() + "~" + tvTwo.getText().toString());
        tvAmount.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tvAmount.setSingleLine(true);
        tvAmount.setSelected(true);
        tvAmount.setFocusable(true);
        tvAmount.setFocusableInTouchMode(true);
        presenter.loadData(true);
    }

    @OnClick(R.id.tv_negative)
    public void negative(View v) {
        requestStoreBill.start = 0;
        requestStoreBill.end = 0;
        tvOne.setText("");
        tvTwo.setText("");
    }

    public void openRightLayout(View view) {
        if (drawerLayout.isDrawerOpen(main_right_drawer_layout)) {
            drawerLayout.closeDrawer(main_right_drawer_layout);
        } else {
            drawerLayout.openDrawer(main_right_drawer_layout);
        }
    }


    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultBillManage>() {

            @Override
            public void onSuccess(List<ResultBillManage> result, int total, final boolean isRefresh) {
                if (isRefresh) {
                    tempPos = 0;
                    amount = total;
                    if (requestStoreBill.start == 0 && requestStoreBill.end == 0) {
                        tvAmount.setText("单据" + amount + "条 （1/" + amount + ")");
                    }else{
                        tvAmount.setText(tvOne.getText().toString() + "~" + tvTwo.getText().toString() + "（" + (tempPos + 1) + "/" + amount + ")");
                    }
                    tvAmount.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    tvAmount.setSingleLine(true);
                    tvAmount.setSelected(true);
                    tvAmount.setFocusable(true);
                    tvAmount.setFocusableInTouchMode(true);
                    if (null != result && result.size() > 0) {
                        mAdapter.notifyDataSetChanged(result);
                        staffMgrRecordDetail(result.get(0));
                    }
                } else {
                    int pos = mAdapter.getItemCount();
                    mAdapter.addAll(result);
                    tempPos = tempPos + 1;
                    amount = total;
                    mRecyclerView.scrollToPosition(pos);
                    staffMgrRecordDetail(mAdapter.getItem(tempPos));
                    if (requestStoreBill.start == 0 && requestStoreBill.end == 0) {
                        tvAmount.setText("单据" + amount + "条 " + "（" + (tempPos + 1) + "/" + amount + ")");
                    } else {
                        tvAmount.setText(tvOne.getText().toString() + "~" + tvTwo.getText().toString() + "（" + (tempPos + 1) + "/" + amount + ")");
                    }
                }

            }

            @Override
            public void onFailed(boolean isRefresh) {
                showToast("加载失败，请稍候重试");
            }

            @Override
            public void onCompleted(boolean isRefresh) {
                dismissLoadingDialog();
            }

            @Override
            public void onEmptyData() {
                mAdapter.clear();
                tempPos = 0;
                amount = 0;
                if(requestStoreBill.start == 0 && requestStoreBill.end == 0){
                    tvAmount.setText("单据" + amount + "条 ");
                }else{
                    tvAmount.setText(tvOne.getText().toString() + "~" + tvTwo.getText().toString() + " (" + tempPos + "/"+ amount + ")");
                }
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");

            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultBillManage>>> subscriber, int pageIndex, int pageSize) {
                showLoadingDialog("加载中...");
                AppModelFactory.model().getStoreConsumeBill(requestStoreBill, storeBean.group_id, pageIndex, pageSize, subscriber);
            }
        });
    }

}

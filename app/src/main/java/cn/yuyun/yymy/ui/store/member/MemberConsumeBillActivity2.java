package cn.yuyun.yymy.ui.store.member;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingmouren.layoutmanagergroup.viewpager.OnViewPagerListener;
import com.dingmouren.layoutmanagergroup.viewpager.ViewPagerLayoutManager;
import com.example.http.DataBean;
import com.example.http.PageInfo;
import com.example.http.Presenter2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lzz.utils.DeviceUtils;
import cn.lzz.utils.LogUtils;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.bean.Sex;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestStoreBill;
import cn.yuyun.yymy.http.result.ResultBillManage;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.utils.GlideHelper;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc
 * @date
 */
public class MemberConsumeBillActivity2 extends BaseActivity {


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
    @BindView(R.id.et1)
    EditText et1;
    @BindView(R.id.et2)
    EditText et2;
    @BindView(R.id.rg_one)
    LinearLayout rgOne;
    @BindView(R.id.rv_card)
    RecyclerView rvCard;
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
    private ViewPagerLayoutManager mLayoutManager;

    private Presenter2<ResultBillManage> presenter;

    private RequestStoreBill requestStoreBill = new RequestStoreBill();

    private MemberBillAdapter mAdapter;

    private ResultMemberBean memberBean;

    public static Intent startMemberConsumeBillActivity(Context context, ResultMemberBean memberBean) {
        return new Intent(context, MemberConsumeBillActivity2.class).putExtra(EXTRA_DATA, memberBean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_consume_bill2);
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

        if (!TextUtils.isEmpty(memberBean.memberAvatar)) {
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
        mLayoutManager = new ViewPagerLayoutManager(this, OrientationHelper.VERTICAL);
        mLayoutManager.setOnViewPagerListener(new OnViewPagerListener() {
            @Override
            public void onInitComplete() {

            }

            @Override
            public void onPageRelease(boolean isNext,int position) {
                LogUtils.e("释放位置:" + position +" 下一页:" + isNext);
               /* int index = 0;
                if (isNext){
                    index = 0;
                }else {
                    index = 1;
                }
                releaseVideo(index);*/
            }

            @Override
            public void onPageSelected(int position,boolean isBottom) {
                LogUtils.e("选中位置:" + position + "  是否是滑动到底部:"+isBottom);
//                playVideo(0);
            }


            public void onLayoutComplete() {
                LogUtils.e("onLayoutComplete");
//                playVideo(0);
            }

        });
        mAdapter = new MemberBillAdapter(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        initLayout();
    }

    @OnClick(R.id.ll_filter)
    public void filter(View v) {
        DeviceUtils.hideSoftKeyboard(v, mContext);
        openRightLayout(v);
    }

    @OnClick(R.id.tv_positive)
    public void positive(View v) {
        openRightLayout(v);
        requestStoreBill.gerate_than = et1.getText().toString().trim();
        requestStoreBill.less_than = et2.getText().toString().trim();
        presenter.loadData(true);
    }

    @OnClick(R.id.tv_negative)
    public void negative(View v) {
        requestStoreBill.gerate_than = "";
        requestStoreBill.less_than = "";
        requestStoreBill.level_id = "";
    }

    public void openRightLayout(View view) {
        if (drawerLayout.isDrawerOpen(main_right_drawer_layout)) {
            drawerLayout.closeDrawer(main_right_drawer_layout);
        } else {
            drawerLayout.openDrawer(main_right_drawer_layout);
        }
    }

    private int amount;

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultBillManage>() {

            @Override
            public void onSuccess(List<ResultBillManage> result, int total, final boolean isRefresh) {
                if(TextUtils.isEmpty(requestStoreBill.gerate_than) && TextUtils.isEmpty(requestStoreBill.less_than)){
                    amount = total;
                    tvAmount.setText("单据" + amount +"条");
                }else{
                    tvAmount.setText("单据" + amount +"条 已筛选" + total + "条（1/" + total + ")");
                }
                if (null != result && result.size() > 0) {
                    mAdapter.addAll(result);
                }
            }

            @Override
            public void onFailed(boolean isRefresh) {
                showToast("加载失败，请稍候重试");
            }

            @Override
            public void onCompleted(boolean isRefresh) {

            }

            @Override
            public void onEmptyData() {

            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");

            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultBillManage>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getStoreConsumeBill(requestStoreBill, LoginInfoPrefs.getInstance(mContext).getGroupId(), pageIndex, pageSize, subscriber);
            }
        });
    }


}

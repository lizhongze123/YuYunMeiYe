package cn.yuyun.yymy.ui.home.member.memberdata;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.PageInfo;
import com.example.http.ProgressSubscriber;
import com.githang.statusbar.StatusBarCompat;
import com.moxun.tagcloudlib.view.TagCloudView;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.DensityUtils;
import cn.lzz.utils.LogUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.bean.Expired;
import cn.yuyun.yymy.constan.Constans;
import cn.yuyun.yymy.event.RefreshMemberDataEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestMemberAsset;
import cn.yuyun.yymy.http.result.ResultDepositAsset;
import cn.yuyun.yymy.http.result.ResultLabel;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.http.result.ResultPackageAsset;
import cn.yuyun.yymy.http.result.ResultProduct;
import cn.yuyun.yymy.http.result.ResultServiceAsset;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.home.member.AppointmentListActivity;
import cn.yuyun.yymy.ui.home.member.AssetDepositAdapter;
import cn.yuyun.yymy.ui.home.member.AssetDepositDetailActivity;
import cn.yuyun.yymy.ui.home.member.AssetPacketAdapter;
import cn.yuyun.yymy.ui.home.member.AssetPacketDetailActivity;
import cn.yuyun.yymy.ui.home.member.AssetProductAdapter;
import cn.yuyun.yymy.ui.home.member.AssetProductDetailActivity;
import cn.yuyun.yymy.ui.home.member.AssetServiceAdapter;
import cn.yuyun.yymy.ui.home.member.AssetServiceDetailActivity;
import cn.yuyun.yymy.ui.home.member.CallbackListActivity;
import cn.yuyun.yymy.ui.home.member.CommunicationListActivity;
import cn.yuyun.yymy.ui.home.member.MemberXFileActivity;
import cn.yuyun.yymy.ui.home.member.RecommendListActivity;
import cn.yuyun.yymy.ui.home.member.SelectMemberLabelActivity;
import cn.yuyun.yymy.ui.home.member.StoredvalueListActivity;
import cn.yuyun.yymy.ui.store.member.MemberConsumeBillActivity;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.ShadowDrawable;
import de.hdodenhof.circleimageview.CircleImageView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author
 * @desc
 * @date
 */

public class MemberDetailActivity extends BaseNoTitleActivity implements RadioGroup.OnCheckedChangeListener {

    private static final String APP_ID = "wx4c2fd70860912239";
    private IWXAPI api;
    public static final String TYPE_PUBLIC = "type_public";
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_store)
    TextView tvStore;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_storedValue)
    TextView tvStoredValue;
    @BindView(R.id.tv_accumulate)
    TextView tvAccumulate;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    @BindView(R.id.tv_arrears)
    TextView tvArrears;
    /* @BindView(R.id.srl)
     SwipeRefreshLayout srl;*/
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.rvOne)
    RecyclerView rvOne;
    @BindView(R.id.rvTwo)
    RecyclerView rvTwo;
    @BindView(R.id.rvThree)
    RecyclerView rvThree;
    @BindView(R.id.rvFour)
    RecyclerView rvFour;
    @BindView(R.id.rl_one)
    RelativeLayout rlOne;
    @BindView(R.id.rl_two)
    RelativeLayout rlTwo;
    @BindView(R.id.rl_three)
    RelativeLayout rlThree;
    @BindView(R.id.rl_four)
    RelativeLayout rlFour;
    @BindView(R.id.empty_one)
    EmptyLayout emptyOne;
    @BindView(R.id.empty_two)
    EmptyLayout emptyTwo;
    @BindView(R.id.empty_three)
    EmptyLayout emptyThree;
    @BindView(R.id.empty_four)
    EmptyLayout emptyFour;
    @BindView(R.id.ll_storedValue)
    LinearLayout llStoredValue;
    @BindView(R.id.ll_accumulate)
    LinearLayout llAccumulate;
    @BindView(R.id.ll_arrears)
    LinearLayout llArrears;
    @BindView(R.id.iv_addLabel)
    ImageView ivAddLabel;
    @BindView(R.id.fragment_tagcloud)
    TagCloudView tagCloudView;
    @BindView(R.id.iv_xFile)
    ImageView ivXFile;

    private ResultMemberBean memberBean;
    private String memberId;
    private StoreBean storeBean;
    private String type;

    private AssetServiceAdapter assetServiceAdapter;
    private AssetPacketAdapter assetPacketAdapter;
    private AssetProductAdapter assetProductAdapter;
    private AssetDepositAdapter assetDepositAdapter;

    public static Intent startMemberDetailActivityWithId(Context context, String memberId, StoreBean storeBean) {
        return new Intent(context, MemberDetailActivity.class).putExtra(EXTRA_DATA, memberId).putExtra(EXTRA_DATA2, storeBean);
    }

    public static Intent startMemberDetailActivityFromPublic(Context context, String memberId, StoreBean storeBean) {
        return new Intent(context, MemberDetailActivity.class).putExtra(EXTRA_TYPE, TYPE_PUBLIC).putExtra(EXTRA_DATA, memberId).putExtra(EXTRA_DATA2, storeBean);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshMemberDataEvent(RefreshMemberDataEvent event) {
        getMemberDetail();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.title_mine));
        setContentView(R.layout.activity_member_detail2);
        EvenManager.register(this);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        memberId = getIntent().getStringExtra(EXTRA_DATA);
        storeBean = (StoreBean) getIntent().getSerializableExtra(EXTRA_DATA2);
        type = getIntent().getStringExtra(EXTRA_TYPE);
        initViews();
        getMemberDetail();
        getMemberLabel();
        assetService();
        assetPackage();
        assetProduct();
        assetDeposit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EvenManager.unregister(this);
    }

    private void initViews() {
        titleBar.setTilte("会员详情");

        if (null != memberBean) {
            initViewDatas(memberBean);
        }
     /*   srl = (SwipeRefreshLayout) findViewById(R.id.srl);
        srl.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMemberDetail();
            }
        });*/

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memberDetailShare();
            }
        });

        radioGroup.setOnCheckedChangeListener(this);
        rvOne.setLayoutManager(new LinearLayoutManager(mContext));
        rvTwo.setLayoutManager(new LinearLayoutManager(mContext));
        rvThree.setLayoutManager(new LinearLayoutManager(mContext));
        rvFour.setLayoutManager(new LinearLayoutManager(mContext));
        assetServiceAdapter = new AssetServiceAdapter(mContext);
        assetPacketAdapter = new AssetPacketAdapter(mContext);
        assetProductAdapter = new AssetProductAdapter(mContext);
        assetDepositAdapter = new AssetDepositAdapter(mContext);

        rvOne.setAdapter(assetServiceAdapter);
        rvTwo.setAdapter(assetPacketAdapter);
        rvThree.setAdapter(assetProductAdapter);
        rvFour.setAdapter(assetDepositAdapter);

        assetServiceAdapter.setOnItemClickListener(new AssetServiceAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultServiceAsset bean, int position) {
                startActivity(new Intent(AssetServiceDetailActivity.startAssetServiceDetailActivity(mContext, bean)));
            }
        });
        assetProductAdapter.setOnItemClickListener(new AssetProductAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultProduct bean, int position) {
                startActivity(new Intent(AssetProductDetailActivity.startAssetDetailActivity(mContext, bean)));
            }
        });
        assetPacketAdapter.setOnItemClickListener(new AssetPacketAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultPackageAsset bean, int position) {
                startActivity(AssetPacketDetailActivity.startAssetPacketDetailActivity(mContext, bean));
            }
        });
        assetDepositAdapter.setOnItemClickListener(new AssetDepositAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultDepositAsset bean, int position) {
                startActivity(AssetDepositDetailActivity.startAssetDepositDetailActivity(mContext, bean));
            }
        });
        ivAddLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constans.isStoreLoginer) {
                    startActivityForResult(SelectMemberLabelActivity.startSelectMemberLabelActivity(mContext, memberBean), 1000);
                } else {
                    showToast(getString(R.string.PARTNER));
                }
            }
        });
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private void getMemberDetail() {
        AppModelFactory.model().memberDetail(storeBean.group_id, memberId, new ProgressSubscriber<DataBean<ResultMemberBean>>(mContext) {
            @Override
            public void onNext(DataBean<ResultMemberBean> result) {
                if (null != result) {
                    memberBean = result.data;
                    initViewDatas(memberBean);
                }
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast("请求失败，请稍后重试");
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(mContext.getString(R.string.error_no_network));
            }
        });
    }

    private void memberDetailShare() {
        AppModelFactory.model().memberDetailShare(storeBean.group_id, memberId, new ProgressSubscriber<DataBean<String>>(mContext) {
            @Override
            public void onNext(DataBean<String> result) {
                if (!TextUtils.isEmpty(result.data)) {
                    WXWebpageObject webpage = new WXWebpageObject();
                    webpage.webpageUrl = result.data;
                    final WXMediaMessage msg = new WXMediaMessage(webpage);
                    msg.title = memberBean.memberName;

                    if(TextUtils.isEmpty(memberBean.memberLevelName)){
                        msg.description = "会员级别:";
                    }else{
                        msg.description = "会员级别:" + memberBean.memberLevelName;
                    }

                    if(TextUtils.isEmpty(memberBean.member_in_sp_name)){
                        msg.description = "\n门店:";
                    }else{
                        msg.description = "\n门店:" + memberBean.member_in_sp_name;
                    }
                    if(TextUtils.isEmpty(memberBean.memberAvatar)){
                        Bitmap thumb = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.avatar_default_female);
                        if (thumb != null) {
                            Bitmap mBp = Bitmap.createScaledBitmap(thumb, 120, 120, true);
                            msg.thumbData = bmpToByteArray(thumb, true);
                            thumb.recycle();
                        }
                    }else{
                        Bitmap thumb = BitmapFactory.decodeFile(memberBean.memberAvatar);
                        if (thumb != null) {
                            Bitmap mBp = Bitmap.createScaledBitmap(thumb, 120, 120, true);
                            msg.thumbData = bmpToByteArray(thumb, true);
                            thumb.recycle();
                        }
                    }
                    //创建一个请求对象
                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.message = msg;
                    //req.scene = SendMessageToWX.Req.WXSceneTimeline;    //设置发送到朋友圈
                    //设置发送给朋友
                    req.scene = SendMessageToWX.Req.WXSceneSession;
                    //用于在回调中区分是哪个分享请求
                    req.transaction = "设置一个tag";
                    //如果调用成功微信,会返回true
                    if(api == null){
                        api = WXAPIFactory.createWXAPI(mContext, APP_ID, true);
                        api.registerApp(APP_ID);
                    }
                    if(api == null){
                        LogUtils.e("分享结果----null");
                    }else{
                        boolean successed = api.sendReq(req);
                        LogUtils.e("分享结果----" + successed);
                    }
                }
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast("请求失败，请稍后重试");
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(mContext.getString(R.string.error_no_network));
            }
        });
    }

    private void assetProduct() {
        RequestMemberAsset body = new RequestMemberAsset();
        body.asset_type = 1;
        body.member_id = memberId;
        AppModelFactory.model().assetProduct(storeBean.group_id, 1, 10000, body, new ProgressSubscriber<DataBean<PageInfo<ResultProduct>>>(mContext) {
            @Override
            public void onNext(DataBean<PageInfo<ResultProduct>> result) {
                if (null != result.data) {
                    if (result.data.dataLsit.size() == 0) {
                        emptyThree.setVisibility(View.VISIBLE);
                        emptyThree.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
                        rvThree.setVisibility(View.GONE);
                    } else {
                        emptyThree.setVisibility(View.GONE);
                        rvThree.setVisibility(View.VISIBLE);
                        assetProductAdapter.addAll(result.data.dataLsit);
                    }
                }
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast("请求失败，请稍后重试");
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(mContext.getString(R.string.error_no_network));
            }
        });
    }

    private void assetService() {
        RequestMemberAsset body = new RequestMemberAsset();
        body.asset_type = 2;
        body.member_id = memberId;
        AppModelFactory.model().assetService(storeBean.group_id, 1, 10000, body, new ProgressSubscriber<DataBean<PageInfo<ResultServiceAsset>>>(mContext) {
            @Override
            public void onNext(DataBean<PageInfo<ResultServiceAsset>> result) {
                if (null != result.data) {
                    if (result.data.dataLsit.size() == 0) {
                        emptyOne.setVisibility(View.VISIBLE);
                        emptyOne.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
                        rvOne.setVisibility(View.GONE);
                    } else {
                        emptyOne.setVisibility(View.GONE);
                        rvOne.setVisibility(View.VISIBLE);
                        assetServiceAdapter.addAll(result.data.dataLsit);
                    }
                }
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast("请求失败，请稍后重试");
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(mContext.getString(R.string.error_no_network));
            }
        });
    }

    private void assetPackage() {
        RequestMemberAsset body = new RequestMemberAsset();
        body.asset_type = 3;
        body.member_id = memberId;
        AppModelFactory.model().assetPackage(storeBean.group_id, 1, 10000, body, new ProgressSubscriber<DataBean<PageInfo<ResultPackageAsset>>>(mContext) {
            @Override
            public void onNext(DataBean<PageInfo<ResultPackageAsset>> result) {
                if (null != result.data) {
                    if (result.data.dataLsit.size() == 0) {
                        emptyTwo.setVisibility(View.VISIBLE);
                        emptyTwo.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
                        rvTwo.setVisibility(View.GONE);
                    } else {
                        emptyTwo.setVisibility(View.GONE);
                        rvTwo.setVisibility(View.VISIBLE);
                        assetPacketAdapter.addAll(result.data.dataLsit);
                    }
                }
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast("请求失败，请稍后重试");
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(mContext.getString(R.string.error_no_network));
            }
        });
    }

    private void assetDeposit() {
        AppModelFactory.model().assetDeposit(storeBean.group_id, 1, 10000, memberId, new ProgressSubscriber<DataBean<PageInfo<ResultDepositAsset>>>(mContext) {
            @Override
            public void onNext(DataBean<PageInfo<ResultDepositAsset>> result) {
                if (null != result.data) {
                    if (result.data.dataLsit.size() == 0) {
                        emptyFour.setVisibility(View.VISIBLE);
                        emptyFour.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
                        rvFour.setVisibility(View.GONE);
                    } else {
                        emptyFour.setVisibility(View.GONE);
                        rvFour.setVisibility(View.VISIBLE);
                        assetDepositAdapter.addAll(result.data.dataLsit);
                    }
                }
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast("请求失败，请稍后重试");
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(mContext.getString(R.string.error_no_network));
            }
        });
    }

    private void getMemberLabel() {
        AppModelFactory.model().getMemberLabel(2, memberId, new ProgressSubscriber<DataBean<List<ResultLabel>>>(mContext) {

            @Override
            public void onNext(DataBean<List<ResultLabel>> result) {
                if (null != result) {
                    if (null != result.data) {
                        if (result.data.size() == 0) {
                            ivAddLabel.setVisibility(View.VISIBLE);
                            tagCloudView.setVisibility(View.GONE);
                            return;
                        }
                        ivAddLabel.setVisibility(View.GONE);
                        tagCloudView.setVisibility(View.VISIBLE);
                        String[] strings = new String[20];
                        int k = 0;
                        for (int i = 0; i < strings.length; i++) {
                            if (k >= result.data.size()) {
                                k = 0;
                            }
                            strings[i] = result.data.get(k).kv_value;
                            k++;
                        }
                        TextTagsAdapter adapter = new TextTagsAdapter(strings);
                        adapter.setOnItemClickListener(new TextTagsAdapter.OnMyItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                startActivityForResult(SelectMemberLabelActivity.startSelectMemberLabelActivity(mContext, memberBean), 1000);
                            }
                        });
                        tagCloudView.setAdapter(adapter);
                    } else {
                        ivAddLabel.setVisibility(View.VISIBLE);
                        tagCloudView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast("请求失败，请稍后重试");
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

    private void initViewDatas(ResultMemberBean result) {
        if (null != memberBean) {
            if (!TextUtils.isEmpty(memberBean.memberAvatar)) {
                if (memberBean.memberAvatar.startsWith(getString(R.string.HTTP))) {
                    GlideHelper.displayImage(mContext, memberBean.memberAvatar, ivAvatar, memberBean.member_sex.resId);
                } else {
                    GlideHelper.displayImage(mContext, getString(R.string.image_url_prefix) + memberBean.memberAvatar, ivAvatar, memberBean.member_sex.resId);
                }
            } else {
                GlideHelper.displayImage(mContext, memberBean.member_sex.resId, ivAvatar);
            }
        }
        TextViewUtils.setTextOrEmpty(tvName, result.memberName);
        TextViewUtils.setTextOrEmpty(tvStore, "门店:" + result.member_in_sp_name);
        TextViewUtils.setTextOrEmpty(tvTime, "到店时间:" + DateTimeUtils.StringToDate(result.memberConsumptionLatestTime, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_DATE_UI_TWO));
        if (TextUtils.isEmpty(result.memberLevelName)) {
            tvPosition.setVisibility(View.GONE);
        } else {
            tvPosition.setVisibility(View.VISIBLE);
            tvPosition.setText("(" + result.memberLevelName + ")");
        }
        TextViewUtils.setTextOrEmpty(tvStoredValue, "" + StringFormatUtils.formatDecimal(result.memberStoredValue));
        TextViewUtils.setTextOrEmpty(tvArrears, "" + StringFormatUtils.formatDecimal(result.memberArrears));
        TextViewUtils.setTextOrEmpty(tvAccumulate, "" + StringFormatUtils.formatDecimal(result.memberAccumulate));

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.rb_one:
                rlOne.setVisibility(View.VISIBLE);
                rlTwo.setVisibility(View.GONE);
                rlThree.setVisibility(View.GONE);
                rlFour.setVisibility(View.GONE);
                break;
            case R.id.rb_two:
                rlOne.setVisibility(View.GONE);
                rlTwo.setVisibility(View.VISIBLE);
                rlThree.setVisibility(View.GONE);
                rlFour.setVisibility(View.GONE);
                break;
            case R.id.rb_three:
                rlOne.setVisibility(View.GONE);
                rlTwo.setVisibility(View.GONE);
                rlThree.setVisibility(View.VISIBLE);
                rlFour.setVisibility(View.GONE);
                break;
            case R.id.rb_four:
                rlOne.setVisibility(View.GONE);
                rlTwo.setVisibility(View.GONE);
                rlThree.setVisibility(View.GONE);
                rlFour.setVisibility(View.VISIBLE);
                break;
            default:
        }
    }

    @OnClick({R.id.iv_xFile, R.id.iv_edit, R.id.ll_storedValue, R.id.ll1, R.id.ll2, R.id.ll4, R.id.ll5, R.id.ll6})
    public void onClickItem(View view) {
        switch (view.getId()) {
            case R.id.iv_xFile:
                //私密档案
                startActivity(MemberXFileActivity.startMemberXFileActivity(mContext, memberBean, storeBean));
                break;
            case R.id.ll_storedValue:
                startActivity(StoredvalueListActivity.startStoredvalueListActivity(mContext, memberBean, storeBean));
                break;
            case R.id.iv_edit:
                if (null == type) {
                    startActivity(MemberDataActivity.startMemberDataActivity(mContext, memberBean));
                } else if (type.equals(TYPE_PUBLIC)) {
                    startActivity(MemberDataActivity.startMemberDataActivityFromPublic(mContext, memberBean));
                }
                break;
            case R.id.ll1:
                startActivity(MemberConsumeBillActivity.startMemberConsumeBillActivity(mContext, memberBean, storeBean));
                break;
            case R.id.ll2:
                startActivity(RecommendListActivity.startRecommendListActivity(mContext, memberBean.memberId, storeBean));
                break;
//            case R.id.ll3:
//                startActivity(AssetActivity.startAssetActivity(mContext, memberBean));
//                break;
            case R.id.ll4:
                startActivity(CommunicationListActivity.startCommunicationRecordListActivity(mContext, memberBean, storeBean));
                break;
            case R.id.ll5:
                startActivity(CallbackListActivity.startCallbackListActivity(mContext, memberBean));
                break;
            case R.id.ll6:
                startActivity(AppointmentListActivity.startAppointmentListActivity(mContext, memberBean, storeBean));
                break;
            default:
        }
    }


    @OnCheckedChanged({R.id.rb_oneIng, R.id.rb_oneFinish, R.id.rb_twoIng, R.id.rb_twoFinish})
    public void OnCheckedChangeListener(CompoundButton view, boolean ischanged) {
        switch (view.getId()) {
            case R.id.rb_oneIng:
                if (ischanged) {
                    assetServiceAdapter.setType(0);
                }
                break;
            case R.id.rb_oneFinish:
                if (ischanged) {
                    assetServiceAdapter.setType(1);
                }
                break;
            case R.id.rb_twoIng:
                if (ischanged) {
                    assetPacketAdapter.setType(0);
                }
                break;
            case R.id.rb_twoFinish:
                if (ischanged) {
                    assetPacketAdapter.setType(1);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getMemberLabel();
        }
    }


}

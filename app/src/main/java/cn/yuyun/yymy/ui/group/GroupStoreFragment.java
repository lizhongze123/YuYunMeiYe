package cn.yuyun.yymy.ui.group;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.NoneProgressSubscriber;
import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.DensityUtils;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseNoTitleFragment;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestClassfiyStore;
import cn.yuyun.yymy.http.request.RequestSpDetail;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean.OgServiceplacesRspListBean;
import cn.yuyun.yymy.http.result.ResultGroup;
import cn.yuyun.yymy.http.result.ResultSpDetail;
import cn.yuyun.yymy.sp.StorePrefs;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.store.AmountConsumeListActivity;
import cn.yuyun.yymy.ui.store.AmountTotalListActivity;
import cn.yuyun.yymy.ui.store.ResultsListActivity;
import cn.yuyun.yymy.ui.store.StoreAnalysisActivity;
import cn.yuyun.yymy.ui.store.StoreAreaAdapter;
import cn.yuyun.yymy.ui.store.StoreChildAdapter;
import cn.yuyun.yymy.ui.store.StoreDetailActivity;
import cn.yuyun.yymy.ui.store.StoreDetailAllActivity;
import cn.yuyun.yymy.view.ShadowDrawable;
import cn.yuyun.yymy.view.dialog.BottomDialog;
import cn.yuyun.yymy.view.lineview.HistogramMode;
import cn.yuyun.yymy.view.lineview.HistogramView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author 门店Fragment
 * @desc
 * @date
 */
public class GroupStoreFragment extends BaseNoTitleFragment {

    @BindView(R.id.layout_permission)
    LinearLayout layoutPermission;
    @BindView(R.id.rv_top)
    RecyclerView rvTop;
    @BindView(R.id.iv_filter)
    ImageView ivFilter;
    @BindView(R.id.iv_personTime)
    ImageView ivPersonTime;
    @BindView(R.id.tv_peopleTimeDesc)
    TextView tvPeopleTimeDesc;
    @BindView(R.id.tv_peopleTime)
    TextView tvPeopleTime;
    @BindView(R.id.ll_personTime)
    LinearLayout llPersonTime;
    @BindView(R.id.iv_manualfee)
    ImageView ivManualfee;
    @BindView(R.id.tv_manualfeeDesc)
    TextView tvManualfeeDesc;
    @BindView(R.id.tv_newMember)
    TextView tvNewMember;
    @BindView(R.id.ll_newMember)
    LinearLayout llNewMember;
    @BindView(R.id.iv_personNum)
    ImageView ivPersonNum;
    @BindView(R.id.tv_peopleNumDesc)
    TextView tvPeopleNumDesc;
    @BindView(R.id.tv_peopleNum)
    TextView tvPeopleNum;
    @BindView(R.id.ll_personNum)
    LinearLayout llPersonNum;
    @BindView(R.id.rl_arrears)
    RelativeLayout rlArrears;
    @BindView(R.id.ll_top)
    LinearLayout llTop;
    @BindView(R.id.iv_arrears)
    ImageView ivArrears;
    @BindView(R.id.tv_arrearsDesc)
    TextView tvArrearsDesc;
    @BindView(R.id.tv_arrears)
    TextView tvArrears;
    @BindView(R.id.histogramView)
    HistogramView histogramView;
    @BindView(R.id.rv_normal)
    RecyclerView rvNormal;
    @BindView(R.id.rv_stop)
    RecyclerView rvStop;
    @BindView(R.id.tv_normal)
    TextView tvNormal;
    @BindView(R.id.tv_stop)
    TextView tvStop;
    @BindView(R.id.tv_normal2)
    TextView tvNormal2;
    @BindView(R.id.tv_stop2)
    TextView tvStop2;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.iv_backBlack)
    ImageView ivBackBlack;
    private int timeType = 0;
    private TimePickerView timePickerView;
    private LinearLayoutManager mLinearLayoutManager;

    private BottomDialog dialog;

    private long start;
    private long end;

    private StoreChildAdapter mStopAdapter;
    private StoreChildAdapter mNormalAdapter;
    private StoreAreaAdapter storeAreaAdapter;
    private Context mContext;
    private List<String> intentSpIdList = new ArrayList<>();
    private List<ResultClassfiyStoreBean> mResult;

    /**保存门店id用来获取门店信息*/
    private List<String> tempSpIdList = new ArrayList<>();

    private ResultGroup resultGroup;

    @Override
    protected void onBindViewBefore(View root) {
        super.onBindViewBefore(root);
        mContext = getContext();
    }

    @Override
    protected int getTheLayoutId() {
        return R.layout.fragment_store_group;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onResume() {
        StatusBarCompat.setStatusBarColor(getActivity(), getResources().getColor(R.color.title_bg));
        super.onResume();
    }

    public static GroupStoreFragment newInstance(ResultGroup resultGroup) {
        GroupStoreFragment fragment = new GroupStoreFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATA, resultGroup);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        if (getArguments() != null) {
            resultGroup = (ResultGroup) getArguments().getSerializable(EXTRA_DATA);
        }
    }


    @Override
    protected void initViews(View root) {
        super.initViews(root);
        titleBar.setLineIsVisible(View.GONE);
        tvNormal2.setVisibility(View.VISIBLE);
        tvStop2.setVisibility(View.VISIBLE);
        tvNormal.setText(resultGroup.name);
        tvStop.setText(resultGroup.name);
        ShadowDrawable.setShadowDrawable(llPersonTime, Color.parseColor("#ffffff"), DensityUtils.dip2px(getContext(), 6),
                Color.parseColor("#22000000"),
                DensityUtils.dip2px(getContext(), 3), 0, 0);
        ShadowDrawable.setShadowDrawable(llNewMember, Color.parseColor("#ffffff"), DensityUtils.dip2px(getContext(), 6),
                Color.parseColor("#22000000"),
                DensityUtils.dip2px(getContext(), 3), 0, 0);
        ShadowDrawable.setShadowDrawable(llPersonNum, Color.parseColor("#ffffff"), DensityUtils.dip2px(getContext(), 6),
                Color.parseColor("#22000000"),
                DensityUtils.dip2px(getContext(), 3), 0, 0);
        ShadowDrawable.setShadowDrawable(rlArrears, Color.parseColor("#ffffff"), DensityUtils.dip2px(getContext(), 6),
                Color.parseColor("#22000000"),
                DensityUtils.dip2px(getContext(), 3), 0, 0);

        rvNormal.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvStop.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        rvTop.setLayoutManager(mLinearLayoutManager);
        mNormalAdapter = new StoreChildAdapter(mContext);
        mStopAdapter = new StoreChildAdapter(mContext);
        mNormalAdapter.setOnItemClickListener(new StoreChildAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(OgServiceplacesRspListBean bean, int position) {
                if (position == 0 && storeAreaAdapter.getPosition() == 0) {
                    startActivity(StoreDetailAllActivity.startStoreDetailActivity(mContext, tempSpIdList, mResult.get(0).ogServiceplacesRspList, bean.group_id));
                } else {
                    StoreBean storeBean = new StoreBean();
                    storeBean.spId = bean.sp_id;
                    storeBean.group_id = bean.group_id;
                    storeBean.spName = bean.sp_name;
                    storeBean.thumb_url = bean.thumb_url;
                    storeBean.chairmantel = bean.chairmantel;
                    storeBean.chairman = bean.chairman;
                    startActivity(StoreDetailActivity.startStoreDetailActivity(mContext, storeBean));
                }
            }
        });
        storeAreaAdapter = new StoreAreaAdapter(mContext);
        storeAreaAdapter.setOnItemClickListener(new StoreAreaAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(int position) {
                List<String> spList = new ArrayList<>();
                List<OgServiceplacesRspListBean> normalList =  new ArrayList<>();
                List<OgServiceplacesRspListBean> stopList =  new ArrayList<>();
                for (int i = 0; i < mResult.get(position).ogServiceplacesRspList.size(); i++) {
                    OgServiceplacesRspListBean bean = mResult.get(position).ogServiceplacesRspList.get(i);
                    //不要把总店的id添加进来
                    if (bean.ogType != 1) {
                        spList.add(bean.sp_id);
                    }
                    if (bean.status == 1) {
                        normalList.add(bean);
                    } else if (bean.status == 0) {
                        stopList.add(bean);
                    }
                }
                tempSpIdList = spList;
                intentSpIdList = spList;
                mNormalAdapter.notifyDataSetChanged(normalList);
                mStopAdapter.notifyDataSetChanged(stopList);
                tvNormal2.setText("所管门店(" + normalList.size() + ")");
                tvStop2.setText("停用门店(" + stopList.size() + ")");
                dialog.setPosition(position);
                getSpDetails();
            }
        });
        rvNormal.setAdapter(mNormalAdapter);
        rvStop.setAdapter(mStopAdapter);
        rvTop.setAdapter(storeAreaAdapter);
        srl.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tvStart.setText(DateTimeUtils.getDateTimeText(DateTimeUtils.getTimesMonthMorning(), DateTimeUtils.FORMAT_DATE_UI));
                tvEnd.setText(DateTimeUtils.getNowTime());
                start = DateTimeUtils.getTimesMonthMorning();
                end = DateTimeUtils.getNowTimeStamp();
                getSpDetails();
            }
        });
        ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != dialog){
                    dialog.show();
                }
            }
        });
        timePickerView = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (timeType == 0) {
                    tvStart.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATE_UI));
                    start = date.getTime() / 1000;
                } else {
                    tvEnd.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATE_UI));
                    end = date.getTime() / 1000;
                }
                if(end - start < 0){
                    showTextDialog("结束时间不能小于开始时间", false);
                    return;
                }
                getSpDetails();
            }
        }).build();
        tvStart.setText(DateTimeUtils.getDateTimeText(DateTimeUtils.getTimesMonthMorning(), DateTimeUtils.FORMAT_DATE_UI));
        tvEnd.setText(DateTimeUtils.getNowTime());
        start = DateTimeUtils.getTimesMonthMorning();
        end = DateTimeUtils.getNowTimeStamp();
        getClassfiyStoreList();
        root.findViewById(R.id.rl_analysis).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(StoreAnalysisActivity.startStoreAnalysisActivity(mContext, intentSpIdList, resultGroup.group_id));
            }
        });
    }

    private void getClassfiyStoreList() {
        RequestClassfiyStore requestClassfiyStore = new RequestClassfiyStore();
        AppModelFactory.model().getMyStoreList(resultGroup.group_id, requestClassfiyStore, new NoneProgressSubscriber<DataBean<List<ResultClassfiyStoreBean>>>(mContext) {

            @Override
            public void onCompleted() {
                if (srl.isRefreshing()) {
                    srl.setRefreshing(false);
                }
            }

            @Override
            public void onNext(DataBean<List<ResultClassfiyStoreBean>> result) {
                if (null != result) {
                    if (null != result.data) {
                        //缓存门店列表数据
                        Gson gson = new Gson();
                        String json = gson.toJson(result.data);
                        StorePrefs.getInstance(mContext).saveInfo(json);

                        //组合数据把所有门店添加到“全部”下
                        ResultClassfiyStoreBean bean = new ResultClassfiyStoreBean();
                        bean.name = "全部";
                        List<OgServiceplacesRspListBean> normalList =  new ArrayList<>();
                        List<OgServiceplacesRspListBean> stopList =  new ArrayList<>();
                        //头部大区数据
                        List<ResultClassfiyStoreBean> dataList = new ArrayList<>();
                        bean.ogServiceplacesRspList = new ArrayList<>();
                        for (int i = 0, iLength = result.data.size(); i < iLength; i++) {
                            for (int j = 0, jLength = result.data.get(i).ogServiceplacesRspList.size(); j < jLength; j++) {
                                OgServiceplacesRspListBean b = result.data.get(i).ogServiceplacesRspList.get(j);
                                intentSpIdList.add(b.sp_id);
                                if(b.status == 1){
                                    normalList.add(b);
                                }else if(b.status == 0){
                                    stopList.add(b);
                                }
                            }
                            //只有大区下有门店才添加进来
                            if(result.data.get(i).ogServiceplacesRspList.size() > 0){
                                dataList.add(result.data.get(i));
                            }
                        }

                        OgServiceplacesRspListBean b = new OgServiceplacesRspListBean();
                        b.sp_name = "全部";
                        b.status = 1;
                        b.chairman = "无";
                        b.group_id = resultGroup.group_id;
                        normalList.add(0, b);
                        dataList.add(0, bean);

                        bean.ogServiceplacesRspList.addAll(normalList);
                        bean.ogServiceplacesRspList.addAll(stopList);
                        mResult = dataList;
                        mNormalAdapter.notifyDataSetChanged(normalList);
                        mStopAdapter.notifyDataSetChanged(stopList);
                        tvNormal2.setText("所管门店(" + normalList.size() + ")");
                        tvStop2.setText("停用门店(" + stopList.size() + ")");

                        storeAreaAdapter.notifyDataSetChanged(dataList);

                        tempSpIdList = intentSpIdList;

                        if(tempSpIdList.size() == 0){
                            layoutPermission.setVisibility(View.VISIBLE);
                            return;
                        }

                        dialog = new BottomDialog(mContext, dataList);
                        dialog.setClickListener(new BottomDialog.OnClickListener() {
                            @Override
                            public void onClick(int position) {
                                storeAreaAdapter.setPosition(position);
                                ((LinearLayoutManager) rvTop.getLayoutManager()).scrollToPositionWithOffset(position, 0);
                                List<String> spList = new ArrayList<>();
                                List<OgServiceplacesRspListBean> normalList =  new ArrayList<>();
                                List<OgServiceplacesRspListBean> stopList =  new ArrayList<>();
                                for (int i = 0; i < mResult.get(position).ogServiceplacesRspList.size(); i++) {
                                    OgServiceplacesRspListBean bean = mResult.get(position).ogServiceplacesRspList.get(i);
                                    spList.add(bean.sp_id);
                                    if(bean.status == 1){
                                        normalList.add(bean);
                                    }else if(bean.status == 0){
                                        stopList.add(bean);
                                    }
                                }
                                mNormalAdapter.notifyDataSetChanged(normalList);
                                mStopAdapter.notifyDataSetChanged(stopList);
                                tempSpIdList = spList;
                                intentSpIdList = spList;
                                dialog.dismiss();
                                tvNormal2.setText("所管门店(" + normalList.size() + ")");
                                tvStop2.setText("停用门店(" + stopList.size() + ")");
                                getSpDetails();
                            }
                        });

                        getSpDetails();
                    }
                }
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(mContext.getString(R.string.error_no_network));
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast(ex.message);
            }
        });
    }

    private void getSpDetails() {
        RequestSpDetail body = new RequestSpDetail();
        body.start_date = start;
        body.end_date = end;
        body.group_id = resultGroup.group_id;
        body.sp_id_list = tempSpIdList;
        AppModelFactory.model().getSpDetails(body, new NoneProgressSubscriber<DataBean<ResultSpDetail>>(mContext) {

            @Override
            public void onCompleted() {
                if (srl.isRefreshing()) {
                    srl.setRefreshing(false);
                }
            }

            @Override
            public void onNext(DataBean<ResultSpDetail> result) {

                if (null != result.data) {
                    HistogramMode histogramMode1 = new HistogramMode();
                    HistogramMode histogramMode2 = new HistogramMode();
                    HistogramMode histogramMode3 = new HistogramMode();
                    histogramMode1.setColor(ResoureUtils.getColor(mContext, R.color.histogram_one));
                    histogramMode2.setColor(ResoureUtils.getColor(mContext, R.color.histogram_two));
                    histogramMode2.setColor(ResoureUtils.getColor(mContext, R.color.histogram_three));
                    double percnetTotal = result.data.totalAmount + result.data.amountConsume + result.data.storePerformance;
                    if (percnetTotal == 0) {
                        histogramMode1.setValue(0);
                        histogramMode2.setValue(0);
                        histogramMode3.setValue(0);
                    } else {
                        histogramMode1.setValue(result.data.totalAmount);
                        histogramMode2.setValue(result.data.amountConsume);
                        histogramMode3.setValue(result.data.storePerformance);
                    }
                    List<HistogramMode> data = new ArrayList<>();
                    data.add(histogramMode1);
                    data.add(histogramMode2);
                    data.add(histogramMode3);
                    histogramView.setDataList(data);
                    histogramView.setOnChartClickListener(new HistogramView.OnChartClickListener() {
                        @Override
                        public void onClick(HistogramMode bean, int pos) {
                            if(pos == 0){
                                startActivity(AmountTotalListActivity.startAmountTotalListActivity(mContext, intentSpIdList, resultGroup.group_id));
                            }else if(pos == 1){
                                startActivity(AmountConsumeListActivity.startAmountConsumeListActivity(mContext, intentSpIdList, resultGroup.group_id));
                            }else{
                                startActivity(ResultsListActivity.startFromTotalAmount(mContext, intentSpIdList, resultGroup.group_id));
                            }
                        }
                    });
                    TextViewUtils.setTextOrEmpty(tvArrears, StringFormatUtils.formatDecimal(result.data.arrears));
                    TextViewUtils.setTextOrEmpty(tvPeopleTime, result.data.person_times + "", "0");
                    TextViewUtils.setTextOrEmpty(tvNewMember, result.data.add_member + "", "0");
                    TextViewUtils.setTextOrEmpty(tvPeopleNum, result.data.person_number + "", "0");
                }

            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(mContext.getString(R.string.error_no_network));
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast(ex.message);
            }

        });
    }

    @OnClick(R.id.rl_start)
    public void start(View v) {
        timeType = 0;
        showDateTimePickerDialog();
    }

    @OnClick(R.id.rl_end)
    public void end(View v) {
        timeType = 1;
        showDateTimePickerDialog();
    }

    @OnClick(R.id.iv_backBlack)
    public void back(View v) {
        getActivity().finish();
    }

    private void showDateTimePickerDialog() {
        timePickerView.show();
    }

}

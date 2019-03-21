package cn.yuyun.yymy.ui.store;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;
import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lzz.utils.DateTimeUtils;
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
import cn.yuyun.yymy.http.result.ResultPermissionHq;
import cn.yuyun.yymy.http.result.ResultSpDetail;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.StorePrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.utils.MathUtils;
import cn.yuyun.yymy.view.dialog.BottomDialog;
import cn.yuyun.yymy.view.lineview.HistogramMode;
import cn.yuyun.yymy.view.lineview.HistogramView;

/**
 * @author
 * @desc
 * @date
 */
public class StoreFragment extends BaseNoTitleFragment {

    @BindView(R.id.layout_permission)
    LinearLayout layoutPermission;
    @BindView(R.id.rv_top)
    RecyclerView rvTop;
    @BindView(R.id.iv_filter)
    ImageView ivFilter;
    @BindView(R.id.iv_personTime)
    ImageView ivPersonTime;
    @BindView(R.id.tv_peopleTime)
    TextView tvPeopleTime;
    @BindView(R.id.ll_personTime)
    LinearLayout llPersonTime;
    @BindView(R.id.ll_storedValue)
    LinearLayout llStoredValue;
    @BindView(R.id.ll_canbeUsed)
    LinearLayout llCanbeUsed;
    @BindView(R.id.ll_arrears)
    LinearLayout llArrears;
    @BindView(R.id.iv_manualfee)
    ImageView ivManualfee;
    @BindView(R.id.tv_newMember)
    TextView tvNewMember;
    @BindView(R.id.ll_newMember)
    LinearLayout llNewMember;
    @BindView(R.id.iv_personNum)
    ImageView ivPersonNum;
    @BindView(R.id.tv_peopleNum)
    TextView tvPeopleNum;
    @BindView(R.id.tv_serviceNum)
    TextView tvServiceNum;
    @BindView(R.id.ll_personNum)
    LinearLayout llPersonNum;
    @BindView(R.id.ll_top)
    LinearLayout llTop;
    @BindView(R.id.iv_arrears)
    ImageView ivArrears;
    @BindView(R.id.tv_arrears)
    TextView tvArrears;
    @BindView(R.id.tv_canbeUsed)
    TextView tvCanbeUsed;
    @BindView(R.id.tv_storedValue)
    TextView tvStoredValue;
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
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_end)
    TextView tvEnd;
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
    /**总部和全部的权限*/
    public int permission = 0;
    /**包括了“全部”大区的所有数据*/
    private List<ResultClassfiyStoreBean> mAllResult;
    /**不包括“全部”大区的所有门店数据*/
    private List<OgServiceplacesRspListBean> mResult;
    /**头部大区数据*/
    private List<ResultClassfiyStoreBean> areaDataList = new ArrayList<>();
    /**
     * 保存门店id用来获取门店信息
     */
    private List<String> tempSpIdList = new ArrayList<>();
    /**
     * 用来传递的id
     */
    private List<String> intentSpIdList = new ArrayList<>();
    private ResultSpDetail tempResultSpDetail;
    private boolean storedValueWan = false;
    private boolean arrearsWan = false;
    private boolean canbeUsedWan = false;

    @Override
    protected void onBindViewBefore(View root) {
        super.onBindViewBefore(root);
        mContext = getContext();
    }

    @Override
    protected int getTheLayoutId() {
        return R.layout.fragment_store;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onResume() {
        StatusBarCompat.setStatusBarColor(getActivity(), getResources().getColor(R.color.title_bg));
        super.onResume();
    }

    @Override
    protected void initViews(View root) {
        super.initViews(root);
        if (TextUtils.isEmpty(LoginInfoPrefs.getInstance(getContext()).getToken())) {
            showLogin();
            return;
        }
        titleBar.setLineIsVisible(View.GONE);
        if (!UserfoPrefs.getInstance(mContext).getPermission()) {
            //没有app设置权限
            layoutPermission.setVisibility(View.VISIBLE);
            return;
        } else {
            //有app设置权限
            layoutPermission.setVisibility(View.GONE);
            //获取总部全部权限
            getPermissionHq(LoginInfoPrefs.getInstance(mContext).getUserName());
        }
    }

    private void getPermissionHq(String phoneNum) {
        AppModelFactory.model().getPermissionHq(phoneNum, new ProgressSubscriber<DataBean<ResultPermissionHq>>(mContext, false) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean<ResultPermissionHq> result) {
                if (null != result && null != result.data) {
                    UserfoPrefs.getInstance(mContext).setPermissionHq(result.data);
                    setPermissionData(result.data);
                }
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
            }
        });
    }

    private void setPermissionData(ResultPermissionHq resultPermissionHq) {
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

        //具有全部和总部
        mNormalAdapter.setOnItemClickListener(new StoreChildAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(ResultClassfiyStoreBean.OgServiceplacesRspListBean bean, int position) {
                if(permission == 1){
                    if (position == 0) {
                        startActivity(StoreDetailAllActivity.startStoreDetailActivity(mContext, tempSpIdList, mResult, LoginInfoPrefs.getInstance(mContext).getGroupId()));
                    }else if(position == 1){
                        startActivity(StoreDetailHqActivity.startStoreDetailActivity(mContext, tempSpIdList,LoginInfoPrefs.getInstance(mContext).getGroupId()));
                    }else{
                        StoreBean storeBean = new StoreBean();
                        storeBean.spId = bean.sp_id;
                        storeBean.group_id = bean.group_id;
                        storeBean.spName = bean.sp_name;
                        storeBean.thumb_url = bean.thumb_url;
                        storeBean.chairmantel = bean.chairmantel;
                        storeBean.chairman = bean.chairman;
                        storeBean.addr = bean.addr;
                        startActivity(StoreDetailActivity.startStoreDetailActivity(mContext, storeBean));
                    }
                }else if(permission == 2){
                    if (position == 0) {
                        startActivity(StoreDetailAllActivity.startStoreDetailActivity(mContext, tempSpIdList, mResult, LoginInfoPrefs.getInstance(mContext).getGroupId()));
                    }else{
                        StoreBean storeBean = new StoreBean();
                        storeBean.spId = bean.sp_id;
                        storeBean.group_id = bean.group_id;
                        storeBean.spName = bean.sp_name;
                        storeBean.thumb_url = bean.thumb_url;
                        storeBean.chairmantel = bean.chairmantel;
                        storeBean.chairman = bean.chairman;
                        storeBean.addr = bean.addr;
                        startActivity(StoreDetailActivity.startStoreDetailActivity(mContext, storeBean));
                    }
                }else{
                    StoreBean storeBean = new StoreBean();
                    storeBean.spId = bean.sp_id;
                    storeBean.group_id = bean.group_id;
                    storeBean.spName = bean.sp_name;
                    storeBean.thumb_url = bean.thumb_url;
                    storeBean.chairmantel = bean.chairmantel;
                    storeBean.chairman = bean.chairman;
                    storeBean.addr = bean.addr;
                    startActivity(StoreDetailActivity.startStoreDetailActivity(mContext, storeBean));
                }

            }
        });
        storeAreaAdapter = new StoreAreaAdapter(mContext);
        storeAreaAdapter.setOnItemClickListener(new StoreAreaAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(int position) {
                List<String> spList = new ArrayList<>();
                List<OgServiceplacesRspListBean> normalList = new ArrayList<>();
                List<OgServiceplacesRspListBean> stopList = new ArrayList<>();
                for (int i = 0; i < mAllResult.get(position).ogServiceplacesRspList.size(); i++) {
                    OgServiceplacesRspListBean bean = mAllResult.get(position).ogServiceplacesRspList.get(i);
                    //不要把总部的id添加进来
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
                tvNormal.setText("所管门店(" + normalList.size() + ")");
                tvStop.setText("停用门店(" + stopList.size() + ")");
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
                tvStart.setText(DateTimeUtils.getDateTimeText(DateTimeUtils.getTimesMonthMorning(), DateTimeUtils.FORMAT_DATETIME_YEAR_MONTH_DAY));
                tvEnd.setText(DateTimeUtils.getDateTimeText(DateTimeUtils.getNowTimeStamp(), DateTimeUtils.FORMAT_DATETIME_YEAR_MONTH_DAY));
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
                    tvStart.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATETIME_YEAR_MONTH_DAY));
                    start = date.getTime() / 1000;
                } else {
                    tvEnd.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATETIME_YEAR_MONTH_DAY));
                    end = date.getTime() / 1000;
                }
                if (end - start < 0) {
                    showTextDialog("结束时间不能小于开始时间", false);
                    return;
                }
                getSpDetails();
            }
        }).build();
        tvStart.setText(DateTimeUtils.getDateTimeText(DateTimeUtils.getTimesMonthMorning(), DateTimeUtils.FORMAT_DATETIME_YEAR_MONTH_DAY));
        tvEnd.setText(DateTimeUtils.getDateTimeText(DateTimeUtils.getNowTimeStamp(), DateTimeUtils.FORMAT_DATETIME_YEAR_MONTH_DAY));
        start = DateTimeUtils.getTimesMonthMorning();
        end = DateTimeUtils.getNowTimeStamp();
        getClassfiyStoreList(resultPermissionHq);

        tvStoredValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != tempResultSpDetail){
                    if(storedValueWan){
                        storedValueWan = false;
                        TextViewUtils.setTextOrEmpty(tvStoredValue, StringFormatUtils.formatDecimal(tempResultSpDetail.storedvalue));
                    }else{
                        if(tempResultSpDetail.storedvalue >= 10000){
                            storedValueWan = true;
                            tvStoredValue.setText(MathUtils.formatNum(tvStoredValue.getText().toString(), false));
                        }
                    }
                }

            }
        });
        tvArrears.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != tempResultSpDetail){
                    if(arrearsWan){
                        arrearsWan = false;
                        TextViewUtils.setTextOrEmpty(tvArrears, StringFormatUtils.formatDecimal(tempResultSpDetail.arrears));
                    }else{
                        if(tempResultSpDetail.arrears >= 10000){
                            arrearsWan = true;
                            tvArrears.setText(MathUtils.formatNum(tvArrears.getText().toString(), false));
                        }
                    }
                }

            }
        });
        tvCanbeUsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != tempResultSpDetail){
                    if(canbeUsedWan){
                        canbeUsedWan = false;
                        TextViewUtils.setTextOrEmpty(tvCanbeUsed, StringFormatUtils.formatDecimal(tempResultSpDetail.canbe_consume));
                    }else{
                        if(tempResultSpDetail.canbe_consume >= 10000){
                            canbeUsedWan = true;
                            tvCanbeUsed.setText(MathUtils.formatNum(tvCanbeUsed.getText().toString(), false));
                        }
                    }
                }

            }
        });
       llStoredValue.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(StoreAnalysisActivity.startStoreAnalysisActivityForOne(mContext, intentSpIdList, LoginInfoPrefs.getInstance(mContext).getGroupId()));
           }
       });
        llArrears.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(StoreAnalysisActivity.startStoreAnalysisActivityForTwo(mContext, intentSpIdList, LoginInfoPrefs.getInstance(mContext).getGroupId()));
            }
        });
        llCanbeUsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(StoreAnalysisActivity.startStoreAnalysisActivityForThree(mContext, intentSpIdList, LoginInfoPrefs.getInstance(mContext).getGroupId()));
            }
        });

    }

    private void showAreaDialog(){
        dialog = new BottomDialog(mContext, areaDataList);
        dialog.setClickListener(new BottomDialog.OnClickListener() {
            @Override
            public void onClick(int position) {
                //头部大区移动到指定position
                storeAreaAdapter.setPosition(position);
                dialog.setPosition(position);
                ((LinearLayoutManager) rvTop.getLayoutManager()).scrollToPositionWithOffset(position, 0);
                List<String> spList = new ArrayList<>();
                List<OgServiceplacesRspListBean> normalList = new ArrayList<>();
                List<OgServiceplacesRspListBean> stopList = new ArrayList<>();
                for (int i = 0; i < mAllResult.get(position).ogServiceplacesRspList.size(); i++) {
                    OgServiceplacesRspListBean bean = mAllResult.get(position).ogServiceplacesRspList.get(i);
                    spList.add(bean.sp_id);
                    if (bean.status == 1) {
                        normalList.add(bean);
                    } else if (bean.status == 0) {
                        stopList.add(bean);
                    }
                }
                mNormalAdapter.notifyDataSetChanged(normalList);
                mStopAdapter.notifyDataSetChanged(stopList);
                tempSpIdList = spList;
                intentSpIdList = spList;
                dialog.dismiss();
                tvNormal.setText("所管门店(" + normalList.size() + ")");
                tvStop.setText("停用门店(" + stopList.size() + ")");
                getSpDetails();
            }
        });
    }

    private void getClassfiyStoreList(final ResultPermissionHq resultPermissionHq) {
        RequestClassfiyStore requestClassfiyStore = new RequestClassfiyStore();
        AppModelFactory.model().getMyStoreList(LoginInfoPrefs.getInstance(mContext).getGroupId(), requestClassfiyStore, new ProgressSubscriber<DataBean<List<ResultClassfiyStoreBean>>>(mContext, false) {

            @Override
            public void onCompleted() {
                super.onCompleted();
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

                        //大区加一个全部
                        ResultClassfiyStoreBean areaBean = new ResultClassfiyStoreBean();
                        areaBean.name = "全部";
                        areaBean.ogServiceplacesRspList = new ArrayList<>();

                        List<OgServiceplacesRspListBean> normalList = new ArrayList<>();
                        List<OgServiceplacesRspListBean> stopList = new ArrayList<>();
                        mResult = new ArrayList<>();
                        //遍历数据全放到“全部”大区下
                        for (int i = 0, iLength = result.data.size(); i < iLength; i++) {
                            for (int j = 0, jLength = result.data.get(i).ogServiceplacesRspList.size(); j < jLength; j++) {
                                OgServiceplacesRspListBean b = result.data.get(i).ogServiceplacesRspList.get(j);
                                intentSpIdList.add(b.sp_id);
                                //区分管理和停用门店
                                if (b.status == 1) {
                                    normalList.add(b);
                                    mResult.add(b);
                                } else if (b.status == 0) {
                                    stopList.add(b);
                                }
                            }
                            //界面显示:只有大区下有门店才添加进来
                            if (result.data.get(i).ogServiceplacesRspList.size() > 0) {
                                areaDataList.add(result.data.get(i));
                            }
                        }

                        if(resultPermissionHq.all == 1 && resultPermissionHq.headquarters == 1){
                            permission = 1;
                            //具有全部和总部
                            OgServiceplacesRspListBean allBean = new OgServiceplacesRspListBean();
                            allBean.sp_name = "全部";
                            allBean.status = 1;
                            allBean.chairman = "无";
                            allBean.group_id = LoginInfoPrefs.getInstance(mContext).getGroupId();
                            allBean.sp_id = LoginInfoPrefs.getInstance(mContext).getGroupId();
                            OgServiceplacesRspListBean hqBean = new OgServiceplacesRspListBean();
                            hqBean.sp_name = "总部";
                            hqBean.status = 1;
                            hqBean.chairman = "无";
                            hqBean.baseonType = 1;
                            hqBean.group_id = LoginInfoPrefs.getInstance(mContext).getGroupId();
                            hqBean.sp_id = LoginInfoPrefs.getInstance(mContext).getGroupId();
                            //加入到正常门店recylerview下
                            normalList.add(0, hqBean);
                            normalList.add(0, allBean);

                            areaBean.ogServiceplacesRspList.addAll(normalList);
                            areaBean.ogServiceplacesRspList.addAll(stopList);
                            areaDataList.add(0, areaBean);

                            mAllResult = areaDataList;

                            mNormalAdapter.notifyDataSetChanged(normalList);
                            mStopAdapter.notifyDataSetChanged(stopList);
                            tvNormal.setText("所管门店(" + normalList.size() + ")");
                            tvStop.setText("停用门店(" + stopList.size() + ")");
                            storeAreaAdapter.notifyDataSetChanged(areaDataList);

                            tempSpIdList = intentSpIdList;
                            if (tempSpIdList.size() == 0) {
                                layoutPermission.setVisibility(View.VISIBLE);
                                return;
                            }
                            showAreaDialog();
                        }else if(resultPermissionHq.all == 1){
                            //只有全部，没有总部
                            //多个门店显示全部
                            if(normalList.size() > 1){
                                permission = 2;
                                OgServiceplacesRspListBean allBean = new OgServiceplacesRspListBean();
                                allBean.sp_name = "全部";
                                allBean.status = 1;
                                allBean.chairman = "无";
                                allBean.group_id = LoginInfoPrefs.getInstance(mContext).getGroupId();
                                //加入到正常门店recylerview下
                                normalList.add(0, allBean);
                                areaBean.ogServiceplacesRspList.addAll(normalList);
                                areaBean.ogServiceplacesRspList.addAll(stopList);
                                areaDataList.add(0, areaBean);
                                mAllResult = areaDataList;
                                mNormalAdapter.notifyDataSetChanged(normalList);
                                mStopAdapter.notifyDataSetChanged(stopList);
                                tvNormal.setText("所管门店(" + normalList.size() + ")");
                                tvStop.setText("停用门店(" + stopList.size() + ")");
                                storeAreaAdapter.notifyDataSetChanged(areaDataList);
                                tempSpIdList = intentSpIdList;
                                if (tempSpIdList.size() == 0) {
                                    layoutPermission.setVisibility(View.VISIBLE);
                                    return;
                                }
                                showAreaDialog();
                            }else{
                                permission = 3;
                                //只有一个门店的时候不显示全部
                                mAllResult = areaDataList;
                                mNormalAdapter.notifyDataSetChanged(normalList);
                                mStopAdapter.notifyDataSetChanged(stopList);
                                tvNormal.setText("所管门店(" + normalList.size() + ")");
                                tvStop.setText("停用门店(" + stopList.size() + ")");
                                storeAreaAdapter.notifyDataSetChanged(areaDataList);
                                tempSpIdList = intentSpIdList;
                                if (tempSpIdList.size() == 0) {
                                    layoutPermission.setVisibility(View.VISIBLE);
                                    return;
                                }
                                showAreaDialog();
                            }
                        }else {
                            //没有总部和全部
                            permission = 4;
                            mNormalAdapter.notifyDataSetChanged(normalList);
                            mStopAdapter.notifyDataSetChanged(stopList);
                            tvNormal.setText("所管门店(" + normalList.size() + ")");
                            tvStop.setText("停用门店(" + stopList.size() + ")");
                            storeAreaAdapter.notifyDataSetChanged(areaDataList);
                            tempSpIdList = intentSpIdList;
                            if (tempSpIdList.size() == 0) {
                                layoutPermission.setVisibility(View.VISIBLE);
                                return;
                            }
                            showAreaDialog();
                        }
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
        body.group_id = LoginInfoPrefs.getInstance(getContext()).getGroupId();
        body.sp_id_list = tempSpIdList;
        AppModelFactory.model().getSpDetails(body, new ProgressSubscriber<DataBean<ResultSpDetail>>(mContext, false) {
            @Override
            public void onCompleted() {
                super.onCompleted();
                if (srl.isRefreshing()) {
                    srl.setRefreshing(false);
                }
            }

            @Override
            public void onNext(DataBean<ResultSpDetail> result) {

                if (null != result.data) {
                    tempResultSpDetail = result.data;
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
                            if (pos == 0) {
                                startActivity(AmountTotalListActivity.startAmountTotalListActivity(mContext, intentSpIdList, LoginInfoPrefs.getInstance(mContext).getGroupId()));
                            } else if (pos == 1) {
                                startActivity(AmountConsumeListActivity.startAmountConsumeListActivity(mContext, intentSpIdList, LoginInfoPrefs.getInstance(mContext).getGroupId()));
                            } else {
                                startActivity(ResultsListActivity.startFromTotalAmount(mContext, intentSpIdList, LoginInfoPrefs.getInstance(mContext).getGroupId()));
                            }
                        }
                    });
                    if(result.data.storedvalue >= 10000){
                        storedValueWan = true;
                        tvStoredValue.setText(MathUtils.formatNum(result.data.storedvalue + "", false));
                    }else{
                        storedValueWan = false;
                        TextViewUtils.setTextOrEmpty(tvStoredValue, StringFormatUtils.formatDecimal(result.data.storedvalue));
                    }
                    if(result.data.arrears >= 10000){
                        arrearsWan = true;
                        tvArrears.setText(MathUtils.formatNum(result.data.arrears + "", false));
                    }else{
                        arrearsWan = false;
                        TextViewUtils.setTextOrEmpty(tvArrears, StringFormatUtils.formatDecimal(result.data.arrears));
                    }
                    if(result.data.canbe_consume >= 10000){
                        canbeUsedWan = true;
                        tvCanbeUsed.setText(MathUtils.formatNum(result.data.canbe_consume + "", false));
                    }else{
                        canbeUsedWan = false;
                        TextViewUtils.setTextOrEmpty(tvCanbeUsed, StringFormatUtils.formatDecimal(result.data.canbe_consume));
                    }
                    TextViewUtils.setTextOrEmpty(tvPeopleTime, result.data.person_times + "", "0");
                    TextViewUtils.setTextOrEmpty(tvNewMember, result.data.add_member + "", "0");
                    TextViewUtils.setTextOrEmpty(tvPeopleNum, result.data.person_number + "", "0");
                    TextViewUtils.setTextOrEmpty(tvServiceNum, result.data.service_numbers + "", "0");
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

    @OnClick(R.id.ll_start)
    public void start(View v) {
        timeType = 0;
        showDateTimePickerDialog();
    }

    @OnClick(R.id.ll_end)
    public void end(View v) {
        timeType = 1;
        showDateTimePickerDialog();
    }

    private void showDateTimePickerDialog() {
        if(null != timePickerView){
            timePickerView.show();
        }
    }

}

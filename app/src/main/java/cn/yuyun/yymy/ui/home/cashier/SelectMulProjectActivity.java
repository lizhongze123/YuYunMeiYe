package cn.yuyun.yymy.ui.home.cashier;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ajguan.library.EasyRefreshLayout;
import com.example.http.DataBean;
import com.example.http.PageInfo;
import com.example.http.Presenter2;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lzz.utils.DensityUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestProject;
import cn.yuyun.yymy.http.result.ResultListStaff;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.http.result.ResultProject;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.home.appointment.ProjectListAdapter;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.ShadowDrawable;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc
 * @date
 */

public class SelectMulProjectActivity extends BaseActivity {

    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.rb_service)
    RadioButton rbService;
    @BindView(R.id.rb_packet)
    RadioButton rbPacket;
    @BindView(R.id.rb_product)
    RadioButton rbProduct;
    @BindView(R.id.radioGroupTitle)
    RadioGroup radioGroupTitle;
    @BindView(R.id.rv_service)
    RecyclerView rvService;
    @BindView(R.id.easylayoutService)
    EasyRefreshLayout easylayoutService;
    @BindView(R.id.rv_packet)
    RecyclerView rvPacket;
    @BindView(R.id.easylayoutPacket)
    EasyRefreshLayout easylayoutPacket;
    @BindView(R.id.rv_product)
    RecyclerView rvProduct;
    @BindView(R.id.easylayoutProduct)
    EasyRefreshLayout easylayoutProduct;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    private Presenter2<ResultProject> presenterService;
    private Presenter2<ResultProject> presenterPacket;
    private Presenter2<ResultProject> presenterProduct;

    private ProjectListAdapter serviceAdapter;
    private ProjectListAdapter packetAdapter;
    private ProjectListAdapter productAdapter;

    private int REQUEST_CODE = 10001;
    private ResultMemberBean memberBean;

    private RequestProject bodyService = new RequestProject();
    private RequestProject bodyPacket = new RequestProject();
    private RequestProject bodyProduct = new RequestProject();


    public static Intent startSelectMulProjectActivity(Context context, ResultMemberBean memberBean) {
        return new Intent(context, SelectMulProjectActivity.class).putExtra(EXTRA_DATA, memberBean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mul_select_project);
        EvenManager.register(this);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    private void initDatas() {
        List<String>list = new ArrayList<>();
        list.add(UserfoPrefs.getInstance(mContext).getBaseonId());
        bodyService.spIdList = list;
        bodyPacket.spIdList = list;
        bodyProduct.spIdList = list;
        bodyService.good_type = 2;
        bodyProduct.good_type = 1;
        bodyPacket.good_type = 3;
        configurePresenter();
        presenterService.loadData(true);
        presenterPacket.loadData(true);
        presenterProduct.loadData(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if (notifyEvent.tag == NotifyEvent.FINISH_CASHIER) {
            finish();
        }else if(notifyEvent.tag == NotifyEvent.SELECT_PROJECT){
            CashierBuyBean bean = (CashierBuyBean) notifyEvent.value;

            if(bean.asset_type == 1){
                for (ResultProject resultProject : productAdapter.getData()) {
                    if(resultProject.good_id.equals(bean.good_id)){
                        resultProject.isChecked = false;
                        productAdapter.check(false,  resultProject);
                        productAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }else if(bean.asset_type == 2){
                for (ResultProject resultProject : serviceAdapter.getData()) {
                    if(resultProject.good_id.equals(bean.good_id)){
                        resultProject.isChecked = false;
                        serviceAdapter.check(false,  resultProject);
                        serviceAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }else if(bean.asset_type == 3){
                for (ResultProject resultProject : packetAdapter.getData()) {
                    if(resultProject.good_id.equals(bean.good_id)){
                        resultProject.isChecked = false;
                        packetAdapter.check(false,  resultProject);
                        packetAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            int total = serviceAdapter.getSelectedItem().size() + packetAdapter.getSelectedItem().size() + productAdapter.getSelectedItem().size();
            if(total == 0){
                titleBar.setTvRight("确定");
            }else{
                titleBar.setTvRight("确定(" + total + ")");
            }

        }
    }

    List<CashierBuyBean> list = new ArrayList<>();

    private void initViews() {
        titleBar.setTilte("选择商品");
        titleBar.setTvRight("确定");
        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ResultProject> productList = productAdapter.getSelectedItem();
                List<ResultProject> serviceList = serviceAdapter.getSelectedItem();
                List<ResultProject> packetList = packetAdapter.getSelectedItem();
                if(productList.size() == 0 && serviceList.size() == 0 && packetList.size() == 0){
                    showToast("请至少选择一个商品");
                    return;
                }
                List<ResultProject> goodsList = new ArrayList<>();
                goodsList.addAll(serviceList);
                goodsList.addAll(packetList);
                goodsList.addAll(productList);

                list.clear();

                if(null != intentList){
                    boolean flag = false;
                    for (int i = 0; i < goodsList.size(); i++) {
                        for (int j = 0; j < intentList.size(); j++) {
                            if(goodsList.get(i).good_id.equals(intentList.get(j).good_id)){
                                list.add(intentList.get(j));
                                flag = true;
                                break;
                            }
                        }
                        if(flag){
                            flag = false;
                            continue;
                        }

                        CashierBuyBean cashierBuyBean = new CashierBuyBean();
                        cashierBuyBean.name = goodsList.get(i).name;
                        cashierBuyBean.good_id = goodsList.get(i).good_id;
                        cashierBuyBean.guideprice = goodsList.get(i).guideprice;
                        cashierBuyBean.transaction_price = cashierBuyBean.guideprice * cashierBuyBean.total_num;
                        cashierBuyBean.thumb_url = goodsList.get(i).thumb_url;
                        cashierBuyBean.asset_type = goodsList.get(i).good_type;
                        cashierBuyBean.amount_realpay = cashierBuyBean.transaction_price;
                        cashierBuyBean.achieve_percent = goodsList.get(i).achieve_percent;
                        cashierBuyBean.achieve_statistics_type = goodsList.get(i).achieve_statistics_type;
                        list.add(cashierBuyBean);
                    }
                    startActivityForResult(CashierBuyListActivity.startCashierBuyListActivity(mContext, list, memberBean), REQUEST_CODE);
                }else{

                    for (ResultProject resultProject : goodsList) {
                        CashierBuyBean cashierBuyBean = new CashierBuyBean();
                        cashierBuyBean.name = resultProject.name;
                        cashierBuyBean.good_id = resultProject.good_id;
                        cashierBuyBean.guideprice = resultProject.guideprice;
                        cashierBuyBean.transaction_price = cashierBuyBean.guideprice * cashierBuyBean.total_num;
                        cashierBuyBean.thumb_url = resultProject.thumb_url;
                        cashierBuyBean.asset_type = resultProject.good_type;
                        cashierBuyBean.amount_realpay = cashierBuyBean.transaction_price;
                        cashierBuyBean.achieve_percent = resultProject.achieve_percent;
                        cashierBuyBean.achieve_statistics_type = resultProject.achieve_statistics_type;
                        list.add(cashierBuyBean);
                    }
                    startActivityForResult(CashierBuyListActivity.startCashierBuyListActivity(mContext, list, memberBean), REQUEST_CODE);
                }


            }
        });
        memberBean = (ResultMemberBean) getIntent().getSerializableExtra(EXTRA_DATA);
        ShadowDrawable.setShadowDrawable(llSearch, Color.parseColor("#ffffff"), DensityUtils.dip2px(mContext, 99),
                Color.parseColor("#80e0e0e0"),
                DensityUtils.dip2px(mContext, 5), 0, 0);
        rbPacket.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    easylayoutService.setVisibility(View.GONE);
                    easylayoutPacket.setVisibility(View.VISIBLE);
                    easylayoutProduct.setVisibility(View.GONE);
                    etInput.setHint("请输入套餐名称");
                }
            }
        });
        rbProduct.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    easylayoutService.setVisibility(View.GONE);
                    easylayoutPacket.setVisibility(View.GONE);
                    easylayoutProduct.setVisibility(View.VISIBLE);
                    etInput.setHint("请输入产品名称");
                }
            }
        });
        rbService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    easylayoutService.setVisibility(View.VISIBLE);
                    easylayoutPacket.setVisibility(View.GONE);
                    easylayoutProduct.setVisibility(View.GONE);
                    etInput.setHint("请输入项目名称");
                }
            }
        });

        easylayoutPacket.setRefreshHeadView(new RefreshHeaderView(mContext));
        easylayoutProduct.setRefreshHeadView(new RefreshHeaderView(mContext));
        easylayoutService.setRefreshHeadView(new RefreshHeaderView(mContext));
        easylayoutPacket.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                presenterPacket.loadData(false);
            }

            @Override
            public void onRefreshing() {
                presenterPacket.loadData(true);
            }
        });
        easylayoutProduct.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                presenterProduct.loadData(false);
            }

            @Override
            public void onRefreshing() {
                presenterProduct.loadData(true);
            }
        });
        easylayoutService.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                presenterService.loadData(false);
            }

            @Override
            public void onRefreshing() {
                presenterService.loadData(true);
            }
        });
        rvPacket.setLayoutManager(new LinearLayoutManager(this));
        rvProduct.setLayoutManager(new LinearLayoutManager(this));
        rvService.setLayoutManager(new LinearLayoutManager(this));
        serviceAdapter = new ProjectListAdapter(this);
        packetAdapter = new ProjectListAdapter(this);
        productAdapter = new ProjectListAdapter(this);
        rvPacket.setAdapter(packetAdapter);
        rvProduct.setAdapter(productAdapter);
        rvService.setAdapter(serviceAdapter);

        serviceAdapter.setOnItemClickListener(new ProjectListAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultProject bean, int position) {
                int total = serviceAdapter.getSelectedItem().size() + packetAdapter.getSelectedItem().size() + productAdapter.getSelectedItem().size();
                if(total == 0){
                    titleBar.setTvRight("确定");
                }else{
                    titleBar.setTvRight("确定(" + total + ")");
                }
            }
        });

        packetAdapter.setOnItemClickListener(new ProjectListAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultProject bean, int position) {
                int total = serviceAdapter.getSelectedItem().size() + packetAdapter.getSelectedItem().size() + productAdapter.getSelectedItem().size();
                if(total == 0){
                    titleBar.setTvRight("确定");
                }else{
                    titleBar.setTvRight("确定(" + total + ")");
                }
            }
        });

        productAdapter.setOnItemClickListener(new ProjectListAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultProject bean, int position) {
                int total = serviceAdapter.getSelectedItem().size() + packetAdapter.getSelectedItem().size() + productAdapter.getSelectedItem().size();
                if(total == 0){
                    titleBar.setTvRight("确定");
                }else{
                    titleBar.setTvRight("确定(" + total + ")");
                }
            }
        });

        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() <= 0) {
                    bodyPacket.search_text = "";
                    bodyProduct.search_text = "";
                    bodyService.search_text = "";
                    if(rbPacket.isChecked()){
                        presenterPacket.loadData(true);
                    }else if(rbProduct.isChecked()){
                        presenterProduct.loadData(true);
                    }else if(rbService.isChecked()){
                        presenterService.loadData(true);
                    }
                } else {
                    if(rbPacket.isChecked()){
                        bodyPacket.search_text = s.toString();
                        presenterPacket.loadData(true);
                    }else if(rbProduct.isChecked()){
                        bodyProduct.search_text = s.toString();
                        presenterProduct.loadData(true);
                    }else if(rbService.isChecked()){
                        bodyService.search_text = s.toString();
                        presenterService.loadData(true);
                    }
                }
            }
        });
    }


    private void configurePresenter() {
        presenterPacket = new Presenter2<>();
        presenterPacket.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultProject>() {

            @Override
            public void onSuccess(List<ResultProject> result, int total, boolean isRefresh) {
                if (null != result) {
                    if(isRefresh){
                        packetAdapter.notifyDataSetChanged(result);
                    }else{
                        packetAdapter.addAll(result);
                    }
                }
            }

            @Override
            public void onFailed(boolean isRefresh) {
                showError();
            }

            @Override
            public void onCompleted(boolean isRefresh) {
                easylayoutPacket.refreshComplete();
                easylayoutPacket.closeLoadView();
            }

            @Override
            public void onEmptyData() {
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultProject>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getProjectList(LoginInfoPrefs.getInstance(mContext).getGroupId(), pageIndex, pageSize, bodyPacket, subscriber);
            }
        });

        presenterProduct = new Presenter2<>();
        presenterProduct.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultProject>() {

            @Override
            public void onSuccess(List<ResultProject> result, int total, boolean isRefresh) {
                if (null != result) {
                    if(isRefresh){
                        productAdapter.notifyDataSetChanged(result);
                    }else{
                        productAdapter.addAll(result);
                    }
                }
            }

            @Override
            public void onFailed(boolean isRefresh) {
                showError();
            }

            @Override
            public void onCompleted(boolean isRefresh) {
                easylayoutProduct.refreshComplete();
                easylayoutProduct.closeLoadView();
            }

            @Override
            public void onEmptyData() {
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultProject>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getProjectList(LoginInfoPrefs.getInstance(mContext).getGroupId(), pageIndex, pageSize, bodyProduct, subscriber);
            }
        });

        presenterService = new Presenter2<>();
        presenterService.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultProject>() {

            @Override
            public void onSuccess(List<ResultProject> result, int total, boolean isRefresh) {
                if (null != result) {
                    if(isRefresh){
                        serviceAdapter.notifyDataSetChanged(result);
                    }else{
                        serviceAdapter.addAll(result);
                    }
                }
            }

            @Override
            public void onFailed(boolean isRefresh) {
                showError();
            }

            @Override
            public void onCompleted(boolean isRefresh) {
                easylayoutService.refreshComplete();
                easylayoutService.closeLoadView();
            }

            @Override
            public void onEmptyData() {
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultProject>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getProjectList(LoginInfoPrefs.getInstance(mContext).getGroupId(), pageIndex, pageSize, bodyService, subscriber);
            }
        });

    }

    List<CashierBuyBean> intentList;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            intentList = (List<CashierBuyBean>) data.getSerializableExtra("intent");
        }
    }
}

package cn.yuyun.yymy.ui.store.report;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.example.http.DataBean;
import com.example.http.PageInfo;
import com.example.http.Presenter2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lzz.utils.DeviceUtils;
import cn.lzz.utils.ResoureUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestStock;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean.OgServiceplacesRspListBean;
import cn.yuyun.yymy.http.result.ResultStock;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author
 * @desc 库存管理--全部
 * @date
 */

public class StockAllActivity extends BaseActivity{

    @BindView(R.id.main_drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.main_right_drawer_layout)
    RelativeLayout main_right_drawer_layout;

    @BindView(R.id.rv_card)
    RecyclerView rvCard;
    HqStoreAdapter hqStoreAdapter;
    StringBuilder tempLevelName;
    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.ll_searchMyMember)
    LinearLayout llSearchMyMember;
    @BindView(R.id.rl_search)
    RelativeLayout rlSearch;
    @BindView(R.id.rv_analysis)
    RecyclerView rvAnalysis;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;

    @BindView(R.id.tv_amount)
    TextView tvAmount;

    private StockAdapter stockAdapter;
    private RequestStock requestStock = new RequestStock();

    private Presenter2<ResultStock> presenter;
    private List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> storeList;
    private List<String> tempSpIdList;
    private String groupId;

    public static Intent startStockActivity(Context context, List<String> tempSpIdList, List<OgServiceplacesRspListBean> storeList,  String groupId) {
        return new Intent(context, StockAllActivity.class).putExtra(EXTRA_DATA2, (Serializable) tempSpIdList).putExtra(EXTRA_TYPE, (Serializable) storeList).putExtra(EXTRA_DATA, groupId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_all);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    private void initDatas() {
        requestStock.shIdList = tempSpIdList;
        requestStock.hq_id = "";
        configurePresenter();
        easyRefreshLayout.autoRefresh();
    }

    private void initViews() {
        titleBar.setTilte("库存管理");
        tempSpIdList = (List<String>) getIntent().getSerializableExtra(EXTRA_DATA2);
        storeList = (List<OgServiceplacesRspListBean>) getIntent().getSerializableExtra(EXTRA_TYPE);
        groupId = getIntent().getStringExtra(EXTRA_DATA);
        tempLevelName = new StringBuilder();
        rvAnalysis.setLayoutManager(new LinearLayoutManager(this));
        stockAdapter = new StockAdapter(this);
        stockAdapter.setOnItemClickListener(new StockAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultStock bean, int position) {
                startActivity(StockDetailActivity.startStockDetailActivity(mContext, bean));
            }
        });
        rvAnalysis.setAdapter(stockAdapter);
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(this));
        easyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                presenter.loadData(false);
            }

            @Override
            public void onRefreshing() {
                presenter.loadData(true);
            }
        });
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(etInput.getText().toString())) {
                        requestStock.searchText = etInput.getText().toString().trim();
                    }else{
                        requestStock.searchText = "";
                    }
                    requestStock.shIdList = tempSpIdList;
                    hqStoreAdapter.clearAllSelection();
                    easyRefreshLayout.autoRefresh();
                }
                return false;
            }
        });
        initLayout();
    }

    public void initLayout() {
        //设置菜单内容之外其他区域的背景色
        drawerLayout.setScrimColor(ResoureUtils.getColor(mContext, R.color.transparent_black));
        //侧滑栏关闭手势滑动
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        hqStoreAdapter = new HqStoreAdapter(mContext);
        rvCard.setLayoutManager(new GridLayoutManager(this, 2){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvCard.setAdapter(hqStoreAdapter);
        hqStoreAdapter.addAll(storeList);
    }

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultStock>() {

            @Override
            public void onSuccess(List<ResultStock> result, int total, final boolean isRefresh) {
                if (result != null) {
                    if (isRefresh) {
                        stockAdapter.notifyDataSetChanged(result);
                    } else {
                        stockAdapter.addAll(result);
                    }
                }
            }

            @Override
            public void onFailed(boolean isRefresh) {
                showToast("加载失败，请稍候重试");
            }

            @Override
            public void onCompleted(boolean isRefresh) {
                easyRefreshLayout.refreshComplete();
                easyRefreshLayout.closeLoadView();
            }

            @Override
            public void onEmptyData() {

            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultStock>>> subscriber, int pageIndex, int pageSize) {
                showContent();
                requestStock.startRow = pageIndex;
                requestStock.count = pageSize;
                AppModelFactory.model().getStock(requestStock, subscriber);
            }
        });
    }

    /**
     * 右边菜单开关事件
     */
    public void openRightLayout(View view) {
        if (drawerLayout.isDrawerOpen(main_right_drawer_layout)) {
            drawerLayout.closeDrawer(main_right_drawer_layout);
        } else {
            drawerLayout.openDrawer(main_right_drawer_layout);
        }
    }

    @OnClick(R.id.tv_positive)
    public void positive(View v) {
        openRightLayout(v);
        List<String> list = new ArrayList<>();
        List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> listStore = hqStoreAdapter.getSelectedItem();

        if(null != listStore && listStore.size() > 0){
            tempLevelName.delete(0, tempLevelName.length());
            for (int i = 0; i < listStore.size(); i++) {
                list.add(listStore.get(i).sp_id);
                if(i == listStore.size() - 1){
                    tempLevelName.append(listStore.get(i).sp_name);
                }else{
                    tempLevelName.append(listStore.get(i).sp_name + "、");
                }
            }
            requestStock.shIdList = list;
            requestStock.hq_id = "";
            tvAmount.setText(tempLevelName.toString().substring(0,tempLevelName.length() - 1));
        }

        tvAmount.setText(tempLevelName.toString());
        stockAdapter.setHq(false);
        requestStock.searchText = "";
        etInput.setText("");
        tvAmount.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tvAmount.setSingleLine(true);
        tvAmount.setSelected(true);
        tvAmount.setFocusable(true);
        tvAmount.setFocusableInTouchMode(true);
        easyRefreshLayout.autoRefresh();
    }

    @OnClick(R.id.tv_negative)
    public void negative(View v) {
        openRightLayout(v);
        requestStock.searchText = "";
        requestStock.hq_id = "";
        etInput.setText("");
        hqStoreAdapter.clearAllSelection();
        requestStock.shIdList = tempSpIdList;
        tempLevelName.delete(0, tempLevelName.length());
        tvAmount.setText("");
        presenter.loadData(true);
    }

    @OnClick(R.id.rl_filter)
    public void filter(View v) {
        DeviceUtils.hideSoftKeyboard(v, mContext);
        openRightLayout(v);
    }

}

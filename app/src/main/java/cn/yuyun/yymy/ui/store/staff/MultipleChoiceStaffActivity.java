package cn.yuyun.yymy.ui.store.staff;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.ajguan.library.EasyRefreshLayout;
import com.example.http.DataBean;
import com.example.http.PageInfo;
import com.example.http.Presenter2;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestStoreStaffList;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;
import cn.yuyun.yymy.http.result.ResultListStaff;
import cn.yuyun.yymy.ui.home.cashier.CommisionListActivity;
import cn.yuyun.yymy.ui.home.leave.AlreadyStaffAdapter;
import cn.yuyun.yymy.ui.home.leave.SelectListStaffAdapter;
import cn.yuyun.yymy.ui.home.work.AlreadyStaffAdapter2;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author
 * @desc 多选员工
 * @date
 */
public class MultipleChoiceStaffActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;

    @BindView(R.id.rv_top)
    RecyclerView rvTop;

    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;

    private Presenter2<ResultListStaff> presenter;

    private AlreadyStaffAdapter2 alreadyStaffAdapter;
    private SelectListStaffAdapter selectStaffAdapter;

    private ResultClassfiyStoreBean.OgServiceplacesRspListBean storeBean;

    private static String TYPE_GROUP = "type_group";
    private static String TYPE_STORE = "type_store";
    private static String TYPE_COMMISION_BUY = "type_commision_buy";
    private static String TYPE_COMMISION_CONSUME = "type_commision_consume";
    private static String TYPE_COMMISION_CHARGE = "type_commision_charge";
    private String type = "";

    private List<ResultListStaff> mIntentResult;

    private RequestStoreStaffList body;

    @BindView(R.id.et_search)
    EditText etSearch;

    public static Intent startMultipleChoiceStaffActivity(Context context, ResultClassfiyStoreBean.OgServiceplacesRspListBean bean) {
        return new Intent(context, MultipleChoiceStaffActivity.class).putExtra(EXTRA_TYPE, TYPE_STORE).putExtra(EXTRA_DATA2, bean);
    }

    public static Intent startMultipleChoiceStaffActivityFromGroup(Context context, ResultClassfiyStoreBean.OgServiceplacesRspListBean bean) {
        return new Intent(context, MultipleChoiceStaffActivity.class).putExtra(EXTRA_TYPE, TYPE_GROUP).putExtra(EXTRA_DATA2, bean);
    }

    public static Intent startFromCommisionStaffSelectForBuy(Context context, ResultClassfiyStoreBean.OgServiceplacesRspListBean bean) {
        return new Intent(context, MultipleChoiceStaffActivity.class).putExtra(EXTRA_TYPE, TYPE_COMMISION_BUY).putExtra(EXTRA_DATA2, bean);
    }

    public static Intent startFromCommisionStaffSelectForConsume(Context context, ResultClassfiyStoreBean.OgServiceplacesRspListBean bean) {
        return new Intent(context, MultipleChoiceStaffActivity.class).putExtra(EXTRA_TYPE, TYPE_COMMISION_CONSUME).putExtra(EXTRA_DATA2, bean);
    }

    public static Intent startFromCommisionStaffSelectForCharge(Context context, ResultClassfiyStoreBean.OgServiceplacesRspListBean bean) {
        return new Intent(context, MultipleChoiceStaffActivity.class).putExtra(EXTRA_TYPE, TYPE_COMMISION_CHARGE).putExtra(EXTRA_DATA2, bean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_choice_staff);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        EvenManager.register(this);
        initViews();
        initDatas();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if (notifyEvent.tag == NotifyEvent.FINISH_CASHIER) {
            finish();
        }
    }

    private void initDatas() {
        configurePresenter();
        easyRefreshLayout.autoRefresh();
    }

    private void initViews() {
        titleBar.setTvRight("确定");
        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        titleBar.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ResultListStaff> data = selectStaffAdapter.getSelectedItem();
                if(data.size() > 0){
                    Intent intent = getIntent();
                    intent.putExtra(EXTRA_DATA, (Serializable) data);
                    setResult(RESULT_OK, intent);
                }
                finish();
            }
        });
        storeBean = (ResultClassfiyStoreBean.OgServiceplacesRspListBean) getIntent().getSerializableExtra(EXTRA_DATA2);
        type = getIntent().getStringExtra(EXTRA_TYPE);
        if(type.equals(TYPE_COMMISION_BUY) || type.equals(TYPE_COMMISION_CONSUME) || type.equals(TYPE_COMMISION_CHARGE)){
            titleBar.setTilte("选择提成员工");
        }else{
            titleBar.setTilte("选择审批人");
        }

        if(getIntent().getSerializableExtra(EXTRA_DATA) != null){
            mIntentResult = (List<ResultListStaff>)getIntent().getSerializableExtra(EXTRA_DATA);
        }else{
            mIntentResult = new ArrayList<>();
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectStaffAdapter = new SelectListStaffAdapter(mContext, mIntentResult);
        recyclerView.setAdapter(selectStaffAdapter);

        LinearLayoutManager ms = new LinearLayoutManager(this);
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvTop.setLayoutManager(ms);
        alreadyStaffAdapter = new AlreadyStaffAdapter2(mContext);
        rvTop.setAdapter(alreadyStaffAdapter);
        alreadyStaffAdapter.addAll(mIntentResult);

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
        selectStaffAdapter.setOnItemClickListener(new SelectListStaffAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultListStaff bean, int position) {
                alreadyStaffAdapter.notifyDataSetChanged(selectStaffAdapter.getSelectedItem());
                if(mIntentResult.size() > 0){
                    rvTop.smoothScrollToPosition(mIntentResult.size() - 1);
                }
                if(selectStaffAdapter.getSelectedItem().size() == 0){
                    titleBar.setTvRight("确定");
                }else{
                    titleBar.setTvRight("确定(" + selectStaffAdapter.getSelectedItem().size() + ")");
                }
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() <= 0) {
                    body.search_text = "";
                } else {
                    //网络搜索
                    body.search_text = s.toString();
                }
                presenter.loadData(true);
            }
        });
    }

    private void configurePresenter() {
        body = new RequestStoreStaffList();
        List<String>spList = new ArrayList<>();
        if(type.equals(TYPE_GROUP)){
            body.baseon_type = 1;
            spList.add(storeBean.group_id);
            body.spIdList = spList;
        }else{
            body.baseon_type = 2;
            spList.add(storeBean.sp_id);
            body.spIdList = spList;
        }

        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultListStaff>() {

            @Override
            public void onSuccess(List<ResultListStaff> result, int total, boolean isRefresh) {
                if (null != result) {
                    easyRefreshLayout.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                    if (isRefresh) {
                        selectStaffAdapter.notifyDataSetChanged(result);
                    } else {
                        selectStaffAdapter.addAll(result);
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
                easyRefreshLayout.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
                emptyLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultListStaff>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getStaffList(storeBean.group_id, pageIndex, pageSize, body, subscriber);
            }
        });

    }

    public void save() {
       List<ResultListStaff> list =  selectStaffAdapter.getSelectedItem();
        if(list.size() == 0){
            showToast("请选择员工");
            return;
        }
        if(type.equals(TYPE_COMMISION_BUY)){
            EvenManager.sendEvent(new NotifyEvent(list, NotifyEvent.FINISH_COMMISION));
            Intent intent = CommisionListActivity.startCommisionListActivityForBuy(mContext, list, storeBean);
            intent.putExtra("amount", getIntent().getDoubleExtra("amount", 0));
            intent.putExtra("count", getIntent().getIntExtra("count", 1));
            intent.putExtra("cashierBuyBean", getIntent().getSerializableExtra("cashierBuyBean"));
            startActivity(intent);
            finish();
        }else if(type.equals(TYPE_COMMISION_CONSUME)){
            EvenManager.sendEvent(new NotifyEvent(list, NotifyEvent.FINISH_COMMISION));
            Intent intent = CommisionListActivity.startCommisionListActivityForConsume(mContext, list, storeBean);
            intent.putExtra("amount", getIntent().getDoubleExtra("amount", 0));
            intent.putExtra("project", getIntent().getSerializableExtra("project"));
            intent.putExtra("count", getIntent().getIntExtra("count", 1));
            startActivity(intent);
            finish();
        }else if(type.equals(TYPE_COMMISION_CHARGE)){
            EvenManager.sendEvent(new NotifyEvent(list, NotifyEvent.FINISH_COMMISION));
            Intent intent = CommisionListActivity.startCommisionListActivityForCharge(mContext, list, storeBean);
            intent.putExtra("amount", getIntent().getDoubleExtra("amount", 0));
            intent.putExtra("count", getIntent().getIntExtra("count", 1));
            intent.putExtra("cashierBuyBean", getIntent().getSerializableExtra("cashierBuyBean"));
            startActivity(intent);
            finish();
        }else{
            EvenManager.sendEvent(new NotifyEvent(list, NotifyEvent.DATA));
            onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        EvenManager.unregister(this);
        super.onDestroy();
    }
}

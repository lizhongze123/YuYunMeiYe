package cn.yuyun.yymy.ui.store.member;

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
import cn.yuyun.yymy.http.request.RequestMemberFilter;
import cn.yuyun.yymy.http.request.RequestStoreStaffList;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;
import cn.yuyun.yymy.http.result.ResultListStaff;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.ui.home.cashier.CommisionListActivity;
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
 * @desc 多选会员
 * @date
 */
public class MultipleChoiceMemberActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;
    @BindView(R.id.rv_top)
    RecyclerView rvTop;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;

    private Presenter2<ResultMemberBean> presenter;
    private AlreadyMemberAdapter alreadyMemberAdapter;
    private SelectMulMemberAdapter selectMulMemberAdapter;
    private ResultClassfiyStoreBean.OgServiceplacesRspListBean storeBean;
    private static String TYPE_STORE = "type_store";
    private String type = "";

    private List<ResultMemberBean> mIntentResult;

    private RequestMemberFilter body;

    @BindView(R.id.et_search)
    EditText etSearch;

    public static Intent startMultipleChoiceMemberActivity(Context context, ResultClassfiyStoreBean.OgServiceplacesRspListBean bean) {
        return new Intent(context, MultipleChoiceMemberActivity.class).putExtra(EXTRA_TYPE, TYPE_STORE).putExtra(EXTRA_DATA2, bean);
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
        body = new RequestMemberFilter();
        List<String>spList = new ArrayList<>();
        spList.add(storeBean.sp_id);
        body.spIdList = spList;
        configurePresenter();
        easyRefreshLayout.autoRefresh();
    }

    private void initViews() {
        titleBar.setTilte("选择会员");
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
                List<ResultMemberBean> data = selectMulMemberAdapter.getSelectedItem();
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
        etSearch.setHint("请输入会员姓名");
        if(getIntent().getSerializableExtra(EXTRA_DATA) != null){
            mIntentResult = (List<ResultMemberBean>)getIntent().getSerializableExtra(EXTRA_DATA);
        }else{
            mIntentResult = new ArrayList<>();
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectMulMemberAdapter = new SelectMulMemberAdapter(mContext, mIntentResult);
        recyclerView.setAdapter(selectMulMemberAdapter);

        LinearLayoutManager ms = new LinearLayoutManager(this);
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvTop.setLayoutManager(ms);
        alreadyMemberAdapter = new AlreadyMemberAdapter(mContext);
        rvTop.setAdapter(alreadyMemberAdapter);
        alreadyMemberAdapter.addAll(mIntentResult);

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
        selectMulMemberAdapter.setOnItemClickListener(new SelectMulMemberAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultMemberBean bean, int position) {
                alreadyMemberAdapter.notifyDataSetChanged(selectMulMemberAdapter.getSelectedItem());
                if(mIntentResult.size() > 0){
                    rvTop.smoothScrollToPosition(mIntentResult.size() - 1);
                }
                if(selectMulMemberAdapter.getSelectedItem().size() == 0){
                    titleBar.setTvRight("确定");
                }else{
                    titleBar.setTvRight("确定(" + selectMulMemberAdapter.getSelectedItem().size() + ")");
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
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultMemberBean>() {

            @Override
            public void onSuccess(List<ResultMemberBean> result, int total, boolean isRefresh) {
                if (null != result) {
                    easyRefreshLayout.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                    if (isRefresh) {
                        selectMulMemberAdapter.notifyDataSetChanged(result);
                    } else {
                        selectMulMemberAdapter.addAll(result);
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
            public void onLoad(Subscriber<DataBean<PageInfo<ResultMemberBean>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getMemberListOfStore(body, LoginInfoPrefs.getInstance(mContext).getGroupId(), pageIndex, pageSize, subscriber);
            }
        });

    }

    public void save() {
       List<ResultMemberBean> list =  selectMulMemberAdapter.getSelectedItem();
        if(list.size() == 0){
            showToast("请选择会员");
            return;
        }
        EvenManager.sendEvent(new NotifyEvent(list, NotifyEvent.DATA));
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        EvenManager.unregister(this);
        super.onDestroy();
    }
}

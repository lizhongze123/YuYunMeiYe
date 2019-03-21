package cn.yuyun.yymy.ui.store.bill;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import com.ajguan.library.EasyRefreshLayout;
import com.example.http.Presenter;

import butterknife.BindView;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.result.ResultMessageTemplate;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.store.member.MessageTemplateAdapter;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author lzz
 * @desc 明细单据
 * @date 2018/5/29
 */
public class BillActivity extends BaseActivity {

    @BindView(R.id.rl1)
    RelativeLayout rl1;
    @BindView(R.id.rv1)
    RecyclerView rv1;
    @BindView(R.id.easylayout1)
    EasyRefreshLayout easyRefreshLayout1;
    @BindView(R.id.rl2)
    RelativeLayout rl2;
    @BindView(R.id.rv2)
    RecyclerView rv2;
    @BindView(R.id.easylayout2)
    EasyRefreshLayout easyRefreshLayout2;

    private MessageTemplateAdapter messageTemplateAdapter2;
    private Presenter<ResultMessageTemplate> presenter1;
    private Presenter<ResultMessageTemplate> presenter2;

    private StoreBean storeBean;

    public static Intent startBillActivity(Context context, StoreBean storeBean) {
        return new Intent(context, BillActivity.class).putExtra(EXTRA_DATA, storeBean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
//        initViews();
        initDatas();
    }

    private void initDatas() {
//        configurePresenter1();
//        configurePresenter2();
        presenter1.loadData(true);
        presenter2.loadData(true);
    }

//    private void initViews() {
//        titleBar.setTilte("群发信息");
//        storeBean = (StoreBean) getIntent().getSerializableExtra(EXTRA_DATA);
//
//        messageTemplateAdapter1 = new MessageTemplateAdapter(mContext);
//        rv1.setLayoutManager(new LinearLayoutManager(this));
//        easyRefreshLayout1.setEnablePullToRefresh(false);
//        easyRefreshLayout1.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
//            @Override
//            public void onLoadMore() {
//                presenter1.loadData(false);
//            }
//
//            @Override
//            public void onRefreshing() {
//                presenter1.loadData(true);
//            }
//        });
//        rv1.setAdapter(messageTemplateAdapter1);
//        easyRefreshLayout2.setEnablePullToRefresh(false);
//        messageTemplateAdapter2 = new MessageTemplateAdapter(mContext);
//        rv2.setLayoutManager(new LinearLayoutManager(this));
//        easyRefreshLayout2.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
//            @Override
//            public void onLoadMore() {
//                presenter2.loadData(false);
//            }
//
//            @Override
//            public void onRefreshing() {
//                presenter2.loadData(true);
//            }
//        });
//        rv2.setAdapter(messageTemplateAdapter2);
//
//        messageTemplateAdapter1.setOnItemClickListener(new MessageTemplateAdapter.OnMyItemClickListener() {
//            @Override
//            public void onItemClick(View view, ResultMessageTemplate bean, int position) {
//                etContent.setText("");
//                etContent.setText(bean.text);
//            }
//        });
//        messageTemplateAdapter2.setOnItemClickListener(new MessageTemplateAdapter.OnMyItemClickListener() {
//            @Override
//            public void onItemClick(View view, ResultMessageTemplate bean, int position) {
//                etContent.setText("");
//                etContent.setText(bean.text);
//            }
//        });
//
//
//        ((RadioButton)(findViewById(R.id.rb1))).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    rl1.setVisibility(View.VISIBLE);
//                    rl2.setVisibility(View.GONE);
//                }
//            }
//        });
//        ((RadioButton)(findViewById(R.id.rb2))).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    rl1.setVisibility(View.GONE);
//                    rl2.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//    }

//    private void configurePresenter1() {
//        presenter1 = new Presenter<>();
//        presenter1.setLoadDataListener(new Presenter.OnPresenterLoadListener<ResultMessageTemplate>() {
//
//            @Override
//            public void onSuccess(List<ResultMessageTemplate> result, int total, final boolean isRefresh) {
//                if (result != null) {
//                    List<ResultMessageTemplate> newResult = new ArrayList<>();
//                    for (int i = 0; i < result.size(); i++) {
//                        if(result.get(i).og_type == 2){
//                            newResult.add(result.get(i));
//                        }
//                    }
//                    messageTemplateAdapter1.addAll(newResult);
//                }
//            }
//
//            @Override
//            public void onFailed(boolean isRefresh) {
//                showToast("加载失败，请稍候重试");
//            }
//
//            @Override
//            public void onCompleted(boolean isRefresh) {
//                easyRefreshLayout1.loadMoreComplete();
//            }
//
//            @Override
//            public void onEmptyData() {
//            }
//
//            @Override
//            public void onNoneMoreData() {
//                showToast("没有更多数据了");
//                easyRefreshLayout1.setLoadMoreModel(LoadModel.NONE);
//            }
//
//            @Override
//            public void onLoad(Subscriber<PageInfo<ResultMessageTemplate>> subscriber, int pageIndex, int pageSize) {
//                AppModelFactory.model().getMsgTemplateWithGroup(LoginInfoPrefs.getInstance(mContext).getGroupId(), pageIndex, pageSize, subscriber);
//            }
//        });
//    }

//    private void configurePresenter2() {
//        presenter2 = new Presenter<>();
//        presenter2.setLoadDataListener(new Presenter.OnPresenterLoadListener<ResultMessageTemplate>() {
//
//            @Override
//            public void onSuccess(List<ResultMessageTemplate> result, int total, final boolean isRefresh) {
//                if (result != null) {
//                    messageTemplateAdapter2.addAll(result);
//                }
//            }
//
//            @Override
//            public void onFailed(boolean isRefresh) {
//                showToast("加载失败，请稍候重试");
//            }
//
//            @Override
//            public void onCompleted(boolean isRefresh) {
//                easyRefreshLayout2.loadMoreComplete();
//            }
//
//            @Override
//            public void onEmptyData() {
//            }
//
//            @Override
//            public void onNoneMoreData() {
//                showToast("没有更多数据了");
//                easyRefreshLayout2.setLoadMoreModel(LoadModel.NONE);
//            }
//
//            @Override
//            public void onLoad(Subscriber<PageInfo<ResultMessageTemplate>> subscriber, int pageIndex, int pageSize) {
//                AppModelFactory.model().getMsgTemplateWithStore(storeBean.spId, storeBean.ogType, LoginInfoPrefs.getInstance(mContext).getGroupId(), pageIndex, pageSize, subscriber);
//            }
//        });
//    }

}

package cn.yuyun.yymy.ui.store.member;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.PageInfo;
import com.example.http.Presenter2;
import com.example.http.ProgressSubscriber;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.constan.Constans;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestMemberFilter;
import cn.yuyun.yymy.http.request.RequestModifyMemberStatus;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;

/**
 * @author lzz
 * @desc 会员回收站
 * @date 2018/5/21
 */
public class RecycledMemberListActivity extends BaseActivity {

    private Presenter2<ResultMemberBean> presenter;

    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;
    private RecycledMemberAdapter recycledMemberAdapter;

    @BindView(R.id.recyclerView)
    SwipeMenuRecyclerView swipeRecyclerView;

    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.empty)
    EmptyLayout emptyLayout;

    private String groupId;
    private List<String> tempSpIdList;

    public static Intent startMemberAnalysisActivity(Context context, List<String> tempSpIdList , String groupId) {
        return new Intent(context, RecycledMemberListActivity.class).putExtra(EXTRA_DATA2, (Serializable) tempSpIdList).putExtra(EXTRA_DATA, groupId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycled_member_list);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        configurePresenter();
        initDatas();
    }

    private void initDatas() {
        body.status = -1;
        body.spIdList = tempSpIdList;
        easyRefreshLayout.autoRefresh();
    }

    private void initViews() {
        titleBar.setTilte("会员回收站");
        groupId = getIntent().getStringExtra(EXTRA_DATA);
        tempSpIdList = (List<String>) getIntent().getSerializableExtra(EXTRA_DATA2);
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(this));
        swipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRecyclerView.setHasFixedSize(true);
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

        recycledMemberAdapter = new RecycledMemberAdapter(mContext);
        // 创建菜单
        SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int viewType) {
                int height = ViewGroup.LayoutParams.MATCH_PARENT;
                int width = getResources().getDimensionPixelSize(R.dimen.swipe_item);
                SwipeMenuItem delItem = new SwipeMenuItem(mContext)
                        .setBackground(R.color.text_red)
                        .setText("删除")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                SwipeMenuItem recycleItem = new SwipeMenuItem(mContext)
                        .setBackground(R.color.text_green)
                        .setText("回收")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                // 添加菜单到左侧。
                rightMenu.addMenuItem(recycleItem);
                rightMenu.addMenuItem(delItem);
                // 注意：哪边不想要菜单，那么不要添加即可。
            }
        };
        if(Constans.isStoreLoginer){
            // 设置监听器。
            swipeRecyclerView.setSwipeMenuCreator(mSwipeMenuCreator);
            // 设置菜单Item点击监听。
            swipeRecyclerView.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
                @Override
                public void onItemClick(SwipeMenuBridge menuBridge) {
                    menuBridge.closeMenu();
                    // 左侧还是右侧菜单。
                    int direction = menuBridge.getDirection();
                    // RecyclerView的Item的position。
                    int adapterPosition = menuBridge.getAdapterPosition();
                    // 菜单在RecyclerView的Item中的Position。
                    int menuPosition = menuBridge.getPosition();
                    if(menuPosition == 0){
                        modifyMemberStatus(recycledMemberAdapter.getItemBean(adapterPosition));
                    }else{
                        del(recycledMemberAdapter.getItemBean(adapterPosition));
                    }
                }
            });
        }
        swipeRecyclerView.setAdapter(recycledMemberAdapter);
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(etInput.getText().toString())) {
                        body.search_text = etInput.getText().toString();
                    }else{
                        body.search_text = "";
                    }
                    easyRefreshLayout.autoRefresh();
                }
                return false;
            }
        });
    }

    private void del(ResultMemberBean bean) {
        AppModelFactory.model().delMember(bean.memberId, new ProgressSubscriber<Object>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(Object result) {
                showToast("已删除");
                presenter.loadData(true);
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.getMessage());
            }

        });
    }

    private void modifyMemberStatus(ResultMemberBean bean) {
        RequestModifyMemberStatus requestBean = new RequestModifyMemberStatus();
        requestBean.memberId = bean.memberId;
        requestBean.status = 1;
        AppModelFactory.model().modifyMemberStatus(requestBean, new ProgressSubscriber<Object>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(Object result) {
                showToast("已回收");
                presenter.loadData(true);
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.getMessage());
            }

        });
    }

    private RequestMemberFilter body = new RequestMemberFilter();
    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultMemberBean>() {

            @Override
            public void onSuccess(List<ResultMemberBean> result, int total, final boolean isRefresh) {
                easyRefreshLayout.setVisibility(View.VISIBLE);
                emptyLayout.setVisibility(View.GONE);
                emptyLayout.setErrorType(EmptyLayout.NODATA);
                if (result != null) {
                    if (isRefresh) {
                        recycledMemberAdapter.notifyDataSetChanged(result);
                    } else {
                        recycledMemberAdapter.addAll(result);
                    }
                }
            }

            @Override
            public void onFailed(boolean isRefresh) {
                recycledMemberAdapter.clear();
                showToast("加载失败，请稍候重试");
            }

            @Override
            public void onCompleted(boolean isRefresh) {
                easyRefreshLayout.refreshComplete();
                easyRefreshLayout.closeLoadView();
            }

            @Override
            public void onEmptyData() {
                recycledMemberAdapter.clear();
                easyRefreshLayout.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
                emptyLayout.setErrorType(EmptyLayout.NODATA);
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultMemberBean>>> subscriber, int pageIndex, int pageSize) {
                showContent();
                AppModelFactory.model().getMemberListOfStore(body, groupId, pageIndex, pageSize, subscriber);
            }
        });
    }

}

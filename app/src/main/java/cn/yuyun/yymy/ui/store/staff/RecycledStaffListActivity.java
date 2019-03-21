package cn.yuyun.yymy.ui.store.staff;

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
import com.ajguan.library.LoadModel;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.NoneProgressSubscriber;
import com.example.http.PageInfo;
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
import cn.yuyun.yymy.http.request.RequestModifyStaffStatus;
import cn.yuyun.yymy.http.request.RequestStoreStaff;
import cn.yuyun.yymy.http.result.StaffBean;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author
 * @desc 员工回收站 -- 门店
 * @date
 */
public class RecycledStaffListActivity extends BaseActivity {

    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;
    private RecycledStaffAdapter recycledStaffAdapter;

    @BindView(R.id.recyclerView)
    SwipeMenuRecyclerView swipeRecyclerView;
    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.empty)
    EmptyLayout emptyLayout;

    private String groupId;
    private int type;
    private List<String> tempSpIdList;

    public static Intent startRecycledStaffListActivityForAll(Context context, List<String> tempSpIdList, String groupId) {
        return new Intent(context, RecycledStaffListActivity.class).putExtra(EXTRA_DATA2, (Serializable) tempSpIdList).putExtra(EXTRA_DATA, groupId).putExtra(EXTRA_TYPE, 1);
    }

    public static Intent startRecycledStaffListActivityForHq(Context context, List<String> tempSpIdList, String groupId) {
        return new Intent(context, RecycledStaffListActivity.class).putExtra(EXTRA_DATA2, (Serializable) tempSpIdList).putExtra(EXTRA_DATA, groupId).putExtra(EXTRA_TYPE, 2);
    }

    public static Intent startRecycledStaffListActivityForStore(Context context, List<String> tempSpIdList, String groupId) {
        return new Intent(context, RecycledStaffListActivity.class).putExtra(EXTRA_DATA2, (Serializable) tempSpIdList).putExtra(EXTRA_DATA, groupId).putExtra(EXTRA_TYPE, 3);
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
        initDatas();
    }

    private void initDatas() {
        if(type == 1){
            requestBean.baseon_type = "";
        }else if(type == 2){
            requestBean.baseon_type = "1";
        }else{
            requestBean.baseon_type = "2";
        }
        requestBean.spIdList = tempSpIdList;
        easyRefreshLayout.autoRefresh();
    }

    private void initViews() {
        titleBar.setTilte("员工回收站");
        groupId = getIntent().getStringExtra(EXTRA_DATA);
        type = getIntent().getIntExtra(EXTRA_TYPE, 1);
        tempSpIdList = (List<String>) getIntent().getSerializableExtra(EXTRA_DATA2);
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(this));
        easyRefreshLayout.setLoadMoreModel(LoadModel.NONE);
        swipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRecyclerView.setHasFixedSize(true);
        easyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {

            }

            @Override
            public void onRefreshing() {
                getData();
            }
        });
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(etInput.getText().toString())) {
                        requestBean.searchText = etInput.getText().toString();
                    }else{
                        requestBean.searchText = "";
                    }
                    getData();
                }
                return false;
            }
        });
        recycledStaffAdapter = new RecycledStaffAdapter(mContext);
        if(Constans.isStoreLoginer){
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
                        modifyStaffStatus(recycledStaffAdapter.getItemBean(adapterPosition));
                    }else{
                        del(recycledStaffAdapter.getItemBean(adapterPosition));
                    }
                }
            });
        }
        swipeRecyclerView.setAdapter(recycledStaffAdapter);

    }

    private void del(StaffBean bean) {
        AppModelFactory.model().delStaff(bean.staffId, new ProgressSubscriber<Object>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(Object result) {
                showToast("已删除");
                getData();
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.getMessage());
            }

        });
    }

    private void modifyStaffStatus(StaffBean bean) {
        RequestModifyStaffStatus requestBean = new RequestModifyStaffStatus();
        requestBean.staffId = bean.staffId;
        requestBean.status = RequestStoreStaff.JobStatus.OUT;
        AppModelFactory.model().modifyStaffStatus(requestBean, new ProgressSubscriber<Object>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(Object result) {
                showToast("已回收");
                getData();
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.getMessage());
            }

        });
    }

    private RequestStoreStaff requestBean = new RequestStoreStaff();

    private void getData() {
        requestBean.status = RequestStoreStaff.JobStatus.RECYCLED;
        AppModelFactory.model().getStoreStaffList(groupId, 1, Integer.MAX_VALUE, requestBean, new NoneProgressSubscriber<DataBean<PageInfo<StaffBean>>>(mContext) {

            @Override
            public void onCompleted() {
                easyRefreshLayout.refreshComplete();
            }

            @Override
            public void onNext(DataBean<PageInfo<StaffBean>> result) {
                if(null != result){
                    if(result.data.dataLsit.size() == 0){
                        emptyLayout.setVisibility(View.VISIBLE);
                        emptyLayout.setErrorType(EmptyLayout.NODATA);
                        easyRefreshLayout.setVisibility(View.GONE);
                    }else{
                        emptyLayout.setVisibility(View.GONE);
                        easyRefreshLayout.setVisibility(View.VISIBLE);
                        recycledStaffAdapter.notifyDataSetChanged(result.data.dataLsit);
                    }
                }else{
                    emptyLayout.setVisibility(View.VISIBLE);
                    emptyLayout.setErrorType(EmptyLayout.NODATA);
                    easyRefreshLayout.setVisibility(View.GONE);
                }

            }
        });
    }

}

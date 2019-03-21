package cn.yuyun.yymy.ui.home.leave;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.example.http.ApiException;
import com.example.http.ProgressSubscriber;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.ResultGroupBean;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.home.member.storemember.StoreMemberListActivity;
import cn.yuyun.yymy.ui.store.staff.MultipleChoiceStaffActivity;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author lzz
 * @desc 门店列表
 * @date 2018/2/3
 */

public class StoreListActivity extends BaseActivity {

    private EasyRefreshLayout easyRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private StoreListAdapter mAdapter;

    public static String TYPE_ADD = "type_add";
    public static String TYPE_SELECT = "type_select";
    public static String TYPE_MEMBER = "type_member";
    public static String TYPE_CONTACTS= "type_contacts";
    public static String TYPE_MEMBER_DETAIL = "type_member_detail";

    /**添加考勤规则*/
    public static Intent startFromAddRules(Context context){
        return new Intent(context, StoreListActivity.class).putExtra(EXTRA_TYPE, TYPE_ADD);
    }

    /**员工通讯录*/
    public static Intent startFromContacts(Context context){
        return new Intent(context, StoreListActivity.class).putExtra(EXTRA_TYPE, TYPE_CONTACTS);
    }

    /**预约添加会员*/
    public static Intent startWithMember(Context context){
        return new Intent(context, StoreListActivity.class).putExtra(EXTRA_TYPE, TYPE_MEMBER);
    }

    /**选择门店*/
    public static Intent startWithSelect(Context context){
        return new Intent(context, StoreListActivity.class).putExtra(EXTRA_TYPE, TYPE_SELECT);
    }

    /**查看会员*/
    public static Intent startWithMemberDetail(Context context){
        return new Intent(context, StoreListActivity.class).putExtra(EXTRA_TYPE, TYPE_MEMBER_DETAIL);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);
        EvenManager.register(this);

    }

    @Override
    protected void setUpViewAndData() {
        initViews();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if(notifyEvent.tag == NotifyEvent.DATA){
            finish();
        }
    }

    private void initViews() {
        titleBar.setTilte("门店列表");
        easyRefreshLayout = (EasyRefreshLayout) findViewById(R.id.easylayout);
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(this));
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        mAdapter = new StoreListAdapter(this);
        final String type = getIntent().getStringExtra(EXTRA_TYPE);

        mAdapter.setOnItemClickListener(new StoreListAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, StoreBean bean, int position) {
                if(type != null && type.equals(TYPE_ADD)){
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_DATA, bean);
                    setResult(RESULT_OK, intent);
                    finish();
                } else if(type != null && type.equals(TYPE_MEMBER)){
                    //跳转会员列表界面
                    if(position != 0){
                        startActivity(StoreMemberListActivity.startMemberListActivity(mContext, bean.spId));
                    }
                } else if(type != null && type.equals(TYPE_SELECT)){
                    if(position != 0){
                        Intent intent = new Intent();
                        intent.putExtra(EXTRA_DATA, bean);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                } else if(type != null && type.equals(TYPE_CONTACTS)){
                    if(position == 0){
                        startActivity(StaffListActivity.startContactsListActivityFromGroupWithCheck(mContext));
                    }else{
                        startActivity(StaffListActivity.startContactsListActivityWithCheck(mContext, bean.spId));
                    }
                } else{
                    if(position == 0){
                        //总部多选员工
//                        startActivity(MultipleChoiceStaffActivity.startMultipleChoiceStaffActivityFromGroup(mContext));
                    }else{
                        //门店多选员工
//                        startActivity(MultipleChoiceStaffActivity.startMultipleChoiceStaffActivity(mContext, bean));
                    }
                }

            }
        });

        recyclerView.setAdapter(mAdapter);
        //only refresh
        easyRefreshLayout.setLoadMoreModel(LoadModel.NONE);
        easyRefreshLayout.autoRefresh();
        easyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {}

            @Override
            public void onRefreshing() {
                getGroupInfo();
            }
        });
    }

    private void getGroupInfo() {
        AppModelFactory.model().getGroupInfo(LoginInfoPrefs.getInstance(this).getGroupId(), new ProgressSubscriber<ResultGroupBean>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
                easyRefreshLayout.refreshComplete();
            }

            @Override
            public void onNext(ResultGroupBean result) {
                if(result != null){
                    getStoreList(result);
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.message);
            }

        });
    }

    private void getStoreList(final ResultGroupBean resultGroupBean) {
        AppModelFactory.model().getStoreListWithGroup(new ProgressSubscriber<List<StoreBean>>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
                easyRefreshLayout.refreshComplete();
            }

            @Override
            public void onNext(List<StoreBean> result) {
                if(result != null){
                    StoreBean storeBean = new StoreBean();
                    storeBean.thumb_url = resultGroupBean.thumb_url;
                    storeBean.addr = resultGroupBean.addr;
                    storeBean.chairman = resultGroupBean.charge_man;
                    storeBean.spName = resultGroupBean.name;
                    storeBean.chairmantel = resultGroupBean.charge_mobile;
                    storeBean.group_id = resultGroupBean.group_id;
                    storeBean.spId = resultGroupBean.group_id;
                    storeBean.ogType = 1;
                    result.add(0, storeBean);
                    mAdapter.notifyDataSetChanged(result);
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.message);
            }

        });
    }

}

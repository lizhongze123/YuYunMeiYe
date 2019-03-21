package cn.yuyun.yymy.ui.home.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestBindRule;
import cn.yuyun.yymy.http.request.RequestUnbindRule;
import cn.yuyun.yymy.http.result.ResultAttendanceRules;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;
import cn.yuyun.yymy.http.result.RulesBean;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.ui.home.leave.SelectStoreListActivity;
import cn.yuyun.yymy.ui.home.leave.StoreListActivity;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.WarnningDialog;
import cn.yuyun.yymy.view.dialog.OnBindDialog;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author 考勤规则Fragment
 * @desc
 * @date 2018/1/26
 */
public class AttendanceRuleActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;

    private LinearLayoutManager mLayoutManager;
    private AttendanceRuleAdapter parentAdapter;
    private WarnningDialog warnningDialog;
    private OnBindDialog onBindDialog;

    private RulesBean delTempBean;
    private int delTempPosition;
    private RequestUnbindRule requestUnbindRule;
    private RequestBindRule requestBindRule;
    private ResultClassfiyStoreBean.OgServiceplacesRspListBean storeBean;

    private List<RulesBean> parentList = new ArrayList<>();

    private final int REQUEST_CODE = 1114;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_attendance_rule);
        EvenManager.register(this);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if (notifyEvent.tag == NotifyEvent.RREFRESH) {
            easyRefreshLayout.autoRefresh();
        }
    }

    private void initViews() {
        titleBar.setTilte("考勤规则");
        titleBar.setRightIcon(R.drawable.icon_add_two);
        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, AddRulesActivity.class));
            }
        });
        warnningDialog = new WarnningDialog(mContext, "确定删除？");
        warnningDialog.setOnPositiveListener(new WarnningDialog.OnDialogListener() {
            @Override
            public void onPositive() {
                delRules();
                warnningDialog.dismiss();
            }

            @Override
            public void onNegative() {

            }
        });

        onBindDialog = new OnBindDialog(mContext);
        onBindDialog.setOnPositiveListener(new OnBindDialog.OnDialogListener() {
            @Override
            public void onPositive() {
                bindRules(1);
            }

            @Override
            public void onNegative() {

            }
        });

        mLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager);
        parentAdapter = new AttendanceRuleAdapter(mContext, parentList);
        parentAdapter.setOnItemClickListener(new AttendanceRuleAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, RulesBean bean, int position) {
                startActivityForResult(StoreListActivity.startFromAddRules(mContext), REQUEST_CODE);
            }

            @Override
            public void onDelItem(RulesBean bean, int position) {
                delTempBean = bean;
                delTempPosition = position;
                warnningDialog.show();
            }

            @Override
            public void onUnbind(RulesBean rulesBean, RulesBean.AttendanceOgListBean bean, int childPos, AttendanceRuleAdapter.ViewHolder holder) {
                if (TextUtils.isEmpty(LoginInfoPrefs.getInstance(mContext).getStaffId())) {
                    showTextDialog("该账号没有绑定员工", false);
                    return;
                }
                requestUnbindRule = new RequestUnbindRule();
                requestUnbindRule.og_id = bean.og_id;
                requestUnbindRule.og_type = bean.og_type;
                requestUnbindRule.rule_id = bean.rule_id;
                unbindRules(requestUnbindRule, childPos, holder);
            }

            @Override
            public void onAdd(RulesBean bean, int parentPos) {
                if (TextUtils.isEmpty(LoginInfoPrefs.getInstance(mContext).getStaffId())) {
                    showTextDialog("该账号没有绑定员工", false);
                    return;
                }
                requestBindRule = new RequestBindRule();
                requestBindRule.ruleId = bean.id;
                requestBindRule.createPerson = LoginInfoPrefs.getInstance(mContext).getStaffId();
                requestBindRule.groupId = LoginInfoPrefs.getInstance(mContext).getGroupId();
                startActivityForResult(SelectStoreListActivity.startTypeSelectStore(mContext), REQUEST_CODE);
            }

        });
        recyclerView.setAdapter(parentAdapter);
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(mContext));
        easyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
            }

            @Override
            public void onRefreshing() {
                getData();
            }
        });
        //only refresh
        easyRefreshLayout.setLoadMoreModel(LoadModel.NONE);
    }

    private void initData() {
        easyRefreshLayout.autoRefresh();
    }

    private void delRules() {
        AppModelFactory.model().delRules(delTempBean.id, new ProgressSubscriber<DataBean>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean result) {
                parentList.remove(delTempPosition);
                parentAdapter.notifyDataSetChanged();
                showToast("删除成功");
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast(ex.message);
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast("网络异常，请检查网络");
            }

        });
    }

    private void unbindRules(RequestUnbindRule requestUnbindRule, final int childPos, final AttendanceRuleAdapter.ViewHolder holder) {
        AppModelFactory.model().unbindRules(requestUnbindRule, new ProgressSubscriber<Object>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(Object result) {
                parentAdapter.unbindSuccess(holder, childPos);
                showToast("删除成功");
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast(ex.message);
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast("网络异常，请检查网络");
            }

        });
    }

    private void bindRules(int flag) {
        requestBindRule.autoUntieFlag = flag;
        AppModelFactory.model().bindRules(requestBindRule, new ProgressSubscriber<DataBean>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean result) {
                showToast("添加成功");
                easyRefreshLayout.autoRefresh();
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                if(ex.code == 20300){
                    onBindDialog.setPositiveText("继续绑定");
                    onBindDialog.setTips(ex.message);
                    onBindDialog.show();
                }else{
                    showToast(ex.message);
                }
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast("网络异常，请检查网络");
            }

        });
    }

    /**
     * 查询所有考勤模版
     */
    private void getData() {
        AppModelFactory.model().getAllRules(LoginInfoPrefs.getInstance(mContext).getGroupId(), new ProgressSubscriber<DataBean<ResultAttendanceRules>>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
                easyRefreshLayout.refreshComplete();
            }

            @Override
            public void onNext(DataBean<ResultAttendanceRules> result) {
                if (null != result.data) {
                    if(null != result.data.appAttendanceRulesVoList){
                        parentList.clear();
                        parentList.addAll(result.data.appAttendanceRulesVoList);
                        parentAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast(ex.message);
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast("网络异常，请检查网络");
            }

        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (null != data) {
                storeBean = (ResultClassfiyStoreBean.OgServiceplacesRspListBean) data.getSerializableExtra(EXTRA_DATA);
                RequestBindRule.OgRule ruleStore = new RequestBindRule.OgRule();
                ruleStore.ogId = storeBean.sp_id;
                ruleStore.ogName = storeBean.sp_name;
                ruleStore.ogType = storeBean.ogType;
                List<RequestBindRule.OgRule> list = new ArrayList<>();
                list.add(ruleStore);
                requestBindRule.ogRuleList = list;
                bindRules(0);
            }
        }
    }
}

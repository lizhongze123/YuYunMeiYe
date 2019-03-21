package cn.yuyun.yymy.ui.home.member;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

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
import cn.yuyun.yymy.constan.Constans;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestBindMember;
import cn.yuyun.yymy.http.result.ResultListStaff;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.http.result.StaffBean;
import cn.yuyun.yymy.http.result.WarnningMemberBean;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.home.leave.SelectStoreListActivity;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.WarnningDialog;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;

/**
 * @author
 * @desc TA的会员
 * @date
 */

public class TaCustomerActivity extends BaseActivity {

    private TaRecommendMemberAdapter mAdapter;
    private List<ResultMemberBean> intentList = new ArrayList<>();
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private StaffBean staffBean;
    private StoreBean storeBean;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.empty)
    EmptyLayout empty;
    private WarnningDialog warnningDialog;
    private WarnningMemberBean clickBean;
    private int clickPos;

    public static Intent startTaRecommendActivity(Context context, StaffBean staffBean, StoreBean storeBean) {
        return new Intent(context, TaCustomerActivity.class).putExtra(EXTRA_DATA, staffBean).putExtra(EXTRA_DATA2, storeBean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EvenManager.register(this);
        setContentView(R.layout.activity_ta_member_list);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if (notifyEvent.tag == NotifyEvent.DATA) {
            intentList = (List<ResultMemberBean>) notifyEvent.value;
            RequestBindMember body = new RequestBindMember();
            List<RequestBindMember.StaffMgrMemberQoListBean> list = new ArrayList<>();
            for (int i = 0; i < intentList.size(); i++) {
                RequestBindMember.StaffMgrMemberQoListBean b = new RequestBindMember.StaffMgrMemberQoListBean();
                b.member_id = intentList.get(i).memberId;
                b.staff_id = staffBean.staffId;
                list.add(b);
            }
            body.staffMgrMemberQoList = list;
            bindMember(body);
        }
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        getAccount();
    }

    private void initViews() {
        staffBean = (StaffBean) getIntent().getSerializableExtra(EXTRA_DATA);
        storeBean = (StoreBean) getIntent().getSerializableExtra(EXTRA_DATA2);
        titleBar.setTilte(staffBean.staffName + "的会员");
        titleBar.setRightIcon(R.drawable.icon_edit);
        if(Constans.isStoreLoginer){
            titleBar.setRightOnClicker(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDelVisiable(mAdapter.getDelVisiable());
                }
            });
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TaRecommendMemberAdapter(mContext);
        mAdapter.setOnItemClickListener(new TaRecommendMemberAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, WarnningMemberBean bean, int position) {

            }

            @Override
            public void del(WarnningMemberBean bean, int position) {
                clickBean = bean;
                clickPos = position;
                warnningDialog.show();
            }
        });
        recyclerView.setAdapter(mAdapter);
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SelectStoreListActivity.startTypeSelectMember(mContext,intentList));
            }
        });
        warnningDialog = new WarnningDialog(mContext, "确定解绑？");
        warnningDialog.setOnPositiveListener(new WarnningDialog.OnDialogListener() {
            @Override
            public void onPositive() {
                unbindMember();
                warnningDialog.dismiss();
            }

            @Override
            public void onNegative() {

            }
        });
    }

    private void setDelVisiable(boolean isVisiable) {
        if(isVisiable){
            mAdapter.setDelVisiable(false);
            titleBar.setRightIcon(R.drawable.icon_edit);
            titleBar.setTvRight("");
            tvSubmit.setVisibility(View.GONE);
        }else{
            mAdapter.setDelVisiable(true);
            titleBar.setTvRight("完成");
            titleBar.setRightIcon(0);
            tvSubmit.setVisibility(View.VISIBLE);
        }
    }

    private void bindMember(RequestBindMember body) {
        AppModelFactory.model().bindMember(body, new ProgressSubscriber<Object>(mContext) {
            @Override
            public void onNext(Object result) {
                showToast("关联成功");
                if(null != intentList){
                    intentList.clear();
                }
                getAccount();
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast("关联失败");
            }
        });
    }

    private void unbindMember() {
       AppModelFactory.model().unbindMember(clickBean.id, new ProgressSubscriber<Object>(mContext) {
            @Override
            public void onNext(Object result) {
                showToast("解绑成功");
                mAdapter.remove(clickPos);
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast("解绑失败");
            }
        });
    }

    private void getAccount() {
        AppModelFactory.model().getMemberListFromAccount(storeBean.group_id, staffBean.staffId, new ProgressSubscriber<DataBean<List<WarnningMemberBean>>>(mContext) {
            @Override
            public void onNext(DataBean<List<WarnningMemberBean>> result) {
                if (null != result) {
                    if (result.data.size() > 0) {
                        empty.setVisibility(View.GONE);
                        mAdapter.notifyDataSetChanged(result.data);
                    } else {
                        empty.setVisibility(View.VISIBLE);
                        empty.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
                    }
                } else {
                    empty.setVisibility(View.VISIBLE);
                    empty.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        EvenManager.unregister(this);
        super.onDestroy();
    }
}

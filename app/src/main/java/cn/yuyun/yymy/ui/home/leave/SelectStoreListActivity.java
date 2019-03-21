package cn.yuyun.yymy.ui.home.leave;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.lzz.utils.LogUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;
import cn.yuyun.yymy.http.result.ResultListStaff;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.StorePeoplePrefs;
import cn.yuyun.yymy.ui.home.member.pmember.PublicMemberListActivity;
import cn.yuyun.yymy.ui.home.cashier.SelectCashierMemberActivity;
import cn.yuyun.yymy.ui.home.member.storemember.StoreMemberListActivity;
import cn.yuyun.yymy.ui.store.member.MultipleChoiceMemberActivity;
import cn.yuyun.yymy.ui.store.staff.MultipleChoiceStaffActivity;
import cn.yuyun.yymy.utils.EvenManager;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author
 * @desc 门店列表
 * @date
 */

public class SelectStoreListActivity extends BaseActivity {

    @BindView(R.id.rv_left)
    RecyclerView rvLeft;

    @BindView(R.id.rv_right)
    RecyclerView rvRight;

    @BindView(R.id.et_inputStore)
    EditText etInputStore;

    private SelectStoreListRightAdapter rightAdapter;
    private SelectStoreListLeftAdapter leftAdapter;

    private int REQUEST_CODE_STAFF = 10001;
    private int REQUEST_CODE_MEMBER = 10002;
    private List<ResultListStaff> intentDataStaff;
    private List<ResultMemberBean> intentDataMember;

    private String type;
    public static String TYPE_SELECT_MEMBER_MUL = "type_select_member_mul";
    public static String TYPE_SELECT_STAFF = "type_select_staff";
    public static String TYPE_CHECK_MEMBER = "type_check_member";
    public static String TYPE_SELECT_STORE = "type_select_store";
    public static String TYPE_SELECT_MEMBER = "type_select_member";
    public static String TYPE_SELECT_CASHIER_MEMBER = "type_select_cashier_member";
    public static String TYPE_SELECT_STORE_ADD_MEMBER = "type_select_store_add_member";

    private List<ResultClassfiyStoreBean> cacheData;

    public static Intent startTypeSelectStaff(Context context, List<ResultListStaff> bean) {
        return new Intent(context, SelectStoreListActivity.class).putExtra(EXTRA_DATA, (Serializable) bean).putExtra(EXTRA_TYPE, TYPE_SELECT_STAFF);
    }

    public static Intent startTypeSelectMember(Context context, List<ResultMemberBean> bean) {
        return new Intent(context, SelectStoreListActivity.class).putExtra(EXTRA_DATA, (Serializable) bean).putExtra(EXTRA_TYPE, TYPE_SELECT_MEMBER_MUL);
    }

    public static Intent startTypeCheckMember(Context context) {
        return new Intent(context, SelectStoreListActivity.class).putExtra(EXTRA_TYPE, TYPE_CHECK_MEMBER);
    }

    public static Intent startTypeSelectMember(Context context) {
        return new Intent(context, SelectStoreListActivity.class).putExtra(EXTRA_TYPE, TYPE_SELECT_MEMBER);
    }

    public static Intent startTypeSelectCashierMember(Context context) {
        return new Intent(context, SelectStoreListActivity.class).putExtra(EXTRA_TYPE, TYPE_SELECT_CASHIER_MEMBER);
    }

    public static Intent startTypeSelectStore(Context context) {
        return new Intent(context, SelectStoreListActivity.class).putExtra(EXTRA_TYPE, TYPE_SELECT_STORE);
    }

    public static Intent startTypeSelectStoreFromAddMember(Context context) {
        return new Intent(context, SelectStoreListActivity.class).putExtra(EXTRA_TYPE, TYPE_SELECT_STORE_ADD_MEMBER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_store);
        EvenManager.register(this);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    private void initDatas() {
        String json = StorePeoplePrefs.getInstance(mContext).getJson();
        if (!TextUtils.isEmpty(json)) {
            Gson gson = new Gson();
            Type t = new TypeToken<List<ResultClassfiyStoreBean>>() {}.getType();
            cacheData = gson.fromJson(json, t);

            if (type.equals(TYPE_SELECT_STORE_ADD_MEMBER) || type.equals(TYPE_SELECT_MEMBER)  || type.equals(TYPE_SELECT_CASHIER_MEMBER) || type.equals(TYPE_SELECT_MEMBER_MUL)) {
                //会员都不用总部
            }else{
                ResultClassfiyStoreBean bean = new ResultClassfiyStoreBean();
                bean.name = "总部";
                bean.group_id = LoginInfoPrefs.getInstance(mContext).getGroupId();
                ResultClassfiyStoreBean.OgServiceplacesRspListBean bean1 = new ResultClassfiyStoreBean.OgServiceplacesRspListBean();
                bean1.sp_id = LoginInfoPrefs.getInstance(mContext).getGroupId();
                bean1.group_id = LoginInfoPrefs.getInstance(mContext).getGroupId();
                bean1.sp_name = "总部";
                bean1.ogType = 1;
                bean1.status = 1;
                List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> ogServiceplacesRspList = new ArrayList<>();
                ogServiceplacesRspList.add(bean1);
                bean.ogServiceplacesRspList = ogServiceplacesRspList;
                cacheData.add(0, bean);
            }

            leftAdapter.notifyDataSetChanged(cacheData);
            rightAdapter.notifyDataSetChanged(cacheData);
            classfiyStoreList.addAll(cacheData);
            LogUtils.e("我是读取缓存数据");
        }else {
            getClassifyStoreList();
            LogUtils.e("我是请求网络数据");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if (notifyEvent.tag == NotifyEvent.DATA) {
            finish();
        }else if(notifyEvent.tag == NotifyEvent.FINISH_CASHIER){
            finish();
        }
    }

    private void initViews() {
        titleBar.setTilte("门店列表");
        intentDataStaff = (List<ResultListStaff>) getIntent().getSerializableExtra(EXTRA_DATA);
        intentDataMember = (List<ResultMemberBean>) getIntent().getSerializableExtra(EXTRA_DATA);
        type = getIntent().getStringExtra(EXTRA_TYPE);
        rvLeft.setLayoutManager(new LinearLayoutManager(this));
        rvRight.setLayoutManager(new LinearLayoutManager(this));
        rightAdapter = new SelectStoreListRightAdapter(this);
        leftAdapter = new SelectStoreListLeftAdapter(this);
        rvLeft.setAdapter(leftAdapter);
        rvRight.setAdapter(rightAdapter);
        leftAdapter.setOnItemClickListener(new SelectStoreListLeftAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultClassfiyStoreBean bean, int position) {
                rightAdapter.setIndex(position);
                leftAdapter.setCheckedPosition(position);
            }
        });

        rightAdapter.setOnItemClickListener(new SelectStoreListRightAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultClassfiyStoreBean.OgServiceplacesRspListBean bean, int position) {

                if (type.equals(TYPE_SELECT_STAFF)) {
                    if (null != intentDataStaff) {
                        Intent intent;

                        if(bean.ogType == 1){
                            //总部
                            intent = MultipleChoiceStaffActivity.startMultipleChoiceStaffActivityFromGroup(mContext, bean);
                        }else{
                            intent = MultipleChoiceStaffActivity.startMultipleChoiceStaffActivity(mContext, bean);
                        }
                        intent.putExtra(EXTRA_DATA, (Serializable) intentDataStaff);
                        startActivityForResult(intent, REQUEST_CODE_STAFF);

                        /*if(leftIndex == 0){
                            intent = MultipleChoiceStaffActivity.startMultipleChoiceStaffActivityFromGroup(mContext, bean);
                        }else{
                            intent = MultipleChoiceStaffActivity.startMultipleChoiceStaffActivity(mContext, bean);
                        }
                        intent.putExtra(EXTRA_DATA, (Serializable) intentData);
                        startActivityForResult(intent, REQUEST_CODE);*/
                    } else {
                        if(bean.ogType == 1){
                            //总部
                            startActivityForResult(MultipleChoiceStaffActivity.startMultipleChoiceStaffActivityFromGroup(mContext, bean), REQUEST_CODE_STAFF);
                        }else{
                            startActivityForResult(MultipleChoiceStaffActivity.startMultipleChoiceStaffActivity(mContext, bean), REQUEST_CODE_STAFF);
                        }
                       /* if(leftIndex == 0){
                            startActivityForResult(MultipleChoiceStaffActivity.startMultipleChoiceStaffActivityFromGroup(mContext, bean), REQUEST_CODE);
                        }else{
                            startActivityForResult(MultipleChoiceStaffActivity.startMultipleChoiceStaffActivity(mContext, bean), REQUEST_CODE);
                        }*/
                    }
                } else if (type.equals(TYPE_CHECK_MEMBER)) {
                    startActivity(new Intent(mContext, PublicMemberListActivity.class));
                } else if (type.equals(TYPE_SELECT_STORE)) {
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_DATA, bean);
                    setResult(RESULT_OK, intent);
                    finish();
                } else if (type.equals(TYPE_SELECT_STORE_ADD_MEMBER)) {
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_DATA, bean);
                    setResult(RESULT_OK, intent);
                    finish();
                }else if (type.equals(TYPE_SELECT_MEMBER)) {
                    startActivity(StoreMemberListActivity.startMemberListActivity(mContext, bean.sp_id));
                }else if(type.equals(TYPE_SELECT_CASHIER_MEMBER)){
                    startActivity(SelectCashierMemberActivity.startSelectCashierMemberActivity(mContext, bean.sp_id));
                }else if(type.equals(TYPE_SELECT_MEMBER_MUL)){
                    if (null != intentDataMember) {
                        Intent intent = MultipleChoiceMemberActivity.startMultipleChoiceMemberActivity(mContext, bean);
                        intent.putExtra(EXTRA_DATA, (Serializable) intentDataMember);
                        startActivityForResult(intent, REQUEST_CODE_MEMBER);
                    }else{
                        startActivityForResult(MultipleChoiceMemberActivity.startMultipleChoiceMemberActivity(mContext, bean), REQUEST_CODE_MEMBER);
                    }
                }
            }
        });

        etInputStore.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() <= 0) {
                    leftAdapter.notifyDataSetChanged(classfiyStoreList);
                    rightAdapter.notifyDataSetChanged(classfiyStoreList);
                }else {
                    //本地搜索
                    localSearch(s.toString());
                }

            }
        });
    }

    /**门店数据*/
    private List<ResultClassfiyStoreBean> classfiyStoreList = new ArrayList<>();

    private void localSearch(String text) {
        //返回数据
        List<ResultClassfiyStoreBean> filterclassfiyStoreList = new ArrayList<>();
        for (ResultClassfiyStoreBean bean : classfiyStoreList) {
            ResultClassfiyStoreBean bean1 = new ResultClassfiyStoreBean();
            bean1.name = bean.name;
            bean1.group_id = bean.group_id;
            bean1.id = bean.id;
            List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> ogList = new ArrayList<>();
            for (ResultClassfiyStoreBean.OgServiceplacesRspListBean ogServiceplacesRspListBean : bean.ogServiceplacesRspList) {
                if(ogServiceplacesRspListBean.sp_name.indexOf(text) >= 0){
                    ogList.add(ogServiceplacesRspListBean);
                }
            }
            if(ogList.size() > 0){
                bean1.ogServiceplacesRspList = ogList;
                filterclassfiyStoreList.add(bean1);
            }
        }

        leftAdapter.notifyDataSetChanged(filterclassfiyStoreList);
        rightAdapter.notifyDataSetChanged(filterclassfiyStoreList);
    }

    private void getClassifyStoreList() {
        AppModelFactory.model().getClassifyStoreList(LoginInfoPrefs.getInstance(this).getGroupId(), new ProgressSubscriber<DataBean<List<ResultClassfiyStoreBean>>>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean<List<ResultClassfiyStoreBean>> result) {
                if (null != result) {
                    if (null != result.data) {

                        //缓存门店列表数据
                        Gson gson = new Gson();
                        String json = gson.toJson(result.data);
                        StorePeoplePrefs.getInstance(mContext).saveInfo(json);

                        if (type.equals(TYPE_SELECT_STORE_ADD_MEMBER) || type.equals(TYPE_SELECT_MEMBER) || type.equals(TYPE_SELECT_CASHIER_MEMBER)) {

                        }else{
                            ResultClassfiyStoreBean bean = new ResultClassfiyStoreBean();
                            bean.name = "总部";
                            bean.group_id = LoginInfoPrefs.getInstance(mContext).getGroupId();
                            ResultClassfiyStoreBean.OgServiceplacesRspListBean bean1 = new ResultClassfiyStoreBean.OgServiceplacesRspListBean();
                            bean1.sp_id = LoginInfoPrefs.getInstance(mContext).getGroupId();
                            bean1.group_id = LoginInfoPrefs.getInstance(mContext).getGroupId();
                            bean1.sp_name = "总部";
                            bean1.ogType = 1;
                            bean1.status = 1;
                            List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> ogServiceplacesRspList = new ArrayList<>();
                            ogServiceplacesRspList.add(bean1);
                            bean.ogServiceplacesRspList = ogServiceplacesRspList;
                            result.data.add(0, bean);
                        }

                        leftAdapter.notifyDataSetChanged(result.data);
                        rightAdapter.notifyDataSetChanged(result.data);
                        classfiyStoreList.addAll(result.data);

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
                showToast(mContext.getString(R.string.error_no_network));
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_STAFF) {
            intentDataStaff = (List<ResultListStaff>) data.getSerializableExtra(EXTRA_DATA);
        }else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_MEMBER) {
            intentDataMember = (List<ResultMemberBean>) data.getSerializableExtra(EXTRA_DATA);
        }
    }

}
